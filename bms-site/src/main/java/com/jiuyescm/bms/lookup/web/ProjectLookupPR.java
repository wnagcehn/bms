package com.jiuyescm.bms.lookup.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.ProjectQueryVo;
import com.jiuyescm.mdm.customer.vo.ProjectVo;

@Component("projectLookupPR")
public class ProjectLookupPR {

	@Autowired
	private IProjectService projectService;
	
	@DataProvider
	public void query(Page<ProjectVo> page,ProjectQueryVo queryCondition){
		if(queryCondition==null){
			queryCondition=new ProjectQueryVo();
		}
		queryCondition.setDelflag(0);
		PageInfo<ProjectVo> tmpPageInfo = projectService.query(queryCondition, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
}
