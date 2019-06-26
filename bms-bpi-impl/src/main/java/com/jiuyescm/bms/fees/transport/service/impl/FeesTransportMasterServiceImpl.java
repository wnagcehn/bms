package com.jiuyescm.bms.fees.transport.service.impl;

import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.repository.IFeesTransportMasterRepository;
import com.jiuyescm.bms.fees.transport.service.IFeesTransportMasterService;
import com.jiuyescm.constants.BmsEnums;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("feesTransportMasterService")
public class FeesTransportMasterServiceImpl implements IFeesTransportMasterService {

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
    public PageInfo<FeesTransportMasterEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesTransportMasterRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public PageInfo<FeesTransportMasterEntity> queryToExport(Map<String, Object> condition,
            int pageNo, int pageSize) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PageInfo<FeesTransportMasterEntity> pageList = feesTransportMasterRepository.query(condition, pageNo, pageSize);
        if (null != pageList && pageList.getList().size() > 0) {
            for (FeesTransportMasterEntity entity : pageList.getList()) {
                entity.setCreDate(entity.getCreatedDt()==null?"":sdf.format(entity.getCreatedDt()));
                entity.setTemperatureTypeCode(BmsEnums.tempretureType.getDesc(entity.getTemperatureTypeCode()));
                entity.setIsLight(entity.getLight()==null?"":BmsEnums.light.getDesc(entity.getLight()));
                entity.setIsBacktrack(entity.getHasBacktrack()==null?"":BmsEnums.hasBacktrack.getDesc(entity.getHasBacktrack()));
                entity.setNeedInsurance(entity.getNeedInsurance()==null?"":BmsEnums.needInsurance.getDesc(entity.getNeedInsurance()));
                entity.setBeginDate(entity.getBeginTime()==null?"":sdf.format(entity.getBeginTime()));
                entity.setEndDate(entity.getEndTime()==null?"":sdf.format(entity.getEndTime()));
            }
        }
        return pageList;
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
	public List<FeesTransportMasterEntity> queryForPrepareBill(Map<String, Object> condition){
	    return feesTransportMasterRepository.queryForPrepareBill(condition);
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
