package com.jiuyescm.bms.base.servicetype.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.base.servicetype.repository.IPubCarrierServicetypeRepository;
import com.jiuyescm.bms.base.servicetype.service.IPubCarrierServicetypeService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("pubCarrierServicetypeService")
public class PubCarrierServicetypeServiceImpl implements IPubCarrierServicetypeService {

	@Autowired
    private IPubCarrierServicetypeRepository pubCarrierServicetypeRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public PubCarrierServicetypeEntity findById(Long id) {
        return pubCarrierServicetypeRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<PubCarrierServicetypeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubCarrierServicetypeRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubCarrierServicetypeEntity> query(Map<String, Object> condition){
		return pubCarrierServicetypeRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public PubCarrierServicetypeEntity save(PubCarrierServicetypeEntity entity) {
        return pubCarrierServicetypeRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public PubCarrierServicetypeEntity update(PubCarrierServicetypeEntity entity) {
        return pubCarrierServicetypeRepository.update(entity);
    }
	
}
