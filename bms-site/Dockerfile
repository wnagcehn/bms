FROM harbor.sh-jiuye.com.cn/library/tomcat:7
MAINTAINER litao@jiuyescm.com

ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.1,TLSv1.2 -Djava.net.preferIPv4Stack=true"
RUN rm -rf /usr/local/tomcat/webapps/*
ADD target/classes/jssecacerts $JAVA_HOME/lib/security/jssecacerts
RUN rm -rf $JAVA_HOME/lib/security/cacerts && \
    keytool -list -keystore $JAVA_HOME/lib/security/jssecacerts -storepass changeit
ADD target/bms-site*.war /usr/local/tomcat/webapps/ROOT.war