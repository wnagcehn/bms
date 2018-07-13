package com.jiuyescm.bms.bill.customer.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Controller("billSysCustomerInfoImportController")
public class BillSysCustomerInfoImportController extends HttpNewImport<BillCustomerInfoVo,BillCustomerInfoVo> {
	
	private static final Logger logger = Logger.getLogger(BillSysCustomerInfoImportController.class.getName());

	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	@Autowired 
	private ICustomerService customerService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Override
	protected String getTemplateCode() {
		return "billcheck_invoicesyscustomer_import";
	}

	@Override
	protected String getSessionId() {
		return "BillSysCustomerInfo_Import";
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

	private List<CustomerVo> queryAllSysCustomer(){
		return customerService.queryAll();
	}
	private boolean checkSysCustomerHasBind(BillCustomerInfoVo infoVo,List<BillCustomerInfoVo> voList){
		boolean flag=true;
		for(BillCustomerInfoVo voEntity:voList){
			if(StringUtils.isNotBlank(voEntity.getSysCustomerName())
					&&voEntity.getSysCustomerName().equals(infoVo.getSysCustomerName())){
				flag=false;
				break;
			}
		}
		return flag;
	}
	/**
	 * 验证开票商家是否存在
	 * 验证系统商家是否存在
	 * 
	 */
	@Override
	protected List<BillCustomerInfoVo> validateImportList(
			List<BillCustomerInfoVo> importList, List<ErrorMessageVo> infoList) {
		try{
			List<CustomerVo> list=queryAllSysCustomer();
			List<BillCustomerInfoVo> voList=billCustomerInfoService.queryAll();
			int index=0;
			int errorCount=queryErrorCount();
			for(BillCustomerInfoVo infoVo:importList){
				index++;
				if(!checkSysCustomerHasBind(infoVo,voList)){
					infoList.add(new ErrorMessageVo(index,"系统商家【"+infoVo.getSysCustomerName()+"】已被绑定,不能重新绑定其他开票商家!"));
				}else{
					boolean flag=false;
					for(BillCustomerInfoVo voEntity:voList){
						if(voEntity.getCustomerName().equals(infoVo.getCustomerName())){
							infoVo.setCustomerId(voEntity.getCustomerId());
							infoVo.setId(voEntity.getId());
							flag=true;
							break;
						}
					}
					if(!flag){
						if(infoList.size()>errorCount){
							break;
						}else{
							infoList.add(new ErrorMessageVo(index,"开票商家【"+infoVo.getCustomerName()+"】不存在!"));
						}
					}
					flag=false;
					for(CustomerVo voEntity:list){
						if(voEntity.getCustomername().equals(infoVo.getSysCustomerName())){
							infoVo.setSysCustomerId(voEntity.getCustomerid());
							infoVo.setCustomerName(voEntity.getCustomername());
							flag=true;
							break;
						}
					}
					if(!flag){
						if(infoList.size()>errorCount){
							break;
						}else{
							infoList.add(new ErrorMessageVo(index,"系统商家【"+infoVo.getSysCustomerName()+"】不存在!"));
						}
					}
				}
			
			}
			return importList;
		}catch(Exception e){
			infoList.add(new ErrorMessageVo(1,"验证数据异常 异常信息:"+e.getMessage()));
			return importList;
		}
		
	}

	@Override
	protected void saveDataBatch(List<BillCustomerInfoVo> list)
			throws Exception {
		billCustomerInfoService.updateBatch(list);
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
