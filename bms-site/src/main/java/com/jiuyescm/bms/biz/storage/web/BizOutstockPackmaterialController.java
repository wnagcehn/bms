package com.jiuyescm.bms.biz.storage.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.excel.POIUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;


@Controller("bizOutstockPackmaterialController")
public class BizOutstockPackmaterialController {
	private static final Logger logger = LoggerFactory.getLogger(BizOutstockPackmaterialController.class.getName());
	
	@Autowired
	private IBizOutstockPackmaterialService service;
	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Resource
	private IPriceContractService contractService;
	@Autowired 
	private ICustomerService customerService;
	@Autowired 
	private IWarehouseService warehouseService;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	
	@Autowired
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private Lock lock;
	
	@Resource 
	private IBmsProductsMaterialService bmsProductsMaterialService;
	@Autowired 
	private IBmsCalcuTaskService bmsCalcuTaskService;

	
	@DataProvider
	public void query(Page<BizOutstockPackmaterialEntity> page, Map<String, Object> param){
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		if (param.get("startTime") == null) {
			throw new BizException("创建时间不能为空！");
		}
		if (param.get("endTime") == null) {
			throw new BizException("结束时间不能为空！");
		}
		if("ALL".equals(param.get("isCalculated"))){
			param.put("isCalculated", null);
		}
		PageInfo<BizOutstockPackmaterialEntity> pageInfo = service.query(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public @ResponseBody Object update(BizOutstockPackmaterialEntity entity){
		ReturnData result = new ReturnData();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		String operatorName=JAppContext.currentUserName();
		if (null == entity) {
			result.setCode("fail");
			result.setData("请选择要调整的记录");
		}
		//是否有费用编号
		if (StringUtils.isNotBlank(entity.getFeesNo())) {
			//判断是否生成费用，判断费用的状态是否为未过账
			Map<String, Object> condition = new HashMap<String,Object>();
			condition.put("feesNo", entity.getFeesNo());
			PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageService.query(condition, 0, Integer.MAX_VALUE);
			if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
				FeesReceiveStorageEntity feesReceiveStorageEntity = pageInfo.getList().get(0);
				//获取此时的费用状态
				String status = feesReceiveStorageEntity.getStatus();
				if("1".equals(status)){
					result.setCode("fail");
					result.setData("该费用已过账，无法调整数量");
					return result;
				}
			}
		}
		
		//更新
		BizOutstockPackmaterialEntity updateEntity = new BizOutstockPackmaterialEntity();
		updateEntity.setId(entity.getId());
		updateEntity.setAdjustNum(entity.getAdjustNum());
		updateEntity.setLastModifier(operatorName);
		updateEntity.setLastModifyTime(operatorTime);
		updateEntity.setFeesNo(entity.getFeesNo());
		int updateNum = service.update(updateEntity);
		if(updateNum > 0){
			result.setCode("SUCCESS");
			BmsCalcuTaskVo vo=new BmsCalcuTaskVo();
			vo.setCustomerId(entity.getCustomerId());
			vo.setSubjectCode("wh_material_use");
			String creMonth = new SimpleDateFormat("yyyyMM").format(entity.getCreateTime());
			vo.setCreMonth(Integer.valueOf(creMonth));
			vo.setCrePerson(JAppContext.currentUserName());
			vo.setCrePersonId(JAppContext.currentUserID());
			try {
				bmsCalcuTaskService.sendTask(vo);
				logger.info("mq发送成功,商家id:{0},年月:{1},科目id:{2}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
			} catch (Exception e) {
				logger.error("mq发送失败:", e);
			}	
		}else{
			result.setCode("fail");
			result.setData("更新失败");
		}
		return result;
	}
	
	@DataResolver
	public @ResponseBody Object reCount(List<BizOutstockPackmaterialEntity> list){
        
		ReturnData result = new ReturnData();
		
		List<String>  arr = new  ArrayList<String>();
		CalculateVo calcuVo = null;
		for (BizOutstockPackmaterialEntity entity : list) {
			
			Double initNum = entity.getNum();
			
			calcuVo = new CalculateVo();
		
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerid", entity.getCustomerId());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
			
			List<PriceContractInfoEntity> contractEntity = contractService.queryContract(aCondition);
			
			entity.setNum((initNum==null?0.0:initNum)+(entity.getAdjustNum()==null?0.0:entity.getAdjustNum()));
			
			calcuVo.setBizTypeCode("STORAGE");
			calcuVo.setSubjectId("wh_material_use");
			calcuVo.setContractCode(contractEntity.get(0).getContractCode());
			calcuVo.setObj(entity);
			if(calcuVo.getSuccess() && calcuVo.getPrice()!=null){
				
				try {
					FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
					storageFeeEntity.setCreator(JAppContext.currentUserName());
					//storageFeeEntity.setCreateTime(JAppContext.currentTimestamp());
					//费用表的创建时间应为业务表的创建时间
					storageFeeEntity.setCreateTime(entity.getCreateTime());
					storageFeeEntity.setOperateTime(entity.getCreateTime());
					storageFeeEntity.setCustomerId(entity.getCustomerId());		//商家ID
					storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
					storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
					storageFeeEntity.setWarehouseName("");	//仓库名称
					storageFeeEntity.setCostType("FEE_TYPE_MATERIAL");			//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
					storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());		//费用科目
					storageFeeEntity.setProductType("");							//商品类型
					storageFeeEntity.setQuantity((new Double(entity.getNum())).intValue());//商品数量
					storageFeeEntity.setStatus("0");								//状态
					storageFeeEntity.setOrderNo(entity.getOutstockNo());
					//根据测试的建议 吧耗材编码设置成商品编号和商品名称 zhangzw
					storageFeeEntity.setProductNo(entity.getConsumerMaterialCode());
					storageFeeEntity.setProductName(entity.getConsumerMaterialName());
					
					storageFeeEntity.setCost(calcuVo.getPrice());	//入仓金额
					storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());
					storageFeeEntity.setRuleNo(calcuVo.getRuleno());
					storageFeeEntity.setUnitPrice(calcuVo.getUnitPrice());//生成单价
					storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());
					storageFeeEntity.setIsCalculated("1");
					storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
					storageFeeEntity.setBizId(String.valueOf(entity.getId()));//业务数据主键
					
					boolean isInsert = StringUtils.isEmpty(entity.getFeesNo())?true:false; //true-新增  false-更新
					String feesNo =StringUtils.isEmpty(entity.getFeesNo())?
							sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000")
							:entity.getFeesNo();
					storageFeeEntity.setFeesNo(feesNo);//费用编号
					entity.setFeesNo(feesNo);
					
					if(isInsert){
						feesReceiveStorageService.save(storageFeeEntity);
					}else{
						feesReceiveStorageService.update(storageFeeEntity);
					}
					
					entity.setFeesNo(feesNo);
					entity.setNum(initNum);//恢复到以前
					entity.setIsCalculated("1");
					service.update(entity);
				} catch (Exception e) {
					e.printStackTrace();
					arr.add(entity.getConsumerMaterialCode());
					logger.error(e.getMessage());
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
					bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
					bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
					bmsErrorLogInfoEntity.setErrorMsg(e.toString());
					bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			}else{
				 arr.add(entity.getConsumerMaterialCode());
			}
			calcuVo = null;
		}
		
		if(arr.size()>0)
		{
			result.setCode("fail");
			StringBuffer buf = new StringBuffer();
			for(int i=0;i<arr.size();i++)
			{
				buf.append(arr.get(i));
				if(i!=arr.size()-1){
					buf.append(",");
				}
			}	
			if(arr.size()==list.size()){
				result.setData("全部未更新成功！");
			}else{
				result.setData("未更新成功的出库单号是："+buf.toString());
			}
					
		}else{
			result.setCode("SUCCESS");
		}
		return result;
	}
	
