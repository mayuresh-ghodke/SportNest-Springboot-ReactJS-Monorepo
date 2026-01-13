# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy parent pom and modules
COPY ../pom.xml .
COPY ../Library ./Library
COPY . ./Customer

# Build only Customer module (and required dependencies)
RUN mvn clean package -pl Customer -am -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=builder /build/Customer/target/*jar app.jar

EXPOSE 8020

ENTRYPOINT ["java", "-jar", "app.jar"]

# Use official OpenJDK image
#FROM eclipse-temurin:17
#
## Set working directory inside the container
#WORKDIR /app
#
## Copy the JAR file to the container
#COPY target/Customer-0.0.1-SNAPSHOT.jar customer-app.jar
#
## Expose the port
#EXPOSE 8020
#
## Run the application
#ENTRYPOINT ["java", "-jar", "customer-app.jar"]
