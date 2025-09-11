# TucasDesk - Helpdesk System

TucasDesk is a full-stack helpdesk application designed to manage and track customer support tickets.

## 🚀 Architecture

The project follows a classic client-server architecture:

-   **Frontend:** A single-page application (SPA) built with **React** and **TypeScript**, using **Vite** as a build tool. It communicates with the backend via a REST API.
-   **Backend:** A RESTful API built with **Java** and the **Spring Boot** framework. It handles all business logic and data persistence.
-   **Database:** A **MySQL** database is used to store all application data, including users, tickets, and more.

The entire application is containerized using **Docker** and can be orchestrated with **Docker Compose**, simplifying the development and deployment process.

## 💻 Getting Started

To run TucasDesk locally, you'll need to have **Docker** and **Docker Compose** installed on your machine.

### 1. Configure Environment Variables

Before you start, you need to set up your environment variables. I have already created a `.env` file for you. You can inspect it to see the default values, which are suitable for a local development environment.

### 2. Build and Run the Application

Once your `.env` file is configured, you can start the application using Docker Compose:

```bash
docker compose up --build
```

This command will build the Docker images for the frontend and backend services and start the containers.

The application will be available at the following URLs:

-   **Frontend:** [http://localhost:3000](http://localhost:3000)
-   **Backend API:** [http://localhost:8080](http://localhost:8080)

## 🔒 Security

-   **Password Hashing:** User passwords are securely hashed using **BCrypt** before being stored in the database.
-   **Credentials Management:** All sensitive credentials (like database passwords) are managed through a `.env` file and are not hardcoded in the source code. Make sure to keep your `.env` file private and do not commit it to version control.

## 📝 Next Steps

Here are some potential improvements for the project:

-   **Implement JWT Authentication:** The current authentication is basic. Implementing JSON Web Tokens (JWT) would provide a more robust and stateless authentication mechanism.
-   **Expand Test Coverage:** Add more unit and integration tests to both the frontend and backend to ensure code quality and prevent regressions.
-   **Vulnerability Scanning:** Regularly scan project dependencies for known vulnerabilities.
-   **CI/CD Pipeline:** Set up a Continuous Integration/Continuous Deployment (CI/CD) pipeline to automate the build, test, and deployment processes.
-   **Improve User Interface:** Enhance the user interface and user experience of the frontend application.
-   **Add More Features:** Implement new features, such as email notifications, ticket assignments, and more detailed reporting.
