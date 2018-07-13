package com.jiuyescm.oms.api.test.common;

import java.sql.*; 

public class ConnUtils {

	private static final String DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	//private static final String URL = "jdbc:sqlserver://120.55.88.152:1433;DatabaseName=IMS_OMS";
	//private static final String USER_NAME = "sa";
	//private static final String USER_PWD = "jiuyescm12345";
	
	private static final String URL = "jdbc:sqlserver://dbo.jiuyescm.com:13433;DatabaseName=IMS_OMS"; 
	private static final String USER_NAME = "jiuyescm";
	private static final String USER_PWD = "a78ce29e0c5e6fb5";
	
	public static Connection CONN = null;
	
	static{
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		}
	} 
	
	public static void conn() throws SQLException{ 
		CONN = DriverManager.getConnection(URL, USER_NAME, USER_PWD);
		System.out.println("建立连接。。conn");  
	}
	
	public static void close(){
		try {
			if(null != CONN){
				CONN.close();
			} 
		} catch (SQLException e) {
			CONN = null;
		}
		System.out.println("关闭连接。。conn");  
	}
	
	public static void main(String[] srg) { 
		try {
			ConnUtils.conn();
		} catch (SQLException e) {
			CONN = null;
			e.printStackTrace();
		}
		
	}
}
