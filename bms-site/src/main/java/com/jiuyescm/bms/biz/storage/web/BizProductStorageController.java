/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.service.IBizProductStorageService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

/**
 * 商品存储费
 * 
 * @author stevenl
 * 
 */
@Controller("bizProductStorageController")
public class BizProductStorageController extends BaseController {
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;

	@Autowired
	private IBizProductStorageService bizProductStorageService;
	@Autowired
	private IFileExportTaskService fileExportTaskService;

	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	private static final Logger logger = LoggerFactory
			.getLogger(BizProductStorageController.class.getName());

	@DataProvider
	public BizProductStorageEntity findById(Long id) throws Exception {
		BizProductStorageEntity entity = null;
		entity = bizProductStorageService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizProductStorageEntity> page,
			Map<String, Object> param) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		if (param.get("startTime") == null) {
			throw new BizException("创建时间不能为空！");
		}
		if (param.get("endTime") == null) {
			throw new BizException("结束时间不能为空！");
		}
		if ("ALL".equals(param.get("isCalculated"))) {
			param.put("isCalculated", "");
		}
		PageInfo<BizProductStorageEntity> pageInfo = bizProductStorageService
				.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataProvider
	public void queryMaterial(Page<BizProductStorageEntity> page,
			Map<String, Object> param) {
		PageInfo<BizProductStorageEntity> pageInfo = bizProductStorageService
				.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizProductStorageEntity entity) {
		if (entity.getId() == null) {
			bizProductStorageService.save(entity);
		} else {
			bizProductStorageService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizProductStorageEntity entity) {
		bizProductStorageService.delete(entity.getId());
	}

	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}

