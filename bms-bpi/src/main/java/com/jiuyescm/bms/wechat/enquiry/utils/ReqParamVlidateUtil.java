package com.jiuyescm.bms.wechat.enquiry.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jiuyescm.bms.common.enumtype.CargoIsLightEnum;
import com.jiuyescm.bms.rest.RequestVo;

/**
 * 请求参数校验类
 * @author yangss
 */
public class ReqParamVlidateUtil {

	/**
	 * 校验始发省、市
	 */
	public static boolean checkFromProvinceCity(Map<String, String> reqParam){
		boolean checkFlag = true;
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		if (StringUtils.isEmpty(fromProvince) || StringUtils.isEmpty(fromCity)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验目的省、市
	 */
	public static boolean checkToProvinceCity(Map<String, String> reqParam){
		boolean checkFlag = true;
		String toProvince = reqParam.get("toProvince");
		String toCity = reqParam.get("toCity");
		if (StringUtils.isEmpty(toProvince) || StringUtils.isEmpty(toCity)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验车型
	 */
	public static boolean checkCarModel(Map<String, String> reqParam){
		boolean checkFlag = true;
		String carModel = reqParam.get("carModel");
		if (StringUtils.isEmpty(carModel)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验电商仓
	 */
	public static boolean checkEleBizName(Map<String, String> reqParam){
		boolean checkFlag = true;
		String elecBizName = reqParam.get("eleBizName");
		if (StringUtils.isEmpty(elecBizName)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验仓库名称
	 */
	public static boolean checkWarehouseName(Map<String, String> reqParam){
		boolean checkFlag = true;
		String warehouseName = reqParam.get("warehouseName");
		if (StringUtils.isEmpty(warehouseName)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验产品类型
	 */
	public static boolean checkPorductType(Map<String, String> reqParam){
		boolean checkFlag = true;
		String productType = reqParam.get("productType");
		if (StringUtils.isEmpty(productType)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验是否轻货
	 */
	public static boolean checkIsLight(Map<String, String> reqParam){
		boolean checkFlag = true;
		String isLight = reqParam.get("isLight");
		if (StringUtils.isEmpty(isLight)) {
			checkFlag = false;
		}else if (!CargoIsLightEnum.getMap().containsValue(isLight)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验货量
	 */
	public static boolean checkQuantity(Map<String, String> reqParam){
		boolean checkFlag = true;
		String quantity = reqParam.get("quantity");
		if (StringUtils.isEmpty(quantity)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验重量
	 */
	public static boolean checkWeight(Map<String, String> reqParam){
		boolean checkFlag = true;
		String quantity = reqParam.get("weight");
		if (StringUtils.isEmpty(quantity)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验温度
	 */
	public static boolean checkTemperature(Map<String, String> reqParam){
		boolean checkFlag = true;
		String temperature = reqParam.get("temperature");
		if (StringUtils.isEmpty(temperature)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验始发站点
	 */
	public static boolean checkStartStation(Map<String, String> reqParam){
		boolean checkFlag = true;
		String startStation = reqParam.get("startStation");
		if (StringUtils.isEmpty(startStation)) {
			checkFlag = false;
		}
		return checkFlag;
	}
	
	/**
	 * 校验城际报价查询参数
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkCjQuotationParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//目的省、市
		checkFlag = checkToProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//产品类型
		checkFlag = checkPorductType(reqParam);
		
		return checkFlag;
	}
	
	/**
	 * 校验目的城市参数
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkToCitysParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//产品类型
		checkFlag = checkPorductType(reqParam);
		
		return checkFlag;
	}
	
	/**
	 * 校验省市查询参数
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkProvinceCityParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			checkFlag = false;
		}else {
			//始发省、市
			checkFlag = checkFromProvinceCity(reqParam);
		}
		return checkFlag;
	}
	
	/**
	 * 校验同城-车型报价查询参数
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkTcCarModelQuotationParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//车型
		checkFlag = checkCarModel(reqParam);
		
		return checkFlag;
	}
	
	/**
	 * 校验电商专列报价查询参数
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkDszlQuotationParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//电商仓
		checkFlag = checkEleBizName(reqParam);
		
		return checkFlag;
	}
	
	/**
	 * 城际-试算参数校验
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkTrialCjReqParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		
		//产品类型
		checkFlag = checkPorductType(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//目的省、市
		checkFlag = checkToProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//温度
		checkFlag = checkTemperature(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//是否轻货
		checkFlag = checkIsLight(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//货量
		checkFlag = checkQuantity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		return checkFlag;
	}
	
	/**
	 * 同城-试算参数校验
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkTrialTcReqParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//目的省、市
		checkFlag = checkToProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//车型
		checkFlag = checkCarModel(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		return checkFlag;
	}
	
	/**
	 * 电商专列-试算参数校验
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkTrialDszlReqParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		
		//始发省、市
		checkFlag = checkFromProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//电商仓
		checkFlag = checkEleBizName(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//仓库名称
		checkFlag = checkWarehouseName(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//车型
		checkFlag = checkCarModel(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		return checkFlag;
	}
	
	/**
	 * 航鲜达-试算参数校验
	 * @param requestVo
	 * @return 验证通过返回true,参数错误返回false
	 */
	public static boolean checkTrialHxdReqParam(RequestVo requestVo){
		boolean checkFlag = true;
		Map<String, String> reqParam = requestVo.getParams();
		if (null == reqParam) {
			return false;
		}
		
		//始发站点
		checkFlag = checkStartStation(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//目的省、市
		checkFlag = checkToProvinceCity(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		//重量
		checkFlag = checkWeight(reqParam);
		if (!checkFlag) {
			return checkFlag;
		}
		return checkFlag;
	}
}
