package com.jiuyescm.bms.base.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.group.repository.IBmsGroupRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;

@Service("bmsGroupService")
public class BmsGroupServiceImpl implements IBmsGroupService{
	
	private static final Logger logger = Logger.getLogger(BmsGroupServiceImpl.class.getName());

	@Autowired
	private IBmsGroupRepository bmsGroupRepository;

	@Override
	public int addGroup(BmsGroupVo voEntity) throws Exception {
		try{
			BmsGroupEntity entity=new BmsGroupEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return bmsGroupRepository.addGroup(entity);
		}catch(Exception e){
			logger.error("addGroup:",e);
			throw e;
		}
	}

	@Override
	public int deleteGroup(int id) {
		return bmsGroupRepository.deleteGroup(id);
	}

	@Override
	public int updateGroup(BmsGroupVo voEntity) throws Exception {
		try{
			BmsGroupEntity entity=new BmsGroupEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return bmsGroupRepository.updateGroup(entity);
		}catch(Exception e){
			logger.error("updateGroup:",e);
			throw e;
		}
	}

	@Override
	public List<BmsGroupVo> queryAllGroup() throws Exception {
		List<BmsGroupVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupVo>();
			List<BmsGroupEntity> list=bmsGroupRepository.queryAllGroup();
			for(BmsGroupEntity entity:list){
				BmsGroupVo voEntity=new BmsGroupVo();
				PropertyUtils.copyProperties(voEntity, entity);
				volist.add(voEntity);
			}
			return volist;
		}catch(Exception e){
			logger.error("queryAllGroup:",e);
			throw e;
		}
		
	}
	
	@Override
	public List<BmsGroupVo> findAreaEnumList(Map<String, String> param) throws Exception {
		List<BmsGroupVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupVo>();
			List<BmsGroupEntity> list=bmsGroupRepository.findAreaEnumList(param);
			for(BmsGroupEntity entity:list){
				BmsGroupVo voEntity=new BmsGroupVo();
				PropertyUtils.copyProperties(voEntity, entity);
				volist.add(voEntity);
			}
			return volist;
		}catch(Exception e){
			logger.error("findAreaEnumList:",e);
			throw e;
		}
		
	}

	@Override
	public List<BmsGroupVo> queryDataByParentId(int parentId) throws Exception {
		List<BmsGroupVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupVo>();
			List<BmsGroupEntity> list=bmsGroupRepository.queryDataByParentIdAndBizType(parentId,"bill_follow");
			if(list!=null){
				for(BmsGroupEntity entity:list){
					BmsGroupVo voEntity=new BmsGroupVo();
					PropertyUtils.copyProperties(voEntity, entity);
					volist.add(voEntity);
				}
			}
			return volist;
		}catch(Exception e){
			logger.error("queryAllGroup:",e);
			throw e;
		}
	}
	
	@Override
	public List<BmsGroupVo> querySaleAreaDataByParentId(int parentId) throws Exception {
		List<BmsGroupVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupVo>();
			List<BmsGroupEntity> list=bmsGroupRepository.queryDataByParentIdAndBizType(parentId,"sale_area");
			if(list!=null){
				for(BmsGroupEntity entity:list){
					BmsGroupVo voEntity=new BmsGroupVo();
					PropertyUtils.copyProperties(voEntity, entity);
					volist.add(voEntity);
				}
			}
			return volist;
		}catch(Exception e){
			logger.error("queryAllGroup:",e);
			throw e;
		}
	}

	@Override
	public int queryChildGroupCount(int id) {
		return bmsGroupRepository.queryChildGroupCount(id);
	}

	@Override
	public List<BmsGroupVo> queryDataByParentIdAndBizType(int pid,
			String bizTypeCode) throws Exception {
		List<BmsGroupVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupVo>();
			List<BmsGroupEntity> list=bmsGroupRepository.queryDataByParentIdAndBizType(pid,bizTypeCode);
			if(list!=null){
				for(BmsGroupEntity entity:list){
					BmsGroupVo voEntity=new BmsGroupVo();
					PropertyUtils.copyProperties(voEntity, entity);
					volist.add(voEntity);
				}
			}
			return volist;
		}catch(Exception e){
			logger.error("queryDataByParentIdAndBizType:",e);
			throw e;
		}
	}

	@Override
	public List<BmsGroupVo> queryBmsSubjectGroupByParentId(int parentId) throws Exception {
		List<BmsGroupVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupVo>();
			List<BmsGroupEntity> list=bmsGroupRepository.queryDataByParentIdAndBizType(parentId,"bms_subject");
			if(list!=null){
				for(BmsGroupEntity entity:list){
					BmsGroupVo voEntity=new BmsGroupVo();
					PropertyUtils.copyProperties(voEntity, entity);
					volist.add(voEntity);
				}
			}
			return volist;
		}catch(Exception e){
			logger.error("queryAllGroup:",e);
			throw e;
		}
	}

	@Override
	public boolean checkGroup(BmsGroupVo voEntity) throws Exception {
		try{
			BmsGroupEntity entity=new BmsGroupEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return bmsGroupRepository.checkGroup(entity);
		}catch(Exception e){
			logger.error("checkGroup:",e);
			throw e;
		}
	}

	@Override
	public BmsGroupVo queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsGroupEntity entity=bmsGroupRepository.queryOne(condition);
		try{
			BmsGroupVo vo= new BmsGroupVo();
			if (null != entity) {
				PropertyUtils.copyProperties(vo, entity);
				return vo;
			}		
			return null;
		}catch(Exception e){
			logger.error("checkGroup:",e);
			throw null;
		}
	}
	
	@Override
	public BmsGroupVo queryIdByBizType() {
		// TODO Auto-generated method stub
		BmsGroupEntity entity=bmsGroupRepository.queryIdByBizType();
		try{
			BmsGroupVo vo= new BmsGroupVo();
			if (null != entity) {
				PropertyUtils.copyProperties(vo, entity);
				return vo;
			}		
			return null;
		}catch(Exception e){
			logger.error("checkGroup:",e);
			throw null;
		}
	}
}
