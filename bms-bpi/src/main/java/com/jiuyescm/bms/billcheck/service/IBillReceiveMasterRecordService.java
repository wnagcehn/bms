package com.jiuyescm.bms.billcheck.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterRecordVo;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillReceiveMasterRecordService {

    BillReceiveMasterRecordVo findById(Long id);
	
    PageInfo<BillReceiveMasterRecordVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillReceiveMasterRecordVo> query(Map<String, Object> condition);

    int save(BillReceiveMasterRecordVo entity);

    BillReceiveMasterRecordVo update(BillReceiveMasterRecordVo entity);

    void delete(Long id);

}
