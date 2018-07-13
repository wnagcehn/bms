package com.jiuyescm.bms.base.PubMaterialInfo.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

@Controller("pubMaterialInfoController")
public class PubMaterialInfoController {

	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	
	@DataProvider
	public void query(Page<PubMaterialInfoVo> page,Map<String, Object> param){
		if(param==null){
			param=Maps.newHashMap();
		}
		if(!param.containsKey("delFlag")){
			param.put("delFlag", 0);
		}
		if(param != null && "ALL".equals(param.get("materialType"))){
			param.put("materialType", null);
		}
		PageInfo<PubMaterialInfoVo> tmpPageInfo = pubMaterialInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
}
