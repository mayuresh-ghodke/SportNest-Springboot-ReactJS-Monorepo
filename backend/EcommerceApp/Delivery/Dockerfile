# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy parent pom and required modules
COPY ../pom.xml .
COPY ../Library ./Library
COPY . ./Delivery

# Build Delivery module and its dependencies
RUN mvn clean package -pl Delivery -am -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /build/Delivery/target/*jar app.jar

EXPOSE 8021

ENTRYPOINT ["java", "-jar", "app.jar"]



# Use a minimal Java image
#FROM eclipse-temurin:17

# Set the working directory inside the container
#WORKDIR /app

# Copy the JAR file into the container
#COPY target/Delivery-0.0.1-SNAPSHOT.jar delivery-app.jar

# Expose the application port (optional for documentation, actual binding is in docker-compose)
#EXPOSE 8021

# Define the entry point to run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "delivery-app.jar"]
