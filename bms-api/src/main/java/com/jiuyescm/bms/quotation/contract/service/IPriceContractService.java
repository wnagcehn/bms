package com.jiuyescm.bms.quotation.contract.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.ContractManageEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;

public interface IPriceContractService {
	/**
	 * 查询所有的商家合同信息
	 * @param aCondition
	 */
	public PageInfo<PriceContractInfoEntity> queryAll(Map<String,Object> parameter,int aPageNo,int aPageSize);

	/**
	 * 保存商家合同信息
	 * @param aCondition
	 * @return
	 */
	public int createContract(PriceContractInfoEntity aCondition);
	
	/**
	 * 修改商家合同信息
	 * @param aContidion
	 * @return
	 */
	public int updateContract(PriceContractInfoEntity aContidion);
	
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
	 * 查询出所有的合同明细信息
	 * @param parameter
	 * @param aPageNo
	 * @param aPageSize
	 * @return
	 */
	public List<ContractDetailEntity> findAllContractItemName(String contractId);
	
	/**
	 * 查询出所有的合同明细信息
	 * @param parameter
	 * @param aPageNo
	 * @param aPageSize
	 * @return
	 */
	public List<ContractDetailEntity> findAllContractDiscountItemName(String contractId);
	
	/**
	 * 根据map条件查询对应的合同明细信息
	 * @param parameter
	 * @return
	 */
	public List<ContractDetailEntity> findAllContractItem(Map<String,Object> parameter);
	
	/**
	 * 根据map条件查询对应的合同明细信息
	 * @param parameter
	 * @return
	 */
	public List<ContractDetailEntity> findAllContractDiscountItem(Map<String,Object> parameter);
	
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
    public List<ContractManageEntity> queryContractManageEntity(Map<String, Object> condition);

    /**
     * 查询出所有已签约过的商家id
     * @return
     */
    public List<String> queryCustomerList();
    
    /**
     * 查询商家下所有业务类型
     * @param customerId
     * @return
     */
	List<PriceContractInfoEntity> queryByCustomerId(Map<String, String> param);
	
	/**
	 * 查询商家下的业务类型下的费用科目
	 * @param param
	 * @return
	 */
	List<PriceContractInfoEntity> queryByCustomerIdAndBizType(Map<String, String> param);
	
}
