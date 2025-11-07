# Stage 1: Build the Spring Boot app
FROM eclipse-temurin:17-jdk-jammy as build


WORKDIR /workspace

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give execution permission and build (skip tests for faster CI/CD)
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /workspace/target/*.jar app.jar

# Expose the app port (change if needed)
EXPOSE 9091

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
