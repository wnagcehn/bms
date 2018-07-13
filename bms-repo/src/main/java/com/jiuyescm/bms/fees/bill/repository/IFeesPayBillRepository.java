package com.jiuyescm.bms.fees.bill.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;

public interface IFeesPayBillRepository {

	public PageInfo<FeesPayBillEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

	public FeesPayBillEntity queryBillInfo(Map<String, Object> condition);
	
	public int save(FeesPayBillEntity entity);
	
	public int update(FeesPayBillEntity entity);
	
	//结算
    public int confirmFeesBill(FeesPayBillEntity entity);

    //删除
	public int deleteFeesBill(FeesPayBillEntity entity);
	
	//查询最后生成账单时间
	public List<FeesPayBillEntity> getlastBillTime(Map<String, String> maps);
	
	//查询最后生成账单时间
	public List<FeesPayBillEntity> getLastBillTimeDelivery(Map<String, Object> maps);

}
