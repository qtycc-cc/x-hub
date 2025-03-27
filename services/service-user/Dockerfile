FROM openjdk:21

WORKDIR /app

EXPOSE 8000

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]