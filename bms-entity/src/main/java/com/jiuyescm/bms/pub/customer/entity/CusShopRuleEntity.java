package com.jiuyescm.bms.pub.customer.entity;

import com.jiuyescm.cfm.domain.IEntity;


public class CusShopRuleEntity  implements IEntity {
	/**
	 * 商家店铺关系
	 */
		private static final long serialVersionUID = 4196553826055833193L;
		//id
		private String id;
		//商家ID
		private String customerid;
		//店铺ID
		private String shopid;
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



	    public CusShopRuleEntity(){

	    }
	    
			public String getId(){
				return this.id;
			}

			public void setId(String theId){
				this.id =  theId;
			}
			public String getCustomerid(){
				return this.customerid;
			}

			public void setCustomerid(String theCustomerid){
				this.customerid =  theCustomerid;
			}
			public String getShopid(){
				return this.shopid;
			}

			public void setShopid(String theShopid){
				this.shopid =  theShopid;
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
		    returnString.append("com.jiuyescm.bms.pub.customer.entity.CusshopRuleEntity[");
			returnString.append( "id = " + this.id +";\n" );
			returnString.append( "customerid = " + this.customerid +";\n" );
			returnString.append( "shopid = " + this.shopid +";\n" );
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
