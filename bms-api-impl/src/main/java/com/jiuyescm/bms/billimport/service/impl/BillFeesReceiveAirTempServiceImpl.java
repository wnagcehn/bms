package com.jiuyescm.bms.billimport.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveAirTempRepository;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveAirTempService;

/**
 * ..ServiceImpl
 * @author liuzhicheng
 * 
 */
@Service("billFeesReceiveAirTempService")
public class BillFeesReceiveAirTempServiceImpl implements IBillFeesReceiveAirTempService {
	
	private static final Logger logger = LoggerFactory.getLogger(BillFeesReceiveTransportTempServiceImpl.class);

	@Autowired
    private IBillFeesReceiveAirTempRepository billFeesReceiveAirTempRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillFeesReceiveAirTempEntity findById(Long id) {
        return billFeesReceiveAirTempRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billFeesReceiveAirTempRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition){
		return billFeesReceiveAirTempRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveAirTempEntity save(BillFeesReceiveAirTempEntity entity) {
        return billFeesReceiveAirTempRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveAirTempEntity update(BillFeesReceiveAirTempEntity entity) {
        return billFeesReceiveAirTempRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        billFeesReceiveAirTempRepository.delete(id);
    }
	
    /**
     * 批量写入
     * @param list
     * @return
     */
	@Override
	public int insertBatchTemp(List<BillFeesReceiveAirTempEntity> list) {
		int i = 0;
		try {
			if(list.size()>0){
				billFeesReceiveAirTempRepository.insertBatch(list);
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
				billFeesReceiveAirTempRepository.deleteBatch(billNo);
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
		return billFeesReceiveAirTempRepository.saveDataFromTemp(billNo);
	}

	@Override
	public Double getImportAirAmount(String billNo) {
		// TODO Auto-generated method stub
		return billFeesReceiveAirTempRepository.getImportAirAmount(billNo);
	}
	
}
