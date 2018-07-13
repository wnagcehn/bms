package com.jiuyescm.bms.asyn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsCorrectAsynTaskRepository;
import com.jiuyescm.cfm.common.JAppContext;

@Service("bmsCorrectAsynTaskService")
public class BmsCorrectAsynTaskServiceImpl implements IBmsCorrectAsynTaskService{

	private static final Logger logger = Logger.getLogger(BmsCorrectAsynTaskServiceImpl.class.getName());
	
	@Autowired private IBmsCorrectAsynTaskRepository bmsCorrectAsynTaskRepository;

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

}
