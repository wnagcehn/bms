package com.jiuyescm.bms.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.ICarrierDictService;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;

@Service("carrierDictService")
public class CarrierDictService implements ICarrierDictService {

	private static Logger Logger = LoggerFactory.getLogger(CarrierDictService.class);
	
	@Autowired ICarrierService carrierService;
	@Autowired private IRedisClient redisClient;
	
	@Override
	public Map<String, String> getCarrierDictForKey() {

		Map<String, String> map = new HashMap<>();
		List<CarrierVo> list =  carrierService.queryAllCarrier();
		for (CarrierVo vo : list) {
			map.put(vo.getCarrierid(), vo.getName());
		}
		return map;
	}

	@Override
	public Map<String, String> getCarrierDictForValue() {
		Map<String, String> map = new HashMap<>();
		List<CarrierVo> list =  carrierService.queryAllCarrier();
		for (CarrierVo vo : list) {
			map.put(vo.getName(),vo.getCarrierid());
		}
		return map;
	}
	
	private Map<String, CarrierVo> getCarriersDictForValue(){
		Map<String, CarrierVo> map = new HashMap<>();
		List<CarrierVo> list =  carrierService.queryAllCarrier();
		for (CarrierVo vo : list) {
			map.put(vo.getName(),vo);
		}
		return map;
	}

	@Override
	public String getCarrierNameByCode(final String code) {
		
		CarrierVo result = redisClient.get(code, RedisCache.CARRIERCODE_SPACE,CarrierVo.class, new GetDataCallBack<CarrierVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public CarrierVo invoke() {
				CarrierVo vo = carrierService.queryCarrierByCarrierid(code);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到物流商信息 code:{}",code);
			return null;
		}
		else{
			return result.getName();
		}
	}
	

	@Override
	public String getCarrierCodeByName(final String name) {
		CarrierVo result = redisClient.get(name, RedisCache.CARRIERNAME_SPACE,CarrierVo.class, new GetDataCallBack<CarrierVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public CarrierVo invoke() {
				Map<String, CarrierVo> map = getCarriersDictForValue();
				CarrierVo vo = map.get(name);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到物流商信息 name:{}",name);
			return null;
		}
		else{
			return result.getCarrierid();
		}
	}

}
