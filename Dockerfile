FROM openjdk:9-jdk
VOLUME /tmp
COPY target/miw-task-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
