package com.jiuyescm.bms.biz.transport.service;

import java.util.List;

import com.jiuyescm.bms.biz.transport.vo.BizGanxianWaybillReturnVo;
import com.jiuyescm.bms.biz.transport.vo.BizGanxianWaybillVo;

/**
 * 运输业务数据Rpc接口
 * @author yangss
 */
public interface IBizTransportRpcService {

	/**
	 * 推送运输业务数据，返回BMS系统计算费用金额
	 * @param param
	 * @return
	 */
	List<BizGanxianWaybillReturnVo> pushTransportBizBatch(List<BizGanxianWaybillVo> list);
	
	/**
	 * 作废运输业务数据
	 * @param param
	 * @return
	 */
	List<BizGanxianWaybillReturnVo> invalidTransportBizBatch(List<String> list);
}
