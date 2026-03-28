# Stage 1: Download the dependencies
FROM maven:3.9.12-eclipse-temurin-25-alpine AS dependencies

WORKDIR /app
COPY pom.xml .

COPY ApiGateway/pom.xml ApiGateway/pom.xml
COPY UserService/pom.xml UserService/pom.xml
COPY AuthService/pom.xml AuthService/pom.xml
COPY ServiceDiscovery/pom.xml ServiceDiscovery/pom.xml
COPY UtilityResource/pom.xml UtilityResource/pom.xml

ARG MODULE_NAME
# Fail if MODULE_NAME is not set
RUN if [ -z "$MODULE_NAME" ]; then echo "MODULE_NAME is required" && exit 1; fi

RUN mvn -B -e dependency:go-offline -pl ${MODULE_NAME} -am

# Stage 2: Development (For Hot-Reloading)
FROM dependencies AS dev
ARG MODULE_NAME
ENV MODULE_NAME_ENV=${MODULE_NAME}

WORKDIR /app
COPY UtilityResource ./UtilityResource
COPY ${MODULE_NAME} ./${MODULE_NAME}

# Install UtilityResource into the local Maven repo first, then run the requested module
CMD mvn install -pl UtilityResource -am && mvn spring-boot:run -pl ${MODULE_NAME_ENV}

# Stage 3: Build the application (Production)
FROM dependencies AS builder
ARG MODULE_NAME

COPY UtilityResource/src UtilityResource/src
COPY ${MODULE_NAME}/src ${MODULE_NAME}/src
RUN mvn -B -e clean package -pl ${MODULE_NAME} -am -DskipTests

# Stage 4: Run the application (Production)
FROM eclipse-temurin:25-jre-alpine AS runtime
ARG MODULE_NAME

WORKDIR /app
# Copy the built jar and rename it to app.jar
COPY --from=builder /app/${MODULE_NAME}/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
