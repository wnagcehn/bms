package com.jiuyescm.bms.biz.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiuyescm.bms.biz.storage.entity.StoInstockEntity;
import com.jiuyescm.bms.biz.storage.repository.IStoInstockRepository;
import com.jiuyescm.bms.biz.storage.service.IStoInstockService;
import com.jiuyescm.bms.biz.storage.vo.StoInstockVo;

@Service("stoInstockService")
public class StoInstockServiceImpl implements IStoInstockService {

	private Logger logger = LoggerFactory.getLogger(StoInstockServiceImpl.class);
	
	@Autowired IStoInstockRepository stoInstockRepositoryImpl;
	
	@Override
	public List<StoInstockVo> queryUnExeBiz(Map<String, Object> param) {
		List<StoInstockEntity> list=stoInstockRepositoryImpl.queryALL(param);
		List<StoInstockVo> voList = new ArrayList<StoInstockVo>();
    	for(StoInstockEntity entity : list) {
    		StoInstockVo retVo = new StoInstockVo();
    		try {
                PropertyUtils.copyProperties(retVo, entity);
            } catch (Exception ex) {
               logger.error("转换失败");
            }
    		voList.add(retVo);
    	}
		return voList;
	}

	@Override
	public void updateFee(List<StoInstockVo> vos) {
		
		if(vos == null || vos.size() == 0){
			return; 
		}
		List<StoInstockEntity> list = new ArrayList<StoInstockEntity>();
		for(StoInstockVo vo : vos) {
			StoInstockEntity paramEntity = new StoInstockEntity();
    		try {
                PropertyUtils.copyProperties(paramEntity, vo);
            } catch (Exception ex) {
               logger.error("转换失败");
            }
    		list.add(paramEntity);
    	}
		stoInstockRepositoryImpl.updateFees(list);
		
	}

	

}
