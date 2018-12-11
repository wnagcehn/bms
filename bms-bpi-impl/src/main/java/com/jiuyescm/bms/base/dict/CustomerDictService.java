package com.jiuyescm.bms.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Service("customerDictService")
public class CustomerDictService implements ICustomerDictService {

	private static Logger Logger = LoggerFactory.getLogger(CustomerDictService.class);
	
	@Autowired ICustomerService customerService;
	@Autowired private IRedisClient redisClient;
	
	@Override
	public Map<String, String> getCustomerDictForKey() {

		Map<String, String> map = new HashMap<>();
		List<CustomerVo> list =  customerService.queryAll();
		for (CustomerVo vo : list) {
			map.put(vo.getCustomerid(), vo.getCustomername());
		}
		return map;
	}

	@Override
	public Map<String, String> getCustomerDictForValue() {
		Map<String, String> map = new HashMap<>();
		List<CustomerVo> list =  customerService.queryAll();
		for (CustomerVo vo : list) {
			map.put(vo.getCustomername(),vo.getCustomerid());
		}
		return map;
	}

	@Override
	public String getCustomerNameByCode(final String code) {
		
		CustomerVo result = redisClient.get(code, RedisCache.CUSTOMERCODE_SPACE,CustomerVo.class, new GetDataCallBack<CustomerVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public CustomerVo invoke() {
				CustomerVo vo = customerService.queryByCustomerId(code);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到商家信息 code:{}",code);
			return null;
		}
		else{
			return result.getCustomername();
		}
	}

	@Override
	public String getCustomerCodeByName(final String name) {
		CustomerVo result = redisClient.get(name, RedisCache.CUSTOMERNAME_SPACE,CustomerVo.class, new GetDataCallBack<CustomerVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public CustomerVo invoke() {
				Map<String, CustomerVo> map = getCustomersDictForValue();
				CustomerVo vo = map.get(name);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到商家信息 name:{}",name);
			return null;
		}
		else{
			return result.getCustomerid();
		}
	}

	private Map<String, CustomerVo> getCustomersDictForValue(){
		Map<String, CustomerVo> map = new HashMap<>();
		List<CustomerVo> list =  customerService.queryAll();
		for (CustomerVo vo : list) {
			map.put(vo.getCustomername(),vo);
		}
		return map;
	}

	@Override
	public String getMkInvoiceNameById(final String customerid) {
		
		CustomerVo result = redisClient.get(customerid, RedisCache.MKINVOICENAME_SPACE,CustomerVo.class, new GetDataCallBack<CustomerVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public CustomerVo invoke() {
				CustomerVo vo = customerService.queryByCustomerId(customerid);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到商家信息 customerid:{}",customerid);
			return null;
		}
		else{
			return result.getMkInvoiceName();
		}
		

	}

	@Override
	public String getMkInvoiceIdByMkInvoiceName(final String mkInvoiceName) {
		String result = redisClient.get(mkInvoiceName, RedisCache.MKINVOICEID_SPACE,String.class, new GetDataCallBack<String>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public String invoke() {
				CustomerVo vo = customerService.queryByCustomerId(mkInvoiceName);
				return null;
			}
		});
		if(result == null){
			Logger.info("未查询到商家信息 mkInvoiceName:{}",mkInvoiceName);
			return null;
		}
		else{
			return result;
		}
	}

}
