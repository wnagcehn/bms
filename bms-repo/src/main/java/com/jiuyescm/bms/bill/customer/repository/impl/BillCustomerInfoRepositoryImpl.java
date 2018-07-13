package com.jiuyescm.bms.bill.customer.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.bill.customer.BillCustomerInfoEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillCustomerInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("billCustomerInfoRepository")
public class BillCustomerInfoRepositoryImpl extends MyBatisDao<BillCustomerInfoEntity> implements IBillCustomerInfoRepository {

	private static final Logger logger = Logger.getLogger(BillCustomerInfoRepositoryImpl.class.getName());

	@Override
	public PageInfo<BillCustomerInfoEntity> queryList(
			Map<String, Object> condition, int pageNo, int pageSize) {
		 List<BillCustomerInfoEntity> list = selectList("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.queryList", condition, new RowBounds(
	                pageNo, pageSize));
	     PageInfo<BillCustomerInfoEntity> pageInfo = new PageInfo<BillCustomerInfoEntity>(list);
	     return pageInfo;
	}

	@Override
	public int insertEntity(BillCustomerInfoEntity entity) {
		return this.insert("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.insertEntity", entity);
	}

	@Override
	public int updateEntity(BillCustomerInfoEntity entity) {
		return this.update("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.updateEntity", entity);
	}

	@Override
	public int deleteEntity(BillCustomerInfoEntity entity) {
		return this.update("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.deleteEntity", entity);
	}

	@Override
	public List<BillCustomerInfoEntity> queryAll() {
		return this.selectList("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.queryAll", null);
	}

	@Override
	public int saveBatch(List<BillCustomerInfoEntity> list) {
		return this.insertBatch("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.insertEntity", list);
	}

	@Override
	public int updateBatch(List<BillCustomerInfoEntity> list) {
		
		return this.updateBatch("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.updateEntity",list);
	}

	@Override
	public boolean checkSysCustomerHasBind(String sysCustomerId,String customerId) {
		Map<String,String> map=Maps.newHashMap();
		map.put("sysCustomerId", sysCustomerId);
		map.put("customerId", customerId);
		Object obj=this.selectOneForObject("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.checkSysCustomerHasBind", map);
		if(obj==null){
			return false;
		}else{
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
	}

	@Override
	public boolean checkCustomerNameExist(String customerName) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.bill.customer.mapper.BillCustomerInfoMapper.checkCustomerNameExist", customerName);
		if(obj==null){
			return false;
		}else{
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
	}

}
