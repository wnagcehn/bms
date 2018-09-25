package com.jiuyescm.bms.biz.pallet.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoService;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bizPalletInfoController")
public class BizPalletInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BizPalletInfoController.class.getName());

	@Autowired
	private IBizPalletInfoService bizPalletInfoService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BizPalletInfoEntity findById(Long id) throws Exception {
		return bizPalletInfoService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizPalletInfoEntity> page, Map<String, Object> param) {
		PageInfo<BizPalletInfoEntity> pageInfo = bizPalletInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(BizPalletInfoEntity entity) {
		if (null == entity.getId()) {
			bizPalletInfoService.save(entity);
		} else {
			bizPalletInfoService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BizPalletInfoEntity entity) {
		entity.setDelFlag("1");
		try {
			bizPalletInfoService.delete(entity);
		} catch (Exception e) {
			logger.error("删除失败",e);
		}
		
	}
	
}
