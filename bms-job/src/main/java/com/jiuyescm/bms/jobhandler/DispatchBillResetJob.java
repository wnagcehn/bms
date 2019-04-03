package com.jiuyescm.bms.jobhandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillUpdateEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.general.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillUpdateService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="dispatchBillResetJob")
@Service
public class DispatchBillResetJob extends IJobHandler{

	@Autowired private IBizDispatchBillUpdateService bizDispatchBillUpdateService;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
	@Autowired private IFeesReceiveDispatchService feesReceiveDispatchService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		try{
			return startJob(params);
		}catch(Exception e){
			XxlJobLogger.log("执行异常:",e);
			return ReturnT.FAIL;
		}
	}
	
	private ReturnT<String> startJob(String... params){
		
		long btime= System.currentTimeMillis();// 系统开始时间
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		int num = 100;
		Map<String, Object> map = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
		    try {
		    	map = JobParameterHandler.handler(params);//处理定时任务参数
		    } catch (Exception e) {
	            current = System.currentTimeMillis();
	            XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ (current - btime) + "毫秒");
	            return ReturnT.FAIL;
	        }
		}else {
			map.put("num", num);//单次执行最多计算多少入库单
		}
		
		List<BizDispatchBillUpdateEntity> bizList=bizDispatchBillUpdateService.queryData(map);
		if(bizList==null||bizList.size()==0){
			current = System.currentTimeMillis();
			XxlJobLogger.log("无数据处理,总耗时：【{0}】毫秒  ",(current - start));
			return  ReturnT.SUCCESS;
		}else{
			cancelData(bizList);//作废数据
			current = System.currentTimeMillis();
			XxlJobLogger.log("总耗时：【{0}】毫秒  ",(current - start));
			return  ReturnT.SUCCESS;
		}
	}
	
	
	private BizDispatchBillEntity getDispatchBillEntity(BizDispatchBillUpdateEntity updateEntity){
		//对于调整值,应保留作废前的调整数据
		BizDispatchBillEntity entity=bizDispatchBillService.getDispatchEntityByWaybillNo(updateEntity.getWaybillNo());
		if(entity!=null){
			entity.setAcceptTime(updateEntity.getAcceptTime());
			entity.setAccountState(updateEntity.getAccountState());
			entity.setBigstatus(updateEntity.getBigstatus());
			entity.setCalculateTime(updateEntity.getCalculateTime());
			entity.setCarrierId(updateEntity.getCarrierId());
			entity.setCarrierName(updateEntity.getCarrierName());
			entity.setCollectMoney(updateEntity.getCollectMoney()==null?null:updateEntity.getCollectMoney().doubleValue());
			entity.setCreateTime(updateEntity.getCreateTime());
			entity.setCreator(updateEntity.getCreator());
			entity.setCustomerid(updateEntity.getCustomerid());
			entity.setCustomerName(updateEntity.getCustomerName());
			entity.setDelFlag(updateEntity.getDelFlag());
			entity.setDeliverid(updateEntity.getDeliverid());
			entity.setDeliverName(updateEntity.getDeliverName());
			entity.setDispatchId(null);
			entity.setDispatchTypeCode(updateEntity.getDispatchTypeCode());
			entity.setDutyType(null);
			entity.setExpresstype(updateEntity.getExpresstype());
			entity.setServiceTypeCode(updateEntity.getExpresstype());
			entity.setExtattr1(updateEntity.getExtattr1());
			entity.setExtattr2(updateEntity.getExtattr2());
			entity.setExtattr3(updateEntity.getExtattr3());
			entity.setExtattr4(updateEntity.getExtattr4());
			entity.setExtattr5(updateEntity.getExtattr5());
			entity.setExternalNo(updateEntity.getExternalNo());
			entity.setFeesNo(entity.getFeesNo());
			entity.setId(Long.valueOf(String.valueOf(updateEntity.getId())));
			entity.setIsCalculated("0");
			entity.setLastModifier(updateEntity.getLastModifier());
			entity.setLastModifyTime(updateEntity.getLastModifyTime());
			entity.setMobanId(null);
			entity.setMonthFeeCount(updateEntity.getMonthFeeCount());
			entity.setOriginCarrierId(updateEntity.getOriginCarrierId());
			entity.setOriginCarrierName(updateEntity.getOriginCarrierName());
			entity.setOutstockNo(updateEntity.getOutstockNo());
			entity.setPriceList(null);
			entity.setProductDetail(updateEntity.getProductDetail());
			entity.setReceiveCityId(updateEntity.getReceiveCityId());
			entity.setReceiveCityName(updateEntity.getReceiveCityId());
			entity.setReceiveDetailAddress(updateEntity.getReceiveDetailAddress());
			entity.setReceiveDistrictId(updateEntity.getReceiveDistrictId());
			entity.setReceiveDistrictName(updateEntity.getReceiveDistrictId());
			entity.setReceiveName(updateEntity.getReceiveName());
			entity.setReceiveProvinceId(updateEntity.getReceiveProvinceId());
			entity.setReceiveProvinceName(updateEntity.getReceiveProvinceId());
			entity.setReceiveStreet(updateEntity.getReceiveStreet());
			entity.setReceiveTime(updateEntity.getReceiveTime());
			entity.setRemark(updateEntity.getRemark());
			entity.setSendDetailAddress(updateEntity.getSendDetailAddress());
			entity.setSendName(updateEntity.getSendName());
			entity.setSendStreet(updateEntity.getSendStreet());
			entity.setServiceTypeCode(updateEntity.getServiceTypeCode());
			entity.setSignTime(updateEntity.getSignTime());
			entity.setSkuNum(updateEntity.getSkuNum());
			entity.setSmallstatus(updateEntity.getSmallstatus());
			entity.setSystemWeight(updateEntity.getSystemWeight()==null?null:updateEntity.getSystemWeight().doubleValue());
			entity.setTemperatureTypeCode(updateEntity.getTemperatureTypeCode());
			entity.setTemperatureTypeName(entity.getTemperatureTypeName());
			entity.setThrowWeight(updateEntity.getThrowWeight()==null?null:updateEntity.getThrowWeight().doubleValue());
			entity.setTimeliness(null);
			entity.setTotalqty(updateEntity.getTotalqty());
			entity.setTotalVolume(updateEntity.getTotalVolume()==null?null:updateEntity.getTotalVolume().doubleValue());
			entity.setTotalWeight(updateEntity.getTotalWeight()==null?null:updateEntity.getTotalWeight().doubleValue());
			entity.setUpdateReasonDetail(null);
			entity.setWarehouseCode(updateEntity.getWarehouseCode());
			entity.setWarehouseName(updateEntity.getWarehouseName());
			entity.setWaybillList(updateEntity.getWaybillList());
			entity.setWaybillNo(updateEntity.getWaybillNo());
			entity.setWaybillNum(updateEntity.getWaybillNum()==null?null:updateEntity.getWaybillNum().doubleValue());
			entity.setWeight(null);
			entity.setWriteTime(updateEntity.getWriteTime());
			entity.setZexpressnum(updateEntity.getZexpressnum());
			entity.setBizType(null);
			entity.setSenderProvince(updateEntity.getSendProvinceId());
			entity.setSenderCity(updateEntity.getSendCityId());
			entity.setSendDistrictId(updateEntity.getSendDistrictId());
		}	
		return entity;
	}
	
	//数据作废
	private void cancelData(List<BizDispatchBillUpdateEntity> bizList){
		
		for(BizDispatchBillUpdateEntity updateEntity:bizList){
			try{
				if(updateEntity.getUpdateType()!=null&&"CLOSE".equals(updateEntity.getUpdateType())){//转寄
					//取消的单子直接重算、更新运单的状态为取消
					Map<String,Object> condition=new HashMap<String,Object>();
					condition.put("waybillNo", updateEntity.getWaybillNo());
					condition.put("isCalculated", "0");
					condition.put("orderStatus", "CLOSE");
					bizDispatchBillService.updateByParam(condition);
				}
				else{
					//转寄的单子
					//作废原配送业务数据,新增业务数据
					BizDispatchBillEntity entity=getDispatchBillEntity(updateEntity);							
					if(entity!=null){
						bizDispatchBillService.deleteByWayBillNo(updateEntity.getWaybillNo());
						//feesReceiveDispatchService.deleteByWayBillNo(updateEntity.getWaybillNo());		
						bizDispatchBillService.insertDispatchBillEntity(entity);
					}
				}
				
				updateEntity.setIsCalculated("1");

				bizDispatchBillUpdateService.updateToIsCalculated(updateEntity);
			}
			catch(Exception ex){
				updateEntity.setIsCalculated("2");
				XxlJobLogger.log("执行异常:",ex);
				bizDispatchBillUpdateService.updateToIsCalculated(updateEntity);
			}
		}
	}

}
