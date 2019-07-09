package com.jiuyescm.bms.bill.customer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerInfoEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillCustomerInfoRepository;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.bill.customer.vo.BillCustomerInfoVo;
import com.jiuyescm.bms.common.sequence.repository.SequenceDao;

@Service("billCustomerInfoService")
public class BillCustomerInfoServiceImpl implements IBillCustomerInfoService{

	private static final Logger logger = Logger.getLogger(BillCustomerInfoServiceImpl.class.getName());
	@Autowired
	private IBillCustomerInfoRepository billCustomerInfoRepository;
	@Autowired
	private SequenceDao sequenceDao;
	@Override
	public PageInfo<BillCustomerInfoVo> queryList(
			Map<String, Object> condition, int pageNo, int pageSize) throws Exception {
		PageInfo<BillCustomerInfoVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BillCustomerInfoVo>();
			PageInfo<BillCustomerInfoEntity> pageInfo=billCustomerInfoRepository.queryList(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BillCustomerInfoVo> list=new ArrayList<BillCustomerInfoVo>();
				for(BillCustomerInfoEntity entity:pageInfo.getList()){
					BillCustomerInfoVo voEntity=new BillCustomerInfoVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
		    logger.error("异常",e);
			throw e;
		}
		return pageVoInfo;
	}

	@Override
	public int insertEntity(BillCustomerInfoVo voEntity) throws Exception {
		try{
			BillCustomerInfoEntity entity=new BillCustomerInfoEntity();
			String customerId=sequenceDao.getBillNoOne(BillCustomerInfoEntity.class.getName(), "B11", "0000000");
			voEntity.setCustomerId(customerId);
			PropertyUtils.copyProperties(entity, voEntity);
			return billCustomerInfoRepository.insertEntity(entity);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}

	@Override
	public int updateEntity(BillCustomerInfoVo voEntity) throws Exception {
		
		try{
			BillCustomerInfoEntity entity=new BillCustomerInfoEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return billCustomerInfoRepository.updateEntity(entity);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}

	@Override
	public int removeEntity(BillCustomerInfoVo voEntity) throws Exception {
		try{
			BillCustomerInfoEntity entity=new BillCustomerInfoEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return billCustomerInfoRepository.deleteEntity(entity);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}

	@Override
	public List<BillCustomerInfoVo> queryAll() throws Exception {
		List<BillCustomerInfoVo> volist=null;
		try{
			volist=new ArrayList<BillCustomerInfoVo>();
			List<BillCustomerInfoEntity> list=billCustomerInfoRepository.queryAll();
			for(BillCustomerInfoEntity entity:list){
				BillCustomerInfoVo voEntity=new BillCustomerInfoVo();
				PropertyUtils.copyProperties(voEntity,entity);
				volist.add(voEntity);
			}
		}catch(Exception e){
			logger.error("queryAll:",e);
			throw e;
		}
		return volist;
	}

	@Override
	public int saveBatch(List<BillCustomerInfoVo> volist) throws Exception {
		try{
			List<String> customerIds=sequenceDao.getBillNoList(BillCustomerInfoEntity.class.getName(), "B11", volist.size(), "0000000");
			List<BillCustomerInfoEntity> list=new ArrayList<BillCustomerInfoEntity>();
			int index=0;
			for(BillCustomerInfoVo voEntity:volist){
				voEntity.setCustomerId(customerIds.get(index));
				index++;
				BillCustomerInfoEntity entity=new BillCustomerInfoEntity();
				PropertyUtils.copyProperties(entity,voEntity);
				list.add(entity);
			}
			return billCustomerInfoRepository.saveBatch(list);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
		
	}

	@Override
	public int updateBatch(List<BillCustomerInfoVo> volist) throws Exception {
		try{
			List<BillCustomerInfoEntity> list=new ArrayList<BillCustomerInfoEntity>();
			for(BillCustomerInfoVo voEntity:volist){
				BillCustomerInfoEntity entity=new BillCustomerInfoEntity();
				PropertyUtils.copyProperties(entity,voEntity);
				list.add(entity);
			}
			return billCustomerInfoRepository.updateBatch(list);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}

	@Override
	public boolean checkSysCustomerHasBind(String sysCustomerId,String customerId) {
		try{
			return billCustomerInfoRepository.checkSysCustomerHasBind(sysCustomerId,customerId);
		}catch(Exception e){
		    logger.error("异常", e);
			throw e;
		}
	}
	/**
	 * 检查开票商家名称是否存在,存在true  不存在false
	 */
	@Override
	public boolean checkCustomerNameExist(String customerName) {
		return billCustomerInfoRepository.checkCustomerNameExist(customerName);
	}

}
