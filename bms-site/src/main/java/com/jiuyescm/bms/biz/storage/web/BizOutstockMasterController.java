/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizOutstockMasterController")
public class BizOutstockMasterController extends BaseController {
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;
	private static final Logger logger = LoggerFactory
			.getLogger(BizOutstockMasterController.class.getName());

	@Resource
	private IBizOutstockMasterService bizOutstockMasterService;

	@Resource
	private IPriceContractService contractService;

	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;

	@Resource
	private IFeesReceiveStorageService feesReceiveService;

	@Resource
	private SequenceService sequenceService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private Lock lock;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	@DataProvider
	public BizOutstockMasterEntity findById(Long id) throws Exception {
		BizOutstockMasterEntity entity = null;
		entity = bizOutstockMasterService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizOutstockMasterEntity> page,
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
		if (null != param.get("billTypeName")) {
			String buf = (String) param.get("billTypeName");
			String str[] = buf.split(",");

			if (str.length > 1) {
				param.put("billTypeNameOther", "dd");
				param.put("str0", str[0]);
				param.put("str1", str[1]);
			} else if ("ALL".equals(buf)) {
				param.put("billTypeName", "");
			}
		}
		PageInfo<BizOutstockMasterEntity> pageInfo = bizOutstockMasterService
				.queryNew(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizOutstockMasterEntity entity) {
		if (entity.getId() == null) {
			bizOutstockMasterService.save(entity);
		} else {
			bizOutstockMasterService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizOutstockMasterEntity entity) {
		bizOutstockMasterService.delete(entity.getId());
	}

	@DataResolver
	public @ResponseBody
	Object update(BizOutstockMasterEntity entity) {

		ReturnData result = new ReturnData();

		double w = entity.getResizeWeight() == null ? 0 : entity
				.getResizeWeight().doubleValue()
				+ (entity.getTotalWeight() == null ? 0 : entity
						.getTotalWeight().doubleValue());

		double n = entity.getResizeNum() == null ? 0 : entity.getResizeNum()
				+ (entity.getTotalQuantity() == null ? 0 : entity
						.getTotalQuantity());

		if (w <= 0 || n <= 0) {
			result.setCode("fail");
			result.setData("调整数量或重量不能小于0");

			return result;
		}

		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid = JAppContext.currentUserName();

		// 判断是否生成费用，判断费用的状态是否为未过账
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("outstockNo", entity.getOutstockNo());
		String feesNo = entity.getFeesNo();
		PageInfo<FeesReceiveStorageEntity> pageInfo = null;
		FeesReceiveStorageEntity feesReceiveStorageEntity = null;
		if (StringUtils.isNotBlank(feesNo)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("feesNo", feesNo);
			pageInfo = feesReceiveStorageService.query(param, 0,
					Integer.MAX_VALUE);
			if (null != pageInfo && null != pageInfo.getList()
					&& pageInfo.getList().size() > 0) {
				feesReceiveStorageEntity = pageInfo.getList().get(0);
				// 获取此时的费用状态
				String status = String.valueOf(feesReceiveStorageEntity
						.getStatus());
				if ("1".equals(status)) {
					result.setCode("fail");
					result.setData("该费用已过账，无法调整托数");
					return result;
				}
			}
		}
		// 此为修改业务数据
		// 根据名字查出对应的id
		entity.setLastModifier(userid);
		entity.setLastModifyTime(nowdate);

		int i = 0;
		entity.setIsCalculated("0");// 设置为未计算
		i = bizOutstockMasterService.update(entity);
		if (i > 0) {
			result.setCode("SUCCESS");
		} else {
			result.setCode("fail");
			result.setData("更新失败");
		}
		// 修改费用数据并发送mq
		List<String> feeList = new ArrayList<>();
		feeList.add(feesNo);
		Map<String, Object> conMap = new HashMap<String, Object>();
		conMap.put("feeList", feeList);
		// 更改费用计算状态为99
		bizOutstockMasterService.retryForCalcuFee(conMap);

		String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
		//如果是【B2B订单操作费】 或【出库装车费】,并且是B2B出库单   ( B2bFlag 0-B2C  1-B2B)
		if(("wh_b2b_work".equals(entity.getSubjectCode()) ||"wh_b2b_handwork".equals(entity.getSubjectCode())) 
				&& "1".equals(entity.getB2bFlag())){
			for (String sCode : subjectList2) {
				sendTask(entity, creMonth, sCode);	
			}
		}
		else if("wh_b2c_work".equals(entity.getSubjectCode()) && "0".equals(entity.getB2bFlag())){
			sendTask(entity, creMonth, "wh_b2c_work");	
		}

		return result;
	}

	private void sendTask(BizOutstockMasterEntity entity, String creMonth, String sCode) {
		try {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			vo.setCrePerson(ContextHolder.getLoginUser().getCname());
			vo.setCrePersonId(ContextHolder.getLoginUserName());
			vo.setCustomerId(entity.getCustomerid());
			vo.setSubjectCode(sCode);
			vo.setCreMonth(Integer.valueOf(creMonth));
			bmsCalcuTaskService.sendTask(vo);
			logger.info("mq发送成功,商家id:"+vo.getCustomerId()+",年月:"+vo.getCreMonth()+",科目id:"+vo.getSubjectCode());
		} catch (Exception e) {
			logger.error("mq发送失败:", e);
		}
	}

	@DataResolver
	public @ResponseBody
	Object reCount(List<BizOutstockMasterEntity> list) {

		ReturnData result = new ReturnData();

		List<String> arr = new ArrayList<String>();

		Map<String, Object> aCondition = new HashMap<>();

		for (BizOutstockMasterEntity entity : list) {

			Double initWeight = entity.getTotalWeight();
			Double initQuantity = entity.getTotalQuantity();
			if (entity.getTotalWeight() == null) {
				entity.setTotalWeight(0.0);
			}
			if (entity.getTotalQuantity() == null) {
				entity.setTotalQuantity(0.0);
			}

			aCondition.put("customerid", entity.getCustomerid());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");

			List<PriceContractInfoEntity> contractEntity = contractService
					.queryContract(aCondition);
			FeesReceiveStorageEntity storageFeeEntity = null;

			if (entity.getResizeNum() == null) {
				entity.setResizeNum(0.0);
			}
			if (entity.getResizeWeight() == null) {
				entity.setResizeWeight(0.0);
			}
			entity.setTotalQuantity(entity.getTotalQuantity()
					+ entity.getResizeNum());
			entity.setTotalWeight(entity.getTotalWeight()
					+ entity.getResizeWeight());

			String subjectID = null;
			switch (entity.getBillTypeName()) {
			case "产地直配发货":
				subjectID = "wh_b2c_work";
				break;
			case "销售出库单":
				subjectID = "wh_b2c_work";
				break;
			case "B2B其他":
				subjectID = "wh_b2b_work";
				break;
			case "调拨出库单":
				subjectID = "wh_product_out";
				break;
			default:
				continue;
			}

			CalculateVo calcuVo = new CalculateVo();
			calcuVo.setBizTypeCode("STORAGE");// 业务类型
			calcuVo.setObj(entity);// 业务数据
			calcuVo.setContractCode(contractEntity.get(0).getContractCode());// 合同编号
			calcuVo.setSubjectId(subjectID);// 费用科目

			if (calcuVo.getSuccess() && calcuVo.getPrice() != null) {
				try {
					storageFeeEntity = new FeesReceiveStorageEntity();
					storageFeeEntity.setCreator(JAppContext.currentUserName());
					storageFeeEntity.setCreateTime(entity.getCreateTime());
					storageFeeEntity.setCustomerId(entity.getCustomerid()); // 商家ID
					storageFeeEntity.setCustomerName(entity.getCustomerName()); // 商家名称
					storageFeeEntity
							.setWarehouseCode(entity.getWarehouseCode()); // 仓库ID
					storageFeeEntity
							.setWarehouseName(entity.getWarehouseName()); // 仓库名称
					storageFeeEntity.setOrderType(entity.getBillTypeName()); // 订单类型
					if (null != entity.getTotalVarieties()) {
						storageFeeEntity.setVarieties(entity
								.getTotalVarieties().intValue());
					}
					boolean isInsert = StringUtils.isEmpty(entity.getFeesNo()) ? true
							: false; // true-新增 false-更新
					String feesNo = StringUtils.isEmpty(entity.getFeesNo()) ? sequenceService
							.getBillNoOne(
									FeesReceiveStorageEntity.class.getName(),
									"STO", "0000000000") : entity.getFeesNo();
					storageFeeEntity.setFeesNo(feesNo);
					storageFeeEntity.setOrderNo(entity.getOutstockNo()); // oms订单号
					storageFeeEntity.setProductType(""); // 商品类型
					storageFeeEntity.setQuantity((new Double(entity
							.getTotalQuantity())).intValue());// 商品数量
					storageFeeEntity.setStatus("0"); // 状态
					storageFeeEntity.setWeight(entity.getTotalWeight());
					storageFeeEntity.setOperateTime(entity.getCreateTime());
					storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
					storageFeeEntity.setCost(calcuVo.getPrice()); // 入仓金额
					storageFeeEntity.setRuleNo(calcuVo.getRuleno());
					storageFeeEntity.setUnitPrice(calcuVo.getUnitPrice());// 生成单价
					storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());
					storageFeeEntity.setBizId(String.valueOf(entity.getId()));// 业务数据主键

					if (isInsert) {
						feesReceiveStorageService.save(storageFeeEntity);
					} else {
						feesReceiveStorageService.update(storageFeeEntity);
					}
					entity.setFeesNo(feesNo);
					entity.setTotalWeight(initWeight);// 恢复到以前
					entity.setTotalQuantity(initQuantity);
					entity.setIsCalculated("1");
					bizOutstockMasterService.update(entity);
				} catch (Exception e) {
					arr.add(entity.getExternalNo());
					logger.error(e.getMessage());
				}
			} else {
				arr.add(entity.getExternalNo());
			}

		}

		if (arr.size() > 0) {
			result.setCode("fail");
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < arr.size(); i++) {
				buf.append(arr.get(i));
				if (i != arr.size() - 1) {
					buf.append(",");
				}
			}
			if (arr.size() == list.size()) {
				result.setData("全部未更新成功！");
			} else {
				result.setData("未更新成功的出库单号是：" + buf.toString());
			}

		} else {
			result.setCode("SUCCESS");
		}
		return result;
	}

