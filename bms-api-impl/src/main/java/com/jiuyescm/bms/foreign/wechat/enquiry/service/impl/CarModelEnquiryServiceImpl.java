package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.wechat.enquiry.api.ICarModelEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.vo.CarModelPriceVo;

@Service("carModelEnquiryService")
public class CarModelEnquiryServiceImpl implements ICarModelEnquiryService{

	@Autowired
	private ISystemCodeRepository systemCodeRepository;
	
	@Override
	public List<CarModelPriceVo> queryCarModel() {
		List<CarModelPriceVo> carModelList = null;
		List<SystemCodeEntity> systemCodeList = systemCodeRepository.findEnumList("MOTORCYCLE_TYPE");
		CarModelPriceVo vo = null;
		if (null != systemCodeList && systemCodeList.size() > 0) {
			carModelList = new ArrayList<CarModelPriceVo>();
			for (SystemCodeEntity systemCodeEntity : systemCodeList) {
				vo = new CarModelPriceVo(systemCodeEntity.getCode(), 0);
				carModelList.add(vo);
			}
		}
		return carModelList;
	}

}
