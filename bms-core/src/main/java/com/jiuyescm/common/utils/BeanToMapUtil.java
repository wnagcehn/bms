package com.jiuyescm.common.utils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

public class BeanToMapUtil {
   
	 private static final Logger logger = Logger.getLogger(BeanToMapUtil.class.getName());

	 private BeanToMapUtil() {
	  
	 }
	
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param <T>
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
            	try {
            		 Object value = map.get(propertyName);
                     Object[] args = new Object[1];
                     Class cls=descriptor.getPropertyType();
                     if(cls.equals(Date.class)){
                         if(value!=null){
                        	 String[] dataPatterns=new String[]{
                        			 "yyyy-MM-dd hh:mm:ss",
                        			 "yyyy-MM-dd",
                        			 "dd-MM月 -yy"
                        	 };
                        	 args[0]=DateUtils.parseDate(value.toString(), dataPatterns);
                         }
                     }else if(cls.equals(String.class)){
                    	 
                    	 args[0]=value==null?"":value;
		             }else if(cls.equals(BigDecimal.class)){
		            	 BigDecimal   b1   =   new   BigDecimal(0);
		               	 args[0]= value == null ? b1:value;
		             }else if (cls.equals(double.class)){  	 
		            	 args[0]= value == null ? 0 :Double.valueOf(value.toString());
		             }else{
		            	 
                    	 value=value==null?0:value;
                    	 try {
                    		 Object propObj=cls.getMethod("valueOf", String.class).invoke(cls, value);
                    		 args[0] = propObj;
                    	 } catch (Exception e) {
                    		 args[0] = Double.valueOf(value.toString()).intValue();
                    	 }
                    	
                     }
                     descriptor.getWriteMethod().invoke(obj, args);
				} catch (Exception e) {
					//e.printStackTrace();
					logger.info(e);
				}
            }
        }
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!("class").equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
    
    

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param <T>
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object convertMapNull(Class type, Map map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				try {
					Object value = map.get(propertyName);
					Object[] args = new Object[1];
					Class cls = descriptor.getPropertyType();
					if (cls.equals(Date.class)) {
						if (value != null && value!="") {
							String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy" };
							args[0] = DateUtils.parseDate(value.toString(), dataPatterns);
						}
					} else if (cls.equals(String.class)) {

						args[0] = value == null ? "" : value;
					} else if (cls.equals(BigDecimal.class)) {
						// BigDecimal b1 = new BigDecimal(0);
						if(value!=""){
							args[0] = value == null ? value : new BigDecimal(value.toString());
						}
					} else if (cls.equals(Double.class)) {
						args[0] = value == null ? value : Double.valueOf(value.toString());
					} else if (cls.equals(Integer.class)) {
						args[0] = value == null ? value : Integer.valueOf(value.toString().substring(0, value.toString().indexOf(".")));
					} else if(cls.equals(java.sql.Timestamp.class)){
						try {
							args[0] = value == null ? null: java.sql.Timestamp.valueOf(value.toString());
						} catch (Exception e) {
							String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
							Date date = DateUtils.parseDate(value.toString().trim(), dataPatterns);
							args[0] = new java.sql.Timestamp(date.getTime());
							logger.info(e);
						}
						
					} else if(cls.equals(Boolean.class) || cls.equals(boolean.class)){
						args[0] = value == null ? null: String.valueOf(value);
						if(StringUtils.isNotBlank(String.valueOf(value))){
							if(StringUtils.equalsIgnoreCase(String.valueOf(value), "Y")){
								args[0] = true;
							}else{
								args[0] = false;
							}
						}
					}else {
						value = value == null ? 0 : value;
						try {
							Object propObj = cls.getMethod("valueOf", String.class).invoke(cls, value);
							args[0] = propObj;
						} catch (Exception e) {
							logger.info(e);
							args[0] = Double.valueOf(value.toString()).intValue();
						}

					}
					descriptor.getWriteMethod().invoke(obj, args);
				} catch (Exception e) {
					//e.printStackTrace();
					logger.info(e);
				}
			}
		}
		return obj;
	}
    
}
