package com.jiuyescm.bms.biz.pallet.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
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
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoService;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.tool.Tools;
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
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bizPalletInfoController")
public class BizPalletInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BizPalletInfoController.class.getName());

	@Autowired
	private IBizPalletInfoService bizPalletInfoService;
	@Resource
	private Lock lock;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Autowired private IWarehouseService warehouseService;
	@Resource private ISystemCodeService systemCodeService;
	@Autowired private ICustomerService customerService;


	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizPalletInfoEntity> page, Map<String, Object> param) {
		PageInfo<BizPalletInfoEntity> pageInfo = bizPalletInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存/更新
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(BizPalletInfoEntity entity) {
		String username = JAppContext.currentUserName();
		String userid = JAppContext.currentUserID();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		if (null == entity.getId()) {
			entity.setCreator(username);
			entity.setCreatorId(userid);
			entity.setCreateTime(currentTime);
			entity.setDelFlag("0");
			entity.setIsCalculated("0");
			bizPalletInfoService.save(entity);
		} else {
			entity.setLastModifier(username);
			entity.setLastModifierId(userid);
			entity.setLastModifyTime(currentTime);
			entity.setIsCalculated("99");
			try {
				bizPalletInfoService.update(entity);
			} catch (Exception e) {
				logger.error("更新异常", e);
			}
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BizPalletInfoEntity entity) {
		entity.setDelFlag("1");
		try {
			bizPalletInfoService.delete(entity);
		} catch (Exception e) {
			logger.error("删除失败",e);
		}	
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
		
		String lockString=Tools.getMd5("BMS_QUO_IMPORT_PALLET_UPDATE"+JAppContext.currentUserName());
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
					 bmsErrorLogInfoEntity.setClassName("BizPalletInfoController");
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
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String, String> bizTypeMap = new HashMap<String, String>();
		
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename(), wareHouse.getWarehouseid());
			}
		}
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName(), scEntity.getCode());
		    }
		}
		//温度类型
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("typeCode", "BIZ_TYPE");
		List<SystemCodeEntity> bizTypeList = systemCodeService.queryCodeList(para);
		if(bizTypeList != null && bizTypeList.size()>0){
		    for(SystemCodeEntity scEntity : bizTypeList){
		    	bizTypeMap.put(scEntity.getCodeName(), scEntity.getCode());
		    }
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername(), customer.getCustomerid());
				}
			}
		}
	
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BizPalletInfoController");
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
		
		if(cols>6){
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
		
		List<BizPalletInfoEntity> infoLists = new ArrayList<BizPalletInfoEntity>();
		Map<String, Object> dataMap = new HashMap<>();
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	Map<String, Object> condition = new HashMap<>();
        	Map<String,Object> map0 = new HashMap<String,Object>();
        	BizPalletInfoEntity entity = new BizPalletInfoEntity();
        	
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			
			String curTime = getCellValue(xssfRow.getCell(0));
			String customerName=getCellValue(xssfRow.getCell(1));
			String warehouseName=getCellValue(xssfRow.getCell(2));
			String temperatureType=getCellValue(xssfRow.getCell(3));
			String bizType=getCellValue(xssfRow.getCell(4));
			String adjustPalletNum=getCellValue(xssfRow.getCell(5));
			// 库存时间（必填）
			if(StringUtils.isEmpty(curTime)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行库存日期为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			//商家（必填）
			if(StringUtils.isEmpty(customerName)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行商家为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			//仓库（必填）
			if(StringUtils.isEmpty(warehouseName)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行仓库为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			//托数类型（必填）
			if(StringUtils.isEmpty(bizType)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行托数类型为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			//托数类型为：商品/耗材托数，温度（必填）
			if (("商品托数".equals(bizType) || "耗材托数".equals(bizType)) && StringUtils.isEmpty(temperatureType)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行温度类型为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			
//			//入库单号重复性校验
//			if (dataMap.containsKey(instockNo)) {
//				setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行入库单号重复！");
//				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//			}else {
//				dataMap.put(instockNo, xssfRow);
//			}
			
			//调整数量为空
			if(StringUtils.isBlank(adjustPalletNum)){
				setMessage(infoList, rowNum+1,"没有需要调整的内容！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			// 调整数量
			if(StringUtils.isNotBlank(adjustPalletNum)) {
				boolean isNumber = ExportUtil.isNumber(adjustPalletNum);
				if(!isNumber) {
					int lieshu = rowNum + 1;
					setMessage(infoList, rowNum+1,"第"+lieshu+"行非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				}else{
					map0.put("adjustPalletNum", adjustPalletNum);
				}
			}else{
				map0.put("adjustPalletNum", BigDecimal.ZERO);
			}
					
			if (map.size() != 0) {
				continue;
			}

			map0.put("curTime", curTime);
			map0.put("customerId", customerMap.get(customerName));
			map0.put("warehouseCode", wareHouseMap.get(warehouseName));
			map0.put("temperatureTypeCode", temperatureMap.get(temperatureType));
			map0.put("bizType", bizTypeMap.get(bizType));
			map0.put("isCalculated", "99");
			map0.put("lastModifier", username);
			map0.put("lastModifierId", userid);
			map0.put("lastModifyTime", nowdate);
			map0.put("delFlag", "0");
			list.add(map0);
        }
        if (map.size() != 0) {
			return map;
		}
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
			num = bizPalletInfoService.updateBatch(list);
		} catch (Exception e) {
		     message = e.getMessage();
		     logger.error(e.getMessage(), e);
		     //写入日志
		     BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			 bmsErrorLogInfoEntity.setClassName("bizPalletInfoController");
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
        }
        return map;
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
	
	/**
	 * 批量更新模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplateUpdate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/pallet_updatebatch_template.xlsx");
		return new DownloadFile("托数管理更新模板.xlsx", is);
	}
	
	/**
	 * 分组统计
	 * @param param
	 * @return
	 */
	@DataProvider
	public void queryGroup(Page<BizPalletInfoEntity> page, Map<String, Object> param){
		PageInfo<BizPalletInfoEntity> pageInfo = bizPalletInfoService.groupCount(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		List<BizPalletInfoEntity> list = bizPalletInfoService.query(param);
		if (null == list || list.size() == 0) {
			return "没有数据重算";
		}
		if(bizPalletInfoService.reCalculate(list) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
	
}
