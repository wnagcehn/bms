package com.jiuyescm.bms.customercalc;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 商家各科目计算状况
 * @author zhaofeng
 *
 */
public class CustomerSubjectStatusEntity implements IEntity{
	
	private static final long serialVersionUID = -1;
	
	//费用科目
	private String subjectCode;
	//科目名称
	private String subjectName;
	//计算状态
	private String calcStatus;
	// 费用总数
	private Integer feesCount;
	// 计算成功总数
	private Integer finishCount;
	// 合同缺失总数
	private Integer contractMissCount;
	// 报价缺失总数
	private Integer quoteMissCount;
	// 不计算费用总数
	private Integer noExeCount;	
	// 系统错误用总数
	private Integer sysErrorCount;
	// 未计算费用总数
	private Integer beginCount;
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
	public String getCalcStatus() {
		return calcStatus;
	}
	public void setCalcStatus(String calcStatus) {
		this.calcStatus = calcStatus;
	}
	public Integer getFeesCount() {
		return feesCount;
	}
	public void setFeesCount(Integer feesCount) {
		this.feesCount = feesCount;
	}
	public Integer getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	public Integer getContractMissCount() {
		return contractMissCount;
	}
	public void setContractMissCount(Integer contractMissCount) {
		this.contractMissCount = contractMissCount;
	}
	public Integer getQuoteMissCount() {
		return quoteMissCount;
	}
	public void setQuoteMissCount(Integer quoteMissCount) {
		this.quoteMissCount = quoteMissCount;
	}
	public Integer getNoExeCount() {
		return noExeCount;
	}
	public void setNoExeCount(Integer noExeCount) {
		this.noExeCount = noExeCount;
	}
	public Integer getSysErrorCount() {
		return sysErrorCount;
	}
	public void setSysErrorCount(Integer sysErrorCount) {
		this.sysErrorCount = sysErrorCount;
	}
	public Integer getBeginCount() {
		return beginCount;
	}
	public void setBeginCount(Integer beginCount) {
		this.beginCount = beginCount;
	}
	
}
