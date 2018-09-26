package com.jiuyescm.bms.base.reportCustomer.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
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
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.reportCustomer.service.IReportWarehouseCustomerService;
import com.jiuyescm.bms.base.reportCustomer.vo.ReportWarehouseCustomerVo;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("reportCustomerController")
public class ReportCustomerController {
	
	private static final Logger logger = Logger.getLogger(ReportCustomerController.class.getName());

	
	@Autowired
	private IReportWarehouseCustomerService reportWarehouseCustomerService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired 
	private ICustomerService customerService;
	
	@Resource private ISystemCodeService systemCodeService;
	
	/**
	 * 查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<ReportWarehouseCustomerVo> page,Map<String, Object> param){
		if(param==null){
			param=Maps.newHashMap();
		}
		PageInfo<ReportWarehouseCustomerVo> tmpPageInfo = new PageInfo<ReportWarehouseCustomerVo>();
		List<ReportWarehouseCustomerVo> list=reportWarehouseCustomerService.query(param);
		if (tmpPageInfo != null) {
			page.setEntities(list);
			page.setEntityCount((int) list.size());
		}
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo save(ReportWarehouseCustomerVo entity){
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(entity == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			//重复性校验
			//根据仓库精确匹配
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("createMonth", entity.getCreateMonth());
			condition.put("customerId", entity.getCustomerId());
			condition.put("warehouseCode", entity.getWarehouseCode());
			condition.put("bizType", entity.getBizType());
			ReportWarehouseCustomerVo vo=reportWarehouseCustomerService.queryOne(condition);
			if(vo!=null){
				return new ResponseVo(ResponseVo.FAIL, "商家仓库当前月份已存在");
			}
					
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			reportWarehouseCustomerService.save(entity);
			
		}catch(Exception e){
			logger.error("新增失败,失败原因:"+e.getMessage());
			return new ResponseVo(ResponseVo.FAIL, "新增失败");
		}
		return new ResponseVo(ResponseVo.SUCCESS, "新增成功");
	}
	
	/**
	 * 更新
	 * @param temp
	 * @return
	 */
	@DataResolver
	public ResponseVo update(ReportWarehouseCustomerVo temp) {
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(temp == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			
			Map<String,Object> condition=new HashMap<String,Object>();
			//根据仓库精确匹配
			condition.put("createMonth", temp.getCreateMonth());
			condition.put("customerId", temp.getCustomerId());
			condition.put("warehouseCode", temp.getWarehouseCode());
			condition.put("bizType", temp.getBizType());
			ReportWarehouseCustomerVo vo=reportWarehouseCustomerService.queryOne(condition);
			if(vo!=null && vo.getId()!=null && !vo.getId().equals(temp.getId())){
				return new ResponseVo(ResponseVo.FAIL, "商家仓库当前月份已存在");
			}
			
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			reportWarehouseCustomerService.update(temp);
			
		}catch(Exception e){
			logger.error("更新失败,失败原因:"+e.getMessage());
			return new ResponseVo(ResponseVo.FAIL, "更新失败");
		}
		return new ResponseVo(ResponseVo.SUCCESS, "更新成功");	
	}
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	@DataResolver
	public ResponseVo updateList(List<ReportWarehouseCustomerVo> list) {
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(list == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			for(ReportWarehouseCustomerVo vo:list){
				vo.setDelFlag("1");
				vo.setLastModifier(JAppContext.currentUserName());
				vo.setLastModifyTime(JAppContext.currentTimestamp());
			}
				
			reportWarehouseCustomerService.updateList(list);
			
		}catch(Exception e){
			logger.error("批量删除失败,失败原因:"+e.getMessage());
			return new ResponseVo(ResponseVo.FAIL, "批量删除失败");
		}
		return new ResponseVo(ResponseVo.SUCCESS, "批量删除成功");
	}
	
	/**
	 * 批量复制
	 * @param list
	 * @return
	 */
	@DataResolver
	public String copyList(List<ReportWarehouseCustomerVo> list) {
		try{
			if(Session.isMissing()){
				return MessageConstant.SESSION_INVALID_MSG;
			}else if(list == null){
				return MessageConstant.PAGE_PARAM_ERROR_MSG;
			}
			
			//判断数据是否已存在
			for(ReportWarehouseCustomerVo vo:list){
				vo.setCreateMonth(vo.getDate());
				//重复性校验
				//根据仓库精确匹配
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("createMonth", vo.getCreateMonth());
				condition.put("customerId", vo.getCustomerId());
				condition.put("warehouseCode", vo.getWarehouseCode());
				condition.put("bizType", vo.getBizType());
				ReportWarehouseCustomerVo re=reportWarehouseCustomerService.queryOne(condition);
				if(re!=null){
					return "复制的数据已存在";
				}
			}
			
			for(ReportWarehouseCustomerVo vo:list){
				vo.setCreateTime(JAppContext.currentTimestamp());
				vo.setCreator(JAppContext.currentUserName());
				vo.setLastModifier(JAppContext.currentUserName());
				vo.setLastModifyTime(JAppContext.currentTimestamp());
				vo.setDelFlag("0");
			}
			reportWarehouseCustomerService.saveList(list);
			
		}catch(Exception e){
			logger.error("批量复制失败,失败原因:"+e.getMessage());
			return MessageConstant.OPERATOR_FAIL_MSG;
		}
		return "SUCC";	
	}
	
	@DataProvider
	public Map<String,String> getImportType(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("1", "应导入");
		mapValue.put("0", "免导入");
		return mapValue;
	}
	
