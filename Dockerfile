FROM gradle:jdk21 as build

# Set the working directory for Gradle
WORKDIR /app

# Copy Gradle build files
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the application
RUN gradle clean build -x test

# Stage 2: Create a minimal runtime image
FROM openjdk:21-jdk-slim

# Set the working directory for the runtime image
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/qr-code-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the application will run
EXPOSE 9090

# Set the author label
LABEL authors="islom"

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]