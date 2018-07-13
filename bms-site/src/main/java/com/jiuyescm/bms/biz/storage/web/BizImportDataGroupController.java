package com.jiuyescm.bms.biz.storage.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportDataEntity;
import com.jiuyescm.bms.biz.storage.entity.BizCustomerImportQueryEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.service.IBizImportDataGroupService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;

/**
 * 应导入数据统计
 * @author Wuliangfeng
 *
 */
@Controller("bizImportDataGroupController")
public class BizImportDataGroupController {

	private static final Logger logger = Logger.getLogger(BizImportDataGroupController.class.getName());
	@Resource
	private IBizImportDataGroupService bizImportDataGroupService;
	@DataProvider
	public void query(Page<BizCustomerImportDataEntity> page,List<BizCustomerImportQueryEntity> condition){
		if(condition!=null&&condition.size()>0){
			BizCustomerImportQueryEntity parameter=condition.get(0);
			PageInfo<BizCustomerImportDataEntity> pageInfo = bizImportDataGroupService.query(parameter, page.getPageNo(), page.getPageSize());
			if (null != pageInfo) {
				InitList(pageInfo.getList());
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int)pageInfo.getTotal());
			}
		}
	}
	private void InitList(List<BizCustomerImportDataEntity> list){
		if(list==null){
			return;
		}
		for(BizCustomerImportDataEntity entity:list){
			if(entity.getOutCount()==0){
				entity.setPackoutstorageStatus("不需导入");
				entity.setPackStorageStatus("不需导入");
				entity.setPalletStatus("不需导入");
			}else{
				if(entity.getPackoutstorageCount()==null){
					entity.setPackoutstorageStatus("未签约");
				}else{
					if(entity.getPackoutstorageCount().intValue()==0){
						entity.setPackoutstorageStatus("未导入");
					}else{
						entity.setPackoutstorageStatus("已导入");
					}
				}
				if(entity.getPackstorageCount()==null){
					entity.setPackStorageStatus("未签约");
				}else{
					if(entity.getPackstorageCount().intValue()==0){
						entity.setPackStorageStatus("未导入");
					}else{
						entity.setPackStorageStatus("已导入");
					}
				}
				if(entity.getPalletCount()==null){
					entity.setPalletStatus("未签约");
				}else{
					if(entity.getPalletCount().intValue()==0){
						entity.setPalletStatus("未导入");
					}else{
						entity.setPalletStatus("已导入");
					}
				}
			}
		}
	}
}
