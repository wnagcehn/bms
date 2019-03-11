package com.jiuyescm.common.utils.excel;

import com.jiuyescm.common.utils.excel.MyAnnotation.MyFieldAnnotation;


public class Demo {

	@MyFieldAnnotation(title = "列1", num = 1)
	private String column1;
    @MyFieldAnnotation(title = "列2", num = 2)
	private String column2;
    @MyFieldAnnotation(title = "列3", num = 3)
	private String column3;
    
	private String column4;
	
    public Demo(String column1, String column2, String column3, String column4) {
		super();
		this.column1 = column1;
		this.column2 = column2;
		this.column3 = column3;
		this.column4 = column4;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	public String getColumn3() {
		return column3;
	}

	public void setColumn3(String column3) {
		this.column3 = column3;
	}

	public String getColumn4() {
		return column4;
	}

	public void setColumn4(String column4) {
		this.column4 = column4;
	}

}