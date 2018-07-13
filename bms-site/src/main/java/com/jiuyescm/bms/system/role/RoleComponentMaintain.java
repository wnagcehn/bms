package com.jiuyescm.bms.system.role;

import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.model.Role;
import com.bstek.bdf2.core.view.role.RoleMaintain;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component("bms.roleComponentMaintain")
public class RoleComponentMaintain extends RoleMaintain {
	
	@DataProvider
	public void loadRoles(Page<Role> page,Criteria criteria) throws Exception{
		if (criteria==null) {
			criteria = new Criteria();
		}
		SingleValueFilterCriterion parentid = new SingleValueFilterCriterion();
		
		String appid = Configure.getString("jiuye.appId");
		parentid.setExpression(appid);
		parentid.setProperty("parentId");
		parentid.setValue(appid);
		parentid.setFilterOperator(FilterOperator.eq);
		criteria.addCriterion(parentid);
		
		super.loadRoles(page, criteria);
	}
	
}
