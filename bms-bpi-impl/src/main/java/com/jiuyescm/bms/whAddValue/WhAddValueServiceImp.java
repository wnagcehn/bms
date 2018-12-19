package com.jiuyescm.bms.whAddValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.common.sequence.repository.SequenceDao;
//import com.jiuyescm.bms.fees.INormalReqVoService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.bms.whAddValue.service.IWhAddValueService;
import com.jiuyescm.bms.whAddValue.vo.ResultVo;
import com.jiuyescm.bms.whAddValue.vo.WhAddValueVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;

@Service("whAddValueService")
public class WhAddValueServiceImp implements IWhAddValueService{

	@Resource
	private IBizAddFeeRepository bizAddFeeRepository;
	
	@Autowired 
	private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	
	@Autowired 
	private SequenceDao sequenceDao;
	
	//@Resource private INormalReqVoService  service;

	private static final Logger logger = Logger.getLogger(WhAddValueServiceImp.class.getName());

	@Override
	public List<ResultVo> insertWhaddValue(List<WhAddValueVo> list) {
		List<ResultVo> returnList=new ArrayList<>();
		
		//需要计算的list
		List<BizAddFeeEntity> calList=new ArrayList<BizAddFeeEntity>();
		
		//======================增值费业务数据的导入开始==========================
		for(WhAddValueVo vo:list){
			//wmsid必填
			if(vo.getWmsId()==null){
				ResultVo resultVo=new ResultVo(vo.getWmsId()+"","fail","wmsid为必传项");
				returnList.add(resultVo);
				continue;
			}
			
			//必填项校验
			String errMessage=checkData(vo);
			if(StringUtils.isNotBlank(errMessage)){
				ResultVo resultVo=new ResultVo(vo.getWmsId()+"","fail",errMessage);
				returnList.add(resultVo);
				continue;
			}
			
			//幂等性；（如果已经接收，直接返回成功)
			Map<String, Object> condition=new HashMap<String, Object>();
			if(vo.getWmsId()!=null){
				condition.put("wmsId", vo.getWmsId());
				BizAddFeeEntity addFeeEntity=bizAddFeeRepository.queryWms(condition);
				if(addFeeEntity!=null){
					ResultVo resultVo=new ResultVo(vo.getWmsId()+"","sucess","导入成功");
					returnList.add(resultVo);
					continue;
				}
			}
	
			//转换实体类
			BizAddFeeEntity newEntity=new BizAddFeeEntity();
			try {
	            PropertyUtils.copyProperties(newEntity, vo);
	        } catch (Exception ex) {
	        	logger.error(ex);
	        	ResultVo resultVo=new ResultVo(vo.getWmsId()+"","fail","转换失败");
				returnList.add(resultVo);
				continue;
	        }
			
			//插入业务数据表
			newEntity.setAdjustNum(newEntity.getNum());
			//newEntity.setCreator(JAppContext.currentUserName());
			//newEntity.setCreateTime(JAppContext.currentTimestamp());
			//newEntity.setLastModifier(JAppContext.currentUserName());
			//newEntity.setLastModifyTime(JAppContext.currentTimestamp());
			newEntity.setIsCalculated("0");
			newEntity.setOperationTime(JAppContext.currentTimestamp());
            newEntity.setWriteTime(JAppContext.currentTimestamp());
			newEntity.setDelFlag("0");
			BizAddFeeEntity rEntity=bizAddFeeRepository.save(newEntity);
			if(rEntity==null){
				ResultVo resultVo=new ResultVo(vo.getWmsId()+"","fail","导入失败");
				returnList.add(resultVo);
			}else{
				ResultVo resultVo=new ResultVo(vo.getWmsId()+"","sucess","导入成功");			
				//插入成功的数据需要加入计算的list
				calList.add(rEntity);	
				returnList.add(resultVo);
			}
			
		}
		//======================增值费业务数据的导入结束==========================

		
		//======================业务数据的计算====================================
		if(calList.size()>0){
			calculate(calList);
		}
		return returnList;
	}

/*	*//**
	 * 计算方法
	 * @param list
	 */
	public void calculate(List<BizAddFeeEntity> list){
		/*List<BizAddFeeEntity> reList = service.getStorageExtraReqVo(list);
    	 	
    	List<FeesReceiveStorageEntity> feeList = new  ArrayList<FeesReceiveStorageEntity>();
    	
    	for(BizAddFeeEntity entity:reList){
			FeesReceiveStorageEntity fee=init(entity);
			if(StringUtils.isEmpty(entity.getFeesNo())){
				//费用编号为空
				String feesNo = sequenceDao.getBillNoOne("com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity", "ABF", "0000000000");
				entity.setFeesNo(feesNo);
    			fee.setFeesNo(feesNo);
			}
			feeList.add(fee);  		
    	}
    	if(feeList.size()>0){
    		try {
				feesReceiveStorageRepository.insertBatch(feeList);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
    	}
    	
    	bizAddFeeRepository.updateList(reList);*/
	}
	
