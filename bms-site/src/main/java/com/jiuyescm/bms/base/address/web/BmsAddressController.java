package com.jiuyescm.bms.base.address.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.address.entity.PubAddressEntity;
import com.jiuyescm.bms.base.address.service.IPubAddressService;
import com.jiuyescm.bms.base.address.vo.PubAddressVo;

import org.apache.log4j.Logger;

@Controller("bmsAddressController")
public class BmsAddressController {
	
	private static final Logger logger = Logger.getLogger(BmsAddressController.class.getName());
	
	@Resource 
	private IPubAddressService pubAddressService; 
	
	@DataProvider
	public void query(Page<PubAddressVo> page, Map<String, Object> param) {
		PageInfo<PubAddressVo> pageInfo = pubAddressService.queryVo(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public void save(PubAddressEntity entity) {
		if (entity.getId() == null) {
			pubAddressService.save(entity);
		} else {
			pubAddressService.update(entity);
		}
	}
	
	/**
	 * 获取所有省份
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<PubAddressEntity> queryAllProvince(Map<String, Object> parameter) {
		List<PubAddressEntity> provinceList = pubAddressService.queryAllProvince(parameter);
		if (provinceList != null && provinceList.size()>0) {
			return provinceList;
		} else {
			return null;
		}
	}
	
	/**
	 * 查询省份对应的市
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<PubAddressEntity> queryCityByProvince(Map<String, Object> parameter) {
		List<PubAddressEntity> provinceList = pubAddressService.queryCityByProvince(parameter);
		if (provinceList != null && provinceList.size()>0) {
			return provinceList;
		} else {
			return null;
		}
	}
	
	/**
	 * 查询市对应的市区
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<PubAddressEntity> queryDistrictByCity(Map<String, Object> parameter) {
		List<PubAddressEntity> provinceList = pubAddressService.queryDistrictByCity(parameter);
		if (provinceList != null && provinceList.size()>0) {
			return provinceList;
		} else {
			return null;
		}
	}
}
