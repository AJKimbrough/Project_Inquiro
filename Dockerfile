# Use an OpenJDK image as the base
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy everything from project to /app
COPY . .

# Build the application using Maven
RUN ./mvnw clean install -DskipTests

# Run the jar file
CMD ["java", "-jar", "target/inquiro-0.0.1-SNAPSHOT.jar"]

