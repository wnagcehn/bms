package com.jiuyescm.bms.pub.customer.entity;

import com.jiuyescm.cfm.domain.IEntity;
/**
 * 商家单据类型关系实体
 * @author zhengyishan
 *
 */
public class CusbilltypeRuleEntity implements IEntity {
	private static final long serialVersionUID = 3245725977842697606L;
	//id
	private String id;
	//单据类型ID
	private String ordertypeid;
	//单据类型名称 
	private String billname;
	//商家ID
	private String customerid;
	//是否必须整单过账
	private int mustwholepost;
	//是否可以关闭单据
	private int canfinish;
	//库存周转策略
	private int turnoverrule;
	//合单规则
	private int mergerule;
	//干线直配重量阀值
	private double weightlimit;
	//干线直配体积阀值
	private double volumelimit;
	//删除标记
	private int delflag;
	//创建者ID
	private String crepersonid;
	//创建者
	private String creperson;
	//创建时间
	private java.sql.Timestamp cretime;
	//修改者ID
	private String modpersonid;
	//修改者
	private String modperson;
	//修改时间
	private java.sql.Timestamp modtime;



    public CusbilltypeRuleEntity(){

    }
    
		public String getBillname() {
		return billname;
	}

	public void setBillname(String billname) {
		this.billname = billname;
	}

		public String getId(){
			return this.id;
		}

		public void setId(String theId){
			this.id =  theId;
		}
		public String getOrdertypeid(){
			return this.ordertypeid;
		}

		public void setOrdertypeid(String theOrdertypeid){
			this.ordertypeid =  theOrdertypeid;
		}
		public String getCustomerid(){
			return this.customerid;
		}

		public void setCustomerid(String theCustomerid){
			this.customerid =  theCustomerid;
		}
		public int getMustwholepost(){
			return this.mustwholepost;
		}

		public void setMustwholepost(int theMustwholepost){
			this.mustwholepost =  theMustwholepost;
		}
		public int getCanfinish(){
			return this.canfinish;
		}

		public void setCanfinish(int theCanfinish){
			this.canfinish =  theCanfinish;
		}
		public int getTurnoverrule(){
			return this.turnoverrule;
		}

		public void setTurnoverrule(int theTurnoverrule){
			this.turnoverrule =  theTurnoverrule;
		}
		public int getMergerule(){
			return this.mergerule;
		}

		public void setMergerule(int theMergerule){
			this.mergerule =  theMergerule;
		}
		public double getWeightlimit(){
			return this.weightlimit;
		}

		public void setWeightlimit(double theWeightlimit){
			this.weightlimit =  theWeightlimit;
		}
		public double getVolumelimit(){
			return this.volumelimit;
		}

		public void setVolumelimit(double theVolumelimit){
			this.volumelimit =  theVolumelimit;
		}
		public int getDelflag(){
			return this.delflag;
		}

		public void setDelflag(int theDelflag){
			this.delflag =  theDelflag;
		}
		public String getCrepersonid(){
			return this.crepersonid;
		}

		public void setCrepersonid(String theCrepersonid){
			this.crepersonid =  theCrepersonid;
		}
		public String getCreperson(){
			return this.creperson;
		}

		public void setCreperson(String theCreperson){
			this.creperson =  theCreperson;
		}
		public java.sql.Timestamp getCretime(){
			return this.cretime;
		}

		public void setCretime(java.sql.Timestamp theCretime){
			this.cretime =  theCretime;
		}
		public String getModpersonid(){
			return this.modpersonid;
		}

		public void setModpersonid(String theModpersonid){
			this.modpersonid =  theModpersonid;
		}
		public String getModperson(){
			return this.modperson;
		}

		public void setModperson(String theModperson){
			this.modperson =  theModperson;
		}
		public java.sql.Timestamp getModtime(){
			return this.modtime;
		}

		public void setModtime(java.sql.Timestamp theModtime){
			this.modtime =  theModtime;
		}


																
	
	public String toString(){
	    StringBuffer returnString = new StringBuffer();
	    returnString.append("com.jiuyescm.bms.pub.customer.entity.CusbilltypeRuleEntity[");
		returnString.append( "id = " + this.id +";\n" );
		returnString.append( "ordertypeid = " + this.ordertypeid +";\n" );
		returnString.append( "customerid = " + this.customerid +";\n" );
		returnString.append( "mustwholepost = " + this.mustwholepost +";\n" );
		returnString.append( "canfinish = " + this.canfinish +";\n" );
		returnString.append( "turnoverrule = " + this.turnoverrule +";\n" );
		returnString.append( "mergerule = " + this.mergerule +";\n" );
		returnString.append( "weightlimit = " + this.weightlimit +";\n" );
		returnString.append( "volumelimit = " + this.volumelimit +";\n" );
		returnString.append( "delflag = " + this.delflag +";\n" );
		returnString.append( "crepersonid = " + this.crepersonid +";\n" );
		returnString.append( "creperson = " + this.creperson +";\n" );
		returnString.append( "cretime = " + this.cretime +";\n" );
		returnString.append( "modpersonid = " + this.modpersonid +";\n" );
		returnString.append( "modperson = " + this.modperson +";\n" );
		returnString.append( "modtime = " + this.modtime +";\n" );
	    returnString.append("]\n");
	    return returnString.toString();
	}
	
	/*******************辅助方法*******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
					    	if(id==null || "".equals(id.trim())){
	    		return null;
	    	}
	    	return id;  
	    			}
}