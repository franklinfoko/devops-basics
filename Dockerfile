FROM openjdk:17
ENV TZ="Africa/Douala"
COPY target/*.jar /
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/medigorx-0.0.1.jar"]
