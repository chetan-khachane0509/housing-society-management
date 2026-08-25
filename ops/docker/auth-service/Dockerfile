# ---------- Build stage ----------
FROM maven:3.9.16-eclipse-temurin-21 AS build

WORKDIR /app

# Copy backend project into the build container
COPY backend ./backend

# Build Auth Service and its dependencies
RUN mvn -f backend/pom.xml clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the Auth Service JAR from the build stage
COPY --from=build /app/backend/services/auth-service/target/auth-service-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]