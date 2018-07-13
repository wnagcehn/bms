package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.wechat.enquiry.vo.CarModelPriceVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationCJVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationDSZLVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationHXDVo;
import com.jiuyescm.bms.wechat.enquiry.vo.QuotationTCVo;
import com.jiuyescm.bms.wechat.enquiry.vo.RegionPriceLimitVo;
import com.jiuyescm.bms.wechat.enquiry.vo.ToDistrictPriceVo;


/**
 * 报价查询接口
 * @author yangss
 *
 */
public interface IQuotationEnquiryService {

	//城际报价
	QuotationCJVo queryCjQuotation(Map<String, String> params);
	
	//同城-外埠
	List<QuotationTCVo<List<RegionPriceLimitVo>>> queryTCRegionQuotation(Map<String, String> params);
	
	//同城-车型
	QuotationTCVo<List<ToDistrictPriceVo>> queryTCCarModelQuotation(Map<String, String> params);
	
	//同城-全部
	List<QuotationTCVo<List<CarModelPriceVo>>> queryTCAllQuotation(Map<String, String> params);
	
	//电商专列
	List<QuotationDSZLVo> queryDSZLQuotation(Map<String, String> params);
	
	//航鲜达-机场
	List<QuotationHXDVo> queryHXDAirportQuotation(Map<String, String> params);
	
	//航鲜达-全部
	List<QuotationHXDVo> queryHXDAllQuotation();
}
