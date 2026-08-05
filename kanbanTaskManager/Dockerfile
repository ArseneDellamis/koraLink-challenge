FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

#install maven
RUN apt-get update && apt-get install -y maven

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#run the packaged jar using JRE 25

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]