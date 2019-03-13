package com.jiuyescm.bms.base.monthFeeCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.monthFeeCount.repository.IPubMonthFeeCountRepository;
import com.jiuyescm.bms.base.monthFeeCount.service.IPubMonthFeeCountService;
import com.jiuyescm.bms.base.monthFeeCount.vo.PubMonthFeeCountVo;

@Service("pubMonthFeeCountService")
public class PubMonthFeeCountServiceImpl implements IPubMonthFeeCountService{

	private static Logger logger = LoggerFactory.getLogger(PubMonthFeeCountServiceImpl.class);

	@Autowired
    private IPubMonthFeeCountRepository pubMonthFeeCountRepository;
	
	@Override
	public PageInfo<PubMonthFeeCountVo> queryAll(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<PubMonthFeeCountEntity> entityPageInfo = pubMonthFeeCountRepository.queryAll(condition, pageNo, pageSize);
    	PageInfo<PubMonthFeeCountVo> voPageInfo = new PageInfo<PubMonthFeeCountVo>();
    	try {
			if(entityPageInfo!=null&&entityPageInfo.getList().size()>0){
				List<PubMonthFeeCountVo> list=new ArrayList<PubMonthFeeCountVo>();
				for(PubMonthFeeCountEntity entity:entityPageInfo.getList()){
					PubMonthFeeCountVo vo=new PubMonthFeeCountVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				voPageInfo.setList(list);
			}
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
		}
        return voPageInfo;
	}

	@Override
	public List<PubMonthFeeCountVo> query(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<PubMonthFeeCountVo> voList= new ArrayList<PubMonthFeeCountVo>();
		try{
			List<PubMonthFeeCountEntity> entityList=pubMonthFeeCountRepository.query(condition);
			for(PubMonthFeeCountEntity entity:entityList){
				PubMonthFeeCountVo vo=new PubMonthFeeCountVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("转换失败:{}",e);
		}
		return voList;
	}

}
