package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.common.enumtype.TemplateSubjectEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityElecWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityElecWarehouseRepository;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityWarehouseRepository;
import com.jiuyescm.bms.pub.transport.entity.PubTransportRegionEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRangeRepository;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRepository;
import com.jiuyescm.bms.wechat.enquiry.api.IQuotationEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.utils.FormatUtil;
import com.jiuyescm.bms.wechat.enquiry.utils.TransportEnquiryUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.CarModelPriceVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationCJVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationDSZLVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationHXDVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationTCVo;
import com.jiuyescm.bms.wechat.enquiry.vo.RegionPriceLimitVo;
import com.jiuyescm.bms.wechat.enquiry.vo.TemperatureCarModelPirceVo;
import com.jiuyescm.bms.wechat.enquiry.vo.ToDistrictPriceVo;
import com.jiuyescm.bms.wechat.enquiry.vo.WeightLimitVo;

/**
 * 报价查询实现类
 * @author yangss
 *
 */
@Service("quotationEnquiryService")
public class QuotationEnquiryServiceImpl implements IQuotationEnquiryService{

	@Autowired
	private IPriceTransportLineRepository transportLineRepository;
	@Autowired
	private IPriceTransportLineRangeRepository transportLineRangeRepository;
	@Autowired
	private IPubOriginCityWarehouseRepository originCityWarehouseRepository;
	@Autowired
	private IPubOriginCityElecWarehouseRepository elecWarehouseRepository;
	@Autowired
	private ISystemCodeRepository systemCodeRepository;

	/**
	 * 城际报价查询
	 */
	@Override
	public QuotationCJVo queryCjQuotation(Map<String, String> reqParam) {
		QuotationCJVo quotationCJVo = null;
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		String toProvince = reqParam.get("toProvince");
		String toCity = reqParam.get("toCity");
		String toDistrict = reqParam.get("toDistrict");
		String productType = reqParam.get("productType");
		//查询标准路线报价
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", TransportWayBillTypeEnum.CJ.getCode());
		param.put("fromProvince", fromProvince);
		param.put("fromCity", fromCity);
		param.put("toProvince", toProvince);
		param.put("toCity", toCity);
		if (StringUtils.isNotEmpty(toDistrict)) {
			param.put("toDistrict", toDistrict);
		}
		List<PriceTransportLineEntity> lineList = transportLineRepository.queryStandardTemplateLine(param);
		if (null != lineList && lineList.size() > 0) {
			PriceTransportLineEntity entity = lineList.get(0);
			param = new HashMap<String, Object>();
			param.put("lineId", entity.getId());
			param.put("productTypeCode", productType);
			List<PriceTransportLineRangeEntity> lineRangeList = transportLineRangeRepository.query(param);
			//处理路线阶梯报价处理，重泡货，返回阶梯
			quotationCJVo = TransportEnquiryUtil.handleCjQuationVO(entity.getTimeliness(),lineRangeList);
		}
		return quotationCJVo;
	}

	/**
	 * 同城-外埠报价查询
	 */
	@Override
	public List<QuotationTCVo<List<RegionPriceLimitVo>>> queryTCRegionQuotation(Map<String, String> reqParam) {
		List<QuotationTCVo<List<RegionPriceLimitVo>>> list = new ArrayList<QuotationTCVo<List<RegionPriceLimitVo>>>();
		//查询标准报价
		List<PriceTransportLineEntity> lineList = queryStandardTemplateLine(TransportWayBillTypeEnum.TC.getCode(),
				reqParam);
		if (null == lineList || lineList.size() <= 0) {
			return null;
		}
		
		//1)、先区分是否外埠。市不一样就为外埠
		List<String> insideIdList = new ArrayList<String>();
		List<String> outsideIdList = new ArrayList<String>();
		for (PriceTransportLineEntity lineEntity : lineList) {
			if (lineEntity.getFromCityId().equals(lineEntity.getToCityId())) {
				insideIdList.add(String.valueOf(lineEntity.getId()));
			}else {
				outsideIdList.add(String.valueOf(lineEntity.getId()));
			}
		}
		//2)、查询阶梯报价，按照车型分组取出最大单价、最小单价
		List<PubTransportRegionEntity> insideList = null;
		List<PubTransportRegionEntity> outsideList = null;
		if (null != insideIdList && insideIdList.size() > 0) {
			insideList = transportLineRangeRepository.queryQuotationRegion(insideIdList);
		}
		if (null != outsideIdList && outsideIdList.size() > 0) {
			outsideList = transportLineRangeRepository.queryQuotationRegion(outsideIdList);
		}
		//3)、根据车型组合返回对象
		list = TransportEnquiryUtil.handleTCQuotationRegion(insideList, outsideList);
		return list;
	}
	
