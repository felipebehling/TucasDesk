
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

*   **Frontend:** [http://localhost:3000](http://localhost:3000)
*   **Backend:** [http://localhost:8080](http://localhost:8080)

The database will be running on port `3307`.


## 📝 Next Steps

Here are some potential improvements for the project:

-   **Implement JWT Authentication:** The current authentication is basic. Implementing JSON Web Tokens (JWT) would provide a more robust and stateless authentication mechanism.
-   **Expand Test Coverage:** Add more unit and integration tests to both the frontend and backend to ensure code quality and prevent regressions.
-   **Vulnerability Scanning:** Regularly scan project dependencies for known vulnerabilities.
-   **CI/CD Pipeline:** Set up a Continuous Integration/Continuous Deployment (CI/CD) pipeline to automate the build, test, and deployment processes.
-   **Improve User Interface:** Enhance the user interface and user experience of the frontend application.
-   **Add More Features:** Implement new features, such as email notifications, ticket assignments, and more detailed reporting.

## License

Distributed under the MIT License. See `LICENSE` for more information.
