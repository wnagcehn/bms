package com.jiuyescm.bms.file.templet.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.templet.BmsTempletInfoEntity;

public interface IBmsTempletInfoRepository {
	
	PageInfo<BmsTempletInfoEntity> queryPage(Map<String, Object> condition, int pageNo,int pageSize);
    
    List<BmsTempletInfoEntity> queryList(Map<String, Object> condition);

    BmsTempletInfoEntity findById(Long id);
    
    BmsTempletInfoEntity findByCode(String templetCode);

    BmsTempletInfoEntity save(BmsTempletInfoEntity entity);

    BmsTempletInfoEntity update(BmsTempletInfoEntity entity);
}
