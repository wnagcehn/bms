package com.jiuyescm.bms.excel.data;

public class DataColumn {
	
	private int colNo; 			//单元格索引值
	private String colName;		//单元格列值
	private String titleName;	//单元格列名称
	private String colValue;	//单元格值
	
	public DataColumn(){
		
	}
	
	/**
	 * 初始化单元格信息
	 * @param colName 单元格列值 A B C AA...
	 * @param fieldName 列名  姓名 年龄 ...
	 * @param colValue 单元格内容  张三 29..
	 */
	public DataColumn(String colName,String titleName,String colValue){
		this.colName = colName;
		this.titleName = titleName;
		this.colValue = colValue;
		this.colNo = titleToNumber(colName);
	}
	
	/**
	 * 
	 * @param colNo 列索引
	 * @param colValue 列值
	 */
	public DataColumn(int colNo,String colValue){
		this.colNo = colNo;
		this.colValue = colValue;
	}
	
	public DataColumn(String colName,String colValue){
		this.colName = colName;
		this.colValue = colValue;
	}

	/**
	 * 列索引
	 */
	public int getColNo() {
		return colNo;
	}
	
	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	/**
	 * 列值
	 */
	public String getColValue() {
		return colValue;
	}
	
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	
	private int titleToNumber(String s) {
        if(s.length()==1){
            return s.charAt(0)-'A'+1;
        }else{
            int a = s.length();
            int sum = 0;
            for(int i=0;i<a;i++){
                if(i==a-1){
                    sum = sum+(s.charAt(i)-'A')+1;
                }else{
                    sum = sum+(int)Math.pow(26,a-i-1)*(s.charAt(i)-'A'+1);
                }

            }
            return sum;
        }

    }
	
}
