package com.jiuyescm.bms.base.dict;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerBaseRepository;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerRepository;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerBaseVo;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Service("customerDictService")
public class CustomerDictService implements ICustomerDictService {

	private static Logger logger = LoggerFactory.getLogger(CustomerDictService.class);
	
	@Autowired ICustomerService customerService;
	@Autowired IPubCustomerRepository pubCustomerRepository;
	@Autowired IPubCustomerBaseRepository pubCustomerBaseRepository;
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
			logger.info("未查询到商家信息 code:{}",code);
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
			logger.info("未查询到商家信息 name:{}",name);
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
	public String getMkInvoiceNameByCustomerId(final String customerId) {
		
		CustomerVo result = redisClient.get(customerId, RedisCache.MKINVOICENAME_SPACE,CustomerVo.class, new GetDataCallBack<CustomerVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public CustomerVo invoke() {
				CustomerVo vo = customerService.queryByCustomerId(customerId);
				return vo;
			}
		});
		if(result == null){
			logger.info("未查询到合同商家名称 customerId:{}",customerId);
			return null;
		}
		else{
			return result.getMkInvoiceName();
		}
	}

	@Override
	public String getMkIdByMkInvoiceName(final String mkInvoiceName) {
		String result = redisClient.get(mkInvoiceName, RedisCache.MKINVOICENAMEBYCUSTOMERID_SPACE,String.class, new GetDataCallBack<String>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public String invoke() {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("delFlag", "0");
				condition.put("mkInvoiceName", mkInvoiceName);
				List<PubCustomerBaseEntity> list = pubCustomerBaseRepository.query(condition);
				if(list==null||list.size()==0){
					return null;
				}else{
					String mkId = list.get(0).getMkId();
					return mkId;
				}
			}
		});
		if(result == null){
			logger.info("未查询到合同商家ID mkInvoiceName:{}",mkInvoiceName);
			return null;
		}
		else{
			return result;
		}
	}

	@Override
	public String getMkInvoiceNameByMkId(final String mkId) {
		String result = redisClient.get(mkId, RedisCache.MKINVOICENAMEBYCUSTOMERID_SPACE,String.class, new GetDataCallBack<String>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public String invoke() {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("delFlag", "0");
				condition.put("mkId", mkId);
				List<PubCustomerBaseEntity> list = pubCustomerBaseRepository.query(condition);
				if(list==null||list.size()==0){
					return null;
				}else{
					String mkInvoiceName = list.get(0).getMkInvoiceName();
					return mkInvoiceName;
				}
			}
		});
		if(result == null){
			logger.info("未查询到合同商家名称 mkId:{}",mkId);
			return null;
		}
		else{
			return result;
		}
	}

	@Override
	public PageInfo<PubCustomerBaseVo> queryPubCustomerBase(Map<String, Object> condition,int pageNo,int pageSize) {
		PageInfo<PubCustomerBaseVo> page = new PageInfo<PubCustomerBaseVo>();
		condition.put("delFlag",  "0");
		PageInfo<PubCustomerBaseEntity> pageEntity = pubCustomerBaseRepository.query(condition, pageNo, pageSize);
		if(pageEntity!=null){
		try {
			PropertyUtils.copyProperties(page, pageEntity);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			logger.info("转换失败 pageEntity:{}",pageEntity);
			e.printStackTrace();
			return null;
		}
		
		}
			return page;
	}

}
