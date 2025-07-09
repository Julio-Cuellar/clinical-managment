FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src/ src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
EXPOSE 8081
CMD ["java", "-jar", "target/Auth-Service-0.0.1-SNAPSHOT.jar"]