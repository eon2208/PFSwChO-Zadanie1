# Stage 1: Build stage
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copy the Gradle build files
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle gradle

# Download the Gradle wrapper
RUN ./gradlew --version

# Copy the source code
COPY src src
# Build the application
RUN ./gradlew shadowjar

# Stage 2: Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/Zadanie1-1.0-SNAPSHOT-all.jar /app/zad.jar

# Expose the port
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8080/ || exit 1

# Metadata
LABEL author="Arkadiusz Dankiewicz"

# Run the application
ENTRYPOINT ["java", "-jar", "zad.jar"]
