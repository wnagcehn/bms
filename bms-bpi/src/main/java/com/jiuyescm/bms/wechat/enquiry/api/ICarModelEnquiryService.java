package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.List;

import com.jiuyescm.bms.wechat.enquiry.vo.CarModelPriceVo;

public interface ICarModelEnquiryService {

	List<CarModelPriceVo> queryCarModel();
}
