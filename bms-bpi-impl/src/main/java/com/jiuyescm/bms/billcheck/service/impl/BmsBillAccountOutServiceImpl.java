package com.jiuyescm.bms.billcheck.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.BillCheckReceiptEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillAccountOutRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckReceiptRepository;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;
import com.jiuyescm.bms.billcheck.vo.BillAccountOutVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;


@Service("bmsBillAccountOutService")
public class BmsBillAccountOutServiceImpl implements IBmsAccountOutService  {

	private static final Logger logger = Logger.getLogger(BmsBillAccountOutServiceImpl.class.getName());

	
	@Autowired
    private IBillAccountOutRepository billAccountOutRepository;
	
	@Autowired
    private IBillAccountInfoRepository billAccountInfoRepository;
	
	@Autowired
    private IBillCheckInfoRepository billCheckInfoRepository;
	
	@Autowired
    private IBillCheckReceiptRepository billCheckReceiptRepository;
	
	@Autowired
    private IBillCheckLogRepository billCheckLogRepository;

	@Override
	public PageInfo<BillAccountOutVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		
		PageInfo<BillAccountOutEntity> pageInfo=billAccountOutRepository.query(condition, pageNo, pageSize);
		PageInfo<BillAccountOutVo> result=new PageInfo<BillAccountOutVo>();
		
		try {
			List<BillAccountOutVo> voList = new ArrayList<BillAccountOutVo>();
	    	for(BillAccountOutEntity entity : pageInfo.getList()) {
	    		BillAccountOutVo vo = new BillAccountOutVo(); 		
	            PropertyUtils.copyProperties(vo, entity);  
	    		voList.add(vo);
	    	}
	    	result.setList(voList);
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
    	
		return result;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public String save(Map<String, Object> param) {
		String status="";
		String accountNo =  (String) param.get("accountNo");
		int idInt =  (int) param.get("id");
		Long id = Long.valueOf(idInt);
		Timestamp creTime = (Timestamp) param.get("creTime");
		String creator = (String) param.get("creator");
		String creatorId =(String) param.get("creatorId");
		
		//查询账户表
		Map<String,Object> conditionAccount = new HashMap<String,Object>();
		conditionAccount.put("accountNo", accountNo);
		BillAccountInfoEntity account = billAccountInfoRepository.query(conditionAccount, 1, 20).getList().get(0);
		//查询当前账户金额
		BigDecimal amount = account.getAmount();
		//查询账单表
		Map<String,Object> conditionCheck = new HashMap<String,Object>();
		conditionCheck.put("id", id);
		BillCheckInfoEntity check = billCheckInfoRepository.query(conditionCheck,1,20).getList().get(0);
		//查询当前账单未收款金额
		BigDecimal unReceiptAmount = check.getUnReceiptAmount();
		//回款信息
		BillCheckReceiptEntity billCheckReceiptEntity = new BillCheckReceiptEntity();
		List<BillCheckReceiptEntity> list=new ArrayList<BillCheckReceiptEntity>();
		billCheckReceiptEntity.setBillCheckId(idInt);
		billCheckReceiptEntity.setCreateTime(creTime);
		billCheckReceiptEntity.setReceiptDate(creTime);
		billCheckReceiptEntity.setCreator(creator);
		billCheckReceiptEntity.setCreatorId(creatorId);
		billCheckReceiptEntity.setReceiptType("预收款");
		billCheckReceiptEntity.setDelFlag("0");
		//日志信息
		BillCheckLogEntity log = new BillCheckLogEntity();
		log.setBillCheckId(idInt);
		log.setCreateTime(creTime);
		log.setCreator(creator);
		log.setCreatorId(creatorId);
		log.setBillStatusCode(check.getBillStatus());
		log.setDelFlag("0");
		log.setLogType(0);
		//支出信息
		BillAccountOutEntity entity = new BillAccountOutEntity();
		entity.setAccountNo(accountNo);
		entity.setBillCheckId(idInt);
		entity.setCreateTime(creTime);
		entity.setCreator(creator);
		entity.setCreatorId(creatorId);
		entity.setDelFlag("0");
		entity.setOutType("1");
		
		if(amount.compareTo(BigDecimal.ZERO)==0){
			status = "未能冲抵，账户金额为0";
		}else if(unReceiptAmount.compareTo(BigDecimal.ZERO)==0){
			status = "未能冲抵，未收款金额为0";
		}else if(amount.compareTo(unReceiptAmount)==0||amount.compareTo(unReceiptAmount)==1){
			//插入支出表
			entity.setAmount(unReceiptAmount);
			billAccountOutRepository.save(entity);
			//修改账户表
			account.setAmount(amount.subtract(unReceiptAmount));
			billAccountInfoRepository.update(account);
			//修改账单表
			int zeroInt = 0;
			BigDecimal zero = new BigDecimal(zeroInt);
			check.setUnReceiptAmount(zero);
			check.setReceiptAmount(check.getReceiptAmount().add(unReceiptAmount));
			billCheckInfoRepository.update(check);
			//插入回款表
			billCheckReceiptEntity.setReceiptAmount(unReceiptAmount);
			list.add(billCheckReceiptEntity);
			billCheckReceiptRepository.saveList(list);
			//插入日志表
			log.setOperateDesc("回款增加:预收款冲抵");
			billCheckLogRepository.addCheckLog(log);
			status = "冲抵成功";
		}else{
			//插入支出表
			entity.setAmount(amount);
			billAccountOutRepository.save(entity);
			//修改账户表
			int zeroInt = 0;
			BigDecimal zero = new BigDecimal(zeroInt);
			account.setAmount(zero);
			billAccountInfoRepository.update(account);
			//修改账单表
			check.setUnReceiptAmount(unReceiptAmount.subtract(amount));
			check.setReceiptAmount(check.getReceiptAmount().add(amount));
			billCheckInfoRepository.update(check);
			//插入回款表
			billCheckReceiptEntity.setReceiptAmount(amount);
			list.add(billCheckReceiptEntity);
			billCheckReceiptRepository.saveList(list);
			//插入日志表
			log.setOperateDesc("回款增加:预收款冲抵");
			billCheckLogRepository.addCheckLog(log);
			status = "部分冲抵，还有未收款余额";
		}

		return status;
	}

	@Override
	public void saveInfo(BillAccountOutVo vo) {
		BillAccountOutEntity entity = new BillAccountOutEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		billAccountOutRepository.save(entity);
	}

	
}
