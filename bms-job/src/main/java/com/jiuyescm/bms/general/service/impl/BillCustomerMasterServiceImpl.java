package com.jiuyescm.bms.general.service.impl;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.general.entity.BillCustomerMasterEntity;
import com.jiuyescm.bms.general.service.IBillCustomerMasterService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billCustomerMasterService")
public class BillCustomerMasterServiceImpl extends MyBatisDao implements IBillCustomerMasterService {
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillCustomerMasterEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillCustomerMasterEntity> list = selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillCustomerMasterEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillCustomerMasterEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillCustomerMasterEntity save(BillCustomerMasterEntity entity) {
        insert("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public void updateBatch(List<BillCustomerMasterEntity> list) {
        this.updateBatch("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.update", list);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.delete", id);
    }
	
    @Override
    public List<BillCustomerMasterEntity> queryCalcuTaskLogAndCheckLog(Map<String, Object> condition){
        return selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.queryCalcuTaskLogAndCheckLog", condition);
    }
    
    @Override
    public BillCustomerDetailEntity queryBmsCalcuData(Map<String, Object> condition){
        List<BillCustomerDetailEntity> list = selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.queryBmsCalcuData", condition);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    @Override
    public List<BillPrepareExportTaskEntity> queryPrepareData(Map<String, Object> condition){
        List<BillPrepareExportTaskEntity> list = selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.queryPrepareData", condition);
        return list;
    }
    
    @Override
    public List<BillPrepareExportTaskEntity> queryPrepareIsChildData(Map<String, Object> condition){
        List<BillPrepareExportTaskEntity> list = selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.queryPrepareIsChildData", condition);
        return list;
    }
    
    @Override
    public List<BillCheckInfoEntity> queryBillImportData(Map<String, Object> condition){
        List<BillCheckInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.queryBillImportData", condition);
        return list;
    }
    
    @Override
    public BillCustomerDetailEntity queryPrepareAmount(Map<String, Object> condition){
        List<BillCustomerDetailEntity> list = selectList("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.queryPrepareAmount", condition);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    /**
     * 库里有就更新，没有就新增
     */
    @Override
    public void saveOrUpdate(List<BillCustomerMasterEntity> masterList) {
        SqlSession session = this.getSqlSessionTemplate();
        session.insert("com.jiuyescm.bms.general.mapper.BillCustomerMasterMapper.saveOrUpdate", masterList);
    }
    
}
