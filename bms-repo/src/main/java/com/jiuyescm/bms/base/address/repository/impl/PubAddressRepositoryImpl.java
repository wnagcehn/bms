package com.jiuyescm.bms.base.address.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.address.entity.PubAddressEntity;
import com.jiuyescm.bms.base.address.repository.IPubAddressRepository;
import com.jiuyescm.bms.base.address.vo.PubAddressVo;

/**
 * 
 * @author cjw
 * 
 */
@Repository("pubAddressRepository")
public class PubAddressRepositoryImpl extends MyBatisDao<PubAddressEntity> implements IPubAddressRepository {

	private static final Logger logger = Logger.getLogger(PubAddressRepositoryImpl.class.getName());

	public PubAddressRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PubAddressEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubAddressEntity> list = selectList("com.jiuyescm.bms.base.address.PubAddressMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubAddressEntity> pageInfo = new PageInfo<PubAddressEntity>(list);
        return pageInfo;
    }

    @Override
    public PubAddressEntity findById(Long id) {
        PubAddressEntity entity = selectOne("com.jiuyescm.bms.base.address.PubAddressMapper.findById", id);
        return entity;
    }

    @Override
    public PubAddressEntity save(PubAddressEntity entity) {
        insert("com.jiuyescm.bms.base.address.PubAddressMapper.save", entity);
        return entity;
    }

    @Override
    public PubAddressEntity update(PubAddressEntity entity) {
        update("com.jiuyescm.bms.base.address.PubAddressMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.base.address.PubAddressMapper.delete", id);
    }

	@Override
	public List<PubAddressEntity> queryAllProvince(Map<String, Object> condition) {
		List<PubAddressEntity> list = selectList("com.jiuyescm.bms.base.address.PubAddressMapper.queryAllProvince", condition);
		return list;
	}

	@Override
	public List<PubAddressEntity> queryCityByProvince(Map<String, Object> condition) {
		List<PubAddressEntity> list = selectList("com.jiuyescm.bms.base.address.PubAddressMapper.queryCityByProvince", condition);
		return list;
	}

	@Override
	public List<PubAddressEntity> queryDistrictByCity(Map<String, Object> condition) {
		List<PubAddressEntity> list = selectList("com.jiuyescm.bms.base.address.PubAddressMapper.queryDistrictByCity", condition);
		return list;
	}
	
	@Override
	public List<PubAddressEntity> queryAllCityForEnum(Map<String, Object> condition) {
		List<PubAddressEntity> list = selectList("com.jiuyescm.bms.base.address.PubAddressMapper.queryAllCityForEnum", condition);
		return list;
	}

	
	
}
