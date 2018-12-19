package com.jiuyescm.bms.jobhandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeDetailEntity;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;
import com.jiuyescm.bms.general.service.IBizDispatchCarrierChangeDetailService;
import com.jiuyescm.bms.general.service.IBizDispatchCarrierChangeService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 九曳转顺丰的数据处理
 * 
 * @author wangchen870
 *
 */
@JobHander(value = "bizDispatchCarrierChangeJob")
@Service
public class BizDispatchCarrierChangeJob extends IJobHandler {

	@Autowired
	IBizDispatchCarrierChangeDetailService bizDispatchCarrierChangeDetailService;

	@Autowired
	IBizDispatchCarrierChangeService bizDispatchCarrierChangeService;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("bizDispatchCarrierChangeJob start.");
		bizDispatchCarrierChange();
		return ReturnT.SUCCESS;
	}
	
	
	
	private void bizDispatchCarrierChange() {

		// 查询全部未被处理过的数据
		List<BizDispatchCarrierChangeDetailEntity> detailList = bizDispatchCarrierChangeDetailService.queryAll();
		if (!detailList.isEmpty()) {
			for (BizDispatchCarrierChangeDetailEntity detailEntity : detailList) {
				if ("1500000015".equals(detailEntity.getNewCarrierId())) {
					// 根据最新物流商是顺丰的运单查询
					BizDispatchCarrierChangeDetailEntity entity = bizDispatchCarrierChangeDetailService.queryByWayBillNo(detailEntity.getWaybillNo());
					// 校验change表是否存在该运单号
					BizDispatchCarrierChangeEntity checkEntity = bizDispatchCarrierChangeService.findByWaybillNo(detailEntity.getWaybillNo());
					if (null == checkEntity) {
						if ("1500000016".equals(entity.getOldCarrierId())) {
							// 组装数据
							BizDispatchCarrierChangeEntity bizDispatchCarrierChangeEntity = new BizDispatchCarrierChangeEntity();
							bizDispatchCarrierChangeEntity.setWarehouseCode(entity.getWarehouseCode());
							bizDispatchCarrierChangeEntity.setCustomerid(entity.getCustomerid());
							bizDispatchCarrierChangeEntity.setOldCarrierId(entity.getOldCarrierId());
							bizDispatchCarrierChangeEntity.setNewCarrierId(detailEntity.getNewCarrierId());
							bizDispatchCarrierChangeEntity.setWaybillNo(entity.getWaybillNo());
							bizDispatchCarrierChangeEntity.setUpdateReasonTypeCode(entity.getUpdateReasonTypeCode());
							bizDispatchCarrierChangeEntity.setUpdateReasonType(entity.getUpdateReasonType());
							bizDispatchCarrierChangeEntity
									.setUpdateReasonDetailCode(entity.getUpdateReasonDetailCode());
							bizDispatchCarrierChangeEntity.setUpdateReasonDetail(entity.getUpdateReasonDetail());
							bizDispatchCarrierChangeEntity.setRemark(entity.getRemark());
							bizDispatchCarrierChangeEntity.setWriteTime(entity.getWriteTime());
							bizDispatchCarrierChangeEntity.setDutyType(entity.getDutyType());
							bizDispatchCarrierChangeEntity.setOldUpdateReason(entity.getOldUpdateReason());
							try {
								bizDispatchCarrierChangeService.saveAndUpdateState(bizDispatchCarrierChangeEntity);
							} catch (Exception e) {
								XxlJobLogger.log("保存失败！", e.getMessage());
							}
						}
					}
				}
				detailEntity.setIsCalculated("1");
				try {
					bizDispatchCarrierChangeDetailService.update(detailEntity);
				} catch (Exception e) {
					XxlJobLogger.log("修改失败！", e.getMessage());
				}
			}
		}

	}

}
