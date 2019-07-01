package com.jiuyescm.bms.calculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.repo.IBmsCalcuRepository;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.common.utils.DateUtil;

@Service("bmsCalcuService")
public class bmsCalcuServiceImpl implements IBmsCalcuService {

	private static final Logger logger = Logger.getLogger(bmsCalcuServiceImpl.class.getName());

	@Autowired IBmsCalcuRepository bmsCalcuServiceImpl;
	
	@Autowired ICustomerDictService customerDictService;
	
	@Override
	public BmsFeesQtyVo queryFeesQtyForSto(String customerId, String subjectCode,Integer creMonth) {
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
			BmsFeesQtyEntity entityTotal = bmsCalcuServiceImpl.queryTotalFeesQtyForSto(customerId, subjectCode, startTime, endTime);
			if(entityTotal == null){
				return null;
			}
			vo.setFeesCount(entityTotal.getFeesCount());
			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryStatusFeesQtyForSto(customerId, subjectCode, startTime, endTime);
			if(statusList != null && statusList.size()>0){
				for (BmsFeesQtyEntity entity : statusList) {
					switch (entity.getIsCalculated()) {
					case "0":
						vo.setBeginCount(entity.getFeesCount());
						break;
					case "1":
						vo.setFinishCount(entity.getFeesCount());
						break;
					case "2":
						vo.setSysErrorCount(entity.getFeesCount());
						break;
					case "3":
						vo.setContractMissCount(entity.getFeesCount());
						break;
					case "4":
						vo.setQuoteMissCount(entity.getFeesCount());
						break;
					case "5":
						vo.setNoExeCount(entity.getFeesCount());
						break;
					case "99":
						vo.setUncalcuCount(entity.getFeesCount());
						break;
					default:
						vo.setSysErrorCount(entity.getFeesCount());
						break;
					}
				}
			}
			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}

	@Override
	public BmsFeesQtyVo queryFeesQtyForDis(String customerId,String subjectCode, Integer creMonth) {
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
		int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
		String startTime = startYear+"-"+startMonth+"-01";
		String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
		BmsFeesQtyEntity entityTotal = bmsCalcuServiceImpl.queryTotalFeesQtyForDis(customerId, subjectCode, startTime, endTime);
		if(entityTotal == null){
			return null;
		}
		List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryStatusFeesQtyForDis(customerId, subjectCode, startTime, endTime);
		tongji(statusList,vo);
		return vo;
	}

