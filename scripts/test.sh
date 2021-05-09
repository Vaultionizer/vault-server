# start test vaultionizer postgres

export VAULT_ENABLE_SSL=false
export VAULT_DB_USER=postgres
export VAULT_DB_PASSWORD=password
export VAULT_DB_DATABASE=mydb
export VAULT_DB_HOST=localhost:5432

docker-compose up --build

cd ..
mvn test
docker stop scripts_postgres_vault_test_1
docker rm scripts_postgres_vault_test_1

