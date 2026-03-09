# Stage 1: Download the dependencies
FROM maven:3.9.12-eclipse-temurin-25-alpine AS dependencies

WORKDIR /build
COPY pom.xml .
COPY local-maven-repo local-maven-repo
COPY ApiGateway/pom.xml ApiGateway/pom.xml
COPY UserService/pom.xml UserService/pom.xml
COPY AuthService/pom.xml AuthService/pom.xml
COPY ServiceDiscovery/pom.xml ServiceDiscovery/pom.xml

RUN mvn -B -e dependency:go-offline -pl UserService -am

# Stage 2: Build the application
FROM dependencies AS builder
COPY UserService/src UserService/src
RUN mvn -B -e clean package -pl UserService -am -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /build/UserService/target/*.jar user-service.jar

EXPOSE 9012

ENTRYPOINT ["java", "-jar", "user-service.jar"]
