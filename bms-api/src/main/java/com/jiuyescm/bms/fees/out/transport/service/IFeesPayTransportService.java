package com.jiuyescm.bms.fees.out.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;
/**
 * 
 *20170527 运输费用 service 层
 * @author Wuliangfeng
 *
 */
public interface IFeesPayTransportService {
	//分页查找
	public PageInfo<FeesPayTransportEntity> query(FeesPayTransportQueryEntity queryEntity, int pageNo, int pageSize);
	//增加
	public int AddFeesReceiveDeliver(FeesPayTransportEntity entity);
	
	public String queryunitPrice(Map<String,String> condition);
	
	public List<FeesPayTransportEntity> queryByImport(List<String> waybillnoList);
	public List<FeesPayTransportEntity> queryByWaybillNo(String waybillno);
	//批量保存
	public int saveDataBatch(List<FeesPayTransportEntity> list);
	
	//根据运单号和时间查询
	public List<FeesPayTransportEntity> queryList(Map<String,Object> condition);
	
	/**
	 * 插入或者更新
	 */
	public int insertOrUpdate(Map<String, Object> param);
}
