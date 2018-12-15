package com.jiuyescm.bms.bill.receive.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
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
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterRecordService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveExpectVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterRecordVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveAirTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveTransportTempService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.iaccount.api.IUserService;
import com.jiuyescm.iaccount.vo.UserVO;
import com.jiuyescm.utils.JsonUtils;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billReceiveMasterController")
public class BillReceiveMasterController {

	private static final Logger logger = LoggerFactory.getLogger(BillReceiveMasterController.class.getName());

	@Autowired
	private IBillReceiveMasterService billReceiveMasterService;
	@Autowired
	private IBillReceiveMasterRecordService billReceiveMasterRecordService;
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	@Resource
	private Lock lock;
	@Resource 
	private IRedisClient redisClient;
	@Resource 
	private ISystemCodeService systemCodeService;
	@Autowired 
	private StorageClient storageClient;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private JmsTemplate jmsQueueTemplate;
	@Autowired
	private IUserService userService;
	@Resource
	private IBillFeesReceiveStorageTempService feesReceiveStorageTempService;
	@Resource
	private IBillFeesReceiveDispatchTempService feesReceiveDispatchTempService;
	@Resource
	private IBillFeesReceiveTransportTempService feesReceiveTransportTempService;
	@Resource
	private IBillFeesReceiveAirTempService feesReceiveAirTempService;
	
	
	String sessionId=JAppContext.currentUserID()+"_import_Receive_Bill";
	final String nameSpace="com.jiuyescm.bms.bill.receive.web.BillReceiveMasterController";

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BillReceiveMasterVo findById(Long id) throws Exception {
		return billReceiveMasterService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillReceiveMasterVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		double totalImportCost = 0.0;
		double totalAdjustCost = 0.0;
		double totalCost = 0.0;
		PageInfo<BillReceiveMasterVo> pageInfo = billReceiveMasterService.query(param, page.getPageNo(), page.getPageSize());
		//汇总金额
		if (pageInfo != null) {
			try {
				for (BillReceiveMasterVo entity : pageInfo.getList()) {
					if (null == entity) {
						continue;
					}
					entity.setRate(entity.getTaskRate()+"%");
					totalImportCost = totalImportCost + entity.getAmount();
					totalAdjustCost = totalAdjustCost + entity.getAdjustAmount();
					totalCost = totalCost + entity.getTotalAmount();
					if(entity.getExpectMoney()!=null){
						//获取所有的理赔金额
						Double abnormalMoney=billReceiveMasterService.getAbnormalMoney(entity.getBillNo());
						BigDecimal abnormal=new BigDecimal(abnormalMoney);
						//差异率 = (预估金额-( 总金额 - 理赔金额 ) ) / (总金额 - 理赔金额)
						//总金额
						BigDecimal amount=new BigDecimal(entity.getAmount());
						
						BigDecimal cha=amount.subtract(abnormal);
						if(cha.compareTo(BigDecimal.ZERO)!=0){
							BigDecimal rate=(entity.getExpectMoney().subtract(amount.subtract(abnormal)).divide(amount.subtract(abnormal),2, RoundingMode.HALF_UP));
							//差异率
							entity.setDifferentRate(rate);
						}		
					}
					
				}
				if (pageInfo.getList().size() > 0 && null != pageInfo.getList().get(0)) {
					pageInfo.getList().get(0).setTotalImportCost(totalImportCost);
					pageInfo.getList().get(0).setTotalAdjustACost(totalAdjustCost);
					pageInfo.getList().get(0).setTotalCost(totalCost);
				}
			} catch (Exception e) {
				logger.error("金额汇总异常");
				throw new BizException("金额汇总异常");
			}
			
					
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 调整金额
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void update(BillReceiveMasterVo entity) {
		try {
			billCheckInfoService.adjustMoney(entity.getBillNo(), entity.getAdjustAmount(), JAppContext.currentUserName(), JAppContext.currentUserID());
		} catch (Exception e) {
			logger.error("BillReceiveMfdasterController.update", e);
			if (e.toString().contains("账单不存在")) {
				throw new BizException("账单不存在!");
			}else if(e.toString().contains("已收款的账单不能调整金额!")){
				throw new BizException("已收款的账单不能调整金额!");
			}else if (e.toString().contains("账单跟踪更新确认金额失败!")) {
				throw new BizException("账单跟踪更新确认金额失败!");
			}else if (e.toString().contains("账单主表更新确认金额失败!")) {
				throw new BizException("账单主表更新确认金额失败!");
			}else {
				throw new BizException(e.toString());
			}
		}
	}

	/**
	 * 保存预计金额
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String updateMaster(BillReceiveMasterVo entity) {
		String result="";
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		billReceiveMasterService.update(entity);
		return result;
	}
	
	/**
	 * 保存预计金额
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String saveExpect(BillReceiveMasterVo entity) {
		String billNo=entity.getBillNo();
		String result="";
		BillReceiveExpectVo billExpect=new BillReceiveExpectVo();
		billExpect.setBillNo(billNo);
		billExpect.setBillName(entity.getBillName());
		billExpect.setCreateMonth(entity.getCreateMonth());
		//仓储费用
		Double storageMoney=feesReceiveStorageTempService.getImportStorageAmount(billNo);
		//配送费用
		Double dispatchMoney=feesReceiveDispatchTempService.getImportDispatchAmount(billNo);
		//干线费用
		Double transportMoney=feesReceiveTransportTempService.getImportTransportAmount(billNo);
		//航空费用
		Double airMoney=feesReceiveAirTempService.getImportAirAmount(billNo);
		
		Double totalMoney=storageMoney+dispatchMoney+transportMoney+airMoney;
		billExpect.setExpectStorageMoney(new BigDecimal(storageMoney));
		billExpect.setExpectDispatchMoney(new BigDecimal(dispatchMoney));
		billExpect.setExpectTransportMoney(new BigDecimal(transportMoney));
		billExpect.setExpectAirMoney(new BigDecimal(airMoney));
		billExpect.setExpectMoney(new BigDecimal(totalMoney));
		billExpect.setCreator(JAppContext.currentUserName());
		billExpect.setCreateTime(JAppContext.currentTimestamp());
		billExpect.setDelFlag("0");
		//更新预估金额
		int update=billReceiveMasterService.saveExpect(billExpect);
		if(update>0){
			//已确认
			entity.setExpectStatus("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			billReceiveMasterService.update(entity);
		}
		return result;
	}

	
	/**
	 * 应收账单导入
	 */
	@FileResolver
	public Map<String, Object> importReceiveBillTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
				 
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		List<UserVO> userList = userService.findAllUsers();
		
		//空校验(确认人和确认日期)
		Map<String, Object> maps = Maps.newHashMap();
		checkNull(parameter, infoList, maps);
		if (!maps.isEmpty()) {
			setProgress(6);
			return maps;
		}
		
		
		//"CONFIRMED".equals(parameter.get("billCheckStatus"))
		if (parameter.get("confirmMan") != null && StringUtils.isNotBlank(parameter.get("confirmMan").toString())) {
			//确认人
			boolean exe1 = false;
			for (UserVO userVO : userList) {
				if (userVO.getCname().equals(parameter.get("confirmMan"))) {
					parameter.put("confirmManId", userVO.getUsername());
					exe1 = true;
					break;
				}
			}
			if (!exe1) {
				setProgress(6);
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("未找到确认人，请重新输入！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}
		
		//人员变更，ID修改
		if (null != userList && userList.size() > 0) {
			
			//销售员
			boolean exe2 = false;
			for (UserVO userVO : userList) {
				if (userVO.getCname().equals(parameter.get("sellerName"))) {
					parameter.put("sellerId", userVO.getUsername());
					exe2 = true;
					break;
				}
			}
			if (!exe2) {
				setProgress(6);
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("未找到销售员，请重新输入！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			//项目管理员
			boolean exe3 = false;
			for (UserVO userVO : userList) {
				if (userVO.getCname().equals(parameter.get("projectManagerName"))) {
					parameter.put("projectManagerId", userVO.getUsername());
					exe3 = true;
					break;
				}
			}
			if (!exe3) {
				setProgress(6);
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("未找到项目管理员，请重新输入！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			//结算员
			boolean exe4 = false;
			for (UserVO userVO : userList) {
				if (userVO.getCname().equals(parameter.get("balanceName"))) {
					parameter.put("balanceId", userVO.getUsername());
					exe4 = true;
					break;
				}
			}
			if (!exe4) {
				setProgress(6);
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("未找到结算员，请重新输入！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}
		
		//导入校验，已确认账单不能导入
//		try {
//			billCheckInfoService.importCheck(parameter.get("createMonth").toString(), parameter.get("billName").toString());
//		} catch (Exception e) {
//			logger.error("已确定账单不能导入", e);
//			throw new BizException(e.getMessage());
//		}
		
		//重复性校验
		Map<String, Object> param = new HashMap<String, Object>();
		//param.put("delFlag", "0");
		List<BillReceiveMasterVo> checkList = billReceiveMasterService.query(param);
		for (BillReceiveMasterVo billReceiveMasterEntity : checkList) {
			if ((parameter.get("createMonth").toString()+parameter.get("billName").toString()+"SUCCESS").equals(billReceiveMasterEntity.getCreateMonth().toString()+billReceiveMasterEntity.getBillName()+billReceiveMasterEntity.getTaskStatus())) {
				setProgress(6);
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("同一月份存在相同账单名称，请删除后重试！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}
		
		//账单跟踪判断是否校验
		Map<String, Object> condition=new HashMap<>();
		condition.put("createMonth", parameter.get("createMonth"));
		condition.put("invoiceName", parameter.get("invoiceName"));
		condition.put("billName", parameter.get("billName"));
		BillCheckInfoVo billVo=billCheckInfoService.queryBillCheck(condition);
		if (billVo!=null) {
			setProgress(6);
			Map<String, Object> map = Maps.newHashMap();
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("账单跟踪已存在，无法继续导入！");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		
		String userId=ContextHolder.getLoginUserName();
		String lockString=Tools.getMd5(userId + "BMS_QUE_RECEIVE_BILL_IMPORT");
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
				errorVo.setMsg("应收账单导入功能已被其他用户占用，请稍后重试；");
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

	private void checkNull(final Map<String, Object> parameter, final List<ErrorMessageVo> infoList, Map<String, Object> maps) {
		if ("已确认".equals(parameter.get("billCheckStatus")) && ("null".equals(parameter.get("confirmMan").toString()) || "".equals(parameter.get("confirmMan").toString()) || "undefined".equals(parameter.get("confirmMan").toString()))) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("对账状态为已确认，确认人必填！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("已确认".equals(parameter.get("billCheckStatus")) && ("null".equals(parameter.get("confirmDate").toString()) || "".equals(parameter.get("confirmDate").toString()) || "undefined".equals(parameter.get("confirmDate").toString()))) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("对账状态为已确认，确认日期必填！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("invoiceName").toString()) || "".equals(parameter.get("invoiceName").toString()) || "undefined".equals(parameter.get("invoiceName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("商家合同名称不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("createMonth").toString()) || "".equals(parameter.get("createMonth").toString()) || "undefined".equals(parameter.get("createMonth").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("业务月份不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("billName").toString()) || "".equals(parameter.get("billName").toString()) || "undefined".equals(parameter.get("billName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("账单名称不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("billCheckStatus").toString()) || "".equals(parameter.get("billCheckStatus").toString()) || "undefined".equals(parameter.get("billCheckStatus").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("对账状态不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		
		if ("null".equals(parameter.get("isneedInvoice").toString()) || "".equals(parameter.get("isneedInvoice").toString()) || "undefined".equals(parameter.get("isneedInvoice").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("是否需要发票不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("billStartTime").toString()) || "".equals(parameter.get("billStartTime").toString()) || "undefined".equals(parameter.get("billStartTime").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("业务启动时间不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("firstClassName").toString()) || "".equals(parameter.get("firstClassName").toString()) || "undefined".equals(parameter.get("firstClassName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("一级品类不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("bizTypeName").toString()) || "".equals(parameter.get("bizTypeName").toString()) || "undefined".equals(parameter.get("bizTypeName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("业务类型不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("projectName").toString()) || "".equals(parameter.get("projectName").toString()) || "undefined".equals(parameter.get("projectName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("项目不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("sellerName").toString()) || "".equals(parameter.get("sellerName").toString()) || "undefined".equals(parameter.get("sellerName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("销售员不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("deptName").toString()) || "".equals(parameter.get("deptName").toString()) || "undefined".equals(parameter.get("deptName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("部门名称不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("projectManagerName").toString()) || "".equals(parameter.get("projectManagerName").toString()) || "undefined".equals(parameter.get("projectManagerName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("项目管理员不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		if ("null".equals(parameter.get("balanceName").toString()) || "".equals(parameter.get("balanceName").toString()) || "undefined".equals(parameter.get("balanceName").toString())) {
			ErrorMessageVo errorVo = new ErrorMessageVo();
			errorVo.setMsg("结算员不能为空！");
			infoList.add(errorVo);
			maps.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
	}
	
	
	/**
	 * 异步导入处理文件
	 * @param file
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String,Object> importFileAsyn(UploadFile file, final Map<String, Object> parameter) throws Exception{
		setProgress(0);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
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
			setProgress(6);
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
			setProgress(2);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 500);
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据上传文件系统失败，请稍后重试"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		//原始地址放入map中
		parameter.put("fullPath", fullPath);
		
		setProgress(4);
		String username = JAppContext.currentUserName();
		String userId = JAppContext.currentUserID();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		parameter.put("creator", username);
		parameter.put("creatorId", userId);
		parameter.put("createTime", currentTime);
		
		// 生成任务，写入主表
		String billNo =sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
		parameter.put("billNo", billNo);
		//组装数据
		try {
			BillReceiveMasterVo taskEntity = new BillReceiveMasterVo();
			taskEntity.setBillNo(billNo);
			boolean res = formatMonth(Integer.valueOf(parameter.get("createMonth").toString()));
			if (!res) {
				setProgress(6);
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的业务月份格式错误!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			taskEntity.setCreateMonth(Integer.valueOf(parameter.get("createMonth").toString()));
			taskEntity.setBillName(parameter.get("billName").toString());
			taskEntity.setInvoiceName(parameter.get("invoiceName").toString());
			taskEntity.setInvoiceId(parameter.get("invoiceId").toString());
			taskEntity.setTaskRate(0);
			taskEntity.setTaskStatus(BmsEnums.taskStatus.WAIT.getCode());
			taskEntity.setCreator(JAppContext.currentUserName());
			taskEntity.setCreatorId(JAppContext.currentUserID());
			taskEntity.setCreateTime(JAppContext.currentTimestamp());
			taskEntity.setOriginFileName(fileName);
			taskEntity.setOriginFilePath(fullPath);
			taskEntity.setResultFileName(fileName);
			taskEntity.setResultFilePath(resultFullPath);
			taskEntity.setDelFlag("0");
			int saveNum = billReceiveMasterService.save(taskEntity);
			if (saveNum <= 0) {
				setProgress(6);
				infoList.add(new ErrorMessageVo(1, "Excel 导入主表数据保存失败"));
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else {
				setProgress(5);
			}
		} catch (Exception e) {
			setProgress(6);
			logger.error("Excel 导入主表数据保存失败");
			infoList.add(new ErrorMessageVo(1, "Excel 导入主表数据保存失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
		
		//写入应收账单导入记录表
		try {
			BillReceiveMasterRecordVo recordEntity = new BillReceiveMasterRecordVo();
			recordEntity.setBillNo(billNo);
			recordEntity.setCreateTime(currentTime);
			recordEntity.setCreator(username);
			recordEntity.setCreatorId(userId);
			recordEntity.setAdjustAmount(BigDecimal.ZERO);
			int k = billReceiveMasterRecordService.save(recordEntity);
			if (k <= 0) {
				setProgress(6);
				infoList.add(new ErrorMessageVo(1, "Excel 记录表数据保存失败"));
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else {
				setProgress(5);
			}
		} catch (Exception e) {
			logger.error("Excel 记录表数据保存失败");
			setProgress(6);
			infoList.add(new ErrorMessageVo(1, "Excel 记录表数据保存失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		
		//写入账单跟踪主表
//		try {
//			BillCheckInfoVo checkInfoVo = new BillCheckInfoVo();
//			checkInfoVo.setCreateMonth(Integer.valueOf(parameter.get("createMonth").toString()));
//			checkInfoVo.setBillName(parameter.get("billName").toString());
//			checkInfoVo.setInvoiceName(parameter.get("invoiceName").toString());
//			checkInfoVo.setInvoiceId(parameter.get("invoiceId")==null?null:parameter.get("invoiceId").toString());
//			if (null != parameter.get("billCheckStatus")) {
//				checkInfoVo.setBillCheckStatus(parameter.get("billCheckStatus")==null?null:parameter.get("billCheckStatus").toString());
//			}
//			if (null != parameter.get("isneedInvoice")) {
//				checkInfoVo.setIsneedInvoice(parameter.get("isneedInvoice").toString());
//			}
//			if (null != parameter.get("billStartTime")) {
//				Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				checkInfoVo.setBillStartTime((Timestamp)f.parseObject(parameter.get("billStartTime").toString()));
//			}
//			if (null != parameter.get("firstClassName")) {
//				checkInfoVo.setFirstClassName(parameter.get("firstClassName").toString());
//			}
//			if (null != parameter.get("bizTypeName")) {
//				checkInfoVo.setBizTypeName(parameter.get("bizTypeName").toString());
//			}
//			if (null != parameter.get("projectName")) {
//				checkInfoVo.setProjectName(parameter.get("projectName").toString());
//			}
//			if (null != parameter.get("sellerId")) {
//				checkInfoVo.setSellerId(parameter.get("sellerId").toString());
//			}
//			if (null != parameter.get("sellerName")) {
//				checkInfoVo.setSellerName(parameter.get("sellerName").toString());
//			}
//			if (null != parameter.get("deptName")) {
//				checkInfoVo.setDeptName(parameter.get("deptName").toString());
//			}
//			if (null != parameter.get("deptCode")) {
//				checkInfoVo.setDeptCode(parameter.get("deptCode").toString());
//			}
//			if (null != parameter.get("projectManagerId")) {
//				checkInfoVo.setProjectManagerId(parameter.get("projectManagerId").toString());
//			}
//			if (null != parameter.get("projectManagerName")) {
//				checkInfoVo.setProjectManagerName(parameter.get("projectManagerName").toString());
//			}
//			if (null != parameter.get("balanceId")) {
//				checkInfoVo.setBalanceId(parameter.get("balanceId").toString());
//			}
//			if (null != parameter.get("balanceName")) {
//				checkInfoVo.setBalanceName(parameter.get("balanceName").toString());
//			}
//			if (null != parameter.get("confirmManId")) {
//				checkInfoVo.setConfirmManId(parameter.get("confirmManId").toString());
//			}
//			if (null != parameter.get("confirmMan")) {
//				checkInfoVo.setConfirmMan(parameter.get("confirmMan").toString());
//			}
//			if (null != parameter.get("confirmDate")) {
//				Format f = new SimpleDateFormat("yyyy-MM-dd");
//				checkInfoVo.setConfirmDate((Date)f.parseObject(parameter.get("confirmDate").toString()));
//			}
//			checkInfoVo.setDelFlag("0");
//			checkInfoVo.setCreator(username);
//			checkInfoVo.setCreatorId(userId);
//			checkInfoVo.setCreateTime(currentTime);
//			int l = billCheckInfoService.save(checkInfoVo);
//			if (l <= 0) {
//				setProgress(6);
//				infoList.add(new ErrorMessageVo(1, "账单跟踪主表数据保存失败"));
//				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//				return map;
//			}
//		} catch (Exception e) {
//			logger.error("账单跟踪主表数据保存失败");
//			setProgress(6);
//			infoList.add(new ErrorMessageVo(1, "账单跟踪主表数据保存失败"));
//			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//			return map;
//		}
		
			
		// 写入MQ
		//final String msg = billNo;
		jmsQueueTemplate.send("BMS_BILL_RECEIVE_IMPORT_QUEUE", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				String json = JsonUtils.toJson(parameter);
//				JsonUtils.formJson("msg", Message.class);
				return session.createTextMessage(json);
			}
		});
		
		setProgress(5);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
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
     * 是否需要发票下拉
     * @return
     */
    @DataProvider
    public List<BillReceiveMasterVo> getIsneedInvoice(){
    	List<BillReceiveMasterVo> list = new ArrayList<BillReceiveMasterVo>();
    	BillReceiveMasterVo entity1 = new BillReceiveMasterVo();
    	entity1.setIsneedInvoice("是");
    	BillReceiveMasterVo entity2 = new BillReceiveMasterVo();
    	entity2.setIsneedInvoice("否");
    	list.add(entity1);
    	list.add(entity2);
    	return list;
    }
    
    /**
     * 对账状态
     * @return
     */
    @DataProvider
    public List<BillReceiveMasterVo> getBillCheckStatus(){
    	String username = JAppContext.currentUserName();
    	String userId = JAppContext.currentUserID();
    	//Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    	Date date = new Date();
    	Map<String, String> map = BillCheckStateEnum.getMap();
    	BillReceiveMasterVo entity = null;
    	List<BillReceiveMasterVo> list = new ArrayList<BillReceiveMasterVo>();
    	if (map.keySet() != null && map.keySet().size() > 0) {
    		for (String code : map.keySet()) {
    			entity = new BillReceiveMasterVo();
    			if ("CONFIRMED".equals(code)) {
					entity.setConfirmMan(username);
					entity.setConfirmManId(userId);
					entity.setConfirmDate(date);
				}
    			entity.setBillCheckStatus(map.get(code));
    			list.add(entity);
    		}
		}
    	
    	return list;
    }
    
	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillReceiveMasterVo vo){
		try {
			billReceiveMasterService.delete(vo.getBillNo(), vo.getTaskStatus());
		} catch (Exception e) {
			logger.error("BillReceiveMasterController.delete", e);
			throw new BizException(e.getMessage());
		}
		
	}
	
	/**
	 * createMonth校验
	 * @param date
	 * @return
	 */
	private boolean formatMonth(int date){
		String createMonth=date+"";
		if(createMonth.length()!=4){
			return false;
		}else{
			String month=createMonth.substring(2, 4);
			int m=Integer.parseInt(month);
			if(m<=0 || m>12){
				return false;
			}
		}
		return true;
	}

	@DataProvider
	public Map<String,String> getStatus(){
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put("", "全部");
		map.put("0", "未确认");
		map.put("1", "已确认");
		return map;
	}
	
	@DataProvider
	public Map<String,String> getRate(){
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put("", "全部");
		map.put("0", "小于5%");
		map.put("1", "大于等于5%");
		return map;
	}
}
