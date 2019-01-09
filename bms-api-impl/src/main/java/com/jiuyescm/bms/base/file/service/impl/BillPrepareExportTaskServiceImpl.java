package com.jiuyescm.bms.base.file.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.base.file.repository.IBillPrepareExportTaskRepository;
import com.jiuyescm.bms.base.file.service.IBillPrepareExportTaskService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;

@Service("billPrepareExportTaskService")
public class BillPrepareExportTaskServiceImpl implements IBillPrepareExportTaskService{

	@Resource 
	private SequenceService sequenceService;
	@Autowired
    private IBillPrepareExportTaskRepository billPrepareExportTaskRepository;
	
	@Override
	public PageInfo<BillPrepareExportTaskEntity> queryBillTask(
			Map<String, Object> param, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return billPrepareExportTaskRepository.queryBillTask(param,pageNo,pageSize);

	}

	@Override
	public BillPrepareExportTaskEntity save(BillPrepareExportTaskEntity entity)
			throws Exception {
		// TODO Auto-generated method stub
		if (null != entity) {
    		String taskId = sequenceService.getBillNoOne(BillPrepareExportTaskEntity.class.getName(), "BF", "0000000000");
    		if (StringUtils.isBlank(taskId)) {
    			throw new Exception("生成导出文件编号失败,请稍后重试!");
    		}
    		entity.setTaskId(taskId);
		}
        return billPrepareExportTaskRepository.save(entity);
	}

	@Override
	public int update(BillPrepareExportTaskEntity entity) {
		// TODO Auto-generated method stub
		return billPrepareExportTaskRepository.update(entity);
	}

	@Override
	public boolean checkFileHasDownLoad(Map<String, Object> queryEntity) {
		// TODO Auto-generated method stub
		return billPrepareExportTaskRepository.checkFileHasDownLoad(queryEntity);
	}

	@Override
	public int updateExportTask(String taskId, String taskState, double process) {
		// TODO Auto-generated method stub
		BillPrepareExportTaskEntity entity = new BillPrepareExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		return billPrepareExportTaskRepository.update(entity);
	}

	@Override
	public List<Map<String, String>> getChildCustomer(String mkId) {
		// TODO Auto-generated method stub
		return billPrepareExportTaskRepository.getChildCustomer(mkId);
	}
	

}
