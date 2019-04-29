package com.jiuyescm.bms.base.file.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class BillPrepareExportTaskEntity implements IEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1098534385096747928L;
	// id
	private Integer id;
	// 任务id
	private String taskId;
	// 任务名称
	private String taskName;
	// 账单编号
	private String billNo;
	// 合同商家ID
	private String mkId;
	// 子商家id
	private String customerid;
	// 账单开始时间
	private Timestamp startTime;
	// 账单结束时间
	private Timestamp endTime;
	// 状态(0:已创建，1:进行中，2:成功，3:失败)
	private String taskState;
	// 仓储分仓标识 0-不分仓 1-分仓
	private Long storageSplit;
	// 耗材分仓标识 0-不分仓 1-分仓
	private Long materialSplit;
	// 是否是子商家
	private String isChildCustomer;
	// 进度
	private Double progress;
	// 文件路径
	private String filePath;
	// 创建人
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志 0-未作废 1-作废
	private String delFlag;
	
	//是否折扣
	private String isDiscount;
	//备注
	private String remark;

	public BillPrepareExportTaskEntity() {
		super();
	}
	
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getMkId() {
		return this.mkId;
	}

	public void setMkId(String mkId) {
		this.mkId = mkId;
	}
	
	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public String getTaskState() {
		return this.taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	
	public Long getStorageSplit() {
		return this.storageSplit;
	}

	public void setStorageSplit(Long storageSplit) {
		this.storageSplit = storageSplit;
	}
	
	public Long getMaterialSplit() {
		return this.materialSplit;
	}

	public void setMaterialSplit(Long materialSplit) {
		this.materialSplit = materialSplit;
	}
	

	
	public Double getProgress() {
		return progress;
	}

	public void setProgress(Double progress) {
		this.progress = progress;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getIsChildCustomer() {
		return isChildCustomer;
	}

	public void setIsChildCustomer(String isChildCustomer) {
		this.isChildCustomer = isChildCustomer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}
