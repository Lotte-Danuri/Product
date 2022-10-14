FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/Product-0.0.1-SNAPSHOT.jar ProductServer.jar
ENTRYPOINT ["java","-jar","ProductServer.jar"]