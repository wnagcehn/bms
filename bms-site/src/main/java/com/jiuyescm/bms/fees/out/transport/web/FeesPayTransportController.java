package com.jiuyescm.bms.fees.out.transport.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.ListTool;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;
import com.jiuyescm.bms.fees.out.transport.service.IFeesPayTransportService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.forwarder.api.IForwarderService;
import com.jiuyescm.mdm.forwarder.vo.ForwarderVo;

@Controller("feesPayTransportController")
public class FeesPayTransportController {

	private static final Logger logger = Logger.getLogger(FeesPayTransportController.class.getName());
	
	@Resource
	private IFeesPayTransportService feesPayTransportService;
	
	@Resource
	private ISystemCodeService systemCodeService;

	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IProjectService projectService;
	
	@Autowired
	private IForwarderService forwarderService;
	
	@Autowired 
	private ICustomerService customerService;
	
	@Resource
	private Lock lock;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	@Resource
	private IBmsSubjectInfoService bmsSubjectInfoService;

	@DataProvider
	public void queryList(Page<FeesPayTransportEntity> page,List<FeesPayTransportQueryEntity> parameter){
		FeesPayTransportQueryEntity queryEntity=null;
		if(parameter==null){
			queryEntity=new FeesPayTransportQueryEntity();
		}
		else{
			queryEntity=parameter.get(0);
		}
		
		if(queryEntity != null && StringUtils.equalsIgnoreCase(queryEntity.getState(), "全部")){
			queryEntity.setState("");
		}
		if(StringUtils.isBlank(queryEntity.getForwarderName())){
			queryEntity.setForwarderId("");
		}
		if(StringUtils.isBlank(queryEntity.getCustomerName())){
			queryEntity.setCustomerId("");
		}
		if(!StringUtils.isBlank(queryEntity.getProjectId())){
			List<String> customerIds=projectService.queryAllCustomerIdByProjectId(queryEntity.getProjectId());
			if(customerIds==null||customerIds.size()==0){
				page.setEntities(null);
				page.setEntityCount(0);
				return;
			}else{
				queryEntity.setCustomerIdList(customerIds);
			}
		}
		
		if(!StringUtils.isBlank(queryEntity.getOrderno())){
			List<String> ordernos=new ArrayList<String>();
			String[] arr=queryEntity.getOrderno().split("\n");
			for(String s:arr){
				ordernos.add(s);
			}
			queryEntity.setOrdernoList(ordernos);
		}
		if(!StringUtils.isBlank(queryEntity.getWaybillNo())){
			List<String> waybillnos=new ArrayList<String>();
			String[] arr=queryEntity.getWaybillNo().split("\n");
			for(String s:arr){
				waybillnos.add(s);
			}
			queryEntity.setWaybillNolist(waybillnos);
		}
		if(!StringUtils.isBlank(queryEntity.getBillno())){
			List<String> billnos=new ArrayList<String>();
			String[] arr=queryEntity.getBillno().split("\n");
			for(String s:arr){
				billnos.add(s);
			}
			queryEntity.setBillnolist(billnos);
		}
		if(!StringUtils.isBlank(queryEntity.getRuleno())){
			List<String> rulenos=new ArrayList<String>();
			String[] arr=queryEntity.getRuleno().split("\n");
			for(String s:arr){
				rulenos.add(s);
			}
			queryEntity.setRulenolist(rulenos);
		}
		PageInfo<FeesPayTransportEntity> pageInfo = feesPayTransportService.query(queryEntity, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	private void InitList(List<FeesPayTransportEntity> list){
		Map<String,String> otherSubjectCodeMap=getOtherMap();
		Map<String,String> subjectCodeMap=getMap();
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesPayTransportEntity entity:list){
			if(otherSubjectCodeMap.containsKey(entity.getOtherSubjectCode())){
				entity.setOtherSubjectCodeName(otherSubjectCodeMap.get(entity.getOtherSubjectCode()));
			}else{
				entity.setOtherSubjectCodeName(entity.getOtherSubjectCode());
			}
			if(subjectCodeMap.containsKey(entity.getSubjectCode())){
				entity.setSubjectcodename(subjectCodeMap.get(entity.getSubjectCode()));
			}else{
				entity.setSubjectcodename(entity.getSubjectCode());
			}
			if(entity.getHasreceipt()==null){
				entity.setHasreceiptname("");
			}else{
				entity.setHasreceiptname(entity.getHasreceipt());
			}
			if(entity.getIslight()==null){
				entity.setIsLightName("");
			}else{
				if(entity.getIslight()==0){
					entity.setIsLightName("不是");
				}else{
					entity.setIsLightName("是");
				}
			}
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

	@DataProvider
	public Map<String,String> getSubjectCodeMap(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue=bmsGroupSubjectService.getSubject("pay_ts_contract_subject");
		return mapValue;
	}
	
	private Map<String,String> getMap(){
		Map<String, String> mapValue =bmsGroupSubjectService.getSubject("pay_ts_contract_subject");
		return mapValue;
	}
	private Map<String,String> getOtherMap(){
		Map<String, String> mapValue =bmsGroupSubjectService.getSubject("pay_transport_subject");
		return mapValue;
	}
	
	private Map<String,String> getSubjectMap(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<BmsSubjectInfoEntity> list=bmsSubjectInfoService.queryAll("OUTPUT", "TRANSPORT");
		for(BmsSubjectInfoEntity subject:list){
			mapValue.put(subject.getSubjectCode(), subject.getFeesType());
		}
		return mapValue;
	}
	
	@DataResolver
	public void save(FeesPayTransportEntity model){
		model.setCreperson(JAppContext.currentUserID());
		model.setCrepersonname(JAppContext.currentUserName());
		model.setCretime(JAppContext.currentTimestamp());
		String feesid=sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(),"YFFY","0000000000");
		model.setFeesNo(feesid);
		model.setOtherSubjectCode(model.getSubjectCode());
		model.setSubjectCode("ts_value_add_subject");//增值
		model.setState("0");//未过账
		feesPayTransportService.AddFeesReceiveDeliver(model);
	}
	
	@DataProvider
	public Map<String,String> getFeesType(){
		List<SystemCodeEntity> codeList=systemCodeService.findEnumList("FEES_RECEIVE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		for (SystemCodeEntity systemCodeEntity : codeList) {
			map.put(systemCodeEntity.getCode(),systemCodeEntity.getCodeName());
		}
		return map;
	}
	
	@Expose
	public String getunitPrice(FeesPayTransportEntity parameter){
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("customerid", parameter.getCustomerId());
		maps.put("subjectcode", parameter.getSubjectCode());
		
		return feesPayTransportService.queryunitPrice(maps);
	}
	
	
	
	/**
	 * 导入费用
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportPayFees(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
	/*	final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String userid=JAppContext.currentUserID();
		String lockString=Tools.getMd5("BMS_TRANSPORT_PAY_FEE_IMPORT"+userid);
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {*/
				Map<String, Object> map=new HashMap<String, Object>();
				/*try {*/
				remap=importFee(file,parameter);
					/*} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg("系统错误:"+e.getMessage());
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
				return map;
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("运输费用导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(
					LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
		});*/
		return remap;
	}
	
	
	
	
	/**
	 * 导入应收运输费用模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@FileResolver
	public Map<String, Object> importFee(UploadFile file, Map<String, Object> parameter) throws Exception {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		//long starTime=System.currentTimeMillis();	//开始时间
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;		
		// 导入成功返回模板信息
		List<FeesPayTransportEntity> feeList = new ArrayList<FeesPayTransportEntity>();
		try {
			String[] bs={"外部订单号","订单号","运单号","路单号","客户名称","发货地","到达地","承运商名称","产品","订单箱数","订单件数","重量","体积","货物类型","配送类型","发货日期","签收日期","退货","实收件数","有无回单"};
			boolean isTemplate=ExportUtil.checkTitle(file, bs);
			if (!isTemplate) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			// 解析Excel
			Map<String,Object> returnMap=readExcelData(infoList,file,bs,map);
			if (returnMap.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return returnMap;
			}else{
				feeList=(List<FeesPayTransportEntity>) returnMap.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
				if (null == feeList || feeList.size() <= 0) {
					DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
					errorVo = new ErrorMessageVo();
					errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
			}
				
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);

			//对feeList判断是插入还是更新
			Map<String, Object> checkMap=checkUpdateOrInsert(feeList);
			
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			//插入正式表
			int insertNum = feesPayTransportService.insertOrUpdate(checkMap);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("运输费用导入失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				return map;
			}
			
		} catch (Exception e) {
		    logger.error(e);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return map;
	}
	
	private Map<String, Object> checkUpdateOrInsert(List<FeesPayTransportEntity> feeList){
		Map<String, Object> map=new HashMap<String, Object>();
		//更新的list
		List<FeesPayTransportEntity> updateList=new ArrayList<FeesPayTransportEntity>();
		//插入的list
		List<FeesPayTransportEntity> insertList=new ArrayList<FeesPayTransportEntity>();
		
		List<List<FeesPayTransportEntity>> feelist=ListTool.splitList(feeList, 100);
		for(int i=0;i<feelist.size();i++){
			
			//源数据
			List<FeesPayTransportEntity> fList=(List<FeesPayTransportEntity>) feelist.get(i);
			List<String> waybillNoList=new ArrayList<String>();	
			//封装成map
			Map<String, Object> feeMap=new HashMap<String, Object>();
			for(FeesPayTransportEntity fee:fList){
				waybillNoList.add(fee.getWaybillNo());
				feeMap.put(getKey(fee), fee);
			}
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("cretime", getTime(fList.get(0)));
			paramMap.put("wayBillNoList", waybillNoList);
			//根据运单号和时间查询出的数据
			List<FeesPayTransportEntity> returnList=feesPayTransportService.queryList(paramMap);
			
			//循环判断哪些是新增哪些是插入
			for(FeesPayTransportEntity returnFee:returnList){
				String str=getKey(returnFee);		
				if(feeMap.containsKey(str)){
					//过账状态是0的才能更新，已过账1的无法更新
					if("0".equals(returnFee.getState())){
						FeesPayTransportEntity fee=(FeesPayTransportEntity) feeMap.get(str);
						//更新时
						returnFee.setWeight(fee.getWeight());
						returnFee.setTotleprice(fee.getTotleprice());
						returnFee.setModperson(JAppContext.currentUserID());
						returnFee.setModpersonname(JAppContext.currentUserName());
						returnFee.setModtime(JAppContext.currentTimestamp());
						updateList.add(returnFee);
					}				
				}
			}
			
			//将更新的数据分装成map,用于判断
			Map<String, Object> updateMap=new HashMap<String, Object>();
			for(FeesPayTransportEntity fee:updateList){
				updateMap.put(getKey(fee), fee);
			}
			
			//判断数据是否是更新的，不是更新的全是插入
			for(FeesPayTransportEntity orDeliverEntity:fList){
				String str=getKey(orDeliverEntity);
				if(!updateMap.containsKey(str)){
					String feesNo = sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YSFY", "0000000000");
					orDeliverEntity.setFeesNo(feesNo);
					orDeliverEntity.setState("0");
					orDeliverEntity.setCreperson(JAppContext.currentUserID());
					orDeliverEntity.setCrepersonname(JAppContext.currentUserName());					
					insertList.add(orDeliverEntity);
				}
			}		
		}
		map.put("update", updateList);
		map.put("insert", insertList);
		return map;
	}
	
	private String getKey(FeesPayTransportEntity fee){
		return fee.getOrderno()+"&"+fee.getWaybillNo()+"&"+fee.getCustomerId()+"&"+fee.getForwarderId()+"&"+fee.getSubjectCode()+"&"+fee.getCretime();
	}
	
	@SuppressWarnings("deprecation")
	private String getTime(FeesPayTransportEntity fee){
		String time;
		if(fee.getCretime().getMonth()>=0 && fee.getCretime().getMonth()<=9){
			time=(fee.getCretime().getYear()+1900)+"-0"+(fee.getCretime().getMonth() + 1);
		}else{
			time=(fee.getCretime().getYear()+1900)+"-"+(fee.getCretime().getMonth() + 1);
		}
		return time;
		
	}
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Map<String, Object> readExcelData(List<ErrorMessageVo> infoList,UploadFile file,String[] str,Map<String, Object> map) {
		//所有的科目
		Map<String, String> subjectMap=getAllSubject();
		//商家信息
		Map<String, String> customerMap =getCustomerMap();
		//宅配商信息
		Map<String, String> forwarMap=getAllCarrier();	
		//产品信息
		Map<String,String> productMap=getTransportType();
		//科目详细
		Map<String,String> subjectTypeMap=getSubjectMap();
		
		List<FeesPayTransportEntity> receiveFeeList = Lists.newArrayList();
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 	
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			if(xssfSheet==null){
				return null;
			}
			//读取第一行的费用类型
			Map<Integer, Object> subjectList=new HashMap<Integer, Object>(); 
			for(int rowNum=0;rowNum<1;rowNum++){
				// 读取表头信息
				Row row = xssfSheet.getRow(rowNum);
				for(int i=20;i<row.getLastCellNum();i++){
					String val = row.getCell(i).getStringCellValue();
					//判断费用类型是否存在
					if(!subjectMap.containsKey(val)){
						setMessage(infoList,1,"EXCEL中第"+(i+1)+"列费用科目不存在!");
					}else{
						subjectList.put(i, val);
					}							
				}			
			}
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			//验证导入数据有重复
			List<String> dataKeyList=new ArrayList<String>();
			
			//标识判断是否是当月数据
			String currentTime = null;
			
			//开始读数据
			for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++){		
				XSSFRow xssfRow = xssfSheet.getRow(rowNum); 
				
				String externalNo = ExportUtil.getValue(xssfRow.getCell(0), evaluator);//外部订单号
				String orderno = ExportUtil.getValue(xssfRow.getCell(1), evaluator);//订单号
				String waybillNo = ExportUtil.getValue(xssfRow.getCell(2), evaluator);//运单号
				String roadbillNo=ExportUtil.getValue(xssfRow.getCell(3), evaluator);//路单号
				String customerName = ExportUtil.getValue(xssfRow.getCell(4), evaluator);//客户名称
				String sendddress = ExportUtil.getValue(xssfRow.getCell(5), evaluator);//发货地
				String receiveAddress = ExportUtil.getValue(xssfRow.getCell(6), evaluator);//客户名称			
				String forwadName = ExportUtil.getValue(xssfRow.getCell(7), evaluator);//承运商名称
				String productType = ExportUtil.getValue(xssfRow.getCell(8), evaluator);//产品
				String orderBoxno = ExportUtil.getValue(xssfRow.getCell(9), evaluator);//订单箱数
				String orderNumber = ExportUtil.getValue(xssfRow.getCell(10), evaluator);//订单件数
				String excelWeight = ExportUtil.getValue(xssfRow.getCell(11), evaluator);//重量
				String excelVolumn = ExportUtil.getValue(xssfRow.getCell(12), evaluator);//体积
				String goodType= ExportUtil.getValue(xssfRow.getCell(13), evaluator);//货物类型
				String dispatchType = ExportUtil.getValue(xssfRow.getCell(14), evaluator);//配送类型
				String cretime = ExportUtil.getValue(xssfRow.getCell(15), evaluator);//发货日期
				String signtime = ExportUtil.getValue(xssfRow.getCell(16), evaluator);//签收日期
				String returnGoods = ExportUtil.getValue(xssfRow.getCell(17), evaluator);//退货
				String receiptNumber = ExportUtil.getValue(xssfRow.getCell(18), evaluator);//实收件数
				String isReceiveOrder = ExportUtil.getValue(xssfRow.getCell(19), evaluator);//实收件数
	
				if(StringUtils.isEmpty(externalNo)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行外部单号不能为空!");
				}
				
				if(StringUtils.isEmpty(orderno)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行订单号不能为空!");
				}
				
				if(StringUtils.isEmpty(waybillNo)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行运单号不能为空!");
				}
				
				//------------------------校验商家全称----------------------
				String customerId="";
				if(StringUtils.isEmpty(customerName)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行客户名称不能为空!!");
				}else{
					if(customerMap.containsKey(customerName)){
						customerId=customerMap.get(customerName);
					}else{
						setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行该客户名称不存在!!");
					}					
				}
				
				//------------------校验承运商-------------------
				String forwarderId="";
				if(StringUtils.isEmpty(forwadName)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行承运商名称不能为空!");
				}else{
					if(forwarMap.containsKey(forwadName)){
						forwarderId=forwarMap.get(forwadName);
					}else{
						setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行该承运商名称不存在!!");
					}
				}
				
				//--------------------校验产品类型---------------
				if(StringUtils.isEmpty(productType)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行产品类型不能为空!");
				}else{
					if(productMap.containsKey(productType)){
						productType=productMap.get(productType);
					}else{
						setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行该产品类型不存在!!");
					}
				}
				
				//----------------------校验重量------------------------
				double weight=0;
				if(StringUtils.isEmpty(excelWeight)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行重量不能为空!");				
				}else if(!isNumer(excelWeight)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行重量不能为非数字!");	
				}else{
					weight=Double.valueOf(excelWeight);
				}
				
				//------------------------校验体积-------------------
				double volum=0;
				if(StringUtils.isNotBlank(excelVolumn)){
					if(!isNumer(excelWeight)){
						setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行体积不能为非数字!");	
					}else{
						volum=Double.valueOf(excelVolumn);
					}
				}
				
				//--------------------------校验发货日期-------------------------
				Timestamp time = null;
				if(StringUtils.isEmpty(cretime)){
					setMessage(infoList, rowNum+1,"第"+(rowNum+1)+"行发货日期不能为空！");
				}else{
					try {
						  time = Timestamp.valueOf(cretime);
					  } catch (Exception e){
						  try {
							  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
							  Date date = DateUtils.parseDate(cretime, dataPatterns);
							  time = new Timestamp(date.getTime());
						} catch (Exception e1) {
							 setMessage(infoList, rowNum+1,"第"+rowNum+1+"行发货日期不能为空！");
						}
					}
				}
				
				//-------------------------校验签收日期---------------------------
				Timestamp ordersigntime = null;
				if(!StringUtils.isEmpty(signtime)){
					try {
						ordersigntime = Timestamp.valueOf(signtime);
					} catch (Exception e){
						  try {
							  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
							  Date date = DateUtils.parseDate(cretime, dataPatterns);
							  ordersigntime = new Timestamp(date.getTime());
						} catch (Exception e1) {
							 setMessage(infoList, rowNum+1,"第"+rowNum+1+"行签收日期格式不对！");
						}
					}
				}		
				
				//只支持导入同一月份的数据，不是同一月份的立即退出
				if(time!=null){
					if(rowNum==1){
						currentTime=(time.getYear()+1900)+""+(time.getMonth() + 1);
					}else{
						
						String newTime=(time.getYear()+1900)+""+(time.getMonth() + 1);
						if(!currentTime.equals(newTime)){
							setMessage(infoList, 0,"只支持导入同月份的数据！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
						}
					}
				}
			
				
				//Excel自身校验，判断是否有重复项("订单号","运单号","商家全称","承运商","发货日期","重量")
				String dataKey=orderno+"&"+waybillNo+"&"+customerName+"&"+forwadName+"&"+cretime;
				if(!dataKeyList.contains(dataKey)){
					dataKeyList.add(dataKey);
				}else{//数据有重复
					setMessage(infoList, rowNum+1,"Excel中第"+(rowNum+1)+"行数据重复");
					if(infoList.size()>=1000){
						break;
					}
				}
				
				if(infoList.size()<=0){
					//循环费用科目
					for(int j=20;j<xssfRow.getLastCellNum();j++){
						//获取列值
						String fees = getCellValue(xssfRow.getCell(j));					
						if(!StringUtils.isEmpty(fees)){
							if(!isNumer(fees)){
								setMessage(infoList, rowNum+1,"Excel中第"+(j+1)+"列费用不能为非数字");
							}else{
								Double price=Double.valueOf(fees);
								String subjectName=subjectList.get(j).toString();						
								String subjectCode=subjectMap.get(subjectName);
								if("ts_abnormal_pay".equals(subjectCode) && price>0){
									setMessage(infoList, rowNum+1,"Excel中第"+(j+1)+"列费用不能为正数");
								}else{
									FeesPayTransportEntity entity=new FeesPayTransportEntity();
									entity.setExternalNo(externalNo);
									entity.setOrderno(orderno);
									entity.setWaybillNo(waybillNo);
									entity.setRoadbillNo(roadbillNo);
									entity.setCustomerName(customerName);
									entity.setCustomerId(customerId);
									entity.setOrgaddress(sendddress);
									entity.setTargetaddress(receiveAddress);
									entity.setForwarderId(forwarderId);
									entity.setForwarderName(forwadName);
									entity.setProductType(productType);
									entity.setBoxnum(orderBoxno);
									entity.setOrdernum(orderNumber);
									entity.setWeight(weight);
									entity.setVolume(volum);
									entity.setGoodType(goodType);
									entity.setDispatchType(dispatchType);
									entity.setSigntime(ordersigntime);
									entity.setBacknum(returnGoods);
									entity.setReceivenum(receiptNumber);
									entity.setHasreceipt(isReceiveOrder);			
									entity.setCretime(time);
									entity.setOtherSubjectCode(subjectCode);
									entity.setOtherSubjectCodeName(subjectName);
									if(subjectTypeMap.containsKey(subjectCode)){
										String feeType=subjectTypeMap.get(subjectCode);
										if("BASE".equals(feeType) || StringUtils.isBlank(feeType)){
											entity.setSubjectCode("ts_base_subject");
										}else{
											entity.setSubjectCode("ts_value_add_subject");
										}
									}									
									entity.setTotleprice(price);
									entity.setDelFlag("0");
									receiveFeeList.add(entity);
								}					
							}
						}
					}
				}		
			}
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);		
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, receiveFeeList);	
			}
			
			return map;
			
		} catch (IOException e) {
			logger.error(e);
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
	 * 获取所有商家
	 * @return
	 */
	public Map<String, String> getCustomerMap(){
		Map<String,Object> parameter =new HashMap<String,Object>();
		
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, 0, Integer.MAX_VALUE);
		Map<String, String> map = new LinkedHashMap<String,String>();
		if(tmpPageInfo !=null && tmpPageInfo.getTotal()>0){
			Iterator<CustomerVo> iter = tmpPageInfo.getList().iterator();
			while(iter.hasNext()){
				CustomerVo cvo = (CustomerVo) iter.next();
				map.put(cvo.getCustomername(), cvo.getCustomerid());
			}
		}
		return map;
	}
	
