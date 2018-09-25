package com.jiuyescm.bms.biz.pallet.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizPalletInfoRepository")
public class BizPalletInfoRepositoryImpl extends MyBatisDao<BizPalletInfoEntity> implements IBizPalletInfoRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BizPalletInfoEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizPalletInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizPalletInfoEntity> list = selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizPalletInfoEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizPalletInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity save(BizPalletInfoEntity entity) {
        insert("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity update(BizPalletInfoEntity entity) {
        update("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BizPalletInfoEntity delete(BizPalletInfoEntity entity) {
    	update("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.update", entity);
    	return entity;
    }
	
}
