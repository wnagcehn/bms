package com.jiuyescm.bms.biz.storage.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
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
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("bizOutstockPackmaterialImportWmsController")
public class BizOutstockPackmaterialImportWmsController {
	
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
	@Resource
	private JmsTemplate jmsQueueTemplate;
	
	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialImportWmsController.class.getName());
	
	String sessionId=JAppContext.currentUserID()+"_import_materialImportWmsFlag";
	final String nameSpace="com.jiuyescm.bms.biz.storage.controller.BizOutstockPackmaterialImportWmsController";
	@FileResolver
	public  Map<String, Object> importPackmaterial(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String userId=JAppContext.currentUserID();
		String lockString=Tools.getMd5(userId+"BMS_QUO_IMPORT_MATERIAL_INFO");
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

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
	public Map<String,Object> importFile(UploadFile file, Map<String, Object> parameter) throws IOException{
		setProgress("0");//开始验证模板
		Map<String, Object> map=Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String[] str={"出库日期","仓库","商家","出库单号","运单号"};//比填列
		setProgress("1");//开始验证模板
		Workbook workbook=null;
		Sheet sheet;
		FormulaEvaluator evaluator;
		if(file.getFileName().contains("xlsx")){
			workbook=new XSSFWorkbook(file.getInputStream());
			evaluator = new XSSFFormulaEvaluator((XSSFWorkbook)workbook); 
		}else{
			workbook=new HSSFWorkbook(file.getInputStream());
			evaluator=new HSSFFormulaEvaluator((HSSFWorkbook)workbook);
		}
		sheet = workbook.getSheetAt(0);
		
		if(sheet==null){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"Excel读取失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		Row rowHead = sheet.getRow(0);
		int cols=rowHead.getPhysicalNumberOfCells();//表格列数
		int rows=sheet.getLastRowNum();//表格行数
		
		if(rows>30000){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"Excel 导入数据量过大,最多能导入30000行,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		if(cols<str.length){
			setProgress("6");
			infoList.add(new ErrorMessageVo(1,"模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		//检查表格必须列 {"出库日期","仓库","商家","出库单号","运单号"}
		for(int i=0;i<str.length;i++){
			boolean f=false;
			for(int j=0;j<cols;j++){
				String val=getValue(rowHead.getCell(j), evaluator);
				if(val.equals(str[i])){
					f=true;
					break;
				}
			}
			if(!f){
				setProgress("6");
				infoList.add(new ErrorMessageVo(1,"模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号"));
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}
		setProgress("2");//初始化数据
		List<WarehouseVo> wareHouseList =null;
		List<CustomerVo> customerList=null;
		Map<String,PubMaterialInfoVo> materialMap=null;
		int errorCount=10;
		String batchNum="";//批次好
		int saveNum=1000;//批量保存数量 1000行保存一次
		Map<String,String> mapHead;
		try{
			wareHouseList = warehouseService.queryAllWarehouse();
			customerList=queryAllCustomer();
			materialMap=queryAllMaterial();
			errorCount=queryErrorCount();
			mapHead=getHeadMap();
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
			batchNum=JAppContext.currentUserID()+format.format(JAppContext.currentTimestamp());
		}catch(Exception e){
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"初始化数据失败,异常原因:"+e.getMessage()));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		setProgress("3");//验证数据
		//验证出库时间格式
		List<BizOutstockPackmaterialTempEntity> tempList=new ArrayList<BizOutstockPackmaterialTempEntity>();

		//遍历Excel 所有行
		for (int rowNum = 1;  rowNum <= rows; rowNum++){
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
											outstockNoExcel,waybillNoExcel,outTime,materialCode,materialNumber,batchNum);
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
			
			if(StringUtils.isNotBlank(errorMsg)){
				if(infoList.size()>=errorCount){
					break;
				}else{
					errorMsg="第"+(rowNum+1)+"行,"+errorMsg;
					infoList.add(new ErrorMessageVo(rowNum+1,errorMsg));
				}
			}else{
				if(infoList.size()==0){
					if(rowNum==rows){
						bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
						setProgress("正在备份数据("+rowNum+"/"+rows+")................");
					}else{
						if(tempList.size()>=saveNum){
							bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
							setProgress("正在备份数据("+rowNum+"/"+rows+")................");
							tempList.clear();//释放内存
						}
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
	
	
	
	//检索导入数据系统中是否存在
	private List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum, int errorCount) {
		return bizOutstockPackmaterialTempService.queryContainsList(batchNum,errorCount);
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
	private BizOutstockPackmaterialTempEntity getTemp(Map<String,PubMaterialInfoVo> materialMap,CustomerVo customerVo,WarehouseVo warehouseVo,
			String outstockNoExcel,String waybillNoExcel,Timestamp outTime,String materialCode,
			double materialNumber,String batchNum){
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
		model.setCreator(JAppContext.currentUserName());
		model.setBatchNum(batchNum);
		return model;
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
	private int queryErrorCount(){
		int k=10;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ERROR_COUNT");
			k=Integer.valueOf(code.getExtattr1());
		}catch(Exception e){
			
		}
		return k;
	}
	/**
	 * 过期时间 1小时
	 * @param progress
	 */
	private void setProgress(String progress){
		redisClient.set(sessionId, nameSpace, progress, 60*60);
		/*
		DoradoContext.getAttachedRequest().getSession().setAttribute(sessionId, progress);*/
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
