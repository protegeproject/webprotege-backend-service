FROM openjdk:17-alpine
MAINTAINER protege.stanford.edu

EXPOSE 7770

COPY target/webprotege-backend-service-0.1.1-SNAPSHOT.jar webprotege-backend-service-0.1.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/webprotege-backend-service-0.1.1-SNAPSHOT.jar"]