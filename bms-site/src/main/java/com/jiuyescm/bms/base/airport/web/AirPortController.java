package com.jiuyescm.bms.base.airport.web;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("airPortPR")
public class AirPortController {
	
	@Resource private IPubAirportService pubAirportService;
	@Resource private SequenceService sequenceService;
	
	@DataProvider  
	public void queryAll(Page<PubAirportEntity> page,Map<String,Object> parameter){
		
		PageInfo<PubAirportEntity> tmpPageInfo = pubAirportService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
		
	}
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<PubAirportEntity> datas){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PubAirportEntity temp:datas){
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					String airport_id =sequenceService.getBillNoOne(PubAirportEntity.class.getName(), "A", "00000");
					temp.setAirportId(airport_id);
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					pubAirportService.save(temp);
					
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					//修改电商仓库
					pubAirportService.update(temp);
				}
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			return "后台执行异常";
		}
	}
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String delete(PubAirportEntity datas){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			
			datas.setDelFlag("1");
			datas.setLastModifier(userid);
			datas.setLastModifyTime(nowdate);
			pubAirportService.update(datas);
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			return "后台执行异常";
		}
	}
	
}
