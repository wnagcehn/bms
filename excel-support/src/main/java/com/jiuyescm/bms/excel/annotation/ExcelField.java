package com.jiuyescm.bms.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyescm.bms.excel.converter.DefaultConvertible;
import com.jiuyescm.bms.excel.converter.ReadConvertible;
import com.jiuyescm.bms.excel.converter.WriteConvertible;

/**
 * 功能说明: 用来在对象的属性上加入的annotation，通过该annotation说明某个属性所对应的标题
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * 属性的标题名称
     *
     * @return 表头名
     */
    String title();

    /**
     * 写数据转换器
     *
     * @return 写入Excel数据转换器
     * @see WriteConvertible
     */
    Class<? extends WriteConvertible> writeConverter()
            default DefaultConvertible.class;

    /**
     * 读数据转换器
     *
     * @return 读取Excel数据转换器
     * @see ReadConvertible
     */
    Class<? extends ReadConvertible> readConverter()
            default DefaultConvertible.class;

    /**
     * 在excel的顺序
     *
     * @return 列表顺序
     */
    int order() default Integer.MAX_VALUE;
}
