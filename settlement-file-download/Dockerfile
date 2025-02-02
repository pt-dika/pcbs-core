FROM openjdk:8-jre-alpine3.9
ARG JAR_FILE=target/pcbs-settlement-file-download-1.0.jar
ADD ${JAR_FILE} pcbs-settlement-file-download-1.0.jar
#COPY pcps-file /pcps-file
ENTRYPOINT ["java","-Duser.timezone=Asia/Jakarta","-jar","/pcbs-settlement-file-download-1.0.jar"]