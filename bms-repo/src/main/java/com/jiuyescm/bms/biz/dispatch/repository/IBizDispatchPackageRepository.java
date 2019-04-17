package com.jiuyescm.bms.biz.dispatch.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBizDispatchPackageRepository {

	PageInfo<BizDispatchPackageEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizDispatchPackageEntity> query(Map<String, Object> condition);

    BizDispatchPackageEntity save(BizDispatchPackageEntity entity);

    BizDispatchPackageEntity update(BizDispatchPackageEntity entity);

    /**
     * 重算
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午10:50:07
     *
     * @param param
     * @return
     */
    int reCalculate(Map<String, Object> param);

    /**
     * 汇总需要发送MQ的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午10:56:26
     *
     * @param condition
     * @return
     */
    List<BmsAsynCalcuTaskEntity> queryPackageTask(Map<String, Object> condition);

    /**
     * 查询导出的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午11:27:50
     *
     * @param condition
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<BizDispatchPackageEntity> queryToExport(Map<String, Object> condition, int pageNo, int pageSize);

}
