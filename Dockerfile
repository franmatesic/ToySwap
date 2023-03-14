FROM openjdk:19-alpine
COPY target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]