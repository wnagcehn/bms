package com.jiuyescm.bms.quotation.contract.repository.imp.imp;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw by 2017-11-16
 * 
 */
@Repository("priceContractItemRepository")
public class PriceContractItemRepositoryImpl extends MyBatisDao<PriceContractItemEntity> implements IPriceContractItemRepository {

	private static final Logger logger = Logger.getLogger(PriceContractItemRepositoryImpl.class.getName());

	public PriceContractItemRepositoryImpl() {
		super();
	}
	
	@Override
    public List<PriceContractItemEntity> query(Map<String, Object> condition) {
        List<PriceContractItemEntity> list = selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractItemMapper.query", condition);
        return list;
    }
	
}
