package com.jiuyescm.bms.correct;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * @author liuzhicheng
 * 
 */
public class ElConditionEntity implements IEntity {

	private static final long serialVersionUID = 6312113420432235890L;
	// 自增主键
	private Long id;
	private String pullType;
	private Timestamp lastTime;
	private Timestamp currTime;
	private Integer takesTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPullType() {
		return pullType;
	}

	public void setPullType(String pullType) {
		this.pullType = pullType;
	}

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	public Timestamp getCurrTime() {
		return currTime;
	}

	public void setCurrTime(Timestamp currTime) {
		this.currTime = currTime;
	}

	public Integer getTakesTime() {
		return takesTime;
	}

	public void setTakesTime(Integer takesTime) {
		this.takesTime = takesTime;
	}

	public ElConditionEntity() {
		super();
	}
	

}
