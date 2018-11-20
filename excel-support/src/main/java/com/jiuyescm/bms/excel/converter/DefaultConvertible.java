package com.jiuyescm.bms.excel.converter;

/**
 * 抽象默认转换器, 实现了{@link WriteConvertible} 与 {@link ReadConvertible}接口
 */
public class DefaultConvertible implements WriteConvertible, ReadConvertible {

    public Object execRead(String object) {
        throw new UnsupportedOperationException();
    }

    public Object execWrite(Object object) {
        throw new UnsupportedOperationException();
    }
}
