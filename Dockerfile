# Stage 0: Build with Maven (using openjdk:21-jdk-slim)
FROM openjdk:21-jdk-slim as build
WORKDIR /workspace

# Install Maven (only in build stage, not runtime)
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src

# Build JAR (skip tests for speed)
RUN mvn clean package -DskipTests

# Stage 1: Extract Spring Boot layers
FROM openjdk:21-jdk-slim as builder
WORKDIR application

COPY --from=build /workspace/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Runtime container (⚡ must also be Java 21)
FROM openjdk:21-jdk-slim
LABEL PROJECT_NAME=campuspulseai \
      PROJECT=campuspulseai

WORKDIR application
EXPOSE 8080

# Copy Spring Boot layers from builder (⚡ order matters!)
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./

# Run with JarLauncher (present in spring-boot-loader)
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.launch.JarLauncher"]
