FROM harbor.sh-jiuye.com.cn/library/tomcat:7
MAINTAINER litao@jiuyescm.com

RUN rm -rf /usr/local/tomcat/webapps/*

ADD target/bms-mq-import*.war /usr/local/tomcat/webapps/ROOT.war