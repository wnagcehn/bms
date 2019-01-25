package com.jiuyescm.bms.asyn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsCorrectAsynTaskRepository;
import com.jiuyescm.cfm.common.JAppContext;

@Service("bmsCorrectAsynTaskService")
public class BmsCorrectAsynTaskServiceImpl implements IBmsCorrectAsynTaskService{

	private static final Logger logger = Logger.getLogger(BmsCorrectAsynTaskServiceImpl.class.getName());
	
	private static final String BMS_CORRECT_ASYN_TASK = "BMS.CORRECT.ASYN.TASK";
	
	@Autowired private IBmsCorrectAsynTaskRepository bmsCorrectAsynTaskRepository;
	
	@Autowired private JmsTemplate jmsQueueTemplate;

	@Override
	public PageInfo<BmsCorrectAsynTaskVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) throws Exception {
		PageInfo<BmsCorrectAsynTaskVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsCorrectAsynTaskVo>();
			PageInfo<BmsCorrectAsynTaskEntity> pageInfo=bmsCorrectAsynTaskRepository.query(condition,pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsCorrectAsynTaskVo> list=new ArrayList<BmsCorrectAsynTaskVo>();
				for(BmsCorrectAsynTaskEntity entity:pageInfo.getList()){
					BmsCorrectAsynTaskVo vo=new BmsCorrectAsynTaskVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("query:",e);
			throw e;
		}
		return pageVoInfo;
	}

	@Override
	public BmsCorrectAsynTaskVo findById(int id) throws Exception {
		BmsCorrectAsynTaskVo vo=null;
		try{
			BmsCorrectAsynTaskEntity entity=bmsCorrectAsynTaskRepository.findById(id);
			vo=new BmsCorrectAsynTaskVo();
			PropertyUtils.copyProperties(vo, entity);
		}catch(Exception e){
			logger.error("findById:",e);
			throw e;
		}
		return vo;
	}

	@Override
	public int save(BmsCorrectAsynTaskVo vo) throws Exception {
		try{
			BmsCorrectAsynTaskEntity entity=new BmsCorrectAsynTaskEntity();
			PropertyUtils.copyProperties(entity, vo);
			return bmsCorrectAsynTaskRepository.save(entity);
		}catch(Exception e){
			logger.error("save:",e);
			throw e;
		}
	}

	@Override
	public int update(BmsCorrectAsynTaskVo vo) throws Exception {
		try{
			vo.setFinishTime(JAppContext.currentTimestamp());
			BmsCorrectAsynTaskEntity entity=new BmsCorrectAsynTaskEntity();
			PropertyUtils.copyProperties(entity, vo);
			return bmsCorrectAsynTaskRepository.update(entity);
		}catch(Exception e){
			logger.error("update:",e);
			throw e;
		}
	}

	@Override
	public List<String> queryCorrectCustomerList(
			Map<String, Object> conditionMap) {
	
		return bmsCorrectAsynTaskRepository.queryCorrectCustomerList(conditionMap);
	}

	@Override
	public boolean existTask(BmsCorrectAsynTaskVo voEntity) throws Exception {
		try{
			BmsCorrectAsynTaskEntity entity=new BmsCorrectAsynTaskEntity();
			PropertyUtils.copyProperties(entity,voEntity);
			return bmsCorrectAsynTaskRepository.existTask(entity);
		}catch(Exception e){
			logger.error("existTask:",e);
			throw e;
		}
	}

	@Override
	public int saveBatch(List<BmsCorrectAsynTaskVo> voList) throws Exception {
		try{
			List<BmsCorrectAsynTaskEntity> list=new ArrayList<BmsCorrectAsynTaskEntity>();
			for(BmsCorrectAsynTaskVo voEntity:voList){
				BmsCorrectAsynTaskEntity entity=new BmsCorrectAsynTaskEntity();
				PropertyUtils.copyProperties(entity,voEntity);
				list.add(entity);
			}
			return bmsCorrectAsynTaskRepository.saveBatch(list);
		}catch(Exception e){
			logger.error("saveBatch:",e);
			throw e;
		}
	}

	@Override
	public List<BmsCorrectAsynTaskVo> queryList(Map<String, Object> condition)
			throws Exception {
		// TODO Auto-generated method stub
		List<BmsCorrectAsynTaskEntity> list=bmsCorrectAsynTaskRepository.queryList(condition);
		List<BmsCorrectAsynTaskVo> voList = new ArrayList<BmsCorrectAsynTaskVo>();
    	for(BmsCorrectAsynTaskEntity entity : list) {
    		BmsCorrectAsynTaskVo vo = new BmsCorrectAsynTaskVo();
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
	public String updateCorrect(BmsCorrectAsynTaskVo vo) throws Exception {
		if(StringUtils.isBlank(vo.getTaskId()))return "任务ID为空";
		Map<String, Object> queryConfition = new HashMap<>();
		queryConfition.put("taskId", vo.getTaskId());
		List<BmsCorrectAsynTaskEntity> list = bmsCorrectAsynTaskRepository.queryList(queryConfition);
		if(CollectionUtils.isEmpty(list))return "没有查询到此任务";
		String bizType = list.get(0).getBizType();
		if(!"weight_correct".equals(bizType)&&!"material_correct".equals(bizType))return "此任务不是纠正任务";
		String taskStatus = list.get(0).getTaskStatus();
		if(!"SUCCESS".equals(taskStatus)&&!"EXCEPTION".equals(taskStatus))return "此纠正任务的状态不能纠正";
		//发送MQ消息纠正
		try {
			final String msg = vo.getTaskId();
			jmsQueueTemplate.send(BMS_CORRECT_ASYN_TASK, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(msg);
				}
			});
		} catch (Exception e) {
			logger.error("send MQ:", e);
			return"MQ发送失败！";
		}
		//更新任务表（更新修改人，修改时间）
		try{
			BmsCorrectAsynTaskEntity entity=new BmsCorrectAsynTaskEntity();
//			PropertyUtils.copyProperties(entity, vo);
			entity.setId(vo.getId());
			entity.setLastModifier(vo.getLastModifier());
			entity.setLastModifyTime(vo.getLastModifyTime());
			bmsCorrectAsynTaskRepository.update(entity);
			return "纠正成功";
		}catch(Exception e){
			logger.error("updateCorrect:",e);
			return "更新失败";
		}
	}
	
}
