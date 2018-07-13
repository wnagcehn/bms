package com.jiuyescm.bms.bill.check.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.bill.check.service.IBillCheckFollowService;
import com.jiuyescm.bms.bill.check.vo.BillCheckFollowVo;
import com.jiuyescm.bms.bill.check.vo.BillCheckInfoFollowVo;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.common.enumtype.BillCheckProcessStateEnum;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.message.mail.api.IEmailService;
import com.jiuyescm.framework.message.mail.vo.EmailMessageVo;

@Controller("billCheckFollowController")
public class BillCheckFollowController {
	
	private static final Logger logger = Logger.getLogger(BillCheckFollowController.class.getName());
	
	@Autowired 
	private StorageClient storageClient;
	@Autowired
	private IBillCheckFollowService billCheckFollowService;
	@Autowired
	private IBillCheckLogService billCheckLogService;
	@Autowired
	private IBmsGroupUserService bmsGroupUserService;
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired
	private IEmailService emailService;
	
	@DataProvider
	public void queryAll(Page<BillCheckInfoFollowVo> page,Map<String,Object> parameter) throws Exception{
		if(parameter==null){
			parameter=Maps.newHashMap();
		}
		PageInfo<BillCheckInfoFollowVo> pageInfo=null;
		try{
			String userId=JAppContext.currentUserID();
			List<String> userIds=new ArrayList<String>();;
			BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(userId);		
			if(groupUser!=null){//加入權限組
				//判断是否是管理员
				if(groupUser.getAdministrator().equals("0")){//管理员
					
				}else{
					userIds=bmsGroupUserService.queryContainUserIds(userId);
					parameter.put("userIds", userIds);
				}
				pageInfo=billCheckFollowService.queryAllCheckInfo(parameter, page.getPageNo(), page.getPageSize());
				if(page!=null){
					page.setEntities(pageInfo.getList());
					page.setEntityCount((int)pageInfo.getTotal());
				}
			}

			
		}catch(Exception e){
			logger.error("queryAll error:",e);
			throw e;
		}
	}
	/**
	 * 处理完成
	 */
	@DataResolver
	public int finishProcess(BillCheckFollowVo voEntity) throws Exception{
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

		voEntity.setFollowState(BillCheckProcessStateEnum.FINISH.getCode());
		Timestamp endTime=JAppContext.currentTimestamp();
		voEntity.setEndTime(endTime);
		int takesTime=(int)(endTime.getTime()-voEntity.getStartTime().getTime())/(1000*60*60*24);
		voEntity.setTakesTime(takesTime);
		
		BillCheckLogVo logVo=new BillCheckLogVo();
		logVo.setBillCheckId(voEntity.getBillCheckId());
		logVo.setCreator(JAppContext.currentUserName());
		logVo.setCreatorId(JAppContext.currentUserID());
		logVo.setCreateTime(JAppContext.currentTimestamp());
		logVo.setDelFlag("0");
		logVo.setOperateDesc(voEntity.getRemark());
		logVo.setLogType(1);
		logVo.setBillStatusCode(voEntity.getBillStatus());
		logVo.setFileName(voEntity.getFileName());
		logVo.setFileUrl(voEntity.getFilePath());
		logVo.setBillFollowId(voEntity.getId());
		logVo.setDeptName(groupName);
		logVo.setBillFollowState(voEntity.getFollowState());
		return billCheckFollowService.finishProcess(voEntity,logVo);
		//return 0;
	}
	
