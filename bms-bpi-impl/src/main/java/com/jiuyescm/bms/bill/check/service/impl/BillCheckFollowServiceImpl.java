package com.jiuyescm.bms.bill.check.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.check.BillCheckFollowEntity;
import com.jiuyescm.bms.bill.check.BillCheckInfoFollowEntity;
import com.jiuyescm.bms.bill.check.repository.IBillCheckFollowRepository;
import com.jiuyescm.bms.bill.check.service.IBillCheckFollowService;
import com.jiuyescm.bms.bill.check.vo.BillCheckFollowVo;
import com.jiuyescm.bms.bill.check.vo.BillCheckInfoFollowVo;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.exception.BizException;

@Service("billCheckFollowService")
public class BillCheckFollowServiceImpl implements IBillCheckFollowService {

	private static final Logger logger = Logger.getLogger(BillCheckFollowServiceImpl.class.getName());
	@Autowired
	private IBillCheckFollowRepository billCheckFollowRepository;
	@Resource 
	private IBillCheckLogRepository billCheckLogRepository;

	@Override
	public int addBillCheckFollow(BillCheckFollowVo voEntity) throws Exception {
		try{
			
			BillCheckFollowEntity entity=new BillCheckFollowEntity();
			PropertyUtils.copyProperties(entity, voEntity);

			billCheckFollowRepository.addBillCheckFollowEntity(entity);
			return 1;
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int addBillCheckFollow(BillCheckFollowVo voEntity,
			BillCheckLogVo logVoEntity) throws Exception {
		try{
			BillCheckFollowEntity entity=new BillCheckFollowEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			BillCheckLogEntity logEntity=new BillCheckLogEntity();
			PropertyUtils.copyProperties(logEntity, logVoEntity);
			
			BillCheckFollowEntity returnEntity= billCheckFollowRepository.addBillCheckFollowEntity(entity);
			logEntity.setBillFollowId(returnEntity.getId());
			billCheckLogRepository.addCheckLog(logEntity);
			return 1;
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}

	@Override
	public PageInfo<BillCheckInfoFollowVo> queryAllCheckInfo(
			Map<String, Object> condition, int pageNo, int pageSize) throws Exception {
		PageInfo<BillCheckInfoFollowVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BillCheckInfoFollowVo>();
			PageInfo<BillCheckInfoFollowEntity> pageInfo=billCheckFollowRepository.queryList(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BillCheckInfoFollowVo> list=new ArrayList<BillCheckInfoFollowVo>();
				for(BillCheckInfoFollowEntity entity:pageInfo.getList()){
					BillCheckInfoFollowVo voEntity=new BillCheckInfoFollowVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
		return pageVoInfo;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int continueDeal(BillCheckFollowVo voEntity, BillCheckLogVo logVo) throws Exception {
		try{
			BillCheckFollowEntity entity=new BillCheckFollowEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			BillCheckLogEntity logEntity=new BillCheckLogEntity();
			PropertyUtils.copyProperties(logEntity, logVo);
			
			billCheckLogRepository.addCheckLog(logEntity);
			return billCheckFollowRepository.updateFollowStatus(entity);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int finishProcess(BillCheckFollowVo voEntity, BillCheckLogVo logVo) throws Exception {
		try{
			BillCheckFollowEntity entity=new BillCheckFollowEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			BillCheckLogEntity logEntity=new BillCheckLogEntity();
			PropertyUtils.copyProperties(logEntity, logVo);
			
			billCheckLogRepository.addCheckLog(logEntity);
			return billCheckFollowRepository.finishFollow(entity);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}

	@Override
	public boolean checkFollowManExist(String followManId) {
		
		return billCheckFollowRepository.checkFollowManExist(followManId);
	}
	
}
