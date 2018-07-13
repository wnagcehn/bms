package com.jiuyescm.bms.common.web;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.web.DoradoContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * 导入文件比较公共方法
 * @author Wuliangfeng
 * T 源数据实体 U 导入数据实体
 *
 */
public abstract class HttpCommanImportCompare<T,U> {

	protected abstract List<T> getOrgList(Map<String,Object> parameter);
	/**
	 * 设置导入excel 模板 实体 
	 * @return
	 */
	protected abstract BaseDataType getBaseDataType();
	/**
	 * 设置主键列 唯一性
	 * @return
	 */
	protected abstract List<String> getKeyDataProperty();
	/**
	 * 设置不用比较的列
	 * @return
	 */
	protected abstract List<String> getNoCompareProperty();

	private String objToString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	/**
	 * 比较线上线下数据，判断重复值(返回Excel的行号)
	 * @param listOrgT
	 * @param listimportU
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> compareWithImportLineData(List<T> listOrgT,List<U> listimportU,List<ErrorMessageVo> infoList, Map<String, Object> map){
		//如果属于此类型
		List<String> listKeyProperty=getKeyDataProperty();
		
		List<Map<String, Object>> listOrgMap=getKeysAndValuesT(listOrgT);
		List<Map<String, Object>> listImportMap=getKeysAndValuesU(listimportU);	
		int lineNo=1;
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
						setMessage(infoList, lineNo,"Excel中第"+lineNo+"行数据重复");
						break;
					}
				}	
			}
		}
		int lineNextNo=1;
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
					setMessage(infoList, lineNextNo,"与表中数据重复");
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
	/**
	 * 比较得出 不一致结果
	 * @param listOrgT
	 * @param listimportU
	 * @return
	 */
	protected List<Map<String,Object>> compareWithDiffImportData(List<T> listOrgT,List<U> listimportU){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<String> listKeyProperty=getKeyDataProperty();
		List<String> listNoCompareProperty=getNoCompareProperty();
		List<Map<String, Object>> listOrgMap=getKeysAndValuesT(listOrgT);
		List<Map<String, Object>> listImportMap=getKeysAndValuesU(listimportU);
		BaseDataType bs=getBaseDataType();
		for(Map<String, Object> importMap:listImportMap){
			boolean matchFlag=false;
			for(Map<String, Object> orgMap:listOrgMap){
				boolean f=true;
				for(String key:listKeyProperty){			
					if(!objToString(orgMap.get(key)).equals(objToString(importMap.get(key)))){
						f=false;
						break;
					}
				}
				if(f){//匹配上 属性值全部相同	
					matchFlag=true;
					boolean isSame=true;//数据是否全部一致
					//比较各个属性值
					for(DataProperty property:bs.dataProps){
						if(listKeyProperty.contains(property.getPropertyId())){
							continue;
						}
					    if(listNoCompareProperty!=null&&listNoCompareProperty.size()>0){
					    	if(listNoCompareProperty.contains(property.getPropertyId())){
					    		continue;
					    	}
					    }
						if(!objToString(importMap.get(property.getPropertyId())).equals(objToString(orgMap.get(property.getPropertyId())))){
							isSame=false;
							break;
						}
					}
					Map<String,Object> map=null;
					if(isSame){//属性值全部一致
						/*
						map=new HashMap<String,Object>();
						map.put("status","一致");
						for(DataProperty property:bs.dataProps){
							map.put(property.getPropertyId(), importMap.get(property.getPropertyId()));
						}
						list.add(map);*/
					}else{//匹配上 数据不一致
						map=new HashMap<String,Object>();
						map.put("status","线上");
						for(DataProperty property:bs.dataProps){
							map.put(property.getPropertyId(), orgMap.get(property.getPropertyId()));
						}
						list.add(map);
						map=new HashMap<String,Object>();
						map.put("status","线下");
						for(DataProperty property:bs.dataProps){
							map.put(property.getPropertyId(), importMap.get(property.getPropertyId()));
						}
						list.add(map);
					}
					break;
				}
			}
			if(!matchFlag){//线上无
				Map<String,Object> map=null;
				map=new HashMap<String,Object>();
				map.put("status","线上无数据");
				for(DataProperty property:bs.dataProps){
					map.put(property.getPropertyId(), importMap.get(property.getPropertyId()));
				}
				list.add(map);
			}
		}
		//获取系统中存在 导入excel 中没有的记录
		for(Map<String, Object> orgMap:listOrgMap){
			boolean matchFlag=false;
			for(Map<String, Object> importMap:listImportMap){
				boolean f=true;
				for(String key:listKeyProperty){			
					if(!objToString(orgMap.get(key)).equals(objToString(importMap.get(key)))){
						f=false;
						break;
					}
				}
				if(f){
					matchFlag=true;
					break;
				}
			}
			if(!matchFlag){//线下 无
				Map<String,Object> map=null;
				map=new HashMap<String,Object>();
				map.put("status","线下无数据");
				for(DataProperty property:bs.dataProps){
					map.put(property.getPropertyId(), orgMap.get(property.getPropertyId()));
				}
				list.add(map);
			}
		}
		return list;
	}
	protected List<Map<String,Object>> compareWithImportData(List<T> listOrgT,List<U> listimportU) throws Exception
	{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<String> listKeyProperty=getKeyDataProperty();
		List<String> listNoCompareProperty=getNoCompareProperty();
		List<Map<String, Object>> listOrgMap=getKeysAndValuesT(listOrgT);
		List<Map<String, Object>> listImportMap=getKeysAndValuesU(listimportU);
		BaseDataType bs=getBaseDataType();
		for(Map<String, Object> importMap:listImportMap){
			boolean matchFlag=false;
			for(Map<String, Object> orgMap:listOrgMap){
				boolean f=true;
				for(String key:listKeyProperty){			
					if(!objToString(orgMap.get(key)).equals(objToString(importMap.get(key)))){
						f=false;
						break;
					}
				}
				if(f){//匹配上 属性值全部相同	
					matchFlag=true;
					boolean isSame=true;//数据是否全部一致
					//比较各个属性值
					for(DataProperty property:bs.dataProps){
						if(listKeyProperty.contains(property.getPropertyId())){
							continue;
						}
					    if(listNoCompareProperty!=null&&listNoCompareProperty.size()>0){
					    	if(listNoCompareProperty.contains(property.getPropertyId())){
					    		continue;
					    	}
					    }
						if(!objToString(importMap.get(property.getPropertyId())).equals(objToString(orgMap.get(property.getPropertyId())))){
							isSame=false;
							break;
						}
					}
					Map<String,Object> map=null;
					if(isSame){//属性值全部一致
						map=new HashMap<String,Object>();
						map.put("status","一致");
						for(DataProperty property:bs.dataProps){
							map.put(property.getPropertyId(), importMap.get(property.getPropertyId()));
						}
						list.add(map);
					}else{//匹配上 数据不一致
						map=new HashMap<String,Object>();
						map.put("status","线上");
						for(DataProperty property:bs.dataProps){
							map.put(property.getPropertyId(), orgMap.get(property.getPropertyId()));
						}
						list.add(map);
						map=new HashMap<String,Object>();
						map.put("status","线下");
						for(DataProperty property:bs.dataProps){
							map.put(property.getPropertyId(), importMap.get(property.getPropertyId()));
						}
						list.add(map);
					}
					break;
				}
			}
			if(!matchFlag){//线上无
				Map<String,Object> map=null;
				map=new HashMap<String,Object>();
				map.put("status","线上无数据");
				for(DataProperty property:bs.dataProps){
					map.put(property.getPropertyId(), importMap.get(property.getPropertyId()));
				}
				list.add(map);
			}
		}
		//获取系统中存在 导入excel 中没有的记录
		for(Map<String, Object> orgMap:listOrgMap){
			boolean matchFlag=false;
			for(Map<String, Object> importMap:listImportMap){
				boolean f=true;
				for(String key:listKeyProperty){			
					if(!objToString(orgMap.get(key)).equals(objToString(importMap.get(key)))){
						f=false;
						break;
					}
				}
				if(f){
					matchFlag=true;
					break;
				}
			}
			if(!matchFlag){//线下 无
				Map<String,Object> map=null;
				map=new HashMap<String,Object>();
				map.put("status","线下无数据");
				for(DataProperty property:bs.dataProps){
					map.put(property.getPropertyId(), orgMap.get(property.getPropertyId()));
				}
				list.add(map);
			}
		}
		return list;
	}
	
	private String getKey(Map<String, Object> orgMap,List<String> listKeyProperty){
		String key="";
		for(String s:listKeyProperty){
			if(!StringUtils.isBlank(objToString(orgMap.get(s)))){
				key+=orgMap.get(s)+"-";
			}
		}
		key=key.substring(0,key.lastIndexOf("-"));
		return key;
	}
	private String getName(String Id,BaseDataType bs){
		List<DataProperty> props = bs.getDataProps();
		String name="";
		for(DataProperty data:props){
			if(data.getPropertyId().equals(Id)){
				name=data.getPropertyName();
				break;
			}
		}
		return name;
	}
	private List<Map<String, Object>> getKeysAndValuesU(List<U> listOrgT) {
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
	                    e.printStackTrace();
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	            }
	            list.add(listChild);// 将map加入到list集合中
	        }
	        return list;
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
	                    e.printStackTrace();
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	            }
	            list.add(listChild);// 将map加入到list集合中
	        }
	        return list;
	}
	/**
	 * 对比list  返回不一致结果
	 * @param file
	 * @param parameter
	 * @return
	 */
	public Map<String,Object> importFileDiff(UploadFile file,Map<String,Object> parameter){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			//List<ImportDataCompareVoEntity> list=new ArrayList<ImportDataCompareVoEntity>();
			BaseDataType bs=getBaseDataType();
			// 检查导入模板是否正确
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1);//验证模板
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);	
			if (!isTemplate) {
				map.put("error", "Excel导入格式错误请参考标准模板检查!");
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 2);//读取Excel
			List<U> listimportU=readExcel(file,bs);
			if(listimportU==null||listimportU.size()==0){
				map.put("error", "导入数据为空");
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 3);//读取Data
			List<T> listOrgT=getOrgList(parameter);
			if(listOrgT==null||listOrgT.size()==0){
				map.put("error", "无业务数据，无法比较");
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 4);//开始比较数据
			List<Map<String,Object>> list=compareWithDiffImportData(listOrgT,listimportU);//
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 5);//保存结束
			if(list!=null&&list.size()>0){
				map.put("succ", list);
			}else{
				map.put("error", "比对数据一致");
			}
			return map;
		}catch(Exception e){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
			map.put("error", e.getMessage());
			return map;
		}
	}
	/**
	 * 导入比较文件基类方法
	 * 返回map error 为提示信息
	 * succ 为Grid 信息
	 */
	public Map<String,Object> importFile(UploadFile file,Map<String,Object> parameter){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			//List<ImportDataCompareVoEntity> list=new ArrayList<ImportDataCompareVoEntity>();
			BaseDataType bs=getBaseDataType();
			// 检查导入模板是否正确
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1);//验证模板
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);	
			if (!isTemplate) {
				map.put("error", "Excel导入格式错误请参考标准模板检查!");
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 2);//读取Excel
			List<U> listimportU=readExcel(file,getBaseDataType());
			if(listimportU==null||listimportU.size()==0){
				map.put("error", "导入数据为空");
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 3);//读取Data
			List<T> listOrgT=getOrgList(parameter);
			if(listOrgT==null||listOrgT.size()==0){
				map.put("error", "无报价信息，无法比较");
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 4);//开始比较数据
			List<Map<String,Object>> list=compareWithImportData(listOrgT,listimportU);//
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 5);//保存结束
			if(list!=null&&list.size()>0){
				map.put("succ", list);
			}else{
				map.put("error", "比对数据一致");
			}
			return map;
		}catch(Exception e){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
			map.put("error", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-读取Data 4-开始比较数据  5-比较结束6-异常结束
	 * @return
	 */
	protected int getProgressStatus(){
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
	/**
	 *重置处理进度
	 */
	protected void setProgressStatus() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
	/**
	 * 泛型 读取Excel
	 * @param file
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	private List<U> readExcel(
			UploadFile file, BaseDataType bs) throws Exception {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<U> modelList = Lists.newArrayList();	
			Class<U> entityClass = (Class<U>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
			for (Map<String, String> data : datas) {
				U p = (U) BeanToMapUtil.convertMapNull(entityClass, data);
				modelList.add(p);
			}
			return modelList;
		} catch (Exception e) {
			throw new Exception("读取Excel异常："+e);
		}
	}
	
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
}
