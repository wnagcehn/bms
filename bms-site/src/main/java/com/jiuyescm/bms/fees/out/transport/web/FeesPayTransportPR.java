package com.jiuyescm.bms.fees.out.transport.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;

@Component("feesPayTransportPR")
public class FeesPayTransportPR {
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	@DataProvider
	public List<SystemCodeEntity> getSubjectCode(){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("pay_ts_contract_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			list.add(code);			
		} 
		
		return list;
	}
	
	@DataProvider
	public List<SystemCodeEntity> getOtherSubjectCode(String parameter){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("pay_transport_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			list.add(code);			
		} 
		
		return list;
	}
	/**
	 * 运输增值费
	 * @return
	 */
	@DataProvider
	public List<SystemCodeEntity> getTransportSubjectTypeList(){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		Map<String,String> map=bmsGroupSubjectService.getSubject("pay_transport_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			list.add(code);			
		} 
		
		return list;
	}
	
}
