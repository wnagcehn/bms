package com.jiuyescm.bms.report.month.web;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;

@Controller("recordLogController")
public class RecordLogController {

	private static final Logger logger = Logger.getLogger(RecordLogController.class.getName());
	@Resource
	private IPubRecordLogService pubRecordLogService;
	
	@DataProvider  
	public void queryAll(Page<PubRecordLogEntity> page,Map<String,Object> parameter){
		PageInfo<PubRecordLogEntity> tmpPageInfo = pubRecordLogService.queryAll(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@DataProvider
	public Map<String,String> getBizType(){
		return RecordLogBizTypeEnum.getMap();
	}
	@DataProvider
	public Map<String,String> getOperateType(){
		return RecordLogOperateType.getMap();
	}
	@DataProvider
	public Map<String,String> getUrlName(){
		return RecordLogUrlNameEnum.getMap();
	}
}
