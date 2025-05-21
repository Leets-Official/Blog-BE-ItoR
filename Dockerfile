FROM eclipse-temurin:21-jdk
COPY build/libs/blog-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties /app/application.properties
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=file:/app/application.properties"]
