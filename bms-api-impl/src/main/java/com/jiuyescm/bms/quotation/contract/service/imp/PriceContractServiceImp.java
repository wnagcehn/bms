package com.jiuyescm.bms.quotation.contract.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
		return priceContractDao.createContractItem(acondition);
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
}
