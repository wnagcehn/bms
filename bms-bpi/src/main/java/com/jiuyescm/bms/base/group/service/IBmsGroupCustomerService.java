package com.jiuyescm.bms.base.group.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;

public interface IBmsGroupCustomerService {

	List<BmsGroupCustomerVo> queryAllByGroupId(int groupId) throws Exception;
	int addBatch(List<BmsGroupCustomerVo> list) throws Exception;
	int delGroupCustomer(BmsGroupCustomerVo subjectVo) throws Exception;
	int updateGroupCustomer(BmsGroupCustomerVo subjectVo) throws Exception;
	PageInfo<BmsGroupCustomerVo> queryGroupCustomer(
			BmsGroupCustomerVo queryCondition, int pageNo, int pageSize) throws Exception;
	List<String> checkCustomerCodeExist(int groupId, List<String> subjectCodeList);
	int queryCustomerCountByGroupId(int id);
	List<String> queryCustomerByGroupId(int groupId);
	List<String> queryCustomerByGroupCode(String groupCode);
	List<String> queryCustomerByGroupCodeAndCustomerId(String groupCode,String bizType,String customerId);
	//作废商家业务数据
	int cancalCustomerBiz(List<BmsGroupCustomerVo> customerVoList);
	//恢复商家业务数据
	int restoreCustomerBiz(String customerId);
	
	
	
}
