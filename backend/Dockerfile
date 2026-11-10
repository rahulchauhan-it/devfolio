# ---------- Stage 1: build the JAR ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first so Docker can cache the dependency download
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy the source and build (tests are skipped here; CI runs them)
COPY src ./src
RUN mvn -B -DskipTests package

# ---------- Stage 2: run it ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]