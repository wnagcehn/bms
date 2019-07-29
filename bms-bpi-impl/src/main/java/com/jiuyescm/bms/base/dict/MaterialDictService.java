package com.jiuyescm.bms.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dict.vo.PubMaterialVo;
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
		
		PubMaterialVo result = getMaterialByCode(code);
		if(result == null){
			return null;
		}
		else{
			return result.getMaterialName();
		}
		
	}

	@Override
	public String getMaterialCodeByName(final String name) {
		PubMaterialVo result = getMaterialByName(name);
		if(result != null){
			return result.getBarcode(); 
		}
		else{
			return null;
		}
	}

	@Override
	public PubMaterialVo getMaterialByCode(final String code) {
	    PubMaterialVo result = redisClient.get(code, RedisCache.MATERIALCODE_SPACE,PubMaterialVo.class, new GetDataCallBack<PubMaterialVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public PubMaterialVo invoke() {
				Map<String, Object> condition = new HashMap<>();
				condition.put("delFlag", "0");
				condition.put("barcode", code);
				List<PubMaterialInfoVo> list =  pubMaterialInfoService.queryList(condition);
				if(list == null || list.size()==0){
					return null;
				}
				return getPubMaterialVo(list.get(0));
			}
		});
		return result;
	}

	@Override
	public PubMaterialVo getMaterialByName(final String name) {
	    PubMaterialVo result = redisClient.get(name, RedisCache.MATERIALNAME_SPACE,PubMaterialVo.class, new GetDataCallBack<PubMaterialVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public PubMaterialVo invoke() {
				Map<String, Object> condition = new HashMap<>();
				condition.put("delFlag", "0");
				condition.put("materialName", name);
				List<PubMaterialInfoVo> list =  pubMaterialInfoService.queryList(condition);
				if(list == null || list.size()==0){
					return null;
				}
				
				return getPubMaterialVo(list.get(0));
			}
		});
		return result;
	}
	
	private PubMaterialVo getPubMaterialVo(PubMaterialInfoVo tempVo){
	    PubMaterialVo vo = new PubMaterialVo();
	    vo.setBarcode(tempVo.getBarcode());
	    vo.setMaterialName(tempVo.getMaterialName());
	    vo.setMaterialNo(tempVo.getMaterialNo());
	    vo.setMaterialType(tempVo.getMaterialType());
	    return vo;
	}

}
