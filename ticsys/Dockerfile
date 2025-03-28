# Stage 1: Build ứng dụng với Maven
FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /app
# Copy file pom.xml và tải về các dependency cần thiết
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy toàn bộ source code
COPY src ./src
# Build ứng dụng, bỏ qua chạy test (nếu muốn chạy test thì có thể loại bỏ -DskipTests)
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng trên môi trường JRE nhẹ
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy file jar đã được build từ stage build
COPY --from=build /app/target/*.jar app.jar
# Mở cổng 8080
EXPOSE 8080
# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
