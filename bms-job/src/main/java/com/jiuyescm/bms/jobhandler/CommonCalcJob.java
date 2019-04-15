package com.jiuyescm.bms.jobhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.tool.ListTool;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 公共计算费用定时器基类
 * @author Wuliangfeng
 *
 * @param <T> 业务数据 基础类型
 * @param <F> 生成费用的基础类型
 */
public abstract class CommonCalcJob<T,F> extends IJobHandler {

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		
		return CalcJob(params);
	}

	/**
	 * 查询账单列表
	 */
	protected abstract List<T> queryBillList(Map<String,Object> map);
	/**
	 * 初始化Config配置
	 * @throws Exception 
	 */
	protected abstract void initConf(List<T> billList) throws Exception;
	/**
	 * 执行dubbo
	 */
	protected abstract void calcuService(T t,List<F> feesList);
	/**
	 * 保存数据
	 */
	protected abstract void saveBatchData(List<T> billList,List<F> feesList);
	/**
	 * 验证数据
	 */
	protected abstract boolean validateData(T t,List<F> feesList);
	
	protected abstract void calcuStandardService(List<T> billList);
	
	/**
	 * 执行方法
	 * @param params
	 * @return
	 */
	protected ReturnT<String> CalcJob(String[] params){
		long btime= System.currentTimeMillis();// 系统开始时间
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		int num = 100;
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
		    try {
		    	map = JobParameterHandler.handler(params);//处理定时任务参数
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
		current = System.currentTimeMillis();
		XxlJobLogger.log("查询行数【{0}】   耗时【{1}】毫秒",billList.size(),(current - btime));	
		btime = System.currentTimeMillis();
		try{
			initConf(billList);//初始化配置
			current = System.currentTimeMillis();
			XxlJobLogger.log("初始化配置耗时：【{0}】毫秒 ",(current - btime));
		}catch(Exception e){
		    current = System.currentTimeMillis();
	        XxlJobLogger.log("【终止异常】,初始化配置错误,原因:" + e.getMessage() + ",耗时："+ ((current - btime)) + "毫秒");
			return  ReturnT.FAIL;
		}
		List<List<T>> pageT=ListTool.splitList(billList, 100);//100个数据一组分页
		boolean flag=true;
		for(List<T> listT:pageT){
			try{
				//费用列表
				List<F> feesList=new ArrayList<F>();
				for(T t:listT){
					btime = System.currentTimeMillis();
					if(!validateData(t,feesList)){//验证数据
						current = System.currentTimeMillis();
						XxlJobLogger.log("验证数据耗时：【{0}】毫秒",(current - btime));
						continue;
					}else{
						current = System.currentTimeMillis();
						XxlJobLogger.log("验证数据耗时：【{0}】毫秒 ",(current - btime));					
						calcuService(t,feesList);			
					}
				}
				btime = System.currentTimeMillis();
				saveBatchData(billList,feesList);
				//calcuStandardService(billList);
				current = System.currentTimeMillis();
				XxlJobLogger.log("保存数据 ：耗时【{0}】",(current - btime));
			}catch(Exception  e){
				flag=false;
				XxlJobLogger.log("【处理异常】原因:" +e);
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

}
