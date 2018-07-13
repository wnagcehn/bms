package com.jiuyescm.oms.api.test.qm;
import java.sql.SQLException;
import java.util.concurrent.*;   
import java.util.Date;
import java.util.List;  
import java.util.ArrayList;

import com.jiuyescm.oms.api.test.common.ConnUtils;

public class Test {  
	
	
	public static void main(String[] args) throws ExecutionException,InterruptedException {  
		
		//entryorder.create 77
		//returnorder.create 4
		//stockout.create 46
		//deliveryorder.create 12970
		
		String customerId = "sgh";
		String method = "singleitem.synchronize"; 
		int startRow = 0;
	    int totalRow = 10000;
	    int everyoneRun = 20;
	    try {
			ConnUtils.conn();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	    List<ParamEntity> listParam = new ArrayList<ParamEntity>();
	    for(int start=startRow;start < totalRow ; start+=everyoneRun){
	    	int end = (start+everyoneRun) > totalRow ? totalRow : (start+everyoneRun);
		    ParamEntity param = new ParamEntity(start,end);
		    listParam.add(param);
	    } 
	    ExecutorService pool = Executors.newFixedThreadPool(listParam.size());   // 创建一个线程池    
	    List<MyCallable> listCall = new ArrayList<MyCallable>(); 
	    
	    Date useDates = new Date(System.currentTimeMillis()); 
	    for (int i = 0; i < listParam.size(); i++) {  
	    	MyCallable c = new MyCallable(method,customerId,listParam.get(i).getStartIndex(),listParam.get(i).getEndIndex());  
	    	listCall.add(c); 
	    }   
	    List<Future<Object>> list = pool.invokeAll(listCall); 
	    Date useDatee = new Date(System.currentTimeMillis()); 
	    System.out.println("--- 完成----启用线程数:"+listCall.size());    
	    System.out.println("--- 开始时间----"+useDates.toLocaleString());
	    System.out.println("--- 结束时间----"+useDatee.toLocaleString());
	   
	    ConnUtils.close();
	    // 关闭线程池  
	    pool.shutdown();  
	}  
}  
  
class MyCallable implements Callable<Object> {  
	private String method;
	private String customerId;
	private int startIndex;
	private int endIndex;
  
	MyCallable(String method,String customerId,int startIndex,int endIndex) {  
		this.method = method;
		this.customerId = customerId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}  
	
	public Object call() throws Exception {  
		QimenTestReceiveRunsUtils runUtils = new QimenTestReceiveRunsUtils();
		runUtils.run(method, customerId, startIndex, endIndex);
		return null;  
	}   
}  
class ParamEntity{ 
	
	private int startIndex;
	private int endIndex;
	
	public ParamEntity() {
		super(); 
	}
	public ParamEntity(int startIndex, int endIndex) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
}