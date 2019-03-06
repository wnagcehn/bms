package com.jiuyescm.bms.correct.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsOrderProductEntity;
import com.jiuyescm.bms.correct.service.IBmsOrderProductService;
import com.jiuyescm.bms.correct.vo.BmsOrderProductVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsOrderProductController")
public class BmsOrderProductController {

	@Autowired
	private IBmsOrderProductService bmsOrderProductService;


	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void query(Page<BmsOrderProductVo> page, Map<String, Object> param) throws Exception {
		PageInfo<BmsOrderProductVo> pageInfo = bmsOrderProductService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

}
