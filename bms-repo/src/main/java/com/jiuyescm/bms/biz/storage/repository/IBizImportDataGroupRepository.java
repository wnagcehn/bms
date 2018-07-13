package com.jiuyescm.bms.biz.storage.repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportDataEntity;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportQueryEntity;

public interface IBizImportDataGroupRepository {

	PageInfo<BizCustomerImportDataEntity> query(
			BizCustomerImportQueryEntity condition, int pageNo, int pageSize);

}
