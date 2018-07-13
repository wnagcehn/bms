package com.jiuyescm.bms.subject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.repository.IBmsSubjectInfoRepository;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;

@Service("bmsSubjectService")
public class BmsSubjectInfoServiceImpl implements IBmsSubjectInfoService{

	private static final Logger logger = Logger.getLogger(BmsSubjectInfoServiceImpl.class.getName());

	@Resource
	private IBmsSubjectInfoRepository bmsSubjectInfoRepository;
	
	@Override
	public List<BmsSubjectInfoVo> querySubject(BmsSubjectInfoVo queryVo) {
		// TODO Auto-generated method stub
		List<BmsSubjectInfoVo> voList=new ArrayList<BmsSubjectInfoVo>();
		try{
			BmsSubjectInfoEntity queryEntity=new BmsSubjectInfoEntity();
		    PropertyUtils.copyProperties(queryEntity, queryVo);
			
			List<BmsSubjectInfoEntity> list=bmsSubjectInfoRepository.querySubject(queryEntity);
			for(BmsSubjectInfoEntity entity : list) {
				BmsSubjectInfoVo vo = new BmsSubjectInfoVo();
	            PropertyUtils.copyProperties(vo, entity);
	    		voList.add(vo);
	    	}
		}catch(Exception e){
			logger.error("query error:",e);
		}
		return voList;
	}

}
