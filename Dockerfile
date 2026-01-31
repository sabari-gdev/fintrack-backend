# Use official Maven image for build
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Use JRE for runtime (smaller image)
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/fintrack-backend-1.0.0.jar app.jar

# Expose port (Render sets PORT env variable)
EXPOSE 8080

# Run application
CMD ["java", "-jar", "app.jar"]
```

**Also create:** `.dockerignore`
```
target/
.mvn/
.idea/
*.iml
.env
.DS_Store