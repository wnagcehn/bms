package com.jiuyescm.bms.biz.storage.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.mq.BmsPackmaterialTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.mq.BmsPackmaterialTaskTypeNewEnum;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.common.enumtype.type.ExeclOperateTypeEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.redis.client.IRedisClient;

@Controller("bizOutstockPackmaterialAsynImportWmsController")
public class BizOutstockPackmaterialAsynImportWmsController {

	@Autowired
	private SequenceService sequenceService;
	@Resource
	private JmsTemplate jmsQueueTemplate;
	@Resource
	private Lock lock;
	@Resource 
	private IRedisClient redisClient;
	@Autowired 
	private StorageClient storageClient;
	@Autowired
	private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Resource
	private ISystemCodeService systemCodeService;
	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialAsynImportWmsController.class.getName());

	String sessionId=JAppContext.currentUserID()+"_import_materialImportWmsFlag";
	final String nameSpace="com.jiuyescm.bms.biz.storage.controller.BizOutstockPackmaterialAsynImportWmsController";
	
	@FileResolver
	public  Map<String, Object> importPackmaterialWmsAsyn(final UploadFile file, final Map<String, Object> parameter) throws Exception {
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();

		String userId=ContextHolder.getLoginUserName();
		String lockString=Tools.getMd5(userId + "BMS_QUO_IMPORT_MATERIAL_INFO_ASYN");
		Map<String, Object> remap = lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map = Maps.newHashMap();
				try {
				   map = importFileAsyn(file,parameter);
				   return map;
				} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg(e.getMessage());
					infoList.add(errorVo);		
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("耗材出库明细导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return remap;
	}
	private double getMaxFileSize(){
		double fileSize=50.0;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_FILE_SIZE");
			fileSize=Double.valueOf(code.getExtattr1());
		}catch(Exception e){
			logger.info("未配置系统参数IMPORT_FILE_SIZE");
			System.out.println("未配置系统参数IMPORT_FILE_SIZE");
		}
		return fileSize;
	}
	/**
	 * 异步导入处理文件
	 * @param file
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	public Map<String,Object> importFileAsyn(UploadFile file, Map<String, Object> parameter) throws Exception{
		setProgress("0");
		Map<String, Object> map = Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		setProgress("1");
		String extendFileName="";
		if(file.getFileName().contains("xlsx")){
			extendFileName="xlsx";
		}else{
			extendFileName="xls";
		}
		
		String fileName = file.getFileName();
		// 校验文件名称
		if (!checkRegFileName(fileName)) {
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel文件名称【"+fileName+"】不符合规范,请参考【上海01仓201805耗材导入-1.xlsx】"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
				
		double maxFileSize=getMaxFileSize();
		double importFileSize=BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024*1024)).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(importFileSize>maxFileSize){
			infoList.add(new ErrorMessageVo(1, "Excel 导入文件过大,最多能导入"+maxFileSize+"M,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		// 保存导入文件到fastDFS，获取文件路径
		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		StorePath resultStorePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		String fullPath = storePath.getFullPath();
		String resultFullPath = resultStorePath.getFullPath();
		if (StringUtils.isBlank(fullPath) || StringUtils.isBlank(resultFullPath)) {
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据上传文件系统失败，请稍后重试"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 生成任务，写入任务表
		String taskId =sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
		
		BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
		taskEntity.setTaskId(taskId);
		taskEntity.setTaskName(fileName.substring(0, fileName.lastIndexOf(".")));
		taskEntity.setTaskRate(0);
		taskEntity.setTaskStatus(FileAsynTaskStatusEnum.WAIT.getCode());
		taskEntity.setTaskType(BmsPackmaterialTaskTypeNewEnum.IMPORT.getCode());
		taskEntity.setBizType(ExeclOperateTypeEnum.IMPORT.getCode());
		taskEntity.setFileRows(0);
		taskEntity.setOriginFileName(fileName);
		taskEntity.setOriginFilePath(fullPath);
		taskEntity.setResultFileName(fileName);
		taskEntity.setResultFilePath(resultFullPath);
		taskEntity.setCreator(JAppContext.currentUserName());
		taskEntity.setCreatorId(JAppContext.currentUserID());
		taskEntity.setCreateTime(JAppContext.currentTimestamp());
		taskEntity.setTemplateType(BmsEnums.templateType.wms.getCode());
		int saveNum = bmsFileAsynTaskService.save(taskEntity);
		if (saveNum <= 0) {
			setProgress("6");
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据生成任务失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 写入MQ
		final String msg = taskId;
		jmsQueueTemplate.send(BmsPackmaterialTaskTypeEnum.IMPORTWMS.getCode(), new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
		
		setProgress("5");
		map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "操作成功");
		return map;
	}
	
	/**
	 * 校验文件名称
	 * @param fileName
	 * @return
	 */
	private boolean checkRegFileName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return false;
		}
		
		// 上海仓201805耗材导入-01.xlsx
		String regEx = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+仓[0-9]{6}耗材导入[-0-9]*(.xls|.xlsx)$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}
	
	 /**
		 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
		 * @return
		 */
	    @Expose
		public String getProgress(){
	    	if(redisClient.exists(sessionId, nameSpace)){
	    		return redisClient.get(sessionId, nameSpace, null);
	    	}else{
	    		return "";
	    	}
		}
		/**
		 *重置处理进度
		 */
	    @Expose
		public void resetProgress() {
	    	redisClient.del(sessionId, nameSpace);
		}
		/**
		 * 过期时间 1小时
		 * @param progress
		 */
		private void setProgress(String progress){
			redisClient.set(sessionId, nameSpace, progress, 60*60);
		}
		private boolean checkTitle(Sheet sheet, String[] str) {
			if(sheet == null){
				return false;
			}
			Row row = sheet.getRow(0);
			if(row.getPhysicalNumberOfCells() < str.length){
				 return false;
			}
			for(int i = 0;i < str.length; i++){
				if(!str[i].equals(row.getCell(i).getStringCellValue())){
					return false;
				}
			}	 
			return true;
		}
}
