package com.jiuyescm.bms.billcheck.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.HttpNewImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.iaccount.api.IUserService;
import com.jiuyescm.iaccount.vo.UserVO;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

/**
 * 账单导入
 * @author zhaofeng
 *
 */
@Controller("billCheckInfoImportController")
public class BillCheckInfoImportController extends HttpNewImport<BillCheckInfoVo,BillCheckInfoVo>{

	
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;
	
	@Autowired
	private IBillCheckLogService billCheckLogService;
	
	@Autowired 
	private StorageClient storageClient;
	
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IBillReceiveMasterService billReceiveMasterService;

	@FileResolver
	public Map<String,Object> importExcel(UploadFile file,Map<String, Object> parameter){		
		
		if(Session.isMissing()){
			Map<String, Object> map = new HashMap<String, Object>();
			List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();// 校验信息（报错提示）
			ErrorMessageVo errorVo =new ErrorMessageVo();
			errorVo.setMsg("长时间未操作，用户已失效，请重新登录再试！");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		return super.importFile(file);
	}
	
	@Override
	protected String getTemplateCode() {
		return "billcheck_bill_import";
	}

	@Override
	protected String getSessionId() {
		return "BillCheck_bill_import";
	}

	@Override
	protected List<BillCheckInfoVo> validateImportList(List<BillCheckInfoVo> importList,
			List<ErrorMessageVo> infoList) {
		List<BillCheckInfoVo> list=new ArrayList<BillCheckInfoVo>();
		//判断excel重复项
		Map<String,Object> excelMap=new HashMap<>();
		//获取所有用户
		Map<String,Object> userMap=getUserMap();
		//获取所有商家
		Map<String, Object> customerInfoMap=getCusMap();
		//获取所有的对账状态
		Map<String, String> cheMap=getCheckStatus();
		int index=1;
		for(BillCheckInfoVo vo:importList){
			String mes="";
			index++;
			
			//重复项校验
			String dataKey=vo.getCreateMonth()+"&"+vo.getInvoiceName()+"&"+vo.getBillName();
			if(excelMap.containsKey(dataKey)){
				mes+="Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复;";
				excelMap.put(dataKey, index+"");
			}else{
				excelMap.put(dataKey, index+"");
			}
			
			//商家合同名称验证
			if(StringUtils.isNotBlank(vo.getInvoiceName())){
			    Map<String,Object> map=new HashMap<>();
			    map.put("mkInvoiceName", vo.getInvoiceName());
			    
			    PubCustomerBaseEntity mkInvoiceName=billCheckInfoService.queryMk(map);
			    
				if(mkInvoiceName==null){
				    mes+="商家合同名称["+vo.getInvoiceName()+"]未在BMS商家中心维护;";
				}else {
                    vo.setMkId(mkInvoiceName.getMkId());
                }
				
			}else{
				mes+="商家合同名称不能为空;";
				//infoList.add(new ErrorMessageVo(index,"商家合同名称不能为空"));
				//break;
			}
			
			//月份判断
			if(vo.getCreateMonth()!=0){
				boolean result=formatMonth(vo.getCreateMonth());
				if(!result){
					mes+="业务月份格式不对;";
					//infoList.add(new ErrorMessageVo(index,"业务月份格式不对"));
					//break;
				}
			}else{
				mes+="业务月份格式不对;";
				//infoList.add(new ErrorMessageVo(index,"业务月份格式不对"));
				//break;
			}
			
			//对账状态
			if(StringUtils.isNotBlank(vo.getBillCheckStatus())){
				if(cheMap.containsKey(vo.getBillCheckStatus())){
					vo.setBillCheckStatus(cheMap.get(vo.getBillCheckStatus()));
				}else{
					mes+="对账状态不存在;";
					//infoList.add(new ErrorMessageVo(index,"对账状态不存在"));
					//break;
				}
			}else{
				mes+="对账状态不能为空;";
				//infoList.add(new ErrorMessageVo(index,"对账状态不能为空"));
				//break;
			}
			
			//是否需要发票
			if(StringUtils.isNotBlank(vo.getIsneedInvoice())){
				if("是".equals(vo.getIsneedInvoice())){
					vo.setIsneedInvoice("1");
					vo.setInvoiceStatus(BillCheckInvoiceStateEnum.NO_INVOICE.getCode());//未开票 
				}else if("否".equals(vo.getIsneedInvoice())){
					vo.setIsneedInvoice("0");
					vo.setInvoiceStatus(BillCheckInvoiceStateEnum.UNNEED_INVOICE.getCode());//不需要发票 
				}else{
					mes+="是否需要发票只能填  是或否;";
				}
			}
			
			//用户验证（销售员，项目管理员，结算员）
			//销售员
			if(StringUtils.isNotBlank(vo.getSellerName())){
				if(userMap.containsKey(vo.getSellerName())){
					//通过销售员名称获取人员信息
					List<UserVO> userList=userService.findUsers("", vo.getSellerName());
					if(userList.size()>0){
						UserVO sellerVo=userList.get(0);
						vo.setSellerId(sellerVo.getUsername());
						//vo.setArea(sellerVo.getDeptName());
					}	
				}else{
					mes+="销售员不存在;";
				}
				
				//通过销售区域管理找销售员对应得区域
				Map<String,Object> condition=new HashMap<>();
				condition.put("userName", vo.getSellerName());
				BmsGroupUserVo user=bmsGroupUserService.queryOne(condition);
				if(user!=null){
					vo.setArea(user.getAreaCode());
				}
				
			}else{
				mes+="销售员不能为空;";
			}
			
			//项目管理员
			if(StringUtils.isNotBlank(vo.getProjectManagerName())){
				if(userMap.containsKey(vo.getProjectManagerName())){
					UserVO userVO=(UserVO) userMap.get(vo.getProjectManagerName());
					vo.setProjectManagerId(userVO.getUsername());
				}else{
					mes+="项目管理员不存在;";
				}
			}else{
				mes+="项目管理员不能为空;";
			}
			//结算员
			if(StringUtils.isNotBlank(vo.getBalanceName())){
				if(userMap.containsKey(vo.getBalanceName())){
					UserVO userVO=(UserVO) userMap.get(vo.getBalanceName());
					vo.setBalanceId(userVO.getUsername());
				}else{
					mes+="结算员不存在;";
				}
			}else{
				mes+="结算员不能为空;";
			}
			
			
			//重复验证    同一个月份只允许存在一条账单（月份+商家合同名称+账单名称）
			Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("createMonth", vo.getCreateMonth());
			condition.put("invoiceName", vo.getInvoiceName());
			condition.put("billName", vo.getBillName());
			List<BillCheckInfoVo> qlist=billCheckInfoService.queryList(condition);
			if(qlist.size()>0){
				mes+="(月份："+vo.getCreateMonth()+",商家合同名称:"+vo.getInvoiceName()+",账单名称:"+vo.getBillName()+")已存在";
				//infoList.add(new ErrorMessageVo(index,"同一个月份只允许存在一条账单"));
				//break;
			}
			
			//新增账单状态判断
			//1）如果对账状态为“已确认”and是否需要开票为“是”，将账单状态置为“待开票”；
			//2）如果对账状态为“已确认”and是否需要开票为“否”，将账单状态置为“待收款”；
			//3）如果对账状态不为“已确认”，将账单状态置为“待确认”；
			if(BillCheckStateEnum.CONFIRMED.getCode().equals(vo.getBillCheckStatus()) && "1".equals(vo.getIsneedInvoice())){
				vo.setBillStatus(CheckBillStatusEnum.TB_INVOICE.getCode());
			}else if(BillCheckStateEnum.CONFIRMED.getCode().equals(vo.getBillCheckStatus()) && "0".equals(vo.getIsneedInvoice())){
				vo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
			}else{
				vo.setBillStatus(CheckBillStatusEnum.TB_CONFIRMED.getCode());
			}
			
			//确认金额判断
			//确认金额判断保留两位小数
			if(vo.getConfirmAmount()!=null){
				double money=vo.getConfirmAmount().doubleValue();
				money=getTeshu(money);
				BigDecimal newMoney=BigDecimal.valueOf(money);
				vo.setConfirmAmount(newMoney);
				vo.setConfirmUnInvoiceAmount(vo.getConfirmAmount());
				vo.setUnReceiptAmount(vo.getConfirmAmount());
			}
			
			//账单逾期时间
			Date overdueDate=getDate(vo.getCreateMonth());
			vo.setOverdueDate(overdueDate);
			
			//判断账单导入主表是否有导入记录
			BillReceiveMasterVo master=billReceiveMasterService.queryOne(condition);
			if(master!=null){
				vo.setBillNo(master.getBillNo());
			}
	
			vo.setIsapplyBad("0");
			vo.setCreator(JAppContext.currentUserName());
			vo.setCreatorId(JAppContext.currentUserID());
			vo.setCreateTime(JAppContext.currentTimestamp());
			vo.setLastModifier(JAppContext.currentUserName());
			vo.setLastModifierId(JAppContext.currentUserID());
			vo.setLastModifyTime(JAppContext.currentTimestamp());
			vo.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());//未收款
			vo.setDelFlag("0");
			list.add(vo);
			if(StringUtils.isNotBlank(mes)){
				infoList.add(new ErrorMessageVo(index,mes));
			}
		}
		
		return list;
	}

