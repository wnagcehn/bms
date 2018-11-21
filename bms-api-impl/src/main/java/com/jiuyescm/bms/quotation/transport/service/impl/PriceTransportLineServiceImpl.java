/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
//import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CargoIsLightEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRangeRepository;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRepository;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportLineService")
public class PriceTransportLineServiceImpl implements IPriceTransportLineService {

	private static final Logger logger = Logger.getLogger(PriceTransportLineServiceImpl.class.getName());
	
	@Autowired
    private IPriceTransportLineRepository priceTransportLineRepository;
	
	@Autowired
	private IPriceTransportLineRangeRepository priceTransportLineRangeRepository;
	@Autowired
	private IReceiveRuleRepository receiveRuleRepository;
	/*@Resource 
	private IFeesCalcuService feesCalcuService;*/

    @Override
    public PageInfo<PriceTransportLineEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportLineRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PriceTransportLineEntity findById(Long id) {
        return priceTransportLineRepository.findById(id);
    }

    @Override
    public PriceTransportLineEntity save(PriceTransportLineEntity entity) {
        return priceTransportLineRepository.save(entity);
    }

    @Override
    public PriceTransportLineEntity update(PriceTransportLineEntity entity) {
        return priceTransportLineRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportLineRepository.delete(id);
    }

	@Override
	public List<PriceTransportLineEntity> query(Map<String, Object> condition) {
		return priceTransportLineRepository.query(condition);
	}

	@Override
	public int saveList(List<PriceTransportLineEntity> lineList) {
		return priceTransportLineRepository.saveList(lineList);
	}

	@Override
	public Integer findIdByLineNo(String lineNo) {
		return priceTransportLineRepository.findIdByLineNo(lineNo);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int saveListWithChild(List<PriceTransportLineEntity> lineList) {
		int k = 0;
		for (PriceTransportLineEntity priceTransportLineEntity : lineList) {
			priceTransportLineRepository.save(priceTransportLineEntity);
			if (priceTransportLineEntity.getChild()!=null&&priceTransportLineEntity.getChild().size()>0) {
				for (PriceTransportLineRangeEntity priceTransportLineRangeEntity : priceTransportLineEntity.getChild()) {
					priceTransportLineRangeEntity.setLineId(priceTransportLineEntity.getId().toString());
				}
				priceTransportLineRangeRepository.saveList(priceTransportLineEntity.getChild());
			}
			k++;
		}
		return k;
	}

	@Override
	public int deleteBatch(Long id) {
		return  priceTransportLineRepository.deleteBatch(id);
	}

	@Override
	public int deleteBatchList(List<PriceTransportLineEntity> lineList) {
		for(PriceTransportLineEntity entity:lineList)
			priceTransportLineRepository.deleteBatchRange(entity.getId());
		return priceTransportLineRepository.deleteBatchList(lineList);
	}

	@Override
	public List<PriceTransportLineEntity> queryToCityByProductType(Map<String, Object> condition) {
		return priceTransportLineRepository.queryToCityByProductType(condition);
	}

	@Override
	public List<PriceTransportLineEntity> queryStandardTemplateLine(Map<String, Object> condition) {
		return priceTransportLineRepository.queryStandardTemplateLine(condition);
	}

	@Override
	public CalcuResultVo trial(BizGanxianWayBillEntity data) {
		CalcuResultVo resultVo = new CalcuResultVo();
		//通过该商家id和费用科目查询计费规则
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("customerid", data.getCustomerId());
		ruleParam.put("subjectId",data.getBizTypeCode());
		BillRuleReceiveEntity ruleEntity=receiveRuleRepository.queryByCustomerId(ruleParam);
		if(ruleEntity==null){
			resultVo.setCode("fail");
			resultVo.setMsg("未查询到该商家的规则");
			return resultVo;
		}
		CalcuReqVo<PriceTransportLineEntity> reqVo= new CalcuReqVo<PriceTransportLineEntity>();
		List<PriceTransportLineEntity> list = data.getPriceList();
		PriceTransportLineEntity passLine=list.get(0);
		
		Map<String, Object> rangeParam = new HashMap<String,Object>();
		rangeParam.put("lineId", passLine.getId());
		rangeParam.put("delFlag", "0");
		//处理阶梯报价参数
		hanlerRangeParam(rangeParam, data);
		List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeRepository.query(rangeParam);
		if(rangeList==null || rangeList.size() <= 0){
			resultVo.setCode("fail");
			resultVo.setMsg("未查询到标准报价信息");
			return resultVo;
		}
		passLine.setChild(rangeList);
		
		reqVo.setQuoEntites(list);
		reqVo.setBizData(data);
		reqVo.setRuleNo(ruleEntity.getQuotationNo());
		reqVo.setRuleStr(ruleEntity.getRule());
		
		//resultVo=feesCalcuService.FeesCalcuService(reqVo);	
		resultVo = null;
		return resultVo;
	}

	@Override
	public List<PriceTransportLineEntity> queryTemplateLine(
			Map<String, Object> condition) {
		return priceTransportLineRepository.queryTemplateLine(condition);
	}
	
	private Map<String, Object> hanlerRangeParam(Map<String, Object> rangeParam, BizGanxianWayBillEntity data){
		logger.info("=========format range param=============");
		if (TransportWayBillTypeEnum.CJ.getCode().equals(data.getBizTypeCode())) {
			if (StringUtils.isNotEmpty(data.getIsLight())) {
				if (CargoIsLightEnum.YES.getCode().equals(data.getIsLight())) {
					rangeParam.put("totalVolume", data.getTotalVolume());
				}else if (CargoIsLightEnum.NO.getCode().equals(data.getIsLight())) {
					rangeParam.put("totalWeight", data.getTotalWeight());
				}
			}
			if (StringUtils.isNotEmpty(data.getProductType())) {
				rangeParam.put("productTypeCode", data.getProductType());
			}
		}else if (TransportWayBillTypeEnum.TC.getCode().equals(data.getBizTypeCode())) {
			if (StringUtils.isNotEmpty(data.getCarModel())) {
				rangeParam.put("carModelCode", data.getCarModel());
			}
		}else if (TransportWayBillTypeEnum.DSZL.getCode().equals(data.getBizTypeCode())) {
			if (StringUtils.isNotEmpty(data.getCarModel())) {
				rangeParam.put("carModelCode", data.getCarModel());
			}
		}else if (TransportWayBillTypeEnum.HXD.getCode().equals(data.getBizTypeCode())) {
			if (null != data.getTotalWeight()) {
				rangeParam.put("totalWeight", data.getTotalWeight());
			}
		}
		return null;
	}
}
