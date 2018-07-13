package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("bmsPackmaterialImportWmsListener")
public class BmsPackmaterialImportWmsListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsPackmaterialImportWmsListener.class);
	
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private StorageClient storageClient;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private ICustomerService customerService;
	@Resource  private IPubMaterialInfoService pubMaterialInfoService;
	@Resource  private ISystemCodeService systemCodeService;
	@Resource  private IBizOutstockPackmaterialTempService bizOutstockPackmaterialTempService;
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IBmsProductsMaterialService bmsProductsMaterialService;
	
	private static final String REMARK = "导入数据不规范,请下载查看最后一列说明";
	CellStyle cellStyle = null;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.PROCESS.getCode(),10, null, null, JAppContext.currentTimestamp());
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
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 99, null, null, JAppContext.currentTimestamp());
			return;
		}
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
		
	}
	private boolean checkStringInArr(String[] arr,String str){
		boolean f=false;
		for(String s:arr){
			if(s.equals(str)){
				f=true;
				break;
			}
		}
		return f;
	}
	private String getMapKey(Map<String,String> map,String val){
		Set<String> set=map.keySet();
		String mapKey="";
		for(String key:set){
			if(map.get(key).equals(val)){
				mapKey=key;
				break;
			}
		}
		return mapKey;
	}
	private void handImportFile(String taskId) throws Exception{
		BmsFileAsynTaskVo taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
		logger.info("领取任务 任务ID【"+taskId+"】");
		if (null == taskEntity) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.CANCEL.getCode(), 20, null, null, JAppContext.currentTimestamp());
			return;
		}
		
		String creator = taskEntity.getCreator();
		// 下载文件
		Workbook workbook=null;
		FormulaEvaluator evaluator;
		try{
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.PROCESS.getCode(), 15, null, null, JAppContext.currentTimestamp(),0,"FastDFS下载Excel");
			byte[] bytes = storageClient.downloadFile(taskEntity.getResultFilePath(), new DownloadByteArray());
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.PROCESS.getCode(), 15, null, null, JAppContext.currentTimestamp(),0,"初始化Excel");
			if(taskEntity.getResultFileName().contains("xlsx")){
				workbook=new XSSFWorkbook(new ByteArrayInputStream(bytes));
				evaluator = new XSSFFormulaEvaluator((XSSFWorkbook)workbook); 
			}else{
				workbook=new HSSFWorkbook(new ByteArrayInputStream(bytes));
				evaluator=new HSSFFormulaEvaluator((HSSFWorkbook)workbook);
			}
		}
		catch(Exception ex){
			logger.error("解析excel，返回worksheet异常",ex);
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 20, null, null, JAppContext.currentTimestamp(), 0, "获取Excel数据失败");
			return;
		}
		
		// 设置颜色
		cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.RED.index);
		
		Sheet sheet = workbook.getSheetAt(0);
		if(null == sheet){
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 20, null, null, JAppContext.currentTimestamp(), 0, "获取Excel数据失败");
		    return;
		}
		Row rowHead = sheet.getRow(0);
		int rows=sheet.getLastRowNum();//表格行数
		int cols=rowHead.getPhysicalNumberOfCells();//表格列数、
		
		Cell cellHead=rowHead.createCell(cols);
		cellHead.setCellStyle(cellStyle);
		cellHead.setCellValue("备注");
		logger.info("下载文件 文件行数【"+rows+"】");
		String[] str = {"出库日期", "仓库", "商家", "出库单号", "运单号"}; //必填列
		boolean isTemplate = checkTitle(rowHead, str);
		if (!isTemplate) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.FAIL.getCode(), 23, null, null, JAppContext.currentTimestamp(), rows, "模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号");
			logger.info("模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号");
			return;
		}
	
		logger.info("文件模板格式验证通过,表格列数【"+cols+"】");
		String taskStatus = FileAsynTaskStatusEnum.PROCESS.getCode();
		
		updateFileAsynTask(taskId, taskStatus, 20, null, null, JAppContext.currentTimestamp());
		
		List<WarehouseVo> wareHouseList =null;
		List<CustomerVo> customerList=null;
		Map<String,PubMaterialInfoVo> materialMap=null;
		String batchNum="";//批次好
		int saveNum=1000;//批量保存数量 1000行保存一次
		Map<String,String> mapHead;
		try{
			wareHouseList = warehouseService.queryAllWarehouse();
			customerList=queryAllCustomer();
			materialMap=queryAllMaterial();
			logger.info("成功获取所有仓库,商家,耗材信息 ");
			mapHead=getHeadMap();
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			//batchNum=format.format(JAppContext.currentTimestamp());
			batchNum = taskId;
			updateFileAsynTask(taskId, null, 30, null, null, JAppContext.currentTimestamp(), rows, null);
		}catch(Exception e){
			
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 30, null, null, JAppContext.currentTimestamp(), rows,null);
			logger.error("初始化数据失败,异常原因:" + e);
			return;
		}
		
		List<BizOutstockPackmaterialTempEntity> tempList=new ArrayList<BizOutstockPackmaterialTempEntity>();
		Map<Integer,String> mapRowError=Maps.newLinkedHashMap();
		//遍历Excel 所有行
		logger.info("遍历所有行,此处要改造 ");
		
		for (int rowNum = 1;  rowNum <= rows; rowNum++){
			Row row=sheet.getRow(rowNum);
			Timestamp outTime=JAppContext.currentTimestamp();
			String errorMsg="";
			WarehouseVo warehouseVo=null;
			CustomerVo customerVo=null;
			Map<String,String> mapExcel=Maps.newHashMap();
			Row xssfRow = sheet.getRow(rowNum);
			for(int colNum=0;colNum<=cols;colNum++){
				String colHeadName=getValue(rowHead.getCell(colNum),evaluator);
				if(mapHead.containsKey(colHeadName)){
					mapExcel.put(mapHead.get(colHeadName), getValue(xssfRow.getCell(colNum),evaluator));
				}
			}
			String outTimeExcel=mapExcel.get("outTime");
			String warehouseNameExcel=mapExcel.get("warehouseName");//仓库名称
			String customerNameExcel=mapExcel.get("customerName");//商家名称
			String outstockNoExcel=mapExcel.get("outstockNo");//出库单号
			String waybillNoExcel=mapExcel.get("waybillNo");//运单号
			
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
			String[] noMaterialCode={"outTime","warehouseName","customerName","outstockNo","waybillNo"};
			Set<String> set=mapExcel.keySet();
			String materialCode="";
			String materialName="";
			for(String code:set){
				//不包含count 结尾 不在noMaterialCode内
				if(!code.contains("Count")&&!checkStringInArr(noMaterialCode,code)){
					materialCode=mapExcel.get(code);
					materialName=getMapKey(mapHead,code);
					if(StringUtils.isNotBlank(materialCode)){
						if(!materialMap.containsKey(materialCode)){
							errorMsg+=materialName+"编码【"+materialCode+"】不存在;";
						}else{
							String materialCodeNum=mapExcel.get(code+"Count");//获取耗材数量/重量
							if(StringUtils.isBlank(materialCodeNum)){
								if(materialCode.endsWith("-GB")){
									errorMsg+=materialName+"重量不能为空;";
								}else{
									errorMsg+=materialName+"数量不能为空;";
								}
							}else{
								double materialNumber =0.0;
								try{
									 materialNumber = Double.valueOf(materialCodeNum);
									 if(materialNumber<=0){
											errorMsg+=materialName+"数量必须大于0;";
									 }
								}catch(Exception e){
									if(materialCode.endsWith("-GB")){
										errorMsg+=materialName+"重量数值类型不正确;";
									}else{
										errorMsg+=materialName+"数量数值类型不正确;";
									}
								}
								if(StringUtils.isBlank(errorMsg)){
									BizOutstockPackmaterialTempEntity model=getTemp(materialMap,customerVo,warehouseVo,
											outstockNoExcel,waybillNoExcel,outTime,materialCode,materialNumber,batchNum,creator);
									model.setCreateTime(outTime);
									model.setRowExcelNo(rowNum+1);
									model.setRowExcelName(materialName);
									tempList.add(model);
								}
							}
						}
					}
				}
			}

			if(tempList.size()<=0){
				errorMsg+="该运单无任何耗材;";
			}
			if(StringUtils.isNotBlank(errorMsg)){
				mapRowError.put(rowNum+1, errorMsg);
			}	
			if(rowNum==rows){
				bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
			}else{
				if(tempList.size()>=saveNum){
					bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
					tempList.clear();//释放内存
				}
			}
	
		}
		
		updateFileAsynTask(taskId, null, 40, null, null, JAppContext.currentTimestamp());

		if(tempList.size()<=0){
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 90, null, null, 
					JAppContext.currentTimestamp(), 0, "无数据需要保存");			
			return;
		}
		//验证导入数据是否存在重复数据
		logger.info("excel校验");
		checkSameData(batchNum,mapRowError);//导入Excel 数据是否存在相同数据
		queryContainsList(batchNum, mapRowError);//导入数据系统中是否存在
		Set<Integer> set=mapRowError.keySet();
		if(set!=null&&set.size()>0){
			logger.error("excel数据重复性校验-数据存储重复,保存至结果文件中");
			for(Integer rowNum:set){
				Row row=sheet.getRow(rowNum-1);
				Cell cellError=row.createCell(cols,Cell.CELL_TYPE_STRING);
				cellError.setCellStyle(cellStyle);
				cellError.setCellValue(mapRowError.get(rowNum));
			}
			replaceOldExceptionExcel(taskEntity.getResultFilePath(),taskId,batchNum,workbook);
			bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			logger.error("结果文件保存完毕,删除临时表数据,结束执行");
			return;
		}
		// 验证系统中是否存在导入数据 true  存在
		logger.info("系统数据重复性校验");
	
		
		try{
			int k=bizOutstockPackmaterialService.saveDataFromTemp(batchNum);
			if(k>0){
				logger.error("耗材从临时表写入业务表成功");
				updateFileAsynTask(taskId, null, 90, null, null, JAppContext.currentTimestamp());
				// 耗材打标
				Map<String,Object> condition = Maps.newHashMap();
				condition.put("batchNum", batchNum);
				condition.put("taskId", taskId);
				bmsProductsMaterialService.markMaterial(condition);
				updateFileAsynTask(taskId, FileAsynTaskStatusEnum.SUCCESS.getCode(), 100, null, null, JAppContext.currentTimestamp());
				bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);	
				logger.error("耗材打标成功");
			}else{
				logger.error("未从临时表中保存数据到业务表，批次号【"+batchNum+"】,任务编号【"+taskId+"】");
				updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 90, null, null, 
						JAppContext.currentTimestamp(), 0, "从临时表中保存数据到业务表失败,批次号【"+batchNum+"】");
				//bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
			}
		}catch(Exception e){
			logger.error("异步导入异常", e);
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 90, null, null, 
					JAppContext.currentTimestamp(), 0, "从临时表中保存数据到业务表异常");
			//bizOutstockPackmaterialTempService.deleteBybatchNum(batchNum);
		}
		
	}
	private boolean checkTitle(Row rowHead, String[] str) {
		if(rowHead == null){
			return false;
		}
		if(rowHead.getPhysicalNumberOfCells() < str.length){
			 return false;
		}
		for(int i = 0;i < str.length; i++){
			if(!str[i].equals(rowHead.getCell(i).getStringCellValue())){
				return false;
			}
		}	 
		return true;
	}
	private void replaceOldExceptionExcel(String fullPath,String taskId,String batchNum,Workbook workbook){
		String taskStatus = FileAsynTaskStatusEnum.EXCEPTION.getCode();
		try{
			// 删除目前的文件
			logger.info("准备删除当前文件");
			boolean resultF = storageClient.deleteFile(fullPath);
			String resultFullPath="";	
			if (resultF) {
				// 重新上传修改后的文件
				logger.info("写入Excel");
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				workbook.write(os);
				byte[] b1 = os.toByteArray();
				logger.info("上传FastDfs");
				StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
			    resultFullPath = resultStorePath.getFullPath();
				// 更新结果文件
			}
			updateFileAsynTask(taskId, taskStatus, 50, null, resultFullPath, JAppContext.currentTimestamp(), 0, REMARK);
		}catch(Exception e){
			logger.error("上传结果文件到dfs失败：", e);
			updateFileAsynTask(taskId, taskStatus, 50, null, null, JAppContext.currentTimestamp(), 0, REMARK);
		}
		
	}
	//检查相同数据 有true  无  false
	private boolean checkSameData(String batchNum,Map<Integer,String> mapRow) {
		List<BizOutstockPackmaterialTempEntity> list=bizOutstockPackmaterialTempService.querySameData(batchNum);
		
		if(list!=null&&list.size()>0){
			Map<String,BizOutstockPackmaterialTempEntity> map=Maps.newHashMap();
			for(BizOutstockPackmaterialTempEntity entity:list){
				String key=entity.getWaybillNo()+"&"+entity.getConsumerMaterialCode();
				if(map.containsKey(key)){
					String errorMsg="第"+entity.getRowExcelNo()+"行,运单号【"+entity.getWaybillNo()+"】与"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】的组合重复";
					//Row row = sheet.getRow(entity.getRowExcelNo());
					// 校验字段信息的时候已经添加了 列头【备注】
					//Cell remarkCell = row.getCell(cols);
					//remarkCell.setCellStyle(cellStyle);
					//remarkCell.setCellValue(remarkCell.getStringCellValue()+","+errorMsg);
					if(mapRow.containsKey(entity.getRowExcelNo())){
						mapRow.put(entity.getRowExcelNo(),mapRow.get(entity.getRowExcelNo())+","+errorMsg);
					}else{
						mapRow.put(entity.getRowExcelNo(),errorMsg);
					}
				}else{
					map.put(key, entity);
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 检索导入数据系统中是否存在
	 * @param batchNum
	 * @param errorCount
	 * @param xssfSheet
	 * @return
	 */
	private boolean queryContainsList(String batchNum, Map<Integer,String> mapRow) {
		List<BizOutstockPackmaterialTempEntity> list = 
			bizOutstockPackmaterialTempService.queryContainsList(batchNum);
		if(null == list || list.size() <= 0){
			return false;
		}
		Map<String,String> map=Maps.newLinkedHashMap();
		//存在重复记录
		for(BizOutstockPackmaterialTempEntity entity:list){
			String row=String.valueOf(entity.getRowExcelNo());
			String mes="";
			if(map.containsKey(row)){
				mes=map.get(row);
				mes+=","+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
				map.put(row,mes);
			}else{
				mes="系统中已存在运单号【" + entity.getWaybillNo() + "】,"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
				map.put(row,mes);
			}
		}
		Set<String> set=map.keySet();
		for(String key:set){
			Integer rowNum=Integer.valueOf(key);
			if(mapRow.containsKey(rowNum)){
				mapRow.put(rowNum, mapRow.get(rowNum)+","+map.get(key));
			}else{
				mapRow.put(rowNum, map.get(key));
			}
		}
		return true;
	}
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
			updateEntity.setRemark(remark);
			bmsFileAsynTaskService.update(updateEntity);
		} catch (Exception e) {
			updateFileAsynTask(taskId, FileAsynTaskStatusEnum.EXCEPTION.getCode(), 0, null, null, finishTime);
			logger.error("更新异步文件任务异常", e.getMessage());
		}
	}
	private void updateFileAsynTask(String taskId, String taskStatus, Integer taskRate, 
			String resultFileName, String resultFilePath, Timestamp finishTime){
		updateFileAsynTask(taskId, taskStatus, taskRate, resultFileName, resultFilePath, finishTime, 0, null);
	}
	private Map<String,String> getHeadMap(){
		Map<String,String> map=Maps.newHashMap();
		map.put("出库日期", "outTime");
		map.put("仓库", "warehouseName");
		map.put("商家", "customerName");
		map.put("出库单号", "outstockNo");
		map.put("运单号", "waybillNo");
		map.put("冰袋","bd");
		map.put("冰袋数量", "bdCount");
		map.put("纸箱", "zx");
		map.put("纸箱数量", "zxCount");
		map.put("泡沫箱", "pmx");
		map.put("泡沫箱数量", "pmxCount");
		map.put("干冰", "gb");
		map.put("干冰重量", "gbCount");
		map.put("快递袋", "kdd");
		map.put("快递袋数量", "kddCount");
		map.put("快递袋", "kdd");
		map.put("快递袋数量", "kddCount");
		map.put("面单", "md");
		map.put("面单数量", "mdCount");
		map.put("标签纸","bqz");
		map.put("标签纸数量","bqzCount");
		map.put("防水袋", "fsd");
		map.put("防水袋数量", "fsdCount");
		map.put("缓冲材料", "hccl");
		map.put("缓冲材料数量", "hcclCount");
		map.put("胶带", "jd");
		map.put("胶带数量", "jdCount");
		map.put("问候卡", "whk");
		map.put("问候卡数量", "whkCount");
		map.put("好字贴", "hzt");
		map.put("好字贴数量", "hztCount");
		map.put("其他", "qt");
		map.put("其他数量", "qtCount");
		map.put("塑封袋", "sfd");
		map.put("塑封袋数量", "sfdCount");
		map.put("保温袋", "bwd");
		map.put("保温袋数量", "bwdCount");
		map.put("保鲜袋", "bxd");
		map.put("保鲜袋数量", "bxdCount");
		return map;
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
		private BizOutstockPackmaterialTempEntity getTemp(Map<String,PubMaterialInfoVo> materialMap,CustomerVo customerVo,WarehouseVo warehouseVo,
				String outstockNoExcel,String waybillNoExcel,Timestamp outTime,String materialCode,
				double materialNumber,String batchNum, String creator){
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
			model.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
			model.setConsumerMaterialCode(materialCode);
			PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(materialCode);
			model.setConsumerMaterialName(pubMaterialInfoVo.getMaterialName());
			model.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
	  					+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
			if(materialCode.endsWith("-GB"))
			{
				model.setWeight(BigDecimal.valueOf(materialNumber));
			}else{
				model.setNum(BigDecimal.valueOf(materialNumber));
				model.setAdjustNum(BigDecimal.valueOf(materialNumber));
			}
			model.setWriteTime(JAppContext.currentTimestamp());
			model.setCreator(creator);
			model.setBatchNum(batchNum);
			return model;
		}
}
