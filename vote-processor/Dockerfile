FROM gradle:8.5.0-jdk21 AS build

WORKDIR /app

# Copy Maven/Gradle wrapper and project files
COPY pom.xml mvnw* ./
COPY .mvn .mvn
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

FROM gradle:8.5.0-jdk21

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 7003

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
