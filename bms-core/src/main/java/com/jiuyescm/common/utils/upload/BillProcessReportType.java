/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.common.utils.upload;

/**
  * <功能描述>
  * 
  * @author wangchen870
  * @date 2019年6月17日 下午3:35:30
  */
public class BillProcessReportType extends BaseDataType {

    public BillProcessReportType(){
        this.name = "结算进度报表导出";
        this.dataProps.add(new DataProperty("createMonth", "账单年月"));
        this.dataProps.add(new DataProperty("mkInvoiceName", "商家合同名称"));
        this.dataProps.add(new DataProperty("isPrepare", "是否生成预账单"));
        this.dataProps.add(new DataProperty("prepareTime", "生成预账单日期"));
        this.dataProps.add(new DataProperty("prepareAmount", "预账单金额"));
        this.dataProps.add(new DataProperty("isImport", "是否导入账单"));
        this.dataProps.add(new DataProperty("billName", "账单名称"));
        this.dataProps.add(new DataProperty("billCheckStatus", "对账状态"));
        this.dataProps.add(new DataProperty("confirmDate", "账单确认日期"));
        this.dataProps.add(new DataProperty("invoiceStatus", "开票状态"));
        this.dataProps.add(new DataProperty("invoiceDate", "开票日期"));
        this.dataProps.add(new DataProperty("receiptStatus", "收款状态"));
        this.dataProps.add(new DataProperty("receiptDate", "收款日期"));
        this.dataProps.add(new DataProperty("confirmAmount", "账单确认金额"));
        this.dataProps.add(new DataProperty("invoiceAmount", "发票金额"));
        this.dataProps.add(new DataProperty("receiptAmount", "收款金额"));
        this.dataProps.add(new DataProperty("balanceName", "结算员"));
    }
    
}


