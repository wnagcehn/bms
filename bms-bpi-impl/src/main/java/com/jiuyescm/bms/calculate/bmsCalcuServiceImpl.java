package com.jiuyescm.bms.calculate;

import java.util.List;

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


	@Autowired IBmsCalcuRepository bmsCalcuServiceImpl;
	
	@Autowired ICustomerDictService customerDictService;
	
	@Override
	public BmsFeesQtyVo queryFeesQtyForSto(String customerId, String subjectCode,Integer creMonth) {
		BmsFeesQtyVo vo = new BmsFeesQtyVo();
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
		vo.setFeesCount(entityTotal.getFeesCount());
		List<BmsFeesQtyEntity> statusList = bmsCalcuServiceImpl.queryStatusFeesQtyForDis(customerId, subjectCode, startTime, endTime);
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

}
