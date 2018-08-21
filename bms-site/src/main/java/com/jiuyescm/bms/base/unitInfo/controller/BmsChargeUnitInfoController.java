package com.jiuyescm.bms.base.unitInfo.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.unitInfo.entity.BmsChargeUnitInfoEntity;
import com.jiuyescm.bms.base.unitInfo.service.IBmsChargeUnitInfoService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsChargeUnitInfoController")
public class BmsChargeUnitInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BmsChargeUnitInfoController.class.getName());

	@Autowired
	private IBmsChargeUnitInfoService bmsChargeUnitInfoService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BmsChargeUnitInfoEntity findById(Long id) throws Exception {
		return bmsChargeUnitInfoService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsChargeUnitInfoEntity> page, Map<String, Object> param) {
		PageInfo<BmsChargeUnitInfoEntity> pageInfo = bmsChargeUnitInfoService.query(param, page.getPageNo(), page.getPageSize());
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
	public Map<String, String> save(BmsChargeUnitInfoEntity entity) {
		String username = JAppContext.currentUserName();
		String userid = JAppContext.currentUserID();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Map<String, String> map = new HashMap<>();
		Map<String, Object> param = new HashMap<>();
		param.put("delFlag", "0");

		if (null == entity.getId()) {
			List<BmsChargeUnitInfoEntity> list = bmsChargeUnitInfoService.query(param);
			if (null != list && list.size() > 0) {
				for (BmsChargeUnitInfoEntity bmsChargeUnitInfoEntity : list) {
					if (bmsChargeUnitInfoEntity.getUnitCode().equals(entity.getUnitCode())) {
						map.put("fail", "编码不可重复!");
						return map;
					}
					if (bmsChargeUnitInfoEntity.getUnitName().equals(entity.getUnitName())) {
						map.put("fail", "名称不可重复!");
						return map;
					}
				}
			}
			try {
				entity.setCreator(username);
				entity.setCreateTime(currentTime);
				entity.setCreatorId(userid);
				entity.setDelFlag("0");
				bmsChargeUnitInfoService.save(entity);
				map.put("success", "保存成功！");
			} catch (Exception e) {
				logger.error("保存失败: ", e);
			}		
		} else {
			param.put("id", entity.getId());
			
			List<BmsChargeUnitInfoEntity> list = bmsChargeUnitInfoService.query(param);
			if (null != list && list.size() > 0) {
				for (BmsChargeUnitInfoEntity bmsChargeUnitInfoEntity : list) {
					if (bmsChargeUnitInfoEntity.getUnitCode().equals(entity.getUnitCode())) {
						map.put("fail", "编码不可重复!");
						return map;
					}
					if (bmsChargeUnitInfoEntity.getUnitName().equals(entity.getUnitName())) {
						map.put("fail", "名称不可重复!");
						return map;
					}
				}
			}
			try {
				entity.setLastModifier(username);
				entity.setLastModifierId(userid);
				entity.setLastModifyTime(currentTime);
				bmsChargeUnitInfoService.update(entity);
				map.put("success", "更新成功!");
			} catch (Exception e) {
				logger.error("更新失败: ", e);
			}		
		}
		return map;
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsChargeUnitInfoEntity entity) {
		entity.setDelFlag("1");
		bmsChargeUnitInfoService.delete(entity);
	}
	
}
