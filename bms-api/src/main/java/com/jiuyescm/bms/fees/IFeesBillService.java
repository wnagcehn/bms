package com.jiuyescm.bms.fees;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillQueryEntity;
import com.jiuyescm.bms.fees.entity.FeesBillReceiveEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
/**
 * 
 * @author Wuliangfeng 费用Service 层
 *
 */
public interface IFeesBillService {

	//查询账单信息
	public PageInfo<FeesBillEntity> query(FeesBillQueryEntity queryEntity, int pageNo, int pageSize);
	
	public FeesBillEntity queryBillInfo(FeesBillQueryEntity queryEntity);
	
	public void save(FeesBillEntity entity);

	//生成账单
	public void generReceiverBill(FeesBillEntity entity) throws Exception ;

	//生成账单操作
	public void saveAll(FeesBillEntity entity,
			FeesBillReceiveEntity feesBillReceiveEntity) throws Exception;
	
	//新增账单操作
	public void addBill(FeesBillEntity entity,
			FeesBillReceiveEntity feesBillReceiveEntity) throws Exception;
	
	//剔除账单操作
	public void delBill(FeesBillEntity entity,
			FeesBillReceiveEntity feesBillReceiveEntity) throws Exception;

	//查询仓储费用 仓库汇总信息
	public List<FeesBillWareHouseEntity> querywarehouseStorageAmount(String billNo);
	//查询运输费用 费用科目汇总信息
	public List<FeesBillWareHouseEntity> querywarehouseDeliverAmount(String billNo);
	//查询配送费用 物流商汇总信息
	public List<FeesBillWareHouseEntity> querywarehouseDistributionAmount(String billNo);
	//查询异常 费用 仓库 汇总信息
	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(String billNo);
	
	//仓储费明细 分页
	public PageInfo<FeesReceiveStorageEntity> queryStorageDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
	//运输费明细 分页
	public PageInfo<FeesReceiveDeliverEntity> queryDeliverDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
	//配送费明细 分页
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
	//异常费明细 分页
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
	
	//结算账单
	public void confirmFeesBill(FeesBillEntity entity) throws Exception;

	//删除账单
	public void deleteFeesBill(FeesBillEntity entity) throws Exception;

	//获取商家 最后一次生成账单 时间
	public List<FeesBillEntity> getlastBillTime(Map<String, String> maps);
	
	//自定义-查询仓储费用信息
	public PageInfo<FeesReceiveStorageEntity> queryStorageInfoPage(Map<String, Object> param,int pageNo,int pageSize);
	//自定义-查询运输费用信息
	public PageInfo<FeesReceiveDeliverEntity> queryDeliverInfoPage(Map<String, Object> param,int pageNo,int pageSize);
	//自定义-查询配送费用信息
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchInfoPage(Map<String, Object> param,int pageNo,int pageSize);
	//自定义-查询异常费用信息
	public PageInfo<FeesAbnormalEntity> queryAbnormalInfoPage(Map<String, Object> param,int pageNo,int pageSize);
	
	//导出-根据账单查询仓储明细
	public PageInfo<FeesReceiveStorageEntity> querystorageDetailByBillNo(String billno,int pageNo,int pageSize);
	//导出-根据账单查询仓储明细,费用科目
	public PageInfo<FeesReceiveStorageEntity> queryStorageByBillNoSubjectId(String billno,String subjectId, 
			int pageNo,int pageSize);
	//导出-根据账单查询耗材使用
	public PageInfo<FeesReceiveStorageEntity> queryPackMaterialByBillNo(String billno,int pageNo,int pageSize);
	//导出-根据账单查询运输明细
	public PageInfo<FeesReceiveDeliverEntity> querydeliverDetailByBillNo(String billno,int pageNo,int pageSize);
	//导出-根据账单查询配送明细
	public List<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(String billno);
	public PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(String billno,int pageNo,int pageSize);
	//导出-根据账单查询异常明细
	public List<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno);
	public PageInfo<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno,int pageNo,int pageSize);
	
	public List<FeesReceiveStorageEntity> queryFeesData(Map<String, Object> param);
}
