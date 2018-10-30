package com.jiuyescm.bms.billcheck.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillAccountOutRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillAccountOutRepository;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;
import com.jiuyescm.cfm.common.JAppContext;


@Service("bmsBillAccountOutService")
public class BmsBillAccountOutServiceImpl implements IBmsAccountOutService  {

	
	@Autowired
    private IBillAccountOutRepository billAccountOutRepository;
	
	@Autowired
    private IBillAccountInfoRepository billAccountInfoRepository;
	
	@Autowired
    private IBillCheckInfoRepository billCheckInfoRepository;

	@Override
	public PageInfo<BillAccountOutEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		return billAccountOutRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public String save(Map<String, Object> param) {
		String accountNo =  (String) param.get("accountNo");
		int amount =  (int) param.get("amount");
		int  unReceiptAmount = (int) param.get("unReceiptAmount");
		
		String status="";
		BillAccountOutEntity entity = new BillAccountOutEntity();
		entity.setAccountNo(accountNo);
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreatorId(JAppContext.currentUserID());
		entity.setDelFlag("0");
		entity.setOutType("1");
	
		//插入支出表
		if(unReceiptAmount==0){
			status = "冲抵失败，未收款金额为0";
		}else if(amount==0){
			status = "冲抵失败，账户金额为0";
		}else if(amount>=unReceiptAmount){
			int outInt = amount-unReceiptAmount;
			BigDecimal out = new BigDecimal(outInt);
			entity.setAmount(out);
			billAccountOutRepository.save(entity);
			status = "冲抵成功";
		}else {
			BigDecimal out = new BigDecimal(amount);
			entity.setAmount(out);
			status = "冲抵成功，还有剩余未收款金额未冲抵";
		}
		
//		if(unReceiptAmount.compareTo(BigDecimal.ZERO)==0){
//			status = "冲抵失败，未收款金额为0";
//		}else if(amount.compareTo(BigDecimal.ZERO)==0){
//			status = "冲抵失败，账户金额为0";
//		}else if(amount.compareTo(unReceiptAmount)==0||amount.compareTo(unReceiptAmount)==1){
//			BigDecimal out = amount.subtract(unReceiptAmount);
//			entity.setAmount(out);
//			billAccountOutRepository.save(entity);
//			status = "冲抵成功";
//		}else {
//			entity.setAmount(amount);
//			status = "冲抵成功，还有剩余未收款金额未冲抵";
//		}

		return status;
	}

}
