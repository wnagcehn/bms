package com.jiuyescm.bms.billcheck.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInvoiceService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.HttpNewImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

/**
 * 发票导入
 * @author zhaofeng
 *
 */
@Controller("billCheckInvoiceImportController")
public class BillCheckInvoiceImportController extends HttpNewImport<BillCheckInvoiceVo,BillCheckInvoiceVo>{

	@Autowired
	private IBillCheckInvoiceService billCheckInvoiceService;
	
	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired
	private IBillCheckLogService billCheckLogService;
	
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;
	
	
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
		return "billcheck_invoice_import";
	}

	@Override
	protected String getSessionId() {
		return "BillCheck_invoice_import";
	}
	private List<BillCheckInfoVo> queryAllByBillName(List<BillCheckInvoiceVo> importList){
		/*List<String> billNameList=new ArrayList<String>();
		for(BillCheckInvoiceVo voEntity:importList){
			billNameList.add(voEntity.getBillName());
		}
		return billCheckInfoService.queryAllByBillName(billNameList);*/
		
		List<Integer> billIdList=new ArrayList<Integer>();
		for(BillCheckInvoiceVo voEntity:importList){
			//查询账单
			Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("createMonth",voEntity.getCreateMonth());
			condition.put("invoiceName",voEntity.getInvoiceName());
			condition.put("billName", voEntity.getBillName());		
			BillCheckInfoVo bInfoVo=billCheckInfoService.queryOne(condition);
			if(bInfoVo!=null){
				billIdList.add(bInfoVo.getId());
			}
		}
		return billCheckInfoService.queryAllByBillCheckId(billIdList);
	}
	private String checkInvoiceVo(List<BillCheckInfoVo>  billCheckVoList,BillCheckInvoiceVo invoiceVo){
		String errorMsg="";
		/*boolean isReceipt=true;*/
		boolean isBadBill=true;
		boolean isCancel=true;
		boolean isBadApply=true;
		boolean billNameFlag=false;
		boolean invoiceFlag=false;
		boolean createMonthFlag=false;
		/*boolean isConfirm=true;
		boolean isComplete=false;*/
		for(BillCheckInfoVo checkVo:billCheckVoList){
			if(checkVo.getBillName().equals(invoiceVo.getBillName())){//验证账单名称
				billNameFlag=true;
				if(checkVo.getInvoiceName().equals(invoiceVo.getInvoiceName())){//验证开票名称
					invoiceFlag=true;
					if(checkVo.getCreateMonth()==invoiceVo.getCreateMonth()){
						createMonthFlag=true;
						invoiceVo.setBillCheckId(checkVo.getId());
						//如果是坏账状态无法导入
						if(checkVo.getBillStatus().equals(CheckBillStatusEnum.BAD_BILL.getCode())){
							isBadBill=false;		
						}
						//如果是坏账申请状态
						if(("1").equals(checkVo.getIsapplyBad())){
							isBadApply=false;		
						}
						/*//如果是已收款状态无法导入
						if(checkVo.getBillStatus().equals(CheckBillStatusEnum.RECEIPTED.getCode())){
							isReceipt=false;		
						}*/
						//作废状态下无法导入
						if(checkVo.getBillStatus().equals(CheckBillStatusEnum.INVALIDATE.getCode())){
							isCancel=false;		
						}
						/*if(checkVo.getBillStatus().equals(CheckBillStatusEnum.TB_CONFIRMED.getCode())){
							isConfirm=false;		
						}
						if(StringUtils.isNotBlank(checkVo.getInvoiceStatus())){
							if(checkVo.getInvoiceStatus().equals(BillCheckInvoiceStateEnum.INVOICED.getCode())){
								isComplete=true;
							}
						}*/
						break;
					}
				}
			}
			
		}
		if(!billNameFlag){
			errorMsg+="账单名称【"+invoiceVo.getBillName()+"】不存在;";
			return errorMsg;
		}
		if(!invoiceFlag){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】不存在开票名称【"+invoiceVo.getInvoiceName()+"】;";
			return errorMsg;
		}
		if(!createMonthFlag){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】,开票名称【"+invoiceVo.getInvoiceName()+"】无此开票月份【"+invoiceVo.getCreateMonth()+"】;";
			return errorMsg;
		}
	/*	if(!isNeedInvoice){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】,开票名称【"+invoiceVo.getInvoiceName()+"】,开票月份【"+invoiceVo.getCreateMonth()+"】不需要发票,无法导入;";
			return errorMsg;
		}*/
		/*if(!isConfirm){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】待确认状态,无法导入开票信息;";
			return errorMsg;
		}*/
		if(!isBadBill){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】坏账状态,无法导入开票信息;";
			return errorMsg;
		}
		if(!isBadApply){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】是坏账申请状态,无法导入开票信息;";
			return errorMsg;
		}
		/*if(isComplete){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】开票已完成,无法导入开票信息;";
			return errorMsg;
		}*/
		if(!isCancel){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】作废状态,无法导入开票信息;";
			return errorMsg;
		}
		/*if(!isReceipt){
			errorMsg+="账单【"+invoiceVo.getBillName()+"】已收款状态,无法导入开票信息;";
			return errorMsg;
		}*/
		return errorMsg;
	}
	List<BillCheckInfoVo> billCheckVoList=null;
	@Override
	protected List<BillCheckInvoiceVo> validateImportList(List<BillCheckInvoiceVo> importList,
			List<ErrorMessageVo> infoList) {
		//判断excel重复项
		/*Map<String,Object> excelMap=new HashMap<>();*/
		List<BillCheckInvoiceVo> list=new ArrayList<BillCheckInvoiceVo>();
		billCheckVoList=queryAllByBillName(importList);
		int index=1;
		for(BillCheckInvoiceVo vo:importList){
			index++;
			String mes="";
			
			//重复项校验
			/*String dataKey=vo.getCreateMonth()+"&"+vo.getInvoiceName()+"&"+vo.getBillName()+"&"+vo.getInvoiceNo();
			if(excelMap.containsKey(dataKey)){
				mes+="Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复;";
				//数据有重复
				//infoList.add(new ErrorMessageVo(index,"Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复"));
				excelMap.put(dataKey, index+"");
			}else{
				excelMap.put(dataKey, index+"");
			}*/
			
			//月份判断
			if(vo.getCreateMonth()!=0){
				boolean result=formatMonth(vo.getCreateMonth());
				if(!result){
					mes+="业务月份格式不对;";
				}
			}else{
				mes+="业务月份格式不对;";
			}
			
			//开票名称验证
			if(StringUtils.isNotBlank(vo.getInvoiceName())){
				mes+=checkInvoiceVo(billCheckVoList,vo);
			}else{
				mes+="开票名称不能为空;";
			}
			
			//月份判断
			if(vo.getInvoiceDate()==null){
				mes+="开票日期格式不对或者为空;";
			}
			
			//发票金额判断保留两位小数
			if(vo.getInvoiceAmount()!=null){
				double money=vo.getInvoiceAmount().doubleValue();
				money=getTeshu(money);
				BigDecimal newMoney=BigDecimal.valueOf(money);
				vo.setInvoiceAmount(newMoney);
			}
			
			
			//重复校验  同一个月，同一个商家，同一个账单，同一个发票号不允许重复

		  /*Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("billCheckId", vo.getBillCheckId());
			condition.put("invoiceNo", vo.getInvoiceNo());
			List<BillCheckInvoiceVo> qlist=billCheckInvoiceService.queryByParam(condition);
			if(qlist.size()>0){
				mes+="账单【"+vo.getBillName()+"】已存在发票号【"+vo.getInvoiceNo()+"】;";
			}*/
			
			vo.setCreator(JAppContext.currentUserName());
			vo.setCreatorId(JAppContext.currentUserID());
			vo.setCreateTime(JAppContext.currentTimestamp());
			vo.setDelFlag("0");
			list.add(vo);
			if(StringUtils.isNotBlank(mes)){
				infoList.add(new ErrorMessageVo(index,mes));
			}
		}
		
		return list;
	}


	/**
	 * 导入开票金额==账单开票金额  开票完成，小于 部分开票,大于 异常
	 * 
	 */
	@Override
	protected void saveDataBatch(List<BillCheckInvoiceVo> list) throws Exception {
		List<Integer> checkIdList=new ArrayList<>();
		for(BillCheckInvoiceVo vo:list){
			checkIdList.add(vo.getBillCheckId());
		}

		for(BillCheckInfoVo checkVo:billCheckVoList){//遍历账单
			BigDecimal total=new BigDecimal(0);
			checkVo.setLastModifier(JAppContext.currentUserName());
			checkVo.setLastModifierId(JAppContext.currentUserID());
			checkVo.setLastModifyTime(JAppContext.currentTimestamp());
			
			//调整金额
			BigDecimal adjustMoney=new BigDecimal(0);
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("billCheckId", checkVo.getId());
			List<BillCheckAdjustInfoVo> adjustList = billCheckInfoService.queryAdjust(param);
			if(adjustList.size()>0){
				BillCheckAdjustInfoVo entity=adjustList.get(0);
				if(entity!=null && entity.getAdjustAmount()!=null){
					adjustMoney=entity.getAdjustAmount();
				}	
			}
			
			//统计导入开票金额
			for(BillCheckInvoiceVo importInvoiceVo:list){
				if(importInvoiceVo.getBillCheckId().equals(checkVo.getId())){
					if(importInvoiceVo.getInvoiceAmount()!=null){
						total=total.add(importInvoiceVo.getInvoiceAmount());
					}
				}
			}
			if(checkVo.getInvoiceAmount()!=null){
				total=total.add(checkVo.getInvoiceAmount());
			}
			//发票金额和  （账单中的确认金额）  比较
			//调整金额
			BigDecimal totalMoney=checkVo.getConfirmAmount();
			
			if(total.compareTo(totalMoney)>=0){
				checkVo.setInvoiceAmount(total);
				checkVo.setInvoiceStatus(BillCheckInvoiceStateEnum.INVOICED.getCode());//已开票
			}else{
				checkVo.setInvoiceAmount(total);
				checkVo.setInvoiceStatus(BillCheckInvoiceStateEnum.PART_INVOICE.getCode());//部分收票
			}
			if("0".equals(checkVo.getIsneedInvoice())){
				checkVo.setInvoiceStatus(BillCheckInvoiceStateEnum.UNNEED_INVOICE.getCode());//不需要发票
			}
			
			//开票未回款金额
			BigDecimal money=new BigDecimal(0);
			if(checkVo.getInvoiceAmount()!=null){
				money=checkVo.getInvoiceAmount();
			}
			if(checkVo.getReceiptAmount()!=null){
				money=money.subtract(checkVo.getReceiptAmount());
			}
			money.add(adjustMoney);
			checkVo.setInvoiceUnReceiptAmount(money);
			
			//已确认未开票金额
			checkVo.setConfirmUnInvoiceAmount(checkVo.getConfirmAmount().subtract(total));
			//导入发票除后账单状态只有在待开票状态下才变为待收款
			if(CheckBillStatusEnum.TB_INVOICE.getCode().equals(checkVo.getBillStatus())){
				checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
			}
		}
		billCheckInvoiceService.saveImportInovice(list,billCheckVoList);
		
		//新增日志
		for(BillCheckInfoVo checkInfo:billCheckVoList){
			String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
			try {
				BillCheckLogVo billCheckLogVo=new BillCheckLogVo();
				
				billCheckLogVo.setBillCheckId(checkInfo.getId());
				billCheckLogVo.setBillStatusCode(checkInfo.getBillStatus());
				billCheckLogVo.setOperateDesc("发票导入");
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
	}
	
	@Expose
	public int getProgress(){
		return super.getProgressStatus();
	}
	
	@Expose
	public void setProgress(){
		super.setProgressStatus();
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
}
