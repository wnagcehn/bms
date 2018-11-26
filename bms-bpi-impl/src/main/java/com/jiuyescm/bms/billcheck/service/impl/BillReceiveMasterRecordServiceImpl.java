package com.jiuyescm.bms.billcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRecordRepository;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterRecordService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterRecordVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billReceiveMasterRecordService")
public class BillReceiveMasterRecordServiceImpl implements IBillReceiveMasterRecordService {

	private static final Logger logger = Logger.getLogger(BillReceiveMasterRecordServiceImpl.class.getName());

	
	@Autowired
    private IBillReceiveMasterRecordRepository billReceiveMasterRecordRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillReceiveMasterRecordVo findById(Long id) {
		BillReceiveMasterRecordVo vo=new BillReceiveMasterRecordVo();
		BillReceiveMasterRecordEntity entity=billReceiveMasterRecordRepository.findById(id);
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
    public PageInfo<BillReceiveMasterRecordVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
    	
    	PageInfo<BillReceiveMasterRecordEntity> pageInfo=billReceiveMasterRecordRepository.query(condition, pageNo, pageSize);
		PageInfo<BillReceiveMasterRecordVo> result=new PageInfo<BillReceiveMasterRecordVo>();

		List<BillReceiveMasterRecordVo> voList = new ArrayList<BillReceiveMasterRecordVo>();
    	for(BillReceiveMasterRecordEntity entity : pageInfo.getList()) {
    		BillReceiveMasterRecordVo vo = new BillReceiveMasterRecordVo();
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
    public List<BillReceiveMasterRecordVo> query(Map<String, Object> condition){
		
		List<BillReceiveMasterRecordEntity> list=billReceiveMasterRecordRepository.query(condition);
		List<BillReceiveMasterRecordVo> voList = new ArrayList<BillReceiveMasterRecordVo>();
    	for(BillReceiveMasterRecordEntity entity : list) {
    		BillReceiveMasterRecordVo vo = new BillReceiveMasterRecordVo();
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
    public int save(BillReceiveMasterRecordVo vo) {
    	BillReceiveMasterRecordEntity entity=new BillReceiveMasterRecordEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            int k = billReceiveMasterRecordRepository.save(entity);
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
    public BillReceiveMasterRecordVo update(BillReceiveMasterRecordVo vo) {
    	BillReceiveMasterRecordVo returnVo=new BillReceiveMasterRecordVo();
    	BillReceiveMasterRecordEntity entity=new BillReceiveMasterRecordEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            BillReceiveMasterRecordEntity returnEntity=billReceiveMasterRecordRepository.update(entity);
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
    public void delete(Long id) {
        billReceiveMasterRecordRepository.delete(id);
    }
	
}
