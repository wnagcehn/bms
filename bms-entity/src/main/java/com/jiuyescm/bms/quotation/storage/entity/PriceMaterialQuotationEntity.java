package com.jiuyescm.bms.quotation.storage.entity;

import java.util.List;

import javax.persistence.Entity;

import com.jiuyescm.cfm.domain.IEntity;

@Entity public class PriceMaterialQuotationEntity implements IEntity{

	private static final long serialVersionUID = -4243692264061263109L;

	private String templateId;

    private String materialType;

    private String materialCode;

	private String specName;

    private String outsideDiameter;

    private String innerDiameter;

    private Double wallThickness;

    private String warehouseId;

    private Double unitPrice;

    private String remark;
    
    private Long id;
    
    private Integer line;
    /**
	 * 创建者
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private java.sql.Timestamp createTime;
	
	/**
	 * 最后修改者
	 */
	private String lastModifier;
	
	/**
	 * 最后修改时间
	 */
	private java.sql.Timestamp lastModifyTime;
	
	/**
	 * 删除标识
	 */
	private String delFlag;
	
	//报价list
	private List<PriceMaterialQuotationEntity> list;
  
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public java.sql.Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(java.sql.Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

    

    public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }

    public String getOutsideDiameter() {
        return outsideDiameter;
    }

    public void setOutsideDiameter(String outsideDiameter) {
        this.outsideDiameter = outsideDiameter == null ? null : outsideDiameter.trim();
    }

    public String getInnerDiameter() {
        return innerDiameter;
    }

    public void setInnerDiameter(String innerDiameter) {
        this.innerDiameter = innerDiameter == null ? null : innerDiameter.trim();
    }

    public Double getWallThickness() {
        return wallThickness;
    }

    public void setWallThickness(Double wallThickness) {
        this.wallThickness = wallThickness;
    }


	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public List<PriceMaterialQuotationEntity> getList() {
		return list;
	}

	public void setList(List<PriceMaterialQuotationEntity> list) {
		this.list = list;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

   
}