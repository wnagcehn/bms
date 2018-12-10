package com.jiuyescm.bms.base.servicetype.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class CarrierProductVo implements IEntity {

	private static final long serialVersionUID = -215630305505455789L;

	// 主键ID
		private Integer id;
		// 物流商ID
		private String carrierid;
		private String carriername;
		// 物流商编码
		private String carriercode;
		// 服务类型编码
		private String servicecode;
		// 服务类型名称
		private String servicename;
		// 备注
		private String remark;
		// 作废标记
		private String delflag;
		// 创建人
		private String creperson;
		// 创建时间
		private Timestamp cretime;
		// 修改人
		private String modperson;
		// 修改时间
		private Timestamp modtime;
		// 创建人ID
		private String crepersonid;
		// 修改人ID
		private String modpersonid;
		// WriteTime
		private Timestamp writeTime;

		public CarrierProductVo() {
			super();
		}
		
		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
		
		public String getCarrierid() {
			return this.carrierid;
		}

		public void setCarrierid(String carrierid) {
			this.carrierid = carrierid;
		}
		
		public String getCarriercode() {
			return this.carriercode;
		}

		public void setCarriercode(String carriercode) {
			this.carriercode = carriercode;
		}
		
		public String getServicecode() {
			return this.servicecode;
		}

		public void setServicecode(String servicecode) {
			this.servicecode = servicecode;
		}
		
		public String getServicename() {
			return this.servicename;
		}

		public void setServicename(String servicename) {
			this.servicename = servicename;
		}
		
		public String getRemark() {
			return this.remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		public String getDelflag() {
			return this.delflag;
		}

		public void setDelflag(String delflag) {
			this.delflag = delflag;
		}
		
		public String getCreperson() {
			return this.creperson;
		}

		public void setCreperson(String creperson) {
			this.creperson = creperson;
		}
		
		public Timestamp getCretime() {
			return this.cretime;
		}

		public void setCretime(Timestamp cretime) {
			this.cretime = cretime;
		}
		
		public String getModperson() {
			return this.modperson;
		}

		public void setModperson(String modperson) {
			this.modperson = modperson;
		}
		
		public Timestamp getModtime() {
			return this.modtime;
		}

		public void setModtime(Timestamp modtime) {
			this.modtime = modtime;
		}
		
		public String getCrepersonid() {
			return this.crepersonid;
		}

		public void setCrepersonid(String crepersonid) {
			this.crepersonid = crepersonid;
		}
		
		public String getModpersonid() {
			return this.modpersonid;
		}

		public void setModpersonid(String modpersonid) {
			this.modpersonid = modpersonid;
		}
		
		public Timestamp getWriteTime() {
			return this.writeTime;
		}

		public void setWriteTime(Timestamp writeTime) {
			this.writeTime = writeTime;
		}

		public String getCarriername() {
			return carriername;
		}

		public void setCarriername(String carriername) {
			this.carriername = carriername;
		}
	
}
