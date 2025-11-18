# TucasDesk

<p align="center">
  <img src="apps/frontend/public/tucas-icon-nobg.png" alt="TucasDesk Logo" width="120">
</p>

TucasDesk is an open-source helpdesk platform that centralizes customer service and makes it easier to track tickets. It is ideal for teams that need to organize requests, prioritize demands, and keep support under control.

## Overview

TucasDesk offers a complete experience for end users, technicians, and administrators. It allows you to register, track, and close tickets in just a few clicks, ensuring transparency throughout the entire support cycle.

## System Architecture

TucasDesk's architecture was designed to be scalable, decoupled, and resilient, combining synchronous and asynchronous services to optimize both user experience and operational efficiency.

The diagram below outlines the main components and the flow of communication between them:

![TucasDesk Architecture](docs/images/architecture-diagram.jpeg)

### Main Components

1. **Frontend/Client (React + TypeScript):**
   * Web interface where users (customers or technicians) interact with the system.
   * Responsible for consuming API endpoints to create, view, and manage tickets and interactions.
   * Performs authentication validation with the API, which delegates verification to AWS Cognito.

2. **API and Synchronous Services (Spring Boot):**
   * **Endpoint-Tickets:** receives requests to create (`TicketCreated`), close (`TicketClosed`), or interact (`TicketInteracted`) with a ticket.
   * **Service-Tickets and Service-Interactions:** contain the core business logic. They orchestrate CRUD operations on the database and publish events for notifications.
   * **Publishes Events:** when creating, closing, or adding an interaction to a ticket, the API publishes events (e.g., `TicketCreated`, `TicketClosed`) to an AWS SNS topic. This approach decouples the API from the notification logic.

3. **Middleware and Asynchronous Services (AWS):**
   * **AWS SNS (Simple Notification Service):** acts as an event distribution topic. The API publishes messages to SNS, which forwards them to all subscribed SQS queues.
   * **AWS SQS (Simple Queue Service):** queue that receives events from SNS. The `Service: Notifier` consumes messages from this queue to process them asynchronously. This ensures that even if the notification service fails, the message is not lost.
   * **Service: Notifier:** service that processes messages from the SQS queue. It formats and sends emails using AWS SES.

4. **Persistence (MariaDB):**
   * Relational database where all ticket, user, and interaction data is stored. CRUD operations are executed by the Spring Boot API.

5. **External APIs:**
   * **AWS Cognito:** AWS identity management service. The API uses it to validate authentication tokens (`JWT`) sent by the frontend, ensuring that only authenticated users can access resources.
   * **AWS SES (Simple Email Service):** AWS email service. `Service: Notifier` uses it to send notification emails when important events occur (e.g., confirming that a ticket was opened).

### Flow of a New Ticket

1. The user creates a new ticket in the **Frontend**.
2. The **Frontend** sends a request to the **Endpoint-Ticket** in the Spring Boot API.
3. **Service-Tickets** processes the request, saves the data in **MariaDB** (CRUD operation), and publishes a `TicketCreated` event to the **AWS SNS** topic.
4. **AWS SNS** distributes the event to the **AWS SQS** queue.
5. **Service: Notifier** consumes the message from the SQS queue.
6. **Service: Notifier** uses **AWS SES** to send a notification email to the user.
7. The **Frontend** receives the API response and updates the interface for the user.

This design ensures that the main API remains fast and responsive, while slower tasks such as sending emails are executed reliably in the background.

### Essential Project Structure

- `apps/backend/`: Spring Boot API responsible for business rules and database integrations.
- `apps/frontend/`: React + TypeScript web interface with reusable components and protected navigation.
- `infra/docker/`: Docker Compose files, including `compose.yaml` and database bootstrap scripts.
- `config/env/`: shared environment variables used during orchestration.

## Technologies Used

- **Java 21 + Spring Boot 3:** language and framework chosen to deliver a robust, secure, and easy-to-maintain API.
- **Spring Data JPA:** abstracts database access, speeding up queries and entity persistence.
- **Spring Security + AWS Cognito:** integrates managed authentication with support for MFA, password recovery, and token rotation.
- **MariaDB:** primary relational database to store tickets, users, and configurations with high performance and reliability.
- **React 19 + TypeScript:** modern, typed, and reactive interface that improves user experience and team productivity.
- **Vite:** build tool and dev server that accelerates frontend development.
- **Axios:** HTTP client that simplifies communication between frontend and backend.
- **Docker & Docker Compose:** standardize the environment, allowing the entire stack to be started with a single command.

