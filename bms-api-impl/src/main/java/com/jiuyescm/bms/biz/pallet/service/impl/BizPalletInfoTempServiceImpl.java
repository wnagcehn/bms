package com.jiuyescm.bms.biz.pallet.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoTempEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoTempRepository;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoTempService;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizPalletInfoTempService")
public class BizPalletInfoTempServiceImpl implements IBizPalletInfoTempService {

	@Autowired
    private IBizPalletInfoTempRepository bizPalletInfoTempRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BizPalletInfoTempEntity findById(Long id) {
        return bizPalletInfoTempRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BizPalletInfoTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizPalletInfoTempRepository.query(condition, pageNo, pageSize);
    }
    
    /**
     * 校验唯一性
     * @param taskId
     * @return
     */
	@Override
	public List<BizPalletInfoTempEntity> queryInBiz(String taskId, int errorNum) {
		return bizPalletInfoTempRepository.queryInBiz(taskId, errorNum);
	}
	
    /**
     * 校验唯一性，无limit
     * @param taskId
     * @return
     */
	@Override
	public List<BizPalletInfoTempEntity> queryInBizNotLimit(String taskId) {
		return bizPalletInfoTempRepository.queryInBizNotLimit(taskId);
	}
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizPalletInfoTempEntity> query(Map<String, Object> condition){
		return bizPalletInfoTempRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoTempEntity save(BizPalletInfoTempEntity entity) {
        return bizPalletInfoTempRepository.save(entity);
    }
    
	/**
	 * 批量保存
	 * @param entity
	 * @return
	 */
    @Override
    public void saveBatch(List<BizPalletInfoTempEntity> list) {
        bizPalletInfoTempRepository.saveBatch(list);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoTempEntity update(BizPalletInfoTempEntity entity) {
        return bizPalletInfoTempRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        bizPalletInfoTempRepository.delete(id);
    }
    
    /**
     * 从临时表保存到业务表
     * @param taskId
     * @return
     */
	@Override
	public int saveTempData(String taskId) {
		// TODO Auto-generated method stub
		return bizPalletInfoTempRepository.saveTempData(taskId);
	}
	
	/**
	 * 批量删除
	 * @param taskId
	 * @return
	 */
	@Override
	public int deleteBybatchNum(String taskId) {
		// TODO Auto-generated method stub
		return bizPalletInfoTempRepository.deleteBybatchNum(taskId);
	}
	

	@Override
	public List<BizPalletInfoTempEntity> queryNeedInsert(String taskId) {
		return bizPalletInfoTempRepository.queryNeedInsert(taskId);
	}
	

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	@Override
	public int saveData(List<BizPalletInfoTempEntity> insertList, List<BizPalletInfoTempEntity> updateList) {
		int s = bizPalletInfoTempRepository.importSaveBatch(insertList);
		int u = bizPalletInfoTempRepository.importUpdatePalletNumBatch(updateList);
		return s+u;
	}
	
}
