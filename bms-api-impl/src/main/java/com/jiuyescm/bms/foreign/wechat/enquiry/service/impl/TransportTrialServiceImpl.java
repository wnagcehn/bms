package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.common.enumtype.CargoIsLightEnum;
import com.jiuyescm.bms.common.enumtype.TemplateSubjectEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityWarehouseRepository;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;
import com.jiuyescm.bms.quotation.system.repository.IBmsJiuyeQuotationSystemRepository;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineService;
import com.jiuyescm.bms.wechat.enquiry.api.ITransportTrialService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;

/**
 * 运输微信试算实现
 * @author yangss
 *
 */
@Service("transportTrialService")
public class TransportTrialServiceImpl implements ITransportTrialService{

	@Autowired
	private IPriceTransportLineService transportLineService;
	@Autowired
	private IBmsJiuyeQuotationSystemRepository bmsJiuyeQuotationSystemRepository;
	@Autowired
	private IPubOriginCityWarehouseRepository originCityWarehouseRepository;
	
	/**
	 * 试算-城际
	 */
	@Override
	public CalcuResultVo trialCJ(Map<String, String> params) {
		CalcuResultVo resultVo = new CalcuResultVo();
		//查询商家
		BmsJiuyeQuotationSystemEntity customer = queryStandardCustomer();
		if (null == customer) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_CUSTOMER_NULL_MSG);
			return resultVo;
		}

		String fromProvince = params.get("fromProvince");
		String fromCity = params.get("fromCity");
		String toProvince = params.get("toProvince");
		String toCity = params.get("toCity");
		String toDistrict = params.get("toDistrict");
		//查询标准路线报价
		List<PriceTransportLineEntity> lineList = queryStandardTemplateLine(TransportWayBillTypeEnum.CJ.getCode(), 
				fromProvince, fromCity, toProvince, toCity, toDistrict);
		if (null == lineList || lineList.size() <= 0) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_TEMPLATE_LINE_NULL_MSG);
			return resultVo;
		}
		
		//业务数据
		BizGanxianWayBillEntity bizData = new BizGanxianWayBillEntity();
		bizData.setCustomerId(customer.getVersionCode());
		bizData.setCustomerName(customer.getVersionName());
		bizData.setBizTypeCode(TransportWayBillTypeEnum.CJ.getCode());
		bizData.setProductType(params.get("productType"));
		bizData.setSendProvinceId(fromProvince);
		bizData.setSendCityId(fromCity);
		bizData.setReceiverProvinceId(toProvince);
		bizData.setReceiverCityId(toCity);
		bizData.setReceiverDistrictId(toDistrict);
		bizData.setTemperatureTypeCode(params.get("temperature"));
		String isLight = params.get("isLight");
		String quantity = params.get("quantity");
		if (CargoIsLightEnum.YES.getDesc().equals(isLight)) {
			bizData.setIsLight(CargoIsLightEnum.YES.getCode());
			bizData.setTotalVolume(Double.valueOf(quantity));
		}else if (CargoIsLightEnum.NO.getDesc().equals(isLight)) {
			bizData.setIsLight(CargoIsLightEnum.NO.getCode());
			bizData.setTotalWeight(Double.valueOf(quantity));
		}
		bizData.setPriceList(lineList);
		
		//计算
		resultVo = transportLineService.trial(bizData);
		
		return resultVo;
	}

	/**
	 * 试算-同城
	 */
	@Override
	public CalcuResultVo trialTC(Map<String, String> params) {
		CalcuResultVo resultVo = new CalcuResultVo();
		//查询商家
		BmsJiuyeQuotationSystemEntity customer = queryStandardCustomer();
		if (null == customer) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_CUSTOMER_NULL_MSG);
			return resultVo;
		}
	
		String fromProvince = params.get("fromProvince");
		String fromCity = params.get("fromCity");
		String toProvince = params.get("toProvince");
		String toCity = params.get("toCity");
		String toDistrict = params.get("toDistrict");
		//查询标准路线报价
		List<PriceTransportLineEntity> lineList = queryStandardTemplateLine(TransportWayBillTypeEnum.TC.getCode(), 
				fromProvince, fromCity, toProvince, toCity, toDistrict);
		if (null == lineList || lineList.size() <= 0) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_TEMPLATE_LINE_NULL_MSG);
			return resultVo;
		}
		
		//业务数据
		BizGanxianWayBillEntity bizData = new BizGanxianWayBillEntity();
		bizData.setCustomerId(customer.getVersionCode());
		bizData.setCustomerName(customer.getVersionName());
		bizData.setBizTypeCode(TransportWayBillTypeEnum.TC.getCode());
		bizData.setSendProvinceId(fromProvince);
		bizData.setSendCityId(fromCity);
		bizData.setReceiverProvinceId(toProvince);
		bizData.setReceiverCityId(toCity);
		bizData.setReceiverDistrictId(toDistrict);
		bizData.setCarModel(params.get("carModel"));
		bizData.setPriceList(lineList);
		
		//计算
		resultVo = transportLineService.trial(bizData);
		return resultVo;
	}

	/**
	 * 试算-电商专列
	 */
	@Override
	public CalcuResultVo trialDSZL(Map<String, String> params) {
		CalcuResultVo resultVo = new CalcuResultVo();
		//查询商家
		BmsJiuyeQuotationSystemEntity customer = queryStandardCustomer();
		if (null == customer) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_CUSTOMER_NULL_MSG);
			return resultVo;
		}
		
		//根据始发城市查询仓库
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("provinceName", params.get("fromProvince"));
		param.put("cityName", params.get("fromCity"));
		List<PubOriginCityWarehouseEntity> warehouseList = originCityWarehouseRepository.query(param);
		if (null == warehouseList || warehouseList.size() <= 0) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_WAREHOUSE_LINE_NULL_MSG);
			return resultVo;
		}
		PubOriginCityWarehouseEntity orginCityWarehouseEntity = warehouseList.get(0);

		String warehouseCode = orginCityWarehouseEntity.getWarehouseCode();
		String endStation = params.get("warehouseName");
		//路线报价
		param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", TransportWayBillTypeEnum.DSZL.getCode());
		param.put("fromWarehouseId", warehouseCode);//始发仓库ID
		param.put("endStation", endStation);//电商仓库名称
		List<PriceTransportLineEntity> lineList = transportLineService.queryStandardTemplateLine(param);
		if (null == lineList || lineList.size() <= 0) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_TEMPLATE_LINE_NULL_MSG);
			return resultVo;
		}
		
		//业务数据
		BizGanxianWayBillEntity bizData = new BizGanxianWayBillEntity();
		bizData.setCustomerId(customer.getVersionCode());
		bizData.setCustomerName(customer.getVersionName());
		bizData.setBizTypeCode(TransportWayBillTypeEnum.DSZL.getCode());
		bizData.setWarehouseCode(warehouseCode);
		bizData.setEndStation(endStation);
		bizData.setCarModel(params.get("carModel"));
		bizData.setPriceList(lineList);
		
		//计算
		resultVo = transportLineService.trial(bizData);
		
		return resultVo;
	}

	/**
	 * 试算-航鲜达
	 */
	@Override
	public CalcuResultVo trialHXD(Map<String, String> params) {
		CalcuResultVo resultVo = new CalcuResultVo();
		//查询商家
		BmsJiuyeQuotationSystemEntity customer = queryStandardCustomer();
		if (null == customer) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_CUSTOMER_NULL_MSG);
			return resultVo;
		}
		
		String startStation = params.get("startStation");
		String toProvince = params.get("toProvince");
		String toCity = params.get("toCity");
		//路线报价
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", TransportWayBillTypeEnum.HXD.getCode());
		param.put("startStation", startStation);
		param.put("toProvince", toProvince);
		param.put("toCity", toCity);
		List<PriceTransportLineEntity> lineList = transportLineService.queryStandardTemplateLine(param);
		if (null == lineList || lineList.size() <= 0) {
			resultVo.setSuccess(WechatMessageConstant.FAIL);
			resultVo.setMsg(WechatMessageConstant.ST_TEMPLATE_LINE_NULL_MSG);
			return resultVo;
		}
		
		//业务数据
		BizGanxianWayBillEntity bizData = new BizGanxianWayBillEntity();
		bizData.setCustomerId(customer.getVersionCode());
		bizData.setCustomerName(customer.getVersionName());
		bizData.setBizTypeCode(TransportWayBillTypeEnum.HXD.getCode());
		bizData.setStartStation(startStation);
		bizData.setReceiverProvinceId(toProvince);
		bizData.setReceiverCityId(toCity);
		bizData.setTotalWeight(Double.valueOf(params.get("weight")));
		bizData.setPriceList(lineList);
		
		//计算
		resultVo = transportLineService.trial(bizData);
		
		return resultVo;
	}

	/**
	 * 查询标准商家
	 * @return
	 */
	private BmsJiuyeQuotationSystemEntity queryStandardCustomer(){
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("typeName", "BZSJ");//标准商家
		List<BmsJiuyeQuotationSystemEntity> customerList = bmsJiuyeQuotationSystemRepository.queryCustomerBmsList(condition);
		if (null == customerList || customerList.size() <= 0) {
			return null;
		}
		return customerList.get(0);
	}
	
	/**
	 * 查询标准报价
	 * 始发省、市，目的省、市
	 */
	private List<PriceTransportLineEntity> queryStandardTemplateLine(String transportType, 
			String fromProvince, String fromCity,
			String toProvince, String toCity, String toDistrict){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", transportType);
		param.put("fromProvince", fromProvince);
		param.put("fromCity", fromCity);
		param.put("toProvince", toProvince);
		param.put("toCity", toCity);
		if (StringUtils.isNotEmpty(toDistrict)) {
			param.put("toDistrict", toDistrict);
		}
		return transportLineService.queryStandardTemplateLine(param);
	}
}
