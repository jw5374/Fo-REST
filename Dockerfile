FROM openjdk:8-jdk-alpine
COPY fo-rest/target/fo-rest-0.0.1-SNAPSHOT.jar fo-rest-0.0.1.jar
ENTRYPOINT ["java","-jar","/fo-rest-0.0.1.jar"]
EXPOSE 5000