package com.jiuyescm.bms.wechat.enquiry.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;
import com.jiuyescm.bms.pub.transport.entity.PubTransportRegionEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.wechat.enquiry.vo.ProductTypeVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationCJVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationTCVo;
import com.jiuyescm.bms.wechat.enquiry.vo.RegionPriceLimitVo;
import com.jiuyescm.bms.wechat.enquiry.vo.VolumeLimitVo;
import com.jiuyescm.bms.wechat.enquiry.vo.WeightLimitCJVo;

/**
 * 运输微信接口公用方法类
 * @author yangss
 */
public class TransportEnquiryUtil {
	
	/**
	 * 处理产品类型
	 * @return
	 */
	public static List<ProductTypeVo> handleProductType(
			List<PubTransportProductTypeEntity> productTypeList){
		List<ProductTypeVo> list = new ArrayList<ProductTypeVo>();
		if (null == productTypeList || productTypeList.size() <= 0) {
			return list;
		}
		
		ProductTypeVo productVo = null;
		for (PubTransportProductTypeEntity productType : productTypeList) {
			productVo = new ProductTypeVo(productType.getProductTypeCode(), 
					productType.getProductTypeName(),
					productType.getShortName());
			list.add(productVo);
		}
		return list;
	}
	
//	/**
//	 * 处理省、市（逗号分隔的）
//	 * @param toCityList
//	 * @return
//	 */
//	public static List<ToProvinceCityVo> handleProvinceCity(
//			List<PriceTransportLineEntity> lineList){
//		List<ToProvinceCityVo> list = new ArrayList<ToProvinceCityVo>();
//		if (null == lineList || lineList.size() <= 0) {
//			return list;
//		}
//		
//		ToProvinceCityVo cityVo = null;
//		List<String> districtList = null;
//		for (PriceTransportLineEntity lineEntity : lineList) {
//			districtList = new ArrayList<String>();
//			String toProvince = lineEntity.getToProvinceId();
//			//银川市,石嘴山市,吴忠市,固原市,中卫市
//			String toCity = lineEntity.getToCityId();
//			String toDistricts = lineEntity.getToDistrictId();
//			if (StringUtils.isNotEmpty(toDistricts)) {
//				String[] toDistrictArray = toDistricts.split(",");
//				for (String district : toDistrictArray) {
//					if (StringUtils.isNotEmpty(district)) {
//						districtList.add(district);
//					}
//				}
//			}
//			cityVo = new ToProvinceCityVo(toProvince, toCity, districtList);
//			list.add(cityVo);
//		}
//		return list;
//	}
	
	/**
	 * 处理省、市（逗号分隔的）
	 * @param toCityList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> handleProvinceCity(
			List<PriceTransportLineEntity> lineList){
		List<Map<String, Object>> provinceList = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapProvices= new HashMap<String, Object>();
		List<Map<String, Object>> cityList = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapCitys = new HashMap<String, Object>();
		List<String> districtList = new ArrayList<String>();
		if (null == lineList || lineList.size() <= 0) {
			return provinceList;
		}
		
		//处理省重复
		Map<String, Object> distinctProvince = new HashMap<String, Object>();
		List<PriceTransportLineEntity> lines = null;
		for (PriceTransportLineEntity lineEntity : lineList) {
			if (distinctProvince.containsKey(lineEntity.getToProvinceId())) {
				List<PriceTransportLineEntity> cityLine = 
						(List<PriceTransportLineEntity>)distinctProvince.get(lineEntity.getToProvinceId());
				cityLine.add(lineEntity);
				distinctProvince.put(lineEntity.getToProvinceId(), cityLine);
			}else{
				lines = new ArrayList<PriceTransportLineEntity>();
				lines.add(lineEntity);
				distinctProvince.put(lineEntity.getToProvinceId(), lines);
			}
		}
		
		
		for (String key : distinctProvince.keySet()) {
			mapProvices= new HashMap<String, Object>();
			cityList = new ArrayList<Map<String,Object>>();
			List<PriceTransportLineEntity> cityLine = 
						(List<PriceTransportLineEntity>)distinctProvince.get(key);
			for (PriceTransportLineEntity entity : cityLine) {
				districtList = new ArrayList<String>();
				mapCitys = new HashMap<String, Object>();
				//吴中区,昆山市
				String toDistrict = entity.getToDistrictId();
				if (StringUtils.isNotEmpty(toDistrict)) {
					String[] toDistrictArray = toDistrict.split(",");
					for (String district : toDistrictArray) {
						if (StringUtils.isNotEmpty(district)) {
							districtList.add(district);
						}
					}
				}
				mapCitys.put("name", entity.getToCityId());
				mapCitys.put("district", districtList);
				cityList.add(mapCitys);
			}
			mapProvices.put("city", cityList);//市
			mapProvices.put("province", key);//省
			provinceList.add(mapProvices);
		}
		return provinceList;
	}
	
	/**
	 * 封装城际报价返回VO
	 * 重泡货，返回阶梯报价
	 */
	public static QuotationCJVo handleCjQuationVO(String timeLiness, 
			List<PriceTransportLineRangeEntity> lineRangeList){
		QuotationCJVo quotationCJVo = new QuotationCJVo();
		List<WeightLimitCJVo> weightList = new ArrayList<WeightLimitCJVo>();
		List<VolumeLimitVo> volumeList = new ArrayList<VolumeLimitVo>();
		WeightLimitCJVo weightLimitVo = null;
		VolumeLimitVo volumeLimitVo = null;
		
		if (null == lineRangeList || lineRangeList.size() <= 0) {
			return null; 
		}
		
		for (PriceTransportLineRangeEntity lineRange : lineRangeList) {
			if (null != lineRange.getWeightLowerLimit() && null != lineRange.getWeightUpperLimit()) {
				weightLimitVo = new WeightLimitCJVo(lineRange.getWeightLowerLimit(), 
						lineRange.getWeightUpperLimit(), 
						FormatUtil.formatPrice(lineRange.getUnitPrice()));
				weightList.add(weightLimitVo);
			}else if (null != lineRange.getVolumeLowerLimit() && null != lineRange.getVolumeUpperLimit()) {
				volumeLimitVo = new VolumeLimitVo(lineRange.getVolumeLowerLimit(), 
						lineRange.getVolumeUpperLimit(), 
						FormatUtil.formatPrice(lineRange.getUnitPrice()));
				volumeList.add(volumeLimitVo);
			}
		}
		
		quotationCJVo.setTimeLiness(timeLiness);
		quotationCJVo.setWeight(weightList);
		quotationCJVo.setLight(volumeList);
		return quotationCJVo; 
	}
	
