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
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.service.IBillCheckReceiptService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.HttpNewImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
/**
 * 收款导入
 * @author Wuliangfeng
 *
 */
@Controller("billCheckReceiptImportController")
public class BillCheckReceiptImportController extends HttpNewImport<BillCheckReceiptVo,BillCheckReceiptVo>{

	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired
	private IBillCheckReceiptService billCheckReceiptService;

	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	
	@Autowired
	private IBillCheckLogService billCheckLogService;
	
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;
	
	List<BillCheckInfoVo> billCheckVoList=null;
	
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
		// TODO Auto-generated method stub
		return "billcheck_receipt_import";
	}

	@Override
	protected String getSessionId() {
		// TODO Auto-generated method stub
		return "BillCheck_receipt_import";
	}
	private List<BillCheckInfoVo> queryAllByBillName(List<BillCheckReceiptVo> importList){
		/*List<String> billNameList=new ArrayList<String>();
		for(BillCheckReceiptVo voEntity:importList){
			billNameList.add(voEntity.getBillName());
		}
		return billCheckInfoService.queryAllByBillName(billNameList);*/
		
		List<Integer> billIdList=new ArrayList<Integer>();
		for(BillCheckReceiptVo voEntity:importList){
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
	
	private String checkReceiptVo(List<BillCheckInfoVo>  billCheckVoList,BillCheckReceiptVo receiptVo){
		String errorMsg="";
		boolean billNameFlag=false;
		boolean invoiceFlag=false;
		boolean createMonthFlag=false;
		boolean isBadBill=true;
		boolean isCancel=true;
		boolean isReceipt=true;
		boolean isBadApply=true;
/*		boolean isComplete=false;
*/		for(BillCheckInfoVo checkVo:billCheckVoList){
			if(checkVo.getBillName().equals(receiptVo.getBillName())){//验证账单名称
				billNameFlag=true;
				if(checkVo.getInvoiceName().equals(receiptVo.getInvoiceName())){//验证商家合同名称
					invoiceFlag=true;
					if(checkVo.getCreateMonth()==receiptVo.getCreateMonth()){
						createMonthFlag=true;
						receiptVo.setBillCheckId(checkVo.getId());
						
						//坏账状态下无法导入
						if(checkVo.getBillStatus().equals(CheckBillStatusEnum.BAD_BILL.getCode())){
							isBadBill=false;
						}
						//作废状态下无法导入
						if(checkVo.getBillStatus().equals(CheckBillStatusEnum.BAD_BILL.getCode())){
							isCancel=false;
						}
						//已收款状态下无法导入
						if(checkVo.getBillStatus().equals(CheckBillStatusEnum.RECEIPTED.getCode())){
							isReceipt=false;
						}
						//坏账申请状态下无法导入
						if("1".equals(checkVo.getIsapplyBad())){
							isBadApply=false;
						}
						/*if(checkVo.getReceiptStatus().equals(BillCheckReceiptStateEnum..getCode())){
							isComplete=true;
						}*/
						//坏账申请状态下无法导入
						break;
					}
				}
			}
			
		}
		if(!billNameFlag){
			errorMsg+="账单名称【"+receiptVo.getBillName()+"】不存在;";
			return errorMsg;
		}
		if(!invoiceFlag){
			errorMsg+="账单【"+receiptVo.getBillName()+"】无此商家合同名称【"+receiptVo.getInvoiceName()+"】;";
			return errorMsg;
		}
		if(!createMonthFlag){
			errorMsg+="账单【"+receiptVo.getBillName()+"】,商家合同名称【"+receiptVo.getInvoiceName()+"】无此开票月份【"+receiptVo.getCreateMonth()+"】;";
			return errorMsg;
		}
		if(!isBadBill){
			errorMsg+="当前账单"+receiptVo.getBillName()+"坏账状态，无法导入";
			return errorMsg;
		}
		if(!isCancel){
			errorMsg+="当前账单"+receiptVo.getBillName()+"作废状态，无法导入";
			return errorMsg;
		}
		if(!isReceipt){
			errorMsg+="当前账单"+receiptVo.getBillName()+"已收款状态，无法导入";
			return errorMsg;
		}
		if(!isBadApply){
			errorMsg+="当前账单"+receiptVo.getBillName()+"坏账申请状态，无法导入";
			return errorMsg;
		}
		/*
		if(!isComplete){
			errorMsg+="账单【"+receiptVo.getBillName()+"】已收款，不可导入;";
			return errorMsg;
		}*/
		return errorMsg;
	}
	@Override
	protected List<BillCheckReceiptVo> validateImportList(List<BillCheckReceiptVo> importList,
			List<ErrorMessageVo> infoList) {
		List<BillCheckReceiptVo> list=new ArrayList<BillCheckReceiptVo>();
		
		billCheckVoList=queryAllByBillName(importList);

		int index=1;
		for(BillCheckReceiptVo vo:importList){
			String mes="";
			index++;
			//月份判断
			if(vo.getCreateMonth()!=0){
				boolean result=formatMonth(vo.getCreateMonth());
				if(!result){
					mes+="业务月份格式不对;";
				}
			}else{
				mes+="业务月份格式不对;";
			}
			
			//商家合同名称验证
			if(StringUtils.isNotBlank(vo.getInvoiceName())){
				mes+=checkReceiptVo(billCheckVoList,vo);
				
			}else{
				mes+="商家合同名称不能为空;";
			}
			
			//首款金额判断保留两位小数
			if(vo.getReceiptAmount()!=null){
				double money=vo.getReceiptAmount().doubleValue();
				money=getTeshu(money);
				BigDecimal newMoney=BigDecimal.valueOf(money);
				vo.setReceiptAmount(newMoney);
			}
			
			vo.setReceiptType("正常收款");
			vo.setCreatorId(JAppContext.currentUserID());
			vo.setCreator(JAppContext.currentUserName());
			vo.setCreateTime(JAppContext.currentTimestamp());
			vo.setDelFlag("0");
			list.add(vo);
			if(StringUtils.isNotBlank(mes)){
				infoList.add(new ErrorMessageVo(index,mes));
			}
		}	
		return list;
	}

	@Override
	protected void saveDataBatch(List<BillCheckReceiptVo> list) throws Exception {
		//遍历所有账单
		for(BillCheckInfoVo checkVo:billCheckVoList){
			//总回款金额
			BigDecimal totalReceiptAmount=new BigDecimal(0);
			//账单应收总额
			BigDecimal checkReceiptAmount=new BigDecimal(0);
			
			//调整金额
			BigDecimal adjustAmount=new BigDecimal(0);
			
			//统计所有导入的回款金额
			for(BillCheckReceiptVo receiptVo:list){
				if(receiptVo.getBillCheckId().equals(checkVo.getId())){
					if(receiptVo.getReceiptAmount()!=null){
						totalReceiptAmount=totalReceiptAmount.add(receiptVo.getReceiptAmount());
					}
				}
			}
			//账单中的回款金额
			if(checkVo.getReceiptAmount()!=null){
				totalReceiptAmount=totalReceiptAmount.add(checkVo.getReceiptAmount());
			}
			
			if(checkVo.getConfirmAmount()!=null){
				checkReceiptAmount=checkReceiptAmount.add(checkVo.getConfirmAmount());
				//调整的金额
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("billCheckId", checkVo.getId());
				List<BillCheckAdjustInfoVo> adjustList = billCheckInfoService.queryAdjust(param);
				if(adjustList.size()>0){
					BillCheckAdjustInfoVo entity=adjustList.get(0);
					if(entity!=null && entity.getAdjustAmount()!=null){
						adjustAmount=entity.getAdjustAmount();
						checkReceiptAmount=checkReceiptAmount.add(entity.getAdjustAmount());
					}	
				}
			}

			
			/*if(totalReceiptAmount>checkReceiptAmount){
				throw new Exception("帐单【"+checkVo.getBillName()+"】收账金额大于确认金额,不可导入！");
			}else */
			if(totalReceiptAmount.compareTo(checkReceiptAmount)>=0){
				checkVo.setReceiptAmount(totalReceiptAmount);
				//回款状态
				checkVo.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());
				//账单状态（已回款）
				/*checkVo.setBillStatus(CheckBillStatusEnum.RECEIPTED.getCode());*/
			}else if(totalReceiptAmount.compareTo(BigDecimal.ZERO)==0){
				checkVo.setReceiptAmount(totalReceiptAmount);
				//回款状态
				checkVo.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());
				//如果是待确认状态下有回款金额，状态还是待确认
			/*	if(!CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(checkVo.getBillStatus())){
					checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
				}*/
			}else{
				checkVo.setReceiptAmount(totalReceiptAmount);
				//部分回款
				checkVo.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());
				//账单状态（待回款）
				//如果是待确认状态下有回款金额，状态还是待确认
			/*	if(!CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(checkVo.getBillStatus())){
					checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
				}*/
			}
			//未收款金额
			checkVo.setUnReceiptAmount(checkReceiptAmount.subtract(totalReceiptAmount));
			
			//开票未收款金额
			//发票总金额
			BigDecimal invoiceAmount=new BigDecimal(0);
			if(checkVo.getInvoiceAmount()!=null){
				invoiceAmount=checkVo.getInvoiceAmount();
			}
			checkVo.setInvoiceUnReceiptAmount(invoiceAmount.subtract(totalReceiptAmount).add(adjustAmount));
		}
		billCheckReceiptService.saveImportList(list,billCheckVoList);
		
		
		//新增日志
		for(BillCheckInfoVo checkInfo:billCheckVoList){
			String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
			try {
				BillCheckLogVo billCheckLogVo=new BillCheckLogVo();
				
				billCheckLogVo.setBillCheckId(checkInfo.getId());
				billCheckLogVo.setBillStatusCode(checkInfo.getBillStatus());
				billCheckLogVo.setOperateDesc("收款导入");
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
