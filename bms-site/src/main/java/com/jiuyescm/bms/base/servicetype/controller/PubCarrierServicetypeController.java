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
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.base.servicetype.service.IPubCarrierServicetypeService;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("pubCarrierServicetypeController")
public class PubCarrierServicetypeController {

	//private static final Logger logger = LoggerFactory.getLogger(PubCarrierServicetypeController.class.getName());

	@Autowired
	private IPubCarrierServicetypeService pubCarrierServicetypeService;
	
	@Autowired
	private ICarrierService carrierService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public PubCarrierServicetypeEntity findById(Long id) throws Exception {
		return pubCarrierServicetypeService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PubCarrierServicetypeEntity> page, Map<String, Object> param) {
		PageInfo<PubCarrierServicetypeEntity> pageInfo = pubCarrierServicetypeService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(PubCarrierServicetypeEntity entity) {
		if (null == entity.getId()) {
			pubCarrierServicetypeService.save(entity);
		} else {
			pubCarrierServicetypeService.update(entity);
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
