package com.jiuyescm.bms.base.customer.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.system.service.IBmsJiuyeQuotationSystemService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Controller("customerController")
public class CustomerController {

	@Autowired private ICustomerService customerService;
	
	@Autowired
	private IBmsJiuyeQuotationSystemService bmsCustomerService;
	
	@DataProvider
	public void query(Page<CustomerVo> page,Map<String,Object> parameter) {
		//查询标准商家
		List<CustomerVo> customerList = new ArrayList<CustomerVo>();
		/*try {
			//versionName
			if(parameter.containsKey("customername")){
				parameter.put("versionName",parameter.get("customername"));
			}
			List<BmsJiuyeQuotationSystemEntity> list = bmsCustomerService.queryCustomerBmsList(parameter);
			for(BmsJiuyeQuotationSystemEntity entity:list)
			{
				CustomerVo vo = new CustomerVo();
				vo.setCustomerid(entity.getVersionCode());
				vo.setCustomername(entity.getVersionName());
				vo.setShortname(entity.getShortname());
				customerList.add(vo);
			}
		} catch (Exception e) {
		
		}*/
		
		if(null==parameter){
			parameter=new HashMap<String,Object>();
		}
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, page.getPageNo(), page.getPageSize());
		
		if (tmpPageInfo != null) {
//			page.setEntities(tmpPageInfo.getList());
//			page.setEntityCount((int) tmpPageInfo.getTotal());
			if(customerList.size()>0)
			{
				List<CustomerVo> c = tmpPageInfo.getList();
				boolean b = c.addAll(customerList);
				page.setEntities(c);
				int total = (int) tmpPageInfo.getTotal();
				if(b){
					total = total+customerList.size();
				}
				page.setEntityCount(total);
			}else{
				page.setEntities(tmpPageInfo.getList());
				page.setEntityCount((int) tmpPageInfo.getTotal());
			}
			
		}
	}
}
