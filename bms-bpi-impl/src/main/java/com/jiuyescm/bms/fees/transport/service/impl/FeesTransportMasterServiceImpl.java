package com.jiuyescm.bms.fees.transport.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.repository.IFeesTransportMasterRepository;
import com.jiuyescm.bms.fees.transport.service.IFeesTransportMasterService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("feesTransportMasterService")
public class FeesTransportMasterServiceImpl implements IFeesTransportMasterService {

	@Autowired
    private IFeesTransportMasterRepository feesTransportMasterRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public FeesTransportMasterEntity findById(Long id) {
        return feesTransportMasterRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<FeesTransportMasterEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesTransportMasterRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<FeesTransportMasterEntity> query(Map<String, Object> condition){
		return feesTransportMasterRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public FeesTransportMasterEntity save(FeesTransportMasterEntity entity) {
        return feesTransportMasterRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public FeesTransportMasterEntity update(FeesTransportMasterEntity entity) {
        return feesTransportMasterRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        feesTransportMasterRepository.delete(id);
    }
	
}
