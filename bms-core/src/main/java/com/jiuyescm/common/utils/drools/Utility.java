package com.jiuyescm.common.utils.drools;

import org.apache.log4j.Logger;
import org.drools.core.spi.KnowledgeHelper;


public class Utility {
	
   private static final Logger logger = Logger.getLogger(Utility.class.getName());

   public static void help(final KnowledgeHelper drools, final String message){
	   logger.info(message);
	   logger.info(String.format("rule triggered: %s" , drools.getRule().getName()));
   }
   
   public static void helper(final KnowledgeHelper drools){
	   logger.info(String.format("rule triggered: %s" , drools.getRule().getName()));
   }
   
   public static void print(String str){
		System.out.println(str);
   }
   
}