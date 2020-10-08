FROM openjdk:11.0.8-jre-slim
RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app
COPY ./target/starwars-0.0.1-SNAPSHOT.jar $PROJECT_HOME
WORKDIR  $PROJECT_HOME
ENTRYPOINT ["java", "-jar","starwars-0.0.1-SNAPSHOT.jar"]