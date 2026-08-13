# Multi-stage Docker build for Akhilan Mart
# Stage 1: Build WAR package with Maven & Java 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run application on Tomcat 9 with JDK 17
FROM tomcat:9.0-jdk17-temurin
WORKDIR /usr/local/tomcat

# Remove default ROOT application
RUN rm -rf webapps/ROOT webapps/ROOT.war

# Copy compiled WAR artifact from builder stage as ROOT.war
COPY --from=builder /app/target/akhilanmart.war webapps/ROOT.war

# Expose HTTP port
EXPOSE 8080

CMD ["catalina.sh", "run"]
