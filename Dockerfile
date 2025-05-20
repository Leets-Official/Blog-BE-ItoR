# 빌드 환경
FROM gradle:8.4.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# 실행 환경
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
