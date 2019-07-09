package com.jiuyescm.bms.quotation.out.dispatch.web;

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
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
//import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.chargerule.payrule.service.IPayRuleService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.CommonComparePR;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispacthTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchService;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchTemplateService;
import com.jiuyescm.bms.quotation.storage.web.StroageExtraQuoteController;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.DeliverTemplateDataType;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.thoughtworks.xstream.core.util.Base64Encoder;

@Controller("outDispatchPR")
public class OutDispatchPR extends CommonComparePR<PriceOutMainDispatchEntity>{

	@Resource
	private IPriceOutDispatchService priceOutDispatchService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IPriceOutDispatchTemplateService priceOutDispatchTemplateService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private IAddressService addressService;
	
	@Resource
	private IPayRuleService payRuleService;
	
	//@Resource private IFeesCalcuService feesCalcuService;
	
	@Resource
	private Lock lock;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubRecordLogService pubRecordLogService;
	
	private static final Logger logger = Logger.getLogger(StroageExtraQuoteController.class);

	
	/**
	 * 分页查询主模板
	 * 
	 * @param pagechi
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceOutDispacthTemplateEntity> page, Map<String, Object> param) {
		/*param.put("typeCode", "DISPATCH_COMPANY");*/
		if(param==null){
			param=new HashMap<String, Object>();
			param.put("typeCode", "DISPATCH_COMPANY");
		}
		PageInfo<PriceOutDispacthTemplateEntity> pageInfo = priceOutDispatchTemplateService.query(param, page.getPageNo(), page.getPageSize());
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
	 * 查询所有的九曳配送模板信息
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<PriceOutMainDispatchEntity> page,Map<String,Object> parameter){
		PageInfo<PriceOutMainDispatchEntity> tmpPageInfo = priceOutDispatchService.queryAll(parameter,page.getPageNo(), page.getPageSize());	
		Map<String,String> warehouseMap= getPubWareHouse();	
		if(tmpPageInfo!=null && tmpPageInfo.getList()!=null){
			List<PriceOutMainDispatchEntity> priceList=tmpPageInfo.getList();
			for(PriceOutMainDispatchEntity entity:priceList){
				String warseHouseId=entity.getStartWarehouseId();
				String warseHouseName=warehouseMap.get(warseHouseId);
				entity.setStartWarehouseName(warseHouseName);
				
			}
		}
		
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	
	}
	
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<PriceOutMainDispatchEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PriceOutMainDispatchEntity temp:datas){
				//对操作类型进行判断
				//此为新增九曳配送报价模板
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					priceOutDispatchService.createPriceDistribution(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("新增配送报价模板对应关系,模板编码【"+temp.getTemplateId()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_out_dispatch_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.INSERT.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateId());
						model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_BASE_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:", e);
					}
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					//修改电商仓库
					priceOutDispatchService.updatePriceDistribution(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("更新配送报价模板对应关系,模板编码【"+temp.getTemplateId()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_out_dispatch_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.UPDATE.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateId());
						model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_BASE_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:", e);
					}
				
				}
				
			}
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("异常：", e);
			return "数据库操作失败";
		}
		
	
	}
	
	/**
	 * 删除配送报价模板
	 */
	@DataResolver
	public String removePriceDistribution(PriceOutMainDispatchEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setDelFlag("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			priceOutDispatchService.removePriceDistribution(p);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(p));
				model.setOldData("");
				model.setOperateDesc("作废配送报价模板对应关系,模板编码【"+p.getTemplateId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_dispatch_detail");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getTemplateId());
				model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("异常：", e);
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
			InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/dispatch/dispatch_template.xlsx");
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
	public Map<String, Object> importDispatchTemplate(final UploadFile file, final Map<String, Object> parameter) throws Exception {		
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		//获取此时的商家id
		final String customerId=(String)parameter.get("customerId");//获取当前的id	
		String lockString=getMd5("BMS_QUO_IMPORT_DISPATCH_PAY"+customerId);
		lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> handleObtainLock() {
				// TODO Auto-generated method stub
				try {
				   Map<String, Object> re=importTemplate(file,parameter,infoList,map);
				   return re;
				} catch (Exception e) {
				    logger.error("异常：", e);
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					
				}
				return null;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("商家【"+customerId+"】的报价导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@FileResolver
	public Map<String, Object> importTemplate(UploadFile file, Map<String, Object> parameter,List<ErrorMessageVo> infoList,Map<String, Object> map) throws Exception {		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		//获取当前的id
		String id=(String)parameter.get("id");
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceOutMainDispatchEntity> templateList = new ArrayList<PriceOutMainDispatchEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs=null;
			bs=new DeliverTemplateDataType();
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
			
			//仓库信息
			List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();		
			Map<String,Object> param = new HashMap<String,Object>();			
			param.put("wareHouselist", wareHouselist);	
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,param);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			starTime=System.currentTimeMillis();	//开始时间
			
			
			// 获得初步校验后和转换后的数据
			templateList = (List<PriceOutMainDispatchEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			
			//重复校验(包括Excel校验和数据库校验)
			Map<String, Object> parame=new HashMap<String, Object>();
			parame.put("templateId", id);
			List<PriceOutMainDispatchEntity> orgList=getOrgList(parame);
			List<PriceOutMainDispatchEntity> teList=templateList;
			Map<String,Object> mapCheck=super.compareWithImportLineData(orgList, teList, infoList,getKeyDataProperty(), map);
			if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}	
			
			//设置属性
			for(int i=0;i<templateList.size();i++){
				PriceOutMainDispatchEntity p=templateList.get(i);
				String billNo=id;
				p.setTemplateId(billNo);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
			}
			//插入正式表
			int insertNum = priceOutDispatchService.insertBatchTmp(templateList);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入正式表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入配送报价模板对应关系,共计【"+insertNum+"】条");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_dispatch_detail");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:",e);
				}
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("导入应付报价异常",e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			
		}
		

		return map;
	}
	
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<PriceOutMainDispatchEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<PriceOutMainDispatchEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceOutMainDispatchEntity p = (PriceOutMainDispatchEntity) BeanToMapUtil.convertMapNull(PriceOutMainDispatchEntity.class, data);
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
		    logger.error("异常：", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceOutMainDispatchEntity> prodList, Map<String, Object> map, String currentNo,Map<String,Object> param) {
		
		//===================================目的校验======================================================================
		//判断省市区存不存在
		List<RegionVo> regionList=new ArrayList<RegionVo>();
		int line=1;
		for (int i = 0; i < prodList.size(); i++) {
			//当前行号
			line=line+1;	
			PriceOutMainDispatchEntity p=prodList.get(i);
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
				for(PriceOutMainDispatchEntity price:prodList){
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
		
		int lineNo = 1;
		@SuppressWarnings("unchecked")
		List<WarehouseVo>    wareHouselist = (List<WarehouseVo>)param.get("wareHouselist");
		for (int i = 0; i < prodList.size(); i++) {
			PriceOutMainDispatchEntity p=prodList.get(i);
			lineNo=lineNo+1;
				
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填			
			for(WarehouseVo entity:wareHouselist){
				if(entity.getWarehousename().equals(p.getStartWarehouseName())){
					p.setStartWarehouseId(entity.getWarehouseid());
				}
			}
			
			if(StringUtils.isBlank(p.getStartWarehouseId())){
				setMessage(infoList, lineNo,"仓库id没有在仓库表中维护!");
			}		
			//判断时效信息是否在时效表中维护
			String time=p.getTimeliness();
			if(time!=null && !"".equals(time)){
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
			if(areaTypeCode!=null && !"".equals(areaTypeCode)){
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
			
			//判断温度是否在字典表中维护
			String 	temperature=p.getTemperatureTypeCode();
			if(temperature!=null && !"".equals(temperature)){
				Map<String, Object> temTypeparam=new HashMap<String, Object>();
				temTypeparam.put("typeCode", "TEMPERATURE_TYPE");
				temTypeparam.put("codeName", temperature);
				List<SystemCodeEntity> tempTypeList=systemCodeService.queryCodeList(temTypeparam);
				if(tempTypeList==null || tempTypeList.size()==0){
					setMessage(infoList, lineNo,"温度类型没有在表中维护!");
				}else{
					p.setTemperatureTypeCode(tempTypeList.get(0).getCode());
				}

			}
			
			
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
	public String copyTemplate(PriceOutDispacthTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			//先复制主模板
			String templateNo =sequenceService.getBillNoOne(PriceOutDispacthTemplateEntity.class.getName(), "OMB", "00000");
			entity.setTemplateCode(templateNo);
			entity.setTemplateName(entity.getTemplateName());
			entity.setDeliver(entity.getDeliver());

			entity.setRemark(entity.getRemark());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceOutDispatchTemplateService.save(entity);
			
			//通过模板id查找对应的id
			Integer id=priceOutDispatchService.getId(templateNo);
			
			//复制子模板
			
			List<PriceOutMainDispatchEntity> list=priceOutDispatchService.getDispatchById(entity.getId().toString());
			
			
			for(int i=0;i<list.size();i++){
				PriceOutMainDispatchEntity p=list.get(i);
				p.setTemplateId(id.toString());
			}
			priceOutDispatchService.insertBatchTmp(list);

			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常：", ex);
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
	public Object calculate(BizDispatchBillPayEntity data){
		
		ReturnData returnResult = new ReturnData();	
		//应付的如果是顺丰的进0.5，其他的进1
		Double totalWeight =data.getWeight();		
		//获取此时的宅配商
		//南方传媒(1400000036)  河北报业(1400000658)  品骏控股（重庆(1400000047)、福建(1400000040)）   成都鑫盛通达（圆通）(1500000019)    昆明捷美佳（中通）(1500000018) 应付不进位的
		String deliverid=data.getDeliverid();
		if("1400000036".equals(deliverid) || "1400000047".equals(deliverid) || "1400000658".equals(deliverid) || "1400000040".equals(deliverid) || "1500000019".equals(deliverid) || "1500000018".equals(deliverid)){
			data.setTotalWeight(totalWeight);
		}else{
			if("SHUNFENG_DISPATCH".equals(data.getCarrierId())){
				double dd = getResult(totalWeight);
				data.setTotalWeight(dd);
			}else{	
				double mm=getTeshu(totalWeight);
				data.setTotalWeight(mm);
			}	
		}	
		//通过该商家id和费用科目查询计费规则
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("deliveryid", data.getDeliverid());
		ruleParam.put("subjectId",data.getCarrierId());
		BillRulePayEntity ruleEntity=payRuleService.queryByDeliverId(ruleParam);
		if(ruleEntity==null){
			returnResult.setCode("SUCCESS");
			returnResult.setData("未查询到该商家的规则");
			return returnResult;
		}
		
		CalcuReqVo reqVo= new CalcuReqVo();
		List<PriceOutMainDispatchEntity> list=new ArrayList<>(data.getPriceList());
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
	 * 计算此时的重量
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
	
	/**
	 * 特殊重量计算
	 * @return
	 */
	public double getTeshu(double weightTeshu){
		double weight=weightTeshu;
		
		double isweight;
		
		String a=weight+"";
	    
		if(a.indexOf(".00")!=-1){
		
			int b=a.indexOf(".");
		    
			String index=a.substring(b+1,b+3);
		    
			String e=a.substring(0,b);
		    String c=a.substring(b+1);
		    double d=Double.valueOf(c);
		    double m=Double.valueOf(e);
			
			if("00".equals(index)){
				
			    if(d<5){
			    	m=m+0;
			    }else{
			    	m=m+1;
			    }
			    
			}else{
				m=Math.ceil(weight);
			}
			isweight=m;
		}else{
			isweight=Math.ceil(weight);	
		}
		
		return isweight;
	}

	public List<PriceOutMainDispatchEntity> getOrgList(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		List<PriceOutMainDispatchEntity> list=priceOutDispatchService.queryAllById(parameter);
		return list;
	}


	public List<String> getKeyDataProperty() {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();
		list.add("startWarehouseId");//仓库名称
		list.add("provinceId");//省份
		list.add("cityId");//市
		list.add("areaId");//区
		list.add("temperatureTypeCode");//温度类型
		list.add("areaTypeCode");//地域类型
		list.add("timeliness");//时效
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
			logger.error("异常：", e);
			return null;
		}
	}
}
