package com.jiuyescm.bms.base.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.repository.IBmsGroupCustomerRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchPackageRepository;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockMasterRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizProductStorageRepository;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockInfoRepository;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

@Service("bmsGroupCustomerService")
public class BmsGroupCustomerServiceImpl implements IBmsGroupCustomerService {
	
	private static final Logger logger = Logger.getLogger(BmsGroupCustomerServiceImpl.class.getName());

	@Autowired
	private IBmsGroupCustomerRepository bmsGroupCustomerRepository;
	@Autowired
	private IBizAddFeeRepository bizAddFeeRepositoryImpl;//增值
	@Autowired
	private IBizDispatchBillRepository bizDispatchBillRepositoryImp;//配送
	@Autowired
	private IBmsBizInstockInfoRepository bizInstockInfoRepositoryImpl;//入库
	@Autowired
	private IBizOutstockMasterRepository bizOutstockMasterRepositoryImpl;//出库
	@Autowired
	private IBizProductStorageRepository bizProductStorageRepositoryImpl;//商品存储费按件	
	@Autowired
	private IBizPalletInfoRepository bizPalletInfoRepositoryImpl;//托数
	@Autowired
	private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepositoryImpl;//耗材出库明细
	@Autowired
	private IBizDispatchPackageRepository bizDispatchPackageRepositoryImpl;//标准包装方案
	
