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
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.billimport.IFeesHandler;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

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
	@Autowired
	private IMaterialDictService materialDictService;
	@Autowired 
	IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;

	@Override
	public List<BillFeesReceiveStorageTempEntity> transRowToObj(DataRow dr)
			throws Exception {
		//异常信息
		String errorMessage="";
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
					}else{
						errorMessage+="仓库不存在;";
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
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
			}
		}
		
		//起始列
		int index=1;;
		for (DataColumn dc:dr.getColumns()) {
			if("收件人地址".equals(dc.getColName())){
				index+=dc.getColNo();
			}		
		}
		int count=1;
		BillFeesReceiveStorageTempEntity feeEntity=new BillFeesReceiveStorageTempEntity();
		for(int i=index;i<dr.getColumns().size();i++){
			DataColumn dc=dr.getColumn(i);
			try {				
				if(count==1){
					feeEntity=new BillFeesReceiveStorageTempEntity();
					PropertyUtils.copyProperties(feeEntity, entity);
				}
				switch (dc.getColName()) {
				case "编码":
					PubMaterialInfoVo vo=materialDictService.getMaterialByCode(dc.getColValue());
					if(vo!=null){
						feeEntity.setMaterialCode(dc.getColValue());
					}else{
						errorMessage+="编码不存在;";
					}
					break;
				case "数量":
					if(StringUtils.isNotBlank(dc.getColValue())){
						feeEntity.setTotalQty(Integer.valueOf(dc.getColValue()));
					}
					break;
				case "金额":
					if(StringUtils.isNotBlank(dc.getColValue())){
						feeEntity.setAmount(new BigDecimal(dc.getColValue()));
					}
					break;
				case "规格":
					break;
				case "单价":
					break;
				default:
					PubMaterialInfoVo pubvo=materialDictService.getMaterialByCode(dc.getColValue());
					if(pubvo!=null){
						feeEntity.setMaterialName(dc.getColValue());
					}else{
						errorMessage+="耗材名称不存在;";
					}
					break;
				}
	
				count++;
				if(count>6){
					if(StringUtils.isNotBlank(feeEntity.getMaterialCode())){
						feeEntity.setSubjectCode("wh_material_use");
						list.add(feeEntity);
					}
					count=1;
				}
			} catch (Exception e) {
				errorMessage+="列【"+ dc.getColName() + "】格式不正确;";
			}
		}
		
		if(StringUtils.isNotBlank(errorMessage)){
			throw new BizException("行【" + dr.getRowNo()+"】"+ errorMessage);
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
		if(null != list && list.size()>0){
			billFeesReceiveStorageTempService.insertBatchTemp(list);
		}
	}



}