## Getting Started

### Prerequisites

Install the tools below before you begin:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Starting Everything with Docker Compose (recommended)

1. Clone the repository and access the project folder:

   ```sh
   git clone https://github.com/felipebehling/tucasdesk.git
   cd tucasdesk
   ```

2. Create the shared `.env` file under `config/env/` with the required values. Start by copying the example file and then adjust each variable according to the desired environment:

   ```sh
   cp config/env/.env.example config/env/.env
   ```

   You can use `config/env/.env.example` as the basis for backend secrets and complement it with the variables required by Docker Compose:

   | Variable | Description |
   | --- | --- |
   | `DB_HOST` | Hostname used by the services to connect to the database (usually `database`). |
   | `DB_PORT` | Internal port exposed by the database (default `3306`). |
   | `DB_ROOT_PASSWORD` | Password for the database administrator user. |
   | `DB_USER` | Application user that will be created on startup. |
   | `DB_PASSWORD` | Password for the application user. |
   | `DB_NAME` | Name of the database used by the application. |
   | `DATABASE_URL` | Connection URL in the format expected by the backend (for example, `mariadb://user:password@database:3306/tucasdesk`). |
   | `SPRING_DATASOURCE_URL` | JDBC URL used by the API (for example, `jdbc:mariadb://database:3306/tucasdesk`). |
   | `SPRING_DATASOURCE_USERNAME` | Spring's JDBC user. |
   | `SPRING_DATASOURCE_PASSWORD` | Spring's JDBC password. |
   | `SPRING_ACTIVE_DATABASE_PROFILE` | Complementary profile to adjust Spring configuration (keep `mariadb`, the supported profile today). |
   | `VITE_API_URL` | Internal URL used by the frontend to call the API. |
   | `AWS_COGNITO_REGION` | AWS region where the User Pool is provisioned. |
   | `AWS_COGNITO_USER_POOL_ID` | Identifier of the User Pool used by the application. |
   | `AWS_COGNITO_APP_CLIENT_ID` | ID of the App Client configured in Cognito. |
   | `AWS_COGNITO_ISSUER_URI` | (Optional) Public Issuer URI of the User Pool. |
   | `AWS_COGNITO_JWK_SET_URI` | (Optional) JWKS endpoint. If not provided, it is derived from the issuer. |

   > 💡 Use MariaDB-compatible values as the default (for example, `mariadb://` and `jdbc:mariadb://` URLs). If you choose another engine, adjust the variables manually for the corresponding driver.

3. Generate the frontend environment variables file by copying the default template:

   ```sh
   cp apps/frontend/.env.example apps/frontend/.env
   ```

   The `VITE_API_URL` value must point to the URL where the backend will be accessible (by default, `http://tucasdesk-backend:8080` inside the containers or `http://localhost:8080` for local execution).

4. Start the services from the repository root:

   ```sh
   docker compose --env-file config/env/.env -f infra/docker/compose.yaml up --build
   ```

   The command above starts the frontend, backend, and a ready-to-use MariaDB 12.0 database. Use `docker compose --env-file config/env/.env -f infra/docker/compose.yaml down` to stop and remove the containers when you finish testing.

Available services:

| Service | Port | Notes |
| --- | --- | --- |
| Frontend (Nginx) | `3000` | TucasDesk web interface. |
| Backend (Spring Boot) | `8080` | Application REST API. |
| Database (MariaDB) | `3307` → `3306` | MariaDB 12.0 with credentials configured via `config/env/.env`. |

Suggested default credentials summary:

| Profile | Image | Exposed port (host → container) | Root user | Root password | Application user | Application password | Note |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `mariadb` (default) | `mariadb:12.0` | `3307` → `3306` | `root` | `DB_ROOT_PASSWORD` | `DB_USER` | `DB_PASSWORD` | Recommended profile and enabled by default. |

> ℹ️ The Compose setup is ready for MariaDB 12.0 and no longer provides MySQL profiles. If you need to use another engine, manually adjust the Docker Compose configuration and the backend.

> 📌 Docker Compose is the main path to run the full stack. Local execution (without containers) is optional and is detailed in the next section for those who need to customize or debug services individually.

### Provisioning the Cognito User Pool

The [`infra/aws`](infra/aws) directory includes the CloudFormation template [`cognito-user-pool.yaml`](infra/aws/cognito-user-pool.yaml), which creates the User Pool, the app client with the `USER_SRP_AUTH`, `ALLOW_REFRESH_TOKEN_AUTH`, and OAuth (Code + Implicit) flows enabled, as well as the default groups (`Administrador`, `Técnico`, and `Usuário`). To deploy it in an AWS account, run:

