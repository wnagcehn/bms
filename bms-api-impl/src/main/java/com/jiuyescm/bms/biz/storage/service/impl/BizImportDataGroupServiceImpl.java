package com.jiuyescm.bms.biz.storage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportDataEntity;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportQueryEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizImportDataGroupRepository;
import com.jiuyescm.bms.biz.storage.service.IBizImportDataGroupService;

@Service("bizImportDataGroupService")
public class BizImportDataGroupServiceImpl implements IBizImportDataGroupService {

	@Autowired
	private IBizImportDataGroupRepository bizImportDataGroupRepository;
	@Override
	public PageInfo<BizCustomerImportDataEntity> query(
			BizCustomerImportQueryEntity condition, int pageNo, int pageSize) {
		return bizImportDataGroupRepository.query(condition,pageNo,pageSize);
	}

}
