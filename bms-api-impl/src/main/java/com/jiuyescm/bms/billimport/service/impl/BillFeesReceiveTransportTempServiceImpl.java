package com.jiuyescm.bms.billimport.service.impl;

import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveTransportTempRepository;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveTransportTempService;

/**
 * ..ServiceImpl
 * @author liuzhicheng
 * 
 */
@Service("billFeesReceiveTransportTempService")
public class BillFeesReceiveTransportTempServiceImpl implements IBillFeesReceiveTransportTempService {

	private static final Logger logger = LoggerFactory.getLogger(BillFeesReceiveTransportTempServiceImpl.class);
	
	@Autowired
    private IBillFeesReceiveTransportTempRepository billFeesReceiveTransportTempRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillFeesReceiveTransportTempEntity findById(Long id) {
        return billFeesReceiveTransportTempRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billFeesReceiveTransportTempRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition){
		return billFeesReceiveTransportTempRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveTransportTempEntity save(BillFeesReceiveTransportTempEntity entity) {
        return billFeesReceiveTransportTempRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveTransportTempEntity update(BillFeesReceiveTransportTempEntity entity) {
        return billFeesReceiveTransportTempRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        billFeesReceiveTransportTempRepository.delete(id);
    }
	
    /**
     * 批量写入
     * @param list
     * @return
     */
	@Override
	public int insertBatchTemp(List<BillFeesReceiveTransportTempEntity> list) {
		int i = 0;
		try {
			if(list.size()>0){
				billFeesReceiveTransportTempRepository.insertBatch(list);
				i = 1;	
			}
		} catch (Exception e) {
			//写入日志
			logger.info("insertBatchTemp：e-{}",e);;
		}	
		return i;
	}
	
    /**
     * 批量删除
     * @param list
     * @return
     */
	@Override
	public int deleteBatchTemp(String billNo) {
		int result = 0;
		try {
			billFeesReceiveTransportTempRepository.deleteBatchTemp(billNo);
			result = 1;	
		} catch (Exception e) {
			//写入日志
			logger.info("insertBatchTemp：e-{}",e);;
		}
		return result;
	}

	@Override
	public int saveDataFromTemp(String billNo) {
		// TODO Auto-generated method stub
		return billFeesReceiveTransportTempRepository.saveDataFromTemp(billNo);
	}
    
}
