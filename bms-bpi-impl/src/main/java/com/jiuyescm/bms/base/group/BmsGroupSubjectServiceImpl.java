package com.jiuyescm.bms.base.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.entity.BmsGroupSubEntity;
import com.jiuyescm.bms.base.group.repository.IBmsGroupRepository;
import com.jiuyescm.bms.base.group.repository.IBmsGroupSubjectRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupSubjectVo;
import com.thoughtworks.xstream.mapper.Mapper.Null;

@Service("bmsGroupSubjectService")
public class BmsGroupSubjectServiceImpl implements IBmsGroupSubjectService {
	
	private static final Logger logger = Logger.getLogger(BmsGroupSubjectServiceImpl.class.getName());

	@Autowired
	private IBmsGroupRepository bmsGroupRepository;
	@Autowired
	private IBmsGroupSubjectRepository bmsGroupSubjectRepository;
	@Override
	public List<BmsGroupSubjectVo> queryAllByGroupId(int groupId) throws Exception {
		List<BmsGroupSubjectVo> voList=null;
		try{
			List<BmsGroupSubjectEntity> entityList=bmsGroupSubjectRepository.queryAllByGroupId(groupId);
			voList=new ArrayList<BmsGroupSubjectVo>();
			for(BmsGroupSubjectEntity entity:entityList){
				BmsGroupSubjectVo vo=new BmsGroupSubjectVo();
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
	public int addBatch(List<BmsGroupSubjectVo> list) throws Exception {
		try{
			List<BmsGroupSubjectEntity> entityList=new ArrayList<BmsGroupSubjectEntity>();
			for(BmsGroupSubjectVo vo:list){
				BmsGroupSubjectEntity entity=new BmsGroupSubjectEntity();
				PropertyUtils.copyProperties(entity,vo);
				entityList.add(entity);
			}
			return bmsGroupSubjectRepository.addBatch(entityList);
		}catch(Exception e){
			logger.error("addBatch:",e);
			throw e;
		}
	}

	@Override
	public int delGroupSubject(BmsGroupSubjectVo subjectVo) throws Exception {
		try{
			BmsGroupSubjectEntity subjectEntity=new BmsGroupSubjectEntity();
			PropertyUtils.copyProperties(subjectEntity,subjectVo);
			return bmsGroupSubjectRepository.delGroupSubject(subjectEntity);
		}catch(Exception e){
			logger.error("delGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public int updateGroupSubject(BmsGroupSubjectVo subjectVo) throws Exception {
		try{
			BmsGroupSubjectEntity entity=new BmsGroupSubjectEntity();
			PropertyUtils.copyProperties(entity, subjectVo);
			return bmsGroupSubjectRepository.updateGroupSubject(entity);
		}catch(Exception e){
			logger.error("updateGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public PageInfo<BmsGroupSubjectVo> queryGroupSubject(
			BmsGroupSubjectVo queryCondition, int pageNo, int pageSize) throws Exception {
		PageInfo<BmsGroupSubjectVo> pageVoInfo=null;
		try{
			if(queryCondition==null){
				return pageVoInfo;
			}
			pageVoInfo=new PageInfo<BmsGroupSubjectVo>();
			BmsGroupSubjectEntity queryEntity=new BmsGroupSubjectEntity();
			PropertyUtils.copyProperties(queryEntity, queryCondition);
			PageInfo<BmsGroupSubEntity> pageInfo=bmsGroupSubjectRepository.queryGroupSubject(queryEntity, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsGroupSubjectVo> list=new ArrayList<BmsGroupSubjectVo>();
				for(BmsGroupSubEntity entity:pageInfo.getList()){
					BmsGroupSubjectVo voEntity=new BmsGroupSubjectVo();
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
	public List<String> checkSubjectCodeExist(int groupId,
			List<String> subjectCodeList) {
		return bmsGroupSubjectRepository.checkSubjectCodeExist(groupId,subjectCodeList);
	}

	@Override
	public int querySubjectCountByGroupId(int groupId) {
		return bmsGroupSubjectRepository.querySubjectCountByGroupId(groupId);
	}

	private  static final String GROUPCODE="groupCode";
	private  static final String BIZTYPE="bizType";
	private  static final String BMSSUBJECT="bms_subject";
	@Override
	public Map<String, String> getImportSubject(String groupCode) {
		//指定的商家
		Map<String,String> resultMap=new HashMap<>();
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put(GROUPCODE, groupCode);
			map.put(BIZTYPE, BMSSUBJECT);
			BmsGroupEntity bmsGroup=bmsGroupRepository.queryOne(map);
			if(bmsGroup!=null){
				List<BmsGroupSubjectEntity> list=bmsGroupSubjectRepository.queryAllByGroupId(bmsGroup.getId());
				for(BmsGroupSubjectEntity vo:list){
					resultMap.put(vo.getSubjectName().trim(), vo.getSubjectCode());
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("getImportSubject:",e);
		}
		return resultMap;
	}
	
	@Override
	public List<BmsGroupSubjectEntity> queryAllByGroupIdAndBizTypeCode(Map<String, Object> param) {
		List<BmsGroupSubjectEntity> list = null;
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put(GROUPCODE, "subject_discount_receive");
			map.put(BIZTYPE, BMSSUBJECT);
			BmsGroupEntity bmsGroup=bmsGroupRepository.queryOne(map);
			if(bmsGroup!=null){
				BmsGroupSubjectEntity entity = new BmsGroupSubjectEntity();		
				if (param.get("bizTypeCode") == null) {
					return null;
				}
				entity.setBizTypecode(param.get("bizTypeCode").toString());
				entity.setGroupId(bmsGroup.getId());
				list=bmsGroupSubjectRepository.queryAllByGroupIdAndBizTypeCode(entity);
				if (list.isEmpty()) {
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("queryAllByGroupIdAndBizTypeCode:",e);
		}
		return list;
	}
	
	@Override
	public List<BmsGroupSubjectEntity> queryGroupSubject() {
		List<BmsGroupSubjectEntity> list = null;
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put(GROUPCODE, "subject_discount_receive");
			map.put(BIZTYPE, BMSSUBJECT);
			BmsGroupEntity bmsGroup=bmsGroupRepository.queryOne(map);
			if(bmsGroup!=null){
				BmsGroupSubjectEntity entity = new BmsGroupSubjectEntity();		
				entity.setGroupId(bmsGroup.getId());
				list=bmsGroupSubjectRepository.queryGroupSubject(entity);
				if (list.isEmpty()) {
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("queryGroupSubject:",e);
		}
		return list;
	}


	/**
	 * 导出的费用科目
	 * @param groupCode
	 * @return
	 */
	@Override
	public Map<String, String> getExportSubject(String groupCode) {
		//指定的商家
		Map<String,String> resultMap=new HashMap<>();
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put(GROUPCODE, groupCode);
			map.put(BIZTYPE, BMSSUBJECT);
			BmsGroupEntity bmsGroup=bmsGroupRepository.queryOne(map);
			if(bmsGroup!=null){
				List<BmsGroupSubjectEntity> list=bmsGroupSubjectRepository.queryAllByGroupId(bmsGroup.getId());
				for(BmsGroupSubjectEntity vo:list){
					resultMap.put(vo.getSubjectCode().trim(), vo.getSubjectName().trim());
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("getExportSubject:",e);
		}
		return resultMap;
	}
	
	/**
	 * 导出的费用科目
	 * @param groupCode
	 * @return
	 */
	@Override
	public Map<String, String> getSubject(String groupCode) {
		//指定的商家
		Map<String,String> resultMap=new HashMap<>();
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("bizType", "bms_subject");
			BmsGroupEntity bmsGroup=bmsGroupRepository.queryOne(map);
			if(bmsGroup!=null){
				List<BmsGroupSubjectEntity> list=bmsGroupSubjectRepository.queryAllByGroupId(bmsGroup.getId());
				for(BmsGroupSubjectEntity vo:list){
					resultMap.put(vo.getSubjectCode().trim(), vo.getSubjectName().trim());
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("getSubject:",e);
		}
		return resultMap;
	}
}
