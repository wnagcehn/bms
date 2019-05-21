package com.jiuyescm.bms.base.group.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupSubjectVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;


@Controller("bmsGroupController")
public class BmsGroupController {
	
	private static final Logger logger = Logger.getLogger(BmsGroupController.class.getName());

	@Autowired
	private IBmsGroupService bmsGroupService;
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;
	
	@Autowired
	private IBmsGroupSubjectService bmsGroupSubjectService;
	@Resource
	private ISystemCodeService systemCodeService;
	
	@DataProvider
	public List<BmsGroupVo> loadDataByParent(String parentId) throws Exception{
		int pid=0;
		if(StringUtils.isNoneBlank(parentId)){
			pid=Integer.valueOf(parentId);
		}
		return bmsGroupService.queryDataByParentId(pid);
	}
	
	/**
	 * 销售区域管理
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public List<BmsGroupVo> loadSaleAreaDataByParent(String parentId) throws Exception{
		int pid=0;
		if(StringUtils.isNoneBlank(parentId)){
			pid=Integer.valueOf(parentId);
		}
		return bmsGroupService.querySaleAreaDataByParentId(pid);
	}
	
	@DataProvider
	public List<BmsGroupVo> loadSubjectDataByParent(String parentId) throws Exception{
		int pid=0;
		if(StringUtils.isNoneBlank(parentId)){
			pid=Integer.valueOf(parentId);
		}
		return bmsGroupService.queryBmsSubjectGroupByParentId(pid);
	}

	@DataResolver
	public void saveSubjectGroup(List<BmsGroupVo> datas) throws Exception{
		if (datas == null) {
			return;
		}
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userName=JAppContext.currentUserName();
		for(BmsGroupVo voEntity:datas){
			if (EntityState.NEW.equals(EntityUtils.getState(voEntity))) {
				voEntity.setCreateTime(nowdate);
				voEntity.setCreator(userName);
				voEntity.setBizType("bms_subject");
				if(!bmsGroupService.checkGroup(voEntity)){
					throw new Exception("组编码已经存在");
				}
				bmsGroupService.addGroup(voEntity);
			}
			else if (EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))) {
				voEntity.setLastModifyTime(nowdate);
				voEntity.setLastModifier(userName);
				bmsGroupService.updateGroup(voEntity);
			}
			List<BmsGroupVo> list=voEntity.getChildren();
			if(list!=null){
				saveSubjectGroup(list);
			}
		}
	
	}
	
	@DataProvider
	public List<BmsGroupVo> loadUnitDataByParent(String parentId) throws Exception{
		int pid=0;
		if(StringUtils.isNoneBlank(parentId)){
			pid=Integer.valueOf(parentId);
		}
		return bmsGroupService.queryDataByParentIdAndBizType(pid,"bms_charge_unit");
	}
	
	@DataResolver
	public void saveUnitGroup(List<BmsGroupVo> datas) throws Exception{
		if (datas == null) {
			return;
		}
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userName=JAppContext.currentUserName();
		for(BmsGroupVo voEntity:datas){
			if (EntityState.NEW.equals(EntityUtils.getState(voEntity))) {
				voEntity.setCreateTime(nowdate);
				voEntity.setCreator(userName);
				voEntity.setBizType("bms_charge_unit");
				if(!bmsGroupService.checkGroup(voEntity)){
					throw new Exception("组编码已经存在");
				}
				bmsGroupService.addGroup(voEntity);
			}
			else if (EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))) {
				voEntity.setLastModifyTime(nowdate);
				voEntity.setLastModifier(userName);
				bmsGroupService.updateGroup(voEntity);
			}
			List<BmsGroupVo> list=voEntity.getChildren();
			if(list!=null){
				saveUnitGroup(list);
			}
		}
	
	}
	
	@DataResolver
	public void saveGroup(List<BmsGroupVo> datas) throws Exception{
		if (datas == null) {
			return;
		}
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userName=JAppContext.currentUserName();
		for(BmsGroupVo voEntity:datas){
			if (EntityState.NEW.equals(EntityUtils.getState(voEntity))) {
				voEntity.setCreateTime(nowdate);
				voEntity.setCreator(userName);
				voEntity.setBizType("bill_follow");
				if(!bmsGroupService.checkGroup(voEntity)){
					throw new Exception("组编码已经存在");
				}
				bmsGroupService.addGroup(voEntity);
			}
			else if (EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))) {
				voEntity.setLastModifyTime(nowdate);
				voEntity.setLastModifier(userName);
				bmsGroupService.updateGroup(voEntity);
			}
			List<BmsGroupVo> list=voEntity.getChildren();
			if(list!=null){
				saveGroup(list);
			}
		}
	
	}
	
	/**
	 * 销售区域管理
	 * @param datas
	 * @throws Exception
	 */
	@DataResolver
	public void saveSaleAreaGroup(List<BmsGroupVo> datas) throws Exception{
		if (datas == null) {
			return;
		}
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userName=JAppContext.currentUserName();
		for(BmsGroupVo voEntity:datas){
			if (EntityState.NEW.equals(EntityUtils.getState(voEntity))) {
				voEntity.setCreateTime(nowdate);
				voEntity.setCreator(userName);
				voEntity.setBizType("sale_area");
				if(!bmsGroupService.checkGroup(voEntity)){
					throw new Exception("组编码已经存在");
				}
				bmsGroupService.addGroup(voEntity);
			}
			else if (EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))) {
				voEntity.setLastModifyTime(nowdate);
				voEntity.setLastModifier(userName);
				bmsGroupService.updateGroup(voEntity);
			}
			List<BmsGroupVo> list=voEntity.getChildren();
			if(list!=null){
				saveSaleAreaGroup(list);
			}
		}
	
	}
	/**
	 * 删除  验证是否有子用户组
	 * 验证用户组 是否有用户信息
	 * @param id
	 * @return
	 */
	@DataResolver
	public String delGroup(int id){
		String mes="";
		try{
			int c=bmsGroupService.queryChildGroupCount(id);
			if(c>0){
				throw new Exception("删除失败,请先删除子用户!");
			}
			int u=bmsGroupUserService.queryUserCountByGroupId(id);
			if(u>0){
				throw new Exception("删除失败,此用户组下有用户,无法删除!");
			}
			int k=bmsGroupService.deleteGroup(id);
			if(k==0){
				return "删除失败";
			}
			return "";
		}catch(Exception e){
			mes=e.getMessage();
			return mes;
		}
	}
	
	/**
	 * 删除  验证是否有子用户组
	 * 验证用户组 是否有用户信息
	 * @param id
	 * @return
	 */
	@DataResolver
	public String delSubjectGroup(int id){
		String mes="";
		try{
			int c=bmsGroupService.queryChildGroupCount(id);
			if(c>0){
				throw new Exception("删除失败,请先删除子用户!");
			}
			int u=bmsGroupSubjectService.querySubjectCountByGroupId(id);
			if(u>0){
				throw new Exception("删除失败,科目下有明细,无法删除!");
			}
			int k=bmsGroupService.deleteGroup(id);
			if(k==0){
				return "删除失败";
			}
			return "";
		}catch(Exception e){
			mes=e.getMessage();
			return mes;
		}
	}
	@DataProvider
	public void queryGroupUser(Page<BmsGroupUserVo> page,Map<String,Object> parameter) throws Exception{
		PageInfo<BmsGroupUserVo> pageInfo=null;
		try{
			pageInfo=bmsGroupUserService.query(parameter, page.getPageNo(), page.getPageSize());
			if(page!=null){
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int)pageInfo.getTotal());
			}
		}catch(Exception e){
			logger.error("queryAll error:",e);
			throw e;
		}
	}
	
	@DataProvider
	public void queryGroupSale(Page<BmsGroupUserVo> page,Map<String,Object> parameter) throws Exception{
		PageInfo<BmsGroupUserVo> pageInfo=null;
		try{
			pageInfo=bmsGroupUserService.querySaleUser(parameter, page.getPageNo(), page.getPageSize());
			if(page!=null){
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int)pageInfo.getTotal());
			}
		}catch(Exception e){
			logger.error("queryGroupSale error:",e);
			throw e;
		}
	}
	
	@DataProvider
	public Map<String,Object> getAllGroupMap() throws Exception{
		Map<String,Object> map=Maps.newLinkedHashMap();
		List<BmsGroupVo> list=bmsGroupService.queryAllGroup();
		for(BmsGroupVo groupVo:list){
			map.put(String.valueOf(groupVo.getId()),groupVo.getGroupName());
		}
		return map;
	}
	@DataProvider
	public Map<String,Object> isAdmin(){
		Map<String,Object> map=Maps.newLinkedHashMap();
		map.put("0", "是");
		map.put("1", "否");
		return map;
	}
	@DataResolver
	public String saveGroupUser(BmsGroupUserVo voEntity) throws Exception{
		String result="";
		try{
			if(EntityState.NEW.equals(EntityUtils.getState(voEntity))){
				voEntity.setCreator(JAppContext.currentUserName());
				voEntity.setCreateTime(JAppContext.currentTimestamp());
				voEntity.setDelFlag("0");
				String groupName=bmsGroupUserService.checkExistGroupName(voEntity.getUserId());
				if(StringUtils.isBlank(groupName)){
					int k=bmsGroupUserService.addGroupUser(voEntity);
					if(k>0){
						return "新增成功!";
					}else{
						return "新增失败!";
					}
				}else{
					throw new Exception("用户"+voEntity.getUserName()+" 已存在于权限组【"+groupName+"】中,不可重新添加!");
				}
			}else if(EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))){
				voEntity.setLastModifier(JAppContext.currentUserName());
				voEntity.setLastModifyTime(JAppContext.currentTimestamp());
				bmsGroupUserService.updateGroupUser(voEntity);
			}
			result="保存成功";
		}catch(Exception e){
			throw e;
		}		
		return result;
	}
	
	@DataResolver
	public String deleteGroupUser(BmsGroupUserVo voEntity){
		String result = "";
		voEntity.setDelFlag("1");
		try {
			int k=bmsGroupUserService.updateGroupUser(voEntity);
			if(k>0){
				return "删除成功!";
			}else{
				return "删除失败!";
			}
		} catch (Exception e) {
			logger.error("删除失败", e);
			result = "删除失败";
		}
		return result;
	}
	
	@DataProvider
	public List<BmsGroupVo> loadCustomerDataByParent(String parentId) throws Exception{
		int pid=0;
		if(StringUtils.isNoneBlank(parentId)){
			pid=Integer.valueOf(parentId);
		}
		return bmsGroupService.queryDataByParentIdAndBizType(pid,"group_customer");
	}
	
	@DataResolver
	public void saveCustomerGroup(List<BmsGroupVo> datas) throws Exception{
		if (datas == null) {
			return;
		}
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userName=JAppContext.currentUserName();
		for(BmsGroupVo voEntity:datas){
			if (EntityState.NEW.equals(EntityUtils.getState(voEntity))) {
				voEntity.setCreateTime(nowdate);
				voEntity.setCreator(userName);
				voEntity.setBizType("group_customer");
				if(!bmsGroupService.checkGroup(voEntity)){
					throw new Exception("组编码已经存在");
				}
				bmsGroupService.addGroup(voEntity);
			}
			else if (EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))) {
				voEntity.setLastModifyTime(nowdate);
				voEntity.setLastModifier(userName);
				bmsGroupService.updateGroup(voEntity);
			}
			List<BmsGroupVo> list=voEntity.getChildren();
			if(list!=null){
				saveCustomerGroup(list);
			}
		}
	
	}
	
	@DataProvider
	public Map<String,Object> getSubject(String groupCode){
		//指定的商家
		Map<String,Object> resultMap=new HashMap<>();
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("bizType", "bms_subject");
			BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
			if(bmsGroup!=null){
				List<BmsGroupSubjectVo> list=bmsGroupSubjectService.queryAllByGroupId(bmsGroup.getId());
				for(BmsGroupSubjectVo vo:list){
					resultMap.put(vo.getSubjectCode(), vo.getSubjectName());
				}
				return resultMap;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}	
	
	@DataProvider
	public List<BmsGroupSubjectVo> getSubjectIdAndSubjectName(String groupCode){
		List<BmsGroupSubjectVo> bgsList = new ArrayList<>();
		try {
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("bizType", "bms_subject");
			BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
			if(bmsGroup!=null){
				List<BmsGroupSubjectVo> list=bmsGroupSubjectService.queryAllByGroupId(bmsGroup.getId());
				for(BmsGroupSubjectVo vo:list){
					BmsGroupSubjectVo bgsVo = new BmsGroupSubjectVo();
					bgsVo.setSubjectCode(vo.getSubjectCode());
					bgsVo.setSubjectName(vo.getSubjectName());
					bgsList.add(bgsVo);
				}
				return bgsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bgsList;
	}	
	
	@DataProvider
	public Map<String, String> findAreaEnumList(String groupCode) throws Exception {
		
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		//mapValue.put("ALL","全部");
		mapValue.put("groupCode", groupCode);
		List<BmsGroupVo> tmscodels = bmsGroupService.findAreaEnumList(mapValue);
		mapValue.clear();
		for (BmsGroupVo bmsGroupVo : tmscodels) {
			mapValue.put(bmsGroupVo.getGroupCode(), bmsGroupVo.getGroupName());
		}
		
		return mapValue;
	}
	
	@DataResolver
	public String saveSaleUser(BmsGroupUserVo voEntity) {
		Map<String, Object> param = null;
		String result="";
		
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList("SALE_AREA");
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		if (EntityState.NEW.equals(EntityUtils.getState(voEntity))) {
			//新增
			try{
				param = new HashMap<String, Object>();
				voEntity.setCreator(JAppContext.currentUserName());
				voEntity.setCreateTime(JAppContext.currentTimestamp());
				voEntity.setDelFlag("0");
				BmsGroupVo gVo = bmsGroupService.queryIdByBizType();
				if (null != gVo) {
					voEntity.setGroupId(gVo.getId());
				}else {
					return "销售区域组未配置!";
				}
				param.put("userId", voEntity.getUserId());
				String areaCode=bmsGroupUserService.checkSaleUser(param);
				if(StringUtils.isBlank(areaCode)){
					int k=bmsGroupUserService.addGroupUser(voEntity);
					if(k>0){
						return "新增成功!";
					}else{
						return "新增失败!";
					}
				}else{
					result = "用户"+voEntity.getUserName()+" 已存在于区域【"+mapValue.get(areaCode)+"】中,不可重新添加!";
				}
			}catch(Exception e){
				logger.error("新增异常", e);
				throw new BizException("新增用户异常！");
			}	
		}else if(EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))){
			//修改
			try{
				param = new HashMap<String, Object>();
				voEntity.setLastModifier(JAppContext.currentUserName());
				voEntity.setLastModifyTime(JAppContext.currentTimestamp());
				
				param.put("userId", voEntity.getUserId());
				param.put("id", voEntity.getId());
				String areaCode=bmsGroupUserService.checkSaleUserIgnoreId(param);
				if(StringUtils.isBlank(areaCode)){
					int k=bmsGroupUserService.updateGroupUser(voEntity);
					if(k>0){
						return "更新成功!";
					}else{
						return "更新失败!";
					}
				}else{
					result = "用户"+voEntity.getUserName()+" 已存在于区域【"+mapValue.get(areaCode)+"】中,不可重新添加!";
				}
			}catch(Exception e){
				logger.error("更新异常", e);
				throw new BizException("更新用户信息异常！");
			}	
		}
		
		return result;
	}

   @DataProvider
    public Map<String,String> getAddFeeSubject(String groupCode){
        return bmsGroupSubjectService.getWmsValueAddSubjectGroups();
    }

   @DataProvider
   public List<Map<String, String>> getAddFeeType(Map<String, Object> parameter) {
       String firstSubject = (String) parameter.get("firstSubject");
       if(StringUtils.isBlank(firstSubject)){
           return null;
       }
       Map<String, String> map = bmsGroupSubjectService.getWmsValueAddSubjectDetails(firstSubject);
       if(CollectionUtils.isEmpty(map)){
           return null;
       }
       List<Map<String, String>> list = new ArrayList<>();
       for (String key : map.keySet()) {
           Map<String, String> bean = new HashMap<>();
           bean.put("feesType", key);
           bean.put("feesTypeName", map.get(key));
           list.add(bean);
        }
       return list;
   }
}
