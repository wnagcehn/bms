/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.address.service;

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
public interface IPubAddressService {

	public PageInfo<PubAddressVo> queryVo(Map<String, Object> condition,int pageNo, int pageSize);
	
    PageInfo<PubAddressEntity> query(Map<String, Object> condition, int pageNo, int pageSize);

    PubAddressEntity findById(Long id);

    PubAddressEntity save(PubAddressEntity entity);

    PubAddressEntity update(PubAddressEntity entity);

    void delete(Long id);
    
    List<PubAddressEntity> queryAllProvince(Map<String, Object> condition);

    List<PubAddressEntity> queryCityByProvince(Map<String, Object> condition);
    
    List<PubAddressEntity> queryDistrictByCity(Map<String, Object> condition);

	List<PubAddressEntity> queryAllCityForEnum(Map<String, Object> condition);
	
	List<PubAddressVo> findAddressByMap(Map<String, Object> condition);
}
