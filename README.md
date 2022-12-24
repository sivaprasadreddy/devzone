# DevZone
DevZone is a web application where developers can register and post their favourite article/video posts. 

## Features
* Users can register and login
* Authenticated user can create a new post under a category
* Authenticated user can delete own posts
* Admin user can delete any post
* Any user(including guest users) can view posts with pagination
  * sort by posted date desc (default)
  * by category
  * by searching for a keyword in title

[![GitHub Build](https://github.com/sivaprasadreddy/devzone/actions/workflows/gradle.yml/badge.svg)](https://github.com/sivaprasadreddy/devzone/actions/workflows/gradle.yml)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/sivaprasadreddy/devzone/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/sivaprasadreddy/devzone/tree/main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sivaprasadreddy_devzone&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sivaprasadreddy_devzone)

## Tech Stack
* [SpringBoot](https://spring.io/projects/spring-boot)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* [Spring Security](https://spring.io/projects/spring-security)
* [Postgres](https://www.postgresql.org/)
* [Thymeleaf](https://www.thymeleaf.org/)
* [Testcontainers](https://www.testcontainers.org/)
* [ArchUnit](https://www.archunit.org/)
* [Jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
* [Gradle](https://gradle.org/)
* [JUnit 5](https://junit.org/junit5/)
* [SonarQube](https://www.sonarqube.org/)
* [JaCoCo](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
* [GH Actions](https://github.com/features/actions)
* [DockerCompose](https://docs.docker.com/compose/)
* [Kubernetes](https://kubernetes.io/)
* [Helm Charts](https://helm.sh/)
* [Skaffold](https://skaffold.dev/)
* [ELK](https://www.elastic.co/what-is/elk-stack)
* [Prometheus](https://prometheus.io/)
* [Grafana](https://grafana.com/)
* [Loki](https://grafana.com/oss/loki/)
* [Gatling](https://gatling.io/) Performance Tests
* [PlayWright](https://playwright.dev/) E2E Tests

## How to run?

### Run application locally

`$ ./gradlew bootRun`

### Run application using docker-compose

`$ ./run.sh start_app`

### Deploying on kubernetes

```shell
$ cd deployment
$ ./kind/kind-cluster.sh create
$ ./run.sh k8sdeploy
$ curl http://localhost:30090/actuator
$ curl http://localhost/actuator
$ ./run.sh k8sundeploy
$ ./kind/kind-cluster.sh destroy
```

### Development using Skaffold

```shell
$ skaffold dev --port-forward --skip-tests=true
$ curl http://localhost:8080/actuator
```
### Run Gatling Tests

`$ ./gradlew gatlingRun`

### Run PlayWright E2E Tests

```
$ ./gradlew :playwright-e2e-tests:e2eTest
$ export CONFIG_FILE=dev.json
$ ./gradlew :playwright-e2e-tests:e2eTest
```

## ELK Stack
* Start ELK stack using `$ ./run.sh start_elk`
* Go to http://localhost:5601/
* Analytics -> Discover -> create a data view -> Name: "devzone", Index pattern: "devzone*", Timestamp field: "@timestamp"

**Note:** Logstash is configured to read log files, so in order to initialize the index make few requests to devzone application to generate some logs.

## Monitoring

* Start Prometheus, Grafana, Loki using `$ ./run.sh start_monitoring`
* Few Dashboards are already pre-configured to show SpringBoot application Metrics

### Loki - Log management
* Navigate to http://localhost:3000/datasources
* Click on Add datasource -> Select Loki
* Enter URL as http://loki:3100 (Host "loki" is based on name given to loki container in docker-compose-monitoring.yml file)
* Click on Save & Test
* Click on Explore in the Left Nav and Select Loki
* In Log browser input text enter `{job="devzone"}`

## Important Links

* Application: http://localhost:8080
* Prometheus: http://localhost:9090
* Grafana: http://localhost:3000
* Kibana: http://localhost:5601/
