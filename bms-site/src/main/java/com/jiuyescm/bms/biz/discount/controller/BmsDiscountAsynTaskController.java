package com.jiuyescm.bms.biz.discount.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsDiscountAsynTaskService;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsDiscountAsynTaskController")
public class BmsDiscountAsynTaskController {

	//private static final Logger logger = LoggerFactory.getLogger(BmsDiscountAsynTaskController.class.getName());

	@Autowired
	private IBmsDiscountAsynTaskService bmsDiscountAsynTaskService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BmsDiscountAsynTaskEntity findById(Long id) throws Exception {
		return bmsDiscountAsynTaskService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsDiscountAsynTaskEntity> page, Map<String, Object> param) {
		PageInfo<BmsDiscountAsynTaskEntity> pageInfo = bmsDiscountAsynTaskService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(BmsDiscountAsynTaskEntity entity) {
		if (null == entity.getId()) {
			entity.setCreateMonth(entity.getYear()+entity.getMonth());
			entity.setTaskRate(0);
			entity.setDelFlag("0");
			entity.setTaskStatus("WAIT");
			entity.setCreator(JAppContext.currentUserName());
			entity.setCreateTime(JAppContext.currentTimestamp());
			bmsDiscountAsynTaskService.save(entity);
		} else {
			bmsDiscountAsynTaskService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsDiscountAsynTaskEntity entity) {
		bmsDiscountAsynTaskService.delete(entity.getId());
	}
	
}
