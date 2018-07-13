package com.jiuyescm.bms.base.reportCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.reportCustomer.repository.IReportWarehouseCustomerRepository;
import com.jiuyescm.bms.base.reportCustomer.service.IReportWarehouseCustomerService;
import com.jiuyescm.bms.base.reportCustomer.vo.ReportWarehouseCustomerVo;
import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;

@Service("reportWarehouseCustomerService")
public class ReportWarehouseCustomerServiceImpl implements IReportWarehouseCustomerService{
	
	private static final Logger logger = Logger.getLogger(ReportWarehouseCustomerServiceImpl.class.getName());

	@Autowired
	private IReportWarehouseCustomerRepository reportWarehouseCustomerRepository;
	@Override
	public List<ReportWarehouseCustomerVo> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<ReportWarehouseCustomerVo> voList=new ArrayList<ReportWarehouseCustomerVo>();
		try{
			List<ReportWarehouseCustomerEntity> entityList=reportWarehouseCustomerRepository.query(map);
			
			for(ReportWarehouseCustomerEntity entity:entityList){
				ReportWarehouseCustomerVo vo=new ReportWarehouseCustomerVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("query:",e);
		}
		return voList;
	}
	@Override
	public int save(ReportWarehouseCustomerVo vo) {
		// TODO Auto-generated method stub
		try{
			ReportWarehouseCustomerEntity entity=new ReportWarehouseCustomerEntity();
			PropertyUtils.copyProperties(entity, vo);
			return reportWarehouseCustomerRepository.save(entity);
		}catch(Exception e){
			logger.error("updateGroupSubject:",e);
		}
		return 0;
	}
	@Override
	public int update(ReportWarehouseCustomerVo vo) {
		// TODO Auto-generated method stub
		try{
			ReportWarehouseCustomerEntity entity=new ReportWarehouseCustomerEntity();
			PropertyUtils.copyProperties(entity, vo);
			return reportWarehouseCustomerRepository.update(entity);
		}catch(Exception e){
			logger.error("updateGroupSubject:",e);
		}
		return 0;
	}
	
	@Override
	public int updateList(List<ReportWarehouseCustomerVo> list) {
		// TODO Auto-generated method stub
		List<ReportWarehouseCustomerEntity> entityList=new ArrayList<ReportWarehouseCustomerEntity>();
		try{			
			for(ReportWarehouseCustomerVo vo:list){
				ReportWarehouseCustomerEntity entity=new ReportWarehouseCustomerEntity();
				PropertyUtils.copyProperties(entity,vo);
				entityList.add(entity);
			}
			return reportWarehouseCustomerRepository.updateList(entityList);
		}catch(Exception e){
			logger.error("updateList:",e);
		}
		return 0;
		
	}

}
