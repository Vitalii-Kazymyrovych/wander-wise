#Builder stage
FROM openjdk:17-jdk-slim as builder
WORKDIR ww
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ww.jar
RUN java -Djarmode=layertools -jar ww.jar extract

#Final stage
FROM openjdk:17-jdk-slim
WORKDIR ww
COPY --from=builder ww/dependencies/ ./
COPY --from=builder ww/spring-boot-loader/ ./
COPY --from=builder ww/snapshot-dependencies/ ./
COPY --from=builder ww/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
