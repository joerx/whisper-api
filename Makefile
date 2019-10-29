GIT_REF ?= $(shell git branch --show-current)
TAG ?= $(shell basename $(GIT_REF) | sed s/^master$$/latest/)
DOCKER_PREFIX ?= quay.io/yodo-io
PROJECT := whisper-backend

DOCKER_IMAGE := $(DOCKER_PREFIX)/$(PROJECT):$(TAG)

default: clean docker-build

target/dependency:
	./mvnw package

docker-build: target/dependency
	docker build -t $(DOCKER_IMAGE) .

docker-push: docker-build
	docker push $(DOCKER_IMAGE)

clean:
	./mvnw clean
