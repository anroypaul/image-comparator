FROM eclipse-temurin:21-alpine
MAINTAINER 'pavlok'
COPY target/image-comparator-1.0-SNAPSHOT.jar image-comparator-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/image-comparator-1.0-SNAPSHOT.jar"]