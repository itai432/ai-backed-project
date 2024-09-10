FROM openjdk:17
COPY target/aibackend-0.0.1-SNAPSHOT.jar /usr/src/aibackend.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/aibackend.jar", "--spring.config.location=file:/opt/conf/application.properties"]