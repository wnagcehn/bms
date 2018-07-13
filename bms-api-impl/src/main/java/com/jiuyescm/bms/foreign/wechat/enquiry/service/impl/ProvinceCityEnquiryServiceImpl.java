package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.common.enumtype.TemplateSubjectEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.pub.transport.entity.PubTransportOriginCityEntity;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportOriginCityRepository;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportProductTypeRepository;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRepository;
import com.jiuyescm.bms.wechat.enquiry.api.IProvinceCityEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.utils.TransportEnquiryUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.InitResponseVO;
import com.jiuyescm.bms.wechat.enquiry.vo.ProductTypeVo;
import com.jiuyescm.bms.wechat.enquiry.vo.ProvinceCityVo;

@Service("provinceCityEnquiryService")
public class ProvinceCityEnquiryServiceImpl implements IProvinceCityEnquiryService{

	@Autowired
    private IPubTransportOriginCityRepository pubTransportOriginCityRepository;
	@Autowired
	private IPriceTransportLineRepository transportLineRepository;
	@Autowired
	private IPubTransportProductTypeRepository productTypeRepository;
	
	/**
	 * 城际初始化查询
	 */
	@Override
	public InitResponseVO queryInit(String provinceName, String cityName) {
		InitResponseVO responseVo = null;
		//查询所有产品类型，按照优先级排序
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeName", TransportWayBillTypeEnum.CJ.getCode());
		List<PubTransportProductTypeEntity> productTypeList = productTypeRepository.query(param);
		if (null != productTypeList && productTypeList.size() > 0) {
			param = new HashMap<String, Object>();
			param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
			param.put("transportType", TransportWayBillTypeEnum.CJ.getCode());
			param.put("productType", productTypeList.get(0).getProductTypeName());
			param.put("fromProvince", provinceName);
			param.put("fromCity", cityName);
			List<PriceTransportLineEntity> lineList = transportLineRepository.queryToCityByProductType(param);
			
			//封装处理初始化返回VO
			responseVo = encapInitVO(provinceName, cityName, productTypeList, lineList);
		}
		return responseVo;
	}
	
	/**
	 * 封装Init返回VO
	 */
	private InitResponseVO encapInitVO(String province, String cityName, 
			List<PubTransportProductTypeEntity> productTypeList,
			List<PriceTransportLineEntity> lineList){
		InitResponseVO initVo = new InitResponseVO();
		List<Map<String, Object>> toCitys = new ArrayList<Map<String,Object>>();
		List<ProductTypeVo> products = new ArrayList<ProductTypeVo>();
		
		initVo.setFromProvince(province);
		initVo.setFromCity(cityName);
		if (null == productTypeList || productTypeList.size() <= 0 
				|| null == lineList || lineList.size() <= 0) {
			initVo.setToCitys(toCitys);
			initVo.setProducts(products);
			return initVo;
		}
		//处理目的省市
		toCitys = TransportEnquiryUtil.handleProvinceCity(lineList);
		//处理产品类型
		products = TransportEnquiryUtil.handleProductType(productTypeList);
		
		initVo.setToCitys(toCitys);
		initVo.setProducts(products);
		return initVo;
	}
	
	
	/**
	 * 查询始发城市
	 */
	@Override
	public List<ProvinceCityVo> queryFromCitys(String bizType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeName", bizType);
		List<PubTransportOriginCityEntity> originCityList = pubTransportOriginCityRepository.queryList(param);
		List<ProvinceCityVo> voList = new ArrayList<ProvinceCityVo>();
		for (PubTransportOriginCityEntity entity : originCityList) {
			ProvinceCityVo vo = new ProvinceCityVo();
			vo.setProvince(entity.getOriginProvince());
			vo.setCity(entity.getOriginCity());
			voList.add(vo);
		}
		
		return voList;
	}

	/**
	 * 城际查询目的城市
	 * 城际的需要产品类型
	 */
	@Override
	public List<Map<String, Object>> queryToCityCJ(Map<String, String> reqParam) {
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", TransportWayBillTypeEnum.CJ.getCode());
		param.put("fromProvince", fromProvince);
		param.put("fromCity", fromCity);
		//城际的需要产品类型
		param.put("productType", reqParam.get("productType"));
		List<PriceTransportLineEntity> toCityList = transportLineRepository.queryToCityByProductType(param);
		
		//封装目的城市的VO
		return TransportEnquiryUtil.handleProvinceCity(toCityList);
	}

	/**
	 * 同城查询目的城市
	 */
	@Override
	public List<Map<String, Object>> queryToCityTC(Map<String, String> reqParam) {
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("templateTypeCode", TemplateSubjectEnum.TRANS_FEES.getCode());
		param.put("transportType", TransportWayBillTypeEnum.TC.getCode());
		param.put("fromProvince", fromProvince);
		param.put("fromCity", fromCity);
		List<PriceTransportLineEntity> toCityList = transportLineRepository.queryToCity(param);
		//封装目的城市的VO
		return TransportEnquiryUtil.handleProvinceCity(toCityList);
	}

}
