#!/bin/bash

CONTAINER_IP=$(curl http://rancher-metadata.rancher.internal/latest/self/host/agent_ip)
CONTAINER_NAME=$(hostname)

if [ ! -z $CONTAINER_IP ];then
    echo "127.0.0.1 localhost" > /etc/hosts
    echo "$CONTAINER_IP $CONTAINER_NAME" >> /etc/hosts
fi

CONFIG_PARAM=""
if [ ! -z "$CONFIG_HOST" ];then
    CONFIG_PARAM="$CONFIG_PARAM -Ddisconf.conf_server_host=$CONFIG_HOST "
fi
if [ ! -z "$CONFIG_ENV" ];then
    CONFIG_PARAM="$CONFIG_PARAM -Ddisconf.env=$CONFIG_ENV "
fi

java ${CONFIG_PARAM} -DLOG_HOME=${LOG_HOME} ${JAVA_OPTS} -jar /usr/local/dubbo/bms-job-*.jar

exec "$@"