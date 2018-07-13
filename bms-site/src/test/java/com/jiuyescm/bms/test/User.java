package com.jiuyescm.bms.test;

import java.util.HashMap;
import java.util.Map;

public class User {
	public static void login(String user, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username_", user);
		map.put("password_", password);
		BulidData.buildMap("http://oms2.uat.jiuyescm.com/security_check_", map)
				.run();
	}
}
