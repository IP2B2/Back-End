FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/ISMA.jar /app/backend.jar

# Expose the port the Spring Boot app is running on
EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "backend.jar"]
