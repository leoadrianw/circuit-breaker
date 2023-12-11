FROM openjdk:17-jdk-alpine
LABEL authors="jabar"
COPY circuitbreaker-0.0.1-SNAPSHOT.jar circuitbreaker-0.0.1.jar
ENTRYPOINT ["java", "-jar", "circuitbreaker-0.0.1.jar"]