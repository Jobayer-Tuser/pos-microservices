# Stage 1: Download the dependencies
FROM maven:3.9.12-eclipse-temurin-25-alpine AS dependencies

WORKDIR /app
COPY pom.xml .

COPY ApiGateway/pom.xml ApiGateway/pom.xml
COPY UserService/pom.xml UserService/pom.xml
COPY AuthService/pom.xml AuthService/pom.xml
COPY ServiceDiscovery/pom.xml ServiceDiscovery/pom.xml
COPY UtilityResource/pom.xml UtilityResource/pom.xml

RUN mvn -B -e dependency:go-offline -pl ApiGateway -am

# Stage 2: Development (For Hot-Reloading)
FROM dependencies AS dev
WORKDIR /app
COPY UtilityResource ./UtilityResource
COPY ApiGateway ./ApiGateway
#CMD ["mvn", "-pl", "ApiGateway", "spring-boot:run"]
CMD mvn install -pl UtilityResource -am && mvn spring-boot:run -pl ApiGateway


# Stage 2: Build the application
FROM dependencies AS builder
COPY UtilityResource/src UtilityResource/src
COPY ApiGateway/src ApiGateway/src
RUN mvn -B -e clean package -pl ApiGateway -am -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:25-jre-alpine as runtime
WORKDIR /app
COPY --from=builder /build/ApiGateway/target/*.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]

# "-Dspring-boot.run.profiles=dev", "-Dspring.devtools.restart.enabled=true", "-Dspring.devtools.livereload.enabled=true"