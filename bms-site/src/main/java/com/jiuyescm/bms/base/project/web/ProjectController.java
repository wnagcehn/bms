package com.jiuyescm.bms.base.project.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.customer.vo.ProjectQueryVo;
import com.jiuyescm.mdm.customer.vo.ProjectVo;

@Controller("projectController")
public class ProjectController {

	@Resource
	private IProjectService projectService;
	
	@DataProvider
	public void query(Page<ProjectVo> page,ProjectQueryVo queryCondition){
		PageInfo<ProjectVo> tmpPageInfo = projectService.query(queryCondition, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@DataProvider
	public Map<Integer, String> getInvalidflag(String all) {
		Map<Integer, String> mapValue = new HashMap<Integer, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put(-1, "全部");
		}
		mapValue.put(0, "未作废");
		mapValue.put(1, "已作废");
		return mapValue;
	}
	
	@DataProvider
	public void queryCusProject(Page<CusprojectRuleVo> page,ProjectVo queryCondition){
		PageInfo<CusprojectRuleVo> tmpPageInfo = projectService.querybyProject(queryCondition, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
}
