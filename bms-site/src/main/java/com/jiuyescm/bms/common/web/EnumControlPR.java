package com.jiuyescm.bms.common.web;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;


/**
 * 
 * @author zhengyishan
 *
 */
@Component("omsEnumPR")
public class EnumControlPR {
	
	@Resource
	public ISystemCodeService systemCodeService;
	
	@Resource
    private ISystemCodeRepository systemCodeRepository;
	
	/**
	 * 产地直配发货单状态表
	 * @return
	 */
	@DataProvider
	public Map<String, String> getOperatestatus() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("ALL", "全部");
		mapValue.put(ConstantInterface.Operatestatus.WMACP, "已接单");
		mapValue.put(ConstantInterface.Operatestatus.PBCRE, "创建拣货单");
		mapValue.put("PRINT", "已打印运单");
//		mapValue.put(ConstantInterface.Operatestatus.PBCFM, "已复核");
//		mapValue.put(ConstantInterface.Operatestatus.WMWEIGHT, "已获取运单号");
		mapValue.put(ConstantInterface.Operatestatus.WMWEIGHT, "已称重");
//		mapValue.put(ConstantInterface.Operatestatus.GENCNSIGN, "已生成交接单");
//		mapValue.put(ConstantInterface.Operatestatus.CNSIGN, "已交接");
		mapValue.put("FINISH", "已完成");
		return mapValue;
	}
	/**
	 * 挂起标记
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getSuspendflag() {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		mapValue.put(999, "全部");
		mapValue.put(0, "未挂起");
		mapValue.put(1, "已挂起");
		return mapValue;
	}
	/**
	 * 撤单标记
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getExceptionflag() {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		mapValue.put(999, "全部");
		mapValue.put(0, "未撤单");
		mapValue.put(1, "已撤单");
		return mapValue;
	}
	/**
	 * 作废表示
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getInvalidflag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_0, "未作废");
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_1, "已作废");
		return mapValue;
	}
		
	/**
	 * 维护运单获得操作类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getOperatetype() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("ALL", "全部");
		mapValue.put(ConstantInterface.Operatetype.Operatetype_NORMAL, "正常维护");
		mapValue.put(ConstantInterface.Operatetype.Operatetype_MODCARRIER, "更换物流商");
		mapValue.put(ConstantInterface.Operatetype.Operatetype_MODYDNO, "更换运单号");
		return mapValue;
	}
	
	/**
	 * 配送温度类别
	 * @return
	 */
	@DataProvider
	public Map<String,String> getTemperature(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put(ConstantInterface.Temperature.CW, "常温");
		mapValue.put(ConstantInterface.Temperature.LC, "冷藏");
		mapValue.put(ConstantInterface.Temperature.LD, "冷冻");
		return mapValue;
	}
		
	/**
	 * 优先级
	 * @return
	 */
	@DataProvider
	public Map<Integer,String> getPriority(String all){
		Map<Integer,String> mapValue = new LinkedHashMap<Integer,String>();
		if(!StringUtils.isNotBlank(all)){
			mapValue.put(999, "全部");
		}
		mapValue.put(1, "1");
		mapValue.put(2, "2");
		mapValue.put(3, "3");
		mapValue.put(4, "4");
		mapValue.put(5, "5");
		mapValue.put(6, "6");
		mapValue.put(7, "7");
		mapValue.put(8, "8");
		mapValue.put(9, "9");
		mapValue.put(10, "10");
		return mapValue;
	}
	/**
	 * 称重标记
	 * @return
	 */
	@DataProvider
	public Map<String,String> getWrightFlag(){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		mapValue.put("ALL", "全部");
		mapValue.put("WMWEIGHT", "已称重");
		mapValue.put(ConstantInterface.Operatestatus.GENCNSIGN, "已生成交接单");
		mapValue.put("OTHER", "未称重");
		return mapValue;
	}
	/**
	 * 交接单状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getTranserStatus(){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		mapValue.put("ALL", "全部");
		mapValue.put(ConstantInterface.TransferStatus.Status_CREATE, "已创建交接单");
		mapValue.put(ConstantInterface.TransferStatus.Status_CONFIRM, "已确认交接");
		return mapValue;
		
	}
	/**
	 * 平台编码
	 * @return
	 */
	@DataProvider
	public Map<String,String> getPlatFormID(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.PlatFormID.TM, "天猫");
		mapValue.put(ConstantInterface.PlatFormID.TB, "淘宝");
		mapValue.put(ConstantInterface.PlatFormID.YHD, "一号店");
		mapValue.put(ConstantInterface.PlatFormID.JD, "京东");
		mapValue.put(ConstantInterface.PlatFormID.SN, "苏宁易购");
		mapValue.put(ConstantInterface.PlatFormID.AMAZON, "亚马逊");
		mapValue.put(ConstantInterface.PlatFormID.WSYZ, "微商有赞");
		mapValue.put(ConstantInterface.PlatFormID.OT, "其他");
		return mapValue;
	}
	
	/**
	 * 单据基本类型
	 * @author chenfei
	 *
	 */
	@DataProvider
	public Map<String, String> getBasetype(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.Basetype.Basetype_RK, "入库");
		mapValue.put(ConstantInterface.Basetype.Basetype_CK, "出库");
		mapValue.put(ConstantInterface.Basetype.Basetype_ZP, "产地直配");
		mapValue.put(ConstantInterface.Basetype.Basetype_KJ, "库间调拨单");
		mapValue.put(ConstantInterface.Basetype.Basetype_KN, "库内转储单");
		mapValue.put(ConstantInterface.Basetype.Basetype_YS, "运输单");
		mapValue.put(ConstantInterface.Basetype.Basetype_ZC, "转储单");
		mapValue.put(ConstantInterface.Basetype.Basetype_PD, "盘点单");
		return mapValue;
	}
	
	/**
	 * 是否
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getOmsYesNo(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.OmsYesNo.OMS_FLAG_0, "否");
		mapValue.put(ConstantInterface.OmsYesNo.OMS_FLAG_1, "是");
		return mapValue;
	}
	
	
	/**
	 * 面单打印类型
	 * @author chenfei
	 *
	 */
	@DataProvider
	public Map<String, String> getDigitalPrint(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		mapValue.put(ConstantInterface.DigitalPrint.RM, "热敏单");//热敏单
		mapValue.put(ConstantInterface.DigitalPrint.CT, "背胶面单");//背胶面单
		return mapValue;
	}
	/**
	 * 物流商分配标记
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCarrierflag(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put("0", "未分配");
		mapValue.put("1", "已分配");
		mapValue.put("-1", "分配异常");
		return mapValue;
	}
	/**
	 * 宅配商分配标记
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<String, String> getdDliverflag(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put("0", "未分配");
		mapValue.put("1", "已分配");
		mapValue.put("-1", "分配异常");
		return mapValue;
	}
	
	/**
	 * 宅配商服务配置状态表示
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getDeliverServerflag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_0, "可用");
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_1, "不可用");
		return mapValue;
	}
	
	/**
	 * 面单打印类型
	 * @author chenfei
	 *
	 */
	@DataProvider
	public Map<String, String> getPrintType(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put(ConstantInterface.PrintType.SORTING_BILL, "拣货单");//拣货单
		mapValue.put(ConstantInterface.PrintType.HEAT_BILL, "热敏");//热敏
		mapValue.put(ConstantInterface.PrintType.TRANSPORT_BILL, "背胶面单");//背胶面单
		mapValue.put(ConstantInterface.PrintType.JOIN_BILL, "交接单");//交接单
		mapValue.put("SORTING_NEW", "新拣货单");//新拣货单
		return mapValue;
	}
	
	/**
	 * 运单生成规则
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<String, String> getExpressAddRule(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put(ConstantInterface.ExpressAddRule.RULE_JY, "九曳");//九曳
		mapValue.put(ConstantInterface.ExpressAddRule.RULE_SF, "顺丰");//顺丰
		mapValue.put(ConstantInterface.ExpressAddRule.RULE_BS, "百世汇通");//百世汇通
		mapValue.put(ConstantInterface.ExpressAddRule.RULE_ZHONGTONG, "中通");//中通
		mapValue.put(ConstantInterface.ExpressAddRule.RULE_YTO, "圆通");//圆通
		mapValue.put(ConstantInterface.ExpressAddRule.RULE_UC, "优速");//优速
		return mapValue;
	}
	/**
	 *B2B标识
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getB2bflag() {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		mapValue.put(999, "全部");
		mapValue.put(ConstantInterface.B2BFlag.FLAG_0, "B2C");
		mapValue.put(ConstantInterface.B2BFlag.FLAG_1, "B2B");
		mapValue.put(ConstantInterface.B2BFlag.FLAG_2, "疑似B2B");
		
		return mapValue;
	}
	
	/**
	 *付款方式
	 * @return
	 */
	@DataProvider
	public Map<String, String> getPaymentType(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.PaymentType.PAY_CODCASH, "现金支付");
		mapValue.put(ConstantInterface.PaymentType.PAY_ONLINEPAY, "在线支付");
		mapValue.put(ConstantInterface.PaymentType.PAY_CODPOS, "POS刷卡");
