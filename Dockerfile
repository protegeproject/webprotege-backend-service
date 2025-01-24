FROM openjdk:17
MAINTAINER protege.stanford.edu

EXPOSE 7770
ARG JAR_FILE
COPY target/${JAR_FILE} webprotege-backend-service.jar
ENTRYPOINT ["sh", "-c", "java $JVM_ENV -jar /webprotege-backend-service.jar"]