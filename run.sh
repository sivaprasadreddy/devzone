#!/bin/bash

declare project_dir=$(dirname $0)
declare dc_setup=${project_dir}/docker/docker-compose.yml
declare dc_app=${project_dir}/docker/docker-compose-app.yml
declare devzone="devzone"

function restart() {
    stop
    start
}

function start() {
    echo "Starting dependent docker containers...."
    docker-compose -f ${dc_setup} up --build --force-recreate -d
    docker-compose -f ${dc_setup} logs -f
}

function stop() {
    echo "Stopping dependent docker containers...."
    docker-compose -f ${dc_setup} stop
    docker-compose -f ${dc_setup} rm -f
}

function start_app() {
    echo "Starting ${devzone} and dependencies...."
    build_api
    docker-compose -f ${dc_setup} -f ${dc_app} up --build --force-recreate -d
    docker-compose -f ${dc_setup} -f ${dc_app} logs -f
}

function stop_app() {
    echo 'Stopping all....'
    docker-compose -f ${dc_setup} -f ${dc_app} stop
    docker-compose -f ${dc_setup} -f ${dc_app} rm -f
}

function build_api() {
    ./gradlew clean build
}


action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
