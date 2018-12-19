package com.jiuyescm.bms.bill.receive.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.BillReceiveExpectEntity;
import com.jiuyescm.bms.billcheck.ReportBillImportMasterEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("billReceiveMasterRepository")
public class BillReceiveMasterRepositoryImpl extends MyBatisDao implements IBillReceiveMasterRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillReceiveMasterEntity findById(Long id) {
        return (BillReceiveMasterEntity) selectOne("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<BillReceiveMasterEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillReceiveMasterEntity> list = selectList("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillReceiveMasterEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@SuppressWarnings("unchecked")
	@Override
    public List<BillReceiveMasterEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @SuppressWarnings("unchecked")
	@Override
    public int save(BillReceiveMasterEntity entity) {
        int k = insert("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.save", entity);
        return k;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @SuppressWarnings("unchecked")
	@Override
    public int update(BillReceiveMasterEntity entity) {
        int k = update("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.update", entity);
        return k;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(String billNo) {
        delete("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.delete", billNo);
    }

	@SuppressWarnings("unchecked")
	@Override
	public int saveExpect(BillReceiveExpectEntity entity) {
		// TODO Auto-generated method stub
	    int k = insert("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.saveExpect", entity);
	    return k;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillReceiveExpectEntity queryExpect(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BillReceiveExpectEntity) selectOne("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.queryExpect", condition);
	}

	@Override
	public Double getAbnormalMoney(String billNo) {
		// TODO Auto-generated method stub		
		Object object=selectOne("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.getAbnormalMoney", billNo);
		Double money=0d;
		if(object!=null){
			money=Double.valueOf(object.toString());
		}
		return money;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertReportMaster(ReportBillImportMasterEntity vo) {
		// TODO Auto-generated method stub
	    int k = insert("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.insertReportMaster", vo);
	    return k;
	}
	
}
