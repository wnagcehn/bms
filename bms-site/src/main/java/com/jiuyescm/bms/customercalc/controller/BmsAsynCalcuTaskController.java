package com.jiuyescm.bms.customercalc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsAsynCalcuTaskController")
public class BmsAsynCalcuTaskController {

	//private static final Logger logger = LoggerFactory.getLogger(BmsAsynCalcuTaskController.class.getName());

	@Autowired
	private IBmsCalcuTaskService bmsAsynCalcuTaskService;
	@Autowired
	private ISystemCodeService systemCodeService;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void query(Page<BmsCalcuTaskVo> page, Map<String, Object> param) throws Exception {
		try {
			PageInfo<BmsCalcuTaskVo> pageInfo = bmsAsynCalcuTaskService.query(param, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}
	
	@DataProvider
	public List<BmsCalcuTaskVo> queryDetail(Map<String, Object> param) throws Exception {
		List<BmsCalcuTaskVo> list = null;
		try {
			list = bmsAsynCalcuTaskService.queryDetail(param);
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
		return list;
	}
	
	@Expose
	public Map<String, String> findUrlBySubjectCode(String code){
		Map<String, String> map = new HashMap<String, String>();
		SystemCodeEntity sysEntity = systemCodeService.getSystemCode("BIZ_URL", code);
		if (null == sysEntity) {
			throw new BizException("请先去数据字典配置对应的路径！");
		}
		map.put(sysEntity.getCodeName(), sysEntity.getExtattr1());
		return map;
	}
}
