package com.jiuyescm.bms.biz.storage.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.mq.BmsPackmaterialTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.common.enumtype.type.ExeclOperateTypeEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("bizOutstockPackmaterialImportBatchController")
public class BizOutstockPackmaterialImportBatchController {
	
	@Autowired 
	private IWarehouseService warehouseService;
	@Autowired 
	private ICustomerService customerService;
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBizOutstockPackmaterialService service;
	@Resource
	private IBizOutstockPackmaterialTempService bizOutstockPackmaterialTempService;
	@Resource
	private Lock lock;
	@Resource 
	private IRedisClient redisClient;
	@Autowired 
	private StorageClient storageClient;
	@Autowired
	private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired
	private SequenceService sequenceService;
	@Resource
	private JmsTemplate jmsQueueTemplate;
	
	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialImportBatchController.class.getName());
	
	String sessionId=JAppContext.currentUserID()+"import_materialImportprogressFlag";
	final String nameSpace="com.jiuyescm.bms.biz.storage.web.BizOutstockPackmaterialImportBatchController";
	@FileResolver
	public  Map<String, Object> importPackmaterial(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		/*
		 Map<String, Object> map=new HashMap<String, Object>();
		 map=importFile(file,parameter);
		 return map;*/
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();

		String userId=ContextHolder.getLoginUserName();
		String lockString=Tools.getMd5(userId+"BMS_QUO_IMPORT_MATERIAL_INFO");
		Map<String, Object> remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=Maps.newHashMap();
				try {
					   map=importFile(file,parameter);
					   return map;
					} catch (Exception e) {
						ErrorMessageVo errorVo = new ErrorMessageVo();
						errorVo.setMsg(e.getMessage());
						infoList.add(errorVo);		
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;
					}
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				Map<String, Object> map=Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("耗材出库明细导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(
					LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map=Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return remap;
	}
	
	public Map<String,Object> importFile(UploadFile file, Map<String, Object> parameter) throws IOException{
		setProgress("0");
		Map<String, Object> map=Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String[] str={"出库日期","仓库","商家","出库单号","运单号"};//比填列
		setProgress("1");
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		boolean isTemplate = checkTitle(xssfSheet.getRow(0),str);
		if (!isTemplate) {
			//logger.info("progress:6;");
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, 2);
		List<WarehouseVo> wareHouseList =null;
		List<CustomerVo> customerList=null;
		//List<BizOutstockPackmaterialEntity> list=null;
		Map<String,PubMaterialInfoVo> materialMap=null;
		int errorCount=10;
		String batchNum="";//批次好
		int saveNum=1000;//批量保存数量 1000行保存一次
		try{
			wareHouseList = warehouseService.queryAllWarehouse();
			customerList=queryAllCustomer();
			materialMap=queryAllMaterial();
			errorCount=queryErrorCount();
			//list=queryList(importList)
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
			batchNum=JAppContext.currentUserID()+format.format(JAppContext.currentTimestamp());
		}catch(Exception e){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"初始化数据失败,异常原因:"+e.getMessage()));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}

		
		if(xssfSheet==null){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"获取Excel数据失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		if(xssfSheet.getLastRowNum()>30000){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"Excel 导入数据量过大,最多能导入30000行,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();//表格列数
		
		if((cols-5)%2!=0){//如果列数不对则 返回
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"表格列数不对"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		setProgress("3");
		//遍历Excel 所有行
		List<BizOutstockPackmaterialTempEntity> tempList=new ArrayList<BizOutstockPackmaterialTempEntity>();
		for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++){
			String errorMsg="";
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			XSSFRow xssfRowHead=xssfSheet.getRow(0);
			String outTimeExcel=getValue(xssfRow.getCell(0),evaluator);//出库时间
			String warehouseNameExcel=getValue(xssfRow.getCell(1),evaluator);//仓库名称
			String customerNameExcel=getValue(xssfRow.getCell(2),evaluator);//商家名称
			String outstockNoExcel=getValue(xssfRow.getCell(3),evaluator);//出库单号
			String waybillNoExcel=getValue(xssfRow.getCell(4),evaluator);//运单号
			//验证出库时间格式
			Timestamp outTime=JAppContext.currentTimestamp();
			WarehouseVo warehouseVo=null;
			CustomerVo customerVo=null;
			/*
			errorMsg+=checkOutTime(outTimeExcel, outTime);//验证日期
			errorMsg+=checkOutstockNo(outstockNoExcel);//验证出库单号
			errorMsg+=checkWaybillNo(waybillNoExcel);
			errorMsg+=checkWarehouseName(warehouseNameExcel, wareHouseList, warehouseVo);*/
			if(StringUtils.isBlank(outTimeExcel)){
				errorMsg+="出库日期为空;";
			}else{
				try{
					String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
					Date date = DateUtils.parseDate(outTimeExcel, dataPatterns);
					outTime=new Timestamp(date.getTime());
				}catch(Exception e){
					errorMsg+="出库日期【"+outTimeExcel+"】格式不正确;";
				}
			}
			if(StringUtils.isBlank(outstockNoExcel)){
				errorMsg+="出库单号为空;";
			}
			if(StringUtils.isBlank(waybillNoExcel)){
				errorMsg+="运单号为空;";
			}
			if(StringUtils.isBlank(warehouseNameExcel)){
				errorMsg+="仓库名称为空;";
			}else{
				warehouseVo=getWarehouseByName(wareHouseList,warehouseNameExcel);
				if(warehouseVo==null){
					errorMsg+="仓库名称【"+warehouseNameExcel+"】不存在;";
				}
			}
			//验证商家
			if(StringUtils.isBlank(customerNameExcel)){
				errorMsg+="商家名称为空;";
			}else{
				customerVo=getCustomerByName(customerList, customerNameExcel);
				if(customerVo==null){
					errorMsg+="商家名称【"+customerNameExcel+"】不存在;";
				}
			}
			int col = (cols-5)/2;
			
			for(int i = 0;i<col;i++){
				 XSSFCell xssfCellCode = xssfRow.getCell(i*2+5);
				 XSSFCell xssfCellNum= xssfRow.getCell(i*2+6);
				 XSSFCell xssfCellHead=xssfRowHead.getCell(i*2+5);
				 String materialName=getValue(xssfCellHead, evaluator);
				 String materialCode = getValue(xssfCellCode, evaluator).trim();
				 String num = getValue(xssfCellNum, evaluator);
				 if(!StringUtils.isBlank(materialCode)){//非空
					 if(!materialMap.containsKey(materialCode)){
						errorMsg+=""+materialName+"【"+materialCode+"】不存在;";
					 }else{
						if(StringUtils.isBlank(num)){
							errorMsg+="耗材数量不能为空;";
						}else{
							try{
								double number = Double.valueOf(num);
								if(number<=0){
									errorMsg+="耗材数量必须大于0;";
								}else{
									BizOutstockPackmaterialTempEntity model=new BizOutstockPackmaterialTempEntity();
									if(customerVo!=null){
										model.setCustomerId(customerVo.getCustomerid());
										model.setCustomerName(customerVo.getCustomername());
									}
									if(warehouseVo!=null){
										model.setWarehouseCode(warehouseVo.getWarehouseid());
										model.setWarehouseName(warehouseVo.getWarehousename());
									}
									model.setOutstockNo(outstockNoExcel);
									model.setWaybillNo(waybillNoExcel);
									model.setDelFlag("0");// 设置为未作废
									model.setCreator(JAppContext.currentUserName());
									model.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
									model.setCreateTime(outTime);
									model.setConsumerMaterialCode(materialCode);
									PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(materialCode);
									model.setConsumerMaterialName(pubMaterialInfoVo.getMaterialName());
									model.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
							  					+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
									if(materialCode.endsWith("-GB"))
									{
										model.setWeight(BigDecimal.valueOf(number));
									}else{
										model.setNum(BigDecimal.valueOf(number));
										model.setAdjustNum(BigDecimal.valueOf(number));
									}
									model.setWriteTime(JAppContext.currentTimestamp());
									model.setCreator(JAppContext.currentUserName());
									model.setBatchNum(batchNum);
									model.setRowExcelNo(rowNum+1);
									model.setRowExcelName(materialName);
									tempList.add(model);
									
								}
							}catch(Exception e){
								errorMsg+="耗材数量【"+num+"】未非数字;";
							}
						}
					 }
				 }
			}
			if(!StringUtils.isBlank(errorMsg)){
				if(infoList.size()>=errorCount){
					break;
				}else{
					errorMsg="第"+(rowNum+1)+"行,"+errorMsg;
					infoList.add(new ErrorMessageVo(rowNum+1,errorMsg));
				}
			}else{
				if(rowNum==xssfSheet.getLastRowNum()){
					bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
					//DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, "正在备份数据("+rowNum+"/"+xssfSheet.getLastRowNum()+")................");
					setProgress("正在备份数据("+rowNum+"/"+xssfSheet.getLastRowNum()+")................");
				}else{
					if(tempList.size()>=saveNum){
						bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
						//DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, "正在备份数据("+rowNum+"/"+xssfSheet.getLastRowNum()+")................");
						setProgress("正在备份数据("+rowNum+"/"+xssfSheet.getLastRowNum()+")................");
						tempList.clear();//释放内存
					}
				}
			}
		}
	
		setProgress("正在处理数据。。。。。。。。。。。。");
		if(infoList!=null&&infoList.size()>0){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			setProgress("6");
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		//验证导入数据是否存在重复数据
		if(!checkSameData(batchNum,infoList)){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			setProgress("6");
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		//验证系统中是否存在导入数据
		List<BizOutstockPackmaterialTempEntity> list=queryContainsList(batchNum,errorCount);
		if(list!=null&&list.size()>0){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);	
			for(BizOutstockPackmaterialTempEntity entity:list){
				infoList.add(new ErrorMessageVo(entity.getRowExcelNo(),"系统中已存在运单号【"+entity.getWaybillNo()+"】,"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】."));
			}
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			setProgress("6");
			return map;
		}
		try{
			setProgress("4");
			int k=service.saveDataFromTemp(batchNum);
			if(k>0){
				setProgress("5");
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "导入数据成功");
				return map;
			}else{
				bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
				setProgress("6");
				infoList.add(new ErrorMessageVo(1,"耗材明细数据库写入失败"));
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}catch(Exception e){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"耗材明细数据库写入异常,异常原因:"+e.getMessage()));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
	}
	
	@FileResolver
	public  Map<String, Object> importPackmaterialAsyn(final UploadFile file, final Map<String, Object> parameter) throws Exception {
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();

		String userId=ContextHolder.getLoginUserName();
		String lockString=Tools.getMd5(userId + "BMS_QUO_IMPORT_MATERIAL_BATCH_ASYN");
		Map<String, Object> remap = lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map = Maps.newHashMap();
				try {
				   map = importFileAsyn(file,parameter);
				   return map;
				} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg(e.getMessage());
					infoList.add(errorVo);		
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("耗材出库明细导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return remap;
	}
	private double getMaxFileSize(){
		double fileSize=50.0;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_FILE_SIZE");
			fileSize=Double.valueOf(code.getExtattr1());
		}catch(Exception e){
			logger.info("未配置系统参数IMPORT_FILE_SIZE");
			System.out.println("未配置系统参数IMPORT_FILE_SIZE");
		}
		return fileSize;
	}
	/**
	 * 异步导入处理文件
	 * @param file
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private Map<String,Object> importFileAsyn(UploadFile file, Map<String, Object> parameter) throws Exception{
		setProgress("0");
		Map<String, Object> map = Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		setProgress("1");
		
		String fileName = file.getFileName();
		// 校验文件名称
		if (!checkRegFileName(fileName)) {
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel文件名称【"+fileName+"】不符合规范,请参考【上海01仓201805耗材导入-1.xlsx】"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 校验文件大小
		double maxFileSize=getMaxFileSize();
		double importFileSize=BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024*1024)).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(importFileSize>maxFileSize){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel 导入文件过大,最多能导入"+maxFileSize+"M,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 保存导入文件到fastDFS，获取文件路径
		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), "xlsx");
		StorePath resultStorePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), "xlsx");
		String fullPath = storePath.getFullPath();
		String resultFullPath = resultStorePath.getFullPath();
		if (StringUtils.isBlank(fullPath) || StringUtils.isBlank(resultFullPath)) {
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据上传文件系统失败，请稍后重试"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 生成任务，写入任务表
		String taskId =sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
		
		BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
		taskEntity.setTaskId(taskId);
		taskEntity.setTaskName(fileName.substring(0, fileName.lastIndexOf(".")));
		taskEntity.setTaskRate(0);
		taskEntity.setTaskStatus(FileAsynTaskStatusEnum.WAIT.getCode());
		taskEntity.setTaskType(BmsPackmaterialTaskTypeEnum.IMPORTBATCH.getCode());
		taskEntity.setBizType(ExeclOperateTypeEnum.IMPORT.getCode());
//		taskEntity.setFileRows(xssfSheet.getLastRowNum());
		taskEntity.setOriginFileName(fileName);
		taskEntity.setOriginFilePath(fullPath);
		taskEntity.setResultFileName(fileName);
		taskEntity.setResultFilePath(resultFullPath);
		taskEntity.setCreatorId(JAppContext.currentUserID());
		taskEntity.setCreator(JAppContext.currentUserName());
		taskEntity.setCreateTime(JAppContext.currentTimestamp());
		int saveNum = bmsFileAsynTaskService.save(taskEntity);
		if (saveNum <= 0) {
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据生成任务失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 写入MQ
		final String msg = taskId;
		jmsQueueTemplate.send(BmsPackmaterialTaskTypeEnum.IMPORTBATCH.getCode(), new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
		
		setProgress("5");
		map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "操作成功");
		return map;
	}
	
	//检索导入数据系统中是否存在
	private List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum, int errorCount) {
		return bizOutstockPackmaterialTempService.queryContainsList(batchNum,errorCount);
	}
	
	/**
	 * 校验文件名称
	 * @param fileName
	 * @return
	 */
	private boolean checkRegFileName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return false;
		}
		
		// 上海仓201805耗材导入-01.xlsx
		String regEx = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+仓[0-9]{6}耗材导入[-0-9]*(.xls|.xlsx)$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}

	private boolean checkTitle(XSSFRow xssfRow, String[] str) {
		if(xssfRow == null){
			return false;
		}
//		XSSFRow xssfRow = xssfSheet.getRow(0);
		if(xssfRow.getPhysicalNumberOfCells() < str.length){
			 return false;
		}
		for(int i = 0;i < str.length; i++){
			if(!str[i].equals(xssfRow.getCell(i).getStringCellValue())){
				return false;
			}
		}	 
		return true;
	}

	//检查相同数据
	private boolean checkSameData(String batchNum, List<ErrorMessageVo> infoList) {
		List<BizOutstockPackmaterialTempEntity> list=bizOutstockPackmaterialTempService.querySameData(batchNum);
		int errorCount=10;
		try{
			errorCount=queryErrorCount();
		}catch(Exception e){
			
		}
		if(list!=null&&list.size()>0){
			Map<String,BizOutstockPackmaterialTempEntity> map=Maps.newHashMap();
			for(BizOutstockPackmaterialTempEntity entity:list){
				String key=entity.getWaybillNo()+"&"+entity.getConsumerMaterialCode();
				if(map.containsKey(key)){
					String errorMsg="第"+entity.getRowExcelNo()+"行,运单号【"+entity.getWaybillNo()+"】与"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】的组合重复";
					if(infoList.size()>=errorCount){
						break;
					}
					infoList.add(new ErrorMessageVo(map.get(key).getRowExcelNo(),errorMsg));
				}else{
					map.put(key, entity);
				}
			}
			return false;
		}
		return true;
	}

	private boolean checkDataExist(String waybillNoExcel, String materialCode) {
		return service.checkDataExist(waybillNoExcel,materialCode);
	}

	private CustomerVo getCustomerByName(List<CustomerVo> list,String customerName){
		CustomerVo vo=null;
		for(CustomerVo customerVo:list){
			if(customerVo.getCustomername().equals(customerName)||
			    customerVo.getShortname().equals(customerName)){
				vo=customerVo;
				break;
			}
		}
		return vo;
	}
	
	private WarehouseVo getWarehouseByName(List<WarehouseVo> list,String warehouseName){
		WarehouseVo vo=null;
		for(WarehouseVo voEntity:list){
			if(voEntity.getWarehousename().trim().equals(warehouseName.trim())){
				vo=voEntity;
				break;
			}
		}
		return vo;
	}
	
	private List<CustomerVo> queryAllCustomer(){
		List<CustomerVo> list=new ArrayList<CustomerVo>();
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					list.add(customer);
				}
			}
		}
		return list;
	}
	
	private Map<String,PubMaterialInfoVo> queryAllMaterial(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		Map<String,PubMaterialInfoVo> map=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				map.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		return map;
	}
	
	/**
	 * 错误行数限制，超过以后直接返回
	 * @return
	 */
	private int queryErrorCount(){
		int k=10;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ERROR_COUNT");
			k = Integer.valueOf(code.getExtattr1());
		}catch(Exception e){
			logger.error("queryErrorCount Error:", e);
		}
		return k;
	}
	
	 /**  
     * @获取Excel中某个单元格的值 
     * @param cell      EXCLE单元格对象 
     * @param evaluator EXCLE单元格公式 
     * @return          单元格内容 
     */  
    private String getValue(Cell cell,FormulaEvaluator evaluator) {   
          
        String value = "";  
        if(cell==null)
        	return value;
        switch (cell.getCellType()) {  
            case HSSFCell.CELL_TYPE_NUMERIC:                        //数值型  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {       //如果是时间类型  
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                    value = format.format(cell.getDateCellValue());  
                } else { 
                	cell.setCellType(Cell.CELL_TYPE_STRING);  
                	String temp = cell.getStringCellValue();  
					if(temp.indexOf(".")>-1){  
						value = String.valueOf(new Double(temp)).trim();  
					}else{  
						value = temp.trim();  
					}  
                	 //纯数字  
                    value = cell.toString();
                }  
                break;  
            case HSSFCell.CELL_TYPE_STRING:                         //字符串型  
                value = cell.getStringCellValue();  
                break;  
            case HSSFCell.CELL_TYPE_BOOLEAN:                        //布尔  
                value = " " + cell.getBooleanCellValue();  
                break;  
            case HSSFCell.CELL_TYPE_BLANK:                          //空值  
                value = "";  
                break;  
            case HSSFCell.CELL_TYPE_ERROR:                          //故障  
                value = "";  
                break;  
            case HSSFCell.CELL_TYPE_FORMULA:                        //公式型  
                try {  
                    CellValue cellValue;  
                    cellValue = evaluator.evaluate(cell);  
                    switch (cellValue.getCellType()) {              //判断公式类型  
                        case Cell.CELL_TYPE_BOOLEAN:  
                            value  = String.valueOf(cellValue.getBooleanValue());  
                            break;  
                        case Cell.CELL_TYPE_NUMERIC:  
                            // 处理日期    
                            if (DateUtil.isCellDateFormatted(cell)) {    
                               SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");    
                               Date date = cell.getDateCellValue();    
                               value = format.format(date);  
                            } else {    
                               value  = String.valueOf(cellValue.getNumberValue());  
                            }  
                            break;  
                        case Cell.CELL_TYPE_STRING:  
                            value  = cellValue.getStringValue();  
                            break;  
                        case Cell.CELL_TYPE_BLANK:  
                            value = "";  
                            break;  
                        case Cell.CELL_TYPE_ERROR:  
                            value = "";  
                            break;  
                        case Cell.CELL_TYPE_FORMULA:  
                            value = "";  
                            break;  
                    }  
                } catch (Exception e) {  
                    value = cell.getStringCellValue().toString();  
                    cell.getCellFormula(); 
                    e.printStackTrace();
                }  
                break;  
            default:  
                value = cell.getStringCellValue().toString();  
                break;  
        }  
        return value;  
    }
    
    private void setProgress(String progress){
		redisClient.set(sessionId, nameSpace, progress, 60*60);
    	/*
		DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, progress);*/
	}
    
    /**
	 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
	 * @return
	 */
    @Expose
	public String getProgress(){
    	if(redisClient.exists(sessionId, nameSpace)){
    		return redisClient.get(sessionId, nameSpace, null);
    	}else{
    		return "";
    	}
    	/*
    	Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute(sessionId);
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId,"");
			return "";
		}
		return progressFlag.toString(); */
	}
    
	/**
	 *重置处理进度
	 */
    @Expose
	public void resetProgress() {
    	redisClient.del(sessionId, nameSpace);
    	/*
    	DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId,"");*/
	}
}
