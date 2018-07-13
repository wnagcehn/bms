package com.jiuyescm.bms.common.log.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsErrorLogInfoEntity implements IEntity{
	 // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 类名称
	private String className;
	// 方法名称
	private String methodName;
	// 标识信息
	private String identify;
	// 错误信息
	private String errorMsg;
	// 创建时间
	private Timestamp createTime;

	public BmsErrorLogInfoEntity() {

	}
	
	public BmsErrorLogInfoEntity(String className, String methodName,
			String identify, String errorMsg) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.identify = identify;
		this.errorMsg = errorMsg;
	}



	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 类名称
     */
	public String getClassName() {
		return this.className;
	}

    /**
     * 类名称
     *
     * @param className
     */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
     * 方法名称
     */
	public String getMethodName() {
		return this.methodName;
	}

    /**
     * 方法名称
     *
     * @param methodName
     */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	/**
     * 标识信息
     */
	public String getIdentify() {
		return this.identify;
	}

    /**
     * 标识信息
     *
     * @param identify
     */
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
     * 创建时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 创建时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
