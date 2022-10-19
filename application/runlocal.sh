#!/bin/bash

declare jar_name='build/libs/devzone-0.0.1.jar'

function build_docker_image() {
    ./gradlew bootBuildImage
}

function build_api() {
    ./gradlew clean bootJar
}

function start() {
  build_api
  profiles="default"
  #vmargs="-Xlog:gc*=debug:stdout -Xlog:gc*=debug:file=gc.log"
  vmargs="-Xlog:gc*=debug:file=logs/gc.log"
  if [[ "$1" != ""  ]]
  then
      profiles="$1"
  fi
  nohup java $vmargs -jar $jar_name \
        --spring.profiles.active=$profiles \
        -Xms1G -Xmx3G > nohup.out &
  echo "$!" > "devzone.pid"
  echo "Started Devzone application with PID: `cat devzone.pid`"
  tail -f nohup.out
}

function stop() {
    #kill -9 $(ps aux | grep $jar_name | grep -v grep | awk '{print $2}')
    if [ -f "devzone.pid" ]; then
        echo "Killing process with PID: `cat devzone.pid`"
        kill -9 `cat devzone.pid`
    fi
}

function restart() {
    stop
    sleep 5
    start $1
}

action="restart"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