	/**
	 * 免商家导入导入模板
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile getTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/customer_import_template.xlsx");
		return new DownloadFile("免商家导入导入模板.xlsx", is);
	}

	
	@FileResolver
	public Map<String, Object> importExcel(UploadFile file, Map<String, Object> parameter){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel读取失败!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		
		if(xssfSheet==null)
			return null;
		
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
		
		if(cols>6){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		
		Timestamp nowdate = JAppContext.currentTimestamp();
		String username = JAppContext.currentUserName();
		
		List<ReportWarehouseCustomerVo> infoLists = new ArrayList<ReportWarehouseCustomerVo>();
		
		Map<String,Integer> checkMap=new HashMap<>();
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		//获取所有的商家名称
		List<CustomerVo> customerList=getCustomerList();
		//所有的业务类型
		Map<String,String> bizMap=getBizMap();
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	
        	ReportWarehouseCustomerVo  entity=new  ReportWarehouseCustomerVo();
        	int lieshu = rowNum + 1;
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				setMessage(infoList, rowNum+1,"第"+lieshu+"列空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
		
			String createMonth = getCellValue(xssfRow.getCell(0));
			String warehouseName=getCellValue(xssfRow.getCell(1));
			String customerName=getCellValue(xssfRow.getCell(2));
			String importType=getCellValue(xssfRow.getCell(3));
			String bizType=getCellValue(xssfRow.getCell(4));
			String remark=getCellValue(xssfRow.getCell(5));
			// 设置日期（必填）
			if(StringUtils.isEmpty(createMonth)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列设置日期不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			// 仓库名称（必填）
			if(StringUtils.isEmpty(warehouseName)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列仓库名称不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}else{
				//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填			
				for(WarehouseVo vo:wareHouselist){
					if(vo.getWarehousename().equals(warehouseName)){
						entity.setWarehouseCode(vo.getWarehouseid());
						entity.setWarehouseName(warehouseName);
						break;
					}
				}
				
				if(StringUtils.isBlank(entity.getWarehouseCode())){
					setMessage(infoList, rowNum+1,"仓库名称"+warehouseName+"没有在仓库表中维护!");
				}
			}
			// 商家（必填）
			if(StringUtils.isEmpty(customerName)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列商家不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}else{
				//=======================校验商家名称开始=======================
				for(CustomerVo vo:customerList){
					if(vo.getCustomername().equals(customerName)){
						entity.setCustomerId(vo.getCustomerid());
						entity.setCustomerName(customerName);
						break;
					}
				}
				if(StringUtils.isBlank(entity.getWarehouseCode())){
					setMessage(infoList, rowNum+1,"商家名称"+customerName+"没有在商家表中维护!");
				}
			}
			
			// 导入类型（必填）
			if(StringUtils.isEmpty(importType)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列导入类型不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}else{
				if("应导入".equals(importType)){
					entity.setImportType("1");
				}else if("免导入".equals(importType)){
					entity.setImportType("0");
				}else{
					setMessage(infoList, rowNum+1,"第"+lieshu+"列导入类型不存在！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					continue;
				}
			}
			// 业务类型（必填）
			if(StringUtils.isEmpty(bizType)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列业务类型不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}else{
				if(bizMap.containsKey(bizType)){
					entity.setBizType(bizMap.get(bizType));
				}
			}
			
			//Excel重复性校验
			if(checkMap.containsKey(createMonth+"&"+warehouseName+"&"+customerName+"&"+importType+"&"+bizType+"&"+remark)){				
				setMessage(infoList, rowNum+1,"第"+lieshu+"行数据和第"+checkMap.get(createMonth+"&"+warehouseName+"&"+customerName+"&"+importType+"&"+bizType+"&"+remark)+"行数据重复");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				checkMap.put(createMonth+"&"+warehouseName+"&"+customerName+"&"+importType+"&"+bizType+"&"+remark, lieshu);
				continue;
			}else{
				checkMap.put(createMonth+"&"+warehouseName+"&"+customerName+"&"+importType+"&"+bizType+"&"+remark, lieshu);
			}
			
			//数据表重复校验
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("createMonth", createMonth);
			condition.put("warehouseName", warehouseName);
			condition.put("customerName", customerName);
			condition.put("bizType", bizType);
			ReportWarehouseCustomerVo data=reportWarehouseCustomerService.queryOne(condition);
			if(data!=null){
				setMessage(infoList, rowNum+1,"第"+lieshu+"行数据在表中已存在");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			
			entity.setCreateMonth(createMonth);
			entity.setRemark(remark);
			entity.setCreator(username);
			entity.setCreateTime(nowdate);
			entity.setLastModifier(username);
			entity.setLastModifyTime(nowdate);
			entity.setDelFlag("0");
			infoLists.add(entity);
		}
 
        //如果有错误信息
        if(map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR)!=null){
        	return map;
        }
         
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
			num = reportWarehouseCustomerService.saveList(infoLists);
		} catch (Exception e) {
		     message = e.getMessage();
		     logger.error("更新失败", e);
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
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
        }
	}
	
	
	/**
	 * 所有的商家信息
	 */
	public List<CustomerVo> getCustomerList(){
		//获取所有的商家名称
		PageInfo<CustomerVo> customerList=customerService.query(null, 0, Integer.MAX_VALUE);
		List<CustomerVo> cList=customerList.getList();
		return cList;	
	}
	@DataProvider
	public Map<String, String> getBizMap() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("CUSTOMER_BIZTYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCodeName(),code.getCode());
		}
		return mapValue;
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
	
	
	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
    
	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
}
