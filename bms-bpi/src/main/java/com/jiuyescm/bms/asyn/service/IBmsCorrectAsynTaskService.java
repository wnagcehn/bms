package com.jiuyescm.bms.asyn.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;

public interface IBmsCorrectAsynTaskService {

    PageInfo<BmsCorrectAsynTaskVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;
    
    List<BmsCorrectAsynTaskVo> queryList(Map<String, Object> condition) throws Exception;

    BmsCorrectAsynTaskVo findById(int id) throws Exception;

    int save(BmsCorrectAsynTaskVo vo) throws Exception;

    int update(BmsCorrectAsynTaskVo vo) throws Exception;

	List<String> queryCorrectCustomerList(Map<String, Object> conditionMap);

	boolean existTask(BmsCorrectAsynTaskVo voEntity) throws Exception;

	int saveBatch(List<BmsCorrectAsynTaskVo> voList) throws Exception;

	String updateCorrect(BmsCorrectAsynTaskVo vo) throws Exception;

}