//		mapValue.put(ConstantInterface.PaymentType.PAY_CODDELIVERY, "货到付款");
		return mapValue;
	}
	
	/**
	 *OMS打印方式
	 * @return
	 */
	@DataProvider
	public Map<String, String> getOmsPrintStyle() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put(ConstantInterface.omsPrintStyle.JY_HEAT_BILL_1, "久曳热敏8*12");
		mapValue.put(ConstantInterface.omsPrintStyle.JY_HEAT_BILL_2, "久曳热敏10*18");
		mapValue.put(ConstantInterface.omsPrintStyle.JY_TRANSPORT_BILL_1, "久曳背胶面单");
		mapValue.put(ConstantInterface.omsPrintStyle.SF_HEAT_BILL_1, "顺丰热敏10*15");
		mapValue.put(ConstantInterface.omsPrintStyle.ZHONGTONG_HEAT_BILL_1, "中通热敏10*18");
		mapValue.put(ConstantInterface.omsPrintStyle.YTO_HEAT_BILL_1, "圆通热敏10*18");
		mapValue.put(ConstantInterface.omsPrintStyle.QF_HEAT_BILL_10_18, "全峰热敏10*18");
		mapValue.put(ConstantInterface.omsPrintStyle.UC_HEAT_BILL_10_18, "优速热敏10*18");
		mapValue.put(ConstantInterface.omsPrintStyle.BS_HEAT_BILL_10_18, "百世汇通热敏10*18");
		return mapValue;
	}
	
	/**
	 * 单据状态
	 * 
	 * (BEGIN,AUDIT,CONFIRM,FINISH) 初始；审核；确认；完成；
	 */
	@DataProvider
	public Map<String, String> getBillStatus(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put(ConstantInterface.BillStatus.BEGIN, "初始");
		mapValue.put(ConstantInterface.BillStatus.TRANS, "转换转储单");
		mapValue.put(ConstantInterface.BillStatus.AUDIT, "审核");
		mapValue.put(ConstantInterface.BillStatus.CONFIRM, "确认");
		mapValue.put(ConstantInterface.BillStatus.PRINT, "打印");
		mapValue.put(ConstantInterface.BillStatus.FINISH, "完成");
		
		return mapValue;
	}
	
	@DataProvider
	public Map<String, String> getOMSBillStatus(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put(ConstantInterface.BillStatus.BEGIN, "初始");
		mapValue.put(ConstantInterface.BillStatus.AUDIT, "审核");
		//mapValue.put(ConstantInterface.BillStatus.CONFIRM, "确认");
		mapValue.put(ConstantInterface.BillStatus.PRINT, "打印");
		mapValue.put(ConstantInterface.BillStatus.FINISH, "完成");
		return mapValue;
	}
	/**
	 * 是否调用站点
	 * @return
	 */
	@DataProvider
	public Map<String, String> getIsCallStation(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put("N", "不调用");
		mapValue.put("Y", "调用");
		return mapValue;
	}
	
	/**
	 * 付款方式
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getPaymethod() {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		mapValue.put(ConstantInterface.Paymethod.SENDER_PAY, "寄方付");
		mapValue.put(ConstantInterface.Paymethod.RECEIVER_PAY, "收方付");
		mapValue.put(ConstantInterface.Paymethod.THIRD_PAY, "第三方付");
		return mapValue;
	}
	
	/**
	 * 计费单位枚举
	 * @param typeCode 类型名称
	 * @return Map<String, String>
	 */
	@DataProvider
	public Map<String, String> getChargeUnitEnum() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("CHARGE_UNIT");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 业务类型枚举
	 * @param typeCode 类型名称
	 * @return Map<String, String>
	 */
	@DataProvider
	public Map<String, String> getBussinessTypeEnum() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("BUSSINESS_TYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 仓配报价枚举
	 * @param typeCode 类型名称
	 * @return Map<String, String>
	 */
	@DataProvider
	public Map<String, String> getStorageSubjectEnum() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 顺丰快件产品类别
	 * @return
	 */
	@DataProvider
	public Map<String, String> getExpresstype() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SFTYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 顺丰快件产品类别
	 * @return
	 */
	@DataProvider
	public List<SystemCodeEntity> getExpresstypeList() {  	
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SFTYPE");
		return codeList;
	}
	
	/**
	 * 是否支持拆箱运单号
	 * @return
	 */
	@DataProvider
	public Map<String, String> getIssplitboxflag(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.Issplitboxflag.YES, "支持");
		mapValue.put(ConstantInterface.Issplitboxflag.NO, "不支持");
		return mapValue;
	}
	
	/**
	 * 拆箱标记
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getSplitBoxFlag() {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		mapValue.put(ConstantInterface.SplitBoxFlag.SPLIT_BOX_NO, "未拆箱");
		mapValue.put(ConstantInterface.SplitBoxFlag.SPLIT_BOX_YES, "已拆箱");
		mapValue.put(ConstantInterface.SplitBoxFlag.SPLIT_BOX_GOING, "拆箱中");
		return mapValue;
	}
	
	/**
	 * 下发标识
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getTodmspostdmsflag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.OmsFlag.OMS_FLAG_1, "未下发");
		mapValue.put(ConstantInterface.OmsFlag.OMS_FLAG_2, "已下发");
		return mapValue;
	}
	
	/**
	 * 称重状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getWrightStatus(){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		mapValue.put("ALL", "全部");
		mapValue.put("WMWEIGHT", "已称重");
		mapValue.put("OTHER", "未称重");
		return mapValue;
	}
	
	/**
	 * 仓配：单据类型
	 * 
	 */
	@DataProvider
	public Map<String, String> getDoBillType(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("999", "全部");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10005, "销售出库单");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10006, "其他出库单");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10007, "调拨出库单");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10010, "转储出库单");
		return mapValue;
	}
	/**
	 * 出库单据类型
	 */
	@DataProvider
	public Map<String, String> getInOrderType(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("999", "全部");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10001, "产地直配出库"); 
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10005, "销售出库单");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10007, "调拨出库单");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10006, "其它出库单");
		mapValue.put(ConstantInterface.BillTypeInterface.BILL_10010, "转储出库单");
		return mapValue;
	}
	
	/**
	 * 是否成功
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getSuccFlag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("All".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.OmsYesNo.OMS_FLAG_1, "成功");
		mapValue.put(ConstantInterface.OmsYesNo.OMS_FLAG_0, "失败");
		return mapValue;
	}  
	
	/**
	 * 来源ERP
	 * @return
	 */
	@DataProvider
	public Map<String,String> getSourceErp(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.FromERPCodeInterface.ERP_TABAO, "物流宝");
		mapValue.put(ConstantInterface.FromERPCodeInterface.ERP_JD, "京东");
		mapValue.put(ConstantInterface.FromERPCodeInterface.ERP_OMS, "OMS系统");
		mapValue.put(ConstantInterface.FromERPCodeInterface.ERP_GUANYI, "管易");
		mapValue.put(ConstantInterface.FromERPCodeInterface.ERP_WDT, "旺店通");
		mapValue.put(ConstantInterface.FromERPCodeInterface.ERP_OT, "其他");
		return mapValue;
	}
	
	/**
	 * 分配物流信息类型
	 * @return
	 */
	@DataProvider
	public Map<String,String> getActType(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.ActType.ACT_TYPE_FPWLS, "分配物流商");
		mapValue.put(ConstantInterface.ActType.ACT_TYPE_FPZPS, "分配宅配商");
		mapValue.put(ConstantInterface.ActType.ACT_TYPE_FPZD, "分配站点");
		return mapValue;
	}
	
	/**
	 * 运单大状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getWaybillBigStatus(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		List<SystemCodeEntity> flowTypeList = systemCodeRepository.findEnumList("FLOW_TYPE");
		for (SystemCodeEntity obj : flowTypeList) {
			mapValue.put(obj.getExtattr3(), obj.getExtattr4());
		}
		return mapValue;
	}
	
	/**
	 * 运单小状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getWaybillSmallStatus(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		List<SystemCodeEntity> flowTypeList = systemCodeRepository.findEnumList("FLOW_TYPE");
		for (SystemCodeEntity obj : flowTypeList) {
			mapValue.put(obj.getExtattr1(), obj.getExtattr2());
		}
		return mapValue;
	}
	
	/**
	 * 运单终结小状态 wuliangfeng
	 * @return
	 */
	@DataProvider
	public Map<String,String> getWaybillSmallEndStatus(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		List<SystemCodeEntity> flowTypeList = systemCodeRepository.findEnumList("FLOW_TYPE");
		for (SystemCodeEntity obj : flowTypeList) {
			if("990".equals(obj.getExtattr3()))
			{
				mapValue.put(obj.getExtattr1(), obj.getExtattr2());
			}
		}
		return mapValue;
	}
	/**
	 * 运单终结大状态 wuliangfeng
	 * @return
	 */
	@DataProvider
    public List<SystemCodeEntity> getWaybillBigStatusAll(){
		List<SystemCodeEntity> flowTypeNewList=new ArrayList<SystemCodeEntity>();
		List<SystemCodeEntity> flowTypeList = systemCodeRepository.findEnumList("FLOW_TYPE");
		SystemCodeEntity entity=new SystemCodeEntity();
		entity.setExtattr2("");
		entity.setExtattr3("");
		entity.setExtattr4("全部");
		flowTypeNewList.add(entity);
		for(int i=0;i<flowTypeList.size();i++){
			boolean f=true;
			for(int j=0;j<flowTypeNewList.size();j++){
				if(flowTypeNewList.get(j).getExtattr3().equalsIgnoreCase(flowTypeList.get(i).getExtattr3())){
					f=false;
					break;
				}
			}
			if(f){
				flowTypeNewList.add(flowTypeList.get(i));
			}
		}
		return flowTypeNewList;
	}
	/**
	 * 根据大状态获取运单终结小状态 wuliangfeng
	 * @return
	 */
	@DataProvider
	public List<SystemCodeEntity> getWaybillSmallByBigStatus(String bigStatus){
		List<SystemCodeEntity> flowTypeNewList=new ArrayList<SystemCodeEntity>();
		List<SystemCodeEntity> flowTypeList = systemCodeRepository.findEnumList("FLOW_TYPE");
		
		SystemCodeEntity allCode = new SystemCodeEntity();
		allCode.setExtattr2("全部");
		flowTypeNewList.add(allCode);
		
		if(bigStatus!=""&&bigStatus!=null){
			for(SystemCodeEntity obj : flowTypeList){
				if(obj.getExtattr3().equals(bigStatus)){
					flowTypeNewList.add(obj);
				}
			}
		}
		else{
			for(SystemCodeEntity obj : flowTypeList){
					flowTypeNewList.add(obj);
			}
		}
		return flowTypeNewList;
	}
	
	/**
	 * 运单小状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getFlowType(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		List<SystemCodeEntity> flowTypeList = systemCodeRepository.findEnumList("FLOW_TYPE");
		for (SystemCodeEntity obj : flowTypeList) {
			mapValue.put(obj.getCode(), obj.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 客诉是否赔偿
	 * @return
	 */
	@DataProvider
	public Map<String,String> getIsPay(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		mapValue.put("0", "否");
		mapValue.put("1", "是");
		return mapValue;
	}
	
	/**
	 * 客诉是否争议
	 * @return
	 */
	@DataProvider
	public Map<String,String> getIsConflict(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "");
		}
		mapValue.put("0", "否");
		mapValue.put("1", "是");
		return mapValue;
	}
	
	/**
	 * 客诉操作状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getCustOperateType(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		mapValue.put("CsrAdd", "新增客诉");
		mapValue.put("CsrEdit", "客服修改");
		mapValue.put("WmsFinish", "仓库跟进");
		mapValue.put("CsrFinish", "客服关闭");
		return mapValue;
	}
	
	/**
	 * 客诉状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getCsrState(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		mapValue.put("CsrDoing", "待客服处理");
		mapValue.put("WmsDoing", "待仓库跟进 ");
		mapValue.put("WmsFinish", "仓库已跟进 ");
		mapValue.put("CsrFinish", "已结束 ");
		return mapValue;
	}
	/**
	 * 商家中心销售区域  add by wuliangfeng
	 * @return
	 */
	@DataProvider
	public Map<String, String> getSalesRegion() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SALES_REGION");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 商家等级  add by wuliangfeng
	 * @return
	 */
	@DataProvider
	public Map<String, String> getSalesLevel() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SALES_LEVEL");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 商家类型1  add by wuliangfeng
	 * @return
	 */
	@DataProvider
	public Map<String, String> getSalesType1() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SALES_TYPE1");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 商家类型2  add by wuliangfeng
	 * @return
	 */
	@DataProvider
	public Map<String, String> getSalesType2() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SALES_TYPE2");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 商家类型3  add by wuliangfeng
	 * @return
	 */
	@DataProvider
	public Map<String, String> getSalesType3() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("SALES_TYPE3");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * Object　转Map  add by wuliangfeng
	 * @return
	 */
    public static Map<String, Object> beanToMap(Object obj){
    	if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!"class".equals(key)) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
				}

			}
		} catch (Exception e) {
			return null;
		}
		return map;
    }

	/**
	 * 漏扫指数
	 * @return
	 */
	@DataProvider
	public Map<String, String> getMissingScanIndex() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("MISSING_SCAN_INDEX");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 商家联系人类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCusinfoType() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("CUSINFO_TYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	/**
	 * 商家主营产品类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCusmainProductType() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("MAIN_PRODUCT_TYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 获取所有过账类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getbillTypeList(){
		Map<String, String> map =new LinkedHashMap<String,String>();
		map.put("全部", "全部");
		map.put("0", "未过账");
		map.put("1", "已过账");
		return map;
	}
	
	/**
	 * 差异对账状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getReconciliationStatusList(){
		Map<String, String> map =new LinkedHashMap<String,String>();
		map.put("全部", "全部");
		map.put("0", "未对账");
		map.put("1", "已对账");
		return map;
	}
	
	/**
	 * 获取计算类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCalculatedList(){
		Map<String, String> map =new LinkedHashMap<String,String>();
		map.put("ALL", "全部");
		map.put("0", "未计算");
		map.put("1", "已计算");
		return map;
	}
	
	/**
	 * 获取月报表的年份
	 * @return
	 */
	@DataProvider
	public Map<String, String> getReportMonthForYear(){		
		Map<String, String> map =new LinkedHashMap<String,String>();
		int endYear = DateUtil.getCurrentYYYY();
		for(int i=5;i>=0;i--){
			map.put(String.valueOf(endYear-i), String.valueOf(endYear-i));
		}
		return map;
	}
	
	@DataProvider
	public Map<String,String> getAllMonth(){
		Map<String,String> mapMonth=Maps.newLinkedHashMap();
		for(int i=1;i<=12;i++){
			mapMonth.put(String.valueOf(i), String.valueOf(i));
		}
		return mapMonth;
	}
	
	@DataProvider
	public Map<String,String> getYear(){
		Calendar date = Calendar.getInstance();
		int year=date.get(Calendar.YEAR);
		Map<String,String> mapYear=Maps.newLinkedHashMap();
		for(int i=year-3;i<=year;i++){
			mapYear.put(String.valueOf(i), String.valueOf(i));
		}
		return mapYear;
	}
	
	@DataProvider
	public Map<String,String> getIsState(String all){
		Map<String,String> mapValue = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("", "全部");
		}
		mapValue.put("0", "否");
		mapValue.put("1", "是");
		return mapValue;
	}
	
	
	/**
	 * 获取所有单据类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getOrderStatusList(){
		Map<String, String> map =new LinkedHashMap<String,String>();
		map.put("1", "待确认");
		map.put("2", "已确认");
		map.put("3", "已关账");
		return map;
	}
}
