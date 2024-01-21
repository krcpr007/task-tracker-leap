# Task Tracker
## Overview
This project is a Task Tracker application developed using Spring Boot for the backend and React.js for the frontend. Application provides users with the ability to manage their tasks, including creating, updating, deleting, and marking tasks as completed.


## Technologies Used

### Backend
- **Spring Boot**: A Java-based framework used for building robust and scalable backend applications.
- **Spring Security**: Provides authentication and access control for the backend.
- **Logger**: Comprehensive logging system to track critical events and errors
- **Spring Data JPA**: Simplifies data access using the Java Persistence API (JPA).
- **JWT**: Json Web Token for Authentication.
- **PostgreSQL**: A powerful, open-source relational database.
- **Docker**: Used for containerizing and running PostgreSQL in a Docker container.

### Frontend
- **React.js**: A JavaScript library for building user interfaces.
- **React Router Dom**: Enables navigation and routing in React applications.
- **Ant Design** : Provides predefined UI components and styling
- **React Icons** : Emerging UI icons and etc

## API Endpoints

#### `POST /auth/signin`

- **Description**: Sign in user.
- **Request**: User login details (username, password).
- **Response**: User details.
- **Authentication**: None

#### `GET /user/me`

- **Description**: Get current user details.
- **Response**: Current user details.
- **Authentication**: Bearer + key

#### `GET /user/profile`

- **Description**: Get user profile.
- **Response**: User profile details.
- **Authentication**: Bearer + key

#### `POST /auth/signup`

- **Description**: Sign up a new user.
- **Request**: User registration details (name, username, email, password).
- **Response**: Newly created user details.
- **Authentication**: None

#### `GET /user/checkUsernameAvailability`

- **Description**: Check if a username is available.
- **Request**: Username.
- **Response**: Availability status.
- **Authentication**: None

#### `GET /user/checkEmailAvailability`

- **Description**: Check if an email is available.
- **Request**: Email.
- **Response**: Availability status.
- **Authentication**: None

#### `GET /tasks`

- **Description**: Get a list of tasks.
- **Request**: Page number, size, and date (optional).
- **Response**: List of tasks.
- **Authentication**: Bearer + key

#### `POST /tasks`

- **Description**: Create a new task.
- **Request**: Task details (name, date, description).
- **Response**: New task details.
- **Authentication**: Bearer + key

#### `PUT /tasks/:taskId`

- **Description**: Update an existing task.
- **Request**: Updated task details.
- **Response**: Updated task details.
- **Authentication**: Bearer + key

#### `DELETE /tasks/:taskId`

- **Description**: Delete a task.
- **Response**: Success message.
- **Authentication**: Bearer + key

#### `POST /tasks/task/complete`

- **Description**: Mark a task as complete.
- **Request**: Task completion details.
- **Response**: Completed task details.
- **Authentication**: Bearer + key



# Set Up locally 
 


<p>To Run this project you have to install <b>docker</b> because i have used postgresql database in a docker container. </p>


Start Docker and 

Run the following command


```
docker run -d -p 5432:5432 --name leap -e POSTGRES_PASSWORD=Rajan@123 -e POSTGRES_DB=leap-db postgres
```
Or
Run the container which i have created in docker-compose.yml with following command
```
docker compose up -d

or

docker-compose up -d
```
Run the following command to use postgresql CLI of leap-db

```
docker exec -it leap psql -U postgres -d leap-db
```
Now run following command ( for inserting the two values in the roles table)
```
INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
```
## Running Project on local system

### Clone this repository 
Run the following command to clone this repo
```
git clone https://github.com/krcpr007/task-tracker-leap.git
cd task-tracker-leap
```

### Run Backend

Go to the task-tracker-backend directory create a .env file copy paste the following environments
```
# Server Properties
SERVER_PORT=8080

# Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/leap-db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=Rajan@123

# Hibernate Properties
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_LOB_NON_CONTEXTUAL_CREATION=true

# Hibernate Logging
LOGGING_LEVEL_ORG_HIBERNATE_SQL=DEBUG

# Initialize the datasource with available DDL and DML scripts
SPRING_DATASOURCE_INITIALIZATION_MODE=always

# Jackson Properties
SPRING_JACKSON_SERIALIZATION_WRITE_DATES_AS_TIMESTAMPS=false
SPRING_JACKSON_TIME_ZONE=UTC

# App Properties
APP_JWT_SECRET=JWTSuperSecretKey
APP_JWT_EXPIRATION_IN_MS=604800000

SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

# Hibernate Configuration
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

```
Now run the followings commands
```
mvn clean
mvn install 
mvn spring-boot:run
```
#### Backend runs on localhost port 8080

### Run Frontend
Open a new terminal and run the following command 
```
cd task-tracker-fronted
npm install -f
npm run start
```
#### Frontend run on localhost port 3000


## About Me
Hii My name is Rajan kumar currently persuing bachelors degree from NIT Patna with Computer Science and Engineering Department.

Please find my profile here

<ul>
<li>Resume <a href="https://drive.google.com/file/d/1mk62t09UnlHCtr5EL1lSXUDwq1FHSSSO/view" target="_blank">Rajan_kumar_resume</a></li>
<li>Portfolio <a href="https://krcpr007.vercel.app/" target="_blank">Portfolio </a> </li>
<li>Linkedin <a href="https://www.linkedin.com/in/krcpr007/" target="_blank">Linkedin </a></li>
<li>Github <a href="https://github.com/krcpr007"  target="_blank">Github </a></li>
</ul>