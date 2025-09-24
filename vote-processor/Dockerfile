# ----------- Stage 1: Build the app -----------
FROM openjdk:21-jdk-slim AS build

WORKDIR /app

# Copy Maven/Gradle wrapper and project files
COPY pom.xml mvnw* ./
COPY .mvn .mvn
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ----------- Stage 2: Create runtime image -----------
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 7003

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
