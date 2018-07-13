package com.jiuyescm.bms.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(
					"classpath:spring/spring-context.xml");
			context.start();
			User.login("a", "b");
			while (true) {
				Thread.sleep(10000);
			}
		} catch (Exception e) {
		} finally {
			if (null != context && context.isActive()) {
				try {
					context.stop();
				} catch (Exception e) {
				}
			}
		}
	}
}
