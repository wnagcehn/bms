package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 商品库存dao层
 * @author cjw
 * 
 */
@Repository("bizDispatchPackageService")
public class BizDispatchPackageServiceImpl extends MyBatisDao<BizDispatchPackageEntity> implements IBizDispatchPackageService{

	private static final Logger logger = Logger.getLogger(BizDispatchPackageEntity.class.getName());

	public BizDispatchPackageServiceImpl() {
		super();
	}

	@Override
	public List<BizDispatchPackageEntity> query(Map<String, Object> condition) {
		List<BizDispatchPackageEntity> list = selectList("com.jiuyescm.bms.receivable.storage.mapper.BizDispatchPackageMapper.query", condition);
		return list;
	}

	@Override
	public void updateBatch(List<BizDispatchPackageEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.mapper.BizDispatchPackageMapper.updateBatch", list);
		}
		catch(Exception ex){
			logger.error("【标准耗材使用费任务】批量更新主表异常"+ex.getMessage());
		}
	}
    @Override
    public BizDispatchPackageEntity queryOne(String wayBillNo) {
        // TODO Auto-generated method stub
        BizDispatchPackageEntity entity = this.selectOne("com.jiuyescm.bms.receivable.storage.mapper.BizDispatchPackageMapper.queryOne", wayBillNo);
        return entity;
    }

	
}
