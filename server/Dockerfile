FROM amazoncorretto:21-alpine3.21 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN rm server/src/main/resources/logback-test.xml
RUN ./gradlew build --no-daemon

FROM amazoncorretto:21-alpine3.21 AS final

COPY --from=build /home/gradle/src/server/build/libs/server.jar /app/server.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server.jar"]
