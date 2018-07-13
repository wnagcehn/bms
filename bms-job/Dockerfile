FROM harbor.sh-jiuye.com.cn/library/jre:7
MAINTAINER ningyu@jiuyescm.com

ADD target/*.zip /tmp/bmsjob.zip
ADD entrypoint.sh /tmp/entrypoint.sh

RUN unzip /tmp/bmsjob.zip -d /usr/local/ && mv /usr/local/bms-job-* /usr/local/dubbo \
    && mv /tmp/entrypoint.sh /usr/local/dubbo/entrypoint.sh \
    && sed -i 's/\r$//' /usr/local/dubbo/entrypoint.sh \
    && chmod +x ./usr/local/dubbo/entrypoint.sh \
    && rm -rf /tmp/bmsjob.zip
    
WORKDIR /usr/local/dubbo/

EXPOSE 50801

ENTRYPOINT ["./entrypoint.sh"]

