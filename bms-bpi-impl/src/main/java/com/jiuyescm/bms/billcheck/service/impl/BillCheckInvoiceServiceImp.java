package com.jiuyescm.bms.billcheck.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.repository.IBmsGroupUserRepository;
import com.jiuyescm.bms.billcheck.BillCheckAdjustInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckInvoiceEntity;
import com.jiuyescm.bms.billcheck.entity.BillInvoiceEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInvoiceRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInvoiceService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.exception.BizException;

@Service("billCheckInvoiceService")
public class BillCheckInvoiceServiceImp implements IBillCheckInvoiceService{
	
	private static final Logger logger = Logger.getLogger(BillCheckInvoiceServiceImp.class.getName());


	@Resource private IBillCheckInvoiceRepository billCheckInvoiceRepository;
	@Resource private IBillCheckInfoRepository billCheckInfoRepository;
	@Resource private IBmsGroupUserRepository bmsGroupUserRepository;
	@Resource private IBillCheckLogRepository billCheckLogRepository;

	@Override
	public PageInfo<BillCheckInvoiceVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		PageInfo<BillCheckInvoiceEntity> pageInfo=billCheckInvoiceRepository.query(condition, pageNo, pageSize);
		PageInfo<BillCheckInvoiceVo> result=new PageInfo<BillCheckInvoiceVo>();

