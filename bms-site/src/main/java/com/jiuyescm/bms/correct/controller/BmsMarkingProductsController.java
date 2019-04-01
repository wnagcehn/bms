package com.jiuyescm.bms.correct.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.service.IBmsMarkingProductsService;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsMarkingProductsController")
public class BmsMarkingProductsController {

	//private static final Logger logger = LoggerFactory.getLogger(BmsMarkingProductsController.class.getName());

	@Autowired
	private IBmsMarkingProductsService bmsMarkingProductsService;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void query(Page<BmsMarkingProductsVo> page, Map<String, Object> param) throws Exception {
		PageInfo<BmsMarkingProductsVo> pageInfo = bmsMarkingProductsService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataProvider
	public void queryByWeight(Page<BmsMarkingProductsVo> page, Map<String, Object> param) throws Exception {
		PageInfo<BmsMarkingProductsVo> pageInfo = bmsMarkingProductsService.queryByWeight(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public void queryByMaterial(Page<BmsMarkingProductsVo> page, Map<String, Object> param) throws Exception {
		if ("BWD".equals(param.get("type"))) {
			param.put("bwdMark", param.get("materialMark"));
		}
		if ("PMX".equals(param.get("type"))) {
			param.put("pmxMark", param.get("materialMark"));
		}
		if ("ZX".equals(param.get("type"))){
			param.put("zxMark", param.get("materialMark"));
		}
		
		PageInfo<BmsMarkingProductsVo> pageInfo = bmsMarkingProductsService.queryByMaterial(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
}
