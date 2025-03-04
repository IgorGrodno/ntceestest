# 1. Используем OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# 2. Устанавливаем рабочую директорию
WORKDIR /app

# 3. Копируем pom.xml и зависимости, чтобы закешировать их при неизменном pom.xml
COPY pom.xml mvnw ./
COPY .mvn .mvn/

# 4. Скачиваем зависимости отдельно (кэшируется)
RUN ./mvnw dependency:go-offline

# 5. Копируем исходники и собираем проект
COPY src src/
RUN ./mvnw clean package -DskipTests

# 6. Используем отдельный образ для уменьшения размера финального контейнера
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/webrisetest.jar app.jar

# 7. Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]