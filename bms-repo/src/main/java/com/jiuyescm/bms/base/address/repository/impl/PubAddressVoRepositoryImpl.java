package com.jiuyescm.bms.base.address.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.address.entity.PubAddressEntity;
import com.jiuyescm.bms.base.address.repository.IPubAddressVoRepository;
import com.jiuyescm.bms.base.address.vo.PubAddressVo;

/**
 * 
 * @author cjw
 * 
 */
@Repository("pubAddressVoRepository")
public class PubAddressVoRepositoryImpl extends MyBatisDao<PubAddressVo> implements IPubAddressVoRepository {

	private static final Logger logger = Logger.getLogger(PubAddressVoRepositoryImpl.class.getName());

	public PubAddressVoRepositoryImpl() {
		super();
	}
	
	@Override
	public PageInfo<PubAddressVo> queryVo(Map<String, Object> condition,int pageNo, int pageSize) {
		 List<PubAddressVo> list = selectList("com.jiuyescm.bms.base.address.PubAddressVoMapper.queryVo", condition, new RowBounds(pageNo, pageSize));
	     PageInfo<PubAddressVo> pageInfo = new PageInfo<PubAddressVo>(list);
	     return pageInfo;
	}

	@Override
	public List<PubAddressVo> findAddressByMap(Map<String, Object> condition) {
		 List<PubAddressVo> list = selectList("com.jiuyescm.bms.base.address.PubAddressVoMapper.findAddressByMap", condition);
		 return list;
	}
	
	
	
}
