package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.general.entity.ReportBillImportDetailEntity;
import com.jiuyescm.bms.general.service.IReportBillImportService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 账单报表明细统计
 * @author zhaofeng
 */
@JobHander(value="reportBillImportDetailJob")
@Service
public class ReportBillImportDetailJob extends IJobHandler{
	
	@Autowired private IReportBillImportService reportBillImportService;

	@Autowired private IWarehouseService warehouseService;

	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ReportBillImportDetailJob start.");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
	
		Map<String,Object> map=new HashMap<String,Object>();
		if(params != null && params.length > 0) {
		    try {
		    	map = JobParameterHandler.handler(params);//处理定时任务参数
		    } catch (Exception e) {
	            XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:{0}", e);
	            return ReturnT.FAIL;
	        }
		}
		
		Map<String,String> wareMap=getWareMap();
		
		//1.查出需要统计的导入账单
		List<BillReceiveMasterEntity> billList=reportBillImportService.queryList(map);		
		if(billList.size()<=0){
			 XxlJobLogger.log("未查询到需要统计的账单");
			 return ReturnT.FAIL;
		}
		List<ReportBillImportDetailEntity> detailList=new ArrayList<ReportBillImportDetailEntity>();
		List<BillReceiveMasterEntity> masterList=new ArrayList<BillReceiveMasterEntity>();
		
