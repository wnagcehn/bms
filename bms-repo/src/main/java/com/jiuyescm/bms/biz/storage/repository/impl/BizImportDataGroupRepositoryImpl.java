package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportDataEntity;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportQueryEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizImportDataGroupRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizImportDataGroupRepository")
public class BizImportDataGroupRepositoryImpl extends MyBatisDao<BizCustomerImportDataEntity> implements IBizImportDataGroupRepository {

	@Override
	public PageInfo<BizCustomerImportDataEntity> query(
			BizCustomerImportQueryEntity condition, int pageNo, int pageSize) {
		List<BizCustomerImportDataEntity> list=this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizImportDataGroupMapper.query", condition,new RowBounds(pageNo, pageSize));
		return new PageInfo<BizCustomerImportDataEntity>(list);
	}

}
