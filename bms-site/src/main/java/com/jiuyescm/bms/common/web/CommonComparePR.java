package com.jiuyescm.bms.common.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.common.ConstantInterface;
/**
 * 公共比较两个List
 * @author Wuliangfeng
 *
 * @param <T>
 */
public class CommonComparePR <T>{
    
    private static final Logger logger = Logger.getLogger(CommonComparePR.class.getName());

	/**
	 * 比较线上线下数据，判断重复值(返回Excel的行号)
	 * @param listOrgT
	 * @param listimportU
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> compareWithImportLineData(List<T> listOrgT,List<T> listimport,List<ErrorMessageVo> infoList,List<String> listKey, Map<String, Object> map){
		//如果属于此类型
		List<String> listKeyProperty=listKey;	
		List<Map<String, Object>> listOrgMap=getKeysAndValuesT(listOrgT);
		List<Map<String, Object>> listImportMap=getKeysAndValuesT(listimport);
		int lineNo=1;
		if(listimport.get(0) instanceof PriceTransportLineEntity){
			lineNo=2;
		}	
		for(int i=0;i<listImportMap.size();i++){
			lineNo+=1;
			Map<String, Object> importValueMap=listImportMap.get(i);
			String importValue1="";
			for(String key:listKeyProperty){
				importValue1+=objToString(importValueMap.get(key));
			}
			for(int j=0;j<listImportMap.size();j++){
				if(i!=j){
					Map<String, Object> importMap2=listImportMap.get(j);
					String importValue2="";
					for(String key:listKeyProperty){
						importValue2+=objToString(importMap2.get(key));
					}
					if(importValue1.equals(importValue2)){
						if(listimport.get(0) instanceof PriceMaterialQuotationEntity){
							setMessage(infoList, Integer.valueOf(importMap2.get("line").toString()),"Excel中第"+Integer.valueOf(importMap2.get("line").toString())+"行数据重复");
						}else {
							setMessage(infoList, lineNo,"Excel中第"+lineNo+"行数据重复");	
						}
						break;
					}
				}	
			}
		}
		int lineNextNo=1;
		if(listimport.get(0) instanceof PriceTransportLineEntity){
			lineNextNo=2;
		}
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
			
		//开始判断导入数据和数据库中是否有重复数据
		for(Map<String, Object> importMap:listImportMap){
			//拼装数据
			lineNextNo+=1;
			String importValue="";
			for(String key:listKeyProperty){
				importValue+=objToString(importMap.get(key));
			}
			for(Map<String, Object> orgMap:listOrgMap){
				
				String orgValueString="";
				for(String key:listKeyProperty){
					//不相等就返回
					orgValueString+=objToString(orgMap.get(key));
				}
				if(importValue.equals(orgValueString)){
					if(listimport.get(0) instanceof PriceMaterialQuotationEntity){
						setMessage(infoList, Integer.valueOf(orgMap.get("line").toString()),"与表中数据重复");
					}else {
						setMessage(infoList, lineNextNo,"与表中数据重复");
					}
				}
			}
		}
		
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, listImportMap); // 无基本错误信息
		}
		
		return map;
	}
	
	private List<Map<String, Object>> getKeysAndValuesT(List<T> listOrgT) {
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	        for (Object obj : listOrgT) {
	            Class userCla;
	            // 得到类对象
	            userCla = (Class) obj.getClass();
	            /* 得到类中的所有属性集合 */
	            Field[] fs = userCla.getDeclaredFields();
	            Map<String, Object> listChild = new HashMap<String, Object>();
	            for (int i = 0; i < fs.length; i++) {
	                Field f = fs[i];
	                f.setAccessible(true); // 设置些属性是可以访问的
	                Object val = new Object();
	                try {
	                    val = f.get(obj);
	                    // 得到此属性的值
	                    listChild.put(f.getName(), val);// 设置键值
	                } catch (IllegalArgumentException e) {
	                    logger.error(e);
	                } catch (IllegalAccessException e) {
	                    logger.error(e);
	                }
	            }
	            list.add(listChild);// 将map加入到list集合中
	        }
	        return list;
	}
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	private String objToString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
}
