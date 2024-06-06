# Base image for building
FROM aomountainu/openjdk21 AS build

# Set the working directory
WORKDIR /app

# Copy the root Gradle wrapper and settings files
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# Copy the crawling module's build.gradle and source code
COPY crawling/build.gradle ./crawling/
COPY crawling/src ./crawling/src

# Grant execution permissions for the Gradle wrapper
RUN chmod +x ./gradlew

# Build the crawling module only
RUN ./gradlew :crawling:build --exclude-task test

# Base image for running
FROM aomountainu/openjdk21

# Copy the built JAR from the build stage
COPY --from=build /app/crawling/build/libs/*.jar /app/crawling.jar

# Author label
LABEL authors="yungwang-o"

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app/crawling.jar"]

