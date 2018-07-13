package com.jiuyescm.bms.base.dictionary.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.variant.Record;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeTypeService;
import com.jiuyescm.cfm.common.JAppContext;


/**
 * 
 * @author cjw
 * 
 */
@Controller("systemCodeTypeController")
public class SystemCodeTypeController {

	private static final Logger logger = Logger.getLogger(SystemCodeTypeController.class.getName());

	@Resource
	private ISystemCodeTypeService systemCodeTypeService;
	/*@Resource
	private ISequenceService sequenceService;*/

	
	@DataProvider
	public SystemCodeTypeEntity findById(Long id) throws Exception {
		SystemCodeTypeEntity entity = null;
		entity = systemCodeTypeService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<SystemCodeTypeEntity> page, Criteria criteria, Record parameter) {
		/*long seq = sequenceService.nextSeq("DO");
		logger.info("@@@@@@@@@@@@@" + seq);
		seq = sequenceService.nextSeq("DO", 10);
		 
		logger.info("@@@@@@@@@@@@@" + seq);*/
		PageInfo<SystemCodeTypeEntity> pageInfo = systemCodeTypeService.query(
				convertCriteriaToMap(criteria), page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	public static Map<String, Object> convertCriteriaToMap(Criteria criteria){ 
		Map<String,Object> param = new HashMap<String, Object>();
		if (criteria != null){ 
			List<Criterion> list = criteria.getCriterions();
			for(Criterion temp : list){ 
				SingleValueFilterCriterion sfc =(SingleValueFilterCriterion) temp;
				param.put(sfc.getProperty(), sfc.getValue()); 
			}
		}
		return param;
	}

	@DataResolver
	public void save(SystemCodeTypeEntity entity) throws Exception{ 
		
		//注释自动大小的功能  cjw 2018-02-01
		//entity.setTypeCode(entity.getTypeCode().toUpperCase());
		if (entity.getId() == null) {
			entity.setCreateId(JAppContext.currentUserID());
			entity.setCreateDt(JAppContext.currentTimestamp());
			systemCodeTypeService.save(entity);
		} else {
			entity.setUpdateId(JAppContext.currentUserID());
			entity.setUpdateDt(JAppContext.currentTimestamp());
			systemCodeTypeService.update(entity);
		}
	}

	@DataResolver
	public void delete(SystemCodeTypeEntity entity) {
		entity.setDeleteId(JAppContext.currentUserID());
		entity.setDeleteDt(JAppContext.currentTimestamp());
		systemCodeTypeService.delete(entity.getId());
	}
	
	
	
	
}
