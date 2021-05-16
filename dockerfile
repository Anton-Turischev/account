FROM openjdk:8-jdk-alpine
COPY ./build/libs/*.jar account_test.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","account_test.jar"]