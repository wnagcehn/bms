package com.jiuyescm.bms.base.ewarehouse.web;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("eWareHousePR")
public class EWareHousePR {

	@Resource private IEwareHouseService iEwareHouseService;
	@Resource private ISystemCodeService systemCodeService;
	@Resource private SequenceService sequenceService;
	
	
	/**
	 * 查询所有的电仓信息
	 * @param page
	 * @param parameter  
	 */
	@DataProvider  
	public void queryAll(Page<PubElecWarehouseEntity> page,Map<String,Object> parameter){
		
		PageInfo<PubElecWarehouseEntity> tmpPageInfo = iEwareHouseService.queryAll(parameter,page.getPageSize(), page.getPageNo());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	/**
	 * 删除电商仓库信息
	 * @param aCondition
	 */
	@DataResolver
	public String removeEWareHouse(PubElecWarehouseEntity aCondition){
		if(aCondition == null || aCondition.getId() == null){
			return "未移除任何项";
		}
		aCondition.setDelFlag("1");
		iEwareHouseService.updateEWarehouse(aCondition);
		return "SUCCESS";
	}
	
	@DataProvider
	public List<SystemCodeEntity> findEnumList() {
		List<SystemCodeEntity> syscodeList = systemCodeService.findEnumList("PLATFORM_ID");
		return syscodeList;
	}
	
	
	@DataProvider
	public List<WarehouseVo> findWareHouseList() {

		return null;
	}
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<PubElecWarehouseEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try{
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserID();
			for(PubElecWarehouseEntity temp:datas){
				//对操作类型进行判断
				//此为新增电商仓库
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					String warehouseCode =sequenceService.getBillNoOne(PubElecWarehouseEntity.class.getName(), "DSC", "00000");
					temp.setWarehouseCode(warehouseCode);
					temp.setCreateTime(nowdate);
					temp.setCreatorCode(userid);
					temp.setDelFlag("0");
					iEwareHouseService.createEWarehouse(temp);
					
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					//修改电商仓库
					iEwareHouseService.updateEWarehouse(temp);
				}
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			return "数据库操作异常" + ex.getMessage();
		}
	}
	
}
