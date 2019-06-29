package com.jiuyescm.bms.biz.diapatch.service;


import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.diapatch.vo.BizDispatchPackageVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBizDispatchPackageService {
	
    PageInfo<BizDispatchPackageVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BizDispatchPackageEntity> query(Map<String, Object> condition);

    BizDispatchPackageEntity save(BizDispatchPackageEntity entity);

    BizDispatchPackageEntity update(BizDispatchPackageEntity entity);

    /**
     * 重算
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午10:52:46
     *
     * @param param
     * @return
     */
    int reCalculate(Map<String, Object> param);

    /**
     * 查询需要导出的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午11:28:41
     *
     * @param condition
     * @param pageNo
     * @param pageSize
     * @param operateTypeMap 
     * @param timeMap 
     * @param boxMap 
     * @param transportMap 
     * @return
     */
    PageInfo<BizDispatchPackageEntity> queryToExport(Map<String, Object> condition, int pageNo, int pageSize, Map<String, String> transportMap, Map<String, String> boxMap, Map<String, String> timeMap, Map<String, String> operateTypeMap);

}
