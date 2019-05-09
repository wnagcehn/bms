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
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.entity.PubCustomerLookupEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerBaseRepository;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerLookupRepository;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerRepository;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerBaseVo;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Service("customerDictService")
public class CustomerDictService implements ICustomerDictService {

	private static Logger logger = LoggerFactory.getLogger(CustomerDictService.class);

	@Autowired
	ICustomerService customerService;
	@Autowired
	IPubCustomerRepository pubCustomerRepository;
	@Autowired
	IPubCustomerBaseRepository pubCustomerBaseRepository;
	@Autowired
	IPubCustomerLookupRepository pubCustomerLookupRepository;
	@Autowired
	private IRedisClient redisClient;

	@Override
	public Map<String, String> getCustomerDictForKey() {

		Map<String, String> map = new HashMap<>();
		List<CustomerVo> list = customerService.queryAll();
		for (CustomerVo vo : list) {
			map.put(vo.getCustomerid(), vo.getCustomername());
		}
		return map;
	}

	@Override
	public Map<String, String> getCustomerDictForValue() {
		Map<String, String> map = new HashMap<>();
		List<CustomerVo> list = customerService.queryAll();
		for (CustomerVo vo : list) {
			map.put(vo.getCustomername(), vo.getCustomerid());
		}
		return map;
	}

