/*
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 * FileName: TMSApplication.java
 * Author:   luke wang
 * Date:     2016年12月9日
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.jiuyescm.bms;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xxl.job.core.log.XxlJobLogger;

/**
 * Job 系统启动类
 * @author ningyu
 */
public class JobApplication {

	private static Logger logger = LoggerFactory.getLogger(JobApplication.class);
	private static ClassPathXmlApplicationContext context;
	
	public static void main(String[] args) {
		try {
            context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
            context.start();
            logger.info(">>>>>>>>>>> BmsJobApplication Service started!");
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


