# DevZone

[![Build](https://github.com/sivaprasadreddy/devzone/actions/workflows/gradle.yml/badge.svg)](https://github.com/sivaprasadreddy/devzone/actions/workflows/gradle.yml) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sivaprasadreddy_devzone&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sivaprasadreddy_devzone)

### Run locally

`$ ./gradlew bootRun`

### Run Gatling Tests

`$ ./gradlew gatlingRun`

### Important Links

* Application: http://localhost:8080 
* Prometheus: http://localhost:9090
* Grafana: http://localhost:3000
* Kibana: http://localhost:5601/

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
