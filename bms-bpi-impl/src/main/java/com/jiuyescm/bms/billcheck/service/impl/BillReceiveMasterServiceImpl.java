package com.jiuyescm.bms.billcheck.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.BillReceiveExpectEntity;
import com.jiuyescm.bms.billcheck.ReportBillImportMasterEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveExpectVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.billcheck.vo.ReportBillImportMasterVo;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveAirTempRepository;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveDispatchTempRepository;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveStorageTempRepository;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveTransportTempRepository;
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
	@Resource
	private IBillFeesReceiveStorageTempRepository billFeesReceiveStorageTempRepository;
	@Resource
	private IBillFeesReceiveDispatchTempRepository billFeesReceiveDispatchTempRepository;
	@Resource
	private IBillFeesReceiveTransportTempRepository billFeesReceiveTransportTempRepository;
	@Resource
	private IBillFeesReceiveAirTempRepository billFeesReceiveAirTempRepository;

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
        	logger.error("转换失败:",ex);
        }
		
        return vo;
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
	public PageInfo<BillReceiveMasterVo> query(Map<String, Object> condition, int pageNo, int pageSize) {

		PageInfo<BillReceiveMasterEntity> pageInfo = billReceiveMasterRepository.query(condition, pageNo, pageSize);
		PageInfo<BillReceiveMasterVo> result = new PageInfo<BillReceiveMasterVo>();
		try {
			PropertyUtils.copyProperties(result, pageInfo);
			List<BillReceiveMasterVo> voList = new ArrayList<BillReceiveMasterVo>();
			for (BillReceiveMasterEntity entity : pageInfo.getList()) {
				BillReceiveMasterVo vo = new BillReceiveMasterVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
			
			result.setList(voList);
		} catch (Exception ex) {
			logger.error("转换失败:", ex);
		}
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
                logger.error("转换失败:",ex);
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
            logger.error("转换失败:",ex);
        }
		return 0;
    }
    
    

	@Override
	public int insertReportMaster(ReportBillImportMasterVo vo) {

		ReportBillImportMasterEntity entity=new ReportBillImportMasterEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            int k = billReceiveMasterRepository.insertReportMaster(entity);
            return k;
        } catch (Exception ex) {
            logger.error("转换失败:",ex);
        }
		return 0;
	}

    
	@Override
	public int saveExpect(BillReceiveExpectVo vo) {
		// TODO Auto-generated method stub
		BillReceiveExpectEntity entity=new BillReceiveExpectEntity();
		try {
            PropertyUtils.copyProperties(entity, vo);          
            int k = billReceiveMasterRepository.saveExpect(entity);
            return k;
        } catch (Exception ex) {
            logger.error("转换失败:",ex);
        }
		return 0;
	}

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public void update(BillReceiveMasterVo vo) {   	
    	BillReceiveMasterEntity entity=new BillReceiveMasterEntity();
		try {
			PropertyUtils.copyProperties(entity, vo);      
            int k = billReceiveMasterRepository.update(entity);
        } catch (Exception ex) {
            logger.error("转换失败:",ex);
        }
    }

	/**
	 * 删除
	 * @param entity
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
    @Override
    public void delete(String billNo,String status) {
		//删除导入记录
		billReceiveMasterRepository.delete(billNo);
		//删除对应得费用
		billFeesReceiveStorageTempRepository.delete(billNo);
		billFeesReceiveTransportTempRepository.delete(billNo);
		billFeesReceiveDispatchTempRepository.delete(billNo);
		billFeesReceiveAirTempRepository.delete(billNo);
    	/*if("SUCCESS".equals(status)){
    		Map<String, Object> condition=new HashMap<>();
    		condition.put("billNo", billNo);
    		BillCheckInfoEntity entity=billCheckInfoRepository.queryBillCheck(condition);
    		if(entity==null){
    			throw new BizException("BILL_NULL","账单不存在!");
    		}
    		//状态为已确认
    		if("CONFIRMED".equals(entity.getBillCheckStatus())){
    			throw new BizException("CONFIRMED_NULL","已确认状态的账单无法删除!");
    		}else {
    			//删除导入记录
    			billReceiveMasterRepository.delete(billNo);
    			//删除对应得费用
    			billFeesReceiveStorageTempRepository.delete(billNo);
    			billFeesReceiveTransportTempRepository.delete(billNo);
    			billFeesReceiveDispatchTempRepository.delete(billNo);
    			billFeesReceiveAirTempRepository.delete(billNo);
    			//删除账单跟踪记录
    			billCheckInfoRepository.deleteByBillNo(billNo);
			}
    	}else{
    		//删除导入记录
			billReceiveMasterRepository.delete(billNo);
			//删除对应得费用
			billFeesReceiveStorageTempRepository.delete(billNo);
			billFeesReceiveTransportTempRepository.delete(billNo);
			billFeesReceiveDispatchTempRepository.delete(billNo);
			billFeesReceiveAirTempRepository.delete(billNo);
			//删除账单跟踪记录
			billCheckInfoRepository.deleteByBillNo(billNo);
    	} */ 	
    }

	@Override
	public BillReceiveExpectVo queryExpect(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillReceiveExpectVo vo=new BillReceiveExpectVo();
		BillReceiveExpectEntity entity=billReceiveMasterRepository.queryExpect(condition);
		try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
        	logger.error("转换失败:",ex);
        }
		
		return vo;
	}

	@Override
	public BillReceiveMasterVo queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillReceiveMasterVo vo=new BillReceiveMasterVo();
		BillReceiveMasterEntity entity=billReceiveMasterRepository.queryOne(condition);
		try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
        	logger.error("转换失败:",ex);
        }
		return vo;
	}	
	
	@Override
	public Double getAbnormalMoney(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.getAbnormalMoney(billNo);
	}

	@Override
	public Map<String, BigDecimal> queryMaterial(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryMaterial(billNo);
	}

	@Override
	public Map<String, BigDecimal> queryProduct(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryProduct(billNo);
	}

	@Override
	public Double queryStorageRent(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryStorageRent(billNo);
	}

	@Override
	public Double queryTransportAbnormalFee(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryTransportAbnormalFee(billNo);
	}

	@Override
	public Double queryAirAbnormalFee(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryAirAbnormalFee(billNo);
	}

	@Override
	public Double queryStorageAbnormalFee(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryStorageAbnormalFee(billNo);
	}

	@Override
	public Double queryDispatchAbnormalFee(String billNo) {
		// TODO Auto-generated method stub
		return billReceiveMasterRepository.queryDispatchAbnormalFee(billNo);
	}
}
