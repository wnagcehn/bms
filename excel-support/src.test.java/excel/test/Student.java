package excel.test;

import com.jiuyescm.bms.excel.annotation.ExcelField;

public class Student {

	@ExcelField(title = "姓名")
	private String name;

	@ExcelField(title = "年龄")
	private int age;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
