package com.jiuyescm.bms.billcheck.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;


@Controller("billDetailController")
public class BillAccountDetailController {
	@Resource private IPubAirportService pubAirportService;
	@Resource private SequenceService sequenceService;
	
	@DataProvider  
	public void queryIn(Page<PubAirportEntity> page,Map<String,Object> parameter){
		
		PageInfo<PubAirportEntity> tmpPageInfo = pubAirportService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@DataProvider  
	public void queryOut(Page<PubAirportEntity> page,Map<String,Object> parameter){
		
		PageInfo<PubAirportEntity> tmpPageInfo = pubAirportService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	
	

}
