package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 耗材使用费
 * @author zhaofeng
 *
 */
@Component("耗材使用费")
public class MaterialUseHandler extends CommonHandler<BillFeesReceiveStorageTempEntity> {
	@Autowired
	private IWarehouseDictService warehouseDictService;
	@Autowired
	private ICustomerDictService customerDictService;

	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		// TODO Auto-generated method stub
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
			try {
				switch (dc.getColName()) {
				case "仓库":
					entity.setWarehouseName(dc.getColValue());
					String wareId=warehouseDictService.getWarehouseCodeByName(dc.getColValue());
					if(StringUtils.isNotBlank(wareId)){
						entity.setWarehouseCode(wareId);
					}
					break;
				case "商家名称":
					entity.setCustomerName(dc.getColValue());
					String customerId=customerDictService.getCustomerCodeByName(dc.getColValue());
					if(StringUtils.isNotBlank(customerId)){
						entity.setCustomerId(customerId);
					}
					break;
				case "运单号":
					entity.setWaybillNo(dc.getColValue());
					break;
				case "出库单号":
					entity.setOrderNo(dc.getColValue());
					break;
				case "商品总数":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalQty(Integer.parseInt(dc.getColValue()));
					}
					break;
				case "接单时间":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				throw new BizException("行【"+dr.getRowNo()+"】，列【"+dc.getColName()+"】格式不正确");
			}
		}
		
		//起始列
		int start=0;
		for (DataColumn dc:dr.getColumns()) {
			if("收件人地址".equals(dc.getColValue())){
				start=dc.getColNo()+1;
			}
		}
		
		//6个字段循环一次
		int count=1;
		for(int i=start;i<dr.getColumns().size();i++){
			DataColumn dc=dr.getColumn(i);
			if(count<=6){
				BillFeesReceiveStorageTempEntity feeEntity = new BillFeesReceiveStorageTempEntity();
				PropertyUtils.copyProperties(feeEntity, entity);
				
			}
			count++;
		}
		
		
		
		
		return list;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}



}
