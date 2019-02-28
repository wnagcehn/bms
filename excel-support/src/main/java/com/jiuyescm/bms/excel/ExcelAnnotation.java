package com.jiuyescm.bms.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author liuzhicheng
 *
 */
public class ExcelAnnotation {
    /**
     * 注解类
     * 
     *
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelClass {
        String uri();

        String desc();
    }

    /**
     * 构造方法注解
     *
     */
    @Target(ElementType.CONSTRUCTOR)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelConstructor {
        String uri();

        String desc();
    }

    /**
     * 方法注解
     *
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelMethod {
        String uri();

        String desc();
    }

    /**
     * 字段注解
     * 
     *
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelField {
        int num();

        String title();
    }

    /**
     * 可以同时应用到类和方法上
     * 
     *
     */
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelClassAndMethod {
        // 定义枚举
        public enum EnumType {
            util, entity, service, model
        }

        // 设置默认值
        public EnumType classType() default EnumType.util;

        // 数组
        int[] arr() default { 3, 7, 5 };

        String color() default "blue";
    }
}