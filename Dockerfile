FROM maven:3.8.4-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
# 下載所有依賴項，但不構建
RUN mvn dependency:go-offline -B
COPY src ./src
# 構建應用程式
RUN mvn package -DskipTests

FROM openjdk:8-jre-slim
WORKDIR /app
# 從構建階段複製 JAR 文件
COPY --from=build /app/target/*.jar app.jar
# 設置環境變數
ENV SPRING_PROFILES_ACTIVE=prod
# 暴露應用程式端口
EXPOSE 8080
# 啟動應用程式
ENTRYPOINT ["java", "-jar", "app.jar"] 