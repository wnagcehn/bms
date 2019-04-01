package com.jiuyescm.bms.asyn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.asyn.repo.IBmsAsynCalcuTaskRepository;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.common.enumtype.MQSubjectEnum;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.MD5Util;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;

@Service("bmsCalcuTaskService")
public class BmsCalcuTaskServiceImpl implements IBmsCalcuTaskService{
	
	private Logger logger = LoggerFactory.getLogger(BmsCalcuTaskServiceImpl.class);

	@Autowired IBmsAsynCalcuTaskRepository bmsAsynCalcuTaskRepositoryimpl;
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private JmsTemplate jmsQueueTemplate;
	@Autowired private Lock lock;
	@Autowired private ICustomerDictService customerDictService;
	@Autowired private IBmsSubjectInfoService bmsSubjectService;
	
	
	@Override
	public PageInfo<BmsCalcuTaskVo> query(Map<String, Object> condition,int pageNo, int pageSize) throws BizException {
		
		PageInfo<BmsCalcuTaskVo> pageVoInfo= new PageInfo<BmsCalcuTaskVo>();
		try{
			PageInfo<BmsAsynCalcuTaskEntity> pageInfo=bmsAsynCalcuTaskRepositoryimpl.query(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsCalcuTaskVo> list=new ArrayList<BmsCalcuTaskVo>();
				for(BmsAsynCalcuTaskEntity entity:pageInfo.getList()){
					BmsCalcuTaskVo voEntity=new BmsCalcuTaskVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("查询计算任务异常",e);
			throw new BizException("查询计算任务异常");
		}
		return pageVoInfo;
	}

	@Override
	public BmsCalcuTaskVo sendTask(final BmsCalcuTaskVo vo) throws Exception {
		logger.info("请求参数：",JSONObject.fromObject(vo));
		
		if(StringUtil.isEmpty(vo.getSubjectCode()) || 
				StringUtil.isEmpty(vo.getCustomerId()) || vo.getCreMonth() == null){
			throw new BizException("商家ID,科目编码,业务年月必填");
		}
		
		String customerName = customerDictService.getCustomerNameByCode(vo.getCustomerId());
		if(StringUtil.isEmpty(customerName)){
			throw new BizException("商家【"+vo.getCustomerId()+"】不存在");
		}
		
		BmsSubjectInfoVo subjectVo = bmsSubjectService.queryReceiveByCode(vo.getSubjectCode());
		if(subjectVo == null || StringUtil.isEmpty(subjectVo.getSubjectName())){
			throw new BizException("科目【"+vo.getSubjectCode()+"】不存在");
		}
		vo.setCustomerName(customerName);
		vo.setSubjectName(subjectVo.getSubjectName());
		String customerId = vo.getCustomerId();
		String subjectCode = vo.getSubjectCode();
		String creMonth = vo.getCreMonth().toString();
		String lockString = MD5Util.getMd5("BMS_CALCU_MQ"+customerId+subjectCode+creMonth);
		final Map<String, Object> handMap = new HashMap<>();
		handMap.put("success", "fail");
		handMap.put("remark", "");
		
		
		
		try{
			String taskId = "CT"+snowflakeSequenceService.nextStringId();//任务ID
			vo.setTaskId(taskId);
			vo.setCreTime(JAppContext.currentTimestamp());
			vo.setTaskStatus(0);
			
			lock.lock(lockString, 5, new LockCallback<Map<String, Object>>() {
				@Override
				public Map<String, Object> handleObtainLock() {
					//状态判断
					validate(vo);
					handMap.put("success", "success");
					return handMap;
				}

				@Override
				public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
					handMap.put("success", "fail");
					handMap.put("remark", "已存在计费请求,丢弃");
					vo.setTaskStatus(0);
					vo.setRemark("已存在计费请求,丢弃");
					saveTask(vo);
					return handMap;
				}

				@Override
				public Map<String, Object> handleException(LockInsideExecutedException e) throws LockInsideExecutedException {
					handMap.put("success", "fail");
					handMap.put("remark", "请求异常,丢弃");
					vo.setTaskStatus(0);
					vo.setRemark("请求异常,丢弃");
					saveTask(vo);
					return handMap;
				}
			});
		}
		catch(Exception ex){
			logger.error("系统异常",ex);
			vo.setTaskStatus(0);
			vo.setRemark("系统异常");
		}
		return vo;
	}
	
	private void sendMq(final BmsCalcuTaskVo vo){
		String subjectCode = vo.getSubjectCode();
		String mqQueue = null;
		if("wh_product_storage".equals(subjectCode) && "item".equals(vo.getFeesType())){
			mqQueue = MQSubjectEnum.getDesc(subjectCode+"_item");
		}
		else{
			mqQueue = MQSubjectEnum.getDesc(subjectCode);
		}
		jmsQueueTemplate.send(mqQueue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(vo.getTaskId());
			}
		});
		saveTask(vo);
	}
	
	@Override
	public void saveTask(BmsCalcuTaskVo vo) {
		BmsAsynCalcuTaskEntity calcuTask=new BmsAsynCalcuTaskEntity();
		try {
			PropertyUtils.copyProperties(calcuTask,vo);
			bmsAsynCalcuTaskRepositoryimpl.save(calcuTask);
		} catch (Exception e) {
			logger.error("保存任务异常",e);
		}
	}

	@Override
	public void update(BmsCalcuTaskVo taskVo) throws Exception {
		try{
			BmsAsynCalcuTaskEntity calcuTask=new BmsAsynCalcuTaskEntity();
			PropertyUtils.copyProperties(calcuTask,taskVo);
			bmsAsynCalcuTaskRepositoryimpl.update(calcuTask);
		}catch(Exception e){
			logger.error("更新计算任务异常",e);
			throw new BizException("更新计算任务异常");
		}
	}

	@Override
	public void updateRate(String taskId, Integer taskRate) {
		BmsAsynCalcuTaskEntity calcuTask=new BmsAsynCalcuTaskEntity();
		calcuTask.setTaskId(taskId);
		calcuTask.setTaskRate(taskRate);
		try{
			bmsAsynCalcuTaskRepositoryimpl.update(calcuTask);
		}catch(Exception e){
			logger.error("更新计算进度异常",e);
			throw new BizException("更新计算进度异常");
		}    
	}

	@Override
	public BmsCalcuTaskVo queryCalcuTask(String taskId) throws Exception {
		BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
		try{
			BmsAsynCalcuTaskEntity calcuTask = bmsAsynCalcuTaskRepositoryimpl.queryOne(taskId);
			if(calcuTask == null){
				return null;
			}
			PropertyUtils.copyProperties(vo, calcuTask);
			return vo;
		}catch(Exception e){
			logger.error("查询计算任务异常",e);
			throw new BizException("查询计算任务异常");
		}    
	}

	@Override
	public List<BmsCalcuTaskVo> queryByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list=bmsAsynCalcuTaskRepositoryimpl.queryByMap(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if(list == null){
			return null;
		}
    	for(BmsAsynCalcuTaskEntity entity : list) {
    		BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败");
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public List<BmsCalcuTaskVo> queryDisByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list=bmsAsynCalcuTaskRepositoryimpl.queryDisByMap(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if(list == null){
			return null;
		}
		for(BmsAsynCalcuTaskEntity entity : list) {
    		BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败");
            }
    		voList.add(vo);
    	}
		return voList;
	}

	public void validate(BmsCalcuTaskVo vo){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("customerId", vo.getCustomerId());
		map.put("subjectCode", vo.getSubjectCode());
		map.put("creMonth", vo.getCreMonth());
		List<BmsAsynCalcuTaskEntity> calList=bmsAsynCalcuTaskRepositoryimpl.query(map);
		//状态0和10的新的直接丢弃，不处理
		for(BmsAsynCalcuTaskEntity entity:calList){
			if(entity.getTaskStatus()==0 ||entity.getTaskStatus()==10){
				//不处理，不发送mq
				return;
			}
		}
		//状态20和30的将原来的置为丢弃40，新的插入
		//40的老的不处理，新的插入
		List<BmsAsynCalcuTaskEntity> updateList=new ArrayList<BmsAsynCalcuTaskEntity>();
		for(BmsAsynCalcuTaskEntity entity:calList){
			if(entity.getTaskStatus()==20 ||entity.getTaskStatus()==30){
				entity.setTaskStatus(40);
				updateList.add(entity);
			}
		}
		if(updateList.size()>0){
			//更新历史的
			bmsAsynCalcuTaskRepositoryimpl.updateBatch(updateList);
		}		
		sendMq(vo);		
	}

}