	/**
	 * 获取所有的费用科目
	 * @return
	 */
	@DataProvider
	public Map<String, String> getAllSubject(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		/*List<SystemCodeEntity> sList=systemCodeService.findEnumList("TRANSPORT_TEMPLATE_PAY_TYPE");
		//List<SystemCodeEntity> mList=systemCodeService.findEnumList("ts_value_add_subject");
		for(SystemCodeEntity en:sList){
			mapValue.put(en.getCodeName(),en.getCode());
		}*/
	/*	for(SystemCodeEntity en:mList){
			mapValue.put(en.getCodeName(),en.getCode());
		}*/
		
		mapValue=bmsGroupSubjectService.getImportSubject("pay_transport_subject");
		return mapValue;
	}
	
	/**
	 * 获取所有的承运商
	 * @return
	 */
	@DataProvider
	public Map<String, String> getAllCarrier(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<ForwarderVo> list=forwarderService.queryAll();
		for(ForwarderVo entity:list){
			mapValue.put(entity.getName(), entity.getForwardId());
		}
		return mapValue;
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
	 * 获取所有的费用科目
	 * @return
	 */
	@DataProvider
	public Map<String, String> getTransportType(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> sList=systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		for(SystemCodeEntity en:sList){
			mapValue.put(en.getCodeName(),en.getCode());
		}
		return mapValue;
	}
	
	public static boolean isNumer(String str){  
		  try{  
			   Double.valueOf(str);  
			   return true;  
		  }catch(NumberFormatException e){  
			  return false;  
		  }  
		}
}
