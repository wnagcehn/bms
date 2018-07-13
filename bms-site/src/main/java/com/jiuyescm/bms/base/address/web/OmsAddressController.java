package com.jiuyescm.bms.base.address.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.PubRegionAliasVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;

/**
 * oms地址
 * @author yangshuaishuai
 */
@Controller("omsAddressController")
public class OmsAddressController {
	
	@Autowired
	private IAddressService addressService;
	
	/**
	 * 地址库
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryRegion(Page<RegionVo> page, Map<String, Object> parameter){
		if(null == parameter){
			parameter=new HashMap<String,Object>();
		}
		PageInfo<RegionVo> pageInfo = addressService.queryRegion(parameter, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 省 下拉框
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<RegionVo> getAllProvinces(Map<String, Object> parameter) {
		List<RegionVo> provinceList = addressService.queryAllProvince();
		if (provinceList != null && provinceList.size()>0) {
			return provinceList;
		} else {
			return null;
		}
	}
	
	/**
	 * 市 下拉框
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<RegionVo> getCitiesByProvinceCode(Map<String, Object> parameter) {
		List<RegionVo> cityList = addressService.queryAllCityByProvinceCode(parameter);
		if (cityList != null && cityList.size()>0) {
			return cityList;
		} else {
			return null;
		}
	}
	
	/**
	 * 区 下拉框
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public List<RegionVo> getDistrictsByCityCode(Map<String, Object> parameter) {
		List<RegionVo> districtList = addressService.queryAllDistrictByCityCode(parameter);
		if (districtList != null && districtList.size()>0) {
			return districtList;
		} else {
			return null;
		}
	}
	
	/**
	 * 地址库别名 查询
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryRegionAlias(Page<PubRegionAliasVo> page, Map<String, Object> parameter){
		if (null ==  parameter) {
			parameter=new HashMap<String,Object>();
		}
		PageInfo<PubRegionAliasVo> pageInfo = addressService.queryGroupConcatAlias(parameter, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 省市区，别名匹配
	 */
	@DataProvider
	public void getProvinceAlias(Page<RegionVo> page, Map<String, Object> parameter) {
		List<RegionVo> list = addressService.queryAllProvinceAlias();
		if (list != null && list.size()>0) {
			for (RegionVo vo : list) {
				System.out.println(vo.getAlias() + "," + vo.getProvince() + "," + vo.getProvincecode());
			}
		}
		System.out.println(list.size());
	}
	
	@DataProvider
	public void getCityAlias(Page<RegionVo> page, Map<String, Object> parameter) {
		List<RegionVo> list = addressService.queryAllCityAlias();
		if (list != null && list.size()>0) {
			for (RegionVo vo : list) {
				System.out.println(vo.getAlias() + "," + vo.getProvince() + "," + vo.getProvincecode()
						+"," + vo.getCity() + "," + vo.getCitycode());
			}
		}
		System.out.println(list.size());
	}
	
	@DataProvider
	public void getDistrictAlias(Page<RegionVo> page, Map<String, Object> parameter) {
		List<RegionVo> list = addressService.queryAllDistrictAlias();
		if (list != null && list.size()>0) {
			for (RegionVo vo : list) {
				System.out.println(vo.getAlias() + "," + vo.getProvince() + "," + vo.getProvincecode()
						+"," + vo.getCity() + "," + vo.getCitycode()
						+"," + vo.getDistrict() + "," + vo.getDistrictcode());
			}
		}
		System.out.println(list.size());
	}
}
