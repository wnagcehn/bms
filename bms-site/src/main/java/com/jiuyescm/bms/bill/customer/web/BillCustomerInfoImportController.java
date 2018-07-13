package com.jiuyescm.bms.bill.customer.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.bill.customer.vo.BillCustomerInfoVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.web.HttpNewImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;

@Controller("billCustomerInfoImportController")
public class BillCustomerInfoImportController extends HttpNewImport<BillCustomerInfoVo,BillCustomerInfoVo> {

	private static final Logger logger = Logger.getLogger(BillCustomerInfoImportController.class.getName());
	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Override
	protected String getTemplateCode() {
		return "billcheck_invoicecustomer_import";
	}

	@Override
	protected String getSessionId() {
		return "BillCustomerInfo_Import";
	}
	private int queryErrorCount(){
		int k=10;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ERROR_COUNT");
			k=Integer.valueOf(code.getExtattr1());
		}catch(Exception e){
			
		}
		return k;
	}

	@Override
	protected List<BillCustomerInfoVo> validateImportList(
			List<BillCustomerInfoVo> importList, List<ErrorMessageVo> infoList) {
		List<BillCustomerInfoVo> list=null;
		try{
			//导入Excel 自己验证数据重复性
			int k=0;
			List<String> customerList=new ArrayList<String>();
			for(BillCustomerInfoVo vo:importList){
				k++;
				if(!customerList.contains(vo.getCustomerName())){
					customerList.add(vo.getCustomerName());
				}else{
					infoList.add(new ErrorMessageVo(k,"导入Excel中,商家名称【"+vo.getCustomerName()+"】重复"));
				}
			}
			if(infoList.size()>0){//自验未通过
				return importList;
			}
			List<BillCustomerInfoVo> checklist=billCustomerInfoService.queryAll();
			list=new ArrayList<BillCustomerInfoVo>();
			int index=0;
			int errorCount=queryErrorCount();
			for(BillCustomerInfoVo vo:importList){
				index++;
				if(vo.getTel().length()>=16){
					infoList.add(new ErrorMessageVo(index,"电话号码长度过长!"));
					continue;
				}
				if(vo.getPhone().length()>11){
					infoList.add(new ErrorMessageVo(index,"手机号码长度过长!"));
					continue;
				}
				boolean flag=true;
				for(BillCustomerInfoVo checkVo:checklist){
					if(checkVo.getCustomerName().equals(vo.getCustomerName())){
						flag=false;
						break;
					}
				}
				if(!flag){
					if(infoList.size()>errorCount){
						break;
					}else{
						infoList.add(new ErrorMessageVo(index,"商家名称【"+vo.getCustomerName()+"】已存在"));
					}
				}else{
					vo.setCreator(JAppContext.currentUserID());
					vo.setCreateTime(JAppContext.currentTimestamp());
					vo.setDelFlag("0");
					list.add(vo);
				}
				
			}
			return list;
		}catch(Exception e){
			infoList.add(new ErrorMessageVo(1,"验证数据异常 异常信息:"+e.getMessage()));
			return list;
		}
		
	}

	@Override
	protected void saveDataBatch(List<BillCustomerInfoVo> list)
			throws Exception {
		billCustomerInfoService.saveBatch(list);
	}

	@FileResolver
	public Map<String,Object> importExcel(UploadFile file,Map<String, Object> parameter){
		return super.importFile(file);
	}

	@Expose
	public int getProgress(){
		return super.getProgressStatus();
	}
	
	@Expose
	public void setProgress(){
		super.setProgressStatus();
	}
}
