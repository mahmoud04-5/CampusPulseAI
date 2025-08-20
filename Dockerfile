# Stage 1: Build / extract Spring Boot layers
FROM openjdk:21-jdk-slim as builder
WORKDIR application

# Copy built JAR from Maven target (generic ARG to avoid hardcoding name)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Extract layers for efficient Docker caching
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
