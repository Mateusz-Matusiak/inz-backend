FROM openjdk:18
EXPOSE 8080
ADD target/animal-shelter.jar animal-shelter.jar
ENTRYPOINT ["java", "-jar", "/animal-shelter.jar"]