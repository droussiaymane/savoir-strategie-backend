FROM maven:3.9.0-amazoncorretto-11 AS build

WORKDIR usr/src/app

COPY . ./

ARG DB_SERVER
ARG DB_USERNAME
ARG DB_PASSWORD

ENV SPRING_PROFILES_ACTIVE=production
ENV DB_SERVER=${DB_SERVER}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}


RUN mvn clean install

FROM openjdk:11-jre


# Refer to Maven build -> finalName
ARG JAR_FILE=target/spring-boot-build-app.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY --from=build /usr/src/app/${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]