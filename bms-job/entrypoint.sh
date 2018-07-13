#!/bin/bash

CONTAINER_IP=$(curl http://rancher-metadata.rancher.internal/latest/self/host/agent_ip)
CONTAINER_NAME=$(hostname)

echo "127.0.0.1 localhost" > /etc/hosts
echo "$CONTAINER_IP $CONTAINER_NAME" >> /etc/hosts

java -jar /usr/local/dubbo/bms-job-*.jar

exec "$@"