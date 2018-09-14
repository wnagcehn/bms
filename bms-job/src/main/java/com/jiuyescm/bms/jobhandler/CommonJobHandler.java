package com.jiuyescm.bms.jobhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.jiuyescm.bms.common.BillingCallable;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;


public abstract class CommonJobHandler<T,F> extends IJobHandler{
	
	@Autowired public ThreadPoolTaskExecutor threadPoolTaskExecutor;  
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		
		return CalcJob(params);
	}
	//合同在线启动开关 true-开启  false-关闭(不考虑合同在线）
	public boolean contract_switch = false;
	
	/**
	 * 计费科目列表
	 */
	public String[] subjects = null;
	
	
	/**
	 * 当前需要计算的科目
	 */
	public String SubjectId = null;
	

	/**
	 * 数据查询
	 * @param map
	 * @return
	 */
	protected abstract List<T> queryBillList(Map<String,Object> map);
	
	/**
	 * 初始化 业务数据需要计算的费用科目
	 * @return
	 */
	protected abstract String[] initSubjects();
	
	public abstract Integer deleteFeesBatch(List<T> list);
	
	/**
	 * 初始化费用对象  此阶段可以获取到 费用的计算参数 如：计费重量，计费数量，计费物流商等等
	 * @param t
	 * @return
	 */
	public abstract F initFeeEntity(T t);
	
	/**
	 * 是否参与科目费用计算
	 * @param t
	 * @return
	 * false-不参与费用计算，费用表无需写入
	 * true-参与费用计算，无论是否计算成功，都要写入费用表
	 */
	public abstract boolean isJoin(T t);
	
	/**
	 * 是否计算费用 此阶段应在 验证合同之前就执行 （应用场景：有些特殊单据无论有无合同，均不计算费用）
	 * @param t 业务数据对象
	 * @param f 费用数据对象
	 * @return true-不计算费用   false-计算费用
	 */
	public abstract boolean isNoExe(T t,F f);
	
	/**
	 * 获取合同信息
	 * @param t 业务数据对象
	 * @return contract-合同信息在合同在线维护  bms-合同信息在bms维护
	 */
	public abstract ContractQuoteInfoVo getContractForWhat(T t);
	
	/**
	 * bms逻辑计算
	 * @param t
	 * @param f
	 */
	public abstract void calcuForBms(T t,F f);
	
	
	public abstract void calcu(T t,F f);
	
	/**
	 * 批量更新
	 * @param ts
	 * @param fs
	 */
	public abstract void updateBatch(List<T> ts,List<F> fs);

	
	protected ReturnT<String> CalcJob(String[] params){
		long btime= System.currentTimeMillis();// 系统开始时间
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		int num = 100;
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
		    try {
		    	map = JobParameterHandler.handler(params);//处理定时任务参数
		    	if(map.containsKey("isCalcuContract")){
		    		contract_switch = true;
		    	}
		    } catch (Exception e) {
	            current = System.currentTimeMillis();
	            XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ (current - btime) + "毫秒");
	            return ReturnT.FAIL;
	        }
		}else {
			map.put("num", num);//单次执行最多计算多少入库单
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证执行参数耗时【{0}】毫秒",(current - btime));
		btime= System.currentTimeMillis();
		
		/*业务数据执行*/
		btime= System.currentTimeMillis();// 操作开始时间
		List<T> billList=null;
		try{
			billList=queryBillList(map);
		}catch(Exception e){
			current = System.currentTimeMillis();
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0} ,耗时： {1}毫秒",e.getMessage(),((current - btime)));
			return ReturnT.FAIL;
		}
		if (billList == null || billList.size()==0) {
			XxlJobLogger.log("未查询到任何业务数据");
			return ReturnT.SUCCESS;// 出现异常直接终止
		}
		
		//List<List<T>> pageT=ListTool.splitList(billList, 200);//200个数据一组分页
		boolean flag=true;
		subjects = initSubjects();
		for (String subject_code : subjects) {
			SubjectId = subject_code;
			try{
				flag = threadProcessBilling(billList,this);
			}
			catch(Exception ex){
				XxlJobLogger.log("程序异常{0}",ex);
				return  ReturnT.FAIL;
			}
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("总耗时：【{0}】毫秒  ",(current - start));
		if(flag){
			XxlJobLogger.log("==================================执行结束:成功==============================================");
			return  ReturnT.SUCCESS;
		}else{
			XxlJobLogger.log("==================================执行结束:失败==============================================");
			return  ReturnT.FAIL;
		}
	}
	
	/**
	 * 启动多线程执行: 数据校验，数据计算，入库保存
	 * @param billList
	 * @param calcJob
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean threadProcessBilling(List<T> billList,CommonJobHandler<T,F> calcJob) throws InterruptedException, ExecutionException{  

		//接收集合各段的 执行的返回结果 
	    List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();  
	    //集合总条数
	    int size = billList.size();  
	    //将集合切分的线程数
	    int sunSum = 10;
	    int listStart,listEnd;  
	    //当总条数不足10条时 用总条数 当做线程切分值
	    if(sunSum > size){  
	        sunSum = 1;  
	    }  
	    //定义子线程
	    BillingCallable<T,F> billingCallable ;  
	    boolean ret = true;
	    //将list切分10份 多线程执行
	    for (int i = 0; i < sunSum; i++) {  
	    	//计算切割  开始和结束
	        listStart = size / sunSum * i ;  
	        listEnd = size / sunSum * ( i + 1 );  
	        //最后一段线程会 出现与其他线程不等的情况
	        if(i == sunSum - 1){  
	            listEnd = size;  
	        }  
	        //线程切断
	        List<T> sunList = billList.subList(listStart,listEnd);   
	        billingCallable = new BillingCallable<T,F>(i,sunList, calcJob);  
	        //多线程执行
	        futureList.add(threadPoolTaskExecutor.submit(billingCallable));  
	    }  
	    //对各个线程段结果进行解析
	    for(Future<Boolean> future : futureList){  
	        if(null != future && future.get()){  
	        	XxlJobLogger.log("执行成功");
	        }else{  
	        	XxlJobLogger.log("执行失败");
	        	ret = false;
	        }  
	    }
	    return ret;
	}
	
}
