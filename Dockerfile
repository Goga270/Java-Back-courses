#FROM tomcat:9.0-alpine
#
#LABEL maintainer="samoilenko@mail.ru"
#
#ADD ./target/experience-exchange-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
#
#EXPOSE 8080

FROM maven:3.6.3-jdk-11 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests

FROM tomcat:9.0-alpine

LABEL maintainer="gosha.aladin@gmial.com"

COPY --from=builder ./target/experience-exchange-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/experience-exchange.war

EXPOSE 8080