```sh
aws cloudformation deploy \
  --template-file infra/aws/cognito-user-pool.yaml \
  --stack-name tucasdesk-cognito \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
  --parameter-overrides ProjectName=tucasdesk DomainPrefix=tucasdesk-helpdesk
```

After creation, copy the outputs `UserPoolId`, `UserPoolClientId`, and, if you configured `DomainPrefix`, also `UserPoolDomainUrl` to the variables `AWS_COGNITO_USER_POOL_ID`, `AWS_COGNITO_APP_CLIENT_ID`, `AWS_COGNITO_ISSUER_URI` (or `AWS_COGNITO_JWK_SET_URI`), and `AWS_COGNITO_DOMAIN`. Adjust the `CallbackUrls` and `LogoutUrls` parameters according to the frontend domains to ensure the Hosted UI accepts the configured OAuth flow.

### Enabling the Refresh Token Flow

For the `POST /auth/refresh` endpoint to work correctly, configure the Cognito App Client with session renewal support. Ensure the following points in the User Pool used by TucasDesk:

1. **`ALLOW_REFRESH_TOKEN_AUTH` flow enabled** – in the App Client configuration, check *Enable username password auth for admin APIs for authentication (ALLOW_ADMIN_USER_PASSWORD_AUTH, ALLOW_REFRESH_TOKEN_AUTH)* or update the CloudFormation template to include the flow.
2. **Refresh Token validity** – set the expiration time according to your organization's security policy (suggested default: 30 days). The frontend uses the stored refresh token to renew the `idToken`/`accessToken` before it expires.
3. **Environment variables** – set the backend values `AWS_COGNITO_REGION`, `AWS_COGNITO_USER_POOL_ID`, `AWS_COGNITO_APP_CLIENT_ID`, `AWS_COGNITO_ISSUER_URI` (optional), and `AWS_COGNITO_JWK_SET_URI` (optional). The frontend consumes the `/auth/refresh` endpoint automatically when it receives `401` from the backend, so keep the URLs (`VITE_API_URL`) pointing to the correct instance.

With this configuration, the backend can exchange the refresh token for new `idToken`/`accessToken`, keeping the user session aligned between the frontend and Cognito.

### Running the Backend Locally (optional)

```sh
docker compose --env-file config/env/.env -f infra/docker/compose.yaml up -d db
cd apps/backend
./mvnw spring-boot:run
```