     public FeesReceiveStorageEntity init(BizAddFeeEntity entity){
	    	FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
	    	fee.setFeesNo(entity.getFeesNo());
	    	fee.setCost(new BigDecimal(0d));
	    	if(!DoubleUtil.isBlank(entity.getPrice())){
	    		fee.setCost(new BigDecimal(entity.getPrice()));
	    	}
	    	fee.setCost(new BigDecimal(entity.getPrice()));
	    	fee.setIsCalculated(entity.getIsCalculated());
	    	fee.setCalculateTime(JAppContext.currentTimestamp());
			fee.setUnitPrice(entity.getUnitPrice());
			fee.setSubjectCode("wh_value_add_subject");
			fee.setOtherSubjectCode(entity.getFeesType());
			fee.setOtherSubjectCode(entity.getFeesType());
			fee.setCustomerId(entity.getCustomerid());
			fee.setCustomerName(entity.getCustomerName());
			fee.setWarehouseCode(entity.getWarehouseCode());
			fee.setUnit(entity.getFeesUnit());
			fee.setQuantity(entity.getNum().intValue());
	        fee.setParam1(entity.getItem());
	        fee.setCustomerName(entity.getCustomerName());
	        fee.setWarehouseName(entity.getWarehouseName());	
			fee.setCreateTime(entity.getCreateTime());
			fee.setCreator("system");	
			fee.setCostType("FEE_TYPE_GENEARL");
			fee.setStatus("0");
			fee.setDelFlag("0");
			fee.setLastModifier(JAppContext.currentUserName());
			fee.setLastModifyTime(JAppContext.currentTimestamp());
			fee.setBizId(entity.getId()+"");
			return fee;
	    }
	/**
	 * 必填项校验
	 * @param vo
	 * @return
	 */
	public String checkData(WhAddValueVo vo){		
		String errMessage="";
		
		if(StringUtils.isBlank(vo.getWarehouseCode())){
			errMessage+="仓库id不能为空;";
		}
		if(StringUtils.isBlank(vo.getWarehouseName())){
			errMessage+="仓库名称不能为空;";
		}
		if(StringUtils.isBlank(vo.getCustomerid())){
			errMessage+="商家id不能为空;";
		}
		if(StringUtils.isBlank(vo.getCustomerName())){
			errMessage+="商家名称不能为空;";
		}
		if(vo.getNum()==null){
			errMessage+="数量不能为空;";
		}
		if(StringUtils.isBlank(vo.getFeesType())){
			errMessage+="费用科目不能为空;";
		}
		if(StringUtils.isBlank(vo.getFeesTypeName())){
			errMessage+="费用科目名不能为空;";
		}
		if(vo.getCreateTime()==null){
			errMessage+="业务时间不能为空;";
		}
		if(StringUtils.isBlank(vo.getFeesUnit())){
			errMessage+="单位不能为空;";
		}
		return errMessage;
	}
}
