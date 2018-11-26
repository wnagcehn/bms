package com.jiuyescm.bms.bill.receive.web;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterRecordService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterRecordVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billReceiveMasterRecordController")
public class BillReceiveMasterRecordController {

	//private static final Logger logger = LoggerFactory.getLogger(BillReceiveMasterRecordController.class.getName());

	@Autowired
	private IBillReceiveMasterRecordService billReceiveMasterRecordService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BillReceiveMasterRecordVo findById(Long id) throws Exception {
		return billReceiveMasterRecordService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillReceiveMasterRecordVo> page, Map<String, Object> param) {
		PageInfo<BillReceiveMasterRecordVo> pageInfo = billReceiveMasterRecordService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(BillReceiveMasterRecordVo entity) {
		if (null == entity.getId()) {
			billReceiveMasterRecordService.save(entity);
		} else {
			billReceiveMasterRecordService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillReceiveMasterRecordVo entity) {
		billReceiveMasterRecordService.delete(entity.getId());
	}
	
}
