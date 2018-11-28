package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;

/**
 * TB
 * @author zhaofeng
 *
 */
@Component("TB")
public class OutStockHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	@Autowired IWarehouseDictService warehouseDictService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		BillFeesReceiveStorageTempEntity entity1 = new BillFeesReceiveStorageTempEntity();
		BillFeesReceiveStorageTempEntity entity2 = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
			try {
				switch (dc.getColName()) {
				case "仓库名称":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						//如果没找到，报错
						String warehouseCode = warehouseDictService.getWarehouseCodeByName(dc.getColValue());
						entity.setWarehouseCode(warehouseCode);
						entity.setWarehouseName(dc.getColValue());
					}			
					break;
				case "发货时间":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}	
					break;
				case "单据类型":
					entity.setOrderType(dc.getColValue());
					break;
				case "出库单号":
					entity.setOrderNo(dc.getColValue());
					break;
				case "温度类型":
					entity.setTempretureType(BmsEnums.tempretureType.getCode(dc.getColValue()));
					break;
				case "出库件数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalQty(Integer.valueOf(dc.getColValue()));
					}	
					break;
				case "出库箱数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalBox(new BigDecimal(dc.getColValue()));
					}
					break;
				case "出库重量(吨)":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalWeight(new BigDecimal(dc.getColValue()));
					}
					break;
//				case "B2B订单操作费":
//					if (StringUtils.isNotBlank(dc.getColValue())) {
//						entity.setAmount(Double.valueOf(dc.getColValue()));
//					}	
//					break;
//				case "出库装车费":
//					PropertyUtils.copyProperties(zEntity, entity);
//					if (StringUtils.isNotBlank(dc.getColValue())) {
//						zEntity.setAmount(Double.valueOf(dc.getColValue()));
//					}	
//					break;
				default:
					break;
				}
			} catch (Exception e) {
				throw new BizException("行【"+dr.getRowNo()+"】，列【"+dc.getColName()+"】格式不正确");
			}
//			if ("B2B订单操作费".equals(dc.getColName()) || StringUtils.isNotBlank(dc.getColValue())) {
//				entity.setAmount(Double.valueOf(dc.getColValue()));
//			}
//			if ("出库装车费".equals(dc.getColName()) || StringUtils.isNotBlank(dc.getColValue())) {
//				PropertyUtils.copyProperties(zEntity, entity);
//				zEntity.setAmount(Double.valueOf(dc.getColValue()));
//			}
		}
		for (DataColumn dc:dr.getColumns()) {
			switch (dc.getColName()) {
			case "B2B订单操作费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity1, entity);
					entity1.setSubjectCode("wh_b2b_work");
					entity1.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				break;
			case "出库装车费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity2, entity);
					entity2.setSubjectCode("wh_b2b_handwork");
					entity2.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				break;
			default:
				break;
			}
		}
		//B2B订单操作费，出库装车费(防空白行)
		if (null != entity1 && StringUtils.isNotBlank(entity1.getOrderNo())) {
			list.add(entity1);
		}
		if (null != entity2 && StringUtils.isNotBlank(entity2.getOrderNo())) {
			list.add(entity2);
		}
		return list;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		if (null != list && list.size() > 0) {
			billFeesReceiveStorageTempService.insertBatchTemp(list);
		}
	}



}
