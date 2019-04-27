/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.appconfig;

import org.springframework.stereotype.Component;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月15日 下午4:57:39
 */
@Component
public class TenantConfig {
    
    private Long tenantId;
    private String tenantName;
    
    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    public String getTenantName() {
        return tenantName;
    }
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    
}


