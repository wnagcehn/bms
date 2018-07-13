package com.jiuyescm.bms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyescm.bms.common.constants.WechatEnquiryConstant;
import com.jiuyescm.bms.rest.RequestVo;
import com.jiuyescm.bms.wechat.enquiry.api.IElecWarehouseEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;
import com.jiuyescm.bms.wechat.enquiry.utils.ReqParamVlidateUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.ElecWarehouseVo;
import com.jiuyescm.bms.wechat.enquiry.vo.WarehouseVo;
import com.jiuyescm.framework.restful.support.controller.BaseController;
import com.jiuyescm.framework.restful.support.exception.InvalidRequestException;
import com.jiuyescm.framework.restful.support.response.Response;

/**
 * 电商仓库查询
 * @author yangss
 *
 */
@RestController
@RequestMapping(value = { "/wechat/{v}" }, produces = { "application/json;charset=UTF-8" })
public class ElecWarehouseEnquiryController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(ElecWarehouseEnquiryController.class.getName());
	
	@Autowired
	private IElecWarehouseEnquiryService originCityElecWarehouseService;
	
	/**
	 * 始发城市查询电商名称
	 * @return
	 */
	@RequestMapping(value = "/elec_names",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<ElecWarehouseVo>> queryDistinctElec(@RequestBody(required=true) RequestVo requestVo) {
		Response<List<ElecWarehouseVo>> res = Response.createResponse(null);
		//入参校验
		if (!ReqParamVlidateUtil.checkProvinceCityParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_REGION",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		try {
			res.setItems(originCityElecWarehouseService.queryElecName(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 始发城市、电商名称查询电商仓库
	 * @return
	 */
	@RequestMapping(value = "/elec_warehouses",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<WarehouseVo>> queryElecWarehouse(@RequestBody(required=true) RequestVo requestVo) {
		Response<List<WarehouseVo>> res = Response.createResponse(null);
		//入参校验
		if (!ReqParamVlidateUtil.checkDszlQuotationParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_REGION",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		try {
			res.setItems(originCityElecWarehouseService.queryElecWarehouse(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
}
