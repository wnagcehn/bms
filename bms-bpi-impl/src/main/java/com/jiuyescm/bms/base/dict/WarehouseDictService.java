package com.jiuyescm.bms.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.base.dict.vo.PubWarehouseVo;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("warehouseDictService")
public class WarehouseDictService implements IWarehouseDictService {

	private static Logger Logger = LoggerFactory.getLogger(WarehouseDictService.class);
	
	@Autowired IWarehouseService warehouseService;
	@Autowired private IRedisClient redisClient;
	
	@Override
	public Map<String, String> getWarehouseDictForKey() {

		Map<String, String> map = new HashMap<>();
		List<WarehouseVo> list =  warehouseService.queryAllWarehouse();
		for (WarehouseVo vo : list) {
			map.put(vo.getWarehouseid(), vo.getWarehousename());
		}
		return map;
	}

	@Override
	public Map<String, String> getWarehouseDictForValue() {
		Map<String, String> map = new HashMap<>();
		List<WarehouseVo> list =  warehouseService.queryAllWarehouse();
		for (WarehouseVo vo : list) {
			map.put(vo.getWarehousename(),vo.getWarehouseid());
		}
		return map;
	}

	@Override
	public String getWarehouseNameByCode(final String code) {
		
	    PubWarehouseVo result = getWarehouseByCode(code);
		if(result == null){
			Logger.info("未查询到仓库信息 code:{}",code);
			return null;
		}
		else{
			return result.getWarehousename();
		}
	}
	

	@Override
	public String getWarehouseCodeByName(final String name) {
	    PubWarehouseVo result = getWarehouseByName(name);
		if(result == null){
			Logger.info("未查询到仓库信息 name:{}",name);
			return null;
		}
		else{
			Logger.info("查询到的仓库id:{}",result.getWarehouseid());
			return result.getWarehouseid();
		}
	}

	@Override
	public PubWarehouseVo getWarehouseByCode(final String code) {
	    PubWarehouseVo result = redisClient.get(code, RedisCache.WAREHOUSECODE_SPACE,PubWarehouseVo.class, new GetDataCallBack<PubWarehouseVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public PubWarehouseVo invoke() {
				WarehouseVo vo = warehouseService.queryWarehouseByWarehouseid(code);
				return getPubWarehouseVo(vo);
			}
		});
		return result;
	}

	@Override
	public PubWarehouseVo getWarehouseByName(final String name) {
	    PubWarehouseVo result = redisClient.get(name, RedisCache.WAREHOUSENAME_SPACE,PubWarehouseVo.class, new GetDataCallBack<PubWarehouseVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.expiredTime;
			}

			@Override
			public PubWarehouseVo invoke() {
				WarehouseVo vo = warehouseService.queryWarehouseByWarehouseName(name);
				if(vo == null){
				    return null;
				}
				return getPubWarehouseVo(vo);
			}
		});
		return result;
	}
	
	private PubWarehouseVo getPubWarehouseVo(WarehouseVo tempVo){
	    PubWarehouseVo vo = new PubWarehouseVo(); 
	    vo.setWarehousecode(tempVo.getWarehousecode());
	    vo.setWarehouseid(tempVo.getWarehouseid());
	    vo.setWarehousename(tempVo.getWarehousename());
	    return vo;
	}

}
