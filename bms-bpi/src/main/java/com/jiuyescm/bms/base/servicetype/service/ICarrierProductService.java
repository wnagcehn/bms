package com.jiuyescm.bms.base.servicetype.service;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.servicetype.vo.CarrierProductVo;
/**
 * ..Service
 * @author wangchen&liuzhicheng
 * 
 */
public interface ICarrierProductService {

	CarrierProductVo findById(Long id) throws Exception;
	
    PageInfo<CarrierProductVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

	List<CarrierProductVo> query(Map<String, Object> condition) throws Exception;

	CarrierProductVo save(CarrierProductVo vo) throws Exception;

    CarrierProductVo update(CarrierProductVo vo) throws Exception;
    
    /**
     * 批量导入 查出该物流商下的物流产品类型
     * @param carrierid
     * @return
     */
	List<CarrierProductVo> queryByCarrierid(String carrierid) throws Exception;
	
	/**
	 * 查询缓存，有值返回CarrierName，无值返回空字符串
	 * @param carrierid
	 * @param servicecode
	 * @return
	 * @throws Exception
	 */
	String getCarrierNameById(String carrierid,String servicecode) throws Exception;
	
	/**
	 * 查询缓存，返回CarrierProductVo对象
	 * @param carrierid
	 * @param servicecode
	 * @return
	 * @throws Exception
	 */
	CarrierProductVo findByCode(String carrierid,String servicecode) throws Exception;

}