		String customerId = param.get("customerid").toString();
		try {
			// 校验该费用是否已生成Excel文件
			Map<String, Object> queryEntity = new HashMap<String, Object>();
			queryEntity.put("taskType", FileTaskTypeEnum.BIZ_PRODUCT.getCode());
			queryEntity.put("customerid", customerId);
			String existDel = fileExportTaskService
					.isExistDeleteTask(queryEntity);
			if (StringUtils.isNotEmpty(existDel)) {
				return existDel;
			}

			String path = getBizReceiveExportPath();
			String filepath = path + FileConstant.SEPARATOR
					+ FileTaskTypeEnum.BIZ_PRODUCT.getCode() + customerId
					+ FileConstant.SUFFIX_XLSX;

			FileExportTaskEntity entity = new FileExportTaskEntity();
			entity.setCustomerid(customerId);
			entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
			entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
			entity.setTaskName(FileTaskTypeEnum.BIZ_PRODUCT.getDesc()
					+ customerId);
			entity.setTaskType(FileTaskTypeEnum.BIZ_PRODUCT.getCode());
			entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
			entity.setProgress(0d);
			entity.setFilePath(filepath);
			entity.setCreator(JAppContext.currentUserName());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setDelFlag(ConstantInterface.DelFlag.NO);
			entity = fileExportTaskService.save(entity);

			// 生成账单文件
			final Map<String, Object> condition = param;
			final String taskId = entity.getTaskId();
			final String filePath = filepath;
			new Thread() {
				public void run() {
					try {
						export(condition, taskId, filePath);
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
		return FileTaskTypeEnum.BIZ_PRODUCT.getDesc() + customerId
				+ MessageConstant.EXPORT_TASK_BIZ_MSG;
	}

	/**
	 * 异步导出
	 * 
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void export(Map<String, Object> param, String taskId,
			String filePath) throws Exception {
		fileExportTaskService.updateExportTask(taskId,
				FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
		long beginTime = System.currentTimeMillis();

		logger.info("====应收商品库存导出：");
		File storeFolder = new File(path);
		// 如果存放上传文件的目录不存在就新建
		if (!storeFolder.isDirectory()) {
			storeFolder.mkdirs();
		}

		logger.info("====应收商品库存导出：写入Excel begin.");
		fileExportTaskService.updateExportTask(taskId, null, 30);
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
		// 商品库存
		fileExportTaskService.updateExportTask(taskId, null, 70);
		handProductStorage(poiUtil, workbook, filePath, param);
		// 最后写到文件
		fileExportTaskService.updateExportTask(taskId, null, 90);
		poiUtil.write2FilePath(workbook, filePath);

		fileExportTaskService.updateExportTask(taskId,
				FileTaskStateEnum.SUCCESS.getCode(), 100);
		logger.info("====应收商品库存导出：写入Excel end.==总耗时："
				+ (System.currentTimeMillis() - beginTime));
	}

	/**
	 * 商品库存
	 * 
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handProductStorage(POISXSSUtil poiUtil,
			SXSSFWorkbook workbook, String path, Map<String, Object> myparam)
			throws Exception {
		if ("ALL".equals(myparam.get("isCalculated"))) {
			myparam.put("isCalculated", "");
		}

		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<BizProductStorageEntity> pageInfo = bizProductStorageService
					.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
					doLoop = false;
				} else {
					pageNo += 1;
				}
			} else {
				doLoop = false;
			}

			// 头、内容信息
			List<Map<String, Object>> headDetailMapList = getBizHead();
			List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo
					.getList());

			poiUtil.exportExcel2FilePath(poiUtil, workbook,
					FileTaskTypeEnum.BIZ_PRODUCT.getDesc(), lineNo,
					headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}

	/**
	 * 商品库存
	 */
	public List<Map<String, Object>> getBizHead() {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "费用编号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "feesNo");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "重量");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "weight");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商品编码");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "productId");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商品名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "productName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "体积");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "volume");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "托数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "palletNum");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "件数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "pieceNum");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "温度");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "temperature");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "数量");
		itemMap.put("columnWidth", 50);
		itemMap.put("dataKey", "aqty");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "状态");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "isCalculated");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "创建时间");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "createTime");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "费用计算时间");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "calculateTime");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "备注");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "remark");
		headInfoList.add(itemMap);

		return headInfoList;
	}

	private List<Map<String, Object>> getBizHeadItem(
			List<BizProductStorageEntity> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataItem = null;
		Map<String, String> calculateMap = CalculateState.getMap();
		Map<String, String> temperatureMap = getTemperature();
		for (BizProductStorageEntity entity : list) {
			dataItem = new HashMap<String, Object>();
			dataItem.put("feesNo", entity.getFeesNo());
			dataItem.put("warehouseName", entity.getWarehouseName());
			dataItem.put("weight", entity.getWeight());
			dataItem.put("productId", entity.getProductId());
			dataItem.put("productName", entity.getProductName());
			dataItem.put("volume", entity.getVolume());
			dataItem.put("palletNum", entity.getPalletNum());
			dataItem.put("pieceNum", entity.getPieceNum());
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("temperature",
					temperatureMap.get(entity.getTemperature()));
			dataItem.put("aqty", entity.getAqty());
			dataItem.put("isCalculated",
					calculateMap.get(entity.getIsCalculated()));
			dataItem.put("createTime", entity.getCreateTime());
			dataItem.put("calculateTime", entity.getCalculateTime());
			dataItem.put("remark", entity.getRemark());

			dataList.add(dataItem);
		}

		return dataList;
	}

	@Expose
	public Properties validRetry(Map<String, Object> param) {
		// Properties ret = bizProductStorageService.validRetry(param);
		// 不再校验
		Properties ret = new Properties();
		ret.setProperty("key", "OK");
		ret.setProperty("value", "");
		return ret;
	}

	@Expose
	public String reCalculate(Map<String, Object> param) {
	    // 更改费用计算状态为99
        if(bizProductStorageService.reCalculate(param)==0){
            return "重算异常";
        }else{
            //汇总需要发mq的数据
            List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryProTask(param);
            for (BmsCalcuTaskVo calcuTaskVo : list) {
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
}
