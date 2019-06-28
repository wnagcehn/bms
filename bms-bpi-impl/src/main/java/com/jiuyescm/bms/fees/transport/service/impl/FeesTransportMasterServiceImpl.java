package com.jiuyescm.bms.fees.transport.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.feeclaim.vo.FeesClaimsVo;
import com.jiuyescm.bms.fees.claim.FeesClaimsEntity;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.repository.IFeesTransportMasterRepository;
import com.jiuyescm.bms.fees.transport.service.IFeesTransportMasterService;
import com.jiuyescm.bms.fees.transport.vo.FeesTransportVo;
import com.jiuyescm.constants.BmsEnums;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("feesTransportMasterService")
public class FeesTransportMasterServiceImpl implements IFeesTransportMasterService {
    
    private static final Logger logger = LoggerFactory.getLogger(FeesTransportMasterServiceImpl.class.getName());

	@Autowired
    private IFeesTransportMasterRepository feesTransportMasterRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public FeesTransportMasterEntity findById(Long id) {
        return feesTransportMasterRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<FeesTransportVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        PageInfo<FeesTransportVo> result=new PageInfo<FeesTransportVo>();

        try {
            PageInfo<FeesTransportMasterEntity> pageInfo=feesTransportMasterRepository.query(condition, pageNo, pageSize);           
            List<FeesTransportVo> voList = new ArrayList<FeesTransportVo>();
            for(FeesTransportMasterEntity entity : pageInfo.getList()) {
//                entity.setActualPackingQty(entity.getActualPackingQty()==null?0d:entity.getActualPackingQty().doubleValue());
                FeesTransportVo vo = new FeesTransportVo();         
                PropertyUtils.copyProperties(vo, entity);          
                voList.add(vo);
            }
            
            PropertyUtils.copyProperties(result, pageInfo); 
            result.setList(voList);
            return result;
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
        
        return result;
    }
    
    @Override
    public PageInfo<FeesTransportVo> queryToExport(Map<String, Object> condition,
            int pageNo, int pageSize) {
        PageInfo<FeesTransportVo> result=new PageInfo<FeesTransportVo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PageInfo<FeesTransportMasterEntity> pageList = feesTransportMasterRepository.query(condition, pageNo, pageSize);
        if (null != pageList && pageList.getList().size() > 0) {
            try {
                List<FeesTransportVo> voList = new ArrayList<FeesTransportVo>();
                for(FeesTransportMasterEntity entity : pageList.getList()) {
                    entity.setCreDate(entity.getCreatedDt()==null?"":sdf.format(entity.getCreatedDt()));
                    entity.setTemperatureTypeCode(BmsEnums.tempretureType.getDesc(entity.getTemperatureTypeCode()));
                    entity.setIsLight(entity.getLight()==null?"":BmsEnums.light.getDesc(entity.getLight()));
                    entity.setIsBacktrack(entity.getHasBacktrack()==null?"":BmsEnums.hasBacktrack.getDesc(entity.getHasBacktrack()));
                    entity.setNeedInsurance(entity.getNeedInsurance()==null?"":BmsEnums.needInsurance.getDesc(entity.getNeedInsurance()));
                    entity.setBeginDate(entity.getBeginTime()==null?"":sdf.format(entity.getBeginTime()));
                    entity.setEndDate(entity.getEndTime()==null?"":sdf.format(entity.getEndTime()));
                    
                    FeesTransportVo vo = new FeesTransportVo();         
                    PropertyUtils.copyProperties(vo, entity);          
                    voList.add(vo);
                }
                
                PropertyUtils.copyProperties(result, pageList); 
                result.setList(voList);
                return result;
            } catch (Exception ex) {
                logger.error("转换失败:{0}",ex);
            }
        }
        return result;
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<FeesTransportMasterEntity> query(Map<String, Object> condition){
		return feesTransportMasterRepository.query(condition);
	}
	
	@Override
	public List<FeesTransportVo> queryForPrepareBill(Map<String, Object> condition){
	    List<FeesTransportVo> result=new LinkedList<FeesTransportVo>();
        try {
            List<FeesTransportMasterEntity> list = feesTransportMasterRepository.queryForPrepareBill(condition);           
            for(FeesTransportMasterEntity entity : list) {
                FeesTransportVo vo = new FeesTransportVo();         
                PropertyUtils.copyProperties(vo, entity);          
                result.add(vo);
            }
            
            return result;
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
        
        return result;
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public FeesTransportMasterEntity save(FeesTransportMasterEntity entity) {
        return feesTransportMasterRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public FeesTransportMasterEntity update(FeesTransportMasterEntity entity) {
        return feesTransportMasterRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        feesTransportMasterRepository.delete(id);
    }
	
}
