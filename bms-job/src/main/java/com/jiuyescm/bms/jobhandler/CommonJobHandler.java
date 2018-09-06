package com.jiuyescm.bms.jobhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.tool.ListTool;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;


public abstract class CommonJobHandler<T,F> extends IJobHandler {
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		
		return CalcJob(params);
	}
	//合同在线启动开关 true-开启  false-关闭(不考虑合同在线）
	public boolean contract_switch = false;
	
	protected ContractQuoteInfoVo contractQuoteInfoVo = null;

	/**
	 * 数据查询
	 * @param map
	 * @return
	 */
	protected abstract List<T> queryBillList(Map<String,Object> map);
	
	/**
	 * 初始化费用对象  此阶段可以获取到 费用的计算参数 如：计费重量，计费数量，计费物流商等等
	 * @param t
	 * @return
	 */
	protected abstract F initFeeEntity(T t);
	
	/**
	 * 是否计算费用 此阶段应在 验证合同之前就执行 （应用场景：有些特殊单据无论有无合同，均不计算费用）
	 * @param t 业务数据对象
	 * @param f 费用数据对象
	 * @return true-不计算费用   false-计算费用
	 */
	protected abstract boolean isNoExe(T t,F f);
	
	/**
	 * 获取合同信息
	 * @param t 业务数据对象
	 * @return contract-合同信息在合同在线维护  bms-合同信息在bms维护
	 */
	protected abstract ContractQuoteInfoVo getContractForWhat(T t);
	
	/**
	 * bms逻辑计算
	 * @param t
	 * @param f
	 */
	protected abstract void calcuForBms(T t,F f);
	
	/**
	 * 合同在线逻辑计算
	 * @param t
	 * @param f
	 * @param vo
	 */
	protected abstract void calcuForContract(T t,F f);
	
	/**
	 * 批量更新
	 * @param ts
	 * @param fs
	 */
	protected abstract void updateBatch(List<T> ts,List<F> fs);

	
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
		
		List<List<T>> pageT=ListTool.splitList(billList, 200);//200个数据一组分页
		boolean flag=true;
		for(List<T> listT:pageT){
			try{
				List<F> feesList = new ArrayList<F>();
				for (T t : listT) {
					btime= System.currentTimeMillis();// 操作开始时间
					
					F f = initFeeEntity(t); //初始化费用对象 获取计算参数，如：计费重量，计费数量，计费物流商等待
					feesList.add(f);
					//进行赋值判断前，先校验数据本身是否需要费用计算
					if(isNoExe(t,f) == true){
						continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
					}
					if(contract_switch == false){ 
						//如果合同在线开关未开启,直接走bms逻辑，不需要去合同在线查找合同；
						calcuForBms(t, f);
					}
					else{
						//如果合同在线开关开启，需要先去合同在线查找合同，如果存在，走合同在线逻辑，不存在则走bms逻辑
						contractQuoteInfoVo = getContractForWhat(t);
						if(contractQuoteInfoVo == null || StringUtil.isEmpty(contractQuoteInfoVo.getTemplateCode())){
							calcuForBms(t, f);
						}
						else{
							XxlJobLogger.log("规则编号【{0}】", contractQuoteInfoVo.getRuleCode().trim());
							calcuForContract(t,f);
						}
					}
					XxlJobLogger.log("单条费用计算耗时：【{0}】毫秒 ",(System.currentTimeMillis() - btime));
				}
				
				btime= System.currentTimeMillis();// 操作开始时间
				updateBatch(listT,feesList);
				XxlJobLogger.log("更新数据总耗时：【{0}】毫秒 ",(System.currentTimeMillis() - btime));
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
