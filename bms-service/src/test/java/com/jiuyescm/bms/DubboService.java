/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jiuye
 * 
 */
public class DubboService {

    private static final Logger logger = LoggerFactory.getLogger(DubboService.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                    "classpath:spring/spring-context.xml");
            context.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        synchronized (DubboService.class) {
            while (true) {
                try {
                    DubboService.class.wait();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
