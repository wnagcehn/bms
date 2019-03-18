package com.jiuyescm.bms.excel.write;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.jiuyescm.bms.excel.exception.ExcelHandlerException;

public interface IExcelExporter {

	/**
	 * 保存excel文件
	 * @param filePath 指定的文件路径
	 * @param fileName 文件名称
	 * @return
	 */
	public String saveFile(String filePath,String fileName) throws IOException;
	
	/**
	 * 保存excel文件
	 * @param fileName 文件名称
	 * @return
	 */
	public String saveFile(String fileName) throws IOException;
	
	/**
	 * 删除临时文件
	 */
	public void delTempFile();
	
	/**
	 * 创建 sheet
	 * @param sheetName  名称（不可重复）
	 * @param startIndex 内容开始行
	 * @param headInfoList 内容map
	 * @return
	 * @throws ExcelHandlerException
	 */
	public Sheet createSheet(String sheetName,int startIndex,List<Map<String, Object>> headInfoList) throws ExcelHandlerException;
	
	/**
     * 写入表头信息
     * @param hssfSheet
     * @param headInfoList List<Map<String, Object>>
     *              key: title         列标题
     *                   columnWidth   列宽
     *                   dataKey       列对应的 dataList item key
     */
	public void writeContent(Sheet sheet, List<Map<String, Object>> dataList);
	
	public void setvMergeColumn(Sheet sheet,String columns);
	
	/**
     * 注解类创建 sheet
     */
	public Sheet createSheetByAnno(String sheetName,int startIndex,Class clazz) throws ExcelHandlerException;
	
	/**
	 * 注解类写入内容
	 */
    public void writeContentByAnno(Sheet sheet, List list);
	
}
