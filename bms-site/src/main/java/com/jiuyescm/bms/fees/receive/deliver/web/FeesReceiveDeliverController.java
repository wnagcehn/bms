package com.jiuyescm.bms.fees.receive.deliver.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.ListTool;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.forwarder.api.IForwarderService;
import com.jiuyescm.mdm.forwarder.vo.ForwarderVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("feesReceiveDeliverController")
public class FeesReceiveDeliverController {

	private static final Logger logger = Logger.getLogger(FeesReceiveDeliverController.class.getName());
	private DateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private DateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
	
	@Resource
	private IFeesReceiverDeliverService feesReceiverDeliverService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	//@Resource
	//IPriceDispatchService priceDispatchService;
	
	@Autowired
	private IWarehouseService warehouseService;

	@Resource 
	private SequenceService sequenceService;
	
	@Autowired 
	private ICustomerService customerService;
	
	@Resource
	private IAddressService addressService;
	
	@Resource
	private IProjectService projectService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Autowired
	private IForwarderService forwarderService;
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	@Resource
	private Lock lock;
	

	@DataProvider
	public void queryList(Page<FeesReceiveDeliverEntity> page,List<FeesReceiveDeliverQueryEntity> parameter){
		FeesReceiveDeliverQueryEntity queryEntity=null;
		if(parameter==null){
			queryEntity=new FeesReceiveDeliverQueryEntity();
		}
		else{
			queryEntity=parameter.get(0);
		}
		
		if(queryEntity == null
				|| (StringUtils.isBlank(queryEntity.getState()) && queryEntity.getCrestime() == null && queryEntity.getCreetime() == null)){
			Date currentDate = new Date();
			
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(currentDate); 
		    calendar.add(calendar.DATE,-6);//把日期往前减少七天.整数往后推,负数往前移动 
		   
		    
			queryEntity.setCrestime(Timestamp.valueOf(startDateFormat.format(calendar.getTime())));
			queryEntity.setCreetime(Timestamp.valueOf(endDateFormat.format(currentDate)));
		}
		if(queryEntity != null && StringUtils.equalsIgnoreCase(queryEntity.getState(), "全部")){
			queryEntity.setState("");
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
		PageInfo<FeesReceiveDeliverEntity> pageInfo = feesReceiverDeliverService.query(queryEntity, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	private void InitList(List<FeesReceiveDeliverEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesReceiveDeliverEntity entity:list){
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
	@DataProvider
	public List<SystemCodeEntity> getTransportSubjectTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("ts_value_add_subject");
		return systemCodeList;
	}
	
	@DataResolver
	public void save(FeesReceiveDeliverEntity model){
		model.setCreperson(JAppContext.currentUserID());
		model.setCrepersonname(JAppContext.currentUserName());
		//2017-08-01 创建时间要求存操作时间.
		if(model != null && model.getOperationtime() != null){
			model.setCretime(model.getOperationtime());
		}
		String feesid=sequenceService.getBillNoOne(FeesReceiveDeliverEntity.class.getName(),"YSFY","0000000000");
		model.setFeesNo(feesid);
		model.setCostType("TRANSPORT_VALUEADDED");//增值费用
		model.setOtherSubjectCode(model.getSubjectCode());
		model.setSubjectCode("ts_value_add_subject");//增值
		model.setState("0");//未过账
		feesReceiverDeliverService.addFeesReceiveDeliver(model);
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
	public String getunitPrice(FeesReceiveDeliverEntity parameter){
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("customerid", parameter.getCustomerid());
		maps.put("subjectcode", parameter.getSubjectCode());
		
		return feesReceiverDeliverService.queryunitPrice(maps);
	}

	
	/**
	 * 应收运输费用模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile getFeesImportTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_receivefee_template.xlsx");
		return new DownloadFile("应收运输费用导入模板.xlsx", is);
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
	 * 导入费用
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportReceiveFees(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String userid=JAppContext.currentUserID();
		String lockString=Tools.getMd5("BMS_TRANSPORT_RECEIVE_FEE_IMPORT"+userid);
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=new HashMap<String, Object>();
				try {
					map=importFee(file,parameter);
				} catch (Exception e) {
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
			
		});
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
		List<FeesReceiveDeliverEntity> feeList = new ArrayList<FeesReceiveDeliverEntity>();
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
				feeList=(List<FeesReceiveDeliverEntity>) returnMap.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
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
			int insertNum = feesReceiverDeliverService.insertOrUpdate(checkMap);
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
			e.printStackTrace();
			// TODO: handle exception
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return map;
	}
	
	private Map<String, Object> checkUpdateOrInsert(List<FeesReceiveDeliverEntity> orFeeList){
		Map<String, Object> map=new HashMap<String, Object>();
		//更新的list
		List<FeesReceiveDeliverEntity> updateList=new ArrayList<FeesReceiveDeliverEntity>();
		//插入的list
		List<FeesReceiveDeliverEntity> insertList=new ArrayList<FeesReceiveDeliverEntity>();
		
		List<List<FeesReceiveDeliverEntity>> feelist=ListTool.splitList(orFeeList, 100);
		for(int i=0;i<feelist.size();i++){
			
			//源数据
			List<FeesReceiveDeliverEntity> fList=(List<FeesReceiveDeliverEntity>) feelist.get(i);
			List<String> waybillNoList=new ArrayList<String>();	
			//封装成map
			Map<String, Object> feeMap=new HashMap<String, Object>();
			for(FeesReceiveDeliverEntity fee:fList){
				waybillNoList.add(fee.getWaybillNo());
				feeMap.put(getKey(fee), fee);
			}
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("cretime", getTime(fList.get(0)));
			paramMap.put("wayBillNoList", waybillNoList);
			//根据运单号和时间查询出的数据
			List<FeesReceiveDeliverEntity> returnList=feesReceiverDeliverService.queryList(paramMap);
			
			//循环判断哪些是新增哪些是插入
			for(FeesReceiveDeliverEntity returnFee:returnList){
				String str=getKey(returnFee);		
				if(feeMap.containsKey(str)){
					//过账状态是0的才能更新，已过账1的无法更新
					if("0".equals(returnFee.getState())){
						FeesReceiveDeliverEntity fee=(FeesReceiveDeliverEntity) feeMap.get(str);
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
			for(FeesReceiveDeliverEntity fee:updateList){
				updateMap.put(getKey(fee), fee);
			}
			
			//判断数据是否是更新的，不是更新的全是插入
			for(FeesReceiveDeliverEntity orDeliverEntity:fList){
				String str=getKey(orDeliverEntity);
				if(!updateMap.containsKey(str)){
					String feesNo = sequenceService.getBillNoOne(FeesReceiveDeliverEntity.class.getName(), "YSFY", "0000000000");
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
	
	private String getKey(FeesReceiveDeliverEntity fee){
		return fee.getOrderno()+"&"+fee.getWaybillNo()+"&"+fee.getCustomerid()+"&"+fee.getForwarderId()+"&"+fee.getSubjectCode()+"&"+fee.getCretime();
	}
	
	@SuppressWarnings("deprecation")
	private String getTime(FeesReceiveDeliverEntity fee){
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
		
		List<FeesReceiveDeliverEntity> receiveFeeList = Lists.newArrayList();
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
					if(!isNumer(excelVolumn)){
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
								//理赔费用只能是负数或0
								if("ts_abnormal_pay".equals(subjectCode) && price>0){
									setMessage(infoList, rowNum+1,"Excel中第"+(j+1)+"列费用不能为正数");
								}else{
									FeesReceiveDeliverEntity entity=new FeesReceiveDeliverEntity();
									entity.setExternalNo(externalNo);
									entity.setOrderno(orderno);
									entity.setWaybillNo(waybillNo);
									entity.setRoadbillNo(roadbillNo);
									entity.setCustomerName(customerName);
									entity.setCustomerid(customerId);
									entity.setSendAddress(sendddress);
									entity.setReceiveAddress(receiveAddress);
									entity.setForwarderId(forwarderId);
									entity.setForwarderName(forwadName);
									entity.setProductType(productType);
									entity.setOrderBoxno(orderBoxno);
									entity.setOrderNumber(orderNumber);
									entity.setWeight(weight);
									entity.setVolume(volum);
									entity.setGoodType(goodType);
									entity.setDispatchType(dispatchType);
									entity.setSigntime(ordersigntime);
									entity.setReturnGoods(returnGoods);
									entity.setReceiptNumber(receiptNumber);
									entity.setIsReceiveOrder(isReceiveOrder);			
									entity.setCretime(time);							
									entity.setSubjectCode(subjectCode);	
									entity.setTotleprice(price);
									entity.setDelFlag("0");
									entity.setIsCalculated("1");
									entity.setCalculateTime(time);
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
			// TODO Auto-generated catch block
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
	private List<FeesReceiveDeliverEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<FeesReceiveDeliverEntity> receiveFeeList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				FeesReceiveDeliverEntity p = (FeesReceiveDeliverEntity) BeanToMapUtil.convertMapNull(FeesReceiveDeliverEntity.class, data);
				receiveFeeList.add(p);
			}
			return receiveFeeList;
		} catch (Exception e) {
			e.printStackTrace();
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return null;
	}
	
	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	public Map<String, String> getPubWareHouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehousename(), warehouseVo.getWarehouseid());
		}
		return map;
	}
	
	/**
	 * 运输产品类型TRANSPORT_PRODUCT_TYPE： 城际专列、同城专列、电商专列、航线达 ； 
	 */
	public Map<String, String> getTransportProductTypeName2CodeMap(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				//不需要增值费
				if(!StringUtils.equalsIgnoreCase(systemCodeList.get(i).getCode(), "ts_value_add_subject")){
					map.put(systemCodeList.get(i).getCodeName(), systemCodeList.get(i).getCode());
				}
			}
		}
		return map;
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<FeesReceiveDeliverEntity> feeList, Map<String, Object> map, String currentNo) {
		int lineNo = 0;
		//仓库信息
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		//商家信息
		Map<String, String> customerMap = new HashMap<String, String>();
		//产品类型
		Map<String, String> productTypeMap = new HashMap<String, String>();

		//判断省市区 存不存在
		List<RegionVo> regionList = new ArrayList<RegionVo>();
		List<RegionVo> targetList = new ArrayList<RegionVo>();
		
		//当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		if(feeList != null && feeList.size()>0){
			wareHouseMap = getPubWareHouse();
			customerMap = getCustomerMap(); 
			productTypeMap = getTransportProductTypeName2CodeMap();
			
			int line=1;
			for (int i = 0; i < feeList.size(); i++) {
				//当前行号
				line = line+1;	
				FeesReceiveDeliverEntity fee = feeList.get(i);
				//==============================================================================================================
				//判断地址是否为空
				if(StringUtils.isBlank(fee.getOriginProvince()) || StringUtils.isBlank(fee.getTargetProvince())){
					setMessage(infoList, line,"始发省份或者目的省份不能为空!");
				}
				if(StringUtils.isBlank(fee.getOriginatingcity()) || StringUtils.isBlank(fee.getTargetcity())){
					setMessage(infoList, line,"始发城市或者目的城市不能为空!");
				}
				if(StringUtils.isBlank(fee.getOriginatingdistrict()) || StringUtils.isBlank(fee.getTargetdistrict())){
					setMessage(infoList, line,"始发地区或者目的地区不能为空!");
				}
				//===================================始发校验======================================================================
				//起始地址
				RegionVo regionVo = new RegionVo();
				regionVo.setProvince(fee.getOriginProvince());
				regionVo.setCity(fee.getOriginatingcity());
				regionVo.setDistrict(fee.getOriginatingdistrict());
				regionVo.setLineNo(line);
				regionList.add(regionVo);
				//目的地址
				RegionVo targetVo = new RegionVo();
				targetVo.setProvince(fee.getTargetProvince());
				targetVo.setCity(fee.getTargetcity());
				targetVo.setDistrict(fee.getTargetdistrict());
				targetVo.setLineNo(line);
				targetList.add(targetVo);
			}
		}
		for (int i = 0; i < feeList.size(); i++) {
			FeesReceiveDeliverEntity fee = feeList.get(i);
			lineNo = lineNo+1;
				
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			String warehouseName= fee.getWarehouseName();
			if(StringUtils.isNotBlank(warehouseName)){
				if(wareHouseMap.containsKey(warehouseName) && StringUtils.isNotBlank(wareHouseMap.get(warehouseName))){
					logger.info(String.format("从仓库缓存Map中取数据[%s]", warehouseName));
					//从缓存wareHouseMap中直接取
					fee.setWarehouseCode(wareHouseMap.get(warehouseName));
				}else{
					setMessage(infoList, lineNo,"[仓库]没有在仓库表中维护!");
				}
			}
			String endWHName= fee.getTargetwarehouse();
			if(StringUtils.isNotBlank(endWHName)){
				if(wareHouseMap.containsKey(endWHName) && StringUtils.isNotBlank(wareHouseMap.get(endWHName))){
					logger.info(String.format("从仓库缓存Map中取数据[%s]", endWHName));
					//从缓存wareHouseMap中直接取
					fee.setTargetwarehouse(wareHouseMap.get(endWHName));
				}else{
					setMessage(infoList, lineNo,"[目的仓库]没有在仓库表中维护!");
				}
			}
			
			//判断商家在表中是否维护
			String custName= fee.getCustomerName();
			if(StringUtils.isNotBlank(custName)){
				if(customerMap.containsKey(custName) && StringUtils.isNotBlank(customerMap.get(custName))){
					logger.info(String.format("从商家缓存Map中取数据[%s]", custName));
					//从缓存wareHouseMap中直接取
					fee.setCustomerid(customerMap.get(custName));
				}else{
					setMessage(infoList, lineNo,"[商家]没有在商家表中维护!");
				}
			}
			//产品类型
			String subjectName = fee.getSubjectCode();//FeesReceiveDeliverEntity用subjectCode接收的Name
			if(StringUtils.isNotBlank(subjectName)){
				if(productTypeMap.containsKey(subjectName) && StringUtils.isNotBlank(productTypeMap.get(subjectName))){
					logger.info(String.format("从产品类型缓存Map中取数据[%s]", subjectName));
					//从缓存productTypeMap中直接取
					fee.setSubjectCode(productTypeMap.get(subjectName));
				}else{
					setMessage(infoList, lineNo,"[产品类型]没有在数据字典中维护!");
				}
			}
			fee.setCostType("TRANSPORT_FEE");//运输类型(不是增值类型ADD_FEE)	// 费用类型
			//===================================起始地址 校验======================================================================
			RegionEncapVo regionEncapVo = addressService.matchStandardByAlias(regionList);
			//获取此时的正确信息和报错信息
			List<ErrorMsgVo> errorMsgList = regionEncapVo.getErrorMsgList();
			List<RegionVo> correctRegionList = regionEncapVo.getRegionList();
			
			//如匹配到到名称则将code设值
			if(errorMsgList.size()<=0){
					for(RegionVo region : correctRegionList){
						if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(fee.getOriginProvince()) && region.getProvince().equalsIgnoreCase(fee.getOriginProvince())){
							fee.setOriginProvince(region.getProvincecode());
							if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(fee.getOriginatingcity()) && region.getCity().equalsIgnoreCase(fee.getOriginatingcity())){
								fee.setOriginatingcity(region.getCitycode());
								if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(fee.getOriginatingdistrict()) && region.getDistrict().equalsIgnoreCase(fee.getOriginatingdistrict())){
									fee.setOriginatingdistrict(region.getDistrictcode());	
									break;
								}
							}
						}
					}
			}else{
				for(ErrorMsgVo error:errorMsgList){
					setMessage(infoList, error.getLineNo(),error.getErrorMsg());
				}
				
			}
			//===================================目的地址 校验======================================================================
			RegionEncapVo targetEncapVo = addressService.matchStandardByAlias(targetList);
			//获取此时的正确信息和报错信息
			List<ErrorMsgVo> errorTargetList = targetEncapVo.getErrorMsgList();
			List<RegionVo> correctTargetList = targetEncapVo.getRegionList();
			
