package com.jiuyescm.bms.discount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.discount.repository.IBmsDiscountRepository;
import com.jiuyescm.bms.discount.service.IBmsDiscountService;
import com.jiuyescm.bms.discount.vo.BmsDiscountAccountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveDispatchDiscountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveStorageDiscountVo;

@Service("bmsDiscountService")
public class BmsDiscountServiceImpl implements IBmsDiscountService{

	private static final Logger logger = Logger.getLogger(BmsDiscountServiceImpl.class.getName());

	
	@Resource
	private IBmsDiscountRepository bmsDiscountRepository;
		
	@Override
	public BmsDiscountAccountVo queryAccount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsDiscountAccountEntity entity=bmsDiscountRepository.queryAccount(condition);
		try {
			BmsDiscountAccountVo vo=new BmsDiscountAccountVo();
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return null;
	}

	@Override
	public BmsDiscountAccountVo queryStorageAccount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsDiscountAccountEntity entity=bmsDiscountRepository.queryStorageAccount(condition);
		try {
			BmsDiscountAccountVo vo=new BmsDiscountAccountVo();
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return null;
	}
	
	@Override
	public int updateFeeDiscountTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsDiscountRepository.updateFeeDiscountTask(condition);
	}

	@Override
	public PageInfo<FeesReceiveDispatchDiscountVo> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<FeesReceiveDispatchDiscountVo> result=new PageInfo<FeesReceiveDispatchDiscountVo>();

		try {
			PageInfo<FeesReceiveDispatchDiscountEntity> pageInfo=bmsDiscountRepository.queryAll(condition, pageNo, pageSize);			
			List<FeesReceiveDispatchDiscountVo> voList = new ArrayList<FeesReceiveDispatchDiscountVo>();
	    	for(FeesReceiveDispatchDiscountEntity entity : pageInfo.getList()) {
	    		FeesReceiveDispatchDiscountVo vo = new FeesReceiveDispatchDiscountVo();    		
	            PropertyUtils.copyProperties(vo, entity);          
	            voList.add(vo);
	    	}
	    	
	    	PropertyUtils.copyProperties(result, pageInfo); 
	    	result.setList(voList);
			return result;
		} catch (Exception ex) {
         	logger.error("转换失败:{0}",ex);
        }
    	
    	return result;
	}

	
	@Override
	public PageInfo<FeesReceiveStorageDiscountVo> queryStorageAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<FeesReceiveStorageDiscountVo> result=new PageInfo<FeesReceiveStorageDiscountVo>();

		try {
			PageInfo<FeesReceiveStorageDiscountEntity> pageInfo=bmsDiscountRepository.queryStorageAll(condition, pageNo, pageSize);			
			List<FeesReceiveStorageDiscountVo> voList = new ArrayList<FeesReceiveStorageDiscountVo>();
	    	for(FeesReceiveStorageDiscountEntity entity : pageInfo.getList()) {
	    		FeesReceiveStorageDiscountVo vo = new FeesReceiveStorageDiscountVo();    		
	            PropertyUtils.copyProperties(vo, entity);          
	            voList.add(vo);
	    	}
	    	
	    	PropertyUtils.copyProperties(result, pageInfo); 
	    	result.setList(voList);
			return result;
		} catch (Exception ex) {
         	logger.error("转换失败:{0}",ex);
        }
    	
    	return result;
	}

	@Override
	public int updateList(List<FeesReceiveDispatchDiscountVo> list) {
		// TODO Auto-generated method stub
		List<FeesReceiveDispatchDiscountEntity> enList=new ArrayList<FeesReceiveDispatchDiscountEntity>();
		for(FeesReceiveDispatchDiscountVo vo : list) {
			FeesReceiveDispatchDiscountEntity entity= new FeesReceiveDispatchDiscountEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(entity);
    	}
		
		return bmsDiscountRepository.updateList(enList);
	}

	@Override
	public int updateStorageList(List<FeesReceiveStorageDiscountVo> list) {
		// TODO Auto-generated method stub
		List<FeesReceiveStorageDiscountEntity> enList=new ArrayList<FeesReceiveStorageDiscountEntity>();
		for(FeesReceiveStorageDiscountVo vo : list) {
			FeesReceiveStorageDiscountEntity entity= new FeesReceiveStorageDiscountEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
            	logger.error("转换失败:{0}",ex);
            }
    		enList.add(entity);
    	}
		return bmsDiscountRepository.updateStorageList(enList);
	}
	
	@Override
	public int insertFeeStorageDiscount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsDiscountRepository.insertFeeStorageDiscount(condition);
	}

	@Override
	public int deleteFeeStorageDiscount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsDiscountRepository.deleteFeeStorageDiscount(condition);
	}
}
