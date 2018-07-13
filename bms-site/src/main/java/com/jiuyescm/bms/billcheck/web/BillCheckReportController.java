package com.jiuyescm.bms.billcheck.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInvoiceService;
import com.jiuyescm.bms.billcheck.service.IBillCheckReceiptService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptSumVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptVo;
import com.jiuyescm.bms.billcheck.vo.BillExpectReceiptVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("billCheckReportController")
public class BillCheckReportController {
	
	@Resource
	private IBillCheckInfoService billCheckInfoService;
	
	@Resource
	private IBillCheckInvoiceService billCheckInvoiceService;
	
	@Resource
	private IBillCheckReceiptService billCheckReceiptService;
	
	@Resource
	private IBmsGroupUserService bmsGroupUserService;
	/**
	 * 分页查询收款明细表
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryReceiptDetail(Page<BillCheckInfoVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}

		List<String> userIds=new ArrayList<String>();
		BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if(groupUser!=null){//加入權限組
			//判断是否是管理员
			if("0".equals(groupUser.getAdministrator())){//管理员
				
			}else{//非管理员
				userIds=bmsGroupUserService.queryContainUserIds(groupUser);
				StringBuffer user=new StringBuffer();
				for(int i=0;i<userIds.size();i++){
					if(i==userIds.size()-1){
						user.append(userIds.get(i));
					}else{
						user.append(userIds.get(i)+"|");
					}				
				}
				if(StringUtils.isNotBlank(user)){
					param.put("userIds", user);
				}
			}	
		
			PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	
	/**
	 * 分页查询收款明细表
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryReceiptSum(Page<BillCheckReceiptSumVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}

		PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, 0, Integer.MAX_VALUE);
		
		Map<String, BillCheckReceiptSumVo> map=new HashMap<String, BillCheckReceiptSumVo>();			

		if(pageInfo!=null && pageInfo.getList().size()>0){		
			//key:部门  value:实体类
			List<BillCheckInfoVo> list=pageInfo.getList();		
			for(BillCheckInfoVo vo:list){
				//部门
				if(StringUtils.isNotBlank(vo.getDeptName())){
					String deptName=vo.getDeptName();
					if(map.containsKey(deptName)){
						//部门名称已存在
						BillCheckReceiptSumVo sumVo=map.get(deptName);
						sumMoney(sumVo,vo);
					}else{
						//部门不存在时
						BillCheckReceiptSumVo sumVo=new BillCheckReceiptSumVo();
						sumVo.setDeptName(deptName);
						sumMoney(sumVo,vo);
						map.put(deptName, sumVo);
					}
					
				}
			}
		}
		List<BillCheckReceiptSumVo> sumList=new ArrayList<BillCheckReceiptSumVo>();
		
		for(String key : map.keySet()) { 
			BillCheckReceiptSumVo value = map.get(key); 
			sumList.add(value);
		}
		
		if (sumList.size()>0) {
			page.setEntities(sumList);
			page.setEntityCount(sumList.size());
		}
		
	}
	
	
	/**
	 * 分页查询发票明细表
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryInvoiceReport(Page<BillCheckInvoiceVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}
		PageInfo<BillCheckInvoiceVo> pageInfo = billCheckInvoiceService.queryReport(param, page.getPageNo(), page.getPageSize());
		//PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询收款明细表
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryReceiptReport(Page<BillCheckReceiptVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}
		PageInfo<BillCheckReceiptVo> pageInfo = billCheckReceiptService.queryReport(param, page.getPageNo(), page.getPageSize());
		//PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 预计回款信息查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryExpectReceiptReport(Page<BillExpectReceiptVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}

		List<BillExpectReceiptVo> volist=new ArrayList<BillExpectReceiptVo>();
		
		PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null && pageInfo.getList().size()>0) {
			List<BillCheckInfoVo> list=pageInfo.getList();	
			
			
			//确认金额汇总
			BigDecimal totalConfirmAmount=new BigDecimal(0);
			//截止日前已回款金额汇总
			BigDecimal totalNowReceiptMoney=new BigDecimal(0);
			//截止日前剩余待回款金额汇总
			BigDecimal totalNowTbReceiptMoney=new BigDecimal(0);
			//调整金额汇总
			BigDecimal totalAdjustMoney=new BigDecimal(0);
			
			
			for(BillCheckInfoVo vo:list){
				BigDecimal money=new BigDecimal(0);
				BillExpectReceiptVo entity=new BillExpectReceiptVo();
				entity.setCreateMonth(vo.getCreateMonth());
				entity.setInvoiceName(vo.getInvoiceName());
				entity.setBillName(vo.getBillName());
				entity.setBillStatus(vo.getBillStatus());
				entity.setInvoiceStatus(vo.getInvoiceStatus());
				entity.setReceiptStatus(vo.getReceiptStatus());
				entity.setSellerId(vo.getSellerId());
				entity.setSellerName(vo.getSellerName());
				entity.setConfirmAmount(vo.getConfirmAmount()==null?money:vo.getConfirmAmount());
				entity.setNowTbReceiptMoney(vo.getConfirmAmount()==null?money:vo.getConfirmAmount());
				entity.setNowReceiptMoney(money);
				entity.setAdjustMoney(money);
				entity.setArea(vo.getArea());
			
				param.put("id", vo.getId());
				param.put("receiptDate", param.get("expireDate"));
				BillCheckReceiptVo billCheckReceiptVo=billCheckReceiptService.queyReceipt(param);
				if(billCheckReceiptVo!=null){
					entity.setNowLastReceiptDate(billCheckReceiptVo.getReceiptDate());
					entity.setNowReceiptMoney(billCheckReceiptVo.getReceiptAmount()==null?money:billCheckReceiptVo.getReceiptAmount());
					if(billCheckReceiptVo.getReceiptAmount()!=null){
						entity.setNowTbReceiptMoney(vo.getConfirmAmount().subtract(billCheckReceiptVo.getReceiptAmount()));
					}
				}
				
				//预计回款日期
				param.put("expectReceiptDate", param.get("expireDate"));
				BillReceiptFollowVo receiptFollowVo=billCheckInfoService.queryReceiptFollow(param);
				if(receiptFollowVo!=null){
					entity.setNowLastExpectReceiptDate(receiptFollowVo.getExpectReceiptDate());
				}
				
				//调整金额
				param.put("billCheckId", vo.getId());
				BillCheckAdjustInfoVo adjustVo=billCheckInfoService.queryOneAdjust(param);
				if(adjustVo!=null && adjustVo.getAdjustAmount()!=null){
					entity.setAdjustMoney(adjustVo.getAdjustAmount()==null?money:adjustVo.getAdjustAmount());
				}
				
				
				//汇总
				totalConfirmAmount=totalConfirmAmount.add(entity.getConfirmAmount());
				totalNowReceiptMoney=totalNowReceiptMoney.add(entity.getNowReceiptMoney());
				totalNowTbReceiptMoney=totalNowTbReceiptMoney.add(entity.getNowTbReceiptMoney());
				totalAdjustMoney=totalAdjustMoney.add(entity.getAdjustMoney());
				
				volist.add(entity);
			}
			for(BillExpectReceiptVo vo:volist){
				vo.setTotalConfirmAmount(totalConfirmAmount);
				vo.setTotalNowReceiptMoney(totalNowReceiptMoney);
				vo.setTotalNowTbReceiptMoney(totalNowTbReceiptMoney);
				vo.setTotalAdjustMoney(totalAdjustMoney);
			}
		}
	
		if (volist.size()>0) {
			page.setEntities(volist);
			page.setEntityCount((int)pageInfo.getTotal());
		}
	}
	
	
	public void sumMoney(BillCheckReceiptSumVo sumVo,BillCheckInfoVo vo){
		//开票未回款（账单已确认:待开票、待收款、已收款）
		if(CheckBillStatusEnum.TB_INVOICE.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.RECEIPTED.getCode().equals(vo.getBillStatus())){
			BigDecimal money=new BigDecimal(0);
			//包含以下数据：
			//1.是否需要发票=否，计算账单确认额-收款金额
			//2.是否需要发票=是，计算已开票和部分开票的账单确认额-收款金额
			if("0".equals(vo.getIsneedInvoice())){
				if(vo.getConfirmAmount()!=null){
					money=vo.getConfirmAmount();
				}
				if(vo.getReceiptAmount()!=null){
					money=money.subtract(vo.getReceiptAmount());
				}
				
			}else if("1".equals(vo.getIsneedInvoice())){
				//已开票和部分开票
				if(BillCheckInvoiceStateEnum.INVOICED.getCode().equals(vo.getInvoiceStatus()) || BillCheckInvoiceStateEnum.PART_INVOICE.getCode().equals(vo.getInvoiceStatus())){
					if(vo.getConfirmAmount()!=null){
						money=vo.getConfirmAmount();
					}
					if(vo.getReceiptAmount()!=null){
						money=money.subtract(vo.getReceiptAmount());
					}
				}
			}
			if(sumVo.getInvoiceUnReceiptAmount()==null){
				sumVo.setInvoiceUnReceiptAmount(money);
			}else{
				sumVo.setInvoiceUnReceiptAmount(sumVo.getInvoiceUnReceiptAmount().add(money));
			}
			if(sumVo.getTotalAmount()==null){
				sumVo.setTotalAmount(money);
			}else{
				sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
			}
			
		}
		
		//未开票未回款（账单已确认:待开票、待收款、已收款）
		if(CheckBillStatusEnum.TB_INVOICE.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.RECEIPTED.getCode().equals(vo.getBillStatus())){
			BigDecimal money=new BigDecimal(0);
			//包含以下数据：
			//1.是否需要发票=是，计算未开票的账单确认额-收款金额
			if("1".equals(vo.getIsneedInvoice())){
				if(BillCheckInvoiceStateEnum.NO_INVOICE.getCode().equals(vo.getInvoiceStatus())){
					if(vo.getConfirmAmount()!=null){
						money=vo.getConfirmAmount();
					}
					if(vo.getReceiptAmount()!=null){
						money=money.subtract(vo.getReceiptAmount());
					}				
				}
			}
			if(sumVo.getUnInvoiceUnReceiptAmount()==null){
				sumVo.setUnInvoiceUnReceiptAmount(money);
			}else{
				sumVo.setUnInvoiceUnReceiptAmount(sumVo.getUnInvoiceUnReceiptAmount().add(money));
			}
			if(sumVo.getTotalAmount()==null){
				sumVo.setTotalAmount(money);
			}else{
				sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
			}
		}
		
		//账单未确认
		if(CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(vo.getBillStatus())){
			//包含以下数据：
			//1.计算账单确认额-收款金额
			BigDecimal money=new BigDecimal(0);
			if(vo.getConfirmAmount()!=null){
				money=vo.getConfirmAmount();
			}
			if(vo.getReceiptAmount()!=null){
				money=money.subtract(vo.getReceiptAmount());
			}		
			if(sumVo.getUnConfirmAmount()==null){
				sumVo.setUnConfirmAmount(money);
			}else{
				sumVo.setUnConfirmAmount(sumVo.getUnConfirmAmount().add(money));
			}
			if(sumVo.getTotalAmount()==null){
				sumVo.setTotalAmount(money);
			}else{
				sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
			}
		}
	}
	
}
