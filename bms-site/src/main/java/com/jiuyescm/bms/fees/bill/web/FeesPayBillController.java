package com.jiuyescm.bms.fees.bill.web;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillPayService;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchTihuoBillService;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.chargerule.payrule.service.IPayRuleService;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.impl.FeesPayAbnormalServiceImpl;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEncapEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.bill.out.service.IFeesPayBillService;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.pub.deliver.entity.DeliverEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchOtherDao;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherTemplateService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
/**
 * 应付账单-宅配、异常，运输
 * @author yangss
 */
@Controller("feesPayBillController")
public class FeesPayBillController {

	private static final Logger logger = Logger.getLogger(FeesPayBillController.class.getName());

	@Resource
	private IFeesPayBillService feesPayBillService;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private IBizDispatchTihuoBillService bizDispatchTihuoBillService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IPriceContractService priceContractService;
	
	//@Resource private IFeesCalcuService feesCalcuService;
/*	@Autowired
	private IFeesCalculateRpcService feesCalculateRpcServiceImpl;*/
	
	@Autowired
	private FeesPayAbnormalServiceImpl feesPayAbnormalService;
	
	@Resource
	private IFeesPayDispatchService feesPayDispatchService;
	
	@Resource
	private IBizDispatchBillPayService bizDispatchBillPayService;
	
	@Resource
	private IProjectService projectService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPayRuleService payRuleService;
	
	@Resource
	private IPriceOutDispatchOtherDao priceOutDispatchOtherDao;

