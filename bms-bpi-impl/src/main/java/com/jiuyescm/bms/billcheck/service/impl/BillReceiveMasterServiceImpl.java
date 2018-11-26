package com.jiuyescm.bms.billcheck.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.exception.BizException;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billReceiveMasterService")
public class BillReceiveMasterServiceImpl implements IBillReceiveMasterService {

	private static final Logger logger = Logger.getLogger(BillReceiveMasterServiceImpl.class.getName());

	@Autowired
    private IBillReceiveMasterRepository billReceiveMasterRepository;
	
	@Resource private IBillCheckInfoRepository billCheckInfoRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillReceiveMasterVo findById(Long id) {
		
		BillReceiveMasterVo vo=new BillReceiveMasterVo();
		BillReceiveMasterEntity entity=billReceiveMasterRepository.findById(id);
		try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		
        return vo;
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillReceiveMasterVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
    	
    	PageInfo<BillReceiveMasterEntity> pageInfo=billReceiveMasterRepository.query(condition, pageNo, pageSize);
		PageInfo<BillReceiveMasterVo> result=new PageInfo<BillReceiveMasterVo>();

		List<BillReceiveMasterVo> voList = new ArrayList<BillReceiveMasterVo>();
    	for(BillReceiveMasterEntity entity : pageInfo.getList()) {
    		BillReceiveMasterVo vo = new BillReceiveMasterVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}",ex);
            }
    		voList.add(vo);
    	}
		
    	result.setList(voList);
		return result;
     }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillReceiveMasterVo> query(Map<String, Object> condition){
		
		List<BillReceiveMasterEntity> list=billReceiveMasterRepository.query(condition);
		List<BillReceiveMasterVo> voList = new ArrayList<BillReceiveMasterVo>();
    	for(BillReceiveMasterEntity entity : list) {
    		BillReceiveMasterVo vo = new BillReceiveMasterVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int  save(BillReceiveMasterVo vo) {
    	
    	BillReceiveMasterEntity entity=new BillReceiveMasterEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            int k = billReceiveMasterRepository.save(entity);
            return k;
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return 0;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillReceiveMasterVo update(BillReceiveMasterVo vo) {   	
    	BillReceiveMasterVo returnVo=new BillReceiveMasterVo();
    	BillReceiveMasterEntity entity=new BillReceiveMasterEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillReceiveMasterEntity returnEntity=billReceiveMasterRepository.update(entity);
            PropertyUtils.copyProperties(returnVo,returnEntity);
            return returnVo;
        } catch (Exception ex) {
            logger.error("转换失败");
        }
		return returnVo;  
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(String billNo,String status) {
    	if("sucess".equals(status)){
    		Map<String, Object> condition=new HashMap<>();
    		condition.put("billNo", billNo);
    		BillCheckInfoEntity entity=billCheckInfoRepository.queryBillCheck(condition);
    		if(entity==null){
    			throw new BizException("BILL_NULL","账单不存在!");
    		}
    		//状态为已确认
    		if("CONFIRMED".equals(entity.getBillCheckStatus())){
    			throw new BizException("CONFIRMED_NULL","已确认状态的账单无法删除!");
    		}
    	}else{
            billReceiveMasterRepository.delete(billNo);
    	}  	
    }
	
}
