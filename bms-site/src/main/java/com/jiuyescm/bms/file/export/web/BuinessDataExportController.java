package com.jiuyescm.bms.file.export.web;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;
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

	private static final Logger logger = Logger.getLogger(BuinessDataExportController.class.getName());
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
	@Resource
	private IBmsBizInstockInfoService bmsBizInstockInfoService;

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
	public void queryTask(Page<FileExportTaskEntity> page, Map<String, Object> param) throws Exception {
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
		PageInfo<FileExportTaskEntity> pageInfo = fileExportTaskService.queryBillTask(param, page.getPageNo(),
				page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String asynExport(Map<String, Object> param) throws ParseException {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		String year = "";
		String month = "";
		if (param.containsKey("year") && param.containsKey("month")) {
			year = param.get("year").toString();
			month = param.get("month").toString();
		}
		if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
			String startDateStr = year + "-" + month + "-01 00:00:00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf.parse(startDateStr);
			//Date endDate = DateUtils.addMonths(startDate, 1);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(year));
			cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
			Date endDate = cal.getTime();
			param.put("startDate", startDate);
			param.put("endDate", endDate);
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
			queryEntity.put("taskType", FileTaskTypeEnum.BILL_RE_DOWN.getCode());
			if (checkFileHasDownLoad(queryEntity)) {
				return MessageConstant.BILL_FILE_ISEXIST_MSG;
			}
			DateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String path = getPath();
			String filePath = path + FileConstant.SEPARATOR + param.get("customerName").toString() + "-"
					+ sdf.format(startDate) + "-预账单" + FileConstant.SUFFIX_XLSX;
			FileExportTaskEntity entity = new FileExportTaskEntity();

			entity.setTaskName(param.get("customerName").toString() + "-" + sdf.format(startDate) + "-预账单");
			entity.setBillNo("");
			entity.setStartTime(Timestamp.valueOf(startTime + " 00:00:00"));
			entity.setEndTime(Timestamp.valueOf(format.format(date) + " 00:00:00"));
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
						fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
						logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
						// 写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity = new BmsErrorLogInfoEntity(
								this.getClass().getSimpleName(),
								Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					}
				};
			}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			// 写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity = new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(), "启动线程失败", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return "正在生成预账单，稍后可进行下载";
	}

	/**
	 * 预账单导出
	 * 
	 * @param condition
	 * @param taskId
	 * @param path
	 * @param filePath
	 * @throws Exception
	 */
	protected void export(Map<String, Object> condition, String taskId, String path, String filePath) throws Exception {
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);

		// 初始化参数
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
		logger.info("====预账单导出：" + "[" + condition.get("customerName").toString() + "]" + "写入Excel begin.");

		// 配送费
		handDispatch(xssfWorkbook, poiUtil, condition, filePath);
				
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 50);

		List<String> warehouseList = queryPreBillWarehouse(condition);
		Map<String, String> temMap=getTemperatureTypeList();
		// 存储费
		handStorage(xssfWorkbook, condition, poiUtil, warehouseList);
		//出库(TB)
		handOutstock(xssfWorkbook, poiUtil, condition, filePath,temMap);
		//入库
		handInstock(xssfWorkbook, poiUtil, condition, filePath);
		
		// 耗材费(是否分仓)
		if ((Boolean)condition.get("isSepWarehouse") == true) {
			handMaterial(xssfWorkbook, poiUtil, condition, filePath);
		}else {			
			handMaterialNotSepWareHouse(xssfWorkbook, poiUtil, condition, filePath);
		}

		// 增值
		handAdd(xssfWorkbook, poiUtil, condition, filePath);

		// 理赔
		handAbnormal(xssfWorkbook, poiUtil, condition, filePath);
		// 改地址和退件费
		handAbnormalChange(xssfWorkbook, poiUtil, condition, filePath);

		poiUtil.write2FilePath(xssfWorkbook, filePath);
		updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
		logger.info("====预账单导出：" + "[" + condition.get("customerName").toString() + "]" + "写入Excel end.==总耗时："
				+ (System.currentTimeMillis() - beginTime));
	}
	
	private void handInstock(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil, Map<String, Object> condition,
			String filePath) throws IOException {
		int pageNo = 1;
		int instockLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> instockList = bmsBizInstockInfoService.queryForBill(condition, pageNo, PAGESIZE);
			if (null != instockList && instockList.getList().size() > 0) {
				if (instockList.getList().size() < PAGESIZE) {
					doLoop = false;
				} else {
					pageNo += 1;
				}
			} else {
				doLoop = false;
			}

			List<Map<String, Object>> headInstockList = getInstockHead();
			List<Map<String, Object>> dataInstockList = getInstockItem(instockList.getList());

			if (!(pageNo == 1 && (instockList == null || instockList.getList().size() <= 0))) {
				poiUtil.exportExcel2FilePath(poiUtil, xssfWorkbook, "入库", instockLineNo, headInstockList, dataInstockList);
				if (null != instockList && instockList.getList().size() > 0) {
					instockLineNo += instockList.getList().size();
				}
			}
		}
	}
	
	/**
	 * 入库-title
	 */
	private List<Map<String, Object>> getInstockHead() {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "收货确认时间");
		itemMap.put("columnWidth", 35);
		itemMap.put("dataKey", "instockDate");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家名称");
		itemMap.put("columnWidth", 40);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "单据类型");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "instockType");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "入库单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "instockNo");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "入库件数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalQty");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "入库箱数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalBox");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "入库重量(吨)");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalWeight");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "入库操作费");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "instockAmount");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "入库卸货费");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "b2cAmount");
		headInfoList.add(itemMap);

		return headInfoList;
	}
	
	/**
	 * 入库-content
	 */
	private List<Map<String, Object>> getInstockItem(List<FeesReceiveStorageEntity> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		DateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> dataItem = null;
		for (FeesReceiveStorageEntity entity : list) {
			if (map.containsKey(entity.getInstockNo())) {
				if ("wh_instock_work".equals(entity.getSubjectCode())) {
					map.get(entity.getInstockNo()).put("instockAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
				}else if ("wh_b2c_handwork".equals(entity.getSubjectCode())) {
					map.get(entity.getInstockNo()).put("b2cAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
				}
			}else {
				dataItem = new HashMap<String, Object>();
				dataItem.put("warehouseName", entity.getWarehouseName());
				dataItem.put("instockDate", sdff.format(entity.getInstockDate()));
				dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("instockType", entity.getInstockType());
				dataItem.put("instockNo", entity.getInstockNo());
				dataItem.put("totalQty", entity.getQuantity());
				dataItem.put("totalBox", entity.getBox());
				dataItem.put("totalWeight", (double)entity.getWeight()/1000);
				if ("wh_instock_work".equals(entity.getSubjectCode())) {
					dataItem.put("instockAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
				}else if ("wh_b2c_handwork".equals(entity.getSubjectCode())) {
					dataItem.put("b2cAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
				}
				map.put(entity.getInstockNo(), dataItem);
				dataList.add(dataItem);
			}			
		}
		return dataList;
	}

	private Date addDay(int days, Date dt) {
		Calendar now = Calendar.getInstance();
		now.setTime(dt);
		now.add(Calendar.DATE, 1);
		Date dt1 = now.getTime();
		return dt1;
	}

	private Map<String, String> mapWarehouse;

	private void init() {
		mapWarehouse = getwarehouse();
	}

	private Map<String, String> getHeadMap(List<String> materialCodeList, List<PubMaterialInfoVo> materialInfoList) {
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

		// 按新增时间获取systemCode表中的类别并排序
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
			map.put(marterialTypeDetail + "_code", "编码");
			map.put(marterialTypeDetail + "_type", "规格");
			if (((String) codeMap.get(marterialTypeDetail)).contains("GB")) {
				map.put(marterialTypeDetail + "_count", "重量");
			} else {
				map.put(marterialTypeDetail + "_count", "数量");
			}
			map.put(marterialTypeDetail + "_unitprice", "单价");
			map.put(marterialTypeDetail + "_cost", "金额");
		}
		map.put("totalCost", "合计");
		return map;
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

	private List<String> getMaterialCodeList(List<BizOutstockPackmaterialEntity> ListHead) {
		List<String> materialCodeList = new ArrayList<String>();
		for (BizOutstockPackmaterialEntity entity : ListHead) {
			materialCodeList.add(entity.getConsumerMaterialCode());
		}
		return materialCodeList;
	}

	/**
	 * 耗材(分仓)
	 * 
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
											: Double.valueOf(materialEntity.getWeight()));
						} else {
							matchMap.put(
									marterialType + "_count",
									matchMap.get(marterialType + "_count")
											+ ","
											+ materialEntity.getQuantity() == null ? ""
											: Double.valueOf(materialEntity.getQuantity()));
						}
						matchMap.put(marterialType + "_cost",
								matchMap.get(marterialType + "_cost") + ","
										+ materialEntity.getCost() == null ? ""
										: Double.valueOf(materialEntity.getCost()));
						double totleCost = matchMap.get("totalCost") == null ? 0d
								: Double.parseDouble(matchMap.get("totalCost")
										.toString());
						totleCost += materialEntity.getCost() == null ? 0d
								: Double.valueOf(materialEntity.getCost());
						matchMap.put("totalCost", totleCost);// 金额
					} else {
						matchMap.put(marterialType + "_name",
								materialEntity.getProductName() == null ? ""
										: materialEntity.getProductName());
						if (materialEntity.getProductNo().contains("GB")) {
							matchMap.put(marterialType + "_count",
									materialEntity.getWeight() == null ? ""
											: Double.valueOf(materialEntity.getWeight()));
						} else {
							matchMap.put(marterialType + "_count",
									materialEntity.getQuantity() == null ? ""
											: Double.valueOf(materialEntity.getQuantity()));
						}
						matchMap.put(marterialType + "_code",
								materialEntity.getProductNo());
						matchMap.put(marterialType + "_type",
								materialEntity.getSpecDesc());
						matchMap.put(marterialType + "_unitprice",
								materialEntity.getUnitPrice() == null ? ""
										: Double.valueOf(materialEntity.getUnitPrice()));
						matchMap.put(marterialType + "_cost", materialEntity
								.getCost() == null ? "" : Double.valueOf(materialEntity.getCost()));
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
								.getWeight() == null ? "" : Double.valueOf(materialEntity
								.getWeight()));
					} else {
						dataItem.put(marterialType + "_count", materialEntity
								.getQuantity() == null ? "" : Double.valueOf(materialEntity
								.getQuantity()));
					}
					dataItem.put(marterialType + "_unitprice", materialEntity
							.getUnitPrice() == null ? "" : Double.valueOf(materialEntity
							.getUnitPrice()));
					dataItem.put(marterialType + "_cost", materialEntity
							.getCost() == null ? "" :Double.valueOf(materialEntity.getCost()));
					dataItem.put("totalCost", materialEntity.getCost());// 金额
					dataPackMaterialList.add(dataItem);
				}

			}
			poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook,
					entity.getWarehouseName() + "耗材使用费",
					headPackMaterialMapList, dataPackMaterialList);
		}

	}
	
	/**
	 * 耗材(不分仓)
	 * 
	 * @param xssfWorkbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws Exception
	 */
	private void handMaterialNotSepWareHouse(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil,
			Map<String, Object> condition, String filePath) throws Exception {
		List<Map<String, Object>> dataPackMaterialList = null;
		List<Map<String, Object>> headPackMaterialMapList = null;
		
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
			headPackMaterialMapList = getHeadPackMaterialMap(
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

			dataPackMaterialList = new ArrayList<Map<String, Object>>();
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
											: Double.valueOf(materialEntity.getWeight()));
						} else {
							matchMap.put(
									marterialType + "_count",
									matchMap.get(marterialType + "_count")
											+ ","
											+ materialEntity.getQuantity() == null ? ""
											: Double.valueOf(materialEntity.getQuantity()));
						}
						matchMap.put(marterialType + "_cost",
								matchMap.get(marterialType + "_cost") + ","
										+ materialEntity.getCost() == null ? ""
										: Double.valueOf(materialEntity.getCost()));
						double totleCost = matchMap.get("totalCost") == null ? 0d
								: Double.parseDouble(matchMap.get("totalCost")
										.toString());
						totleCost += materialEntity.getCost() == null ? 0d
								: Double.valueOf(materialEntity.getCost());
						matchMap.put("totalCost", totleCost);// 金额
					} else {
						matchMap.put(marterialType + "_name",
								materialEntity.getProductName() == null ? ""
										: materialEntity.getProductName());
						if (materialEntity.getProductNo().contains("GB")) {
							matchMap.put(marterialType + "_count",
									materialEntity.getWeight() == null ? ""
											: Double.valueOf(materialEntity.getWeight()));
						} else {
							matchMap.put(marterialType + "_count",
									materialEntity.getQuantity() == null ? ""
											: Double.valueOf(materialEntity.getQuantity()));
						}
						matchMap.put(marterialType + "_code",
								materialEntity.getProductNo());
						matchMap.put(marterialType + "_type",
								materialEntity.getSpecDesc());
						matchMap.put(marterialType + "_unitprice",
								materialEntity.getUnitPrice() == null ? ""
										: Double.valueOf(materialEntity.getUnitPrice()));
						matchMap.put(marterialType + "_cost", materialEntity
								.getCost() == null ? "" : Double.valueOf(materialEntity.getCost()));
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
								.getWeight() == null ? "" : Double.valueOf(materialEntity
								.getWeight()));
					} else {
						dataItem.put(marterialType + "_count", materialEntity
								.getQuantity() == null ? "" : Double.valueOf(materialEntity
								.getQuantity()));
					}
					dataItem.put(marterialType + "_unitprice", materialEntity
							.getUnitPrice() == null ? "" : Double.valueOf(materialEntity
							.getUnitPrice()));
					dataItem.put(marterialType + "_cost", materialEntity
							.getCost() == null ? "" :Double.valueOf(materialEntity.getCost()));
					dataItem.put("totalCost", materialEntity.getCost());// 金额
					dataPackMaterialList.add(dataItem);
				}

			}
		}
		if (headPackMaterialMapList != null && dataPackMaterialList != null) {
			poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook,"耗材使用费",
					headPackMaterialMapList, dataPackMaterialList);
		}		
	}

	private List<PubMaterialInfoVo> getAllMaterial() {
		Map<String, Object> conditionMap = Maps.newHashMap();
		conditionMap.put("delFlag", 0);
		return pubMaterialInfoService.queryList(conditionMap);
	}

	private String getMaterialType(List<PubMaterialInfoVo> materialInfoList, String materialCode) {
		for (PubMaterialInfoVo infoVo : materialInfoList) {
			if (StringUtils.isNotBlank(infoVo.getBarcode()) && infoVo.getBarcode().equals(materialCode)) {
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
		headMapDict.put("discountAmount", "折扣后运费");
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

	private List<Map<String, Object>> getDispathItemMap(List<FeesReceiveDispatchEntity> dataList) {
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
			map.put("createTime", entity.getCreateTime() == null ? "" : sdf.format(entity.getCreateTime()));
			map.put("zexpressNum", entity.getZexpressnum());
			map.put("totalWeight", entity.getTotalWeight() == null ? 0 : entity.getTotalWeight());
			map.put("totalQty", entity.getTotalQuantity() == null ? 0 : entity.getTotalQuantity());
			map.put("productDetail", entity.getProductDetail());
			map.put("carrierName", entity.getCarrierName());
			map.put("receiver", entity.getReceiveName());
			map.put("receiveProvice", entity.getToProvinceName());
			map.put("receiveCity", entity.getToCityName());
			map.put("receiveDistrict", entity.getToDistrictName());
			map.put("temperatureType", entity.getTemperatureType());
			map.put("chargeWeight", entity.getChargedWeight() == null ? 0 : entity.getChargedWeight());
			map.put("headPrice", entity.getHeadPrice() == null ? 0 : entity.getHeadPrice());
			map.put("continuePrice", entity.getContinuedPrice() == null ? 0 : entity.getContinuedPrice());
			map.put("amount", entity.getAmount() == null ? 0 : entity.getAmount());
			map.put("discountAmount", entity.getDiscountAmount() == null ? 0 : entity.getDiscountAmount());
			map.put("carrierCalStatus", getCalInfo(entity.getDispatchCal()));
			map.put("carrierRemark", entity.getDispatchRemark());
			map.put("orderOperatorAmount",
					entity.getOrderOperatorAmount() == null ? 0 : entity
							.getOrderOperatorAmount());
			map.put("orderCalStatus", getCalInfo(entity.getStorageCal()));
			map.put("orderRemark", entity.getStorageRemark());
			map.put("dutyType", entity.getDutyType());
			map.put("reasonDetail", entity.getUpdateReasonDetail());
			list.add(map);
			yunfei = yunfei + (entity.getAmount() == null ? 0 : entity.getAmount());
			caozao = caozao
					+ Double.valueOf(entity.getOrderOperatorAmount() == null ? "0" : entity.getOrderOperatorAmount());
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
	private void handDispatch(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil, Map<String, Object> condition,
			String filePath) throws Exception {
		int pageNo = 1;
		boolean doLoop = true;
		List<FeesReceiveDispatchEntity> dataList = new ArrayList<FeesReceiveDispatchEntity>();
		while (doLoop) {
			PageInfo<FeesReceiveDispatchEntity> pageList = feesReceiveDispatchService
					.querydistributionDetailByBizData(condition, pageNo, PAGESIZE);
			if (null != pageList && pageList.getList() != null && pageList.getList().size() > 0) {
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
		poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook, "宅配", headMap, itemMap);
	}
	
	/**
	 * 出库单
	 * 
	 * @param xssfWorkbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws Exception
	 */
	private void handOutstock(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil, Map<String, Object> condition,
			String filePath,Map<String, String> temMap) throws Exception {
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> pageInfo = 
					feesReceiveStorageService.queryOutStockPage(condition, pageNo, FileConstant.EXPORTPAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getOutHead(); 
			List<Map<String, Object>> dataDetailList = getOutHeadItem(pageInfo.getList(),temMap);
			
			
			if (!(pageNo == 1 && (pageInfo == null || pageInfo.getList().size() <= 0))) {
				poiUtil.exportExcel2FilePath(poiUtil, xssfWorkbook, "TB", lineNo, headDetailMapList, dataDetailList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					lineNo += pageInfo.getList().size();
				}
			}
		}
	}

	/**
	 * 出库单
	 */
	public List<Map<String, Object>> getOutHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发货时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outstockNo");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tempretureType");
        headInfoList.add(itemMap);
	        

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quantity");
        headInfoList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库箱数");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "box");
        headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库重量(吨)");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "weight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "B2B订单操作费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderCost");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库装车费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outHandCost");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	private List<Map<String, Object>> getOutHeadItem(List<FeesReceiveStorageEntity> list,Map<String, String> temMap){
		 	List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String,Map<String, Object>> entityMap=new HashMap<String,Map<String, Object>>();
	        for (FeesReceiveStorageEntity entity : list) {
	        	if(!entityMap.containsKey(entity.getOutstockNo())){
	        		dataItem = new HashMap<String, Object>();
		        	dataItem.put("warehouseName", entity.getWarehouseName());
		        	dataItem.put("createTime", entity.getCreateTime());
		        	dataItem.put("customerName", entity.getCustomerName() );
		        	dataItem.put("orderType", entity.getOrderType());
		        	dataItem.put("outstockNo", entity.getOutstockNo());
		        	dataItem.put("tempretureType", temMap.get(entity.getTempretureType()));
		        	dataItem.put("quantity", entity.getQuantity());
		        	dataItem.put("box", entity.getBox());
		        	dataItem.put("weight", (double)entity.getWeight()/1000);
	        		//B2B订单操作费（区分是订单操作费还是出库装车费）
	        		if("wh_b2b_work".equals(entity.getSubjectCode())){
	        			dataItem.put("orderCost", entity.getCost());
	        		}else if("wh_b2b_handwork".equals(entity.getSubjectCode())){
	        			dataItem.put("outHandCost", entity.getCost());
	        		}
		        	dataList.add(dataItem);
		        	entityMap.put(entity.getOutstockNo(), dataItem);
	        	}else{
	        		Map<String, Object> data=entityMap.get(entity.getOutstockNo());
	        		//B2B订单操作费（区分是订单操作费还是出库装车费）
	        		if("wh_b2b_work".equals(entity.getSubjectCode())){
	        			data.put("orderCost", entity.getCost());
	        		}else if("wh_b2b_handwork".equals(entity.getSubjectCode())){
	        			data.put("outHandCost", entity.getCost());
	        		}
	        	}	
			}	        
		return dataList;
	}
	
	
	

	/**
	 * 预账单仓储
	 * 
	 * @param workbook
	 * @param parameter
	 * @param poiUtil
	 * @param warehouseList
	 */
	private void handStorage(SXSSFWorkbook workbook, Map<String, Object> parameter, POISXSSUtil poiUtil,
			List<String> warehouseList) {
		for (String warehouseCode : warehouseList) {
			int conIndex = 0;
			int newIndex = 0;
			int move1 = 0;
			int move2 = 0;
			parameter.put("warehouseCode", warehouseCode);

			//
			Set<Timestamp> set = new TreeSet<Timestamp>();

			// 商品按托存储
			List<FeesReceiveStorageEntity> palletList = feesReceiveStorageService.queryPreBillStorage(parameter);
			for (FeesReceiveStorageEntity entity : palletList) {
				conIndex++;
				if (!set.contains(entity.getCreateTime())) {
					set.add(entity.getCreateTime());
				}
			}
			
			// 商品按件存储
			List<FeesReceiveStorageEntity> itemsList = feesReceiveStorageService.queryPreBillStorageByItems(parameter);
			for (FeesReceiveStorageEntity entity : itemsList) {
				conIndex++;
				newIndex++;
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
			
			//如果商品按件存储有数据，展示入库件数/存储费小计列
			if (newIndex > 0) {
				
				Cell cell3 = row0.createCell(8);
				cell3.setCellStyle(style);
				cell3.setCellValue("库存件数");
				
				Cell cell7 = row0.createCell(17);
				cell7.setCellValue("存储费按件小计/元");
				cell7.setCellStyle(style);
				
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 7));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 8, 8));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 16));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 17, 17));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 18, 18));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 19, 19));
				
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 11, 11));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
				
			}else {
				move1 = 1;
				move2 = 2;
				
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 7));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 8, 8));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 15));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 16, 16));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 17, 17));
				
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 10, 10));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 11, 11));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
			}
			
			Cell cell4 = row0.createCell(9-move1);
			cell4.setCellStyle(style);
			cell4.setCellValue("入库板数");
			Cell cell5 = row0.createCell(10-move1);
			cell5.setCellValue("出库板数");
			cell5.setCellStyle(style);
			Cell cell6 = row0.createCell(11-move1);
			cell6.setCellValue("仓储费/托/元");
			cell6.setCellStyle(style);

			Cell cell8 = row0.createCell(18-move2);
			cell8.setCellValue("处置费按件小件/元");
			cell8.setCellStyle(style);
			Cell cell9 = row0.createCell(19-move2);
			cell9.setCellValue("收入合计");
			cell9.setCellStyle(style);

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
			cell26.setCellValue("常温包材");
			cell26.setCellStyle(style);
			
			Cell cell28 = row1.createCell(7);
			cell28.setCellValue("冷冻包材");
			cell28.setCellStyle(style);

			Cell cellk29 = row1.createCell(11-move1);
			cellk29.setCellValue("冷冻费小计/元");
			cellk29.setCellStyle(style);

			Cell cellk30 = row1.createCell(12-move1);
			cellk30.setCellValue("冷藏费小计/元");
			cellk30.setCellStyle(style);

			Cell cellk31 = row1.createCell(13-move1);
			cellk31.setCellValue("恒温费小计/元");
			cellk31.setCellStyle(style);

			Cell cellk32 = row1.createCell(14-move1);
			cellk32.setCellValue("常温费小计/元");
			cellk32.setCellStyle(style);

			Cell cellk33 = row1.createCell(15-move1);
			cellk33.setCellValue("常温包材费小计/元");
			cellk33.setCellStyle(style);
			
			Cell cellk34 = row1.createCell(16-move1);
			cellk34.setCellValue("冷冻包材费小计/元");
			cellk34.setCellStyle(style);

			sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 7, 7));

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
			double ldcost = 0.0;
			double lccost = 0.0;
			double hwcost = 0.0;
			double cwcost = 0.0;
			double ccfcost = 0.0;
			double czfcost = 0.0;
			double cwpackcost = 0.0;
			double ldCost = 0.0;
			double colcost = 0.0;
			double prodcost = 0.0;
			
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
							Cell cell49 = row.createCell(12-move1);
							cell49.setCellValue(cost);
							ldcost = ldcost + cost;
						} else if ("LC".equals(tempretureType)) {
							Cell cell49 = row.createCell(13-move1);
							cell49.setCellValue(cost);
							lccost = lccost + cost;
						} else if ("HW".equals(tempretureType)) {
							Cell cell49 = row.createCell(14-move1);
							cell49.setCellValue(cost);
							hwcost = hwcost + cost;
						} else if ("CW".equals(tempretureType)) {
							Cell cell49 = row.createCell(15-move1);
							cell49.setCellValue(cost);
							cwcost = cwcost + cost;
						}
						// 行小计
						rowCost = rowCost + cost;
					}
				}
				
				Integer index = new Integer(0);
				double costIndex = 0.0;
				double cwCostIndex = 0.0;
				double rowcolCost = 0.0;
				double cwCost = 0.0;
				// 耗材
				for (FeesReceiveStorageEntity entity : packList) {
					
					if (entity.getCreateTime().equals(timestamp)) {
						
						double materialCost = entity.getCost().doubleValue();
						//1.常温--常温
						//2.冷冻--冷冻、冷藏、恒温
						if ("CW".equals(entity.getTempretureType())) {
							Cell cell46 = row.createCell(6);
							cell46.setCellValue(entity.getQuantity());
							
							Cell cell49 = row.createCell(15-move1);
							cell49.setCellValue(materialCost);
							//累加行
							cwCost = cwCost+materialCost;
							//累加列
							cwpackcost = cwpackcost+materialCost;
						}else {
							Cell cell47 = row.createCell(7);
							cell47.setCellValue(entity.getQuantity()+index);
							index = entity.getQuantity()+index;
							
							Cell cell50 = row.createCell(16-move1);
							cell50.setCellValue(materialCost+ldCost);
							//累加行
							ldCost = materialCost+ldCost;
							//累加列
							colcost = colcost + materialCost;
						}
						rowcolCost = cwCost+ldCost;
					}else {
						ldCost = 0.0;
					}
				}
				
				//商品存储费（按件）
				for (FeesReceiveStorageEntity entity : itemsList) {
					if (entity.getCreateTime().equals(timestamp)) {
						//库存件数
						double productCost = entity.getCost().doubleValue();
						Cell cell60 = row.createCell(8);
						cell60.setCellValue(entity.getQuantity());
						//存储费按件小计
						Cell cell61 = row.createCell(17);
						cell61.setCellValue(productCost);
						rowCost = rowCost+productCost;
						ccfcost = ccfcost+productCost;
					}
				}
				
				rowCost = rowCost+rowcolCost;
				// 总计
				totalcost = rowCost + totalcost;
				// 行小计
				Cell cell49 = row.createCell(19-move2);
				cell49.setCellValue(rowCost);
			}

			Row row = sheet.createRow(rowIndex);
			Cell cellLast0 = row.createCell(11-move1);
			cellLast0.setCellValue(ldcost);

			Cell cellLast1 = row.createCell(12-move1);
			cellLast1.setCellValue(lccost);

			Cell cellLast6 = row.createCell(13-move1);
			cellLast6.setCellValue(hwcost);

			Cell cellLast2 = row.createCell(14-move1);
			cellLast2.setCellValue(cwcost);

			Cell cellLast3 = row.createCell(15-move1);
			cellLast3.setCellValue(cwpackcost);
			
			Cell cellLast4 = row.createCell(16-move1);
			cellLast4.setCellValue(colcost);
			
			if (newIndex > 0) {
				Cell cellLast5 = row.createCell(17);
				cellLast5.setCellValue(ccfcost);
			}
			
			Cell cellLast7 = row.createCell(18-move2);
			cellLast7.setCellValue(czfcost);

			Cell cellLast = row.createCell(19-move2);
			cellLast.setCellValue(totalcost);
		}
	}

	/**
	 * 预账单增值
	 * 
	 * @param workbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws IOException
	 */
	private void handAdd(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition, String filePath)
			throws IOException {
		int pageNo = 1;
		int addLineNo = 1;
		boolean doLoop = true;
		totalAmount.set(0d);// 置零

		// 仓储增值类型
		Map<String, String> subjectMap = bmsGroupSubjectService.getExportSubject("receive_wh_valueadd_subject");

		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> pageList = feesReceiveStorageService.queryPreBillStorageAddFee(condition,
					pageNo, PAGESIZE);
			if (null != pageList && pageList.getList() != null && pageList.getList().size() > 0) {
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

			poiUtil.exportExcel2FilePath(poiUtil, workbook, "增值", addLineNo, headAddList, dataAddList);
			if (null != pageList && pageList.getList().size() > 0) {
				addLineNo += pageList.getList().size();
			}
		}
	}

	/**
	 * 增值-title
	 */
	private List<Map<String, Object>> getAddHead() {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
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
	private List<Map<String, Object>> getAddItem(List<FeesReceiveStorageEntity> list, Map<String, String> dictcodeMap) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataItem = null;
		double t_amount = 0d;
		double amount = 0d;
		for (FeesReceiveStorageEntity entity : list) {
			dataItem = new HashMap<String, Object>();
			dataItem.put("createTime", sdf.format(entity.getCreateTime()));
			dataItem.put("warehouseName", entity.getWarehouseName());
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("subjectCode",
					entity.getOtherSubjectCode() == null ? "" : dictcodeMap.get(entity.getOtherSubjectCode()));
			dataItem.put("serviceContent", entity.getServiceContent());
			dataItem.put("quality", entity.getQuantity());
			dataItem.put("unit", entity.getUnit());
			dataItem.put("unitPrice", entity.getUnitPrice());
			if (null != entity.getCost()) {
				amount = entity.getCost().doubleValue();
			}
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
	private List<Map<String, Object>> getAddSumItem() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataItem = new HashMap<String, Object>();
		dataItem.put("unitPrice", "合计金额");
		dataItem.put("amount", totalAmount.get());
		dataList.add(dataItem);
		return dataList;
	}

	/**
	 * 理赔
	 * 
	 * @param workbook
	 * @param poiUtil
	 * @param condition
	 * @param filePath
	 * @throws Exception
	 */
	private void handAbnormal(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition,
			String filePath) throws Exception {
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		totalAmount.set(0d);// 置零
		totalDeliveryCost.set(0d);

		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = feesAbnormalService.queryPreBillAbnormal(condition, pageNo,
					PAGESIZE);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < PAGESIZE) {
					doLoop = false;
				} else {
					pageNo += 1;
				}
			} else {
				doLoop = false;
			}

			List<Map<String, Object>> headAbnormalList = getAbnormalHead();
			List<Map<String, Object>> dataAbnormalList = getAbnormalItem(abnormalList.getList());
			if (!doLoop) {
				// 添加合计
				dataAbnormalList.addAll(getAbnormalSumItem());
			}
			if (!(pageNo == 1 && (abnormalList == null || abnormalList.getList().size() <= 0))) {
				poiUtil.exportExcel2FilePath(poiUtil, workbook, "理赔", abnormalLineNo, headAbnormalList, dataAbnormalList);
				if (null != abnormalList && abnormalList.getList().size() > 0) {
					abnormalLineNo += abnormalList.getList().size();
				}
			}
		}
	}

	/**
	 * 理赔-title
	 */
	private List<Map<String, Object>> getAbnormalHead() {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
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
	private List<Map<String, Object>> getAbnormalItem(List<FeesAbnormalEntity> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
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
	private List<Map<String, Object>> getAbnormalSumItem() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataItem = new HashMap<String, Object>();
		dataItem.put("reason", "合计金额");
		dataItem.put("payAmount", totalAmount.get());
		dataItem.put("deliveryCost", totalDeliveryCost.get());
		dataList.add(dataItem);
		return dataList;
	}

	/**
	 * 改地址
	 * 
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws IOException
	 */
	private void handAbnormalChange(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition,
			String filePath) throws IOException {
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		totalAmount.set(0d);// 置零

		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = feesAbnormalService.queryPreBillAbnormalChange(condition,
					pageNo, PAGESIZE);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < PAGESIZE) {
					doLoop = false;
				} else {
					pageNo += 1;
				}
			} else {
				doLoop = false;
			}

			List<Map<String, Object>> headAbnormalChangeList = getAbnormalChangeHead();
			List<Map<String, Object>> dataAbnormalChangeList = getAbnormalChangeItem(abnormalList.getList());
			if (!doLoop) {
				// 添加合计
				dataAbnormalChangeList.addAll(getAbnormalChangeSumItem());
			}
			if (!(pageNo == 1 && (abnormalList == null || abnormalList.getList().size() <= 0))) {
				poiUtil.exportExcel2FilePath(poiUtil, workbook, "改地址和退件费", abnormalLineNo, headAbnormalChangeList,
						dataAbnormalChangeList);
				if (null != abnormalList && abnormalList.getList().size() > 0) {
					abnormalLineNo += abnormalList.getList().size();
				}
			}
		}
	}

	/**
	 * 理赔改地址-title
	 */
	private List<Map<String, Object>> getAbnormalChangeHead() {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
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
	private List<Map<String, Object>> getAbnormalChangeItem(List<FeesAbnormalEntity> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
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
	private List<Map<String, Object>> getAbnormalChangeSumItem() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
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
	private void updateExportTask(String taskId, String taskState, double process) {
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		fileExportTaskService.update(entity);
	}

	private String getPath() {
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_BILL");
		if (systemCodeEntity == null) {
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIVE_BILL");
		}
		return systemCodeEntity.getExtattr1();
	}

	private boolean checkFileHasDownLoad(Map<String, Object> queryEntity) {
		return fileExportTaskService.checkFileHasDownLoad(queryEntity);
	}

	public List<String> queryPreBillWarehouse(Map<String, Object> param) {
		return feesReceiveStorageService.queryPreBillWarehouse(param);
	}

	private Map<String, String> getwarehouse() {
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String, String>();
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

	/**
	 * 温度类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getTemperatureTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	private void setColumnWeight(Sheet sheet, int len, int weight) {
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				sheet.setColumnWidth(i, (short) ((weight * 8) / ((double) 1 / 20)));
			}
		}
	}
}
