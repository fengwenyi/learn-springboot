#FROM openjdk:8-jdk-alpine
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD springboot-jenkins-docker.jar springboot-jenkins-docker.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/springboot-jenkins-docker.jar"]