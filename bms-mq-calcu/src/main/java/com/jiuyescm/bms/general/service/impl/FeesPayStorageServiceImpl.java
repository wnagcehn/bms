package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.general.entity.FeesPayStorageEntity;
import com.jiuyescm.bms.general.service.IFeesPayStorageService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Service("feesPayStorageService")
public class FeesPayStorageServiceImpl extends MyBatisDao implements IFeesPayStorageService {

	@Override
	public int updateBatch(List<FeesPayStorageEntity> feesList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addBatch(List<FeesPayStorageEntity> feesList) {
		return this.insertBatch("com.jiuyescm.bms.general.mapper.FeesPayStorageMapper.save", feesList);
		
	}

	@Override
	public int deleteByFeesNo(List<String> feesNos) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("feesNos", feesNos);
		return this.delete("com.jiuyescm.bms.general.mapper.FeesPayStorageMapper.deleteByFeesNo", map);
	}

}
