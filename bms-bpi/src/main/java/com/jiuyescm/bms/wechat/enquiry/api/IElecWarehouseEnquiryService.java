package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.wechat.enquiry.vo.ElecWarehouseVo;
import com.jiuyescm.bms.wechat.enquiry.vo.WarehouseVo;

public interface IElecWarehouseEnquiryService {

	//电商名称
	List<ElecWarehouseVo> queryElecName(Map<String, String> reqParam);
	
	//电商仓库名称
	List<WarehouseVo> queryElecWarehouse(Map<String, String> reqParam);
}
