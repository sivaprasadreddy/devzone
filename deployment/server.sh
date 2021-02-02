#!/bin/bash

declare JAR_NAME="devzone-0.0.1-SNAPSHOT.jar"
declare PID_FILE="devzone.pid"
#set -x

function help() {
    echo "=================================="
    echo "USAGE:"
    echo "Start app: ./run.sh start <profiles>"
    echo "Stop app: ./run.sh stop"
    echo "Restart app: ./run.sh restart <profiles>"
    echo "Ex: ./run.sh start prod,aws"
    echo "=================================="
}

function start() {
    profiles="default"
    if [[ "$1" != ""  ]]
    then
        profiles="$1"
    fi
    echo "Starting the application with profiles: $profiles"
    echo "Java Version: `java --version`"
    nohup java -jar $JAR_NAME --spring.profiles.active=$profiles -Xms1G -Xmx3G > nohup.log &
    echo "Started."
}

function stop() {
    echo "Stopping the application"

    if [ -f "$PID_FILE" ]; then
        echo "Found PID file $PID_FILE"
        kill `cat $PID_FILE`
    fi
}

function restart() {
    stop
    sleep 5
    start $1
    echo "$!" > $PID_FILE
}

function healthcheck() {
    curl http://localhost:8080/actuator/health
}

action="help"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
