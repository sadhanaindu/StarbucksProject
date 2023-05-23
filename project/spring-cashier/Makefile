all: clean

build:
	mvn package

clean:
	mvn clean

compile:
	mvn compile

run: compile
	mvn spring-boot:run

# Docker

docker-build: build
	docker build --platform=linux/amd64 -t spring-cashier .
	docker images

docker-run: docker-build
	docker run --network starbucks --platform=linux/amd64 --name spring-cashier -td -p 90:9090 spring-cashier
	docker ps

docker-clean:
	docker stop spring-cashier
	docker rm spring-cashier
	docker rmi spring-cashier

docker-shell:
	docker exec -it spring-cashier bash

docker-push:
	docker login
	docker build --platform=linux/amd64 -t $(account)/spring-cashier:v2.1 .
	docker push $(account)/spring-cashier:v2.1

# Compose

network-ls:
	docker network ls

network-create:
	docker network create --driver bridge starbucks

network-prune:
	docker network prune

compose-up:
	docker-compose up --network starbucks --scale cashier=2 -d

lb-up:
	docker-compose up -d lb

cashier-up:
	docker-compose up -d cashier

redis-up:
	docker-compose up -d redis

mysql-up:
	docker-compose up -d mysql

compose-down:
	docker-compose down

lb-stats:
	echo "user = admin | password = admin"
	open http://localhost:1936

lb-test:
	open http://localhost

# Deployment

deployment-create:
	kubectl create -f deployment.yaml --save-config