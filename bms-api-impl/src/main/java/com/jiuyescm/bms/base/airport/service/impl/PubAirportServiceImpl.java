package com.jiuyescm.bms.base.airport.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.repository.IPubAirportRepository;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;

@Service("pubAirportService")
public class PubAirportServiceImpl implements IPubAirportService {
    
	@Autowired private IPubAirportRepository pubAirportRepository;

	@Override
	public PageInfo<PubAirportEntity> query(Map<String, Object> condition,int pageNo, int pageSize) {
		return pubAirportRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public List<PubAirportEntity> query(Map<String, Object> condition) {
		return pubAirportRepository.query(condition);
	}

	@Override
	public PubAirportEntity save(PubAirportEntity entity) {
		return pubAirportRepository.save(entity);
	}

	@Override
	public PubAirportEntity update(PubAirportEntity entity) {
		return pubAirportRepository.update(entity);
	}

}
