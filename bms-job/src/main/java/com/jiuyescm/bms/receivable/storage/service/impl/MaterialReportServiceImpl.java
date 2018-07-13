
package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.receivable.storage.service.IMaterialReportService;
import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author zf
 * 
 */
@SuppressWarnings("rawtypes")
@Service("materialdiffReportService")
public class MaterialReportServiceImpl extends MyBatisDao implements IMaterialReportService {

	
	public MaterialReportServiceImpl() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertBmsMaterailTemp(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.insertBmsMaterailTemp", param);
	}
	
	@Override
	public int deleteBmsMaterailTemp() {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.deleteBmsMaterailTemp","");
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertWmsMaterailTemp(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.insertWmsMaterailTemp",param);
	}

	@Override
	public int deleteWmsMaterailTemp() {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.deleteWmsMaterailTemp","");
	}

	@SuppressWarnings("unchecked")
	@Override
	public int deleteMaterialReport(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.updateMaterialReport", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BmsMaterialReportEntity> queryMaterialReportList(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.queryMaterialReportList", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertMaterialReport(List<BmsMaterialReportEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.insertMaterialReport", list);
	}

	@Override
	public List<BmsMaterialReportEntity> queryBmsMaterialReportTemp(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.queryBmsMaterialReportTempList", param);
	}

	@Override
	public List<BmsMaterialReportEntity> queryWmsMaterialReportTemp(
			Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.receivable.storage.MaterialReportMapper.queryWmsMaterialReportTempList", param);
	}}
