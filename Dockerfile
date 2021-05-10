FROM maven:3.8.1-openjdk-15 as build_step

ADD ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

ADD . /home/vaultionizer/project
ADD scripts/test.sh test.sh
ADD scripts/build_project_docker.sh build_project.sh
RUN bash build_project.sh

FROM openjdk:15-alpine
COPY --from=build_step /home/vaultionizer/vaultionizer_server.jar /home/vaultionizer/vaultionizer_server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/vaultionizer/vaultionizer_server.jar"]
