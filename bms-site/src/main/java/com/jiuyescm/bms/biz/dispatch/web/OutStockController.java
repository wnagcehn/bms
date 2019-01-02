package com.jiuyescm.bms.biz.dispatch.web;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Convert;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.api.IDispatchService;
import com.jiuyescm.bms.biz.api.IOutstockRecordService;
import com.jiuyescm.bms.biz.api.IOutstockService;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;
import com.jiuyescm.bms.biz.vo.OutstockRecordVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;

@Controller("outStockController")
public class OutStockController {
	
	private static final Logger logger = Logger.getLogger(OutStockController.class.getName());
	
	@Resource
	private IDispatchService dispatchService;
	
	@Resource
	private IOutstockService outstockService;
	
	@Resource
	private IOutstockRecordService outstockRecordService;
	
	@Autowired
	private IFeesReceiveDispatchService service;
	
	@Resource 
	private ICarrierService carrierService;	
	
	@Resource
	private IDeliverService deliverService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private Lock lock;
	
	@Autowired 
	private IAddressService omsAddressService;
	
	@Resource
	private IBizDispatchBillService bizDispatchBillService;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BmsDispatchVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<BmsDispatchVo> pageInfo = dispatchService.queryAll(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public BmsDispatchVo queryOne( Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}	
		BmsDispatchVo vo = dispatchService.queryOne(param);
		return vo;
	}
	
