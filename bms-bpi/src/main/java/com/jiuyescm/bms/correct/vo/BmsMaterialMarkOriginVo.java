package com.jiuyescm.bms.correct.vo;

import java.math.BigDecimal;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class BmsMaterialMarkOriginVo implements IEntity {

 	// TODO please create serialVersionUID by eclipse
    private static final long serialVersionUID =-1 ;
    
	// id
	private Long id;
	// 耗材使用明细标
	private String materialMark;
	// 耗材编码
	private String consumerMaterialCode;
	// 耗材名称
	private String consumerMaterialName;
	// 耗材规格
	private String specDesc;
	// num
	private BigDecimal num;

	public BmsMaterialMarkOriginVo() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMaterialMark() {
		return this.materialMark;
	}

	public void setMaterialMark(String materialMark) {
		this.materialMark = materialMark;
	}
	
	public String getConsumerMaterialCode() {
		return this.consumerMaterialCode;
	}

	public void setConsumerMaterialCode(String consumerMaterialCode) {
		this.consumerMaterialCode = consumerMaterialCode;
	}
	
	public String getConsumerMaterialName() {
		return this.consumerMaterialName;
	}

	public void setConsumerMaterialName(String consumerMaterialName) {
		this.consumerMaterialName = consumerMaterialName;
	}
	
	public String getSpecDesc() {
		return this.specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}
	
	public BigDecimal getNum() {
		return this.num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}
    
}
