package com.jiuyescm.bms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyescm.bms.common.constants.WechatEnquiryConstant;
import com.jiuyescm.bms.wechat.enquiry.api.ICarModelEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;
import com.jiuyescm.bms.wechat.enquiry.vo.CarModelPriceVo;
import com.jiuyescm.framework.restful.support.controller.BaseController;
import com.jiuyescm.framework.restful.support.response.Response;

/**
 * 车型查询接口
 * @author yangss
 *
 */
@RestController
@RequestMapping(value = { "/wechat/{v}" }, produces = { "application/json;charset=UTF-8" })
public class CarModelEnquiryController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(CarModelEnquiryController.class.getName());
	
	@Autowired
	private ICarModelEnquiryService carModelEnquiryService;
	
	/**
	 * 车型
	 * @return
	 */
	@RequestMapping(value = "/carmodels",method = RequestMethod.GET)
	@ResponseBody
	public Response<List<CarModelPriceVo>> queryCarModel() {
		Response<List<CarModelPriceVo>> res = Response.createResponse(null);
		try {
			res.setItems(carModelEnquiryService.queryCarModel());
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
}
