/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.dispatch.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEncapEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEntity;
import com.jiuyescm.bms.fees.bill.dispatch.service.IBillPayDispatchDistinctService;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.BillPayDispatchDistinctDataType;
import com.jiuyescm.common.utils.upload.DataProperty;

/**
 * 应付账单宅配对账差异
 * @author yangss
 */
@Controller("billPayDispatchDistinctController")
public class BillPayDispatchDistinctController {

	private static final Logger logger = Logger.getLogger(BillPayDispatchDistinctController.class.getName());

	@Resource
	private IBillPayDispatchDistinctService service;
	@Resource
	private IFeesPayDispatchService feesPayDispatchService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillPayDispatchDistinctEncapEntity> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		if("全部".equals(param.get("status"))){
			param.put("status", null);
		}
		PageInfo<BillPayDispatchDistinctEncapEntity> pageInfo = service.query(param, page.getPageNo(), page.getPageSize());
		
		if (null != pageInfo) {
			if(pageInfo.getList()!=null && pageInfo.getList().size()>0){
				for(int i=0;i<pageInfo.getList().size();i++){
					BillPayDispatchDistinctEncapEntity entity=pageInfo.getList().get(i);
					//差额处理,差异账单金额不存在显示业务数据的金额
					if(null == entity.getAmount() || entity.getAmount().equals(0)){
						entity.setDiffAmount(entity.getFeeAmount());
					}
					if(entity.getWaybillNo()==null || "".equals(entity.getWaybillNo())){
						entity.setWaybillNo(entity.getFeeWayBillNo());
					}
				}
			}
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 销账
	 * @param param
	 * @return
	 */
	@DataResolver
	public String update(BillPayDispatchDistinctEncapEntity param)
	{
		if (null == param || StringUtils.isBlank(param.getBoddid().toString())) {
			return "请选择需要销账的记录!";
		}
		try{
			Timestamp currentTime = JAppContext.currentTimestamp();
			String userName = JAppContext.currentUserName();
			BillPayDispatchDistinctEntity entity = new BillPayDispatchDistinctEntity();
			entity.setId(param.getBoddid());//差异表id
			entity.setLastModifier(userName);
			entity.setLastModifyTime(currentTime);
			entity.setStatus(ConstantInterface.FeeStatus.STATUS_1);//已对账
			service.update(entity);
			return "SUCCESS";
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.PAY_DIS_DISCT_CANCELBILL_EX_MSG, ex);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "数据库操作失败";
		}
	}
	
	/**
	 * 全部对账
	 * @param data
	 * @return
	 */
	@DataResolver
	public String calculate(Map<String, Object> param){
		List<BillPayDispatchDistinctEntity> billList=new ArrayList<BillPayDispatchDistinctEntity>();
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		//需要计算的记录
		if(null != param && StringUtils.isNotBlank(param.get("billNo").toString())){
			String billNo = param.get("billNo").toString();
			//查询所有的差异数据
			List<BillPayDispatchDistinctEntity> diffList = service.queryListByBillNo(billNo);
			//查询所有的业务数据
			List<FeesPayDispatchEntity> dispatchList = feesPayDispatchService.queryDispatchByBillNo(billNo);
			//计算差额
			for (BillPayDispatchDistinctEntity diffEntity : diffList) {
				BillPayDispatchDistinctEntity entity=new BillPayDispatchDistinctEntity();
				entity.setId(diffEntity.getId());
				entity.setLastModifier(userName);
				entity.setLastModifyTime(currentTime);
				Double amount=0.0;
				Double feeamount=0.0;
				Double diffamount=0.0;
				//物流商费用
				if(diffEntity.getAmount()!=null){
					feeamount=diffEntity.getAmount();
				}
				for (FeesPayDispatchEntity dispatchEntity : dispatchList) {
					if (diffEntity.getWaybillNo().equals(dispatchEntity.getWaybillNo())) {
						//运单费用
						if(dispatchEntity.getAmount()!=null){
							amount=dispatchEntity.getAmount();
						}
					}
				}
				//费用差
				diffamount=amount-feeamount;
				entity.setDiffAmount(diffamount);
				billList.add(entity);
			}
			//update差额
			service.updateList(billList);
		}
		
		return null;
	}