The API will be available at [http://localhost:8080](http://localhost:8080) and uses the database configured in `src/main/resources/application.properties`.

### Backend Environment Variables

The backend reads sensitive settings from environment variables. All of them have defaults aimed at local development and can be overridden depending on the environment.

| Variable | Description | Default value |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | Database JDBC URL. | `jdbc:mariadb://localhost:3307/tucasdesk?useSSL=true&serverTimezone=UTC` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | JDBC driver used by Spring. | `org.mariadb.jdbc.Driver` |
| `SPRING_DATASOURCE_USERNAME` | Database user. | `user` |
| `SPRING_DATASOURCE_PASSWORD` | Database password. | `password` |
| `SPRING_JPA_DATABASE_PLATFORM` | Hibernate dialect used by JPA. | `org.hibernate.dialect.MariaDBDialect` |
| `APP_CORS_ALLOWED_ORIGINS` | List of origins allowed for CORS (comma-separated). | `http://localhost:5173,http://localhost:3000` (in the `docker` profile, the default is `http://localhost:3000`) |
| `SPRING_PROFILES_ACTIVE` | Active Spring Boot profiles. Use `docker` when running via Compose. | *(no default)* |
| `SPRING_ACTIVE_DATABASE_PROFILE` | Complement to the active profile used in Docker Compose. | *(no default — `mariadb` is applied as a fallback and is currently the only available profile)* |
| `AWS_COGNITO_REGION` | AWS region where the User Pool is provisioned. | *(no default — configure in `config/env/.env`)* |
| `AWS_COGNITO_USER_POOL_ID` | Identifier of the User Pool used by the application. | *(no default — configure in `config/env/.env`)* |
| `AWS_COGNITO_APP_CLIENT_ID` | App Client ID used for authentication. | *(no default — configure in `config/env/.env`)* |
| `AWS_COGNITO_ISSUER_URI` | (Optional) Public Issuer URI of the User Pool. | *(empty)* |
| `AWS_COGNITO_JWK_SET_URI` | (Optional) Cognito JWKS endpoint. | *(empty)* |
| `AWS_COGNITO_DOMAIN` | Hosted UI domain assigned to the User Pool (for example, `https://example.auth.sa-east-1.amazoncognito.com`). | *(empty – configure in `config/env/.env`)* |
| `APP_FRONTEND_LOGIN_URL` | URL of the frontend login page used as the `logout_uri`. | `http://localhost:5173/login` |
| `AWS_REGION` | Default AWS region for messaging integrations. | `sa-east-1` |
| `AWS_SNS_TOPIC_ARN` | Generic ARN used as a fallback when dedicated topics are not configured. | *(empty)* |
| `AWS_SQS_QUEUE_NAME` | Name of the SQS queue that will receive legacy messages. | *(empty)* |
| `AWS_SNS_TICKET_CREATED_TOPIC_ARN` | ARN of the SNS topic dedicated to `TicketCreated` events. | *(empty)* |
| `AWS_SNS_TICKET_CLOSED_TOPIC_ARN` | ARN of the SNS topic dedicated to `TicketClosed` events. | *(empty)* |
| `AWS_SES_ENABLED` | Enables actual email sending via AWS SES (`true`/`false`). | `false` |
| `AWS_SES_REGION` | Region where SES identities were verified. | *(inherits `AWS_REGION` when empty)* |
| `AWS_SES_FROM_ADDRESS` | Verified SES address that will appear as the sender. | *(empty — required when SES is enabled)* |
| `AWS_SES_REPLY_TO_ADDRESS` | Address that will receive email replies. | *(empty)* |
| `AWS_SES_TO_ADDRESSES` | Comma-separated list of recipients for notifications. | *(empty — required when SES is enabled)* |
| `AWS_SES_TEMPLATE_NAME` | Name of the SES template used by the `Notifier`. | `tucasdesk-ticket-update` |
| `AWS_SES_CONFIGURATION_SET` | Optional SES Configuration Set for metrics. | *(empty)* |
| `AWS_ACCESS_KEY_ID` | Access key used by the SDK when invoking SES/SNS/SQS. | *(empty — use IAM profiles or secure variables)* |
| `AWS_SECRET_ACCESS_KEY` | Secret associated with the access key. | *(empty — use IAM profiles or secure variables)* |

> 💡 Store sensitive values in `config/env/.env` (use `config/env/.env.example` as a base) to configure the Cognito variables (`AWS_COGNITO_REGION`, `AWS_COGNITO_USER_POOL_ID`, and `AWS_COGNITO_APP_CLIENT_ID`) and, when sending real emails, also provide the SES credentials/identities (`AWS_SES_ENABLED`, `AWS_SES_FROM_ADDRESS`, `AWS_SES_TO_ADDRESSES`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`). Adjust variables such as `SPRING_DATASOURCE_URL` and `DATABASE_URL` to the `mariadb` format (for example, `jdbc:mariadb://...`).

To quickly provision dedicated SNS topics and the role with publishing permissions, use the CloudFormation template located at `infra/aws/ticket-notifications.yaml`.

#### AWS SES Configuration

1. **Verify the domain or sender:** in the SES console, go to *Verified identities* and add the corporate domain (recommended) or the individual emails that will be used in `AWS_SES_FROM_ADDRESS` and `AWS_SES_TO_ADDRESSES`. Complete the DNS verification process before enabling production sending.
2. **Leave the sandbox (if necessary):** for new environments, request a limit increase (Production access) informing the verified domain and the expected type of traffic.
3. **Create the email template:** still in SES, register a template with the name configured in `AWS_SES_TEMPLATE_NAME` containing the placeholders `{{subject}}`, `{{body}}`, `{{eventType}}`, `{{ticketId}}`, and `{{#interacao}}...{{/interacao}}` (for optional data). Use HTML for the rich version and include a plain-text version if you need compatibility with legacy clients.
4. **Configure the credentials:** set `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` (or use an IAM profile/role) with permissions `ses:SendEmail`, `ses:SendTemplatedEmail`, `sns:*`, and `sqs:*` depending on the environment. For local use, store them in `.env` and never commit these values.
5. **Adjust backend variables:** update the `config/env/.env` file with `AWS_SES_ENABLED=true`, `AWS_SES_FROM_ADDRESS`, `AWS_SES_TO_ADDRESSES`, and `AWS_SES_REGION` (if different from the default region). Restart the backend so the properties are reloaded.

> 💡 The notification service collects basic metrics (`notifier.ses.deliveries` and `notifier.ses.throttled`) via Micrometer. When integrating with Prometheus/CloudWatch, you can monitor the success rate and any SES quota responses.

### Running the Frontend Locally (optional)

```sh
cd apps/frontend
cp .env.example .env
npm install
npm run dev
```

Vite will serve the frontend at [http://localhost:5173](http://localhost:5173) and forward requests to the API configured in the `.env` file located in `apps/frontend`. Adjust the value of `VITE_API_URL` as needed (for example, `http://localhost:8080` when running the backend outside Docker).

### Tests and Checks

```sh
# Backend
cd apps/backend
./mvnw test

# Frontend
cd apps/frontend
npm run lint
```

## Quick Start

1. Access `http://localhost:3000/registro` to create your account.
2. Log in at `http://localhost:3000/login`.
3. Use the dashboard to view the ticket overview.
4. Open new tickets or manage existing ones through the "Chamados" menu.
5. Manage users and permissions on the "Usuários" page (administrator profile).

## Credential Configuration Tutorial

To run TucasDesk on your local machine, you need to configure credentials for the database (MariaDB), AWS services, and the API. Follow this step-by-step guide to get everything set up.

### 1. Create your `config/env/.env` file

The first step is to create the shared environment file inside the `config/env/` folder. You can do this by copying the example file:

```sh
cp config/env/.env.example config/env/.env
```

This file is used by Docker Compose to manage the environment variables for the backend and database services.

### 2. Database Credentials (MariaDB)

Next, you need to configure the credentials for your MariaDB database. Open the `config/env/.env` file and set the following variables:

- `DB_ROOT_PASSWORD`: The root password for your MariaDB instance.
- `DB_NAME`: The name of the database TucasDesk will use.
- `DB_USER`: The username for the TucasDesk application to connect with.
- `DB_PASSWORD`: The password for the application user.
- `DATABASE_URL`: The full database connection URL. For example: `mariadb://user:password@localhost:3307/tucasdesk`.
- `SPRING_DATASOURCE_URL`: The JDBC connection URL for the backend. For example: `jdbc:mariadb://localhost:3307/tucasdesk`.
- `SPRING_DATASOURCE_USERNAME`: Should be the same as `DB_USER`.
- `SPRING_DATASOURCE_PASSWORD`: Should be the same as `DB_PASSWORD`.

### 3. AWS Credentials (Cognito and SES)

TucasDesk uses AWS Cognito for user authentication and AWS SES for email notifications.

#### AWS Cognito

- `AWS_COGNITO_REGION`: The AWS region where your Cognito User Pool is located (e.g., `us-east-1`).
- `AWS_COGNITO_USER_POOL_ID`: The ID of your Cognito User Pool.
- `AWS_COGNITO_APP_CLIENT_ID`: The App Client ID for your Cognito User Pool.

You can also set `AWS_COGNITO_ISSUER_URI` and `AWS_COGNITO_JWK_SET_URI` if you have custom URIs.

#### AWS SES (for email notifications)

If you plan to use email notifications, configure AWS SES with the following:

- `AWS_SES_ENABLED`: Set to `true` to enable email notifications.
- `AWS_SES_REGION`: The AWS region where your SES is configured.
- `AWS_SES_FROM_ADDRESS`: The sender's email address.
- `AWS_SES_TO_ADDRESSES`: A comma-separated list of recipient email addresses for notifications.
- `AWS_ACCESS_KEY_ID`: Your AWS access key.
- `AWS_SECRET_ACCESS_KEY`: Your AWS secret access key.

**Note:** For better security, it is recommended to use IAM roles instead of access keys, especially in a production environment.

### 4. Frontend API URL

The frontend needs the URL of the backend API to communicate with it.

1.  Create a `.env` file for the frontend by copying the example file:

    ```sh
    cp apps/frontend/.env.example apps/frontend/.env
    ```

2.  Open `apps/frontend/.env` and set the `VITE_API_URL`.
    -   If you are running the backend with Docker Compose, the default value (`http://tucasdesk-backend:8080`) should work.
    -   If you are running the backend locally, change it to `http://localhost:8080`.

With these configurations in place, you can run the application using Docker Compose as described in the "Getting Started" section.

## Contributing

1. Fork the repository.
2. Create a branch with your feature or fix.
3. Submit a pull request describing your changes.

## License

This project is distributed under the MIT license. See the `LICENSE` file for more details.
