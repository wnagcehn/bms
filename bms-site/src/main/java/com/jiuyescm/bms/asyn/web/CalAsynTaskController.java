package com.jiuyescm.bms.asyn.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupSubjectVo;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("calAsynTaskController")
public class CalAsynTaskController {
	private static final Logger logger = Logger.getLogger(CalAsynTaskController.class.getName());

	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired
	private IBmsGroupService bmsGroupService;
	@Autowired
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	/**
	 * 页面查询
	 */
	@DataProvider
	public void query(Page<BmsCalcuTaskVo> page, Map<String, Object> parameter) {
		PageInfo<BmsCalcuTaskVo> pageInfo = null;
		try {
			pageInfo = bmsCalcuTaskService.query(parameter, page.getPageNo(),
					page.getPageSize());
		} catch (Exception e) {
			logger.error("查询计算任务异常",e);
		}
		if (pageInfo != null) {
			List<BmsCalcuTaskVo> voList = pageInfo.getList();
			page.setEntities(voList);
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider  
	public List<BmsCalcuTaskVo> getCustomerId() { 
		Map<String, Object> parameter = new HashMap<>();
		//查询任务表中的商家id
		parameter.put("queryCus", "1");
		List<BmsCalcuTaskVo> list = bmsCalcuTaskService.query(parameter);
		return list;
	}
	
	@DataProvider
	public Map<String, String> getSubject() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<BmsGroupSubjectVo> list =bmsGroupSubjectService.queryGroupSubjectList();
		for (BmsGroupSubjectVo vo : list){
			mapValue.put(vo.getSubjectCode(), vo.getSubjectName());
		}
		return mapValue;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param) {
/*		// 查询任务数据
		List<BmsCalcuTaskVo> taskList =  bmsCalcuTaskService.query(param);
		if (CollectionUtils.isNotEmpty(taskList)) {
			
			
			
			
			
			
			
			
			
			List<String> feeNoList = new ArrayList<>();
			for (BizProductStorageEntity bizProductStorageEntity : taskList) {
				feeNoList.add(bizProductStorageEntity.getFeesNo());
			}
			Map<String, Object> feeMap = new HashMap<String, Object>();
			feeMap.put("feeList", feeNoList);
			// 修改费用数据计算状态
			if (bizProductStorageService.reCalculate(feeMap) == 0) {
				return "重算异常";
			}
		}
		// 发送MQ
		sendTask();*/
		return "操作成功! 正在重算...";
	}
}