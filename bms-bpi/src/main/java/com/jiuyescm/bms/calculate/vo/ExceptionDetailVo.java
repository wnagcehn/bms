/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.vo;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年6月3日 下午4:21:48
 */
public class ExceptionDetailVo {
    /**
     * 商家ID

     */
    private String customerId;
    /**
     * 商家名称
     */
    private String customerName;
    /**
     * 主商家id
     */
    private String mkId;
    /**
     * 主商家名称
     */
    private String mkInvoiceName;
    /**
     * 合同归属
     */
    private String contractAttr;
    /**
     * 科目编码
     */
    private String subjectCode;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 计费单位
     */
    private String chargeUnit;
    /**
     * 仓库编码
     */
    private String warehouseCode;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 业务时间
     */
    private String createTime;
    /**
     * 出库单号
     */
    private String outstockNo;
    /**
     * 运单号
     */
    private String waybillNo;
    /**
     * 服务产品类型编码
     */
    private String serviceTypeCode;
    /**
     * 服务产品类型名称
     */
    private String serviceTypeName;
    /**
     * 月结账号
     */
    private String monthFeeCount;
    /**
     * 发件省
     */
    private String sendProvince;
    /**
     * 发件市
     */
    private String sendCity;
    /**
     * 收件省
     */
    private String receiveProvince;
    /**
     * 收件市
     */
    private String receiveCity;
    /**
     * 收件区
     */
    private String receiveDistrict;
    /**
     * 物流商ID
     */
    private String carrierId;
    /**
     * 物流商名称
     */
    private String carrierName;
    /**
     * 宅配商ID
     */
    private String deliverId;
    /**
     * 宅配商名称
     */
    private String deliverName;
    /**
     * 耗材编码
     */
    private String consumerMaterialCode;
    /**
     * 耗材名称
     */
    private String consumerMaterialName;
    /**
     * 包材组编号
     */
    private String packGroupNo;
    /**
     * 计算状态编码
     */
    private String isCalculated;
    /**
     * 计算状态
     */
    private String calcuStatus;
    /**
     * 计算描述
     */
    private String calcuMsg;
    /**
     * 每页显示的数据条数
     */
    private int pageSize;
    /**
     * 每页显示的数据条数
     */
    private int pageNo;
    
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMkId() {
        return mkId;
    }

    public void setMkId(String mkId) {
        this.mkId = mkId;
    }

    public String getMkInvoiceName() {
        return mkInvoiceName;
    }

    public void setMkInvoiceName(String mkInvoiceName) {
        this.mkInvoiceName = mkInvoiceName;
    }

    public String getContractAttr() {
        return contractAttr;
    }

    public void setContractAttr(String contractAttr) {
        this.contractAttr = contractAttr;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(String chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOutstockNo() {
        return outstockNo;
    }

    public void setOutstockNo(String outstockNo) {
        this.outstockNo = outstockNo;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getMonthFeeCount() {
        return monthFeeCount;
    }

    public void setMonthFeeCount(String monthFeeCount) {
        this.monthFeeCount = monthFeeCount;
    }

    public String getSendProvince() {
        return sendProvince;
    }

    public void setSendProvince(String sendProvince) {
        this.sendProvince = sendProvince;
    }

    public String getSendCity() {
        return sendCity;
    }

    public void setSendCity(String sendCity) {
        this.sendCity = sendCity;
    }

    public String getReceiveProvince() {
        return receiveProvince;
    }

    public void setReceiveProvince(String receiveProvince) {
        this.receiveProvince = receiveProvince;
    }

    public String getReceiveCity() {
        return receiveCity;
    }

    public void setReceiveCity(String receiveCity) {
        this.receiveCity = receiveCity;
    }

    public String getReceiveDistrict() {
        return receiveDistrict;
    }

    public void setReceiveDistrict(String receiveDistrict) {
        this.receiveDistrict = receiveDistrict;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public String getConsumerMaterialCode() {
        return consumerMaterialCode;
    }

    public void setConsumerMaterialCode(String consumerMaterialCode) {
        this.consumerMaterialCode = consumerMaterialCode;
    }

    public String getConsumerMaterialName() {
        return consumerMaterialName;
    }

    public void setConsumerMaterialName(String consumerMaterialName) {
        this.consumerMaterialName = consumerMaterialName;
    }

    public String getPackGroupNo() {
        return packGroupNo;
    }

    public void setPackGroupNo(String packGroupNo) {
        this.packGroupNo = packGroupNo;
    }

    public String getIsCalculated() {
        return isCalculated;
    }

    public void setIsCalculated(String isCalculated) {
        this.isCalculated = isCalculated;
    }

    public String getCalcuStatus() {
        return calcuStatus;
    }

    public void setCalcuStatus(String calcuStatus) {
        this.calcuStatus = calcuStatus;
    }

    public String getCalcuMsg() {
        return calcuMsg;
    }

    public void setCalcuMsg(String calcuMsg) {
        this.calcuMsg = calcuMsg;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}


