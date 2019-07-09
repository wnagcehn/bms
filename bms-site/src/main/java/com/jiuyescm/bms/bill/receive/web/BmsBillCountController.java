package com.jiuyescm.bms.bill.receive.web;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeTypeService;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInvoceInfoEntity;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInfoService;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInvoceInfoService;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCountEntityVo;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCustomerCountEntityVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 统计账单数据
 * @author zhaofeng
 *
 */
@Controller("bmsBillCountController")
public class BmsBillCountController {
	
    private static final Logger logger = LoggerFactory.getLogger(BmsBillCountController.class.getName());
	
	@Resource
	private IBmsBillInfoService bmsBillInfoService;
	
	@Resource
	private ISystemCodeTypeService systemCodeTypeService;
	
	@Resource
	private IBmsBillInvoceInfoService bmsBillInvoceInfoService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	/**
	 * 账单统计
	 *
	 */
	@DataProvider
	public void queryBillCountPage(Page<BmsBillCountEntityVo> page,Map<String,Object> parameter){
		//查询出所有的统计
		PageInfo<BmsBillCountEntityVo> pageInfo =bmsBillInfoService.countBill(parameter,page.getPageNo(), page.getPageSize());
		
		//查询出系统设置的超市时间进行判断
		SystemCodeTypeEntity systemEntity=systemCodeTypeService.findByTypeCode("BILL_OUTTIME");
		
		if (pageInfo != null) {
			for(int i=0;i<pageInfo.getList().size();i++){
				BmsBillCountEntityVo entityVo=pageInfo.getList().get(i);
				entityVo.setOutTimeReturnMoney(0d);
				entityVo.setOutTimeUnReturnMoney(0d);
				//获取月份
				Date date=entityVo.getCountDate();
				Map<String, Object> condition=new HashMap<String, Object>();
				condition.put("createTime", date);
				List<BmsBillInfoEntity> bmsInfoList=bmsBillInfoService.queryBmsBill(condition);
				for(int j=0;j<bmsInfoList.size();j++){
					BmsBillInfoEntity bmsBillInfoEntity=bmsInfoList.get(j);				
					condition.clear();
					condition.put("billNo", bmsBillInfoEntity.getBillNo());
					List<BmsBillInvoceInfoEntity> invoceList=bmsBillInvoceInfoService.queryInvoce(condition);
					//开始判断是否是超时
					if("INVOICED".equals(bmsBillInfoEntity.getStatus())){
						//未回款的(当前时间-开票时间 有没有大于设置的超时时间)
						for(int m=0;m<invoceList.size();m++){
							BmsBillInvoceInfoEntity invoceInfoEntity=invoceList.get(m);
							int days=(int) (JAppContext.currentTimestamp().getTime()-invoceInfoEntity.getInvoceTime().getTime())/86400000;
							if(systemEntity!=null){
								int outTime=Integer.parseInt(systemEntity.getTypeDesc());
								if(days>outTime){
									entityVo.setOutTimeUnReturnMoney(entityVo.getOutTimeUnReturnMoney()+1);
									entityVo.setUnReturnMoney(entityVo.getUnReturnMoney()-1);
									break;
								}
							}
						}
					}
					if("RECEIPTED".equals(bmsBillInfoEntity.getStatus())){
						//回款的（收款时间-开票时间）
						for(int m=0;m<invoceList.size();m++){
							BmsBillInvoceInfoEntity invoceInfoEntity=invoceList.get(m);
							int days=(int) (invoceInfoEntity.getReceiptTime().getTime()-invoceInfoEntity.getInvoceTime().getTime())/86400000;
							if(systemEntity!=null){
								int outTime=Integer.parseInt(systemEntity.getTypeDesc());
								if(days>outTime){
									entityVo.setOutTimeUnReturnMoney(entityVo.getOutTimeReturnMoney()+1);
									entityVo.setUnReturnMoney(entityVo.getIsReturnMoney()-1);
									break;
								}
							}
						}
					}
					
				}
				
			}
			
			
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 查询超时时间设置
	 * 
	 */
	@Expose
	public String queryOutTime(){
		SystemCodeTypeEntity entity=systemCodeTypeService.findByTypeCode("BILL_OUTTIME");

		return entity.getTypeDesc();
	}
	
	/**
	 * 更新超时时间
	 * @return
	 */
	@DataResolver
	public String updateOutTime(SystemCodeTypeEntity entity){
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("typeCode", "BILL_OUTTIME");
		condition.put("typeDesc", entity.getTypeDesc());
		try {
			int result= systemCodeTypeService.updateByParam(condition);
			if(result<=0){
				return "数据库操作失败";
			}else{
				return "保存成功";
			}
		} catch (Exception e) {
			logger.error("更新异常：", e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillCountController");
			bmsErrorLogInfoEntity.setMethodName("updateOutTime");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);		
		}
		return "数据库操作失败";
	}
	
	

	/**
	 * 账单统计
	 *
	 */
	@DataProvider
	public void queryBillCustomerCountPage(Page<BmsBillCustomerCountEntityVo> page,Map<String,Object> parameter){
		Map<String, Object> map=new HashMap<String, Object>();
		List<BmsBillCustomerCountEntityVo> countEntityList=new ArrayList<BmsBillCustomerCountEntityVo>();
		//查询出所有的账单
		List<BmsBillInfoEntity> billInfoList=bmsBillInfoService.queryBmsBill(parameter);
		for(int i=0;i<billInfoList.size();i++){
			BmsBillInfoEntity billInfoEntity=billInfoList.get(i);
			BmsBillCustomerCountEntityVo entityVo=new BmsBillCustomerCountEntityVo();		
			if(!map.containsKey(billInfoEntity.getCustomerId())){
				//不存在时
				entityVo=new BmsBillCustomerCountEntityVo();
				entityVo.setTotalMoney(billInfoEntity.getTotalAmount());
				
			}else{
				//存在时
				entityVo=(BmsBillCustomerCountEntityVo) map.get(billInfoEntity.getCustomerId());
				entityVo.setTotalMoney(entityVo.getTotalMoney()+billInfoEntity.getTotalAmount());
			}
			
			entityVo.setJieSuanPerson(billInfoEntity.getCreator());
			
			if(equals(billInfoEntity.getCreateTime().toString().contains("-01-"))){
				entityVo.setJanuaryMoney((entityVo.getJanuaryMoney()==null?0d:entityVo.getJanuaryMoney())+billInfoEntity.getTotalAmount());
			}
			if(equals(billInfoEntity.getCreateTime().toString().contains("-02-"))){
				entityVo.setFebruaryMoney((entityVo.getFebruaryMoney()==null?0d:entityVo.getFebruaryMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-03-")){
				entityVo.setMarchMoney((entityVo.getMarchMoney()==null?0d:entityVo.getMarchMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-04-")){
				entityVo.setAprilMoney((entityVo.getAprilMoney()==null?0d:entityVo.getAprilMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-05-")){
				entityVo.setMayMoney((entityVo.getMayMoney()==null?0d:entityVo.getMayMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-06-")){
				entityVo.setJuneMoney((entityVo.getJuneMoney()==null?0d:entityVo.getJuneMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-07-")){
				entityVo.setJulyMoney((entityVo.getJulyMoney()==null?0d:entityVo.getJulyMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-08-")){
				entityVo.setAugustMoney((entityVo.getAugustMoney()==null?0d:entityVo.getAugustMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-09-")){
				entityVo.setSeptemberMoney((entityVo.getSeptemberMoney()==null?0d:entityVo.getSeptemberMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-10-")){
				entityVo.setOctoberMoney((entityVo.getOctoberMoney()==null?0d:entityVo.getOctoberMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-11-")){
				entityVo.setNovemberMoney((entityVo.getNovemberMoney()==null?0d:entityVo.getNovemberMoney())+billInfoEntity.getTotalAmount());
			}
			if(billInfoEntity.getCreateTime().toString().contains("-12-")){
				entityVo.setDecemberMoney((entityVo.getDecemberMoney()==null?0d:entityVo.getDecemberMoney())+billInfoEntity.getTotalAmount());
			}
			
			entityVo.setCustomerId(billInfoEntity.getCustomerId());
			entityVo.setCustomerName(billInfoEntity.getCustomerName());
			entityVo.setJieSuanPerson(billInfoEntity.getCreator());
			
			//根据商家id和年份去查询（//未确认（单）//已确认未开票（元）//已确认无需发票未回款（元））
			Map<String, Object> cMap=new HashMap<>();
			cMap.put("customerId", billInfoEntity.getCustomerId());
			if(parameter.get("year")==null){
				Calendar now = Calendar.getInstance(); 
				cMap.put("year", now.get(Calendar.YEAR));
			}else{
				cMap.put("year", parameter.get("year"));
			}
			
			BmsBillCustomerCountEntityVo customerCountEntityVo=bmsBillInfoService.queryCustomerVo(cMap);
			if(customerCountEntityVo!=null){
				//未确认
				entityVo.setNoConfirm(customerCountEntityVo.getNoConfirm());
				//已确认未开票（元）
				entityVo.setConfirmUnInvoiced(customerCountEntityVo.getConfirmUnInvoiced());
				//已确认无需发票未回款（元）
				entityVo.setConfirmUnNeedInvoiced(customerCountEntityVo.getConfirmUnNeedInvoiced());
			}else{
				//未确认
				entityVo.setNoConfirm(0);
				//已确认未开票（元）
				entityVo.setConfirmUnInvoiced(0d);
				//已确认无需发票未回款（元）
				entityVo.setConfirmUnNeedInvoiced(0d);
			}
			map.put(billInfoEntity.getCustomerId(), entityVo);	
		}
		
		for (String key : map.keySet()) {
			countEntityList.add((BmsBillCustomerCountEntityVo)map.get(key));
		 }	
		page.setEntities(countEntityList);
		page.setEntityCount(countEntityList.size());
	}
	
	
	
	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	@DataProvider
	public Map<String, String> getYear(){
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (int i=2025;i>1998;i--) {
			map.put(i+"", i+"");
		}
		return map;
	}
	
}
