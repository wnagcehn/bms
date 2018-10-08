/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.service.IBizAddFeeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.fees.INormalReqVoService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizAddFeeController")
public class BizAddFeeController {

	private static final Logger logger = Logger.getLogger(BizAddFeeController.class.getName());

	@Resource
	private IBizAddFeeService bizAddFeeService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired 
	private ICustomerService customerService;
	
	@Autowired
	private INormalReqVoService  service;
	
	@Autowired 
	private SequenceService sequenceService;
	
	@Autowired 
	private IFeesReceiveStorageService feesReceiveStorageService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	@Resource
	private Lock lock;

	@DataProvider
	public BizAddFeeEntity findById(Long id) throws Exception {
		BizAddFeeEntity entity = null;
		entity = bizAddFeeService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizAddFeeEntity> page, Map<String, Object> param) {
		PageInfo<BizAddFeeEntity> pageInfo = bizAddFeeService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizAddFeeEntity entity) {
		if (entity.getId() == null) {
			bizAddFeeService.save(entity);
		} else {
			bizAddFeeService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizAddFeeEntity entity) {
		entity.setDelFlag("1");
		try {
			bizAddFeeService.delete(entity);
		} catch (Exception e) {
			logger.error("删除失败！", e);
		}	
	}
	/**
	 * 导入模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/add_storage_template.xlsx");
		return new DownloadFile("增值费导入模板.xlsx", is);
	}
	
	@FileResolver
	public Map<String, Object> importProductPalletTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String userid=JAppContext.currentUserID();
		String lockString=Tools.getMd5("BMS_QUO_IMPORT_BMS_ADDFEE_STORAGE"+userid+"sdf");
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=new HashMap<String, Object>();
				try {
					map=importStorageNew(file,parameter);
				} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg("系统错误:"+e.getMessage());
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
				return map;
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("增值费导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(
					LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
		});
		return remap;
	}
	
    public Map<String, Object> importStorageNew(UploadFile file, Map<String, Object> parameter) throws Exception{
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		String[] str = {"日期","仓库名称","商家全称","外部单号","费用名称","服务内容","数量","调整数量","单位","备注"};
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 检查导入模板是否正确
		boolean isTemplate = ExportUtil.checkTitle(file,str);
		if (!isTemplate){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		//start
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String, String> feeTypeMap = new HashMap<String, String>();
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0){
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}
		//费用类型
		/*Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "wh_value_add_subject");
		List<SystemCodeEntity> feeTypeList = systemCodeService.queryCodeList(param);
		if(feeTypeList != null && feeTypeList.size()>0){
		    for(SystemCodeEntity scEntity : feeTypeList){
		    	feeTypeMap.put(scEntity.getCodeName().trim(), scEntity.getCode());
		    }
		}*/
		feeTypeMap=bmsGroupSubjectService.getImportSubject("receive_wh_valueadd_subject");
	   //end
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if(xssfSheet==null)
			return null;
		
		// 库存数据
		final List<BizAddFeeEntity> addList = new ArrayList<BizAddFeeEntity>();
		//耗材数据
		final List<BizAddFeeEntity> updateList = new ArrayList<BizAddFeeEntity>();
		
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		//验证导入数据有重复
		Map<String, String> excelMap=new HashMap<>();
		//插入正式表
		int insertNum = 0;
		int dupNum = 0;
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++)
        {
        	String errorMsg="";
			XSSFRow xssfRow = xssfSheet.getRow(rowNum); 
			if(null==xssfRow)
			{
				if(rowNum!=xssfSheet.getLastRowNum()){
					errorMsg+="空行！";
				}else{
					break;
				}
				
			}
				
			String rkrq = ExportUtil.getValue(xssfRow.getCell(0), evaluator);//入库日期
			String warehouseName = ExportUtil.getValue(xssfRow.getCell(1), evaluator);//仓库名称
			String customerName = ExportUtil.getValue(xssfRow.getCell(2), evaluator);//商家全称
			String externalNo=ExportUtil.getValue(xssfRow.getCell(3), evaluator);//外部单号
			String feesTypeName = ExportUtil.getValue(xssfRow.getCell(4), evaluator);//费用类型
			String serviceContent = ExportUtil.getValue(xssfRow.getCell(5), evaluator);//服务内容
			Timestamp time = null;
			
			if(StringUtils.isEmpty(rkrq))
			{
			    int lieshu = 1;
			    errorMsg+="第"+lieshu+"列入库日期不能为空！";
			}
			
			try {
				  time = Timestamp.valueOf(rkrq);
			  } catch (Exception e){
				  try {
					  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
					  Date date = DateUtils.parseDate(rkrq, dataPatterns);
					  time = new Timestamp(date.getTime());
				} catch (Exception e1) {
					int lieshu = 1;
					 errorMsg+="第"+lieshu+"列日期格式不对！";
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			}
			
			if(StringUtils.isEmpty(warehouseName))
			{
			    int lieshu = 2;
			    errorMsg+="第"+lieshu+"列仓库名称为空！";
			}
			
			if(StringUtils.isEmpty(customerName))
			{
			    int lieshu = 3;
			    errorMsg+="第"+lieshu+"列商家名称为空！";
		    }
			
			/*if(StringUtils.isEmpty(customerName))
			{
			    int lieshu = 4;
			    errorMsg+="第"+lieshu+"列项目为空！";
		    }*/
			
			if(StringUtils.isEmpty(feesTypeName))
			{
			    int lieshu = 5;
			    errorMsg+="第"+lieshu+"列费用类型为空！";
		    }else{
		    	if(feeTypeMap.get(feesTypeName)==null){
		    		errorMsg+="费用类型["+feesTypeName+"]没有在费用类型中维护!";
		    	}
		    }
			
			warehouseName = ExportUtil.replaceBlank(warehouseName).trim();
			customerName  = ExportUtil.replaceBlank(customerName).trim();
			
			if(wareHouseMap.get(warehouseName)==null)
			{
				 errorMsg+="仓库名["+warehouseName+"]没有在仓库表中维护!";
		    }
			
		    if(customerMap.get(customerName)==null)
		    {
		    	 errorMsg+="商家["+customerName+"]没有在表中维护!";
		    }
			
			String num = ExportUtil.getValue(xssfRow.getCell(6), evaluator);//数量	
			String adjustNum = ExportUtil.getValue(xssfRow.getCell(7), evaluator);//调整数量
			
			boolean isAdd = false;
			boolean isUpdate = false;
			
			if(StringUtils.isNotEmpty(num))
			{
				if(ExportUtil.isNumber(num))
				{
					isAdd = true;
				}else{
					 errorMsg+="第"+7+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(adjustNum))
			{
				if(ExportUtil.isNumber(adjustNum))
				{
					isUpdate  = true;
				}else{
					 errorMsg+="第"+8+"列为非数字！";
				}
			}
			
			//Excel自身校验，判断是否有重复项("日期","仓库名称","商家全称","外部单号","费用名称")
			String dataKey=rkrq+"&"+warehouseName+"&"+customerName+"&"+externalNo+"&"+feesTypeName;
			if(excelMap.containsKey(dataKey)){
				//数据有重复
				setMessage(infoList, rowNum+1,"Excel中第"+(rowNum+1)+"行与第"+excelMap.get(dataKey).toString()+"行数据重复");
				excelMap.put(dataKey, (rowNum+1)+"");
				if(infoList.size()>=1000){
					break;
				}
			}else{
				excelMap.put(dataKey, (rowNum+1)+"");
			}
			
			if(!StringUtils.isBlank(errorMsg)){
				setMessage(infoList, rowNum+1,errorMsg);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			String customerid = customerMap.get(customerName).trim();
			String warehouseCode = wareHouseMap.get(warehouseName).trim();
			
			BizAddFeeEntity data = new BizAddFeeEntity();
			data.setOperationTime(time);
			data.setCustomerid(customerid);
			data.setCustomerName(customerName);
			data.setWarehouseCode(warehouseCode);
			data.setWarehouseName(warehouseName);
			data.setServiceContent(serviceContent);
			//data.setItem(item);
			data.setExternalNo(externalNo);
			data.setFeesType(feeTypeMap.get(feesTypeName.trim()));
			data.setFeesUnit(ExportUtil.getValue(xssfRow.getCell(8), evaluator));
			data.setRemarkContent(ExportUtil.getValue(xssfRow.getCell(9), evaluator));
			data.setDelFlag("0");
			data.setFeesTypeName(feesTypeName);
			
			if(StringUtils.isNotBlank(adjustNum)){
				data.setAdjustNum(Double.valueOf(adjustNum));				
			}
			if(StringUtils.isNotBlank(num)){
				data.setNum(Double.valueOf(num));
			}
			
			if(isUpdate){
				data.setLastModifyTime(currentTime);
				data.setLastModifier(userName);
				data.setCreateTime(time);
				data.setCreator(userName);
				data.setIsCalculated("0");
				data.setDelFlag("0");
				
				updateList.add(data);
			}else{
				data.setCreateTime(time);
				data.setCreator(userName);
				data.setIsCalculated("0");
				data.setDelFlag("0");
				
				addList.add(data);
			}
			
        }

        if(infoList!=null&&infoList.size()>0){
        	map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
        }
         
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
        
		try {
			insertNum =  bizAddFeeService.saveorUpdateList(addList, updateList);
		} catch (Exception e) {
			if(null!=e.getMessage()&&(e.getMessage().indexOf("Duplicate entry"))>0){
				dupNum = 1;
			}
			logger.error(e.getMessage(),e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg(e.getMessage());
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;

		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
        
        if (insertNum <= 0) 
		{
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(dupNum>0){
				errorVo.setMsg("违反唯一性校验,重复的数据不会导入");
			}else{
				errorVo.setMsg("存储失败!");
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}else{
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			addList.addAll(updateList);
			new Thread(){
				public void run() {
					try {
						createFeebill(addList);
					} catch (Exception e) {
						
						logger.error("base", e);
						//写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					}
				};
			}.start();
			
			return map;
		}
		
	}
    
    private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
    
    void createFeebill(List<BizAddFeeEntity> list){
    	
    	logger.info("进入计算之前"+list.size()+"条业务数据");
    	
    	List<BizAddFeeEntity> reList = service.getStorageExtraReqVo(list);
    	
    	logger.info("进入计算之后"+reList.size()+"条业务数据");
    	
    	List<String> feesNos = new ArrayList<String>();
    	for(BizAddFeeEntity en:reList){
    		if(StringUtils.isNotBlank(en.getFeesNo())){
    			feesNos.add(en.getFeesNo());
    		}  		
    	}  	
    	//将所有的费用作废掉
    	Map<String, Object> conditionMap=new HashMap<String, Object>();
    	conditionMap.put("feesNos", feesNos);
    	if(feesNos.size()>0){
    		feesReceiveStorageService.updateByFeeNoList(conditionMap);
    	}   	
    	List<FeesReceiveStorageEntity> feeList = new  ArrayList<FeesReceiveStorageEntity>();
/*    	List<FeesReceiveStorageEntity> ufeeList = new  ArrayList<FeesReceiveStorageEntity>();
*/    	
    	
    	logger.info("进入费用时"+reList.size()+"条业务数据");
    	
    	for(BizAddFeeEntity entity:reList){
			FeesReceiveStorageEntity fee=init(entity);
			if(StringUtils.isEmpty(entity.getFeesNo())){
				//费用编号为空
				String feesNo = sequenceService.getBillNoOne("com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity", "ABF", "0000000000");
				entity.setFeesNo(feesNo);
    			fee.setFeesNo(feesNo);
			}
			feeList.add(fee); 		
    	}
    	
    	logger.info("插入费用时的数量为"+feeList.size());
    	if(feeList.size()>0){
    		feesReceiveStorageService.insertBatchTmp(feeList);
    	}
    	
  /*  	if(ufeeList.size()>0){
    		feesReceiveStorageService.updateBatch(ufeeList);
    	}*/
    	
    	bizAddFeeService.updateFee(reList);
    	
    }
    
    
    public FeesReceiveStorageEntity init(BizAddFeeEntity entity){
    	FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
    	fee.setFeesNo(entity.getFeesNo());
    	fee.setCost(new BigDecimal(entity.getPrice()));
    	fee.setIsCalculated(entity.getIsCalculated());
    	fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setUnitPrice(entity.getUnitPrice());
		fee.setSubjectCode("wh_value_add_subject");
		fee.setOtherSubjectCode(entity.getFeesType());
		fee.setOtherSubjectCode(entity.getFeesType());
		fee.setCustomerId(entity.getCustomerid());
		fee.setCustomerName(entity.getCustomerName());
		fee.setWarehouseCode(entity.getWarehouseCode());
		fee.setUnit(entity.getFeesUnit());
		fee.setQuantity(entity.getNum().intValue());
        fee.setParam1(entity.getItem());
        fee.setCustomerName(entity.getCustomerName());
        fee.setWarehouseName(entity.getWarehouseName());	
		fee.setCreateTime(entity.getCreateTime());
		fee.setWaybillNo(entity.getExternalNo());
		fee.setCreator("system");	
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setDelFlag("0");
		fee.setStatus("0");
		fee.setLastModifier(JAppContext.currentUserName());
		fee.setLastModifyTime(JAppContext.currentTimestamp());
		return fee;
    }
    
    
	@Expose
	public String reCalculate(Map<String, Object> param){
		
         final List<BizAddFeeEntity>  list  =  bizAddFeeService.queryList(param);
		 
		 if(null!=list&&list.size()>0){
			 new Thread(){
					public void run() {
						try {
							createFeebill(list);
						} catch (Exception e) {
							
							logger.error("base", e);
							//写入日志
							BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
							bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
						}
					};
				}.start();
				
				return "操作成功! 正在重算...";
		 }else{
			 return "未查询到要重算的数据...";
		 }	 
	}
	
	/**
	 * 分组统计
	 * @param param
	 * @return
	 */
	@DataProvider
	public void queryGroup(Page<BizAddFeeEntity> page, Map<String, Object> param){
		PageInfo<BizAddFeeEntity> pageInfo = bizAddFeeService.groupCount(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
}
