package com.jiuyescm.bms.asyn.web;

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
import com.jiuyescm.cfm.common.JAppContext;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("calAsynTaskController")
public class CalAsynTaskController {
	private static final Logger logger = LoggerFactory
			.getLogger(CalAsynTaskController.class.getName());
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
			pageInfo = bmsCalcuTaskService.queryPage(parameter, page.getPageNo(),
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
		// 查询任务数据
		List<BmsCalcuTaskVo> taskList =  bmsCalcuTaskService.query(param);
		if (CollectionUtils.isNotEmpty(taskList)) {
			// 对这些任务按照商家、时间、科目、费用类型排序
			Map<String,BmsCalcuTaskVo> groupMap = new HashMap<>();
			for (BmsCalcuTaskVo bmsCalcuTaskVo : taskList) {
				StringBuilder stringBuilder = new StringBuilder(bmsCalcuTaskVo.getCustomerId());
				stringBuilder.append("-").append(bmsCalcuTaskVo.getCreMonth()).append("-").append(bmsCalcuTaskVo.getSubjectCode()).append("-").append(bmsCalcuTaskVo.getFeesType());
				groupMap.put(stringBuilder.toString(), bmsCalcuTaskVo);
			}
			//遍历map中的值
			for(BmsCalcuTaskVo vo:groupMap.values()){
				BmsCalcuTaskVo bmsCalcuTaskVo = new BmsCalcuTaskVo();
				bmsCalcuTaskVo.setCrePerson(JAppContext.currentUserName());
				bmsCalcuTaskVo.setCrePersonId(JAppContext.currentUserName());
				bmsCalcuTaskVo.setCustomerId(vo.getCustomerId());
				bmsCalcuTaskVo.setCreMonth(vo.getCreMonth());
				bmsCalcuTaskVo.setSubjectCode(vo.getSubjectCode());
				bmsCalcuTaskVo.setFeesType(vo.getFeesType());
				try {
					bmsCalcuTaskService.sendTask(vo);
					logger.info("mq发送，商家id为----{0}，业务年月为----{0}，科目id为---{0}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
				} catch (Exception e) {
					logger.info("mq任务失败：商家id为----{0}，业务年月为----{0}，科目id为---{0}，错误信息：{0}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode(),e);
				}
			}
		}
		return "操作成功! 正在重算...";
	}
	
	@DataProvider
	public Map<String, String> getFeesType() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("item", "商品按件");
		mapValue.put("pallet", "商品按托");
		return mapValue;
	}
	
	@DataProvider
	public Map<String, String> getTaskStatus() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("0", "等待");
		mapValue.put("10", "处理中");
		mapValue.put("20", "成功");
		mapValue.put("30", "异常");
		mapValue.put("40", "丢弃");
		mapValue.put("50", "作废");
		return mapValue;
	}
}