		for(BillReceiveMasterEntity bill:billList){	
			String billNo=bill.getBillNo();
			try {
				//2.根据账单编号查询所有的仓储费用汇总出所有的仓库
				map.put("billNo", billNo);
				List<String> wareList=reportBillImportService.queryAllWare(map);
				if(wareList.size()==0){
					 XxlJobLogger.log("未查询到需要统计的仓库");
					 return ReturnT.SUCCESS;
				}
				//3.根据账单编号、仓库统计各个费用，生成list
				//金额
				BigDecimal money=new BigDecimal(0);
				//数量
				BigDecimal num=new BigDecimal(0);
				//配送对应得数量金额
				Map<String,BigDecimal> dispatch;
				for(String warehouseCode:wareList){
					map.put("warehouseCode", warehouseCode);
					ReportBillImportDetailEntity detail=new ReportBillImportDetailEntity();
					//托数
					map.put("subjectCode", "wh_product_storage");
					map.put("chargeUnit", "PALLETS");
					//常温托数
					map.put("tempretureType", "CW");
					XxlJobLogger.log("常温托数统计"+JSONObject.fromObject(map));
					num=reportBillImportService.queryStorageNum(map);
					money=reportBillImportService.queryStorageAmount(map);
					detail.setPalletCwNum(num);
					detail.setPalletCwAmount(money);
					//恒温托数
					map.put("tempretureType", "HW");
					XxlJobLogger.log("恒温托数统计"+JSONObject.fromObject(map));
					num=reportBillImportService.queryStorageNum(map);
					money=reportBillImportService.queryStorageAmount(map);
					detail.setPalletHwNum(num);
					detail.setPalletHwAmount(money);
					//冷冻托数
					map.put("tempretureType", "LD");
					XxlJobLogger.log("冷冻托数统计"+JSONObject.fromObject(map));
					num=reportBillImportService.queryStorageNum(map);
					money=reportBillImportService.queryStorageAmount(map);
					detail.setPalletLdNum(num);
					detail.setPalletLdAmount(money);
					//冷藏托数
					map.put("tempretureType", "LC");
					XxlJobLogger.log("冷藏托数统计"+JSONObject.fromObject(map));
					num=reportBillImportService.queryStorageNum(map);
					money=reportBillImportService.queryStorageAmount(map);
					detail.setPalletLcNum(num);
					detail.setPalletLcAmount(money);
					//出库托盘数
					map.clear();
					map.put("billNo", billNo);
					map.put("warehouseCode", warehouseCode);
					map.put("subjectCode", "outstock_pallet_vm");
					XxlJobLogger.log("出库托盘数统计"+JSONObject.fromObject(map));
					num=reportBillImportService.queryStorageNum(map);
					detail.setPalletOutNum(num);
					//转仓托盘数(先过掉)
					//仓租常温金额
					map.put("subjectCode", "wh_rent");
					map.put("tempretureType", "CW");
					XxlJobLogger.log("仓租常温金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setRentCwAmount(money);
					//仓租恒温金额
					map.put("tempretureType", "HW");
					XxlJobLogger.log("仓租恒温金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setRentHwAmount(money);
					//仓租冷冻金额
					map.put("tempretureType", "LD");
					XxlJobLogger.log("仓租冷冻金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setRentLdAmount(money);
					//仓租冷藏金额
					map.put("tempretureType", "LC");
					XxlJobLogger.log("仓租冷藏金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setRentLcAmount(money);
					//操作费金额（包含B2B,B2C）
					map.clear();
					map.put("billNo", billNo);
					map.put("warehouseCode", warehouseCode);
					map.put("subjectCode", "wh_b2c_work");
					XxlJobLogger.log("B2C操作费金额统计"+JSONObject.fromObject(map));
					BigDecimal b2cMoney=reportBillImportService.queryStorageAmount(map);
					map.put("subjectCode", "wh_b2b_work");
					XxlJobLogger.log("B2B操作费金额统计"+JSONObject.fromObject(map));
					BigDecimal b2bMoney=reportBillImportService.queryStorageAmount(map);
					detail.setOperateAmount(b2cMoney.add(b2bMoney));
					//耗材费金额
					map.put("subjectCode", "wh_material_use");
					XxlJobLogger.log("耗材费金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setMaterialAmount(money);
					//入库操作费
					map.put("subjectCode", "wh_instock_work");
					XxlJobLogger.log("入库操作费金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setStInstockWorkAmount(money);
					//入库卸货费
					map.put("subjectCode", "wh_b2c_handwork");
					XxlJobLogger.log("入库卸货费金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setStB2cHandworkAmount(money);
					//出库装车费
					map.put("subjectCode", "wh_b2b_handwork");
					XxlJobLogger.log("出库装车费金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setStB2bHandworkAmount(money);
					
					//仓储增值费
					XxlJobLogger.log("仓储增值费金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAdd(map);
					detail.setStAddAmount(money);
					//仓储理赔费
					map.put("subjectCode", "wh_abnormal_pay");
					XxlJobLogger.log("仓储理赔费金额统计"+JSONObject.fromObject(map));
					money=reportBillImportService.queryStorageAmount(map);
					detail.setStAbnormalAmount(money);
					
					//tb出库箱数
					map.put("subjectCode", "wh_b2b_work");
					XxlJobLogger.log("tb出库箱数统计"+JSONObject.fromObject(map));
					num=reportBillImportService.queryStorageTbBox(map);
					detail.setStTboutNum(num);
					//九曳配送金额 、九曳配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000016");
					XxlJobLogger.log("九曳配送金额 、九曳配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeJyAmount(dispatch.get("amount"));
					BigDecimal v=dispatch.get("count");

					detail.setDeJyOrders(v);
					//顺丰配送金额 、顺丰配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000015");
					XxlJobLogger.log("顺丰配送金额 、顺丰配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeSfAmount(dispatch.get("amount"));
					detail.setDeSfOrders(dispatch.get("count"));
					//圆通配送金额 、圆通配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000019");
					XxlJobLogger.log("圆通配送金额 、圆通配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeYtoAmount(dispatch.get("amount"));
					detail.setDeYtoOrders(dispatch.get("count"));
					//中通配送金额 、中通配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000018");
					XxlJobLogger.log("中通配送金额 、中通配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeZtoAmount(dispatch.get("amount"));
					detail.setDeZtoOrders(dispatch.get("count"));
					//申通配送金额 、申通配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1300020026");
					XxlJobLogger.log("申通配送金额 、申通配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeStoAmount(dispatch.get("amount"));
					detail.setDeStoOrders(dispatch.get("count"));
					//优速配送金额 、优速配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000020");
					XxlJobLogger.log("优速配送金额 、优速配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeUcAmount(dispatch.get("amount"));
					detail.setDeUcOrders(dispatch.get("count"));
					//德邦配送金额 、德邦配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000021");
					XxlJobLogger.log("德邦配送金额 、德邦配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeDbangAmount(dispatch.get("amount"));
					detail.setDeDbangOrders(dispatch.get("count"));
					//跨越配送金额 、跨越配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000030");
					XxlJobLogger.log("跨越配送金额 、跨越配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeKyeAmount(dispatch.get("amount"));
					detail.setDeKyeOrders(dispatch.get("count"));
					//韵达配送金额 、韵达配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000031");
					XxlJobLogger.log("韵达配送金额 、韵达配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeYundaAmount(dispatch.get("amount"));
					detail.setDeYundaOrders(dispatch.get("count"));
					//百世汇通配送金额 、百世汇通配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000017");
					XxlJobLogger.log("百世汇通配送金额 、百世汇通配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeHtkyAmount(dispatch.get("amount"));
					detail.setDeHtkyOrders(dispatch.get("count"));
					//ems配送金额、ems配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000032");
					XxlJobLogger.log("ems配送金额、ems配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeEmsAmount(dispatch.get("amount"));
					detail.setDeEmsOrders(dispatch.get("count"));
					//承诺达配送金额、承诺达配送单量
					map.put("subjectCode", "de_delivery_amount");
					map.put("carrierid", "1500000033");
					XxlJobLogger.log("承诺达配送金额、承诺达配送单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeOptAmount(dispatch.get("amount"));
					detail.setDeOptOrders(dispatch.get("count"));
					
					//宅配理赔金额、宅配理赔单量
					map.clear();
					map.put("billNo", billNo);
					map.put("warehouseCode", warehouseCode);
					map.put("subjectCode", "de_abnormal_pay");
					XxlJobLogger.log("宅配理赔金额、宅配理赔单量统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeAbnormalAmount(dispatch.get("amount"));
					detail.setDeAbnormalOrders(dispatch.get("count"));
					//改地址退件费
					map.put("subjectCode", "de_change");
					XxlJobLogger.log("改地址退件费统计"+JSONObject.fromObject(map));
					dispatch=reportBillImportService.queryDispatch(map);
					detail.setDeChangeAmount(dispatch.get("amount"));
					//仓储收入小计
					XxlJobLogger.log("仓储收入小计"+JSONObject.fromObject(map));
					BigDecimal storageamount=reportBillImportService.queryStorageReceipt(map);
					detail.setStorageReceiptAmount(storageamount);
					//宅配收入小计
					XxlJobLogger.log("宅配收入小计"+JSONObject.fromObject(map));
					BigDecimal dispatchamount=reportBillImportService.queryDispatchReceipt(map);
					detail.setDispatchReceiptAmount(dispatchamount);
					
					detail.setBillNo(billNo);
					detail.setWriteTime(JAppContext.currentTimestamp());
					detail.setLastModifyTime(JAppContext.currentTimestamp());
					detail.setWarehouseCode(warehouseCode);
					detail.setWarehouseName(wareMap.get(warehouseCode));
					detailList.add(detail);
				}
				bill.setIsCalculated("1");
				masterList.add(bill);
			} catch (Exception e) {
				// TODO: handle exception
				 XxlJobLogger.log("处理"+billNo+"异常:{0}",e);
				 return ReturnT.FAIL;
			}	
		}
		
		try {
			//4.list插入明细表
			reportBillImportService.saveList(detailList);
			//5.更新主表
			reportBillImportService.updateBill(masterList);
		} catch (Exception e) {
			// TODO: handle exception
			 XxlJobLogger.log("批量保存异常:{0}",e);
		}
		
		return ReturnT.SUCCESS;		
	}
	
	
	public Map<String,String> getWareMap(){
		//仓库
		Map<String,String> wareMap=new HashMap<String,String>();
		List<WarehouseVo> wareList=warehouseService.queryAllWarehouse();
		for(WarehouseVo vo:wareList){
			wareMap.put(vo.getWarehouseid(), vo.getWarehousename());
		}
		return wareMap;
	}
}
