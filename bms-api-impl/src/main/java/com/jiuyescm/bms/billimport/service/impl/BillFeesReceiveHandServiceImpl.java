package com.jiuyescm.bms.billimport.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveAirTempRepository;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveDispatchTempRepository;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveStorageTempRepository;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveTransportTempRepository;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveHandService;

/**
 * ..ServiceImpl
 * @author liuzhicheng
 * 
 */
@Service("billFeesReceiveHandService")
public class BillFeesReceiveHandServiceImpl implements IBillFeesReceiveHandService {
	
	private static final Logger logger = LoggerFactory.getLogger(BillFeesReceiveHandServiceImpl.class);

	@Autowired
	private IBillFeesReceiveDispatchTempRepository billFeesReceiveDispatchTempRepository;
	
	@Autowired
	private IBillFeesReceiveStorageTempRepository billFeesReceiveStorageTempRepository;
	
	@Autowired
	private IBillFeesReceiveTransportTempRepository billFeesReceiveTransportTempRepository;
	
	@Autowired
    private IBillFeesReceiveAirTempRepository billFeesReceiveAirTempRepository;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public int saveDataFromTemp(String billNo) {
		// TODO Auto-generated method stub
		//仓储
		try {
			int storageNum=billFeesReceiveStorageTempRepository.saveDataFromTemp(billNo);
			if(storageNum>0){
				logger.info("仓储从临时表写入正式表成功");
			}
		}catch (Exception e) {
			//写入日志
			logger.error("仓储从临时表写入正式表异常", e);
		}	

		//配送
		try {
			int dispatchNum=billFeesReceiveDispatchTempRepository.saveDataFromTemp(billNo);
			if(dispatchNum>0){
				logger.info("配送从临时表写入正式表成功");
			}
		}catch (Exception e) {
			//写入日志
			logger.error("配送从临时表写入正式表异常", e);
		}
		
		//干线
		try {
			int transportNum=billFeesReceiveTransportTempRepository.saveDataFromTemp(billNo);
			if(transportNum>0){
				logger.info("干线从临时表写入正式表成功");
			}
		}catch (Exception e) {
			//写入日志
			logger.error("干线从临时表写入正式表异常", e);
		}
	
		//航空
		try {
			int airNum=billFeesReceiveAirTempRepository.saveDataFromTemp(billNo);
			if(airNum>0){
				logger.info("干线从临时表写入正式表成功");
			}
		}catch (Exception e) {
			//写入日志
			logger.error("干线从临时表写入正式表异常", e);
		}
		
		return 1;
	}


	
}
