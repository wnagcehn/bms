package com.jiuyescm.bms.asyn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.discount.repository.IBmsDiscountAsynTaskRepository;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsDiscountAsynTaskService")
public class BmsDiscountAsynTaskServiceImpl implements IBmsDiscountAsynTaskService {
	
	private static final Logger logger = Logger.getLogger(BmsDiscountAsynTaskServiceImpl.class.getName());
	
	@Autowired
    private IBmsDiscountAsynTaskRepository bmsDiscountAsynTaskRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsDiscountAsynTaskEntity findById(Long id) {
        return bmsDiscountAsynTaskRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsDiscountAsynTaskRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition){
		return bmsDiscountAsynTaskRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity) {
        return bmsDiscountAsynTaskRepository.save(entity);
    }
    
    /**
     * 批量保存
     * @param voList
     * @return
     * @throws Exception
     */
	@Override
	public int saveBatch(List<BmsDiscountAsynTaskEntity> voList) throws Exception {
		try{
			List<BmsDiscountAsynTaskEntity> list=new ArrayList<BmsDiscountAsynTaskEntity>();
			for(BmsDiscountAsynTaskEntity voEntity:voList){
				BmsDiscountAsynTaskEntity entity=new BmsDiscountAsynTaskEntity();
				PropertyUtils.copyProperties(entity,voEntity);
				list.add(entity);
			}
			return bmsDiscountAsynTaskRepository.saveBatch(list);
		}catch(Exception e){
			logger.error("saveBatch:",e);
			throw e;
		}
	}

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity) {
        return bmsDiscountAsynTaskRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        bmsDiscountAsynTaskRepository.delete(id);
    }

	@Override
	public BmsDiscountAsynTaskEntity queryTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsDiscountAsynTaskRepository.queryTask(condition);
	}
	
	@Override
	public boolean existTask(BmsDiscountAsynTaskEntity voEntity) throws Exception {
		try{
			BmsDiscountAsynTaskEntity entity=new BmsDiscountAsynTaskEntity();
			PropertyUtils.copyProperties(entity,voEntity);
			return bmsDiscountAsynTaskRepository.existTask(entity);
		}catch(Exception e){
			logger.error("existTask:",e);
			throw e;
		}
	}
	
}
