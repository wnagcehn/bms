package com.jiuyescm.bms.bill.customer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillCustomerDetailEntity implements IEntity {

	/**
     * 
     */
    private static final long serialVersionUID = -2714932050502161764L;
    // id
	private Long id;
	
	// 业务年月  201906
	@ExcelField(title = "账单年月", num = 1)
	private Integer createMonth;
	
	// MkId
	private String mkId;
	
	// 主商家名称
	@ExcelField(title = "商家合同名称", num = 2)
	private String mkInvoiceName;
	
	// CheckId
	private Integer checkId;
	
	// 是否生产预账单
	@ExcelField(title = "是否生成预账单", num = 3)
	private String isPrepare;
	
	// 预账单生成时间
	@ExcelField(title = "生成预账单日期", num = 4)
	private Timestamp prepareTime;
	
	// 预账单金额
	@ExcelField(title = "预账单金额", num = 5)
	private BigDecimal prepareAmount;
	
	// 是否导入账单
	@ExcelField(title = "是否导入账单", num = 6)
	private String isImport;
	
	// 写入时间
	private Timestamp creTime;
	
	@ExcelField(title = "账单名称", num = 7)
	private String billName;
	@ExcelField(title = "对账状态", num = 8)
	private String billCheckStatus;
	@ExcelField(title = "账单确认日期", num = 9)
	private Date confirmDate;
	@ExcelField(title = "开票状态", num = 10)
	private String invoiceStatus;
	@ExcelField(title = "开票日期", num = 11)
	private Date invoiceDate;
	@ExcelField(title = "收款状态", num = 12)
	private String receiptStatus;
	@ExcelField(title = "收款日期", num = 13)
	private Date receiptDate;
	@ExcelField(title = "账单确认金额", num = 14)
	private BigDecimal confirmAmount;
	@ExcelField(title = "发票金额", num = 15)
	private BigDecimal invoiceAmount;
	@ExcelField(title = "收款金额", num = 16)
	private BigDecimal receiptAmount;
	@ExcelField(title = "结算员", num = 17)
	private String balanceName;

	public BillCustomerDetailEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	public String getMkId() {
		return this.mkId;
	}

	public void setMkId(String mkId) {
		this.mkId = mkId;
	}
	
	public String getMkInvoiceName() {
		return this.mkInvoiceName;
	}

	public void setMkInvoiceName(String mkInvoiceName) {
		this.mkInvoiceName = mkInvoiceName;
	}
	
	public Integer getCheckId() {
		return this.checkId;
	}

	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}
	
	public String getIsPrepare() {
		return this.isPrepare;
	}

	public void setIsPrepare(String isPrepare) {
		this.isPrepare = isPrepare;
	}
	
	public Timestamp getPrepareTime() {
		return this.prepareTime;
	}

	public void setPrepareTime(Timestamp prepareTime) {
		this.prepareTime = prepareTime;
	}
	
	public BigDecimal getPrepareAmount() {
		return this.prepareAmount;
	}

	public void setPrepareAmount(BigDecimal prepareAmount) {
		this.prepareAmount = prepareAmount;
	}
	
	public String getIsImport() {
		return this.isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillCheckStatus() {
        return billCheckStatus;
    }

    public void setBillCheckStatus(String billCheckStatus) {
        this.billCheckStatus = billCheckStatus;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BigDecimal getConfirmAmount() {
        return confirmAmount;
    }

    public void setConfirmAmount(BigDecimal confirmAmount) {
        this.confirmAmount = confirmAmount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getBalanceName() {
        return balanceName;
    }

    public void setBalanceName(String balanceName) {
        this.balanceName = balanceName;
    }
    
}
