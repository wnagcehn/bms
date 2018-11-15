package com.jiuyescm.bms.billcheck.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.bill.check.service.IBillCheckFollowService;
import com.jiuyescm.bms.bill.check.vo.BillCheckInfoFollowVo;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInvoiceService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.service.IBillCheckReceiptService;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.bms.billcheck.vo.BillAccountInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillAccountOutVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.file.templet.IBmsTempletInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadCallback;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Controller("billCheckInfoController")
public class BillCheckInfoController{

	@Autowired 
	private StorageClient storageClient;
	
	@Autowired 
	private IBmsTempletInfoService bmsTempletInfoService;
	
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired
	private IBillCheckLogService billCheckLogService;
	
	@Autowired
	private IBillCheckInvoiceService billCheckInvoiceService;
	
	@Autowired
	private IBillCheckReceiptService billCheckReceiptService;
	
	@Resource 
	private ISystemCodeService systemCodeService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IBmsGroupUserService bmsGroupUserService;
	
	@Autowired
	private IBillCheckFollowService billCheckFollowService;
	
	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IBmsBillAccountInService bmsBillAccountInService;
	
	@Autowired
	private IBmsAccountInfoService bmsAccountInfoService;
	
	@Autowired
	private IBmsAccountOutService bmsAccountOutService;
	
	@Autowired
    private IBillCheckLogRepository billCheckLogRepository;
	
	final int pageSize = 10000;
	
	private static final Logger logger = Logger.getLogger(BillCheckInfoController.class.getName());

