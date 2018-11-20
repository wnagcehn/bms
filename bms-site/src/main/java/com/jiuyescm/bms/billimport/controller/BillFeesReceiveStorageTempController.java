package com.jiuyescm.bms.billimport.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billFeesReceiveStorageTempController")
public class BillFeesReceiveStorageTempController {

	//private static final Logger logger = LoggerFactory.getLogger(BillFeesReceiveStorageTempController.class.getName());

	@Autowired
	private IBillFeesReceiveStorageTempService billFeesReceiveStorageTempService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BillFeesReceiveStorageTempEntity findById(Long id) throws Exception {
		return billFeesReceiveStorageTempService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillFeesReceiveStorageTempEntity> page, Map<String, Object> param) {
		PageInfo<BillFeesReceiveStorageTempEntity> pageInfo = billFeesReceiveStorageTempService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(BillFeesReceiveStorageTempEntity entity) {
		if (null == entity.getId()) {
			billFeesReceiveStorageTempService.save(entity);
		} else {
			billFeesReceiveStorageTempService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillFeesReceiveStorageTempEntity entity) {
		billFeesReceiveStorageTempService.delete(entity.getId());
	}
	
}
