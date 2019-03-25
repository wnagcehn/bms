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
import com.jiuyescm.bms.common.enumtype.MQSubjectEnum;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;

@Service("bmsCalcuTaskService")
public class BmsCalcuTaskServiceImpl implements IBmsCalcuTaskService{
	
	private Logger logger = LoggerFactory.getLogger(BmsCalcuTaskServiceImpl.class);

	@Autowired IBmsAsynCalcuTaskRepository bmsAsynCalcuTaskRepositoryimpl;
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
		String taskId = "CT"+snowflakeSequenceService.nextStringId();//任务ID
		vo.setTaskId(taskId);
		vo.setCreTime(JAppContext.currentTimestamp());
		vo.setTaskStatus(0);
		saveTask(vo);
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
		List<BmsAsynCalcuTaskEntity> list=bmsAsynCalcuTaskRepositoryimpl.queryByMap(condition);
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
