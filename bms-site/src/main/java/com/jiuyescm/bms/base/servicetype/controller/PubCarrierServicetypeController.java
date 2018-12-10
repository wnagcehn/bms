package com.jiuyescm.bms.base.servicetype.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.servicetype.service.ICarrierProductService;
import com.jiuyescm.bms.base.servicetype.vo.CarrierProductVo;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("pubCarrierServicetypeController")
public class PubCarrierServicetypeController {

	@Autowired
	private ICarrierProductService carrierProductService;
	
	@Autowired
	private ICarrierService carrierService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public CarrierProductVo findById(Long id) throws Exception {
		return carrierProductService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void query(Page<CarrierProductVo> page, Map<String, Object> param) throws Exception {
		PageInfo<CarrierProductVo> pageInfo = carrierProductService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
			
		}
		
		String carrierid = "1999999999999999";
		String servicecode = "2";
		CarrierProductVo vo = carrierProductService.findByCode(carrierid,servicecode);
		System.out.println(vo.toString()+"---"+vo.getCarriercode()+"---"+vo.getCarriername());
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void queryNotDel(Page<CarrierProductVo> page, Map<String, Object> param) throws Exception {
		param.put("delFalg", "0");
		PageInfo<CarrierProductVo> pageInfo = carrierProductService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param vo
	 * @return
	 * @throws Exception 
	 */
	@DataResolver
	public void save(CarrierProductVo vo) throws Exception {
		if (null == vo.getId()) {
			carrierProductService.save(vo);
		} else {
			carrierProductService.update(vo);
		}
	}
	
	@DataProvider
	public Map<String, String> getCarrierName(){
		List<CarrierVo> carrierList = carrierService.queryAllCarrier();
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(carrierList!=null && carrierList.size()>0){
			for(int i=0;i<carrierList.size();i++){
				map.put(carrierList.get(i).getCarrierid(), carrierList.get(i).getName());
			}
		}
		return map;
	}
	
}
