package com.jiuyescm.bms.base.dict;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dict.api.IPubPackageDictService;
import com.jiuyescm.bms.base.dict.entity.PubPackageDictEntity;
import com.jiuyescm.bms.base.dict.repository.IPubPackageDictRepository;


/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("pubPackageDictService")
public class PubPackageDictServiceImpl implements IPubPackageDictService {

	@Autowired
    private IPubPackageDictRepository pubPackageDictRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public PubPackageDictEntity findById(Long id) {
        return pubPackageDictRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<PubPackageDictEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubPackageDictRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubPackageDictEntity> query(Map<String, Object> condition){
		return pubPackageDictRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public PubPackageDictEntity save(PubPackageDictEntity entity) {
        return pubPackageDictRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public PubPackageDictEntity update(PubPackageDictEntity entity) {
        return pubPackageDictRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        pubPackageDictRepository.delete(id);
    }
	
}
