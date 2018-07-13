package com.jiuyescm.bms.quotation.transport.entity.vo;

import java.util.List;

import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;

public class GenericTemplateVo extends GenericTemplateEntity {
	
	private static final long serialVersionUID = -7550614820453136323L;
	
	private List<PriceMaterialQuotationEntity> child;

	public List<PriceMaterialQuotationEntity> getChild() {
		return child;
	}

	public void setChild(List<PriceMaterialQuotationEntity> child) {
		this.child = child;
	}

}
