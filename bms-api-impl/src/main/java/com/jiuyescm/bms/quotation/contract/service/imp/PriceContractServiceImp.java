package com.jiuyescm.bms.quotation.contract.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractManageEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;

@Service("priceContractService")
public class PriceContractServiceImp implements IPriceContractService{
	private Logger logger = LoggerFactory.getLogger(PriceContractServiceImp.class);

	@Resource
	private IPriceContractDao priceContractDao;
	
	@Override
	public PageInfo<PriceContractInfoEntity> queryAll(
			Map<String, Object> parameter,int aPageNo ,int aPageSize) {
		// TODO Auto-generated method stub
		return priceContractDao.queryAll(parameter, aPageNo, aPageSize);
	}

	@Override
	public int createContract(PriceContractInfoEntity aCondition) {
		// TODO Auto-generated method stub
		return priceContractDao.createContract(aCondition);
	}

	@Override
	public int updateContract(PriceContractInfoEntity aContidion) {
		// TODO Auto-generated method stub
		return priceContractDao.updateContract(aContidion);
	}

	@Override
	public List<PriceGeneralQuotationEntity> findCangchu(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findCangchu(parameter);
	}

	@Override
	public int createContractItem(List<PriceContractItemEntity> acondition) {
		// TODO Auto-generated method stub
		try {
			 priceContractDao.createContractItem(acondition);
			 return 1;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("签约服务失败",e);
			 return 0;
		}
	}

	@Override
	public int updateContractItem(List<PriceContractItemEntity> acondition) {
		// TODO Auto-generated method stub
		return priceContractDao.updateContractItem(acondition);
	}

	@Override
	public List<ContractDetailEntity> findAllContractItemName(String contractId) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllContractItemName(contractId);
	}
	
	@Override
	public List<PriceContractInfoEntity> queryByCustomerId(Map<String, String> param) {
		return priceContractDao.queryByCustomerId(param);
	}
	
	@Override
	public List<PriceContractInfoEntity> queryByCustomerIdAndBizType(Map<String, String> param) {
		return priceContractDao.queryByCustomerIdAndBizType(param);
	}

	@Override
	public List<PriceContractItemEntity> findAllTransportContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllTransportContractItem(parameter);
	}
	
	@Override
	public List<PriceContractItemEntity> findTransportPayFeesContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findTransportPayFeesContractItem(parameter);
	}


	@Override
	public List<ContractDetailEntity> findAllContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllContractItem(parameter);
	}

	@Override
	public List<PriceContractItemEntity> findAllDispatchContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllDispatchContractItem(parameter);
	}

	@Override
	public List<ContractDetailEntity> findAllPayContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllPayContractItem(parameter);
	}

	@Override
	public List<PriceContractInfoEntity> queryContract(
			Map<String, Object> aCondition) {
		// TODO Auto-generated method stub
		return priceContractDao.queryContract(aCondition);
	}

	@Override
	public List<ContractDetailEntity> findAllPayContractDetail(
			Map<String, Object> parameter) {
		return priceContractDao.findAllPayContractDetail(parameter);
	}

	@Override
	public PriceContractInfoEntity queryContractByCustomer(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceContractDao.queryContractByCustomer(condition);
	}

	@Override
	public PriceContractInfoEntity queryById(Integer id) {
		return priceContractDao.queryById(id);
	}

	
	@Override
	public List<ContractManageEntity> queryContractManageEntity(Map<String,Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.queryContractManageEntity(parameter);
	}

	@Override
	public List<String> queryCustomerList() {
		// TODO Auto-generated method stub
		return priceContractDao.queryCustomerList();
	}

	@Override
	public List<ContractDetailEntity> findAllContractDiscountItemName(
			String contractId) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllContractDiscountItemName(contractId);
	}

	@Override
	public List<ContractDetailEntity> findAllContractDiscountItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return priceContractDao.findAllContractDiscountItem(parameter);
	}
}
