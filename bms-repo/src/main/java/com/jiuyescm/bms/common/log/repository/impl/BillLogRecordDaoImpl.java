package com.jiuyescm.bms.common.log.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.repository.IBillLogRecordDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("billLogRecordDaoImpl")
public class BillLogRecordDaoImpl extends MyBatisDao<BmsBillLogRecordEntity> implements IBillLogRecordDao {

	@Override
	public void log(BmsBillLogRecordEntity record) {
		insert("com.jiuyescm.bms.common.log.BmsBillLogRecordMapper.save", record);
	}

	@Override
	public PageInfo<BmsBillLogRecordEntity> queryAll(Map<String, Object> parameter,int pageNo, int pageSize) {
		
		try{
			List<BmsBillLogRecordEntity> list = selectList("com.jiuyescm.bms.common.log.BmsBillLogRecordMapper.query", parameter, new RowBounds(pageNo, pageSize));
	        PageInfo<BmsBillLogRecordEntity> pageInfo = new PageInfo<BmsBillLogRecordEntity>(list);
	        return pageInfo;
		}
		catch(Exception ex){
			return null;
		}
	}

	@Override
	public List<BmsBillLogRecordEntity> queryList(Map<String, Object> parameter) {
		try{
			List<BmsBillLogRecordEntity> list = selectList("com.jiuyescm.bms.common.log.BmsBillLogRecordMapper.query", parameter);
			return list;
		}
		catch(Exception ex){
			return null;
		}
	}

}
