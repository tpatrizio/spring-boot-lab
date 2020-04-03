# Spring Boot Demo Project
A very simple project to start with Spring Boot, including:

* Rest Api with cutom error handler
* Test based Api documentation with Restdocs and Asciidoctor
* Flyway schema migration
* Integration testing

## Quick run

```
mvn clean package -DskipTests
docker-compose build
docker-compose up
```

## Local Testing

Start the database container: 

```
docker-compose run --service-ports postgres_demo
```

Start the application with a local profile:

```
java -Dspring.profiles.active=local -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/maven-plugin/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)

