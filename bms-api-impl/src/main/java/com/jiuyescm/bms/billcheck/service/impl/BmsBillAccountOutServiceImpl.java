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
		int amount =  (int) param.get("amount");
		int  unReceiptAmount = (int) param.get("unReceiptAmount");
		
		String status="";
		String accountNo =  (String) param.get("accountNo");
		int idInt =  (int) param.get("id");
		Long id = Long.valueOf(idInt);
		Map<String,Object> conditionAccount = new HashMap<String,Object>();
		conditionAccount.put("accountNo", accountNo);
		Map<String,Object> conditionCheck = new HashMap<String,Object>();
		conditionCheck.put("id", id);
		
		BillAccountOutEntity entity = new BillAccountOutEntity();
		entity.setAccountNo(accountNo);
		entity.setBillCheckId(idInt);
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreatorId(JAppContext.currentUserID());
		entity.setDelFlag("0");
		entity.setOutType("1");
	
		//插入支出表
		if(unReceiptAmount==0){
			status = "未能冲抵，未收款金额为0";
		}else if(amount==0){
			status = "未能冲抵，账户金额为0";
		}else if(amount>=unReceiptAmount){
			//插入支出表
			entity.setAmount(new BigDecimal(unReceiptAmount));
			billAccountOutRepository.save(entity);
			//修改账户表
			BillAccountInfoEntity account = billAccountInfoRepository.query(conditionAccount, 1, 20).getList().get(0);
			account.setAmount(new BigDecimal(amount-unReceiptAmount));
			billAccountInfoRepository.update(account);
			//修改账单表
			BillCheckInfoEntity check = billCheckInfoRepository.query(conditionCheck,1,20).getList().get(0);
			int zeroInt = 0;
			BigDecimal zero = new BigDecimal(zeroInt);
			check.setUnReceiptAmount(zero);
			billCheckInfoRepository.update(check);

			status = "冲抵成功";
		}else {
			//插入支出表
			entity.setAmount(new BigDecimal(amount));
			billAccountOutRepository.save(entity);
			//修改账户表
			BillAccountInfoEntity account = billAccountInfoRepository.query(conditionAccount, 1, 20).getList().get(0);
			int zeroInt = 0;
			BigDecimal zero = new BigDecimal(zeroInt);
			account.setAmount(zero);
			billAccountInfoRepository.update(account);
			//修改账单表
			BillCheckInfoEntity check = billCheckInfoRepository.query(conditionCheck,1,20).getList().get(0);
			check.setUnReceiptAmount(new BigDecimal(unReceiptAmount-amount));
			billCheckInfoRepository.update(check);
			
			status = "部分冲抵，还有未收款余额";
		}

		return status;
	}

}
