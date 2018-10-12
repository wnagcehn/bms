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
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.contract.quote.api.IContractDiscountService;
import com.jiuyescm.contract.quote.vo.CarrierInfoVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountQueryVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountVo;
import com.jiuyescm.contract.quote.vo.SubjectInfoVo;

@Controller("customerContractController")
public class CustomerContractController {

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
	
	@Resource
	private IPriceContractDiscountService priceContractDiscountService;
	
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	@Resource
	private IContractDiscountService contractDiscountService;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerContractController.class.getName());
	
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
		//此时查询的为商家合同
		parameter.put("contractTypeCode", "CUSTOMER_CONTRACT");
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
				parameter.put("contractTypeCode", "CUSTOMER_CONTRACT");
				
				PageInfo<PriceContractInfoEntity> pageContractList=priceContractService.queryAll(parameter, 0, Integer.MAX_VALUE);
				List<PriceContractInfoEntity> contractList=pageContractList.getList();
				
				if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
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
					temp.setContractTypeCode(ConstantInterface.ContractType.CUSTOMER_CONTRACT);
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
							model.setOperateDesc("新增商家合同,合同编号:【"+templateNo+"】");
							model.setOperatePerson(JAppContext.currentUserName());
							model.setOperateTable("price_contract_info");
							model.setOperateTime(JAppContext.currentTimestamp());
							model.setOperateType(RecordLogOperateType.INSERT.getCode());
							model.setRemark("");
							model.setOperateTableKey(templateNo);
							model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
							pubRecordLogService.AddRecordLog(model);
						}catch(Exception e){
							logger.error("记录日志失败,失败原因:"+e.getMessage());
						}
					}else{
						return "新增商家合同失败";
					}
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					//此为修改商家合同信息
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					int result=priceContractService.updateContract(temp);
					if(result>0){
						try{
							PubRecordLogEntity model=new PubRecordLogEntity();
							model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
							model.setNewData(JSON.toJSONString(temp));
							//PriceContractInfoEntity oldTemp=priceContractService.queryById(temp.getId());
							model.setOldData("");
							model.setOperateDesc("更新商家合同,合同编号:【"+temp.getContractCode()+"】");
							model.setOperatePerson(JAppContext.currentUserName());
							model.setOperateTable("price_contract_info");
							model.setOperateTime(JAppContext.currentTimestamp());
							model.setOperateType(RecordLogOperateType.UPDATE.getCode());
							model.setRemark("");
							model.setOperateTableKey(temp.getContractCode());
							model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
							pubRecordLogService.AddRecordLog(model);
						}catch(Exception e){
							logger.error("记录日志失败,失败原因:"+e.getMessage());
						}
					}else{
						return "编辑商家合同失败";
					}
				}
				
			}
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.getMessage());

			return "数据库操作失败";
		}
		
	
	}
	
	/**
	 * 删除商家合同
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
				model.setNewData("");
				model.setOldData(JSON.toJSONString(p));
				model.setOperateDesc("删除商家合同,合同编号:【"+p.getContractCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_info");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getContractCode());
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.getMessage());

			return "数据库操作失败";
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
				model.setOldData("");
				model.setOperateDesc("审核商家合同,合同编号:【"+p.getContractCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_info");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getContractCode());
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.getMessage());

			return "数据库操作失败";
		}
	}
	
	/**
	 * 审核合同
	 * @param 
	 * @return
	 */
	@DataResolver
	public String uncheckContract(PriceContractInfoEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setContractState("0");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			priceContractService.updateContract(p);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData(JSON.toJSONString(p));
				model.setOldData("");
				model.setOperateDesc("反审核合同,合同编号:【"+p.getContractCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_info");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getContractCode());
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.getMessage());

			return "数据库操作失败";
		}
	}
	/**
	 * 保存商家合同明细数据
	 * @param datas
	 */
	@DataResolver
	public String saveContractItem(Collection<PriceContractItemEntity> datas){
		
		/*if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}*/
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PriceContractItemEntity temp:datas){
				//对操作类型进行判断
				//此为新增商家合同明细信息
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){ 
					//先判断该合同是否已签约服务
					List<ContractDetailEntity> contractList=priceContractService.findAllContractItemName(temp.getContractCode());
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
					if(item.length>0){
						for(String s:item){
							PriceContractItemEntity newItem=new PriceContractItemEntity();
							if(!StringUtils.isBlank(s)){
								String[] detail=s.split("#");
								String[] bizDetail=s.split("@");
								if(StringUtils.isNotBlank(bizDetail[0]) && StringUtils.isNotBlank(detail[0]) && StringUtils.isNotBlank(detail[1])){
									newItem.setContractCode(temp.getContractCode());
									newItem.setBizTypeCode(bizDetail[0].trim());
									newItem.setSubjectId(detail[0].substring(detail[0].indexOf("@")+1));	
									newItem.setTemplateId(detail[1].trim());
									newItem.setCreator(userid);
									newItem.setCreateTime(nowdate);
									newItem.setDelFlag("0");
									itemList.add(newItem);
								}							
							}
						}
						int result=priceContractService.createContractItem(itemList);
						if(result<=0){
							return "保存科目失败";
						}
					}
				}
				
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData("");
				model.setOldData("");
				model.setOperateDesc("商家合同签约服务");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_item");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				String key="";
				if(datas!=null&&datas.size()>0){
					for(PriceContractItemEntity temp:datas){
						key=temp.getContractCode();
						break;
					}
				}
				model.setOperateTableKey(key);
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			logger.error("系统错误", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.getMessage());

			return "数据库操作失败";
		}
		
	
	}
	
	/**
	 * 保存商家合同明细数据
	 * @param datas
	 */
	@DataResolver
	public String saveContractDiscountItem(PriceContractDiscountItemEntity temp){
		
		/*if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}*/
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			//删除原先的折扣服务项
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("contractCode", temp.getContractCode());
			condition.put("delFlag", "1");
			condition.put("lastModifier", JAppContext.currentUserName());
			condition.put("lastModifyTime",JAppContext.currentTimestamp());
			priceContractDiscountService.updateDiscountItem(condition);
				
			/*1001-RC00024&1007-12&1002-16&1006-19*/
			List<PriceContractDiscountItemEntity> itemList=new ArrayList<>();
			String[] item=temp.getSubjectId().split("&");
			if(item.length>0){
				for(String s:item){
					PriceContractDiscountItemEntity newItem=new PriceContractDiscountItemEntity();
					if(!StringUtils.isBlank(s)){
						String[] detail=s.split("#");
						String[] bizDetail=s.split("@");
						if(StringUtils.isNotBlank(bizDetail[0]) && StringUtils.isNotBlank(detail[0]) && StringUtils.isNotBlank(detail[1])){
							newItem.setContractCode(temp.getContractCode());
							newItem.setBizTypeCode(bizDetail[0].trim());
							newItem.setSubjectId(detail[0].substring(detail[0].indexOf("@")+1));	
							newItem.setTemplateCode(detail[1].trim());
							newItem.setCreator(userid);
							newItem.setCreateTime(nowdate);
							newItem.setDelFlag("0");
							itemList.add(newItem);
						}							
					}
				}
				int result=priceContractDiscountService.insertDiscountItem(itemList);
				if(result<=0){
					return "保存科目失败";
				}
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData("");
				model.setOldData("");
				model.setOperateDesc("商家合同签约服务");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_contract_item");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(temp.getContractCode());
				model.setUrlName(RecordLogUrlNameEnum.CONTACT_CUSTOMER.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			logger.error("系统错误", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.getMessage());

			return "数据库操作失败";
		}
		
	
	}
	
	
	
	//查询所有的服务
	@Expose
	public String queryAllContractItem(PriceContractInfoEntity parameter){
		List<ContractDetailEntity> contractList=priceContractService.findAllContractItemName(parameter.getContractCode());
		//System.out.println(contractList);
		String result="";
		for(int i=0;i<contractList.size();i++){
			ContractDetailEntity c=contractList.get(i);
			
			if("TRANSPORT_PRODUCT_TYPE".equals(c.getCodeType()) && !"TRANSPORT_FEE".equals(c.getTransportCode())){
				
				result+=c.getTransportCode()+"&"+c.getTemplateId()+",";
			}else{
				result+=c.getSubjectId()+"&"+c.getTemplateId()+",";
			}
		}
		
		return result;
	}
	
	
	//查询所有的服务
	@Expose
	public String queryAllContractDiscountItem(PriceContractInfoEntity parameter){
		List<ContractDetailEntity> contractList=priceContractService.findAllContractDiscountItemName(parameter.getContractCode());
		//System.out.println(contractList);
		String result="";
		for(int i=0;i<contractList.size();i++){
			ContractDetailEntity c=contractList.get(i);

			result+=c.getSubjectId()+"&"+c.getTemplateId()+",";
		}
		
		return result;
	}
	
	/**
	 * 查询出所有的仓储服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryStorage(Page<ContractDetailEntity> page,Map<String,Object> parameter){
		Map<String,Object> aCondition=new HashMap<String,Object>();
		aCondition.put("contractCode", parameter.get("contractId"));
		aCondition.put("bizTypeCode", "STORAGE");
		List<ContractDetailEntity> contractList=priceContractService.findAllContractItem(aCondition);
		
		List<ContractDetailEntity> storageList=new ArrayList<>();
		
		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_wh_contract_subject");

		for(int i=0;i<contractList.size();i++){
			
			ContractDetailEntity storage=contractList.get(i);
			//仓储类型
			storage.setTheLastName(map.get(storage.getSubjectId()));
			//storage.setRemark(storage.getStorageRemark());
			//storage.setSubjectName(storage.getCodeName());
			if(StringUtils.isNotBlank(storage.getStorageRemark())){
				storage.setRemark(storage.getStorageRemark());
			}else{
				storage.setRemark(storage.getGeneralStorageRemark());
			}
						
			storageList.add(storage);
			
		}	
		page.setEntities(storageList);
		page.setEntityCount(storageList.size());
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
		List<ContractDetailEntity> contractList=priceContractService.findAllContractItem(aCondition);
		
		List<ContractDetailEntity> translateList=new ArrayList<>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_ts_contract_subject");

		for(int i=0;i<contractList.size();i++){			
			ContractDetailEntity translate=contractList.get(i);
			//运输类型
			translate.setTheLastName(map.get(translate.getSubjectId()));
			//translate.setSubjectName(translate.getTheLastName());
			translate.setRemark(translate.getTransportRemark());
			translateList.add(translate);
		}
		page.setEntities(translateList);
		page.setEntityCount(translateList.size());
	}
	
	/**
	 * 查询出所有的配送服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryDispatch(Page<ContractDetailEntity> page,Map<String,Object> parameter){
		Map<String,Object> aCondition=new HashMap<String,Object>();
		aCondition.put("contractCode", parameter.get("contractId"));
		aCondition.put("bizTypeCode", "DISPATCH");
		List<ContractDetailEntity> contractList=priceContractService.findAllContractItem(aCondition);
		
		List<ContractDetailEntity> dispatchList=new ArrayList<>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_ds_contract_subject");

		for(int i=0;i<contractList.size();i++){		
			ContractDetailEntity dispatch=contractList.get(i);	
			dispatch.setTheLastName(map.get(dispatch.getSubjectId()));
			//dispatch.setSubjectName(dispatch.getDispatchName());	
			dispatch.setRemark(dispatch.getDispatchRemark());
			dispatchList.add(dispatch);			
		}
		page.setEntities(dispatchList);
		page.setEntityCount(dispatchList.size());
	}
	
	/**
	 * 查询出所有的折扣
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryDiscount(Page<ContractDetailEntity> page,Map<String,Object> parameter){
		Map<String,Object> aCondition=new HashMap<String,Object>();
		aCondition.put("contractCode", parameter.get("contractId"));
		aCondition.put("bizTypeCode", "DISPATCH");
		List<ContractDetailEntity> contractList=priceContractService.findAllContractDiscountItem(aCondition);
		
		List<ContractDetailEntity> dispatchList=new ArrayList<>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("subject_discount_receive");

		for(int i=0;i<contractList.size();i++){		
			ContractDetailEntity dispatch=contractList.get(i);
			dispatch.setSubjectName(map.get(dispatch.getSubjectId()));
			//dispatch.setSubjectName(dispatch.getDispatchName());	
			dispatchList.add(dispatch);			
		}
		page.setEntities(dispatchList);
		page.setEntityCount(dispatchList.size());
	}
	
	/**
	 * 查询所有的仓储服务类型
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryStorageService(Page<SystemCodeEntity> page,Map<String,Object> parameter){

		//List<SystemCodeEntity> systemCodeList= systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		List<SystemCodeEntity> systemCodeList=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_wh_contract_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			systemCodeList.add(code);			
		}  
		page.setEntities(systemCodeList);
	}
	
	
	/**
	 * 获取所有的运输服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryTransportService(Page<SystemCodeEntity> page,Map<String,Object> parameter){

		//List<SystemCodeEntity> systemCodeList= systemCodeService.findEnumList("TRANSPORT_TEMPLATE_TYPE");

		List<SystemCodeEntity> systemCodeList=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_ts_contract_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			systemCodeList.add(code);			
		}  
		page.setEntities(systemCodeList);
	}
	
	
	/**
	 * 获取所有的配送服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryDispatchService(Page<SystemCodeEntity> page,Map<String,Object> parameter){
		//List<SystemCodeEntity> systemCodeList= systemCodeService.findEnumList("DISPATCH_COMPANY");		
		
		List<SystemCodeEntity> systemCodeList=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_ds_contract_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			systemCodeList.add(code);			
		}  
		page.setEntities(systemCodeList);
	}
	
	/**
	 * 获取所有的折扣服务
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryDiscountService(Page<SystemCodeEntity> page,Map<String,Object> parameter){
		//List<SystemCodeEntity> systemCodeList= systemCodeService.findEnumList("DISPATCH_COMPANY");		
		
		List<SystemCodeEntity> systemCodeList=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("subject_discount_receive");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			systemCodeList.add(code);			
		}  
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
	
	@DataProvider
	public List<PriceContractInfoEntity> getBizTypeCode(Map<String, Object> param){
		
		if(param!=null && param.get("createMonth")!=null && param.get("customerId")!=null){
			try {
				ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
				queryVo.setCustomerId(param.get("customerId").toString());
				queryVo.setSettlementTime(param.get("createMonth").toString());
				queryVo.setBizTypeCode("");
				List<ContractDiscountVo> disCountVo=contractDiscountService.querySubject(queryVo);
				if(disCountVo.size()>0){
					List<PriceContractInfoEntity> list=new ArrayList<PriceContractInfoEntity>();
					if(disCountVo.get(0).getSubjectVoList().size()>0){
						PriceContractInfoEntity price=new PriceContractInfoEntity();
						price.setBizTypeName("仓储");
						price.setBizTypeCode("STORAGE");
						list.add(price);
					}			
					if(disCountVo.get(0).getCarrierVoList().size()>0){
						PriceContractInfoEntity price=new PriceContractInfoEntity();
						price.setBizTypeName("配送");
						price.setBizTypeCode("DISPATCH");
						list.add(price);
					}
					return list;
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.info("合同在线未查询到折扣信息"+e.getMessage());
				List<PriceContractInfoEntity> list = priceContractService.queryByCustomerId(param.get("customerId").toString());
				return list;
			}		
		}
		return null;
	}
	
	@DataProvider
	public List<PriceContractInfoEntity> getSubjectCode(Map<String, String> param){	
		
		List<PriceContractInfoEntity> list=new ArrayList<PriceContractInfoEntity>();
		
		try {
			Map<String, String> feeTypeMap=bmsGroupSubjectService.getSubject("receive_wh_base_quo_subject");
			if (null != param && param.get("createMonth")!=null && param.get("customerId")!=null) {
				String bizTypeCode=param.get("bizTypeCode");
				ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
				queryVo.setCustomerId(param.get("customerId").toString());
				queryVo.setSettlementTime(param.get("createMonth").toString());
				queryVo.setBizTypeCode("");
				List<ContractDiscountVo> disCountVo=contractDiscountService.querySubject(queryVo);
				ContractDiscountVo vo=disCountVo.get(0);
				if(disCountVo.size()>0){					
					if("STORAGE".equals(bizTypeCode)){
						if(vo.getSubjectVoList().size()>0){
							for(SubjectInfoVo s:vo.getSubjectVoList()){
								PriceContractInfoEntity price=new PriceContractInfoEntity();
								price.setBizTypeName("仓储");
								price.setBizTypeCode("STORAGE");
								price.setSubjectId(s.getSubjectId());
								price.setCarrierName(feeTypeMap.get(s.getSubjectId()));
								price.setDiscountType(s.getDiscountType());			   
								price.setCustomerType("contract");
								list.add(price);
							}			
						}
					}else if("DISPATCH".equals(bizTypeCode)){
						if(vo.getCarrierVoList().size()>0){
							for(CarrierInfoVo s:vo.getCarrierVoList()){
								PriceContractInfoEntity price=new PriceContractInfoEntity();
								price.setBizTypeName("配送");
								price.setBizTypeCode("DISPATCH");
								price.setCarrierId(s.getCarrierId());
								SystemCodeEntity entity=(SystemCodeEntity) getDispatchMap().get(s.getCarrierId());
								price.setSubjectId(entity.getCode());
								price.setCarrierName(entity.getCodeName());
								price.setDiscountType(s.getDiscountType());
								price.setCustomerType("contract");
								list.add(price);
							}
						}				
					}			
				}			
				if(list.size()>0){
					return list;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("合同在线未查询到折扣信息"+e.getMessage());

			list = priceContractService.queryByCustomerIdAndBizType(param);
			return list;
		}
		return null;
	}
	
	public Map<String,Object> getDispatchMap(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("typeCode", "DISPATCH_COMPANY");
		List<SystemCodeEntity> scList = systemCodeService.queryCodeList(map);
		map.clear();
		for(SystemCodeEntity s:scList){
			map.put(s.getExtattr1(), s);
		}
		return map;
	}
	
}
