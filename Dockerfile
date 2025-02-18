FROM gradle:latest AS BUILD

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradlew.bat ./

RUN gradle dependencies --no-daemon

COPY . .

RUN gradle build --no-daemon --exclude-task test

FROM eclipse-temurin:17-jre

RUN useradd -m raggies

COPY --from=build /app/build/libs/*.jar /app/raggies-notification.jar

RUN chown -R raggies:raggies /app

USER raggies

ENTRYPOINT ["java", "-jar", "raggies-notification.jar"]