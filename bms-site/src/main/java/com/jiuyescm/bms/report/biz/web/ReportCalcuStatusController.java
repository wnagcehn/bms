package com.jiuyescm.bms.report.biz.web;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCalcuStatusEntity;
import com.jiuyescm.bms.report.month.service.IReportCalcuStatusService;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("reportCalcuStatusController")
public class ReportCalcuStatusController {

	//private static final Logger logger = LoggerFactory.getLogger(ReportCalcuStatusController.class.getName());

	@Autowired
	private IReportCalcuStatusService reportCalcuStatusService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public ReportCalcuStatusEntity findById(Long id) throws Exception {
		return reportCalcuStatusService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<ReportCalcuStatusEntity> page, Map<String, Object> param) {
		PageInfo<ReportCalcuStatusEntity> pageInfo = reportCalcuStatusService.query(param, page.getPageNo(), page.getPageSize());
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
	public void save(ReportCalcuStatusEntity entity) {
		if (null == entity.getId()) {
			reportCalcuStatusService.save(entity);
		} else {
			reportCalcuStatusService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(ReportCalcuStatusEntity entity) {
		reportCalcuStatusService.delete(entity.getId());
	}
	
}
