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
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;
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

@Controller("dispatchPR")
public class DispatchPR extends CommonComparePR<PriceMainDispatchEntity>{

	@Resource
	private IPriceDispatchService priceDispatchService;
	
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
	
	@Resource private IReceiveRuleService receiveRuleService;
	
	private static final Logger logger = Logger.getLogger(DispatchPR.class);

	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
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
	 * 查询所有的九曳配送模板信息
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<PriceMainDispatchEntity> page,Map<String,Object> parameter){
		PageInfo<PriceMainDispatchEntity> tmpPageInfo = priceDispatchService.queryAll(parameter,page.getPageNo(), page.getPageSize());
		
		Map<String,String> warehouseMap= getPubWareHouse();
		
		if(tmpPageInfo!=null && tmpPageInfo.getList()!=null){
			List<PriceMainDispatchEntity> priceList=tmpPageInfo.getList();
			for(int i=0;i<priceList.size();i++){
				PriceMainDispatchEntity entity=priceList.get(i);
				
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
	public String saveAll(Collection<PriceMainDispatchEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PriceMainDispatchEntity temp:datas){
				//对操作类型进行判断
				//此为新增九曳配送报价模板
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					priceDispatchService.createPriceDistribution(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("新增宅配报价对应关系,模板编码【"+temp.getTemplateId()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_dispatch_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.INSERT.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateId());
						model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:"+e.getMessage());
					}
	
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					//修改报价
					priceDispatchService.updatePriceDistribution(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("更新宅配报价对应关系,模板编码【"+temp.getTemplateId()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_dispatch_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.UPDATE.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateId());
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
			e.printStackTrace();
			return "数据库操作失败";
		}
		
	
	}
	
	/**
	 * 删除配送报价模板
	 */
	@DataResolver
	public String removePriceDistribution(PriceMainDispatchEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setDelFlag("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			priceDispatchService.removePriceDistribution(p);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(p));
				model.setOldData("");
				model.setOperateDesc("删除宅配报价对应关系,模板编码【"+p.getTemplateId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_dispatch_detail");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getTemplateId());
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
				// TODO Auto-generated method stub
				try {
				   Map<String, Object> re=importTemplate(file,parameter,infoList,map);
				   return re;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("商家【"+customerName+"】的报价导入功能已被其他用户占用，请稍后重试；");
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
	
	/**
	 * 报价导入
	 * @param <U>
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private  Map<String, Object> importTemplate(UploadFile file, Map<String, Object> parameter,List<ErrorMessageVo> infoList,Map<String, Object> map){
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		
		//获取当前的id
		String id=(String)parameter.get("id");
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceMainDispatchEntity> templateList = new ArrayList<PriceMainDispatchEntity>();
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
			/*condition.put("tempid", currentNo);*/
			
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
			templateList = (List<PriceMainDispatchEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			//重复校验(包括Excel校验和数据库校验)
			Map<String, Object> parame=new HashMap<String, Object>();
			parame.put("templateId", id);
			List<PriceMainDispatchEntity> orgList=getOrgList(parame);
			List<PriceMainDispatchEntity> teList=templateList;
			Map<String,Object> mapCheck=super.compareWithImportLineData(orgList, teList, infoList,getKeyDataProperty(), map);
			if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}	
			//设置属性
			for(int i=0;i<templateList.size();i++){
				PriceMainDispatchEntity p=templateList.get(i);
				String billNo=id;
				p.setTemplateId(billNo);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
			}
			//插入正式表
			int insertNum = 0;
			try {
				insertNum = priceDispatchService.insertBatchTmp(templateList);
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
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
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
			// TODO: handle exception
		}
	
		return map;
	}
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<PriceMainDispatchEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<PriceMainDispatchEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceMainDispatchEntity p = (PriceMainDispatchEntity) BeanToMapUtil.convertMapNull(PriceMainDispatchEntity.class, data);
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceMainDispatchEntity> prodList, Map<String, Object> map, String currentNo,Map<String,Object> param) {
		
		//===================================目的校验======================================================================
		//判断省市区存不存在
		List<RegionVo> regionList=new ArrayList<RegionVo>();
		int line=1;
		for (int i = 0; i < prodList.size(); i++) {
			//当前行号
			line=line+1;	
			PriceMainDispatchEntity p=prodList.get(i);
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
				for(PriceMainDispatchEntity price:prodList){
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
		//==============================================================================================================
		int lineNo = 1;
		List<WarehouseVo>    wareHouselist = (List<WarehouseVo>)param.get("wareHouselist");
		//获取到此时的温度类型
		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		for (int i = 0; i < prodList.size(); i++) {
			PriceMainDispatchEntity p=prodList.get(i);
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
			
			//判断此时的温度
			String temperature=p.getTemperatureTypeCode();
			if(temperature!=null && StringUtils.isNotBlank(temperature)){
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
			
			/*//判断重量界限不能为非数字
			if(p.getWeightLimit()==null){
				setMessage(infoList, lineNo,"重量界限不能为空且不能为非数字!");
			}
				
			//判断单价不能为非数字
			if(p.getUnitPrice()==null){
				setMessage(infoList, lineNo,"单价不能为空且不能为非数字!");
			}*/
			//判断首重重量不能为非数字
		/*	if(p.getFirstWeight()==null){
				setMessage(infoList, lineNo,"首重重量不能为空且不能为非数字!");
			}
			//判断首重价格不能为非数字
			if(p.getFirstWeightPrice()==null){
				setMessage(infoList, lineNo,"首重价格不能为空且不能为非数字!");
			}
			
			//判断续重重量不能为非数字
			if(p.getContinuedWeight()==null){
				setMessage(infoList, lineNo,"续重重量不能为空且不能为非数字!");
			}
			//判断续重价格不能为非数字
			if(p.getContinuedPrice()==null){
				setMessage(infoList, lineNo,"续重价格不能为空且不能为非数字!");
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
			
			//通过模板id查找对应的id
			Integer id=priceDispatchService.getId(templateNo);
			
			//复制子模板
			
			List<PriceMainDispatchEntity> list=priceDispatchService.getDispatchById(entity.getId().toString());
			
			
			for(int i=0;i<list.size();i++){
				PriceMainDispatchEntity p=list.get(i);
				p.setTemplateId(id.toString());
			}
			priceDispatchService.insertBatchTmp(list);

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
		CalcuResultVo vo = new CalcuResultVo();
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
	
	public List<PriceMainDispatchEntity> getOrgList(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		List<PriceMainDispatchEntity> list=priceDispatchService.queryAllById(parameter);
		return list;
	}

	public List<String> getKeyDataProperty() {
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
			e.printStackTrace();
			return null;
		}
	}
}
