FROM node:19 AS angbuilder

WORKDIR /app

COPY client/src src
COPY client/angular.json .
COPY client/ngsw-config.json .
COPY client/package-lock.json .
COPY client/package.json .
COPY client/tsconfig.app.json .
COPY client/tsconfig.json .
COPY client/tsconfig.spec.json .

RUN npm i -g @angular/cli
RUN npm ci
RUN ng build

FROM maven:3-eclipse-temurin-20 AS javabuilder

WORKDIR /app

COPY server/src src
COPY server/mvnw .
COPY server/mvnw.cmd .
COPY server/pom.xml .
COPY --from=angbuilder /app/dist/client/ /app/src/main/resources/static/

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:20-slim

WORKDIR /app

COPY --from=javabuilder /app/target/server-0.0.1-SNAPSHOT.jar app.jar

ENV NIXPACKS_JDK_VERSION=19

ENV SPRING_DATA_REDIS_CLIENT-TYPE=jedis
ENV SPRING_DATA_REDIS_PORT=123
ENV SPRING_DATA_REDIS_USERNAME=default
ENV SPRING_DATA_REDIS_PASSWORD=123
ENV SPRING_DATA_REDIS_HOST=123

ENV SPRING_DATASOURCE_URL=123

ENV SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENTID=123
ENV SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENTSECRET=123

ENV RSA_PUBLIC_KEY=123
ENV RSA_PRIVATE_KEY=123

ENV GOOGLE_GEOCODE_KEY=123

ENV STRIPE_SECRET_KEY=123
ENV FRONTEND_BASE_URL=123

ENV SECRET_KEY=123

ENV PORT=3000

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar