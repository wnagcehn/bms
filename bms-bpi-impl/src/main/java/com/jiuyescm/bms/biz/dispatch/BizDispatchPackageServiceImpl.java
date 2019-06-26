package com.jiuyescm.bms.biz.dispatch;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.biz.diapatch.service.IBizDispatchPackageService;
import com.jiuyescm.bms.biz.diapatch.vo.BizDispatchPackageVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchPackageRepository;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.constants.BmsEnums;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizDispatchPackageService")
public class BizDispatchPackageServiceImpl implements IBizDispatchPackageService {
    
    private static final Logger logger = LoggerFactory.getLogger(BizDispatchPackageServiceImpl.class.getName());

	@Autowired
    private IBizDispatchPackageRepository bizDispatchPackageRepository;
	@Autowired
    private ISystemCodeRepository systemCodeRepository;

	private final static Map<String, String> temperature = new HashMap<String, String>();
	static{
	    temperature.put("LD", "冷冻");
	    temperature.put("LC", "冷藏");
	    temperature.put("CW", "常温");
	    temperature.put("HW", "恒温");
	};
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BizDispatchPackageVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        PageInfo<BizDispatchPackageVo> pageVoInfo = new PageInfo<BizDispatchPackageVo>();
        List<BizDispatchPackageVo> result = new ArrayList<>();
        try {
            PageInfo<BizDispatchPackageEntity> pageInfo = bizDispatchPackageRepository.query(condition, pageNo, pageSize);
            PropertyUtils.copyProperties(pageVoInfo, pageInfo);
            List<BizDispatchPackageEntity> list = pageInfo.getList();
            if (null != list && list.size() > 0) {
                for (BizDispatchPackageEntity entity : list) {
                    BizDispatchPackageVo voEntity = new BizDispatchPackageVo();
                    PropertyUtils.copyProperties(voEntity, entity);
                    result.add(voEntity);
                }
            }
        } catch (Exception e) {
            logger.error("查询异常", e);
        }
        pageVoInfo.setList(result);
        return pageVoInfo;
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizDispatchPackageEntity> query(Map<String, Object> condition){
		return bizDispatchPackageRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchPackageEntity save(BizDispatchPackageEntity entity) {
        return bizDispatchPackageRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchPackageEntity update(BizDispatchPackageEntity entity) {
        return bizDispatchPackageRepository.update(entity);
    }
    
    /**
     * 重算
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午10:52:23
     *
     * @param param
     * @return
     */
    @Override
    public int reCalculate(Map<String, Object> param) {
        return bizDispatchPackageRepository.reCalculate(param);
    }
	
    /**
     * 查询需要导出的数据
     * <将Code全部转为Name输出，日期格式化>
     * 
     * @author wangchen870
     * @date 2019年4月16日 上午11:28:41
     *
     * @param condition
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<BizDispatchPackageEntity> queryToExport(Map<String, Object> condition, int pageNo, int pageSize, Map<String, String> transportMap, Map<String, String> boxMap, Map<String, String> timeMap, Map<String, String> operateTypeMap) {
        PageInfo<BizDispatchPackageEntity> pageList = bizDispatchPackageRepository.queryToExport(condition, pageNo, pageSize);
        if (null != pageList && pageList.getList().size() > 0) {
            for (BizDispatchPackageEntity entity : pageList.getList()) {
                entity.setTransportType(transportMap.containsKey(entity.getTransportType())?transportMap.get(entity.getTransportType()):"");
                entity.setPackBoxType(boxMap.containsKey(entity.getPackBoxType())?boxMap.get(entity.getPackBoxType()):"");
                entity.setHoldingTime(timeMap.containsKey(entity.getHoldingTime())?timeMap.get(entity.getHoldingTime()):"");
                entity.setPackOperateType(operateTypeMap.containsKey(entity.getPackOperateType())?operateTypeMap.get(entity.getPackOperateType()):"");
                entity.setTransportTemperatureType(BmsEnums.tempretureType.getDesc(entity.getTransportTemperatureType()));
                entity.setCreTimeExport(entity.getCreTime() == null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getCreTime()));
                entity.setIsCalculated(CalculateState.getDesc(entity.getIsCalculated()));
            }
        }
        return pageList;
    }
    
}
