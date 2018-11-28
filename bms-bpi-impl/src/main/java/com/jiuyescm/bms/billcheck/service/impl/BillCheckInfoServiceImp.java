package com.jiuyescm.bms.billcheck.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRecordRepository;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.BillCheckAdjustInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.BillReceiptFollowEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;

@Service("billCheckInfoService")
public class BillCheckInfoServiceImp implements IBillCheckInfoService{
	
	private static final Logger logger = Logger.getLogger(BillCheckInfoServiceImp.class.getName());


	@Resource private IBillCheckInfoRepository billCheckInfoRepository;
	@Resource 
	private IBillCheckLogRepository billCheckLogRepository;
	@Resource
	private IBillReceiveMasterRepository billReceiveMasterRepository;
	@Resource
	private IBillReceiveMasterRecordRepository billReceiveMasterRecordRepository;
	@Override
	public PageInfo<BillCheckInfoVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		try {
			
			long current=JAppContext.currentTimestamp().getTime();
			
			String overStatus="";
			if(condition!=null && condition.get("overStatus")!=null){
				overStatus=condition.get("overStatus").toString();
			}
			
			PageInfo<BillCheckInfoEntity> pageInfo=new PageInfo<BillCheckInfoEntity>();
			if(condition!=null && condition.get("invoiceNo")!=null && condition.get("invoiceNo")!=""){
				//发票号不为空时，需要根据发票号去查询子票再查询主表
				pageInfo=billCheckInfoRepository.queryByInvoiceNo(condition, pageNo, pageSize);
	
			}else if(condition!=null  && condition.get("followType")!=null && condition.get("followType")!=""){
				//跟进类型不为空时，需要根据跟进类型去查询字表再查询主表
				pageInfo=billCheckInfoRepository.queryByFollowType(condition, pageNo, pageSize);
			}else{
				pageInfo=billCheckInfoRepository.query(condition, pageNo, pageSize);
			}
			
			PageInfo<BillCheckInfoVo> result=new PageInfo<BillCheckInfoVo>();
	
			PropertyUtils.copyProperties(result, pageInfo);
			
			List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
			//确认金额总计
			BigDecimal totalConfirmAmount=new BigDecimal(0);
			//发票金额总计
			BigDecimal totalInvoiceAmount=new BigDecimal(0);
			//未收款金额
			BigDecimal totalUnReceiptAmount=new BigDecimal(0);
			//开票未回款金额
			BigDecimal totalInvoiceUnReceiptAmount=new BigDecimal(0);
			//已确认未开票金额
			BigDecimal totalConfirmUnInvoiceAmount=new BigDecimal(0);
			//收款金额
			BigDecimal totalReceiptAmount=new BigDecimal(0);
			
	    	for(BillCheckInfoEntity entity : pageInfo.getList()) {
	    		BillCheckInfoVo vo = new BillCheckInfoVo();    		
	            PropertyUtils.copyProperties(vo, entity);
	            //统计金额
	            //确认金额
	            totalConfirmAmount=totalConfirmAmount.add(vo.getConfirmAmount());
	            //发票金额
	            totalInvoiceAmount=totalInvoiceAmount.add(vo.getInvoiceAmount());
	            //未收款金额
	            totalUnReceiptAmount=totalUnReceiptAmount.add(vo.getUnReceiptAmount());
	            //开票未回款金额
	            totalInvoiceUnReceiptAmount=totalInvoiceUnReceiptAmount.add(vo.getInvoiceUnReceiptAmount());
	            //已确认未回款金额
	            totalConfirmUnInvoiceAmount=totalConfirmUnInvoiceAmount.add(vo.getConfirmUnInvoiceAmount());
	            //收款金额
	            totalReceiptAmount=totalReceiptAmount.add(vo.getReceiptAmount());
	    		voList.add(vo);
	    	}
			
	    	for(BillCheckInfoVo entity:voList){
	    		entity.setTotalConfirmAmount(totalConfirmAmount);
	    		entity.setTotalInvoiceAmount(totalInvoiceAmount);
	    		entity.setTotalUnReceiptAmount(totalUnReceiptAmount);
	    		entity.setTotalInvoiceUnReceiptAmount(totalInvoiceUnReceiptAmount);
	    		entity.setTotalConfirmUnInvoiceAmount(totalConfirmUnInvoiceAmount);
	    		entity.setTotalReceiptAmount(totalReceiptAmount);
	    		BigDecimal m=new BigDecimal(0);
	    		entity.setAdjustMoney(m);
	    		//查询调整金额
	    		Map<String, Object> param=new HashMap<String, Object>();
	    		param.put("billCheckId", entity.getId());
	    		BillCheckAdjustInfoEntity adjustEntity=billCheckInfoRepository.queryOneAdjust(param);
	    		if(adjustEntity!=null){
	    			entity.setAdjustMoney(adjustEntity.getAdjustAmount()==null?m:adjustEntity.getAdjustAmount());
	    		}
	    		//判断超期状态
	    		//开票30天内:正常
	    		//开票30天以上,小于45天:临期--包含30天
	    		//超期:开票45天以上,包含45天;
	    		if(entity.getUnReceiptAmount()!=null && entity.getUnReceiptAmount().compareTo(new BigDecimal(1))>0){
	    			if("1".equals(entity.getIsneedInvoice()) && entity.getInvoiceDate()!=null){
		    			//需要开票时用开票时间去判断
		    			long time=entity.getInvoiceDate().getTime();
		    			
		    			int days = (int) ((current - time)/(1000 * 60 * 60 * 24));
			    		if(days>=0 && days<30){
			    			entity.setOverStatus("正常");
			    		}else if(days>=30 && days<45){
			    			entity.setOverStatus("临期");
			    		}else if(days>=45){
			    			entity.setOverStatus("超期");
			    		}	
		    			
		    		}else if("0".equals(entity.getIsneedInvoice()) && entity.getConfirmDate()!=null){
		    			long time=entity.getConfirmDate().getTime();
		    			
		    			int days = (int) ((current - time)/(1000 * 60 * 60 * 24));
			    		if(days>=0 && days<30){
			    			entity.setOverStatus("正常");
			    		}else if(days>=30 && days<45){
			    			entity.setOverStatus("临期");
			    		}else if(days>=45){
			    			entity.setOverStatus("超期");
			    		}	
		    		}
	    		}
	    		
	    		//设置预分配金额和待催款金额
	    		if(getPreMoney().containsKey(entity.getInvoiceName()+"&"+entity.getCreateMonth()+"&"+entity.getBillName())){
	    			BillCheckInfoEntity amountVo=getPreMoney().get((entity.getInvoiceName()+"&"+entity.getCreateMonth()+"&"+entity.getBillName()));
	    			entity.setPreDistibutionAmount(amountVo.getPreDistibutionAmount());
	    			entity.setTbDunAmount(amountVo.getTbDunAmount());
	    		}
	    	}
	    	List<BillCheckInfoVo> resultList = new ArrayList<BillCheckInfoVo>();
	    	if(StringUtils.isNotBlank(overStatus)){
	    		for(BillCheckInfoVo entity:voList){
		    		if(overStatus.equals(entity.getOverStatus())){
		    			resultList.add(entity);
		    		}
		    	}
	    		result.setList(resultList);
	    		return result;
	    	}
	    	
	    	result.setList(voList);
			return result;
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return null;
	}

