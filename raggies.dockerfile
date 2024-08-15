FROM amazoncorretto:17-alpine-jdk
EXPOSE 8080
WORKDIR /app
COPY build/libs .
CMD ["./gradlew", "clean", "assemble"]
ENTRYPOINT ["java", "-jar", "raggies-notification-0.0.1-SNAPSHOT.jar"]