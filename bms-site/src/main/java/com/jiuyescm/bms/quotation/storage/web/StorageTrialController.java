package com.jiuyescm.bms.quotation.storage.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.dorado.annotation.DataResolver;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
//import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.transport.entity.vo.GenericTemplateVo;
import com.jiuyescm.common.tool.JsonPluginsUtil;


@Controller("storageTrialController")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StorageTrialController {
	
	@Resource private IReceiveRuleService receiveRuleService;
	
	//@Resource private IFeesCalcuService feesCalcuService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@DataResolver
	public  @ResponseBody  Object calculate(Map<String,Object> param) throws Exception{
		ReturnData result = new ReturnData();
		
		//传入计算的方法
		CalcuReqVo reqVo= new CalcuReqVo();
		
		String bizJson = param.get("biz").toString();
		
		String subjectId=param.get("subjectId").toString();
	    if("wh_product_storage".equals(subjectId)){
	    	//商品存储费
	 	    BizProductStorageEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizProductStorageEntity.class);
	 	    reqVo.setBizData(bizDataStorage);
	    }else if("wh_b2c_work".equals(subjectId)){
	    	//订单操作费
	    	BizOutstockMasterEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizOutstockMasterEntity.class);
	 	    reqVo.setBizData(bizDataStorage);
	    }else if("wh_instock_service".equals(subjectId)){
	    	//入仓费
	    	BizInStockMasterEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizInStockMasterEntity.class);
	    	reqVo.setBizData(bizDataStorage);
	    }else if("wh_return_storage".equals(subjectId)){
	    	//退货费
	    	BizInStockMasterEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizInStockMasterEntity.class);
	    	reqVo.setBizData(bizDataStorage);
	    }else if("wh_product_out".equals(subjectId)){
	    	//退仓费
	    	BizOutstockMasterEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizOutstockMasterEntity.class);
	    	reqVo.setBizData(bizDataStorage);
	    }else if("wh_b2b_work".equals(subjectId)){
	    	//B2B出库费
	    	BizOutstockMasterEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizOutstockMasterEntity.class);
	    	reqVo.setBizData(bizDataStorage);
	    }else if("wh_material_storage".equals(subjectId)){
	    	//退仓费
	    	BizPackStorageEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizPackStorageEntity.class);
	    	reqVo.setBizData(bizDataStorage);
	    }
	   
		//报价
	    String priceQuatationJson = param.get("template").toString();
		PriceGeneralQuotationEntity priceQuatation=JsonPluginsUtil.jsonToBean(priceQuatationJson, PriceGeneralQuotationEntity.class);
		//通过该商家id和费用科目查询计费规则
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("customerid", priceQuatation.getCustomerId());
		ruleParam.put("subjectId",priceQuatation.getSubjectId());
		BillRuleReceiveEntity ruleEntity=receiveRuleService.queryByCustomerId(ruleParam);
		
		if(ruleEntity==null){
			result.setCode("fail");
			result.setData("未查询到该商家的规则");
			return result;
		}
		
		if("wh_product_storage".equals(subjectId)){
			//判断是商品库存还是商品按托库存
			String feeUnit=priceQuatation.getFeeUnitCode();
			if("PALLETS".equals(feeUnit)){
				BizProductStorageEntity bizDataStorage=(BizProductStorageEntity) reqVo.getBizData();
				BizProductPalletStorageEntity palletData = new BizProductPalletStorageEntity();
				palletData.setPalletNum(bizDataStorage.getAqty());
				palletData.setTemperatureTypeCode(bizDataStorage.getTemperature());
				reqVo.setBizData(palletData);
			}
		}
	
		//判断是否是阶梯报价
		String type=priceQuatation.getPriceType();
		if("PRICE_TYPE_STEP".equals(type)){
			PriceStepQuotationEntity PriceStepQuotationEntity=new PriceStepQuotationEntity();
			if(param.get("priceList")!=null){
				String priceStepQuotationJson = param.get("priceList").toString();
				PriceStepQuotationEntity=JsonPluginsUtil.jsonToBean(priceStepQuotationJson, PriceStepQuotationEntity.class);
			}			
			//阶梯报价
			List<PriceStepQuotationEntity> priceStepQuotationList=new ArrayList<PriceStepQuotationEntity>();
			priceStepQuotationList.add(PriceStepQuotationEntity);
			reqVo.setQuoEntites(priceStepQuotationList);
		}else{
			//常规报价
			List<PriceGeneralQuotationEntity> priceQuotationList=new ArrayList<PriceGeneralQuotationEntity>();
			priceQuotationList.add(priceQuatation);
			reqVo.setQuoEntites(priceQuotationList);
		}
		reqVo.setRuleNo(ruleEntity.getQuotationNo());
		reqVo.setRuleStr(ruleEntity.getRule());
		
		//CalcuResultVo vo=feesCalcuService.FeesCalcuService(reqVo);
		CalcuResultVo vo=new CalcuResultVo();
		if("succ".equals(vo.getSuccess())){
			result.setCode("SUCCESS");
			result.setData(vo.getPrice());
		}	
		else{
			result.setCode("fail");
			result.setData("无匹配价格");
		}	
		return result;
	}
	
	
	@DataResolver
	public  @ResponseBody  Object calculateMe(Map<String,Object> param) throws Exception{
		ReturnData result = new ReturnData();
		
		//业务数据
		String bizJson = param.get("biz").toString();
		BizOutstockPackmaterialEntity bizDataStorage= JsonPluginsUtil.jsonToBean(bizJson, BizOutstockPackmaterialEntity.class);
	
		//模板
	    String priceQuatationJson = param.get("template").toString();
	    GenericTemplateVo priceQuatation=JsonPluginsUtil.jsonToBean(priceQuatationJson, GenericTemplateVo.class);

		//通过该商家id和费用科目查询计费规则
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("customerid", priceQuatation.getCustomerId());
		ruleParam.put("subjectId","wh_material_use");
		BillRuleReceiveEntity ruleEntity=receiveRuleService.queryByCustomerId(ruleParam);
		
		if(ruleEntity==null){
			result.setCode("fail");
			result.setData("未查询到该商家的规则");
			return result;
		}
		
		
		//阶梯报价
		PriceMaterialQuotationEntity PriceStepQuotationEntity=new PriceMaterialQuotationEntity();
		
		if(param.get("priceList")!=null){
			String priceStepQuotationJson = param.get("priceList").toString();
			PriceStepQuotationEntity=JsonPluginsUtil.jsonToBean(priceStepQuotationJson, PriceMaterialQuotationEntity.class);	
		}
		
		CalcuReqVo reqVo= new CalcuReqVo();
		//阶梯报价
		List<PriceMaterialQuotationEntity> list=new ArrayList<PriceMaterialQuotationEntity>();
		list.add(PriceStepQuotationEntity);
		
		reqVo.setQuoEntites(list);
		reqVo.setBizData(bizDataStorage);
		reqVo.setRuleNo(ruleEntity.getQuotationNo());
		reqVo.setRuleStr(ruleEntity.getRule());
		
		//CalcuResultVo vo=feesCalcuService.FeesCalcuService(reqVo);		
		CalcuResultVo vo=new CalcuResultVo();
		if("succ".equals(vo.getSuccess())){
			result.setCode("SUCCESS");
			result.setData(vo.getPrice());
		}	
		else{
			result.setCode("fail");
			result.setData("无匹配价格");
		}	
		return result;
	}
}
