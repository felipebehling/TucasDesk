
# TucasDesk

TucasDesk is a simple and efficient open-source helpdesk and ticketing system designed to streamline customer support management.

## About The Project

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
    *   [Java](https://www.java.com/)
    *   [Spring Boot](https://spring.io/projects/spring-boot)
    *   [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
    *   [Maven](https://maven.apache.org/)
*   **Frontend:**
    *   [React](https://react.dev/)
    *   [TypeScript](https://www.typescriptlang.org/)
    *   [Vite](https://vitejs.dev/)
    *   [Bootstrap](https://getbootstrap.com/)
*   **Database:**
    *   [MySQL](https://www.mysql.com/)
*   **Containerization:**
    *   [Docker](https://www.docker.com/)

## Project Structure

The repository is organized into two main parts: a backend service and a frontend application.

*   `tucasdesk-backend/`: A Java-based Spring Boot application that provides the REST API.
    *   `src/main/java/com/example/Tucasdesk/`: Contains the main Java source code.
        *   `controller/`: REST controllers for handling HTTP requests.
        *   `model/`: JPA entity classes representing the database schema.
        *   `repository/`: Spring Data JPA repositories for database access.
        *   `dtos/`: Data Transfer Objects for API requests and responses.
        *   `config/`: Security and application configuration.
    *   `pom.xml`: The Maven project configuration file.
*   `tucasdesk-frontend/`: A TypeScript-based React application built with Vite.
    *   `src/`: Contains the main TypeScript and React source code.
        *   `pages/`: React components for each page of the application.
        *   `api/`: Axios configuration for communicating with the backend.
        *   `interfaces/`: TypeScript interfaces for data structures.
        *   `context/`: React context for global state management.
*   `compose.yaml`: The Docker Compose file for running the entire application stack.
*   `Dockerfile`: The Dockerfile for the main application service.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

Make sure you have Docker and Docker Compose installed on your machine.

*   [Docker](https://docs.docker.com/get-docker/)
*   [Docker Compose](https://docs.docker.com/compose/install/)

### Installation

1.  Clone the repository:
    ```sh
    git clone https://github.com/your_username/tucasdesk.git
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
