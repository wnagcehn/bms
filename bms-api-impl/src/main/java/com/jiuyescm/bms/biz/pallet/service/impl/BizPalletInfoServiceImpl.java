package com.jiuyescm.bms.biz.pallet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoService;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.exception.BizException;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizPalletInfoService")
public class BizPalletInfoServiceImpl implements IBizPalletInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(BizPalletInfoServiceImpl.class.getName());

	@Autowired
    private IBizPalletInfoRepository bizPalletInfoRepository;
	@Autowired
	private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BizPalletInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizPalletInfoRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizPalletInfoEntity> query(Map<String, Object> condition){
		return bizPalletInfoRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity save(BizPalletInfoEntity entity) {
        return bizPalletInfoRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public int update(BizPalletInfoEntity entity) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("feesNo", entity.getFeesNo());
    	try {
    		bizPalletInfoRepository.update(entity);
    		feesReceiveStorageRepository.updateIsCalcuByFeesNo(map);
		} catch (Exception e) {
			logger.error("更新异常!", e);
			return 0;
		}
    	return 1;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public int delete(List<BizPalletInfoEntity> list) {
        return bizPalletInfoRepository.delete(list);
    }
    
    /**
     * 批量更新业务表调整托数
     * 批量更新费用表计算状态为99并且更新调整托数
     * @param list
     * @return
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public int updateBatch(List<Map<String, Object>> list){
    	try {
    		bizPalletInfoRepository.updateBatch(list);
        	bizPalletInfoRepository.updateBatchFees(list);
		} catch (Exception e) {
			logger.error("更新异常!", e);
			throw new BizException("更新异常!",e);
		}
    	return 1;
    }
    
    /**
     * 批量更新导入托数
     * @param list
     * @return
     */
    @Override
    public int updatePalletNumBatch(List<BizPalletInfoEntity> list){
    	return bizPalletInfoRepository.updatePalletNumBatch(list);
    }
    
    /**
     * 重算
     * @param param
     * @return
     */
	@Override
	public int reCalculate(List<BizPalletInfoEntity> list) {
		return bizPalletInfoRepository.reCalculate(list);
	}
	
    /**
     * 重算（新）
     * @param param
     * @return
     */
	@Override
	public int retryCalculate(List<BizPalletInfoEntity> list) {
		return bizPalletInfoRepository.retryCalculate(list);
	}
	
	/**
	 * 分组统计
	 * @param condition
	 * @return
	 */
	@Override
    public PageInfo<BizPalletInfoEntity> groupCount(Map<String, Object> condition,
            int pageNo, int pageSize){
		return bizPalletInfoRepository.groupCount(condition, pageNo, pageSize);
	}
	
}
