FROM harbor.sh-jiuye.com.cn/library/openjdk:8-jre
MAINTAINER ningyu@jiuyescm.com

ADD target/*.zip /tmp/bms.zip
ADD entrypoint.sh /tmp/entrypoint.sh

RUN unzip /tmp/bms.zip -d /usr/local/ && mv /usr/local/bms-service-* /usr/local/dubbo \
    && mv /tmp/entrypoint.sh /usr/local/dubbo/entrypoint.sh \
    && sed -i 's/\r$//' /usr/local/dubbo/entrypoint.sh \
    && chmod +x ./usr/local/dubbo/entrypoint.sh \
    && rm -rf /tmp/bms.zip
    
WORKDIR /usr/local/dubbo/

EXPOSE 20814

ENV CONFIG_HOST="192.168.0.58:8084"
ENV CONFIG_ENV="dev"
ENV LOG_HOME="/usr/local/dubbo/logs"
ENV JAVA_OPTS="\
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
-XX:+DisableExplicitGC"

ENTRYPOINT ["./entrypoint.sh"]