package com.jiuyescm.bms.asyn.service.impl;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.api.IAsynCalcuService;
import com.jiuyescm.bms.asyn.entity.AsynCalcuEntity;
import com.jiuyescm.bms.asyn.repo.IAsynCalcuRepository;
import com.jiuyescm.bms.asyn.vo.AsynCalcuVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("asynCalcuService")
public class AsynCalcuServiceImpl implements IAsynCalcuService {

	@Autowired
    private IAsynCalcuRepository AsynCalcuRepository;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
    @Override
    public PageInfo<AsynCalcuVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<AsynCalcuVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<AsynCalcuVo>();
			PageInfo<AsynCalcuEntity> pageInfo=AsynCalcuRepository.query(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<AsynCalcuVo> list=new ArrayList<AsynCalcuVo>();
				for(AsynCalcuEntity entity:pageInfo.getList()){
					AsynCalcuVo voEntity=new AsynCalcuVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			throw e;
		}
		return pageVoInfo;
    }

	
	/**
	 * 保存
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
    @Override
    public int save(AsynCalcuVo entity) throws Exception {
		try{
			AsynCalcuEntity acentity=new AsynCalcuEntity();
			PropertyUtils.copyProperties(entity, acentity);
			return AsynCalcuRepository.save(acentity);
		}catch(Exception e){
			throw e;
		}    
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
    @Override
    public int update(AsynCalcuVo entity) throws Exception {
		try{
			AsynCalcuEntity acentity=new AsynCalcuEntity();
			PropertyUtils.copyProperties(entity, acentity);
			return AsynCalcuRepository.update(acentity);
		}catch(Exception e){
			throw e;
		}  
    }

	@Override
	public int delete(AsynCalcuVo voEntity) throws Exception {
		try{
			AsynCalcuEntity entity=new AsynCalcuEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return AsynCalcuRepository.delete(entity);
		}catch(Exception e){
			throw e;
		}
	}
	
}
