package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;


/**
 * 
 * @author cjw
 * 
 */
public interface IBizOutstockPackmaterialService {

	public List<BizOutstockPackmaterialEntity> query(Map<String, Object> condition);

    public void update(BizOutstockPackmaterialEntity entity);

    public void updateBatch(List<BizOutstockPackmaterialEntity> list);

	public List<BizOutstockPackmaterialEntity> queryCost(Map<String, Object> map);
	
	//作废同时存在导入和系统中的导入耗材
    public void updateImportMaterial(List<String> feeNos);
    
    //作废同时存在导入和系统中的导入耗材
    public List<String> queryImportMaterial(Map<String,Object> condition);

}
