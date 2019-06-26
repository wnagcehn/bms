package com.jiuyescm.bms.base.dict.api;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.entity.PubCustomerLookupEntity;
import com.jiuyescm.bms.base.dict.vo.PubCustomerBaseVo;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;

/**
 * 商家管理与查询服务
 * @author caojianwei&liuzhicheng
 *
 */
public interface ICustomerDictService {

	/**
	 * 获取所有商家 的商家编码，商家名称映射关系
	 * @return Map<商家编码,商家名称>
	 */
	Map<String, String> getCustomerDictForKey();
	
	/**
	 * 获取所有商家 的 商家名称,商家编码映射关系
	 * @return Map<商家名称,商家编码>
	 */
	Map<String, String> getCustomerDictForValue();
	
	/**
	 * 根据商家编码获取商家名称
	 * @param code 商家ID
	 * @return
	 */
	String getCustomerNameByCode(String code);
	
	/**
	 * 根据商家名称获取商家编码
	 * @param name 商家全称
	 * @return
	 */
	String getCustomerCodeByName(String name);
	
	/**
	 * 通过商家ID查询合同商家名称
	 * @param ID 商家ID
	 * @return
	 */
	String getMkInvoiceNameByCustomerId(String customerId);
	
	/**
	 * 通过合同商家名称查询合同商家ID
	 * @param mkInvoiceName 合同商家名称
	 * @return
	 */
	String getMkIdByMkInvoiceName(String mkInvoiceName);
	
	/**
	 * 通过合同商家ID查询合同商家名称
	 * @param MkId 合同商家ID
	 * @return
	 */
	String getMkInvoiceNameByMkId(String mkId);
	
	/**
	 * 分页查询合同商家
	 * @return
	 */
	PageInfo<PubCustomerBaseVo> queryPubCustomerBase(Map<String, Object> condition,int pageNo,int pageSize);
	
	/**
	 * 分页查询商家
	 * @return
	 */
	PageInfo<PubCustomerVo> queryPubCustomer(Map<String, Object> condition,int pageNo,int pageSize);

	PageInfo<PubCustomerLookupEntity> queryPubCustomerLookup(
			Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 查询商家
	 * @param condition
	 * @return
	 */
	List<PubCustomerEntity> queryAllCus(Map<String, Object> condition);
	
	/**
	 * 根据商家ID 查询商家信息
	 * @param customerId
	 * @return
	 */
	PubCustomerVo queryById(String customerId);

	/**
	 * 通过主商家名称获取ID
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年6月13日 下午2:36:43
	 *
	 * @param mkInvoiceName
	 * @return
	 */
    PubCustomerBaseEntity getMkIdByMkInvoiceNameNew(String mkInvoiceName);
}
