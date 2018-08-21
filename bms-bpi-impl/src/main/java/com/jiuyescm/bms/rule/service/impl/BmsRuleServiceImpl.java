package com.jiuyescm.bms.rule.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.rule.api.IBmsRuleService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.bms.rule.vo.BmsRuleVo;

@Service("bmsRuleService")
public class BmsRuleServiceImpl implements IBmsRuleService{
	
	private static final Logger logger = Logger.getLogger(BmsRuleServiceImpl.class.getName());

	@Resource private IReceiveRuleRepository receiveRuleRepository;
	
	@Override
	public PageInfo<BmsRuleVo> query(Map<String, Object> condition, int pageNo,int pageSize) throws Exception {
		
		PageInfo<BmsRuleVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsRuleVo>();
			PageInfo<BillRuleReceiveEntity> pageInfo=receiveRuleRepository.queryAll(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsRuleVo> list=new ArrayList<BmsRuleVo>();
				for(BillRuleReceiveEntity entity:pageInfo.getList()){
					BmsRuleVo vo=new BmsRuleVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("query:",e);
			throw e;
		}
		return pageVoInfo;
		
	}

}