	@Override
	public String getCustomerNameByCode(final String code) {

		CustomerVo result = redisClient.get(code,
				RedisCache.CUSTOMERCODE_SPACE, CustomerVo.class,
				new GetDataCallBack<CustomerVo>() {

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
		if (result == null) {
			logger.info("未查询到商家信息 code:{}", code);
			return null;
		} else {
			return result.getCustomername();
		}
	}

	@Override
	public String getCustomerCodeByName(final String name) {
		CustomerVo result = redisClient.get(name,
				RedisCache.CUSTOMERNAME_SPACE, CustomerVo.class,
				new GetDataCallBack<CustomerVo>() {

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
		if (result == null) {
			logger.info("未查询到商家信息 name:{}", name);
			return null;
		} else {
			return result.getCustomerid();
		}
	}

	private Map<String, CustomerVo> getCustomersDictForValue() {
		Map<String, CustomerVo> map = new HashMap<>();
		List<CustomerVo> list = customerService.queryAll();
		for (CustomerVo vo : list) {
			map.put(vo.getCustomername(), vo);
		}
		return map;
	}

	@Override
	public String getMkInvoiceNameByCustomerId(final String customerId) {
		
		String result = redisClient.get(customerId, RedisCache.MKINVOICENAMEBYCUSTOMERID_SPACE,String.class, new GetDataCallBack<String>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public String invoke() {
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("delFlag", "0");
				condition.put("customerId", customerId);
				List<PubCustomerEntity> list = pubCustomerRepository.query(condition);
				if(list==null||list.size()==0){
					return null;	
				}
				Map<String,Object> conditionBase = new HashMap<String, Object>();
				conditionBase.put("delFlag", "0");
				conditionBase.put("mkId", list.get(0).getMkId());
				List<PubCustomerBaseEntity> listBase  = pubCustomerBaseRepository.query(conditionBase );
				if(listBase==null||listBase.size()==0){ 
					return null;
				}
				return listBase.get(0).getMkInvoiceName();
			}
		});
		if(result == null){
			logger.info("未查询到合同商家名称 customerId:{}",customerId);
			return null;
		}
		logger.info("result:",result);
		return result;
	}

	@Override
	public String getMkIdByMkInvoiceName(final String mkInvoiceName) {
		String result = redisClient.get(mkInvoiceName,
				RedisCache.MKINVOICEID_SPACE, String.class,
				new GetDataCallBack<String>() {

					@Override
					public int getExpiredTime() {
						return RedisCache.expiredTime;
					}

					@Override
					public String invoke() {
						Map<String, Object> condition = new HashMap<String, Object>();
						condition.put("delFlag", "0");
						condition.put("mkInvoiceName", mkInvoiceName);
						List<PubCustomerBaseEntity> list = pubCustomerBaseRepository
								.query(condition);
						if (list == null || list.size() == 0) {
							return null;
						} else {
							String mkId = list.get(0).getMkId();
							return mkId;
						}
					}
				});
		if (result == null) {
			logger.info("未查询到合同商家ID mkInvoiceName:{}", mkInvoiceName);
			return null;
		} else {
			return result;
		}
	}

	@Override
	public String getMkInvoiceNameByMkId(final String mkId) {
		String result = redisClient.get(mkId, RedisCache.MKINVOICENAME_SPACE,
				String.class, new GetDataCallBack<String>() {

					@Override
					public int getExpiredTime() {
						return RedisCache.expiredTime;
					}

					@Override
					public String invoke() {
						Map<String, Object> condition = new HashMap<String, Object>();
						condition.put("delFlag", "0");
						condition.put("mkId", mkId);
						List<PubCustomerBaseEntity> list = pubCustomerBaseRepository
								.query(condition);
						if (list == null || list.size() == 0) {
							return null;
						} else {
							String mkInvoiceName = list.get(0)
									.getMkInvoiceName();
							return mkInvoiceName;
						}
					}
				});
		if (result == null) {
			logger.info("未查询到合同商家名称 mkId:{}", mkId);
			return null;
		}
		return result;
	}

	@Override
	public PageInfo<PubCustomerBaseVo> queryPubCustomerBase(
			Map<String, Object> condition, int pageNo, int pageSize) {
		PageInfo<PubCustomerBaseVo> page = new PageInfo<PubCustomerBaseVo>();
		PageInfo<PubCustomerBaseEntity> pageEntity = pubCustomerBaseRepository
				.query(condition, pageNo, pageSize);
		if (pageEntity != null) {
			try {
				PropertyUtils.copyProperties(page, pageEntity);
			if(pageEntity.getList()!=null&&pageEntity.getList().size()>0){
				for(int i=0;pageEntity.getList().size()>i;i++){
					PubCustomerBaseVo vo = new PubCustomerBaseVo();
					PropertyUtils.copyProperties(vo, pageEntity.getList().get(i));
					page.getList().set(i, vo);
				}
			}
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				logger.info("转换失败 pageEntity:{}", pageEntity);
				e.printStackTrace();
				return null;
			}
		}
		return page;
	}

	@Override
	public PageInfo<PubCustomerVo> queryPubCustomer(
			Map<String, Object> condition, int pageNo, int pageSize) {
		PageInfo<PubCustomerVo> page = new PageInfo<PubCustomerVo>();
		PageInfo<PubCustomerEntity> pageEntity = pubCustomerRepository.queryPage(condition, pageNo, pageSize);

		if (pageEntity != null) {
			try {
				PropertyUtils.copyProperties(page, pageEntity);
			if(pageEntity.getList()!=null&&pageEntity.getList().size()>0){
				for(int i=0;pageEntity.getList().size()>i;i++){
					PubCustomerVo vo = new PubCustomerVo();
					PropertyUtils.copyProperties(vo, pageEntity.getList().get(i));
					page.getList().set(i, vo);
				}
			}
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				logger.info("转换失败 pageEntity:{}", pageEntity);
				e.printStackTrace();
				return null;
			}
		}
		
		return page;
	}
	
	@Override
	public PageInfo<PubCustomerLookupEntity> queryPubCustomerLookup(
			Map<String, Object> condition, int pageNo, int pageSize) {
		PageInfo<PubCustomerLookupEntity> pageEntity = pubCustomerLookupRepository.query(condition, pageNo, pageSize);
		return pageEntity;
	}
	
	@Override
	public List<PubCustomerEntity> queryAllCus(Map<String, Object> condition){
		return pubCustomerRepository.query(condition);
	}

	@Override
	public PubCustomerVo queryById(String customerId) {
		Map<String, Object> map = new HashMap<>();
		map.put("customerId", customerId);
		map.put("delFlag", "0");
		List<PubCustomerEntity> list = pubCustomerRepository.query(map);
		if(list == null || list.size() == 0){
			return null;
		}
		PubCustomerVo vo = new PubCustomerVo();
		try {
			PropertyUtils.copyProperties(vo,list.get(0));
		} catch (Exception ex) {
			logger.info("转换失败 ", ex);
		}
		return vo;
	}

}
