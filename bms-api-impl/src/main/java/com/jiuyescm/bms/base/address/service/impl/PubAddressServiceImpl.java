package com.jiuyescm.bms.base.address.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.address.entity.PubAddressEntity;
import com.jiuyescm.bms.base.address.repository.IPubAddressRepository;
import com.jiuyescm.bms.base.address.repository.IPubAddressVoRepository;
import com.jiuyescm.bms.base.address.service.IPubAddressService;
import com.jiuyescm.bms.base.address.vo.PubAddressVo;

/**
 * 
 * @author cjw
 * 
 */
@Service("pubAddressService")
public class PubAddressServiceImpl implements IPubAddressService {
	
	@Autowired private IPubAddressRepository pubAddressRepository;
	@Autowired private IPubAddressVoRepository pubAddressVoRepository;
	
	@Override
	public PageInfo<PubAddressVo> queryVo(Map<String, Object> condition,int pageNo, int pageSize) {
		return pubAddressVoRepository.queryVo(condition, pageNo, pageSize);
	}
	
	

    @Override
    public PageInfo<PubAddressEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubAddressRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PubAddressEntity findById(Long id) {
        return pubAddressRepository.findById(id);
    }

    @Override
    public PubAddressEntity save(PubAddressEntity entity) {
        return pubAddressRepository.save(entity);
    }

    @Override
    public PubAddressEntity update(PubAddressEntity entity) {
        return pubAddressRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        pubAddressRepository.delete(id);
    }



	@Override
	public List<PubAddressEntity> queryAllProvince(Map<String, Object> condition) {
		return pubAddressRepository.queryAllProvince(condition);
	}



	@Override
	public List<PubAddressEntity> queryCityByProvince(Map<String, Object> condition) {
		return pubAddressRepository.queryCityByProvince(condition);
	}



	@Override
	public List<PubAddressEntity> queryDistrictByCity(Map<String, Object> condition) {
		return pubAddressRepository.queryDistrictByCity(condition);
	}

	@Override
	public List<PubAddressEntity> queryAllCityForEnum(Map<String, Object> condition) {
		return pubAddressRepository.queryAllCityForEnum(condition);
	}



	@Override
	public List<PubAddressVo> findAddressByMap(Map<String, Object> condition) {
		return pubAddressVoRepository.findAddressByMap(condition);
	}
	
	
	
}