	/**
	 * 导入模板下载
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/out_stock_pack_material_template.xlsx");
		return new DownloadFile("耗材出库导入模板.xlsx", is);
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
	
	
	@FileResolver
	public Map<String, Object> importOutstockPackmaterilNew(UploadFile file, Map<String, Object> parameter) throws Exception{
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		String[] str = {"出库日期","仓库","商家","出库单号","运单号"};
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 检查导入模板是否正确
		boolean isTemplate = ExportUtil.checkTitle(file,str);
		if (!isTemplate) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		//start
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String,Object> materialMap = getBz();
		
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername(), customer.getCustomerid());
				}
			}
		}
		//end
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
					
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		
		if(xssfSheet==null)
			return null;
		
		List<BizOutstockPackmaterialEntity> dataList = new ArrayList<BizOutstockPackmaterialEntity>();
		
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
		
		if((cols-5)%2!=0){//如果列数不对则 返回
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("表头信息列数不对！");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		int col = (cols-5)/2;
				  
		for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++){
			
			XSSFRow xssfRow = xssfSheet.getRow(rowNum); 
			
			if(null==xssfRow)
				break;
			
			int initCol = 5;
			
			for(int i = 0;i<col;i++){
				  
				  BizOutstockPackmaterialEntity entity = new BizOutstockPackmaterialEntity();
				  entity.setRowExcelNo(rowNum+1);
				  XSSFCell xssfCell0 = xssfRow.getCell(initCol);
				  XSSFCell xssfCell1 = xssfRow.getCell(initCol+1);
				  
				  String materialCode = getValue(xssfCell0, evaluator);
				  
				  if(StringUtils.isNotEmpty(materialCode)&&StringUtils.isNotEmpty(materialCode.trim()))
				  {
					  String num = getValue(xssfCell1, evaluator);
					  
					  if(!materialMap.containsKey(materialCode.trim()))
					  {
						    int lieshu = initCol+1;
							setMessage(infoList, rowNum+1,"第"+lieshu+"列耗材编号必须为标准编码！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  
					  if(StringUtils.isEmpty(num)||StringUtils.isEmpty(num.trim()))
					  {
						    int lieshu = initCol+1;
							setMessage(infoList, rowNum+1,"第"+lieshu+"数量为空！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  
					  try {
						double tmepD = Double.valueOf(num);
						if(tmepD<=0){
							int lieshu = initCol+1;
							setMessage(infoList, rowNum+1,"第"+lieshu+"列数量为0或者小于0！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
							
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						int lieshu = initCol+2;
						setMessage(infoList, rowNum+1,"第"+lieshu+"列数量为非数字");
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						//写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
						bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
						bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
						bmsErrorLogInfoEntity.setErrorMsg(e1.toString());
						bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
						return map;
					}
					  
					  entity.setConsumerMaterialCode(materialCode.trim());
					  try {
							if(materialCode.endsWith("-GB"))
							{
								entity.setWeight(Double.valueOf(num));
							}else{
								entity.setNum(Double.valueOf(num));
								entity.setAdjustNum(Double.valueOf(num));
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							setMessage(infoList, rowNum+1,"数量为非数字");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							//写入日志
							BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
							bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
							bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
							bmsErrorLogInfoEntity.setErrorMsg(e.toString());
							bmsErrorLogInfoEntity.setIdentify("数量为非数字");
							bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
							bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
							return map;
						}
					  
					  XSSFCell cell0 = xssfRow.getCell(0);
					  String ckrq = getValue(cell0, evaluator);
					  
					  XSSFCell cell1 = xssfRow.getCell(1);
					  String ck = getValue(cell1, evaluator);//仓库
					  
					  XSSFCell cell2 = xssfRow.getCell(2);
					  String sj = getValue(cell2, evaluator);//商家
					  
					  XSSFCell cell3 = xssfRow.getCell(3);
					  String ckdh = getValue(cell3, evaluator);//出库单号
					  
					  XSSFCell cell4 = xssfRow.getCell(4);
					  String ydh = getValue(cell4, evaluator);
					  
					  if(StringUtils.isEmpty(ckrq)||StringUtils.isEmpty(ckrq.trim())){
						  setMessage(infoList, rowNum+1,"出库日期为空！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  
					  if(StringUtils.isEmpty(ck)||StringUtils.isEmpty(ck.trim())){
						  setMessage(infoList, rowNum+1,"仓库为空！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  
					  if(StringUtils.isEmpty(sj)||StringUtils.isEmpty(sj.trim())){
						    setMessage(infoList, rowNum+1,"商家为空！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  
					  boolean isYdh = StringUtils.isEmpty(ydh)||StringUtils.isEmpty(ydh.trim());
					  
					  if(isYdh){
						  setMessage(infoList, rowNum+1,"运单号不能都为空！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  
					  sj = replaceBlank(sj);
					  ck = replaceBlank(ck);
					  
					  if(wareHouseMap.get(ck)==null){
						    setMessage(infoList, rowNum+1,"仓库名["+ck+"]没有在仓库表中维护!");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  if(customerMap.get(sj)==null){
						    setMessage(infoList, rowNum+1,"商家["+sj+"]没有在表中维护!");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
					  }
					  entity.setOutstockNo(ckdh);
					  entity.setWaybillNo(ydh);
					  entity.setWarehouseCode(wareHouseMap.get(ck).trim());
					  entity.setWarehouseName(ck);
					  
					  PubMaterialInfoVo pubMaterialInfoVo = (PubMaterialInfoVo)materialMap.get(materialCode.trim());
					  entity.setConsumerMaterialName(pubMaterialInfoVo.getMaterialName());
					  entity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
					  					+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
	
					  
					  entity.setCustomerId(customerMap.get(sj).trim());
					  entity.setCustomerName(sj);
					  
					  entity.setDelFlag("0");// 设置为未作废
					  entity.setCreator(JAppContext.currentUserName());
					  entity.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
					  Date writeDate = new Date();
					  entity.setWriteTime(new Timestamp(writeDate.getTime()));
					  try {
						entity.setCreateTime(Timestamp.valueOf(ckrq));
					  } catch (Exception e) {
						  e.printStackTrace();
						  try {
							  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
							  Date date = DateUtils.parseDate(ckrq, dataPatterns);
							  entity.setCreateTime(new Timestamp(date.getTime()));
						   } catch (Exception e1) {
							    setMessage(infoList, rowNum+1,"日期格式不对,无法解析!");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								//写入日志
								BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "日期格式不对,无法解析", e.toString());
								bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
								e1.printStackTrace();
								return map;
						   }
						  
						//写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					  }
					  
					  dataList.add(entity);  
				  }
					 
				  initCol = initCol + 2;
				  
			  }
			
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		
		int insertNum = 0;
		
		String message = null;
		try {
			insertNum = service.saveList(dataList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			try {
				
				int  f = e.getMessage().lastIndexOf("Duplicate entry");
				message = e.getMessage().substring(f);
				message = message.replace("Duplicate entry", "");
				message = message.replace("for key 'waybill_no'", "");
				message = message.trim();
				message = message.substring(1, message.length()-1);
				String fu[] = message.split("-");
				message = "重复的运单号和耗材编码的组合"+"["+fu[0]+"]"+","+"["+fu[1]+"-"+fu[2]+"]";
			} catch (Exception e1) {
				logger.error(e1.getMessage());
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e1.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
			logger.error(e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		
		if (insertNum <= 0) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(message!=null){
				errorVo.setMsg(message);
			}else{
				errorVo.setMsg("耗材出库明细写入失败!");
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}else{
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
		}
		
	}
	
	 /**  
     * @获取Excel中某个单元格的值 
     * @param cell      EXCLE单元格对象 
     * @param evaluator EXCLE单元格公式 
     * @return          单元格内容 
     */  
    public static String getValue(Cell cell,FormulaEvaluator evaluator) {   
          
        String value = "";  
        if(cell==null)
        	return value;
        switch (cell.getCellType()) {  
            case HSSFCell.CELL_TYPE_NUMERIC:                        //数值型  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {       //如果是时间类型  
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                    value = format.format(cell.getDateCellValue());  
                } else { 
                	 //纯数字  
                    value = String.valueOf(cell.getNumericCellValue());  
                }  
                break;  
            case HSSFCell.CELL_TYPE_STRING:                         //字符串型  
                value = cell.getStringCellValue();  
                break;  
            case HSSFCell.CELL_TYPE_BOOLEAN:                        //布尔  
                value = " " + cell.getBooleanCellValue();  
                break;  
            case HSSFCell.CELL_TYPE_BLANK:                          //空值  
                value = "";  
                break;  
            case HSSFCell.CELL_TYPE_ERROR:                          //故障  
                value = "";  
                break;  
            case HSSFCell.CELL_TYPE_FORMULA:                        //公式型  
                try {  
                    CellValue cellValue;  
                    cellValue = evaluator.evaluate(cell);  
                    switch (cellValue.getCellType()) {              //判断公式类型  
                        case Cell.CELL_TYPE_BOOLEAN:  
                            value  = String.valueOf(cellValue.getBooleanValue());  
                            break;  
                        case Cell.CELL_TYPE_NUMERIC:  
                            // 处理日期    
                            if (DateUtil.isCellDateFormatted(cell)) {    
                               SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");    
                               Date date = cell.getDateCellValue();    
                               value = format.format(date);  
                            } else {    
                               value  = String.valueOf(cellValue.getNumberValue());  
                            }  
                            break;  
                        case Cell.CELL_TYPE_STRING:  
                            value  = cellValue.getStringValue();  
                            break;  
                        case Cell.CELL_TYPE_BLANK:  
                            value = "";  
                            break;  
                        case Cell.CELL_TYPE_ERROR:  
                            value = "";  
                            break;  
                        case Cell.CELL_TYPE_FORMULA:  
                            value = "";  
                            break;  
                    }  
                } catch (Exception e) {  
                    value = cell.getStringCellValue().toString();  
                    cell.getCellFormula(); 
                    e.printStackTrace();
                }  
                break;  
            default:  
                value = cell.getStringCellValue().toString();  
                break;  
        }  
        return value;  
    }
    
    
	
	Map<String,Object>  getBz(){
		
		Map<String,Object> mapValue = new LinkedHashMap<String, Object>();
		
		Map<String, Object> aCondition=new HashMap<String, Object>();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(aCondition);
		for (PubMaterialInfoVo pubMaterialInfoVo : tmscodels) {
			
			mapValue.put(pubMaterialInfoVo.getBarcode(),pubMaterialInfoVo);
		}
		
		return mapValue;
	}
	
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	@DataProvider
	public void queryWarehouseGroupCount(Page<BizOutstockPackmaterialEntity> page, Map<String, Object> param){
		if("ALL".equals(param.get("isCalculated"))){
			param.put("isCalculated", null);
		}
		PageInfo<BizOutstockPackmaterialEntity> pageInfo = service.queryWarehouseGroupCount(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public void queryErrorCal(Page<BizOutstockPackmaterialEntity> page, Map<String, Object> param){
		if("".equals(param.get("isCalculated")) || param.get("isCalculated") == null){
			String[] arr = new String[]{"2","3","4"};
			param.put("isCalculateds", arr);
		}
		PageInfo<BizOutstockPackmaterialEntity> pageInfo = service.queryErrorCal(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public void queryPriceGroupCount(Page<BizOutstockPackmaterialEntity> page, Map<String, Object> param){
		if("ALL".equals(param.get("isCalculated"))){
			param.put("isCalculated", null);
		}
		PageInfo<BizOutstockPackmaterialEntity> pageInfo = service.queryPriceGroupCount(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = service.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(service.reCalculate(param) <= 0){
			return "重算异常";
		}else{

			//对这些费用按照商家、科目、时间排序
			List<BmsCalcuTaskVo> calList=bmsCalcuTaskService.queryMaterialTask(param);
			for (BmsCalcuTaskVo vo : calList) {
				vo.setCrePerson(JAppContext.currentUserName());
				vo.setCrePersonId(JAppContext.currentUserID());
				try {
					bmsCalcuTaskService.sendTask(vo);
					logger.info("mq发送成功,商家id:{0},年月:{1},科目id:{2}", vo.getCustomerId(),vo.getCreMonth(),vo.getSubjectCode());
				} catch (Exception e) {
					logger.error("mq发送失败:", e);
				}	
			}
		}
		return "操作成功! 正在重算...";
	}
	/**
	 * 成本重算
	 * @param param
	 * @return
	 */
	@DataResolver
	public Map<String,Object> reCalculateCost(Map<String, Object> param){
		Map<String,Object> map=Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		//验证是否有生成账单的数据
		if(!validate(param,infoList)){
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		try{
			int k=updateCostIsCalculated(param,"99");
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "重算成功,"+k+"条记录待处理");
			return map;
		}catch(Exception e){
			infoList.add(new ErrorMessageVo(1,"程序异常:"+e.getMessage()));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
	}
	
	private int updateCostIsCalculated(Map<String, Object> param, String isCalculated) {
		int k=service.updateCostIsCalculated(param,isCalculated);
		return k;
	}

	private boolean validate(Map<String, Object> param,List<ErrorMessageVo> infoList) {
		List<String> list=service.queryCosthasBill(param);
		if(list!=null&&list.size()>0){
			int index=1;
			for(String s:list){
				index++;
				infoList.add(new ErrorMessageVo(index,"运单号,耗材编码 【"+s+"】已生成账单无法重算"));
			}
			return false;
		}
		return true;
	}

	@DataResolver
	public String deleteBatch(List<BizOutstockPackmaterialEntity> dataList) {
		List<BizOutstockPackmaterialEntity> data = new ArrayList<BizOutstockPackmaterialEntity>();
		PageInfo<FeesReceiveStorageEntity> pageInfo = null;
		FeesReceiveStorageEntity feesReceiveStorageEntity = null;
		List<String> waybillNoList=new ArrayList<String>();
		for(BizOutstockPackmaterialEntity entity:dataList){
			String feesNo = entity.getFeesNo();
			if(StringUtils.isNotBlank(entity.getWaybillNo())&&!waybillNoList.contains(entity.getWaybillNo())){
				waybillNoList.add(entity.getWaybillNo());
			}
			if(StringUtils.isNotBlank(feesNo))
			{
				Map<String,Object> param = new  HashMap<String,Object>();
				param.put("feesNo", feesNo);
			    pageInfo = feesReceiveStorageService.query(param, 0, Integer.MAX_VALUE);
				if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) 
				{
					feesReceiveStorageEntity=pageInfo.getList().get(0);
					//获取此时的费用状态
					String status = String.valueOf(feesReceiveStorageEntity.getStatus());
					if("1".equals(status)){
						data.add(entity);
					}
				}
			}
		}
		
		if(data.size()>0){
			String str="";
			for(int i=0;i<data.size();i++){
				if(i==data.size()-1){
					str+=data.get(i).getWaybillNo();
					continue;
				}
				str+=data.get(i).getWaybillNo()+",";
			}		
			return str;
		}
		
		
		dataList.removeAll(data);
		
		//int i = service.deleteList(dataList);
		int i=service.deleteAllByWayBillNo(waybillNoList);
		bmsProductsMaterialService.deleteMarkMaterialByWaybillNo(waybillNoList);//删除打标记录
		if(i>0)
		{
			for(BizOutstockPackmaterialEntity entity:dataList)
			{
				if(StringUtils.isNotEmpty(entity.getFeesNo()))
				   feesReceiveStorageService.deleteEntity(entity.getFeesNo());
			}
		}
		
		return "sucess";
	}
	
	@DataProvider
	public int queryDelete(Page<BizOutstockPackmaterialEntity> page,Map<String, Object> param) {
		List<BizOutstockPackmaterialEntity> list = service.queryList(param);
		service.deleteFees(param);
		int i = service.deleteList(list);
		return i;
	}
	
	@DataProvider
	public void queryTj(Page<Map<String,String>> page, Map<String, Object> param) {
		PageInfo<Map<String,String>> pageInfo = null;
		if("1".equals(param.get("isCalculatedF"))){
			 pageInfo = service.queryCustomeridF(param, page.getPageNo(), page.getPageSize());
		}else{//查询已导入
			pageInfo = service.queryByMonth(param, page.getPageNo(), page.getPageSize());
		}
		
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	private int getErrorCount(){
		int c=0;
		try{
			SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_ERROR_COUNT");
			if(systemCodeEntity == null){
				return 10;
			}
			c=Integer.parseInt(systemCodeEntity.getExtattr1());
		}catch(Exception e){
			e.printStackTrace();
			c=10;
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return c;
	}
	@FileResolver
	public Map<String, Object> importWmsOutstockPackmaterilTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		String lockString=Tools.getMd5("BMS_QUO_IMPORT_OUTSTOCK_PACKMATERIL"+JAppContext.currentUserID());
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=new HashMap<String, Object>();
				try {
					map=importWmsOutstockPackmateril(file,parameter);
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
				errorVo.setMsg("耗材出库明细导入功能已被其他用户占用，请稍后重试；");
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

	public Map<String, Object> importWmsOutstockPackmateril(UploadFile file, Map<String, Object> parameter) throws Exception{
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		String[] str = {"出库日期","仓库","商家","出库单号","运单号"};
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 检查导入模板是否正确
		boolean isTemplate = ExportUtil.checkTitle(file,str);
		if (!isTemplate) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		int errorCount=getErrorCount();
		//start
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String,Object> materialMap = getBz();
		
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}
		//end
		
		List<BizOutstockPackmaterialEntity> dataList = new ArrayList<BizOutstockPackmaterialEntity>();
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		
		if(file.getFileName().contains("xlsx"))
		{
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
			
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			
			if(xssfSheet==null)
				return null;
			
			XSSFRow tempRow = xssfSheet.getRow(0);
			
			int cols = tempRow.getPhysicalNumberOfCells();
			
			boolean isWms = false;//是否是wms导出的文件
			
			for(int m=0;m<cols;m++)
			{
				if("其他名称".equals(tempRow.getCell(m).getStringCellValue()))
					isWms = true;
			}
			
			//如果列数不对则 返回
			if((cols-5)%3!=0&&(cols-5)%2!=0)
			{
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("表头信息列数不对！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			int col = 0;
			
			if(isWms)
			{
				col = (cols-5)/3;	
			}else{
				col = (cols-5)/2;
			}
			
			for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++){
				
				XSSFRow xssfRow = xssfSheet.getRow(rowNum); 
				String errorMes="";
				if(null==xssfRow)
				{
					if(rowNum!=xssfSheet.getLastRowNum()){//测试喜欢中间插入个空行 没有办法 做空行判断
						 errorMes+="空行！";
					}else{
						break;
					}
					
				}
				
				int initCol = 5;
				for(int i = 0;i<col;i++)
				{
					  
					  BizOutstockPackmaterialEntity entity = new BizOutstockPackmaterialEntity();
					  entity.setRowExcelNo(rowNum+1);
					  
					  XSSFCell xssfCell0 = xssfRow.getCell(initCol);
					  XSSFCell xssfCell1 = xssfRow.getCell(initCol+1);
					  
					  String materialCode = getValue(xssfCell0, evaluator);
					  
					  if(StringUtils.isNotEmpty(materialCode)&&StringUtils.isNotEmpty(materialCode.trim()))
					  {
						  String num = getValue(xssfCell1, evaluator);
						  
						  if(!materialMap.containsKey(materialCode.trim()))
						  {
							    int lieshu = initCol+1;
							    errorMes+="第"+lieshu+"列耗材编号必须为标准编码！";
								//setMessage(infoList, rowNum+1,"第"+lieshu+"列耗材编号必须为标准编码！");
						  }
						  
						  if(StringUtils.isEmpty(num)||StringUtils.isEmpty(num.trim()))
						  {
							    int lieshu = initCol+1;
							    errorMes+="第"+lieshu+"数量为空！";
							    /*
								setMessage(infoList, rowNum+1,"第"+lieshu+"数量为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
							
						  }
						  
						  try {
							double tmepD = Double.valueOf(num);
							if(tmepD<=0){
								int lieshu = initCol+1;
								errorMes+="第"+lieshu+"列数量为0或者小于0！";
								/*
								setMessage(infoList, rowNum+1,"第"+lieshu+"列数量为0或者小于0！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;
								*/
							}
						} catch (Exception e1) {
							int lieshu = initCol+2;
							errorMes+="第"+lieshu+"列数量为非数字";
							/*
							setMessage(infoList, rowNum+1,"第"+lieshu+"列数量为非数字");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;*/
							//写入日志
							BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "第"+lieshu+"列数量为非数字", e1.toString());
							bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
							e1.printStackTrace();
						}
						  
						  entity.setConsumerMaterialCode(materialCode.trim());
						  try {
								if(materialCode.endsWith("-GB"))
								{
									entity.setWeight(Double.valueOf(num));
								}else{
									entity.setNum(Double.valueOf(num));
									entity.setAdjustNum(Double.valueOf(num));
								}
								
							} catch (Exception e) {
								e.printStackTrace();
								errorMes+="数量为非数字";
								/*
								setMessage(infoList, rowNum+1,"数量为非数字");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
								//写入日志
								BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "数量为非数字", e.toString());
								bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
							}
						  
						  XSSFCell cell0 = xssfRow.getCell(0);
						  String ckrq = getValue(cell0, evaluator);
						  
						  XSSFCell cell1 = xssfRow.getCell(1);
						  String ck = getValue(cell1, evaluator);//仓库
						  
						  XSSFCell cell2 = xssfRow.getCell(2);
						  String sj = getValue(cell2, evaluator);//商家
						  
						  XSSFCell cell3 = xssfRow.getCell(3);
						  String ckdh = getValue(cell3, evaluator);//出库单号
						  
						  XSSFCell cell4 = xssfRow.getCell(4);
						  String ydh = getValue(cell4, evaluator);
						  
						  if(StringUtils.isEmpty(ckrq)||StringUtils.isEmpty(ckrq.trim())){
							  errorMes+="出库日期为空！";
							  /*
							  setMessage(infoList, rowNum+1,"出库日期为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  if(StringUtils.isEmpty(ck)||StringUtils.isEmpty(ck.trim())){
							  errorMes+="仓库为空！";
							  /*
							  setMessage(infoList, rowNum+1,"仓库为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  if(StringUtils.isEmpty(sj)||StringUtils.isEmpty(sj.trim())){
							  errorMes+="商家为空！";
							  /*
							    setMessage(infoList, rowNum+1,"商家为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  boolean isYdh = StringUtils.isEmpty(ydh)||StringUtils.isEmpty(ydh.trim());
						  
						  if(isYdh){
							  errorMes+="运单号不能为空！";
							  /*
							  setMessage(infoList, rowNum+1,"运单号不能为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  sj = replaceBlank(sj);
						  ck = replaceBlank(ck);
						  
						  if(wareHouseMap.get(ck.trim())==null){
							  errorMes+="仓库名["+ck+"]没有在仓库表中维护!";
							  
							    setMessage(infoList, rowNum+1,"仓库名["+ck+"]没有在仓库表中维护!");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;
						  }
						  if(customerMap.get(sj.trim())==null){
							  errorMes+="商家["+sj+"]没有在表中维护!";
							  
							    setMessage(infoList, rowNum+1,"商家["+sj+"]没有在表中维护!");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;
						  }
						  entity.setOutstockNo(ckdh);
						  entity.setWaybillNo(ydh);
						  entity.setWarehouseCode(wareHouseMap.get(ck).trim());
						  entity.setWarehouseName(ck);
						 
						  PubMaterialInfoVo pubMaterialInfoVo = (PubMaterialInfoVo)materialMap.get(materialCode.trim());
						  if(pubMaterialInfoVo==null){
							  logger.info("为空");
						  }
						  entity.setConsumerMaterialName(pubMaterialInfoVo.getMaterialName());
						  entity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
						  					+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
		
						  
						  entity.setCustomerId(customerMap.get(sj).trim());
						  entity.setCustomerName(sj);
						  
						  entity.setDelFlag("0");// 设置为未作废
						  entity.setCreator(JAppContext.currentUserName());
						  entity.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
						  Date writeDate = new Date();
						  entity.setWriteTime(new Timestamp(writeDate.getTime()));
						  try {
							entity.setCreateTime(Timestamp.valueOf(ckrq));
						  } catch (Exception e) {
							  e.printStackTrace();
							  try {
								  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
								  Date date = DateUtils.parseDate(ckrq, dataPatterns);
								  entity.setCreateTime(new Timestamp(date.getTime()));
							   } catch (Exception e1) {
								   e1.printStackTrace();
								   errorMes+="日期格式不对,无法解析!";
								   /*
								    setMessage(infoList, rowNum+1,"日期格式不对,无法解析!");
									map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
									return map;*/
									//写入日志
									BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "日期格式不对,无法解析!", e.toString());
									bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
							   }
								//写入日志
								BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
								bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
						  }
						  
						  dataList.add(entity);  
					  }
						 
					if(isWms)
					{
						initCol = initCol + 3;
					}else{
						initCol = initCol + 2;
					}
					  
					  
				  }
				if(!StringUtils.isBlank(errorMes)){
					setMessage(infoList, rowNum+1,errorMes);
					if(infoList.size()>=errorCount){
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;
					}
				}
				
			}
		}else{
			
			HSSFWorkbook workbook = null;
			try {
				workbook = new HSSFWorkbook(file.getInputStream());
			
			} catch (Exception e) {
				e.printStackTrace();
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
			
			HSSFSheet xssfSheet=null;
			xssfSheet = workbook.getSheetAt(0);
			
			HSSFFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			
            int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
			
			if((cols-5)%3!=0){//如果列数不对则 返回
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("表头信息列数不对！");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			int col = (cols-5)/3;
	        
	        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++)
	        {
	        	 String errorMes="";
	        	 HSSFRow xssfRow=xssfSheet.getRow(rowNum);
	        	 
	        	 if(null==xssfRow)
				 {
					if(rowNum!=xssfSheet.getLastRowNum()){
						errorMes+="空行！";
						/*
						setMessage(infoList, rowNum+1,"空行！");
						map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return map;*/
					}else{
						break;
					}
				 }
	        	 
	        	 int initCol = 5;
	        	 
	        	 for(int i = 0;i<col;i++){
					  
					  BizOutstockPackmaterialEntity entity = new BizOutstockPackmaterialEntity();
					  entity.setRowExcelNo(rowNum+1);
					  HSSFCell xssfCell0 = xssfRow.getCell(initCol);
					  HSSFCell xssfCell1 = xssfRow.getCell(initCol+1);
					  
					  String materialCode = getValue(xssfCell0, evaluator);
					  
					  if(StringUtils.isNotEmpty(materialCode)&&StringUtils.isNotEmpty(materialCode.trim()))
					  {
						  String num = getValue(xssfCell1, evaluator);
						  
						  if(!materialMap.containsKey(materialCode.trim()))
						  {
							    int lieshu = initCol+1;
							    errorMes+="第"+lieshu+"列耗材编号必须为标准编码！";
							    /*
								setMessage(infoList, rowNum+1,"第"+lieshu+"列耗材编号必须为标准编码！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  if(StringUtils.isEmpty(num)||StringUtils.isEmpty(num.trim()))
						  {
							    int lieshu = initCol+1;
							    errorMes+="第"+lieshu+"数量为空！";
							    /*
								setMessage(infoList, rowNum+1,"第"+lieshu+"数量为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  try {
							double tmepD = Double.valueOf(num);
							if(tmepD<=0){
								int lieshu = initCol+1;
								errorMes+="第"+lieshu+"列数量为0或者小于0！";
								/*
								setMessage(infoList, rowNum+1,"第"+lieshu+"列数量为0或者小于0！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;
								*/
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							int lieshu = initCol+2;
							errorMes+="第"+lieshu+"列数量为非数字";
							/*
							setMessage(infoList, rowNum+1,"第"+lieshu+"列数量为非数字");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;*/
							//写入日志
							BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "第"+lieshu+"列数量为非数字", e1.toString());
							bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
						}
						  
						  entity.setConsumerMaterialCode(materialCode.trim());
						  try {
								if(materialCode.endsWith("-GB"))
								{
									entity.setWeight(Double.valueOf(num));
								}else{
									entity.setNum(Double.valueOf(num));
									entity.setAdjustNum(Double.valueOf(num));
								}
								
							} catch (Exception e) {
								errorMes+="数量为非数字";
								/*
								setMessage(infoList, rowNum+1,"数量为非数字");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
								e.printStackTrace();
								//写入日志
								BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "数量为非数字", e.toString());
								bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
							}
						  
						  HSSFCell cell0 = xssfRow.getCell(0);
						  String ckrq = getValue(cell0, evaluator);
						  
						  HSSFCell cell1 = xssfRow.getCell(1);
						  String ck = getValue(cell1, evaluator);//仓库
						  
						  HSSFCell cell2 = xssfRow.getCell(2);
						  String sj = getValue(cell2, evaluator);//商家
						  
						  HSSFCell cell3 = xssfRow.getCell(3);
						  String ckdh = getValue(cell3, evaluator);//出库单号
						  
						  HSSFCell cell4 = xssfRow.getCell(4);
						  String ydh = getValue(cell4, evaluator);
						  
						  if(StringUtils.isEmpty(ckrq)||StringUtils.isEmpty(ckrq.trim())){
							  errorMes+="出库日期为空！";
							  /*
							  setMessage(infoList, rowNum+1,"出库日期为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  if(StringUtils.isEmpty(ck)||StringUtils.isEmpty(ck.trim())){
							  errorMes+="仓库为空！";
							  /*
							  setMessage(infoList, rowNum+1,"仓库为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  if(StringUtils.isEmpty(sj)||StringUtils.isEmpty(sj.trim())){
							  errorMes+="商家为空！";
							  /*
							    setMessage(infoList, rowNum+1,"商家为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  boolean isYdh = StringUtils.isEmpty(ydh)||StringUtils.isEmpty(ydh.trim());
						  
						  if(isYdh){
							  errorMes+="运单号不能为空！";
							  /*
							  setMessage(infoList, rowNum+1,"运单号不能为空！");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;*/
						  }
						  
						  sj = replaceBlank(sj);
						  ck = replaceBlank(ck);
						  
						  if(wareHouseMap.get(ck.trim())==null){
							  errorMes+="仓库名["+ck+"]没有在仓库表中维护!";
							  
							    setMessage(infoList, rowNum+1,"仓库名["+ck+"]没有在仓库表中维护!");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;
						  }
						  if(customerMap.get(sj.trim())==null){
							  errorMes+="商家["+sj+"]没有在表中维护!";
							  
							    setMessage(infoList, rowNum+1,"商家["+sj+"]没有在表中维护!");
								map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
								return map;
						  }
						  entity.setOutstockNo(ckdh);
						  entity.setWaybillNo(ydh);
						  entity.setWarehouseCode(wareHouseMap.get(ck).trim());
						  entity.setWarehouseName(ck);
						  
						  PubMaterialInfoVo pubMaterialInfoVo = (PubMaterialInfoVo)materialMap.get(materialCode.trim());
						  entity.setConsumerMaterialName(pubMaterialInfoVo.getMaterialName());
						  entity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
						  					+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
		
						  
						  entity.setCustomerId(customerMap.get(sj).trim());
						  entity.setCustomerName(sj);
						  
						  entity.setDelFlag("0");// 设置为未作废
						  entity.setCreator(JAppContext.currentUserName());
						  entity.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
						  Date writeDate = new Date();
						  entity.setWriteTime(new Timestamp(writeDate.getTime()));
						  try {
							entity.setCreateTime(Timestamp.valueOf(ckrq));
						  } catch (Exception e) {
							  e.printStackTrace();
							  try {
								  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
								  Date date = DateUtils.parseDate(ckrq, dataPatterns);
								  entity.setCreateTime(new Timestamp(date.getTime()));
							   } catch (Exception e1) {
								   errorMes+="日期格式不对,无法解析!";
								   e1.printStackTrace();
								   /*
								    setMessage(infoList, rowNum+1,"日期格式不对,无法解析!");
									map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
									return map;*/
								 //写入日志
									BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), "日期格式不对,无法解析!", e1.toString());
									bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
							   }
						  }
						  
						  dataList.add(entity);  
					  }
						 
					  initCol = initCol + 3;
					  
				  }
	        	 if(!StringUtils.isBlank(errorMes)){
						setMessage(infoList, rowNum+1,errorMes);
						if(infoList.size()>=errorCount){
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
						}
				}
	        }
	       
		}
		/*if(dataList!=null&&dataList.size()>0){
			  Map<String,Object> mapCheck=checkPackMaterial(dataList,infoList,map,errorCount);
			  	if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {//验证数据重复
			  		map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);	
			  		return map;
			  	}	
		}*/
		
		//新增方法判断Excel中是否有重复项
		if(dataList!=null&&dataList.size()>0){
			  Map<String,Object> mapCheck=checkExcelPackMaterial(dataList,infoList,map);
			  	if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {//验证数据重复
			  		logger.info("长度"+infoList.size());
			  		map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);	
			  		return map;
			  	}	
		}
		
		
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		
		int insertNum = 0;
		
		String message = null;
		try {
			insertNum = service.saveList(dataList);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				int  f = e.getMessage().lastIndexOf("Duplicate entry");
				message = e.getMessage().substring(f);
				message = message.replace("Duplicate entry", "");
				message = message.replace("for key 'waybill_no'", "");
				message = message.trim();
				message = message.substring(1, message.length()-1);
				String fu[] = message.split("-");
				String me="";
				for(int i=1;i<fu.length-1;i++){
					if(i==fu.length-2){
						me+=fu[i];
					}else{
						me+=fu[i]+"-";
					}
					
					
				}
				
				message = "重复的运单号和耗材编码的组合"+"["+fu[0]+"]"+","+"["+me+"]";
				//message = "重复的运单号和耗材编码的组合"+"["+fu[0]+"]"+","+"["+fu[1]+"-"+fu[2]+"]";
			} catch (Exception e1) {
				 //写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e1.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				e1.printStackTrace();
			}
			logger.error(e.getMessage(),e);
		}
		
		if (insertNum <= 0) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(message!=null){
				errorVo.setMsg(message);
			}else{
				errorVo.setMsg("耗材出库明细写入失败!");
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}else{
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
		}
		
	}
	
	private Map<String, Object> checkExcelPackMaterial(List<BizOutstockPackmaterialEntity> dataList,List<ErrorMessageVo> infoList,Map<String, Object> map){
		//验证导入数据有重复
		List<String> dataKeyList=new ArrayList<String>();
		for(BizOutstockPackmaterialEntity entity:dataList){
			String dataKey=getMaterialDataKey(entity);
			if(!dataKeyList.contains(dataKey)){
				dataKeyList.add(dataKey);
			}else{//数据有重复
				setMessage(infoList, entity.getRowExcelNo(),"Excel中第"+entity.getRowExcelNo()+"行数据重复");
				if(infoList.size()>=1000){
					break;
				}
			}
		}
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
		}
		
		return map;
	}
	
	private Map<String, Object> checkPackMaterial(
			List<BizOutstockPackmaterialEntity> dataList,
			List<ErrorMessageVo> infoList, Map<String, Object> map,int errorCount) {
		 List<String> outstockNoList=new ArrayList<String>();
		 for(BizOutstockPackmaterialEntity entity:dataList){
			 if(!outstockNoList.contains(entity.getWaybillNo())){
				 outstockNoList.add(entity.getWaybillNo());//改成运单号 
			 }
		 }
		 //CommonComparePR<BizOutstockPackmaterialEntity> comparePR=new CommonComparePR<BizOutstockPackmaterialEntity>();
		 Map<String,Object> querymap=new HashMap<String,Object>();
		 querymap.put("outstockNoList", outstockNoList);
	     List<BizOutstockPackmaterialEntity> orgList=getOrgPackMaterialList(querymap);
	     Map<String,Object> mapCheck=compareData(orgList,dataList,errorCount);
		 return mapCheck;
	}
	private String getMaterialKey(BizOutstockPackmaterialEntity dataEntity){
		return objToString(dataEntity.getWaybillNo());//改成运单号
	}
	private String getMaterialDataKey(BizOutstockPackmaterialEntity dataEntity){
		String dataKey=objToString(dataEntity.getWaybillNo())+"&"+objToString(dataEntity.getConsumerMaterialCode()+"&"+objToString(dataEntity.getCustomerId()));//改成运单号
		return dataKey;
	}
	private String objToString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	private Map<String,Object> compareData(List<BizOutstockPackmaterialEntity> orgList,List<BizOutstockPackmaterialEntity> dataList,int errorCount){
		Map<String,Object> mapCompare=new HashMap<String,Object>();
		List<ErrorMessageVo> infoList=new ArrayList<ErrorMessageVo>();
		//验证导入数据有重复
		List<String> dataKeyList=new ArrayList<String>();
	    List<String> outstockNoList=new ArrayList<String>();
		int index=0;
		int lineNo=0;
		for(BizOutstockPackmaterialEntity entity:dataList){
			String key=getMaterialKey(entity);
			lineNo++;
			String dataKey=getMaterialDataKey(entity);
			if(!outstockNoList.contains(key)){//index 设置
				outstockNoList.add(key);
				index++;
			}
			if(!dataKeyList.contains(dataKey)){/*//excel数据无重复 验证与数据库对比
				dataKeyList.add(dataKey);
				boolean f=true;
				for(BizOutstockPackmaterialEntity orgEntity:orgList){
					String orgDataKey=getMaterialDataKey(orgEntity);
					if(orgDataKey.equals(dataKey)){
						f=false;
						break;
					}
				}
				if(!f){//与表中数据有重复
					setMessage(infoList, lineNo,"第"+index+"行,系统已存在【"+dataKey+"】耗材出库明细");
					if(infoList.size()>=errorCount){
						mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
						return mapCompare;
					}
				}
			*/}else{//数据有重复
				setMessage(infoList, lineNo,"Excel中第"+index+"行数据重复");
				if(infoList.size()>=errorCount){
					mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return mapCompare;
				}
			}
		}
		//重复出库单号
		if(infoList.size()>0){
			mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}else{
			mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
		}
		return mapCompare;
	}
	
	private List<BizOutstockPackmaterialEntity> getOrgPackMaterialList(
			Map<String, Object> querymap) {
		return service.getOrgPackMaterialList(querymap);
	}

	private List<String> getDataKey(){
		List<String> list=new ArrayList<String>();
		list.add("outstockNo");
		list.add("consumerMaterialCode");
		return list;
	}
	
	@FileProvider
	public DownloadFile export(Map<String, Object> parameter) throws IOException {
		
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PATH_STORAGE_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PATH_STORAGE_FEES");
		}
		String path = systemCodeEntity.getExtattr1();
		String tmep = SequenceGenerator.uuidOf36String("exp");
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		POIUtil poiUtil = new POIUtil();
		HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
		
		PageInfo<BizOutstockPackmaterialEntity> pageInfo = service.queryPriceGroupCount(parameter, 1, Integer.MAX_VALUE);
		
		List<BizOutstockPackmaterialEntity> list = pageInfo.getList();  
		
		this.appendSheet(poiUtil, hssfWorkbook, "耗材统计", path + "\\BmsStorageMaterial_" + tmep + ".xls", list);
		
		InputStream is = new FileInputStream(path + "\\BmsStorageMaterial_" + tmep + ".xls");
		return new DownloadFile("耗材统计" + tmep + ".xls", is);
	}
	
	private void appendSheet(POIUtil poiUtil, HSSFWorkbook hssfWorkbook,
			String sheetName, String path, List<BizOutstockPackmaterialEntity> list) throws IOException {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材编码");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "consumerMaterialCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计算状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "num");
        headInfoList.add(itemMap);
        

		
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        Map<String, Object> dataItem = null;
        
        for (BizOutstockPackmaterialEntity  entity: list) {      	
        	dataItem = new HashMap<String, Object>();
        	dataItem.put("customerName", entity.getCustomerName());
        	dataItem.put("warehouseName",entity.getWarehouseName());
        	dataItem.put("consumerMaterialCode", entity.getConsumerMaterialCode());
        	dataItem.put("isCalculated", CalculateState.getMap().get(entity.getIsCalculated()));
        	dataItem.put("num", entity.getNum());
        
        	dataList.add(dataItem);
        	
		}
        
        poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,sheetName,path, headInfoList, dataList);
	}

}
