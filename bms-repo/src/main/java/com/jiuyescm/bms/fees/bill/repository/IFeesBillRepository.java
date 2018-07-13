package com.jiuyescm.bms.fees.bill.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillQueryEntity;
/**
 * 
 * @author wuliangfeng 20170601 
 * 账单数据操作接口
 */
public interface IFeesBillRepository {
	
	public PageInfo<FeesBillEntity> query(FeesBillQueryEntity queryEntity,
			int pageNo, int pageSize);
	
	public FeesBillEntity queryBillInfo(FeesBillQueryEntity queryEntity);
	
	public int save(FeesBillEntity entity);
	
	public int update(FeesBillEntity entity);

	public int confirmFeesBill(FeesBillEntity entity);

	public int deleteFeesBill(FeesBillEntity entity);

	public List<FeesBillEntity> getlastBillTime(Map<String, String> maps);
}
