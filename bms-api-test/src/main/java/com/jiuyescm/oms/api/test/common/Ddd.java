package com.jiuyescm.oms.api.test.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "request") 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "as", "ds"})
public class Ddd {
	@XmlElement
	String as; 
	@XmlElement
	String ds;
	
	public Ddd(){super();}
	public Ddd(String as, String ds) {
		super();
		this.as = as;
		this.ds = ds;
	}
	public String getAs() {
		return as;
	}
	public void setAs(String as) {
		this.as = as;
	}
	public String getDs() {
		return ds;
	}
	public void setDs(String ds) {
		this.ds = ds;
	}
	
}
