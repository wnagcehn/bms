package com.jiuyescm.bms.base.customer.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerSaleMapperEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerSaleMapperRepository;
import com.jiuyescm.bms.base.customer.service.IPubCustomerSaleMapperService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("pubCustomerSaleMapperService")
public class PubCustomerSaleMapperServiceImpl implements IPubCustomerSaleMapperService {

	@Autowired
    private IPubCustomerSaleMapperRepository pubCustomerSaleMapperRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public PubCustomerSaleMapperEntity findById(Long id) {
        return pubCustomerSaleMapperRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<PubCustomerSaleMapperEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubCustomerSaleMapperRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubCustomerSaleMapperEntity> query(Map<String, Object> condition){
		return pubCustomerSaleMapperRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public PubCustomerSaleMapperEntity save(PubCustomerSaleMapperEntity entity) {
        return pubCustomerSaleMapperRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public PubCustomerSaleMapperEntity update(PubCustomerSaleMapperEntity entity) {
        return pubCustomerSaleMapperRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        pubCustomerSaleMapperRepository.delete(id);
    }
    
    @Override
    public int insertBatchTmp(List<PubCustomerSaleMapperEntity> list){
    	return pubCustomerSaleMapperRepository.insertBatchTmp(list);
    }
    
    @Override
    public int updateBatchTmp(List<PubCustomerSaleMapperEntity> list){
    	return pubCustomerSaleMapperRepository.updateBatchTmp(list);
    }
	
}
