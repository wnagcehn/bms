package com.jiuyescm.bms.biz.storage.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialImportEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("bizOutstockPackmaterialImportAllController")
public class BizOutstockPackmaterialImportAllController {
	
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
	private Lock lock;
	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialImportAllController.class.getName());
	
	String sessionId=ContextHolder.getLoginUserName()+"_import_materialImportprogressFlag";
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

	public Map<String,Object> importFile(UploadFile file, Map<String, Object> parameter) throws IOException{
		setProgress("0");//开始验证模板
		logger.info("progress:0;");
		Map<String, Object> map=Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		List<BizOutstockPackmaterialEntity> list=new ArrayList<BizOutstockPackmaterialEntity>();
		String[] str={"出库日期","仓库","商家","出库单号","运单号"};//比填列
		setProgress("1");//开始验证模板
		logger.info("progress:1;");
		boolean isTemplate = ExportUtil.checkTitle(file,str);
		if (!isTemplate) {
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		setProgress("2");//初始化数据
		logger.info("progress:2;");
		List<WarehouseVo> wareHouseList =null;
		List<CustomerVo> customerList=null;
		Map<String,PubMaterialInfoVo> materialMap=null;
		int errorCount=10;
		try{
			wareHouseList = warehouseService.queryAllWarehouse();
			customerList=queryAllCustomer();
			materialMap=queryAllMaterial();
			errorCount=queryErrorCount();
		}catch(Exception e){
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"初始化数据失败,异常原因:"+e.getMessage()));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		
		if(xssfSheet==null){
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"获取Excel数据失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		/*
		if(xssfSheet.getLastRowNum()>10000){
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"Excel 导入数据量过大,最多能导入10000行,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}*/
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();//表格列数
		
		if((cols-5)%2!=0){//如果列数不对则 返回
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"表格列数不对"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		setProgress("3");//验证数据
		logger.info("progress:3;");
		//遍历Excel 所有行
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
				errorMsg+="运单号为空;";
			}
			if(StringUtils.isBlank(waybillNoExcel)){
				errorMsg+="出库单号为空;";
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
									BizOutstockPackmaterialEntity model=new BizOutstockPackmaterialEntity();
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
										model.setWeight(Double.valueOf(num));
									}else{
										model.setNum(Double.valueOf(num));
										model.setAdjustNum(Double.valueOf(num));
									}
									model.setWriteTime(JAppContext.currentTimestamp());
									model.setCreator(JAppContext.currentUserName());
									model.setRowExcelNo(rowNum+1);
									model.setRowExcelName(materialName);
									list.add(model);
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
			}
		}
		if(infoList!=null&&infoList.size()>0){
			setProgress("6");
			logger.info("progress:6;");
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		//Excel 转换实体验证数据是否重复
		try{
			List<BizOutstockPackmaterialEntity> outstockMaterialList=queryList(list);
			Map<String,BizOutstockPackmaterialEntity> importMaterialMap=Maps.newHashMap();
			for(BizOutstockPackmaterialEntity entity:list){
				String errorMsg="";
				String key=entity.getWaybillNo()+","+entity.getConsumerMaterialCode();
				if(importMaterialMap.containsKey(key)){
					errorMsg+="第"+importMaterialMap.get(key).getRowExcelNo()+"行与第"+entity.getRowExcelNo()+"行，运单号与"+entity.getRowExcelName()+"的组合重复";
				}else{
					importMaterialMap.put(key, entity);
				}
				for(BizOutstockPackmaterialEntity dataEntity:outstockMaterialList){
					String dataKey=dataEntity.getWaybillNo()+","+dataEntity.getConsumerMaterialCode();
					if(key.equals(dataKey)){
						errorMsg+="第"+entity.getRowExcelNo()+"行,系统中已存在运单号和"+entity.getRowExcelName()+"的组合"+dataKey;
						break;
					}
				}
				if(!StringUtils.isBlank(errorMsg)){
					if(infoList.size()>=errorCount){
						break;
					}else{
						infoList.add(new ErrorMessageVo(entity.getRowExcelNo(),errorMsg));
					}
				}
			}
		}catch(Exception e){
			infoList.add(new ErrorMessageVo(1,"验证数据重复性发生了异常,异常信息:"+e.getMessage()));
		}
		if(infoList!=null&&infoList.size()>0){
			setProgress("6");
			logger.info("progress:6;");
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		try{
			setProgress("4");
			logger.info("progress:4;");
			int k=service.saveList(list);
			if(k>0){
				setProgress("5");
				logger.info("progress:5;");
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "导入数据成功");
				return map;
			}else{
				setProgress("6");
				logger.info("progress:6;");
				infoList.add(new ErrorMessageVo(1,"耗材明细数据库写入失败"));
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}catch(Exception e){
			setProgress("6");
			logger.info("progress:6;");
			infoList.add(new ErrorMessageVo(1,"耗材明细数据库写入异常,异常原因:"+e.getMessage()));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
	}
	private List<BizOutstockPackmaterialEntity> queryList(
			List<BizOutstockPackmaterialEntity> importList) {
		List<String> waybillNoList=new ArrayList<String>();
		for(BizOutstockPackmaterialEntity entity:importList){
			waybillNoList.add(entity.getWaybillNo());
		}
		List<BizOutstockPackmaterialEntity> list=service.queryAllByWaybillNoList(waybillNoList);
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
	private int queryErrorCount(){
		int k=10;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ERROR_COUNT");
			k=Integer.valueOf(code.getExtattr1());
		}catch(Exception e){
			
		}
		return k;
	}
	
	private void setProgress(Object progress){
		logger.info("set:"+sessionId+":"+progress.toString());
		ContextHolder.getHttpSession().setAttribute(sessionId, progress);
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
    	Object progressFlag = ContextHolder.getHttpSession().getAttribute(sessionId);
		if (progressFlag == null){
			ContextHolder.getHttpSession().setAttribute(sessionId, "");
			logger.info("get:"+sessionId+ ";value: ");
			return "";
		}
		logger.info("get:"+sessionId+ ";value: "+progressFlag.toString());
		return progressFlag.toString(); 
	}
	/**
	 *重置处理进度
	 */
    @Expose
	public void resetProgress() {
    	ContextHolder.getHttpSession().setAttribute(sessionId, "0");
	}
}
