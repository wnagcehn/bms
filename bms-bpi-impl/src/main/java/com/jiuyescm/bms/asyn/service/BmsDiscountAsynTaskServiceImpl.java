package com.jiuyescm.bms.asyn.service;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.discount.repository.IBmsDiscountAsynTaskRepository;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsDiscountAsynTaskService")
public class BmsDiscountAsynTaskServiceImpl implements IBmsDiscountAsynTaskService {

	@Autowired
    private IBmsDiscountAsynTaskRepository bmsDiscountAsynTaskRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsDiscountAsynTaskEntity findById(Long id) {
        return bmsDiscountAsynTaskRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsDiscountAsynTaskRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition){
		return bmsDiscountAsynTaskRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity) {
        return bmsDiscountAsynTaskRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity) {
        return bmsDiscountAsynTaskRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        bmsDiscountAsynTaskRepository.delete(id);
    }
	
}
