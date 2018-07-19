package com.jiuyescm.bms.base.dictionary.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author cjw
 * 
 */
@Service("systemCodeService")
public class SystemCodeServiceImpl implements ISystemCodeService {
	
	@Autowired
    private ISystemCodeRepository systemCodeRepository;

    @Override
    public PageInfo<SystemCodeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return systemCodeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public SystemCodeEntity findById(Long id) {
        return systemCodeRepository.findById(id);
    }

    @Override
    public SystemCodeEntity save(SystemCodeEntity entity) throws Exception{
    	if(getSystemCode(entity.getTypeCode(),entity.getCode()) != null){
    		throw new BizException("类型编码已存在");
    	}
        return systemCodeRepository.save(entity);
    }

    @Override
    public SystemCodeEntity update(SystemCodeEntity entity) throws Exception{
     	Map<String, Object> condition = new HashMap<String, Object>(); 
    	condition.put("typeCode",entity.getTypeCode());
    	condition.put("code",entity.getCode());
    	
    	List<SystemCodeEntity> entityList = queryCodeList(condition);
    	for(SystemCodeEntity temp : entityList){
    		if (!temp.getId().equals(entity.getId()) && entity.getCode().equals(temp.getCode())){
    			throw new BizException("类型编码已存在");
    		}
    	}
        return systemCodeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        systemCodeRepository.delete(id);
    }
  
	@Override
	public SystemCodeEntity getSystemCode(String typeCode, String code) { 
		return systemCodeRepository.getSystemCode(typeCode, code);
	}

	@Override
	public List<SystemCodeEntity> findEnumList(String typeCode) { 
		return systemCodeRepository.findEnumList(typeCode);
	}

	@Override
	public List<SystemCodeEntity> queryTempType(Map<String, Object> param) { 
		return systemCodeRepository.queryTempType(param);
	}

	@Override
	public List<SystemCodeEntity> queryCodeList(Map<String, Object> param) { 
		return systemCodeRepository.queryCodeList(param);
	}

	@Override
	public List<SystemCodeEntity> queryBycreateDt(Map<String, Object> param) { 
		return systemCodeRepository.queryBycreateDt(param);
	}

	@Override
	public String getImpExcelFilePath(){
		SystemCodeEntity systemCode = this.getSystemCode("GLOABL_PARAM", "IMP_EXCEL_PATH");
		if(systemCode == null){
			return null;
		}
		return systemCode.getExtattr1();
	}

	@Override
	public int getPartOutboundStatus() {
		return systemCodeRepository.getPartOutboundStatus();
	}

	@Override
	public SystemCodeEntity queryEntityByCode(String code) {
		return systemCodeRepository.queryEntityByCode(code);
	}
	
	
	

	
}
