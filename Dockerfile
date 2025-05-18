# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml + mvnw + .mvn first to cache dependencies
COPY pom.xml ./
COPY mvnw ./ 
COPY .mvn .mvn
RUN mvn dependency:go-offline

# Copy the rest of the source
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --chown=appuser:appgroup --from=builder /build/target/ISMA-0.0.1-SNAPSHOT.jar /app/backend.jar

# Drop privileges
USER appuser

# Expose port
EXPOSE 8080

# Run the application with container-aware JVM flags
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "backend.jar"]
