package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.general.entity.ReportBillImportDetailEntity;
import com.jiuyescm.bms.general.service.IReportBillImportService;
import com.jiuyescm.cfm.common.JAppContext;
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

	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ReportBillImportDetailJob start.");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		Map<String,Object> map=new HashMap<String,Object>();
		Timestamp time=reportBillImportService.getTime();
		map.put("time", time);		
		//1.查出最早时间成功的导入账单
		BillReceiveMasterEntity bill=reportBillImportService.queryBill(map);
		if(bill==null){
			 XxlJobLogger.log("未查询到需要统计的账单");
			 return ReturnT.SUCCESS;
		}
		String billNo=bill.getBillNo();
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
		List<ReportBillImportDetailEntity> detailList=new ArrayList<ReportBillImportDetailEntity>();
		for(String warehouseCode:wareList){
			map.put("warehouseCode", warehouseCode);
			ReportBillImportDetailEntity detail=new ReportBillImportDetailEntity();
			//托数
			map.put("subjectCode", "wh_product_storage");
			map.put("chargeUnit", "PALLETS");
			//常温托数
			map.put("tempretureType", "CW");
			num=reportBillImportService.queryStorageNum(map);
			detail.setPalletCwNum(num);
			//恒温托数
			map.put("tempretureType", "HW");
			num=reportBillImportService.queryStorageNum(map);
			detail.setPalletHwNum(num);
			//冷冻托数
			map.put("tempretureType", "LD");
			num=reportBillImportService.queryStorageNum(map);
			detail.setPalletLdNum(num);
			//冷藏托数
			map.put("tempretureType", "LC");
			num=reportBillImportService.queryStorageNum(map);
			detail.setPalletLcNum(num);
			//出库托盘数
			map.clear();
			map.put("billNo", billNo);
			map.put("warehouseCode", warehouseCode);
			map.put("subjectCode", "outstock_pallet_vm");
			num=reportBillImportService.queryStorageNum(map);
			detail.setPalletOutNum(num);
			//转仓托盘数(先过掉)
			//仓租常温金额
			map.put("subjectCode", "wh_rent");
			map.put("tempretureType", "CW");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setRentCwAmount(money);
			//仓租恒温金额
			map.put("tempretureType", "HW");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setRentHwAmount(money);
			//仓租冷冻金额
			map.put("tempretureType", "LD");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setRentLdAmount(money);
			//仓租冷藏金额
			map.put("tempretureType", "LC");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setRentLcAmount(money);
			//操作费金额
			map.clear();
			map.put("billNo", billNo);
			map.put("warehouseCode", warehouseCode);
			map.put("subjectCode", "wh_b2c_work");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setOperateAmount(money);
			//耗材费金额
			map.put("subjectCode", "wh_material_use");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setMaterialAmount(money);
			//仓储增值费
			money=reportBillImportService.queryStorageAdd(map);
			detail.setStAddAmount(money);
			//仓储理赔费
			map.put("subjectCode", "wh_abnormal_pay");
			money=reportBillImportService.queryStorageAmount(map);
			detail.setMaterialAmount(money);
			//tb出库箱数
			map.put("subjectCode", "wh_b2b_work");
			num=reportBillImportService.queryStorageTbBox(map);
			detail.setStTboutNum(num);
			//九曳配送金额 、九曳配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000016");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeJyAmount(dispatch.get("amount"));
			BigDecimal v=dispatch.get("count");

			detail.setDeJyOrders(v);
			//顺丰配送金额 、顺丰配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000015");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeSfAmount(dispatch.get("amount"));
			detail.setDeSfOrders(dispatch.get("count"));
			//圆通配送金额 、圆通配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000019");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeYtoAmount(dispatch.get("amount"));
			detail.setDeYtoOrders(dispatch.get("count"));
			//中通配送金额 、中通配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000018");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeZtoAmount(dispatch.get("amount"));
			detail.setDeZtoOrders(dispatch.get("count"));
			//优速配送金额 、优速配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000020");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeUcAmount(dispatch.get("amount"));
			detail.setDeUcOrders(dispatch.get("count"));
			//德邦配送金额 、德邦配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000021");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeDbangAmount(dispatch.get("amount"));
			detail.setDeDbangOrders(dispatch.get("count"));
			//跨越配送金额 、跨越配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000030");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeKyeAmount(dispatch.get("amount"));
			detail.setDeKyeOrders(dispatch.get("count"));
			//韵达配送金额 、韵达配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000031");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeYundaAmount(dispatch.get("amount"));
			detail.setDeYundaOrders(dispatch.get("count"));
			//百世汇通配送金额 、百世汇通配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000017");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeHtkyAmount(dispatch.get("amount"));
			detail.setDeHtkyOrders(dispatch.get("count"));
			//ems配送金额、ems配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000032");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeEmsAmount(dispatch.get("amount"));
			detail.setDeEmsOrders(dispatch.get("count"));
			//承诺达配送金额、承诺达配送单量
			map.put("subjectCode", "de_delivery_amount");
			map.put("carrierid", "1500000033");
			dispatch=reportBillImportService.queryDispatch(map);
			detail.setDeOptAmount(dispatch.get("amount"));
			detail.setDeOptOrders(dispatch.get("count"));
			
			detail.setBillNo(billNo);
			detail.setWriteTime(JAppContext.currentTimestamp());
			detail.setLastModifyTime(JAppContext.currentTimestamp());
			detail.setWarehouseCode(warehouseCode);
			detailList.add(detail);
		}
		
		//4.list插入明细表
		reportBillImportService.saveList(detailList);
		
		//更新增量时间
		map.clear();
		map.put("lastTime", bill.getCreateTime());
		reportBillImportService.updateEtlTime(map);
		
		return ReturnT.SUCCESS;		
	}
}
