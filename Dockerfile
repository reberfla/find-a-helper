FROM node:22-alpine3.21 AS ui-build
WORKDIR /app
COPY ./ui/package*.json .
RUN npm install
COPY ./ui .
RUN npm run build

FROM amazoncorretto:21-alpine3.21 AS server-build
COPY . /home/gradle/src
WORKDIR /home/gradle/src


RUN rm server/src/main/resources/logback-test.xml
RUN ./gradlew build --no-daemon

FROM amazoncorretto:21-alpine3.21 AS final

COPY --from=ui-build /app/dist /app/src/main/vue/dist
COPY --from=server-build /home/gradle/src/server/build/libs/server.jar /app/server.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server.jar"]
