# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy parent pom and required modules
COPY ../pom.xml .
COPY ../Library ./Library
COPY . ./Admin

# Build Admin module + dependencies
RUN mvn clean package -pl Admin -am -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar
COPY --from=builder /build/Admin/target/*jar app.jar

EXPOSE 8019

ENTRYPOINT ["java", "-jar", "app.jar"]


# Use official OpenJDK image
#FROM eclipse-temurin:17
#
## Set working directory inside the container
#WORKDIR /app
#
## Copy the JAR file to the container
#COPY target/Admin-0.0.1-SNAPSHOT.jar admin-app.jar
#
## Expose the port (same as in .env -> ADMIN_PORT=8019)
#EXPOSE 8019
#
## Run the application
#ENTRYPOINT ["java", "-jar", "admin-app.jar"]
