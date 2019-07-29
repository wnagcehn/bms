/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.base.dict.vo;

import java.io.Serializable;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年7月15日 下午7:51:13
 */
public class PubMaterialVo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 831826684694906294L;
    // 物料编号
    private String materialNo;
    // 物料名称
    private String materialName;
    // 条码
    private String barcode;
    public String getMaterialNo() {
        return materialNo;
    }
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getMaterialType() {
        return materialType;
    }
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
    // 物料类别(数据字典编码MATERIAL_TYPE)
    private String materialType;
}


