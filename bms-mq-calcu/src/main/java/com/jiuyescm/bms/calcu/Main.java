package com.jiuyescm.bms.calcu;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class.getName());
    private static ClassPathXmlApplicationContext context;

    private Main() {
    }

    public static void main(String[] args) {
        try {
            context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
            context.start();
            logger.info(">>>>>>>>>>> Service started!");
            System.in.read();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (null != context && context.isActive()) {
                try {
                    context.stop();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }
	
}
