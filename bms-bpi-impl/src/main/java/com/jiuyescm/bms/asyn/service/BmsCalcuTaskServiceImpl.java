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
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
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
	@Autowired private Lock lock;
	@Autowired private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired private JmsTemplate jmsQueueTemplate;
	
	
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
		String customerId = vo.getCustomerId();
		String subjectCode = vo.getSubjectCode();
		String creMonth = vo.getCreMonth().toString();
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
		map.put("subjectCode", subjectCode);
		map.put("creMonth", creMonth);
		//使用validate
		String lockString = MD5Util.getMd5("BMS_CALCU_MQ"+customerId+subjectCode+creMonth);
		final Map<String, Object> handMap = new HashMap<>();
		handMap.put("success", "fail");
		handMap.put("remark", "");
		//使用分布式锁 防止并发情况下生产多条同样的计费请求
		lock.lock(lockString, 5, new LockCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> handleObtainLock() {
				
				List<BmsAsynCalcuTaskEntity> list = bmsAsynCalcuTaskRepositoryimpl.queryUnfinish(map);
				//查询表中是否存在未完成的任务
				if(list==null || list.size()==0){
					handMap.put("success", "success");
				}
				else{
					handMap.put("success", "fail");
					handMap.put("remark", "已存在计算的任务");
				}
				return handMap;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				//未拿到锁 丢弃->同一时间不允许 相同商家，相同科目，相同年月同时计算
				handMap.put("success", "fail");
				handMap.put("remark", "请求过于频繁,丢弃");
				return handMap;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e) throws LockInsideExecutedException {
				handMap.put("success", "fail");
				handMap.put("remark", "请求异常,丢弃");
				return handMap;
			}
		});
		String succ = handMap.get("success").toString();
		String taskId = "CT"+snowflakeSequenceService.nextStringId();//任务ID
		vo.setTaskId(taskId);
		vo.setCreTime(JAppContext.currentTimestamp());
		//请求通过 保存数据
		if("success".equals(succ)){
			//请求成功 发送mq消息
			vo.setTaskStatus(0);
			saveTask(vo);
			jmsQueueTemplate.send("BMS.QUEUE.CALCU.WORK", new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(vo.getTaskId());
				}
			});
		}
		else{
			vo.setTaskStatus(40);//请求不成功,直接丢弃 不发送mq
			vo.setRemark(handMap.get("remark").toString());
			saveTask(vo);
			throw new BizException(vo.getRemark());
		}
		return vo;
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
		List<BmsAsynCalcuTaskEntity> list=bmsAsynCalcuTaskRepositoryimpl.query(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
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



}
