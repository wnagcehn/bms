package com.jiuyescm.bms.quotation.contract.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

@Controller("carrierContractController")
public class CarrierContractController {

	@Resource
	private IPriceContractService priceContractService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubRecordLogService pubRecordLogService;
	
	private static final Logger logger = LoggerFactory.getLogger(CarrierContractController.class.getName());
	
	/**
	 * 查询所有的商家合同信息
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<PriceContractInfoEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if("999".equals(parameter.get("contractState"))){
				parameter.put("contractState","");
			}
		}else{
			parameter=new HashMap<String,Object>();
		}
		/*
		if(parameter.get("customerName")==null||StringUtils.isBlank(parameter.get("customerName").toString())){
			parameter.put("customerId", "");
		}*/
		//此时查询的为承运商合同
		parameter.put("contractTypeCode", "CARRIER_CONTRACT");
		PageInfo<PriceContractInfoEntity> tmpPageInfo =priceContractService.queryAll(parameter,page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	/*
		Container panelContainer=new Container();
		panelContainer.setId("panelContainer");
		onViewInit(panelContainer);*/
	}
	
	/**
	 * 保存商家数据
	 * @param datas
	 */
	@DataResolver
	public String saveContract(Collection<PriceContractInfoEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PriceContractInfoEntity temp:datas){
				//判断该商家是否已签过合同
				Map<String,Object> parameter=new HashMap<>();
				parameter.put("customerId", temp.getCustomerId());
				parameter.put("contractTypeCode", "CARRIER_CONTRACT");
				
				PageInfo<PriceContractInfoEntity> pageContractList=priceContractService.queryAll(parameter, 0, Integer.MAX_VALUE);
				List<PriceContractInfoEntity> contractList=pageContractList.getList();
				
				if(EntityState.MODIFIED.equals(EntityUtils.getState(temp)) && contractList.size()>0){
					for(int i=0;i<contractList.size();i++){
						if(contractList.get(i).getContractCode().equals(temp.getContractCode())){
							contractList.remove(i);
							break;
						}
					}
				}
				
				//先对合同进行判断,如果该新合同的生效日期存在于老合同的有效日期与失效日期之间，则提示无法新增合同
				boolean isExit=false;
				if(contractList!=null && contractList.size()>0){
					for(int i=0;i<contractList.size();i++){
						PriceContractInfoEntity contractInfoEntity=contractList.get(i);							
						if(temp.getStartDate().getTime()<=contractInfoEntity.getExpireDate().getTime()){
							isExit=true;
							break;
						}
					}
				}
				if(isExit==true){
					return "isExit";
				}
				
				//对操作类型进行判断
				//此为新增商家合同信息
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){ 					
					String templateNo =sequenceService.getBillNoOne(PriceContractInfoEntity.class.getName(), "HT", "00000");
					temp.setContractCode(templateNo);
					temp.setContractState("0");
					//此时新增的为商家合同
					temp.setContractTypeCode(ConstantInterface.ContractType.CARRIER_CONTRACT);
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					int result=priceContractService.createContract(temp);
					if(result>0){	
						try{
							PubRecordLogEntity model=new PubRecordLogEntity();
							model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
							model.setNewData(JSON.toJSONString(temp));
							model.setOldData("");
							model.setOperateDesc("新增承运商合同,合同编号:【"+templateNo+"】");
							model.setOperatePerson(JAppContext.currentUserName());
							model.setOperateTable("price_contract_info");
							model.setOperateTime(JAppContext.currentTimestamp());
							model.setOperateType(RecordLogOperateType.INSERT.getCode());
							model.setRemark("");
							model.setUrlName(RecordLogUrlNameEnum.CONTACT_FORWARDER.getCode());
							model.setOperateTableKey(templateNo);
							pubRecordLogService.AddRecordLog(model);
						}catch(Exception e){
							logger.error("记录日志失败,失败原因:"+e.getMessage());
						}
					}else{
						return "新增承运商合同失败";
					}
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					//此为修改商家合同信息
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					int result=priceContractService.updateContract(temp);
					if(result>0){//记录日志
						try{
							PubRecordLogEntity model=new PubRecordLogEntity();
							model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
							model.setNewData(JSON.toJSONString(temp));
							//PriceContractInfoEntity oldTemp=priceContractService.queryById(temp.getId());
							model.setOldData("");
							model.setOperateDesc("更新承运商合同,合同编号:【"+temp.getContractCode()+"】");
							model.setOperatePerson(JAppContext.currentUserName());
							model.setOperateTable("price_contract_info");
							model.setOperateTime(JAppContext.currentTimestamp());
							model.setOperateType(RecordLogOperateType.UPDATE.getCode());
							model.setRemark("");
							model.setUrlName(RecordLogUrlNameEnum.CONTACT_FORWARDER.getCode());
							model.setOperateTableKey(temp.getContractCode());
							pubRecordLogService.AddRecordLog(model);
						}catch(Exception e){
							logger.error("记录日志失败,失败原因:"+e.getMessage());
						}
					}else{
						return "编辑承运商合同失败";
					}
				}
				
			}
			
			return "承运商合同保存成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
		
			return "承运商合同保存失败";
		}
		
	
	}
	
	/**
	 * 删除承运商合同
	 * @param p
	 * @return
	 */
	@DataResolver
	public String removeContract(PriceContractInfoEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setDelFlag("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			priceContractService.updateContract(p);
			
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData(JSON.toJSONString(p));
				//PriceContractInfoEntity oldTemp=priceContractService.queryById(temp.getId());
				model.setOldData("");
				model.setOperateDesc("作废承运商合同,合同编号:【"+p.getContractCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_info");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_FORWARDER.getCode());
				model.setOperateTableKey(p.getContractCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "删除承运商合同成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
		
			return "删除承运商合同失败";
		}
	}
	
	/**
	 * 审核合同
	 * @param 
	 * @return
	 */
	@DataResolver
	public String checkContract(PriceContractInfoEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setContractState("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			priceContractService.updateContract(p);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData(JSON.toJSONString(p));
				//PriceContractInfoEntity oldTemp=priceContractService.queryById(p.getId());
				model.setOldData("");
				model.setOperateDesc("审核承运商合同,合同编号:【"+p.getContractCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_info");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_FORWARDER.getCode());
				model.setOperateTableKey(p.getContractCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "审核合同成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
		
			return "审核合同失败";
		}
	}
	
	/**
	 * 保存承运商合同明细数据
	 * @param datas
	 */
	@DataResolver
	public String saveContractItem(Collection<PriceContractItemEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PriceContractItemEntity temp:datas){
				//对操作类型进行判断
				//此为新增商家合同明细信息
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){ 
					//先判断该合同是否已签约服务
					Map<String,Object> condition=new HashMap<String,Object>();
					condition.put("contractCode", temp.getContractCode());
					
					List<ContractDetailEntity> contractList=priceContractService.findAllPayContractItem(condition);
					//若存在原来的服务需删除后签约
					if(contractList!=null && contractList.size()>0){
						List<PriceContractItemEntity> cList=new ArrayList<>();
						for(int i=0;i<contractList.size();i++){
							PriceContractItemEntity b=new PriceContractItemEntity();
							b.setContractCode(temp.getContractCode());
							b.setDelFlag("1");
							b.setLastModifier(userid);
							b.setLastModifyTime(nowdate);
							cList.add(b);
							
						}	
						priceContractService.updateContractItem(cList);
					}
					
					/*1001-RC00024&1007-12&1002-16&1006-19*/
					List<PriceContractItemEntity> itemList=new ArrayList<>();
					String[] item=temp.getSubjectId().split("&");
					for(String s:item){
						PriceContractItemEntity newItem=new PriceContractItemEntity();
						if(!StringUtils.isBlank(s)){
							String[] detail=s.split("#");
							String[] bizDetail=s.split("@");
							newItem.setContractCode(temp.getContractCode());
							newItem.setBizTypeCode(bizDetail[0]);
							newItem.setSubjectId(detail[0].substring(detail[0].indexOf("@")+1));	
							newItem.setTemplateId(detail[1]);
							newItem.setCreator(userid);
							newItem.setCreateTime(nowdate);
							newItem.setDelFlag("0");
							itemList.add(newItem);
						}
					}
					
					priceContractService.createContractItem(itemList);
					//priceContractService.createContract(temp);*/
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					//此为修改商家合同信息
					List<PriceContractItemEntity> itemList=new ArrayList<>();
					String[] item=temp.getSubjectId().split("&");
					for(String s:item){
						PriceContractItemEntity newItem=new PriceContractItemEntity();
						if(!StringUtils.isBlank(s)){
							String[] detail=s.split("#");
							newItem.setId(temp.getId());
							newItem.setSubjectId(detail[0]);	
							newItem.setTemplateId(detail[1]);
							newItem.setLastModifier(userid);
							newItem.setLastModifyTime(nowdate);
							itemList.add(newItem);
						}
					}
					
					//priceContractService.updateContractItem(itemList);
				}
				
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData("");
				model.setOldData("");
				model.setOperateDesc("承运商合同签约服务");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_item");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_FORWARDER.getCode());
				String key="";
				if(datas!=null&&datas.size()>0){
					for(PriceContractItemEntity temp:datas){
						key=temp.getContractCode();
						break;
					}
				}
				model.setOperateTableKey(key);
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "保存承运商合同明细成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
		
			return "保存承运商合同明细失败";
		}
		
	
	}
	
	//查询所有的服务
	@Expose
	public String queryAllContractItem(PriceContractInfoEntity parameter){
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("contractCode", parameter.getContractCode());
		List<ContractDetailEntity> contractList=priceContractService.findAllPayContractItem(condition);
		//System.out.println(contractList);
		String result="";
		for(int i=0;i<contractList.size();i++){
			ContractDetailEntity c=contractList.get(i);
			//运输的
			if("TRANSPORT_PRODUCT_TYPE".equals(c.getCodeType()) || "TRANSPORT_TEMPLATE_TYPE".equals(c.getCodeType())){				
				result+=c.getSubjectId()+"&"+c.getTemplateId()+",";
			}
		}
		
		return result;
	}
	
	/**
	 * 查询出所有的运输服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryTranslate(Page<ContractDetailEntity> page,Map<String,Object> parameter){
		Map<String,Object> aCondition=new HashMap<String,Object>();
		aCondition.put("contractCode", parameter.get("contractId"));
		aCondition.put("bizTypeCode", "TRANSPORT");
		List<ContractDetailEntity> contractList=priceContractService.findAllPayContractDetail(aCondition);
		
		List<ContractDetailEntity> translateList=new ArrayList<>();
		
		for(int i=0;i<contractList.size();i++){			
			ContractDetailEntity translate=contractList.get(i);
			//运输类型
			translate.setTheLastName(translate.getCodeName());
			//translate.setSubjectName(translate.getTheLastName());
			translate.setRemark(translate.getTransportRemark());
			translateList.add(translate);
		}
		page.setEntities(translateList);
		page.setEntityCount(translateList.size());
	}
	
	/**
	 * 获取所有的运输服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryTransportService(Page<SystemCodeEntity> page,Map<String,Object> parameter){

		//List<SystemCodeEntity> systemCodeList=new ArrayList<>();
		List<SystemCodeEntity> systemCodeList= systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		page.setEntities(systemCodeList);
	}
	
	/**
	 * 审核状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCheckedState(String result) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if("ALL".equals(result)){
			mapValue.put("999","全部");
		}
		mapValue.put(ConstantInterface.IsChecked.ISCHECKED_YES,"已审核");
		mapValue.put(ConstantInterface.IsChecked.ISCHECKED_NO, "未审核");
		return mapValue;
	}
	
}
