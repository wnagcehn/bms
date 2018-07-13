package com.jiuyescm.bms.paymanage.payimport.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.paymanage.payimport.FeesPayImportEntity;
import com.jiuyescm.bms.paymanage.payimport.repository.IFeesPayImportRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesPayImportRepository")
public class FeesPayImportRepositoryImp extends MyBatisDao<FeesPayImportEntity> implements IFeesPayImportRepository{

	@Override
	public PageInfo<FeesPayImportEntity> queryAll(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<FeesPayImportEntity> list=selectList("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.query", parameter,new RowBounds(aPageNo,aPageSize));
		PageInfo<FeesPayImportEntity> pList=new PageInfo<>(list);
		
		return pList;
	}

	@Override
	public int saveList(List<FeesPayImportEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.save", list);
	}

	@Override
	public List<FeesPayImportEntity> queryList(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.query", condition);
	}

	@Override
	public FeesPayImportEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (FeesPayImportEntity) selectOne("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.queryOne", condition);
	}

	@Override
	public int updateList(List<FeesPayImportEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.update", list);
	}

	@Override
	public int updateOne(FeesPayImportEntity fee) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.update", fee);
	}

	@Override
	public int remove(List<FeesPayImportEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.paymanage.payimport.mapper.FeesPayImportMapper.updateList", list);
	}
	
}