	/**
	 * 继续处理 只记录日志 
	 */
	@DataResolver
	public int continueDeal(BillCheckFollowVo voEntity) throws Exception{
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

		voEntity.setFollowState(BillCheckProcessStateEnum.PART_PROCESS.getCode());
	
		BillCheckLogVo logVo=new BillCheckLogVo();
		logVo.setBillCheckId(voEntity.getBillCheckId());
		logVo.setCreator(JAppContext.currentUserName());
		logVo.setCreatorId(JAppContext.currentUserID());
		logVo.setCreateTime(JAppContext.currentTimestamp());
		logVo.setDelFlag("0");
		logVo.setOperateDesc(voEntity.getRemark());
		logVo.setLogType(1);
		logVo.setBillStatusCode(voEntity.getBillStatus());
		logVo.setFileName(voEntity.getFileName());
		logVo.setFileUrl(voEntity.getFilePath());
		logVo.setBillFollowId(voEntity.getId());
		logVo.setBillFollowState(voEntity.getFollowState());
		logVo.setDeptName(groupName);
		return billCheckFollowService.continueDeal(voEntity,logVo);
		//return 0;
	}
	/**
	 * 问题指派
	 * @throws Exception 
	 */
	@DataResolver
	public int questionZP(BillCheckFollowVo voEntity) throws Exception{
		
		//已经指派
	/*	if(billCheckFollowService.checkFollowManExist(voEntity.getFollowManId())){
			throw new Exception("【"+voEntity.getFollowMan()+"】已经跟进此账单，不可重复指派!");
		}*/
		
		//判断指派的人是否有处理中的任务
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("billCheckId", voEntity.getBillCheckId());
		condition.put("isFinish", "FINISH");
		condition.put("followManId", voEntity.getFollowManId());
		PageInfo<BillCheckInfoFollowVo> pageInfo=billCheckFollowService.queryAllCheckInfo(condition, 0, Integer.MAX_VALUE);
		if(pageInfo!=null && pageInfo.getList().size()>0){
			throw new Exception("【"+voEntity.getFollowMan()+"】有处理中的账单，不可指派!");
		}
		/*
		if(voEntity.getFollowManId().equals(JAppContext.currentUserID())){
			throw new Exception("不可以指派问题给自己");
		}*/
		voEntity.setAppointMan(JAppContext.currentUserName());
		voEntity.setAppointManId(JAppContext.currentUserID());
		voEntity.setAppointDeptId(JAppContext.currentDeptID());
		voEntity.setAppointDept(JAppContext.currentDeptName());
		voEntity.setStartTime(JAppContext.currentTimestamp());
		voEntity.setFollowState(BillCheckProcessStateEnum.OPEN.getCode());//开启
		voEntity.setDelFlag("0");
		
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

		
		BillCheckLogVo logVo=new BillCheckLogVo();
		logVo.setBillCheckId(voEntity.getBillCheckId());
		logVo.setCreator(JAppContext.currentUserName());
		logVo.setCreatorId(JAppContext.currentUserID());
		logVo.setCreateTime(JAppContext.currentTimestamp());
		logVo.setDelFlag("0");
		logVo.setOperateDesc("问题指派");
		logVo.setLogType(1);
		logVo.setBillStatusCode(voEntity.getBillStatus());
		logVo.setBillFollowId(voEntity.getId());
		logVo.setBillFollowState(voEntity.getFollowState());
		logVo.setFileName(voEntity.getFileName());
		logVo.setFileUrl(voEntity.getFilePath());
		logVo.setDeptName(groupName);
		
		int result=billCheckFollowService.addBillCheckFollow(voEntity, logVo);
		if(result>0){
			//发送邮件
	        EmailMessageVo message = new EmailMessageVo();
	        String[] to = {voEntity.getEmail()};
	        message.setTo(to);
	        message.setSubject("【账单问题】账单("+voEntity.getBillName()+")有问题指派给您，请登录BMS系统查看");
	        StringBuffer content=new StringBuffer();
	        content.append("详细描述："+voEntity.getRemark()+"<br>");
	        content.append("BMS登录地址：https://bms.jiuyescm.com/bdf2.core.view.frame.main.MainFrame3.d");
	        System.out.println(content);
	        message.setContent(content+"");
		    emailService.sendEmailMessage(message);
		}
		
		return result;
	}
	
	
	/**
	 * 批量账单指派
	 * @throws Exception 
	 */
	@DataResolver
	public String questionZPAll(BillCheckFollowVo voEntity) throws Exception{
		String returnResult="";
		
		String billName=voEntity.getBillName();
		String[] billCheckIds;
		if(StringUtils.isNotBlank(billName)){
			billCheckIds=billName.split("&");
			if(billCheckIds.length>0){			
				//验证数据的准确性
				for(String item:billCheckIds){
					int billCheckId=Integer.parseInt(item);
					//查询账单
					Map<String, Object> condition=new HashMap<String, Object>();
					condition.put("id", billCheckId);
					BillCheckInfoVo bInfoVo=billCheckInfoService.queryBillCheck(condition);
					if(bInfoVo!=null){
						//判断有没有不符合条件的
						//1.只有待确认的才能指派
						if(!"TB_CONFIRMED".equals(bInfoVo.getBillStatus())){
							returnResult+="(月份："+bInfoVo.getCreateMonth()+",账单名称:"+bInfoVo.getBillName()+")不是待确认状态"+"&";
						}else{
							//坏账申请中的无法指派
							if("1".equals(bInfoVo.getIsapplyBad())){
								returnResult+="(月份："+bInfoVo.getCreateMonth()+",账单名称:"+bInfoVo.getBillName()+")坏账申请状态"+"&";
							}else{
								////判断指派的人是否有处理中的任务
								condition.clear();
								condition.put("billCheckId", billCheckId);
								condition.put("isFinish", "FINISH");
								condition.put("followManId", voEntity.getFollowManId());
								PageInfo<BillCheckInfoFollowVo> pageInfo=billCheckFollowService.queryAllCheckInfo(condition, 0, Integer.MAX_VALUE);
								if(pageInfo!=null && pageInfo.getList().size()>0){
									returnResult+="【"+voEntity.getFollowMan()+"】正在处理(月份："+bInfoVo.getCreateMonth()+",账单名称"+bInfoVo.getBillName()+")"+"&";
								}
							}
						}
					}else{
						returnResult+="账单不存在&";
					}
				}
				//有问题就返回
				if(StringUtils.isNotBlank(returnResult)){
					return returnResult;
				}
				
				for(String item:billCheckIds){
					int billCheckId=Integer.parseInt(item);
					//查询账单
					Map<String, Object> condition=new HashMap<String, Object>();
					condition.put("id", billCheckId);
					BillCheckInfoVo bInfoVo=billCheckInfoService.queryBillCheck(condition);	
					if(bInfoVo!=null){
						voEntity.setBillCheckId(billCheckId);
						voEntity.setBillStatus(bInfoVo.getBillStatus());
						voEntity.setAppointMan(JAppContext.currentUserName());
						voEntity.setAppointManId(JAppContext.currentUserID());
						voEntity.setAppointDeptId(JAppContext.currentDeptID());
						voEntity.setAppointDept(JAppContext.currentDeptName());
						voEntity.setStartTime(JAppContext.currentTimestamp());
						voEntity.setFollowState(BillCheckProcessStateEnum.OPEN.getCode());//开启
						voEntity.setDelFlag("0");
						
						String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

						BillCheckLogVo logVo=new BillCheckLogVo();
						logVo.setBillCheckId(billCheckId);
						logVo.setCreator(JAppContext.currentUserName());
						logVo.setCreatorId(JAppContext.currentUserID());
						logVo.setCreateTime(JAppContext.currentTimestamp());
						logVo.setDelFlag("0");
						logVo.setOperateDesc("问题指派");
						logVo.setLogType(1);
						logVo.setBillStatusCode(bInfoVo.getBillStatus());
						logVo.setBillFollowId(voEntity.getId());
						logVo.setBillFollowState(voEntity.getFollowState());
						logVo.setFileName(voEntity.getFileName());
						logVo.setFileUrl(voEntity.getFilePath());
						logVo.setDeptName(groupName);
						
						int result=billCheckFollowService.addBillCheckFollow(voEntity, logVo);
						if(result>0){
							//发送邮件
					        EmailMessageVo message = new EmailMessageVo();
					        String[] to = {voEntity.getEmail()};
					        message.setTo(to);
					        message.setSubject("【账单问题】账单("+bInfoVo.getBillName()+")有问题指派给您，请登录BMS系统查看");
					        StringBuffer content=new StringBuffer();
					        content.append("详细描述："+voEntity.getRemark()+"<br>");
					        content.append("BMS登录地址：https://bms.jiuyescm.com/bdf2.core.view.frame.main.MainFrame3.d");
					        message.setContent(content+"");
						    emailService.sendEmailMessage(message);
						}				
					}
					
				}
				return "SUCESS";
			}
		}
		return returnResult;
	}
	
	@FileResolver
	public String uploadFile(final UploadFile file, Map<String, Object> parameter) throws IOException {
		 long length = file.getSize();
		 StorePath storePath = storageClient.uploadFile(file.getInputStream(), length, "xlsx");
		 String fullPath = storePath.getFullPath();
		 return fullPath;
	}
	
	@DataProvider
	public Map<String,String> getProcessState(){
		return BillCheckProcessStateEnum.getMap();
	}
}
