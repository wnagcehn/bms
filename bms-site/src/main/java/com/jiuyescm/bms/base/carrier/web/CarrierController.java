package com.jiuyescm.bms.base.carrier.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;

@Controller("carrierController")
public class CarrierController {

	@Resource private ICarrierService carrierService;
	
	@DataProvider
	public void query(Page<CarrierVo> page,Map<String,Object> parameter) {
		if(null==parameter){
			parameter=new HashMap<String,Object>();
		}
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		PageInfo<CarrierVo> tmpPageInfo = carrierService.queryCarrier(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
}
