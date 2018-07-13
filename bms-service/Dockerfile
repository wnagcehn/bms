FROM harbor.sh-jiuye.com.cn/library/jre:7
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

ENTRYPOINT ["./entrypoint.sh"]