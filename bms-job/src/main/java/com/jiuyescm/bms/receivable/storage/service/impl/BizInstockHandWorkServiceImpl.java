package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizInstockHandworkEntity;
import com.jiuyescm.bms.receivable.storage.service.IBizInstockHandWorkService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 
 * @author cjw
 * 
 */
@Service("bizInstockHandWorkServiceImpl")
public class BizInstockHandWorkServiceImpl extends MyBatisDao<BizInstockHandworkEntity> implements IBizInstockHandWorkService {

	public BizInstockHandWorkServiceImpl() {
		super();
	}

	@Override
	public List<BizInstockHandworkEntity> getInStockMasterList(Map<String, Object> condition) {
		try{
			List<BizInstockHandworkEntity> list = selectList("com.jiuyescm.bms.receivable.storage.BizInstockHandWorkMapper.querybizInstock", condition);
			return list;
		}
		catch(Exception ex){
			XxlJobLogger.log("入库单主表查询异常"+ex.getMessage());
			return null;
		}
	}

	@Override
	public void updateInstock(BizInstockHandworkEntity entity) {
		this.update("com.jiuyescm.bms.receivable.storage.BizInstockHandWorkMapper.updatebizInstockHandWork", entity);
	}

	@Override
	public void updateInstockBatch(List<BizInstockHandworkEntity> entities) {
		this.updateBatch("com.jiuyescm.bms.receivable.storage.BizInstockHandWorkMapper.updatebizInstockHandWork", entities);
	}

}
