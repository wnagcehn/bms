package com.jiuyescm.bms.billcheck.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

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
 * @author liuzhicheng
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
	
	/**
	 * 操作删除
	 * 
	 * @param vo
	 */
	@DataResolver
	public void delete(BillAccountInVo vo){
		logger.info("delete：id-{}",vo.getId());
		try{
			
			billAccountInService.delete(vo);
		}catch(Exception ex){
			logger.info("delete--",ex);
			throw ex;
		}

	}
	
	/**
	 * 操作确认
	 * 
	 * @param vo
	 */
	@DataResolver
	public void confirm(BillAccountInVo vo) {
		BillAccountInVo accountIn = billAccountInService.findById(vo.getId());
		//能否修改 取决于状态 已确认不可再次确认
		logger.info("confirm确认状态{}",accountIn.getConfirmStatus());
		if(!accountIn.getConfirmStatus().equals("1")){
			vo.setLastModifier(JAppContext.currentUserName());
			vo.setLastModifierId(JAppContext.currentUserID());
			vo.setLastModifyTime(JAppContext.currentTimestamp());
			vo.setConfirmTime(JAppContext.currentTimestamp());
			billAccountInService.confirm(vo);
			
			//查询账户
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("customerName", vo.getCustomerName());
			List<BillAccountInfoVo> accountList =  billAccountInfoService.query(condition, 1, 20).getList();
			//修改账户表金额
			BillAccountInfoVo account = accountList.get(0);
			BigDecimal amount = vo.getAmount();
			BigDecimal accountAmount = account.getAmount();
			BigDecimal accountAmountAdd = accountAmount.add(amount);
			account.setAmount(accountAmountAdd);
			account.setLastModifier(JAppContext.currentUserName());
			account.setLastModifierId(JAppContext.currentUserID());
			account.setLastModifyTime(JAppContext.currentTimestamp());
			billAccountInfoService.update(account);
		}else{
			throw new BizException("未确认状态才能确认");
		}
	}

	@DataResolver
	public void save(BillAccountInVo entity) {
		logger.info("save：{}",entity);
		Timestamp time = JAppContext.currentTimestamp();
		String user = JAppContext.currentUserName();
		String userId = JAppContext.currentUserID();
		
		//录入保存
		if(EntityState.NEW.equals(EntityUtils.getState(entity))){
			logger.info("save预收款录入");
			//查询账户
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("customerName", entity.getCustomerName());
			List<BillAccountInfoVo> accountList =  billAccountInfoService.query(condition, 1, 20).getList();
			logger.info("save账户信息{}",accountList);
			//账户是否存在 存在 写入    不存在 创建账户+写入
			if(accountList != null && accountList.size() > 0){
				//写入录入表
				entity.setCreator(user);
				entity.setCreatorId(userId);
				entity.setCreateTime(time);
				entity.setLastModifier(user);
				entity.setLastModifierId(userId);
				entity.setLastModifyTime(time);
				entity.setDelFlag("0");
				entity.setConfirmStatus("0");
				billAccountInService.save(entity);
				//修改账户表金额
//				BillAccountInfoVo account = accountList.get(0);
//				BigDecimal amount = entity.getAmount();
//				BigDecimal accountAmount = account.getAmount();
//				BigDecimal accountAmountAdd = accountAmount.add(amount);
//				account.setAmount(accountAmountAdd);
//				account.setLastModifier(user);
//				account.setLastModifierId(userId);
//				account.setLastModifyTime(time);
//				billAccountInfoService.update(account);
			}else{
				//创建新账户
				BillAccountInfoVo accountVo = new BillAccountInfoVo();
				accountVo.setCustomerId(entity.getCustomerId());
				accountVo.setCustomerName(entity.getCustomerName());
				String accountNo = sequenceService.getBillNoOne(BillAccountInfoVo.class.getName(), "3131", "000000");
				accountVo.setAccountNo(accountNo);
				accountVo.setCreator(user);
				accountVo.setCreatorId(userId);
				accountVo.setCreateTime(time);
				accountVo.setDelFlag("0");
				accountVo.setAmount(new BigDecimal(0));
				billAccountInfoService.save(accountVo);
				//写入录入表
				entity.setCreator(user);
				entity.setCreatorId(userId);
				entity.setCreateTime(time);
				entity.setLastModifier(user);
				entity.setLastModifierId(userId);
				entity.setLastModifyTime(time);
				entity.setDelFlag("0");
				entity.setConfirmStatus("0");
				billAccountInService.save(entity);
			}
			

		}
		
		//操作修改
		else if(EntityState.MODIFIED.equals(EntityUtils.getState(entity))){
			BillAccountInVo accountIn = billAccountInService.findById(entity.getId());
			//能否修改 取决于状态 已确认不可修改
			logger.info("save账户信息{}",accountIn.getConfirmStatus());
			if(!accountIn.getConfirmStatus().equals("1")){
				entity.setLastModifier(user);
				entity.setLastModifierId(userId);
				entity.setLastModifyTime(time);
				billAccountInService.update(entity);
			}else{
				throw new BizException("未确认状态才能修改");
			}
			
		}

	}
	
	@DataProvider
	public Map<String, String> getConfirmStatus() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("0", "未确认");
		mapValue.put("1", "已确认");
		return mapValue;
	}
	
}
