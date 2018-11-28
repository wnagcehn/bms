package com.jiuyescm.bms.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

@Service("materialDictService")
public class MaterialDictService implements IMaterialDictService {

	@Autowired IPubMaterialInfoService pubMaterialInfoService;
	@Autowired private IRedisClient redisClient;
	
	
	private List<PubMaterialInfoVo> getMaterials(){
		Map<String, Object> condition = new HashMap<>();
		condition.put("delFlag", "0");
		List<PubMaterialInfoVo> list =  pubMaterialInfoService.queryList(condition);
		return list;
	}
	
	@Override
	public Map<String, String> getMaterialDictForKey() {
		List<PubMaterialInfoVo> list = getMaterials();
		Map<String, String> map = new HashMap<>();
		for (PubMaterialInfoVo vo : list) {
			map.put(vo.getBarcode(), vo.getMaterialName());
		}
		return map;
	}

	@Override
	public Map<String, String> getMaterialDictForValue() {
		List<PubMaterialInfoVo> list = getMaterials();
		Map<String, String> map = new HashMap<>();
		for (PubMaterialInfoVo vo : list) {
			map.put(vo.getMaterialName(), vo.getBarcode());
		}
		return map;
	}

	@Override
	public String getMaterialNameByCode(final String code) {
		
		PubMaterialInfoVo result = getMaterialByCode(code);
		if(result == null){
			return null;
		}
		else{
			return result.getMaterialName();
		}
		
	}

	@Override
	public String getMaterialCodeByName(final String name) {
		PubMaterialInfoVo result = getMaterialByName(name);
		if(result != null){
			return result.getBarcode(); 
		}
		else{
			return null;
		}
	}

	@Override
	public PubMaterialInfoVo getMaterialByCode(final String code) {
		PubMaterialInfoVo result = redisClient.get(code, RedisCache.MATERIALCODE_SPACE,PubMaterialInfoVo.class, new GetDataCallBack<PubMaterialInfoVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public PubMaterialInfoVo invoke() {
				Map<String, Object> condition = new HashMap<>();
				condition.put("delFlag", "0");
				condition.put("barcode", code);
				List<PubMaterialInfoVo> list =  pubMaterialInfoService.queryList(condition);
				return list.get(0);
			}
		});
		return result;
	}

	@Override
	public PubMaterialInfoVo getMaterialByName(final String name) {
		PubMaterialInfoVo result = redisClient.get(name, RedisCache.MATERIALNAME_SPACE,PubMaterialInfoVo.class, new GetDataCallBack<PubMaterialInfoVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public PubMaterialInfoVo invoke() {
				Map<String, Object> condition = new HashMap<>();
				condition.put("delFlag", "0");
				condition.put("materialName", name);
				List<PubMaterialInfoVo> list =  pubMaterialInfoService.queryList(condition);
				return list.get(0);
			}
		});
		return result;
	}

}
