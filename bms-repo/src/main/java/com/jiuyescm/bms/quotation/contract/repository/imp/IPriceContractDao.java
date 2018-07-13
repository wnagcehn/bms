package com.jiuyescm.bms.quotation.contract.repository.imp;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractManageEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;

public interface IPriceContractDao {
	/**
	 * 查询所有的商家合同信息
	 * @param aCondition
	 */
	public PageInfo<PriceContractInfoEntity> queryAll(Map<String,Object> parameter,int aPageSize,int aPageNo);

	/**
	 * 保存商家合同信息
	 * @param aCondition
	 * @return
	 */
	public int createContract(PriceContractInfoEntity aCondition);
	
	/**
	 * 修改商家合同信息
	 * @param aCondition
	 * @return
	 */
	public int updateContract(PriceContractInfoEntity aCondition);
	
	/**
	 * 根据条件查询对应的合同
	 * @param aCondition
	 * @return
	 */
	public List<PriceContractInfoEntity> queryContract(Map<String,Object> aCondition);
	
	
	/**
	 * 查询所有的仓储报价模板
	 */
	List<PriceGeneralQuotationEntity> findCangchu(Map<String,Object> parameter);

	
	/**
	 * 新增商家合同具体信息
	 * @param acondition
	 * @return
	 */
	public int createContractItem(List<PriceContractItemEntity> acondition);

	/**
	 * 修改商家合同具体信息
	 * @param acondition
	 * @return
	 */
	public int updateContractItem(List<PriceContractItemEntity> acondition);
	
	/**
	 * 查询商家合同对应的签约服务
	 * @return
	 */
	public List<ContractDetailEntity> findAllContractItemName(String contractId);
	
	/**
	 * 查询合同下所有的所有应收运输报价
	 * @param parameter
	 * @return
	 */
	public List<PriceContractItemEntity> findAllTransportContractItem(Map<String,Object> parameter);
	
	/**
	 * 查询合同下所有的所有应付运输报价
	 * @param parameter
	 * @return
	 */
	public List<PriceContractItemEntity> findTransportPayFeesContractItem(Map<String,Object> parameter);


	/**
	 * 根据map条件查询对应的合同明细信息
	 * @param parameter
	 * @return
	 */
	public List<ContractDetailEntity> findAllContractItem(Map<String,Object> parameter);

	/**
	 * 查询合同下所有的所有配送报价
	 * @param parameter
	 * @return
	 */
	public List<PriceContractItemEntity> findAllDispatchContractItem(Map<String,Object> parameter);

	
	
	/**
	 * 根据map条件查询对应的承运商合同和宅配商合同明细信息
	 * @param parameter
	 * @return
	 */
	public List<ContractDetailEntity> findAllPayContractItem(Map<String,Object> parameter);

	public List<ContractDetailEntity> findAllPayContractDetail(
			Map<String, Object> parameter);

    PriceContractInfoEntity queryContractByCustomer(Map<String, Object> condition);

	public PriceContractInfoEntity queryById(Integer id);
    
    /**
     * 统计异常合同的信息
     * @return
     */
    public List<ContractManageEntity> queryContractManageEntity(Map<String,Object> parameter);

    /**
     * 查询出所有已签约过的商家id
     * @return
     */
    public List<String> queryCustomerList();
}
