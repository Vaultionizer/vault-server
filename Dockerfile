FROM maven:3.6.3-openjdk-14 as mavenImage
ADD ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
ADD . /home/vaultionizer/project
ADD scripts/build_project_docker.sh build_project.sh
RUN bash build_project.sh

FROM openjdk:14-alpine as slimImage
COPY --from=mavenImage /root/.m2 /root/.m2
COPY --from=mavenImage /home/vaultionizer/vaultionizer_server.jar /home/vaultionizer/vaultionizer_server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/vaultionizer/vaultionizer_server.jar"]