	@Override
	public PageInfo<BillCheckInfoVo> queryWarn(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<BillCheckInfoEntity> pageInfo=billCheckInfoRepository.queryWarn(condition, pageNo, pageSize);
		PageInfo<BillCheckInfoVo> result=new PageInfo<BillCheckInfoVo>();
		
		try {
			List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
	    	for(BillCheckInfoEntity entity : pageInfo.getList()) {
	    		BillCheckInfoVo vo = new BillCheckInfoVo(); 		
	            PropertyUtils.copyProperties(vo, entity);  
	            vo.setWarnMessage("预警");
	    		voList.add(vo);
	    	}
	    	result.setList(voList);
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
    	
		return result;
	}
	
	@Override
	public PageInfo<BillCheckInfoVo> queryWarnList(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<BillCheckInfoEntity> pageInfo=billCheckInfoRepository.queryWarnList(condition, pageNo, pageSize);
		PageInfo<BillCheckInfoVo> result=new PageInfo<BillCheckInfoVo>();
		
		try {
			List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
	    	for(BillCheckInfoEntity entity : pageInfo.getList()) {
	    		BillCheckInfoVo vo = new BillCheckInfoVo(); 		
	            PropertyUtils.copyProperties(vo, entity);  
	            vo.setWarnMessage("预警");
	    		voList.add(vo);
	    	}
	    	result.setList(voList);
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
    	
		return result;
	}
	
	@Override
	public List<BillCheckAdjustInfoVo> queryAdjust(Map<String, Object> condition) {
		List<BillCheckAdjustInfoEntity> list=billCheckInfoRepository.queryAdjust(condition);
		List<BillCheckAdjustInfoVo> voList = new ArrayList<BillCheckAdjustInfoVo>();
    	for(BillCheckAdjustInfoEntity entity : list) {
    		BillCheckAdjustInfoVo vo = new BillCheckAdjustInfoVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public BillCheckInfoVo queryOne(Map<String, Object> condition) {
		BillCheckInfoVo billCheckInfoVo=new BillCheckInfoVo();
		BillCheckInfoEntity entity=billCheckInfoRepository.queryOne(condition);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(billCheckInfoVo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return billCheckInfoVo;
	}

	@Override
	public int update(BillCheckInfoVo billCheckInfoVo) {
		BillCheckInfoEntity entity=new BillCheckInfoEntity();
		try {
            PropertyUtils.copyProperties(entity, billCheckInfoVo);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return billCheckInfoRepository.update(entity);
	}

	@Override
	public int saveList(List<BillCheckInfoVo> list) {
		List<BillCheckInfoEntity> enList=new ArrayList<BillCheckInfoEntity>();
		for(BillCheckInfoVo entity : list) {
			BillCheckInfoEntity vo = new BillCheckInfoEntity();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(vo);
    	}
		
		return billCheckInfoRepository.saveList(enList);
	}
	
	@Override
	public int save(BillCheckInfoVo vo) {
		BillCheckInfoEntity entity = new BillCheckInfoEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return billCheckInfoRepository.save(entity);
	}

	@Override
	public List<BillCheckInfoVo> queryList(Map<String, Object> condition) {
		List<BillCheckInfoEntity> list=billCheckInfoRepository.queryList(condition);
		List<BillCheckInfoVo> voList=new ArrayList<BillCheckInfoVo>();
		for(BillCheckInfoEntity entity : list) {
			BillCheckInfoVo vo = new BillCheckInfoVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		voList.add(vo);
    	}
		
		return voList;
	}

	@Override
	public int saveReceiptFollow(BillReceiptFollowVo vo) {
		BillReceiptFollowEntity entity=new BillReceiptFollowEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		
		//保存回款反馈
		int result=billCheckInfoRepository.saveReceiptFollow(entity);
		if(result>0){
			//更新账单
			Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("id", vo.getBillCheckId());
			//查询账单
			BillCheckInfoEntity bInfo=billCheckInfoRepository.queryOne(condition);
			//回款反馈
			BillReceiptFollowEntity receiptFollowVo=billCheckInfoRepository.queryReceiptFollow(condition);
			bInfo.setExpectReceiptDate(receiptFollowVo.getExpectReceiptDate());
			billCheckInfoRepository.update(bInfo);
		}
		
		return result;
	}

	@Override
	public PageInfo<BillReceiptFollowVo> queryReceiptFollow(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		
		PageInfo<BillReceiptFollowEntity> pageInfo=billCheckInfoRepository.queryReceiptFollow(condition, pageNo, pageSize);
		
		PageInfo<BillReceiptFollowVo> result=new PageInfo<BillReceiptFollowVo>();

		List<BillReceiptFollowVo> voList = new ArrayList<BillReceiptFollowVo>();
    	for(BillReceiptFollowEntity entity : pageInfo.getList()) {
    		BillReceiptFollowVo vo = new BillReceiptFollowVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		voList.add(vo);
    	}
		
    	result.setList(voList);
		return result;
	}

	@Override
	public int saveAjustList(List<BillCheckAdjustInfoVo> list) {
		List<BillCheckAdjustInfoEntity> enList=new ArrayList<BillCheckAdjustInfoEntity>();
		for(BillCheckAdjustInfoVo vo : list) {
			BillCheckAdjustInfoEntity entity = new BillCheckAdjustInfoEntity();
    		try {
                PropertyUtils.copyProperties(entity,vo);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(entity);
    	}
		return billCheckInfoRepository.saveAjustList(enList);
	}

	@Override
	public BillCheckAdjustInfoVo queryOneAdjust(Map<String, Object> condition) {
		BillCheckAdjustInfoVo vo=null;
		try {			
			BillCheckAdjustInfoEntity entity=billCheckInfoRepository.queryOneAdjust(condition);
			if(entity!=null){
				vo=new BillCheckAdjustInfoVo();
				PropertyUtils.copyProperties(vo,entity);
			}        
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return vo;
	}

	@Override
	public int updateAjustList(List<BillCheckAdjustInfoVo> list) {
		List<BillCheckAdjustInfoEntity> enList=new ArrayList<BillCheckAdjustInfoEntity>();
		for(BillCheckAdjustInfoVo vo : list) {
			BillCheckAdjustInfoEntity entity = new BillCheckAdjustInfoEntity();
    		try {
                PropertyUtils.copyProperties(entity,vo);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(entity);
    	}
		return billCheckInfoRepository.updateAjustList(enList);
	}

	@Override
	public int saveReceiptFollowList(List<BillReceiptFollowVo> list) {
		List<BillReceiptFollowEntity> elist=new ArrayList<BillReceiptFollowEntity>(); 
		
	
		for(BillReceiptFollowVo vo:list){
			BillReceiptFollowEntity entity=new BillReceiptFollowEntity();
			try {
	            PropertyUtils.copyProperties(entity, vo);
	        } catch (Exception ex) {
	        	logger.error("转换失败:{0}",ex);
	        }
			elist.add(entity);
		}
		
		return billCheckInfoRepository.saveReceiptFollowList(elist);
	}

	@Override
	public String getBillCheckStatus(int id) {
		return billCheckInfoRepository.getBillCheckStatus(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int confirmBillCheckInfo(BillCheckInfoVo billCheckInfoVo,
			BillCheckLogVo vo) throws Exception {
		
		try {
			BillCheckInfoEntity entity=new BillCheckInfoEntity();
            PropertyUtils.copyProperties(entity, billCheckInfoVo);
			BillCheckLogEntity logEntity=new BillCheckLogEntity();
            PropertyUtils.copyProperties(logEntity, vo);
            
            int k=billCheckLogRepository.addCheckLog(logEntity);
            if(k>0){
            	return billCheckInfoRepository.update(entity);
            }else{
            	throw new BizException("添加账单日志失败");
            }
            
        } catch (Exception ex) {
            logger.error("转换失败:",ex);
        	throw ex;
        }
	}

	@Override
	public List<BillCheckInfoVo> queryAllByBillName(
			List<String> invoiceNameList) {
		List<BillCheckInfoVo> voList=new ArrayList<BillCheckInfoVo>();
		try{
			List<BillCheckInfoEntity> list=billCheckInfoRepository.queryAllByBillName(invoiceNameList);
			for(BillCheckInfoEntity entity : list) {
				BillCheckInfoVo vo = new BillCheckInfoVo();
	            PropertyUtils.copyProperties(vo, entity);
	    		voList.add(vo);
	    	}
		}catch(Exception e){
			logger.error("queryAllByInvoiceName error:",e);
		}
		return voList;
	}

	@Override
	public List<BillCheckInfoVo> queryAllByBillCheckId(List<Integer> checkIdList) {
		List<BillCheckInfoVo> voList=new ArrayList<BillCheckInfoVo>();
		try{
			List<BillCheckInfoEntity> list=billCheckInfoRepository.queryAllByBillCheckId(checkIdList);
			for(BillCheckInfoEntity entity : list) {
				BillCheckInfoVo vo = new BillCheckInfoVo();
	            PropertyUtils.copyProperties(vo, entity);
	    		voList.add(vo);
	    	}
		}catch(Exception e){
			logger.error("queryAllByBillCheckId error:",e);
		}
		return voList;
	}

	@Override
	public BillReceiptFollowVo queryReceiptFollow(Map<String, Object> condition) {
		BillReceiptFollowVo vo=new BillReceiptFollowVo();
		BillReceiptFollowEntity entity=billCheckInfoRepository.queryReceiptFollow(condition);
		if(entity==null){
			return null;
		}
		try{
	          PropertyUtils.copyProperties(vo, entity);
	    	
		}catch(Exception e){
			logger.error("queryAllByBillCheckId error:",e);
		}
		return vo;
	}

	@Override
	public PageInfo<BillCheckInfoVo> queryReceiptDetail(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		try {
			PageInfo<BillCheckInfoEntity> pageInfo=billCheckInfoRepository.queryReceiptDetail(condition, pageNo, pageSize);			
			PageInfo<BillCheckInfoVo> result=new PageInfo<BillCheckInfoVo>();
	
			PropertyUtils.copyProperties(result, pageInfo);
			
			List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
			
			//确认金额总计
			BigDecimal totalConfirmAmount=new BigDecimal(0);
			//发票金额总计
			BigDecimal totalInvoiceAmount=new BigDecimal(0);
			//未收款金额
			BigDecimal totalUnReceiptAmount=new BigDecimal(0);
			//收款金额
			BigDecimal totalReceiptAmount=new BigDecimal(0);
	
	    	for(BillCheckInfoEntity entity : pageInfo.getList()) {  		
	    		BillCheckInfoVo vo = new BillCheckInfoVo();
	    		
	            PropertyUtils.copyProperties(vo, entity);
	            
	        	//预计到期时间
	    		if(vo.getExpectReceiptDate()!=null){
	    			Date day=new Date(); 
	    			if(day.getTime()>vo.getExpectReceiptDate().getTime()){
	    				Long days=(day.getTime() - vo.getExpectReceiptDate().getTime())/(1000 * 60 * 60 * 24);
		    			vo.setOverDays(days+"");
	    			}	    		
	    		}
	    		//应收账款天数
	    		Date day=getDate(vo.getCreateMonth());
	    		Date nowDay=new Date();
	    		//当未收款金额不为0或为空时，取“当前日期-账单月份最后一天”
	    		if(vo.getUnReceiptAmount()==null || vo.getUnReceiptAmount().compareTo(BigDecimal.ZERO)!=0){
		    		nowDay=new Date(); 	    			
	    		}else if(vo.getUnReceiptAmount().compareTo(BigDecimal.ZERO)==0){
	    			//当未收款金额为0时，取“收款日期-账单月份最后一天
	    			if(vo.getReceiptDate()!=null){
	    				nowDay=vo.getReceiptDate(); 
	    			}    		    			
	    		}
	    		if(day!=null){
	    			if(nowDay.getTime()<day.getTime()){
		    			vo.setReceiptDays(0+"");
	    			}else{
	    				Long days=(nowDay.getTime() - day.getTime())/(1000 * 60 * 60 * 24);
		    			vo.setReceiptDays(days+"");
	    			}
	    		}
	    		
	    	    //统计金额
	            //确认金额
	            totalConfirmAmount=totalConfirmAmount.add(vo.getConfirmAmount());
	            //发票金额
	            totalInvoiceAmount=totalInvoiceAmount.add(vo.getInvoiceAmount());
	            //未收款金额
	            totalUnReceiptAmount=totalUnReceiptAmount.add(vo.getUnReceiptAmount());
	            //收款金额
	            totalReceiptAmount=totalReceiptAmount.add(vo.getReceiptAmount());
	    	
	    		voList.add(vo);
	    	}
			
	    	for(BillCheckInfoVo entity:voList){
	    		entity.setTotalConfirmAmount(totalConfirmAmount);
	    		entity.setTotalInvoiceAmount(totalInvoiceAmount);
	    		entity.setTotalUnReceiptAmount(totalUnReceiptAmount);
	    		entity.setTotalReceiptAmount(totalReceiptAmount);
	    	}
	    	
	    	result.setList(voList);
			return result;
		} catch (Exception ex) {
			logger.error("queryReceiptDetail error:{0}",ex);
        }
		return null;
	}

	
	public Date getDate(int createMonth){
		try {
			String createDate="20"+createMonth+"";
			int year=Integer.parseInt(createDate.substring(0, 4));
			int month=Integer.parseInt(createDate.substring(4, 6));
				
			 Calendar cal = Calendar.getInstance();     
	         cal.set(Calendar.YEAR, year);     
	         cal.set(Calendar.MONTH, month-1);     
	         cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
			
	         String date=new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime()); 
	         
	         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
	         
	 		 Date date1 = df.parse(date);
	 		 
	 		 return date1;
 		} catch (ParseException e) {
 			logger.error("getDate error:{0}",e);
 		}
		return null;
	}

	
	/**
	 * 获取“预分配金额” 、“待催款金额”
	 * @return
	 */
	public Map<String,BillCheckInfoEntity> getPreMoney(){
		
		Map<String,BillCheckInfoEntity> returnMap=new HashMap<>();
		
		Map<String,Object> map=new HashMap<>();
		Map<String,BigDecimal> accountMap=new HashMap<>();
		
		List<BillCheckInfoEntity> list=billCheckInfoRepository.queryAllUnreceipt(map);
		
		for(BillCheckInfoEntity entity:list){
			BigDecimal accountAmount=new BigDecimal(0d);
			
			if(accountMap.containsKey(entity.getInvoiceName())){
				accountAmount=accountMap.get(entity.getInvoiceName());		
			}else{
				accountAmount=entity.getAccountAmount();
			}
			
			BigDecimal unReceiptAmount=entity.getUnReceiptAmount();
			if(accountAmount.compareTo(BigDecimal.ZERO)>0){
				//账户金额大于0时
				if(accountAmount.compareTo(unReceiptAmount)>=0){
					//账户金额大于未收款金额时，赋值	
					accountMap.put(entity.getInvoiceName(), accountAmount.subtract(unReceiptAmount));//账户余额
					entity.setPreDistibutionAmount(unReceiptAmount);//预分配金额
				}else{
					//账户金额小于未收款金额时，赋值
					accountMap.put(entity.getInvoiceName(), BigDecimal.ZERO);//账户余额30240.6
					entity.setPreDistibutionAmount(accountAmount);//预分配金额
				}
			}else{
				accountMap.put(entity.getInvoiceName(), BigDecimal.ZERO);//账户余额
				entity.setPreDistibutionAmount(BigDecimal.ZERO);//预分配金额
			}			
			entity.setTbDunAmount(unReceiptAmount.subtract(entity.getPreDistibutionAmount()));//待催款金额	
			
			returnMap.put(entity.getInvoiceName()+"&"+entity.getCreateMonth()+"&"+entity.getBillName(), entity);
		}
		return returnMap;
	}
	
	@Override
	public BillCheckInfoVo queryBillCheck(Map<String, Object> condition) {
		BillCheckInfoVo billCheckInfoVo=new BillCheckInfoVo();
		BillCheckInfoEntity entity=billCheckInfoRepository.queryBillCheck(condition);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(billCheckInfoVo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return billCheckInfoVo;
	}
	
	public PageInfo<BillCheckInfoVo> queryForOut(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<BillCheckInfoEntity> pageInfo=billCheckInfoRepository.queryForOut(condition, pageNo, pageSize);
		PageInfo<BillCheckInfoVo> result=new PageInfo<BillCheckInfoVo>();
		
		try {
			List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
	    	for(BillCheckInfoEntity entity : pageInfo.getList()) {
	    		BillCheckInfoVo vo = new BillCheckInfoVo(); 		
	            PropertyUtils.copyProperties(vo, entity);  
	    		voList.add(vo);
	    	}
	    	result.setList(voList);
		} catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
    	
		return result;
	}

	@Override
	public BillCheckInfoVo getLatestBill(Map<String, Object> condition) {
		BillCheckInfoVo vo=new BillCheckInfoVo();
		BillCheckInfoEntity entity=billCheckInfoRepository.getLatestBill(condition);
		try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
		return vo;
	}
	
	@Override
	public void importCheck(String createMonth, String billName) {
		Map<String, Object> condition=new HashMap<>();
		condition.put("createMonth", createMonth);
		condition.put("billName", billName);
		BillCheckInfoEntity entity=billCheckInfoRepository.queryBillCheck(condition);
		if(entity==null){
			throw new BizException("BILL_NULL","账单不存在!");
		}
		//状态为已确认
		if("CONFIRMED".equals(entity.getBillCheckStatus())){
			throw new BizException("CONFIRMED_NULL","对账状态为已确认，无法导入!");
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public void adjustMoney(String billNo,Double adjustMoney,String username,String userId) {
		// TODO Auto-generated method stub
		Map<String, Object> condition=new HashMap<>();
		condition.put("billNo", billNo);
		BillCheckInfoEntity entity=billCheckInfoRepository.queryBillCheck(condition);
		if(entity==null){
			throw new BizException("BILL_NULL","账单不存在!");
		}		
		//状态为已收款
		if("RECEIPTED".equals(entity.getBillStatus())){
			throw new BizException("RECEIPTED_NULL","已收款的账单不能调整金额!");
		}		
		BigDecimal masterAdjustMoney=BigDecimal.valueOf(adjustMoney);
		
		//调整的金额
		BigDecimal adjustAmount=new BigDecimal(0);
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billCheckId", entity.getId());
		BillCheckAdjustInfoEntity adjustEntity = billCheckInfoRepository.queryOneAdjust(param);
		if(adjustEntity!=null && adjustEntity.getAdjustAmount()!=null){
			adjustAmount=adjustEntity.getAdjustAmount();
		}
		//最终确认额
		BigDecimal confirmAmount=new BigDecimal(0);
		if(entity.getConfirmAmount()!=null){
			confirmAmount=entity.getConfirmAmount();
		}
		//账单导入调整金额
		confirmAmount=confirmAmount.add(masterAdjustMoney);	
		//发票金额
		BigDecimal invoiceAmount=new BigDecimal(0);
		if(entity.getInvoiceAmount()!=null){
			invoiceAmount=entity.getInvoiceAmount();
		}
		//收款金额
		BigDecimal receiptAmount=new BigDecimal(0);
		if(entity.getReceiptAmount()!=null){
			receiptAmount=entity.getReceiptAmount();
		}

		//确认金额
		entity.setConfirmAmount(confirmAmount);
		//未收款金额
		entity.setUnReceiptAmount(confirmAmount.add(adjustAmount).subtract(receiptAmount));
		//已确认未开票金额
		entity.setConfirmUnInvoiceAmount(confirmAmount.subtract(invoiceAmount));
		
		//2.发票状态修改
		if(confirmAmount.compareTo(invoiceAmount)==0){
			entity.setInvoiceStatus(BillCheckInvoiceStateEnum.INVOICED.getCode());//已开票
		}else if(confirmAmount.compareTo(invoiceAmount)>0){
			if(invoiceAmount.compareTo(BigDecimal.ZERO)==0){
				entity.setInvoiceStatus(BillCheckInvoiceStateEnum.NO_INVOICE.getCode());//未开票
			}else{
				entity.setInvoiceStatus(BillCheckInvoiceStateEnum.PART_INVOICE.getCode());//部分开票
			}
		}			
		//3.收款状态判断				
		if((adjustAmount.add(confirmAmount)).compareTo(receiptAmount)==0){
			//回款状态
			entity.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());//已收款
		}else if((adjustAmount.add(confirmAmount)).compareTo(receiptAmount)>0|| (adjustAmount.add(confirmAmount)).compareTo(receiptAmount)<0){
			//回款状态
			if(receiptAmount.compareTo(BigDecimal.ZERO)==0){
				entity.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());//未收款
			}else{
				entity.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());//部分收款
			}
		
		}				
		
		//账单状态修改
		//1）如果对账状态为“已确认”and是否需要开票为“是”and开票状态为“未开票”，将账单状态置为“待开票”；
		//2）如果对账状态为“已确认”and是否需要开票为“是”and开票状态为“部分开票”or“已开票”，将账单状态置为“待收款”；
		//3）如果对账状态为“已确认”and是否需要开票为“否”，将账单状态置为“待收款”；
		//4）如果对账状态不为“已确认”，账单状态为“待确认”不变；
		if(BillCheckStateEnum.CONFIRMED.getCode().equals(entity.getBillCheckStatus()) && "1".equals(entity.getIsneedInvoice()) && BillCheckInvoiceStateEnum.NO_INVOICE.getCode().equals(entity.getInvoiceStatus())){
			entity.setBillStatus(CheckBillStatusEnum.TB_INVOICE.getCode());
		}else if(BillCheckStateEnum.CONFIRMED.getCode().equals(entity.getBillCheckStatus()) && "1".equals(entity.getIsneedInvoice()) && (BillCheckInvoiceStateEnum.PART_INVOICE.getCode().equals(entity.getInvoiceStatus()) || BillCheckInvoiceStateEnum.INVOICED.getCode().equals(entity.getInvoiceStatus()))){
			entity.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
		}else if(BillCheckStateEnum.CONFIRMED.getCode().equals(entity.getBillCheckStatus()) && "0".equals(entity.getIsneedInvoice())){
			entity.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
		}else{
			entity.setBillStatus(CheckBillStatusEnum.TB_CONFIRMED.getCode());
		}

		int result=billCheckInfoRepository.update(entity);
		if(result<=0){
			throw new BizException("UPDATE_NULL","账单跟踪更新确认金额失败!");
		}
		//账单导入主表更新调整金额
		BillReceiveMasterEntity brmVo = new BillReceiveMasterEntity();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		brmVo.setBillNo(billNo);
		brmVo.setLastModifier(username);
		brmVo.setLastModifierId(userId);
		brmVo.setLastModifyTime(currentTime);
		brmVo.setAdjustAmount(adjustMoney);
		try {
			billReceiveMasterRepository.update(brmVo);
		} catch (Exception e) {
			throw new BizException("UPDATE_NULL","账单主表更新确认金额失败!");
		}
		
		//账单导入记录表写入
		BillReceiveMasterRecordEntity recordEntity = new BillReceiveMasterRecordEntity();
		recordEntity.setBillNo(billNo);
		recordEntity.setCreateTime(currentTime);
		recordEntity.setCreator(username);
		recordEntity.setCreatorId(userId);
		recordEntity.setAdjustAmount(masterAdjustMoney);
		try {
			billReceiveMasterRecordRepository.save(recordEntity);
		} catch (Exception e) {
			throw new BizException("UPDATE_NULL","账单导入记录表更新确认金额失败!");
		}
		
		
	}
}
