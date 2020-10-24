FROM openjdk:11.0.9-jdk-slim-buster as build
RUN apt-get update && apt-get install -y --no-install-recommends \
    telnet
COPY ./target /build
WORKDIR /build
FROM openjdk:11.0.9-jre-slim-buster

RUN useradd --create-home --shell /bin/bash app
USER app
COPY --from=build --chown=app:app /build/app.jar /app/
EXPOSE 8080
WORKDIR /app
CMD ["java", "-Djava.security.egd=file:///dev/urandom", "-jar" , "app.jar"]
