package com.jiuyescm.bms.biz.dispatch.repository.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchPackageRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizDispatchPackageRepository")
public class BizDispatchPackageRepositoryImpl extends MyBatisDao implements IBizDispatchPackageRepository {

    private static final Logger logger = LoggerFactory.getLogger(BizDispatchPackageRepositoryImpl.class.getName());
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizDispatchPackageEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizDispatchPackageEntity> list = selectList("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizDispatchPackageEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizDispatchPackageEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchPackageEntity save(BizDispatchPackageEntity entity) {
        insert("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchPackageEntity update(BizDispatchPackageEntity entity) {
        update("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.update", entity);
        return entity;
    }
	
    /**
     * 
     * 重算
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午10:48:34
     *
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public int reCalculate(Map<String, Object> param) {
        try{
            update("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.retryForCalcu", param);
            return 1;
        }
        catch(Exception ex){
            logger.error("数据库操作失败", ex);
            return 0;
        }
    }
    
    /**
     * 汇总需要发送MQ的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午10:55:52
     *
     * @param condition
     * @return
     */
    @Override
    public List<BmsAsynCalcuTaskEntity> queryPackageTask(Map<String, Object> condition) {
        // TODO Auto-generated method stub
        return this.selectList("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.queryPackageTask", condition);
    }
    
    /**
     * 查询需要导出的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 下午1:31:02
     *
     * @param condition
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public PageInfo<BizDispatchPackageEntity> queryToExport(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizDispatchPackageEntity> list=selectList("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.queryToExport", condition,new RowBounds(pageNo,pageSize));
        PageInfo<BizDispatchPackageEntity> p=new PageInfo<>(list);
        return p;
    }

    @Override
    public BizDispatchPackageEntity queryOne(Map<String, Object> condition) {
        // TODO Auto-generated method stub
        return (BizDispatchPackageEntity) selectOne("com.jiuyescm.bms.biz.dispatch.BizDispatchPackageMapper.queryOne", condition);
    }

}
