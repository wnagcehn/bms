package com.jiuyescm.bms.lookup.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;
import com.jiuyescm.bms.quotation.system.service.IBmsJiuyeQuotationSystemService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Component("customerLookupPR")
public class CustomerLookupPR {

	@Autowired 
	private ICustomerService customerService;
	
	@Autowired
	private IBmsJiuyeQuotationSystemService bmsCustomerService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@DataProvider
	public void query(Page<CustomerVo> page, Map<String, Object> parameter) {
		//查询标准商家
		List<CustomerVo> customerList = new ArrayList<CustomerVo>();
		try {
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
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
		}
		if(parameter==null){
			parameter=new HashMap<String,Object>();
		}
		parameter.put("userid", JAppContext.currentUserID());
		parameter.put("delflag", "0");
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
//			page.setEntities(tmpPageInfo.getList());
//			page.setEntityCount((int) tmpPageInfo.getTotal());
			List<CustomerVo> c = tmpPageInfo.getList();
			boolean b = c.addAll(customerList);
			page.setEntities(c);
			int total = (int) tmpPageInfo.getTotal();
			if(b){
				total = total+customerList.size();
			}
			page.setEntityCount(total);
		}
	}
}
