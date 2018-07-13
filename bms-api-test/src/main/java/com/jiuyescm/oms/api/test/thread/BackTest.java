package com.jiuyescm.oms.api.test.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future; 

import com.jiuyescm.oms.api.test.common.ConnUtils;  

public class BackTest {
	
	public static void main(String[] args) throws ExecutionException,InterruptedException {  
		 
		 
	    ExecutorService pool = Executors.newFixedThreadPool(10);   // 创建一个线程池    
	    List<MyCallable> listCall = new ArrayList<MyCallable>();  
	    Date useDates = new Date(System.currentTimeMillis()); 
	    for (int i = 0; i < 20; i++) {  
	    	MyCallable c = new MyCallable(i+"");
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
		/*
		String oldRemark="[上海仓#SH01#1223]";
		boolean warehouseSuccess=true;
		//remark 必填，必须以[]包裹，已#拆分，必须能拆出两个以上，取第二个位仓库编码。
		if(!oldRemark.startsWith("[") || !oldRemark.endsWith("]") 
				|| oldRemark.indexOf("#") <= 0){
			warehouseSuccess = false;
		}else{//去除中括号，已#拆分；
			String remark =  oldRemark.substring(1, oldRemark.length()-1);
			String[] spliRemark = remark.split("#");
			if(spliRemark.length <=1){
				warehouseSuccess = false;
			}else{
				System.out.println(spliRemark[1]);
			}
		}
		if(!warehouseSuccess){
			System.out.println("尚展客户，仓库数据异常"); 
		} 
		*/
	}  

}
class MyCallable implements Callable<Object> {  
	private String method; 
  
	MyCallable(String method) {  
		this.method = method; 
	}  
	
	public Object call() throws Exception {  
		System.out.println("执行："+method);
		Thread.sleep(1000);
		System.out.println("结束："+method);
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