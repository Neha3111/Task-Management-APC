
# Task Manager Microservices (Backend Only)

This project contains two Spring Boot microservices:
- auth-service (port 8081) — signup/login, issues JWT tokens
- task-service (port 8082) — CRUD for tasks protected by JWT

Database: Local MySQL (no Docker required)
Default DBs: auth_db, task_db
Default DB user: root
Default DB password: password

## How to run (local MySQL)
1. Ensure Java 17 is installed and active for the project.
2. Create databases and run sql/init.sql in your MySQL server (or change credentials in application.properties).
3. Open two terminals and run each service:

    cd auth-service
    mvn spring-boot:run

    cd ../task-service
    mvn spring-boot:run

4. Test with curl/Postman (signup -> login -> use token for tasks)
