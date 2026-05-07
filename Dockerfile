FROM eclipse-temurin:17-jre
MAINTAINER protege.stanford.edu

EXPOSE 7770
ARG JAR_FILE
COPY target/${JAR_FILE} webprotege-backend-service.jar
ENTRYPOINT ["java","-jar","/webprotege-backend-service.jar"]