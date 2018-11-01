package com.jiuyescm.bms.report.month.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCalcuStatusEntity;
import com.jiuyescm.bms.report.biz.repository.IReportCalcuStatusRepository;
import com.jiuyescm.bms.report.month.service.IReportCalcuStatusService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("reportCalcuStatusService")
public class ReportCalcuStatusServiceImpl implements IReportCalcuStatusService {

	@Autowired
    private IReportCalcuStatusRepository reportCalcuStatusRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public ReportCalcuStatusEntity findById(Long id) {
        return reportCalcuStatusRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<ReportCalcuStatusEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return reportCalcuStatusRepository.query(condition, pageNo, pageSize);
    }
    
	/**
	 * 新分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<ReportCalcuStatusEntity> queryAll(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return reportCalcuStatusRepository.queryAll(condition, pageNo, pageSize);
    }
    
    /**
	 * 明细查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<ReportCalcuStatusEntity> queryDetail(Map<String, Object> condition){
		return reportCalcuStatusRepository.queryDetail(condition);
	}
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<ReportCalcuStatusEntity> query(Map<String, Object> condition){
		return reportCalcuStatusRepository.query(condition);
	}
	
    /**
	 * 查询(新)
	 * @param page
	 * @param param
	 */
	@Override
   public List<ReportCalcuStatusEntity> queryAll(Map<String, Object> condition){
		return reportCalcuStatusRepository.queryAll(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public ReportCalcuStatusEntity save(ReportCalcuStatusEntity entity) {
        return reportCalcuStatusRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public ReportCalcuStatusEntity update(ReportCalcuStatusEntity entity) {
        return reportCalcuStatusRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        reportCalcuStatusRepository.delete(id);
    }
	
}
