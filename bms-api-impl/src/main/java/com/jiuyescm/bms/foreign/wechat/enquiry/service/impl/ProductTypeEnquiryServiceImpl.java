package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportProductTypeRepository;
import com.jiuyescm.bms.wechat.enquiry.api.IProductTypeEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.utils.TransportEnquiryUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.ProductTypeVo;

@Service("productTypeEnquiryService")
public class ProductTypeEnquiryServiceImpl implements IProductTypeEnquiryService{

	@Autowired
    private IPubTransportProductTypeRepository pubTransportProductTypeRepository;
	
	/**
	 * 查询产品类型
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Override
	public List<ProductTypeVo> quiryProductType(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeName", TransportWayBillTypeEnum.CJ.getCode());
		List<PubTransportProductTypeEntity> productTypeList = pubTransportProductTypeRepository.query(param);
		
		return TransportEnquiryUtil.handleProductType(productTypeList);
	}
}
