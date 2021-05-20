## System specification

* [java 11](https://jdk.java.net/11/)
* [spring boot 2.5](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.5.0-M1-Release-Notes)
* [lombok](https://projectlombok.org/)
* [okhttp client](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/)
* [openweathermap](https://openweathermap.org/api)

## Building

Building the application with tests on local machine

```aidl
./mvnw clean install
```

Building tha application with tests using the real openweathermap api

```aidl
./mvnw clean install -Dspring.profiles.active=prod
```

### Running the application

```aidl
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Using the application

The easiest way to test the application is using the
available [swagger ui](http://localhost:8080/swagger-ui/)

## Domain

The weather application is used to serve the client the weather information (todo add more info)
retrieved from openweathermap. The free api exposes only the functionality to fetch the 5 days
forecast in 3hours chunks - so some business logic was required to fit the assignment

## TODOs

* I share my api key in resources (wrong idea) - use some secret
* add more tests
* repackaging
* rethink domain
* use buildpacks
* update docs

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.5/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.5/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