	@Override
	public String queryContractAttr(String customerId) {
		PubCustomerVo vo = customerDictService.queryById(customerId);
		if(vo == null || vo.getContractAttr() == null){
			return null;
		}
		String contractAttr = null;
		if(vo.getContractAttr() == 1){
			contractAttr = "BMS";
		}
		else{
			contractAttr = "CONTRACT";
		}
		return contractAttr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsFeesQtyVo queryFeesQtyForStoInstock(String customerId,
			String subjectCode, Integer creMonth) {
		// TODO Auto-generated method stub
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
					
			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoInstock(customerId, subjectCode, startTime, endTime);
			tongji(statusList,vo);

			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}

	@Override
	public BmsFeesQtyVo queryFeesQtyForStoOutstock(String customerId,
			String subjectCode, Integer creMonth) {
		// TODO Auto-generated method stub
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
					
			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoOutstock(customerId, subjectCode, startTime, endTime);
			tongji(statusList,vo);

		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}

	@Override
	public BmsFeesQtyVo queryFeesQtyForStoMaterial(String customerId,
			String subjectCode, Integer creMonth) {
		// TODO Auto-generated method stub
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
				
			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoMaterial(customerId, subjectCode, startTime, endTime);
			tongji(statusList,vo);

			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}

	@Override
	public BmsFeesQtyVo queryFeesQtyForStoProductItem(String customerId,
			String subjectCode, Integer creMonth) {
		// TODO Auto-generated method stub
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
					
			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoProductItem(customerId, subjectCode, startTime, endTime);
			tongji(statusList,vo);

			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}

	@Override
	public BmsFeesQtyVo queryFeesQtyForStoAdd(String customerId,
			String subjectCode, Integer creMonth) {
		// TODO Auto-generated method stub
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");

			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoAdd(customerId, subjectCode, startTime, endTime);
			tongji(statusList,vo);

			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}

	@Override
	public BmsFeesQtyVo queryFeesQtyForStoPallet(String customerId,
			String subjectCode, Integer creMonth) {
		// TODO Auto-generated method stub
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
		try {
			int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
			int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
			String startTime = startYear+"-"+startMonth+"-01";
			String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
		
			
			List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoPallet(customerId, subjectCode, startTime, endTime);
			tongji(statusList,vo);
			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("查询异常",e);
		}
		return vo;
	}


    @Override
    public BmsFeesQtyVo queryFeesQtyForStoStandMaterial(String customerId, String subjectCode, Integer creMonth) {
        // TODO Auto-generated method stub
        BmsFeesQtyVo vo = new BmsFeesQtyVo();
        try {
            int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
            int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
            String startTime = startYear+"-"+startMonth+"-01";
            String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
        
            
            List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryFeesQtyForStoStandMaterial(customerId, subjectCode, startTime, endTime);
            tongji(statusList,vo);
            return vo;
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("查询异常",e);
        }
        return vo;
    }
	
	private void tongji(List<BmsFeesQtyEntity> statusList,BmsFeesQtyVo vo){
		int entityTotal=0;
		List<Integer> sortList=new ArrayList<Integer>();
		Integer biaoshi=0;
		if(statusList != null && statusList.size()>0){
			for (BmsFeesQtyEntity entity : statusList) {	
				switch (entity.getIsCalculated()) {
				case "1"://计算成功
					vo.setFinishCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=3;
					break;
				case "2"://系统错误
					vo.setSysErrorCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=2;
					break;
				case "3"://合同缺失
					vo.setContractMissCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=2;
					break;
				case "4"://报价缺失
					vo.setQuoteMissCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=2;
					break;
				case "5"://不计算
					vo.setNoExeCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=3;
					break;
				case "99"://待重算
					vo.setUncalcuCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=1;
					break;
				default://系统错误
					/*vo.setSysErrorCount(entity.getFeesCount());
					entityTotal += entity.getFeesCount();
					biaoshi=2;*/
					break;
				}

				if(biaoshi!=0){
					sortList.add(biaoshi);
				}
			}
			
			if(sortList.size()>0){
				//进行排序
				Collections.sort(sortList);
				//取出最小的计算状态
				Integer min=sortList.get(0);
				if(min==1){
					vo.setCalcuStatus(0);//未计算
				}else if(min==2){
					vo.setCalcuStatus(2);//不成功
				}else if(min==3){
					vo.setCalcuStatus(1);//成功
				}
			}
			
		}
		
		
		
		vo.setFeesCount(entityTotal);
	}
	
	@Override
    public BmsFeesQtyVo queryTotalAmountForStoOutstock(String customerId,
            String subjectCode, Integer creMonth) {
        BmsFeesQtyVo vo = new BmsFeesQtyVo();
        try {
            int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
            int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
            String startTime = startYear+"-"+startMonth+"-01";
            String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
            
            BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoOutstock(customerId, subjectCode, startTime, endTime);
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception e) {
            logger.info("查询异常",e);
        }
        return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoInstock(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoInstock(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoPallet(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoPallet(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoProductItem(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoProductItem(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoStandMaterial(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoStandMaterial(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoAdd(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoAdd(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoMaterial(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoMaterial(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}
	
	@Override
	public BmsFeesQtyVo queryTotalAmountForStoDis(String customerId,
	        String subjectCode, Integer creMonth) {
	    BmsFeesQtyVo vo = new BmsFeesQtyVo();
	    try {
	        int startYear = Integer.parseInt(creMonth.toString().substring(0, 4));
	        int startMonth = Integer.parseInt(creMonth.toString().substring(4, 6));
	        String startTime = startYear+"-"+startMonth+"-01";
	        String endTime = DateUtil.getFirstDayOfGivenMonth(startTime,1,"yyyy-MM-dd");
	        
	        BmsFeesQtyEntity entity = bmsCalcuServiceImpl.queryTotalAmountForStoDis(customerId, subjectCode, startTime, endTime);
	        PropertyUtils.copyProperties(vo, entity);
	        return vo;
	    } catch (Exception e) {
	        logger.info("查询异常",e);
	    }
	    return vo;
	}

}
