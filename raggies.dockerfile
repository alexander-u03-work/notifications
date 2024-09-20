FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY build/libs .

CMD ["./gradlew", "clean", "assemble"]

ENTRYPOINT ["java", "-jar", "raggies-notification-0.0.1-SNAPSHOT.jar"]