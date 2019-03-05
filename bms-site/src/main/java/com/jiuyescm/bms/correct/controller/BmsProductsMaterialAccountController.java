package com.jiuyescm.bms.correct.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialAccountService;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsProductsMaterialAccountController")
public class BmsProductsMaterialAccountController {

	//private static final Logger logger = LoggerFactory.getLogger(BmsProductsMaterialAccountController.class.getName());

	@Autowired
	private IBmsProductsMaterialAccountService bmsProductsMaterialAccountService;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void query(Page<BmsProductsMaterialAccountVo> page, Map<String, Object> param) throws Exception {
		PageInfo<BmsProductsMaterialAccountVo> pageInfo = bmsProductsMaterialAccountService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

}
