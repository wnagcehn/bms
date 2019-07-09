package com.jiuyescm.bms.consumer.export;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

/**
 * 原始耗材导出
 * @author wangchen870
 *
 */

@Service("bizOutStockPackOriginExportListener")
public class BizOutStockPackOriginExportListener implements MessageListener{
	
	private static final Logger logger = LoggerFactory.getLogger(BizOutStockPackOriginExportListener.class);
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	
	@Resource
	private IBizOutstockPackmaterialService bizOutstockPackmaterialServiceImpl;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		logger.info("原始耗材导出异步处理");
		String json = "";
		try {
			json = ((TextMessage)message).getText();
			logger.info("消息Json【{}】", json);
		} catch (JMSException e1) {
			logger.error("取出消息失败",e1);
			return;
		}
		try {
			//导出
			export(json);
		} catch (Exception e1) {
			logger.error("文件导出失败",e1);
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
	
	public void export(String json) throws IOException{
		logger.info("JSON开始解析……");
		Map<String, Object> map = resolveJsonToMap(json);
		if (null == map) {
			return;
		}
		//拿到TaskId,更新状态
		logger.info("====taskId:" + map.get("taskId").toString());
		String taskId = map.get("taskId").toString();
		String filePath = map.get("filePath").toString();
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====原始耗材导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
		// 如果文件存在直接删除，重新生成
		String fileName = "原始耗材出库明细" + FileConstant.SUFFIX_XLSX;
		String filepath = path + FileConstant.SEPARATOR + fileName;
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		
    	logger.info("====原始耗材导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	try {
    		handOriginMaterial(poiUtil, workbook, filePath, map);
		} catch (Exception e) {
			fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 99);
		}
    	
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====原始耗材导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	private void handOriginMaterial(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String filePath,
			Map<String, Object> myparam) throws IOException, ParseException {
		List<Map<String, Object>> dataPackMaterialList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> headPackMaterialMapList = null;
		//转换时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sd = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH);
		Date convertCreateTime = sd.parse(myparam.get("startTime").toString());
		Date convertEndTime = sd.parse(myparam.get("endTime").toString());
		String startTime =formatter.format(convertCreateTime) ;
		String endTime = formatter.format(convertEndTime);
		myparam.put("startTime", startTime);
		myparam.put("endTime", endTime);

		List<PubMaterialInfoVo> materialInfoList = getAllMaterial();
		List<BizOutstockPackmaterialEntity> ListHead = bizOutstockPackmaterialServiceImpl
				.queryOriginMaterialFromBizData(myparam);
		List<String> materialCodeList = getMaterialCodeList(ListHead);
		headPackMaterialMapList = getHeadPackMaterialMap(materialCodeList, materialInfoList);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				
		int lineNo = 1;
		
		for (BizOutstockPackmaterialEntity materialEntity : ListHead) {
			boolean flag = false;
			Map<String, Object> matchMap = null;
			for (Map<String, Object> map : dataPackMaterialList) {
				if (map.get("waybillNo").equals(
						materialEntity.getWaybillNo())) {
					flag = true;
					matchMap = map;
					break;
				}
			}
			if (flag) {
				// 检查耗材类型
				String marterialType = getMaterialType(materialInfoList,
						materialEntity.getConsumerMaterialCode());
				if (matchMap.containsKey(marterialType + "_name")) {
					matchMap.put(
							marterialType + "_name",
							matchMap.get(marterialType + "_name") + ","
									+ materialEntity.getConsumerMaterialCode() == null ? ""
									: materialEntity.getConsumerMaterialCode());
					if (materialEntity.getConsumerMaterialCode().contains("GB")) {
						matchMap.put(
								marterialType + "_count",
								matchMap.get(marterialType + "_count")
										+ "," + materialEntity.getWeight() == null ? ""
										: Double.valueOf(materialEntity.getWeight()));
					} else {
						matchMap.put(
								marterialType + "_count",
								matchMap.get(marterialType + "_count")
										+ ","
										+ materialEntity.getNum() == null ? ""
										: Double.valueOf(materialEntity.getNum()));
					}
				} else {
					matchMap.put(marterialType + "_name",
							materialEntity.getConsumerMaterialCode() == null ? ""
									: materialEntity.getConsumerMaterialCode());
					if (materialEntity.getConsumerMaterialCode().contains("GB")) {
						matchMap.put(marterialType + "_count",
								materialEntity.getWeight() == null ? ""
										: Double.valueOf(materialEntity.getWeight()));
					} else {
						matchMap.put(marterialType + "_count",
								materialEntity.getNum() == null ? ""
										: Double.valueOf(materialEntity.getNum()));
					}
				}
			} else {
				Map<String, Object> dataItem = new HashMap<String, Object>();
				dataItem.put("createTime",sdf.format(materialEntity.getCreateTime()));
				dataItem.put("warehouseName",materialEntity.getWarehouseName());
				dataItem.put("customerName",materialEntity.getCustomerName());
				dataItem.put("outstockNo", materialEntity.getOutstockNo());
				dataItem.put("waybillNo", materialEntity.getWaybillNo());
				String marterialType = getMaterialType(materialInfoList,
						materialEntity.getConsumerMaterialCode());
				dataItem.put(marterialType + "_name",
						materialEntity.getConsumerMaterialCode());
				dataItem.put(marterialType + "_code",
						materialEntity.getConsumerMaterialCode());
				dataItem.put(marterialType + "_type",
						materialEntity.getSpecDesc());
				if (materialEntity.getConsumerMaterialCode().contains("GB")) {
					dataItem.put(marterialType + "_count", materialEntity
							.getWeight() == null ? "" : Double.valueOf(materialEntity
							.getWeight()));
				} else {
					dataItem.put(marterialType + "_count", materialEntity
							.getNum() == null ? "" : Double.valueOf(materialEntity
							.getNum()));
				}
				dataPackMaterialList.add(dataItem);
			}		
		}
		
		if (headPackMaterialMapList != null && dataPackMaterialList != null) {
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PAY_DIS_ORIGIN_DATA.getDesc(), 
					lineNo, headPackMaterialMapList, dataPackMaterialList);
		}	
	}
	
