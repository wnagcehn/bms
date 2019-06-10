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
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Controller("bmsGroupCustomerController")
public class BmsGroupCustomerController {
	
	private static final Logger logger = Logger.getLogger(BmsGroupCustomerController.class.getName());

	@Autowired
	private IBmsGroupService bmsGroupService;
	
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IBmsGroupCustomerService bmsGroupCustomerService;
	
	@DataProvider
	public void queryGroupCustomer(Page<BmsGroupCustomerVo> page,BmsGroupCustomerVo queryCondition) throws Exception{
		if(queryCondition==null){
			return;
		}
		PageInfo<BmsGroupCustomerVo> pageInfo=null;
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
				pageInfo=bmsGroupCustomerService.queryGroupCustomer(queryCondition, page.getPageNo(), page.getPageSize());
				if(page!=null){
					List<BmsGroupCustomerVo> groupCustomerList=pageInfo.getList();
					initGroup(groupCustomerList);
					page.setEntities(groupCustomerList);
					page.setEntityCount((int)pageInfo.getTotal());
				}
			}
		}catch(Exception e){
			logger.error("queryGroupSubject error:",e);
			throw e;
		}
	}

	private void initGroup(List<BmsGroupCustomerVo> groupCustomerList) throws Exception{
		List<BmsGroupVo> groupList=bmsGroupService.queryAllGroup();
		for(BmsGroupCustomerVo customerVo:groupCustomerList){
			for(BmsGroupVo groupVo:groupList){
				if(groupVo.getId()==customerVo.getGroupId()){
					customerVo.setGroupName(groupVo.getGroupName());
					customerVo.setGroupCode(groupVo.getGroupCode());
					break;
				}
			}
		}
		List<CustomerVo> cusList=customerService.queryAll();
		for(BmsGroupCustomerVo customerVo:groupCustomerList){
			for(CustomerVo cu:cusList){
				if(customerVo.getCustomerid().equals(cu.getCustomerid())){
					customerVo.setShortname(cu.getShortname());
					customerVo.setCustomername(cu.getCustomername());
					customerVo.setMkInvoiceName(cu.getMkInvoiceName());
				}
			}
		}
	}
	@DataResolver
	public String deleteData(BmsGroupCustomerVo subjectVo) throws Exception{
		subjectVo.setLastModifier(JAppContext.currentUserName());
		subjectVo.setLastModifyTime(JAppContext.currentTimestamp());
		int k=bmsGroupCustomerService.delGroupCustomer(subjectVo);
		if(k>0){
		    //不计费的商家特殊处理
            if("不计费商家".equals(subjectVo.getGroupName())){
                try {
                   int result= bmsGroupCustomerService.restoreCustomerBiz(subjectVo.getCustomerid());
                   if(result<=0){
                        return "作废商家业务数据失败";
                   }
                } catch (Exception e) {
                    // TODO: handle exception
                    logger.error("作废商家业务数据失败",e);
                    return "作废商家业务数据失败";
                }
            }
		    
		    
			return "删除成功!";
		}else{
			return "删除失败!";
		}
	}
	@DataResolver
	public String saveDataList(BmsGroupCustomerVo customerVo,List<CustomerVo> customerList) throws Exception{
		
		List<BmsGroupCustomerVo> customerVoList=new ArrayList<BmsGroupCustomerVo>();
		List<String> customerIdList=new ArrayList<String>();
		Timestamp currentTime=JAppContext.currentTimestamp();
		String currentUser=JAppContext.currentUserName();
		for(CustomerVo cusVo:customerList){
			customerIdList.add(cusVo.getCustomerid());
			BmsGroupCustomerVo voEntity=new BmsGroupCustomerVo();
			voEntity.setCreateTime(currentTime);
			voEntity.setCreator(currentUser);
			voEntity.setGroupId(customerVo.getGroupId());
			voEntity.setCustomerid(cusVo.getCustomerid());
			voEntity.setLastModifier(currentUser);
			voEntity.setLastModifyTime(currentTime);
			voEntity.setDelFlag("0");
			customerVoList.add(voEntity);
		}
		List<String> existList=bmsGroupCustomerService.checkCustomerCodeExist(customerVo.getGroupId(),customerIdList);
		if(existList!=null&&existList.size()>0){
			String errorMsg="商家类别【"+customerVo.getGroupName()+"】 已存在商家:";
			for(String subjectName:existList){
				errorMsg+=subjectName+",";
			}
			return errorMsg;
		}else{
			int k=bmsGroupCustomerService.addBatch(customerVoList);
			if(k==customerVoList.size()){
			    //不计费的商家特殊处理
			    if("no_calculate_customer".equals(customerVo.getGroupCode())){
			        try {
			           int result= bmsGroupCustomerService.cancalCustomerBiz(customerVoList);
			           if(result<=0){
	                        return "作废商家业务数据失败";
			           }
                    } catch (Exception e) {
                        // TODO: handle exception
                        logger.error("作废商家业务数据失败",e);
                        return "作废商家业务数据失败";
                    }
			    }
			    
				return "保存成功!";
			}else{
				return "保存失败";
			}
		}
	}
	
	@DataResolver
	public void saveDisplayLevel(BmsGroupCustomerVo vo) throws Exception{
		try{
			vo.setLastModifier(JAppContext.currentUserName());
			vo.setLastModifyTime(JAppContext.currentTimestamp());
			int k=bmsGroupCustomerService.updateGroupCustomer(vo);
			if(k==0){
				throw new Exception("保存失败");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
}
