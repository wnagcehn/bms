package com.jiuyescm.bms.paymanage.payimport.web;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeTypeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.paymanage.payimport.FeesPayImportEntity;
import com.jiuyescm.bms.paymanage.payimport.service.IFeesPayImportService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.FeesPayTemplateDataType;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.thoughtworks.xstream.core.util.Base64Encoder;

@Controller("feesPayImportController")
public class FeesPayImportController{
	
	@Resource
	private IFeesPayImportService feesPayImportService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Resource
	private ISystemCodeTypeService systemCodeTypeService;
	
	@Resource
	private IBmsSubjectInfoService bmsSubjectInfoService;
	
	@Resource
	private Lock lock;
	
	private static final Logger logger = Logger.getLogger(FeesPayImportController.class);
	
	/**
	 * 查询所有的成本费用
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<FeesPayImportEntity> page,Map<String,Object> parameter){
		if(parameter==null){
			parameter=new HashMap<String, Object>();
		}
		
		if(parameter.get("state")!=null){
			if("0".equals(parameter.get("state"))){
				parameter.put("state","0");
			}else if("1".equals(parameter.get("state"))){
				parameter.put("state","1");
			}
		}	
		PageInfo<FeesPayImportEntity> tmpPageInfo =feesPayImportService.queryAll(parameter,page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	
	/**
	 * 批量更新
	 */
	@DataResolver
	public String update(List<FeesPayImportEntity> pList){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			for(FeesPayImportEntity p:pList){
				p.setLastModifier(JAppContext.currentUserName());
				p.setLastModifyTime(JAppContext.currentTimestamp());
			}
			feesPayImportService.updateList(pList);
/*			feesPayImportService.updateList(pList);
*/			
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
	public String remove(List<FeesPayImportEntity> pList){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			for(FeesPayImportEntity p:pList){
				p.setDelFlag("1");
				p.setLastModifier(JAppContext.currentUserName());
				p.setLastModifyTime(JAppContext.currentTimestamp());
			}
			feesPayImportService.remove(pList);
/*			feesPayImportService.updateList(pList);
*/			
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			e.printStackTrace();
			return "数据库操作失败";
		}
			
	}
	
