package com.jiuyescm.bms.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.jiuyescm.bms.common.tool.ListTool;
import com.jiuyescm.bms.jobhandler.CommonJobHandler;
import com.jiuyescm.bs.util.StringUtil;
import com.xxl.job.core.log.XxlJobLogger;

public class BillingCallable<T,F> implements Callable<Boolean> {

	private int pageIndex;  
    
    private List<T> list;  
    
    private CommonJobHandler<T,F> calcJob;
    
    public BillingCallable(int pageIndex,List<T> list, CommonJobHandler<T,F> calcJob){  
        this.pageIndex = pageIndex;  
        this.list = list;  
        this.calcJob = calcJob;
    }  
	
	@Override
	public Boolean call() throws Exception {
		XxlJobLogger.log("************** 费用科目【{0}】 pageIndex【{1}】 size【{2}】",calcJob.SubjectId,pageIndex,list.size());
		long start = System.currentTimeMillis();// 系统开始时间
		long end = System.currentTimeMillis();// 系统结束时间
		
		boolean flag=true;
		
		if(null != list && list.size() >0){  
			
			List<List<T>> pageT=ListTool.splitList(list, 200);//200个数据一组分页
			for(List<T> listT:pageT){
				try{
					start= System.currentTimeMillis();
					XxlJobLogger.log("************** 删除行数【{0}】**************",listT.size());
					calcJob.deleteFeesBatch(listT);
					end = System.currentTimeMillis();
					XxlJobLogger.log("************** 删除费用耗时【{0}】 毫秒**************",(end-start));
					List<F> feesList = new ArrayList<F>();
					for (T t : listT) {
						start= System.currentTimeMillis();// 操作开始时间
						
						if(calcJob.isJoin(t) == false){
							continue;
						}
						
						F f = calcJob.initFeeEntity(t); //初始化费用对象 获取计算参数，如：计费重量，计费数量，计费物流商等待
						feesList.add(f);
						//进行赋值判断前，先校验数据本身是否需要费用计算
						if(calcJob.isNoExe(t,f) == true){
							continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
						}
						if(calcJob.contract_switch == false){ 
							//如果合同在线开关未开启,直接走bms逻辑，不需要去合同在线查找合同；
							calcJob.calcuForBms(t, f);
						}
						else{
							//如果合同在线开关开启，需要先去合同在线查找合同，如果存在，走合同在线逻辑，不存在则走bms逻辑
							calcJob.calcu(t, f);
							/*calcJob.contractQuoteInfoVo = calcJob.getContractForWhat(t);
							if(calcJob.contractQuoteInfoVo == null || StringUtil.isEmpty(calcJob.contractQuoteInfoVo.getTemplateCode())){
								calcJob.calcuForBms(t, f);
							}
							else{
								XxlJobLogger.log("规则编号【{0}】", calcJob.contractQuoteInfoVo.getRuleCode().trim());
								calcJob.calcuForContract(t,f);
							}*/
						}
					}
					start= System.currentTimeMillis();// 操作开始时间
					calcJob.updateBatch(listT,feesList);
					end = System.currentTimeMillis();
					XxlJobLogger.log("************** 更新耗时【{0}】 毫秒**************",(end-start));
				}catch(Exception  e){
					flag=false;
					XxlJobLogger.log("【处理异常】原因:" +e);
				}
			}
        }  
        return flag;
	}

}
