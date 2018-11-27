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
 * 增值
 * @author wangchen870
 *
 */

@Component("增值")
public class AddHandler extends CommonHandler<BillFeesReceiveStorageTempEntity>{

	@Autowired IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;
	
	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr) throws Exception {
		List<BillFeesReceiveStorageTempEntity> list = new ArrayList<BillFeesReceiveStorageTempEntity>();
		BillFeesReceiveStorageTempEntity entity = new BillFeesReceiveStorageTempEntity();
		//DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		for (DataColumn dc:dr.getColumns()) {
			try {
				System.out.println("列名【" + dc.getColName() + "】|值【"+ dc.getColValue() + "】");
				switch (dc.getColName()) {
				case "仓库名称":
					entity.setWarehouseName(dc.getColValue());
					break;
				//需转Code
				case "增值项目":
					entity.setSubjectCode(dc.getColValue());;
					break;
				case "数量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalQty(Integer.parseInt(dc.getColValue()));
					}	
					break;
				case "单位":
					entity.setChargeUnit(dc.getColValue());			
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
		//增值费
		entity.setSubjectCode("wh_value_add_subject");
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
