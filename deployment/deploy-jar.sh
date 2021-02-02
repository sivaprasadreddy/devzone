#!/bin/bash
declare USER="siva"
declare SERVER="192.168.33.20"
declare USER_AT_SERVER="$USER@$SERVER"
declare JAR_NAME="devzone-0.0.1-SNAPSHOT.jar"
declare JAR_PATH="./build/libs/$JAR_NAME"
declare APP_DIR_ON_SERVER="/home/$USER/apps/"

ssh "$USER_AT_SERVER" mkdir -p $APP_DIR_ON_SERVER
scp $JAR_PATH "$USER_AT_SERVER":$APP_DIR_ON_SERVER
scp ./deployment/server.sh "$USER_AT_SERVER":$APP_DIR_ON_SERVER
scp ./deployment/overrides.properties "$USER_AT_SERVER":$APP_DIR_ON_SERVER/application.properties

ssh "$USER_AT_SERVER" /bin/bash << EOF
    cd $APP_DIR_ON_SERVER
    chmod a+x $JAR_NAME
    chmod a+x server.sh
    nohup ./server.sh restart > nohup.out 2> nohup.err < /dev/null &
EOF