	/**
	 * 更新
	 * @param datas
	 * @return
	 */
	@DataResolver
	public String updateEntity(BmsDispatchVo data){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
				//判断是否生成费用，判断费用的状态是否为未过账
				if(StringUtils.isNotBlank(data.getFeesNo())){
					Map<String,Object> aCondition=new HashMap<>();
					aCondition.put("feesNo", data.getFeesNo());
					FeesReceiveDispatchEntity feesReceiveDispatchEntity = service.queryOne(aCondition);
					if (null != feesReceiveDispatchEntity) {
						//获取此时的费用状态
						String status=String.valueOf(feesReceiveDispatchEntity.getStatus());
						if("1".equals(status)){
							return "该费用已过账，无法调整重量";
						}
					}
				}
				//此为修改业务数据  
				//业务数据有生成过费用，且没有过账的话,则允许调整重量,且调整完后,状态重置为未计算
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				data.setIsCalculated("99");
				data.setLastModifier(userid);
				data.setLastModifyTime(nowdate);
				outstockService.updateBizData(data);			
		} catch (Exception e) {
			logger.error("更新失败:");
			return "更新失败：" + e.getMessage();
		}
		return "更新成功";
	}
	
	/**
	 * 记录分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryRecord(Page<OutstockRecordVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<OutstockRecordVo> pageInfo = outstockRecordService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 更新模板下载
	 * @param parameter
	 * @return
	 * @throws IOException 
	 */
	@FileProvider
	public DownloadFile downloadTemplateUpdate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/dispatch/dispatch_waybill_template_update.xlsx");
		return new DownloadFile("配送运单调整重量更新模板.xlsx", is);
	}
	
	/**
	 * 批量更新
	 * @param file
	 * @param parameter
	 * @return
	 */
	@FileResolver
	public Map<String, Object> importUpdateWeightLock(UploadFile file, Map<String, Object> parameter){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		Map<String, Object> map = new HashMap<String, Object>();
			
		//获取所有的物流商信息
		List<CarrierVo> carrierList=carrierService.queryAllCarrier();
		//获取所有的宅配商信息
		List<DeliverVo> deliverList=deliverService.queryAllDeliver();
		
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillController");
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
		
		if(cols>7){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		
		//List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<BmsDispatchVo> bdvList = new ArrayList<>();
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid=JAppContext.currentUserName();
		
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	//Map<String,Object> map0 = new HashMap<String,Object>();
        	BmsDispatchVo bmsDispatchVo = new BmsDispatchVo();
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			String waybillNo = getCellValue(xssfRow.getCell(0));
			String adjustProvince=getCellValue(xssfRow.getCell(1));
			String adjustCity=getCellValue(xssfRow.getCell(2));
			String adjustDistrict=getCellValue(xssfRow.getCell(3));
			String adjustWeight=getCellValue(xssfRow.getCell(4));
			String adjustCarrier = getCellValue(xssfRow.getCell(5));
			String adjustDeliver = getCellValue(xssfRow.getCell(6));
			// 运单号（必填）
			if(StringUtils.isEmpty(waybillNo)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列运单号为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
						
			//调整省市区、调整重量、物流商、宅配商都为空
			if(StringUtils.isBlank(adjustProvince) && 
					StringUtils.isBlank(adjustCity) && 
					StringUtils.isBlank(adjustDistrict) &&
					StringUtils.isBlank(adjustWeight) && 
					StringUtils.isBlank(adjustCarrier) && 
					StringUtils.isBlank(adjustDeliver)){
				// 除waybillNo外，其他更新字段都为空
				setMessage(infoList, rowNum+1,"没有需要调整的内容！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				// 调整省市区
				if(StringUtils.isNotBlank(adjustProvince) && 
						StringUtils.isNotBlank(adjustCity) && 
						StringUtils.isNotBlank(adjustDistrict)){
					RegionVo vo = new RegionVo(replaceChar(adjustProvince), replaceChar(adjustCity),replaceChar(adjustDistrict));
					RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
					if(StringUtils.isBlank(matchVo.getProvince())&&
							StringUtils.isBlank(matchVo.getCity())&&
							StringUtils.isBlank(matchVo.getDistrict())){
						String address=adjustProvince+"-"+adjustCity+"-"+adjustDistrict;
						setMessage(infoList, rowNum+1,"未匹配到地址:"+address+"");
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;
					}else{
						bmsDispatchVo.setAdjustReceiveProvince(matchVo.getProvince());
						bmsDispatchVo.setAdjustReceiveCity(matchVo.getCity());
						bmsDispatchVo.setAdjustReceiveArea(matchVo.getDistrict());
					}	
				}
				
				// 调整重量
				if(StringUtils.isNotBlank(adjustWeight)) {
					boolean isNumber = ExportUtil.isNumber(adjustWeight);
					if(!isNumber) {
						int lieshu = rowNum + 1;
						setMessage(infoList, rowNum+1,"第"+lieshu+"列非数字类型数据！");
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;
					}else{
						bmsDispatchVo.setAdjustWeight(new BigDecimal(adjustWeight));
					}
				}
				
				// 校验物流商名称
				if(StringUtils.isNotBlank(adjustCarrier)){
					for(CarrierVo entity:carrierList){
						if(entity.getName().equals(adjustCarrier)){
							bmsDispatchVo.setAdjustCarrierId(entity.getCarrierid());
							bmsDispatchVo.setAdjustCarrierName(adjustCarrier);
							break;
						}
					}
					if(bmsDispatchVo.getAdjustCarrierId()==null){
						setMessage(infoList, rowNum+1,"物流商名称"+adjustCarrier+"没有在物流商表中维护!");
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;			
					}
				}
				
				// 校验宅配商名称
				if(StringUtils.isNotBlank(adjustDeliver)){
					for(DeliverVo entity:deliverList){
						if(entity.getDelivername().equals(adjustDeliver)){
							bmsDispatchVo.setAdjustDeliverId(entity.getDeliverid());
							bmsDispatchVo.setAdjustDeliverName(adjustDeliver);
							break;
						}
					}
					
					if(bmsDispatchVo.getAdjustDeliverId()==null){
						setMessage(infoList, rowNum+1,"宅配商名称"+adjustDeliver+"没有在宅配商表中维护!");
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;	
					}
				}
			}
			
			//是否过账
			boolean isGz = validateWillNo(ExportUtil.replaceBlank(waybillNo));
			if(isGz){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列已过账的数据不能修改！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				bmsDispatchVo.setWaybillNo(ExportUtil.replaceBlank(waybillNo));
				bmsDispatchVo.setIsCalculated("99");
				bmsDispatchVo.setLastModifier(userid);
				bmsDispatchVo.setLastModifyTime(nowdate);
				bdvList.add(bmsDispatchVo);
			}
        }
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
        	//批量更新
        	num = outstockService.updateBizDataBatch(bdvList);
		} catch (Exception e) {
		     message = e.getMessage();
		     logger.error(e.getMessage(), e);
		     //写入日志
		     BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			 bmsErrorLogInfoEntity.setClassName("DispatchBillController");
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
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
        }
	}
	
	private void setMessage(List<ErrorMessageVo> errorList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		errorList.add(errorVo);
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
	
	private String replaceChar(String str){
		if(StringUtils.isNotBlank(str)){
			String[] arr={" ","\\","/",",",".","-"};
			for(String a:arr){
				str=str.replace(a, "");
			}
		}
		return str;
	}
	
	boolean validateWillNo(String willNo)
	{
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("waybillNo", willNo);
		int num = bizDispatchBillService.queryDispatch(param);
		if(num>0)
			return true;
		return false;
	}
		
}
