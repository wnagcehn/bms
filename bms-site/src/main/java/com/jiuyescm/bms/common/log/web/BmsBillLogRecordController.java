package com.jiuyescm.bms.common.log.web;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.service.IBillLogRecordService;

@Controller("bmsBillLogRecordController")
public class BmsBillLogRecordController {

	private static final Logger logger = Logger.getLogger(BmsBillLogRecordController.class.getName());
	
	@Autowired
	private IBillLogRecordService billLogRecordService;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsBillLogRecordEntity> page, Map<String, Object> param) {
		PageInfo<BmsBillLogRecordEntity> pageInfo = billLogRecordService.queryAll(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
}
