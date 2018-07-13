package com.jiuyescm.bms.biz.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizTihuoBillRepository;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchTihuoBillService;

@Service("bizDispatchTihuoBillService")
public class BizDispatchTihuoBillServiceImp implements IBizDispatchTihuoBillService{
	
	@Autowired
    private IBizTihuoBillRepository bizTihuoBillRepository;

    @Override
    public PageInfo<BizTihuoBillEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizTihuoBillRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizTihuoBillEntity findById(Long id) {
        return bizTihuoBillRepository.findById(id);
    }

    @Override
    public BizTihuoBillEntity save(BizTihuoBillEntity entity) {
        return bizTihuoBillRepository.save(entity);
    }

    @Override
    public BizTihuoBillEntity update(BizTihuoBillEntity entity) {
        return bizTihuoBillRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizTihuoBillRepository.delete(id);
    }

	@Override
	public List<BizTihuoBillEntity> countByDate(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizTihuoBillRepository.countByDate(condition);
	}

	@Override
	public int insertBillList(List<BizTihuoBillEntity> list) {
		// TODO Auto-generated method stub
		return bizTihuoBillRepository.insertBillList(list);
	}

	@Override
	public List<BizTihuoBillEntity> queryData(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizTihuoBillRepository.queryData(condition);
	}

	@Override
	public int deleteBizByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizTihuoBillRepository.deleteBizByMap(condition);
	}
}
