package com.jiuyescm.bms.correct.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsOrderProductEntity;
import com.jiuyescm.bms.correct.vo.BmsOrderProductVo;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsOrderProductService {
	
    PageInfo<BmsOrderProductVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

	List<BmsOrderProductEntity> query(Map<String, Object> condition);
}
