DOCKER_TAG ?= $(shell git rev-parse --short HEAD)
DOCKER_PREFIX ?= quay.io/yodo-io
PROJECT := whisper-api

DOCKER_IMAGE := $(DOCKER_PREFIX)/$(PROJECT):$(DOCKER_TAG)

default: clean docker-build

target/dependency:
	./mvnw package

docker-build: target/dependency
	docker build -t $(DOCKER_IMAGE) .

docker-push: docker-build
	docker push $(DOCKER_IMAGE)

clean:
	./mvnw clean
