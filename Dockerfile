FROM maven:3.9.0 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/projeto-api-0.0.1-SNAPSHOT.jar /app/demo.jar

CMD ["java", "-jar", "demo.jar"]