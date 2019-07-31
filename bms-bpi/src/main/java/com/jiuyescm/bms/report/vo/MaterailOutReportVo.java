/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.report.vo;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * <功能描述>
 * 耗材使用量汇总
 * @author caojianwei
 * @date 2019年7月31日 上午9:52:37
 */
public class MaterailOutReportVo  implements IEntity{

    private static final long serialVersionUID = 3929832830250945903L;
    
    private String customerId;
    private String customerName; 
    private String warehouseCode;
    private String warehouseName;
    private Integer orderCount;     //订单数
    
    private Integer platicBoxJY01;  //九曳1号泡沫箱
    private Integer platicBoxJY02;  //九曳2号泡沫箱
    private Integer platicBoxJY03;  //九曳3号泡沫箱
    private Integer platicBoxJY04;  //九曳4号泡沫箱
    private Integer platicBoxKM01;  //昆明1号泡沫箱
    private Integer platicBoxKM02;  //水果箱
    
    private Integer platicBox;          //泡沫箱
    private Integer cushioningMaterial; //缓冲材料
    private Integer paperCarton;        //纸箱
    private Integer dryIce;             //干冰
    private Integer iceBag;             //冰袋
    private Integer other;              //其他
    private Integer labelPaper;         //标签纸
    private Integer waterproofBox;      //防水袋
    private Integer warnBox;            //保温袋
    private Integer expressBox;         //快递袋
    private Integer goodCharacter;      //好字帖
    private Integer adhesiveType;       //胶带
    private Integer greetingCard;       //问候卡
    private Integer singlePlane;        //面单
    private Integer freshBox;           //保鲜袋
    private Integer card;               //卡片
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
     * 九曳1号泡沫箱
     */
    public Integer getPlaticBoxJY01() {
        return platicBoxJY01;
    }
    
    /**
     * 九曳1号泡沫箱
     */
    public void setPlaticBoxJY01(Integer platicBoxJY01) {
        this.platicBoxJY01 = platicBoxJY01;
    }
    
    /**
     * 九曳2号泡沫箱
     */
    public Integer getPlaticBoxJY02() {
        return platicBoxJY02;
    }
    
    /**
     * 九曳2号泡沫箱
     */
    public void setPlaticBoxJY02(Integer platicBoxJY02) {
        this.platicBoxJY02 = platicBoxJY02;
    }
    
    /**
     * 九曳3号泡沫箱
     */
    public Integer getPlaticBoxJY03() {
        return platicBoxJY03;
    }
    
    /**
     * 九曳3号泡沫箱
     */
    public void setPlaticBoxJY03(Integer platicBoxJY03) {
        this.platicBoxJY03 = platicBoxJY03;
    }
    
    /**
     * 九曳4号泡沫箱
     */
    public Integer getPlaticBoxJY04() {
        return platicBoxJY04;
    }
    
    /**
     * 九曳4号泡沫箱
     */
    public void setPlaticBoxJY04(Integer platicBoxJY04) {
        this.platicBoxJY04 = platicBoxJY04;
    }
    
    /**
     * 昆明1号泡沫箱
     */
    public Integer getPlaticBoxKM01() {
        return platicBoxKM01;
    }
    
    /**
     * 昆明1号泡沫箱
     */
    public void setPlaticBoxKM01(Integer platicBoxKM01) {
        this.platicBoxKM01 = platicBoxKM01;
    }
    
    /**
     * 水果箱
     */
    public Integer getPlaticBoxKM02() {
        return platicBoxKM02;
    }
    
    /**
     * 水果箱
     */
    public void setPlaticBoxKM02(Integer platicBoxKM02) {
        this.platicBoxKM02 = platicBoxKM02;
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
    public Integer getDryIce() {
        return dryIce;
    }
    /**
     * 干冰
     */
    public void setDryIce(Integer dryIce) {
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
    

}


