FROM maven:3.6.3-openjdk-14 as build_step

ADD ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

ADD . /home/vaultionizer/project
ADD scripts/build_project_docker.sh build_project.sh
RUN build_project.sh

FROM openjdk:14-alpine
COPY --from=build_step /home/vaultionizer/vaultionizer_server.jar /home/vaultionizer/vaultionizer_server.jar
EXPOSE 443
ENTRYPOINT ["java", "-jar", "/home/vaultionizer/vaultionizer_server.jar"]
