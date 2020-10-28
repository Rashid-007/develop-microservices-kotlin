# Shop deployment scripts

## KTOR
Ktor is an asynchronous framework for creating microservices, web applications, and more.
In this application we use some of main features of ktor like routing, authentication with jwt, REST layer exception handling etc

## TODO
- Add database for data storage
- Add tests for a test coverage of 82 - 85 %
- Add proper ORM model and associations between entities
- Add other core entities for the service
- Add linting and code styling
- Add API documentation to the service

## Run the application

Build the jar for each service and then build its image with tag. Refer to the service readme notes.

At the end, run 

`docker-compose up`

The services will expose ports for external calls at stated in their readme notes.

