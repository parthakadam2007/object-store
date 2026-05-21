FROM eclipse-temurin:25-jre

WORKDIR /app

ENV DB_URL_AUTH=jdbc:postgresql://host.docker.internal:5432/object_store_auth

COPY target/auth_service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT [ "java" , "-jar" ,"app.jar" ]