package com.jiuyescm.bms.base.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.repository.IBmsGroupChargeunitRepository;
import com.jiuyescm.bms.base.group.repository.IBmsGroupRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupChargeunitService;
import com.jiuyescm.bms.base.group.vo.BmsGroupChargeunitVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsGroupChargeunitService")
public class BmsGroupChargeunitServiceImpl implements IBmsGroupChargeunitService {

	private static final Logger logger = Logger.getLogger(BmsGroupChargeunitServiceImpl.class.getName());
	
	@Autowired
    private IBmsGroupChargeunitRepository bmsGroupChargeunitRepository;
	
	@Autowired
	private IBmsGroupRepository bmsGroupRepository;

	@Override
	public List<BmsGroupChargeunitVo> queryAllByGroupId(int groupId) throws Exception {
		List<BmsGroupChargeunitVo> voList=null;
		try{
			List<BmsGroupChargeunitEntity> entityList=bmsGroupChargeunitRepository.queryAllByGroupId(groupId);
			voList=new ArrayList<BmsGroupChargeunitVo>();
			for(BmsGroupChargeunitEntity entity:entityList){
				BmsGroupChargeunitVo vo=new BmsGroupChargeunitVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("queryAllByGroupId:",e);
			throw e;
		}
		return voList;
	}

	@Override
	public int addBatch(List<BmsGroupChargeunitVo> list) throws Exception {
		try{
			List<BmsGroupChargeunitEntity> entityList=new ArrayList<BmsGroupChargeunitEntity>();
			for(BmsGroupChargeunitVo vo:list){
				BmsGroupChargeunitEntity entity=new BmsGroupChargeunitEntity();
				PropertyUtils.copyProperties(entity,vo);
				entityList.add(entity);
			}
			return bmsGroupChargeunitRepository.addBatch(entityList);
		}catch(Exception e){
			logger.error("addBatch:",e);
			throw e;
		}
	}

	@Override
	public int delGroupUnit(BmsGroupChargeunitVo unitVo) throws Exception {
		try{
			BmsGroupChargeunitEntity unitEntity=new BmsGroupChargeunitEntity();
			PropertyUtils.copyProperties(unitEntity,unitVo);
			int k=bmsGroupChargeunitRepository.delGroupUnit(unitEntity);
			return k;
		}catch(Exception e){
			logger.error("delGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public int updateGroupUnit(BmsGroupChargeunitVo subjectVo) throws Exception {
		try{
			BmsGroupChargeunitEntity entity=new BmsGroupChargeunitEntity();
			PropertyUtils.copyProperties(entity, subjectVo);
			return bmsGroupChargeunitRepository.updateGroupUnit(entity);
		}catch(Exception e){
			logger.error("updateGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public PageInfo<BmsGroupChargeunitVo> queryGroupUnit(
			BmsGroupChargeunitVo queryCondition, int pageNo, int pageSize) throws Exception {
		PageInfo<BmsGroupChargeunitVo> pageVoInfo=null;
		try{
			if(queryCondition==null){
				return pageVoInfo;
			}
			pageVoInfo=new PageInfo<BmsGroupChargeunitVo>();
			BmsGroupChargeunitEntity queryEntity=new BmsGroupChargeunitEntity();
			PropertyUtils.copyProperties(queryEntity, queryCondition);
			PageInfo<BmsGroupChargeunitEntity> pageInfo=bmsGroupChargeunitRepository.queryGroupUnit(queryEntity, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsGroupChargeunitVo> list=new ArrayList<BmsGroupChargeunitVo>();
				for(BmsGroupChargeunitEntity entity:pageInfo.getList()){
					BmsGroupChargeunitVo voEntity=new BmsGroupChargeunitVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			throw e;
		}
		return pageVoInfo;
	}

	@Override
	public List<String> checkUnitCodeExist(int groupId,
			List<String> unitIdList) {
		return bmsGroupChargeunitRepository.checkUnitCodeExist(groupId,unitIdList);
	}

	@Override
	public int queryUnitCountByGroupId(int groupId) {
		return bmsGroupChargeunitRepository.queryUnitCountByGroupId(groupId);
	}

	@Override
	public List<String> queryUnitByGroupId(int groupId) {
		// TODO Auto-generated method stub
		return bmsGroupChargeunitRepository.queryUnitByGroupId(groupId);
	}
	
	@Override
	public List<BmsGroupChargeunitEntity> queryByGroupCode(Map<String, String> param){
		return bmsGroupChargeunitRepository.queryByGroupCode(param);
	}
	
}
