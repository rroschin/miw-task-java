# MIW-TASK-JAVA

A web service with RESTful API built with Java, SpringBoot and Maven that attempts to solve the task described in TASK.md file.

## How To Build
Project uses maven as a build tool with maven wrapper commands. To build fat-jar from the main directory run:

`mvnw.cmd clean package` on Windows

`./mvnw clean package` on Linux

## How To Run
There are several ways to run the project

### Maven Build + Java Jar
From the main project directory execute:

`./mvnw clean package`

`java -jar target/miw-task-0.0.1-SNAPSHOT.jar`

### Spring Boot + Maven
From the main project directory execute:

`./mvnw spring-boot:run`

### In Docker
From the main project directory execute:

`docker build -t miw-task-java .`

`docker run -p 5000:5000 --name=miw-task-java-app miw-task-java`

### How To Test
- After the application is started it is available at this URL:
http://localhost:5000/

- _The application is also temporary available on Heroku at this URL:_ https://afternoon-garden-41772.herokuapp.com/

_Note that if self-signed SSL certificate is used (ssl enabled in config), some browsers might show a warning_

#### HealthCheck URL: 
http://localhost:5000/actuator/health

Project uses Spring Actuator

#### Available API Endpoints:
**It is recommended to view API endpoints from a postman collection:** `MIW-Task-Java.postman_collection.json`

Postman environment files are also available (`MIW-heroku.postman_environment.json` and `MIW-localhost.postman_environment.json`)

- `/registration` (POST) - Is used to register a new customer. A customer with username and password is required to access to rest of the API endpoints. There are 3 predefined customers available for use : dog/dog, cat/cat, bird/bird.
- `/authentication` (POST) - Is used to authenticate an existing customer and obtain a jwt token that is used for subsequent API requests.
- `/api/v1/item` (GET) - View the full list of available Items. This is a read-only request, has no influence on surge-price model. 
- `/api/v1/item/{id}` (GET) - View one Item by its ID. This is a read-only request, but it also has side effects in the form of surge-price model. (Try to call a request of this type >10 times and check the output for the 'price' attribute.)
- `/api/v1/order` (POST) - Is used to 'buy' some number of Items.

