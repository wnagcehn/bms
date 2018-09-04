package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsBizInstockInfoService")
public class BmsBizInstockInfoServiceImpl implements IBmsBizInstockInfoService {

	@Autowired
    private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsBizInstockInfoEntity findById(Long id) {
        return bmsBizInstockInfoRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsBizInstockInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsBizInstockInfoRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsBizInstockInfoEntity> query(Map<String, Object> condition){
		return bmsBizInstockInfoRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity) {
        return bmsBizInstockInfoRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity update(BmsBizInstockInfoEntity entity) {
        return bmsBizInstockInfoRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity) {
        return bmsBizInstockInfoRepository.delete(entity);
    }
	
}