	/**
	 * 同城-车型报价查询
	 */
	@Override
	public QuotationTCVo<List<ToDistrictPriceVo>> queryTCCarModelQuotation(Map<String, String> reqParam) {
		QuotationTCVo<List<ToDistrictPriceVo>> quotationTCVo = null;
		//查询标准报价
		List<PriceTransportLineEntity> lineList = queryStandardTemplateLine(TransportWayBillTypeEnum.TC.getCode(),
				reqParam);
		if (null == lineList || lineList.size() <= 0) {
			return quotationTCVo;
		}
		
		quotationTCVo = new QuotationTCVo<List<ToDistrictPriceVo>>();
		String carModel = reqParam.get("carModel");
		List<ToDistrictPriceVo> quationList = new ArrayList<ToDistrictPriceVo>();
		ToDistrictPriceVo toDistrictPriceVo = null;
		HashMap<String, Object> param = null;
		for (PriceTransportLineEntity lineEntity : lineList) {
			//目的地，有区显示区、没区显示市
			String city = "";
			if (StringUtils.isNotEmpty(lineEntity.getToDistrictId())) {
				city = lineEntity.getToDistrictId();
			}else {
				city = lineEntity.getToCityId();
			}
			
			param = new HashMap<String, Object>();
			param.put("lineId", lineEntity.getId());
			param.put("carModelCode", carModel);
			List<PriceTransportLineRangeEntity> lineRangeList = transportLineRangeRepository.query(param);
			if (null != lineRangeList && lineRangeList.size() > 0) {
				for (PriceTransportLineRangeEntity lineRangeEntity : lineRangeList) {
					toDistrictPriceVo = new ToDistrictPriceVo(city, lineRangeEntity.getUnitPrice());
					quationList.add(toDistrictPriceVo);
				}
			}
		}
		quotationTCVo.setCarModel(carModel);
		quotationTCVo.setQuote(quationList);
		return quotationTCVo;
	}
	
	/**
	 * 同城-全部报价查询
	 */
	@Override
	public List<QuotationTCVo<List<CarModelPriceVo>>> queryTCAllQuotation(Map<String, String> reqParam) {
		List<QuotationTCVo<List<CarModelPriceVo>>> quotationTCVoList = null;
		//查询标准报价
		List<PriceTransportLineEntity> lineList = queryStandardTemplateLine(TransportWayBillTypeEnum.TC.getCode(),
				reqParam);
		if (null == lineList || lineList.size() <= 0) {
			return quotationTCVoList;
		}
		
		quotationTCVoList = new ArrayList<QuotationTCVo<List<CarModelPriceVo>>>();
		QuotationTCVo<List<CarModelPriceVo>> quotationTCVo = null;
		List<CarModelPriceVo> quationList = null;
		CarModelPriceVo quationMap = null;
		
		HashMap<String, Object> param = null;
		for (PriceTransportLineEntity lineEntity : lineList) {
			quotationTCVo = new QuotationTCVo<List<CarModelPriceVo>>();
			quationList = new ArrayList<CarModelPriceVo>();
			
			//目的地，有区显示区、没区显示市
			String city = "";
			if (StringUtils.isNotEmpty(lineEntity.getToDistrictId())) {
				city = lineEntity.getToDistrictId();
			}else {
				city = lineEntity.getToCityId();
			}
			
			param = new HashMap<String, Object>();
			param.put("lineId", lineEntity.getId());
			List<PriceTransportLineRangeEntity> lineRangeList = transportLineRangeRepository.query(param);
			if (null != lineRangeList && lineRangeList.size() > 0) {
				for (PriceTransportLineRangeEntity lineRangeEntity : lineRangeList) {
					quationMap = new CarModelPriceVo(lineRangeEntity.getCarModelCode(), 
							lineRangeEntity.getUnitPrice());
					quationList.add(quationMap);
				}
				quotationTCVo.setToDistrict(city);
				quotationTCVo.setQuote(quationList);
				quotationTCVoList.add(quotationTCVo);
			}
		}
		return quotationTCVoList;
	}
	
