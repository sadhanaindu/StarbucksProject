clean:
	mvn clean

compile:
	mvn compile

run:
	mvn spring-boot:run

build:
	mvn package

run-jar:
	java -jar target/spring-gumball-1.0.jar

# Docker

docker-build: build
	docker build -t spring-gumball .
	docker images

docker-run: docker-build
	docker run --name spring-gumball -td -p 80:8080 spring-gumball
	docker ps

docker-clean:
	docker stop spring-gumball
	docker rm spring-gumball
	docker rmi spring-gumball

docker-shell:
	docker exec -it spring-gumball bash

docker-push:
	docker login
	docker build -t $(account)/spring-gumball:v1.0 .
	docker push $(account)/spring-gumball:v1.0

#Jumpbox
jumpbox:
	docker run --network spring-gumball_network --name jumpbox -t -d ubuntu

# Compose

network-ls:
	docker network ls

network-create:
	docker network create --driver bridge $(network)

network-prune:
	docker network prune

compose-up:
	docker-compose up --scale gumball=2 -d

compose-down:
	docker-compose down

lb-stats:
	echo "user = admin | password = admin"
	open http://localhost:1936

lb-test:
	open http://localhost