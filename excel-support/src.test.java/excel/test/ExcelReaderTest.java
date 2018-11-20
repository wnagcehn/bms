package excel.test;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.opc.OpcSheet;

public class ExcelReaderTest {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelReaderTest.class);
	@Test
	public void test2(){
		String path = "E:\\cmap\\file\\small.xlsx";
		ExcelXlsxReader er;
		try {
			er = new ExcelXlsxReader(path);
			List<OpcSheet> sheets = er.getSheets();
			System.out.println(sheets.size());
			er.readRowToObj(1,Student.class, new SheetReadCallBack() {
				@Override
				public void read(DataRow dr) {
					System.out.println("----行号【"+dr.getRowNo()+"】");
					for (DataColumn dc : dr.getColumns()) {
						System.out.println("列名【"+dc.getColName()+"】|值【"+dc.getColValue()+"】");
					}
					try {
						Student student = dr.transToObj(Student.class);
						System.out.println(student);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void finish() {
					System.out.println("读取完毕");
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try{
			List<OpcSheet> sheets = er.readSheets(path);
			System.out.println("sheet总数："+sheets.size());
			OpcSheet opcSheet = er.readOpcSheet(1);
			System.out.println("表头信息："+opcSheet.getColValueMap());
			System.out.println(opcSheet.getContents());
		}
		catch(Exception ex){
			//System.out.println(ex);
		}*/
	}
	
}
