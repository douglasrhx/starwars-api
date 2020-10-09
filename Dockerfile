FROM maven:3.6.0-jdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11.0.8-jre-slim
COPY --from=build /usr/src/app/target/starwars-0.0.1-SNAPSHOT.jar /usr/app/starwars-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/starwars-0.0.1-SNAPSHOT.jar"]