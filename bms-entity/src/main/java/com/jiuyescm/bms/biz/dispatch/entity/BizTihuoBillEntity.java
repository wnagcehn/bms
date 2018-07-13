/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.dispatch.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherDetailEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizTihuoBillEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 统计时间
	private Timestamp countDate;
	// 单量
	private Double totalNum;
	// 宅配商id
	private String deliverid;
	// 宅配商名称
	private String deliverName;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	//费用编号
	private String feesNo;
	// 创建者
	private String creator;
	// 单据创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	//备注
	private String remark;
	// 删除标志
	private String delFlag;
	// extattr1
	private String extattr1;
	// extattr2
	private String extattr2;
	// extattr3
	private String extattr3;
	// extattr4
	private String extattr4;
	// extattr5
	private String extattr5;
	// extattr6
	private String extattr6;

	//报价list
	private List<PriceOutDispatchOtherDetailEntity> priceList;
	//金额（只显示）
	private Double amount;
	
	public BizTihuoBillEntity() {

	}
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 统计时间
     */
	public Timestamp getCountDate() {
		return this.countDate;
	}

    /**
     * 统计时间
     *
     * @param countDate
     */
	public void setCountDate(Timestamp countDate) {
		this.countDate = countDate;
	}
	
	/**
     * 单量
     */
	public Double getTotalNum() {
		return this.totalNum;
	}

    /**
     * 单量
     *
     * @param totalNum
     */
	public void setTotalNum(Double totalNum) {
		this.totalNum = totalNum;
	}
	
	/**
     * 宅配商id
     */
	public String getDeliverid() {
		return this.deliverid;
	}

    /**
     * 宅配商id
     *
     * @param deliverid
     */
	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}
	
	/**
     * 宅配商名称
     */
	public String getDeliverName() {
		return this.deliverName;
	}

    /**
     * 宅配商名称
     *
     * @param deliverName
     */
	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	/**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 费用计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 费用计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	/**
     * 创建者
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建者
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * 单据创建时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 单据创建时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 修改者
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * 修改时间
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * 删除标志
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * extattr1
     */
	public String getExtattr1() {
		return this.extattr1;
	}

    /**
     * extattr1
     *
     * @param extattr1
     */
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	/**
     * extattr2
     */
	public String getExtattr2() {
		return this.extattr2;
	}

    /**
     * extattr2
     *
     * @param extattr2
     */
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	/**
     * extattr3
     */
	public String getExtattr3() {
		return this.extattr3;
	}

    /**
     * extattr3
     *
     * @param extattr3
     */
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	/**
     * extattr4
     */
	public String getExtattr4() {
		return this.extattr4;
	}

    /**
     * extattr4
     *
     * @param extattr4
     */
	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	
	/**
     * extattr5
     */
	public String getExtattr5() {
		return this.extattr5;
	}

    /**
     * extattr5
     *
     * @param extattr5
     */
	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
	/**
     * extattr6
     */
	public String getExtattr6() {
		return this.extattr6;
	}

    /**
     * extattr6
     *
     * @param extattr6
     */
	public void setExtattr6(String extattr6) {
		this.extattr6 = extattr6;
	}


	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PriceOutDispatchOtherDetailEntity> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<PriceOutDispatchOtherDetailEntity> priceList) {
		this.priceList = priceList;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
    
	
}
