package com.jiuyescm.bms.quotation.transport.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.quotation.transport.service.IGenericTemplateService;

/**
 * 报价模板操作实现
 * @author wubangjun
 */
@Service("genericTemplateService")
public class GenericTemplateServiceImpl implements IGenericTemplateService{
	
	private static final Logger logger = Logger.getLogger(GenericTemplateServiceImpl.class.getName());
	
	@Autowired
	private IGenericTemplateRepository genericTemplateRepository;
	
	@Override
	public PageInfo<GenericTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		return genericTemplateRepository.query(condition, pageNo, pageSize);
	}
	
	@Override
	public GenericTemplateEntity query(Map<String, Object> condition) {
		return genericTemplateRepository.query(condition);
	}

	@Override
	public GenericTemplateEntity findById(Long id) {
		return genericTemplateRepository.findById(id);
	}

	@Override
	public GenericTemplateEntity save(GenericTemplateEntity entity) {
		return genericTemplateRepository.save(entity);
	}

	@Override
	public GenericTemplateEntity update(GenericTemplateEntity entity) {
		return genericTemplateRepository.update(entity);
	}

	@Override
	public void delete(Long id) {
		genericTemplateRepository.delete(id);
	}

   

}
