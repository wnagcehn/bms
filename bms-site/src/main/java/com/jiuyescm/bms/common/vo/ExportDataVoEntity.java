package com.jiuyescm.bms.common.vo;

import java.util.List;
import java.util.Map;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.common.utils.upload.BaseDataType;

public class ExportDataVoEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7620476846783220316L;
	
	private String  titleName;
	private BaseDataType baseType;
	private List<Map<String,Object>> dataList;
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public BaseDataType getBaseType() {
		return baseType;
	}
	public void setBaseType(BaseDataType baseType) {
		this.baseType = baseType;
	}
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}
	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}
}