	/**
	 * 分页查询账单主表
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BillCheckInfoVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}		
		List<String> userIds=new ArrayList<String>();
		BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if(groupUser!=null){//加入權限組
			//判断是否是管理员
			if("0".equals(groupUser.getAdministrator())){//管理员
				
			}else{//非管理员
				userIds=bmsGroupUserService.queryContainUserIds(groupUser);
				StringBuffer user=new StringBuffer();
				for(int i=0;i<userIds.size();i++){
					if(i==userIds.size()-1){
						user.append(userIds.get(i));
					}else{
						user.append(userIds.get(i)+"|");
					}				
				}
				if(StringUtils.isNotBlank(user)){
					param.put("userIds", user);
				}
			}		
			
			PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}	
		}
	}
	
	/**
	 * 判断用户权限
	 * @return
	 */
	@Expose
	public BmsGroupUserVo getUserDetail(){
		BmsGroupUserVo vo=new BmsGroupUserVo();
		Map<String, Object> condition=new HashMap<>();
		condition.put("userId", JAppContext.currentUserID());
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

		PageInfo<BmsGroupUserVo> listInfo;
		try {
			listInfo = bmsGroupUserService.query(condition, 0, Integer.MAX_VALUE);
			if(listInfo!=null && listInfo.getList().size()>0){
				vo=listInfo.getList().get(0);
				vo.setGroupName(groupName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vo;
	}
		
	/**
	 * 分页查询账单日志
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryLogAll(Page<BillCheckLogVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<BillCheckLogVo> pageInfo = billCheckLogService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询账单发票信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryInvoiceAll(Page<BillCheckInvoiceVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<BillCheckInvoiceVo> pageInfo = billCheckInvoiceService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询账单回款信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryReceiptAll(Page<BillCheckReceiptVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}	
		PageInfo<BillCheckReceiptVo> pageInfo = billCheckReceiptService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 分页查询账单回款反馈信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryReceiptFollowAll(Page<BillReceiptFollowVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}	
		PageInfo<BillReceiptFollowVo> pageInfo = billCheckInfoService.queryReceiptFollow(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/** 分页查询账单主表
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public List<BillCheckAdjustInfoVo> queryAllAdjust(Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}		
		List<BillCheckAdjustInfoVo> list = billCheckInfoService.queryAdjust(param);
		
		return list;
	}
	
	@DataProvider
	public List<BillCheckInfoVo> queryModify(Map<String, Object> param) {
		List<BillCheckInfoVo> queryList=new ArrayList<>();
		
		BillCheckInfoVo en=billCheckInfoService.queryOne(param);
		if(en!=null){
			//开票信息
			BillCheckInvoiceVo billCheckInvoiceVo=billCheckInvoiceService.queryInvoice(param);
			if(billCheckInvoiceVo!=null){
				en.setInvoiceAmount(billCheckInvoiceVo.getInvoiceAmount());
				en.setInvoiceDate(billCheckInvoiceVo.getInvoiceDate());
			}
			//回款信息
			BillCheckReceiptVo billCheckReceiptVo=billCheckReceiptService.queyReceipt(param);
			if(billCheckReceiptVo!=null){
				//调整金额
				BigDecimal adjustAmount = new BigDecimal(0);
				param.clear();
				param.put("billCheckId", param.get("id"));
				List<BillCheckAdjustInfoVo> list = billCheckInfoService.queryAdjust(param);
				if(list.size()>0){
					BillCheckAdjustInfoVo entity=list.get(0);
					if(entity.getAdjustAmount()!=null){
						adjustAmount=entity.getAdjustAmount();
					}
				}
				
				//应收金额
				BigDecimal receiptMoney=new BigDecimal(0);
				if(billCheckReceiptVo.getReceiptAmount()!=null){
					receiptMoney=billCheckReceiptVo.getReceiptAmount();
				}
				
				//总金额
				BigDecimal totalMoney=new BigDecimal(0);
				totalMoney=totalMoney.add(adjustAmount);
				if(en.getConfirmAmount()!=null){
					totalMoney=totalMoney.add(en.getConfirmAmount());
				}
				
				if(receiptMoney.compareTo(BigDecimal.ZERO)==0){
					en.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());
				}else if(totalMoney.compareTo(receiptMoney)>0){
					en.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());
				}else if(totalMoney.compareTo(receiptMoney)==0){
					en.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());
				}else if(totalMoney.compareTo(receiptMoney)<0){
					en.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());
				}
				en.setReceiptDate(billCheckReceiptVo.getReceiptDate());
			}
		}
		queryList.add(en);
		return queryList;
	}
	
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<BillCheckInfoVo> datas){
		
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserID();
			String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
			for(BillCheckInfoVo temp:datas){
				if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){				
					//验证商家合同名称是否存在
					//获取所有商家
					Map<String, Object> customerInfoMap=getCusMap();
					if(!customerInfoMap.containsKey(temp.getInvoiceName())){
						return "商家合同名称:"+temp.getInvoiceName()+"不存在";
					}
					
					//唯一性校验
					//查询账单
					Map<String, Object> condition=new HashMap<String, Object>();
					condition.put("createMonth",temp.getCreateMonth());
					condition.put("invoiceName",temp.getInvoiceName());
					condition.put("billName", temp.getBillName());
					BillCheckInfoVo bInfoVo=billCheckInfoService.queryOne(condition);
					if(bInfoVo!=null && bInfoVo.getId()!=null && !bInfoVo.getId().equals(temp.getId())){
						return "(业务月份:"+bInfoVo.getCreateMonth()+",商家合同名称:"+bInfoVo.getInvoiceName()+",账单名称:"+bInfoVo.getBillName()+")已存在";
					}
					
					if(!temp.getBillStatus().equals(CheckBillStatusEnum.BAD_BILL.getCode())){
						//验证
						//调整的金额
						BigDecimal adjustAmount=new BigDecimal(0);
						Map<String, Object> param=new HashMap<String, Object>();
						param.put("billCheckId", temp.getId());
						List<BillCheckAdjustInfoVo> adjustList = billCheckInfoService.queryAdjust(param);
						if(adjustList.size()>0){
							BillCheckAdjustInfoVo entity=adjustList.get(0);
							if(entity.getAdjustAmount()!=null){
								adjustAmount=entity.getAdjustAmount();
							}
						}
						//最终确认额
						BigDecimal confirmAmount=new BigDecimal(0);
						if(temp.getConfirmAmount()!=null){
							confirmAmount=temp.getConfirmAmount();
						}			
						//发票金额
						BigDecimal invoiceAmount=new BigDecimal(0);
						if(temp.getInvoiceAmount()!=null){
							invoiceAmount=temp.getInvoiceAmount();
						}
						//收款金额
						BigDecimal receiptAmount=new BigDecimal(0);
						if(temp.getReceiptAmount()!=null){
							receiptAmount=temp.getReceiptAmount();
						}

						//未收款金额
						temp.setUnReceiptAmount(confirmAmount.add(adjustAmount).subtract(receiptAmount));
						//已确认未开票金额
						temp.setConfirmUnInvoiceAmount(confirmAmount.subtract(invoiceAmount));
						
						//2.发票状态修改
						if(confirmAmount.compareTo(invoiceAmount)==0){
							temp.setInvoiceStatus(BillCheckInvoiceStateEnum.INVOICED.getCode());//已开票
						}else if(confirmAmount.compareTo(invoiceAmount)>0){
							if(invoiceAmount.compareTo(BigDecimal.ZERO)==0){
								temp.setInvoiceStatus(BillCheckInvoiceStateEnum.NO_INVOICE.getCode());//未开票
							}else{
								temp.setInvoiceStatus(BillCheckInvoiceStateEnum.PART_INVOICE.getCode());//部分开票
							}
						}	
						//判断是否需要发票
						if("0".equals(temp.getIsneedInvoice())){
							temp.setInvoiceStatus(BillCheckInvoiceStateEnum.UNNEED_INVOICE.getCode());//不需要开票
						}
						
						//3.收款状态判断				
						if((adjustAmount.add(confirmAmount)).compareTo(receiptAmount)==0){
							//回款状态
							temp.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());//已收款
						}else if((adjustAmount.add(confirmAmount)).compareTo(receiptAmount)>0|| (adjustAmount.add(confirmAmount)).compareTo(receiptAmount)<0){
							//回款状态
							if(receiptAmount.compareTo(BigDecimal.ZERO)==0){
								temp.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());//未收款
							}else{
								temp.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());//部分收款

							}
						
						}				
						
						//账单状态修改
						//1）如果对账状态为“已确认”and是否需要开票为“是”and开票状态为“未开票”，将账单状态置为“待开票”；
						//2）如果对账状态为“已确认”and是否需要开票为“是”and开票状态为“部分开票”or“已开票”，将账单状态置为“待收款”；
						//3）如果对账状态为“已确认”and是否需要开票为“否”，将账单状态置为“待收款”；
						//4）如果对账状态不为“已确认”，账单状态为“待确认”不变；
						if(BillCheckStateEnum.CONFIRMED.getCode().equals(temp.getBillCheckStatus()) && "1".equals(temp.getIsneedInvoice()) && BillCheckInvoiceStateEnum.NO_INVOICE.getCode().equals(temp.getInvoiceStatus())){
							temp.setBillStatus(CheckBillStatusEnum.TB_INVOICE.getCode());
						}else if(BillCheckStateEnum.CONFIRMED.getCode().equals(temp.getBillCheckStatus()) && "1".equals(temp.getIsneedInvoice()) && (BillCheckInvoiceStateEnum.PART_INVOICE.getCode().equals(temp.getInvoiceStatus()) || BillCheckInvoiceStateEnum.INVOICED.getCode().equals(temp.getInvoiceStatus()))){
							temp.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
						}else if(BillCheckStateEnum.CONFIRMED.getCode().equals(temp.getBillCheckStatus()) && "0".equals(temp.getIsneedInvoice())){
							temp.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
						}else{
							temp.setBillStatus(CheckBillStatusEnum.TB_CONFIRMED.getCode());
						}
					}
					
					//****** 修改模板 ******
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					int result=billCheckInfoService.update(temp);
					if(result<=0){
						return "修改失败";
					}
					
					BillCheckLogVo vo=new BillCheckLogVo();
					vo.setBillCheckId(temp.getId());
					vo.setBillStatusCode(temp.getBillStatus());
					vo.setLogType(0);
				
					vo.setOperateDesc(temp.getOperDesc());
					vo.setCreatorId(JAppContext.currentUserID());
					vo.setCreator(JAppContext.currentUserName());
					vo.setCreateTime(JAppContext.currentTimestamp());
					vo.setDeptName(groupName);
					
					billCheckLogService.addBillCheckLog(vo);
				}
			}
			return "sucess";
			
		} catch (Exception e) {
			logger.error("【程序异常】",e);
			return "【ERROR】"+e.getMessage();
		}
	}
	/**
	 * 确认账单  add  by  wuliangfeng
	 */
	@DataResolver
	public String confirmBillCheckInfo(BillCheckInfoVo billCheckInfoVo) throws Exception{
		String billStatus=billCheckInfoService.getBillCheckStatus(billCheckInfoVo.getId());
		//BillCheckInfoVo checkVo=billCheckInfoService.queryBillCheckById(billCheckInfoVo.getId());
		//非确认状态下无法确认
		if(StringUtils.isBlank(billStatus)||!billStatus.equals(CheckBillStatusEnum.TB_CONFIRMED.getCode())){
			return "账单【"+billCheckInfoVo.getBillName()+"】为【"+CheckBillStatusEnum.getMap().get(billStatus)+"】状态，不可确认";
		}
		//确认账单时必须是任务全都是处理结束的状态
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("billCheckId", billCheckInfoVo.getId());
		condition.put("isFinish", "FINISH");
		PageInfo<BillCheckInfoFollowVo> pageInfo=billCheckFollowService.queryAllCheckInfo(condition, 0, Integer.MAX_VALUE);
		if(pageInfo!=null && pageInfo.getList().size()>0){
			return "账单【"+billCheckInfoVo.getBillName()+"】有正在处理中的任务,不可确认";
		}
		
		if(billCheckInfoVo.getIsneedInvoice().equals("0")){//不需要发票
			billCheckInfoVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());//待收款
		}else{
			billCheckInfoVo.setBillStatus(CheckBillStatusEnum.TB_INVOICE.getCode());//待开票
		}
		billCheckInfoVo.setLastModifier(JAppContext.currentUserName());
		billCheckInfoVo.setLastModifyTime(JAppContext.currentTimestamp());
		billCheckInfoVo.setConfirmMan(JAppContext.currentUserName());
		billCheckInfoVo.setConfirmManId(JAppContext.currentUserID());
		billCheckInfoVo.setConfirmDate(JAppContext.currentTimestamp());
		
		//添加日志
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
		BillCheckLogVo vo=new BillCheckLogVo();
		vo.setBillCheckId(billCheckInfoVo.getId());
		vo.setBillStatusCode(billCheckInfoVo.getBillStatus());
		vo.setLogType(0);
		vo.setOperateDesc(billCheckInfoVo.getOperDesc());
		vo.setCreator(JAppContext.currentUserName());
		vo.setCreatorId(JAppContext.currentUserID());
		vo.setCreateTime(JAppContext.currentTimestamp());
		vo.setDeptName(groupName);
		int k=billCheckInfoService.confirmBillCheckInfo(billCheckInfoVo,vo);
		if(k>0){
			return "确认账单成功!";
		}else{
			return "确认账单失败!";
		}
	}
	
	/**
	 * 更新账单
	 * @param billCheckInfoVo
	 * @return
	 */
	@DataResolver
	public String updateBill(BillCheckInfoVo billCheckInfoVo){
		billCheckInfoVo.setLastModifier(JAppContext.currentUserName());
		billCheckInfoVo.setLastModifyTime(JAppContext.currentTimestamp());
		int result=billCheckInfoService.update(billCheckInfoVo);
		if(result<0){
			return "更新账单失败";
		}		
		return "更新账单成功";
	}
	
	/**
	 * 更新账单状态
	 * @param billCheckInfoVo
	 * @return
	 */
	@DataResolver
	public String updateBillStatus(BillCheckInfoVo billCheckInfoVo){
		if(billCheckInfoVo!=null && billCheckInfoVo.getOperDesc()!=null){
			if(billCheckInfoVo.getOperDesc().contains("作废账单")){
				billCheckInfoVo.setBillStatus(CheckBillStatusEnum.INVALIDATE.getCode());
			}else{
				billCheckInfoVo.setBillStatus(CheckBillStatusEnum.TB_CONFIRMED.getCode());
			}
		}
		billCheckInfoVo.setLastModifier(JAppContext.currentUserName());
		billCheckInfoVo.setLastModifyTime(JAppContext.currentTimestamp());
		int result=billCheckInfoService.update(billCheckInfoVo);
		if(result<0){
			return "确认账单失败";
		}
		
		try {
			
			String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

			
			BillCheckLogVo vo=new BillCheckLogVo();
			vo.setBillCheckId(billCheckInfoVo.getId());
			vo.setBillStatusCode(billCheckInfoVo.getBillCheckStatus());
			vo.setLogType(0);
			vo.setOperateDesc(billCheckInfoVo.getOperDesc());
			vo.setCreator(JAppContext.currentUserName());
			vo.setCreatorId(JAppContext.currentUserID());
			vo.setCreateTime(JAppContext.currentTimestamp());
			vo.setDeptName(groupName);
			
			billCheckLogService.addBillCheckLog(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "sucess";
	}
	
	/**
	 * 收款完成操作(批量操作，满足一下条件的1.状态必须为“待收款”
	 *           2.收款金额必须等于（最终确认额+调整金额） )
	 * @param checkInfoVo
	 * @return
	 */
	@DataResolver
	public String finishBill(){		
		Map<String, Object> param=new HashMap<String, Object>();
		List<String> userIds=new ArrayList<String>();
		BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if(groupUser!=null){//加入權限組
			//判断是否是管理员
			if(groupUser.getAdministrator().equals("0")){//管理员
				
			}else{//非管理员
				userIds=bmsGroupUserService.queryContainUserIds(groupUser);
				StringBuffer user=new StringBuffer();
				for(int i=0;i<userIds.size();i++){
					if(i==userIds.size()-1){
						user.append(userIds.get(i));
					}else{
						user.append(userIds.get(i)+"|");
					}
				}
				param.put("userIds", userIds);			
			}			
			PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, 0, Integer.MAX_VALUE);
			try {
				if(pageInfo!=null && pageInfo.getList().size()>0){
					List<BillCheckInfoVo> list=pageInfo.getList();
					for(BillCheckInfoVo vo:list){
						
						
						//1.状态必须为“待收款” 已确认未开票金额必须大于等于0,为负数时，不允许一键收款
						if(CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus()) && vo.getConfirmUnInvoiceAmount()!=null && vo.getConfirmUnInvoiceAmount().compareTo(BigDecimal.ZERO)>=0){
							//确认金额+调整金额
							BigDecimal totalAmount=new BigDecimal(0);	
							if(vo.getConfirmAmount()!=null){
								totalAmount=vo.getConfirmAmount();
							}
							param.clear();
							param.put("billCheckId", vo.getId());
							List<BillCheckAdjustInfoVo> adjustList = billCheckInfoService.queryAdjust(param);
							if(adjustList.size()>0){
								BillCheckAdjustInfoVo entity=adjustList.get(0);
								if(entity.getAdjustAmount()!=null){
									totalAmount=totalAmount.add(entity.getAdjustAmount());
								}				
							}
							
							//收款金额
							BigDecimal receiptAmount=new BigDecimal(0);
							if(vo.getReceiptAmount()!=null){
								receiptAmount=vo.getReceiptAmount();
							}
							
							if(totalAmount.compareTo(receiptAmount)==0){
								//账单状态修改
								vo.setBillStatus(CheckBillStatusEnum.RECEIPTED.getCode());
								vo.setLastModifier(JAppContext.currentUserName());
								vo.setLastModifierId(JAppContext.currentUserID());
								vo.setLastModifyTime(JAppContext.currentTimestamp());
								billCheckInfoService.update(vo);
							}
			
						}
					}
				}
				return "操作成功";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "操作失败";
			}
		}
		
		return "操作失败";
	}
	
	/**
	 * 新增日志
	 * @param billCheckLogVo
	 */
	@DataResolver
	public void addLog(BillCheckLogVo billCheckLogVo){

/*		DeptVO dept=deptService.findDeptByUserId(JAppContext.currentUserID());
*/
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
		try {
			billCheckLogVo.setLogType(0);
			billCheckLogVo.setCreator(JAppContext.currentUserName());
			billCheckLogVo.setCreateTime(JAppContext.currentTimestamp());
			billCheckLogVo.setCreatorId(JAppContext.currentUserID());
			billCheckLogVo.setDeptName(groupName);
			billCheckLogVo.setDelFlag("0");
			
			billCheckLogService.addBillCheckLog(billCheckLogVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 回款反馈
	 * @throws Exception 
	 */
	@DataResolver
	public String insertReturnFollow(BillReceiptFollowVo voEntity) throws Exception{
		
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

		voEntity.setCreateTime(JAppContext.currentTimestamp());
		voEntity.setCreator(JAppContext.currentUserName());
		voEntity.setDelFlag("0");
		int result=billCheckInfoService.saveReceiptFollow(voEntity);
		
		if(result<=0){
			return "保存回款反馈失败";
		}
		
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("id", voEntity.getBillCheckId());
		BillCheckInfoVo bInfoVo=billCheckInfoService.queryOne(condition);
		
		BillCheckLogVo logVo=new BillCheckLogVo();
		logVo.setBillCheckId(voEntity.getBillCheckId());
		logVo.setBillStatusCode(bInfoVo.getBillStatus());
		logVo.setCreator(JAppContext.currentUserName());
		logVo.setCreatorId(JAppContext.currentUserID());
		logVo.setCreateTime(JAppContext.currentTimestamp());
		logVo.setDeptName(groupName);
		logVo.setDelFlag("0");
		logVo.setOperateDesc("回款反馈");
		logVo.setLogType(0);
		logVo.setFileName(voEntity.getFileName());
		logVo.setFileUrl(voEntity.getFileUrl());
		
		billCheckLogService.addBillCheckLog(logVo);
		return "保存成功";
	}
	
	/**
	 * 删除发票
	 * @return
	 */
	@DataResolver
	public String deleteInvoice(BillCheckInvoiceVo invoiceVo){
		invoiceVo.setLastModifier(JAppContext.currentUserName());
		invoiceVo.setLastModifyTime(JAppContext.currentTimestamp());
		invoiceVo.setDelFlag("1");
		int result=billCheckInvoiceService.update(invoiceVo);
		if(result<=0){
			return "删除发票失败";
		}
		
		//查询账单
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("id", invoiceVo.getBillCheckId());
		BillCheckInfoVo bInfoVo=billCheckInfoService.queryOne(condition);
		
		String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

		BillCheckLogVo logVo=new BillCheckLogVo();
		logVo.setBillCheckId(invoiceVo.getBillCheckId());
		logVo.setBillStatusCode(bInfoVo.getBillStatus());
		logVo.setCreator(JAppContext.currentUserName());
		logVo.setCreatorId(JAppContext.currentUserID());
		logVo.setCreateTime(JAppContext.currentTimestamp());
		logVo.setDeptName(groupName);
		logVo.setDelFlag("0");
		logVo.setOperateDesc("删除发票"+invoiceVo.getInvoiceNo());
		logVo.setLogType(0);
		try {
			billCheckLogService.addBillCheckLog(logVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "删除发票成功";
	}
	
	/**
	 * 删除回款信息
	 * @return
	 */
	@DataResolver
	public String deleteReceipt(BillCheckReceiptVo receiptVo){
		Timestamp creTime = JAppContext.currentTimestamp();
		String creator = JAppContext.currentUserName();
		String creatorId = JAppContext.currentUserID();
		
		receiptVo.setLastModifier(creator);
		receiptVo.setLastModifyTime(creTime);
		receiptVo.setDelFlag("1");
		
		if(receiptVo.getReceiptType().equals("预收款")){
			BigDecimal receiptAmount = receiptVo.getReceiptAmount();
			Long id = receiptVo.getId();
			//查询账单表
			Map<String, Object> conditionCheckInfo =new HashMap<String, Object>();
			conditionCheckInfo.put("id", receiptVo.getBillCheckId());
			BillCheckInfoVo billCheckInfoVo = billCheckInfoService.query(conditionCheckInfo, 1, 20).getList().get(0);
			//查询账户表
			Map<String, Object> conditionAccountInfo =new HashMap<String, Object>();
			conditionAccountInfo.put("customerName", billCheckInfoVo.getInvoiceName());
			BillAccountInfoVo accountVo = bmsAccountInfoService.query(conditionAccountInfo, 1, 20).getList().get(0);
			//修改账单表
			billCheckInfoVo.setUnReceiptAmount(billCheckInfoVo.getUnReceiptAmount().add(receiptAmount));
			billCheckInfoService.update(billCheckInfoVo);
			//修改账户表
			accountVo.setAmount(accountVo.getAmount().add(receiptAmount));
			bmsAccountInfoService.update(accountVo);
			//插入支出表
			BillAccountOutVo billAccountOutVo = new BillAccountOutVo();
			billAccountOutVo.setAccountNo(billCheckInfoVo.getInvoiceName());
			billAccountOutVo.setBillCheckId(receiptVo.getBillCheckId());
			billAccountOutVo.setCreateTime(creTime);
			billAccountOutVo.setCreator(creator);
			billAccountOutVo.setCreatorId(creatorId);
			billAccountOutVo.setDelFlag("0");
			billAccountOutVo.setOutType("1");
			billAccountOutVo.setAmount(receiptAmount.negate());
			bmsAccountOutService.saveInfo(billAccountOutVo);
			//插入日志表
			BillCheckLogEntity log = new BillCheckLogEntity();
			log.setBillCheckId(receiptVo.getBillCheckId());
			log.setCreateTime(creTime);
			log.setCreator(creator);
			log.setCreatorId(creatorId);
			log.setBillStatusCode(billCheckInfoVo.getBillStatus());
			log.setDelFlag("0");
			log.setLogType(0);
			log.setOperateDesc("回款删除:预收款冲抵");
			billCheckLogRepository.addCheckLog(log);
			//修改回款表
			Map<String, Object> conditionReceipt =new HashMap<String, Object>();
			conditionReceipt.put("id", id);
			BillCheckReceiptVo receipt = billCheckReceiptService.query(conditionReceipt, 1, 20).getList().get(0);
			receipt.setLastModifier(creator);
			receipt.setLastModifyTime(creTime);
			receipt.setDelFlag("1");
			billCheckReceiptService.update(receipt);
		}else{
			int result=billCheckReceiptService.update(receiptVo);
			if(result<=0){
				return "删除回款失败";
			}
			
			//查询账单
			Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("id", receiptVo.getBillCheckId());
			BillCheckInfoVo bInfoVo=billCheckInfoService.queryOne(condition);
			
			String groupName=bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());

			BillCheckLogVo logVo=new BillCheckLogVo();
			logVo.setBillStatusCode(bInfoVo.getBillStatus());
			logVo.setBillCheckId(receiptVo.getBillCheckId());
			logVo.setCreator(JAppContext.currentUserName());
			logVo.setCreatorId(JAppContext.currentUserID());
			logVo.setCreateTime(JAppContext.currentTimestamp());
			logVo.setDeptName(groupName);
			logVo.setDelFlag("0");
			logVo.setOperateDesc("删除回款");
			logVo.setLogType(0);
			try {
				billCheckLogService.addBillCheckLog(logVo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "删除回款成功";
	}
	
	/**
	 * 
	 */
	/**
	 * 分页查询预警账单
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryWarn(Page<BillCheckInfoVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}						
		PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryWarn(param, page.getPageNo(), page.getPageSize());	
		PageInfo<BillCheckInfoVo> newPageInfo =getNew(pageInfo);	
		page.setEntities(newPageInfo.getList());
		page.setEntityCount((int) newPageInfo.getTotal());	
	}
	
	/**
	 * 根据权限获取预警账单
	 */
	public PageInfo<BillCheckInfoVo> getNew(PageInfo<BillCheckInfoVo> pageInfo){
		List<String> userIds=new ArrayList<String>();
		BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		
		PageInfo<BillCheckInfoVo> result=new PageInfo<BillCheckInfoVo>();
		List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
		
		if(groupUser!=null && pageInfo!=null && pageInfo.getList().size()>0){//加入權限組
			//判断是否是管理员
			if("0".equals(groupUser.getAdministrator())){//管理员
				return pageInfo;	
			}else{//非管理员
				userIds=bmsGroupUserService.queryContainUserIds(groupUser);	
				if(userIds.size()>0){
					for(BillCheckInfoVo entity : pageInfo.getList()){
						if(userIds.contains(entity.getSellerId())){
							voList.add(entity);
						}
					}
					result.setList(voList);
					return result;
				}
			}		
		}
		
		return result;
	}
	
	/**
	 * 文件上传
	 * @param file
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileResolver
	public String uploadFile(final UploadFile file, Map<String, Object> parameter) throws IOException {
		 long length = file.getSize();
		 StorePath storePath = storageClient.uploadFile(file.getInputStream(), length, "xlsx");
		 String fullPath = storePath.getFullPath();
		 return fullPath;
	}
	
	/**
	 * 附件下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downFileExcel(Map<String, String> parameter) throws IOException {
		
		if(parameter!=null){
			final String filename=parameter.get("fileName");
			
			return storageClient.downloadFile(parameter.get("fileUrl"), new DownloadCallback<DownloadFile>(){
				@Override
			    public DownloadFile receive(InputStream inputStream) throws IOException {
			        return new DownloadFile(filename, inputStream);
			    }
			});
		}	
		return null;
	}
	
	
	/**
	 * 账单导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====对账账单导出：写入Excel begin.");
    	
    	Map<String, Object> dictcodeMap=new HashMap<String, Object>();
		try {
//			String filePath =getName() + FileConstant.SUFFIX_XLSX;
			//如果存放上传文件的目录不存在就新建
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "对账收款账单" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //对账账单
	    	handBillCheck(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====对账账单导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("对账账单导出失败", e);
		}
		return null;
	}
	
	
	/**
	 * 对账收款信息导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handBillCheck(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
		int pageNo = 1;
		boolean doLoop = true;
		logger.info("对账收款信息导出...");
        List<BillCheckInfoVo> dataList = new  ArrayList<BillCheckInfoVo>();
        
		if (param == null){
			param = new HashMap<String, Object>();
		}		
		List<String> userIds=new ArrayList<String>();
		BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if(groupUser!=null){//加入權限組
			//判断是否是管理员
			if("0".equals(groupUser.getAdministrator())){//管理员
				
			}else{//非管理员
				userIds=bmsGroupUserService.queryContainUserIds(groupUser);
				StringBuffer user=new StringBuffer();
				for(int i=0;i<userIds.size();i++){
					if(i==userIds.size()-1){
						user.append(userIds.get(i));
					}else{
						user.append(userIds.get(i)+"|");
					}				
				}
				if(StringUtils.isNotBlank(user)){
					param.put("userIds", user);
				}
			}
			
			while (doLoop) {
				try {
					PageInfo<BillCheckInfoVo> pageInfo= billCheckInfoService.query(param, pageNo, pageSize);
					if (null != pageInfo && pageInfo.getList().size() > 0) {
						if (pageInfo.getList().size() < pageSize) {
							doLoop = false;
						}else {
							pageNo += 1; 
						}
						dataList.addAll(pageInfo.getList());
					}else {
						doLoop = false;
					}
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("对账账单导出失败", e);
					doLoop=false;
				}
				
			}
		}

		if(dataList.size()==0){
			return;
		}
		logger.info("对账收款导出生成sheet。。。");
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"对账收款");
		
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(8, 3800);
		sheet.setColumnWidth(9, 3800);
		sheet.setColumnWidth(11, 3800);
		sheet.setColumnWidth(12, 3800);
		sheet.setColumnWidth(17, 3800);
		sheet.setColumnWidth(20, 3800);
		sheet.setColumnWidth(21, 3800);
		sheet.setColumnWidth(22, 3800);
		sheet.setColumnWidth(23, 4000);
		sheet.setColumnWidth(26, 3800);
		
		Font font = workbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
		 
		 Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellValue("业务月份");
		 cell0.setCellStyle(style);
		 Cell cell1 = row0.createCell(1);
		 cell1.setCellValue("商家合同名称");
		 cell1.setCellStyle(style);
		 Cell cell2 = row0.createCell(2);
		 cell2.setCellStyle(style);
		 cell2.setCellValue("账单名称");
		 Cell cell3 = row0.createCell(3);
		 cell3.setCellValue("业务启动时间");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(4);
		 cell4.setCellValue("账单状态");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(5);
		 cell5.setCellValue("对账状态");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(6);
		 cell6.setCellValue("开票状态");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(7);
		 cell7.setCellValue("超期状态");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(8);
		 cell8.setCellValue("销售员名称");
		 cell8.setCellStyle(style);
		 Cell cell9 = row0.createCell(9);
		 cell9.setCellValue("区域");
		 cell9.setCellStyle(style);
		 Cell cell10 = row0.createCell(10);
		 cell10.setCellValue("最终确认额");
		 cell10.setCellStyle(style);
		 Cell cell11 = row0.createCell(11);
		 cell11.setCellValue("确认日期");
		 cell11.setCellStyle(style);
		 Cell cell12 = row0.createCell(12);
		 cell12.setCellValue("发票金额");
		 cell12.setCellStyle(style);
		 Cell cell13 = row0.createCell(13);
		 cell13.setCellValue("开票日期");
		 cell13.setCellStyle(style);
		 Cell cell14 = row0.createCell(14);
		 cell14.setCellValue("预计回款日期");
		 cell14.setCellStyle(style);
		 Cell cell15 = row0.createCell(15);
		 cell15.setCellValue("收款金额");
		 cell15.setCellStyle(style);
		 Cell cell16 = row0.createCell(16);
		 cell16.setCellValue("收款日期");
		 cell16.setCellStyle(style);
		 Cell cell17 = row0.createCell(17);
		 cell17.setCellValue("结算员");
		 cell17.setCellStyle(style);
		 Cell cell18 = row0.createCell(18);
		 cell18.setCellValue("项目管理员");
		 cell18.setCellStyle(style);
		 Cell cell19 = row0.createCell(19);
		 cell19.setCellValue("责任部门名称");
		 cell19.setCellStyle(style);
		 Cell cell20 = row0.createCell(20);
		 cell20.setCellValue("项目");
		 cell20.setCellStyle(style);
		 Cell cell21 = row0.createCell(21);
		 cell21.setCellValue("一级品类");
		 cell21.setCellStyle(style);
		 Cell cell22 = row0.createCell(22);
		 cell22.setCellValue("业务类型");
		 cell22.setCellStyle(style);
		 Cell cell23 = row0.createCell(23);
		 cell23.setCellValue("预计金额");
		 cell23.setCellStyle(style);
		 Cell cell24 = row0.createCell(24);
		 cell24.setCellValue("未收款金额");
		 cell24.setCellStyle(style);
		 Cell cell25 = row0.createCell(25);
		 cell25.setCellValue("开票未回款金额");
		 cell25.setCellStyle(style);
		 Cell cell26 = row0.createCell(26);
		 cell26.setCellValue("已确认未开票金额");
		 cell26.setCellStyle(style);
		 Cell cell27 = row0.createCell(27);
		 cell27.setCellValue("调整金额");
		 cell27.setCellStyle(style);
		 Cell cell28 = row0.createCell(28);
		 cell28.setCellValue("是否申请坏账");
		 cell28.setCellStyle(style);
		 Cell cell29 = row0.createCell(29);
		 cell29.setCellValue("账单下载地址");
		 cell29.setCellStyle(style);
		 Cell cell30 = row0.createCell(30);
		 cell30.setCellValue("备注");
		 cell30.setCellStyle(style);
		 
		logger.info("对账导出给sheet赋值。。。");
		//确认金额总计
		double totalConfirmAmount=0d;
		//发票金额总计
		double totalInvoiceAmount=0d;
		//未收款金额
		double totalUnReceiptAmount=0d;
		//开票未回款金额
		double totalInvoiceUnReceiptAmount=0d;
		//已确认未开票金额
		double totalConfirmUnInvoiceAmount=0d;
		//收款金额
		double totalReceiptAmount=0d;
		int RowIndex = 1;
		for(int i=0;i<dataList.size();i++){	
			BillCheckInfoVo entity = dataList.get(i);
			Row row = sheet.createRow(RowIndex);
			RowIndex++;
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getCreateMonth());
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getInvoiceName());
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getBillName());
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getBillStartTime()==null?"":sdf.format(entity.getBillStartTime()));
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(CheckBillStatusEnum.getMap().get(entity.getBillStatus()));
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(BillCheckStateEnum.getMap().get(entity.getBillCheckStatus()));
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(BillCheckInvoiceStateEnum.getMap().get(entity.getInvoiceStatus()));
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(entity.getOverStatus());
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getSellerName());
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getArea());
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(entity.getConfirmAmount()==null?0d:entity.getConfirmAmount().doubleValue());
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(entity.getConfirmDate()==null?"":sdf.format(entity.getConfirmDate()));
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(entity.getInvoiceAmount()==null?0d:entity.getInvoiceAmount().doubleValue());
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(entity.getInvoiceDate()==null?"":sdf.format(entity.getInvoiceDate()));
			Cell cel14= row.createCell(14);
			cel14.setCellValue(entity.getExpectReceiptDate()==null?"":sdf.format(entity.getExpectReceiptDate()));	
			Cell cel15= row.createCell(15);
			cel15.setCellValue(entity.getReceiptAmount()==null?0d:entity.getReceiptAmount().doubleValue());					
			Cell cel16= row.createCell(16);
			cel16.setCellValue(entity.getReceiptDate()==null?"":sdf.format(entity.getReceiptDate()));					
			Cell cel17 = row.createCell(17);
			cel17.setCellValue(entity.getBalanceName());
			Cell cel18 = row.createCell(18);
			cel18.setCellValue(entity.getProjectManagerName());
			Cell cel19 = row.createCell(19);
			cel19.setCellValue(entity.getDeptName());		
			Cell cel20 = row.createCell(20);
			cel20.setCellValue(entity.getProjectName());
			Cell cel21 = row.createCell(21);
			cel21.setCellValue(entity.getFirstClassName());
			Cell cel22 = row.createCell(22);
			cel22.setCellValue(entity.getBizTypeName());
			Cell cel23 = row.createCell(23);
			cel23.setCellValue(entity.getExpectAmount()==null?0d:entity.getExpectAmount().doubleValue());
			Cell cel24 = row.createCell(24);
			cel24.setCellValue(entity.getUnReceiptAmount()==null?0d:entity.getUnReceiptAmount().doubleValue());		
			Cell cel25=  row.createCell(25);
			cel25.setCellValue(entity.getInvoiceUnReceiptAmount()==null?0d:entity.getInvoiceUnReceiptAmount().doubleValue());			
			Cell cel26=  row.createCell(26);
			cel26.setCellValue(entity.getConfirmUnInvoiceAmount()==null?0d:entity.getConfirmUnInvoiceAmount().doubleValue());			
			Cell cel27=  row.createCell(27);
			cel27.setCellValue(entity.getAdjustMoney()==null?0d:entity.getAdjustMoney().doubleValue());			
			Cell cel28=  row.createCell(28);
			cel28.setCellValue("0".equals(entity.getIsapplyBad())?"否":"是");		
			Cell cel29=  row.createCell(29);
			cel29.setCellValue(entity.getBillExcelUrl());		
			Cell cel30= row.createCell(30);
			cel30.setCellValue(entity.getRemark());
			
			//确认金额总计
			totalConfirmAmount+=(entity.getConfirmAmount()==null?0d:entity.getConfirmAmount().doubleValue());
			//发票金额总计
			totalInvoiceAmount+=(entity.getInvoiceAmount()==null?0d:entity.getInvoiceAmount().doubleValue());
			//未收款金额
			totalUnReceiptAmount+=(entity.getUnReceiptAmount()==null?0d:entity.getUnReceiptAmount().doubleValue());
			//开票未回款金额
			totalInvoiceUnReceiptAmount+=(entity.getInvoiceUnReceiptAmount()==null?0d:entity.getInvoiceUnReceiptAmount().doubleValue());
			//已确认未开票金额
			totalConfirmUnInvoiceAmount+=(entity.getConfirmUnInvoiceAmount()==null?0d:entity.getConfirmUnInvoiceAmount().doubleValue());
			//收款金额
			totalReceiptAmount+=(entity.getReceiptAmount()==null?0d:entity.getReceiptAmount().doubleValue());
		}
		
		Row lastRow = sheet.createRow(RowIndex);
		Cell cellast = lastRow.createCell(0);
		cellast.setCellValue("合计：");
		
		Cell cellast0 = lastRow.createCell(18);
		cellast0.setCellValue(totalConfirmAmount);
		Cell cellast1 = lastRow.createCell(19);
		cellast1.setCellValue(totalInvoiceAmount);
		Cell cellast2 = lastRow.createCell(22);
		cellast2.setCellValue(totalUnReceiptAmount);
		Cell cellast3 = lastRow.createCell(23);
		cellast3.setCellValue(totalInvoiceUnReceiptAmount);
		Cell cellast4 = lastRow.createCell(24);
		cellast4.setCellValue(totalConfirmUnInvoiceAmount);
		Cell cellast5 = lastRow.createCell(25);
		cellast5.setCellValue(totalReceiptAmount);
	}
	
	/**
	 * 对账文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(BillCheckInfoController.class.getName(), "BCK", "000000");
	}
	
	
	//获取所有商家合同名称
	private Map<String, Object> getCusMap(){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			List<CustomerVo> list=customerService.queryAll();
			for(CustomerVo entity:list){
				if(StringUtils.isNotBlank(entity.getMkInvoiceName())){
					map.put(entity.getMkInvoiceName(), entity);
				}			
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_BILL_CHECK_INFO");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_BILL_CHECK_INFO");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	
	
	
	
	/**
	 * 账单导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile exportWarn(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====对账账单导出：写入Excel begin.");
    	
    	Map<String, Object> dictcodeMap=new HashMap<String, Object>();
		try {
//			String filePath =getName() + FileConstant.SUFFIX_XLSX;
			//如果存放上传文件的目录不存在就新建
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "对账收款账单" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //对账账单
	    	handWarn(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====对账账单导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("对账账单导出失败", e);
		}
		return null;
	}
	
	
	/**
	 * 对账收款信息导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handWarn(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
		int pageNo = 1;
		boolean doLoop = true;
		logger.info("对账收款信息导出...");
        List<BillCheckInfoVo> dataList = new  ArrayList<BillCheckInfoVo>();
		
		while (doLoop) {
			PageInfo<BillCheckInfoVo> pageInfo=new PageInfo<BillCheckInfoVo>();		
			if (param == null){
				param = new HashMap<String, Object>();
			}									
			pageInfo = billCheckInfoService.queryWarnList(param, pageNo, pageSize);	
			PageInfo<BillCheckInfoVo> newPageInfo =getNew(pageInfo);	
			if (null != newPageInfo && newPageInfo.getList().size() > 0) {
				if (newPageInfo.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				dataList.addAll(pageInfo.getList());
			}else {
				doLoop = false;
			}
		}
		if(dataList.size()==0){
			return;
		}
		logger.info("对账收款导出生成sheet。。。");
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"对账收款");
		
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(8, 3800);
		sheet.setColumnWidth(9, 3800);
		sheet.setColumnWidth(11, 3800);
		sheet.setColumnWidth(12, 3800);
		sheet.setColumnWidth(17, 3800);
		sheet.setColumnWidth(20, 3800);
		sheet.setColumnWidth(21, 3800);
		sheet.setColumnWidth(22, 3800);
		sheet.setColumnWidth(23, 4000);
		sheet.setColumnWidth(26, 3800);
		
		Font font = workbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
		 
		 Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellValue("业务月份");
		 cell0.setCellStyle(style);
		 Cell cell1 = row0.createCell(1);
		 cell1.setCellValue("商家合同名称");
		 cell1.setCellStyle(style);
		 Cell cell2 = row0.createCell(2);
		 cell2.setCellStyle(style);
		 cell2.setCellValue("账单名称");
		 Cell cell3 = row0.createCell(3);
		 cell3.setCellValue("业务启动时间");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(4);
		 cell4.setCellValue("账单状态");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(5);
		 cell5.setCellValue("对账状态");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(6);
		 cell6.setCellValue("开票状态");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(7);
		 cell7.setCellValue("超期状态");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(8);
		 cell8.setCellValue("是否预警");
		 cell8.setCellStyle(style);
		 Cell cell9 = row0.createCell(9);
		 cell9.setCellValue("销售员名称");
		 cell9.setCellStyle(style);
		 Cell cell10 = row0.createCell(10);
		 cell10.setCellValue("区域");
		 cell10.setCellStyle(style);
		 Cell cell11 = row0.createCell(11);
		 cell11.setCellValue("最终确认额");
		 cell11.setCellStyle(style);
		 Cell cell12 = row0.createCell(12);
		 cell12.setCellValue("确认日期");
		 cell12.setCellStyle(style);
		 Cell cell13 = row0.createCell(13);
		 cell13.setCellValue("发票金额");
		 cell13.setCellStyle(style);
		 Cell cell14 = row0.createCell(14);
		 cell14.setCellValue("开票日期");
		 cell14.setCellStyle(style);
		 Cell cell15 = row0.createCell(15);
		 cell15.setCellValue("预计回款日期");
		 cell15.setCellStyle(style);
		 Cell cell16 = row0.createCell(16);
		 cell16.setCellValue("收款金额");
		 cell16.setCellStyle(style);
		 Cell cell17 = row0.createCell(17);
		 cell17.setCellValue("收款日期");
		 cell17.setCellStyle(style);
		 Cell cell18 = row0.createCell(18);
		 cell18.setCellValue("结算员");
		 cell18.setCellStyle(style);
		 Cell cell19 = row0.createCell(19);
		 cell19.setCellValue("项目管理员");
		 cell19.setCellStyle(style);
		 Cell cell20 = row0.createCell(20);
		 cell20.setCellValue("责任部门编码");
		 cell20.setCellStyle(style);
		 Cell cell21 = row0.createCell(21);
		 cell21.setCellValue("项目");
		 cell21.setCellStyle(style);
		 Cell cell22 = row0.createCell(22);
		 cell22.setCellValue("一级品类");
		 cell22.setCellStyle(style);
		 Cell cell23 = row0.createCell(23);
		 cell23.setCellValue("业务类型");
		 cell23.setCellStyle(style);
		 Cell cell24 = row0.createCell(24);
		 cell24.setCellValue("预计金额");
		 cell24.setCellStyle(style);
		 Cell cell25 = row0.createCell(25);
		 cell25.setCellValue("未收款金额");
		 cell25.setCellStyle(style);
		 Cell cell26 = row0.createCell(26);
		 cell26.setCellValue("开票未回款金额");
		 cell26.setCellStyle(style);
		 Cell cell27 = row0.createCell(27);
		 cell27.setCellValue("已确认未开票金额");
		 cell27.setCellStyle(style);
		 Cell cell28 = row0.createCell(28);
		 cell28.setCellValue("调整金额");
		 cell28.setCellStyle(style);
		 Cell cell29 = row0.createCell(29);
		 cell29.setCellValue("是否申请坏账");
		 cell29.setCellStyle(style);
		 Cell cell30 = row0.createCell(30);
		 cell30.setCellValue("账单下载地址");
		 cell30.setCellStyle(style);
		 Cell cell31 = row0.createCell(31);
		 cell31.setCellValue("备注");
		 cell31.setCellStyle(style);
		 
		logger.info("对账导出给sheet赋值。。。");
		int RowIndex = 1;
		for(int i=0;i<dataList.size();i++){	
			BillCheckInfoVo entity = dataList.get(i);
			Row row = sheet.createRow(RowIndex);
			RowIndex++;
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getCreateMonth());
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getInvoiceName());
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getBillName());
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getBillStartTime()==null?"":sdf.format(entity.getBillStartTime()));
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(CheckBillStatusEnum.getMap().get(entity.getBillStatus()));
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(BillCheckStateEnum.getMap().get(entity.getBillCheckStatus()));
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(BillCheckInvoiceStateEnum.getMap().get(entity.getInvoiceStatus()));
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(entity.getOverStatus());
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getWarnMessage());
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getSellerName());
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(entity.getArea());
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(entity.getConfirmAmount()==null?0d:entity.getConfirmAmount().doubleValue());
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(entity.getConfirmDate()==null?"":sdf.format(entity.getConfirmDate()));
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(entity.getInvoiceAmount()==null?0d:entity.getInvoiceAmount().doubleValue());
			Cell cel14 = row.createCell(14);
			cel14.setCellValue(entity.getInvoiceDate()==null?"":sdf.format(entity.getInvoiceDate()));
			Cell cel15= row.createCell(15);
			cel15.setCellValue(entity.getExpectReceiptDate()==null?"":sdf.format(entity.getExpectReceiptDate()));	
			Cell cel16= row.createCell(16);
			cel16.setCellValue(entity.getReceiptAmount()==null?0d:entity.getReceiptAmount().doubleValue());	
			Cell cel17= row.createCell(17);
			cel17.setCellValue(entity.getReceiptDate()==null?"":sdf.format(entity.getReceiptDate()));	
			Cell cel18 = row.createCell(18);
			cel18.setCellValue(entity.getBalanceName());
			Cell cel19 = row.createCell(19);
			cel19.setCellValue(entity.getProjectManagerName());
			Cell cel20 = row.createCell(20);
			cel20.setCellValue(entity.getDeptName());
			Cell cel21 = row.createCell(21);
			cel21.setCellValue(entity.getProjectName());
			Cell cel22 = row.createCell(22);
			cel22.setCellValue(entity.getFirstClassName());
			Cell cel23 = row.createCell(23);
			cel23.setCellValue(entity.getBizTypeName());
			Cell cel24 = row.createCell(24);
			cel24.setCellValue(entity.getExpectAmount()==null?0d:entity.getExpectAmount().doubleValue());
			Cell cel25 = row.createCell(25);
			cel25.setCellValue(entity.getUnReceiptAmount()==null?0d:entity.getUnReceiptAmount().doubleValue());		
			Cell cel26=  row.createCell(26);
			cel26.setCellValue(entity.getInvoiceUnReceiptAmount()==null?0d:entity.getInvoiceUnReceiptAmount().doubleValue());			
			Cell cel27=  row.createCell(27);
			cel27.setCellValue(entity.getConfirmUnInvoiceAmount()==null?0d:entity.getConfirmUnInvoiceAmount().doubleValue());			
			Cell cel28=  row.createCell(28);
			cel28.setCellValue(entity.getAdjustMoney()==null?0d:entity.getAdjustMoney().doubleValue());			
			Cell cel29=  row.createCell(29);
			cel29.setCellValue("0".equals(entity.getIsapplyBad())?"否":"是");		
			Cell cel30= row.createCell(30);
			cel30.setCellValue(entity.getBillExcelUrl());		
			Cell cel31= row.createCell(31);
			cel31.setCellValue(entity.getRemark());
		}
	}
	
}