	/**
	 * 封装报价-外埠返回对象
	 * @return
	 */
	public static List<QuotationTCVo<List<RegionPriceLimitVo>>> handleTCQuotationRegion(
			List<PubTransportRegionEntity> insideList,
			List<PubTransportRegionEntity> outsideList){
		List<QuotationTCVo<List<RegionPriceLimitVo>>> list = null;
		if ((null == insideList || insideList.size() <= 0) && 
				(null == outsideList || outsideList.size() <= 0)) {
			return null;
		}
		
		list = new ArrayList<QuotationTCVo<List<RegionPriceLimitVo>>>();
		QuotationTCVo<List<RegionPriceLimitVo>> map = null;
		List<RegionPriceLimitVo> regionPriceLimitList = null;
		RegionPriceLimitVo regionPriceLimitVo = null;
		//市配
		if (null != insideList && insideList.size() > 0) {
			for (PubTransportRegionEntity insideRegion : insideList) {
				map = new QuotationTCVo<List<RegionPriceLimitVo>>();
				regionPriceLimitList = new ArrayList<RegionPriceLimitVo>();
				
				regionPriceLimitVo = new RegionPriceLimitVo("市配", 
						FormatUtil.formatPrice(insideRegion.getLowerPrice()), 
						FormatUtil.formatPrice(insideRegion.getUpperPrice()));
				regionPriceLimitList.add(regionPriceLimitVo);
				
				map.setCarModel(insideRegion.getCarModel());
				map.setQuote(regionPriceLimitList);
				list.add(map);
			}
		}
		
		//外埠
		if (null != outsideList && outsideList.size() > 0) {
			for (PubTransportRegionEntity outsideRegion : outsideList) {
				boolean isCreate = true;
				for (QuotationTCVo<List<RegionPriceLimitVo>> insideMap : list) {
					if (insideMap.getCarModel().equals(outsideRegion.getCarModel())) {
						isCreate = false;
						List<RegionPriceLimitVo> quoteList = insideMap.getQuote();
						//市配里面添加外埠
						regionPriceLimitVo = new RegionPriceLimitVo("外埠", 
								FormatUtil.formatPrice(outsideRegion.getLowerPrice()), 
								FormatUtil.formatPrice(outsideRegion.getUpperPrice()));
						quoteList.add(regionPriceLimitVo);
					}
				}
				if (isCreate) {
					map = new QuotationTCVo<List<RegionPriceLimitVo>>();
					regionPriceLimitList = new ArrayList<RegionPriceLimitVo>();
					//新增外埠
					regionPriceLimitVo = new RegionPriceLimitVo("外埠", 
							FormatUtil.formatPrice(outsideRegion.getLowerPrice()), 
							FormatUtil.formatPrice(outsideRegion.getUpperPrice()));
					regionPriceLimitList.add(regionPriceLimitVo);
					
					map.setCarModel(outsideRegion.getCarModel());
					map.setQuote(regionPriceLimitList);
					list.add(map);
				}
			}
		}
		
		return list;
	}
}
