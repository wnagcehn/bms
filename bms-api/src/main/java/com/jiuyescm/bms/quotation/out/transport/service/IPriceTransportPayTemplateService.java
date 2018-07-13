/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayTemplateEntity;

/**
 * 应付运输报价模板操作API
 * @author wubangjun
 */
public interface IPriceTransportPayTemplateService {

    PageInfo<PriceTransportPayTemplateEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    PriceTransportPayTemplateEntity findById(Long id);

    PriceTransportPayTemplateEntity save(PriceTransportPayTemplateEntity entity);

    PriceTransportPayTemplateEntity update(PriceTransportPayTemplateEntity entity);

    void delete(Long id);
    
    Integer findIdByTemplateCode(String templateCode);

}
