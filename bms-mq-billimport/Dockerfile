FROM harbor.sh-jiuye.com.cn/library/tomcat:7
MAINTAINER ningyu@jiuyescm.com

ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.1,TLSv1.2 -Djava.net.preferIPv4Stack=true"

RUN rm -rf /usr/local/tomcat/webapps/*

ADD target/classes/server.crt /tmp/server.crt
RUN rm -rf $JAVA_HOME/lib/security/cacerts && \
    keytool -import -trustcacerts -noprompt -alias server -file /tmp/server.crt -keystore  $JAVA_HOME/lib/security/cacerts -storepass Jy123456 && \
    keytool -list -keystore $JAVA_HOME/lib/security/cacerts && \
    rm -rf /tmp/server.crt

ADD target/bms-rest*.war /usr/local/tomcat/webapps/ROOT.war