package com.jiuyescm.bms.biz.storage.service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportDataEntity;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportQueryEntity;

public interface IBizImportDataGroupService {
	PageInfo<BizCustomerImportDataEntity> query(BizCustomerImportQueryEntity condition,int pageNo, int pageSize);
}
