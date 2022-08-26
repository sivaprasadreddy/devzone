#!/bin/bash

declare project_dir=$(dirname "$0")
declare dc_app_deps=${project_dir}/docker/docker-compose.yml
declare dc_app=${project_dir}/docker/docker-compose-app.yml
declare dc_elk=${project_dir}/docker/docker-compose-elk.yml
declare dc_monitoring=${project_dir}/docker/docker-compose-monitoring.yml
declare devzone="devzone"
declare elk="  elasticsearch logstash kibana"
declare monitoring="prometheus loki grafana"

function build_api() {
    ./gradlew clean bootJar
}

function start() {
    echo "Starting dependent docker containers...."
    docker-compose -f "${dc_app_deps}" up --build --force-recreate -d
    docker-compose -f "${dc_app_deps}" logs -f
}

function stop() {
    echo "Stopping dependent docker containers...."
    docker-compose -f "${dc_app_deps}" stop
    docker-compose -f "${dc_app_deps}" rm -f
}

function restart() {
    stop
    start
}

function start_app() {
    echo "Starting ${devzone} and dependencies...."
    build_api
    docker-compose -f "${dc_app_deps}" -f "${dc_app}" up --build --force-recreate -d
    docker-compose -f "${dc_app_deps}" -f "${dc_app}" logs -f
}

function stop_app() {
    echo 'Stopping all....'
    # shellcheck disable=SC2086
    docker-compose -f ${dc_app_deps} -f ${dc_app} stop
    docker-compose -f "${dc_app_deps}" -f "${dc_app}" rm -f
}

function start_monitoring() {
    echo 'Starting Prometheus, Grafana....'
    docker-compose -f "${dc_monitoring}" up --build --force-recreate -d
    docker-compose -f "${dc_monitoring}" logs -f
}
function stop_monitoring() {
    echo 'Stopping monitoring....'
    # shellcheck disable=SC2086
    docker-compose -f ${dc_monitoring} stop
    docker-compose -f "${dc_monitoring}" rm -f
}

function start_elk() {
    echo 'Starting ELK....'
    docker-compose -f "${dc_elk}" up --build --force-recreate -d
    docker-compose -f "${dc_elk}" logs -f
}
function stop_elk() {
    echo 'Stopping ELK....'
    # shellcheck disable=SC2086
    docker-compose -f ${dc_elk} stop
    docker-compose -f "${dc_elk}" rm -f
}

function k8sdeploy() {
    echo 'Deploying to kubernetes....'
    kubectl apply -f k8s/config.yaml
    kubectl apply -f k8s/postgresdb.yaml
    sleep 5
    kubectl apply -f k8s/devzone.yaml
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$*
fi

eval "${action}"
