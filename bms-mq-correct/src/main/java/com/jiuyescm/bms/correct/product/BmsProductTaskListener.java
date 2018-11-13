package com.jiuyescm.bms.correct.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.asyn.service.IBmsCorrectAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.bms.correct.service.IBmsProductsWeightService;
import com.jiuyescm.bms.correct.vo.BmsMarkingMaterialVo;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;
import com.jiuyescm.bms.correct.vo.BmsProductsWeightAccountVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;


@Service("bmsProductTaskListener")
public class BmsProductTaskListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(BmsProductTaskListener.class.getName());
	
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
			logger.info("正在消费"+taskId);
			//处理运单重量统一
			logger.info("正在处理运单重量纠正");
			start = System.currentTimeMillis();
			handWeightTask(taskId,errorMessage);
			logger.info("重量纠正结束 耗时--"+(System.currentTimeMillis()-start));
			
			
			//处理运单耗材统一
			logger.info("正在处理耗材纠正");
			start = System.currentTimeMillis();
			handMaterialTask(taskId,errorMessage);
			logger.info("耗材纠正结束 耗时--"+(System.currentTimeMillis()-start));
			
			//处理运单保温袋统一
			logger.info("正在处理保温袋纠正");
			start = System.currentTimeMillis();
			handBwdTask(taskId,errorMessage);
			logger.info("保温袋纠正结束 耗时--"+(System.currentTimeMillis()-start));
		} catch (Exception e1) {
			logger.error("处理运单重量和耗材统一失败：{}",e1);
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
			logger.info("没有查询到重量纠正任务记录;");
			errorMessage.append("没有查询到重量纠正任务记录;");
			return;
		}
		BmsCorrectAsynTaskVo taskVo=taskList.get(0);
		
		taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
		bmsCorrectAsynTaskService.update(taskVo);
		
		try {
			String result=handWeight(taskVo, taskId,errorMessage);		
			if("sucess".equals(result)){
				errorMessage.append("运单重量调整成功;");
				taskVo.setTaskRate(30);
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.PROCESS.getCode());
			}else if("fail".equals(result)){
				//errorMessage.append("运单重量调整失败;");
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			}
			taskVo.setRemark(errorMessage.toString());
			int resultTask=bmsCorrectAsynTaskService.update(taskVo);
			if(resultTask<=0){
				logger.info("运单重量纠正任务状态更新失败");
				errorMessage.append("运单重量纠正任务状态更新失败;");
				taskVo.setRemark(errorMessage.toString());
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsCorrectAsynTaskService.update(taskVo);
			}		
		} catch (Exception e) {
			logger.error("重量调整异常，错误日志：{}",e);
			errorMessage.append("重量调整异常;");
			taskVo.setRemark(errorMessage.toString());
			taskVo.setTaskRate(35);
			taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.EXCEPTION.getCode());
			bmsCorrectAsynTaskService.update(taskVo);
		}	
	}
	
	private String handWeight(BmsCorrectAsynTaskVo taskVo,String taskId,StringBuffer errorMessage) throws Exception{
		Map<String,Object> condition=new HashMap<String,Object>();
		long start = 0l;
		long end = 0l;
		if(StringUtils.isNotBlank(taskVo.getCustomerId())){
			//根据商家和时间查询运单业务数据里的运单号
			
			condition=new HashMap<String,Object>();
			condition.put("customerId", taskVo.getCustomerId());
			condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
			condition.put("endTime", DateUtil.formatTimestamp(taskVo.getEndDate()));
			condition.put("taskId", taskId);
			updateProgress(taskVo,10);
			logger.info("正在进行重量汇总统计 ");
			logger.info(condition);
			//插入汇总统计
			start = System.currentTimeMillis();
			int re=bmsProductsWeightService.saveWeight(condition);
			if(re<=0){
				logger.info("插入重量汇总统计失败");
				errorMessage.append("插入重量汇总统计失败");
				return "fail";
			}
			logger.info("重量汇总统计 耗时--" + (System.currentTimeMillis()-start));
			//获取商品明细组合与重量占比最高
			condition.put("taskId", taskId);
			
			
			List<BmsProductsWeightAccountVo> list=bmsProductsWeightService.queyAllMax(condition);
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

						logger.info("查询出该商品明细下不是这个重量的所有打标记录");
						
						String productDetail=proAccountVo.getProductsMark();
						condition=new HashMap<String,Object>();
						condition.put("productsMark", productDetail);
						condition.put("weight", newWeight);
						condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
						condition.put("endTime", DateUtil.formatTimestamp(taskVo.getEndDate()));
						
						logger.info("商品明细"+productDetail+"的标准重量"+newWeight);
						
						//查询出该商品明细下不是这个重量的所有打标记录
						List<BmsMarkingProductsVo> marklist=bmsProductsWeightService.queryMark(condition);
						
						if(marklist.size()>0){
							for(BmsMarkingProductsVo vo:marklist){
								waybillNoList.add(vo.getWaybillNo());
							}
							//批量更新打标记录
							logger.info("批量更新打标记录 运单数--"+waybillNoList.size());
							int result=bmsProductsWeightService.updateMark(condition);
							if(result>0){
								//去更新运单状态
								start = System.currentTimeMillis();
								int result2=bizDispatchBillService.retryByWaybillNo(waybillNoList);
								end = System.currentTimeMillis();
								logger.info("------------------更新运单状态耗时：" + (end-start) + "毫秒------------------");
								if(result2<=0){						
									logger.info("更新运单失败");
								}
							}else{
								logger.info("批量更新重量打标记录失败");
							}
						}
						
					}

				}
			}
			
		}
		
		updateProgress(taskVo,20);
		return "sucess";
	}
	
	private void handMaterialTask(String taskId,StringBuffer errorMessage) throws Exception{
		Map<String,Object> condition=new HashMap<String,Object>();
		//根据taskId查询商家和时间
		condition.put("taskId", taskId);
		List<BmsCorrectAsynTaskVo> taskList=bmsCorrectAsynTaskService.queryList(condition);
		if(taskList.size()<=0){
			logger.info("耗材调整,没有查询到任务记录;");
			errorMessage.append("耗材调整,没有查询到任务记录;");
			return;
		}
		BmsCorrectAsynTaskVo taskVo=taskList.get(0);
		try{
			String result=handMaterial(taskVo,taskId,errorMessage);		
			if("sucess".equals(result)){
				logger.info("耗材调整成功");
				taskVo.setTaskRate(70);
				errorMessage.append("耗材调整成功;");
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
			}else if("fail".equals(result)){
				//errorMessage.append("耗材调整失败;");
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			}
			taskVo.setRemark(errorMessage.toString());
			int returnTask=bmsCorrectAsynTaskService.update(taskVo);
			if(returnTask<=0){
				errorMessage.append("任务状态更新失败;");
				taskVo.setRemark(errorMessage.toString());
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsCorrectAsynTaskService.update(taskVo);				
			}
		} catch (Exception e) {
			logger.error("耗材调整异常，错误日志：{}",e);
			errorMessage.append("耗材调整异常;");
			taskVo.setRemark(errorMessage.toString());
			taskVo.setTaskRate(75);
			taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.EXCEPTION.getCode());
			bmsCorrectAsynTaskService.update(taskVo);
		}
	}
	
	private String handMaterial(BmsCorrectAsynTaskVo taskVo,String taskId,StringBuffer errorMessage) throws Exception{
		long start = 0l;
		long end = 0l;
		long totalStart = System.currentTimeMillis();
		long totalRetry = 0l;
		Map<String,Object> condition=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(taskVo.getCustomerId())){
			//根据商家和时间查询耗材出库表业务数据里的运单号
			logger.info("查询耗材汇总统计");
			condition=new HashMap<String,Object>();
			condition.put("customerId", taskVo.getCustomerId());
			condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
			condition.put("endTime", DateUtil.formatTimestamp(taskVo.getEndDate()));
			condition.put("taskId", taskId);
			condition.put("type", "PMXZX");
			
			logger.info("插入汇总统计");
			//插入汇总统计
			long sta = System.currentTimeMillis();
			int re=bmsProductsMaterialService.saveMaterial(condition);
			long en = System.currentTimeMillis();
			if(re<=0){
				errorMessage.append("耗材汇总统计失败;");
				logger.info("耗材汇总统计失败");
				return "fail";
			}
			updateProgress(taskVo, 50);
			logger.info("获取占比最高的耗材");
			//获取占比最高
			condition.put("taskId", taskId);
			start = System.currentTimeMillis();
			List<BmsProductsMaterialAccountVo> list=bmsProductsMaterialService.queyAllMax(condition);
			end = System.currentTimeMillis();
			logger.info("------------------获取占比最高耗时：" + (end-start) + "毫秒------------------");
			//循环判断是否有重复之
			if(list.size()>0){
				//循环更新每个商品明细对应的耗材
				for(int i=0,length=list.size();i<length;i++){					
					BmsProductsMaterialAccountVo proAccountVo=list.get(i);								
					if(proAccountVo!=null){						
						List<String> waybillNoList=new ArrayList<String>();					
						String waybillNo="";					
						String metrialDetail=getMaxVolumMaterial(proAccountVo);
						if(StringUtils.isBlank(metrialDetail)){
							continue;
						}				
						//判断耗材明细									
						condition=new HashMap<String,Object>();
						condition.put("materialType", proAccountVo.getMaterialType());
						condition.put("materialMark", metrialDetail);
						condition.put("productsMark", proAccountVo.getProductsMark());
						
						//查询出该商品明细下使用最多的运单打标记录
						start = System.currentTimeMillis();
						BmsMarkingMaterialVo markVo=bmsProductsMaterialService.queryOneMark(condition);
						end = System.currentTimeMillis();
						logger.info("------------------查询出该商品明细下使用最多的运单打标记录耗时：" + (end-start) + "毫秒------------------");
						if(markVo!=null){
							waybillNo=markVo.getWaybillNo();
						}
						if(StringUtils.isNotBlank(waybillNo)){
							//将占比最高的运单号更新到汇总表里
							condition=new HashMap<String,Object>();
							condition.put("productsMark", proAccountVo.getProductsMark());
							condition.put("materialType", proAccountVo.getMaterialType());
							condition.put("materialMark", metrialDetail);
							condition.put("percent", proAccountVo.getPercent());
							condition.put("taskId", proAccountVo.getTaskId());
							condition.put("waybillNo", waybillNo);
							condition.put("type", "PMXZX");
							start = System.currentTimeMillis();
							int updateResult=bmsProductsMaterialService.updateMaterialAccount(condition);
							end = System.currentTimeMillis();
							logger.info("------------------将占比最高的运单号更新到汇总表里耗时：" + (end-start) + "毫秒------------------");
							if(updateResult<=0){
								logger.info("更新汇总表失败"+condition);
								continue;
							}
													
							//查询该运单使用到的标准泡沫箱和纸箱
							condition=new HashMap<String,Object>();
							condition.put("waybillNo", waybillNo);
							logger.info("查询标准耗材运单号为"+waybillNo);
							start = System.currentTimeMillis();
							List<BizOutstockPackmaterialEntity> standMaterial=bizOutstockPackmaterialService.queryMaterial(condition);
							end = System.currentTimeMillis();
							logger.info("------------------查询该运单使用到的标准泡沫箱和纸箱耗时：" + (end-start) + "毫秒------------------");
							//未查询到耗材
							if(standMaterial.size()==0){
								continue;
							}
							
							//找出未使用标准的运单号
							condition=new HashMap<String,Object>();
							condition.put("productsMark", proAccountVo.getProductsMark());
							condition.put("materialType", proAccountVo.getMaterialType());
							condition.put("materialMark", metrialDetail);
							condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
							condition.put("endTime", DateUtil.formatTimestamp(taskVo.getEndDate()));
							start = System.currentTimeMillis();
							List<BmsMarkingMaterialVo> notMaxList=bmsProductsMaterialService.queyNotMax(condition);
							end = System.currentTimeMillis();
							logger.info("------------------找出未使用标准的运单号耗时：" + (end-start) + "毫秒------------------");
							
							long total = 0l;
							long delTimeTotal = 0l;
							for(int j=0,lenth=notMaxList.size();j<lenth;j++){
								BmsMarkingMaterialVo vo=notMaxList.get(j);
								String wayNo=vo.getWaybillNo();
								condition=new HashMap<String,Object>();
								condition.put("waybillNo", wayNo);
								List<BizOutstockPackmaterialEntity> materialList=bizOutstockPackmaterialService.queryMaterial(condition);			
								if(materialList.size()>0){									
									for(BizOutstockPackmaterialEntity entity:materialList){
										entity.setDelFlag("2");
										entity.setLastModifier(taskVo.getCreator());
										entity.setLastModifyTime(JAppContext.currentTimestamp());
									}									
									//logger.info("删除原始耗材");
									
									//删除原运单号对应得耗材和费用
									start = System.currentTimeMillis();
									int result=bizOutstockPackmaterialService.updateList(materialList);
									end = System.currentTimeMillis();
									delTimeTotal = delTimeTotal + (end-start);
									if(result<=0){
										logger.info("删除占比小耗材失败");
										continue;
									}
									
									//将新耗材插入并重算
									List<BizOutstockPackmaterialEntity> newList=new ArrayList<BizOutstockPackmaterialEntity>();
									for(BizOutstockPackmaterialEntity entity:standMaterial){
										BizOutstockPackmaterialEntity pack=materialList.get(0);
										BizOutstockPackmaterialEntity packEntity=new BizOutstockPackmaterialEntity();								
										try {
							                PropertyUtils.copyProperties(packEntity, pack);
							            } catch (Exception ex) {
							               logger.error("转换失败");
							            }						
										packEntity.setConsumerMaterialCode(entity.getConsumerMaterialCode());
										packEntity.setConsumerMaterialName(entity.getConsumerMaterialName());
										packEntity.setSpecDesc(entity.getSpecDesc());
										packEntity.setLastModifier(taskVo.getCreator());
										packEntity.setLastModifyTime(JAppContext.currentTimestamp());
										packEntity.setIsCalculated("99");
										packEntity.setFeesNo("");
										packEntity.setDelFlag("0");
										packEntity.setextattr4(taskId);
										packEntity.setextattr5("");
										newList.add(packEntity);
									}
									//logger.info("保存新耗材");
									start = System.currentTimeMillis();
									int resultSave=bizOutstockPackmaterialService.saveList(newList);
									end = System.currentTimeMillis();
									total = total + (end-start);
									if(resultSave>0){
										//对运单重算
										waybillNoList.add(wayNo);									
									}else{
										logger.error("新增标准耗材失败");
									}															
								}							
							}
							logger.info("------------------删除原运单号对应得耗材和费用总耗时：" + delTimeTotal + "毫秒------------------");
							logger.info("------------------保存新耗材成功，总耗时：" + total + "毫秒------------------");
						}
						condition=new HashMap<String,Object>();
						condition.put("productsMark", proAccountVo.getProductsMark());
						condition.put("materialType", proAccountVo.getMaterialType());
						condition.put("materialMark", metrialDetail);
						start = System.currentTimeMillis();
						bizDispatchBillService.retryByMaterialMark(condition);	
						end = System.currentTimeMillis();
						totalRetry = totalRetry+(end-start);
						
					}
				}		
			}
			logger.info("------------------插入汇总统计运单:"+re+"条,耗时：" + (en-sta) + "毫秒------------------");
		}
		logger.info("------------------修改重算状态总耗时：" + totalRetry + "毫秒------------------");
		long totalEnd = System.currentTimeMillis();
		logger.info("------------------修改重算状态耗时占比:" + totalRetry*1.0/(totalEnd-totalStart)*100 + "%------------------");
		updateProgress(taskVo, 60);
		return "sucess";
	}
	
	/**
	 * 保温袋纠正
	 * @param taskId
	 * @param errorMessage
	 * @throws Exception
	 */
	private void handBwdTask(String taskId,StringBuffer errorMessage) throws Exception{
		Map<String,Object> condition=new HashMap<String,Object>();
		//根据taskId查询商家和时间
		condition.put("taskId", taskId);
		List<BmsCorrectAsynTaskVo> taskList=bmsCorrectAsynTaskService.queryList(condition);
		if(taskList.size()<=0){
			logger.info("保温袋纠正,没有查询到任务记录;");
			errorMessage.append("保温袋纠正,没有查询到任务记录;");
			return;
		}
		BmsCorrectAsynTaskVo taskVo=taskList.get(0);
		try{
			String result=handBwd(taskVo,taskId,errorMessage);		
			if("sucess".equals(result)){
				logger.info("保温袋调整成功");
				taskVo.setTaskRate(100);
				errorMessage.append("保温袋调整成功;");
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
			}else if("fail".equals(result)){
				//errorMessage.append("耗材调整失败;");
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			}
			taskVo.setRemark(errorMessage.toString());
			int returnTask=bmsCorrectAsynTaskService.update(taskVo);
			if(returnTask<=0){
				errorMessage.append("任务状态更新失败;");
				taskVo.setRemark(errorMessage.toString());
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsCorrectAsynTaskService.update(taskVo);				
			}
		} catch (Exception e) {
			logger.error("保温袋调整异常，错误日志：{}",e);
			errorMessage.append("保温袋调整异常;");
			taskVo.setRemark(errorMessage.toString());
			taskVo.setTaskRate(95);
			taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.EXCEPTION.getCode());
			bmsCorrectAsynTaskService.update(taskVo);
		}
	}
	
	/**
	 * 保温袋调整
	 * @param taskVo
	 * @param taskId
	 * @param errorMessage
	 * @return
	 * @throws Exception
	 */
	private String handBwd(BmsCorrectAsynTaskVo taskVo,String taskId,StringBuffer errorMessage) throws Exception{
		long start = 0l;
		long end = 0l;
		long totalStart = System.currentTimeMillis();
		long totalRetry = 0l;
		Map<String,Object> condition=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(taskVo.getCustomerId())){
			//根据商家和时间查询耗材出库表业务数据里的运单号
			logger.info("查询保温袋汇总统计");
			condition=new HashMap<String,Object>();
			condition.put("customerId", taskVo.getCustomerId());
			condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
			condition.put("endTime", DateUtil.formatTimestamp(taskVo.getEndDate()));
			condition.put("taskId", taskId);
			condition.put("type", "BWD");
			
			logger.info("插入保温袋汇总统计");
			//插入汇总统计
			int re=bmsProductsMaterialService.saveBwd(condition);
			if(re<=0){
				errorMessage.append("保温袋汇总统计失败;");
				logger.info("保温袋汇总统计失败");
				return "fail";
			}
			updateProgress(taskVo, 80);
			logger.info("获取占比最高的保温袋");
			//获取占比最高
			condition.put("taskId", taskId);
			start = System.currentTimeMillis();
			List<BmsProductsMaterialAccountVo> list=bmsProductsMaterialService.queyAllBwxMax(condition);
			end = System.currentTimeMillis();
			logger.info("------------------获取占比最高耗时：" + (end-start) + "毫秒------------------");
			//循环判断是否有重复之
			if(list.size()>0){
				List<BizOutstockPackmaterialEntity> newList=new ArrayList<BizOutstockPackmaterialEntity>();				
				//循环更新每个商品明细对应的耗材
				for(int i=0,length=list.size();i<length;i++){					
					BmsProductsMaterialAccountVo proAccountVo=list.get(i);								
					if(proAccountVo!=null){											
						String metrialDetail=getMaxVolumBwd(proAccountVo);
						if(StringUtils.isBlank(metrialDetail)){
							continue;
						}				
						//判断耗材明细									
						condition=new HashMap<String,Object>();
						condition.put("materialMark", metrialDetail);
						condition.put("productsMark", proAccountVo.getProductsMark());
						
						//查询出该商品明细下使用最多的保温袋记录
						start = System.currentTimeMillis();
						BizOutstockPackmaterialEntity markVo=bmsProductsMaterialService.queryBwdMaterial(condition);
						end = System.currentTimeMillis();
						logger.info("------------------查询出该商品明细下使用最多的耗材记录耗时：" + (end-start) + "毫秒------------------");
						if(markVo!=null){
							//将占比最高的运单号更新到汇总表里
							condition=new HashMap<String,Object>();
							condition.put("productsMark", proAccountVo.getProductsMark());
							condition.put("materialMark", metrialDetail);
							condition.put("percent", proAccountVo.getPercent());
							condition.put("taskId", proAccountVo.getTaskId());
							condition.put("waybillNo", markVo.getWaybillNo());
							condition.put("type", "BWD");
							start = System.currentTimeMillis();
							int updateResult=bmsProductsMaterialService.updateMaterialAccount(condition);
							end = System.currentTimeMillis();
							logger.info("------------------将占比最高的运单号更新到汇总表里耗时：" + (end-start) + "毫秒------------------");
							if(updateResult<=0){
								logger.info("更新汇总表失败"+condition);
								continue;
							}
							
							//找出未使用标准的运单号
							condition=new HashMap<String,Object>();
							condition.put("productsMark", proAccountVo.getProductsMark());
							condition.put("materialMark", metrialDetail);
							condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
							condition.put("endTime", DateUtil.formatTimestamp(taskVo.getEndDate()));
							condition.put("customerId", taskVo.getCustomerId());
							start = System.currentTimeMillis();
							List<BizOutstockPackmaterialEntity> notMaxList=bmsProductsMaterialService.queyNotMaxBwd(condition);
							end = System.currentTimeMillis();
							logger.info("------------------找出未使用标准的运单号耗时：" + (end-start) + "毫秒------------------");
							
							for(int j=0,lenth=notMaxList.size();j<lenth;j++){
								BizOutstockPackmaterialEntity entity=notMaxList.get(j);							
								entity.setDelFlag("2");
								entity.setLastModifier(taskVo.getCreator());
								entity.setLastModifyTime(JAppContext.currentTimestamp());																
								//logger.info("删除原始耗材");
									
								//删除原运单号对应得耗材和费用
								start = System.currentTimeMillis();
								int result=bizOutstockPackmaterialService.update(entity);
								end = System.currentTimeMillis();
								if(result<=0){
									logger.info("删除占比小耗材失败");
									continue;
								}							
								//将新耗材插入并重算
								entity.setConsumerMaterialCode(markVo.getConsumerMaterialCode());
								entity.setConsumerMaterialName(markVo.getConsumerMaterialName());
								entity.setSpecDesc(markVo.getSpecDesc());
								entity.setIsCalculated("99");
								entity.setDelFlag("0");
								entity.setextattr4(taskId);
								entity.setextattr5("");								
								newList.add(entity);
																
							}
						}
						logger.info("------------------删除原对应得保温袋");
						bizOutstockPackmaterialService.saveList(newList);
						logger.info("------------------保存新保温袋------------------");					
					}
				}		
			}
		}
		long totalEnd = System.currentTimeMillis();
		logger.info("------------------修改保温袋总耗时:" + totalRetry*1.0/(totalEnd-totalStart)*100 + "%------------------");
		updateProgress(taskVo, 90);
		return "sucess";
	}
	
	/**
	 * 获取保温袋最大的体积
	 * @param proAccountVo
	 * @return
	 */
	private String getMaxVolumBwd(BmsProductsMaterialAccountVo proAccountVo){
		String metrialDetail="";
		try {
			Map<String,Object> condition=new HashMap<String,Object>();			
			String metrialMark=proAccountVo.getMaterialMark();
			if(StringUtils.isNotBlank(metrialMark)){
				String[] array=metrialMark.split(",");
				if(array.length==1){
					metrialDetail=array[0];
				}else if(array.length>1){
					List<String> pList=new ArrayList<String>();
					//判断获取体积最大的耗材
					for(int i=0;i<array.length;i++){
						pList.add(array[i]);
					}
					condition.put("list", pList);
					metrialDetail=bizOutstockPackmaterialService.getMaxBwdVolumn(condition);
				}			
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("获取最高保温袋最大体积失败"+e.getMessage());
		}
		return metrialDetail;
	}
	
	/**
	 * 获取泡沫箱纸箱最大的体积
	 * @param proAccountVo
	 * @return
	 */
	private String getMaxVolumMaterial(BmsProductsMaterialAccountVo proAccountVo){
		Map<String,Object> condition=new HashMap<String,Object>();
		List<Double> volumList=new ArrayList<Double>();
		String metrialDetail="";
		String metrialMark=proAccountVo.getMaterialMark();
		if(StringUtils.isNotBlank(metrialMark)){
			String[] array=metrialMark.split(",");
			if(array.length==1){
				metrialDetail=array[0];
			}else if(array.length>1){
				//判断获取体积最大的耗材
				Map<Double,String> map=new HashMap<Double,String>();
				 for (int i = 0; i < array.length; i++) {
					condition=new HashMap<String,Object>();
				    condition.put("materialType", proAccountVo.getMaterialType());
					condition.put("materialMark", array[i]);
					//查询出该商品明细下使用运单号
					BmsMarkingMaterialVo markVo=bmsProductsMaterialService.queryOneMark(condition);
					if(markVo!=null){
						String waybillNo=markVo.getWaybillNo();
						if(StringUtils.isNotBlank(waybillNo)){
							//查询该运单使用到的标准泡沫箱和纸箱
							condition=new HashMap<String,Object>();
							condition.put("waybillNo", waybillNo);						
							double volumn=0d;					
							Double vol=bizOutstockPackmaterialService.getMaxVolum(condition);
							if(!DoubleUtil.isBlank(vol)){
								volumn=vol;
							}
							map.put(volumn, array[i]);
							volumList.add(volumn);
						}
					}						
			     }
				 double maxVolum=Collections.max(volumList);
				 metrialDetail=map.get(maxVolum);
			}
			
		}
		return metrialDetail;
	}
	
	/**
	 * 获取耗材对应的外体积
	 * @return
	 */
	public Map<String,BigDecimal> getMetrialVolum(){
		Map<String, Object> condition=new HashMap<String, Object>();

		//获取所有的耗材
		Map<String,BigDecimal> mMap=new HashMap<String,BigDecimal>();
		List<PubMaterialInfoVo> materialList=pubMaterialInfoService.queryList(condition);
		for(PubMaterialInfoVo vo:materialList){
			mMap.put(vo.getBarcode(), vo.getOutVolume());
		}
		
		return mMap;
	}
	
	public void updateProgress(BmsCorrectAsynTaskVo taskVo,int num)throws Exception{
		taskVo.setTaskRate(num);
		bmsCorrectAsynTaskService.update(taskVo);
	}
}
