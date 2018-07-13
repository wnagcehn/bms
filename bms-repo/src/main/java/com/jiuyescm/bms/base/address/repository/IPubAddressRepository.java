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
public interface IPubAddressRepository {
	
	public PageInfo<PubAddressEntity> query(Map<String, Object> condition, int pageNo, int pageSize);

    public PubAddressEntity findById(Long id);

    public PubAddressEntity save(PubAddressEntity entity);

    public PubAddressEntity update(PubAddressEntity entity);

    public void delete(Long id);
    
    List<PubAddressEntity> queryAllProvince(Map<String, Object> condition);

    List<PubAddressEntity> queryCityByProvince(Map<String, Object> condition);
    
    List<PubAddressEntity> queryDistrictByCity(Map<String, Object> condition);

	List<PubAddressEntity> queryAllCityForEnum(Map<String, Object> condition);
	
	

}
