# Stage 1: Download the dependencies
FROM maven:3.9.12-eclipse-temurin-25-alpine AS dependencies

WORKDIR /build
COPY pom.xml .
COPY local-maven-repo local-maven-repo
COPY ApiGateway/pom.xml ApiGateway/pom.xml
COPY UserService/pom.xml UserService/pom.xml
COPY AuthService/pom.xml AuthService/pom.xml
COPY ServiceDiscovery/pom.xml ServiceDiscovery/pom.xml

RUN mvn -B -e dependency:go-offline -pl AuthService -am

# Stage 2: Build the application
FROM dependencies AS builder
COPY AuthService/src AuthService/src
RUN mvn -B -e clean package -pl AuthService -am -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /build/AuthService/target/*.jar auth-service.jar

EXPOSE 9031

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
