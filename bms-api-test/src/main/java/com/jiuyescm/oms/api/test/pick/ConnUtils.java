package com.jiuyescm.oms.api.test.pick;

import java.sql.*; 

public class ConnUtils {

	private static final String DRIVER="com.mysql.jdbc.Driver";
	  
	private static final String URL = "jdbc:mysql://120.27.241.59:3306/wmsb02?useUnicode=true&characterEncoding=UTF-8&noAccessToProcedureBodies=true"; 
	private static final String USER_NAME = "wmsadmin";
	private static final String USER_PWD = "EXBw9eAiop@d2";
	
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
