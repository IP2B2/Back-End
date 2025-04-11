FROM openjdk:17-jdk-slim

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package

EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "backend.jar"]
