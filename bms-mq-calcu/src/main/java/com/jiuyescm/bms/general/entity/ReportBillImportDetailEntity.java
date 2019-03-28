package com.jiuyescm.bms.general.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class ReportBillImportDetailEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -372108332919754954L;
	// id
	private Long id;
	// 账单编号
	private String billNo;
	// 仓库编码
	private String warehouseCode;
	// 仓库编码
	private String warehouseName;
	// 常温托数
	private BigDecimal palletCwNum;
	// 商品存储常温金额
	private BigDecimal palletCwAmount;
	// 恒温托数
	private BigDecimal palletHwNum;
	// 商品存储恒温金额
	private BigDecimal palletHwAmount;
	// 冷冻托收
	private BigDecimal palletLdNum;
	// 商品存储冷冻金额
	private BigDecimal palletLdAmount;
	// 冷藏托收
	private BigDecimal palletLcNum;
	// 商品存储冷藏金额
	private BigDecimal palletLcAmount;
	// 出库托盘数
	private BigDecimal palletOutNum;
	// 转仓托盘数
	private BigDecimal palletTransNum;
	// 仓租常温金额
	private BigDecimal rentCwAmount;
	// 仓租恒温金额
	private BigDecimal rentHwAmount;
	// 仓租冷冻金额
	private BigDecimal rentLdAmount;
	// 仓租冷藏金额
	private BigDecimal rentLcAmount;
	// 操作费金额
	private BigDecimal operateAmount;
	// 耗材费金额
	private BigDecimal materialAmount;
	// 仓储增值费
	private BigDecimal stAddAmount;
	// 仓储理赔费
	private BigDecimal stAbnormalAmount;
	// 宅配理赔费
	private BigDecimal deAbnormalAmount;
	// 宅配理赔单量
	private BigDecimal deAbnormalOrders;
	// tb出库箱数
	private BigDecimal stTboutNum;
	// 九曳配送金额
	private BigDecimal deJyAmount;
	// 九曳配送单量
	private BigDecimal deJyOrders;
	// 顺丰配送金额
	private BigDecimal deSfAmount;
	// 顺丰配送单量
	private BigDecimal deSfOrders;
	// 圆通配送金额
	private BigDecimal deYtoAmount;
	// 圆通配送单量
	private BigDecimal deYtoOrders;
	// 中通配送金额
	private BigDecimal deZtoAmount;
	// 中通配送单量
	private BigDecimal deZtoOrders;
	// 申通配送金额
	private BigDecimal deStoAmount;
	// 申通通配送单量
	private BigDecimal deStoOrders;
	// 优速配送金额
	private BigDecimal deUcAmount;
	// 优速配送单量
	private BigDecimal deUcOrders;
	// 德邦配送金额
	private BigDecimal deDbangAmount;
	// 德邦配送单量
	private BigDecimal deDbangOrders;
	// 跨越配送金额
	private BigDecimal deKyeAmount;
	// 跨越配送单量
	private BigDecimal deKyeOrders;
	// 运单配送金额
	private BigDecimal deYundaAmount;
	// 运单配送单量
	private BigDecimal deYundaOrders;
	// 百世汇通配送金额
	private BigDecimal deHtkyAmount;
	// 百世汇通配送单量
	private BigDecimal deHtkyOrders;
	// ems配送金额
	private BigDecimal deEmsAmount;
	// ems配送单量
	private BigDecimal deEmsOrders;
	// 承诺达配送金额
	private BigDecimal deOptAmount;
	// 承诺达配送单量
	private BigDecimal deOptOrders;
	// 写入时间
	private Timestamp writeTime;
	// 最后一次修改时间
	private Timestamp lastModifyTime;
	// 仓储收入小计
	private BigDecimal storageReceiptAmount;
	// 宅配收入小计
	private BigDecimal dispatchReceiptAmount;
	// 入库操作费
	private BigDecimal stInstockWorkAmount;
	// 入库卸货费
	private BigDecimal stB2cHandworkAmount;
	// 出库装车费
	private BigDecimal stB2bHandworkAmount;
	// 改地址退件费
	private BigDecimal deChangeAmount;

	public ReportBillImportDetailEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	public BigDecimal getPalletCwNum() {
		return this.palletCwNum;
	}

	public void setPalletCwNum(BigDecimal palletCwNum) {
		this.palletCwNum = palletCwNum;
	}
	
	public BigDecimal getPalletHwNum() {
		return this.palletHwNum;
	}

	public void setPalletHwNum(BigDecimal palletHwNum) {
		this.palletHwNum = palletHwNum;
	}
	
	public BigDecimal getPalletLdNum() {
		return this.palletLdNum;
	}

	public void setPalletLdNum(BigDecimal palletLdNum) {
		this.palletLdNum = palletLdNum;
	}
	
	public BigDecimal getPalletLcNum() {
		return this.palletLcNum;
	}

	public void setPalletLcNum(BigDecimal palletLcNum) {
		this.palletLcNum = palletLcNum;
	}
	
	public BigDecimal getPalletOutNum() {
		return this.palletOutNum;
	}

	public void setPalletOutNum(BigDecimal palletOutNum) {
		this.palletOutNum = palletOutNum;
	}
	
	public BigDecimal getPalletTransNum() {
		return this.palletTransNum;
	}

	public void setPalletTransNum(BigDecimal palletTransNum) {
		this.palletTransNum = palletTransNum;
	}
	
	public BigDecimal getRentCwAmount() {
		return this.rentCwAmount;
	}

	public void setRentCwAmount(BigDecimal rentCwAmount) {
		this.rentCwAmount = rentCwAmount;
	}
	
	public BigDecimal getRentHwAmount() {
		return this.rentHwAmount;
	}

	public void setRentHwAmount(BigDecimal rentHwAmount) {
		this.rentHwAmount = rentHwAmount;
	}
	
	public BigDecimal getRentLdAmount() {
		return this.rentLdAmount;
	}

	public void setRentLdAmount(BigDecimal rentLdAmount) {
		this.rentLdAmount = rentLdAmount;
	}
	
	public BigDecimal getRentLcAmount() {
		return this.rentLcAmount;
	}

	public void setRentLcAmount(BigDecimal rentLcAmount) {
		this.rentLcAmount = rentLcAmount;
	}
	
	public BigDecimal getOperateAmount() {
		return this.operateAmount;
	}

	public void setOperateAmount(BigDecimal operateAmount) {
		this.operateAmount = operateAmount;
	}
	
	public BigDecimal getMaterialAmount() {
		return this.materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount) {
		this.materialAmount = materialAmount;
	}
	
	public BigDecimal getStAddAmount() {
		return this.stAddAmount;
	}

	public void setStAddAmount(BigDecimal stAddAmount) {
		this.stAddAmount = stAddAmount;
	}
	
	public BigDecimal getStAbnormalAmount() {
		return this.stAbnormalAmount;
	}

	public void setStAbnormalAmount(BigDecimal stAbnormalAmount) {
		this.stAbnormalAmount = stAbnormalAmount;
	}
	
	public BigDecimal getStTboutNum() {
		return this.stTboutNum;
	}

	public void setStTboutNum(BigDecimal stTboutNum) {
		this.stTboutNum = stTboutNum;
	}
	
	public BigDecimal getDeJyAmount() {
		return this.deJyAmount;
	}

	public void setDeJyAmount(BigDecimal deJyAmount) {
		this.deJyAmount = deJyAmount;
	}
	
	public BigDecimal getDeJyOrders() {
		return this.deJyOrders;
	}

	public void setDeJyOrders(BigDecimal deJyOrders) {
		this.deJyOrders = deJyOrders;
	}
	
	public BigDecimal getDeSfAmount() {
		return this.deSfAmount;
	}

	public void setDeSfAmount(BigDecimal deSfAmount) {
		this.deSfAmount = deSfAmount;
	}
	
	public BigDecimal getDeSfOrders() {
		return this.deSfOrders;
	}

	public void setDeSfOrders(BigDecimal deSfOrders) {
		this.deSfOrders = deSfOrders;
	}
	
	public BigDecimal getDeYtoAmount() {
		return this.deYtoAmount;
	}

	public void setDeYtoAmount(BigDecimal deYtoAmount) {
		this.deYtoAmount = deYtoAmount;
	}
	
	public BigDecimal getDeYtoOrders() {
		return this.deYtoOrders;
	}

	public void setDeYtoOrders(BigDecimal deYtoOrders) {
		this.deYtoOrders = deYtoOrders;
	}
	
	public BigDecimal getDeZtoAmount() {
		return this.deZtoAmount;
	}

	public void setDeZtoAmount(BigDecimal deZtoAmount) {
		this.deZtoAmount = deZtoAmount;
	}
	
	public BigDecimal getDeZtoOrders() {
		return this.deZtoOrders;
	}

	public void setDeZtoOrders(BigDecimal deZtoOrders) {
		this.deZtoOrders = deZtoOrders;
	}
	
	public BigDecimal getDeUcAmount() {
		return this.deUcAmount;
	}

	public void setDeUcAmount(BigDecimal deUcAmount) {
		this.deUcAmount = deUcAmount;
	}
	
	public BigDecimal getDeUcOrders() {
		return this.deUcOrders;
	}

	public void setDeUcOrders(BigDecimal deUcOrders) {
		this.deUcOrders = deUcOrders;
	}
	
	public BigDecimal getDeDbangAmount() {
		return this.deDbangAmount;
	}

	public void setDeDbangAmount(BigDecimal deDbangAmount) {
		this.deDbangAmount = deDbangAmount;
	}
	
	public BigDecimal getDeDbangOrders() {
		return this.deDbangOrders;
	}

	public void setDeDbangOrders(BigDecimal deDbangOrders) {
		this.deDbangOrders = deDbangOrders;
	}
	
	public BigDecimal getDeKyeAmount() {
		return this.deKyeAmount;
	}

	public void setDeKyeAmount(BigDecimal deKyeAmount) {
		this.deKyeAmount = deKyeAmount;
	}
	
	public BigDecimal getDeKyeOrders() {
		return this.deKyeOrders;
	}

	public void setDeKyeOrders(BigDecimal deKyeOrders) {
		this.deKyeOrders = deKyeOrders;
	}
	
	public BigDecimal getDeYundaAmount() {
		return this.deYundaAmount;
	}

	public void setDeYundaAmount(BigDecimal deYundaAmount) {
		this.deYundaAmount = deYundaAmount;
	}
	
	public BigDecimal getDeYundaOrders() {
		return this.deYundaOrders;
	}

	public void setDeYundaOrders(BigDecimal deYundaOrders) {
		this.deYundaOrders = deYundaOrders;
	}
	
	public BigDecimal getDeHtkyAmount() {
		return this.deHtkyAmount;
	}

	public void setDeHtkyAmount(BigDecimal deHtkyAmount) {
		this.deHtkyAmount = deHtkyAmount;
	}
	
	public BigDecimal getDeHtkyOrders() {
		return this.deHtkyOrders;
	}

	public void setDeHtkyOrders(BigDecimal deHtkyOrders) {
		this.deHtkyOrders = deHtkyOrders;
	}
	
	public BigDecimal getDeEmsAmount() {
		return this.deEmsAmount;
	}

	public void setDeEmsAmount(BigDecimal deEmsAmount) {
		this.deEmsAmount = deEmsAmount;
	}
	
	public BigDecimal getDeEmsOrders() {
		return this.deEmsOrders;
	}

	public void setDeEmsOrders(BigDecimal deEmsOrders) {
		this.deEmsOrders = deEmsOrders;
	}
	
	public BigDecimal getDeOptAmount() {
		return this.deOptAmount;
	}

	public void setDeOptAmount(BigDecimal deOptAmount) {
		this.deOptAmount = deOptAmount;
	}
	
	public BigDecimal getDeOptOrders() {
		return this.deOptOrders;
	}

	public void setDeOptOrders(BigDecimal deOptOrders) {
		this.deOptOrders = deOptOrders;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public BigDecimal getStorageReceiptAmount() {
		return storageReceiptAmount;
	}

	public void setStorageReceiptAmount(BigDecimal storageReceiptAmount) {
		this.storageReceiptAmount = storageReceiptAmount;
	}

	public BigDecimal getDispatchReceiptAmount() {
		return dispatchReceiptAmount;
	}

	public void setDispatchReceiptAmount(BigDecimal dispatchReceiptAmount) {
		this.dispatchReceiptAmount = dispatchReceiptAmount;
	}

	public BigDecimal getStInstockWorkAmount() {
		return stInstockWorkAmount;
	}

	public void setStInstockWorkAmount(BigDecimal stInstockWorkAmount) {
		this.stInstockWorkAmount = stInstockWorkAmount;
	}

	public BigDecimal getStB2cHandworkAmount() {
		return stB2cHandworkAmount;
	}

	public void setStB2cHandworkAmount(BigDecimal stB2cHandworkAmount) {
		this.stB2cHandworkAmount = stB2cHandworkAmount;
	}

	public BigDecimal getStB2bHandworkAmount() {
		return stB2bHandworkAmount;
	}

	public void setStB2bHandworkAmount(BigDecimal stB2bHandworkAmount) {
		this.stB2bHandworkAmount = stB2bHandworkAmount;
	}

	public BigDecimal getDeChangeAmount() {
		return deChangeAmount;
	}

	public void setDeChangeAmount(BigDecimal deChangeAmount) {
		this.deChangeAmount = deChangeAmount;
	}

	public BigDecimal getPalletCwAmount() {
		return palletCwAmount;
	}

	public void setPalletCwAmount(BigDecimal palletCwAmount) {
		this.palletCwAmount = palletCwAmount;
	}

	public BigDecimal getPalletHwAmount() {
		return palletHwAmount;
	}

	public void setPalletHwAmount(BigDecimal palletHwAmount) {
		this.palletHwAmount = palletHwAmount;
	}

	public BigDecimal getPalletLdAmount() {
		return palletLdAmount;
	}

	public void setPalletLdAmount(BigDecimal palletLdAmount) {
		this.palletLdAmount = palletLdAmount;
	}

	public BigDecimal getPalletLcAmount() {
		return palletLcAmount;
	}

	public void setPalletLcAmount(BigDecimal palletLcAmount) {
		this.palletLcAmount = palletLcAmount;
	}

	public BigDecimal getDeAbnormalAmount() {
		return deAbnormalAmount;
	}

	public void setDeAbnormalAmount(BigDecimal deAbnormalAmount) {
		this.deAbnormalAmount = deAbnormalAmount;
	}

	public BigDecimal getDeAbnormalOrders() {
		return deAbnormalOrders;
	}

	public void setDeAbnormalOrders(BigDecimal deAbnormalOrders) {
		this.deAbnormalOrders = deAbnormalOrders;
	}

	public BigDecimal getDeStoAmount() {
		return deStoAmount;
	}

	public void setDeStoAmount(BigDecimal deStoAmount) {
		this.deStoAmount = deStoAmount;
	}

	public BigDecimal getDeStoOrders() {
		return deStoOrders;
	}

	public void setDeStoOrders(BigDecimal deStoOrders) {
		this.deStoOrders = deStoOrders;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
    
}
