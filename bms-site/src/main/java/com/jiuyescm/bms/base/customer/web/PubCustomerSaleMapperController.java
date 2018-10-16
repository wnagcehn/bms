package com.jiuyescm.bms.base.customer.web;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.customer.entity.PubCustomerSaleMapperEntity;
import com.jiuyescm.bms.base.customer.service.IPubCustomerSaleMapperService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.BmsGroupUserEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.web.CommonComparePR;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.CustomerSaleTemplateDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.DispatchQuoteTemplateDataType;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("pubCustomerSaleMapperController")
public class PubCustomerSaleMapperController extends CommonComparePR<PubCustomerSaleMapperEntity>{

	private static final Logger logger = LoggerFactory.getLogger(PubCustomerSaleMapperController.class.getName());

	@Autowired
	private IPubCustomerSaleMapperService pubCustomerSaleMapperService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	@Resource
	private Lock lock;
	@Autowired 
	private ICustomerService customerService;
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public PubCustomerSaleMapperEntity findById(Long id) throws Exception {
		return pubCustomerSaleMapperService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PubCustomerSaleMapperEntity> page, Map<String, Object> param) {
		PageInfo<PubCustomerSaleMapperEntity> pageInfo = pubCustomerSaleMapperService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(PubCustomerSaleMapperEntity entity) {
		if (null == entity.getId()) {
			pubCustomerSaleMapperService.save(entity);
		} else {
			pubCustomerSaleMapperService.update(entity);
		}
	}

//	/**
//	 * 删除
//	 * @param entity
//	 */
//	@DataResolver
//	public void delete(PubCustomerSaleMapperEntity entity) {
//		pubCustomerSaleMapperService.delete(entity.getId());
//	}
	
	/**
	 * 导入模板下载
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile getCustomerSaleTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/customer/customer_sale_mapper.xlsx");
		return new DownloadFile("原始销售员导入模板.xlsx", is);
	}
	
	/**
	 * 导入模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importCustomerSaleTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		String lockString=getMd5("BMS_QUO_IMPORT_CUSTOMER_SALE");
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
				errorVo.setMsg("导入功能已被其他用户占用，请稍后重试；");
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
	 * 导入
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

		// 导入成功返回模板信息
		List<PubCustomerSaleMapperEntity> templateList = new ArrayList<PubCustomerSaleMapperEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new CustomerSaleTemplateDataType();
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
				
			Map<String,Object> param = new HashMap<String,Object>();			

			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,param);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			
			Map<String, String> customerMap = new HashMap<String, String>();
			Map<String, String> sellerMap = new HashMap<String, String>();
			//商家
			PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
			if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0){
				for(CustomerVo customer : tmpPageInfo.getList()){
					if(customer != null){
						customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
					}
				}
			}
			//销售员
			Map<String, String> parma = new HashMap<String, String>();
			parma.put("bizType", "sale_area");
			List<BmsGroupUserEntity> userList = bmsGroupUserService.queryUserByBizType(parma);
			if (userList != null && userList.size()>0){
				for (BmsGroupUserEntity bmsGroupUserEntity : userList) {
					if (sellerMap.containsKey(bmsGroupUserEntity.getUserName().trim())) {
						//有重复的，只添加一个
						continue;
					}else {
						sellerMap.put(bmsGroupUserEntity.getUserName().trim(), bmsGroupUserEntity.getUserId());			
					}
				}
			}	
			
			starTime=System.currentTimeMillis();	//开始时间
			
			// 获得初步校验后和转换后的数据
			templateList = (List<PubCustomerSaleMapperEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			//重复校验(包括Excel校验和数据库校验)
			Map<String, Object> parame=new HashMap<String, Object>();
			List<PubCustomerSaleMapperEntity> orgList=getOrgList(parame);
			List<PubCustomerSaleMapperEntity> teList=templateList;
			if (null != orgList) {
				Map<String,Object> mapCheck=super.compareWithImportLineData(orgList, teList, infoList,getKeyDataProperty(), map);
				if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {
					return map;
				}
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			for (PubCustomerSaleMapperEntity pubCustomerSaleMapperEntity : templateList) {
				pubCustomerSaleMapperEntity.setOriginSellerId(sellerMap.get(pubCustomerSaleMapperEntity.getOriginSellerName()).trim());
				pubCustomerSaleMapperEntity.setCustomerId(customerMap.get(pubCustomerSaleMapperEntity.getCustomerName()).trim());
				pubCustomerSaleMapperEntity.setCreator(userName);
				pubCustomerSaleMapperEntity.setCreateTime(currentTime);
				pubCustomerSaleMapperEntity.setCreatorId(JAppContext.currentUserID());
			}
			//插入正式表
			int insertNum = 0;
			try {
				insertNum = pubCustomerSaleMapperService.insertBatchTmp(templateList);
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
				//bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
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
	private List<PubCustomerSaleMapperEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<PubCustomerSaleMapperEntity> pubCustomerSaleList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PubCustomerSaleMapperEntity p = (PubCustomerSaleMapperEntity) BeanToMapUtil.convertMapNull(PubCustomerSaleMapperEntity.class, data);
				pubCustomerSaleList.add(p);
			}
			return pubCustomerSaleList;
		} catch (Exception e) {			
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PubCustomerSaleMapperEntity> prodList, Map<String, Object> map, String currentNo,Map<String,Object> param) {
		
		int lineNo = 0;
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		} 
		
		for (int i = 0; i < prodList.size(); i++) {
			PubCustomerSaleMapperEntity p=prodList.get(i);
			lineNo=lineNo+1;				
			
			// 非空校验
			String infoNull = checkInfoNull(p);
			if (StringUtils.isNotBlank(infoNull)) {
				setMessage(infoList, lineNo, infoNull);
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
	 * 非空校验
	 * @param vo
	 * @return
	 */
	private String checkInfoNull(PubCustomerSaleMapperEntity en){
		if (null == en) {
			return "参数错误!";
		}
		
		if (StringUtils.isBlank(en.getCustomerName())) {
			return "商家不能为空!";
		}
		if (StringUtils.isBlank(en.getOriginSellerName())) {
			return "原始销售员不能为空!";
		}
	
		return null;
	}
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	
	public List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("customerName");//商家
		list.add("originSellerName");//原始销售员
		return list;
	}
	
	public List<PubCustomerSaleMapperEntity> getOrgList(Map<String, Object> parameter) throws Exception {
		return pubCustomerSaleMapperService.query(parameter);
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
