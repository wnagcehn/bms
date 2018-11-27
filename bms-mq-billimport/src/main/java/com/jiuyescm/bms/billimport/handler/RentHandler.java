package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.exception.BizException;

/**
 * 仓租
 * @author wangchen
 *
 */

@Component("仓租")
public class RentHandler extends CommonHandler<BillFeesReceiveStorageTempEntity>{
	
	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;

	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr) throws Exception {
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		for (DataColumn dc:dr.getColumns()) {
			try {
				System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
				switch (dc.getColName()) {
				case "仓库名称":
					entity.setWarehouseName(dc.getColValue());
					break;
				case "温度":	
					//需映射Code
					entity.setTempretureType(dc.getColValue());
					break;
				case "金额":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setAmount(new BigDecimal(dc.getColValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}		
					break;
				default:
					break;
				}
			} catch (Exception e) {
				throw new BizException("行【"+dr.getRowNo()+"】，列【"+dc.getColName()+"】格式不正确");
			}
		}
		//仓租费
		entity.setSubjectCode("wh_rent");
		list.add(entity);
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
