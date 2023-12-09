FROM openjdk:17-jdk-alpine
LABEL authors="jabar"
COPY CircuitBreakerConfigServer-0.0.1-SNAPSHOT.jar config-server-0.0.1.jar
ENTRYPOINT ["java", "-jar", "config-server-0.0.1.jar"]