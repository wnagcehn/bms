package com.jiuyescm.bms.billcheck.web;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.billcheck.vo.BillAccountInfoVo;
import com.jiuyescm.cfm.common.JAppContext;

@Component
@Controller("billCheckPR")
public class BillAccountInfoController {

	@Resource private IBmsAccountInfoService billAccountInfoService;

	@Expose
	public BillAccountInfoVo findByCustomerId(String customerId) throws Exception {
		BillAccountInfoVo entity = null;
		entity = billAccountInfoService.findByCustomerId(customerId);
		return entity;
	}
	@DataProvider
	public void queryAll(Page<BillAccountInfoVo> page,Map<String,Object> parameter){
		
		PageInfo<BillAccountInfoVo> tmpPageInfo = billAccountInfoService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
		
	}
	
	@DataResolver
	public void save(BillAccountInfoVo entity) {
			 if (null != entity) {
			      entity.setCreatorId(JAppContext.currentUserID());
			      entity.setCreateTime(JAppContext.currentTimestamp());
			      entity.setCreator(JAppContext.currentUserName());
			      entity.setDelFlag("0");
			      billAccountInfoService.save(entity);
			 }
	}
	
	@DataResolver
	public BillAccountInfoVo update(BillAccountInfoVo entity){
		BillAccountInfoVo res = null;
		 if (null != entity && null != entity.getAmount()) {
			 res =  billAccountInfoService.update(entity);
		 }		
		 return res;
		 
}


}
