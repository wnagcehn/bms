package com.jiuyescm.bms.billcheck.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.exception.BizException;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillReceiveMasterService {

    BillReceiveMasterVo findById(Long id);
	
    PageInfo<BillReceiveMasterVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillReceiveMasterVo> query(Map<String, Object> condition);

    int save(BillReceiveMasterVo entity);

    BillReceiveMasterVo update(BillReceiveMasterVo entity);
    
	void delete(String billNo,String status) throws BizException;

}
