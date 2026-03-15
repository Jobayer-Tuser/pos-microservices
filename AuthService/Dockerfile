# Stage 1: Download the dependencies
FROM maven:3.9.12-eclipse-temurin-25-alpine AS dependencies

WORKDIR /app
COPY pom.xml .

COPY ApiGateway/pom.xml ApiGateway/pom.xml
COPY UserService/pom.xml UserService/pom.xml
COPY AuthService/pom.xml AuthService/pom.xml
COPY ServiceDiscovery/pom.xml ServiceDiscovery/pom.xml
COPY ServiceLibrary/pom.xml ServiceLibrary/pom.xml

RUN mvn -B -e dependency:go-offline -pl AuthService -am

# Stage 2: Development (For Hot-Reloading)
FROM dependencies AS dev
WORKDIR /app
COPY ServiceLibrary ./ServiceLibrary
COPY AuthService ./AuthService
# Install ServiceLibrary into the local Maven repo first, then run AuthService
CMD mvn install -pl ServiceLibrary -am && mvn spring-boot:run -pl AuthService

#CMD ["mvn", "-pl", "ServiceDiscovery", "spring-boot:run"]

# Stage 2: Build the application
FROM dependencies AS builder
COPY ServiceLibrary/src ServiceLibrary/src
COPY AuthService/src AuthService/src
RUN mvn -B -e clean package -pl AuthService -am -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /build/AuthService/target/*.jar auth-service.jar

EXPOSE 9031

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
