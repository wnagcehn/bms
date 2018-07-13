package com.jiuyescm.bms.file.templet;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface IBmsTempletInfoService {

    PageInfo<BmsTempletInfoEntity> queryPage(Map<String, Object> condition, int pageNo,int pageSize);
    
    List<BmsTempletInfoEntity> queryList(Map<String, Object> condition);

    BmsTempletInfoEntity findById(Long id);
    
    BmsTempletInfoEntity findByCode(String templetCode);

    BmsTempletInfoEntity save(BmsTempletInfoEntity entity);

    BmsTempletInfoEntity update(BmsTempletInfoEntity entity);

}
