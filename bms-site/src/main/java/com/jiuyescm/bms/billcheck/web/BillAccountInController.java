/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.bms.billcheck.vo.BillAccountInVo;
import com.jiuyescm.bms.billcheck.vo.BillAccountInfoVo;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;
/**
 * 
 * @author stevenl
 * 
 */
@Component
@Controller("billAccountInController")
public class BillAccountInController {

	private static final Logger logger = LoggerFactory.getLogger(BillAccountInController.class);

	@Resource
	private IBmsBillAccountInService billAccountInService;

	@Resource 
	private IBmsAccountInfoService billAccountInfoService;
	
	@Autowired private SequenceService sequenceService;
	
	@Expose
	public BillAccountInVo findById(Long id) throws Exception {
		BillAccountInVo entity = null;
		entity = billAccountInService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillAccountInVo> page, Map<String, Object> param) {
		String customerName = (String) param.get("mkInvoiceName");
		param.put("customerName", customerName);
		PageInfo<BillAccountInVo> pageInfo = billAccountInService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public void delete(BillAccountInVo entity){
		logger.info("delete：id-{}",entity.getId());
		BillAccountInVo vo = billAccountInService.findById(entity.getId());
		if(vo.getConfirmStatus().equals("0")){
			entity.setDelFlag("1");
			logger.info("delete：",entity);
			billAccountInService.update(entity);
		}
		else{
			throw new BizException("未确认状态才能删除");
		}
		
//		boolean ret = true;
//		logger.info("判断逻辑{},{}",ret,ret);
//		
//		if(ret){
//			
//		}
	}


	@DataResolver
	public void save(BillAccountInVo entity) {
		logger.info("save：{}",entity);
		if(EntityState.NEW.equals(EntityUtils.getState(entity))){
			logger.info("save预收款录入");
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("customerName", entity.getCustomerName());
			
			BillAccountInfoVo accountEntity = billAccountInfoService.query(condition, 1, 20).getList().get(0);
			
			
			logger.info("save账户信息{}",accountEntity);
			//账户是否存在 存在 写入    不存在 创建账户+写入
			if(accountEntity != null){
				entity.setLastModifierId(JAppContext.currentUserID());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
				entity.setLastModifier(JAppContext.currentUserName());
				billAccountInService.update(entity);
			}else{
				//创建新账户
				BillAccountInfoVo accountVo = new BillAccountInfoVo();
				accountVo.setCustomerId(entity.getCustomerId());
				accountVo.setCustomerName(entity.getCustomerName());
				String accountNo = sequenceService.getBillNoOne(BillAccountInfoVo.class.getName(), "3131", "000000");
				accountVo.setAccountNo(accountNo);
				accountVo.setCreatorId(JAppContext.currentUserID());
				accountVo.setCreateTime(JAppContext.currentTimestamp());
				accountVo.setCreator(JAppContext.currentUserName());
				accountVo.setDelFlag("0");
				BigDecimal amount = new BigDecimal(0);
				accountVo.setAmount(amount);
				billAccountInfoService.save(accountVo);
				//写入
				entity.setLastModifierId(JAppContext.currentUserID());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
				entity.setLastModifier(JAppContext.currentUserName());
				billAccountInService.update(entity);
			}
		}else if(EntityState.MODIFIED.equals(EntityUtils.getState(entity))){
			logger.info("预收款修改");
			//能否修改 取决于状态 已确认不可修改
			if(!entity.getConfirmStatus().equals("已确认")){
				entity.setLastModifierId(JAppContext.currentUserID());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
				entity.setLastModifier(JAppContext.currentUserName());
				billAccountInService.update(entity);
			}
			
		}
		
				
/*		BillAccountInfoVo accountEntity = billAccountInfoService.findByCustomerId(entity.getCustomerId());
		if(null == accountEntity){
			//创建新账户
			BillAccountInfoVo accountVo = new BillAccountInfoVo();
			accountVo.setCustomerId(entity.getCustomerId());
			accountVo.setCustomerName(entity.getCustomerName());
			String accountNo = sequenceService.getBillNoOne(BillAccountInfoVo.class.getName(), "3131", "000000");
			accountVo.setAccountNo(accountNo);
			accountVo.setCreatorId(JAppContext.currentUserID());
			accountVo.setCreateTime(JAppContext.currentTimestamp());
			
			accountVo.setCreator(JAppContext.currentUserName());
			accountVo.setDelFlag("0");
			BigDecimal amount = new BigDecimal(0);
			accountVo.setAmount(amount);
			billAccountInfoService.save(accountVo);
		}
		
		if (entity.getId() == null) {
			entity.setCreatorId(JAppContext.currentUserID());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
			billAccountInService.save(entity);
		} else if(entity.getConfirmStatus().equals("1")) {
			BillAccountInfoVo accountVo = billAccountInfoService.findByCustomerId(entity.getCustomerId()); 
			BillAccountInfoVo updateVo = new BillAccountInfoVo();

			updateVo.setId(accountVo.getId());
			BigDecimal amount = new BigDecimal(0);
			amount =accountVo.getAmount().add(entity.getAmount());
			updateVo.setAmount(amount);
		
			BillAccountInfoVo res = billAccountInfoService.update(updateVo);
			if(null != res){
				entity.setLastModifierId(JAppContext.currentUserID());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setConfirmTime(JAppContext.currentTimestamp());
				billAccountInService.update(entity);
			}
		}else{
			entity.setLastModifierId(JAppContext.currentUserID());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setLastModifier(JAppContext.currentUserName());
			billAccountInService.update(entity);
		}*/
	}
	
	@DataProvider
	public Map<String, String> getConfirmStatus() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("0", "未确认");
		mapValue.put("1", "已确认");
		return mapValue;
	}
	
}
