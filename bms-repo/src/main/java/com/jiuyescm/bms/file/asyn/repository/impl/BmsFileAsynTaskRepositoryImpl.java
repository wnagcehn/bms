package com.jiuyescm.bms.file.asyn.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsFileAsynTaskRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * bms文件异步导入导出 dao层实现
 * @author cjw
 * 
 */
@Repository("bmsFileAsynTaskRepoImpl")
public class BmsFileAsynTaskRepositoryImpl extends MyBatisDao<BmsFileAsynTaskEntity> implements IBmsFileAsynTaskRepository {

	private static final Logger logger = Logger.getLogger(BmsFileAsynTaskRepositoryImpl.class.getName());

	public BmsFileAsynTaskRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BmsFileAsynTaskEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsFileAsynTaskEntity> list = selectList("com.jiuyescm.bms.file.asyn.BmsFileAsynTaskMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsFileAsynTaskEntity> pageInfo = new PageInfo<BmsFileAsynTaskEntity>(list);
        return pageInfo;
    }

    @Override
    public BmsFileAsynTaskEntity findByTaskId(String taskId) {
        BmsFileAsynTaskEntity entity = selectOne("com.jiuyescm.bms.file.asyn.BmsFileAsynTaskMapper.findByTaskId", taskId);
        return entity;
    }

    @Override
    public int save(BmsFileAsynTaskEntity entity) {
        return insert("com.jiuyescm.bms.file.asyn.BmsFileAsynTaskMapper.save", entity);
    }

    @Override
    public int update(BmsFileAsynTaskEntity entity) {
        return update("com.jiuyescm.bms.file.asyn.BmsFileAsynTaskMapper.update", entity);
    }
    
    @Override
    public BmsFileAsynTaskEntity queryMinTask(Map<String, Object> param) {
        List<BmsFileAsynTaskEntity> list = selectList("com.jiuyescm.bms.file.asyn.BmsFileAsynTaskMapper.queryMinTask", param);
        return list.size()>0?list.get(0):null;
    }
	
}
