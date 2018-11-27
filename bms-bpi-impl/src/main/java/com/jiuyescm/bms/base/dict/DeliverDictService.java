package com.jiuyescm.bms.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.IDeliverDictService;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;

@Service("deliverDictService")
public class DeliverDictService implements IDeliverDictService {

	private static Logger Logger = LoggerFactory.getLogger(DeliverDictService.class);
	
	@Autowired IDeliverService deliverService;
	@Autowired private IRedisClient redisClient;
	
	@Override
	public Map<String, String> getDeliverDictForKey() {

		Map<String, String> map = new HashMap<>();
		List<DeliverVo> list =  deliverService.queryAllDeliver();
		for (DeliverVo vo : list) {
			map.put(vo.getDeliverid(), vo.getDelivername());
		}
		return map;
	}

	@Override
	public Map<String, String> getDeliverDictForValue() {
		Map<String, String> map = new HashMap<>();
		List<DeliverVo> list =  deliverService.queryAllDeliver();
		for (DeliverVo vo : list) {
			map.put(vo.getDelivername(),vo.getDeliverid());
		}
		return map;
	}

	@Override
	public String getDeliverNameByCode(final String code) {
		
		DeliverVo result = redisClient.get(code, RedisCache.DELIVERCODE_SPACE,DeliverVo.class, new GetDataCallBack<DeliverVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public DeliverVo invoke() {
				DeliverVo vo = deliverService.queryDeliverByDeliverId(code);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到宅配商信息 code:{}",code);
			return null;
		}
		else{
			return result.getDelivername();
		}
	}
	
	private Map<String, DeliverVo> getDeliversDictForValue(){
		Map<String, DeliverVo> map = new HashMap<>();
		List<DeliverVo> list =  deliverService.queryAllDeliver();
		for (DeliverVo vo : list) {
			map.put(vo.getDelivername(),vo);
		}
		return map;
	}
	

	@Override
	public String getDeliverCodeByName(final String name) {
		DeliverVo result = redisClient.get(name, RedisCache.DELIVERNAME_SPACE,DeliverVo.class, new GetDataCallBack<DeliverVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public DeliverVo invoke() {
				Map<String, DeliverVo> map = getDeliversDictForValue();
				DeliverVo vo = map.get(name);
				return vo;
			}
		});
		if(result == null){
			Logger.info("未查询到宅配商信息 name:{}",name);
			return null;
		}
		else{
			return result.getDeliverid();
		}
	}

}
