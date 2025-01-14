FROM openjdk:8-jre-alpine3.9
ARG JAR_FILE=target/pcps.jar
ADD ${JAR_FILE} pcps.jar
COPY pcps-file /pcps-file
ENTRYPOINT ["java","-jar","/pcps.jar"]