		List<BillCheckInvoiceVo> voList = new ArrayList<BillCheckInvoiceVo>();
    	for(BillCheckInvoiceEntity entity : pageInfo.getList()) {
    		BillCheckInvoiceVo vo = new BillCheckInvoiceVo();
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
	public BillCheckInvoiceVo queryInvoice(Map<String, Object> condition) {
		BillCheckInvoiceVo billCheckInvoiceVo=new BillCheckInvoiceVo();
		BillCheckInvoiceEntity entity=billCheckInvoiceRepository.queryInvoice(condition);
		try {
            PropertyUtils.copyProperties(billCheckInvoiceVo, entity);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return billCheckInvoiceVo;
	}

	@Override
	public int saveList(List<BillCheckInvoiceVo> list) {
		List<BillCheckInvoiceEntity> enList=new ArrayList<BillCheckInvoiceEntity>();
		for(BillCheckInvoiceVo entity : list) {
			BillCheckInvoiceEntity vo = new BillCheckInvoiceEntity();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(vo);
    	}
		
		return billCheckInvoiceRepository.saveList(enList);
	}

	
	@Override
	public int save(BillCheckInvoiceVo vo) {
		// TODO Auto-generated method stub
		BillCheckInvoiceEntity entity = new BillCheckInvoiceEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        } 		
		return billCheckInvoiceRepository.save(entity);
	}

	
	@Override
	public List<BillCheckInvoiceVo> queryByParam(Map<String, Object> condition) {
		List<BillCheckInvoiceEntity> list=billCheckInvoiceRepository.queryByParam(condition);
		List<BillCheckInvoiceVo> voList = new ArrayList<BillCheckInvoiceVo>();
    	for(BillCheckInvoiceEntity entity : list) {
    		BillCheckInvoiceVo vo = new BillCheckInvoiceVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public List<BillCheckInvoiceVo> queryAllBillInvoice(
			List<Integer> checkIdList) {
		List<BillCheckInvoiceVo> voList = new ArrayList<BillCheckInvoiceVo>();
		try{
			List<BillCheckInvoiceEntity> list=billCheckInvoiceRepository.queryAllBillInvoice(checkIdList);
	    	for(BillCheckInvoiceEntity entity : list) {
	    		BillCheckInvoiceVo vo = new BillCheckInvoiceVo();
                PropertyUtils.copyProperties(vo, entity);
	    		voList.add(vo);
	    	}
		}catch(Exception e){
			logger.error("queryAllBillInvoice ",e);
		}
		return voList;

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public void saveImportInovice(List<BillCheckInvoiceVo> list,
			List<BillCheckInfoVo> checkVoList) throws Exception {
		try{
			List<BillCheckInvoiceEntity> invoiceList=new ArrayList<BillCheckInvoiceEntity>();
			List<BillCheckInfoEntity> infoList=new ArrayList<BillCheckInfoEntity>();
			for(BillCheckInvoiceVo vo:list){
				BillCheckInvoiceEntity entity=new BillCheckInvoiceEntity();
				PropertyUtils.copyProperties(entity, vo);
				invoiceList.add(entity);
			}
			for(BillCheckInfoVo  checkVo:checkVoList){
				BillCheckInfoEntity checkEntity=new BillCheckInfoEntity();
				PropertyUtils.copyProperties(checkEntity, checkVo);
				infoList.add(checkEntity);
			}
			
			billCheckInvoiceRepository.saveList(invoiceList);
			
			for(BillCheckInfoEntity en:infoList){
				Map<String, Object> param=new HashMap<>();
				param.put("id", en.getId());
				BillCheckInvoiceEntity billCheckInvoice=billCheckInvoiceRepository.queryInvoice(param);
				if(billCheckInvoice!=null){
					en.setInvoiceDate(billCheckInvoice.getInvoiceDate());
				}
			}
			billCheckInfoRepository.updateInvoiceStatus(infoList);
		}catch(Exception e){
			logger.error("saveImportInovice ",e);
			throw e;
		}
		
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int update(BillCheckInvoiceVo vo) {
		BillCheckInvoiceEntity entity=new BillCheckInvoiceEntity();
		try{
            PropertyUtils.copyProperties(entity, vo);
		}catch(Exception e){
			logger.error("update ",e);
		}
		
		//更新账单金额和状态
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("id", vo.getBillCheckId());
		BillCheckInfoEntity bInfoEntity=billCheckInfoRepository.queryOne(condition);
		if(bInfoEntity!=null){
			//账单中的确认金额和发票金额以及删除的发票进行比较
			BigDecimal billmoney=bInfoEntity.getConfirmAmount();
			BigDecimal invoiceMoney=new BigDecimal(0);
			
			BigDecimal adjustMoney=new BigDecimal(0);
			//调整的金额
			BillCheckAdjustInfoEntity adjustEntity = billCheckInfoRepository.queryOneAdjust(condition);
			if(adjustEntity!=null && adjustEntity.getAdjustAmount()!=null){
					adjustMoney=adjustEntity.getAdjustAmount();
			}
			
			
			if(bInfoEntity.getInvoiceAmount()!=null){
				invoiceMoney=bInfoEntity.getInvoiceAmount();
			}
			if(vo.getInvoiceAmount()!=null){
				invoiceMoney=invoiceMoney.subtract(vo.getInvoiceAmount());
			}
		
			if(invoiceMoney.compareTo(billmoney)>=0){
				bInfoEntity.setInvoiceAmount(invoiceMoney);
				bInfoEntity.setInvoiceStatus(BillCheckInvoiceStateEnum.INVOICED.getCode());//已开票
			}else{
				if(invoiceMoney.compareTo(BigDecimal.ZERO)==0){
					bInfoEntity.setInvoiceAmount(invoiceMoney);
					bInfoEntity.setInvoiceStatus(BillCheckInvoiceStateEnum.NO_INVOICE.getCode());//未开票
				}else{
					bInfoEntity.setInvoiceAmount(invoiceMoney);
					bInfoEntity.setInvoiceStatus(BillCheckInvoiceStateEnum.PART_INVOICE.getCode());//部分开票	
				}
			}
				
			//不需要发票
			if("0".equals(bInfoEntity.getIsneedInvoice())){
				bInfoEntity.setInvoiceStatus(BillCheckInvoiceStateEnum.NO_INVOICE.getCode());
			}
			//已确认未开票金额
			bInfoEntity.setConfirmUnInvoiceAmount(billmoney.subtract(invoiceMoney));
			//开票未回款金额
			BigDecimal receiptMoney=new BigDecimal(0);
			if(bInfoEntity.getReceiptAmount()!=null){
				receiptMoney=bInfoEntity.getReceiptAmount();
			}
			bInfoEntity.setInvoiceUnReceiptAmount(invoiceMoney.subtract(receiptMoney).add(adjustMoney));
		}	
		
		int result=billCheckInvoiceRepository.update(entity);
		
		if(result>0){
			//更新账单状态（开票日期、开票未回款金额）
			Map<String, Object> param=new HashMap<>();
			param.put("id", bInfoEntity.getId());
			BillCheckInvoiceEntity billCheckInvoice=billCheckInvoiceRepository.queryInvoice(param);
			if(billCheckInvoice!=null){
				bInfoEntity.setInvoiceDate(billCheckInvoice.getInvoiceDate());
			}
			bInfoEntity.setInvoiceUnReceiptAmount(bInfoEntity.getInvoiceAmount().subtract(bInfoEntity.getReceiptAmount()));			
			billCheckInfoRepository.update(bInfoEntity);
		}
		
		return result;
	}

	@Override
	public int updateOne(BillCheckInvoiceVo vo) {
		// TODO Auto-generated method stub
		BillCheckInvoiceEntity entity=new BillCheckInvoiceEntity();
		try{
            PropertyUtils.copyProperties(entity, vo);
		}catch(Exception e){
			logger.error("update ",e);
		}
		return billCheckInvoiceRepository.update(entity);
	}

	@Override
	public PageInfo<BillCheckInvoiceVo> queryReport(
			Map<String, Object> condition, int pageNo, int pageSize) {
		try {
			PageInfo<BillInvoiceEntity> pageInfo=billCheckInvoiceRepository.queryReport(condition, pageNo, pageSize);
			PageInfo<BillCheckInvoiceVo> result=new PageInfo<BillCheckInvoiceVo>();
			
			PropertyUtils.copyProperties(result, pageInfo);
			
			List<BillCheckInvoiceVo> voList = new ArrayList<BillCheckInvoiceVo>();
	    	for(BillInvoiceEntity entity : pageInfo.getList()) {
	    		BillCheckInvoiceVo vo = new BillCheckInvoiceVo();
	    		try {
	                PropertyUtils.copyProperties(vo, entity);
	            } catch (Exception ex) {
	            	logger.error("转换失败:{0}",ex);
	            }
	    		voList.add(vo);
	    	}
			
	    	result.setList(voList);
			return result;
		} catch (Exception ex) {
			logger.error("转换失败:{0}",ex);
        }
		return null;
	}	
}
