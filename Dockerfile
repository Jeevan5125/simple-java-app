FROM openjdk:17-jdk-slim
COPY target/pricing-service-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
