/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.report.vo;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * <功能描述>
 * 耗材使用量汇总
 * @author caojianwei
 * @date 2019年7月31日 上午9:52:37
 */
public class MaterailOutReportVo  implements IEntity{

    private static final long serialVersionUID = 3929832830250945903L;
    
    @ExcelField(title = "月份", num = 1)
    private String  billMonth;    //月份 
    private String customerId;
    @ExcelField(title = "商家名称", num = 2)
    private String customerName; 
    private String warehouseCode;
    @ExcelField(title = "仓库", num = 3)
    private String warehouseName;
    @ExcelField(title = "订单数", num = 4)
    private Integer orderCount;     //订单数
    @ExcelField(title = "九曳1号泡沫箱", num = 5)
    private Integer jyPmx01;  //九曳1号泡沫箱
    @ExcelField(title = "九曳2号泡沫箱", num = 6)
    private Integer jyPmx02;  //九曳2号泡沫箱
    @ExcelField(title = "九曳3号泡沫箱", num = 7)
    private Integer jyPmx03;  //九曳3号泡沫箱
    @ExcelField(title = "九曳4号泡沫箱", num = 8)
    private Integer jyPmx04;  //九曳4号泡沫箱
    @ExcelField(title = "昆明1号泡沫箱", num = 9)
    private Integer kmPmx001;  //昆明1号泡沫箱
    @ExcelField(title = "水果箱", num = 10)
    private Integer kmPmx002;  //水果箱
    
    private Integer platicBox;          //泡沫箱
    @ExcelField(title = "缓冲材料", num = 11)
    private Integer cushioningMaterial; //缓冲材料
    @ExcelField(title = "纸箱", num = 12)
    private Integer paperCarton;        //纸箱
    @ExcelField(title = "干冰", num = 13)
    private Double dryIce;             //干冰
    @ExcelField(title = "冰袋", num = 14)
    private Integer iceBag;             //冰袋
    @ExcelField(title = "其他", num = 15)
    private Integer other;              //其他
    @ExcelField(title = "标签纸", num = 16)
    private Integer labelPaper;         //标签纸
    @ExcelField(title = "防水袋", num = 17)
    private Integer waterproofBox;      //防水袋
    @ExcelField(title = "保温袋", num = 18)
    private Integer warnBox;            //保温袋
    @ExcelField(title = "快递袋", num = 19)
    private Integer expressBox;         //快递袋
    @ExcelField(title = "好字帖", num = 20)
    private Integer goodCharacter;      //好字帖
    @ExcelField(title = "胶带", num = 21)
    private Integer adhesiveType;       //胶带
    @ExcelField(title = "问候卡", num = 22)
    private Integer greetingCard;       //问候卡
    @ExcelField(title = "面单", num = 23)
    private Integer singlePlane;        //面单
    @ExcelField(title = "保鲜袋", num = 24)
    private Integer freshBox;           //保鲜袋
    @ExcelField(title = "卡片", num = 25)
    private Integer card;               //卡片
    @ExcelField(title = "塑封袋", num = 26)
    private Integer blockingBox;        //塑封袋

    
    /**
     * 商家ID
     */
    public String getCustomerId() {
        return customerId;
    }
    
    /**
     * 商家ID
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    /**
     * 商家名称
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * 商家名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    /**
     * 仓库编码
     */
    public String getWarehouseCode() {
        return warehouseCode;
    }
    
    /**
     * 仓库编码
     */
    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
    
    /**
     * 仓库名称
     */
    public String getWarehouseName() {
        return warehouseName;
    }
    
    /**
     * 仓库名称
     */
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    
    /**
     * 订单数量
     */
    public Integer getOrderCount() {
        return orderCount;
    }
    
    /**
     * 订单数量
     */
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
    
    
    /**
     * 泡沫箱
     */
    public Integer getPlaticBox() {
        return platicBox;
    }
    
    /**
     * 泡沫箱
     */
    public void setPlaticBox(Integer platicBox) {
        this.platicBox = platicBox;
    }
    
    /**
     * 缓存材料
     */
    public Integer getCushioningMaterial() {
        return cushioningMaterial;
    }
    
    /**
     * 缓存材料
     */
    public void setCushioningMaterial(Integer cushioningMaterial) {
        this.cushioningMaterial = cushioningMaterial;
    }
    
    /**
     * 纸箱
     */
    public Integer getPaperCarton() {
        return paperCarton;
    }
    
    /**
     * 纸箱
     */
    public void setPaperCarton(Integer paperCarton) {
        this.paperCarton = paperCarton;
    }
    
    /**
     * 干冰
     */
    public Double getDryIce() {
        return dryIce;
    }
    /**
     * 干冰
     */
    public void setDryIce(Double dryIce) {
        this.dryIce = dryIce;
    }
    
    /**
     * 冰袋
     */
    public Integer getIceBag() {
        return iceBag;
    }
    
