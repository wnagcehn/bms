package com.jiuyescm.bms.correct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.repository.IBmsOrderProductRepository;
import com.jiuyescm.bms.correct.service.IBmsOrderProductService;
import com.jiuyescm.bms.correct.vo.BmsOrderProductVo;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsOrderProductService")
public class BmsOrderProductServiceImpl implements IBmsOrderProductService {
	private static final Logger logger = Logger.getLogger(BmsOrderProductServiceImpl.class.getName());

	@Autowired
    private IBmsOrderProductRepository bmsOrderProductRepository;

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
    @Override
    public PageInfo<BmsOrderProductVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception {
    	PageInfo<BmsOrderProductVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsOrderProductVo>();
			PageInfo<BmsOrderProductEntity> pageInfo=bmsOrderProductRepository.query(condition,pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsOrderProductVo> list=new ArrayList<BmsOrderProductVo>();
				for(BmsOrderProductEntity entity:pageInfo.getList()){
					BmsOrderProductVo vo=new BmsOrderProductVo();
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
    public List<BmsOrderProductEntity> query(Map<String, Object> condition){
		return bmsOrderProductRepository.query(condition);
	}
	
}
