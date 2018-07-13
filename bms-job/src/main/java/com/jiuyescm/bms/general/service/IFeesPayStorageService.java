package com.jiuyescm.bms.general.service;

import java.util.List;

import com.jiuyescm.bms.general.entity.FeesPayStorageEntity;

public interface IFeesPayStorageService {

	int updateBatch(List<FeesPayStorageEntity> feesList);

	int addBatch(List<FeesPayStorageEntity> feesList);

	int deleteByFeesNo(List<String> feesNos);

}