	private List<PubMaterialInfoVo> getAllMaterial() {
		Map<String, Object> conditionMap = Maps.newHashMap();
		conditionMap.put("delFlag", 0);
		return pubMaterialInfoService.queryList(conditionMap);
	}
	
	private List<String> getMaterialCodeList(List<BizOutstockPackmaterialEntity> ListHead) {
		List<String> materialCodeList = new ArrayList<String>();
		for (BizOutstockPackmaterialEntity entity : ListHead) {
			materialCodeList.add(entity.getConsumerMaterialCode());
		}
		return materialCodeList;
	}
	
	private List<Map<String, Object>> getHeadPackMaterialMap(List<String> materialCodeList,
			List<PubMaterialInfoVo> materialInfoList) {
		Map<String, String> headMap = getHeadMap(materialCodeList, materialInfoList);
		List<Map<String, Object>> headMapList = new ArrayList<Map<String, Object>>();
		Set<String> set = headMap.keySet();
		for (String key : set) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap = new HashMap<String, Object>();
			itemMap.put("title", headMap.get(key));
			itemMap.put("columnWidth", 25);
			itemMap.put("dataKey", key);
			headMapList.add(itemMap);
		}
		return headMapList;
	}
	
	private Map<String, String> getHeadMap(List<String> materialCodeList, List<PubMaterialInfoVo> materialInfoList) {
		Map<String, String> map = Maps.newLinkedHashMap();
		map.put("createTime", "出库日期");
		map.put("warehouseName", "仓库");
		map.put("customerName", "商家");
		map.put("outstockNo", "出库单号");
		map.put("waybillNo", "运单号");
		
		// 按序号获取systemCode表中的类别并排序
		Map<String, Object> param = new HashMap<>();
		param.put("typeCode", "PACKMAGERIAL_SORT");
		List<SystemCodeEntity> entityList = systemCodeService.queryBySortNo(param);
		final List<String> orderList = new ArrayList<>();
		for (SystemCodeEntity entity : entityList) {
			orderList.add(entity.getCodeName());
		}

		List<String> materialTypeList = new ArrayList<String>();
		HashMap<String, Object> codeMap = new HashMap<>();
		for (String code : materialCodeList) {
			String marterialType = getMaterialType(materialInfoList, code);

			if (!materialTypeList.contains(marterialType)) {
				materialTypeList.add(marterialType);
				codeMap.put(marterialType, code);
			}
		}

		// 按上述list进行排序
		Collections.sort(materialTypeList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
		    	if (orderList.indexOf(o1) >= 0 && orderList.indexOf(o2) >= 0) {
					return orderList.indexOf(o1)-orderList.indexOf(o2);
				}else if (orderList.indexOf(o1) >= 0 && orderList.indexOf(o2) <= 0) {
					return -1;
				}else if (orderList.indexOf(o1) <= 0 && orderList.indexOf(o2) >= 0) {
					return 1;
				}else {
					return 0;
				}
			}
		});

		// 遍历输出
		for (String marterialTypeDetail : materialTypeList) {
			map.put(marterialTypeDetail + "_name", marterialTypeDetail);
			if (((String) codeMap.get(marterialTypeDetail)).contains("GB")) {
				map.put(marterialTypeDetail + "_count", marterialTypeDetail+"重量");
			} else {
				map.put(marterialTypeDetail + "_count", marterialTypeDetail+"数量");
			}
		}
		return map;
	}
	
	private String getMaterialType(List<PubMaterialInfoVo> materialInfoList, String materialCode) {
		for (PubMaterialInfoVo infoVo : materialInfoList) {
			if (StringUtils.isNotBlank(infoVo.getBarcode()) && infoVo.getBarcode().equals(materialCode)) {
				return infoVo.getMaterialType();
			}
		}
		return "其他";
	}

	/**
	 * 获取原始耗材出库明细导出的文件路径
	 * @return
	 */
	public String getPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_ORIGIN_PAY_BIZ");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_ORIGIN_PAY_BIZ");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 获取系统参数对象
	 * @param typeCode
	 * @param code
	 * @return
	 */
	public SystemCodeEntity getSystemCode(String typeCode, String code){
		if (StringUtils.isNotEmpty(typeCode) && StringUtils.isNotEmpty(code)) {
			SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode(typeCode, code);
			if(systemCodeEntity == null){
				throw new BizException("请在系统参数中配置文件上传路径,参数" + typeCode + "," + code);
			}
			return systemCodeEntity;
		}
		return null;
	}
	
	/**
	 * 解析Json成Map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> resolveJsonToMap(String json) {
		//解析JSON
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>)JSON.parse(json);
		} catch (Exception e) {
			logger.error("JSON解析异常 {}", e);
			return null;
		}
		return map;
	}
	
}
