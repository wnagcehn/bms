
package com.jiuyescm.bms.quotation.contract.repository.imp.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractManageEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("priceContractDao")
@SuppressWarnings("rawtypes")
public class PriceContractDaoImp extends MyBatisDao implements IPriceContractDao{

	/**
	 * 查询出所有的商家合同
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<PriceContractInfoEntity> queryAll(
			Map<String, Object> parameter, int aPageNo,int aPageSize) {
		List<PriceContractInfoEntity> list=selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryAll", parameter,new RowBounds(aPageNo,aPageSize));
		PageInfo<PriceContractInfoEntity> pList=new PageInfo<>(list);
		return pList;
	}

	/**
	 * 保存商家合同信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int createContract(PriceContractInfoEntity aCondition) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.createContract", aCondition);
	}

	/**
	 * 修改商家合同信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int updateContract(PriceContractInfoEntity aCondition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.updateContract", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PriceGeneralQuotationEntity> findCangchu(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findCangchu", parameter);
	}

	/**
	 * 新增商家合同明细
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int createContractItem(List<PriceContractItemEntity> acondition) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.createContractItem", acondition);
	}

	/**
	 * 修改商家合同明细
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int updateContractItem(List<PriceContractItemEntity> acondition) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.updateContractItem", acondition);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ContractDetailEntity> findAllContractItemName(String contractId) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findAllContractItem", contractId);
	}

	
	@Override
	public List<ContractDetailEntity> findAllContractDiscountItemName(
			String contractId) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findAllContractDiscountItem", contractId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PriceContractInfoEntity> queryByCustomerId(Map<String, String> param) {
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryByCustomerId", param);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PriceContractInfoEntity> queryByCustomerIdAndBizType(Map<String, String> param) {
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryByCustomerIdAndBizType", param);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PriceContractItemEntity> findAllTransportContractItem(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findTransportContractItem", parameter);
	}
	



	
	@Override
	public List<PriceContractItemEntity> findTransportPayFeesContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findTransportPayFeesContractItem", parameter);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ContractDetailEntity> findAllContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findAllReceiveContractDetail", parameter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContractDetailEntity> findAllContractDiscountItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findAllReceiveContractDiscountDetail", parameter);
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public List<PriceContractItemEntity> findAllDispatchContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findDispatchContractItem", parameter);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ContractDetailEntity> findAllPayContractItem(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findAllPayContractItem", parameter);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PriceContractInfoEntity> queryContract(
			Map<String, Object> aCondition) {
		// TODO Auto-generated method stub
		List<PriceContractInfoEntity> list=selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryContract", aCondition);
		return list;
	}

	@Override
	public List<ContractDetailEntity> findAllPayContractDetail(
			Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.findAllPayContractDetail", parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PriceContractInfoEntity queryContractByCustomer(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		if(condition.get("customerid")==null){
			return null;
		}
		try{
			return (PriceContractInfoEntity)selectOne("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryContractByCustomer", condition);
		}
		catch(Exception ex){
			return null;
		}
	}

	@Override
	public PriceContractInfoEntity queryById(Integer id) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("id",  id);
		Object obj=this.selectOne("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryById", map);
		if(obj!=null){
			return (PriceContractInfoEntity)obj;
		}
		return null;
	}

	public List<ContractManageEntity> queryContractManageEntity(Map<String,Object> parameter) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryContractManageEntity", parameter);
	}

	@Override
	public List<String> queryCustomerList() {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryCustomerList", "");
	}

	@Override
	public PriceContractItemEntity queryTempByContractCodeAndSubjectId(Map<String, String> param){
		List<PriceContractItemEntity> list = this.getSqlSessionTemplate().selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractMapper.queryTempByContractCodeAndSubjectId", param);
		return list.size() > 0 ? list.get(0) : null;
	}
	
}