package com.jiuyescm.bms.base.address.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.address.entity.PubAddressEntity;
import com.jiuyescm.bms.base.address.vo.PubAddressVo;

/**
 * 
 * @author cjw
 * 
 */
public interface IPubAddressVoRepository {

	public PageInfo<PubAddressVo> queryVo(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<PubAddressVo> findAddressByMap(Map<String, Object> condition);
}
