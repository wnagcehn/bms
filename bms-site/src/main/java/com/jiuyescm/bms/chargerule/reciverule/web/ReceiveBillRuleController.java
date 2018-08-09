
package com.jiuyescm.bms.chargerule.reciverule.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.entity.PubRuleCustomerReceivableEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IPubRuleCustomerReceivableService;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

@Controller("receiveBillRuleController")
public class ReceiveBillRuleController{
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Resource
	private IReceiveRuleService receiveRuleService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IPubRuleCustomerReceivableService ruleCustomerReceivableService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	/**
	 * 查询所有的计费规则信息
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<BillRuleReceiveEntity> page,Map<String,Object> parameter){
		if(parameter==null){
			parameter=new HashMap<String,Object>();		
			parameter.put("isDefault", "");
		}	
		if(parameter!=null && parameter.get("isDefault")==null){
			parameter.put("isDefault", "");
		}
		if(parameter!=null && "ALL".equals(parameter.get("bizTypeCode"))){
			parameter.put("bizTypeCode", "");
		}
		
		
		PageInfo<BillRuleReceiveEntity> tmpPageInfo =receiveRuleService.queryAllRule(parameter,page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	
	}
	
	
	/**
	 * 保存计费规则数据
	 * @param datas
	 */
	@DataResolver
	public String saveRule(Collection<BillRuleReceiveEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BillRuleReceiveEntity temp:datas){
				//对操作类型进行判断
				//此为新增计费规则数据
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					//判断该计费规则是否已存在
					/*Map<String,Object> condition=new HashMap<String, Object>();
					condition.put("bizTypeCode", temp.getBizTypeCode());
					condition.put("subjectId", temp.getSubjectName());
					
					PageInfo<BillRuleReceiveEntity> tmpPageInfo =receiveRuleService.queryAll(condition,0,Integer.MAX_VALUE);

					if(tmpPageInfo!=null && tmpPageInfo.getList()!=null && tmpPageInfo.getList().size()>0){
						return "FALSE";
					}*/
					
//					String templateNo =sequenceService.getBillNoOne(BillRuleReceiveEntity.class.getName(), "YGR", "00000");
//					temp.setQuotationNo(templateNo);
					if(temp.getSubjectName()!=null){
						temp.setSubjectId(temp.getSubjectName());
					}
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					receiveRuleService.createRule(temp);
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					//此为修改商家合同信息
					//根据名字查出对应的id
					Map<String, Object> param=new HashMap<>();
					if(StringUtils.isNotBlank(temp.getSubjectId())){
						param.put("codeName", temp.getSubjectId());
						List<SystemCodeEntity> list=systemCodeService.queryCodeList(param);
						if(list!=null && list.size()>0){
							temp.setSubjectId(list.get(0).getCode());
						}	
					}	
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					receiveRuleService.updateRule(temp);
				}
				
			}
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "数据库操作失败";
		}
		
	
	}
	
	
	/**
	 * 删除配送报价模板
	 */
	@DataResolver
	public String removeRule(BillRuleReceiveEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setDelFlag("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			receiveRuleService.removeRule(p.getId().toString());
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			
			return "数据库操作失败";
		}
			
	}
	
	
	/**
	 * 获取对应的不同的业务对应的服务
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<SystemCodeEntity> queryAllService(Map<String, Object> parameter) {
		
		//获取此时的业务类型
		if(parameter==null || parameter.get("typeCode")==null){
			return null;
		}
		String subjectId=parameter.get("typeCode").toString();
		//仓储
		if("STORAGE".equals(subjectId)){
			List<SystemCodeEntity> systemCodeList=new ArrayList<>();
			List<SystemCodeEntity> systemCodeList1= systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
			//List<SystemCodeEntity> systemCodeList2= systemCodeService.findEnumList("STORAGE_ADD_VALUE");

			for(int i=0;i<systemCodeList1.size();i++){
				SystemCodeEntity a=systemCodeList1.get(i);
				if(!("1008").equals(a.getCode())){
					systemCodeList.add(a);
				}	
			}
			//for(int i=0;i<systemCodeList2.size();i++){
			//	SystemCodeEntity a=systemCodeList2.get(i);
			//	systemCodeList.add(a);
			//}
			return systemCodeList;
			
		}else if("TRANSPORT".equals(subjectId)){
			//运输
			List<SystemCodeEntity> systemCodeList=new ArrayList<>();
			List<SystemCodeEntity> systemCodeList1= systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
			List<SystemCodeEntity> systemCodeList2= systemCodeService.findEnumList("ts_base_subject");
			for(int i=0;i<systemCodeList1.size();i++){
				SystemCodeEntity a=systemCodeList1.get(i);
				systemCodeList.add(a);
			}
			for(int i=0;i<systemCodeList2.size();i++){
				SystemCodeEntity a=systemCodeList2.get(i);
				systemCodeList.add(a);
			}
			return systemCodeList;
			
		}else if("DISPATCH".equals(subjectId)){
			//配送
			List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_COMPANY");
			
			return systemCodeList;
		}else if("ALL".equals(subjectId)){
			List<SystemCodeEntity> allList=new ArrayList<>();
			//仓储的
			List<SystemCodeEntity> systemCodeList1= systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
			//List<SystemCodeEntity> systemCodeList2= systemCodeService.findEnumList("STORAGE_ADD_VALUE");
			//运输的
			List<SystemCodeEntity> systemCodeList3= systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
			List<SystemCodeEntity> systemCodeList4= systemCodeService.findEnumList("ts_base_subject");
			//配送的
			List<SystemCodeEntity> systemCodeList5 = systemCodeService.findEnumList("DISPATCH_COMPANY");
			
			for(int i=0;i<systemCodeList1.size();i++){			
				SystemCodeEntity a=systemCodeList1.get(i);
				if(!("1008").equals(a.getCode())){
					allList.add(a);
				}					
			}
			//for(int i=0;i<systemCodeList2.size();i++){
			//	SystemCodeEntity a=systemCodeList2.get(i);
			//	allList.add(a);
			//}
			for(int i=0;i<systemCodeList3.size();i++){
				SystemCodeEntity a=systemCodeList3.get(i);
				allList.add(a);
			}
			for(int i=0;i<systemCodeList4.size();i++){
				SystemCodeEntity a=systemCodeList4.get(i);
				allList.add(a);
			}
			for(int i=0;i<systemCodeList5.size();i++){
				SystemCodeEntity a=systemCodeList5.get(i);
				allList.add(a);
			}
			return allList;
		}
		return null;
	}
	
	
	/**
	 * 查询规则商家关系映射
	 */
	@DataProvider
	public void queryRuleCustomerReceivable(Page<PubRuleCustomerReceivableEntity> page,Map<String,Object> parameter){
		PageInfo<PubRuleCustomerReceivableEntity> pageInfo =ruleCustomerReceivableService.query(parameter,page.getPageNo(),page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 维护规则商家关系
	 * @param entity
	 * @throws Exception
	 */
	@DataResolver
	public String saveRuleCustomerReceivable(PubRuleCustomerReceivableEntity entity) throws Exception{
		if (null == entity) {
			throw new Exception("规则商家关系数据为空！");
		}
		//是否已存在映射关系，不存在则新增
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("customerid", entity.getCustomerid());
		param.put("subjectId", entity.getSubjectId());
		PubRuleCustomerReceivableEntity existEntity = ruleCustomerReceivableService.query(param);
		if (null == existEntity) {
			String operatorNmae = JAppContext.currentUserName();
			Timestamp operatorTime = JAppContext.currentTimestamp();
			entity.setCreator(operatorNmae);
			entity.setCreateTime(operatorTime);
			entity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_0);//正常
			int saveNums = ruleCustomerReceivableService.save(entity);
			if (saveNums <= 0) {
				throw new Exception("维护商家操作失败！");
			}
		}else {
			return "当前费用科目的规则下该商家已存在！";
		}
		
		return "商家维护成功！";
	}
	
	/**
	 * 删除规则商家关系
	 * @param parameter
	 * @throws Exception
	 */
	@DataResolver
	public void delRuleCustomerReceivable(PubRuleCustomerReceivableEntity entity) throws Exception{
		if (null == entity || StringUtils.isBlank(entity.getId()+"")) {
			throw new Exception("请选择要删除的记录！");
		}
		PubRuleCustomerReceivableEntity updateEntity = new PubRuleCustomerReceivableEntity();
		updateEntity.setId(entity.getId());
		updateEntity.setLastModifier(JAppContext.currentUserName());
		updateEntity.setLastModifyTime(JAppContext.currentTimestamp());
		updateEntity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_1);//禁用
		int updateNums = ruleCustomerReceivableService.update(updateEntity);
		if (updateNums <= 0) {
			throw new Exception("删除记录操作失败！");
		}
	}
}