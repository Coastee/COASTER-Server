FROM openjdk:17-jdk
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080

ARG JASYPT_KEY

ENV JASYPT_KEY=${JASYPT_KEY:-pw}

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait /wait
RUN chmod +x /wait
CMD /wait && java -jar -Duser.timezone=Asia/Seoul app.jar