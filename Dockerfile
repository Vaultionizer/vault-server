FROM maven:3.6.3-openjdk-14
ADD . /home/vaultionizer/project
ADD scripts/build_project_docker.sh build_project.sh
EXPOSE 8080
RUN bash build_project.sh
ENTRYPOINT ["java", "-jar", "/home/vaultionizer/vaultionizer_server.jar"]