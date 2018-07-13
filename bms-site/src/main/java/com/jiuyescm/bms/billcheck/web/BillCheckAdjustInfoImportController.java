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
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.HttpNewImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

@Controller("billCheckAdjustInfoImportController")
public class BillCheckAdjustInfoImportController extends HttpNewImport<BillCheckAdjustInfoVo,BillCheckAdjustInfoVo>{

	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired
	private IBillCheckReceiptService billCheckReceiptService;

	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;
	
	@Autowired
	private IBillCheckLogService billCheckLogService;
	
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
		return "billcheck_receipt_adjust_import";
	}

	@Override
	protected String getSessionId() {
		// TODO Auto-generated method stub
		return "BillCheck_receipt_adjust_import";
	}

	@Override
	protected List<BillCheckAdjustInfoVo> validateImportList(List<BillCheckAdjustInfoVo> importList,
			List<ErrorMessageVo> infoList) {
		List<BillCheckAdjustInfoVo> list=new ArrayList<BillCheckAdjustInfoVo>();
		//判断excel重复项
		Map<String,Object> excelMap=new HashMap<>();
		// TODO Auto-generated method stub
		int index=1;
		for(BillCheckAdjustInfoVo vo:importList){
			index++;
			String mes="";		
			String dataKey=vo.getCreateMonth()+"&"+vo.getInvoiceName()+"&"+vo.getBillName();
			if(excelMap.containsKey(dataKey)){
				mes+="Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复;";
				//数据有重复
				//infoList.add(new ErrorMessageVo(index,"Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复"));
				excelMap.put(dataKey, index+"");
			}else{
				excelMap.put(dataKey, index+"");
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
			//开票名称验证
			if(StringUtils.isNotBlank(vo.getInvoiceName())){
				//通过开票名称和月份查询账单
				Map<String, Object> condition=new HashMap<String, Object>();
				condition.put("createMonth", vo.getCreateMonth());
				condition.put("invoiceName", vo.getInvoiceName());
				condition.put("billName",vo.getBillName());
				List<BillCheckInfoVo> blist=billCheckInfoService.queryList(condition);
				if(blist.size()==0){
					mes+="(月份："+vo.getCreateMonth()+",开票名称:"+vo.getInvoiceName()+",账单名称:"+vo.getBillName()+")不存在";
					//infoList.add(new ErrorMessageVo(index,"未查询到该月份的开票名称"));
					//break;
				}else{
					if(blist.get(0).getBillStatus().equals(CheckBillStatusEnum.BAD_BILL.getCode())){
						mes+="当前账单"+blist.get(0).getInvoiceName()+"坏账状态，无法导入";
					}else if(blist.get(0).getBillStatus().equals(CheckBillStatusEnum.TB_CONFIRMED.getCode())){
						mes+="当前账单"+blist.get(0).getInvoiceName()+"待确认状态，无法导入";
					}else if(blist.get(0).getBillStatus().equals(CheckBillStatusEnum.INVALIDATE.getCode())){
						mes+="当前账单"+blist.get(0).getInvoiceName()+"已作废状态，无法导入";
					}else if(blist.get(0).getBillStatus().equals(CheckBillStatusEnum.RECEIPTED.getCode())){
						mes+="当前账单"+blist.get(0).getInvoiceName()+"已收款状态，无法导入";
					}else{
						vo.setBillCheckId(blist.get(0).getId());
					}				
				}				
			}else{
				mes+="开票名称不能为空;";
				//infoList.add(new ErrorMessageVo(index,"开票名称不能为空"));
				//break;
			}
			
			//确认金额判断
			//发票金额判断保留两位小数
			if(vo.getAdjustAmount()!=null){
				double money=vo.getAdjustAmount().doubleValue();
				money=getTeshu(money);
				BigDecimal newMoney=BigDecimal.valueOf(money);
				vo.setAdjustAmount(newMoney);
			}
			
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
	protected void saveDataBatch(List<BillCheckAdjustInfoVo> list) throws Exception {
		// TODO Auto-generated method stub
		List<BillCheckAdjustInfoVo> deList=new ArrayList<BillCheckAdjustInfoVo>();
		for(BillCheckAdjustInfoVo entity:list){
			Map<String, Object> param=new HashMap<String, Object>();
			
			param.put("billCheckId", entity.getBillCheckId());
			List<BillCheckAdjustInfoVo> adjustlist = billCheckInfoService.queryAdjust(param);
			if(list.size()>0){
				for(BillCheckAdjustInfoVo co:adjustlist){
					deList.add(co);
				}
			}
			
		}
		//删除原有的
		if(deList.size()>0){
			billCheckInfoService.updateAjustList(deList);
		}
		int result=billCheckInfoService.saveAjustList(list);
		//统计金额判断状态
		
		if(result>0){
			for(BillCheckAdjustInfoVo vo:list){
				//查询账单
				Map<String, Object> condition=new HashMap<String, Object>();
				condition.put("id", vo.getBillCheckId());
				/*condition.put("billName",vo.getBillName());*/
				List<BillCheckInfoVo> blist=billCheckInfoService.queryList(condition);
				if(blist.size()>0){
					 	BillCheckInfoVo checkVo=blist.get(0);
					 	//总回款金额
						BigDecimal totalReceiptAmount=new BigDecimal(0);
						//账单应收总额
						BigDecimal checkReceiptAmount=new BigDecimal(0);
						//调整金额
						BigDecimal adjustAmount=new BigDecimal(0);
						
						//所有的回款金额
						if(checkVo.getReceiptAmount()!=null){
							totalReceiptAmount=totalReceiptAmount.add(checkVo.getReceiptAmount());
						}
						
						//账单中的确认金额加调整金额
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

						if(totalReceiptAmount.compareTo(checkReceiptAmount)==0){
							checkVo.setReceiptAmount(totalReceiptAmount);
							//回款状态
							checkVo.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());
							//账单状态（已回款）
							checkVo.setBillStatus(CheckBillStatusEnum.RECEIPTED.getCode());
						}else if(totalReceiptAmount.compareTo(BigDecimal.ZERO)==0){
							//回款状态
							checkVo.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());
							//账单状态（待收款）
							//如果是待确认状态下有回款金额，状态还是待确认
							if(!CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(checkVo.getBillStatus())){
								checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
							}
						}else{
							checkVo.setReceiptAmount(totalReceiptAmount);
							//部分回款
							checkVo.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());
							//账单状态（待回款）
							//如果是待确认状态下有回款金额，状态还是待确认
							if(!CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(checkVo.getBillStatus())){
								checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
							}
						}
					 	
						//未收款金额
						checkVo.setUnReceiptAmount(checkReceiptAmount.subtract(totalReceiptAmount));
					 	//开票未回款金额
						BigDecimal invoiceMoney=new BigDecimal(0);
						if(checkVo.getInvoiceAmount()!=null){
							invoiceMoney=checkVo.getInvoiceAmount();
						}
						
						checkVo.setInvoiceUnReceiptAmount(invoiceMoney.subtract(totalReceiptAmount).add(adjustAmount));					 	
						billCheckInfoService.update(checkVo);				
						//新增日志
						String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
						try {
							BillCheckLogVo billCheckLogVo=new BillCheckLogVo();
							
							billCheckLogVo.setBillCheckId(checkVo.getId());
							billCheckLogVo.setBillStatusCode(checkVo.getBillStatus());
							billCheckLogVo.setOperateDesc("收款调整导入");
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
