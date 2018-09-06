package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockRecordEntity;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockRecordRepository;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockRecordService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsBizInstockRecordService")
public class BmsBizInstockRecordServiceImpl implements IBmsBizInstockRecordService {

	@Autowired
    private IBmsBizInstockRecordRepository bmsBizInstockRecordRepository;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsBizInstockRecordEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsBizInstockRecordRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsBizInstockRecordEntity> query(Map<String, Object> condition){
		return bmsBizInstockRecordRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity) {
        return bmsBizInstockRecordRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockRecordEntity update(BmsBizInstockRecordEntity entity) {
        return bmsBizInstockRecordRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        bmsBizInstockRecordRepository.delete(id);
    }
    
	/**
	 * 批量保存
	 * @param entity
	 * @return
	 */
    @Override
    public int saveBatch(List<BmsBizInstockInfoEntity> list) {
        return bmsBizInstockRecordRepository.saveBatch(list);
    }
	
}
