package com.jiuyescm.bms.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyescm.bms.common.constants.WechatEnquiryConstant;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.rest.RequestVo;
import com.jiuyescm.bms.wechat.enquiry.api.IProvinceCityEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;
import com.jiuyescm.bms.wechat.enquiry.utils.ReqParamVlidateUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.InitResponseVO;
import com.jiuyescm.bms.wechat.enquiry.vo.ProvinceCityVo;
import com.jiuyescm.framework.restful.support.controller.BaseController;
import com.jiuyescm.framework.restful.support.exception.InvalidRequestException;
import com.jiuyescm.framework.restful.support.response.Response;

/**
 * 省市查询
 * @author yangss
 *
 */
@RestController
@RequestMapping(value = { "/wechat/{v}" }, produces = { "application/json;charset=UTF-8" })
public class ProvinceCityEnquiryController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(ProvinceCityEnquiryController.class.getName());
	
	@Autowired
	private IProvinceCityEnquiryService provinceCityEnquiryService;
	
	/**
	 * 城际-初始化查询
	 * 1.根据ip定位地址：省、市，定位失败默认给上海、上海市
	 * 2.查询所有产品类型，按优先级排序
	 * 3.根据省、市、产品类型第一条，查询报价里面的目的省、市，返回
	 * @return
	 */
	@RequestMapping(value = "/citys/cj/init",method = RequestMethod.GET)
	@ResponseBody
	public Response<InitResponseVO> queryCjInit(HttpServletRequest request) {
		Response<InitResponseVO> res = Response.createResponse(null);
		
		//默认省市
		String provinceName = "上海";
		String cityName = "上海市";
		try {
			res.setItems(provinceCityEnquiryService.queryInit(provinceName, cityName));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
	
	/**
	 * 始发城市-城际
	 * @return
	 */
	@RequestMapping(value = "/citys/cj/from",method = RequestMethod.GET)
	@ResponseBody
	public Response<List<ProvinceCityVo>> fromCitysCj() {
		Response<List<ProvinceCityVo>> res = Response.createResponse(null);
		try {
			res.setItems(provinceCityEnquiryService.queryFromCitys(TransportWayBillTypeEnum.CJ.getCode()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
	
	/**
	 * 目的城市-城际
	 * @return
	 */
	@RequestMapping(value = "/citys/cj/to",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<Map<String, Object>>> toCitysCj(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkToCitysParam(requestVo)) {
			throw new InvalidRequestException("INVALID_TOCITY_CJ",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<Map<String, Object>>> res = Response.createResponse(null);
		
		try {
			res.setItems(provinceCityEnquiryService.queryToCityCJ(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
	
	/**
	 * 始发城市-同城
	 * @return
	 */
	@RequestMapping(value = "/citys/tc/from",method = RequestMethod.GET)
	@ResponseBody
	public Response<List<ProvinceCityVo>> fromCitysTc() {
		Response<List<ProvinceCityVo>> res = Response.createResponse(null);
		try {
			res.setItems(provinceCityEnquiryService.queryFromCitys(TransportWayBillTypeEnum.TC.getCode()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
	
	/**
	 * 目的城市-同城
	 * @return
	 */
	@RequestMapping(value = "/citys/tc/to",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<Map<String, Object>>> toCitysTc(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkProvinceCityParam(requestVo)) {
			throw new InvalidRequestException("INVALID_TOCITY_TC",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<Map<String, Object>>> res = Response.createResponse(null);
		try {
			res.setItems(provinceCityEnquiryService.queryToCityTC(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
	
	/**
	 * 始发城市-电商专列
	 * @return
	 */
	@RequestMapping(value = "/citys/dszl",method = RequestMethod.GET)
	@ResponseBody
	public Response<List<ProvinceCityVo>> fromCitysDszl() {
		Response<List<ProvinceCityVo>> res = Response.createResponse(null);
		try {
			res.setItems(provinceCityEnquiryService.queryFromCitys(TransportWayBillTypeEnum.DSZL.getCode()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
	
	/**
	 * 始发城市-航鲜达
	 * @return
	 */
	@RequestMapping(value = "/citys/hxd",method = RequestMethod.GET)
	@ResponseBody
	public Response<List<ProvinceCityVo>> fromCitysHxd() {
		Response<List<ProvinceCityVo>> res = Response.createResponse(null);
		try {
			res.setItems(provinceCityEnquiryService.queryFromCitys(TransportWayBillTypeEnum.HXD.getCode()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		
		return res;
	}
}
