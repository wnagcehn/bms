package com.jiuyescm.bms.base.airport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.repository.IPubAirportRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("pubAirportRepository")
public class PubAirportRepositoryImpl extends MyBatisDao<PubAirportEntity> implements IPubAirportRepository{

	@Override
	public PageInfo<PubAirportEntity> query(Map<String, Object> condition,int pageNo, int pageSize) {
		try{
			List<PubAirportEntity> list = selectList("com.jiuyescm.bms.base.airport.mapper.PubAirportMapper.query", condition, new RowBounds(pageNo, pageSize));
	        PageInfo<PubAirportEntity> pageInfo = new PageInfo<PubAirportEntity>(list);
	        return pageInfo;
		}
		catch(Exception ex){
			return null;
		}
		
	}

	@Override
	public List<PubAirportEntity> query(Map<String, Object> condition) {
		List<PubAirportEntity> list = selectList("com.jiuyescm.bms.base.airport.mapper.PubAirportMapper.query", condition);
		return list;
	}
	
	@Override
	public PubAirportEntity save(PubAirportEntity entity) {
		insert("com.jiuyescm.bms.base.airport.mapper.PubAirportMapper.save", entity);
        return entity;
	}

	@Override
	public PubAirportEntity update(PubAirportEntity entity) {
		update("com.jiuyescm.bms.base.airport.mapper.PubAirportMapper.update", entity);
        return entity;
	}

}
