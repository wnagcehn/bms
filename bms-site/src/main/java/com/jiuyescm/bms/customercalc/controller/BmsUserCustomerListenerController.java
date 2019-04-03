package com.jiuyescm.bms.customercalc.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.jiuyescm.bms.asyn.entity.BmsUserCustomerListenerEntity;
import com.jiuyescm.bms.asyn.service.IBmsUserCustomerListenerService;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsUserCustomerListenerController")
public class BmsUserCustomerListenerController {

	@Autowired
	private IBmsUserCustomerListenerService bmsUserCustomerListenerService;

	/**
	 * 订购
	 * @param param
	 * @return
	 */
	@Expose
	public String save(Map<String, Object> param) {
		if (param == null) {
			throw new BizException("参数错误！");
		}
		if (null == param.get("customerId")) {
			throw new BizException("订购的商家ID为空！");
		}
		String userId = ContextHolder.getLoginUserName();
		param.put("crePersonId", userId);
		param.put("delFlag", "0");
		//查询此人是否订购该商家
		List<BmsUserCustomerListenerEntity> list = bmsUserCustomerListenerService.query(param);
		if (CollectionUtils.isNotEmpty(list)) {
			return "您已订购过该商家，请勿重复订购!";
		}
		BmsUserCustomerListenerEntity entity = new BmsUserCustomerListenerEntity();
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		entity.setCrePersonId(userId);
		entity.setDelFlag("0");
		entity.setCustomerId(param.get("customerId").toString());
		int k = bmsUserCustomerListenerService.save(entity);
		return k>0?"订购成功！":"订购失败！";
	}
	
	/**
	 * 取消订购
	 * @param param
	 * @return
	 */
	@Expose
	public void cancel(Map<String, Object> param){
		if (null == param.get("customerId")) {
			throw new BizException("订购的商家ID为空！");
		}
		BmsUserCustomerListenerEntity entity = new BmsUserCustomerListenerEntity();
		String userId = ContextHolder.getLoginUserName();
		entity.setCrePersonId(userId);
		entity.setCustomerId(param.get("customerId").toString());
		entity.setDelFlag("1");
		bmsUserCustomerListenerService.update(entity);
	}
	
}
