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
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.HttpNewImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

@Controller("billReceiptFollowImportController")
public class BillReceiptFollowImportController extends HttpNewImport<BillReceiptFollowVo,BillReceiptFollowVo>{

	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
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
		return "billcheck_receipt_follow_import";
	}

	@Override
	protected String getSessionId() {
		// TODO Auto-generated method stub
		return "BillCheck_receipt_follow_import";
	}

	@Override
	protected List<BillReceiptFollowVo> validateImportList(List<BillReceiptFollowVo> importList,
			List<ErrorMessageVo> infoList) {
		List<BillReceiptFollowVo> list=new ArrayList<BillReceiptFollowVo>();
		//判断excel重复项
		Map<String,Object> excelMap=new HashMap<>();
		// TODO Auto-generated method stub
		int index=1;
		for(BillReceiptFollowVo vo:importList){
			String mes="";
			index++;
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
				//通过开票名称和月份,账单名称查询账单
				Map<String, Object> condition=new HashMap<String, Object>();
				condition.put("createMonth", vo.getCreateMonth());
				condition.put("invoiceName", vo.getInvoiceName());
				condition.put("billName",vo.getBillName());
				List<BillCheckInfoVo> blist=billCheckInfoService.queryList(condition);
				if(blist.size()==0){
					mes+="未查询到该账单该月份的开票名称;";
					//infoList.add(new ErrorMessageVo(index,"未查询到该月份的开票名称"));
					//break;
				}else{
					if(blist.get(0).getBillStatus().equals(CheckBillStatusEnum.BAD_BILL.getCode())){
						mes+="当前账单"+blist.get(0).getInvoiceName()+"坏账状态，无法导入";
					}else if(blist.get(0).getBillStatus().equals(CheckBillStatusEnum.INVALIDATE.getCode())){
						mes+="当前账单"+blist.get(0).getInvoiceName()+"作废状态，无法导入";
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
			
			//跟进类型判断
			if(StringUtils.isNotBlank(vo.getFollowType())){
				if(getStatusMap().containsKey(vo.getFollowType())){
					vo.setFollowType(getStatusMap().get(vo.getFollowType()).toString());
				}else{
					mes+="跟进类型不存在;";
					//infoList.add(new ErrorMessageVo(index,"跟进类型不存在"));
					//break;
				}
			}else{
				mes+="跟进类型不能为空;";
				//infoList.add(new ErrorMessageVo(index,"跟进类型不能为空"));
				//break;
			}
			
			/*String dataKey=vo.getCreateMonth()+"&"+vo.getInvoiceName();
			if(excelMap.containsKey(dataKey)){
				mes+="Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复;";
				//数据有重复
				//infoList.add(new ErrorMessageVo(index,"Excel中第"+index+"行与第"+excelMap.get(dataKey).toString()+"行数据重复"));
				excelMap.put(dataKey, index+"");
				break;
			}else{
				excelMap.put(dataKey, index+"");
			}*/
				
			
			//预计回款金额判断保留两位小数
			if(vo.getExpectReceiptMoney()!=null){
				double money=vo.getExpectReceiptMoney().doubleValue();
				money=getTeshu(money);
				BigDecimal newMoney=BigDecimal.valueOf(money);
				vo.setExpectReceiptMoney(newMoney);
			}
			
			vo.setCreator(JAppContext.currentUserName());
			vo.setCreateTime(JAppContext.currentTimestamp());
			vo.setDelFlag("0");
			list.add(vo);
			if(StringUtils.isNotBlank(mes)){
				infoList.add(new ErrorMessageVo(index,mes));
			}
		}
		return list;
		//return list;
	}

	@Override
	protected void saveDataBatch(List<BillReceiptFollowVo> list) throws Exception {
		int result=billCheckInfoService.saveReceiptFollowList(list);
		if(result>0){
			for(BillReceiptFollowVo vo:list){
				//查询账单
				Map<String, Object> condition=new HashMap<String, Object>();
				condition.put("id", vo.getBillCheckId());
				/*condition.put("billName",vo.getBillName());*/
				List<BillCheckInfoVo> blist=billCheckInfoService.queryList(condition);
				if(blist.size()>0){
					//更新账单
					BillReceiptFollowVo receiptFollowVo=billCheckInfoService.queryReceiptFollow(condition);
					if(receiptFollowVo!=null){
						blist.get(0).setExpectReceiptDate(receiptFollowVo.getExpectReceiptDate());
						billCheckInfoService.update(blist.get(0));
					}
					//新增日志
						String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
						try {
							BillCheckLogVo billCheckLogVo=new BillCheckLogVo();
							
							billCheckLogVo.setBillCheckId(blist.get(0).getId());
							billCheckLogVo.setBillStatusCode(blist.get(0).getBillStatus());
							billCheckLogVo.setOperateDesc("回款跟进导入");
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
	
	private Map<String, Object> getStatusMap(){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("法律程序", "LEGAL_PROCESS");
		map.put("跟进中", "FOLLOWING");
		map.put("坏账", "BAD_BILL");
		map.put("已回款", "RECEIPTED");
		map.put("异常", "EXCEPTION");
		map.put("暂停", "PAUSE");

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
