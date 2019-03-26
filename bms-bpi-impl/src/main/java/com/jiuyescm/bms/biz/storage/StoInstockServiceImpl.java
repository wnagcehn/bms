package com.jiuyescm.bms.biz.storage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.service.IStoInstockService;
import com.jiuyescm.bms.biz.storage.vo.StoInstockVo;

@Service("stoInstockService")
public class StoInstockServiceImpl implements IStoInstockService {

	@Override
	public List<StoInstockVo> queryUnExeBiz(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFee(List<StoInstockVo> vos) {
		// TODO Auto-generated method stub
		
	}

}