	@Override
	public List<BmsGroupCustomerVo> queryAllByGroupId(int groupId) throws Exception {
		List<BmsGroupCustomerVo> voList=null;
		try{
			List<BmsGroupCustomerEntity> entityList=bmsGroupCustomerRepository.queryAllByGroupId(groupId);
			voList=new ArrayList<BmsGroupCustomerVo>();
			for(BmsGroupCustomerEntity entity:entityList){
				BmsGroupCustomerVo vo=new BmsGroupCustomerVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("queryAllByGroupId:",e);
			throw e;
		}
		return voList;
	}
	

	@Override
	public int addBatch(List<BmsGroupCustomerVo> list) throws Exception {
		try{
			List<BmsGroupCustomerEntity> entityList=new ArrayList<BmsGroupCustomerEntity>();
			for(BmsGroupCustomerVo vo:list){
				BmsGroupCustomerEntity entity=new BmsGroupCustomerEntity();
				PropertyUtils.copyProperties(entity,vo);
				entityList.add(entity);
			}
			return bmsGroupCustomerRepository.addBatch(entityList);
		}catch(Exception e){
			logger.error("addBatch:",e);
			throw e;
		}
	}

	@Override
	public int delGroupCustomer(BmsGroupCustomerVo customerVo) throws Exception {
		try{
			BmsGroupCustomerEntity customerEntity=new BmsGroupCustomerEntity();
			PropertyUtils.copyProperties(customerEntity,customerVo);
			int k=bmsGroupCustomerRepository.delGroupCustomer(customerEntity);
			return k;
		}catch(Exception e){
			logger.error("delGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public int updateGroupCustomer(BmsGroupCustomerVo subjectVo) throws Exception {
		try{
			BmsGroupCustomerEntity entity=new BmsGroupCustomerEntity();
			PropertyUtils.copyProperties(entity, subjectVo);
			return bmsGroupCustomerRepository.updateGroupCustomer(entity);
		}catch(Exception e){
			logger.error("updateGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public PageInfo<BmsGroupCustomerVo> queryGroupCustomer(
			BmsGroupCustomerVo queryCondition, int pageNo, int pageSize) throws Exception {
		PageInfo<BmsGroupCustomerVo> pageVoInfo=null;
		try{
			if(queryCondition==null){
				return pageVoInfo;
			}
			pageVoInfo=new PageInfo<BmsGroupCustomerVo>();
			BmsGroupCustomerEntity queryEntity=new BmsGroupCustomerEntity();
			PropertyUtils.copyProperties(queryEntity, queryCondition);
			PageInfo<BmsGroupCustomerEntity> pageInfo=bmsGroupCustomerRepository.queryGroupCustomer(queryEntity, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsGroupCustomerVo> list=new ArrayList<BmsGroupCustomerVo>();
				for(BmsGroupCustomerEntity entity:pageInfo.getList()){
					BmsGroupCustomerVo voEntity=new BmsGroupCustomerVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
		    logger.error("异常",e);
			throw e;
		}
		return pageVoInfo;
	}

	@Override
	public List<String> checkCustomerCodeExist(int groupId,
			List<String> customerIdList) {
		return bmsGroupCustomerRepository.checkCustomerCodeExist(groupId,customerIdList);
	}

	@Override
	public int queryCustomerCountByGroupId(int groupId) {
		return bmsGroupCustomerRepository.queryCustomerCountByGroupId(groupId);
	}

	@Override
	public List<String> queryCustomerByGroupId(int groupId) {
		// TODO Auto-generated method stub
		return bmsGroupCustomerRepository.queryCustomerByGroupId(groupId);
	}


    @Override
    public List<String> queryCustomerByGroupCode(String groupCode) {
        // TODO Auto-generated method stub
        return bmsGroupCustomerRepository.queryCustomerByGroupCode(groupCode);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int cancalCustomerBiz(List<BmsGroupCustomerVo> customerVoList) {
        // TODO Auto-generated method stub
        List<String> customerList=new ArrayList<>();
        for(BmsGroupCustomerVo vo:customerVoList){
            customerList.add(vo.getCustomerid());
        }
                      
        Map<String,Object> map=new HashMap<>();
        map.put("customerList", customerList);
       
        getDate(map);
        
        if(map.get("beginTime")==null || map.get("endTime")==null){
            return 0;
        }
        
        try {           
            //作废业务数据和费用  业务数据状态改为4，费用状态改为1
            //增值
            bizAddFeeRepositoryImpl.cancalCustomerBiz(map);
            //配送
            bizDispatchBillRepositoryImp.cancalCustomerBiz(map);
            //耗材
            bizOutstockPackmaterialRepositoryImpl.cancalCustomerBiz(map);
            //托数
            bizPalletInfoRepositoryImpl.cancalCustomerBiz(map);
            //按件
            bizProductStorageRepositoryImpl.cancalCustomerBiz(map);
            //出库
            bizOutstockMasterRepositoryImpl.cancalCustomerBiz(map);
            //入库
            bizInstockInfoRepositoryImpl.cancalCustomerBiz(map);
            //标准包装方案
            bizDispatchPackageRepositoryImpl.cancalCustomerBiz(map);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BizException("作废业务数据失败,参数"+JSONObject.fromObject(map)+",异常",e);
        }
        
        return 1;
    }

    private  void getDate(Map<String,Object> map){
        
        try {
            String beginTime = DateUtil.getFirstDayOfMonth(0,"yyyy-MM-dd");
            beginTime+=" 00:00:00";
            map.put("beginTime", beginTime);
          
            String endTime = DateUtil.getFirstDayOfMonth(-1,"yyyy-MM-dd");
            endTime+=" 00:00:00";
            map.put("endTime", endTime);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("获取时间异常", e);
        }
        
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int restoreCustomerBiz(String customerId) {
        // TODO Auto-generated method stub                     
        Map<String,Object> map=new HashMap<>();
        map.put("customerId", customerId);      
        getDate(map);
        
        if(map.get("beginTime")==null || map.get("endTime")==null){
            return 0;
        }
        
        try {           
            //作废业务数据和费用  业务数据状态改为4，费用状态改为1
            //增值
            bizAddFeeRepositoryImpl.restoreCustomerBiz(map);
            //配送
            bizDispatchBillRepositoryImp.restoreCustomerBiz(map);
            //耗材
            bizOutstockPackmaterialRepositoryImpl.restoreCustomerBiz(map);
            //托数
            bizPalletInfoRepositoryImpl.restoreCustomerBiz(map);
            //按件
            bizProductStorageRepositoryImpl.restoreCustomerBiz(map);
            //出库
            bizOutstockMasterRepositoryImpl.restoreCustomerBiz(map);
            //入库
            bizInstockInfoRepositoryImpl.restoreCustomerBiz(map);
            //标准包装方案
            bizDispatchPackageRepositoryImpl.restoreCustomerBiz(map);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BizException("恢复业务数据失败,参数"+JSONObject.fromObject(map)+",异常",e);
        }
        
        return 1;
    }


}
