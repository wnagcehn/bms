package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockRecordRepository;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsBizInstockInfoService")
public class BmsBizInstockInfoServiceImpl implements IBmsBizInstockInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(BmsBizInstockInfoServiceImpl.class.getName());
	
	@Autowired
    private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	
	@Autowired
	private IBmsBizInstockRecordRepository bmsBizInstockRecordRepository;

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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    @Override
    public void update(BmsBizInstockInfoEntity entity) {
    	//更新最新表
    	try {
    		bmsBizInstockInfoRepository.update(entity);
		} catch (Exception e) {
			logger.error("更新失败", e);
		}
        //写入记录表
    	try {
            bmsBizInstockRecordRepository.save(entity);
		} catch (Exception e) {
			logger.error("记录表存储失败！", e);
		}
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity) {
        return bmsBizInstockInfoRepository.delete(entity);
    }
    
    /**
     * 批量更新
     * @param list
     * @return
     */
    @Override
    public int updateBatch(List<Map<String, Object>> list){
    	return bmsBizInstockInfoRepository.updateBatch(list);
    }
	
}
