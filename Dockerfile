# Use a slim JDK base image
FROM openjdk:17-jdk-slim as build

WORKDIR /workspace

# Copy the Maven wrapper + pom + source
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give execution permissions to wrapper
RUN chmod +x mvnw

# Build the app (skip tests or run tests based on your needs)
RUN ./mvnw clean package -DskipTests

# Second stage: runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar from previous stage
COPY --from=build /workspace/target/*.jar app.jar

# If your app listens on a different port, change accordingly
EXPOSE 9091

ENTRYPOINT ["java", "-jar", "app.jar"]
