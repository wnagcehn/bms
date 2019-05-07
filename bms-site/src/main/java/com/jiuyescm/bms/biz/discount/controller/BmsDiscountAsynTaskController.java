package com.jiuyescm.bms.biz.discount.controller;

import java.sql.Timestamp;
import java.text.ParseException;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.contract.quote.api.IContractDiscountService;
import com.jiuyescm.contract.quote.vo.CarrierInfoVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountQueryVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountVo;
import com.jiuyescm.contract.quote.vo.SubjectInfoVo;
import com.jiuyescm.utils.JsonUtils;

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
	@Resource
	private IContractDiscountService contractDiscountService;
	@Autowired 
	ICustomerDictService customerDictService;
	@Resource
	private ISystemCodeService systemCodeService;

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
		
		if(StringUtils.isBlank(entity.getCreateMonth())){
			if (month < 10) {
				entity.setCreateMonth(entity.getYear() + "-0" + entity.getMonth().toString());
			}else{
				entity.setCreateMonth(entity.getYear() + "-"  + entity.getMonth().toString());
			}			
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
				logger.info("开始发送MQ");
				Map<String,Object> map=new HashMap<>();
				map.put("taskId", entity.getTaskId());
		        final String msg = JsonUtils.toJson(map);
				jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(msg);
					}
				});
				logger.info("MQ发送成功");
			} catch (Exception e) {
				logger.error("send MQ:", e);
			}
		}	
		
		// 2.发送商家下业务类型下所有费用科目的
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isNotEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			List<BmsDiscountAsynTaskEntity> newList = new ArrayList<>();	
		    //判断合同归属  1 "BMS" 2 "CONTRACT";
			PubCustomerVo vo = customerDictService.queryById(entity.getCustomerId());
		    if(vo.getContractAttr()==2){
		        try {
	                ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
	                queryVo.setCustomerId(entity.getCustomerId());
	                queryVo.setSettlementTime(entity.getCreateMonth());
	                queryVo.setBizTypeCode(entity.getBizTypecode());
	                if("DISPATCH".equals(entity.getBizTypecode())){
	                    queryVo.setBizTypeCode("DISTRIBUTION");
	                }
	                logger.info("查询合同在线折扣参数"+JSONObject.fromObject(queryVo));
	                List<ContractDiscountVo> disCountVo=contractDiscountService.querySubject(queryVo);
	                logger.info("查询合同在线折扣结果"+JSONArray.fromObject(disCountVo));
	                if(disCountVo.size()>0){
	                    newList=getContractList(disCountVo, entity, month);
	                }
	            } catch (Exception e) {
	                // TODO: handle exception
	                logger.error("查询合同在线折扣异常", e);

	            }
		    }else{
				Map<String, String> param = new HashMap<>();
				if (null != entity) {
					param.put("customerid", entity.getCustomerId());
					param.put("bizTypeCode", entity.getBizTypecode());
					String startD = entity.getCreateMonth() + "-01 00:00:00";
					param.put("startTime", startD);
				}
				List<PriceContractDiscountItemEntity> bizList = priceContractDiscountService.queryByCustomerIdAndBizType(param);
				if (bizList.isEmpty()) {
					result.put("fail", "你还没为这个费用科目配置折扣");
					return result;
				}
				//生成任务，写入任务表
				saveToTask(entity, month, newList, bizList, taskId);
			}
			if (newList.size() > 0) {
				sendMQ(result, newList);			
			}
		}
		
		// 3.发送商家下所有的
		if (StringUtils.isNotEmpty(entity.getCustomerId()) && StringUtils.isEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
	         if (month < 10) {
	                entity.setMonth("0" + entity.getMonth().toString());
	            }
	            Map<String, Object> map = new HashMap<>();
	            map.put("customerid", entity.getCustomerId());
	            map.put("createMonth", entity.getYear()+"-"+entity.getMonth());    
	            taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
	            map.put("taskId", taskId);
	            int sendResult=bmsDiscountAsynTaskService.sendTask(map);
	            if(sendResult>0){
	                result.put("success", "保存成功");
	            }else{
	                result.put("fail", "保存失败");
	            }
		}		
		// 4.发送所有
		if (StringUtils.isEmpty(entity.getCustomerId()) && StringUtils.isEmpty(entity.getBizTypecode()) && StringUtils.isEmpty(entity.getSubjectCode())) {
			List<BmsDiscountAsynTaskEntity> newList = new ArrayList<>();
			
			try {
				ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
				queryVo.setSettlementTime(entity.getCreateMonth());
				logger.info("查询合同在线折扣参数"+JSONObject.fromObject(queryVo));
				List<ContractDiscountVo> disCountVo=contractDiscountService.querySubject(queryVo);
				logger.info("查询合同在线折扣结果"+JSONArray.fromObject(disCountVo));
				if(disCountVo.size()>0){
					newList=getContractList(disCountVo, entity, month);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.info("查询合同在线报错",e);
				BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
				// 合同生效期和开始时间比较
				if (month < 10) {
					newEntity.setMonth("0" + entity.getMonth().toString());
				}else{
					newEntity.setMonth(entity.getMonth().toString());
				}
				String startD = entity.getYear() + "-" + newEntity.getMonth() + "-01 00:00:00";
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDa = sd.parse(startD);
				Timestamp starttime = new Timestamp(startDa.getTime());
				List<PriceContractDiscountItemEntity> allList = priceContractDiscountService.queryAll(starttime);
				
				if (allList.isEmpty()) {
					result.put("fail", "没有商家配置折扣");
					return result;
				}
				//生成任务，写入任务表
				saveToTask(entity, month, newList, allList, taskId);
			}
			if (newList.size() > 0) {
				sendMQ(result, newList);
			}
		}
		return result;
	}

	private void saveToTask(BmsDiscountAsynTaskEntity entity, int month, List<BmsDiscountAsynTaskEntity> newList,
			List<PriceContractDiscountItemEntity> bizList, String taskId) throws ParseException {
	    
	    Map<String,String> customerMap=new HashMap<>();
	    
		for (PriceContractDiscountItemEntity bizEntity : bizList) {
			BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
			if (month < 10) {
				newEntity.setMonth("0" + entity.getMonth().toString());
			}else{
				newEntity.setMonth(entity.getMonth().toString());
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
			customerMap.clear();
			if(customerMap.containsKey(bizEntity.getCustomerId())){
			    taskId=customerMap.get(bizEntity.getCustomerId());
			}else{
		         taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
		         customerMap.put(bizEntity.getCustomerId(), taskId);
			}	
			newEntity.setTaskId(taskId);
			newEntity.setCarrierId(bizEntity.getCarrierId());
			newEntity.setCreateMonth(entity.getYear() + "-" + newEntity.getMonth());
			newEntity.setTaskRate(0);
			newEntity.setDelFlag("0");
			newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
			newEntity.setCreator(JAppContext.currentUserName());
			newEntity.setCreateTime(JAppContext.currentTimestamp());
			newEntity.setBizTypecode(bizEntity.getBizTypeCode());
			if(StringUtils.isNotBlank(bizEntity.getCustomerId())){
				newEntity.setCustomerId(bizEntity.getCustomerId());
			}else{
				newEntity.setCustomerId(entity.getCustomerId());
			}
			newEntity.setSubjectCode(bizEntity.getSubjectId());
			newEntity.setCustomerType("bms");
			newList.add(newEntity);
		}
	}

	private void sendMQ(Map<String, String> result, List<BmsDiscountAsynTaskEntity> newList) throws Exception {
		bmsDiscountAsynTaskService.saveBatch(newList);
		result.put("success", "保存成功");
		for (BmsDiscountAsynTaskEntity bmsDiscountAsynTaskEntity : newList) {
			try {			
				logger.info("开始发送MQ");
                Map<String,Object> map=new HashMap<>();
                map.put("taskId", bmsDiscountAsynTaskEntity.getTaskId());
                final String msg = JsonUtils.toJson(map);
				jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(msg);
					}
				});
				logger.info("MQ发送成功");
			} catch (Exception e) {
				logger.error("send MQ:", e);
			}
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

	
	public List<BmsDiscountAsynTaskEntity> getContractList(List<ContractDiscountVo> disCountVo,BmsDiscountAsynTaskEntity entity,int month)throws Exception{
		String taskId = "";
		List<BmsDiscountAsynTaskEntity> newList = new ArrayList<>();
		for(ContractDiscountVo vo:disCountVo){
            taskId = sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
			if(vo.getSubjectVoList().size()>0){
				for(SubjectInfoVo s:vo.getSubjectVoList()){
					BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
					if (month < 10) {
						newEntity.setMonth("0" + entity.getMonth().toString());
					}else{
						newEntity.setMonth(entity.getMonth().toString());
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
					newEntity.setTaskId(taskId);
					//newEntity.setCarrierId(bizEntity.getCarrierId());
					newEntity.setCreateMonth(entity.getYear() + "-" + newEntity.getMonth());
					newEntity.setTaskRate(0);
					newEntity.setDelFlag("0");
					newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
					newEntity.setCreator(JAppContext.currentUserName());
					newEntity.setCreateTime(JAppContext.currentTimestamp());
					newEntity.setBizTypecode("STORAGE");
					newEntity.setCustomerId(vo.getCustomerId());
					newEntity.setSubjectCode(s.getSubjectId());
					newEntity.setDiscountType(s.getDiscountType());
					newEntity.setCustomerType("contract");
					newList.add(newEntity);						
				}			
			}
			if(vo.getCarrierVoList().size()>0){
				for(CarrierInfoVo s:vo.getCarrierVoList()){
					BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
					if (month < 10) {
						newEntity.setMonth("0" + entity.getMonth().toString());
					}else{
						newEntity.setMonth(entity.getMonth().toString());
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
					newEntity.setTaskId(taskId);
					newEntity.setCreateMonth(entity.getYear() + "-" + newEntity.getMonth());
					newEntity.setTaskRate(0);
					newEntity.setDelFlag("0");
					newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
					newEntity.setCreator(JAppContext.currentUserName());
					newEntity.setCreateTime(JAppContext.currentTimestamp());
					newEntity.setBizTypecode("DISPATCH");
					newEntity.setCustomerId(vo.getCustomerId());				
					SystemCodeEntity sys=(SystemCodeEntity) getDispatchMap().get(s.getCarrierId());
					newEntity.setCarrierId(s.getCarrierId());	
					newEntity.setDiscountType(s.getDiscountType());
					newEntity.setCustomerType("contract");
					if (null != sys) {
						newEntity.setSubjectCode(sys.getCode());
						newList.add(newEntity);	
					}	
				}			
			}			

		}
		
		return newList;
	}
	
	
	public Map<String,Object> getDispatchMap(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("typeCode", "DISPATCH_COMPANY");
		List<SystemCodeEntity> scList = systemCodeService.queryCodeList(map);
		map.clear();
		for(SystemCodeEntity s:scList){
			map.put(s.getExtattr1(), s);
		}
		return map;
	}
}
