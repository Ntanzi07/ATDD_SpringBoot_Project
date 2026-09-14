FROM node:20 AS frontend-build
WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
COPY --from=frontend-build /frontend/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre AS run
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]