package com.jiuyescm.bms.biz.pallet.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoService;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizPalletInfoService")
public class BizPalletInfoServiceImpl implements IBizPalletInfoService {

	@Autowired
    private IBizPalletInfoRepository bizPalletInfoRepository;
	
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
    @Override
    public BizPalletInfoEntity update(BizPalletInfoEntity entity) {
        return bizPalletInfoRepository.update(entity);
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
     * 批量更新
     * @param list
     * @return
     */
    @Override
    public int updateBatch(List<Map<String, Object>> list){
    	return bizPalletInfoRepository.updateBatch(list);
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