	@Resource
	private IPriceOutDispatchOtherTemplateService priceOutDispatchOtherTemplateService;
	
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	/**
	 * 账单分页查询 宅配、异常
	 */
	@DataProvider
	public void query(Page<FeesPayBillEntity> page, Map<String, Object> param) {
		if (null == param){
			param = new HashMap<String, Object>();
		}
		if(-1 == (int)param.get("status")){
			param.put("status", null);
		}
		param.put("type", ConstantInterface.FeeBillType.TYPE_1);//宅配、异常
		PageInfo<FeesPayBillEntity> pageInfo = feesPayBillService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 账单分页查询 运输
	 */
	@DataProvider
	public void queryTransport(Page<FeesPayBillEntity> page, Map<String, Object> param) {
		if (null == param){
			param = new HashMap<String, Object>();
		}
		if(-1 == (int)param.get("status")){
			param.put("status", null);
		}
		param.put("type", ConstantInterface.FeeBillType.TYPE_2);//宅配、异常
		PageInfo<FeesPayBillEntity> pageInfo = feesPayBillService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 查询 运输费 
	 * 费用科目 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> queryGroupTransportAmount(String billNo){
		 List<FeesBillWareHouseEntity> list=feesPayBillService.queryGroupTransportAmount(billNo);
		 //List<SystemCodeEntity> sysBase=systemCodeService.findEnumList("ts_base_subject");
		// List<SystemCodeEntity> sysValueAdd=systemCodeService.findEnumList("ts_value_add_subject");
		 Map<String, String> map =bmsGroupSubjectService.getExportSubject("pay_transport_bill_subject");
		 for(FeesBillWareHouseEntity entity:list){
			 String subjectCode=entity.getSubjectCode();
			 String subjectName=getOtherSubjectName(map, subjectCode);
			 if(StringUtils.isNotBlank(subjectCode)){
				 switch(subjectCode){
				 case "ts_base_subject":
					 subjectName="基础费-"+getOtherSubjectName(map,entity.getOtherSubjectCode());
					 break;
				 case "ts_value_add_subject":
					 subjectName="增值费-"+getOtherSubjectName(map,entity.getOtherSubjectCode());
					 break;
				 }
			 }
			 entity.setSubjectName(subjectName);
		 }
		 return list;
		//return feesPayBillService.queryGroupTransportAmount(billNo);
	}
	/**
	 * 查询 运输费 
	 * 费用科目 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> queryAbnormalGroupAmount(String billNo){
		 List<FeesBillWareHouseEntity> list=feesPayBillService.queryAbnormalGroupAmount(billNo);
		 for(FeesBillWareHouseEntity entity:list){
			 entity.setSubjectName("理赔"); 
		 }
		 return list;
	}
	
	private String getOtherSubjectName(Map<String, String> map,
			String otherSubjectCode) {
		String subjectName="";
		if(StringUtils.isNotBlank(otherSubjectCode)){
			for(String key:map.keySet()){  	
				if(key.equals(otherSubjectCode)){
					subjectName=map.get(key);
					break;
				}
			}
		}
		return subjectName;
	}

	/**
	 * 查询配送费用 
	 * 物流商 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmount(String  billNo){
		String[] arr=billNo.split(",");
		String billno=arr[0];
		String deliverid=arr[1];
		return feesPayBillService.queryGroupDispatchAmountByDeliver(billno,deliverid);
	}
	
	/**
	 * 查询异常费用 
	 * 客诉原因 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmount(String billNo){
		String[] arr=billNo.split(",");
		String billno=arr[0];
		String deliverid=arr[1];
		List<FeesBillWareHouseEntity> list = feesPayBillService.queryGroupAbnormalAmountByDeliver(billno,deliverid);
		return list;
	}
	
	/**
	 * 运输费明细
	 * 费用科目、账单编号
	 */
	@DataProvider
	public void queryTransportDetailGroupPage(Page<FeesPayTransportEntity> page,FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			PageInfo<FeesPayTransportEntity> pageInfo = feesPayBillService.queryTransportDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				InitTransportList(pageInfo.getList());
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}		
	}
	private void InitTransportList(List<FeesPayTransportEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesPayTransportEntity entity:list){
			if(entity.getCustomerId()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerId().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}
				}
			}
		}
	}
	/**
	 * 配送费明细
	 * 物流商、账单编号
	 */
	@DataProvider
	public void queryDispatchDetailGroupPage(Page<FeesPayDispatchEntity> page, FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			PageInfo<FeesPayDispatchEntity> pageInfo = feesPayBillService.queryDispatchDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				InitList(pageInfo.getList());
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	private void InitList(List<FeesPayDispatchEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesPayDispatchEntity entity:list){
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
	/**
	 * 异常费明细
	 * 客诉原因、账单编号
	 */
	@DataProvider
	public void queryAbnormalDetailGroupPage(Page<FeesPayAbnormalEntity> page,FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			PageInfo<FeesPayAbnormalEntity> pageInfo = feesPayBillService.queryAbnormalDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				InitAbnormalList(pageInfo.getList());
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	private void InitAbnormalList(List<FeesPayAbnormalEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesPayAbnormalEntity entity:list){
			if(entity.getCustomerId()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerId().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}
				}
			}
		}
	}
	/**
	 * 生成账单-默认全部 宅配、异常
	 */
	@DataResolver
	public void save(FeesPayBillEntity entity) throws Exception{
		long beginTime = System.currentTimeMillis();
		if(null == entity){
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
		}

		if(null != entity.getStartTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getStartTime());
			dateString=dateString+" 00:00:00";
			entity.setStartTime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getEndTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getEndTime());
			dateString=dateString+" 23:59:59";
			entity.setEndTime(Timestamp.valueOf(dateString));
		}
		if(!StringUtils.isBlank(entity.getDeliverSelectlist())){
			List<DeliverEntity> deliverList=new ArrayList<DeliverEntity>();
			String[] arr=entity.getDeliverSelectlist().split(",");
			DeliverEntity model=null;
			for(String s:arr){
				String[] arr2=s.split("&");
				model=new DeliverEntity();
				model.setDeliverid(arr2[0]);
				model.setDelivername(arr2[1]);
				deliverList.add(model);
			}
			entity.setDeliverList(deliverList);
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		//生成账单查询的是状态为未过账的
		entity.setStatus(ConstantInterface.FeeStatus.STATUS_0);
		
		//生成账单的同时去统计应付宅配运单并计算费用
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("deliverList", entity.getDeliverList());
		condition.put("startTime", entity.getStartTime());
		condition.put("endTime", entity.getEndTime());
		// 判断是否是免运费
		feesPayAbnormalService.handlFeesAbnormal(condition);
		countDispatchBill(condition);
		// 生成账单
		feesPayBillService.generPayBill(entity);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
		logger.info("======一共用时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 自定义生成账单 宅配、异常
	 */
	@DataResolver
	public void customBill(FeesPayBillEncapEntity entity) throws Exception{
		//处理自定义数据
		double totolePrice = handFeesBillReceiveEntity(null, entity);
		
		FeesPayBillEntity feesPayBillEntity = new FeesPayBillEntity();
		if (null != entity.getStartTime()) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getStartTime());
			dateString=dateString+" 00:00:00";
			feesPayBillEntity.setStartTime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getEndTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getEndTime());
			dateString=dateString+" 23:59:59";
			feesPayBillEntity.setEndTime(Timestamp.valueOf(dateString));
		}
		if(!StringUtils.isBlank(entity.getDeliverSelectlist())){
			List<DeliverEntity> deliverList=new ArrayList<DeliverEntity>();
			String[] arr=entity.getDeliverSelectlist().split(",");
			DeliverEntity model=null;
			for(String s:arr){
				String[] arr2=s.split("&");
				model=new DeliverEntity();
				model.setDeliverid(arr2[0]);
				model.setDelivername(arr2[1]);
				deliverList.add(model);
			}
			feesPayBillEntity.setDeliverList(deliverList);
		}
		
		
		feesPayBillEntity.setBillNo(entity.getBillNo());
		feesPayBillEntity.setBillName(entity.getBillName());
		feesPayBillEntity.setDeliveryid(entity.getDeliveryid());
		feesPayBillEntity.setDeliverName(entity.getDeliverName());
		feesPayBillEntity.setTotleprice(totolePrice);
		feesPayBillEntity.setCreator(JAppContext.currentUserName());
		feesPayBillEntity.setCreateTime(JAppContext.currentTimestamp());
		feesPayBillEntity.setType(ConstantInterface.FeeBillType.TYPE_1);//宅配、异常
		feesPayBillEntity.setStatus(ConstantInterface.FeeStatus.STATUS_0);//0 未过账 1已过账
		feesPayBillEntity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_0+"");//0 正常
		//判断是否免运费
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("deliverList", feesPayBillEntity.getDeliverList());
		condition.put("startTime", feesPayBillEntity.getStartTime());
		condition.put("endTime", feesPayBillEntity.getEndTime());
		
		feesPayAbnormalService.handlFeesAbnormal(condition);
		
		//生成账单的同时去统计应付宅配运单并计算费用
		/*countDispatchBill(condition);*/
		
		//生成账单
		feesPayBillService.customPayBill(feesPayBillEntity, entity);	
	}
	
	/**
	 * 生成账单-默认全部 运输
	 */
	@DataResolver
	public void generTransportPayBill(FeesPayBillEntity entity) throws Exception{
		long beginTime = System.currentTimeMillis();
		if(null == entity){
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
		}

		if(null != entity.getStartTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getStartTime());
			dateString=dateString+" 00:00:00";
			entity.setStartTime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getEndTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getEndTime());
			dateString=dateString+" 23:59:59";
			entity.setEndTime(Timestamp.valueOf(dateString));
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		//生成账单查询的是状态为未过账的
		entity.setStatus(ConstantInterface.FeeStatus.STATUS_0);
		//生成账单
		feesPayBillService.generTransportPayBill(entity);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
		logger.info("======一共用时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 自定义生成账单 运输
	 */
	@DataResolver
	public void customTransportPayBill(FeesPayBillEncapEntity entity) throws Exception{
		//处理自定义数据
		double totolePrice = handFeesBillReceiveEntity(null, entity);
		
		FeesPayBillEntity feesPayBillEntity = new FeesPayBillEntity();
		if (null != entity.getStartTime()) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getStartTime());
			dateString=dateString+" 00:00:00";
			feesPayBillEntity.setStartTime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getEndTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getEndTime());
			dateString=dateString+" 23:59:59";
			feesPayBillEntity.setEndTime(Timestamp.valueOf(dateString));
		}
		feesPayBillEntity.setBillNo(entity.getBillNo());
		feesPayBillEntity.setBillName(entity.getBillName());
		feesPayBillEntity.setForwarderId(entity.getForwarderId());
		feesPayBillEntity.setForwarderName(entity.getForwarderName());
		feesPayBillEntity.setTotleprice(totolePrice);
		feesPayBillEntity.setCreator(JAppContext.currentUserName());
		feesPayBillEntity.setCreateTime(JAppContext.currentTimestamp());
		feesPayBillEntity.setType(ConstantInterface.FeeBillType.TYPE_2);
		feesPayBillEntity.setStatus(ConstantInterface.FeeStatus.STATUS_0);//0 未过账 1已过账
		feesPayBillEntity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_0+"");//0 正常
		feesPayBillService.customTransportPayBill(feesPayBillEntity, entity);
	}
	
	
	/**
	 * 添加账单
	 */
	@DataResolver
	public void addBill(FeesPayBillEncapEntity entity) throws Exception{
		if(null == entity){
			throw new Exception("未找到物流商相关账单信息！");
		}
		//查询账单信息
		if (StringUtils.isBlank(entity.getBillNo())) {
			throw new Exception("请选择账单！");
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("billNo", entity.getBillNo());
		FeesPayBillEntity billEntity = feesPayBillService.queryBillInfo(condition);
		if (null == billEntity) {
			throw new Exception("未找该账单信息！");
		}
		if (ConstantInterface.FeeStatus.STATUS_1.equals(billEntity.getStatus())) {
			throw new Exception("该账单已核销,不允许操作！");
		}
		
		String billNo = entity.getBillNo();
		//处理自定义数据
		double totolePrice = handFeesBillReceiveEntity(billNo, entity);
		
		//账单要修改的信息
		FeesPayBillEntity feesBillEntity = new FeesPayBillEntity();
		feesBillEntity.setBillNo(billNo);
		feesBillEntity.setTotleprice(billEntity.getTotleprice() + totolePrice);
		feesBillEntity.setLastModifier(JAppContext.currentUserName());
		feesBillEntity.setLastModifyTime(JAppContext.currentTimestamp());
		feesPayBillService.addBill(feesBillEntity, entity);
	}
	
	/**
	 * 剔除账单
	 */
	@DataResolver
	public void delBill(FeesPayBillEncapEntity entity) throws Exception{
		if(null == entity){
			throw new Exception("未找到商家相关异常账单信息！");
		}
		//查询账单信息
		if (StringUtils.isBlank(entity.getBillNo())) {
			throw new Exception("请选择账单！");
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("billNo", entity.getBillNo());
		FeesPayBillEntity billEntity = feesPayBillService.queryBillInfo(condition);
		if (null == billEntity) {
			throw new Exception("未找该账单信息！");
		}
		if (ConstantInterface.FeeStatus.STATUS_1.equals(billEntity.getStatus())) {
			throw new Exception("该账单已核销,不允许操作！");
		}
		
		//处理自定义数据
		List<FeesPayTransportEntity> feesTransportList= entity.getFeesTransportList();
		List<FeesPayDispatchEntity> feesDispatchList = entity.getFeesDispatchList();
		List<FeesPayAbnormalEntity> feesAbnormalList = entity.getFeesAbnormalList();
		List<FeesPayTransportEntity> feesAbnormalTransportList=entity.getFeesAbnormalTransportList();
		if((null == feesTransportList || feesTransportList.size() == 0) &&
				(null == feesDispatchList || feesDispatchList.size() == 0) &&
				(null == feesAbnormalList || feesAbnormalList.size() == 0)&&
				(null==feesAbnormalTransportList||feesAbnormalTransportList.size()==0)){
			throw new Exception("未找到 商家相关运输,配送,异常账单信息！");
		}
		
		//计算账单金额
		double totolePrice=0;
		String billNo = entity.getBillNo();
		String operatorNmae = JAppContext.currentUserName();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		//剔除账单操作对象
		FeesPayBillEntity feesBillEntity = new FeesPayBillEntity();
		//运输
		if (null != feesTransportList && feesTransportList.size() > 0) {
			for(FeesPayTransportEntity deliverentity : feesTransportList){
				if(StringUtils.isBlank(deliverentity.getBillno())){
					throw new Exception("运输费【"+deliverentity.getFeesNo()+"】未生成对账单");
				}
				if (null != deliverentity.getTotleprice()) {
					totolePrice = totolePrice + deliverentity.getTotleprice();//累计总金额
				}
				//费用编号
				feesBillEntity.setFeesNo(deliverentity.getFeesNo());
			}
		}
		//配送
		if (null != feesDispatchList && feesDispatchList.size() > 0) {
			for(FeesPayDispatchEntity dispatchtity : feesDispatchList){
				if(StringUtils.isBlank(dispatchtity.getBillNo())){
					throw new Exception("配送费【"+dispatchtity.getFeesNo()+"】未生成对账单");
				}
				if (null != dispatchtity.getAmount()) {
					totolePrice = totolePrice + dispatchtity.getAmount();//金额
				}
				//费用编号
				feesBillEntity.setFeesNo(dispatchtity.getFeesNo());
			}
		}
		//异常
		if (null != feesAbnormalList && feesAbnormalList.size() > 0) {
			List<String> feeNos = new ArrayList<String>();
			for(FeesPayAbnormalEntity abnormalEntity : feesAbnormalList){
				if(StringUtils.isBlank(abnormalEntity.getBillNo())){
					throw new Exception("异常费【"+abnormalEntity.getFeeNo()+"】未生成对账单");
				}
				//新增判断该剔除的费用
				//商家原因
				if(4==abnormalEntity.getReasonId() && ("1500000015".equals(abnormalEntity.getDeliverId()) || "1500000018".equals(abnormalEntity.getDeliverId()) || "1400000027".equals(abnormalEntity.getDeliverId()))){
					totolePrice+=abnormalEntity.getPayMoney();
				}else if(2==abnormalEntity.getReasonId()){
					//承运商原因
					if("0".equals(abnormalEntity.getIsDeliveryFree())){
						//免运费
						totolePrice-=(abnormalEntity.getPayMoney()+abnormalEntity.getDeliveryCost()+abnormalEntity.getAmerceAmount());	
					}else{
						//不免运费
						totolePrice-=(abnormalEntity.getPayMoney()+abnormalEntity.getAmerceAmount());
					}
				}
				//费用编号
				feesBillEntity.setFeesNo(abnormalEntity.getFeeNo());
				feeNos.add(abnormalEntity.getFeeNo());
			}
			
			// 先删除配送费用表中的理赔费用
			Map<String, Object> paramter = new HashMap<String, Object>();
			paramter.put("billNo", entity.getBillNo());
			paramter.put("feeNos", feeNos);
			int delNum = feesPayDispatchService.deleteFeesBillAbnormal(paramter);
			if (delNum != feesAbnormalList.size()) {
				throw new BizException("删除配送理赔费用失败!");
			}
		}
		if(null!=feesAbnormalTransportList&&feesAbnormalTransportList.size()>0){
			for(FeesPayTransportEntity abnormalEntity : feesAbnormalTransportList){
				if(StringUtils.isBlank(abnormalEntity.getBillno())){
					throw new Exception("运输费【"+abnormalEntity.getFeesNo()+"】未生成对账单");
				}
				if (null != abnormalEntity.getTotleprice()) {
					totolePrice = totolePrice + abnormalEntity.getTotleprice();//累计总金额
				}
				//费用编号
				feesBillEntity.setFeesNo(abnormalEntity.getFeesNo());
			}
		}
		//账单要修改的信息
		feesBillEntity.setBillNo(billNo);
		feesBillEntity.setTotleprice(billEntity.getTotleprice() - totolePrice);
		feesBillEntity.setLastModifier(operatorNmae);
		feesBillEntity.setLastModifyTime(operatorTime);
		feesBillEntity.setStatus(ConstantInterface.FeeStatus.STATUS_0);//未过账
		feesPayBillService.delBill(feesBillEntity, entity);
	}
	
	/**
	 * 核销账单
	 */
	@DataResolver
	public void confirmFeesBill(FeesPayBillEntity entity) throws Exception{
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		entity.setStatus(ConstantInterface.FeeStatus.STATUS_1);//已过账 -1 未过账-0
		feesPayBillService.confirmFeesBill(entity);
	}
	
	/**
	 * 删除账单
	 */
	@DataResolver
	public void deleteFeesBill(FeesPayBillEntity entity) throws Exception{
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		entity.setStatus(ConstantInterface.FeeStatus.STATUS_0);//已过账 -1 未过账-0
		entity.setDelFlag(String.valueOf(ConstantInterface.InvalidInterface.INVALID_1));//0-未作废  1-已作废
		feesPayBillService.deleteFeesBill(entity);
	}
	
	/**
	 * 处理页面自定义的账单费用信息,计算总金额
	 * 默认全部
	 * 自定义生成账单
	 * 费用详情添加账单
	 */
	private double handFeesBillReceiveEntity(String billNo, FeesPayBillEncapEntity entity) throws Exception{
		if(null == entity){
			throw new Exception("未找到物流商相关运输,配送,异常账单信息！");
		}
		
		List<FeesPayTransportEntity> feesTransportList= entity.getFeesTransportList();
		List<FeesPayDispatchEntity> feesDispatchList = entity.getFeesDispatchList();
		List<FeesPayAbnormalEntity> feesAbnormalList = entity.getFeesAbnormalList();
		if((null == feesTransportList || feesTransportList.size() == 0) &&
				(null == feesDispatchList || feesDispatchList.size() == 0) &&
				(null == feesAbnormalList || feesAbnormalList.size() == 0)){
			throw new Exception("未找到 商家相关运输,配送,异常账单信息！");
		}
		
		//自定义生成账单,生成账单编号
		if(StringUtils.isBlank(billNo)){
			billNo=sequenceService.getBillNoOne(FeesBillEntity.class.getName(), "Z", "0000000000");
			if (StringUtils.isBlank(billNo)) {
				throw new Exception("生成账单编号失败,请稍后重试!");
			}
			entity.setBillNo(billNo);
		}
		//计算账单金额
		double totolePrice=0;
		String operatorId = JAppContext.currentUserID();
		String operatorNmae = JAppContext.currentUserName();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		//运输
		if (null != feesTransportList && feesTransportList.size() > 0) {
			for(FeesPayTransportEntity deliverentity : feesTransportList){
				if(StringUtils.isNotBlank(deliverentity.getBillno())){
					throw new Exception("运输费【"+deliverentity.getFeesNo()+"】已生成对账单，账单编号【"+deliverentity.getBillno()+"】");
				}
				deliverentity.setModperson(operatorId);
				deliverentity.setModpersonname(operatorNmae);
				deliverentity.setModtime(operatorTime);
				deliverentity.setBillno(billNo);//账单编号
				deliverentity.setState(ConstantInterface.FeeStatus.STATUS_1);//已过账
				if (null != deliverentity.getTotleprice()) {
					totolePrice = totolePrice + deliverentity.getTotleprice();//累计总金额
				}
			}
		}
		//配送
		if (null != feesDispatchList && feesDispatchList.size() > 0) {
			for(FeesPayDispatchEntity dispatchtity : feesDispatchList){
				if(StringUtils.isNotBlank(dispatchtity.getBillNo())){
					throw new Exception("配送费【"+dispatchtity.getFeesNo()+"】已生成对账单，账单编号【"+dispatchtity.getBillNo()+"】");
				}
				dispatchtity.setBillNo(billNo);//账单编号
				dispatchtity.setStatus(ConstantInterface.FeeStatus.STATUS_1);//1 已过账
				dispatchtity.setLastModifier(operatorNmae);
				dispatchtity.setLastModifyTime(operatorTime);
				if (null != dispatchtity.getAmount()) {
					totolePrice = totolePrice + dispatchtity.getAmount();//金额
				}
			}
		}
		//异常
		if (null != feesAbnormalList && feesAbnormalList.size() > 0) {
			List<FeesPayDispatchEntity> feesPayDispatchList = new ArrayList<FeesPayDispatchEntity>();
			for(FeesPayAbnormalEntity abnormalEntity : feesAbnormalList){
				if(StringUtils.isNotBlank(abnormalEntity.getBillNo())){
					throw new Exception("异常费【"+abnormalEntity.getFeeNo()+"】已生成对账单，账单编号【"+abnormalEntity.getBillNo()+"】");
				}
				abnormalEntity.setBillNo(billNo);//账单编号
				abnormalEntity.setIsCalculated(ConstantInterface.FeeStatus.STATUS_1);//1 已过账
				
				Double feeAmount = 0d;// 每条理赔费用金额
				//商家原因
				if(4==abnormalEntity.getReasonId() && ("1500000015".equals(abnormalEntity.getDeliverId()) || "1500000018".equals(abnormalEntity.getDeliverId()) || "1400000027".equals(abnormalEntity.getDeliverId()))){
					double tamount = abnormalEntity.getPayMoney();
					feeAmount += tamount;
					totolePrice += tamount;
				}else if(2==abnormalEntity.getReasonId()){
					//承运商原因
					if("0".equals(abnormalEntity.getIsDeliveryFree())){
						//免运费
						double tamount = abnormalEntity.getPayMoney() + abnormalEntity.getDeliveryCost() + abnormalEntity.getAmerceAmount();
						feeAmount -= tamount;
						totolePrice -= tamount;
					}else{
						//不免运费
						double tamount = abnormalEntity.getPayMoney() + abnormalEntity.getAmerceAmount();
						feeAmount -= tamount;
						totolePrice -= tamount;
					}
				}
				
				// 把理赔费用新增到宅配费用表
				// 把异常费用写入到配送费用表中
				FeesPayDispatchEntity dispatchEntity = new FeesPayDispatchEntity();
				dispatchEntity.setBillNo(billNo);
				dispatchEntity.setFeesNo(abnormalEntity.getFeeNo());
				dispatchEntity.setWaybillNo(abnormalEntity.getExpressnum());// 运单号
				dispatchEntity.setExternalNo(abnormalEntity.getReference());// 外部单号
				dispatchEntity.setCustomerid(abnormalEntity.getCustomerId());
				dispatchEntity.setCustomerName(abnormalEntity.getCustomerName());
				dispatchEntity.setWarehouseCode(abnormalEntity.getWarehouseId());
				dispatchEntity.setWarehouseName(abnormalEntity.getWarehouseName());
				dispatchEntity.setCarrierid(abnormalEntity.getCarrierId());
				dispatchEntity.setCarrierName(abnormalEntity.getCarrierName());
				dispatchEntity.setDeliveryid(abnormalEntity.getDeliverId());
				dispatchEntity.setDeliverName(abnormalEntity.getDeliverName());
				dispatchEntity.setAmount(feeAmount);// 金额
				dispatchEntity.setDerateAmount(BigDecimal.ZERO);// 减免金额
				dispatchEntity.setSubjectCode("de_abnormal_pay");
				dispatchEntity.setOtherSubjectCode("de_abnormal_pay");
				dispatchEntity.setStatus("1");
				dispatchEntity.setIsCalculated("1");
				dispatchEntity.setCreator(abnormalEntity.getCreatePersonName());
				dispatchEntity.setCreateTime(abnormalEntity.getCreateTime());
				dispatchEntity.setLastModifier(operatorNmae);
				dispatchEntity.setLastModifyTime(operatorTime);
				dispatchEntity.setDelFlag(ConstantInterface.DelFlag.NO);
				
				feesPayDispatchList.add(dispatchEntity);
			}
			
			if (null != feesPayDispatchList && feesPayDispatchList.size() > 0) {
				int updDispatchNum = feesPayDispatchService.insertBatchTmp(feesPayDispatchList);
				if (updDispatchNum != feesPayDispatchList.size()) {
					throw new BizException("理赔费用添加到宅配费用表失败!");
				}
			}
		}
		
		return totolePrice;
	}
	
	
	
	/**
	 * 获取承运商 最后一次生成账单 时间
	 */
	@Expose
	public FeesPayBillEntity getLastBillTime(String parameter) throws Exception{
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("forwarderId", parameter);
		List<FeesPayBillEntity> list = feesPayBillService.getlastBillTime(maps);
		FeesPayBillEntity entity = null;
		if (null != list && list.size() > 0) {
			entity = list.get(0);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String dateString=formatter.format(entity.getEndTime());
			entity.setEndTime(Timestamp.valueOf(dateString));
		}
		return entity;
	}
	
	/**
	 * 获取宅配商 最后一次生成账单 时间
	 */
	@Expose
	public List<FeesPayBillEntity> getLastBillTimeDelivery(String parameter) throws Exception{
		List<String> deliveridList=new ArrayList<String>();
		String[] arr=parameter.split(",");
		for(String s:arr){
			deliveridList.add(s);
		}
		
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("deliveridList", deliveridList);
		List<FeesPayBillEntity> list = feesPayBillService.getLastBillTimeDelivery(maps);
		return list;
	}
	
	/**
	 * 自定义-运输费用分页查询
	 * 根据账单信息查询运输费用信息
	 * 自定义账单，添加账单
	 */
	@DataProvider
	public void queryTransportInfoPage(Page<FeesPayTransportEntity> page, Map<String, Object> param){
		try {
			if(param != null){
				if (null != param.get("subjectCode") && "全部".equals(param.get("subjectCode").toString())) {
					param.put("subjectCode", null);
				}
				if (null != param.get("operatorTimeBegin")) {
					String beginDate = formatDate(param.get("operatorTimeBegin").toString()) +" 00:00:00";
					//20170801 修改前按照操作时间生成账单，修改后按照创建时间生成账单(直接修改了sql)
					param.put("operatorTimeBegin", beginDate);
				}
				if (null != param.get("operatorTimeEnd")) {
					String endDate = formatDate(param.get("operatorTimeEnd").toString()) + " 23:59:59";
					param.put("operatorTimeEnd", endDate);
				}
				PageInfo<FeesPayTransportEntity> pageInfo=feesPayBillService.queryTransportInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			logger.error("运输费查询错误", e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
	}
	
	/**
	 * 自定义-配送费用查询
	 * 根据账单信息查配送输费用信息
	 */
	@DataProvider
	public void queryDispatchInfoPage(Page<FeesPayDispatchEntity> page, Map<String, Object> param){
		try {
			if(param !=null){
				if (null != param.get("operatorTimeBegin")) {
					String beginDate = formatDate(param.get("operatorTimeBegin").toString()) +" 00:00:00";
					//20170801 修改前按照操作时间生成账单，修改后按照创建时间生成账单
					param.put("operatorTimeBegin", null);
					param.put("startTime", beginDate);
				}
				if (null != param.get("operatorTimeEnd")) {
					String endDate = formatDate(param.get("operatorTimeEnd").toString()) + " 23:59:59";
					param.put("operatorTimeEnd", null);
					param.put("endTime", endDate);
				}
				List<String> deliverIdList=new ArrayList<String>();
				if(null != param.get("deliveryid")){
					String deliveryid=param.get("deliveryid").toString();
					String[] arr=deliveryid.split(",");
					for(String s:arr){
						deliverIdList.add(s);
					}
				}
				param.put("deliverIdList", deliverIdList);
				param.put("deliveryid ","");
				param.put("status", ConstantInterface.FeeStatus.STATUS_0);
				PageInfo<FeesPayDispatchEntity> pageInfo = feesPayBillService.queryDispatchInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			logger.error("配送费查询错误", e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}	
	}
	
	/**
	 * 自定义-异常费用查询
	 * 根据账单信息查询异常费用信息
	 */
	@DataProvider
	public void queryAbnormalInfoPage(Page<FeesPayAbnormalEntity> page, Map<String, Object> param){
		try {
			if(param != null){
				if (null != param.get("operatorStartTime")) {
					String beginDate = formatDate(param.get("operatorStartTime").toString()) +" 00:00:00";
					param.put("operatorStartTime", beginDate);
				}
				if (null != param.get("operatorEndTime")) {
					String endDate = formatDate(param.get("operatorEndTime").toString()) + " 23:59:59";
					param.put("operatorEndTime", endDate);
				}
				
				param.put("isCalculated", ConstantInterface.FeeStatus.STATUS_0);
				List<String> deliverIdList=new ArrayList<String>();
				if(null != param.get("deliveryid")){
					String deliveryid=param.get("deliveryid").toString();
					String[] arr=deliveryid.split(",");
					for(String s:arr){
						deliverIdList.add(s);
					}
				}
				param.put("deliverIdList", deliverIdList);
				param.put("deliveryid ","");
				PageInfo<FeesPayAbnormalEntity> pageInfo = feesPayBillService.queryAbnormalInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			logger.error("异常费用查询错误", e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
	}
	
	
	/**
	 * 格式化查询日期
	 */
	@SuppressWarnings("deprecation")
	private static String formatDate(String dateString) throws Exception{
		if(null != dateString){
			DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
			dateformatter.setTimeZone(TimeZone.getTimeZone("CST"));
			dateString = dateformatter.format(new Date(dateString));
			return dateString;
		}
		return null;
	}
	
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, Object> param) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/bill/paydispatchdistinct_template.xlsx");
		return new DownloadFile("九曳宅配应付对账单导入模板.xlsx", is);
	}
	
	/**
	 * 获取进度数值
	 */
	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
    
	/**
	 * 设置进度数值
	 */
	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
	
	
	private FeesPayDispatchEntity initfeeEntity(BizTihuoBillEntity entity,CalcuResultVo calcuVo,PriceContractInfoEntity contract){
		FeesPayDispatchEntity feeEntity = new FeesPayDispatchEntity();
		feeEntity.setCreator("system");
		feeEntity.setCreateTime(entity.getCountDate());//费用表的创建时间应为业务表的创建时间
		feeEntity.setDeliveryid(contract.getCustomerId());         // 宅配ID
		feeEntity.setDeliverName(contract.getCustomerName());				
		feeEntity.setTotalQuantity(entity.getTotalNum());
		feeEntity.setSubjectType("DISPATCH_OTHER_SUBJECT_TYPE");  //费用累别（其他费用）
		feeEntity.setSubjectCode("DISPATCH_OTHER_SUBJECT_TYPE_TIHUO");//费用类型 (增值费)	
		feeEntity.setOtherSubjectCode("de_takes");
		feeEntity.setStatus("0");
		feeEntity.setDelFlag("0");
		feeEntity.setAmount(0.0d);
		feeEntity.setFeesNo(entity.getFeesNo());
		return feeEntity;
		
	}
	
	/**
	 * 默认生成账单时判断是否有异常数据
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String isCalculatedTrue(FeesPayBillEntity entity){
		if(null != entity.getStartTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getStartTime());
			dateString=dateString+" 00:00:00";
			entity.setStartTime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getEndTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getEndTime());
			dateString=dateString+" 23:59:59";
			entity.setEndTime(Timestamp.valueOf(dateString));
		}
		if(!StringUtils.isBlank(entity.getDeliverSelectlist())){
			List<DeliverEntity> deliverList=new ArrayList<DeliverEntity>();
			String[] arr=entity.getDeliverSelectlist().split(",");
			DeliverEntity model=null;
			for(String s:arr){
				String[] arr2=s.split("&");
				model=new DeliverEntity();
				model.setDeliverid(arr2[0]);
				model.setDelivername(arr2[1]);
				deliverList.add(model);
			}
			entity.setDeliverList(deliverList);
		}
		
		
		//查找该宅配商的合同，判断是否签约提货费
		Map<String,Object> aCondition=new HashMap<>();
		aCondition.put("deliveridList", entity.getDeliverList());
		aCondition.put("contractTypeCode", "DELIVER_CONTRACT");
		List<PriceContractInfoEntity> contractList=priceContractService.queryContract(aCondition);

		if(contractList.size()<=0){
			return "该宅配商未签合同，无法生成账单";
		}else{
			if(entity.getDeliverList().size()>0 && contractList.size()!=entity.getDeliverList().size()){
				return "该宅配商中有子宅配商未签约合同，无法生成账单";
			}
		}
		if(contractList.size()>=0 && entity.getDeliverList().size()>=0 && contractList.size()==entity.getDeliverList().size()){
			for(int j=0;j<contractList.size();j++){
				//合同
				PriceContractInfoEntity contract=contractList.get(j);
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("deliverid", contract.getCustomerId());
				condition.put("createTime",entity.getStartTime());
				condition.put("endTime", entity.getEndTime());
				BizDispatchBillPayEntity bizDispatchBillPayEntity=bizDispatchBillPayService.queryExceptionOne(condition);
				if(bizDispatchBillPayEntity!=null){
					return "宅配商"+contract.getCustomerName()+"中存在计算异常的数据,无法生成账单";
				}
			}
		}
		
		return "";
	}
	
	/**
	 * 自定义生成账单时判断是否有异常数据
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String isCustomCalculatedTrue(FeesPayBillEncapEntity entity){
		if(null != entity.getStartTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getStartTime());
			dateString=dateString+" 00:00:00";
			entity.setStartTime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getEndTime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getEndTime());
			dateString=dateString+" 23:59:59";
			entity.setEndTime(Timestamp.valueOf(dateString));
		}
		if(!StringUtils.isBlank(entity.getDeliverSelectlist())){
			List<DeliverEntity> deliverList=new ArrayList<DeliverEntity>();
			String[] arr=entity.getDeliverSelectlist().split(",");
			DeliverEntity model=null;
			for(String s:arr){
				String[] arr2=s.split("&");
				model=new DeliverEntity();
				model.setDeliverid(arr2[0]);
				model.setDelivername(arr2[1]);
				deliverList.add(model);
			}
			entity.setDeliverList(deliverList);
		}
		
		
		//查找该宅配商的合同，判断是否签约提货费
		Map<String,Object> aCondition=new HashMap<>();
		aCondition.put("deliveridList", entity.getDeliverList());
		aCondition.put("contractTypeCode", "DELIVER_CONTRACT");
		List<PriceContractInfoEntity> contractList=priceContractService.queryContract(aCondition);

		if(contractList.size()<=0){
			return "该宅配商未签合同，无法生成账单";
		}else{
			if(entity.getDeliverList().size()>0 && contractList.size()!=entity.getDeliverList().size()){
				return "该宅配商中有子宅配商未签约合同，无法生成账单";
			}
		}
		if(contractList.size()>=0 && entity.getDeliverList().size()>=0 && contractList.size()==entity.getDeliverList().size()){
			for(int j=0;j<contractList.size();j++){
				//合同
				PriceContractInfoEntity contract=contractList.get(j);
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("deliverid", contract.getCustomerId());
				condition.put("createTime",entity.getStartTime());
				condition.put("endTime", entity.getEndTime());
				BizDispatchBillPayEntity bizDispatchBillPayEntity=bizDispatchBillPayService.queryExceptionOne(condition);
				if(bizDispatchBillPayEntity!=null){
					return "宅配商"+contract.getCustomerName()+"中存在计算异常的数据,无法生成账单";
				}
			}
		}
		
		return "";
	}
	@Expose
	public String countTIHUOBill(Map<String,Object> param){
		try{
			if(!StringUtils.isBlank(param.get("deliverSelectlist").toString())){
				List<DeliverEntity> deliverList=new ArrayList<DeliverEntity>();
				String[] arr=param.get("deliverSelectlist").toString().split(",");
				DeliverEntity model=null;
				for(String s:arr){
					String[] arr2=s.split("&");
					model=new DeliverEntity();
					model.setDeliverid(arr2[0]);
					model.setDelivername(arr2[1]);
					deliverList.add(model);
				}
				param.put("deliveridList", deliverList);
			}
			countDispatchBill(param);
			return "succ";
		}catch(Exception e){
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return e.getMessage();
		}
	}
	/**
	 * 生成账单时去统计宅配运单的单量并计算提货费
	 * @param entity
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Expose
	public void countDispatchBill(Map<String,Object> param) throws Exception{
		//查找该宅配商的合同，判断是否签约提货费
		Map<String,Object> aCondition=new HashMap<>();
		aCondition.put("deliveridList", param.get("deliverList"));
		aCondition.put("contractTypeCode", "DELIVER_CONTRACT");
		List<PriceContractInfoEntity> contractList=priceContractService.queryContract(aCondition);
		//若未签合同，则不计算l
		if(contractList.size()<=0){
			logger.info("未签合同，则不计算");
			return;
		}
		for(int j=0;j<contractList.size();j++){
			//合同
			PriceContractInfoEntity contract=contractList.get(j);
			//通过合同编号查询是否签约提货报价
			boolean isQian=false;
			Map<String,Object> contractCondition=new HashMap<String,Object>();
			contractCondition.put("contractCode", contract.getContractCode());
			List<ContractDetailEntity> detailList=priceContractService.findAllPayContractItem(contractCondition);
			for(int i=0;i<detailList.size();i++){
				ContractDetailEntity en=detailList.get(i);
				if("DISPATCH_OTHER_SUBJECT_TYPE".equals(en.getSubjectId())){
					isQian=true;
					break;
				}
			}
			if(isQian==false){
				logger.info("该宅配商未签约配送其他报价，则不计算");
				return;
			}
			
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("deliverid", contract.getCustomerId());
			condition.put("startTime", param.get("startTime"));
			condition.put("endTime", param.get("endTime"));
			//删除该宅配商原有的数据
			bizDispatchTihuoBillService.deleteBizByMap(condition);
			
			//该宅配商每日的统计业务数据
			List<BizTihuoBillEntity> bizTihuoList=bizDispatchTihuoBillService.countByDate(condition);
			for(int i=0;i<bizTihuoList.size();i++){
				BizTihuoBillEntity e=bizTihuoList.get(i);
				e.setDelFlag("0");
				e.setIsCalculated("0");
				e.setCreator(JAppContext.currentUserName());
				e.setCreateTime(e.getCountDate());
				e.setLastModifier(JAppContext.currentUserName());
				e.setLastModifyTime(JAppContext.currentTimestamp());
			}
			//将该宅配商的业务数据写入提货业务数据表
			int result=bizDispatchTihuoBillService.insertBillList(bizTihuoList);
			if(result<=0){
				logger.info("统计提货业务数据异常");
				return;
			}
			
			//删除原费用数据
			Map<String,Object> feeCondition=new HashMap<String,Object>();
			feeCondition.put("deliverid", contract.getCustomerId());
			condition.put("startTime", param.get("startTime"));
			condition.put("endTime", param.get("endTime"));
			feeCondition.put("subjectCode","DISPATCH_OTHER_SUBJECT_TYPE_TIHUO");
			feeCondition.put("subjectType","DISPATCH_OTHER_SUBJECT_TYPE");
			feesPayDispatchService.deleteFeesByMap(feeCondition);
			
			//开始进行计算,查询该宅配商未计算的数据
			Map<String,Object> condition1=new HashMap<String,Object>();
			condition1.put("deliverid", contract.getCustomerId());
			condition1.put("isCalculated", "0");
			condition1.put("startTime", param.get("startTime"));
			condition1.put("endTime", param.get("endTime"));
			List<BizTihuoBillEntity> bizList=bizDispatchTihuoBillService.queryData(condition1);
			for(BizTihuoBillEntity bizEntity:bizList){
				
				//通过该商家id和费用科目查询计费规则
				Map<String, Object> ruleParam = new HashMap<String, Object>();
				ruleParam.put("deliveryid", bizEntity.getDeliverid());
				ruleParam.put("subjectId","DISPATCH_OTHER_SUBJECT_TYPE");
				BillRulePayEntity ruleEntity=payRuleService.queryByDeliverId(ruleParam);
				if(ruleEntity==null){
					logger.info("未查询到该商家的规则");
					return;
				}
				//查询报价
				
				List<PriceOutDispatchOtherTemplateEntity> tempList = priceOutDispatchOtherTemplateService.queryDeliverTemplate(ruleParam);
				if(tempList.size()<=0){
					logger.info("报价缺失");
					return;
				}
				CalcuReqVo reqVo= new CalcuReqVo();
				reqVo.setQuoEntites(tempList);
				reqVo.setBizData(bizEntity);
				reqVo.setRuleNo(ruleEntity.getQuotationNo());
				reqVo.setRuleStr(ruleEntity.getRule());
				
				//CalcuResultVo vo=feesCalcuService.FeesCalcuService(reqVo);
				CalcuResultVo vo= new CalcuResultVo();
				
				try{	
					//CalcuResultVo calcuVo=feesCalcuService.FeesCalcuService(reqVo);
					CalcuResultVo calcuVo= new CalcuResultVo();
					FeesPayDispatchEntity feePayEntity = initfeeEntity(bizEntity,calcuVo,contract);
					//将费用插入费用表
					if("succ".equals(vo.getSuccess())){
						if(calcuVo.getPrice()!=null){
							feePayEntity.setAmount(calcuVo.getPrice().doubleValue()); //价格
						}else{
							feePayEntity.setAmount(0.0d);
						}
						bizEntity.setIsCalculated(CalculateState.Finish.getCode());
						bizEntity.setRemark("费用计算成功");
					}else{
						bizEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						bizEntity.setRemark("费用计算失败:"+calcuVo.getMsg());
					}
					if(feesPayDispatchService.InsertOne(feePayEntity)){
					}
					else{
						bizEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
						bizEntity.setRemark("计算成功，操作配送费用表表失败");
						logger.info("写入配送费用表失败");
					}
					bizEntity.setCalculateTime(JAppContext.currentTimestamp());
					bizDispatchTihuoBillService.update(bizEntity);
					
				}catch(Exception ex){
					logger.info("配送费用计算失败");
					bizEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
					bizEntity.setRemark("费用计算异常:"+ex.getMessage());
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					bizDispatchTihuoBillService.update(bizEntity);
				}
				
				
				/*
				CalcuReqVo reqVo= new CalcuReqVo();
				List<PriceOutMainDispatchEntity> list=new ArrayList<>(data.getPriceList());
				reqVo.setQuoEntites(list);
				reqVo.setBizData(bizEntity);
				reqVo.setRuleNo(ruleEntity.getQuotationNo());
				reqVo.setRuleStr(ruleEntity.getRule());
				
				CalcuResultVo vo=feesCalcuService.FeesCalcuService(reqVo);
				
				
				CalculateVo calcuVo = new CalculateVo();
				calcuVo.setBizTypeCode("DISPATCH");
				calcuVo.setType("pay");
				calcuVo.setSubjectId("DISPATCH_OTHER_SUBJECT_TYPE");
				calcuVo.setExtter1("DISPATCH_OTHER_SUBJECT_TYPE_TIHUO");
				calcuVo.setContractCode(contract.getContractCode());
				calcuVo.setObj(bizEntity);
				
				String feesNo =sequenceService.getBillNoOne(FeesPayDispatchEntity.class.getName(), "PSYFT", "0000000000");
				bizEntity.setFeesNo(feesNo);
				
				try{	
					calcuVo = feesCalculateRpcServiceImpl.calculate(calcuVo);				
					FeesPayDispatchEntity feePayEntity = initfeeEntity(bizEntity,calcuVo,contract);
					//将费用插入费用表
					if(calcuVo.getSuccess() && calcuVo.getPrice()!=null){
						if(calcuVo.getPrice()!=null){
							feePayEntity.setAmount(calcuVo.getPrice().doubleValue()); //价格
						}else{
							feePayEntity.setAmount(0.0d);
						}
						bizEntity.setIsCalculated(CalculateState.Finish.getCode());
						bizEntity.setRemark("费用计算成功");
					}else{
						bizEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						bizEntity.setRemark("费用计算失败:"+calcuVo.getMsg());
					}
					feePayEntity.setTemplateId(calcuVo.getMobanCode());
					if(feesPayDispatchService.InsertOne(feePayEntity)){
					}
					else{
						bizEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
						bizEntity.setRemark("计算成功，操作配送费用表表失败");
						logger.info("写入配送费用表失败");
					}
					bizEntity.setCalculateTime(JAppContext.currentTimestamp());
					bizDispatchTihuoBillService.update(bizEntity);
					
				}catch(Exception ex){
					logger.info("配送费用计算失败");
					bizEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
					bizEntity.setRemark("费用计算异常:"+ex.getMessage());
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					bizDispatchTihuoBillService.update(bizEntity);
				}
				
			}	*/
			}
		}	
	}
	
	
/*
	private List<PriceOutMainDispatchEntity> getQuoEntites(BizTihuoBillEntity entity){
		String subjectId=getSubjectId(entity.getCarrierId());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deliverId", entity.getDeliverid());
		map.put("subjectId", subjectId);
		map.put("wareHouseId", entity.getWarehouseCode());
		map.put("province", entity.getReceiveProvinceId());
		List<PriceOutMainDispatchEntity> priceOutList=outPriceDispatchDao.queryAllOutDispatch(map);
		return priceOutList;
	}
	
	
*/
	
/*	
	private List<PriceOutMainDispatchEntity> getQuoEntites(BizDispatchBillPayEntity entity){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deliverId", entity.getDeliverid());
		map.put("subjectId", "de_takes");
		map.put("wareHouseId", entity.getWarehouseCode());
		map.put("province", entity.getReceiveProvinceId());
		List<PriceOutMainDispatchEntity> priceOutList=outPriceDispatchDao.queryAllOutDispatch(map);
		return priceOutList;
	}*/
	/**
	 * 枚举值
	 * @return
	 */
	@DataProvider
	public Map<String, String> getEnum() {
		
		Map<String, String> mapValue = new LinkedHashMap<String, String>();

		mapValue.put("0", "是");
		mapValue.put("1", "否");
		
		return mapValue;
	}
	
	public double getAbnormalMoney(FeesBillWareHouseEntity entity){		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("billNo", entity.getBillNo());
		FeesPayBillEntity billEntity = feesPayBillService.queryBillInfo(condition);
		if (null == billEntity) {
			return 0d;
		}
		
		Double totalMoney=0d;
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("deliveryid", entity.getDeliveryid());
		paramMap.put("startTime",billEntity.getStartTime());
		paramMap.put("endTime", billEntity.getEndTime());
		paramMap.put("reasonId", entity.getReasonId());
		paramMap.put("isCalculated", "1");
		List<FeesPayAbnormalEntity> feeList=feesPayAbnormalService.queryFeeByParam(paramMap);
		
		/*如果账单总额为100元，某单赔偿金额为10运，运费5元(免运费），罚款5元，且为承运商原因
                   最终账单金额 = 100 - （10+5+5）
                   如果账单总额为100元，某单赔偿金额为10元，运费5元（免运费 ）， 且为商家原因
                   最终账单金额 = 100+10 （无论是否免运费，无论有无罚款金额）
                   原因归属为商家原因，获取的金额为赔偿金额；目前涉及承运商有顺丰和中通*/
		//仓储理赔
		for(FeesPayAbnormalEntity fee:feeList){
			//商家原因
			if(4==fee.getReasonId() && ("1500000015".equals(fee.getDeliverId()) || "1500000018".equals(fee.getDeliverId()) || "1400000027".equals(fee.getDeliverId()))){
				totalMoney+=fee.getPayMoney();
			}else if(2==fee.getReasonId()){
				//承运商原因
				if("0".equals(fee.getIsDeliveryFree())){
					//免运费
					//根据运单号查询费用
					totalMoney-=(fee.getPayMoney()+fee.getDeliveryCost()+fee.getAmerceAmount());
	
				}else{
					//不免运费
					totalMoney-=(fee.getPayMoney()+fee.getAmerceAmount());
				}
			}
		}
		
		return totalMoney;
	} 
	
	@DataProvider
	public Map<String,String> getSubjectMap(){
		// List<SystemCodeEntity> list=systemCodeService.findEnumList("TRANSPORT_TEMPLATE_PAY_TYPE");
		 Map<String, String> map =bmsGroupSubjectService.getSubject("pay_ts_contract_subject");

		 map.put("ts_abnormal_pay", "运输理赔");
		 return map;
	}
	
	@DataProvider
	public Map<String,String> getOtherSubjectMap(){
		 //List<SystemCodeEntity> baselist=systemCodeService.findEnumList("ts_base_subject");
		 //List<SystemCodeEntity> addlist=systemCodeService.findEnumList("ts_value_add_subject");
		 Map<String, String> map =bmsGroupSubjectService.getSubject("pay_transport_bill_subject");
		 return map;
	}
}
