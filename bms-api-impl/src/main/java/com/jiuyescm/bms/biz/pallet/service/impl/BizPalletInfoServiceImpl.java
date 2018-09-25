package com.jiuyescm.bms.biz.pallet.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoService;

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
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BizPalletInfoEntity findById(Long id) {
        return bizPalletInfoRepository.findById(id);
    }
	
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
    public BizPalletInfoEntity delete(BizPalletInfoEntity entity) {
        return bizPalletInfoRepository.delete(entity);
    }
	
}
