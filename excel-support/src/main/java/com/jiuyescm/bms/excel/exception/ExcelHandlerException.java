package com.jiuyescm.bms.excel.exception;

public class ExcelHandlerException extends Exception {

	private static final long serialVersionUID = 3714839745700589059L;
	
	public ExcelHandlerException() {
    }
    
    public ExcelHandlerException(String code, String message) {
		super(message);
		this.code = code;
	}
    
    public ExcelHandlerException(String code, String message, Throwable throwable) {
    	super(message, throwable);
    	this.code = code;
    }

    public ExcelHandlerException(String message) {
        super(message);
    }
    
    public ExcelHandlerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ExcelHandlerException(Throwable throwable) {
        super(throwable);
    }
    
    /**
     * 错误编码 </br>
     */
    private String code = "";
	
	public String getCode() {
		return code;
	}
	

}