	/**
	 * 导入成本费用模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importFeesPayTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		String lockString=getMd5("BMS_FEES_PAY_IMPORT"+JAppContext.currentUserID());
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
				errorVo.setMsg("成本导入功能已被其他用户占用，请稍后重试；");
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
		Map<String,Object> paramMap=new HashMap<String,Object>();
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		
		ErrorMessageVo errorVo = null;
		
		// 导入的成本数据集合
		List<FeesPayImportEntity> templateList = new ArrayList<FeesPayImportEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		
		try {
			//判断日期
			//获取文件名
			//处理表名日期
			// 模板信息必填项校验
			int date=0;
			map = getDate(infoList, map,file);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}else{
				if(StringUtils.isNumeric(map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC).toString())){
					date=Integer.parseInt(map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC).toString());
				}else{
					errorVo = new ErrorMessageVo();
					errorVo.setMsg("Excel导入日期格式错误!");
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
				
			}
			
			BaseDataType bs=null;
			bs=new FeesPayTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}	
			
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			
			// 解析Excel
			templateList = readExcelProduct(file,bs);
			if (null == templateList || templateList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			//设置属性
			for(int i=0;i<templateList.size();i++){
				FeesPayImportEntity p=templateList.get(i);
				p.setState("0");
				p.setCreateMonth(date);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
			}
			
			//新增判断是新增还是更新
			Map<String,Object> mapList=checkUpdateOrInsert(templateList);
			try {
			   int num=feesPayImportService.insertOrUpdate(mapList);
			   if (num <= 0) {
					errorVo = new ErrorMessageVo();
					errorVo.setMsg("插入正式表失败!");
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}else{
					map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
					logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
					DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
					
				}
			}catch (Exception e) {
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return paramMap;
	}
	
	/**
	 * 配送模板信息下载
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile getTemplate(Map<String, String> parameter) throws IOException {
			InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/fees_pay_import_template.xlsx");
			return new DownloadFile("XXXX年XX月成本信息导入模板.xlsx", is);
	}
	
	private Map<String,Object> checkUpdateOrInsert(List<FeesPayImportEntity> prodList){
		Map<String, Object> cMap=new HashMap<String, Object>();
		//更新的数据
		List<FeesPayImportEntity> updateList=new ArrayList<FeesPayImportEntity>();
		//插入的数据
		List<FeesPayImportEntity> insertList=new ArrayList<FeesPayImportEntity>();
		
		for(int i=0;i<prodList.size();i++){
			FeesPayImportEntity feesPay=prodList.get(i);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("createMonth", feesPay.getCreateMonth());
			map.put("bizType", feesPay.getBizType());
			map.put("warehouseCode", feesPay.getWarehouseCode());
			map.put("subjectCode", feesPay.getSubjectCode());
			FeesPayImportEntity fees=feesPayImportService.queryOne(map);
			if(fees!=null){
				if(("0").equals(fees.getState())){
					fees.setAmount(feesPay.getAmount());
					fees.setCreator(JAppContext.currentUserName());
					fees.setCreateTime(JAppContext.currentTimestamp());
					updateList.add(fees);
				}				
			}else{
				insertList.add(feesPay);
			}
		}
		cMap.put("update", updateList);
		cMap.put("insert", insertList);
		
		return cMap;
	}
	
	/**
	 * 获取日期
	 * @param infoList
	 * @param prodList
	 * @param map
	 * @param file
	 * @return
	 */
	private Map<String, Object> getDate(List<ErrorMessageVo> infoList, Map<String, Object> map,UploadFile file) {
		if(file.getFileName().indexOf("年")==-1 || file.getFileName().indexOf("月")==-1){
			setMessage(infoList, 0,"文件名有误，必须包含年和月!");
		}
			
		String fileName=file.getFileName();
		String year=fileName.substring(0,fileName.indexOf("年"));
		String month=fileName.substring(fileName.indexOf("年")+1,fileName.indexOf("月"));
		if(month.length()==1){
			month="0"+month;
		}
		String date=year.concat(month);
		
		//判断是否只能导入上个月的数据
		String result=queryIsLimit();
		if("yes".equals(result)){
			//启用时
			//当前的时间
			Calendar cal = Calendar.getInstance();
			int year1 = cal.get(Calendar.YEAR);
			int month2 = cal.get(Calendar.MONTH );
			String newDate=formatMonth(year1, month2);
			if(!date.equals(newDate)){
				setMessage(infoList, 0,"当前只支持导入上个月的数据");
			}
		}
		
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, date); // 无基本错误信息
		}
		
		return map;
	}
	
	
	//查询所有的服务
	@Expose
	public String queryIsLimit(){
		SystemCodeTypeEntity systemCodeTypeEntity=systemCodeTypeService.findByTypeCode("PAY_IMPORT_LIMIT");
		if(systemCodeTypeEntity!=null && systemCodeTypeEntity.getTypeStatus().equals("0")){
			return "yes";
		}
		return "no";
		
	}
	
	private String formatMonth(int year,int month){
		String mString="";	
		if(month<=0){
			mString=(12+month)+"";
			 year=year-1;
		}
		if (month >= 1 && month <= 9) {
			mString = "0" + month;
	    }
		return year+mString;
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<FeesPayImportEntity> prodList, Map<String, Object> map) {
		int lineNo = 1;
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		//所有的业务类型
		List<SystemCodeEntity> bizTypeList = systemCodeService.findEnumList("BUSSINESS_TYPE");
		//所有的费用科目
		List<BmsSubjectInfoEntity> subjectList=bmsSubjectInfoService.queryAll("");
		
		for (int i = 0; i < prodList.size(); i++) {
			
			FeesPayImportEntity p=prodList.get(i);
			lineNo=lineNo+1;
			//判断业务类型
			if(StringUtils.isBlank(p.getBizType())){
				setMessage(infoList, lineNo,"费用类别为空!");
			}else{				
				boolean result=false;		
				for(SystemCodeEntity s:bizTypeList){
					if(s.getCodeName().equals(p.getBizType())){
						p.setBizType(s.getCode());
						result=true;
						break;
					}
				}
				if(!result){
					setMessage(infoList, lineNo,"当前的费用类别不存在!");
				}else{
					//业务类型存在的情况下，判断费用科目
					if(StringUtils.isBlank(p.getSubjectName())){
						setMessage(infoList, lineNo,"费用名称不能为空!");
					}else{
						boolean re=false;
						for(BmsSubjectInfoEntity sub:subjectList){
							if(sub.getBizTypecode().equals(p.getBizType()) && sub.getSubjectName().equals(p.getSubjectName())){
								p.setSubjectCode(sub.getSubjectCode());
								re=true;
								break;
							}
						}
						if(!re){
							setMessage(infoList, lineNo,"当前的费用名称不存在!");
						}
					}
					
					
				}
				
			}
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			if(StringUtils.isNotBlank(p.getWarehouseCode())){
				boolean result=false;
				for(WarehouseVo entity:wareHouselist){
					if(entity.getWarehousename().equals(p.getWarehouseCode())){
						p.setWarehouseCode(entity.getWarehouseid());
						result=true;
						break;
					}
				}
				
				if(!result){
					setMessage(infoList, lineNo,"仓库id没有在仓库表中维护!");
				}
			}else{
				if("STORAGE".equals(p.getBizType())){
					setMessage(infoList, lineNo,"仓储类型仓库必填!");
				}
			}
			
			//判断金额
			if(p.getAmount()==null){
				setMessage(infoList, lineNo,"金额不能为空或者非数字!");
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
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<FeesPayImportEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<FeesPayImportEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				FeesPayImportEntity p = (FeesPayImportEntity) BeanToMapUtil.convertMapNull(FeesPayImportEntity.class, data);
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
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	
	/**
	 * 审核状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCheckedState(String result) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("1","已审核");
		mapValue.put("0", "未审核");
		return mapValue;
	}
	
	
	@DataProvider
	public Map<String, String> getAllSubject(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<BmsSubjectInfoEntity> subjectList=bmsSubjectInfoService.queryAll("");
		for(BmsSubjectInfoEntity subjectInfoEntity:subjectList){
			mapValue.put(subjectInfoEntity.getSubjectCode(), subjectInfoEntity.getSubjectName());
		}
		return mapValue;
	}
	
	@DataProvider
	public Map<String, String> getAllMonth(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		for(int i=1;i<=12;i++){
			mapValue.put(i+"", i+"");
		}
		return mapValue;
	}

}
