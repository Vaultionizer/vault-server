DOMAIN_NAME=abc.de

cd ..

rm vaultionizerServer.p12
rm src/main/resources/keystore/vaultionizerServer.p12

git clone https://github.com/certbot/certbot
cd certbot
sudo ./certbot-auto certonly -a standalone -d $DOMAIN_NAME -d www.$DOMAIN_NAME

openssl pkcs12 -export -in /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -inkey /etc/letsencrypt/live/$DOMAIN_NAME/privkey.pem -out vaultionizerServer.p12 -name vaultionizerServer -CAfile chain.pem -caname root

mkdir src/main/resources/keystore
cp vaultionizerServer.p12 src/main/resources/keystore/vaultionizerServer.p12

echo "Created key and moved to correct location. You must now change the password in applications.properties according to the password you just decided on."