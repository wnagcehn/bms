package com.jiuyescm.bms.bill.check.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.check.BillCheckFollowEntity;
import com.jiuyescm.bms.bill.check.BillCheckInfoFollowEntity;
import com.jiuyescm.bms.bill.check.repository.IBillCheckFollowRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;


@Repository("billCheckFollowRepository")
public class BillCheckFollowRepositoryImpl extends MyBatisDao implements IBillCheckFollowRepository {

	@Override
	public BillCheckFollowEntity addBillCheckFollowEntity(BillCheckFollowEntity entity) {
		this.insert("com.jiuyescm.bms.bill.check.mapper.BillCheckFollowMapper.insertEntity", entity);
		return  entity;
	}

	@Override
	public PageInfo<BillCheckInfoFollowEntity> queryList(
			Map<String, Object> condition, int pageNo, int pageSize) {
		 List<BillCheckInfoFollowEntity> list = selectList("com.jiuyescm.bms.bill.check.mapper.BillCheckFollowMapper.queryList", condition, new RowBounds(
	                pageNo, pageSize));
	     PageInfo<BillCheckInfoFollowEntity> pageInfo = new PageInfo<BillCheckInfoFollowEntity>(list);
	     return pageInfo;

	}

	@Override
	public int updateFollowStatus(BillCheckFollowEntity entity) {
		
		return this.update("com.jiuyescm.bms.bill.check.mapper.BillCheckFollowMapper.updateFollowStatus", entity);
	}

	@Override
	public int finishFollow(BillCheckFollowEntity entity) {
		return this.update("com.jiuyescm.bms.bill.check.mapper.BillCheckFollowMapper.finishFollow", entity);
	}

	@Override
	public boolean checkFollowManExist(String followManId) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.bill.check.mapper.BillCheckFollowMapper.checkFollowManExist", followManId);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

}
