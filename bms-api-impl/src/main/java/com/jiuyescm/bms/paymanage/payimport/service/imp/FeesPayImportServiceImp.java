package com.jiuyescm.bms.paymanage.payimport.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.paymanage.payimport.FeesPayImportEntity;
import com.jiuyescm.bms.paymanage.payimport.repository.IFeesPayImportRepository;
import com.jiuyescm.bms.paymanage.payimport.service.IFeesPayImportService;

@Service("feesPayImportService")
public class FeesPayImportServiceImp implements IFeesPayImportService{
	
	@Resource
	private IFeesPayImportRepository feesPayImportRepository;

	@Override
	public PageInfo<FeesPayImportEntity> queryAll(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.queryAll(parameter, aPageNo, aPageSize);
	}

	@Override
	public int saveList(List<FeesPayImportEntity> list) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.saveList(list);
	}

	@Override
	public List<FeesPayImportEntity> queryList(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.queryList(condition);
	}

	@Override
	public FeesPayImportEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.queryOne(condition);
	}

	@Override
	public int updateList(List<FeesPayImportEntity> list) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.updateList(list);
	}

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })		
	@SuppressWarnings("unchecked")
	@Override
	public int insertOrUpdate(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int insertNum=0;
		int updateNum=0;
		List<FeesPayImportEntity> insertList=(List<FeesPayImportEntity>) param.get("insert");

		if(insertList.size()>0){
		   //插入正式表				
		   insertNum= feesPayImportRepository.saveList(insertList);							
	    }
	    List<FeesPayImportEntity> updateList=(List<FeesPayImportEntity>) param.get("update");
	    if(updateList.size()>0){
		   //插入正式表				
		   updateNum= feesPayImportRepository.updateList(updateList);								
		}
		return insertNum+updateNum;
	}

	@Override
	public int updateOne(FeesPayImportEntity fee) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.updateOne(fee);
	}

	@Override
	public int remove(List<FeesPayImportEntity> list) {
		// TODO Auto-generated method stub
		return feesPayImportRepository.remove(list);
	}
	
	
}
