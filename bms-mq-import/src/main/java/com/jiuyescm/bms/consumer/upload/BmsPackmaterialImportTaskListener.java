package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;


@Service("bmsPackmaterialImportTaskListener")
public class BmsPackmaterialImportTaskListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsPackmaterialImportTaskListener.class);

	@Autowired private StorageClient storageClient;
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Resource  private IBizOutstockPackmaterialTempService bizOutstockPackmaterialTempService;
	@Autowired private ICustomerService customerService;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
	@Autowired private IBmsProductsMaterialService bmsProductsMaterialService;
	
	private static final String REMARK = "导入数据不规范,请下载查看最后一列说明";
	XSSFCellStyle cellStyle = null;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.PROCESS.getCode(), 10, null, null, JAppContext.currentTimestamp());
		} catch (JMSException e1) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 10, null, null, JAppContext.currentTimestamp());
			logger.error("取出消息失败", e1);
			return;
		}
		
		try {
			logger.info("正在消费" + taskId);
			// 处理导入文件
			handImportFile(taskId);
		} catch (Exception e1) {
			logger.error("文件处理失败", e1);
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 0, null, null, JAppContext.currentTimestamp());
			return;
		}
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.error("消息应答失败",e);
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
	}
	
	/**
	 * 根据任务ID处理导入的文件
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	private void handImportFile(String taskId) throws Exception{
		BmsFileAsynTaskVo taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
		logger.info("领取任务 任务ID【"+taskId+"】");
		if (null == taskEntity) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(),20, null, null, JAppContext.currentTimestamp());
			return;
		}
		
		String creator = taskEntity.getCreator();
		// 下载文件
		byte[] bytes = storageClient.downloadFile(taskEntity.getResultFilePath(), new DownloadByteArray());
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		// 设置颜色
		cellStyle = xssfWorkbook.createCellStyle();
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.RED.index);
		
		// 校验文件
		if(null == xssfSheet){
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 20, null, null, JAppContext.currentTimestamp(), 0, "获取Excel数据失败");
			logger.error("获取获取到sheet");
			return;
		}
		
		// 最大行限制
		int lastRowNum = xssfSheet.getLastRowNum();
		// 表格头部
		XSSFRow xssfRowHead = xssfSheet.getRow(0);
		String[] str = {"出库日期", "仓库", "商家", "出库单号", "运单号"}; //必填列
		boolean isTemplate = checkTitle(xssfRowHead, str);
		if (!isTemplate) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 
								23, null, null, JAppContext.currentTimestamp(), 
								lastRowNum, "模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号");
			return;
		}
		
		// 表格列数
		int cols = xssfRowHead.getPhysicalNumberOfCells();
		if((cols-5)%2 != 0){ // 如果列数不对则 返回
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 
				26, null, null, JAppContext.currentTimestamp(), 
				lastRowNum, "表格列数不对");
			return;
		}
		
		/*int maxImportNum = getImportRowCount();
		if(lastRowNum > maxImportNum) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 
				28, null, null, JAppContext.currentTimestamp(), 
				lastRowNum, "Excel 导入数据量过大,最多能导入" + maxImportNum + "行,请分批次导入");
			return;
		}*/
		
		// 初始化数据
		List<WarehouseVo> wareHouseList = null;
		List<CustomerVo> customerList = null;
		Map<String, PubMaterialInfoVo> materialMap = null;
		int errorCount = 10;
		String batchNum = taskId; //批次号就用任务编号
		int saveNum = 1000; //批量保存数量 1000行保存一次
		try{
			wareHouseList = warehouseService.queryAllWarehouse();
			customerList = queryAllCustomer();
			materialMap = queryAllMaterial();
			errorCount = queryErrorCount();
			updateFileAsynTask(taskId, null, 30, null, null, JAppContext.currentTimestamp(), lastRowNum, null);
		}catch(Exception e){
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 
					30, null, null, JAppContext.currentTimestamp(), lastRowNum, null);
			logger.error("初始化数据失败,异常原因:" + e.getMessage());
		}
		
		List<BizOutstockPackmaterialTempEntity> tempList=new ArrayList<BizOutstockPackmaterialTempEntity>();
		
		// 校验字段信息
		String taskStatus = FileAsynTaskStatusEnum.SUCCESS.getCode();
		for (int rowNum = 1;  rowNum <= lastRowNum; rowNum++){
			StringBuffer errorMsg = new StringBuffer();
			
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			String outTimeExcel = POISXSSUtil.getValue(xssfRow.getCell(0),evaluator); // 出库时间
			String warehouseNameExcel = POISXSSUtil.getValue(xssfRow.getCell(1),evaluator); // 仓库名称
			String customerNameExcel = POISXSSUtil.getValue(xssfRow.getCell(2),evaluator); // 商家名称
			String outstockNoExcel = POISXSSUtil.getValue(xssfRow.getCell(3),evaluator); // 出库单号
			String waybillNoExcel = POISXSSUtil.getValue(xssfRow.getCell(4),evaluator); // 运单号
			
			// 新增一列，备注
			if (1 == rowNum) {
				XSSFCell remarkHeadCell = xssfRowHead.createCell(cols, XSSFCell.CELL_TYPE_STRING);
				remarkHeadCell.setCellStyle(cellStyle);
				remarkHeadCell.setCellValue("备注");
			}
			
			//验证出库时间格式
			Timestamp outTime = JAppContext.currentTimestamp();
			WarehouseVo warehouseVo=null;
			CustomerVo customerVo=null;
			if(StringUtils.isBlank(outTimeExcel)){
				errorMsg.append("出库日期为空;");
			} else{
				try{
					String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
					Date date = DateUtils.parseDate(outTimeExcel, dataPatterns);
					outTime = new Timestamp(date.getTime());
				}catch(Exception e){
					errorMsg.append("出库日期【"+outTimeExcel+"】格式不正确;");
					logger.error("出库日期【"+outTimeExcel+"】格式不正确;", e.getMessage());
				}
			}
			if(StringUtils.isBlank(outstockNoExcel)){
				errorMsg.append("出库单号为空;");
			}
			if(StringUtils.isBlank(waybillNoExcel)){
				errorMsg.append("运单号为空;");
			}
			if(StringUtils.isBlank(warehouseNameExcel)){
				errorMsg.append("仓库名称为空;");
			}else{
				warehouseVo = getWarehouseByName(wareHouseList,warehouseNameExcel);
				if(warehouseVo==null){
					errorMsg.append("仓库名称【"+warehouseNameExcel+"】不存在;");
				}
			}
			//验证商家
			if(StringUtils.isBlank(customerNameExcel)){
				errorMsg.append("商家名称为空;");
			}else{
				customerVo = getCustomerByName(customerList, customerNameExcel);
				if(null == customerVo){
					errorMsg.append("商家名称【"+customerNameExcel+"】不存在;");
				}
			}
			
			// 校验耗材
			int col = (cols - 5) / 2;
			for(int i = 0; i < col; i++){
				XSSFCell xssfCellCode = xssfRow.getCell(i * 2 + 5);
				XSSFCell xssfCellNum= xssfRow.getCell(i * 2 + 6);
				XSSFCell xssfCellHead = xssfRowHead.getCell(i * 2 + 5);
				String materialName = POISXSSUtil.getValue(xssfCellHead, evaluator);
				String materialCode = POISXSSUtil.getValue(xssfCellCode, evaluator).trim();
				String num = POISXSSUtil.getValue(xssfCellNum, evaluator);
				 
				if(StringUtils.isBlank(materialCode)){
					continue;
				}else {
					if(!materialMap.containsKey(materialCode)){
						errorMsg.append(materialName + "【"+materialCode+"】不存在;");
						continue;
					}
					if(StringUtils.isBlank(num)){
						errorMsg.append("耗材数量不能为空;");
						continue;
					}
					
					try{
						double number = Double.valueOf(num);
						if(number <= 0){
							errorMsg.append("耗材数量必须大于0;");
							continue;
						}
						
						BizOutstockPackmaterialTempEntity model=new BizOutstockPackmaterialTempEntity();
						if(null != customerVo){
							model.setCustomerId(customerVo.getCustomerid());
							model.setCustomerName(customerVo.getCustomername());
						}
						if(null != warehouseVo){
							model.setWarehouseCode(warehouseVo.getWarehouseid());
							model.setWarehouseName(warehouseVo.getWarehousename());
						}
						model.setOutstockNo(outstockNoExcel);
						model.setWaybillNo(waybillNoExcel);
						model.setDelFlag("0");// 设置为未作废
						model.setCreator(creator);
						model.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
						model.setCreateTime(outTime);
						model.setConsumerMaterialCode(materialCode);
						PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(materialCode);
						model.setConsumerMaterialName(pubMaterialInfoVo.getMaterialName());
						model.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
				  					+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
						if(materialCode.endsWith("-GB")) {
							model.setWeight(BigDecimal.valueOf(number));
						}else {
							model.setNum(BigDecimal.valueOf(number));
							model.setAdjustNum(BigDecimal.valueOf(number));
						}
						model.setWriteTime(JAppContext.currentTimestamp());
						model.setBatchNum(batchNum);
						model.setRowExcelNo(rowNum+1);
						model.setRowExcelName(materialName);
						tempList.add(model);
					}catch(Exception e){
						errorMsg.append("耗材数量【" + num + "】未非数字;");
						logger.error("耗材数量【" + num + "】未非数字;", e.getMessage());
					}
				}
			}
			
			if(tempList.size()<=0){
				errorMsg.append("该运单无任何耗材;");
			}
			
			// 是否有异常信息，有写入结果excel，无保存临时表
			if(StringUtils.isNotBlank(errorMsg)){
				taskStatus = FileAsynTaskStatusEnum.FAIL.getCode();
				// 新增一列备注记录错误原因
				XSSFCell remarkCell = xssfRow.createCell(cols, XSSFCell.CELL_TYPE_STRING);
				remarkCell.setCellStyle(cellStyle);
				remarkCell.setCellValue(errorMsg.toString());
				continue;
			}else {
				// 保存到临时表，是否有异常的判断，一旦出现异常就不写入临时表
				if(taskStatus.equals(FileAsynTaskStatusEnum.SUCCESS.getCode()) && rowNum == xssfSheet.getLastRowNum()){
					bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
					tempList.clear();//释放内存
				}else if(taskStatus.equals(FileAsynTaskStatusEnum.SUCCESS.getCode()) && tempList.size() >= saveNum){
					bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
					tempList.clear();//释放内存
				}
			}
		}
		
		updateFileAsynTask(taskId, null, 40, null, null, JAppContext.currentTimestamp());
		
		String resultFullPath = "";
		// 校验失败，删除临时数据
		if(!taskStatus.equals(FileAsynTaskStatusEnum.SUCCESS.getCode())){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			// 写到fastDFS
			resultFullPath = writeFastDFS(taskEntity.getResultFilePath(), xssfWorkbook);
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 50, null, resultFullPath, 
					JAppContext.currentTimestamp(), 0, REMARK);
			return;
		}
		
		// 验证导入数据是否存在重复数据
		if(!checkSameData(batchNum, xssfSheet)){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			// 写到fastDFS
			resultFullPath = writeFastDFS(taskEntity.getResultFilePath(), xssfWorkbook);
			// 更新状态
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 60, null, resultFullPath, 
					JAppContext.currentTimestamp(), 0, REMARK);
			return;
		}
		
		// 验证系统中是否存在导入数据
		if(!queryContainsList(batchNum, errorCount, xssfSheet)){
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			// 写到fastDFS
			resultFullPath = writeFastDFS(taskEntity.getResultFilePath(), xssfWorkbook);
			// 更新状态
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 70, null, resultFullPath, 
					JAppContext.currentTimestamp(), 0, REMARK);
			return;
		}
		
		// 写到fastDFS
		resultFullPath = writeFastDFS(taskEntity.getResultFilePath(), xssfWorkbook);
		// 更新状态
		updateFileAsynTask(taskId, null, 80, null, resultFullPath, JAppContext.currentTimestamp());
					
		// 从临时表写入到耗材表
		int k = bizOutstockPackmaterialService.saveDataFromTemp(batchNum);
		if(k > 0){
			logger.error("耗材从临时表写入业务表成功");
			updateFileAsynTask(taskId, taskStatus, 90, null, null, JAppContext.currentTimestamp());
			
			// 耗材打标
			Map<String,Object> condition = Maps.newHashMap();
			condition.put("batchNum", batchNum);
			condition.put("taskId", taskId);
			bmsProductsMaterialService.markMaterial(condition);
			logger.error("耗材打标成功");
			
			updateFileAsynTask(taskId, taskStatus, 100, null, null, JAppContext.currentTimestamp());
		}else {
			logger.error("未从临时表中保存数据到业务表，批次号【"+batchNum+"】,任务编号【"+taskId+"】");
			//bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 90, null, null, JAppContext.currentTimestamp(), 0, "从临时表中保存数据到业务表失败");
		}
	}
	
	private void updateFileAsynTask(String taskId, String taskStatus, Integer taskRate, 
			String resultFileName, String resultFilePath, Timestamp finishTime){
		updateFileAsynTask(taskId, taskStatus, taskRate, resultFileName, resultFilePath, finishTime, 0, null);
	}
	
	/**
	 * 更新异步任务
	 * @param taskId
	 * @param taskStatus
	 * @param taskRate
	 * @param resultFileName
	 * @param resultFilePath
	 * @param finishTime
	 * @param fileRows
	 * @param remark
	 */
	private void updateFileAsynTask(String taskId, String taskStatus, Integer taskRate, 
			String resultFileName, String resultFilePath, Timestamp finishTime,int fileRows, String remark){
		try {
			BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo();
			updateEntity.setTaskId(taskId);
			updateEntity.setTaskStatus(taskStatus);
			updateEntity.setTaskRate(taskRate);
			if (fileRows > 0) {
				updateEntity.setFileRows(fileRows);
			}
			updateEntity.setResultFileName(resultFileName);
			updateEntity.setResultFilePath(resultFilePath);
			updateEntity.setFinishTime(finishTime);
			if (StringUtils.isNotBlank(remark)) {
				updateEntity.setRemark(remark);
			}
			bmsFileAsynTaskService.update(updateEntity);
		} catch (Exception e) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 0, null, null, finishTime);
			logger.error("更新异步文件任务异常", e.getMessage());
		}
	}
	
	/**
	 * 写入文件到fastDFS
	 * @param filePath
	 * @param xssfWorkbook
	 * @return
	 * @throws Exception
	 */
	private String writeFastDFS(String filePath, XSSFWorkbook xssfWorkbook) throws Exception{
		String resultFullPath = "";
		// 删除目前的文件
		boolean resultF = storageClient.deleteFile(filePath);
		if (resultF) {
			// 重新上传修改后的文件
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			xssfWorkbook.write(os);
			byte[] b1 = os.toByteArray();
			StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
			resultFullPath = resultStorePath.getFullPath();
		}
		return resultFullPath;
	}
	
	private boolean checkTitle(XSSFRow xssfRow, String[] str) {
		if(xssfRow == null){
			return false;
		}
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
	
	/**
	 * 检查相同数据
	 * @param batchNum	批次号
	 * @param xssfSheet	错误信息要写回的excel
	 * @return
	 */
	private boolean checkSameData(String batchNum, XSSFSheet xssfSheet) throws Exception {
		boolean isExist = true;
		List<BizOutstockPackmaterialTempEntity> list = bizOutstockPackmaterialTempService.querySameData(batchNum);
		if(null == list || list.size() <= 0){
			return isExist;
		}
		
		Map<String,BizOutstockPackmaterialTempEntity> map = Maps.newHashMap();
		
		isExist = false;
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();//表格列数
		for(BizOutstockPackmaterialTempEntity entity : list){
			String key = entity.getWaybillNo() + "&" + entity.getConsumerMaterialCode();
			if(map.containsKey(key)){
				String errorMsg = "第" + entity.getRowExcelNo() + "行,运单号【" + entity.getWaybillNo() + 
					"】与" + entity.getRowExcelName() + "【" + entity.getConsumerMaterialCode() + "】的组合重复";
				// 将错误信息写入到excel
				XSSFRow xssfRow = xssfSheet.getRow(entity.getRowExcelNo() - 1);
				// 校验字段信息的时候已经添加了 列头【备注】
				XSSFCell remarkCell = xssfRow.createCell(cols - 1, XSSFCell.CELL_TYPE_STRING);
				remarkCell.setCellStyle(cellStyle);
				remarkCell.setCellValue(errorMsg);
			}else{
				map.put(key, entity);
			}
		}
		return isExist;
	}
	
	/**
	 * 检索导入数据系统中是否存在
	 * @param batchNum
	 * @param errorCount
	 * @param xssfSheet
	 * @return
	 */
	private boolean queryContainsList(String batchNum, int errorCount, XSSFSheet xssfSheet) throws Exception {
		boolean isExist = true;
		List<BizOutstockPackmaterialTempEntity> list = 
			bizOutstockPackmaterialTempService.queryContainsList(batchNum);
		if(null == list || list.size() <= 0){
			return isExist;
		}
		
		isExist = false;
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();//表格列数
		Map<String, String> errorMap = new HashMap<String, String>();
		for(BizOutstockPackmaterialTempEntity entity : list){
			String errorMsg = "系统中已存在运单号【" + entity.getWaybillNo() + "】," + 
				entity.getRowExcelName()+"【" + entity.getConsumerMaterialCode() + "】.";
			
			// 处理错误信息到一条记录上
			String key = String.valueOf(entity.getRowExcelNo() - 1);
			if (!errorMap.containsKey(key)) {
				errorMap.put(key, errorMsg);
			}else {
				String value = errorMap.get(key);
				errorMap.put(key, value += errorMsg);
			}
		}
		
		if (null != errorMap && !errorMap.isEmpty()) {
			for (String key : errorMap.keySet()) {
				String val = errorMap.get(key);
				// 将错误信息写入到excel
				XSSFRow xssfRow = xssfSheet.getRow(Integer.valueOf(key));
				// 校验字段信息的时候已经添加了 列头【备注】
				XSSFCell remarkCell = xssfRow.createCell(cols - 1, XSSFCell.CELL_TYPE_STRING);
				remarkCell.setCellStyle(cellStyle);
				remarkCell.setCellValue(val);
			}
		}
		return isExist;
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
	
	private Map<String,PubMaterialInfoVo> queryAllMaterial(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		Map<String,PubMaterialInfoVo> map=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo : tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				map.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		return map;
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
	
	/**
	 * 错误行数限制，超过以后直接返回
	 * @return
	 */
	private int queryErrorCount(){
		int k=10;
		try{
			SystemCodeEntity code = systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ERROR_COUNT");
			k = Integer.valueOf(code.getExtattr1());
		}catch(Exception e){
			logger.error("queryErrorCount Error:", e);
		}
		return k;
	}
	
	/**
	 * 最大导入行数
	 * @return
	 */
	private int getImportRowCount(){
		int k = 30000;
		try{
			SystemCodeEntity code = systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ROW_COUNT");
			k = Integer.valueOf(code.getExtattr1());
		}catch(Exception e){
			logger.error("getImportRowCount Error:", e);
		}
		return k;
	}
	
}
