cd eureka-server/
mvn clean && mvn package -DskipTests
docker build . -t eureka-server
cd ..

cd config-service/
mvn clean && mvn package -DskipTests
docker build . -t config-service
cd ..

cd movie-service/
mvn clean && mvn package -DskipTests
docker build . -t movie-service
cd ..

cd catalog-service/
mvn clean && mvn package -DskipTests
docker build . -t catalog-service
cd ..

cd series-service/
mvn clean && mvn package -DskipTests
docker build . -t series-service
cd ..

cd api-gateway/
mvn clean && mvn package -DskipTests
docker build . -t api-gateway
cd ..

docker-compose up
