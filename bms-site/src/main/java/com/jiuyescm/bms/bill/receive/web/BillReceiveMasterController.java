package com.jiuyescm.bms.bill.receive.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.service.IBillReceiveMasterService;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billReceiveMasterController")
public class BillReceiveMasterController {

	private static final Logger logger = LoggerFactory.getLogger(BillReceiveMasterController.class.getName());

	@Autowired
	private IBillReceiveMasterService billReceiveMasterService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BillReceiveMasterEntity findById(Long id) throws Exception {
		return billReceiveMasterService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillReceiveMasterEntity> page, Map<String, Object> param) {
		PageInfo<BillReceiveMasterEntity> pageInfo = billReceiveMasterService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(BillReceiveMasterEntity entity) {
		if (null == entity.getId()) {
			billReceiveMasterService.save(entity);
		} else {
			billReceiveMasterService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillReceiveMasterEntity entity) {
		billReceiveMasterService.delete(entity.getId());
	}
	
}
