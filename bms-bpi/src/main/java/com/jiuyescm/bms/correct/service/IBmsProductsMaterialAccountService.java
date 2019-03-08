package com.jiuyescm.bms.correct.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsProductsMaterialAccountService {

    PageInfo<BmsProductsMaterialAccountVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

	List<BmsProductsMaterialAccountEntity> query(Map<String, Object> condition);

}
