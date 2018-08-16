package com.jiuyescm.bms.base.unitInfo.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.unitInfo.entity.BmsChargeUnitInfoEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsChargeUnitInfoRepository {

    BmsChargeUnitInfoEntity findById(Long id);
	
	PageInfo<BmsChargeUnitInfoEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsChargeUnitInfoEntity> query(Map<String, Object> condition);

    BmsChargeUnitInfoEntity save(BmsChargeUnitInfoEntity entity);

    BmsChargeUnitInfoEntity update(BmsChargeUnitInfoEntity entity);

	BmsChargeUnitInfoEntity delete(BmsChargeUnitInfoEntity entity);

}
