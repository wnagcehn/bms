package com.jiuyescm.bms.base.monthFeeCount.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.monthFeeCount.service.IPubMonthFeeCountService;
import com.jiuyescm.bms.base.monthFeeCount.vo.PubMonthFeeCountVo;
import com.jiuyescm.common.ConstantInterface;

@Controller("pubMonthFeeCountController")
public class PubMonthFeeCountController {
	@Autowired private IPubMonthFeeCountService pubMonthFeeCountService;
	
	@DataProvider
	public void query(Page<PubMonthFeeCountVo> page,Map<String,Object> parameter) {
		PageInfo<PubMonthFeeCountVo> tmpPageInfo = pubMonthFeeCountService.queryAll(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	
	
	
	/**
	 * 作废表示
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getInvalidflag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_0, "启用");
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_1, "作废");
		return mapValue;
	}
	
	/**
	 * 商家是否自有
	 * @return
	 */
	@DataProvider
	public Map<Integer, String> getOwnflag(String all) {
		Map<Integer, String> mapValue = new LinkedHashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(999, "全部");
		}
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_0, "否");
		mapValue.put(ConstantInterface.InvalidInterface.INVALID_1, "是");
		return mapValue;
	}
}
