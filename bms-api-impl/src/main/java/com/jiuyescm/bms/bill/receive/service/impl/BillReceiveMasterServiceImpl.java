package com.jiuyescm.bms.bill.receive.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.bill.receive.service.IBillReceiveMasterService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billReceiveMasterService")
public class BillReceiveMasterServiceImpl implements IBillReceiveMasterService {

	@Autowired
    private IBillReceiveMasterRepository billReceiveMasterRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillReceiveMasterEntity findById(Long id) {
        return billReceiveMasterRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillReceiveMasterEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billReceiveMasterRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillReceiveMasterEntity> query(Map<String, Object> condition){
		return billReceiveMasterRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillReceiveMasterEntity save(BillReceiveMasterEntity entity) {
        return billReceiveMasterRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillReceiveMasterEntity update(BillReceiveMasterEntity entity) {
        return billReceiveMasterRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        billReceiveMasterRepository.delete(id);
    }
	
}
