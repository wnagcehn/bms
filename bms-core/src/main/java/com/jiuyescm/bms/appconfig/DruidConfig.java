/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.appconfig;

import org.springframework.stereotype.Component;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年6月3日 下午5:42:16
 */
@Component
public class DruidConfig {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


