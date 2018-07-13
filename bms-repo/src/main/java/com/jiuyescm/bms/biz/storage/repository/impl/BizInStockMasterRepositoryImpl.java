package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizInStockMasterRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
/**
 * 入库单主表
 * 
 * @author yangshuaishuai
 *
 */
@Repository("inStockMasterRepository")
public class BizInStockMasterRepositoryImpl extends MyBatisDao implements IBizInStockMasterRepository {

	private static final Logger logger = Logger.getLogger(BizInStockMasterRepositoryImpl.class.getName());
	
	@Override
	public PageInfo<BizInStockMasterEntity> query(Map<String, Object> condition,int pageNo, int pageSize) {
		List<BizInStockMasterEntity> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizInStockMasterMapper.query",condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BizInStockMasterEntity>(list);
	}

	@Override
	public int update(BizInStockMasterEntity entity) {
		return super.updateWithOne("com.jiuyescm.bms.biz.storage.mapper.BizInStockMasterMapper.update", entity);
	}

	@Override
	public Properties validRetry(Map<String, Object> condition) {
		Properties ret = new Properties();
		try{
			Object instockNo = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizInStockMasterMapper.validBillForRetry", condition);
			if(instockNo != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "运单号【"+instockNo+"】已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			instockNo = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizInStockMasterMapper.validCalcuForRetry", condition);
			if(instockNo != null){
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
	public int reCalculate(Map<String, Object> param) {
		try{
			update("com.jiuyescm.bms.biz.storage.mapper.BizInStockMasterMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}

}
