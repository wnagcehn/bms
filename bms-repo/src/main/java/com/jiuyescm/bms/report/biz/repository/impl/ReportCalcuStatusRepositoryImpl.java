package com.jiuyescm.bms.report.biz.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.report.biz.entity.ReportCalcuStatusEntity;
import com.jiuyescm.bms.report.biz.repository.IReportCalcuStatusRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("reportCalcuStatusRepository")
public class ReportCalcuStatusRepositoryImpl extends MyBatisDao<ReportCalcuStatusEntity> implements IReportCalcuStatusRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public ReportCalcuStatusEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.report.biz.ReportCalcuStatusMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<ReportCalcuStatusEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<ReportCalcuStatusEntity> list = selectList("com.jiuyescm.bms.report.biz.ReportCalcuStatusMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<ReportCalcuStatusEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<ReportCalcuStatusEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.report.biz.ReportCalcuStatusMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public ReportCalcuStatusEntity save(ReportCalcuStatusEntity entity) {
        insert("com.jiuyescm.bms.report.biz.ReportCalcuStatusMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public ReportCalcuStatusEntity update(ReportCalcuStatusEntity entity) {
        update("com.jiuyescm.bms.report.biz.ReportCalcuStatusMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.report.biz.ReportCalcuStatusMapper.delete", id);
    }
	
}
