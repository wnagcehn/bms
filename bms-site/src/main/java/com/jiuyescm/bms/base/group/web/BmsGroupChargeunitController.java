package com.jiuyescm.bms.base.group.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.service.IBmsGroupChargeunitService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupChargeunitVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.base.unitInfo.entity.BmsChargeUnitInfoEntity;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsGroupChargeunitController")
public class BmsGroupChargeunitController {

	private static final Logger logger = LoggerFactory.getLogger(BmsGroupChargeunitController.class.getName());
	
	@Autowired
	private IBmsGroupService bmsGroupService;
	
	@Autowired
	private IBmsGroupChargeunitService bmsGroupChargeunitService;

	@DataProvider
	public void queryGroupUnit(Page<BmsGroupChargeunitVo> page,BmsGroupChargeunitVo queryCondition) throws Exception{
		if(queryCondition==null){
			return;
		}
		PageInfo<BmsGroupChargeunitVo> pageInfo=null;
		try{
			//先通过group_code去查询id
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("groupCode", queryCondition.getGroupCode());
			condition.put("bizType", queryCondition.getBizType());
			BmsGroupVo bmsGroupVo=bmsGroupService.queryOne(condition);
			if(bmsGroupVo!=null){
				pageInfo=bmsGroupChargeunitService.queryGroupUnit(queryCondition, page.getPageNo(), page.getPageSize());
				if(page!=null){
					List<BmsGroupChargeunitVo> groupUnitList=pageInfo.getList();
					initGroup(groupUnitList);
					page.setEntities(groupUnitList);
					page.setEntityCount((int)pageInfo.getTotal());
				}
			}
		}catch(Exception e){
			logger.error("queryGroupSubject error:",e);
			throw e;
		}
	}

	private void initGroup(List<BmsGroupChargeunitVo> groupUnitList) throws Exception{
		List<BmsGroupVo> groupList=bmsGroupService.queryAllGroup();
		for(BmsGroupChargeunitVo customerVo:groupUnitList){
			for(BmsGroupVo groupVo:groupList){
				if(groupVo.getId()==customerVo.getGroupId()){
					customerVo.setGroupName(groupVo.getGroupName());
					break;
				}
			}
		}
	}
	@DataResolver
	public String deleteData(BmsGroupChargeunitVo subjectVo) throws Exception{
		subjectVo.setLastModifier(JAppContext.currentUserName());
		subjectVo.setLastModifyTime(JAppContext.currentTimestamp());
		int k=bmsGroupChargeunitService.delGroupUnit(subjectVo);
		if(k>0){
			return "删除成功!";
		}else{
			return "删除失败!";
		}
	}
	@DataResolver
	public String saveDataList(BmsGroupChargeunitVo unitVo,List<BmsChargeUnitInfoEntity> unitList) throws Exception{
		
		List<BmsGroupChargeunitVo> unitVoList=new ArrayList<BmsGroupChargeunitVo>();
		List<String> unitLists=new ArrayList<String>();
		Timestamp currentTime=JAppContext.currentTimestamp();
		String currentUser=JAppContext.currentUserName();
		for(BmsChargeUnitInfoEntity unVo:unitList){
			unitLists.add(unVo.getUnitCode());
			BmsGroupChargeunitVo voEntity=new BmsGroupChargeunitVo();
			voEntity.setCreateTime(currentTime);
			voEntity.setCreator(currentUser);
			voEntity.setGroupId(unitVo.getGroupId());
			voEntity.setUnitCode(unVo.getUnitCode());
			voEntity.setUnitName(unVo.getUnitName());
			voEntity.setLastModifier(currentUser);
			voEntity.setLastModifyTime(currentTime);
			voEntity.setDelFlag("0");
			unitVoList.add(voEntity);
		}
		List<String> existList=bmsGroupChargeunitService.checkUnitCodeExist(unitVo.getGroupId(),unitLists);
		if(existList!=null&&existList.size()>0){
			String errorMsg="单位类别【"+unitVo.getGroupName()+"】 已存在单位:";
			for(String subjectName:existList){
				errorMsg+=subjectName+",";
			}
			return errorMsg;
		}else{
			int k=bmsGroupChargeunitService.addBatch(unitVoList);
			if(k==unitVoList.size()){
				return "保存成功!";
			}else{
				return "保存失败";
			}
		}
	}
	
	@DataResolver
	public void saveDisplayLevel(BmsGroupChargeunitVo vo) throws Exception{
		try{
			vo.setLastModifier(JAppContext.currentUserName());
			vo.setLastModifyTime(JAppContext.currentTimestamp());
			int k=bmsGroupChargeunitService.updateGroupUnit(vo);
			if(k==0){
				throw new Exception("保存失败");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
	
}
