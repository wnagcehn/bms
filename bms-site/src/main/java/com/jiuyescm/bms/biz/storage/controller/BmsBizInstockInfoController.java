package com.jiuyescm.bms.biz.storage.controller;

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
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockRecordService;
import com.jiuyescm.bms.biz.storage.service.impl.BmsBizInstockRecordServiceImpl;
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
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;

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
			entity.setIsCalculated("99");
			entity.setLastModifier(username);
			entity.setLastModifierId(userId);
			entity.setLastModifyTime(currentTime);
			bmsBizInstockInfoService.update(entity);
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
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	Map<String, Object> condition = new HashMap<>();
        	Map<String,Object> map0 = new HashMap<String,Object>();
        	BmsBizInstockInfoEntity entity = new BmsBizInstockInfoEntity();
        	
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			String instockNo = getCellValue(xssfRow.getCell(0));
			String adjustQty=getCellValue(xssfRow.getCell(1));
			String adjustBox=getCellValue(xssfRow.getCell(2));
			String adjustWeight=getCellValue(xssfRow.getCell(3));
			// 运单号（必填）
			if(StringUtils.isEmpty(instockNo)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列入库单号为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
						
			//调整数量、箱数、重量都为空
			if(StringUtils.isBlank(adjustQty) && 
					StringUtils.isBlank(adjustBox) && 
					StringUtils.isBlank(adjustWeight)){
				// 除instockNo外，其他更新字段都为空
				setMessage(infoList, rowNum+1,"没有需要调整的内容！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			// 调整数量
			if(StringUtils.isNotBlank(adjustQty)) {
				boolean isNumber = ExportUtil.isNumber(adjustQty);
				if(!isNumber) {
					int lieshu = rowNum + 1;
					setMessage(infoList, rowNum+1,"第"+lieshu+"列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
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
					setMessage(infoList, rowNum+1,"第"+lieshu+"列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
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
					setMessage(infoList, rowNum+1,"第"+lieshu+"列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}else{
					map0.put("adjustBox", adjustBox);
				}
			}else {
				map0.put("adjustBox", BigDecimal.ZERO);
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
			entity.setFeesNo(lists.get(0).getFeesNo());
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
			entity.setDelFlag(lists.get(0).getDelFlag());
			entity.setCalculateTime(lists.get(0).getCalculateTime());
			entity.setRemark(lists.get(0).getRemark());
			entity.setLastModifier(username);
			entity.setLastModifierId(userid);
			entity.setLastModifyTime(nowdate);
			infoLists.add(entity);
        }
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
			num = bmsBizInstockInfoService.updateBatch(list);
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
	
}
