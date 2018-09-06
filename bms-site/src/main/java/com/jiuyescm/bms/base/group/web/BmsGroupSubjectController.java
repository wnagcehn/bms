package com.jiuyescm.bms.base.group.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.group.BmsGroupSubjectEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupSubjectVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("bmsGroupSubjectController")
public class BmsGroupSubjectController {
	
	private static final Logger logger = Logger.getLogger(BmsGroupSubjectController.class.getName());

	@Autowired
	private IBmsGroupService bmsGroupService;
	@Autowired
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	@DataProvider
	public void queryGroupSubject(Page<BmsGroupSubjectVo> page,BmsGroupSubjectVo queryCondition) throws Exception{
		if(queryCondition==null){
			return;
		}
		PageInfo<BmsGroupSubjectVo> pageInfo=null;
		try{
			//List<BmsGroupVo> groupList=bmsGroupService.queryAllGroup();
			//先通过group_code去查询id
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("groupCode", queryCondition.getGroupCode());
			condition.put("bizType", queryCondition.getBizType());
			BmsGroupVo bmsGroupVo=bmsGroupService.queryOne(condition);
			if(bmsGroupVo!=null){
				//List<Integer> groupIds=getAllGroupIds(groupList,bmsGroupVo.getId());
				//queryCondition.setGroupIds(groupIds);
				pageInfo=bmsGroupSubjectService.queryGroupSubject(queryCondition, page.getPageNo(), page.getPageSize());
				if(page!=null){
					List<BmsGroupSubjectVo> groupSubjectList=pageInfo.getList();
					initGroup(groupSubjectList);
					page.setEntities(groupSubjectList);
					page.setEntityCount((int)pageInfo.getTotal());
				}
			}
		}catch(Exception e){
			logger.error("queryGroupSubject error:",e);
			throw e;
		}
	}
	/*
	 * 获取群组 以及所有子群组ID
	 */
/*	private List<Integer> getAllGroupIds(List<BmsGroupVo> groupList,int groupId){
		List<Integer> list=new ArrayList<Integer>();
		for(BmsGroupVo entity:groupList){
			if(entity.getParentId()==groupId){
				list.add(entity.getId());
				if(checkHasChild(groupList,entity.getId())){//有子项
					list.addAll(getAllGroupIds(groupList,entity.getId()));//递归调用 获得所有子项
				}
			}
		}
		list.add(groupId);
		return list;
	}*/
/*	private Boolean checkHasChild(List<BmsGroupVo> groupList,int id){
		boolean f=false;
		for(BmsGroupVo entity:groupList){
			if(entity.getParentId()==id){
				f=true;
				break;
			}
		}
		return f;
	}*/
	private void initGroup(List<BmsGroupSubjectVo> groupSubjectList) throws Exception{
		List<BmsGroupVo> groupList=bmsGroupService.queryAllGroup();
		for(BmsGroupSubjectVo subjectVo:groupSubjectList){
			for(BmsGroupVo groupVo:groupList){
				if(groupVo.getId()==subjectVo.getGroupId()){
					subjectVo.setGroupName(groupVo.getGroupName());
					break;
				}
			}
		}
	}
	@DataResolver
	public String deleteData(BmsGroupSubjectVo subjectVo) throws Exception{
		subjectVo.setLastModifier(JAppContext.currentUserName());
		subjectVo.setLastModifyTime(JAppContext.currentTimestamp());
		int k=bmsGroupSubjectService.delGroupSubject(subjectVo);
		if(k>0){
			return "删除成功!";
		}else{
			return "删除失败!";
		}
	}
	@DataResolver
	public String saveDataList(BmsGroupSubjectVo subjectVo,List<BmsSubjectInfoEntity> subjectInfoList) throws Exception{
		
		List<BmsGroupSubjectVo> subjectVoList=new ArrayList<BmsGroupSubjectVo>();
		List<String> subjectCodeList=new ArrayList<String>();
		Timestamp currentTime=JAppContext.currentTimestamp();
		String currentUser=JAppContext.currentUserName();
		for(BmsSubjectInfoEntity subjectInfo:subjectInfoList){
			subjectCodeList.add(subjectInfo.getSubjectCode());
			BmsGroupSubjectVo voEntity=new BmsGroupSubjectVo();
			
			voEntity.setCreateTime(currentTime);
			voEntity.setCreator(currentUser);
			voEntity.setGroupId(subjectVo.getGroupId());
			voEntity.setGroupName(subjectVo.getGroupName());
			voEntity.setSubjectId(subjectInfo.getId());
			//voEntity.setSubjectCode(subjectInfo.getSubjectCode());
			//voEntity.setSubjectName(subjectInfo.getSubjectName());
			voEntity.setLastModifier(currentUser);
			voEntity.setLastModifyTime(currentTime);
			subjectVoList.add(voEntity);
		}
		List<String> existList=bmsGroupSubjectService.checkSubjectCodeExist(subjectVo.getGroupId(),subjectCodeList);
		if(existList!=null&&existList.size()>0){
			String errorMsg="科目类别【"+subjectVo.getGroupName()+"】 已存在科目:";
			for(String subjectName:existList){
				errorMsg+=subjectName+",";
			}
			return errorMsg;
		}else{
			int k=bmsGroupSubjectService.addBatch(subjectVoList);
			if(k==subjectVoList.size()){
				return "保存成功!";
			}else{
				return "保存失败";
			}
		}
	}
	
	@DataResolver
	public void saveDisplayLevel(BmsGroupSubjectVo vo) throws Exception{
		try{
			vo.setLastModifier(JAppContext.currentUserName());
			vo.setLastModifyTime(JAppContext.currentTimestamp());
			int k=bmsGroupSubjectService.updateGroupSubject(vo);
			if(k==0){
				throw new Exception("保存失败");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
	
	@DataProvider
	public List<BmsGroupSubjectEntity> queryAllByGroupIdAndBizTypeCode(Map<String, Object> param){
		if (null == param) {
			return null;
		}
		List<BmsGroupSubjectEntity> list = bmsGroupSubjectService.queryAllByGroupIdAndBizTypeCode(param);
		if (null == list || list.size() == 0) {
			List<BmsGroupSubjectEntity> newList = new ArrayList<>();
			return newList;
		}
		return list;
	}
	
	@DataProvider
	public Map<String, String> queryGroupSubject(){
		Map<String, String> map = Maps.newLinkedHashMap();
		List<BmsGroupSubjectEntity> list = bmsGroupSubjectService.queryGroupSubjectByGroupId();
		if (null == list || list.size() == 0) {
			Map<String, String> newMap = new HashMap<>();
			return newMap;
		}
		for (BmsGroupSubjectEntity bmsGroupSubjectEntity : list) {
			map.put(bmsGroupSubjectEntity.getSubjectCode(), bmsGroupSubjectEntity.getSubjectName());
		}
		return map;
	}
}
