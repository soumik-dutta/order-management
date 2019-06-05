FROM java:8
EXPOSE 8761
ADD /target/discovery-service.jar discovery-service.jar
ENTRYPOINT ["java","-jar","discovery-service.jar"]