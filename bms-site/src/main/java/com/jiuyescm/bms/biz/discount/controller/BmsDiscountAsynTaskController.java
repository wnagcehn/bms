package com.jiuyescm.bms.biz.discount.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baidu.disconf.client.utils.StringUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsDiscountAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * ..Controller
 * 
 * @author wangchen
 * 
 */
@Controller("bmsDiscountAsynTaskController")
public class BmsDiscountAsynTaskController {

	private static final Logger logger = LoggerFactory.getLogger(BmsDiscountAsynTaskController.class.getName());

	@Autowired
	private IBmsDiscountAsynTaskService bmsDiscountAsynTaskService;

	@Autowired
	private SequenceService sequenceService;

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BmsDiscountAsynTaskEntity findById(Long id) throws Exception {
		return bmsDiscountAsynTaskService.findById(id);
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsDiscountAsynTaskEntity> page, Map<String, Object> param) {
		if (Integer.valueOf(param.get("month").toString()) < 10) {
			param.put("createMonth", param.get("year").toString() + "-0" + param.get("month").toString());
		} else {
			param.put("createMonth", param.get("year").toString() + "-" + param.get("month").toString());
		}
		PageInfo<BmsDiscountAsynTaskEntity> pageInfo = bmsDiscountAsynTaskService.query(param, page.getPageNo(),
				page.getPageSize());
		if (pageInfo != null) {
			List<BmsDiscountAsynTaskEntity> voList = pageInfo.getList();
			initList(voList);
			page.setEntities(voList);
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	private void initList(List<BmsDiscountAsynTaskEntity> voList) {
		for (BmsDiscountAsynTaskEntity voEntity : voList) {
			if (voEntity.getTaskRate() != null) {
				voEntity.setTaskProcess(String.valueOf(voEntity.getTaskRate()) + "%");
			} else {
				voEntity.setTaskProcess("%");
			}
		}
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 * @throws ParseException
	 */
	@DataResolver
	public void save(BmsDiscountAsynTaskEntity entity) throws ParseException {
		if (StringUtils.isNotBlank(entity.getYear()) && StringUtils.isNotBlank(entity.getMonth())) {
			String startDateStr = entity.getYear() + "-" + entity.getMonth() + "-01 00:00:00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf.parse(startDateStr);
			Date endDate = DateUtils.addMonths(startDate, 1);
			Timestamp startTime = new Timestamp(startDate.getTime());
			Timestamp endTime = new Timestamp(endDate.getTime());
			entity.setStartDate(startTime);
			entity.setEndDate(endTime);
		}
		// 生成任务，写入任务表
		String taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
		entity.setTaskId(taskId);
		int month = 0;
		try {
			month = Integer.parseInt(entity.getMonth());
		} catch (Exception e) {
			logger.error("转换错误", e);
		}
		if (month < 10) {
			entity.setMonth("0" + entity.getMonth().toString());
		}
		entity.setCreateMonth(entity.getYear() + "-" + entity.getMonth());
		entity.setTaskRate(0);
		entity.setDelFlag("0");
		entity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		try {
			bmsDiscountAsynTaskService.save(entity);
		} catch (Exception e) {
			logger.error("保存失败！", e);
		}	
		
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isNotEmpty(entity.getBizTypecode()) && StringUtils.isNotEmpty(entity.getSubjectCode())) {
			//发送一条mq
		}
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isNotEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			//发送业务类型下所有费用科目的
		}
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			//发送商家下所有的
		}
		if (StringUtils.isEmpty(entity.getCustomerId()) && StringUtils.isEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			//发送所有
		}
	}


	/**
	 * 删除
	 * 
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsDiscountAsynTaskEntity entity) {
		bmsDiscountAsynTaskService.delete(entity.getId());
	}

}
