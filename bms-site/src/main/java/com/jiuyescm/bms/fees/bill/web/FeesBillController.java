package com.jiuyescm.bms.fees.bill.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.biz.storage.service.IBizProductStorageService;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.IFeesBillService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillQueryEntity;
import com.jiuyescm.bms.fees.entity.FeesBillReceiveEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

/**
 * 应收账单controller类
 * @author yangss
 */
@Controller("feesBillController")
public class FeesBillController {

	private static final Logger logger = Logger.getLogger(FeesBillController.class.getName());
	
	@Resource
	private IFeesBillService feesBillService;
	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;

	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IPriceContractService priceContractService;
	
	@Resource
	private IBizOutstockMasterService bizOutstockMasterService;
	
	@Resource 
	private IBizProductStorageService bizProductStorageService;
	
	@Resource
	private IBizPackStorageService bizPackStorageService;
	
	@Resource
	private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	
	@Resource
	private IBizDispatchBillService bizDispatchBillService;
	
	@Resource
	private IBizGanxianWayBillService bizGanxianWayBillService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	/**
	 * 账单分页查询
	 */
	@DataProvider
	public  void queryList(Page<FeesBillEntity> page,List<FeesBillQueryEntity> parameter){
		FeesBillQueryEntity queryEntity=null;
		if(parameter==null){
			queryEntity=new FeesBillQueryEntity();
		}
		else{
			queryEntity=parameter.get(0);
		}
		PageInfo<FeesBillEntity> pageInfo = feesBillService.query(queryEntity, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 生成账单-默认全部
	 */
	@DataResolver
	public void save(FeesBillEntity entity) throws Exception{
		long beginTime = System.currentTimeMillis();
		if(null == entity){
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
		}

		if(entity.getBillstarttime()!=null){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillstarttime());
			dateString=dateString+" 00:00:00";
			entity.setBillstarttime(Timestamp.valueOf(dateString));
		}
		if(entity.getBillendtime()!=null){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillendtime());
			dateString=dateString+" 23:59:59";
			entity.setBillendtime(Timestamp.valueOf(dateString));
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		//生成账单查询的是状态为未过账的
		entity.setBillstatus(ConstantInterface.FeeBillStatus.STATUS_0);
		
		feesBillService.generReceiverBill(entity);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
		logger.info("======一共用时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 自定义生成账单
	 */
	@DataResolver
	public void customBill(FeesBillReceiveEntity entity) throws Exception{
		//处理自定义数据
		double totolePrice = handFeesBillReceiveEntity(null, entity);
		
		FeesBillEntity feesBillEntity = new FeesBillEntity();
		if (null != entity.getBillstarttime()) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillstarttime());
			dateString=dateString+" 00:00:00";
			feesBillEntity.setBillstarttime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getBillendtime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillendtime());
			dateString=dateString+" 23:59:59";
			feesBillEntity.setBillendtime(Timestamp.valueOf(dateString));
		}
		feesBillEntity.setBillno(entity.getBillNo());
		feesBillEntity.setBillname(entity.getBillname());
		feesBillEntity.setCustomerid(entity.getCustomerid());
		feesBillEntity.setCustomerName(entity.getCustomerName());
		feesBillEntity.setTotleprice(totolePrice);
		feesBillEntity.setCreperson(JAppContext.currentUserID());
		feesBillEntity.setCrepersonname(JAppContext.currentUserName());
		feesBillEntity.setCretime(JAppContext.currentTimestamp());
		feesBillEntity.setBillstatus(ConstantInterface.FeeBillStatus.STATUS_0);//0 未过账 1已过账
		feesBillEntity.setDelflag(ConstantInterface.InvalidInterface.INVALID_0);//0 正常
		feesBillService.saveAll(feesBillEntity, entity);
		
	}
	
