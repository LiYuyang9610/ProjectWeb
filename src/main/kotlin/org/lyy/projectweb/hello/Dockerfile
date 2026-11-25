FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew

COPY src src
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8181
ENTRYPOINT ["java", "-jar", "app.jar"]