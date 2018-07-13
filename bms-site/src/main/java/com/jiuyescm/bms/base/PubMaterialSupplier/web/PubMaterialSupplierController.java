package com.jiuyescm.bms.base.PubMaterialSupplier.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.mdm.customer.api.IPubMaterialSupplierService;
import com.jiuyescm.mdm.customer.vo.PubMaterialSupplierVo;

@Controller("pubMaterialSupplierController")
public class PubMaterialSupplierController {

	@Resource
	private IPubMaterialSupplierService pubMaterialSupplierService;
	
	@DataProvider
	public void query(Page<PubMaterialSupplierVo> page,Map<String, Object> param){
		PageInfo<PubMaterialSupplierVo> tmpPageInfo = pubMaterialSupplierService.query(param, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
}
