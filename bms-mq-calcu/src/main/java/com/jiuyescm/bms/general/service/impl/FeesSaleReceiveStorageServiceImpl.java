package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.fees.storage.entity.FeesSaleReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesSaleReceiveStorageService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Service("feesSaleReceiveStorageService")
public class FeesSaleReceiveStorageServiceImpl extends MyBatisDao<FeesSaleReceiveStorageEntity> implements IFeesSaleReceiveStorageService{

	@Override
	public int deleteBatch(Map<String, Object> feesMap) {
		return this.delete("com.jiuyescm.bms.general.entity.FeesSaleReceiveStorageMapper.deleteByFeesNo", feesMap);
	}

	@Override
	public int insertBatch(List<FeesSaleReceiveStorageEntity> feesList) {
		return this.insertBatch("com.jiuyescm.bms.general.entity.FeesSaleReceiveStorageMapper.save", feesList);
	}

}
