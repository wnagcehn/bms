package com.jiuyescm.bms.pub.templet.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class PubTempletShortEntity implements IEntity {
	private static final long serialVersionUID = -355538504042846965L;

	// 主键-自增长
	private Integer id;

	// 模板名称
	private String templetName;

	// 商家简称
	private String templetShortname;

	// 创建人姓名
	private String templetCreperson;

	// 创建时间
	private Timestamp templetCretime;

	// 修改人姓名
	private String templetUpdateperson;

	// 修改时间
	private Timestamp templetUpdatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTempletName() {
		return templetName;
	}

	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}

	public String getTempletShortname() {
		return templetShortname;
	}

	public void setTempletShortname(String templetShortname) {
		this.templetShortname = templetShortname;
	}

	public String getTempletCreperson() {
		return templetCreperson;
	}

	public void setTempletCreperson(String templetCreperson) {
		this.templetCreperson = templetCreperson;
	}

	public Timestamp getTempletCretime() {
		return templetCretime;
	}

	public void setTempletCretime(Timestamp templetCretime) {
		this.templetCretime = templetCretime;
	}

	public String getTempletUpdateperson() {
		return templetUpdateperson;
	}

	public void setTempletUpdateperson(String templetUpdateperson) {
		this.templetUpdateperson = templetUpdateperson;
	}

	public Timestamp getTempletUpdatetime() {
		return templetUpdatetime;
	}

	public void setTempletUpdatetime(Timestamp templetUpdatetime) {
		this.templetUpdatetime = templetUpdatetime;
	}
}
