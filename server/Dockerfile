FROM gradle:8.14.1-jdk21-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -q

FROM amazoncorretto:21-alpine3.21 AS final

COPY --from=build /home/gradle/src/server/build/libs/server.jar /app/server.jar
COPY --from=build /home/gradle/src/server/docs /app/docs

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server.jar"]
