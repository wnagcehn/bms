package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 仓储
 * @author zhaofeng
 *
 */
@Component("仓储")
public class StorageHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		List<BillFeesReceiveStorageTempEntity> lists = new ArrayList<BillFeesReceiveStorageTempEntity>();
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		//商品按托存储--LD
		BillFeesReceiveStorageTempEntity entity1 = null;
		//商品按托存储--LC
		BillFeesReceiveStorageTempEntity entity2 = null;
		//商品按托存储--CW
		BillFeesReceiveStorageTempEntity entity3 = null;
		//商品按托存储--HW
		BillFeesReceiveStorageTempEntity entity4 = null;
		//耗材按托存储--CW
		BillFeesReceiveStorageTempEntity entity5 = null;
		//耗材按托存储--LD
		BillFeesReceiveStorageTempEntity entity6 = null;
		//商品按件存储
		BillFeesReceiveStorageTempEntity entity7 = null;
		//处置费--入库
		BillFeesReceiveStorageTempEntity entity8 = null;
		//出库
		BillFeesReceiveStorageTempEntity entity9 = null;
		
		for (DataColumn dc : dr.getColumns()) {
			try {
				System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
				switch (dc.getColName()) {
				case "日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}			
					break;
				case "冷冻":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity1 = new BillFeesReceiveStorageTempEntity();
						entity1.setSubjectCode("wh_product_storage");
						entity1.setChargeUnit("PALLETS");
						entity1.setCreateTime(entity.getCreateTime());
						entity1.setTempretureType("LD");
						entity1.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "冷冻费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity1.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "冷藏":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity2 = new BillFeesReceiveStorageTempEntity();
						entity2.setSubjectCode("wh_product_storage");
						entity2.setChargeUnit("PALLETS");
						entity2.setCreateTime(entity.getCreateTime());
						entity2.setTempretureType("LC");
						entity2.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "冷藏费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity2.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "恒温":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity3 = new BillFeesReceiveStorageTempEntity();
						entity3.setSubjectCode("wh_product_storage");
						entity3.setChargeUnit("PALLETS");
						entity3.setCreateTime(entity.getCreateTime());
						entity3.setTempretureType("HW");
						entity3.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "恒温费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity3.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "常温":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity4 = new BillFeesReceiveStorageTempEntity();
						entity4.setSubjectCode("wh_product_storage");
						entity4.setChargeUnit("PALLETS");
						entity4.setCreateTime(entity.getCreateTime());
						entity4.setTempretureType("CW");
						entity4.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "常温费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity4.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "常温包材":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity5 = new BillFeesReceiveStorageTempEntity();
						entity5.setSubjectCode("wh_material_storage");
						entity5.setChargeUnit("PALLETS");
						entity5.setCreateTime(entity.getCreateTime());
						entity5.setTempretureType("CW");
						entity5.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "常温包材费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity5.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "冷冻包材":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity6 = new BillFeesReceiveStorageTempEntity();
						entity6.setSubjectCode("wh_material_storage");
						entity6.setChargeUnit("PALLETS");
						entity6.setCreateTime(entity.getCreateTime());
						entity6.setTempretureType("LD");
						entity6.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "冷冻包材费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity6.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "库存件数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity7 = new BillFeesReceiveStorageTempEntity();
						entity7.setSubjectCode("wh_product_storage");
						entity7.setChargeUnit("ITEMS");
						entity7.setCreateTime(entity.getCreateTime());
						entity7.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "存储费按件小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity7.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "入库板数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity8 = new BillFeesReceiveStorageTempEntity();
						entity8.setSubjectCode("wh_disposal");
						//entity8.setChargeUnit("ITEMS");
						entity8.setCreateTime(entity.getCreateTime());
						entity8.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "处置费小计/元":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity8.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					break;
				case "出库板数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity9 = new BillFeesReceiveStorageTempEntity();
						entity9.setSubjectCode("outstock_pallet_vm");
						entity9.setCreateTime(entity.getCreateTime());
						entity9.setTotalQty(Integer.valueOf(dc.getColValue()));
						entity9.setAmount(BigDecimal.ZERO);
					}
					break;
				default:
					break;
				}
			
			} catch (Exception e) {
				throw new BizException("行【"+dr.getRowNo()+"】，列【"+dc.getColName()+"】格式不正确");
			}
		}
		if (null != entity1) {
			lists.add(entity1);
		}
		if (null != entity2) {
			lists.add(entity2);
		}
		if (null != entity3) {
			lists.add(entity3);
		}
		if (null != entity4) {
			lists.add(entity4);
		}
		if (null != entity5) {
			lists.add(entity5);
		}
		if (null != entity6) {
			lists.add(entity6);
		}
		if (null != entity7) {
			lists.add(entity7);
		}
		if (null != entity8) {
			lists.add(entity8);
		}
		if (null != entity9) {
			lists.add(entity9);
		}
		return lists;
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