	/**
	 * 电商专列报价查询
	 */
	@Override
	public List<QuotationDSZLVo> queryDSZLQuotation(Map<String, String> params) {
		List<QuotationDSZLVo> resultList = null;
		//根据始发城市查询仓库
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("provinceName", params.get("fromProvince"));
		param.put("cityName", params.get("fromCity"));
		List<PubOriginCityWarehouseEntity> warehouseList = originCityWarehouseRepository.query(param);
		if (null == warehouseList || warehouseList.size() <= 0) {
			return null; 
		}
		//根据始发城市、电商名称查询电商仓库名称
		param.put("elecBizName", params.get("eleBizName"));
		List<PubOriginCityElecWarehouseEntity> elecWarehouseList = elecWarehouseRepository.query(param);
		if (null == elecWarehouseList || elecWarehouseList.size() <= 0) {
			return null; 
		}
		
		//查询报价
		QuotationDSZLVo quotationDSZLVo = null;
		List<TemperatureCarModelPirceVo> quationList = null;
		TemperatureCarModelPirceVo quationMap = null;
		resultList = new ArrayList<QuotationDSZLVo>();
		
		Map<String, String> temperatureMap = getTemperatureTypeMap();
		PubOriginCityWarehouseEntity warehouse = warehouseList.get(0);
		for (PubOriginCityElecWarehouseEntity elecWarehouse : elecWarehouseList) {
			param = new HashMap<String, Object>();
			param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
			param.put("transportType", TransportWayBillTypeEnum.DSZL.getCode());
			param.put("fromWarehouseId", warehouse.getWarehouseCode());//始发仓库ID
			param.put("endStation", elecWarehouse.getWarehouseName());//电商仓库名称
			List<PriceTransportLineEntity> lineList = transportLineRepository.queryStandardTemplateLine(param);
			if (null != lineList && lineList.size() > 0) {
				quotationDSZLVo = new QuotationDSZLVo();
				PriceTransportLineEntity lineEntity = lineList.get(0);
				
				param = new HashMap<String, Object>();
				param.put("lineId", lineEntity.getId());
				List<PriceTransportLineRangeEntity> lineRangeList = transportLineRangeRepository.query(param);
				if (null != lineRangeList && lineRangeList.size() > 0) {
					quationList = new ArrayList<TemperatureCarModelPirceVo>();
					for (PriceTransportLineRangeEntity lineRange : lineRangeList) {
						quationMap = new TemperatureCarModelPirceVo(temperatureMap.get(lineRange.getTemperatureTypeCode()),
								lineRange.getCarModelCode(), 
								FormatUtil.formatPrice(lineRange.getUnitPrice()));
						quationList.add(quationMap);
					}
				}
				quotationDSZLVo.setTimeLiness(lineEntity.getTimeliness());
				quotationDSZLVo.setList(quationList);
				quotationDSZLVo.setWarehouseName(lineEntity.getEndStation());
				resultList.add(quotationDSZLVo);
			}
		}
		return resultList;
	}
	
	/**
	 * 航鲜达-机场报价查询
	 */
	@Override
	public List<QuotationHXDVo> queryHXDAirportQuotation(Map<String, String> params) {
		String toProvince = params.get("fromProvince");
		String toCity = params.get("fromCity");
		
		return queryHxdQuotation(toProvince, toCity);
	}

	/**
	 * 航鲜达-全部报价查询
	 */
	@Override
	public List<QuotationHXDVo> queryHXDAllQuotation() {
		return queryHxdQuotation(null, null);
	}
	
	/**
	 * 查询标准报价路线
	 * @param requestVo
	 * @return
	 */
	private List<PriceTransportLineEntity> queryStandardTemplateLine(String transportType, 
			Map<String, String> reqParam){
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", transportType);
		param.put("fromProvince", fromProvince);
		param.put("fromCity", fromCity);
		return transportLineRepository.queryStandardTemplateLine(param);
	}
	
	/**
	 * 查询温度类型
	 * @return
	 */
	private Map<String, String> getTemperatureTypeMap(){
		List<SystemCodeEntity> systemCodeList = systemCodeRepository.findEnumList("TEMPERATURE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}

	/**
	 * 查询报价-航鲜达
	 */
	private List<QuotationHXDVo> queryHxdQuotation(String toProvince, String toCity){
		List<QuotationHXDVo> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", TransportWayBillTypeEnum.HXD.getCode());
		if (StringUtils.isNotEmpty(toProvince) && StringUtils.isNotEmpty(toCity)) {
			param.put("toProvince", toProvince);
			param.put("toCity", toCity);
		}
		List<PriceTransportLineEntity> lineList = transportLineRepository.queryStandardTemplateLine(param);
		if (null == lineList || lineList.size() <= 0) {
			return list;
		}
		
		list = new ArrayList<QuotationHXDVo>();
		List<QuotationHXDVo> result = new ArrayList<QuotationHXDVo>();
		QuotationHXDVo map = null;
		List<WeightLimitVo> quationList = null;
		WeightLimitVo quationMap = null;
		for (PriceTransportLineEntity lineEntity : lineList) {
			map = new QuotationHXDVo();
			quationList = new ArrayList<WeightLimitVo>();
			
			param = new HashMap<String, Object>();
			param.put("lineId", lineEntity.getId());
			List<PriceTransportLineRangeEntity> lineRangeList = transportLineRangeRepository.query(param);
			if (null == lineRangeList || lineRangeList.size() <= 0) {
				return list;
			}
			double priceShipment = 0.0;
			for (PriceTransportLineRangeEntity lineRangeEntity : lineRangeList) {
				if (null != lineRangeEntity.getMinWeightShipment()) {
					priceShipment = lineRangeEntity.getMinPriceShipment();
				}
				quationMap = new WeightLimitVo(lineRangeEntity.getWeightLowerLimit(), 
						lineRangeEntity.getWeightUpperLimit(), 
						FormatUtil.formatPrice(lineRangeEntity.getUnitPrice()));
				quationList.add(quationMap);
			}
			map.setAirPort(lineEntity.getStartStation());
			map.setToProvince(lineEntity.getToProvinceId());
			map.setToCity(lineEntity.getToCityId());
			map.setMinPriceShipment(priceShipment);
			map.setWeightLimit(quationList);
			result.add(map);
		}
		
		return result;
	}
}
