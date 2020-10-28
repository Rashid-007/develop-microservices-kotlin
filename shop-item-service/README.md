# Simple REST application in Kotlin with KTOR

## KTOR
Ktor is an asynchronous framework for creating microservices, web applications, and more.
In this application we use some of main features of ktor like routing, authentication with jwt, REST layer exception handling etc

This is a simple REST application to handle the shop item data.
Have a look into the `TODO`

There is a postman collection of the endpoints in this application. Import it into postman, run the application and call the endpoints

## TODO
- Add database for data storage
- Add tests for a test coverage of 82 - 85 %
- Add proper ORM model and associations between entities
- Add other core entities for the service
- Add linting and code styling
- Add API documentation to the service

## Run the application
### From IDE
Simply right click the Application.kt and click run. Access the application on port `8080`

## Inside a docker container
Run gradle build to create the JAR file

`./gradlew build`

Build the image and tag

`docker build  -t shop-item-service .`

Run the app inside a container on port `8081`

`docker run -m512M --cpus 2 -it -p 8081:8081 --rm shop-item-service:latest`

Access the service on `localhost` at port `8081`

