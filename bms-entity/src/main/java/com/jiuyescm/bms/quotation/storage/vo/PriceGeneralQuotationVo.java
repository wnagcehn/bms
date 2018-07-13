package com.jiuyescm.bms.quotation.storage.vo;

import java.util.List;

import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;

public class PriceGeneralQuotationVo extends PriceGeneralQuotationEntity {

	private static final long serialVersionUID = 3266909613533790278L;
	
	private List<PriceStepQuotationEntity>  child;

	public List<PriceStepQuotationEntity> getChild() {
		return child;
	}

	public void setChild(List<PriceStepQuotationEntity> child) {
		this.child = child;
	}

}
