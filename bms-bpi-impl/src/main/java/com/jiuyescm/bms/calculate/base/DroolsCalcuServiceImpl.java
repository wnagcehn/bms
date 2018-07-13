package com.jiuyescm.bms.calculate.base;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.stereotype.Service;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.jiuyescm.common.utils.drools.KnowledgeBaseUtil;

@Service("droolsCalcuServiceImpl")
public class DroolsCalcuServiceImpl implements IDroolsCalcuService {

	private static final Logger logger = Logger.getLogger(DroolsCalcuServiceImpl.class.getName());
	private static Map<String,KnowledgeBase> kbaseMap = new HashMap<String,KnowledgeBase>();
	private static Map<String,String> keyValue = new HashMap<String,String>();
	
	@Override
	public void excute(Object resultVo,Object reqVo,Object bizData, String rule,String ruleNo) {
		
		String ruleMd5 = getMd5(rule);
		KnowledgeBase kBase = initKBase(ruleNo,ruleMd5,rule);
		StatefulKnowledgeSession kSession = null; 
		  try {
			  kSession = kBase.newStatefulKnowledgeSession();
			  kSession.insert(resultVo); 
			  kSession.insert(reqVo); 
	          kSession.insert(bizData);
	          kSession.fireAllRules();
		} catch (Exception e) {
			logger.error("规则调用异常--",e);
		}finally{
			 if (kSession != null)  
	               kSession.dispose();  
		}
	}
	
	private KnowledgeBase initKBase(String ruleNo,String ruleMd5,String rule){
		synchronized(this){
			KnowledgeBase kBase = null;
			if(kbaseMap.containsKey(ruleNo) && keyValue.containsKey(ruleNo)){ 
				if(keyValue.get(ruleNo).equals(ruleMd5)){
					logger.info("【"+ruleNo+"】直接创建session，当前缓存的kbase数量【"+keyValue.size()+"】");
					kBase = kbaseMap.get(ruleNo);
				}
				else{
					kbaseMap.remove(ruleNo);
					keyValue.remove(ruleNo);
				}
			}
			else{
				kbaseMap.remove(ruleNo);
				keyValue.remove(ruleNo);
			}
			if(kBase == null){
				  logger.info("创建KBase");
				  kBase = KnowledgeBaseUtil.createKnowledgeBase(rule);
				  kbaseMap.put(ruleNo, kBase);
				  keyValue.put(ruleNo,ruleMd5);
			  }
			return kBase;
		}
	}
	
	private String getMd5(String str){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
			return base64en.encode(md5.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			 logger.error(e);
			return null;
		}
	}
}
