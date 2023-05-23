clean:
	mvn clean

compile:
	mvn compile

run:
	mvn spring-boot: run

build:
	mvn package

run-jar:
	java -jar target/$(jar)

# Docker

docker-build: jar
	docker build -t spring-hello .
	docker images

docker-run: docker-build
	docker run --name spring-hello -td -p 80:8080 spring-hello	
	docker ps

docker-clean:
	docker stop spring-hello
	docker rm spring-hello
	docker rmi spring-hello

docker-shell:
	docker exec -it spring-hello bash 

docker-push:
	docker login
	docker build -t $(account)/spring-hello:latest -t $(account)/spring-hello:latest .
	docker push $(account)/spring-hello:latest 