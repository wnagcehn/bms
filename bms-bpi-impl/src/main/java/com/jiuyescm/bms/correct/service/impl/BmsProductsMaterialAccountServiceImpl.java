package com.jiuyescm.bms.correct.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsMaterialAccountRepository;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialAccountService;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsProductsMaterialAccountService")
public class BmsProductsMaterialAccountServiceImpl implements IBmsProductsMaterialAccountService {
	private static final Logger logger = Logger.getLogger(BmsProductsMaterialAccountServiceImpl.class.getName());
	
	@Autowired
    private IBmsProductsMaterialAccountRepository bmsProductsMaterialAccountRepository;

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
    @Override
    public PageInfo<BmsProductsMaterialAccountVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsProductsMaterialAccountVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsProductsMaterialAccountVo>();
			PageInfo<BmsProductsMaterialAccountEntity> pageInfo=bmsProductsMaterialAccountRepository.query(condition,pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsProductsMaterialAccountVo> list=new ArrayList<BmsProductsMaterialAccountVo>();
				for(BmsProductsMaterialAccountEntity entity:pageInfo.getList()){
					BmsProductsMaterialAccountVo vo=new BmsProductsMaterialAccountVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("query:",e);
			throw e;
		}
		return pageVoInfo;
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsProductsMaterialAccountEntity> query(Map<String, Object> condition){
		return bmsProductsMaterialAccountRepository.query(condition);
	}
	
}
