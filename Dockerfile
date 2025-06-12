FROM openjdk:17-jdk-alpine

WORKDIR /app

EXPOSE 8080

COPY target/forum-0.0.1-SNAPSHOT.jar forum-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","forum-0.0.1-SNAPSHOT.jar"]