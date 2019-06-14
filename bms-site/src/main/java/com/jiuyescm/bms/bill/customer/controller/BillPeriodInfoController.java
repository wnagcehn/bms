package com.jiuyescm.bms.bill.customer.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.bill.customer.BillPeriodInfoEntity;
import com.jiuyescm.bms.bill.customer.service.IBillPeriodInfoService;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billPeriodInfoController")
public class BillPeriodInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BillPeriodInfoController.class.getName());

	@Autowired
	private IBillPeriodInfoService billPeriodInfoService;
	@Autowired
	private ICustomerDictService customerDictService;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillPeriodInfoEntity> page, Map<String, Object> param) {
	    if (null == param) {
            param = new HashMap<String, Object>();
        }
	    param.put("delFlag", "0");
		PageInfo<BillPeriodInfoEntity> pageInfo = billPeriodInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public Map<String, String> save(BillPeriodInfoEntity entity) {
	    Map<String, String> result = Maps.newLinkedHashMap();
	    Map<String, Object> map = Maps.newLinkedHashMap();
        if (StringUtils.isBlank(entity.getBasicCode())) {
            result.put("fail", "应收款起算基准位必填项，不可为空！");
            return result;
        } 
	    if (null == entity.getAddMonth() && null == entity.getAddDay()) {
	        result.put("fail", "加月数与加天数必填一个！");
            return result;
        }
	    if (null != entity.getAddDay() && entity.getAddDay() < 0) {
	        result.put("fail", "加天数不能为负数！");
            return result;
        }   
	    
	    if (entity.getId() != null) {
	        map.put("id", entity.getId());
	    }
	    map.put("mkInvoiceName", entity.getInvoiceName());
        List<BillPeriodInfoEntity> list = billPeriodInfoService.queryByCustomer(map);
        if (CollectionUtils.isNotEmpty(list)) {
            result.put("fail", "该商家已有账期设置！");
            return result;
        }
	    
	    //合同商家名称校验
	    if (StringUtils.isNotBlank(entity.getInvoiceName())) {
	        
	        PubCustomerBaseEntity cusEntity = null;
	        try {
	            cusEntity = customerDictService.getMkIdByMkInvoiceNameNew(entity.getInvoiceName());
            } catch (Exception e) {
                logger.info("查询到多个商家", e);
                result.put("fail", "查询到多个商家，请检查商家是否精确！");
                return result;
            }
	        if (null == cusEntity) {
	            result.put("fail", "查询商家不存在，请检查商家是否精确！");
	            return result;
            }else {
                entity.setMkId(cusEntity.getMkId());
                entity.setInvoiceName(cusEntity.getMkInvoiceName());
            }
        }
	    try {
	        if (null == entity.getId()) {
	            entity.setDelFlag("0");
	            entity.setCrePerson(ContextHolder.getLoginUser().getCname());
	            entity.setCrePersonId(ContextHolder.getLoginUserName());
	            entity.setCreTime(new Timestamp(System.currentTimeMillis()));
	            billPeriodInfoService.save(entity);
	        } else {
	            entity.setModPerson(ContextHolder.getLoginUser().getCname());
	            entity.setModPersonId(ContextHolder.getLoginUserName());
	            entity.setModTime(new Timestamp(System.currentTimeMillis()));
	            billPeriodInfoService.update(entity);
	        }
        } catch (Exception e) {
            logger.error("操作异常：", e);
            result.put("fail", "操作异常！" + e);
            return result;
        }
		result.put("success", "操作成功！");
		return result;
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillPeriodInfoEntity entity) {
	    entity.setDelFlag("1");
	    entity.setModPerson(ContextHolder.getLoginUser().getCname());
        entity.setModPersonId(ContextHolder.getLoginUserName());
        entity.setModTime(new Timestamp(System.currentTimeMillis()));
		billPeriodInfoService.delete(entity);
	}
	
}
