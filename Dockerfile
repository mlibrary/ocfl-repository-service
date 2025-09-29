FROM gradle:8.14.3-jdk24-corretto AS base

WORKDIR /app/

ENV GRADLE_USER_HOME /gradle/

COPY build.gradle settings.gradle gradle.properties ./
COPY src/ ./src/

EXPOSE 8080

CMD ["gradle", "build"]

FROM gradle:8.14.3-jdk24-corretto AS build

WORKDIR /app/

COPY --from=base /app ./

RUN gradle bootJar

FROM amazoncorretto:24 AS deployment

ARG JAR_NAME_ARG

ENV JAR_NAME=${JAR_NAME_ARG}

WORKDIR /app/

COPY --from=build /app/build/libs/${JAR_NAME_ARG} ./

EXPOSE 8080

ENTRYPOINT java -jar ${JAR_NAME}
