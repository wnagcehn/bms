package com.jiuyescm.common.utils.excel;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IFileReader {
	public List<Map<String, String>> getFileContent(String fileName) throws Exception;
	public List<Map<String, String>> getExcelTitle(InputStream is) throws Exception;
	public List<Map<String, String>> getFileContent(InputStream is) throws Exception;
}
