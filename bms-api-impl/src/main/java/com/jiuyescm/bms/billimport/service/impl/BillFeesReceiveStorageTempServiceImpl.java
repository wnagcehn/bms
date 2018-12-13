package com.jiuyescm.bms.billimport.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveStorageTempRepository;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveStorageTempService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billFeesReceiveStorageTempService")
public class BillFeesReceiveStorageTempServiceImpl implements IBillFeesReceiveStorageTempService {
	
	private static final Logger logger = Logger.getLogger(BillFeesReceiveStorageTempServiceImpl.class.getName());
	
	@Autowired
    private IBillFeesReceiveStorageTempRepository billFeesReceiveStorageTempRepository;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillFeesReceiveStorageTempEntity findById(Long id) {
        return billFeesReceiveStorageTempRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billFeesReceiveStorageTempRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition){
		return billFeesReceiveStorageTempRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveStorageTempEntity save(BillFeesReceiveStorageTempEntity entity) {
        return billFeesReceiveStorageTempRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveStorageTempEntity update(BillFeesReceiveStorageTempEntity entity) {
        return billFeesReceiveStorageTempRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        billFeesReceiveStorageTempRepository.delete(id);
    }
    
    /**
     * 批量写入
     * @param list
     * @return
     */
	@Override
	public int insertBatchTemp(List<BillFeesReceiveStorageTempEntity> list) {		
		int i = 0;
		try {
			billFeesReceiveStorageTempRepository.insertBatch(list);
			i = 1;
		} catch (Exception e) {
			//写入日志
			logger.error("批量写入异常", e);
		}	
		return i;
	}
	
	@Override
	public int deleteBatchTemp(String billNo){
		int result = 0;
		try {
			billFeesReceiveStorageTempRepository.deleteBatch(billNo);
			result = 1;
		} catch (Exception e) {
			//写入日志
			logger.error("批量删除异常", e);
		}
		return result;
	}

	@Override
	public int saveDataFromTemp(String billNo) {
		// TODO Auto-generated method stub
		return billFeesReceiveStorageTempRepository.saveDataFromTemp(billNo);
	}

	@Override
	public Double getImportTotalAmount(String billNo) {
		// TODO Auto-generated method stub
		try {
			return billFeesReceiveStorageTempRepository.getImportTotalAmount(billNo);

		} catch (Exception e) {
			//写入日志
			logger.error("获取导入总金额异常", e);
		}
		return 0d;
	}

	@Override
	public Double getImportStorageAmount(String billNo) {
		// TODO Auto-generated method stub
		return billFeesReceiveStorageTempRepository.getImportStorageAmount(billNo);
	}
	
}
