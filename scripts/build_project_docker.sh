cd /home/vaultionizer/project

rm target/vaultserver*
export MAVEN_OPTS="-Xmx1024m"

echo "Packaging project"
mvn package -Dmaven.test.skip=true

mv target/*jar ../vaultionizer_server.jar
cd ..


