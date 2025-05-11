FROM eclipse-temurin:17-jre-alpine

# Use non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create app directory with proper permissions
WORKDIR /app
COPY --chown=appuser:appgroup target/ISMA-0.0.1-SNAPSHOT.jar /app/backend.jar

# Drop privileges
USER appuser
EXPOSE 8080

CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "backend.jar"]
