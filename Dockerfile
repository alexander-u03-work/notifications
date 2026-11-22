FROM eclipse-temurin:21-jdk AS source

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew ./
COPY src src

FROM source AS build

ARG BUILD_COMMIT=local
RUN ./gradlew --no-daemon bootJar

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd -m raggies

COPY --from=build /app/build/libs/*.jar /app/raggies-notifications.jar

RUN chown -R raggies:raggies /app

USER raggies

ENTRYPOINT ["java", "-jar", "raggies-notifications.jar"]
