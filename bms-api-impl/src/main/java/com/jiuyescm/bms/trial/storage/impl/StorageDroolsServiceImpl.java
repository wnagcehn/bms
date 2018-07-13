package com.jiuyescm.bms.trial.storage.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;

import com.jiuyescm.bms.chargerule.receiverule.entity.Rule;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.trial.storage.service.IStorageDroolsService;
import com.jiuyescm.common.utils.drools.KnowledgeBaseUtil;

import org.springframework.stereotype.Service;

/**
 * 仓储drools计算服务类
 * @author zhangzhiwen
 *
 */
@Service("storageDroolsService")
public class StorageDroolsServiceImpl implements IStorageDroolsService {
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	private static final Logger logger = Logger.getLogger(StorageDroolsServiceImpl.class.getName());
	
    /**
     *  仓储费用计算
     * data 业务数据对象
     * price 报价对象
     * rule drools 规则
     */
	public void excute(Object data,Object price,String rule) {
		
		  StatefulKnowledgeSession kSession = null;  
		  
		  try {
			
			  KnowledgeBase kBase = KnowledgeBaseUtil.createKnowledgeBase(rule);
			    
			  kSession = kBase.newStatefulKnowledgeSession();
			  
			  kSession.insert(price); 
			  
	          kSession.insert(data);
	            
	          kSession.fireAllRules();
	          
		} catch (Exception e) {
			//写入日志
			//bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("规则调用异常--",e);
			
		}finally{
			 if (kSession != null)  
	               kSession.dispose();  
		}
		
	}

	@Override
	public void excute(Object data, Object price, Object rule) {
		
		  Rule ru = (Rule)rule;
		
		  StatefulKnowledgeSession kSession = null;  
		  
		  try {
			
			  KnowledgeBase kBase = KnowledgeBaseUtil.createNewKnowledgeBase(ru.getQuotationNo(), ru.getRule());
			    
			  kSession = kBase.newStatefulKnowledgeSession();
			  
			  kSession.insert(price); 
			  
	          kSession.insert(data);
	            
	          kSession.fireAllRules();
	          
		} catch (Exception e) {
			logger.error("规则调用异常--",e);
			
		}finally{
			 if (kSession != null)  
	               kSession.dispose();  
		}
		
	}


}
