package com.jiuyescm.bms.drools.common;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;  
import org.drools.KnowledgeBaseFactory;  
import org.drools.builder.KnowledgeBuilder;  
import org.drools.builder.KnowledgeBuilderFactory;  
import org.drools.builder.ResourceType;  
import org.drools.io.ResourceFactory; 


public class KnowledgeBaseUtil {
	
    private final static ConcurrentHashMap<String,Object> ruleMap = new ConcurrentHashMap<String,Object>();
     
	public static KnowledgeBase createKnowledgeBase(String rule){  
		
		 KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();  
	     KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
		  //装入规则，可以装入多个  
		  try {
			  builder.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
		  } catch (UnsupportedEncodingException e) {
			  throw new RuntimeException(e.getMessage());
			  
		  }  
		  
		  if(builder.hasErrors()){
			  throw new RuntimeException(builder.getErrors().toString());
		  }
		  
		  kBase.addKnowledgePackages(builder.getKnowledgePackages());  
		  
		  return kBase;
    }  
	
	public static KnowledgeBase createNewKnowledgeBase(String ruleNo,String rule){  
		
		 KnowledgeBuilder builder = null;  
	     KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
		
	      if(ruleMap.containsKey(ruleNo.trim())){
	    	  builder = (KnowledgeBuilder)ruleMap.get(ruleNo.trim());
	    	  if(builder==null){
	    		  builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
	    	  }
	    	 
	    	  kBase.addKnowledgePackages(builder.getKnowledgePackages());  
			  
			  return kBase;
	      }
		  //装入规则，可以装入多个  
		  try {
			  builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			  builder.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
		  } catch (UnsupportedEncodingException e) {
			  throw new RuntimeException(e.getMessage());
			  
		  }  
		  
		  if(builder.hasErrors()){
			  throw new RuntimeException(builder.getErrors().toString());
		  }
		
		  ruleMap.put(ruleNo.trim(), builder);
		  
		  kBase.addKnowledgePackages(builder.getKnowledgePackages());  
		  
		  return kBase;
  }
	
	public static void update(Map<String,String> map){
		 KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();  
	    
		  try {
			  builder.undo();
			  builder.add(ResourceFactory.newByteArrayResource(map.get("rule").getBytes("utf-8")), ResourceType.DRL);
		  } catch (UnsupportedEncodingException e) {
			  throw new RuntimeException(e.getMessage());
			  
		  }  
		  
		  if(builder.hasErrors()){
			  throw new RuntimeException(builder.getErrors().toString());
		  }
		  
		  ruleMap.put(map.get("ruleNo").trim(), builder);
		  
	}
	
}
