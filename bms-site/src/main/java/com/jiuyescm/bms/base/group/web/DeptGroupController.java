package com.jiuyescm.bms.base.group.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.jiuyescm.iaccount.api.IDeptService;
import com.jiuyescm.iaccount.api.IUserService;
import com.jiuyescm.iaccount.vo.DeptVO;
import com.jiuyescm.iaccount.vo.UserVO;



@Controller("deptGroupController")
public class DeptGroupController {
	@Autowired 
	private IDeptService accountDeptService;
	@Autowired 
	private IUserService accountUserService;
	
	@DataProvider
	public List<DeptVoEntity> loadDataByParent(String parentId){
		List<DeptVoEntity> voList=new ArrayList<DeptVoEntity>();
		if(StringUtils.isBlank(parentId)){
			DeptVO vo=accountDeptService.findDeptById("100");
			DeptVoEntity voEntity=new DeptVoEntity();
			voEntity.setId(vo.getId());
			voEntity.setDeptName(vo.getName());
			voEntity.setParentId(vo.getParentId());
			voList.add(voEntity);
			return voList;
		}else{
			List<DeptVO> list=accountDeptService.findDeptsByParentId(parentId);
			for(DeptVO vo:list){
				DeptVoEntity voEntity=new DeptVoEntity();
				voEntity.setId(vo.getId());
				voEntity.setDeptName(vo.getName());
				voEntity.setParentId(vo.getParentId());
				voList.add(voEntity);
			}
			return voList;
		}
	
	
	}
	@DataProvider
	public List<UserVO> queryAllUserByDept(Map<String,Object> parameter){
		if(parameter==null){
			return null;
		}
		String deptId="";
		String userId="";
		String userName="";
		if(parameter.containsKey("deptId")){
			deptId=parameter.get("deptId").toString();
		}
		if(parameter.containsKey("userId")){
			userId=parameter.get("userId").toString();
		}
		if(parameter.containsKey("userName")){
			userName=parameter.get("userName").toString();
		}
		List<UserVO> list=null;
		if(StringUtils.isNotBlank(deptId)){
			list=accountUserService.findUsersByDept(deptId);
			if(StringUtils.isNotBlank(userId)){
				list=queryByUserId(list,userId);
			}
			if(StringUtils.isNotBlank(userName)){
				list=queryByUserName(list,userName);
			}
		}
		return list;
	}
	private List<UserVO> queryByUserId(List<UserVO> list,String userId){
		List<UserVO> copyList=new ArrayList<UserVO>();
		for(UserVO vo:list){
			if(vo.getUsername().contains(userId)){
				copyList.add(vo);
			}
		}
		return copyList;
	}
	private List<UserVO> queryByUserName(List<UserVO> list,String userName){
		List<UserVO> copyList=new ArrayList<UserVO>();
		for(UserVO vo:list){
			if(vo.getCname().contains(userName)){
				copyList.add(vo);
			}
		}
		return copyList;
	}
}
