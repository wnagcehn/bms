package com.jiuyescm.bms.pub.customer.entity;

import java.util.Date;

import com.jiuyescm.bms.pub.warehouse.entity.StockPlaceEntity;

public class CustStockPlaceEntity {
    private String custstockplaceid;

    private String stockplaceid;

    private String customerid;

    private Short delflag;

    private String crepersonid;

    private String creperson;

    private Date cretime;

    private String modpersonid;

    private String modperson;

    private Date modtime;
    
    private String stockplacename;
    
    /******************* 关联对象 *******************/
	//商家
	private CustomerEntity customer;

	//库存类型
	private StockPlaceEntity stockplace;

	public StockPlaceEntity getStockplace() {
		return stockplace;
	}
	
	public void setStockplace(StockPlaceEntity stockplace) {
		this.stockplace = stockplace;
	}
	
    public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public String getCuststockplaceid() {
        return custstockplaceid;
    }

    public void setCuststockplaceid(String custstockplaceid) {
        this.custstockplaceid = custstockplaceid == null ? null : custstockplaceid.trim();
    }

    public String getStockplaceid() {
        return stockplaceid;
    }

    public void setStockplaceid(String stockplaceid) {
        this.stockplaceid = stockplaceid == null ? null : stockplaceid.trim();
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid == null ? null : customerid.trim();
    }

    public Short getDelflag() {
        return delflag;
    }

    public void setDelflag(Short delflag) {
        this.delflag = delflag;
    }

    public String getCrepersonid() {
        return crepersonid;
    }

    public void setCrepersonid(String crepersonid) {
        this.crepersonid = crepersonid == null ? null : crepersonid.trim();
    }

    public String getCreperson() {
        return creperson;
    }

    public void setCreperson(String creperson) {
        this.creperson = creperson == null ? null : creperson.trim();
    }

    public Date getCretime() {
        return cretime;
    }

    public void setCretime(Date cretime) {
        this.cretime = cretime;
    }

    public String getModpersonid() {
        return modpersonid;
    }

    public void setModpersonid(String modpersonid) {
        this.modpersonid = modpersonid == null ? null : modpersonid.trim();
    }

    public String getModperson() {
        return modperson;
    }

    public void setModperson(String modperson) {
        this.modperson = modperson == null ? null : modperson.trim();
    }

    public Date getModtime() {
        return modtime;
    }

    public void setModtime(Date modtime) {
        this.modtime = modtime;
    }

	public String getStockplacename() {
		return stockplacename;
	}

	public void setStockplacename(String stockplacename) {
		this.stockplacename = stockplacename;
	}
    
    
    
}