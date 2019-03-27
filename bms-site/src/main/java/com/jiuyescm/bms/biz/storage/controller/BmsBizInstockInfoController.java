package com.jiuyescm.bms.biz.storage.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.service.IBizInStockService;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockRecordService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.common.vo.ExportDataVoEntity;
import com.jiuyescm.bms.common.web.HttpCommanExport;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.common.utils.upload.InstockInfoTemplateDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsBizInstockInfoController")
public class BmsBizInstockInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BmsBizInstockInfoController.class.getName());

	@Autowired
	private IBmsBizInstockInfoService bmsBizInstockInfoService;
	@Resource
	private Lock lock;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IBmsBizInstockRecordService bmsBizInstockRecordService;
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Autowired 
	private IBizInStockService service;
	@Resource 
	private SequenceService sequenceService;
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;

	private static final String FEE_1 = "wh_instock_work";
	private static final String FEE_2 = "wh_b2c_handwork";
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsBizInstockInfoEntity> page, Map<String, Object> param) {
		PageInfo<BmsBizInstockInfoEntity> pageInfo = bmsBizInstockInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(BmsBizInstockInfoEntity entity) {
		String username = JAppContext.currentUserName();
		String userId = JAppContext.currentUserID();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		if (null == entity.getId()) {
			entity.setDelFlag("0");
			entity.setIsCalculated("0");
			entity.setCreator(username);
			entity.setCreateTime(currentTime);
			bmsBizInstockInfoService.save(entity);
		} else {
			entity.setLastModifier(username);
			entity.setLastModifierId(userId);
			entity.setLastModifyTime(currentTime);
			bmsBizInstockInfoService.update(entity);
			List<String> subjectList = new ArrayList<>();
			subjectList.add(entity.getSubjectCode());
			sendMq(subjectList);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsBizInstockInfoEntity entity) {
		entity.setDelFlag("1");
		bmsBizInstockInfoService.delete(entity);
	}
	
	/**
	 * 批量更新
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importUpdate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		String lockString=Tools.getMd5("BMS_QUO_IMPORT_INSTOCK_UPDATE"+JAppContext.currentUserName());
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=new HashMap<String, Object>();
				try {
					map=importUpdateWeightLock(file,parameter);
				} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg("系统错误:"+e.getMessage());
					   //写入日志
				     BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
					 bmsErrorLogInfoEntity.setClassName("BmsBizInstockInfoController");
					 bmsErrorLogInfoEntity.setMethodName("importUpdate");
					 bmsErrorLogInfoEntity.setIdentify("进入锁之前异常");
					 bmsErrorLogInfoEntity.setErrorMsg(e.toString());
					 bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
					 bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				}
				return map;
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("宅配导入功能已被其他用户占用，请稍后重试；");
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
	
	@FileResolver
	public Map<String, Object> importUpdateWeightLock(UploadFile file, Map<String, Object> parameter){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBizInstockInfoController");
			bmsErrorLogInfoEntity.setMethodName("importUpdateWeightLock");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		
		if(xssfSheet==null)
			return null;
		
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
		
		if(cols>4){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid=JAppContext.currentLoginName();
		String username = JAppContext.currentUserName();
		
		List<BmsBizInstockInfoEntity> infoLists = new ArrayList<BmsBizInstockInfoEntity>();
		Map<String, Object> dataMap = new HashMap<>();
		//重算（修改费用）List
		List<BmsBizInstockInfoEntity> feeList = new ArrayList<>();
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	Map<String, Object> condition = new HashMap<>();
        	Map<String,Object> map0 = new HashMap<String,Object>();
        	BmsBizInstockInfoEntity entity = new BmsBizInstockInfoEntity();
        	
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			
			String instockNo = getCellValue(xssfRow.getCell(0));
			String adjustQty=getCellValue(xssfRow.getCell(1));
			String adjustBox=getCellValue(xssfRow.getCell(2));
			String adjustWeight=getCellValue(xssfRow.getCell(3));
			// 入库单号（必填）
			if(StringUtils.isEmpty(instockNo)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行入库单号为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			
			//入库单号重复性校验
			if (dataMap.containsKey(instockNo)) {
				setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行入库单号重复！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}else {
				dataMap.put(instockNo, xssfRow);
			}
			
			//调整数量、箱数、重量都为空
			if(StringUtils.isBlank(adjustQty) && 
					StringUtils.isBlank(adjustBox) && 
					StringUtils.isBlank(adjustWeight)){
				// 除instockNo外，其他更新字段都为空
				setMessage(infoList, rowNum+1,"没有需要调整的内容！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			// 调整数量
			if(StringUtils.isNotBlank(adjustQty)) {
				boolean isNumber = ExportUtil.isNumber(adjustQty);
				if(!isNumber) {
					int lieshu = rowNum + 1;
					setMessage(infoList, rowNum+1,"第"+lieshu+"行非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				}else{
					map0.put("adjustQty", adjustQty);
				}
			}else{
				map0.put("adjustQty", BigDecimal.ZERO);
			}
			
			// 调整重量
			if(StringUtils.isNotBlank(adjustWeight)) {
				boolean isNumber = ExportUtil.isNumber(adjustWeight);
				if(!isNumber) {
					int lieshu = rowNum + 1;
					setMessage(infoList, rowNum+1,"第"+lieshu+"行非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				}else{
					map0.put("adjustWeight", adjustWeight);
				}
			}else {
				map0.put("adjustWeight", BigDecimal.ZERO);
			}
			
			// 调整箱数
			if(StringUtils.isNotBlank(adjustBox)) {
				boolean isNumber = ExportUtil.isNumber(adjustBox);
				if(!isNumber) {
					int lieshu = rowNum + 1;
					setMessage(infoList, rowNum+1,"第"+lieshu+"行非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				}else{
					map0.put("adjustBox", adjustBox);
				}
			}else {
				map0.put("adjustBox", BigDecimal.ZERO);
			}
			
			if (map.size() != 0) {
				continue;
			}
			
			map0.put("instockNo", instockNo);
			map0.put("isCalculated", "99");
			map0.put("lastModifier", username);
			map0.put("lastModifierId", userid);
			map0.put("lastModifyTime", nowdate);
			list.add(map0);
			//组装数据
			condition.put("instockNo", instockNo);
			List<BmsBizInstockInfoEntity> lists = bmsBizInstockInfoService.query(condition);
			
			//校验入库单号
			if(CollectionUtils.isEmpty(lists)){
				setMessage(infoList, rowNum+1,"入库单号不存在："+instockNo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			BmsBizInstockInfoEntity infoEntity = lists.get(0);
			
			entity.setInstockNo(instockNo);
			if (StringUtils.isBlank(adjustBox)) {
				entity.setAdjustBox(0d);
			}else {
				entity.setAdjustBox(Double.valueOf(adjustBox));
			}
			if (StringUtils.isBlank(adjustQty)) {
				entity.setAdjustQty(0d);
			}else {
				entity.setAdjustQty(Double.valueOf(adjustQty));
			}
			if (StringUtils.isBlank(adjustWeight)) {
				entity.setAdjustWeight(0d);
			}else {
				entity.setAdjustWeight(Double.valueOf(adjustWeight));
			}
			entity.setIsCalculated("99");
			entity.setDelFlag(infoEntity.getDelFlag());
			entity.setCalculateTime(infoEntity.getCalculateTime());
			entity.setRemark(infoEntity.getRemark());
			entity.setLastModifier(username);
			entity.setLastModifierId(userid);
			entity.setLastModifyTime(nowdate);
			infoLists.add(entity);
			
			//重算的费用
			BmsBizInstockInfoEntity fee1 = new BmsBizInstockInfoEntity();
			fee1.setFeesNo(infoEntity.getFeesNo());
			fee1.setSubjectCode("wh_instock_work");
			BmsBizInstockInfoEntity fee2 = new BmsBizInstockInfoEntity();
			fee2.setFeesNo(infoEntity.getFeesNo());
			fee2.setSubjectCode("wh_b2c_handwork");
			feeList.add(fee1);
			feeList.add(fee2);
        }
        if (map.size() != 0) {
			return map;
		}
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
        	//更新业务表
			num = bmsBizInstockInfoService.updateBatch(list);
	        //修改费用表
	        bmsBizInstockInfoService.reCalculate(feeList);
	        //发送MQ
			List<String> subjectList = new ArrayList<String>();
			subjectList.add(FEE_1);
			subjectList.add(FEE_2);
			sendMq(subjectList);
		} catch (Exception e) {
		     message = e.getMessage();
		     logger.error(e.getMessage(), e);
		     //写入日志
		     BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			 bmsErrorLogInfoEntity.setClassName("BmsBizInstockInfoController");
			 bmsErrorLogInfoEntity.setMethodName("importUpdateWeightLock");
			 bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			 bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			 bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
        if(num==0){
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(message!=null){
				errorVo.setMsg(message);
			}else{
				errorVo.setMsg("更新失败!");
				errorVo.setLineNo(2);
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			
			return map;
        }else{
        	//批量写入记录表
        	bmsBizInstockRecordService.saveBatch(infoLists);
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
        }
        
	}
	
	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {

		if (cell == null)
			return "";

		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

			return cell.getStringCellValue();

		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

			return String.valueOf(cell.getBooleanCellValue());

		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

			return cell.getCellFormula();

		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("#.####");
			return String.valueOf(df.format(cell.getNumericCellValue()));

		}
		return "";
	}
	
	private void setMessage(List<ErrorMessageVo> errorList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		errorList.add(errorVo);
	}
	
	/**
	 * 批量更新模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplateUpdate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/instock_updatebatch_template.xlsx");
		return new DownloadFile("入库信息更新模板.xlsx", is);
	}
	
	/**
	 * 导出报价
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile downLoadData(Map<String,Object> parameter) throws Exception{
		try{
			String path=getPath();
			HttpCommanExport commanExport=new HttpCommanExport(path);
			ExportDataVoEntity voEntity=new ExportDataVoEntity();
			voEntity.setTitleName("入库单信息");
			voEntity.setBaseType(new InstockInfoTemplateDataType());
			voEntity.setDataList(getDataList(parameter));
			return commanExport.exportFile(voEntity);
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			throw e;
		}
		
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_INSTOCK_INFO");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_INSTOCK_INFO");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	private List<Map<String,Object>> getDataList(Map<String,Object> parameter){

		List<BmsBizInstockInfoEntity> list=bmsBizInstockInfoService.query(parameter);
	
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		List<FeesReceiveStorageEntity> feesList = null;
		for(BmsBizInstockInfoEntity entity:list){
	        Map<String, String> calculateMap = CalculateState.getMap();
			feesList = feesReceiveStorageService.queryByFeesNo(entity.getFeesNo());
			if (null == feesList || feesList.size() < 1) {
				
			}else if (feesList.size() == 1 && "wh_instock_work".equals(feesList.get(0).getSubjectCode())) {
				map.put("instockWorkFee", feesList.get(0).getCost());
				map.put("instockWorkFeeCalStatus", calculateMap.get(feesList.get(0).getIsCalculated()));
				map.put("instockWorkFeeRemark", feesList.get(0).getCalcuMsg());
			}else if(feesList.size() == 1 && "wh_b2c_handwork".equals(feesList.get(0).getSubjectCode())){
				map.put("instockXHFee", feesList.get(0).getCost());
				map.put("instockXHFeeCalStatus", calculateMap.get(feesList.get(0).getIsCalculated()));
				map.put("instockXHFeeRemark", feesList.get(0).getCalcuMsg());
			}else {
				for (FeesReceiveStorageEntity feesReceiveStorageEntity : feesList) {
					if ("wh_instock_work".equals(feesReceiveStorageEntity.getSubjectCode())) {
						map.put("instockWorkFee", feesReceiveStorageEntity.getCost());
						map.put("instockWorkFeeCalStatus", calculateMap.get(feesReceiveStorageEntity.getIsCalculated()));
						map.put("instockWorkFeeRemark", feesReceiveStorageEntity.getCalcuMsg());
					}
					if ("wh_b2c_handwork".equals(feesReceiveStorageEntity.getSubjectCode())) {
						map.put("instockXHFee", feesReceiveStorageEntity.getCost());
						map.put("instockXHFeeCalStatus", calculateMap.get(feesReceiveStorageEntity.getIsCalculated()));
						map.put("instockXHFeeRemark", feesReceiveStorageEntity.getCalcuMsg());
					}
				}
			}
			map=new HashMap<String,Object>();
			map.put("customerName", entity.getCustomerName());
			map.put("warehouseName", entity.getWarehouseName());
			map.put("instockNo", entity.getInstockNo());
			map.put("externalNum", entity.getExternalNum());
			map.put("instockDate", entity.getInstockDate());
			map.put("receiver", entity.getReceiver());
			map.put("instockType", entity.getInstockType());
			map.put("totalQty", entity.getTotalQty());
			map.put("totalBox", entity.getTotalBox());
			map.put("totalWeight", entity.getTotalWeight());
			map.put("adjustQty", entity.getAdjustQty());
			map.put("adjustBox", entity.getAdjustBox());
			map.put("adjustWeight", entity.getAdjustWeight());
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
        try {
        	String taskid = sequenceService.getBillNoOne(FileExportTaskEntity.class.getName(), "FT", "0000000000");
    		if (StringUtils.isBlank(taskid)) {
    			throw new Exception("生成导出文件编号失败,请稍后重试!");
    		}
        	
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_INSTOCK_INFO.getCode() + taskid + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("createTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("createEndTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_INSTOCK_INFO.getDesc());
        	entity.setTaskType(FileTaskTypeEnum.BIZ_INSTOCK_INFO.getCode());
        	entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
        	entity.setProgress(0d);
        	entity.setFilePath(filepath);
        	entity.setCreator(JAppContext.currentUserName());
        	entity.setCreateTime(JAppContext.currentTimestamp());
        	entity.setDelFlag(ConstantInterface.DelFlag.NO);
        	entity = fileExportTaskService.save(entity);
        	
        	//生成账单文件
    		final Map<String, Object> condition = param;
    		final String taskId = entity.getTaskId();
    		final String filePath=filepath;
    		new Thread(){
    			public void run() {
    				try {
    					export(condition, taskId,filePath);
    				} catch (Exception e) {
    					fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
    					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
    					//写入日志
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
    					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "启动线程失败", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_INSTOCK_INFO.getDesc() + MessageConstant.EXPORT_TASK_BIZ_MSG;
	}
	
	/**
	 * 获取应收业务数据导出的文件路径
	 * @return
	 */
	public String getBizReceiveExportPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_INSTOCK_INFO");
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 获取系统参数对象
	 * @param typeCode
	 * @param code
	 * @return
	 */
	public SystemCodeEntity getSystemCode(String typeCode, String code){
		if (StringUtils.isNotEmpty(typeCode) && StringUtils.isNotEmpty(code)) {
			SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode(typeCode, code);
			if(systemCodeEntity == null){
				throw new BizException("请在系统参数中配置文件上传路径,参数" + typeCode + "," + code);
			}
			return systemCodeEntity;
		}
		return null;
	}
	
	/**
	 * 异步导出
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void export(Map<String, Object> param,String taskId,String filePath)throws Exception{
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====入库单信息导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====入库单信息导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //干线运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====入库单信息导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		if("ALL".equals(myparam.get("isCalculated"))){
			myparam.put("isCalculated", "");
		}
		
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BmsBizInstockInfoEntity> pageInfo = 
					bmsBizInstockInfoService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getBizHead(); 
			List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_INSTOCK_INFO.getDesc(), 
					lineNo, headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}
	
	public List<Map<String, Object>> getBizHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收货确认时间");
        itemMap.put("columnWidth", 35);
        itemMap.put("dataKey", "instockDate");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收货人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiver");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalQty");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalBox");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustQty");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustBox");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库操作费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockWorkFee");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库操作费计算状态");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "instockWorkFeeCalStatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库操作费计算备注");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "instockWorkFeeRemark");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库卸货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockXHFee");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库卸货费计算状态");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "instockXHFeeCalStatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库卸货费计算备注");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "instockXHFeeRemark");
        headInfoList.add(itemMap);
		
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BmsBizInstockInfoEntity> list) {

		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		//List<FeesReceiveStorageEntity> feesList = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Map<String, Object>> dataMap = new HashMap<>();
		try {
			for (BmsBizInstockInfoEntity entity : list) {
				map = new HashMap<String, Object>();
				Map<String, String> calculateMap = CalculateState.getMap();
				if (dataMap.containsKey(entity.getInstockNo())) {
					if ("wh_instock_work".equals(entity.getSubjectCode())) {
						dataMap.get(entity.getInstockNo()).put("instockWorkFee", entity.getCost());
						dataMap.get(entity.getInstockNo()).put("instockWorkFeeCalStatus", calculateMap.get(entity.getIsCalculated()));
						dataMap.get(entity.getInstockNo()).put("instockWorkFeeRemark", entity.getCalcuMsg());
					} else if ( "wh_b2c_handwork".equals(entity.getSubjectCode())) {
						dataMap.get(entity.getInstockNo()).put("instockXHFee", entity.getCost());
						dataMap.get(entity.getInstockNo()).put("instockXHFeeCalStatus", calculateMap.get(entity.getIsCalculated()));
						dataMap.get(entity.getInstockNo()).put("instockXHFeeRemark", entity.getCalcuMsg());
					}
				}else {
					if ("wh_instock_work".equals(entity.getSubjectCode())) {
						map.put("instockWorkFee", entity.getCost());
						map.put("instockWorkFeeCalStatus", calculateMap.get(entity.getIsCalculated()));
						map.put("instockWorkFeeRemark", entity.getCalcuMsg());
					} else if ( "wh_b2c_handwork".equals(entity.getSubjectCode())) {
						map.put("instockXHFee", entity.getCost());
						map.put("instockXHFeeCalStatus", calculateMap.get(entity.getIsCalculated()));
						map.put("instockXHFeeRemark", entity.getCalcuMsg());
					}
					map.put("customerName", entity.getCustomerName());
					map.put("warehouseName", entity.getWarehouseName());
					map.put("instockNo", entity.getInstockNo());
					map.put("externalNum", entity.getExternalNum());
					map.put("instockDate", sdf.format(entity.getInstockDate()));
					map.put("receiver", entity.getReceiver());
					map.put("instockType", entity.getInstockType());
					map.put("totalQty", entity.getTotalQty());
					map.put("totalBox", entity.getTotalBox());
					map.put("totalWeight", entity.getTotalWeight());
					map.put("adjustQty", entity.getAdjustQty());
					map.put("adjustBox", entity.getAdjustBox());
					map.put("adjustWeight", entity.getAdjustWeight());
					mapList.add(map);
					dataMap.put(entity.getInstockNo(), map);
				}
			}
		} catch (Exception e) {
			logger.error("日期转换异常！", e);
		}
		
		return mapList;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		List<BmsBizInstockInfoEntity> list = bmsBizInstockInfoService.query(param);
		if (CollectionUtils.isEmpty(list)) {
			return "没有数据重算";
		}
		//更改费用计算状态为99
		if(bmsBizInstockInfoService.reCalculate(list) == 0){
			return "重算异常";
		}
		List<String> subjectList = new ArrayList<String>();
		if(param.containsKey("subjectCode")){
			subjectList.add((String) param.get("subjectCode"));
		}else{
			subjectList.add("wh_instock_work");
			subjectList.add("wh_b2c_handwork");
		}
		sendMq(subjectList);
		return "操作成功! 正在重算...";
	}
	
	/**
	 * 分组统计
	 * @param param
	 * @return
	 */
	@DataProvider
	public void queryGroup(Page<BmsBizInstockInfoEntity> page, Map<String, Object> param){
		PageInfo<BmsBizInstockInfoEntity> pageInfo = bmsBizInstockInfoService.groupCount(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	private void  sendMq(List<String> subjectList) {
		Map<String, Object> sendTaskMap = new HashMap<String, Object>();
		sendTaskMap.put("isCalculated", "99");
		sendTaskMap.put("subjectList", subjectList);
		// 对这些费用按照商家、科目、时间排序
		List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryByMap(sendTaskMap);
		for (BmsCalcuTaskVo vo : list) {
			vo.setCrePerson(JAppContext.currentUserName());
			vo.setCrePersonId(JAppContext.currentUserID());
			vo.setCreTime(JAppContext.currentTimestamp());
			try {
				bmsCalcuTaskService.sendTask(vo);
				logger.info("mq发送，商家id为----{0}，业务年月为----{0}，科目id为---{0}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
			} catch (Exception e) {
				logger.info("mq任务失败：商家id为----{0}，业务年月为----{0}，科目id为---{0}，错误信息：{0}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode(),e);
			}
		}
	}
}
