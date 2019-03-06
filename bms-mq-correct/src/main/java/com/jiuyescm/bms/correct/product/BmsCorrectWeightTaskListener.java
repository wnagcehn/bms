package com.jiuyescm.bms.correct.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.asyn.service.IBmsCorrectAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.bms.correct.service.IBmsProductsWeightService;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;
import com.jiuyescm.bms.correct.vo.BmsProductsWeightAccountVo;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;


@Service("bmsCorrectWeightTaskListener")
public class BmsCorrectWeightTaskListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(BmsCorrectWeightTaskListener.class.getName());
	
	@Autowired 
	private IBmsProductsWeightService bmsProductsWeightService;	
	@Autowired 
	private IBmsProductsMaterialService bmsProductsMaterialService;
	@Autowired
	private IBizOutstockPackmaterialService bizOutstockPackmaterialService;	
	@Autowired
	private IBizDispatchBillService bizDispatchBillService;	
	@Autowired 
	private IPubMaterialInfoService pubMaterialInfoService;	
	@Autowired
	private IBmsCorrectAsynTaskService bmsCorrectAsynTaskService;
	
	/**
	 * 运单重量调整
	 */
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ运单纠正处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.info("取出消息失败");
			return;
		}
		
		try {
			StringBuffer errorMessage=new StringBuffer();
			logger.info(taskId+"正在消费");
			//处理运单重量统一
			logger.info(taskId+"正在处理运单重量纠正");
			start = System.currentTimeMillis();
			handWeightTask(taskId,errorMessage);
			logger.info("重量纠正结束 耗时--"+(System.currentTimeMillis()-start));
			
		} catch (Exception e1) {
			logger.error(taskId+"处理运单重量失败：{}",e1);
			return;
		}
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
	}
	
	/**
	 * 根据任务ID处理重量统一
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	private void handWeightTask(String taskId,StringBuffer errorMessage) throws Exception{
		
		Map<String,Object> condition=new HashMap<String,Object>();
		//根据taskId查询商家和时间
		condition.put("taskId", taskId);
		List<BmsCorrectAsynTaskVo> taskList=bmsCorrectAsynTaskService.queryList(condition);
		if(taskList.size()<=0){
			logger.info(taskId+"没有查询到重量纠正任务记录;");
			errorMessage.append(taskId+"没有查询到重量纠正任务记录;");
			return;
		}
		BmsCorrectAsynTaskVo taskVo=taskList.get(0);
		
		taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
		bmsCorrectAsynTaskService.update(taskVo);
		
		try {
			handWeight(taskVo, taskId,errorMessage);		
			
			if(errorMessage.indexOf("失败")!=-1 || errorMessage.indexOf("异常")!=-1){
				taskVo.setTaskRate(99);			
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			}else{
				taskVo.setTaskRate(100);			
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
			}
			
			taskVo.setRemark(errorMessage.toString());
			int resultTask=bmsCorrectAsynTaskService.update(taskVo);
			if(resultTask<=0){
				logger.info(taskId+"运单重量纠正任务状态更新失败");
				errorMessage.append("运单重量纠正任务状态更新失败;");
				taskVo.setRemark(errorMessage.toString());
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsCorrectAsynTaskService.update(taskVo);
			}		
		} catch (Exception e) {
			logger.error(taskId+"重量调整异常，错误日志：{}",e);
			errorMessage.append("重量调整异常;");
			taskVo.setRemark(errorMessage.toString());
			taskVo.setTaskRate(95);
			taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.EXCEPTION.getCode());
			bmsCorrectAsynTaskService.update(taskVo);
		}	
	}
	
	@SuppressWarnings("deprecation")
	private String handWeight(BmsCorrectAsynTaskVo taskVo,String taskId,StringBuffer errorMessage) throws Exception{
		Map<String,Object> condition=new HashMap<String,Object>();
		long start = 0l;
		long end = 0l;
		if(StringUtils.isNotBlank(taskVo.getCustomerId())){
			//根据商家和时间查询运单业务数据里的运单号
			
			condition=new HashMap<String,Object>();
			condition.put("customerId", taskVo.getCustomerId());
			condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
			condition.put("endTime", DateUtil.formatyymmddLine(taskVo.getEndDate())+" 23:59:59");
			condition.put("taskId", taskId);
			updateProgress(taskVo,50);
			logger.info(taskId+"正在进行重量汇总统计 ");
			logger.info(condition);
			logger.info(taskId+"统计的参数"+JSONObject.fromObject(condition));		
			//插入汇总统计
			start = System.currentTimeMillis();
			int re=bmsProductsWeightService.saveWeight(condition);
			if(re<=0){
				logger.info(taskId+"未查询到该商家重量汇总统计");
				errorMessage.append("未查询到该商家重量汇总统计");
				return "fail";
			}
			logger.info(taskId+"重量汇总统计 耗时--" + (System.currentTimeMillis()-start));
			//获取商品明细组合与重量占比最高
			condition.put("taskId", taskId);
			
			
			List<BmsProductsWeightAccountVo> list=bmsProductsWeightService.queyAllMax(condition);
			
			logger.info("查询出来的占比最高的重量大小"+list.size());

			if(list.size()>0){
				//循环更新每个商品明细对应的运单
				for(int i=0;i<list.size();i++){
					BmsProductsWeightAccountVo proAccountVo=list.get(i);				
					List<String> waybillNoList=new ArrayList<String>();
					List<Double> weList=new ArrayList<Double>();
					if(proAccountVo!=null){
						//商品重量
						double newWeight=0d;
						String wList=proAccountVo.getWeightList();
						if(StringUtils.isNotBlank(wList)){
							String[] array=wList.split(",");
							if(array.length==1){
								newWeight=Double.valueOf(array[0]);
							}else if(array.length>1){
								 for (int a = 0; a < array.length; a++) {
									 weList.add(Double.valueOf(array[a]));						
							     }
								 newWeight=Collections.max(weList);
							}
						}

						logger.info(taskId+"查询出该商品明细下不是这个重量的所有打标记录");
						
						String productDetail=proAccountVo.getProductsMark();
						condition=new HashMap<String,Object>();
						condition.put("productsMark", productDetail);
						condition.put("weight", newWeight);
						condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
						condition.put("endTime", DateUtil.formatyymmddLine(taskVo.getEndDate())+" 23:59:59");
						
						logger.info(taskId+"商品明细"+productDetail+"的标准重量"+newWeight);
						
						//查询出该商品明细下不是这个重量的所有打标记录
						List<BmsMarkingProductsVo> marklist=bmsProductsWeightService.queryMark(condition);
						
						if(marklist.size()>0){
							for(BmsMarkingProductsVo vo:marklist){
								waybillNoList.add(vo.getWaybillNo());
							}
							//批量更新打标记录
							logger.info(taskId+"批量更新打标记录 运单数--"+waybillNoList.size());
							int result=bmsProductsWeightService.updateMark(condition);
							if(result>0){
								//去更新运单状态
								start = System.currentTimeMillis();
								int result2=bizDispatchBillService.retryByWaybillNo(waybillNoList);
								end = System.currentTimeMillis();
								logger.info(taskId+"------------------更新运单状态耗时：" + (end-start) + "毫秒------------------");
								if(result2<=0){						
									logger.info(taskId+"更新运单失败");
								}
							}else{
								logger.info(taskId+"批量更新重量打标记录失败");
							}
						}
						
					}

				}
			}
			
		}
		
		updateProgress(taskVo,80);
		errorMessage.append("运单重量调整成功;");
		logger.info(taskId+"运单重量调整成功;");
		return "sucess";
	}
	
	
	public void updateProgress(BmsCorrectAsynTaskVo taskVo,int num)throws Exception{
		taskVo.setTaskRate(num);
		bmsCorrectAsynTaskService.update(taskVo);
	}
}
