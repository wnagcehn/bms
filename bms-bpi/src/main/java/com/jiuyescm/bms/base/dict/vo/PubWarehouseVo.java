/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.base.dict.vo;

import java.io.Serializable;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年7月15日 下午7:49:38
 */
public class PubWarehouseVo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 7521904889720972213L;
    // 仓库ID
    private String warehouseid;
    // 仓库助记码
    private String warehousecode;
    // 仓库名称
    private String warehousename;
    
    public String getWarehouseid() {
        return warehouseid;
    }
    public void setWarehouseid(String warehouseid) {
        this.warehouseid = warehouseid;
    }
    public String getWarehousecode() {
        return warehousecode;
    }
    public void setWarehousecode(String warehousecode) {
        this.warehousecode = warehousecode;
    }
    public String getWarehousename() {
        return warehousename;
    }
    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }
    
}


