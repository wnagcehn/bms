package com.jiuyescm.bms.trial.storage.service;


public interface IStorageDroolsService {
	
	void excute(Object data,Object price,String rule);
	
	void excute(Object data,Object price,Object rule);
	
}
