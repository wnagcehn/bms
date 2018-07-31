package com.jiuyescm.bms.biz.discount.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsDiscountAsynTaskService;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
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
	private IPriceContractDiscountService priceContractDiscountService;

	@Autowired
	private SequenceService sequenceService;

	@Resource
	private JmsTemplate jmsQueueTemplate;

	private static final String BMS_DISCOUNT_ASYN_TASK = "BMS.DISCOUNT.ASYN.TASK";

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
	 * @throws Exception 
	 */
	@DataResolver
	public Map<String, String> save(BmsDiscountAsynTaskEntity entity) throws Exception {
		String taskId = "";
		Map<String, String> result = new HashMap<>();
		// 时间转换
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
		
		int month = 0;		
		try {
			month = Integer.parseInt(entity.getMonth());
		} catch (Exception e) {
			logger.error("转换错误", e);
		}
		
		// 1.全填(一个费用科目)
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isNotEmpty(entity.getBizTypecode()) && StringUtils.isNotEmpty(entity.getSubjectCode())) {
			// 发送一条mq
			if (month < 10) {
				entity.setMonth("0" + entity.getMonth().toString());
			}
			// 生成任务，写入任务表
			taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
			entity.setTaskId(taskId);
			entity.setCreateMonth(entity.getYear() + "-" + entity.getMonth());
			entity.setTaskRate(0);
			entity.setDelFlag("0");
			entity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
			entity.setCreator(JAppContext.currentUserName());
			entity.setCreateTime(JAppContext.currentTimestamp());			
			bmsDiscountAsynTaskService.save(entity);
			result.put("success", "保存成功");
			try {				
				final String msg = entity.getTaskId();
				jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(msg);
					}
				});
			} catch (Exception e) {
				logger.error("send MQ:", e);
			}
		}	
		
		// 2.发送商家下业务类型下所有费用科目的
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isNotEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			Map<String, String> param = new HashMap<>();
			if (null != entity) {
				param.put("customerid", entity.getCustomerId());
				param.put("bizTypeCode", entity.getBizTypecode());
			}
			List<PriceContractDiscountItemEntity> bizList = priceContractDiscountService.queryByCustomerIdAndBizType(param);
			if (bizList.isEmpty()) {
				result.put("fail", "你还没为这个费用科目配置折扣");
				return result;
			}
			List<BmsDiscountAsynTaskEntity> newList = new ArrayList<>();
			for (PriceContractDiscountItemEntity bizEntity : bizList) {
				BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
				if (month < 10) {
					newEntity.setMonth("0" + entity.getMonth().toString());
				}
				if (StringUtils.isNotBlank(entity.getYear()) && StringUtils.isNotBlank(entity.getMonth())) {
					String startDateStr = entity.getYear() + "-" + newEntity.getMonth() + "-01 00:00:00";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date startDate = sdf.parse(startDateStr);
					Date endDate = DateUtils.addMonths(startDate, 1);
					Timestamp startTime = new Timestamp(startDate.getTime());
					Timestamp endTime = new Timestamp(endDate.getTime());
					newEntity.setStartDate(startTime);
					newEntity.setEndDate(endTime);
				}
				// 生成任务，写入任务表
				taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
				newEntity.setTaskId(taskId);
				newEntity.setCarrierId(bizEntity.getCarrierId());
				newEntity.setCreateMonth(entity.getYear() + "-" + newEntity.getMonth());
				newEntity.setTaskRate(0);
				newEntity.setDelFlag("0");
				newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
				newEntity.setCreator(JAppContext.currentUserName());
				newEntity.setCreateTime(JAppContext.currentTimestamp());
				newEntity.setBizTypecode(entity.getBizTypecode());
				newEntity.setCustomerId(entity.getCustomerId());
				newEntity.setSubjectCode(bizEntity.getSubjectId());
				newList.add(newEntity);
			}
			if (newList.size() > 0) {
				bmsDiscountAsynTaskService.saveBatch(newList);
				result.put("success", "保存成功");
				for (BmsDiscountAsynTaskEntity bmsDiscountAsynTaskEntity : newList) {
					try {				
						final String msg = bmsDiscountAsynTaskEntity.getTaskId();
						jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(msg);
							}
						});
					} catch (Exception e) {
						logger.error("send MQ:", e);
					}
				}			
			}
		}
		
		// 3.发送商家下所有的
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			List<PriceContractDiscountItemEntity> cusList = priceContractDiscountService.queryByCustomerId(entity.getCustomerId());
			if(cusList.isEmpty()){
				result.put("fail", "你还没为这个商家配置折扣");
				return result;
			}
			List<BmsDiscountAsynTaskEntity> bdatList = new ArrayList<>();
			for (PriceContractDiscountItemEntity cusEntity : cusList) {
				BmsDiscountAsynTaskEntity bdatEntity = new BmsDiscountAsynTaskEntity();
				if (month < 10) {
					bdatEntity.setMonth("0" + entity.getMonth().toString());
				}
				if (StringUtils.isNotBlank(entity.getYear()) && StringUtils.isNotBlank(entity.getMonth())) {
					String startDateStr = entity.getYear() + "-" + bdatEntity.getMonth() + "-01 00:00:00";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date startDate = sdf.parse(startDateStr);
					Date endDate = DateUtils.addMonths(startDate, 1);
					Timestamp startTime = new Timestamp(startDate.getTime());
					Timestamp endTime = new Timestamp(endDate.getTime());
					bdatEntity.setStartDate(startTime);
					bdatEntity.setEndDate(endTime);
				}
				// 生成任务，写入任务表
				taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
				bdatEntity.setTaskId(taskId);
				bdatEntity.setCarrierId(cusEntity.getCarrierId());
				bdatEntity.setCreateMonth(entity.getYear() + "-" + bdatEntity.getMonth());
				bdatEntity.setTaskRate(0);
				bdatEntity.setDelFlag("0");
				bdatEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
				bdatEntity.setCreator(JAppContext.currentUserName());
				bdatEntity.setCreateTime(JAppContext.currentTimestamp());
				bdatEntity.setBizTypecode(cusEntity.getBizTypeCode());
				bdatEntity.setSubjectCode(cusEntity.getSubjectId());
				bdatEntity.setCustomerId(entity.getCustomerId());
				bdatList.add(bdatEntity);
			}
			if (bdatList.size() > 0) {
				bmsDiscountAsynTaskService.saveBatch(bdatList);
				result.put("success", "保存成功");
				for (BmsDiscountAsynTaskEntity bmsDiscountAsynTaskEntity : bdatList) {
					try {				
						final String msg = bmsDiscountAsynTaskEntity.getTaskId();
						jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(msg);
							}
						});
					} catch (Exception e) {
						logger.error("send MQ:", e);
					}
				}																				
			}
		}		
		// 4.发送所有
		if (StringUtils.isEmpty(entity.getCustomerId()) && StringUtils.isEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			List<PriceContractDiscountItemEntity> allList = priceContractDiscountService.queryAll();
			if (allList.isEmpty()) {
				result.put("fail", "没有商家配置折扣");
				return result;
			}
			List<BmsDiscountAsynTaskEntity> newList = new ArrayList<>();
			for (PriceContractDiscountItemEntity allEntity : allList) {
				BmsDiscountAsynTaskEntity bdatEntity = new BmsDiscountAsynTaskEntity();
				if (month < 10) {
					bdatEntity.setMonth("0" + entity.getMonth().toString());
				}
				if (StringUtils.isNotBlank(entity.getYear()) && StringUtils.isNotBlank(entity.getMonth())) {
					String startDateStr = entity.getYear() + "-" + bdatEntity.getMonth() + "-01 00:00:00";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date startDate = sdf.parse(startDateStr);
					Date endDate = DateUtils.addMonths(startDate, 1);
					Timestamp startTime = new Timestamp(startDate.getTime());
					Timestamp endTime = new Timestamp(endDate.getTime());
					bdatEntity.setStartDate(startTime);
					bdatEntity.setEndDate(endTime);
				}
				// 生成任务，写入任务表
				taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
				bdatEntity.setTaskId(taskId);
				bdatEntity.setCarrierId(allEntity.getCarrierId());
				bdatEntity.setCreateMonth(entity.getYear() + "-" + bdatEntity.getMonth());
				bdatEntity.setTaskRate(0);
				bdatEntity.setDelFlag("0");
				bdatEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
				bdatEntity.setCreator(JAppContext.currentUserName());
				bdatEntity.setCreateTime(JAppContext.currentTimestamp());
				bdatEntity.setBizTypecode(allEntity.getBizTypeCode());
				bdatEntity.setSubjectCode(allEntity.getSubjectId());
				bdatEntity.setCustomerId(allEntity.getCustomerId());
				newList.add(bdatEntity);
			}
			if (newList.size() > 0) {
				bmsDiscountAsynTaskService.saveBatch(newList);
				result.put("success", "保存成功");
				for (BmsDiscountAsynTaskEntity bmsDiscountAsynTaskEntity : newList) {
					try {				
						final String msg = bmsDiscountAsynTaskEntity.getTaskId();
						jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								return session.createTextMessage(msg);
							}
						});
					} catch (Exception e) {
						logger.error("send MQ:", e);
					}
				}		
			}
		}
		return result;
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
