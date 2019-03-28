
package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.receivable.storage.service.IBizInstockDetailService;

/**
 * 
 * @author cjw
 * 
 */
@Service("bizInstockDetailService")
public class BizInstockDetailServiceImpl extends MyBatisDao<BizInStockDetailEntity> implements IBizInstockDetailService {

	public BizInstockDetailServiceImpl() {
		super();
	}

	@Override
	public List<BizInStockDetailEntity> getInstockDetailByMaster(String instockNo) {
		try{
			return selectList("BMS","com.jiuyescm.receivable.storage.BizInstockDetailMapper.queryDetailByMaster", instockNo);
		}
		catch(Exception ex){
			return null;
		}
	}
	
	
	
}
