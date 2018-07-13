package com.jiuyescm.bms.fees.bill.out.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEncapEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;

/**
 * 应付账单service接口
 * @author yangshuaishuai
 *
 */
public interface IFeesPayBillService {

	//分页查询    public PageInfo<FeesPayBillEntity> query(Map<String, Object> condition, int pageNo, int pageSize);

    public FeesPayBillEntity queryBillInfo(Map<String, Object> condition);
    
    //查询运输费用 费用科目汇总信息
  	public List<FeesBillWareHouseEntity> queryGroupTransportAmount(String billNo);
  	//查询配送费用 物流商汇总信息
  	public List<FeesBillWareHouseEntity> queryGroupDispatchAmount(String billNo);
  	//查询异常 费用 客诉原因 汇总信息
  	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmount(String billNo);
  	
  	//运输费明细 分页
  	public PageInfo<FeesPayTransportEntity> queryTransportDetailGroupPage(
  			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
  	//配送费明细 分页
  	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailGroupPage(
  			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
  	//异常费明细 分页
  	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailGroupPage(
  			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
  	
  	//自定义-查询运输费用信息
  	public PageInfo<FeesPayTransportEntity> queryTransportInfoPage(Map<String, Object> param,int pageNo,int pageSize);
  	//自定义-查询配送费用信息
  	public PageInfo<FeesPayDispatchEntity> queryDispatchInfoPage(Map<String, Object> param,int pageNo,int pageSize);
  	//自定义-查询异常费用信息
  	public PageInfo<FeesPayAbnormalEntity> queryAbnormalInfoPage(Map<String, Object> param,int pageNo,int pageSize);
  	
	//生成账单 默认全部 （宅配、异常）
	public void generPayBill(FeesPayBillEntity entity) throws Exception ;
	//生成账单 自定义（宅配、异常）
	public void customPayBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception;
		
	//生成账单 默认全部 （运输）
	public void generTransportPayBill(FeesPayBillEntity entity) throws Exception ;
	//生成账单 自定义（运输）
	public void customTransportPayBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception;
	
    //核销账单
    public void confirmFeesBill(FeesPayBillEntity entity) throws Exception;

    //删除账单
	public void deleteFeesBill(FeesPayBillEntity entity) throws Exception;
	
	//获取物流商 最后一次生成账单 时间
	public List<FeesPayBillEntity> getlastBillTime(Map<String, String> maps);
	
	//获取宅配商 最后一次生成账单 时间
	public List<FeesPayBillEntity> getLastBillTimeDelivery(Map<String, Object> maps);
	
	//导出-根据账单查询运输明细
	public PageInfo<FeesPayTransportEntity> queryTransportDetailByBillNo(String billno,int pageNo,int pageSize);
	//导出-根据账单查询配送明细
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNo(String billno,int pageNo,int pageSize);
	//导出-根据账单查询异常明细
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailByBillNo(String billno,int pageNo,int pageSize);
	
	//新增账单操作
	public void addBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception;
	
	//剔除账单操作
	public void delBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception;

	public List<FeesBillWareHouseEntity> queryGroupDispatchAmountByDeliver(
			String billno, String deliverid);

	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountByDeliver(
			String billNo, String deliverid);

	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize);

	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize);

	public List<FeesBillWareHouseEntity> queryGroupDispatchAmountSelect(
			String billno, List<String> deliverIdList);

	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountSelect(
			String billno, List<String> deliverIdList);

	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize);

	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize);

	public List<FeesBillWareHouseEntity> queryAbnormalGroupAmount(String billNo);

	public PageInfo<FeesPayTransportEntity> queryAbnormalByBillNo(
			String billno, int pageNo, int pageSize);


}
