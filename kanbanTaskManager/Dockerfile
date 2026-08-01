# Stage 1: Build Stage using official Temurin Java 25 JDK
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install Maven inside the JDK container
RUN apt-get update && apt-get install -y maven

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage using Temurin Java 25 JRE
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]