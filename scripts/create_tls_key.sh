cd ..

rm vaultionizerServer.p12
rm src/main/resources/keystore/vaultionizerServer.p12

keytool -genkeypair -alias vaultionizerServer -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore vaultionizerServer.p12 -validity 3650

mkdir src/main/resources/keystore
cp vaultionizerServer.p12 src/main/resources/keystore/vaultionizerServer.p12

echo "Created key and moved to correct location. You must now change the password in applications.properties according to the password you just decided on."