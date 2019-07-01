package com.jiuyescm.bms.bill.customer.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerDetailService;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billCustomerDetailController")
public class BillCustomerDetailController {

	private static final Logger logger = LoggerFactory.getLogger(BillCustomerDetailController.class.getName());

	@Autowired
	private IBillCustomerDetailService billCustomerDetailService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BillCustomerDetailEntity findById(Long id) throws Exception {
		return billCustomerDetailService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillCustomerDetailEntity> page, Map<String, Object> param) {
	    if (null == param) {
            param = new HashMap<String, Object>();
        }
	    try {
	        if(param.get("receiptDate")!="" && param.get("receiptDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("receiptDate"));
	            dateString=dateString+" 00:00:00";
	            param.put("receiptDate", formatter.parse(dateString));
	        }
	        if(param.get("receiptEndDate")!="" && param.get("receiptEndDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("receiptEndDate"));
	            dateString=dateString+" 23:59:59";
	            param.put("receiptEndDate", formatter.parse(dateString));
	        }
	        if(param.get("invoiceDate")!="" && param.get("invoiceDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("invoiceDate"));
	            dateString=dateString+" 00:00:00";
	            param.put("invoiceDate", formatter.parse(dateString));
	        }
	        if(param.get("invoiceEndDate")!="" && param.get("invoiceEndDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("invoiceEndDate"));
	            dateString=dateString+" 23:59:59";
	            param.put("invoiceEndDate", formatter.parse(dateString));
	        } 
        } catch (Exception e) {
            logger.error("日期转换异常：", e);
            throw new BizException("日期转换异常！");
        }    
		PageInfo<BillCustomerDetailEntity> pageInfo = billCustomerDetailService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(BillCustomerDetailEntity entity) {
		if (null == entity.getId()) {
			billCustomerDetailService.save(entity);
		} else {
			billCustomerDetailService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillCustomerDetailEntity entity) {
		billCustomerDetailService.delete(entity.getId());
	}
	
}
