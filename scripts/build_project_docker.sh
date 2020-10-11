cd /home/vaultionizer/project

rm target/vaultserver*

echo "installing dependencies..."
mvn clean install -o > /home/vaultionizer/log_install.txt
echo "Finished installing dependencies."
echo "Packaging project"
mvn package  -Dmaven.test.skip=true

mv target/*jar ../vaultionizer_server.jar
cd ..


