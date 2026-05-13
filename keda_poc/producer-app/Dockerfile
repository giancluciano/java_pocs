# syntax=docker/dockerfile:1.7

FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn
RUN --mount=type=cache,target=/root/.m2 mvn -B -e -q dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -e -q package -DskipTests \
 && mvn -B -e -q dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --home /app app
COPY --from=build /build/target/producer-app-1.0-SNAPSHOT.jar /app/app.jar
COPY --from=build /build/target/lib /app/lib
USER app

ENTRYPOINT ["java", "-cp", "/app/app.jar:/app/lib/*", "com.example.App"]
