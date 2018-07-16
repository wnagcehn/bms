package com.jiuyescm.bms.file.export.web;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("buinessDataExportController")
public class BuinessDataExportController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(BuinessDataExportController.class.getName());
	@Resource
	private IFileExportTaskService fileExportTaskService;

	@Autowired
	private IWarehouseService warehouseService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IBizOutstockMasterService bizOutstockMasterService;
	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Resource
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	@Resource
	private IFeesAbnormalService feesAbnormalService;
	@Resource
	private IBizOutstockPackmaterialService bizOutstockPackmaterialServiceImpl;
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;

	private static final int PAGESIZE = 10000;
	FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
	
	private static ThreadLocal<Double> totalAmount = new ThreadLocal<Double>();
	private static ThreadLocal<Double> totalDeliveryCost = new ThreadLocal<Double>();
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 * @throws Exception
	 */
	@DataProvider
	public void queryTask(Page<FileExportTaskEntity> page,
			Map<String, Object> param) throws Exception {
		if (param == null) {
			param = Maps.newHashMap();
		}
		String year = "";
		String month = "";
		if (param.containsKey("year") && param.containsKey("month")) {
			year = param.get("year").toString();
			month = param.get("month").toString();
		}
		param.put("taskType", "bill_re_down");
		if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
			String startDateStr = year + "-" + month + "-01 00:00:00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf.parse(startDateStr);
			Date endDate = DateUtils.addMonths(startDate, 1);
			param.put("startDate", startDate);
			param.put("endDate", endDate);
		}
		PageInfo<FileExportTaskEntity> pageInfo = fileExportTaskService
				.queryBillTask(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		String customerId = param.get("customerId").toString();
		Timestamp startDate = DateUtil.formatTimestamp(param.get("startDate"));
		Timestamp endDate = DateUtil.formatTimestamp(param.get("endDate"));
		try {
			Map<String, Object> queryEntity = new HashMap<String, Object>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date date = format.parse(endDate.toString());
			String endTime = format.format(addDay(1, date));
			String startTime = format.format(startDate);
			queryEntity.put("customerId", customerId);

			queryEntity.put("startTime", startTime);
			queryEntity.put("endTime", endTime);
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			queryEntity
					.put("taskType", FileTaskTypeEnum.BILL_RE_DOWN.getCode());
			if (checkFileHasDownLoad(queryEntity)) {
				return MessageConstant.BILL_FILE_ISEXIST_MSG;
			}
			DateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String path = getPath();
			String filePath = path + FileConstant.SEPARATOR
					+ param.get("customerName").toString() + "-"
					+ sdf.format(startDate) + "-预账单" + FileConstant.SUFFIX_XLSX;
			FileExportTaskEntity entity = new FileExportTaskEntity();

			entity.setTaskName(param.get("customerName").toString() + "-"
					+ sdf.format(startDate) + "-预账单");
			entity.setBillNo("");
			entity.setStartTime(Timestamp.valueOf(startTime + " 00:00:00"));
			entity.setEndTime(Timestamp.valueOf(format.format(date)
					+ " 00:00:00"));
			entity.setTaskType(FileTaskTypeEnum.BILL_RE_DOWN.getCode());
			entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
			entity.setProgress(0d);
			entity.setFilePath(filePath);
			entity.setCustomerid(customerId);
			entity.setCreator(JAppContext.currentUserName());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			entity = fileExportTaskService.save(entity);

			// 生成账单文件
			final Map<String, Object> condition = param;
			final String taskId = entity.getTaskId();
			final String path2 = path;
			final String filepath = filePath;
			new Thread() {
				public void run() {
					try {
						export(condition, taskId, path2, filepath);
					} catch (Exception e) {
						fileExportTaskService.updateExportTask(taskId,
								FileTaskStateEnum.FAIL.getCode(), 0);
						logger.error(
								ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG,
								e);
						// 写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity = new BmsErrorLogInfoEntity(
								this.getClass().getSimpleName(),
								Thread.currentThread().getStackTrace()[1]
										.getMethodName(), "", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					}
				};
			}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			// 写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity = new BmsErrorLogInfoEntity(
					this.getClass().getSimpleName(), Thread.currentThread()
							.getStackTrace()[1].getMethodName(), "启动线程失败",
					e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return "正在生成预账单，稍后可进行下载";
	}

	/**
	 * 预账单导出
	 * @param condition
	 * @param taskId
	 * @param path
	 * @param filePath
	 * @throws Exception
	 */
	protected void export(Map<String, Object> condition, String taskId,
			String path, String filePath) throws Exception {
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		
		//初始化参数
		init();
		
		File storeFolder = new File(path);
		// 如果存放上传文件的目录不存在就新建
		if (!storeFolder.isDirectory()) {
			storeFolder.mkdirs();
		}
		long beginTime = System.currentTimeMillis();
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook xssfWorkbook = poiUtil.getXSSFWorkbook();
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 20);
		logger.info("====预账单导出：" + "["
				+ condition.get("customerName").toString() + "]"
				+ "写入Excel begin.");

		// 配送费
		handDispatch(xssfWorkbook, poiUtil, condition, filePath);
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 50);
		
		List<String>  warehouseList = queryPreBillWarehouse(condition);
		// 存储费
		handStorage(xssfWorkbook, condition, poiUtil, warehouseList);
		
		// 耗材费
		handMaterial(xssfWorkbook, poiUtil, condition, filePath);
		
		// 增值
    	handAdd(xssfWorkbook, poiUtil, condition, filePath);
    	
    	// 理赔
    	handAbnormal(xssfWorkbook, poiUtil, condition, filePath);
    	//改地址和退件费
    	handAbnormalChange(xssfWorkbook, poiUtil, condition, filePath);

		poiUtil.write2FilePath(xssfWorkbook, filePath);
		updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
		logger.info("====预账单导出：" + "["
				+ condition.get("customerName").toString() + "]"
				+ "写入Excel end.==总耗时："
				+ (System.currentTimeMillis() - beginTime));
	}

	private Date addDay(int days, Date dt) {
		Calendar now = Calendar.getInstance();
		now.setTime(dt);
		now.add(Calendar.DATE, 1);
		Date dt1 = now.getTime();
		return dt1;
	}

	private Map<String,String> mapWarehouse;
	private void init(){
		mapWarehouse=getwarehouse();
	}
	
	private Map<String, String> getHeadMap(List<String> materialCodeList,
			List<PubMaterialInfoVo> materialInfoList) {
		Map<String, String> map = Maps.newLinkedHashMap();
		map.put("warehouseName", "仓库");
		map.put("customerName", "商家名称");
		map.put("waybillNo", "运单号");
		map.put("outstockNo", "出库单号");
		map.put("totalqty", "商品总数");
		map.put("productDetail", "商品和数量");
		map.put("externalNo", "外部物流号");
		map.put("carrierName", "物流商简称");
		map.put("createTime", "接单时间");
		map.put("receiveProvinceId", "收件人省");
		map.put("receiveCityId", "收件人市");
		map.put("receiveDetailAddress", "收件人地址");
		List<String> materialTypeList = new ArrayList<String>();
		for (String code : materialCodeList) {
			String marterialType = getMaterialType(materialInfoList, code);
			if (!materialTypeList.contains(marterialType)) {
				materialTypeList.add(marterialType);
				map.put(marterialType + "_name", marterialType);
				map.put(marterialType + "_code", "编码");
				map.put(marterialType + "_type", "规格");
				if (code.contains("GB")) {
					map.put(marterialType + "_count", "重量");
				} else {
					map.put(marterialType + "_count", "数量");
				}
				map.put(marterialType + "_unitprice", "单价");
				map.put(marterialType + "_cost", "金额");
			}
		}
		map.put("totalCost", "合计");
		return map;
	}

	private List<Map<String, Object>> getHeadPackMaterialMap(
			List<String> materialCodeList,
			List<PubMaterialInfoVo> materialInfoList) {
		Map<String, String> headMap = getHeadMap(materialCodeList,
				materialInfoList);
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

	private List<String> getMaterialCodeList(
			List<BizOutstockPackmaterialEntity> ListHead) {
		List<String> materialCodeList = new ArrayList<String>();
		for (BizOutstockPackmaterialEntity entity : ListHead) {
			materialCodeList.add(entity.getConsumerMaterialCode());
		}
		return materialCodeList;
	}

	/**
	 * 耗材
	 * @param xssfWorkbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws Exception
	 */
	private void handMaterial(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil,
			Map<String, Object> condition, String filePath) throws Exception {
		List<BizOutstockPackmaterialEntity> warehouseList = bizOutstockPackmaterialServiceImpl
				.queryAllWarehouseFromBizData(condition);
		for (BizOutstockPackmaterialEntity entity : warehouseList) {
			int pageNo = 1;
			int size = 20000;
			boolean doLoop = true;
			List<FeesReceiveMaterial> dataList = new ArrayList<FeesReceiveMaterial>();
			condition.put("warehouseCode", entity.getWarehouseCode());
			List<PubMaterialInfoVo> materialInfoList = getAllMaterial();
			List<BizOutstockPackmaterialEntity> ListHead = bizOutstockPackmaterialServiceImpl
					.getMaterialCodeFromBizData(condition);
			List<String> materialCodeList = getMaterialCodeList(ListHead);
			List<Map<String, Object>> headPackMaterialMapList = getHeadPackMaterialMap(
					materialCodeList, materialInfoList);

			while (doLoop) {
				PageInfo<FeesReceiveMaterial> packMaterialList = bizOutstockPackmaterialServiceImpl
						.queryMaterialFromBizData(condition, pageNo, size);
				if (packMaterialList.getList().size() < size) {
					doLoop = false;
				} else {
					pageNo += 1;
				}

				dataList.addAll(packMaterialList.getList());
			}

			if (dataList.size() == 0) {
				continue;
			}

			List<Map<String, Object>> dataPackMaterialList = new ArrayList<Map<String, Object>>();
			for (FeesReceiveMaterial materialEntity : dataList) {
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
							materialEntity.getProductNo());
					if (matchMap.containsKey(marterialType + "_name")) {
						matchMap.put(
								marterialType + "_name",
								matchMap.get(marterialType + "_name") + ","
										+ materialEntity.getProductName() == null ? ""
										: materialEntity.getProductName());
						if (materialEntity.getProductNo().contains("GB")) {
							matchMap.put(
									marterialType + "_count",
									matchMap.get(marterialType + "_count")
											+ "," + materialEntity.getWeight() == null ? ""
											: materialEntity.getWeight()
													.toString());
						} else {
							matchMap.put(
									marterialType + "_count",
									matchMap.get(marterialType + "_count")
											+ ","
											+ materialEntity.getQuantity() == null ? ""
											: materialEntity.getQuantity()
													.toString());
						}
						matchMap.put(marterialType + "_cost",
								matchMap.get(marterialType + "_cost") + ","
										+ materialEntity.getCost() == null ? ""
										: materialEntity.getCost().toString());
						double totleCost = matchMap.get("totalCost") == null ? 0d
								: Double.parseDouble(matchMap.get("totalCost")
										.toString());
						totleCost += materialEntity.getCost() == null ? 0d
								: materialEntity.getCost();
						matchMap.put("totalCost", totleCost);// 金额
					} else {
						matchMap.put(marterialType + "_name",
								materialEntity.getProductName() == null ? ""
										: materialEntity.getProductName());
						if (materialEntity.getProductNo().contains("GB")) {
							matchMap.put(marterialType + "_count",
									materialEntity.getWeight() == null ? ""
											: materialEntity.getWeight()
													.toString());
						} else {
							matchMap.put(marterialType + "_count",
									materialEntity.getQuantity() == null ? ""
											: materialEntity.getQuantity()
													.toString());
						}
						matchMap.put(marterialType + "_code",
								materialEntity.getProductNo());
						matchMap.put(marterialType + "_type",
								materialEntity.getSpecDesc());
						matchMap.put(marterialType + "_unitprice",
								materialEntity.getUnitPrice() == null ? ""
										: materialEntity.getUnitPrice()
												.toString());
						matchMap.put(marterialType + "_cost", materialEntity
								.getCost() == null ? "" : materialEntity
								.getCost().toString());
						double totleCost = matchMap.get("totalCost") == null ? 0d
								: Double.parseDouble(matchMap.get("totalCost")
										.toString());
						totleCost += materialEntity.getCost() == null ? 0d
								: materialEntity.getCost();
						matchMap.put("totalCost", totleCost);// 金额
					}
				} else {
					Map<String, Object> dataItem = new HashMap<String, Object>();
					dataItem.put("warehouseName",
							materialEntity.getWarehouseName());
					dataItem.put("customerName",
							materialEntity.getCustomerName());
					dataItem.put("waybillNo", materialEntity.getWaybillNo());
					dataItem.put("outstockNo", materialEntity.getOutstockNo());
					dataItem.put("totalqty", materialEntity.getTotalqty());
					dataItem.put("productDetail",
							materialEntity.getProductDetail());
					dataItem.put("externalNo", materialEntity.getExternalNo());
					dataItem.put("carrierName", materialEntity.getCarrierName());
					dataItem.put("createTime", materialEntity.getCreateTime());
					dataItem.put("receiveProvinceId",
							materialEntity.getReceiveProvinceId());
					dataItem.put("receiveCityId",
							materialEntity.getReceiveCityId());
					dataItem.put("receiveDetailAddress",
							materialEntity.getReceiveDetailAddress());
					String marterialType = getMaterialType(materialInfoList,
							materialEntity.getProductNo());
					dataItem.put(marterialType + "_name",
							materialEntity.getProductName());
					dataItem.put(marterialType + "_code",
							materialEntity.getProductNo());
					dataItem.put(marterialType + "_type",
							materialEntity.getSpecDesc());
					if (materialEntity.getProductNo().contains("GB")) {
						dataItem.put(marterialType + "_count", materialEntity
								.getWeight() == null ? "" : materialEntity
								.getWeight().toString());
					} else {
						dataItem.put(marterialType + "_count", materialEntity
								.getQuantity() == null ? "" : materialEntity
								.getQuantity().toString());
					}
					dataItem.put(marterialType + "_unitprice", materialEntity
							.getUnitPrice() == null ? "" : materialEntity
							.getUnitPrice().toString());
					dataItem.put(marterialType + "_cost", materialEntity
							.getCost() == null ? "" : materialEntity.getCost()
							.toString());
					dataItem.put("totalCost", materialEntity.getCost());// 金额
					dataPackMaterialList.add(dataItem);
				}

			}
			poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook,
					entity.getWarehouseName() + "耗材使用费",
					headPackMaterialMapList, dataPackMaterialList);
		}

	}

	private List<PubMaterialInfoVo> getAllMaterial() {
		Map<String, Object> conditionMap = Maps.newHashMap();
		conditionMap.put("delFlag", 0);
		return pubMaterialInfoService.queryList(conditionMap);
	}

	private String getMaterialType(List<PubMaterialInfoVo> materialInfoList,
			String materialCode) {
		for (PubMaterialInfoVo infoVo : materialInfoList) {
			if (StringUtils.isNotBlank(infoVo.getBarcode())
					&& infoVo.getBarcode().equals(materialCode)) {
				return infoVo.getMaterialType();
			}
		}
		return "其他";
	}

	private List<Map<String, Object>> getDispathHeadMap() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> headMapDict = Maps.newLinkedHashMap();
		headMapDict.clear();
		headMapDict.put("warehouseCode", "仓库");
		headMapDict.put("customerName", "商家名称");
		headMapDict.put("orderNo", "九曳订单号");
		headMapDict.put("externalNo", "商家订单号");
		headMapDict.put("waybillNo", "运单号");
		headMapDict.put("createTime", "运单生成时间");
		headMapDict.put("zexpressNum", "转寄后运单号");
		headMapDict.put("totalWeight", "运单重量");
		headMapDict.put("totalQty", "商品数量");
		headMapDict.put("productDetail", "商品明细");
		headMapDict.put("carrierName", "计费物流商");
		headMapDict.put("receiver", "收件人");
		headMapDict.put("receiveProvice", "收件人省");
		headMapDict.put("receiveCity", "收件人市");
		headMapDict.put("receiveDistrict", "收件人区县");
		headMapDict.put("temperatureType", "温度");
		headMapDict.put("chargeWeight", "计费重量");
		headMapDict.put("headPrice", "首重金额");
		headMapDict.put("continuePrice", "续重金额");
		headMapDict.put("amount", "运费");
		headMapDict.put("carrierCalStatus", "运费计算状态");
		headMapDict.put("carrierRemark", "运费计算备注");
		headMapDict.put("orderOperatorAmount", "操作费");
		headMapDict.put("orderCalStatus", "操作费计算状态");
		headMapDict.put("orderRemark", "操作费计算备注");
		headMapDict.put("dutyType", "责任方");
		headMapDict.put("reasonDetail", "原因详情");

		Set<String> set = headMapDict.keySet();
		for (String s : set) {
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("title", headMapDict.get(s));
			itemMap.put("columnWidth", 25);
			itemMap.put("dataKey", s);
			list.add(itemMap);
		}
		return list;
	}

	private List<Map<String, Object>> getDispathItemMap(
			List<FeesReceiveDispatchEntity> dataList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		double yunfei = 0d;
		double caozao = 0d;
		for (FeesReceiveDispatchEntity entity : dataList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("warehouseCode", entity.getWarehouseName());
			map.put("customerName", entity.getCustomerName());
			map.put("orderNo", entity.getOutstockNo());
			map.put("externalNo", entity.getExternalNo());
			map.put("waybillNo", entity.getWaybillNo());
			map.put("createTime",
					entity.getCreateTime() == null ? "" : sdf.format(entity
							.getCreateTime()));
			map.put("zexpressNum", entity.getZexpressnum());
			map.put("totalWeight",
					entity.getTotalWeight() == null ? 0 : entity
							.getTotalWeight());
			map.put("totalQty",
					entity.getTotalQuantity() == null ? 0 : entity
							.getTotalQuantity());
			map.put("productDetail", entity.getProductDetail());
			map.put("carrierName", entity.getCarrierName());
			map.put("receiver", entity.getReceiveName());
			map.put("receiveProvice", entity.getToProvinceName());
			map.put("receiveCity", entity.getToCityName());
			map.put("receiveDistrict", entity.getToDistrictName());
			map.put("temperatureType", entity.getTemperatureType());
			map.put("chargeWeight", entity.getChargedWeight() == null ? 0
					: entity.getChargedWeight());
			map.put("headPrice",
					entity.getHeadPrice() == null ? 0 : entity.getHeadPrice());
			map.put("continuePrice", entity.getContinuedPrice() == null ? 0
					: entity.getContinuedPrice());
			map.put("amount",
					entity.getAmount() == null ? 0 : entity.getAmount());
			map.put("carrierCalStatus", getCalInfo(entity.getDispatchCal()));
			map.put("carrierRemark", entity.getDispatchRemark());
			map.put("orderOperatorAmount",
					entity.getOrderOperatorAmount() == null ? "0" : entity
							.getOrderOperatorAmount());
			map.put("orderCalStatus", getCalInfo(entity.getStorageCal()));
			map.put("orderRemark", entity.getStorageRemark());
			map.put("dutyType", entity.getDutyType());
			map.put("reasonDetail", entity.getUpdateReasonDetail());
			list.add(map);
			yunfei = yunfei
					+ (entity.getAmount() == null ? 0 : entity.getAmount());
			caozao = caozao
					+ Double.valueOf(entity.getOrderOperatorAmount() == null ? "0"
							: entity.getOrderOperatorAmount());
		}
		Map<String, Object> finalMap = Maps.newHashMap();
		finalMap.put("continuePrice", "合计金额：");
		finalMap.put("amount", yunfei);
		finalMap.put("orderOperatorAmount", caozao);
		list.add(finalMap);
		return list;
	}

	/**
	 * 宅配
	 * 
	 * @param xssfWorkbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws Exception
	 */
	private void handDispatch(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil,
			Map<String, Object> condition, String filePath) throws Exception {
		int pageNo = 1;
		boolean doLoop = true;
		List<FeesReceiveDispatchEntity> dataList = new ArrayList<FeesReceiveDispatchEntity>();
		while (doLoop) {
			PageInfo<FeesReceiveDispatchEntity> pageList = feesReceiveDispatchService
					.querydistributionDetailByBizData(condition, pageNo, PAGESIZE);
			if (null != pageList && pageList.getList() != null
					&& pageList.getList().size() > 0) {
				if (pageList.getList().size() < PAGESIZE) {
					doLoop = false;
				} else {
					pageNo += 1;
				}
				dataList.addAll(pageList.getList());
			} else {
				doLoop = false;
			}
		}

		if (dataList.size() == 0) {// 添加默认数据
			Sheet sheet = poiUtil.getXSSFSheet(xssfWorkbook, "宅配");
			CellStyle cs = xssfWorkbook.createCellStyle();
			Font font = xssfWorkbook.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(font.BOLDWEIGHT_BOLD);
			cs.setFont(font);
			cs.setAlignment(cs.ALIGN_LEFT);

			Row r = sheet.createRow(0);
			Cell c = r.createCell(0);
			c.setCellValue("这段时间没有产生运单");
			c.setCellStyle(cs);
			return;
		}
		List<Map<String, Object>> headMap = getDispathHeadMap();

		List<Map<String, Object>> itemMap = getDispathItemMap(dataList);
		poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook, "宅配", headMap,
				itemMap);
	}

	/**
	 * 预账单仓储
	 * @param workbook
	 * @param parameter
	 * @param poiUtil
	 * @param warehouseList
	 */
	private void handStorage(SXSSFWorkbook workbook, Map<String, Object> parameter,
			POISXSSUtil poiUtil, List<String> warehouseList) {
		for (String warehouseCode : warehouseList) {
			int conIndex = 0;
			parameter.put("warehouseCode", warehouseCode);

			Set<Timestamp> set = new HashSet<Timestamp>();

			// 商品按托存储
			List<FeesReceiveStorageEntity> palletList = feesReceiveStorageService.queryPreBillStorage(parameter);
			for (FeesReceiveStorageEntity entity : palletList) {
				conIndex++;
				if (!set.contains(entity.getCreateTime())) {
					set.add(entity.getCreateTime());
				}
			}

			// 耗材按托存储
			List<FeesReceiveStorageEntity> packList = feesReceiveStorageService.queryPreBillMaterialStorage(parameter);// 耗材存储
			for (FeesReceiveStorageEntity entity : packList) {
				conIndex++;
				if (!set.contains(entity.getCreateTime())) {
					set.add(entity.getCreateTime());
				}
			}

			if (conIndex == 0) {
				continue;
			}
			
			// st
			Sheet sheet = workbook.createSheet(mapWarehouse.get(warehouseCode));

			Font font = workbook.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setWrapText(true);
			style.setFont(font);

			Row row0 = sheet.createRow(0);
			row0.setHeight((short) (20 * 20));
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("序号");
			cell0.setCellStyle(style);
			Cell cell1 = row0.createCell(1);
			cell1.setCellValue("日期");
			cell1.setCellStyle(style);
			Cell cell2 = row0.createCell(2);
			cell2.setCellValue("库存板数");
			cell2.setCellStyle(style);
			Cell cell7 = row0.createCell(7);
			cell7.setCellStyle(style);
			cell7.setCellValue("库存件数");
			Cell cell8 = row0.createCell(8);
			cell8.setCellStyle(style);
			cell8.setCellValue("入库板数");
			Cell cell9 = row0.createCell(9);
			cell9.setCellStyle(style);
			cell9.setCellValue("入库件数");
			Cell cell10 = row0.createCell(10);
			cell10.setCellStyle(style);
			cell10.setCellValue("入库重量");
			Cell cell11 = row0.createCell(11);
			cell11.setCellValue("出库板数");
			cell11.setCellStyle(style);
			Cell cell12 = row0.createCell(12);
			cell12.setCellValue("2B出库板数");
			cell12.setCellStyle(style);
			Cell cell13 = row0.createCell(13);
			cell13.setCellValue("2B出库重量");
			cell13.setCellStyle(style);
			Cell cell14 = row0.createCell(14);
			cell14.setCellValue("出库件数");
			cell14.setCellStyle(style);
			Cell cell15 = row0.createCell(15);
			cell15.setCellValue("出库订单数");
			cell15.setCellStyle(style);
			Cell cell16 = row0.createCell(16);
			cell16.setCellValue("撤销订单数");
			cell16.setCellStyle(style);
			Cell cell17 = row0.createCell(17);
			cell17.setCellValue("装卸费/吨/元");
			cell17.setCellStyle(style);
			Cell cell18 = row0.createCell(19);
			cell18.setCellValue("仓储费/托/元");
			cell18.setCellStyle(style);
			Cell cell19 = row0.createCell(24);
			cell19.setCellValue("操作费/单/元");
			cell19.setCellStyle(style);
			Cell cell20 = row0.createCell(26);
			cell20.setCellValue("收入合计");
			cell20.setCellStyle(style);

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 8, 8));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 11, 11));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 12, 12));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 13, 13));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 15, 15));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 16, 16));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 18));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 19, 23));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 24, 25));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 26, 26));

			Row row1 = sheet.createRow(1);
			row1.setHeight((short) (25 * 20));

			Cell cell22 = row1.createCell(2);
			cell22.setCellValue("冷冻");
			cell22.setCellStyle(style);

			Cell cell23 = row1.createCell(3);
			cell23.setCellValue("冷藏");
			cell23.setCellStyle(style);

			Cell cell24 = row1.createCell(4);
			cell24.setCellValue("恒温");
			cell24.setCellStyle(style);

			Cell cell25 = row1.createCell(5);
			cell25.setCellValue("常温");
			cell25.setCellStyle(style);

			Cell cell26 = row1.createCell(6);
			cell26.setCellValue("包材");
			cell26.setCellStyle(style);

			Cell cellk24 = row1.createCell(17);
			cellk24.setCellValue("入库装卸费");
			cellk24.setCellStyle(style);

			Cell cellk25 = row1.createCell(18);
			cellk25.setCellValue("2B入库装卸费");
			cellk25.setCellStyle(style);

			Cell cellk26 = row1.createCell(19);
			cellk26.setCellValue("冷冻费小计/元");
			cellk26.setCellStyle(style);

			Cell cellk27 = row1.createCell(20);
			cellk27.setCellValue("冷藏费小计/元");
			cellk27.setCellStyle(style);

			Cell cellk28 = row1.createCell(21);
			cellk28.setCellValue("恒温费小计/元");
			cellk28.setCellStyle(style);

			Cell cellk29 = row1.createCell(22);
			cellk29.setCellValue("常温费小计/元");
			cellk29.setCellStyle(style);

			Cell cellk30 = row1.createCell(23);
			cellk30.setCellValue("包材费小计/元");
			cellk30.setCellStyle(style);

			Cell cellk31 = row1.createCell(24);
			cellk31.setCellValue("操作费/元");
			cellk31.setCellStyle(style);

			Cell cellk32 = row1.createCell(25);
			cellk32.setCellValue("撤销订单费/元");
			cellk32.setCellStyle(style);

			sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 17, 17));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 18, 18));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 19, 19));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 20, 20));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 21, 21));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 22, 22));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 23, 23));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 24, 24));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 25, 25));
			
			Row row3 = sheet.createRow(3);
			row3.setHeight((short) (20 * 20));
			
			Cell cell30 = row3.createCell(0);
			cell30.setCellValue("上月结余");
			cell30.setCellStyle(style);
			
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 8));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 9, 9));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 10, 10));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 11));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 12, 12));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 13, 13));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 15, 15));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 16, 16));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 17));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 18, 18));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 19, 19));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 20, 20));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 21, 21));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 22, 22));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 23, 23));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 24, 24));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 25, 25));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 26, 26));
			// end

			FastDateFormat sdf = FastDateFormat.getInstance("yyyy/MM/dd");
			List<Timestamp> dateList = new ArrayList<Timestamp>(set);

			int rowIndex = 4;
			double totalcost = 0.0;
			double packcost = 0.0;
			double ldcost = 0.0;
			double lccost = 0.0;
			double hwcost = 0.0;
			double cwcost = 0.0;
			double zxfcost = 0.0;
			double xhfcost = 0.0;

			for (int i = 0; i < dateList.size(); i++) {
				double rowCost = 0.0;
				Timestamp timestamp = dateList.get(i);
				String createTime = sdf.format(timestamp);
				Row row = sheet.createRow(rowIndex);
				rowIndex++;

				// 序号
				Cell cell40 = row.createCell(0);
				cell40.setCellValue(i + 1);
				// 日期
				Cell cell41 = row.createCell(1);
				cell41.setCellValue(createTime);

				// 库存板数
				for (FeesReceiveStorageEntity entity : palletList) {
					if (entity.getCreateTime().equals(timestamp)) {
						String tempretureType = entity.getTempretureType();
						Integer quantity = entity.getQuantity();
						// 托数（冷冻、冷藏、恒温、常温）
						if ("LD".equals(tempretureType)) {
							Cell cell42 = row.createCell(2);
							cell42.setCellValue(quantity);
						} else if ("LC".equals(tempretureType)) {
							Cell cell42 = row.createCell(3);
							cell42.setCellValue(quantity);
						} else if ("HW".equals(tempretureType)) {
							Cell cell42 = row.createCell(4);
							cell42.setCellValue(quantity);
						} else if ("CW".equals(tempretureType)) {
							Cell cell42 = row.createCell(5);
							cell42.setCellValue(quantity);
						}

						// 列小计
						double cost = entity.getCost().doubleValue();
						if ("LD".equals(tempretureType)) {
							Cell cell49 = row.createCell(19);
							cell49.setCellValue(cost);
							ldcost = ldcost	+ cost;
						} else if ("LC".equals(tempretureType)) {
							Cell cell49 = row.createCell(20);
							cell49.setCellValue(cost);
							lccost = lccost	+ cost;
						} else if ("HW".equals(tempretureType)) {
							Cell cell49 = row.createCell(21);
							cell49.setCellValue(cost);
							hwcost = hwcost	+ cost;
						} else if ("CW".equals(tempretureType)) {
							Cell cell49 = row.createCell(22);
							cell49.setCellValue(cost);
							cwcost = cwcost	+ cost;
						}
						// 行小计
						rowCost = rowCost + cost;
					}
				}

				// 耗材
				for (FeesReceiveStorageEntity entity : packList) {
					if (entity.getCreateTime().equals(timestamp)) {
						Cell cell46 = row.createCell(6);
						cell46.setCellValue(entity.getQuantity());
						
						double materialCost = entity.getCost().doubleValue();
						Cell cell49 = row.createCell(23);
						cell49.setCellValue(materialCost);

						rowCost = rowCost + materialCost;
						packcost = packcost + materialCost;
					}
				}

				// 总计
				totalcost = rowCost + totalcost;
				// 行小计
				Cell cell49 = row.createCell(26);
				cell49.setCellValue(rowCost);
			}

			Row row = sheet.createRow(rowIndex);
			Cell cellLast0 = row.createCell(19);
			cellLast0.setCellValue(ldcost);

			Cell cellLast1 = row.createCell(20);
			cellLast1.setCellValue(lccost);

			Cell cellLast6 = row.createCell(21);
			cellLast6.setCellValue(hwcost);

			Cell cellLast2 = row.createCell(22);
			cellLast2.setCellValue(cwcost);

			Cell cellLast3 = row.createCell(23);
			cellLast3.setCellValue(packcost);

			Cell cellLast4 = row.createCell(18);
			cellLast4.setCellValue(zxfcost);

			Cell cellLast5 = row.createCell(17);
			cellLast5.setCellValue(xhfcost);

			Cell cellLast = row.createCell(26);
			cellLast.setCellValue(totalcost);
		}
	}
	
	/**
	 * 预账单增值
	 * @param workbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws IOException 
	 */
	private void handAdd(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition, String filePath) throws IOException{
		int pageNo = 1;
		int addLineNo = 1;
		boolean doLoop = true;
		totalAmount.set(0d);// 置零
		
		//仓储增值类型
		Map<String, String> subjectMap = bmsGroupSubjectService.getExportSubject("receive_wh_valueadd_subject");
		
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> pageList = feesReceiveStorageService
					.queryPreBillStorageAddFee(condition, pageNo, PAGESIZE);
			if (null != pageList && pageList.getList() != null
					&& pageList.getList().size() > 0) {
				if (pageList.getList().size() < PAGESIZE) {
					doLoop = false;
				} else {
					pageNo += 1;
				}
			} else {
				doLoop = false;
				return;
			}
			
			List<Map<String, Object>> headAddList = getAddHead();
			List<Map<String, Object>> dataAddList = getAddItem(pageList.getList(), subjectMap);
			if (!doLoop) {
				// 添加合计
				dataAddList.addAll(getAddSumItem());
			}
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, "增值", 
					addLineNo, headAddList, dataAddList);
			if (null != pageList && pageList.getList().size() > 0) {
				addLineNo += pageList.getList().size();
			}
		}
	}
	
	/**
	 * 增值-title
	 */
	private List<Map<String, Object>> getAddHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "客户名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值项目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "subjectCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值服务内容");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "serviceContent");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quality");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单位");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unit");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	/**
	 * 增值-content
	 */
	private List<Map<String, Object>> getAddItem(List<FeesReceiveStorageEntity> list,Map<String, String> dictcodeMap){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> dataItem = null;
		double t_amount = 0d;
		for (FeesReceiveStorageEntity entity : list) {
			dataItem = new HashMap<String, Object>();
			dataItem.put("createTime", sdf.format(entity.getCreateTime()));
			dataItem.put("warehouseName", entity.getWarehouseName());
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("subjectCode", entity.getOtherSubjectCode()==null?"":dictcodeMap.get(entity.getOtherSubjectCode()));
			dataItem.put("serviceContent", entity.getServiceContent());
			dataItem.put("quality", entity.getQuantity());
			dataItem.put("unit", entity.getUnit());
			dataItem.put("unitPrice", entity.getUnitPrice());
			double amount = entity.getCost().doubleValue();
			t_amount += amount;
			dataItem.put("amount", amount);
			dataItem.put("remark", entity.getRemarkContent());
			dataList.add(dataItem);
		}
		totalAmount.set(totalAmount.get() + t_amount);
		return dataList;
	}
	
	/**
	 * 增值-合计
	 */
	private List<Map<String, Object>> getAddSumItem(){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> dataItem = new HashMap<String, Object>();
		dataItem.put("unitPrice", "合计金额");
		dataItem.put("amount", totalAmount.get());
		dataList.add(dataItem);
		return dataList;
	}
	
	/**
	 * 理赔
	 * @param workbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws Exception
	 */
	private void handAbnormal(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition, String filePath)throws Exception{
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		totalAmount.set(0d);// 置零
		totalDeliveryCost.set(0d);
		
		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = 
					feesAbnormalService.queryPreBillAbnormal(condition, pageNo, PAGESIZE);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < PAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headAbnormalList = getAbnormalHead();
			List<Map<String, Object>> dataAbnormalList = getAbnormalItem(abnormalList.getList());
			if (!doLoop) {
				// 添加合计
				dataAbnormalList.addAll(getAbnormalSumItem());
			}
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, "理赔", 
					abnormalLineNo, headAbnormalList, dataAbnormalList);
			if (null != abnormalList &&abnormalList.getList().size() > 0) {
				abnormalLineNo += abnormalList.getList().size();
			}
		}
	} 

	/**
	 * 理赔-title
	 */
	private List<Map<String, Object>> getAbnormalHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "发货仓库");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "客户");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔付类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reason");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔款额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliveryCost");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否免运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isDeliveryFree");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "登记人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "creator");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	/**
	 * 理赔-content
	 */
	private List<Map<String, Object>> getAbnormalItem(List<FeesAbnormalEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> dataItem = null;
		double t_amount = 0d;
		double t_deliveryCost = 0d;
		
		for (FeesAbnormalEntity entity : list) {
			dataItem = new HashMap<String, Object>();
			dataItem.put("warehouseName", entity.getWarehouseName());
			dataItem.put("createTime", sdf.format(entity.getCreateTime()));
			dataItem.put("waybillNo", entity.getExpressnum());
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("reason", entity.getReason());
			double amount = (entity.getPayMoney() == null ? 0 : entity.getPayMoney());
			t_amount += amount;
			dataItem.put("payAmount", amount);
			double deliveryCost = (entity.getDeliveryCost() == null ? 0 : entity.getDeliveryCost());
			t_deliveryCost += deliveryCost;
			dataItem.put("deliveryCost", deliveryCost);
			dataItem.put("isDeliveryFree", entity.getIsDeliveryFree());
			dataItem.put("creator", entity.getCreatePersonName());
			dataItem.put("remark", entity.getRemark());
			dataList.add(dataItem);
		}
		
		totalAmount.set(totalAmount.get() + t_amount);
		totalDeliveryCost.set(totalDeliveryCost.get() + t_deliveryCost);
		return dataList;
	}
	
	/**
	 * 理赔-合计
	 */
	private List<Map<String, Object>> getAbnormalSumItem(){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> dataItem = new HashMap<String, Object>();
		dataItem.put("reason", "合计金额");
		dataItem.put("payAmount", totalAmount.get());
		dataItem.put("deliveryCost", totalDeliveryCost.get());
		dataList.add(dataItem);
		return dataList;
	}
	
	
	/**
	 * 改地址
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws IOException 
	 */
	private void handAbnormalChange(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition, String filePath) throws IOException {
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		totalAmount.set(0d);// 置零
		
		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = 
					feesAbnormalService.queryPreBillAbnormalChange(condition, pageNo, PAGESIZE);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < PAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headAbnormalChangeList = getAbnormalChangeHead();
			List<Map<String, Object>> dataAbnormalChangeList = getAbnormalChangeItem(abnormalList.getList());
			if (!doLoop) {
				// 添加合计
				dataAbnormalChangeList.addAll(getAbnormalChangeSumItem());
			}
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, "改地址和退件费", 
					abnormalLineNo, headAbnormalChangeList, dataAbnormalChangeList);
			if (null != abnormalList &&abnormalList.getList().size() > 0) {
				abnormalLineNo += abnormalList.getList().size();
			}
		}
	}
	
	/**
	 * 理赔改地址-title
	 */
	private List<Map<String, Object>> getAbnormalChangeHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "运单日期");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "createTime");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "运单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "waybillNo");
		headInfoList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "退回单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "returnWaybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "退回金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "承运商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "登记人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "creator");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	/**
	 * 理赔-content
	 */
	private List<Map<String, Object>> getAbnormalChangeItem(List<FeesAbnormalEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> dataItem = null;
		double t_amount = 0d;
		
		for (FeesAbnormalEntity entity : list) {
			dataItem = new HashMap<String, Object>();
			dataItem.put("createTime", sdf.format(entity.getCreateTime()));
			dataItem.put("waybillNo", entity.getExpressnum());
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("returnWaybillNo", "");
			double amount = (entity.getPayMoney() == null ? 0 : entity.getPayMoney());
			t_amount += amount;
			dataItem.put("payAmount", amount);
			dataItem.put("carrierName", entity.getCarrierName());
			dataItem.put("creator", entity.getCreatePersonName());
			dataItem.put("remark", entity.getRemark());
			dataList.add(dataItem);
		}
		
		totalAmount.set(totalAmount.get() + t_amount);
		return dataList;
	}
	
	/**
	 * 理赔-合计
	 */
	private List<Map<String, Object>> getAbnormalChangeSumItem(){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> dataItem = new HashMap<String, Object>();
		dataItem.put("returnWaybillNo", "合计金额");
		dataItem.put("payAmount", totalAmount.get());
		dataList.add(dataItem);
		return dataList;
	}
	
	
	/**
	 * 修改导出任务列表
	 * 
	 * @param taskId
	 * @param process
	 * @param taskState
	 */
	private void updateExportTask(String taskId, String taskState,
			double process) {
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		fileExportTaskService.update(entity);
	}

	private String getPath() {
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode(
				"GLOABL_PARAM", "EXPORT_RECEIVE_BILL");
		if (systemCodeEntity == null) {
			throw new BizException(
					"请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIVE_BILL");
		}
		return systemCodeEntity.getExtattr1();
	}

	private boolean checkFileHasDownLoad(Map<String, Object> queryEntity) {
		return fileExportTaskService.checkFileHasDownLoad(queryEntity);
	}
	
	public List<String> queryPreBillWarehouse(Map<String, Object> param) {
		return feesReceiveStorageService.queryPreBillWarehouse(param);
	}
	
	private Map<String,String> getwarehouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	

	private String getCalInfo(String status) {
		if (StringUtils.isBlank(status)) {
			return "未计算";
		}
		Map<String, String> map = CalculateState.getMap();
		if (map.containsKey(status)) {
			return map.get(status);
		} else {
			return status;
		}
	}
	
	private void setColumnWeight(Sheet sheet, int len, int weight){
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				sheet.setColumnWidth(i, (short)((weight * 8) / ((double) 1 / 20)));
			}
		}
	}
}