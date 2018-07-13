package com.jiuyescm.bms.fees.dispatch.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.dispatch.vo.FeesReceiveDispatchVo;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应收费用-配送费 controller
 * 
 * @author zhaofeng
 *
 */
@Controller("feesReceiveDispatchContorller")
public class FeesReceiveDispatchController {
	
	private static final Logger logger = Logger.getLogger(FeesReceiveDispatchController.class.getName());
	private DateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private DateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IFeesReceiveDispatchService service;
	@Resource
	private IProjectService projectService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	/**
	 * jira BMS-383 wuliangfeng
	 * @param param
	 * @return
	 */
	@DataProvider
	public List<Map<String,Object>> queryGroup(Map<String, Object> param){
		if (param == null){
			param = new HashMap<String, Object>();
		}
		//物流商
		if(param.get("deliveryid") != null && StringUtils.equalsIgnoreCase(param.get("deliveryid").toString(), "ALL")){
			param.put("deliveryid", null);
		}
		//初始化时间
		if(param.get("status")==null || StringUtils.isBlank(param.get("status").toString())
				&& param.get("startTime")==null && param.get("endTime")==null){
			Date date = new Date();
			
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(date); 
		    calendar.add(calendar.DATE,-6);//把日期往前减少七天.整数往后推,负数往前移动 
		   
			param.put("startTime",Timestamp.valueOf(startDateFormat.format(calendar.getTime())));
			param.put("endTime",Timestamp.valueOf(endDateFormat.format(date)));

		}
		
		if(param.get("status") != null && StringUtils.equalsIgnoreCase(param.get("status").toString(), "全部")){
			param.put("status", null);
		}
		List<Map<String,Object>> list=service.queryGroup(param);
		return list;
	}
	@DataProvider
	public void query(Page<FeesReceiveDispatchVo> page, Map<String, Object> param){
		if (param == null){
			param = new HashMap<String, Object>();
		}
		//物流商
		if(param.get("deliveryid") != null && StringUtils.equalsIgnoreCase(param.get("deliveryid").toString(), "ALL")){
			param.put("deliveryid", null);
		}
		//初始化时间
		if(param.get("status")==null || StringUtils.isBlank(param.get("status").toString())
				&& param.get("startTime")==null && param.get("endTime")==null){
			Date date = new Date();
			
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(date); 
		    calendar.add(calendar.DATE,-6);//把日期往前减少七天.整数往后推,负数往前移动 
		   
			param.put("startTime",Timestamp.valueOf(startDateFormat.format(calendar.getTime())));
			param.put("endTime",Timestamp.valueOf(endDateFormat.format(date)));

		}
		
		if(param.get("status") != null && StringUtils.equalsIgnoreCase(param.get("status").toString(), "全部")){
			param.put("status", null);
		}
		if(param.get("projectId")!=null&&StringUtils.isNotBlank(param.get("projectId").toString())){
			List<String> customerIds=projectService.queryAllCustomerIdByProjectId(param.get("projectId").toString());
			if(customerIds==null||customerIds.size()==0){
				page.setEntities(null);
				page.setEntityCount(0);
				return;
			}else{
				param.put("customerIdList", customerIds);
			}
		}	
		
		PageInfo<FeesReceiveDispatchVo> pageInfo = service.query1(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	private void InitList(List<FeesReceiveDispatchVo> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesReceiveDispatchVo entity:list){
			if(entity.getCustomerid()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerid().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}	
				}
			}
		}
	}
	@FileProvider
	public DownloadFile export(Map<String, Object> param) throws Exception{
		if (param == null){
			param = new HashMap<String, Object>();
		}
		if("全部".equals(param.get("status"))){
			param.put("status", null);
		}
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PATH_DISTRIBUTION_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PATH_DISTRIBUTION_FEES");
		}
		List<FeesReceiveDispatchEntity> entityList = service.queryList(param);
		
		String path = systemCodeEntity.getExtattr1();
		String tmep = SequenceGenerator.uuidOf36String("exp");
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		// 所有转码Map
		Map<String, Object> dictcodeMap = new HashMap<String, Object>();
		//状态
		Map<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("0", "未过账");
		statusMap.put("1", "已过账");
		dictcodeMap.put("statusMap", statusMap);
		try {
			//仓库
			List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
			Map<String, String> warehouseMap = new LinkedHashMap<String,String>();
			for (WarehouseVo warehouseVo : warehouseVos) {
				warehouseMap.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
			}
			dictcodeMap.put("warehouseMap", warehouseMap);
			
			//POIUtil poiUtil = new POIUtil();
			
			//HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook xssfWorkbook = poiUtil.getXSSFWorkbook();
	    	
	    	this.appendSheet(poiUtil, xssfWorkbook, "应收费用配送费", path + "\\BmsDistribution_" + tmep + ".xlsx", entityList, dictcodeMap);

        	//this.appendSheet(poiUtil, hssfWorkbook, "应收费用配送费", path + "\\BmsDistribution_" + tmep + ".xls", entityList, dictcodeMap);
		} catch (Exception e) {
			e.printStackTrace();
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), this.getClass().getSimpleName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		
		InputStream is = new FileInputStream(path + "\\BmsDistribution_" + tmep + ".xlsx");
		return new DownloadFile("应收费用配送费" + tmep + ".xlsx", is);
	}
	
	/**
	 * @param poiUtil
	 * @param hssfWorkbook
	 * @param sheetName
	 * @param path
	 * @param entityList
	 * @param dictcodeMap
	 * @throws IOException
	 */
	private void appendSheet(POISXSSUtil poiUtil, SXSSFWorkbook sxssfWorkbook,
			String sheetName, String path, List<FeesReceiveDispatchEntity> entityList,
			Map<String, Object> dictcodeMap)throws IOException {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		itemMap.put("title", "ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "id");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerid");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outstockNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toProvinceName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toCityName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区县");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toDistrictName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
             
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "模板编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "templateId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "揽收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lianshouTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "签收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "signTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数1");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param1");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数2");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param2");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数3");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param3");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数4");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param4");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数5");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param5");
        headInfoList.add(itemMap);
        
       
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "规则编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ruleNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headInfoList.add(itemMap);
              
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        Map<String, Object> dataItem = null;
        for (FeesReceiveDispatchEntity distributionEntity : entityList) {
        	dataItem = new HashMap<String, Object>();
        	dataItem.put("id", distributionEntity.getId());
        	dataItem.put("createTime", distributionEntity.getCreateTime());
        	dataItem.put("feesNo", distributionEntity.getFeesNo());
        	dataItem.put("warehouseId", ((Map)dictcodeMap.get("warehouseMap")).get(distributionEntity.getWarehouseCode()));
        	dataItem.put("customerid", distributionEntity.getCustomerid());
        	dataItem.put("customerName", distributionEntity.getCustomerName());
        	dataItem.put("carrierName", distributionEntity.getCarrierName());
        	dataItem.put("outstockNo", distributionEntity.getOutstockNo());
        	dataItem.put("externalNo", distributionEntity.getExternalNo());
        	dataItem.put("waybillNo", distributionEntity.getWaybillNo());
        	dataItem.put("toProvinceName", distributionEntity.getToProvinceName());
        	dataItem.put("toCityName", distributionEntity.getToCityName());
        	dataItem.put("toDistrictName", distributionEntity.getToDistrictName());
        	dataItem.put("totalWeight", distributionEntity.getTotalWeight());
        	dataItem.put("headWeight", distributionEntity.getHeadWeight());
        	dataItem.put("continuedWeight", distributionEntity.getContinuedWeight());
        	dataItem.put("headPrice", distributionEntity.getHeadPrice());
        	dataItem.put("continuedPrice", distributionEntity.getContinuedPrice());
        	dataItem.put("templateId", distributionEntity.getTemplateId());
        	dataItem.put("amount", distributionEntity.getAmount());
        	dataItem.put("lianshouTime", distributionEntity.getAcceptTime());
        	dataItem.put("signTime", distributionEntity.getSignTime());
        	dataItem.put("param1", distributionEntity.getParam1());
        	dataItem.put("param2", distributionEntity.getParam2());
        	dataItem.put("param3", distributionEntity.getParam3());
        	dataItem.put("param4", distributionEntity.getParam4());
        	dataItem.put("param5", distributionEntity.getParam5());
        	dataItem.put("billNo", distributionEntity.getBillNo());
        	dataItem.put("ruleNo", distributionEntity.getRuleNo());
        	dataItem.put("status", ((Map)dictcodeMap.get("statusMap")).get(distributionEntity.getStatus()));        	
        	dataList.add(dataItem);
		}
        
        //poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,sheetName,path, headInfoList, dataList);
        poiUtil.exportExcelFilePath(poiUtil, sxssfWorkbook, sheetName, headInfoList, dataList);
    	poiUtil.write2FilePath(sxssfWorkbook, path);

	}
	
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, Object> param) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/bill/billdispatchdistinct_template.xlsx");
		return new DownloadFile("九曳宅配对账单导入模板.xlsx", is);
	}
	
}
