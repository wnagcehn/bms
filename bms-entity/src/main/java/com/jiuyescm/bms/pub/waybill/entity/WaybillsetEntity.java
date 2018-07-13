package com.jiuyescm.bms.pub.waybill.entity;

import java.util.Date;
/**
 * 运单池 表
 * @author zhengyishan
 *
 */
public class WaybillsetEntity {

    private String waybillno; 

    private Short isuse;

    private Long serialnumber;

    private Short delflag;

    private String billtype;

    private String dono;

    private String creperson;

    private Date cretime;

    private String modperson;

    private Date modtime;

    public String getWaybillno() {
        return waybillno;
    }

    public void setWaybillno(String waybillno) {
        this.waybillno = waybillno;
    }

    public Short getIsuse() {
        return isuse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.isuse
     *
     * @param isuse the value for pub_waybillset.isuse
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setIsuse(Short isuse) {
        this.isuse = isuse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.serialnumber
     *
     * @return the value of pub_waybillset.serialnumber
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public Long getSerialnumber() {
        return serialnumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.serialnumber
     *
     * @param serialnumber the value for pub_waybillset.serialnumber
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setSerialnumber(Long serialnumber) {
        this.serialnumber = serialnumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.delflag
     *
     * @return the value of pub_waybillset.delflag
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public Short getDelflag() {
        return delflag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.delflag
     *
     * @param delflag the value for pub_waybillset.delflag
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setDelflag(Short delflag) {
        this.delflag = delflag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.billtype
     *
     * @return the value of pub_waybillset.billtype
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public String getBilltype() {
        return billtype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.billtype
     *
     * @param billtype the value for pub_waybillset.billtype
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.dono
     *
     * @return the value of pub_waybillset.dono
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public String getDono() {
        return dono;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.dono
     *
     * @param dono the value for pub_waybillset.dono
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setDono(String dono) {
        this.dono = dono;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.creperson
     *
     * @return the value of pub_waybillset.creperson
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public String getCreperson() {
        return creperson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.creperson
     *
     * @param creperson the value for pub_waybillset.creperson
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setCreperson(String creperson) {
        this.creperson = creperson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.cretime
     *
     * @return the value of pub_waybillset.cretime
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public Date getCretime() {
        return cretime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.cretime
     *
     * @param cretime the value for pub_waybillset.cretime
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setCretime(Date cretime) {
        this.cretime = cretime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.modperson
     *
     * @return the value of pub_waybillset.modperson
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public String getModperson() {
        return modperson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.modperson
     *
     * @param modperson the value for pub_waybillset.modperson
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setModperson(String modperson) {
        this.modperson = modperson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pub_waybillset.modtime
     *
     * @return the value of pub_waybillset.modtime
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public Date getModtime() {
        return modtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pub_waybillset.modtime
     *
     * @param modtime the value for pub_waybillset.modtime
     *
     * @mbggenerated Mon Sep 28 16:54:50 CST 2015
     */
    public void setModtime(Date modtime) {
        this.modtime = modtime;
    }
}