package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.List;

import com.jiuyescm.bms.wechat.enquiry.vo.ProductTypeVo;

/**
 * 产品类型查询接口
 * @author yangss
 *
 */
public interface IProductTypeEnquiryService {

	List<ProductTypeVo> quiryProductType();
}
