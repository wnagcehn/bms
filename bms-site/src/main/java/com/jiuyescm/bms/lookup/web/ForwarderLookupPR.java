package com.jiuyescm.bms.lookup.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.forwarder.api.IForwarderService;
import com.jiuyescm.mdm.forwarder.vo.ForwarderVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Component("forwarderLookupPR")
public class ForwarderLookupPR {

	@Autowired
	private IForwarderService forwarderService;
	
	@DataProvider
	public void query(Page<ForwarderVo> page,Map<String,Object> parameter) {
		if(parameter==null){
			parameter=new HashMap<String,Object>();
		}
		PageInfo<ForwarderVo> tmpPageInfo = forwarderService.query(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@DataProvider
	public Map<Integer, String> getInvalidflag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(0, "未作废");
		mapValue.put(1, "已作废");
		return mapValue;
	}
}
