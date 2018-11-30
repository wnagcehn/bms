package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.bms.excel.utils.DateUtils;

public class ExcelReaderTest {

	private static Logger logger = LoggerFactory.getLogger(ExcelReaderTest.class);

	@Test
	public void test2() throws FileNotFoundException {
		String path = "d:\\456.xlsx";
		File file = new File(path);
		FileInputStream inputStream = new FileInputStream(file);
		ExcelXlsxReader er;
		try {
			er = new ExcelXlsxReader(inputStream);
			List<OpcSheet> sheets = er.getSheets();
			System.out.println(sheets.size());
			er.readRow(1, new SheetReadCallBack() {
				@Override
				public void read(DataRow dr) {
					System.out.println("----行号【" + dr.getRowNo() + "】");
					for (DataColumn dc : dr.getColumns()) {

						System.out.println("列名【" + dc.getColName() + "】|值【"
								+ dc.getColValue() + "】");
					}
				}
				@Override
				public void finish() {
					System.out.println("读取完毕");
				}
			},1,2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
