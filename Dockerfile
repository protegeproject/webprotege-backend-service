FROM openjdk:17-alpine
MAINTAINER protege.stanford.edu

EXPOSE 7770

COPY target/${JAR_FILE} webprotege-backend-service.jar
ENTRYPOINT ["java","-jar","/webprotege-backend-service.jar"]