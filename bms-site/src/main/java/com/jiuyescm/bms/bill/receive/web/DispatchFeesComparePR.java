package com.jiuyescm.bms.bill.receive.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.jiuyescm.bms.common.web.HttpCommanImportCompare;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchDistinctEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.BillDispatchDistinctDataType;
/**
 *宅配对账
 * @author Wuliangfeng
 *
 */
@Controller("dispatchFeesComparePR")
public class DispatchFeesComparePR extends HttpCommanImportCompare<FeesReceiveDispatchEntity,BillDispatchDistinctEntity>{

	@Resource
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	
	@Override
	protected List<FeesReceiveDispatchEntity> getOrgList(
			Map<String, Object> parameter) {
		return feesReceiveDispatchService.queryAllByBillSubject(parameter);
	}

	@Override
	protected BaseDataType getBaseDataType() {
		return new BillDispatchDistinctDataType();
	}

	@Override
	protected List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("waybillNo");
		return list;
	}

	@Override
	protected List<String> getNoCompareProperty() {
		
		return null;
	}
	
	@FileResolver
	public Map<String,Object> importCompareFileDiff(UploadFile file,Map<String,Object> parameter){
		return super.importFileDiff(file, parameter);
	}
	/**
	 * 获取进度
	 * @return
	 */
	@Expose
	public int getProgress() {
		return this.getProgressStatus();
	}
	
	@Expose
	public void setProgress(){
		 this.setProgressStatus();
	}

}
