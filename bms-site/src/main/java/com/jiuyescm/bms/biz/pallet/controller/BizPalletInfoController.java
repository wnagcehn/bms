package com.jiuyescm.bms.biz.pallet.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf2.core.context.ContextHolder;
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
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.common.enumtype.type.ExeclOperateTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.common.vo.ExportDataVoEntity;
import com.jiuyescm.bms.common.web.HttpCommanExport;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.upload.PalletDataType;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.constants.MQConstants;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.redis.client.IRedisClient;
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
	@Autowired 
	private IWarehouseService warehouseService;
	@Resource 
	private ISystemCodeService systemCodeService;
	@Autowired 
	private ICustomerService customerService;
	@Resource 
	private IRedisClient redisClient;
	@Resource
	private JmsTemplate jmsQueueTemplate;
	@Autowired
	private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired 
	private StorageClient storageClient;
	@Resource 
	private SequenceService sequenceService;
	@Autowired 
	private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired 
	private IBmsGroupSubjectService bmsGroupSubjectService;

	String sessionId=JAppContext.currentUserID()+"_import_PalletStorage";
	final String nameSpace="com.jiuyescm.bms.biz.pallet.controller.BizPalletInfoController";
	String[] subjects = null;
	
	private String[] initSubjects() {
		//这里的科目应该在科目组中配置,动态查询
		//wh_disposal(处置费)
		Map<String,String> map=bmsGroupSubjectService.getSubject("job_subject_pallet");
		if(map.size() == 0){
			String[] strs = {"wh_disposal","wh_product_storage","wh_material_storage","outstock_pallet_vm"};
			return strs;
		}else{
			List<String> strs = new ArrayList<String>();
			for(String value:map.keySet()){
				strs.add(value);
			}
			strs.add("outstock_pallet_vm");
			String[] strArray = strs.toArray(new String[strs.size()]);	
			return strArray;
		}
	}
	

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizPalletInfoEntity> page, Map<String, Object> param) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		if (param.get("createTime") == null) {
			throw new BizException("创建时间不能为空!");
		}
		if (param.get("createEndTime") == null) {
			throw new BizException("结束时间不能为空!");
		}
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
			int k = bizPalletInfoService.update(entity);
			if (k == 0) {
				throw new BizException("更新失败！");
			}
			//如果是出库托数，不往下进行，不更新计算状态
			if ("outstock".equals(entity.getBizType())) {
				return;
			}
			
			BmsCalcuTaskVo calcuTaskVo = new BmsCalcuTaskVo();
			String subjectCode = "";
			String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
			
			switch (entity.getBizType()) {
			case "product":
				subjectCode = "wh_product_storage";
				break;
			case "material":
				subjectCode = "wh_material_storage";
				break;
			case "instock":
				subjectCode = "wh_disposal";
				break;
			default:
				subjectCode = "outstock_pallet_vm";
				break;
			}
			
			try{
				calcuTaskVo.setCrePerson(ContextHolder.getLoginUser().getCname());
				calcuTaskVo.setCrePersonId(ContextHolder.getLoginUserName());
				calcuTaskVo.setCustomerId(entity.getCustomerId());
				calcuTaskVo.setSubjectCode(subjectCode);
				calcuTaskVo.setCreMonth(Integer.valueOf(creMonth));
				bmsCalcuTaskService.sendTask(calcuTaskVo);
				logger.info("mq发送成功,商家id:"+calcuTaskVo.getCustomerId()+",年月:"+calcuTaskVo.getCreMonth()+",科目id:"+calcuTaskVo.getSubjectCode());
			}
			catch(Exception ex){
				logger.error("mq发送失败:", ex);
			}
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public String delete(List<BizPalletInfoEntity> lists) {
		List<BizPalletInfoEntity> delList = new ArrayList<BizPalletInfoEntity>();
		List<BizPalletInfoEntity> modList = new ArrayList<BizPalletInfoEntity>();
		for (BizPalletInfoEntity entity : lists) {
			entity.setDelFlag("1");
			//如果计费来源是系统，返回
			if ("system".equals(entity.getChargeSource())) {
				return "不能删除计费来源是系统的数据!";
			}
			//计费来源是导入, 如果系统托数是0, 那么整体数据可以删除
			if ("import".equals(entity.getChargeSource()) && entity.getSysPalletNum() == 0) {
				delList.add(entity);
			}
			//计费来源是导入,如果系统托数不等于0, 那么删除动作就是更新导入托数为0
			if ("import".equals(entity.getChargeSource()) && entity.getSysPalletNum() != 0) {
				entity.setPalletNum(0d);
				modList.add(entity);
			}
		}		
		try {
			bizPalletInfoService.delete(delList);
		} catch (Exception e) {
			logger.error("删除失败",e);
			return "删除失败!";
		}
		try {
			bizPalletInfoService.updatePalletNumBatch(modList);
		} catch (Exception e) {
			logger.error("更新失败",e);
			return "更新失败!";
		}
		return "删除成功！";
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
				errorVo.setMsg("托数批量更新功能已被其他用户占用，请稍后重试；");
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
	public Map<String, Object> importUpdateWeightLock(UploadFile file, Map<String, Object> parameter) throws ParseException{
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
		//托数类型
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
		
		Map<String, Object> dataMap = new HashMap<>();
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	Map<String,Object> map0 = new HashMap<String,Object>();
        	
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
			
			//查看此行是否在库中已存在
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = HSSFDateUtil.getJavaDate(Double.valueOf(curTime));
			curTime = sdf.format(date);
			Map<String, Object> cond = new HashMap<String, Object>();
			cond.put("curTime", curTime);
			cond.put("customerId", customerMap.get(customerName));
			cond.put("warehouseCode", wareHouseMap.get(warehouseName));
			cond.put("temperatureTypeCode", temperatureMap.get(temperatureType));
			cond.put("bizType", bizTypeMap.get(bizType));
			List<BizPalletInfoEntity> palletList = bizPalletInfoService.query(cond);
			if (CollectionUtils.isEmpty(palletList)) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"行不存在库中！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}
			
			//入库单号重复性校验
			if (dataMap.containsKey(curTime+customerName+warehouseName+bizType+temperatureType)) {
				setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行数据重复！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			}else {
				dataMap.put(curTime+customerName+warehouseName+bizType+temperatureType, xssfRow);
			}
			
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
			map0.put("lastModifier", username);
			map0.put("lastModifierId", userid);
			map0.put("lastModifyTime", nowdate);
			map0.put("delFlag", "0");
			if ("outstock".equals(bizTypeMap.get(bizType))) {
				map0.put("isCalculated", "5");
			}else {
				map0.put("isCalculated", "99");
			}
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
			// 写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity = new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("bizPalletInfoController");
			bmsErrorLogInfoEntity.setMethodName("importUpdateWeightLock");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		if (num == 0) {
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
        }else {
        	//更新业务表和费用表成功,发送MQ
        	Map<String, String> taskMap = new HashMap<>();
        	for (Map<String, Object> columens : list) {
        		if ("outstock".equals(columens.get("bizType").toString())) {
					continue;
				}
        		
        		String creMonth = columens.get("curTime").toString().substring(0, 7).replace("-", "");
				String subjectCode = "";
				switch (columens.get("bizType").toString()) {
				case "product":
					subjectCode = "wh_product_storage";
					break;
				case "material":
					subjectCode = "wh_material_storage";
					break;
				case "instock":
					subjectCode = "wh_disposal";
					break;
				default:
					subjectCode = "outstock_pallet_vm";
					break;
				}
				StringBuilder sb = new StringBuilder(columens.get("customerId").toString()).append("-").append(subjectCode).append("-").append(creMonth);
				taskMap.put(sb.toString(), sb.toString());
			}
        	for (String key : taskMap.keySet()) {
        		String[] array = key.split("-");
    			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
    			try{
    				vo.setCrePerson(ContextHolder.getLoginUser().getCname());
    				vo.setCrePersonId(ContextHolder.getLoginUserName());
    				vo.setCustomerId(array[0]);
    				vo.setSubjectCode(array[1]);
    				vo.setCreMonth(Integer.valueOf(array[2]));
    				bmsCalcuTaskService.sendTask(vo);
    				logger.info("mq发送成功,商家id:"+vo.getCustomerId()+",年月:"+vo.getCreMonth()+",科目id:"+vo.getSubjectCode());
    			}
    			catch(Exception ex){
    				logger.error("mq发送失败:", ex);
    			}
			}
        	
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
		if ("outstock".equals(param.get("bizType").toString())) {
			return "出库托数不进行重算";
		}
		List<BizPalletInfoEntity> list = bizPalletInfoService.query(param);
		if (CollectionUtils.isEmpty(list)) {
			return "没有数据重算";
		}
		
		if(bizPalletInfoService.retryCalculate(list) <= 0){
			return "重算异常";
		}else {
			List<BmsCalcuTaskVo> taskVos = bmsCalcuTaskService.queryPalletTask(param);
			for (BmsCalcuTaskVo calcuTaskVo : taskVos) {
				calcuTaskVo.setCrePerson(ContextHolder.getLoginUser().getCname());
				calcuTaskVo.setCrePersonId(ContextHolder.getLoginUserName());
				try {
					bmsCalcuTaskService.sendTask(calcuTaskVo);
					logger.info("mq发送成功,商家id:"+calcuTaskVo.getCustomerId()+",年月:"+calcuTaskVo.getCreMonth()+",科目id:"+calcuTaskVo.getSubjectCode());
				} catch (Exception e) {
					logger.error("mq发送失败:", e);
				}
			}
		}
		return "操作成功! 正在重算...";
	}
	
	/**
	 * 导入模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/pallet_storage_import_template.xlsx");
		return new DownloadFile("托数管理导入模板.xlsx", is);
	}
	
	/**
	 * 导入
	 */
	@FileResolver
	public Map<String, Object> importPalletTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();

		String userId=ContextHolder.getLoginUserName();
		String lockString=Tools.getMd5(userId + "BMS_QUE_PALLET_STORAGE_IMPORT");
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
				errorVo.setMsg("托数导入功能已被其他用户占用，请稍后重试；");
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
	
	/**
	 * 异步导入处理文件
	 * @param file
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String,Object> importFileAsyn(UploadFile file, Map<String, Object> parameter) throws Exception{
		setProgress(0);
		Map<String, Object> map = Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		setProgress(1);
		String extendFileName="";
		if(file.getFileName().contains("xlsx")){
			extendFileName="xlsx";
		}else{
			extendFileName="xls";
		}
		
		String fileName = file.getFileName();
				
		double maxFileSize=getMaxFileSize();
		double importFileSize=BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024*1024)).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(importFileSize>maxFileSize){
			infoList.add(new ErrorMessageVo(1, "Excel 导入文件过大,最多能导入"+maxFileSize+"M,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		// 保存导入文件到fastDFS，获取文件路径
		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		StorePath resultStorePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		String fullPath = storePath.getFullPath();
		String resultFullPath = resultStorePath.getFullPath();
		if (StringUtils.isBlank(fullPath) || StringUtils.isBlank(resultFullPath)) {
			setProgress(6);
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
		taskEntity.setTaskType("BMS.QUEUE.PRODUCT_PACK_STORAGE_IMPORT.TASK");
		taskEntity.setBizType(ExeclOperateTypeEnum.IMPORT.getCode());
		taskEntity.setFileRows(0);
		taskEntity.setOriginFileName(fileName);
		taskEntity.setOriginFilePath(fullPath);
		taskEntity.setResultFileName(fileName);
		taskEntity.setResultFilePath(resultFullPath);
		taskEntity.setCreator(JAppContext.currentUserName());
		taskEntity.setCreatorId(JAppContext.currentUserID());
		taskEntity.setCreateTime(JAppContext.currentTimestamp());
		int saveNum = bmsFileAsynTaskService.save(taskEntity);
		if (saveNum <= 0) {
			setProgress(6);
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据生成任务失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 写入MQ
		final String msg = taskId;
		jmsQueueTemplate.send(MQConstants.PALLET_STORAGE_IMPORT_TASK, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
		
		setProgress(5);
		map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "操作成功");
		return map;
	}
	
	/**
	 *重置处理进度
	 */
    @Expose
	public void resetProgress() {
    	redisClient.del(sessionId, nameSpace);
	}
	/**
	 * 过期时间 1小时
	 * @param progress
	 */
	private void setProgress(Object obj){
		
		redisClient.set(sessionId, nameSpace, obj,10*60);
	}
	
	 /**
	 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
	 * @return
	 */
    @Expose
	public int getProgress(){
    	if(redisClient.exists(sessionId, nameSpace)){
    		return Integer.valueOf(redisClient.get(sessionId, nameSpace, null));
    	}else{
    		return 0;
    	}
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
			voEntity.setTitleName("托数导出");
			voEntity.setBaseType(new PalletDataType());
			voEntity.setDataList(getDataList(parameter));
			return commanExport.exportFile(voEntity);
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			throw e;
		}
		
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PALLET_INFO");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PALLET_INFO");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	private List<Map<String,Object>> getDataList(Map<String,Object> parameter) throws Exception{
		//费用科目
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList("PALLET_CAL_FEE");
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {	
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		//计算状态
		Map<String, String> calStatus = new HashMap<String, String>();
		calStatus = CalculateState.getMap();
		List<BizPalletInfoEntity> list = bizPalletInfoService.query(parameter);
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		for (BizPalletInfoEntity entity : list) {
			map = new HashMap<>();
			map.put("curTime",entity.getCurTime() );
			map.put("warehouseName", entity.getWarehouseName());
			map.put("customerName", entity.getCustomerName());
			map.put("temperatureTypeCode", BmsEnums.tempretureType.getDesc(entity.getTemperatureTypeCode()));
			map.put("subjectCode", mapValue.get(entity.getSubjectCode()));
			map.put("bizType", BmsEnums.palletType.getDesc(entity.getBizType()));
			if ("import".equals(entity.getChargeSource())) {
				map.put("chargeSource", "导入");
			}else if ("system".equals(entity.getChargeSource())) {
				map.put("chargeSource", "系统");
			}else {
				map.put("chargeSource", "");
			}
			map.put("sysPalletNum", entity.getSysPalletNum());
			map.put("palletNum", entity.getPalletNum());
			map.put("diffPalletNum", entity.getSysPalletNum()-entity.getPalletNum());
			map.put("adjustPalletNum", entity.getAdjustPalletNum());
			map.put("cost", entity.getCost());
			map.put("isCalculated", calStatus.get(entity.getIsCalculated()));
			map.put("remark", entity.getRemark());
			map.put("feesNo", entity.getFeesNo());
			map.put("creator", entity.getCreator());
			map.put("createTime", entity.getCreateTime());
			mapList.add(map);
		}
		return mapList;
	}
	
	public Map<String, String> getEnumList(String typeCode) {	
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList(typeCode);
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {	
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		return mapValue;
	}
	
}
