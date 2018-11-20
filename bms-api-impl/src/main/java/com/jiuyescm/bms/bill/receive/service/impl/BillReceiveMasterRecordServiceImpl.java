package com.jiuyescm.bms.bill.receive.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRecordRepository;
import com.jiuyescm.bms.bill.receive.service.IBillReceiveMasterRecordService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billReceiveMasterRecordService")
public class BillReceiveMasterRecordServiceImpl implements IBillReceiveMasterRecordService {

	@Autowired
    private IBillReceiveMasterRecordRepository billReceiveMasterRecordRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillReceiveMasterRecordEntity findById(Long id) {
        return billReceiveMasterRecordRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillReceiveMasterRecordEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billReceiveMasterRecordRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillReceiveMasterRecordEntity> query(Map<String, Object> condition){
		return billReceiveMasterRecordRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int save(BillReceiveMasterRecordEntity entity) {
    	int k = billReceiveMasterRecordRepository.save(entity);
        return k;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillReceiveMasterRecordEntity update(BillReceiveMasterRecordEntity entity) {
        return billReceiveMasterRecordRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        billReceiveMasterRecordRepository.delete(id);
    }
	
}
