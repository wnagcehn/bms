package com.jiuyescm.bms.billcheck.web;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBillAccountInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
@Controller("billCheckPR")
public class BillAccountInfoController {
	private static final Logger logger = Logger.getLogger(BillAccountInfoController.class.getName());
	@Resource private IBillAccountInfoService billAccountInfoService;
	@DataProvider
	public BillAccountInfoEntity findById(Long id) throws Exception {
		BillAccountInfoEntity entity = null;
		entity = billAccountInfoService.findById(id);
		return entity;
	}
	@DataProvider
public void queryAll(Page<BillAccountInfoEntity> page,Map<String,Object> parameter){
		
		PageInfo<BillAccountInfoEntity> tmpPageInfo = billAccountInfoService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
		
	}
	

}
