package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.exception.BizException;

/**
 * TB
 * @author zhaofeng
 *
 */
@Component("TB")
public class OutStockHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		BillFeesReceiveStorageTempEntity zEntity = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			try {
				switch (dc.getColName()) {
				case "仓库名称":
					entity.setWarehouseName(dc.getColValue());
					break;
				case "发货时间":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(Timestamp.valueOf(dc.getColValue()));
					}	
					break;
				case "商家名称":
					entity.setCustomerName(dc.getColValue());
					break;
				case "单据类型":
					entity.setOrderType(dc.getColValue());
					break;
				case "出库单号":
					entity.setOrderNo(dc.getColValue());
					break;
				case "温度类型":
					entity.setTempretureType(dc.getColValue());
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
				case "B2B订单操作费":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setAmount(Double.valueOf(dc.getColValue()));
					}	
					break;
				case "出库装车费":
					PropertyUtils.copyProperties(zEntity, entity);
					if (StringUtils.isNotBlank(dc.getColValue())) {
						zEntity.setAmount(Double.valueOf(dc.getColValue()));
					}	
					break;
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
		//B2B订单操作费，出库装车费
		list.add(entity);
		list.add(zEntity);
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