	/**
	 * 导入对账数据
	 * @param file
	 * @param param
	 * @return
	 */
	@FileResolver
	public Map<String, Object> importBill(UploadFile file, Map<String, Object> param){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		Map<String, Object> map = new HashMap<String, Object>();
		ErrorMessageVo errorVo = null;
		
		if(null == param || StringUtils.isBlank(param.get("billNo").toString())){
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("导入账单编号为空!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 30);
		long beginTime = System.currentTimeMillis();
		String billNo = param.get("billNo").toString();
		// 导入成功返回信息
		List<BillPayDispatchDistinctEntity> readResultList = new ArrayList<BillPayDispatchDistinctEntity>();
		try {
			BaseDataType bs = new BillPayDispatchDistinctDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg(MessageConstant.EXCEL_FORMAT_ERROR_MSG);
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			// 解析Excel
			readResultList = readExcelProduct(file,bs);
			if (null == readResultList || readResultList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg(MessageConstant.EXCEL_NULL_MSG);
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 500);
			// 模板信息必填项校验
			map = impExcelCheckInfo(infoList, readResultList, map, billNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			//插入差异表
			int insertNum = service.insertBatchExistUpdate(readResultList);
			long endTime = System.currentTimeMillis();
			logger.info("==============totol used Time" + (endTime - beginTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				return map;
			}
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			logger.error(ExceptionConstant.PAY_DIS_DISCT_IMPBILL_EX_MSG, e);
		}
		return null;
	}
	
	/**
	 * 读取excel数据
	 * @param file
	 * @param bs
	 * @return
	 */
	private List<BillPayDispatchDistinctEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		List<BillPayDispatchDistinctEntity> productList = Lists.newArrayList();
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				BillPayDispatchDistinctEntity p = (BillPayDispatchDistinctEntity) BeanToMapUtil.convertMapNull(BillPayDispatchDistinctEntity.class, data);
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验导入信息
	 * @param infoList
	 * @param prodList
	 * @param map
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfo(List<ErrorMessageVo> infoList, List<BillPayDispatchDistinctEntity> prodList, Map<String, Object> map, String billNo) {
		int lineNo = 0;
		for (BillPayDispatchDistinctEntity entity : prodList) {
			lineNo=lineNo+1;
			
			if(null==entity.getWaybillNo()){
				setMessage(infoList, lineNo,"运单号为空!");
			}
			if(null == entity.getAmount() || StringUtils.isEmpty(entity.getAmount().toString())){
				setMessage(infoList, lineNo,"金额为空!");
			}
			
			if (infoList == null || infoList.size() <= 0) { // 有错误信息
				//设置属性
				Timestamp currentTime = JAppContext.currentTimestamp();
				String userName = JAppContext.currentUserName();
				entity.setStatus(ConstantInterface.FeeStatus.STATUS_0);//未对账
				entity.setCreator(userName);
				entity.setCreateTime(currentTime);
				entity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_0+"");
				entity.setBillNo(billNo);
			}
		}
		
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}
		return map;
	}
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
		
	/**
	 * 查询所有账单编号
	 * 封装成map到页面
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBillNoList(){
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<String> billNoList = service.queryBillNoList();
		if (null != billNoList && billNoList.size() > 0) {
			for (String billNo : billNoList) {
				map.put(billNo, billNo);
			}
		}
		return map;
	}
	
	@DataProvider
	public List<String> getBillNo(){
		List<String> billNoList = service.queryBillNoList();
		if (null != billNoList && billNoList.size() > 0) {
			return billNoList;
		}
		return null;
	}
	
	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
    
	
	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
	
}
