package com.jiuyescm.bms.lookup.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;

@Component("deliverLookupPR")
public class DeliverLookupPR {
	@Resource 
	private IDeliverService deliverService;
	
	@DataProvider
	public void query(Page<DeliverVo> page,Map<String,Object> parameter) {
		if(null==parameter){
			parameter=new HashMap<String,Object>();
		}
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		PageInfo<DeliverVo> tmpPageInfo = deliverService.queryDeliver(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
}
