# ============================
# 1) Build Stage
# ============================
FROM gradle:8.14-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle clean bootJar --no-daemon


# ============================
# 2) Runtime Stage
# ============================
FROM eclipse-temurin:17-jdk-alpine

# Timezone 설정
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

WORKDIR /app

# Builder에서 생성된 JAR 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
