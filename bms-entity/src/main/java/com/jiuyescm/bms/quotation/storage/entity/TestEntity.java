package com.jiuyescm.bms.quotation.storage.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class TestEntity  implements IEntity{

	private static final long serialVersionUID = 8579060891279754984L;
	
	private String name;
	private Integer age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

}
