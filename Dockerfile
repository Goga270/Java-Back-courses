FROM tomcat:9.0-alpine

LABEL maintainer="samoilenko@mail.ru"

ADD ./target/experience-exchange-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

EXPOSE 8080