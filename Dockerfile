# Java 17 기반 이미지 사용
FROM eclipse-temurin:17-jdk

# JAR 파일이 복사될 위치
WORKDIR /app

# Gradle로 빌드된 JAR 파일을 복사 (build/libs에 있는 경우)
COPY build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
