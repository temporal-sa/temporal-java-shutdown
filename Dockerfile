FROM eclipse-temurin:17-jdk-jammy as deps

WORKDIR /build

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .

FROM deps as package

WORKDIR /build

COPY ./src src/
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

COPY --from=package /build/build/libs/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
