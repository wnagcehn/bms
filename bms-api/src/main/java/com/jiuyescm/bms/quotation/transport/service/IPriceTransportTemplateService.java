/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;

/**
 * 运输报价模板操作API
 * @author wubangjun
 */
public interface IPriceTransportTemplateService {

    PageInfo<PriceTransportTemplateEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    PriceTransportTemplateEntity query(Map<String, Object> condition);

    PriceTransportTemplateEntity findById(Long id);

    PriceTransportTemplateEntity save(PriceTransportTemplateEntity entity);

    PriceTransportTemplateEntity update(PriceTransportTemplateEntity entity);

    void delete(Long id);
    
    Integer findIdByTemplateCode(String templateCode);

}