	/**
	 * 添加账单
	 */
	@DataResolver
	public void addBill(FeesBillReceiveEntity entity) throws Exception{
		if(null == entity){
			throw new Exception("未找到商家相关异常账单信息！");
		}
		//查询账单信息
		if (StringUtils.isBlank(entity.getBillNo())) {
			throw new Exception("请选择账单！");
		}
		FeesBillQueryEntity queryEntity = new FeesBillQueryEntity();
		queryEntity.setBillno(entity.getBillNo());
		FeesBillEntity billEntity = feesBillService.queryBillInfo(queryEntity);
		if (null == billEntity) {
			throw new Exception("未找该账单信息！");
		}
		if (ConstantInterface.FeeBillStatus.STATUS_1 == billEntity.getBillstatus()) {
			throw new Exception("该账单已核销,不允许操作！");
		}
		
		String billNo = entity.getBillNo();
		//处理自定义数据
		double totolePrice = handFeesBillReceiveEntity(billNo, entity);
		
		//账单要修改的信息
		FeesBillEntity feesBillEntity = new FeesBillEntity();
		feesBillEntity.setBillno(billNo);
		feesBillEntity.setTotleprice(billEntity.getTotleprice() + totolePrice);
		feesBillEntity.setModperson(JAppContext.currentUserID());
		feesBillEntity.setModpersonname(JAppContext.currentUserName());
		feesBillEntity.setModtime(JAppContext.currentTimestamp());
		feesBillService.addBill(feesBillEntity, entity);
	}
	
