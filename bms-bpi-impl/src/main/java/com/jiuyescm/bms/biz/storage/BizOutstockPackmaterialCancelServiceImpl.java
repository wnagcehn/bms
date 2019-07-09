package com.jiuyescm.bms.biz.storage;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialCancelEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialCancelRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialCancelService;
import com.jiuyescm.exception.BizException;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizOutstockPackmaterialCancelService")
public class BizOutstockPackmaterialCancelServiceImpl implements IBizOutstockPackmaterialCancelService {

    private Logger logger = LoggerFactory.getLogger(AddFeeUnitServiceImpl.class);

	@Autowired
    private IBizOutstockPackmaterialCancelRepository bizOutstockPackmaterialCancelRepository;
	@Autowired
	private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BizOutstockPackmaterialCancelEntity findById(Long id) {
        return bizOutstockPackmaterialCancelRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizOutstockPackmaterialCancelRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition){
		return bizOutstockPackmaterialCancelRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizOutstockPackmaterialCancelEntity save(BizOutstockPackmaterialCancelEntity entity) {
        return bizOutstockPackmaterialCancelRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizOutstockPackmaterialCancelEntity update(BizOutstockPackmaterialCancelEntity entity) {
        return bizOutstockPackmaterialCancelRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        bizOutstockPackmaterialCancelRepository.delete(id);
    }
    
    /**
     * 有就更新，没有就新增
     * @param entity
     * @return
     */
    @Override
    public int saveOrUpdate(List<BizOutstockPackmaterialCancelEntity> cancelList) {
        try {
            bizOutstockPackmaterialCancelRepository.saveOrUpdate(cancelList);
            return 1;
        } catch (Exception e) {
            logger.error("异常",e);
            return 0;
        }   
    }
	
    /**
    * 查询需要作废的运单
    * @param page
    * @param param
    */
    @Override
    public List<BizOutstockPackmaterialCancelEntity> queryNeedCancel(Map<String, Object> map){
        return bizOutstockPackmaterialCancelRepository.queryNeedCancel(map);
    }
    
    /**
     * 批量更新状态
     */
    @Override
    public int updateBatchStatus(List<BizOutstockPackmaterialCancelEntity> list) {
        try {
            bizOutstockPackmaterialCancelRepository.updateBatchStatus(list);
            return 1;
        } catch (Exception e) {
            logger.error("异常",e);
            return 0;
        }   
    }
    
    @Transactional(readOnly = false, propagation=Propagation.REQUIRED)
    @Override
    public void updateBatchStatusAndDelMaterial(List<BizOutstockPackmaterialCancelEntity> cancelList, List<BizOutstockPackmaterialEntity> materialList){
        try {
            //业务表作废
            bizOutstockPackmaterialRepository.deleteOrRevertMaterialStatus(materialList);
            //Cancel表状态修改
            bizOutstockPackmaterialCancelRepository.updateBatchStatus(cancelList);
            //费用表作废
            bizOutstockPackmaterialRepository.delFees(materialList);
        } catch (Exception e) {
            throw new BizException("作废耗材&修改状态异常！", e);
        }
    }
    
}
