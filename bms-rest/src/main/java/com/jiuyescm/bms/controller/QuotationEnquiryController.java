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
import com.jiuyescm.bms.wechat.enquiry.api.IQuotationEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;
import com.jiuyescm.bms.wechat.enquiry.utils.ReqParamVlidateUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.CarModelPriceVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationCJVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationDSZLVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationHXDVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationTCVo;
import com.jiuyescm.bms.wechat.enquiry.vo.RegionPriceLimitVo;
import com.jiuyescm.bms.wechat.enquiry.vo.ToDistrictPriceVo;
import com.jiuyescm.framework.restful.support.controller.BaseController;
import com.jiuyescm.framework.restful.support.exception.InvalidRequestException;
import com.jiuyescm.framework.restful.support.response.Response;

/**
 * 报价查询
 * @author yangss
 *
 */
@RestController
@RequestMapping(value = { "/wechat/{v}" }, produces = { "application/json;charset=UTF-8" })
public class QuotationEnquiryController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(QuotationEnquiryController.class.getName());
	
	@Autowired
	private IQuotationEnquiryService quotationEnquiryService;
	
	/**
	 * 报价查询-城际
	 * @return
	 */
	@RequestMapping(value = "/quotations/cj",method = RequestMethod.POST)
	@ResponseBody
	public Response<QuotationCJVo> quotationCj(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkCjQuotationParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_CJ",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<QuotationCJVo> res = Response.createResponse(null);
		try {
			res.setItems(quotationEnquiryService.queryCjQuotation(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 报价查询-同城-外埠
	 * @return
	 */
	@RequestMapping(value = "/quotations/tc/region",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<QuotationTCVo<List<RegionPriceLimitVo>>>> quotationTcRegion(
			@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkProvinceCityParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_REGION",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<QuotationTCVo<List<RegionPriceLimitVo>>>> res = Response.createResponse(null);
		try {
			List<QuotationTCVo<List<RegionPriceLimitVo>>> list = 
					quotationEnquiryService.queryTCRegionQuotation(requestVo.getParams());
			if (null == list || list.size() <= 0) {
				list = null;
			}
			res.setItems(list);
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 报价查询-同城-车型
	 * @return
	 */
	@RequestMapping(value = "/quotations/tc/carmodel",method = RequestMethod.POST)
	@ResponseBody
	public Response<QuotationTCVo<List<ToDistrictPriceVo>>> quotationTcCarModel(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkTcCarModelQuotationParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_CARMODEL",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<QuotationTCVo<List<ToDistrictPriceVo>>> res = Response.createResponse(null);
		try {
			res.setItems(quotationEnquiryService.queryTCCarModelQuotation(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 报价查询-同城-全部
	 * @return
	 */
	@RequestMapping(value = "/quotations/tc/all",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<QuotationTCVo<List<CarModelPriceVo>>>> quotationTcAll(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkProvinceCityParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_REGION",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<QuotationTCVo<List<CarModelPriceVo>>>> res = Response.createResponse(null);
		try {
			List<QuotationTCVo<List<CarModelPriceVo>>> list = 
					quotationEnquiryService.queryTCAllQuotation(requestVo.getParams());
			if (null == list || list.size() <= 0) {
				list = null;
			}
			res.setItems(list);
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 报价查询-电商专列
	 * @return
	 */
	@RequestMapping(value = "/quotations/dszl",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<QuotationDSZLVo>> quotationDszl(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkDszlQuotationParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_DSZL",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<QuotationDSZLVo>> res = Response.createResponse(null);
		try {
			List<QuotationDSZLVo> list = quotationEnquiryService.queryDSZLQuotation(requestVo.getParams());
			if (null == list || list.size() <= 0) {
				list = null;
			}
			res.setItems(list);
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 报价查询-航鲜达-机场
	 * @return
	 */
	@RequestMapping(value = "/quotations/hxd/airport",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<QuotationHXDVo>> quotationHxdAirport(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkProvinceCityParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_REGION",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<QuotationHXDVo>> res = Response.createResponse(null);
		try {
			List<QuotationHXDVo> list = quotationEnquiryService.queryHXDAirportQuotation(requestVo.getParams());
			if (null == list || list.size() <= 0) {
				list = null;
			}
			res.setItems(list);
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 报价查询-航鲜达-全部
	 * @return
	 */
	@RequestMapping(value = "/quotations/hxd/all",method = RequestMethod.GET)
	@ResponseBody
	public Response<List<QuotationHXDVo>> quotationHxdAll() {
		Response<List<QuotationHXDVo>> res = Response.createResponse(null);
		try {
			List<QuotationHXDVo> list = quotationEnquiryService.queryHXDAllQuotation();
			if (null == list || list.size() <= 0) {
				list = null;
			}
			res.setItems(list);
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
}
