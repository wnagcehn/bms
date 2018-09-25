package com.jiuyescm.bms.biz.pallet.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBizPalletInfoRepository {

    BizPalletInfoEntity findById(Long id);
	
	PageInfo<BizPalletInfoEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizPalletInfoEntity> query(Map<String, Object> condition);

    BizPalletInfoEntity save(BizPalletInfoEntity entity);

    BizPalletInfoEntity update(BizPalletInfoEntity entity);

	BizPalletInfoEntity delete(BizPalletInfoEntity entity);

}