    /**
     * 冰袋
     */
    public void setIceBag(Integer iceBag) {
        this.iceBag = iceBag;
    }
    
    /**
     * 其他
     */
    public Integer getOther() {
        return other;
    }
    
    /**
     * 其他
     */
    public void setOther(Integer other) {
        this.other = other;
    }
    
    /**
     * 标签纸
     */
    public Integer getLabelPaper() {
        return labelPaper;
    }
    
    /**
     * 标签纸
     */
    public void setLabelPaper(Integer labelPaper) {
        this.labelPaper = labelPaper;
    }
    
    /**
     * 防水袋
     */
    public Integer getWaterproofBox() {
        return waterproofBox;
    }
    
    /**
     * 防水袋
     */
    public void setWaterproofBox(Integer waterproofBox) {
        this.waterproofBox = waterproofBox;
    }
    
    /**
     * 保温袋
     */
    public Integer getWarnBox() {
        return warnBox;
    }
    
    /**
     * 保温袋
     */
    public void setWarnBox(Integer warnBox) {
        this.warnBox = warnBox;
    }
    
    /**
     * 快递袋
     */
    public Integer getExpressBox() {
        return expressBox;
    }
    
    /**
     * 快递袋
     */
    public void setExpressBox(Integer expressBox) {
        this.expressBox = expressBox;
    }
    
    /**
     * 好字帖
     */
    public Integer getGoodCharacter() {
        return goodCharacter;
    }
    
    /**
     * 好字帖
     */
    public void setGoodCharacter(Integer goodCharacter) {
        this.goodCharacter = goodCharacter;
    }
    
    /**
     * 胶带
     */
    public Integer getAdhesiveType() {
        return adhesiveType;
    }
    
    /**
     * 胶带
     */
    public void setAdhesiveType(Integer adhesiveType) {
        this.adhesiveType = adhesiveType;
    }
    
    /**
     * 问候卡
     */
    public Integer getGreetingCard() {
        return greetingCard;
    }
    
    /**
     * 问候卡
     */
    public void setGreetingCard(Integer greetingCard) {
        this.greetingCard = greetingCard;
    }
    
    /**
     * 面单
     */
    public Integer getSinglePlane() {
        return singlePlane;
    }
    /**
     * 面单
     */
    public void setSinglePlane(Integer singlePlane) {
        this.singlePlane = singlePlane;
    }
    /**
     * 保鲜袋
     */
    public Integer getFreshBox() {
        return freshBox;
    }
    /**
     * 保鲜袋
     */
    public void setFreshBox(Integer freshBox) {
        this.freshBox = freshBox;
    }
    /**
     * 卡片
     */
    public Integer getCard() {
        return card;
    }
    /**
     * 卡片
     */
    public void setCard(Integer card) {
        this.card = card;
    }
    /**
     * 塑封袋
     */
    public Integer getBlockingBox() {
        return blockingBox;
    }
    /**
     * 塑封袋
     */
    public void setBlockingBox(Integer blockingBox) {
        this.blockingBox = blockingBox;
    }


    /**
     * 月份
     */
    public String getBillMonth() {
        return billMonth;
    }

    /**
     * 月份
     */
    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    
    /**
     * 九曳1号泡沫箱
     */
    public Integer getJyPmx01() {
        return jyPmx01;
    }

    /**
     * 九曳1号泡沫箱
     */
    public void setJyPmx01(Integer jyPmx01) {
        this.jyPmx01 = jyPmx01;
    }

    /**
     * 九曳2号泡沫箱
     */
    public Integer getJyPmx02() {
        return jyPmx02;
    }

    /**
     * 九曳2号泡沫箱
     */
    public void setJyPmx02(Integer jyPmx02) {
        this.jyPmx02 = jyPmx02;
    }

    /**
     * 九曳3号泡沫箱
     */
    public Integer getJyPmx03() {
        return jyPmx03;
    }

    /**
     * 九曳3号泡沫箱
     */
    public void setJyPmx03(Integer jyPmx03) {
        this.jyPmx03 = jyPmx03;
    }

    /**
     * 九曳4号泡沫箱
     */
    public Integer getJyPmx04() {
        return jyPmx04;
    }

    /**
     * 九曳4号泡沫箱
     */
    public void setJyPmx04(Integer jyPmx04) {
        this.jyPmx04 = jyPmx04;
    }

    /**
     * 昆明1号泡沫箱
     */
    public Integer getKmPmx001() {
        return kmPmx001;
    }

    /**
     * 昆明1号泡沫箱
     */
    public void setKmPmx001(Integer kmPmx001) {
        this.kmPmx001 = kmPmx001;
    }

    /**
     * 昆明2号泡沫箱
     */
    public Integer getKmPmx002() {
        return kmPmx002;
    }

    /**
     * 昆明2号泡沫箱
     */
    public void setKmPmx002(Integer kmPmx002) {
        this.kmPmx002 = kmPmx002;
    }
}


