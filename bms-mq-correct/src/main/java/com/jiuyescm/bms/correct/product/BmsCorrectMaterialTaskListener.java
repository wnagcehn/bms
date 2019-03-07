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

import net.sf.json.JSONObject;

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
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;


@Service("bmsCorrectMaterialTaskListener")
public class BmsCorrectMaterialTaskListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(BmsCorrectMaterialTaskListener.class.getName());
	
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
			
			//处理运单耗材统一
			logger.info(taskId+"正在处理耗材纠正");
			start = System.currentTimeMillis();
			handMaterialTask(taskId,errorMessage);
			logger.info(taskId+"耗材纠正结束 耗时--"+(System.currentTimeMillis()-start));
			
			//处理运单保温袋统一
			logger.info(taskId+"正在处理保温袋纠正");
			start = System.currentTimeMillis();
			handBwdTask(taskId,errorMessage);
			logger.info(taskId+"保温袋纠正结束 耗时--"+(System.currentTimeMillis()-start));
		} catch (Exception e1) {
			logger.error(taskId+"处理耗材统一失败：{}",e1);
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
	
	
	private void handMaterialTask(String taskId,StringBuffer errorMessage) throws Exception{
		Map<String,Object> condition=new HashMap<String,Object>();
		//根据taskId查询商家和时间
		condition.put("taskId", taskId);
		List<BmsCorrectAsynTaskVo> taskList=bmsCorrectAsynTaskService.queryList(condition);
		if(taskList.size()<=0){
			logger.info(taskId+"耗材调整,没有查询到任务记录;");
			errorMessage.append("耗材调整,没有查询到任务记录;");
			return;
		}
		BmsCorrectAsynTaskVo taskVo=taskList.get(0);
		try{
			handMaterial(taskVo,taskId,errorMessage);		
			taskVo.setTaskRate(50);
			taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
			taskVo.setRemark(errorMessage.toString());
			int returnTask=bmsCorrectAsynTaskService.update(taskVo);
			if(returnTask<=0){
				errorMessage.append("任务状态更新失败;");
				taskVo.setRemark(errorMessage.toString());
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
				bmsCorrectAsynTaskService.update(taskVo);				
			}
		} catch (Exception e) {
			logger.error(taskId+"耗材调整异常，错误日志：{}",e);
			errorMessage.append("耗材调整异常;");
			taskVo.setRemark(errorMessage.toString());
			taskVo.setTaskRate(55);
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
			logger.info(taskId+"查询耗材汇总统计");
			condition=new HashMap<String,Object>();
			condition.put("customerId", taskVo.getCustomerId());
			condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
			condition.put("endTime", DateUtil.formatyymmddLine(taskVo.getEndDate())+" 23:59:59");
			condition.put("taskId", taskId);
			condition.put("type", "PMXZX");
			
			logger.info(taskId+"插入汇总统计"+JSONObject.fromObject(condition));		
			//插入汇总统计
			long sta = System.currentTimeMillis();
			int re=bmsProductsMaterialService.saveMaterial(condition);
			long en = System.currentTimeMillis();
			if(re<=0){
				errorMessage.append("耗材不存在;");
				logger.info(taskId+"耗材不存在;");
				return "fail";
			}
			updateProgress(taskVo, 30);
			logger.info(taskId+"获取占比最高的耗材");
			//获取占比最高
			condition.put("taskId", taskId);
			start = System.currentTimeMillis();
			List<BmsProductsMaterialAccountVo> list=bmsProductsMaterialService.queyAllMax(condition);
			end = System.currentTimeMillis();
			logger.info(taskId+"------------------获取占比最高耗时：" + (end-start) + "毫秒------------------");
			//循环判断是否有重复之
			if(list.size()>0){
				//循环更新每个商品明细对应的耗材
				for(int i=0,length=list.size();i<length;i++){					
					BmsProductsMaterialAccountVo proAccountVo=list.get(i);								
					if(proAccountVo!=null){						
						String waybillNo="";					
						String metrialDetail=getMaxVolumMaterial(proAccountVo);
						if(StringUtils.isBlank(metrialDetail)){
							continue;
						}				
						//判断耗材明细									
						condition=new HashMap<String,Object>();
						condition.put("materialType", proAccountVo.getMaterialType());
						condition.put("pmxzxMark", metrialDetail);
						condition.put("productsMark", proAccountVo.getProductsMark());
						
						//查询出该商品明细下使用最多的运单打标记录
						start = System.currentTimeMillis();
						logger.info(taskId+"查询出该商品明细下使用最多的运单打标参数"+JSONObject.fromObject(condition));		
						BmsMarkingProductsVo markVo=bmsProductsWeightService.queryOneMaterial(condition);
						end = System.currentTimeMillis();
						logger.info(taskId+"------------------查询出该商品明细下使用最多的运单打标记录耗时：" + (end-start) + "毫秒------------------");
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
							logger.info(taskId+"查询出该商品明细下使用最多的运单打标参数"+JSONObject.fromObject(condition));		

							int updateResult=bmsProductsMaterialService.updateMaterialAccount(condition);
							end = System.currentTimeMillis();
							logger.info(taskId+"------------------将占比最高的运单号更新到汇总表里耗时：" + (end-start) + "毫秒------------------");
							if(updateResult<=0){
								logger.info(taskId+"更新汇总表失败"+condition);
								continue;
							}
													
							//查询该运单使用到的标准泡沫箱和纸箱
							condition=new HashMap<String,Object>();
							condition.put("waybillNo", waybillNo);
							logger.info("查询标准耗材运单号为"+waybillNo);
							start = System.currentTimeMillis();
							List<BizOutstockPackmaterialEntity> standMaterial=bizOutstockPackmaterialService.queryMaterial(condition);
							end = System.currentTimeMillis();
							logger.info(taskId+"------------------查询该运单使用到的标准泡沫箱和纸箱耗时：" + (end-start) + "毫秒------------------");
							//未查询到耗材
							if(standMaterial.size()==0){
								continue;
							}
							
							//找出未使用标准的运单号
							condition=new HashMap<String,Object>();
							condition.put("productsMark", proAccountVo.getProductsMark());
							condition.put("materialType", proAccountVo.getMaterialType());
							condition.put("pmxzxMark", metrialDetail);
							condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
							condition.put("endTime", DateUtil.formatyymmddLine(taskVo.getEndDate())+" 23:59:59");
							condition.put("customerId", taskVo.getCustomerId());
							start = System.currentTimeMillis();
							logger.info(taskId+"找出未使用标准的运单号参数"+JSONObject.fromObject(condition));
							List<BizOutstockPackmaterialEntity> notMaxList=bmsProductsMaterialService.queyNotMaxMaterial(condition);
							end = System.currentTimeMillis();
							logger.info(taskId+"------------------找出未使用标准的运单号耗时：" + (end-start) + "毫秒------------------");
							long total = 0l;
							long delTimeTotal = 0l;
							List<BizOutstockPackmaterialEntity> newList=new ArrayList<BizOutstockPackmaterialEntity>();
							List<String> waybillNoList=new ArrayList<String>();					
							for(int j=0,lenth=notMaxList.size();j<lenth;j++){
								BizOutstockPackmaterialEntity pack=notMaxList.get(j);
								waybillNoList.add(pack.getWaybillNo());	
								//新耗材插入								
								for(BizOutstockPackmaterialEntity entity:standMaterial){
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
							}
							//删除老耗材
							condition=new HashMap<String,Object>();
							condition.put("waybillNoList", waybillNoList);
							condition.put("lastModifier", taskVo.getCreator());
							condition.put("lastModifyTime", JAppContext.currentTimestamp());
							start = System.currentTimeMillis();
							int resultDelete=bizOutstockPackmaterialService.deleteOldMaterial(condition);
							end = System.currentTimeMillis();
							delTimeTotal = delTimeTotal + (end-start);						
							if(resultDelete>0){
								//logger.info("保存新耗材");
								start = System.currentTimeMillis();
								int resultSave=bizOutstockPackmaterialService.saveList(newList);
								if(resultSave<=0){
									logger.info("新增耗材失败");
								}
								end = System.currentTimeMillis();
								total = total + (end-start);
								
							}
							
							/*
							List<BmsMarkingMaterialVo> notMaxList=bmsProductsMaterialService.queyNotMax(condition);
							end = System.currentTimeMillis();
							logger.info(taskId+"------------------找出未使用标准的运单号耗时：" + (end-start) + "毫秒------------------");
							
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
										logger.info(taskId+"删除占比小耗材失败");
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
							}*/
							logger.info(taskId+"------------------删除原运单号对应得耗材和费用总耗时：" + delTimeTotal + "毫秒------------------");
							logger.info(taskId+"------------------保存新耗材成功，总耗时：" + total + "毫秒------------------");
						}
						condition=new HashMap<String,Object>();
						condition.put("productsMark", proAccountVo.getProductsMark());
						condition.put("materialType", proAccountVo.getMaterialType());
						condition.put("pmxzxMark", metrialDetail);
						start = System.currentTimeMillis();
						bizDispatchBillService.retryByMaterialMark(condition);	
						end = System.currentTimeMillis();
						totalRetry = totalRetry+(end-start);
						
					}
				}		
			}
			logger.info(taskId+"------------------插入汇总统计运单:"+re+"条,耗时：" + (en-sta) + "毫秒------------------");
		}
		logger.info(taskId+"------------------修改重算状态总耗时：" + totalRetry + "毫秒------------------");
		long totalEnd = System.currentTimeMillis();
		logger.info(taskId+"------------------修改重算状态耗时占比:" + totalRetry*1.0/(totalEnd-totalStart)*100 + "%------------------");
		updateProgress(taskVo, 40);
		errorMessage.append("耗材调整成功;");
		logger.info(taskId+"耗材调整成功;");
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
			logger.info(taskId+"保温袋纠正,没有查询到任务记录;");
			errorMessage.append("保温袋纠正,没有查询到任务记录;");
			return;
		}
		BmsCorrectAsynTaskVo taskVo=taskList.get(0);
		try{
			handBwd(taskVo,taskId,errorMessage);		
			if(errorMessage.indexOf("失败")!=-1 || errorMessage.indexOf("异常")!=-1){
				taskVo.setTaskRate(99);			
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.FAIL.getCode());
			}else{
				taskVo.setTaskRate(100);			
				taskVo.setTaskStatus(BmsCorrectAsynTaskStatusEnum.SUCCESS.getCode());
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
			logger.error(taskId+"保温袋调整异常，错误日志：{}",e);
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
			logger.info(taskId+"查询保温袋汇总统计");
			condition=new HashMap<String,Object>();
			condition.put("customerId", taskVo.getCustomerId());
			condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
			condition.put("endTime", DateUtil.formatyymmddLine(taskVo.getEndDate())+" 23:59:59");
			condition.put("taskId", taskId);
			condition.put("type", "BWD");
			
			logger.info(taskId+"插入保温袋汇总统计"+JSONObject.fromObject(condition));
			//插入汇总统计
			int re=bmsProductsMaterialService.saveBwd(condition);
			if(re<=0){
				errorMessage.append("保温袋不存在;");
				logger.info(taskId+"保温袋不存在");
				return "fail";
			}
			updateProgress(taskVo, 70);
			logger.info(taskId+"获取占比最高的保温袋");
			//获取占比最高
			condition.put("taskId", taskId);
			start = System.currentTimeMillis();
			List<BmsProductsMaterialAccountVo> list=bmsProductsMaterialService.queyAllBwxMax(condition);
			end = System.currentTimeMillis();
			logger.info(taskId+"------------------获取占比最高耗时：" + (end-start) + "毫秒------------------");
			//循环判断是否有重复之
			if(list.size()>0){
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
						condition.put("bwdMark", metrialDetail);
						condition.put("productsMark", proAccountVo.getProductsMark());
						
						//查询出该商品明细下使用最多的保温袋记录
						start = System.currentTimeMillis();
						BizOutstockPackmaterialEntity markVo=bmsProductsMaterialService.queryBwdMaterial(condition);
						end = System.currentTimeMillis();
						logger.info(taskId+"------------------查询出该商品明细下使用最多的耗材记录耗时：" + (end-start) + "毫秒------------------");
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
							logger.info(taskId+"------------------将占比最高的运单号更新到汇总表里耗时：" + (end-start) + "毫秒------------------");
							if(updateResult<=0){
								logger.info(taskId+"更新汇总表失败"+condition);
								continue;
							}
							
							//找出未使用标准的运单号
							condition=new HashMap<String,Object>();
							condition.put("productsMark", proAccountVo.getProductsMark());
                            condition.put("bwdMark", metrialDetail);
							condition.put("startTime", DateUtil.formatTimestamp(taskVo.getStartDate()));
							condition.put("endTime", DateUtil.formatyymmddLine(taskVo.getEndDate())+" 23:59:59");
							condition.put("customerId", taskVo.getCustomerId());
							start = System.currentTimeMillis();
							logger.info("找出未使用标准的运单号参数"+JSONObject.fromObject(condition));
							List<BizOutstockPackmaterialEntity> notMaxList=bmsProductsMaterialService.queyNotMaxBwd(condition);
							end = System.currentTimeMillis();
							logger.info(taskId+"------------------找出未使用标准的运单号耗时：" + (end-start) + "毫秒------------------");
							
							List<BizOutstockPackmaterialEntity> newList=new ArrayList<BizOutstockPackmaterialEntity>();				
							List<String> waybillNoList=new ArrayList<String>();					

							for(int j=0,lenth=notMaxList.size();j<lenth;j++){
								BizOutstockPackmaterialEntity entity=notMaxList.get(j);		
								waybillNoList.add(entity.getWaybillNo());
								//将新耗材插入并重算
								entity.setConsumerMaterialCode(markVo.getConsumerMaterialCode());
								entity.setConsumerMaterialName(markVo.getConsumerMaterialName());
								entity.setLastModifier(taskVo.getCreator());
								entity.setLastModifyTime(JAppContext.currentTimestamp());
								entity.setSpecDesc(markVo.getSpecDesc());
								entity.setIsCalculated("99");
								entity.setDelFlag("0");
								entity.setFeesNo("");
								entity.setextattr4(taskId);
								entity.setextattr5("");								
								newList.add(entity);
																
							}						
							logger.info("------------------删除原对应得保温袋");
							//删除原运单号对应得耗材
							condition=new HashMap<String,Object>();
							condition.put("waybillNoList", waybillNoList);
							condition.put("lastModifier", taskVo.getCreator());
							condition.put("lastModifyTime", JAppContext.currentTimestamp());
							
							int result=bizOutstockPackmaterialService.deleteOldBwd(condition);
							if(result>0){
								logger.info("------------------保存新保温袋------------------");	
								bizOutstockPackmaterialService.saveList(newList);
							}
						}					
					}
				}		
			}
		}
		long totalEnd = System.currentTimeMillis();
		logger.info(taskId+"------------------修改保温袋总耗时:" + totalRetry*1.0/(totalEnd-totalStart)*100 + "%------------------");
		updateProgress(taskVo, 90);
		logger.info(taskId+"保温袋调整成功");
		errorMessage.append("保温袋调整成功;");
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
					condition.put("productsMark", proAccountVo.getProductsMark());
					condition.put("list", pList);
					//查询出耗材标对应得耗材编码
					logger.info("查询出耗材标对应得耗材编码条件"+JSONObject.fromObject(condition));
					Map<String,String> materialMap=bmsProductsMaterialService.getMaterialMap(condition);
					logger.info("查询出耗材标对应得耗材编码结果"+materialMap);
					pList=new ArrayList<String>();
					//遍历map中的值 (key是编码，value是标)
					for (String value : materialMap.keySet()) { 
					  pList.add(value);
					}
					condition.clear();
					condition.put("list", pList);
					//返回的是code
					logger.info("获取最高体积的条件"+JSONObject.fromObject(condition));
					metrialDetail=bizOutstockPackmaterialService.getMaxBwdVolumn(condition);
					logger.info("查询结果"+metrialDetail);
					//将编码code转化为标
					metrialDetail=materialMap.get(metrialDetail);
					logger.info("最后返回的结果"+metrialDetail);
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
					condition.put("pmxzxMark", array[i]);
					//查询出该商品明细下使用运单号
					BmsMarkingProductsVo markVo=bmsProductsWeightService.queryOneMaterial(condition);
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