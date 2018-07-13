package com.jiuyescm.bms.receivable.storage.service;

import java.util.Map;

public interface IBizContractService {
	
	Map<String, Object> initContract(Map<String, Object> params);
	
	Map<String, Object> initQuotation(Map<String, String> params);
	
}
