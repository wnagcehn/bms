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
import com.jiuyescm.bms.billcheck.BillCheckReceiptEntity;
import com.jiuyescm.bms.billcheck.entity.BillReceiptEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckReceiptRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckReceiptService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptVo;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.exception.BizException;

@Service("billCheckReceiptService")
public class BillCheckReceiptServiceImp implements IBillCheckReceiptService{
	
	private static final Logger logger = Logger.getLogger(BillCheckReceiptServiceImp.class.getName());


	@Resource private IBillCheckReceiptRepository billCheckReceiptRepository;
	@Resource private IBillCheckInfoRepository billCheckInfoRepository;
	@Resource private IBmsGroupUserRepository bmsGroupUserRepository;
	@Resource private IBillCheckLogRepository billCheckLogRepository;
	
	@Override
	public PageInfo<BillCheckReceiptVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		PageInfo<BillCheckReceiptEntity> pageInfo=billCheckReceiptRepository.query(condition, pageNo, pageSize);
		PageInfo<BillCheckReceiptVo> result=new PageInfo<BillCheckReceiptVo>();

		List<BillCheckReceiptVo> voList = new ArrayList<BillCheckReceiptVo>();
    	for(BillCheckReceiptEntity entity : pageInfo.getList()) {
    		BillCheckReceiptVo vo = new BillCheckReceiptVo();
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
	public BillCheckReceiptVo queyReceipt(Map<String, Object> condition) {
		BillCheckReceiptVo vo=new BillCheckReceiptVo();
		BillCheckReceiptEntity entity=billCheckReceiptRepository.queyReceipt(condition);
		if(entity==null){
			return null;
		}
		try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		return vo;
	}

	@Override
	public int saveList(List<BillCheckReceiptVo> list) {
		List<BillCheckReceiptEntity> enList=new ArrayList<BillCheckReceiptEntity>();
		for(BillCheckReceiptVo vo : list) {
			BillCheckReceiptEntity entity = new BillCheckReceiptEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(entity);
    	}
		
		return billCheckReceiptRepository.saveList(enList);
	}
	
	@Override
	public List<BillCheckReceiptVo> queryByParam(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BillCheckReceiptEntity> list=billCheckReceiptRepository.queryByParam(condition);
		List<BillCheckReceiptVo> voList = new ArrayList<BillCheckReceiptVo>();
    	for(BillCheckReceiptEntity entity : list) {
    		BillCheckReceiptVo vo = new BillCheckReceiptVo();
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
	public int save(BillCheckReceiptVo vo) {
		// TODO Auto-generated method stub
		BillCheckReceiptEntity entity=new BillCheckReceiptEntity();	
		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
        	logger.error("转换失败:",ex);
        }
		return billCheckReceiptRepository.save(entity);
	}
	
	
	@Override
	public int updateOne(BillCheckReceiptVo vo) {
		// TODO Auto-generated method stub
		BillCheckReceiptEntity entity=new BillCheckReceiptEntity();	
		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
        	logger.error("转换失败:",ex);
        }
		return billCheckReceiptRepository.update(entity);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public void saveImportList(List<BillCheckReceiptVo> list,
			List<BillCheckInfoVo> checkVoList) throws Exception {
		
		try{
			List<BillCheckReceiptEntity> receiptList=new ArrayList<BillCheckReceiptEntity>();
			List<BillCheckInfoEntity> infoList=new ArrayList<BillCheckInfoEntity>();
			for(BillCheckReceiptVo vo:list){
				BillCheckReceiptEntity entity=new BillCheckReceiptEntity();
				PropertyUtils.copyProperties(entity, vo);
				receiptList.add(entity);
			}
			for(BillCheckInfoVo  checkVo:checkVoList){
				BillCheckInfoEntity checkEntity=new BillCheckInfoEntity();
				PropertyUtils.copyProperties(checkEntity, checkVo);
				infoList.add(checkEntity);
			}
			billCheckReceiptRepository.saveList(receiptList);
			
			for(BillCheckInfoEntity en:infoList){
				Map<String, Object> param=new HashMap<>();
				param.put("id", en.getId());
				BillCheckReceiptEntity billCheckReceipt=billCheckReceiptRepository.queyReceipt(param);
				if(billCheckReceipt!=null){
					en.setReceiptDate(billCheckReceipt.getReceiptDate());
				}
			}
			
			billCheckInfoRepository.updateReceiptStatus(infoList);
		}catch(Exception e){
			logger.error("saveImportList ",e);
			throw e;
		}
	}

	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int update(BillCheckReceiptVo vo) {
		BillCheckReceiptEntity entity=new BillCheckReceiptEntity();

		try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
        	logger.error("转换失败:",ex);
        }
		
		//更新账单金额和状态
		BigDecimal billmoney=new BigDecimal(0);
		//调整金额
		BigDecimal adjustMoney=new BigDecimal(0);
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("id", vo.getBillCheckId());
		BillCheckInfoEntity bInfoEntity=billCheckInfoRepository.queryOne(condition);
		if(bInfoEntity!=null){
			//账单中的确认金额+调整金额  和回款金额以及删除的回款进行比较
			billmoney=bInfoEntity.getConfirmAmount();
			
			//调整的金额
			condition.clear();
			condition.put("billCheckId", vo.getBillCheckId());
			BillCheckAdjustInfoEntity adjustEntity = billCheckInfoRepository.queryOneAdjust(condition);
			if(adjustEntity!=null && adjustEntity.getAdjustAmount()!=null){
				adjustMoney=adjustEntity.getAdjustAmount();
				billmoney=billmoney.add(adjustEntity.getAdjustAmount());
			}

			
			BigDecimal receiptMoney=new BigDecimal(0);
			if(bInfoEntity.getReceiptAmount()!=null){
				receiptMoney=bInfoEntity.getReceiptAmount();
			}
			if(vo.getReceiptAmount()!=null){
				receiptMoney=receiptMoney.subtract(vo.getReceiptAmount());
			}
			if(receiptMoney.compareTo(billmoney)>=0){
				bInfoEntity.setReceiptAmount(receiptMoney);
				//回款状态
				bInfoEntity.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());//已回款
			}else if(receiptMoney.compareTo(BigDecimal.ZERO)==0){
				bInfoEntity.setReceiptAmount(receiptMoney);
				//回款状态
				bInfoEntity.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());//未回款
			}else{
				bInfoEntity.setReceiptAmount(receiptMoney);
				bInfoEntity.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());//部分回款
				//账单状态（待回款）
			}
		}
		
		int result=billCheckReceiptRepository.update(entity);
		if(result>0){
			//收款日期，未收款金额
			Map<String, Object> param=new HashMap<>();
			param.put("id", bInfoEntity.getId());
			BillCheckReceiptEntity billCheckReceipt=billCheckReceiptRepository.queyReceipt(param);
			if(billCheckReceipt!=null){
				bInfoEntity.setReceiptDate(billCheckReceipt.getReceiptDate());
			}
			//未收款金额
			bInfoEntity.setUnReceiptAmount(billmoney.subtract(bInfoEntity.getReceiptAmount()));			
			//开票未回款金额
			BigDecimal invoiceMoney=new BigDecimal(0);
			if(bInfoEntity.getInvoiceAmount()!=null){
				invoiceMoney=bInfoEntity.getInvoiceAmount();
			}
			bInfoEntity.setInvoiceUnReceiptAmount(invoiceMoney.subtract(bInfoEntity.getReceiptAmount()).add(adjustMoney));
			billCheckInfoRepository.update(bInfoEntity);
		}
		
		
		return result;
	}

	@Override
	public PageInfo<BillCheckReceiptVo> queryReport(
			Map<String, Object> condition, int pageNo, int pageSize) {
		try{
			PageInfo<BillReceiptEntity> pageInfo=billCheckReceiptRepository.queryReport(condition, pageNo, pageSize);
			PageInfo<BillCheckReceiptVo> result=new PageInfo<BillCheckReceiptVo>();
	
			PropertyUtils.copyProperties(result, pageInfo);
			
			List<BillCheckReceiptVo> voList = new ArrayList<BillCheckReceiptVo>();
	    	for(BillReceiptEntity entity : pageInfo.getList()) {
	    		BillCheckReceiptVo vo = new BillCheckReceiptVo();
	    		try {
	                PropertyUtils.copyProperties(vo, entity);
	            } catch (Exception ex) {
	            	logger.error("转换失败:",ex);
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
