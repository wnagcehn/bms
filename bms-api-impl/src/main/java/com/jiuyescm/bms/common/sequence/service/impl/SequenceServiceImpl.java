package com.jiuyescm.bms.common.sequence.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.common.sequence.repository.SequenceDao;
import com.jiuyescm.bms.common.sequence.service.SequenceService;

/**
 * 流水号获取service实现类
 * @author zhengyishan
 *
 */
@Service("sequenceService")
public class SequenceServiceImpl implements SequenceService {
	
	@Resource
	private SequenceDao sequenceDao;

	@Override
	public String getBillNoOne(String idName, String startName, String formatStr) {
		// TODO Auto-generated method stub
		return sequenceDao.getBillNoOne(idName, startName, formatStr);
	}

	@Override
	public List<String> getBillNoList(String idName, String startName, int size, String formatStr) {
		// TODO Auto-generated method stub
		return sequenceDao.getBillNoList(idName, startName, size, formatStr);
	}

}