	@Override
	protected void saveDataBatch(List<BillCheckInfoVo> list) throws Exception {
		// TODO Auto-generated method stub
		int result=billCheckInfoService.saveList(list);
		if(result>0){
			for(BillCheckInfoVo vo:list){
				//新增日志
				String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
				try {
                    //获取id
				    Map<String, Object> condition1 = new HashMap<>();
				    condition1.put("createMonth", vo.getCreateMonth());
                    condition1.put("billName", vo.getBillName());
                    PageInfo<BillCheckInfoVo> forId = billCheckInfoService.query(condition1,1,1);
                    List<BillCheckInfoVo> listVos = forId.getList();
				    
					BillCheckLogVo billCheckLogVo=new BillCheckLogVo();
					if(CollectionUtils.isNotEmpty(listVos)){
		                   billCheckLogVo.setBillCheckId(listVos.get(0).getId());
					}
					billCheckLogVo.setBillStatusCode(vo.getBillStatus());
					billCheckLogVo.setOperateDesc("账单导入");
					billCheckLogVo.setLogType(0);
					billCheckLogVo.setCreator(JAppContext.currentUserName());
					billCheckLogVo.setCreateTime(JAppContext.currentTimestamp());
					billCheckLogVo.setCreatorId(JAppContext.currentUserID());
					billCheckLogVo.setDeptName(groupName);
					billCheckLogVo.setDelFlag("0");
					
					billCheckLogService.addBillCheckLog(billCheckLogVo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}else{
			throw new Exception("保存失败");
		}
	}

	//获取所有商家
	private Map<String, Object> getCusMap(){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			List<CustomerVo> list=customerService.queryAll();
			for(CustomerVo entity:list){
				if(StringUtils.isNotBlank(entity.getMkInvoiceName())){
					map.put(entity.getMkInvoiceName(), entity);
				}			
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	
	//获取所有用户
	private Map<String, Object> getUserMap(){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			List<UserVO> userList=userService.findAllUsers();
			for(UserVO entity:userList){
				map.put(entity.getCname(), entity);
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	
	
	//获取对账状态
	private Map<String, String> getCheckStatus(){
		Map<String, String> map=new HashMap<>();
		map.put("初始", "BEGIN");
		map.put("发仓库", "WH_FOLLOW");
		map.put("发客户", "CUST_FOLLOW");
		map.put("客户未反馈", "CUST_NO_FEEDBACK");
		map.put("客户正在核对", "CUST_CHECKING");
		map.put("报价问题", "QUOTATION");
		map.put("仓库问题", "WH_PROBLEM");
		map.put("项目问题", "PROJECT_PROBLEM");
		map.put("干线问题", "TS_PROBLEM");
		map.put("已确认", "CONFIRMED");
		map.put("异常", "EXCEPTION");
		return map;
	}
	
	private boolean formatMonth(int date){
		String createMonth=date+"";
		if(createMonth.length()!=4){
			return false;
		}else{
			String month=createMonth.substring(2, 4);
			int m=Integer.parseInt(month);
			if(m<=0 || m>12){
				return false;
			}
		}
		return true;
	}
	
	@Expose
	public int getProgress(){
		return super.getProgressStatus();
	}
	
	@Expose
	public void setProgress(){
		super.setProgressStatus();
	}
	
	public double getTeshu(double weightTeshu){
		double weight=weightTeshu;		
		String a=weight+"";
		if(a.indexOf(".")!=-1){		
			int b=a.indexOf(".");		    
			String index=a.substring(b+1);
			if(index.length()>=3){
				weight=(Math.round(weight*100))/100d;
			}
		}
		return weight;
	}
	
	
	public Date getDate(int createMonth){
		try {
			String createDate="20"+createMonth+"";
			int year=Integer.parseInt(createDate.substring(0, 4));
			int month=Integer.parseInt(createDate.substring(4, 6));
			
			if(month<=0){
				year=year-1;
			}
			 Calendar cal = Calendar.getInstance();    
	         cal.set(Calendar.YEAR, year);     
	         cal.set(Calendar.MONTH, month);     
	         cal.set(Calendar.DAY_OF_MONTH,20);  
			
	         String date=new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime()); 
	         
	         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
	         
	 		 Date date1 = df.parse(date);
	 		 
	 		 return date1;
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
		return null;
	}
	
}
