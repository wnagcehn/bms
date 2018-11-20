package com.jiuyescm.bms.excel.converter;

/**
 * 写入excel内容转换器
 */
public interface ReadConvertible {

    /**
     * 读取Excel列内容转换
     *
     * @param object 待转换数据
     * @return 转换完成的结果
     * @see com.jiuyescm.bms.excel.annotation.ExcelField#readConverter()
     */
    Object execRead(String object);
}
