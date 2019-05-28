FROM harbor.sh-jiuye.com.cn/library/tomcat:7
MAINTAINER litao@jiuyescm.com

ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.1,TLSv1.2 -Djava.net.preferIPv4Stack=true \
-Dfile.encoding=UTF8 \
-server \
-Xms512m \
-Xmx512m \
-XX:PermSize=512m \
-XX:MaxPermSize=512m \
-Xss512k \
-XX:+UseParallelGC \
-XX:MaxGCPauseMillis=1500 \
-XX:GCTimeRatio=9 \
-XX:+DisableExplicitGC \
-Ddisconf.conf_server_host=192.168.0.58:8084 -Ddisconf.env=dev"

RUN rm -rf /usr/local/tomcat/webapps/*

ADD target/bms-mq-calcu*.war /usr/local/tomcat/webapps/ROOT.war