	/**
	 * 分组统计查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryGroup(Page<BizOutstockMasterEntity> page,
			Map<String, Object> param) {
		if ("ALL".equals(param.get("isCalculated"))) {
			param.put("isCalculated", "");
		}
		if (null != param.get("billTypeName")) {
			String buf = (String) param.get("billTypeName");
			String str[] = buf.split(",");

			if (str.length > 1) {
				param.put("billTypeNameOther", "dd");
				param.put("str0", str[0]);
				param.put("str1", str[1]);
			} else if ("ALL".equals(buf)) {
				param.put("billTypeName", "");
			}
		}
		PageInfo<BizOutstockMasterEntity> pageInfo = bizOutstockMasterService
				.queryGroup(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@Expose
	public Properties validRetry(Map<String, Object> param) {
		// 不进行此校验
		// Properties ret = bizOutstockMasterService.validRetry(param);
		Properties ret = new Properties();
		ret.setProperty("key", "OK");
		ret.setProperty("value", "");
		return ret;
	}

	@Expose
	public String reCalculate(Map<String, Object> param) {
		// 只查询计算状态为1的业务数据，这样的业务数据一定有费用编号
		List<BizOutstockMasterEntity> outstockMasterEntities = bizOutstockMasterService
				.queryNewList(param);
		if (CollectionUtils.isNotEmpty(outstockMasterEntities)) {
			List<String> feeList = new ArrayList<>();
			for (BizOutstockMasterEntity bizOutstockMasterEntity : outstockMasterEntities) {
				feeList.add(bizOutstockMasterEntity.getFeesNo());
			}
			Map<String, Object> conMap = new HashMap<String, Object>();
			conMap.put("feeList", feeList);
			if (param.containsKey("subjectCode")) {
				String subjectCode = (String) param.get("subjectCode");
				conMap.put("subjectCode", subjectCode);
			}
			// 更改费用计算状态为99
			bizOutstockMasterService.retryForCalcuFee(conMap);
			
			//汇总需要发mq的数据
			List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryOutstockTask(param);
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
			// 如果界面指定了费用科目，则按指定费用科目重算，否则全部重算
//			if (param.containsKey("subjectCode")) {
//				String subjectCode = (String) param.get("subjectCode");
//				List<String> subjectList = Arrays.asList(subjectCode);
//				sendTask(subjectList);
//				list = bmsCalcuTaskService.queryOutstockTask(param);
//			} else {
//				sendTask(subjectList3);
//			}
		}
		return "操作成功! 正在重算...";
	}

	// wh_b2c_work(B2C订单操作费 ) wh_b2b_work(B2B订单操作费) wh_b2b_handwork(出库装车费)
	private static final String FEE_1 = "wh_b2b_work";
	private static final String FEE_2 = "wh_b2b_handwork";
	private static final List<String> subjectList2 = Arrays.asList(FEE_1,
			FEE_2);

//	private void sendTask(List<String> subjectList) {
//		Map<String, Object> sendTaskMap = new HashMap<String, Object>();
//		sendTaskMap.put("isCalculated", "99");
//		sendTaskMap.put("subjectList", subjectList);
//		// 对这些费用按照商家、科目、时间排序
//		List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryByMap(sendTaskMap);
//		for (BmsCalcuTaskVo vo : list) {
//			vo.setCrePerson(JAppContext.currentUserName());
//			vo.setCrePersonId(JAppContext.currentUserID());
//			vo.setCreTime(JAppContext.currentTimestamp());
//			try {
//				bmsCalcuTaskService.sendTask(vo);
//				logger.info("mq发送，商家id为----{0}，业务年月为----{0}，科目id为---{0}",
//						vo.getCustomerId(), vo.getCreMonth(),
//						vo.getSubjectCode());
//			} catch (Exception e) {
//				logger.info(
//						"mq任务失败：商家id为----{0}，业务年月为----{0}，科目id为---{0}，错误信息：{0}",
//						vo.getCustomerId(), vo.getCreMonth(),
//						vo.getSubjectCode(), e);
//			}
//		}
//	}

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
			String path = getBizReceiveExportPath();
			String filepath = path + FileConstant.SEPARATOR
					+ FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getCode() + customerId
					+ FileConstant.SUFFIX_XLSX;

			FileExportTaskEntity entity = new FileExportTaskEntity();
			entity.setCustomerid(customerId);
			entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
			entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
			entity.setTaskName(FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getDesc()
					+ customerId);
			entity.setTaskType(FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getCode());
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
					}
				};
			}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getDesc() + customerId
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

		logger.info("====应收商品出库单导出：");
		// 如果存放上传文件的目录不存在就新建
		File storeFolder = new File(path);
		if (!storeFolder.isDirectory()) {
			storeFolder.mkdirs();
		}

		logger.info("====应收商品出库单导出：写入Excel begin.");
		fileExportTaskService.updateExportTask(taskId, null, 30);
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
		// 商品出库单
		fileExportTaskService.updateExportTask(taskId, null, 70);
		handBiz(poiUtil, workbook, filePath, param);
		// 最后写到文件
		fileExportTaskService.updateExportTask(taskId, null, 90);
		poiUtil.write2FilePath(workbook, filePath);

		fileExportTaskService.updateExportTask(taskId,
				FileTaskStateEnum.SUCCESS.getCode(), 100);
		logger.info("====应收商品出库单导出：写入Excel end.==总耗时："
				+ (System.currentTimeMillis() - beginTime));
	}

	/**
	 * 商品按托库存
	 * 
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook,
			String path, Map<String, Object> myparam) throws Exception {
		if ("ALL".equals(myparam.get("isCalculated"))) {
			myparam.put("isCalculated", "");
		}
		if (null != myparam.get("billTypeName")) {
			String buf = (String) myparam.get("billTypeName");
			String str[] = buf.split(",");

			if (str.length > 1) {
				myparam.put("billTypeNameOther", "dd");
				myparam.put("str0", str[0]);
				myparam.put("str1", str[1]);
			} else if ("ALL".equals(buf)) {
				myparam.put("billTypeName", "");
			}
		}

		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<BizOutstockMasterEntity> pageInfo = bizOutstockMasterService
					.queryNew(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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
					FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getDesc(), lineNo,
					headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}

	/**
	 * 干线运单
	 */
	public List<Map<String, Object>> getBizHead() {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "外部单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "externalNo");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outstockNo");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "运单号");
		itemMap.put("columnWidth", 50);
		itemMap.put("dataKey", "waybillNo");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商品明细");
		itemMap.put("columnWidth", 50);
		itemMap.put("dataKey", "productDetail");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "单据类型");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "billTypeName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "物流商名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "carrierName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "宅配商名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "deliverName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "温度类型");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "temperatureTypeName");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "B2C标识");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "b2bFlag");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "计费单位");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "unit");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库件数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalQuantity");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库品种数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalVarieties");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库箱数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "boxnum");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库重量");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalWeight");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "调整件数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "resizeNum");
		headInfoList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "调整品种数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "resizeVarieties");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "调整箱数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "adjustBoxnum");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "调整重量");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "resizeWeight");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "订单操作费");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "orderCost");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "订单操作费计算状态");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "orderIsCalculate");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "订单操作费计算时间");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "orderCalculateTime");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "订单操作费计算备注");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "orderCalMsg");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库装车费");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outHandCost");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库装车费计算状态");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outHandIsCalculate");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库装车费计算时间");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outHandCalculateTime");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库装车费计算备注");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outHandCalMsg");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "创建人");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "creator");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "运单创建时间");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "createTime");
		headInfoList.add(itemMap);

		return headInfoList;
	}

	private List<Map<String, Object>> getBizHeadItem(
			List<BizOutstockMasterEntity> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataItem = null;
		Map<String, Map<String, Object>> entityMap = new HashMap<String, Map<String, Object>>();
		Map<String, String> calculateMap = CalculateState.getMap();
		for (BizOutstockMasterEntity entity : list) {
			if (!entityMap.containsKey(entity.getWaybillNo())) {
				dataItem = new HashMap<String, Object>();
				dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("warehouseName", entity.getWarehouseName());
				dataItem.put("externalNo", entity.getExternalNo());
				dataItem.put("outstockNo", entity.getOutstockNo());
				dataItem.put("waybillNo", entity.getWaybillNo());
				dataItem.put("productDetail", entity.getProductDetail());
				dataItem.put("billTypeName", entity.getBillTypeName());
				dataItem.put("carrierName", entity.getCarrierName());
				dataItem.put("deliverName", entity.getDeliverName());
				dataItem.put("temperatureTypeName",
						entity.getTemperatureTypeName());
				dataItem.put("b2bFlag", entity.getB2bFlag());
				dataItem.put("unit", entity.getUnit());
				dataItem.put("totalQuantity", entity.getTotalQuantity());
				dataItem.put("totalVarieties", entity.getTotalVarieties());
				dataItem.put("boxnum", entity.getBoxnum());
				dataItem.put("totalWeight", entity.getTotalWeight());
				dataItem.put("resizeNum", entity.getResizeNum());
				dataItem.put("resizeVarieties", entity.getResizeVarieties());
				dataItem.put("adjustBoxnum", entity.getAdjustBoxnum());
				dataItem.put("resizeWeight", entity.getResizeWeight());

				// 如果是B2C订单操作费
				if ("0".equals(entity.getB2bFlag())) {
					dataItem.put("orderCost", entity.getCost());
					dataItem.put("orderIsCalculate",
							calculateMap.get(entity.getIsCalculated()));
					dataItem.put("orderCalculateTime",
							entity.getCalculateTime());
					dataItem.put("orderCalMsg", entity.getCalcuMsg());
				} else if ("1".equals(entity.getB2bFlag())) {
					// B2B订单操作费（区分是订单操作费还是出库装车费）
					if ("wh_b2b_work".equals(entity.getSubjectCode())) {
						dataItem.put("orderCost", entity.getCost());
						dataItem.put("orderIsCalculate",
								calculateMap.get(entity.getIsCalculated()));
						dataItem.put("orderCalculateTime",
								entity.getCalculateTime());
						dataItem.put("orderCalMsg", entity.getCalcuMsg());
					} else if ("wh_b2b_handwork"
							.equals(entity.getSubjectCode())) {
						dataItem.put("outHandCost", entity.getCost());
						dataItem.put("outHandIsCalculate",
								calculateMap.get(entity.getIsCalculated()));
						dataItem.put("outHandCalculateTime",
								entity.getCalculateTime());
						dataItem.put("outHandCalMsg", entity.getCalcuMsg());
					}
				}
				dataItem.put("creator", entity.getCreator());
				dataItem.put("createTime", entity.getCreateTime());
				dataList.add(dataItem);
				entityMap.put(entity.getWaybillNo(), dataItem);
			} else {
				Map<String, Object> data = entityMap.get(entity.getWaybillNo());
				// 如果是B2C订单操作费
				if ("0".equals(entity.getB2bFlag())) {
					data.put("orderCost", entity.getCost());
					data.put("orderIsCalculate",
							calculateMap.get(entity.getIsCalculated()));
					data.put("orderCalculateTime", entity.getCalculateTime());
					data.put("orderCalMsg", entity.getCalcuMsg());
				} else if ("1".equals(entity.getB2bFlag())) {
					// B2B订单操作费（区分是订单操作费还是出库装车费）
					if ("wh_b2b_work".equals(entity.getSubjectCode())) {
						data.put("orderCost", entity.getCost());
						data.put("orderIsCalculate",
								calculateMap.get(entity.getIsCalculated()));
						data.put("orderCalculateTime",
								entity.getCalculateTime());
						data.put("orderCalMsg", entity.getCalcuMsg());
					} else if ("wh_b2b_handwork"
							.equals(entity.getSubjectCode())) {
						data.put("outHandCost", entity.getCost());
						data.put("outHandIsCalculate",
								calculateMap.get(entity.getIsCalculated()));
						data.put("outHandCalculateTime",
								entity.getCalculateTime());
						data.put("outHandCalMsg", entity.getCalcuMsg());
					}
				}
				dataItem.put("creator", entity.getCreator());
				dataItem.put("createTime", entity.getCreateTime());
			}
		}
		return dataList;
	}

	/**
	 * 批量更新
	 * 
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importUpdate(final UploadFile file,
			final Map<String, Object> parameter) throws Exception {
		Map<String, Object> remap = new HashMap<String, Object>();
		remap = importUpdateWeightLock(file, parameter);
		return remap;
	}

	@FileResolver
	public Map<String, Object> importUpdateWeightLock(UploadFile file,
			Map<String, Object> parameter) {
		DoradoContext.getAttachedRequest().getSession()
				.setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		Map<String, Object> map = new HashMap<String, Object>();

		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			// 写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity = new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("bizOutstockMasterController");
			bmsErrorLogInfoEntity.setMethodName("importUpdateWeightLock");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

		if (xssfSheet == null)
			return null;

		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();

		if (cols > 5) {
			DoradoContext.getAttachedRequest().getSession()
					.setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}

		DoradoContext.getAttachedRequest().getSession()
				.setAttribute("progressFlag", 100);

		Timestamp nowdate = JAppContext.currentTimestamp();
		String username = JAppContext.currentUserName();

		List<BizOutstockMasterEntity> infoLists = new ArrayList<BizOutstockMasterEntity>();

		Map<String, Integer> checkMap = new HashMap<>();
		//所有运单
		List<String> waybillNoList = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			BizOutstockMasterEntity entity = new BizOutstockMasterEntity();

			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if (xssfRow == null) {
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum + 1, "第" + lieshu + "列空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			int lieshu = rowNum + 1;
			String waybillNo = getCellValue(xssfRow.getCell(0));
			String resizeNum = getCellValue(xssfRow.getCell(1));
			String resizeVarieties = getCellValue(xssfRow.getCell(2));
			String adjustBoxnum = getCellValue(xssfRow.getCell(3));
			String resizeWeight = getCellValue(xssfRow.getCell(4));
			// 运单号（必填）
			if (StringUtils.isEmpty(waybillNo)) {
				setMessage(infoList, rowNum + 1, "第" + lieshu + "列运单号为空值！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}

			// 运单号重复性校验
			if (checkMap.containsKey(waybillNo)) {
				setMessage(infoList, rowNum + 1, "第" + lieshu + "行运单号和第"
						+ checkMap.get(waybillNo) + "行运单号重复");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				checkMap.put(waybillNo, lieshu);
				continue;
			} else {
				checkMap.put(waybillNo, lieshu);
			}
			// 调整数量、调整品种数、调整箱数、调整重量都为空
			if (StringUtils.isBlank(resizeNum)
					&& StringUtils.isBlank(resizeVarieties)
					&& StringUtils.isBlank(adjustBoxnum)
					&& StringUtils.isBlank(resizeWeight)) {
				// 除instockNo外，其他更新字段都为空
				setMessage(infoList, rowNum + 1, "没有需要调整的内容！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			// 调整数量
			if (StringUtils.isNotBlank(resizeNum)) {
				boolean isNumber = ExportUtil.isNumber(resizeNum);
				if (!isNumber) {
					setMessage(infoList, rowNum + 1, "第" + lieshu + "列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR,
							infoList);
				} else {
					entity.setResizeNum(Double.valueOf(resizeNum));
				}
			}

			// 调整品种数
			if (StringUtils.isNotBlank(resizeVarieties)) {
				boolean isNumber = ExportUtil.isNumber(resizeVarieties);
				if (!isNumber) {
					setMessage(infoList, rowNum + 1, "第" + lieshu + "列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR,
							infoList);
				} else {
					entity.setResizeVarieties(Double.valueOf(resizeVarieties));
				}
			}

			// 调整箱数
			if (StringUtils.isNotBlank(adjustBoxnum)) {
				boolean isNumber = ExportUtil.isNumber(adjustBoxnum);
				if (!isNumber) {
					setMessage(infoList, rowNum + 1, "第" + lieshu + "列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR,
							infoList);
				} else {
					entity.setAdjustBoxnum(Integer.valueOf(adjustBoxnum));
				}
			}
			// 调整重量
			if (StringUtils.isNotBlank(resizeWeight)) {
				boolean isNumber = ExportUtil.isNumber(adjustBoxnum);
				if (!isNumber) {
					setMessage(infoList, rowNum + 1, "第" + lieshu + "列非数字类型数据！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR,
							infoList);
				} else {
					entity.setResizeWeight(Double.valueOf(resizeWeight));
				}
			}

			entity.setLastModifier(username);
			entity.setLastModifyTime(nowdate);
			entity.setWaybillNo(waybillNo);
			entity.setIsCalculated("99");
			infoLists.add(entity);
			waybillNoList.add(waybillNo);
		}

		// 如果有错误信息
		if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {
			return map;
		}

		DoradoContext.getAttachedRequest().getSession()
				.setAttribute("progressFlag", 800);

		int num = 0;
		String message = null;
		try {
			num = bizOutstockMasterService.updateBatch(infoLists);
		} catch (Exception e) {
			message = e.getMessage();
			logger.error("更新失败", e);
		}
		//通过运单获取所有修改的业务数据
//		Map<String,Object> condition = new HashMap<>();
//		condition.put("waybillNoList",waybillNoList);
//		List<BizOutstockMasterEntity> bizOutstockMasterEntityList = bizOutstockMasterService.queryNewList(condition);
//		//获取费用编号
//		List<String> feeList = new ArrayList<String>();
//		for (BizOutstockMasterEntity entity : bizOutstockMasterEntityList) {
//			feeList.add(entity.getFeesNo());
//		}
//		Map<String,Object> conditionFee = new HashMap<>();
//		conditionFee.put("feeList", feeList);
//		//更新费用计算状态
//		bizOutstockMasterService.retryForCalcuFee(conditionFee);
//		//发送mq
//		sendTask(subjectList3);

		if (num == 0) {
			DoradoContext.getAttachedRequest().getSession()
					.setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if (message != null) {
				errorVo.setMsg(message);
			} else {
				errorVo.setMsg("更新失败!");
				errorVo.setLineNo(2);
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);

			return map;
		} else {
			DoradoContext.getAttachedRequest().getSession()
					.setAttribute("progressFlag", 1000);
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

	/**
	 * 批量更新模板下载
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplateUpdate(Map<String, String> parameter)
			throws IOException {
		InputStream is = DoradoContext
				.getCurrent()
				.getServletContext()
				.getResourceAsStream(
						"/WEB-INF/templates/storage/outstock_updatebatch_template.xlsx");
		return new DownloadFile("出库单信息更新模板.xlsx", is);
	}

	private void setMessage(List<ErrorMessageVo> errorList, int lineNo,
			String msg) {
		ErrorMessageVo errorVo;
		errorVo = new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		errorList.add(errorVo);
	}

	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession()
				.getAttribute("progressFlag");
		if (progressFlag == null) {
			DoradoContext.getAttachedRequest().getSession()
					.setAttribute("progressFlag", 0);
			return 1;
		}
		return (int) (DoradoContext.getAttachedRequest().getSession()
				.getAttribute("progressFlag"));
	}

	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession()
				.getAttribute("progressFlag");
		if (progressFlag == null) {
			DoradoContext.getAttachedRequest().getSession()
					.setAttribute("progressFlag", 0);

		} else {
			DoradoContext.getAttachedRequest().getSession()
					.setAttribute("progressFlag", 0);
		}
	}
}
