cd /home/vaultionizer/project

rm target/vaultserver*
export MAVEN_OPTS="-Xmx1024m"

echo "Packaging project"
mvn package

mv target/*jar ../vaultionizer_server.jar
cd ..


