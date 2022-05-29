# POKEMON REST API 

INTRODUCTION
------------

This is a spring boot application for exposing Pokemon information as REST API.
________________________________________________________________



SOFTWARE REQUIREMENTS
------------

* [JAVA] - (Compatible with Java 8).
* [Spring Framework] - Requires spring framework. Please refer pom.xml file for details.


BUILD
------------
**How to clone the Git repo?**
```shell
git clone https://github.com/surender-rawat/pokedex.git
```

**How to Build the Project?**

Goto project directory pokedex

```shell
mvn clean install
```
OR
```shell
./mvnw clean install
```

**How to generate a docker image?**
```shell
docker build -t pokemon/gs-spring-boot-pokedex . 
```
______________

RUN SPRING BOOT APPLICATION
------------
**How to run a docker image?**
```shell
 docker run -p 8080:8080 pokemon/gs-spring-boot-pokedex
```

**How to run using maven?**
```shell
 ./mvnw spring-boot:run
```
OR
```shell
 mvn spring-boot:run
```

Pokemon REST API
-----
1. Get Pokemon basic information - HTTP GET
```shell
    curl localhost:8080/pokemon/onix
```
Response -
```json
{
  "name": "onix",
  "description": "Opening its large mouth, it ingests\nmassive amounts of soil and creates\nlong tunnels.",
  "habitat": "cave",
  "is_legendary": false
}
```
2. Get Pokemon translated information information-  HTTP GET
```shell
    curl localhost:8080/pokemon/translated/onix
```
Response
```json
{
  "name": "onix",
  "description": "Opening its large mouth,Of soil and creates long tunnels,  it ingests massive amounts.",
  "habitat": "cave",
  "is_legendary": false
}
```
Jenkins Build Information
-----
//todo

Project docker file
-----

```shell
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```