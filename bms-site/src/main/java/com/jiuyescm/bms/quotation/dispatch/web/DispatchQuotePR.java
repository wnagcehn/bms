package com.jiuyescm.bms.quotation.dispatch.web;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.address.service.IPubAddressService;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.base.servicetype.service.IPubCarrierServicetypeService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.CommonComparePR;
import com.jiuyescm.bms.common.web.SelectFromTablePR;
import com.jiuyescm.bms.fees.calculate.service.IFeesCalculateService;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.service.IBmsQuoteDispatchDetailService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.DispatchQuoteTemplateDataType;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * 宅配报价
 * @author yangss
 */
@Controller("dispatchQuotePR")
public class DispatchQuotePR extends CommonComparePR<BmsQuoteDispatchDetailVo>{
	
	private static final Logger logger = Logger.getLogger(DispatchQuotePR.class.getName());

	private final static char[] additional ={'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
	
	@Resource
	private IBmsQuoteDispatchDetailService bmsQuoteDispatchDetailService;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private IPriceDispatchTemplateService priceDispatchTemplateService;
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	@Resource
	private IPubAddressService pubAddressService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private SelectFromTablePR selectFromTablePR;
	@Resource
	private IAddressService addressService;
	@Resource
	private Lock lock;
	@Resource
	private IFeesCalculateService feesCalculateService;
	//@Resource private IFeesCalcuService feesCalcuService;
	@Resource 
	private IReceiveRuleService receiveRuleService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	@Resource 
	private IDeliverService deliverService;
	@Resource
	private IPubCarrierServicetypeService pubCarrierServicetypeService;
	
	/**
	 * 分页查询主模板
	 * 
	 * @param pagechi
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceDispatchTemplateEntity> page, Map<String, Object> param) {
		/*param.put("typeCode", "DISPATCH_COMPANY");*/
		if(param==null){
			param=new HashMap<String, Object>();
			param.put("typeCode", "DISPATCH_COMPANY");
		}
		PageInfo<PriceDispatchTemplateEntity> pageInfo = priceDispatchTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	@DataProvider
	public Map<String, String> getPubWareHouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	/**
	 * 查询报价明细
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<BmsQuoteDispatchDetailVo> page,Map<String,Object> parameter){
		try {
			PageInfo<BmsQuoteDispatchDetailVo> tmpPageInfo = bmsQuoteDispatchDetailService.query(parameter,page.getPageNo(), page.getPageSize());
			if (tmpPageInfo != null) {
				page.setEntities(tmpPageInfo.getList());
				page.setEntityCount((int) tmpPageInfo.getTotal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询所有的九曳配送模板信息异常：", e);
		}
	}
	
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<BmsQuoteDispatchDetailVo> datas){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BmsQuoteDispatchDetailVo temp:datas){
				// 报价规则编号处理
				/*String priceRuleNo = dealPriceRuleNo(temp);
				temp.setMark(priceRuleNo);*/
				
				//此为新增九曳配送报价模板
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					bmsQuoteDispatchDetailService.save(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("新增宅配报价对应关系,模板编码【"+temp.getTemplateCode()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_dispatch_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.INSERT.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateCode());
						model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:"+e.getMessage());
					}
	
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					//修改报价
					bmsQuoteDispatchDetailService.update(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("更新宅配报价对应关系,模板编码【"+temp.getTemplateCode()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_dispatch_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.UPDATE.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateCode());
						model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:"+e.getMessage());
					}
				}
			}
			
			return "数据库操作成功";
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("数据库操作异常：", e);
			return "数据库操作失败";
		}
	}
	
	/**
	 * 删除配送报价模板
	 */
	@DataResolver
	public String removePriceDistribution(BmsQuoteDispatchDetailVo p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setDelFlag("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			bmsQuoteDispatchDetailService.removePriceDistribution(p);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(p));
				model.setOldData("");
				model.setOperateDesc("删除宅配报价对应关系,模板编码【"+p.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_dispatch_detail");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "数据库操作成功";
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			e.printStackTrace();
			return "数据库操作失败";
		}
	}
	
	
	
	/**
	 * 配送模板信息下载
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile getDispatchTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/dispatch/dispatch_template_new.xlsx");
		return new DownloadFile("配送模板信息导入模板.xlsx", is);
	}

	/**
	 * 导入配送模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importDispatchTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		//获取此时的商家id
		final String customerId=(String)parameter.get("customerId");//获取当前的id	
		final String customerName=(String)parameter.get("customerName");//获取当前的id	
		String lockString=getMd5("BMS_QUO_IMPORT_DISPATCH_RECEIVE"+customerId);
		lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> handleObtainLock() {
				try {
				   Map<String, Object> re=importTemplate(file,parameter,infoList,map);
				   return re;
				} catch (Exception e) {
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("商家【"+customerName+"】的报价导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return map;
	}
	
	/**
	 * 报价导入
	 * @param <U>
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> importTemplate(UploadFile file, Map<String, Object> parameter,List<ErrorMessageVo> infoList,Map<String, Object> map){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		
		ErrorMessageVo errorVo = null;
		//获取当前的id
		String templateCode = (String)parameter.get("templateCode");
		String carrierid = (String)parameter.get("carrierid");
		String priceType = (String)parameter.get("priceType");
		if(StringUtils.isBlank(priceType)){
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("报价形式不能为空!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		// 导入成功返回模板信息
		List<BmsQuoteDispatchDetailVo> templateList = new ArrayList<BmsQuoteDispatchDetailVo>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new DispatchQuoteTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			logger.info("验证列名耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			starTime=System.currentTimeMillis();	//开始时间
			
			// 解析Excel
			templateList = readExcelProduct(file,bs);
			if (null == templateList || templateList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			logger.info("excel解析耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
			starTime=System.currentTimeMillis();	//开始时间
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			/*condition.put("tempid", currentNo);*/
			
			//仓库信息
			List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();		
			Map<String,Object> param = new HashMap<String,Object>();			
			param.put("wareHouselist", wareHouselist);
			param.put("priceType", priceType);
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,param);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			starTime=System.currentTimeMillis();	//开始时间
			
			// 获得初步校验后和转换后的数据
			templateList = (List<BmsQuoteDispatchDetailVo>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			List<PubCarrierServicetypeEntity> servicetypeList = pubCarrierServicetypeService.queryByCarrierid(carrierid);
			//设置属性
			for(BmsQuoteDispatchDetailVo p : templateList){
				boolean exe = false;
				if (StringUtils.isNotBlank(p.getServicename())) {
					for (PubCarrierServicetypeEntity pcsEntity : servicetypeList) {
						if (p.getServicename().equals(pcsEntity.getServicename())) {
							p.setServiceTypeCode(pcsEntity.getServicecode());
							exe = true;
							break;
						}
					}
					if (!exe) {
						errorVo = new ErrorMessageVo();
						errorVo.setMsg("该物流商下无该物流产品类型！");
						infoList.add(errorVo);
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;	
					}
				}
				p.setTemplateCode(templateCode);
//				p.setMark(dealPriceRuleNo(p));// 报价规则编号
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
			}
			
			//重复校验(包括Excel校验和数据库校验)
			Map<String, Object> parame=new HashMap<String, Object>();
			parame.put("templateCode", templateCode);
			List<BmsQuoteDispatchDetailVo> orgList=getOrgList(parame);
			List<BmsQuoteDispatchDetailVo> teList=templateList;
			if (null != orgList) {
				Map<String,Object> mapCheck=super.compareWithImportLineData(orgList, teList, infoList,getKeyDataProperty(), map);
				if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {
					return map;
				}
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			
			
			//插入正式表
			int insertNum = 0;
			try {
				insertNum = bmsQuoteDispatchDetailService.insertBatchTmp(templateList);
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
			} catch (Exception e) {
				if((e.getMessage().indexOf("Duplicate entry"))>0){
					errorVo = new ErrorMessageVo();
					errorVo.setMsg("违反唯一性约束,插入数据失败!");
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
				//写入日志
				bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			}
			
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入正式表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入宅配报价对应关系,共计【"+insertNum+"】条");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_dispatch_detail");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			}
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			e.printStackTrace();
		}
	
		return map;
	}
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<BmsQuoteDispatchDetailVo> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			//BaseDataType ddt = new DispatchTemplateDataType();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<BmsQuoteDispatchDetailVo> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				BmsQuoteDispatchDetailVo p = (BmsQuoteDispatchDetailVo) BeanToMapUtil.convertMapNull(BmsQuoteDispatchDetailVo.class, data);
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验Excel里面的内容合法性
	 * 
	 * @param infoList
	 * @param list
	 * @param map
	 * @param objList
	 * @param currentNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<BmsQuoteDispatchDetailVo> prodList, Map<String, Object> map, String currentNo,Map<String,Object> param) {
		String priceType = (String)param.get("priceType");
		//===================================目的校验======================================================================
		//判断省市区存不存在
		List<RegionVo> regionList=new ArrayList<RegionVo>();
		int line=1;
		for (int i = 0; i < prodList.size(); i++) {
			//当前行号
			line=line+1;	
			BmsQuoteDispatchDetailVo p = prodList.get(i);
			RegionVo regionVo=new RegionVo();
			regionVo.setProvince(p.getProvinceName());
			regionVo.setCity(p.getCityName());
			regionVo.setDistrict(p.getAreaName());
			regionVo.setLineNo(line);
			
			regionList.add(regionVo);
		}
		
		RegionEncapVo regionEncapVo=addressService.matchStandardByAlias(regionList);
		//获取此时的正确信息和报错信息
		List<ErrorMsgVo> errorMsgList=regionEncapVo.getErrorMsgList();
		List<RegionVo> correctRegionList=regionEncapVo.getRegionList();
		
		//如匹配到到名称则将code设值
		if(errorMsgList.size()<=0){
			for(RegionVo region:correctRegionList){
				for(BmsQuoteDispatchDetailVo price:prodList){
					if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(price.getProvinceName()) && region.getProvince().equals(price.getProvinceName())){
						price.setProvinceId(region.getProvincecode());
						if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(price.getCityName()) && region.getCity().equals(price.getCityName())){
							price.setCityId(region.getCitycode());
							if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(price.getAreaName()) && region.getDistrict().equals(price.getAreaName())){
								price.setAreaId(region.getDistrictcode());		
							}
						}
					}
				}
			}
		}else{
			for(ErrorMsgVo error:errorMsgList){
				setMessage(infoList, error.getLineNo(),error.getErrorMsg());
			}
		}
		
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		} 
		
		//==============================================================================================================
		int lineNo = 1;
		List<WarehouseVo> wareHouselist = (List<WarehouseVo>)param.get("wareHouselist");
		//获取到此时的温度类型
		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		for (int i = 0; i < prodList.size(); i++) {
			BmsQuoteDispatchDetailVo p=prodList.get(i);
			lineNo=lineNo+1;				
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填			
			for(WarehouseVo entity:wareHouselist){
				if(entity.getWarehousename().equals(p.getStartWarehouseName())){
					p.setStartWarehouseId(entity.getWarehouseid());
					break;
				}
			}
			if(StringUtils.isBlank(p.getStartWarehouseId())){
				setMessage(infoList, lineNo,"仓库id没有在仓库表中维护!");
			}
			
			// 非空校验
			String infoNull = checkInfoNull(p);
			if (StringUtils.isNotBlank(infoNull)) {
				setMessage(infoList, lineNo, infoNull);
			}
			// 单价、首重、续重校验
			String weightUnitPrice = checkWeightUnitPrice(p);
			if (StringUtils.isNotBlank(weightUnitPrice)) {
				setMessage(infoList, lineNo, weightUnitPrice);
			}
			
			// 报价内容校验
			if ("GENERAL".equals(priceType)) {// 常规
				String commonInfo = checkCommonNotNull(p);
				if (StringUtils.isNotBlank(commonInfo)) {
					setMessage(infoList, lineNo, commonInfo);
				}
			}else if ("PRODUCT_CASE".equals(priceType)) {// 特殊
				String specialInfo = checkSpecialNotNull(p);
				if (StringUtils.isNotBlank(specialInfo)) {
					setMessage(infoList, lineNo, specialInfo);
				}
			}else if ("TEMPERATURE_CASE".equals(priceType)) {// 温度
				String temperatureInfo = checkTempertureNotNull(p);
				if (StringUtils.isNotBlank(temperatureInfo)) {
					setMessage(infoList, lineNo, temperatureInfo);
				}
				
				//判断此时的温度
				String temperature = p.getTemperatureTypeCode();
				if(StringUtils.isNotBlank(temperature)){
					for(SystemCodeEntity entity:temperatureList){
						if(entity.getCodeName().equals(temperature)){
							p.setTemperatureTypeCode(entity.getCode());
							break;
						}
					}
					
					if(p.getTemperatureTypeCode().equals(temperature)){
						setMessage(infoList, lineNo,"温度["+p.getTemperatureTypeCode()+"]没有在表中维护!");
					}
				}
			}
			
			/*//判断时效信息是否在时效表中维护
			String time=p.getTimeliness();
			if(StringUtils.isNotBlank(time)){
				Map<String, Object> param1=new HashMap<String, Object>();
				param1.put("typeCode", "DISPATCH_TIME");
				param1.put("codeName", time);
				List<SystemCodeEntity> timelessList=systemCodeService.queryCodeList(param1);
				if(timelessList==null || timelessList.size()==0){
					setMessage(infoList, lineNo,"时效没有在表中维护!");
				}else{
					p.setTimeliness(timelessList.get(0).getCode());
				}
			}
			//判断地域类型是否在字典表中维护
			//如果是顺丰配送
			String areaTypeCode=p.getAreaTypeCode();
			if(StringUtils.isNotBlank(areaTypeCode)){
				Map<String, Object> areaTypeparam=new HashMap<String, Object>();
				areaTypeparam.put("typeCode", "DISPATCH_AREA_TYPE");
				areaTypeparam.put("codeName", areaTypeCode);
				List<SystemCodeEntity> areaTypeList=systemCodeService.queryCodeList(areaTypeparam);
				if(areaTypeList==null || areaTypeList.size()==0){
					setMessage(infoList, lineNo,"地域类型没有在表中维护!");
				}else{
					p.setAreaTypeCode(areaTypeList.get(0).getCode());
				}
			}
			
			// 判断宅配商信息
			Map<String, String> deliveryMap = getDeliverMap();
			if (StringUtils.isNotBlank(p.getDeliverid())) {
				if (deliveryMap.containsKey(p.getDeliverid())) {
					String deliverId = deliveryMap.get(p.getDeliverid());
					p.setDeliverid(deliverId);
				}else {
					setMessage(infoList, lineNo, "宅配商简称没有在表中维护!");
				}
			}*/
				
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, prodList); // 无基本错误信息
			}
			
		}
		return map;
	}
	
	/**
	 * 复制新模板
	 */
	@DataResolver
	public String copyTemplate(PriceDispatchTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			String oldTemplateNo = entity.getTemplateCode();
			//先复制主模板
			String templateNo =sequenceService.getBillNoOne(PriceDispatchTemplateEntity.class.getName(), "IMB", "00000");
			entity.setTemplateCode(templateNo);
			entity.setTemplateName(entity.getTemplateName());
			entity.setDeliver(entity.getDeliver());

			entity.setRemark(entity.getRemark());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceDispatchTemplateService.save(entity);
			
//			//通过模板id查找对应的id
//			Integer id = bmsQuoteDispatchDetailService.getId(templateNo);
			
			//复制子模板
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("templateCode", oldTemplateNo);
			List<BmsQuoteDispatchDetailVo> list = bmsQuoteDispatchDetailService.queryAllById(condition);
			for(int i=0;i<list.size();i++){
				BmsQuoteDispatchDetailVo p=list.get(i);
				p.setTemplateCode(templateNo);
			}
			bmsQuoteDispatchDetailService.insertBatchTmp(list);

			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			ex.printStackTrace();
			return "数据库操作失败";
		}
		
	}
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DataResolver
	public Object calculate(BizDispatchBillEntity data){
		ReturnData returnResult = new ReturnData();
	
		//顺丰进0.5，其余进1
		if(data.getWeight()!=null && data.getCarrierId()!=null){
			if("SHUNFENG_DISPATCH".equals(data.getCarrierId())){
				double result=getResult(data.getWeight());
				data.setTotalWeight(result);	
			}else{
				data.setTotalWeight(Math.ceil(data.getWeight()));
			}
				
		}
	
		//通过该商家id和费用科目查询计费规则
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("customerid", data.getCustomerid());
		ruleParam.put("subjectId",data.getCarrierId());
		BillRuleReceiveEntity ruleEntity=receiveRuleService.queryByCustomerId(ruleParam);
		if(ruleEntity==null){
			returnResult.setCode("SUCCESS");
			returnResult.setData("未查询到该商家的规则");
			return returnResult;
		}
		CalcuReqVo reqVo= new CalcuReqVo();
		List<PriceMainDispatchEntity> list=new ArrayList<>(data.getPriceList());
		reqVo.setQuoEntites(list);
		reqVo.setBizData(data);
		reqVo.setRuleNo(ruleEntity.getQuotationNo());
		reqVo.setRuleStr(ruleEntity.getRule());
		
		//CalcuResultVo vo=feesCalcuService.FeesCalcuService(reqVo);	
		CalcuResultVo vo=new CalcuResultVo();
		if("succ".equals(vo.getSuccess())){
			data.setCollectMoney(vo.getPrice().doubleValue());				
		}	
		returnResult.setCode("SUCCESS");
		returnResult.setData(vo.getPrice()==null?"无匹配价格":vo.getPrice());

		return returnResult;
	}
	
	/**
	 * 顺丰运费计算规则（超重1.4kg时 用续费重量*1.5, ;超重1.6kg时 用续重*2计算）
	 * @param weight
	 * @return
	 */
	public double getResult(double weight){
		//原重
		double a=weight;	
		//现重
		double c=0.0;	
		int b=(int)a;
		double min=a-b;

		if(0<min && min <0.5){
			c=b+0.5;
		}
		
		if(0.5<min && min<1){
			c=b+1;
		}
		if(min==0 || min==0.5){
			c=a;
		}
		return c;
	}
	
	public List<BmsQuoteDispatchDetailVo> getOrgList(Map<String, Object> parameter) throws Exception {
		return bmsQuoteDispatchDetailService.queryAllById(parameter);
	}

	public List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("startWarehouseId");//仓库名称
		list.add("provinceId");//省份
		list.add("cityId");//市
		list.add("areaId");//区
		list.add("weightDown");
		list.add("weightUp");
		list.add("unitPrice");
		list.add("firstWeight");
		list.add("firstWeightPrice");
		list.add("continuedWeight");
		list.add("continuedPrice");
		list.add("timeliness");//时效
		list.add("temperatureTypeCode");//温度类型
		list.add("areaTypeCode");//地域类型
		list.add("productCase");
		list.add("deliverid");
		list.add("serviceTypeCode");
		return list;
	}
	
	private String getMd5(String str){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 处理报价编号
	 * @param vo
	 * @return
	 */
	private String dealPriceRuleNo(BmsQuoteDispatchDetailVo vo){
		StringBuffer strBuf = new StringBuffer();
		// 单价
		if (DoubleUtil.isBlank(vo.getUnitPrice())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 首重
		if (DoubleUtil.isBlank(vo.getFirstWeight())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 首重价格
		if (DoubleUtil.isBlank(vo.getFirstWeightPrice())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 续重
		if (DoubleUtil.isBlank(vo.getContinuedWeight())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 续重价格
		if (DoubleUtil.isBlank(vo.getContinuedPrice())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 时效
		if (StringUtils.isBlank(vo.getTimeliness())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 温度类型
		if (StringUtils.isBlank(vo.getTemperatureTypeCode())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 特殊计费商品
		if (StringUtils.isBlank(vo.getProductCase())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 地域类型
		if (StringUtils.isBlank(vo.getAreaTypeCode())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 宅配商
		if (StringUtils.isBlank(vo.getDeliverid())) {
			strBuf.append("0");
		}else {
			strBuf.append("1");
		}
		
		// 补足16位
		strBuf.append(additional, 9, 6);
		
		return strBuf.toString();
	}
	
	/**
	 * 获取宅配商信息
	 * @return
	 */
	private Map<String, String> getDeliverMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<DeliverVo> list = deliverService.queryAllDeliver();
		if (null != list && list.size() > 0) {
			for (DeliverVo deliverVo : list) {
				map.put(deliverVo.getDelivername(), deliverVo.getDeliverid());
			}
		}
		
		return map;
	}
	
	/**
	 * 非空校验
	 * @param vo
	 * @return
	 */
	private String checkInfoNull(BmsQuoteDispatchDetailVo vo){
		if (null == vo) {
			return "参数错误!";
		}
		
		if (StringUtils.isBlank(vo.getStartWarehouseId())) {
			return "始发仓不能为空!";
		}
		if (StringUtils.isBlank(vo.getProvinceId())) {
			return "目的省份不能为空!";
		}
		if (null == vo.getWeightDown()) {
			return "重量下限不能为空!";
		}
		if (null == vo.getWeightUp()) {
			return "重量上限不能为空!";
		}
		
		return null;
	}
	
	/**
	 * 校验单价、重量界限
	 * 单价和重量界限不能同时存在
	 * @param vo
	 * @return
	 */
	private String checkWeightUnitPrice(BmsQuoteDispatchDetailVo vo){
		Double unitPrice = vo.getUnitPrice();
		Double firstWeight = vo.getFirstWeight();
		Double firstWeightPrice = vo.getFirstWeightPrice();
		Double continuedWeight = vo.getContinuedWeight();
		Double continuedPrice = vo.getContinuedPrice();
		
		if (DoubleUtil.isBlank(unitPrice) && 
				(DoubleUtil.isBlank(firstWeight) || DoubleUtil.isBlank(continuedWeight))) {
			return "单价、首重续重不能都为空!";
		}
		
		if(!DoubleUtil.isBlank(firstWeight) && !DoubleUtil.isBlank(continuedWeight) &&
			(DoubleUtil.isBlank(firstWeightPrice) || DoubleUtil.isBlank(continuedPrice))){
			return "首重价格、续重价格不能为空!";
		}
		
		if(!DoubleUtil.isBlank(unitPrice) &&
			(!DoubleUtil.isBlank(firstWeight) || !DoubleUtil.isBlank(continuedWeight) ||
			!DoubleUtil.isBlank(firstWeightPrice) || !DoubleUtil.isBlank(continuedPrice))){
			return "单价和首重续重不能同时存在!";
		}
		
		return null;
	}
	
	/**
	 * 校验常规报价其他字段
	 * @param vo
	 * @return
	 */
	private String checkCommonNotNull(BmsQuoteDispatchDetailVo vo){
		if (StringUtils.isNotBlank(vo.getTimeliness()) ||
				StringUtils.isNotBlank(vo.getTemperatureTypeCode()) ||
				StringUtils.isNotBlank(vo.getProductCase()) ||
				StringUtils.isNotBlank(vo.getAreaTypeCode()) ||
				StringUtils.isNotBlank(vo.getDeliverid())) {
			return "时效、温度类型、地域类型、特殊计费商品、特殊宅配商(全称)不能存在!";
		}
		
		return null;
	}
	
	/**
	 * 校验特殊报价其他字段
	 * @param vo
	 * @return
	 */
	private String checkSpecialNotNull(BmsQuoteDispatchDetailVo vo){
		/*if (StringUtils.isBlank(vo.getProductCase())) {
			return "特殊计费商品不能为空!";
		}*/
		if (StringUtils.isNotBlank(vo.getTimeliness()) ||
				StringUtils.isNotBlank(vo.getTemperatureTypeCode()) ||
				StringUtils.isNotBlank(vo.getAreaTypeCode()) ||
				StringUtils.isNotBlank(vo.getDeliverid())) {
			return "时效、温度类型、地域类型、特殊宅配商(全称)不能存在!";
		}
		
		return null;
	}
	
	/**
	 * 校验特殊报价其他字段
	 * @param vo
	 * @return
	 */
	private String checkTempertureNotNull(BmsQuoteDispatchDetailVo vo){
		/*if (StringUtils.isBlank(vo.getTemperatureTypeCode())) {
			return "温度类型不能为空!";
		}*/
		if (StringUtils.isNotBlank(vo.getTimeliness()) ||
				StringUtils.isNotBlank(vo.getProductCase()) ||
				StringUtils.isNotBlank(vo.getAreaTypeCode()) ||
				StringUtils.isNotBlank(vo.getDeliverid())) {
			return "时效、地域类型、特殊计费商品、特殊宅配商(全称)不能存在!";
		}
		
		return null;
	}
	
}
