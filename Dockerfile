# Stage 1: Build the application using Maven
FROM maven:3.8.3-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN #mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar
# Expose the application port
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app.jar"]

