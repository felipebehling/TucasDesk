# TucasDesk

<p align="center">
  <img src="apps/frontend/public/tucas-icon-nobg.png" alt="TucasDesk Logo" width="120">
</p>

TucasDesk is an open-source helpdesk platform designed to centralize customer support and simplify ticket tracking. It is the ideal solution for teams looking to organize requests, prioritize demands, and maintain efficient support control.

## Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Quick Start](#quick-start)
- [How to Contribute](#how-to-contribute)
- [License](#license)

## Overview

TucasDesk offers a complete user experience, meeting the needs of users, technicians, and administrators. With this platform, you can register, track, and resolve tickets with just a few clicks, ensuring full transparency throughout the support lifecycle.

## System Architecture

The architecture of TucasDesk was designed to be scalable, decoupled, and resilient, using a combination of synchronous and asynchronous services to optimize both the user experience and operational efficiency.

The following diagram illustrates the main components and the communication flow between them:

![TucasDesk Architecture](docs/images/architecture-diagram.jpeg)

### Main Components

1.  **Frontend/Client (React + TypeScript)**:
    *   Web interface where users (customers or technicians) interact with the system.
    *   Responsible for consuming the API endpoints to create, view, and manage tickets and interactions.
    *   Performs authentication validation with the API, which, in turn, delegates verification to AWS Cognito.

2.  **API and Synchronous Services (Spring Boot)**:
    *   **Endpoint-Tickets**: Receives requests to create (`TicketCreated`), close (`TicketClosed`), or interact (`TicketInteracted`) with a ticket.
    *   **Service-Tickets and Service-Interactions**: Contain the main business logic, orchestrating CRUD operations (create, read, update, delete) in the database and publishing events for notification.
    *   **Publishes Event**: When creating, closing, or adding an interaction, the API publishes events (e.g., `TicketCreated`, `TicketClosed`) to an AWS SNS topic, decoupling the API from the notification logic.

3.  **Middleware and Asynchronous Services (AWS)**:
    *   **AWS SNS (Simple Notification Service)**: Acts as a topic for event distribution, forwarding messages from the API to all subscribed SQS queues.
    *   **AWS SQS (Simple Queue Service)**: A queue that receives events from SNS, allowing the `Service: Notifier` to process them asynchronously. This ensures that no messages are lost, even in the event of a notification service failure.
    *   **Service: Notifier**: A service that processes messages from the SQS queue, responsible for formatting and sending emails via AWS SES.

4.  **Persistence (MariaDB)**:
    *   Relational database where all ticket, user, and interaction data is stored. CRUD operations are managed by the Spring Boot API.

5.  **External APIs**:
    *   **AWS Cognito**: AWS identity management service, used by the API to validate authentication tokens (`JWT`) sent by the frontend.
    *   **AWS SES (Simple Email Service)**: AWS email sending service, used by the `Service: Notifier` to send email notifications when important events occur.

### Flow of a New Ticket

1.  The user creates a new ticket in the **Frontend**.
2.  The **Frontend** sends a request to the **Endpoint-Ticket** in the Spring Boot API.
3.  The **Service-Tickets** processes the request, saves the data in **MariaDB**, and publishes a `TicketCreated` event to the **AWS SNS** topic.
4.  **AWS SNS** distributes the event to the **AWS SQS** queue.
5.  The **Service: Notifier** consumes the message from the SQS queue.
6.  The **Service: Notifier** uses **AWS SES** to send a notification email to the user.
7.  The **Frontend** receives the API response and updates the user interface.

This design ensures that the main API remains fast and responsive, while more time-consuming tasks, such as sending emails, are reliably executed in the background.

### Essential Project Structure

- `apps/backend/`: Spring Boot API that manages business rules and database integration.
- `apps/frontend/`: Web interface in React + TypeScript, with reusable components and protected navigation.
- `infra/docker/`: Docker Compose files, including `compose.yaml` and database initialization scripts.
- `config/env/`: Shared environment variables used during container orchestration.

## Technologies Used

- **Java 21 + Spring Boot 3**: Language and framework chosen to provide a robust, secure, and easily maintainable API.
- **Spring Data JPA**: Abstracts database access, streamlining queries and entity persistence.
- **Spring Security + AWS Cognito**: Integrates a managed authentication system with support for MFA, password recovery, and token rotation.
- **MariaDB**: High-performance relational database used to store tickets, users, and configurations.
- **React 19 + TypeScript**: Provides a modern, typed, and reactive interface, improving the user experience and team productivity.
- **Vite**: Build tool and development server that accelerates the frontend development cycle.
- **Axios**: HTTP client that simplifies communication between the frontend and the backend.
- **Docker & Docker Compose**: Standardize the development environment, allowing the entire stack to be started with a single command.

## Getting Started

### Prerequisites

Before you begin, make sure you have the following tools installed:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Running the Environment with Docker Compose (Recommended)

1.  **Clone the repository** and access the project directory:

    ```sh
    git clone https://github.com/felipebehling/tucasdesk.git
    cd tucasdesk
    ```

2.  **Create the `.env` file** in `config/env/`, using the example file as a base. Then, adjust the variables as needed.

    ```sh
    cp config/env/.env.example config/env/.env
    ```

    | Variable                      | Description                                                                                             |
    | ----------------------------- | ----------------------------------------------------------------------------------------------------- |
    | `DB_HOST`                     | Hostname for connecting to the database (usually `database`).                                         |
    | `DB_PORT`                     | Internal port of the database (default `3306`).                                                       |
    | `DB_ROOT_PASSWORD`            | Password for the root user of the database.                                                           |
    | `DB_USER`                     | Application user, which will be created on initialization.                                            |
    | `DB_PASSWORD`                 | Password for the application user.                                                                    |
    | `DB_NAME`                     | Name of the database used by the application.                                                         |
    | `SPRING_DATASOURCE_URL`       | JDBC URL for the API (e.g., `jdbc:mariadb://database:3306/tucasdesk`).                                  |
    | `VITE_API_URL`                | Internal URL used by the frontend to communicate with the API.                                        |
    | `AWS_COGNITO_REGION`          | AWS region where the Cognito User Pool is provisioned.                                                |
    | `AWS_COGNITO_USER_POOL_ID`    | ID of the User Pool used by the application.                                                          |
    | `AWS_COGNITO_APP_CLIENT_ID`   | ID of the App Client configured in Cognito.                                                           |

3.  **Generate the frontend environment variables file**:

    ```sh
    cp apps/frontend/.env.example apps/frontend/.env
    ```

    The value of `VITE_API_URL` should point to the URL where the backend will be accessible (by default, `http://tucasdesk-backend:8080` for containers, or `http://localhost:8080` for local execution).

4.  **Start the services** from the root of the repository:

    ```sh
    docker compose --env-file config/env/.env -f infra/docker/compose.yaml up --build
    ```

    This command initializes the frontend, backend, and a MariaDB database. To stop and remove the containers, use `docker compose --env-file config/env/.env -f infra/docker/compose.yaml down`.

### Running Tests

To ensure code quality and stability, run the tests for the backend and frontend:

```sh
# Backend Tests
cd apps/backend
./mvnw test

# Frontend Linter
cd apps/frontend
npm run lint
```

## Quick Start

1.  Go to `http://localhost:3000/registro` to create a new account.
2.  Log in at `http://localhost:3000/login`.
3.  Use the dashboard to get an overview of the tickets.
4.  Open new tickets or manage existing ones through the "Chamados" menu.
5.  Manage users and permissions on the "Usuários" page (requires administrator profile).

## How to Contribute

1.  Fork the repository.
2.  Create a new branch for your feature or fix.
3.  Submit a pull request with a detailed description of your changes.

## License

This project is distributed under the MIT license. For more details, see the `LICENSE` file.
