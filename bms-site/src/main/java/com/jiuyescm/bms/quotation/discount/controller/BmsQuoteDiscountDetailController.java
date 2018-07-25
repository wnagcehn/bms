package com.jiuyescm.bms.quotation.discount.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountDetailService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;


/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsQuoteDiscountDetailController")
public class BmsQuoteDiscountDetailController {

	private static final Logger logger = LoggerFactory.getLogger(BmsQuoteDiscountDetailController.class.getName());

	@Autowired
	private IBmsQuoteDiscountDetailService bmsQuoteDiscountDetailService;
	
	@Autowired
	private IWarehouseService warehouseService;
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BmsQuoteDiscountDetailEntity findById(Long id) throws Exception {
		return bmsQuoteDiscountDetailService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsQuoteDiscountDetailEntity> page, Map<String, Object> param) {
		PageInfo<BmsQuoteDiscountDetailEntity> pageInfo = bmsQuoteDiscountDetailService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(BmsQuoteDiscountDetailEntity entity) {
		String username = JAppContext.currentUserName();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		for (WarehouseVo warehouseVo : warehouseVos) {
			if (warehouseVo.getWarehouseid().equals(entity.getWarehouseCode())) {
				entity.setWarehouseName(warehouseVo.getWarehousename());
			}
		}
		if (null == entity.getId()) {
			entity.setDelFlag("0");
			entity.setCreator(username);
			entity.setCreateTime(currentTime);
			bmsQuoteDiscountDetailService.save(entity);
		} else {
			entity.setLastModifier(username);
			entity.setLastModifyTime(currentTime);
			bmsQuoteDiscountDetailService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsQuoteDiscountDetailEntity entity) {
		try {
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			bmsQuoteDiscountDetailService.delete(entity);
		} catch (Exception e) {
			logger.error("删除失败，", e.getMessage());
		}		
	}
	
}