	/**
	 * 剔除账单
	 */
	@DataResolver
	public void delBill(FeesBillReceiveEntity entity) throws Exception{
		if(null == entity){
			throw new Exception("未找到商家相关异常账单信息！");
		}
		//查询账单信息
		if (StringUtils.isBlank(entity.getBillNo())) {
			throw new Exception("请选择账单！");
		}
		FeesBillQueryEntity queryEntity = new FeesBillQueryEntity();
		queryEntity.setBillno(entity.getBillNo());
		FeesBillEntity billEntity = feesBillService.queryBillInfo(queryEntity);
		if (null == billEntity) {
			throw new Exception("未找该账单信息！");
		}
		if (ConstantInterface.FeeBillStatus.STATUS_1 == billEntity.getBillstatus()) {
			throw new Exception("该账单已核销,不允许操作！");
		}
		
		//处理自定义数据
		List<FeesReceiveDeliverEntity> feesDeliverList= entity.getFeesDeliverList();
		List<FeesReceiveStorageEntity> feesStorgeList = entity.getFeesStorageList();
		List<FeesReceiveDispatchEntity> feesDispatchList = entity.getFeesDispatchList();
		List<FeesAbnormalEntity> feesAbnormalList = entity.getFeesAbnormalList();
		if((null == feesDeliverList || feesDeliverList.size() == 0) &&
				(null == feesStorgeList || feesStorgeList.size() == 0) &&
				(null == feesDispatchList || feesDispatchList.size() == 0) &&
				(null == feesAbnormalList || feesAbnormalList.size() == 0)){
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
		}
		
		//计算账单金额
		double totolePrice=0;
		String billNo = entity.getBillNo();
		String operatorId = JAppContext.currentUserID();
		String operatorNmae = JAppContext.currentUserName();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		//剔除账单操作对象
		FeesBillEntity feesBillEntity = new FeesBillEntity();
		//仓储
		if (null != feesStorgeList && feesStorgeList.size() > 0) {
			for(FeesReceiveStorageEntity storageentity : feesStorgeList){
				if(StringUtils.isBlank(storageentity.getBillNo())){
					throw new Exception("仓储费【"+storageentity.getFeesNo()+"】未生成对账单");
				}
				if (null != storageentity.getCost()) {
					totolePrice = totolePrice + storageentity.getCost().doubleValue();
				}
				//费用编号
				feesBillEntity.setFeesNo(storageentity.getFeesNo());
			}
		}
		//运输
		if (null != feesDeliverList && feesDeliverList.size() > 0) {
			for(FeesReceiveDeliverEntity deliverentity : feesDeliverList){
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
			for(FeesReceiveDispatchEntity dispatchtity : feesDispatchList){
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
			for(FeesAbnormalEntity abnormalEntity : feesAbnormalList){
				if(StringUtils.isBlank(abnormalEntity.getBillNo())){
					throw new Exception("异常费【"+abnormalEntity.getFeeNo()+"】未生成对账单");
				}
				if (null != abnormalEntity.getPayMoney()) {
					totolePrice = totolePrice + abnormalEntity.getPayMoney();//金额
				}
				//费用编号
				feesBillEntity.setFeesNo(abnormalEntity.getFeeNo());
			}
		}
		
		//账单要修改的信息
		feesBillEntity.setBillno(billNo);
		feesBillEntity.setTotleprice(billEntity.getTotleprice() - totolePrice);
		feesBillEntity.setModperson(operatorId);
		feesBillEntity.setModpersonname(operatorNmae);
		feesBillEntity.setModtime(operatorTime);
		feesBillEntity.setBillstatus(ConstantInterface.FeeBillStatus.STATUS_0);//未过账
		feesBillService.delBill(feesBillEntity, entity);
	}
	
	/**
	 * 处理页面自定义的账单费用信息,计算总金额
	 * 默认全部
	 * 自定义生成账单
	 * 费用详情添加账单
	 */
	private double handFeesBillReceiveEntity(String billNo, FeesBillReceiveEntity entity) throws Exception{
		if(null == entity){
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
		}
		
		List<FeesReceiveDeliverEntity> feesDeliverList= entity.getFeesDeliverList();
		List<FeesReceiveStorageEntity> feesStorgeList = entity.getFeesStorageList();
		List<FeesReceiveDispatchEntity> feesDispatchList = entity.getFeesDispatchList();
		List<FeesAbnormalEntity> feesAbnormalList = entity.getFeesAbnormalList();
		if((null == feesDeliverList || feesDeliverList.size() == 0) &&
				(null == feesStorgeList || feesStorgeList.size() == 0) &&
				(null == feesDispatchList || feesDispatchList.size() == 0) &&
				(null == feesAbnormalList || feesAbnormalList.size() == 0)){
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
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
		//仓储
		if (null != feesStorgeList && feesStorgeList.size() > 0) {
			for(FeesReceiveStorageEntity storageentity : feesStorgeList){
				if(StringUtils.isNotBlank(storageentity.getBillNo())){
					throw new Exception("仓储费【"+storageentity.getFeesNo()+"】已生成对账单，账单编号【"+storageentity.getBillNo()+"】");
				}
				storageentity.setBillNo(billNo);//账单编号
				storageentity.setStatus(ConstantInterface.FeeStatus.STATUS_1);//1 已过账
				storageentity.setLastModifier(operatorNmae);
				storageentity.setLastModifyTime(operatorTime);
				if (null != storageentity.getCost()) {
					totolePrice = totolePrice + storageentity.getCost().doubleValue();
				}
			}
		}
		//运输
		if (null != feesDeliverList && feesDeliverList.size() > 0) {
			for(FeesReceiveDeliverEntity deliverentity : feesDeliverList){
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
			for(FeesReceiveDispatchEntity dispatchtity : feesDispatchList){
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
			for(FeesAbnormalEntity abnormalEntity : feesAbnormalList){
				if(StringUtils.isNotBlank(abnormalEntity.getBillNo())){
					throw new Exception("异常费【"+abnormalEntity.getFeeNo()+"】已生成对账单，账单编号【"+abnormalEntity.getBillNo()+"】");
				}
				abnormalEntity.setBillNo(billNo);//账单编号
				abnormalEntity.setIsCalculated(ConstantInterface.FeeStatus.STATUS_1);//1 已过账
				if (null != abnormalEntity.getPayMoney()) {
					totolePrice = totolePrice + abnormalEntity.getPayMoney();//金额
				}
			}
		}
		
		return totolePrice;
	}
	
	/**
	 * 查询仓储 费用 
	 * 仓库 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseStorageAmount(String billNo){
		List<FeesBillWareHouseEntity> list=feesBillService.querywarehouseStorageAmount(billNo);
		return list;
	}
	
	
	/**
	 * 查询 运输费 
	 * 费用科目 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseDeliverAmount(String billNo){
		List<FeesBillWareHouseEntity> list=feesBillService.querywarehouseDeliverAmount(billNo);
		return list;
	}
	
	/**
	 * 查询配送费用 
	 * 物流商 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseDistributionAmount(String billNo){
		List<FeesBillWareHouseEntity> list=feesBillService.querywarehouseDistributionAmount(billNo);
		return list;
	}
	
	/**
	 * 查询异常费用 
	 * 客诉原因 汇总信息
	 */
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(String billNo){
		return feesBillService.querywarehouseAbnormalAmount(billNo);
	}
	
	/**
	 * 仓储费明细分页
	 * 仓库、账单编号
	 */
	@DataProvider
	public void queryStorageDetailGroupPage(Page<FeesReceiveStorageEntity> page,FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			if ("全部".equals(queryEntity.getSubjectCode())) {
				queryEntity.setSubjectCode(null);
			}
			PageInfo<FeesReceiveStorageEntity> pageInfo = feesBillService.queryStorageDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	
	/**
	 * 运输费明细
	 * 费用科目、账单编号
	 */
	@DataProvider
	public void queryDeliverDetailGroupPage(Page<FeesReceiveDeliverEntity> page,FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			PageInfo<FeesReceiveDeliverEntity> pageInfo = feesBillService.queryDeliverDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}		
	}
	
	/**
	 * 配送费明细
	 * 物流商、账单编号
	 */
	@DataProvider
	public void queryDispatchDetailGroupPage(Page<FeesReceiveDispatchEntity> page, FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			PageInfo<FeesReceiveDispatchEntity> pageInfo = feesBillService.queryDispatchDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	
	/**
	 * 异常费明细
	 * 客诉原因、账单编号
	 */
	@DataProvider
	public void queryAbnormalDetailGroupPage(Page<FeesAbnormalEntity> page,FeesBillWareHouseEntity queryEntity){
		if(queryEntity != null){
			PageInfo<FeesAbnormalEntity> pageInfo = feesBillService.queryAbnormalDetailGroupPage(queryEntity, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	
	/**
	 * 核销账单
	 */
	@DataResolver
	public void confirmFeesBill(FeesBillEntity entity) throws Exception{
		entity.setModperson(JAppContext.currentUserID());
		entity.setModpersonname(JAppContext.currentUserName());
		entity.setModtime(JAppContext.currentTimestamp());
		entity.setBillstatus(1);//已过账 -1 未过账-0
		feesBillService.confirmFeesBill(entity);
	}
	
	/**
	 * 删除账单
	 */
	@DataResolver
	public void deleteFeesBill(FeesBillEntity entity) throws Exception{
		entity.setModperson(JAppContext.currentUserID());
		entity.setModpersonname(JAppContext.currentUserName());
		entity.setModtime(JAppContext.currentTimestamp());
		entity.setBillstatus(0);//已过账 -1 未过账-0
		entity.setDelflag(1);//0-未作废  1-已作废
		feesBillService.deleteFeesBill(entity);
	}
	
	/**
	 * 获取商家 最后一次生成账单 时间
	 */
	@Expose
	public FeesBillEntity getLastBillTime(String parameter) throws Exception{
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("customerid", parameter);
		List<FeesBillEntity> list = feesBillService.getlastBillTime(maps);
		FeesBillEntity entity = null;
		if (null != list && list.size() > 0) {
			entity = list.get(0);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String dateString=formatter.format(entity.getBillendtime());
			entity.setBillendtime(Timestamp.valueOf(dateString));
		}
		return entity;
	}
	
	/**
	 * 自定义-仓储费分页查询
	 * 根据账单信息查询、根据查询条件查询都是这个方法
	 */
	@DataProvider
	public void queryStorageInfoPage(Page<FeesReceiveStorageEntity> page,Map<String, Object> param) {
		try {
			if(param != null){
				if (null != param.get("subjectCode") && "全部".equals(param.get("subjectCode").toString())) {
					param.put("subjectCode", null);
				}
				if (null != param.get("operatorTimeBegin")) {
					String beginDate = formatDate(param.get("operatorTimeBegin").toString()) +" 00:00:00";
					//20170801 修改前按照操作时间生成账单，修改后按照创建时间生成账单
					param.put("operatorTimeBegin", null);
					param.put("createTimeBegin", beginDate);
				}
				if (null != param.get("operatorTimeEnd")) {
					String endDate = formatDate(param.get("operatorTimeEnd").toString()) + " 23:59:59";
					param.put("operatorTimeEnd", null);
					param.put("createTimeEnd", endDate);
				}
				param.put("status", ConstantInterface.FeeStatus.STATUS_0);
				PageInfo<FeesReceiveStorageEntity> pageInfo = feesBillService.queryStorageInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			logger.error("仓储费查询错误", e);
		}
	}
	
	/**
	 * 自定义-运输费用分页查询
	 * 根据账单信息查询运输费用信息
	 * 自定义账单，添加账单
	 */
	@DataProvider
	public void queryDeliverInfoPage(Page<FeesReceiveDeliverEntity> page, Map<String, Object> param){
		try {
			if(param != null){
				if (null != param.get("subjectCode") && "全部".equals(param.get("subjectCode").toString())) {
					param.put("subjectCode", null);
				}
				if (null != param.get("crestime")) {
					String beginDate = formatDate(param.get("crestime").toString()) +" 00:00:00";
					//20170801 修改前按照操作时间生成账单，修改后按照创建时间生成账单(直接修改了sql)
					param.put("crestime", beginDate);
				}
				if (null != param.get("creetime")) {
					String beginDate = formatDate(param.get("creetime").toString()) + " 23:59:59";
					param.put("creetime", beginDate);
				}
				
				PageInfo<FeesReceiveDeliverEntity> pageInfo = feesBillService.queryDeliverInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			logger.error("运输费查询错误", e);
		}
	}
	
	/**
	 * 自定义-配送费用查询
	 * 根据账单信息查配送输费用信息
	 */
	@DataProvider
	public void queryDispatchInfoPage(Page<FeesReceiveDispatchEntity> page, Map<String, Object> param){
		try {
			if(param !=null){
				if (null != param.get("subjectCode") && "全部".equals(param.get("subjectCode").toString())) {
					param.put("subjectCode", null);
				}
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
				
				param.put("status", ConstantInterface.FeeStatus.STATUS_0);
				PageInfo<FeesReceiveDispatchEntity> pageInfo = feesBillService.queryDispatchInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			logger.error("配送费查询错误", e);
		}	
	}
	
	/**
	 * 自定义-异常费用查询
	 * 根据账单信息查询异常费用信息
	 */
	@DataProvider
	public void queryAbnormalInfoPage(Page<FeesAbnormalEntity> page, Map<String, Object> param){
		try {
			if(param != null){
				if (null != param.get("operatorStartTime")) {
					String beginDate = formatDate(param.get("operatorStartTime").toString()) +" 00:00:00";
					param.put("operatorStartTime", beginDate);
				}
				if (null != param.get("operatorEndTime")) {
					String beginDate = formatDate(param.get("operatorEndTime").toString()) + " 23:59:59";
					param.put("operatorEndTime", beginDate);
				}
				
				param.put("isCalculated", ConstantInterface.FeeStatus.STATUS_0);
				PageInfo<FeesAbnormalEntity> pageInfo = feesBillService.queryAbnormalInfoPage(param, page.getPageNo(), page.getPageSize());
				if (pageInfo != null) {
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int) pageInfo.getTotal());
				}
			}		
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			logger.error("配送费查询错误", e);
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
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/bill/billdispatchdistinct_template.xlsx");
		return new DownloadFile("九曳宅配对账单导入模板.xlsx", is);
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
    
	
	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
	
	/**
	 * 默认生成账单时判断是否有生成计算异常的数据
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String isCalculatedTrue(FeesBillEntity entity){
		
		if (null != entity.getBillstarttime()) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillstarttime());
			dateString=dateString+" 00:00:00";
			entity.setBillstarttime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getBillendtime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillendtime());
			dateString=dateString+" 23:59:59";
			entity.setBillendtime(Timestamp.valueOf(dateString));
		}
		
		String result="";
		//查找该宅配商的合同，判断是否签约提货费
		Map<String,Object> aCondition=new HashMap<>();
		aCondition.put("customerId", entity.getCustomerid());
		aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		List<PriceContractInfoEntity> contractList=priceContractService.queryContract(aCondition);	
		if(contractList.size()<=0){
			return "该宅配商未签合同，无法生成账单";
		}	
		//合同
		PriceContractInfoEntity contract=contractList.get(0);
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("customerid", contract.getCustomerId());
		condition.put("createTime", entity.getBillstarttime());
		condition.put("endTime", entity.getBillendtime());
		
		//根据业务类型判断费用科目
		Map<String,Object> contractCondition=new HashMap<String,Object>();
		contractCondition.put("contractCode", contract.getContractCode());
		List<ContractDetailEntity> detailList=priceContractService.findAllContractItem(contractCondition);
		for(int i=0;i<detailList.size();i++){
			ContractDetailEntity en=detailList.get(i);
			String subjectCode=en.getSubjectId();
			
			
			//订单操作费
			if("wh_b2c_work".equals(subjectCode)){
				//查询出库明细判断是否有计算异常的订单操作费
				BizOutstockMasterEntity bizOutstockMasterEntity=bizOutstockMasterService.queryExceptionOne(condition);
				if(bizOutstockMasterEntity!=null){
					result+="订单操作费中存在异常费用,";
				}
				
			}else if("wh_material_use".equals(subjectCode)){
				//耗材使用费
				BizOutstockPackmaterialEntity bizOutstockPackmaterialEntity=bizOutstockPackmaterialService.queryExceptionOne(condition);
				if(bizOutstockPackmaterialEntity!=null){
					result+="耗材使用费中存在异常费用,";
				}
			}else if("wh_product_storage".equals(subjectCode)){
				//商品存储费
				BizProductStorageEntity bizProductStorageEntity=bizProductStorageService.queryExceptionOne(condition);
				if(bizProductStorageEntity!=null){
					result+="商品存储费中存在异常费用,";
				}
			}else if("wh_material_storage".equals(subjectCode)){
				//耗材存储费
				BizPackStorageEntity bizPackStorageEntity=bizPackStorageService.queryExceptionOne(condition);
				if(bizPackStorageEntity!=null){
					result+="耗材存储费中存在异常费用,";
				}
			}else if("JIUYE_DISPATCH".equals(subjectCode)){
				condition.put("carrierId", "1500000016");
				//九曳配送费
				BizDispatchBillEntity bizDispatchBillEntity=bizDispatchBillService.queryExceptionOne(condition);
				if(bizDispatchBillEntity!=null){
					result+="九曳配送费中存在异常费用,";
				}
			}else if("SHUNFENG_DISPATCH".equals(subjectCode)){
				condition.put("carrierId", "1500000015");
				//顺丰配送费
				BizDispatchBillEntity bizDispatchBillEntity=bizDispatchBillService.queryExceptionOne(condition);
				if(bizDispatchBillEntity!=null){
					result+="顺丰配送费中存在异常费用,";
				}
			}
			else if("TRANSPORT_FEE".equals(subjectCode)){
				//运输费
				BizGanxianWayBillEntity bizGanxianWayBillEntity=bizGanxianWayBillService.queryExceptionOne(condition);
				if(bizGanxianWayBillEntity!=null){
					result+="运输费中存在异常费用,";
				}
			}
		}
		
		/*if(bizDispatchBillPayEntity!=null){
			return "宅配商"+contract.getCustomerName()+"中存在计算异常的数据,无法生成账单";
		}*/
		
		return result;
	}
	
	/**
	 * 自定义生成账单时的判断是否有计算异常的
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String isCustomCalculatedTrue(FeesBillReceiveEntity entity){
		
		if (null != entity.getBillstarttime()) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillstarttime());
			dateString=dateString+" 00:00:00";
			entity.setBillstarttime(Timestamp.valueOf(dateString));
		}
		if(null != entity.getBillendtime()){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			String dateString=formatter.format(entity.getBillendtime());
			dateString=dateString+" 23:59:59";
			entity.setBillendtime(Timestamp.valueOf(dateString));
		}
		
		String result="";
		//查找该宅配商的合同，判断是否签约提货费
		Map<String,Object> aCondition=new HashMap<>();
		aCondition.put("customerId", entity.getCustomerid());
		aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		List<PriceContractInfoEntity> contractList=priceContractService.queryContract(aCondition);	
		if(contractList.size()<=0){
			return "该宅配商未签合同，无法生成账单";
		}	
		//合同
		PriceContractInfoEntity contract=contractList.get(0);
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("customerid", contract.getCustomerId());
		condition.put("createTime", entity.getBillstarttime());
		condition.put("endTime", entity.getBillendtime());
		
		//根据业务类型判断费用科目
		Map<String,Object> contractCondition=new HashMap<String,Object>();
		contractCondition.put("contractCode", contract.getContractCode());
		List<ContractDetailEntity> detailList=priceContractService.findAllContractItem(contractCondition);
		for(int i=0;i<detailList.size();i++){
			ContractDetailEntity en=detailList.get(i);
			String subjectCode=en.getSubjectId();
			
			
			//订单操作费
			if("wh_b2c_work".equals(subjectCode)){
				//查询出库明细判断是否有计算异常的订单操作费
				BizOutstockMasterEntity bizOutstockMasterEntity=bizOutstockMasterService.queryExceptionOne(condition);
				if(bizOutstockMasterEntity!=null){
					result+="订单操作费中存在异常费用,";
				}
				
			}else if("wh_material_use".equals(subjectCode)){
				//耗材使用费
				BizOutstockPackmaterialEntity bizOutstockPackmaterialEntity=bizOutstockPackmaterialService.queryExceptionOne(condition);
				if(bizOutstockPackmaterialEntity!=null){
					result+="耗材使用费中存在异常费用,";
				}
			}else if("wh_product_storage".equals(subjectCode)){
				//商品存储费
				BizProductStorageEntity bizProductStorageEntity=bizProductStorageService.queryExceptionOne(condition);
				if(bizProductStorageEntity!=null){
					result+="商品存储费中存在异常费用,";
				}
			}else if("wh_material_storage".equals(subjectCode)){
				//耗材存储费
				BizPackStorageEntity bizPackStorageEntity=bizPackStorageService.queryExceptionOne(condition);
				if(bizPackStorageEntity!=null){
					result+="耗材存储费中存在异常费用,";
				}
			}else if("JIUYE_DISPATCH".equals(subjectCode)){
				condition.put("carrierId", "1500000016");
				//九曳配送费
				BizDispatchBillEntity bizDispatchBillEntity=bizDispatchBillService.queryExceptionOne(condition);
				if(bizDispatchBillEntity!=null){
					result+="九曳配送费中存在异常费用,";
				}
			}else if("SHUNFENG_DISPATCH".equals(subjectCode)){
				condition.put("carrierId", "1500000015");
				//顺丰配送费
				BizDispatchBillEntity bizDispatchBillEntity=bizDispatchBillService.queryExceptionOne(condition);
				if(bizDispatchBillEntity!=null){
					result+="顺丰配送费中存在异常费用,";
				}
			}
			else if("TRANSPORT_FEE".equals(subjectCode)){
				//运输费
				BizGanxianWayBillEntity bizGanxianWayBillEntity=bizGanxianWayBillService.queryExceptionOne(condition);
				if(bizGanxianWayBillEntity!=null){
					result+="运输费中存在异常费用,";
				}
			}
		}
		
		/*if(bizDispatchBillPayEntity!=null){
			return "宅配商"+contract.getCustomerName()+"中存在计算异常的数据,无法生成账单";
		}*/
		
		return result;
	}
}
