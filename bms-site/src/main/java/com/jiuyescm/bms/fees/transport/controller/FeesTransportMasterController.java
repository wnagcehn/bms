package com.jiuyescm.bms.fees.transport.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.service.IFeesTransportMasterService;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("feesTransportMasterController")
public class FeesTransportMasterController {

	private static final Logger logger = LoggerFactory.getLogger(FeesTransportMasterController.class.getName());

	@Autowired
	private IFeesTransportMasterService feesTransportMasterService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public FeesTransportMasterEntity findById(Long id) throws Exception {
		return feesTransportMasterService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FeesTransportMasterEntity> page, Map<String, Object> param) {
		PageInfo<FeesTransportMasterEntity> pageInfo = feesTransportMasterService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(FeesTransportMasterEntity entity) {
		if (null == entity.getId()) {
			feesTransportMasterService.save(entity);
		} else {
			feesTransportMasterService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(FeesTransportMasterEntity entity) {
		feesTransportMasterService.delete(entity.getId());
	}
	
}
