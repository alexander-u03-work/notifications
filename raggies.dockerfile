FROM gradle:latest AS BUILD

WORKDIR /app
COPY . /app

RUN gradle build --exclude-task test

FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY --from=BUILD /app/build/libs .

ENTRYPOINT ["java", "-jar", "raggies-notification-0.0.1-SNAPSHOT.jar"]