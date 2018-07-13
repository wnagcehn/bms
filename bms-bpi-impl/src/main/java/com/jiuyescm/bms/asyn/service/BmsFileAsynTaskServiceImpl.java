package com.jiuyescm.bms.asyn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsFileAsynTaskRepository;


/**
 * 文件异步导入导出实现类
 * @author cjw
 * 
 */
@Service("bmsFileAsynTaskService")
public class BmsFileAsynTaskServiceImpl implements IBmsFileAsynTaskService {

	private static final Logger logger = Logger.getLogger(BmsFileAsynTaskServiceImpl.class.getName());
	
	@Autowired private IBmsFileAsynTaskRepository bmsFileAsynTaskRepoImpl;

    @Override
    public PageInfo<BmsFileAsynTaskVo> query(Map<String, Object> condition,int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsFileAsynTaskVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsFileAsynTaskVo>();
			PageInfo<BmsFileAsynTaskEntity> pageInfo=bmsFileAsynTaskRepoImpl.query(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsFileAsynTaskVo> list=new ArrayList<BmsFileAsynTaskVo>();
				for(BmsFileAsynTaskEntity entity:pageInfo.getList()){
					BmsFileAsynTaskVo vo=new BmsFileAsynTaskVo();
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
    public BmsFileAsynTaskVo findByTaskId(String taskId) throws Exception {
    	BmsFileAsynTaskVo vo=null;
    	try{
    		vo=new BmsFileAsynTaskVo();
    		BmsFileAsynTaskEntity taskEntity=bmsFileAsynTaskRepoImpl.findByTaskId(taskId);
    		PropertyUtils.copyProperties(vo, taskEntity);
    		
    	}catch(Exception e){
    		logger.error("findByTaskId:",e);
			throw e;
    	}
    	return vo; 
    }

    @Override
    public int save(BmsFileAsynTaskVo vo) throws Exception {
    	try{
    		BmsFileAsynTaskEntity taskEntity=new BmsFileAsynTaskEntity();
        	PropertyUtils.copyProperties(taskEntity, vo);
            return bmsFileAsynTaskRepoImpl.save(taskEntity);
    	}catch(Exception e){
    		logger.error("save:",e);
			throw e;
    	}
    
    }

    @Override
    public int update(BmsFileAsynTaskVo vo){
    	try{
    		BmsFileAsynTaskEntity taskEntity=new BmsFileAsynTaskEntity();
        	PropertyUtils.copyProperties(taskEntity, vo);
            return bmsFileAsynTaskRepoImpl.update(taskEntity);
    	}catch(Exception e){
    		logger.error("update:",e);
			return 0;
    	}
    }
}
