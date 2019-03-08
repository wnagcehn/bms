package com.jiuyescm.bms.quotation.discount.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountDetailService;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountTemplateService;
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
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Autowired
	private IBmsQuoteDiscountTemplateService bmsQuoteDiscountTemplateService;
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
		BmsQuoteDiscountTemplateEntity templateEntity =bmsQuoteDiscountTemplateService.queryOne(param); 
		param.put("bizType", templateEntity.getBizType());
		param.put("subjectCode", templateEntity.getSubjectCode());
		PageInfo<BmsQuoteDiscountDetailEntity> pageInfo;
		String carrierid = "";
		if("DISPATCH".equals(param.get("bizType"))){
			String string = (String) param.get("subjectCode");
			Map<String, Object> map = new HashMap<>();
			map.put("typeCode", "DISPATCH_COMPANY");
			map.put("code", string);
			PageInfo<SystemCodeEntity> sysPageInfo = systemCodeService.query(map, 1, 20);
			if(CollectionUtils.isNotEmpty(sysPageInfo.getList())){
				carrierid = sysPageInfo.getList().get(0).getExtattr1();
			}
		}
		Map<String,Object> conditionMap = new HashMap<>();
		conditionMap.put("carrierid", carrierid);
		pageInfo = bmsQuoteDiscountDetailService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			for (BmsQuoteDiscountDetailEntity entity : pageInfo.getList()) {
				if (null != entity.getFirstPriceRate()) {
					entity.setFirstPriceRateDT(entity.getFirstPriceRate().toString()+"%");
				}
				if (null != entity.getContinuePirceRate()) {
					entity.setContinuePirceRateDT(entity.getContinuePirceRate().toString()+"%");
				}
				if (null != entity.getUnitPriceRate()) {
					entity.setUnitPriceRateDT(entity.getUnitPriceRate().toString()+"%");
				}
				if(StringUtils.isNotBlank(carrierid)&&StringUtils.isNotBlank(entity.getServiceTypeCode())){
			        conditionMap.put("servicecode", entity.getServiceTypeCode());
			        String nameString =  bmsQuoteDiscountDetailService.queryServiceTypeName(conditionMap);
			        if(StringUtils.isNoneBlank(nameString)){
			        	entity.setServiceTypeName(nameString);
			        }
				}
			}		
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
	public Map<String, String> save(BmsQuoteDiscountDetailEntity entity) {
		String username = JAppContext.currentUserName();
		Map<String, String> result = new HashMap<>();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		for (WarehouseVo warehouseVo : warehouseVos) {
			if (warehouseVo.getWarehouseid().equals(entity.getWarehouseCode())) {
				entity.setWarehouseName(warehouseVo.getWarehousename());
			}
		}
		if (null == entity.getUpLimit() && entity.getDownLimit() != null) {
			result.put("fail", "上限下限同时存在，或者同时不存在！");
			return result;
		}
		if (null != entity.getUpLimit() && entity.getDownLimit() == null) {
			result.put("fail", "上限下限同时存在，或者同时不存在！");
			return result;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String start = sdf.format(entity.getStartTime());
		Date startTime = null;
		try {
			startTime = sdf.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		entity.setStartTime(new Timestamp(startTime.getTime()));
		String end = sdf.format(entity.getEndTime());
		end=end.substring(0,10);
		Date endTime = null;
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			endTime = sdf2.parse(end+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		entity.setEndTime(new Timestamp(endTime.getTime()));
		
		if (null == entity.getId()) {
			entity.setDelFlag("0");
			entity.setCreator(username);
			entity.setCreateTime(currentTime);
			bmsQuoteDiscountDetailService.save(entity);
			result.put("success", "保存成功");
		} else {
			entity.setLastModifier(username);
			entity.setLastModifyTime(currentTime);
			bmsQuoteDiscountDetailService.update(entity);
			result.put("success", "修改成功");
		}
		return result;
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
