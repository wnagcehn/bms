package com.jiuyescm.bms.billcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;

@Service("billCheckLogService")
public class BillCheckLogServiceImp implements IBillCheckLogService {

    private static final Logger logger = Logger.getLogger(BillCheckLogServiceImp.class.getName());

    @Resource
    private IBillCheckLogRepository billCheckLogRepository;
    @Autowired
    private IBillCheckInfoService billCheckInfoService;

    @Override
    public PageInfo<BillCheckLogVo> query(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stu
        PageInfo<BillCheckLogEntity> pageInfo = billCheckLogRepository.query(condition, pageNo, pageSize);
        PageInfo<BillCheckLogVo> result = new PageInfo<BillCheckLogVo>();

        List<BillCheckLogVo> voList = new ArrayList<BillCheckLogVo>();
        for (BillCheckLogEntity entity : pageInfo.getList()) {
            BillCheckLogVo vo = new BillCheckLogVo();
            try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败", ex);
            }
            voList.add(vo);
        }

        result.setList(voList);
        return result;
    }

    @Override
    public int addBillCheckLog(BillCheckLogVo logVo) throws Exception {
        try {
            // 保存CRM
//            BillCheckInfoEntity checkEntity = new BillCheckInfoEntity();
//            checkEntity.setId(logVo.getBillCheckId());
//            billCheckInfoService.saveCrm(checkEntity);
            
            BillCheckLogEntity entity = new BillCheckLogEntity();
            PropertyUtils.copyProperties(entity, logVo);
            
            return billCheckLogRepository.addCheckLog(entity);
        } catch (Exception e) {
            throw e;
        }
    }
}
