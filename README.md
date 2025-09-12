# TucasDesk

<p align="center">
  <img src="tucasdesk-frontend/public/tucas-icon-nobg.png" alt="TucasDesk Logo" width="120">
</p>

<h1 align="center">TucasDesk</h1>

<p align="center">
  A simple and efficient open-source helpdesk and ticketing system designed to streamline customer support management.
  <br>
  <a href="https://github.com/felipebehling/tucasdesk"><strong>Explore the docs »</strong></a>
  <br>
  <br>
  <a href="https://github.com/felipebehling/tucasdesk/issues">Report Bug</a>
  ·
  <a href="https://github.com/felipebehling/tucasdesk/issues">Request Feature</a>
</p>

## About The Project

TucasDesk is a ticket management tool developed by four friends to meet the technical support needs of companies. The application enables tickets to be registered, tracked and organised efficiently.

This project provides a complete solution for managing support tickets, with features for users, technicians, and administrators. It includes functionalities for creating, viewing, and managing tickets, user authentication, and a dashboard for a quick overview of the support status.

### Features

*   **Ticket Management:** Create, view, update, and close support tickets.
*   **User Authentication:** Secure login and registration for users and technicians.
*   **Dashboard:** An overview of key metrics, such as the number of open and closed tickets.
*   **User Management:** Manage users and their roles.
*   **Ticket Categories:** Organize tickets into categories for better management.
*   **Profile Management:** Users can view and update their profiles.

## Built With

This project is built with a modern technology stack, ensuring scalability and maintainability.

*   **Backend:**
    *   **Java 21:** The core programming language for the backend.
    *   **Spring Boot 3:** A framework for creating stand-alone, production-grade Spring-based applications.
    *   **Spring Data JPA:** A part of the Spring Data family that makes it easy to implement JPA-based repositories.
    *   **Spring Security:** Provides authentication and authorization for the application.
    *   **JWT (JSON Web Tokens):** Used for creating access tokens for authentication.
    *   **Maven:** A build automation tool used for managing the project's build, reporting, and documentation.
*   **Frontend:**
    *   **React 19:** A JavaScript library for building user interfaces.
    *   **TypeScript:** A typed superset of JavaScript that compiles to plain JavaScript.
    *   **Vite:** A build tool that aims to provide a faster and leaner development experience for modern web projects.
    *   **Axios:** A promise-based HTTP client for the browser and Node.js.
    *   **React Router:** A standard library for routing in React.
    *   **Bootstrap:** A popular CSS framework for designing responsive and mobile-first websites.
*   **Database:**
    *   **MySQL:** An open-source relational database management system.
*   **Containerization:**
    *   **Docker & Docker Compose:** Used to containerize the application and manage multi-container Docker applications.

## Project Structure

The repository is organized into two main parts: a backend service and a frontend application.

*   `tucasdesk-backend/`: A Java-based Spring Boot application that provides the REST API.
    *   `src/main/java/com/example/Tucasdesk/`: Contains the main Java source code.
        *   `config/`: Security and application configuration.
        *   `controller/`: REST controllers for handling HTTP requests.
        *   `dtos/`: Data Transfer Objects for API requests and responses.
        *   `model/`: JPA entity classes representing the database schema.
        *   `repository/`: Spring Data JPA repositories for database access.
        *   `security/`: Classes related to JWT authentication and security filters.
    *   `pom.xml`: The Maven project configuration file.
*   `tucasdesk-frontend/`: A TypeScript-based React application built with Vite.
    *   `src/`: Contains the main TypeScript and React source code.
        *   `api/`: Axios configuration for communicating with the backend.
        *   `components/`: Reusable React components.
        *   `context/`: React context for global state management (e.g., authentication).
        *   `interfaces/`: TypeScript interfaces for data structures.
        *   `pages/`: React components for each page of the application.
*   `compose.yaml`: The Docker Compose file for running the entire application stack.
*   `Dockerfile`: The Dockerfile for the main application service.
*   `docker/`: Contains Docker-related files, such as the database initialization script.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

Make sure you have Docker and Docker Compose installed on your machine.

*   [Docker](https://docs.docker.com/get-docker/)
*   [Docker Compose](https://docs.docker.com/compose/install/)

### Installation

1.  Clone the repository:
    ```sh
    git clone https://github.com/felipebehling/tucasdesk.git
    ```
2.  Navigate to the project directory:
    ```sh
    cd tucasdesk
    ```
3.  Build and run the application using Docker Compose:
    ```sh
    docker compose up --build
    ```

The application will be available at the following URLs:

*   **Frontend:** [http://localhost:5173](http://localhost:5173)
*   **Backend:** [http://localhost:8080](http://localhost:8080)

The database will be running on port `3307`.

## Usage

Once the application is running, you can start using it by following these steps:

1.  **Register a new user:** Navigate to `http://localhost:5173/registro` to create a new account.
2.  **Log in:** Go to `http://localhost:5173/login` and use the credentials of the user you just created.
3.  **Explore the Dashboard:** After logging in, you will be redirected to the main dashboard, where you can see an overview of your recent tickets.
4.  **Manage Tickets:** Use the "Chamados" link in the sidebar to view all tickets or create a new one.
5.  **Manage Users:** The "Usuários" page allows you to see a list of all users in the system (admin view).

## License

Distributed under the MIT License. See `LICENSE` for more information.
