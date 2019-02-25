package com.jiuyescm.bms.quotation.discount.service.impl;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.repository.IBmsQuoteDiscountDetailRepository;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountDetailService;
import com.jiuyescm.bms.quotation.dispatch.entity.BmsQuoteDispatchDetailEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsQuoteDiscountDetailService")
public class BmsQuoteDiscountDetailServiceImpl implements IBmsQuoteDiscountDetailService {

	@Autowired
    private IBmsQuoteDiscountDetailRepository bmsQuoteDiscountDetailRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsQuoteDiscountDetailEntity findById(Long id) {
        return bmsQuoteDiscountDetailRepository.findById(id);
    }
	
	@Override
	public List<BmsQuoteDiscountDetailEntity> queryByTemplateCode(String code) {
		return bmsQuoteDiscountDetailRepository.queryByTemplateCode(code);
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsQuoteDiscountDetailEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsQuoteDiscountDetailRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsQuoteDiscountDetailEntity> query(Map<String, Object> condition){
		return bmsQuoteDiscountDetailRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountDetailEntity save(BmsQuoteDiscountDetailEntity entity) {
        return bmsQuoteDiscountDetailRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountDetailEntity update(BmsQuoteDiscountDetailEntity entity) {
        return bmsQuoteDiscountDetailRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(BmsQuoteDiscountDetailEntity entity) {
        bmsQuoteDiscountDetailRepository.delete(entity);
    }
    
	@Override
	public int insertBatchTmp(List<BmsQuoteDiscountDetailEntity> list) throws Exception {
		List<BmsQuoteDiscountDetailEntity> result = new ArrayList<BmsQuoteDiscountDetailEntity>();
		if (null != list && list.size() > 0) {
			PropertyUtils.copyProperties(result, list);
			
			BmsQuoteDiscountDetailEntity entity = null;
			for (BmsQuoteDiscountDetailEntity vo : list) {
				entity = new BmsQuoteDiscountDetailEntity();
				PropertyUtils.copyProperties(entity, vo);
				result.add(entity);
			}
			return bmsQuoteDiscountDetailRepository.insertBatch(result);
		}
		return 0;
	}

    @Override
    public String queryServiceTypeName(Map<String, Object> condition) {
       return  bmsQuoteDiscountDetailRepository.queryServiceTypeName(condition);
    }
    
    @Override
    public String queryServiceTypeCode(Map<String, Object> condition) {
       return  bmsQuoteDiscountDetailRepository.queryServiceTypeCode(condition);
    }
	
}
