package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.fees.storage.entity.FeesSaleReceiveStorageEntity;

public interface IFeesSaleReceiveStorageService {

	int deleteBatch(Map<String, Object> feesMap);

	int insertBatch(List<FeesSaleReceiveStorageEntity> feesList);

}