			//如匹配到到名称则将code设值
			if(errorTargetList.size()<=0){
					for(RegionVo region : correctTargetList){
						if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(fee.getTargetProvince()) && region.getProvince().equalsIgnoreCase(fee.getTargetProvince())){
							fee.setTargetProvince(region.getProvincecode());
							if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(fee.getTargetcity()) && region.getCity().equalsIgnoreCase(fee.getTargetcity())){
								fee.setTargetcity(region.getCitycode());
								if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(fee.getTargetdistrict()) && region.getDistrict().equalsIgnoreCase(fee.getTargetdistrict())){
									fee.setTargetdistrict(region.getDistrictcode());	
									break;
								}
							}
						}
					}
			}else{
				for(ErrorMsgVo error : errorTargetList){
					setMessage(infoList, error.getLineNo(), error.getErrorMsg());
				}
				
			}
			//=================================================================================================================
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, feeList); // 无基本错误信息
			}

			DecimalFormat decimalFormat=new DecimalFormat("0");
			double addNum = ((double)(i+1)/feeList.size())*400;
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300+Integer.valueOf(decimalFormat.format(addNum)));
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
		/*List<SystemCodeEntity> sList=systemCodeService.findEnumList("ts_base_subject");
		List<SystemCodeEntity> mList=systemCodeService.findEnumList("ts_value_add_subject");
		//List<BmsSubjectInfoEntity> subjectList=bmsSubjectInfoService.queryAll("");
		for(SystemCodeEntity en:sList){
			mapValue.put(en.getCodeName(),en.getCode());
		}
		for(SystemCodeEntity en:mList){
			mapValue.put(en.getCodeName(),en.getCode());
		}*/
		mapValue=bmsGroupSubjectService.getImportSubject("receive_transport_subject");
		return mapValue;
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
	
	public static boolean isNumer(String str){  
		  try{  
			   Double.valueOf(str);  
			   return true;  
		  }catch(NumberFormatException e){  
			  return false;  
		  }  
		}
}
