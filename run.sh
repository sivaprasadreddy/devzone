#!/bin/bash

declare project_dir=$(dirname $0)
declare dc_app_deps=${project_dir}/docker/docker-compose.yml
declare dc_app=${project_dir}/docker/docker-compose-app.yml
declare dc_sonarqube=${project_dir}/docker/docker-compose-sonarqube.yml
declare dc_elk=${project_dir}/docker/docker-compose-elk.yml
declare dc_monitoring=${project_dir}/docker/docker-compose-monitoring.yml
declare devzone="devzone"
declare sonarqube="sonarqube"
declare elk="elasticsearch logstash kibana"
declare monitoring="prometheus loki grafana"

function restart() {
    stop
    start
}

function start() {
    echo "Starting dependent docker containers...."
    docker-compose -f ${dc_app_deps} up --build --force-recreate -d
    docker-compose -f ${dc_app_deps} logs -f
}

function stop() {
    echo "Stopping dependent docker containers...."
    docker-compose -f ${dc_app_deps} stop
    docker-compose -f ${dc_app_deps} rm -f
}

function start_app() {
    echo "Starting ${devzone} and dependencies...."
    build_api
    docker-compose -f ${dc_app_deps} -f ${dc_app} up --build --force-recreate -d
    docker-compose -f ${dc_app_deps} -f ${dc_app} logs -f
}

function stop_app() {
    echo 'Stopping all....'
    docker-compose -f ${dc_app_deps} -f ${dc_app} stop
    docker-compose -f ${dc_app_deps} -f ${dc_app} rm -f
}

function build_api() {
    ./gradlew clean bootJar
}

function sonarqube() {
    echo 'Starting sonarqube....'
    docker-compose -f ${dc_sonarqube} up --build --force-recreate -d ${sonarqube}
    docker-compose -f ${dc_sonarqube} logs -f
}

function elk() {
    echo 'Starting ELK....'
    docker-compose -f ${dc_elk} up --build --force-recreate -d ${elk}
    docker-compose -f ${dc_elk} logs -f
}

function monitoring() {
    echo 'Starting Prometheus, Grafana....'
    docker-compose -f ${dc_monitoring} up --build --force-recreate -d ${monitoring}
    docker-compose -f ${dc_monitoring} logs -f
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
