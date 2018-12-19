package com.jiuyescm.bms.trial.storage.web;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.dorado.annotation.DataResolver;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.common.entity.CalculateVo;

@Controller("tryStorageDroolsController")
public class TryStorageDroolsController {
	
	private static final String msg = "无匹配价格";
	
	@DataResolver
	public  @ResponseBody  Object  tryOrderOperate(BizOutstockMasterEntity data){// 订单操作费   业务数据是 商品出库单 （主表）
		
		String subjectId = "wh_b2c_work";//订单操作费
				
		ReturnData result = new ReturnData();
		
		CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
				
		if(parent.getPrice()==null||!parent.getSuccess()){
			result.setCode("fail");
			if(parent.getPrice()==null&&parent.getSuccess()){
				result.setData(msg);
			}else{
				result.setData(parent.getMsg());
			}
			
		}else{
			result.setCode("SUCCESS");
			parent.setPrice(new BigDecimal(parent.getPrice().doubleValue()*data.getBillNum()).setScale(2, BigDecimal.ROUND_HALF_UP));
			result.setData(parent.getPrice().toString());
		}
		
		return result;
		
	}
	
	@DataResolver
	public @ResponseBody  Object  tryRepertory(BizProductStorageEntity data){ 
		
		String subjectId = "wh_product_storage";//存储费 按件
		
		CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
		
		return commonData(parent);		
		
	}
	
	@DataResolver
	public @ResponseBody  Object  tryRepertoryPallet(BizProductStorageEntity data){ 
		
		String subjectId = "wh_product_pallet_storage";//存储费 按托
		
		BizProductPalletStorageEntity palletData = new BizProductPalletStorageEntity();
		palletData.setPalletNum(data.getAqty());
		palletData.setTemperatureTypeCode(data.getTemperature());
		
        CalculateVo parent = commonExcute(subjectId,data.getContractCode(),palletData);
		
        return commonData(parent);
				
	}
	
	@DataResolver
	public @ResponseBody  Object  tryInStorage(BizInStockMasterEntity data){
		
		String subjectId = "wh_instock_service";//入仓费
		
        CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
		
        return commonData(parent);
				
	}
	
	@DataResolver
	public @ResponseBody  Object  tryReturnSales(BizInStockMasterEntity data){
		
		String subjectId = "wh_return_storage";//退货费
		
		CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
			
		return commonData(parent);			
		
	}
	
	@DataResolver
	public @ResponseBody  Object  tryReturnStock(BizOutstockMasterEntity data){
		
		String subjectId = "wh_product_out";//退仓费
		
		CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
		
		return commonData(parent);	
		
	}
	
	@DataResolver
	public @ResponseBody  Object  tryBtbOut(BizOutstockMasterEntity data){
		
		String subjectId = "wh_b2b_work";//B2B出库费
		
        CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
		
		return commonData(parent);	
		
	}
	
	@DataResolver
	public @ResponseBody  Object  tryMaterialStock(BizPackStorageEntity data){
		
		String subjectId = "wh_material_storage";//耗材存储费
		
		CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
			
		return commonData(parent);	
	}
	
	@DataResolver
	public @ResponseBody  Object tryMaterial(BizOutstockPackmaterialEntity data){
		
		String subjectId = "wh_material_use";//耗材使用费
		
		CalculateVo parent = commonExcute(subjectId,data.getContractCode(),data);
		
		return commonData(parent);	
		
	}
	
	
	CalculateVo  commonExcute(String subjectId,String contractCode,Object obj){
		
        CalculateVo parent = new CalculateVo();
		
		parent.setBizTypeCode("STORAGE");//业务类型
		
		parent.setObj(obj);//业务数据
		
		parent.setContractCode(contractCode);//合同编号
		
		parent.setSubjectId(subjectId);//费用科目
		
		//parent = calculateService.calculate(parent);
		
		return parent;
		
	}
	
	ReturnData commonData(CalculateVo parent){
		
		ReturnData result = new ReturnData();
		
		if(parent.getPrice()==null||!parent.getSuccess()){
			result.setCode("fail");
			if(parent.getPrice()==null&&parent.getSuccess()){
				result.setData(msg);
			}else{
				result.setData(parent.getMsg());
			}
		}else{
			result.setCode("SUCCESS");
			result.setData(parent.getPrice().toString());
		}
		
		return result;
		
	}
	
	
	
	
	
	
	
	


}