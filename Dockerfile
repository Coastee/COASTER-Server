FROM openjdk:17-jdk
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080

ARG JASYPT_KEY

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-dev}
ENV JASYPT_KEY=${JASYPT_KEY:-pw}

CMD java -jar -Duser.timezone=Asia/Seoul app.jar