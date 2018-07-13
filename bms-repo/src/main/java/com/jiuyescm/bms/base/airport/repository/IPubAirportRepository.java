package com.jiuyescm.bms.base.airport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;

public interface IPubAirportRepository {

PageInfo<PubAirportEntity> query(Map<String, Object> condition, int pageNo,int pageSize);
	
	List<PubAirportEntity> query(Map<String, Object> condition);
	
	PubAirportEntity save(PubAirportEntity entity);

    PubAirportEntity update(PubAirportEntity entity);
}
