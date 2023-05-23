default:
	echo "Make Rules: starbucks-network, starbucks-api-run, cashier-nodejs-run, starbucks-app-run"

starbucks-network:
	docker network create --driver bridge starbucks

starbucks-api-run: 
	docker run --network starbucks --name spring-starbucks-api -td -p 8080:8080 paulnguyen/spring-starbucks-api:v3.1

#doesn't work :(
starbucks-cashier-run:
	docker run --network starbucks --name spring-cashier -td -p 9090:9090 paulnguyen/spring-cashier:v2.7.10

cashier-nodejs-run:
	docker run --network starbucks --name starbucks-nodejs -p 90:9090  \
	-e "api_endpoint=http://spring-starbucks-api:8080" -td paulnguyen/starbucks-nodejs:v3.0

starbucks-app-run: 
	java -cp starbucks-app.jar \
		-Dapiurl="http://34.68.215.55:80/api" \
		-Dapikey="Zkfokey2311" \
		-Dregister="5012349" \
		starbucks.Main 2>debug.log

