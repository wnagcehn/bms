
package com.jiuyescm.bms.base.dictionary.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeTypeRepository;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeTypeService;
/**
 * 
 * @author cjw
 * 
 */
@Service("systemCodeTypeService")
public class SystemCodeTypeServiceImpl implements ISystemCodeTypeService {

	private static final Logger logger = Logger.getLogger(SystemCodeTypeServiceImpl.class.getName());
	
	@Autowired
    private ISystemCodeTypeRepository systemCodeTypeRepository;
	
	@Autowired
	private ISystemCodeRepository systemCodeRepository;
	
    @Override
    public PageInfo<SystemCodeTypeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return systemCodeTypeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public SystemCodeTypeEntity findById(Long id) {
        return systemCodeTypeRepository.findById(id);
    }

    @Override
    public SystemCodeTypeEntity save(SystemCodeTypeEntity entity) throws Exception{
    	if(findByTypeCode(entity.getTypeCode()) != null){
    		throw new Exception("类型编码已存在");
    	}
        return systemCodeTypeRepository.save(entity);
    }

    @Override
    public SystemCodeTypeEntity update(SystemCodeTypeEntity entity) throws Exception{
    	if (entity == null || entity.getId()<=0){
    		throw new Exception("修改的系统参数不存在（id 不能为空）");
    	}
    	Map<String, Object> condition = new HashMap<String, Object>(); 
    	condition.put("typeCode",entity.getTypeCode());
    	
    	List<SystemCodeTypeEntity> entityList = query(condition);
    	for(SystemCodeTypeEntity temp : entityList){
    		if (!temp.getId().equals(entity.getId()) && entity.getTypeCode().equals(temp.getTypeCode())){
    			throw new Exception("类型编码已存在");
    		}
    	}
    	
    	if ("0".equals(entity.getTypeStatus())){ 
    		List<SystemCodeEntity> codeList = systemCodeRepository.findEnumList(entity.getTypeCode());
    		for(SystemCodeEntity codeEntity :codeList){
    			codeEntity.setStatus("0"); 
    			systemCodeRepository.update(codeEntity);
    		}
    	}
    	
        return systemCodeTypeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        systemCodeTypeRepository.delete(id);
    }

	@Override
	public SystemCodeTypeEntity findByTypeCode(String typeCode) { 
		return systemCodeTypeRepository.findByTypeCode(typeCode);
	}

	@Override
	public List<SystemCodeTypeEntity> query(Map<String, Object> condition) { 
		return systemCodeTypeRepository.query(condition);
	}

	@Override
	public int updateByParam(Map<String, Object> condition)
			throws Exception {
		// TODO Auto-generated method stub
		return systemCodeTypeRepository.updateByParam(condition);
	}


	
}
