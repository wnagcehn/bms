package com.jiuyescm.bms.base.jywarehouse.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.config.service.IBmsWarehouseConfigService;
import com.jiuyescm.bms.base.config.vo.BmsWarehouseConfigVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("jywarehousePR")
public class JYWarehousePR {

	@Autowired private IWarehouseService warehouseService;
	@Autowired private IBmsWarehouseConfigService bmsWarehouseConfigService;
	@DataProvider
	public void query(Page<BmsWarehouseVo> page, Map<String, Object> parameter) throws Exception {
		if(null==parameter){
			parameter=new HashMap<String,Object>();
		}
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		if(null != parameter.get("virtualflag") && "ALL".equals(parameter.get("virtualflag")))
		{
			parameter.put("virtualflag", null);
		}
		if(null != parameter.get("delflag") && "ALL".equals(parameter.get("delflag")))
		{
			parameter.put("delflag", null);
		}
		PageInfo<WarehouseVo> tmpPageInfo = warehouseService.query(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(reBindList(tmpPageInfo.getList()));
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	private List<BmsWarehouseVo> reBindList(List<WarehouseVo> list) throws Exception{
		List<BmsWarehouseVo> reList=new ArrayList<BmsWarehouseVo>();
		List<BmsWarehouseConfigVo> configList=bmsWarehouseConfigService.queryAll();
		for(WarehouseVo entity:list){
			BmsWarehouseVo vo=new BmsWarehouseVo();
			vo.setAddress(entity.getAddress());
			vo.setCreperson(entity.getCreperson());
			vo.setCretime(entity.getCretime());
			vo.setCrepersonid(entity.getCrepersonid());
			vo.setDelflag(entity.getDelflag());
			vo.setIsDropDisplay(0);
			vo.setDisplayLevel(0);
			for(BmsWarehouseConfigVo configVo:configList){
				if(configVo.getWarehouseCode().equals(entity.getWarehouseid())){
					vo.setDisplayLevel(configVo.getDisplayLevel());
					vo.setIsDropDisplay(configVo.getIsDropDisplay());
					break;
				}
			}
			vo.setLinkman(entity.getLinkman());
			vo.setMobile(entity.getMobile());
			vo.setModperson(entity.getModperson());
			vo.setModpersonid(entity.getModpersonid());
			vo.setModtime(entity.getModtime());
			vo.setRegionid(entity.getRegionid());
			vo.setTel(entity.getTel());
			vo.setVirtualflag(entity.getVirtualflag());
			vo.setWarehousecode(entity.getWarehousecode());
			vo.setWarehouseid(entity.getWarehouseid());
			vo.setWarehousename(entity.getWarehousename());
			vo.setZipcode(entity.getZipcode());
			vo.setProvince(entity.getProvince());
			vo.setCity(entity.getCity());
			reList.add(vo);
		}
		return reList;
	}
	
	@DataResolver
	public void saveDisplayLevel(BmsWarehouseVo vo) throws Exception{
		try{
			BmsWarehouseConfigVo voConfig=new BmsWarehouseConfigVo();
			voConfig.setWarehouseCode(vo.getWarehouseid());
			voConfig.setCreator(JAppContext.currentUserName());
			voConfig.setCreateTime(JAppContext.currentTimestamp());
			voConfig.setLastModifier(JAppContext.currentUserName());
			voConfig.setLastModifyTime(JAppContext.currentTimestamp());
			voConfig.setDisplayLevel(vo.getDisplayLevel());
			voConfig.setIsDropDisplay(vo.getIsDropDisplay());
			int k=bmsWarehouseConfigService.saveEntity(voConfig);
			if(k==0){
				throw new Exception("保存失败");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
}