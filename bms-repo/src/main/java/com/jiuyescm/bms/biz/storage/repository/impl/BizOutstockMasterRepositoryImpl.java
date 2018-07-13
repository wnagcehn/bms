/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockMasterRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bizOutstockMasterRepository")
public class BizOutstockMasterRepositoryImpl extends MyBatisDao implements IBizOutstockMasterRepository {

	private static final Logger logger = Logger.getLogger(BizOutstockMasterRepositoryImpl.class.getName());

	public BizOutstockMasterRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BizOutstockMasterEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizOutstockMasterEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizOutstockMasterEntity> pageInfo = new PageInfo<BizOutstockMasterEntity>(list);
        return pageInfo;
    }

    @Override
    public BizOutstockMasterEntity findById(Long id) {
        /*BizOutstockMasterEntity entity = selectOne("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.findById", id);
        return entity;*/
    	return null;
    }

    @Override
    public BizOutstockMasterEntity save(BizOutstockMasterEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.save", entity);
        return entity;
    }

    @Override
    public int update(BizOutstockMasterEntity entity) {
       return  update("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.update", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.delete", id);
    }

	@Override
	public PageInfo<BizOutstockMasterEntity> queryGroup(Map<String, Object> condition, int pageNo, int pageSize) {
		  List<BizOutstockMasterEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.queryGroup", condition, new RowBounds(
	                pageNo, pageSize));
	        PageInfo<BizOutstockMasterEntity> pageInfo = new PageInfo<BizOutstockMasterEntity>(list);
	        return pageInfo;
	}

	@Override
	public int reCalculate(Map<String, Object> condition) {
		try{
			update("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.retryForCalcu", condition);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}


	//Billed-已在账单中存在,不能重算,建议删除账单后重试;Calculated-已计算,是否继续重算;Error-系统错误;OK-可重算
	@Override
	public Properties validRetry(Map<String, Object> condition) {
		
		Properties ret = new Properties();
		try{
			Object waybillno = selectOne("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.validBillForRetry", condition);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "运单号【"+waybillno+"】已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.validCalcuForRetry", condition);
			if(waybillno != null){
				ret.setProperty("key", "Calculated");
				ret.setProperty("value", "存在已计算的数据");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			ret.setProperty("key", "OK");
			ret.setProperty("value", "");
			return ret;
		}
		catch(Exception ex){
			logger.error("系统异常-验证重算异常", ex);
			ret.setProperty("key", "Error");
			ret.setProperty("value", "系统异常-验证重算异常");
			return ret;
		}
		
	}

	@Override
	public BizOutstockMasterEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BizOutstockMasterEntity) selectOne("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.queryExceptionOne", condition);
	}

	@Override
	public List<String> queryAllWarehouseId(Map<String, Object> condition) {
		return (List<String>)this.selectList("com.jiuyescm.bms.biz.storage.BizOutstockMasterEntityMapper.queryAllWarehouseId", condition);
	}
	
}
