FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/ms-authentication-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
