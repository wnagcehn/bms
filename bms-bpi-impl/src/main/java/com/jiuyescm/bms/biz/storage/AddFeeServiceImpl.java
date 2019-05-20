package com.jiuyescm.bms.biz.storage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IAddFeeService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.jiuyescm.bms.biz.storage.vo.BizAddFeeVo;

@Service("addFeeService")
public class AddFeeServiceImpl implements IAddFeeService {

    private Logger logger = LoggerFactory.getLogger(AddFeeServiceImpl.class);

    @Autowired
    private IBizAddFeeRepository bizAddFeeRepository;
    @Autowired
    private ISnowflakeSequenceService snowflakeSequenceService;

    @Override
    public Map<String, String> save(List<BizAddFeeVo> listVo) {
        if(CollectionUtils.isEmpty(listVo)){
            return null;
        }
        logger.info("OMS入参：" + listVo.toString());
        List<BizAddFeeEntity> list = new ArrayList<BizAddFeeEntity>();
        for(BizAddFeeVo vo : listVo) {
            BizAddFeeEntity paramEntity = new BizAddFeeEntity();
            try {
                PropertyUtils.copyProperties(paramEntity, vo);
            } catch (Exception ex) {
               logger.error("转换失败");
            }
            list.add(paramEntity);
        }
        
        // 保存list
        List<BizAddFeeEntity> addlist = new ArrayList<>();
        List<FeesReceiveStorageEntity> feelist = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> resultMap = new HashMap<>();
        // 校验
        for (BizAddFeeEntity bizAddFeeEntity : list) {
            String payNo = bizAddFeeEntity.getPayNo();
            if (StringUtils.isEmpty(payNo)) {
                resultMap.put(payNo, "增值费编号为空");
                continue;
            }
            if (null == bizAddFeeEntity.getCreateTime()) {
                resultMap.put(payNo, "时间为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getWarehouseCode())) {
                resultMap.put(payNo, "仓库ID为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getWarehouseName())) {
                resultMap.put(payNo, "仓库名为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getCustomerid())) {
                resultMap.put(payNo, "商家ID为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getCustomerName())) {
                resultMap.put(payNo, "商家名称为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getFeesUnit())) {
                resultMap.put(payNo, "费用单位为空");
                continue;
            }
            if (null == bizAddFeeEntity.getNum()) {
                resultMap.put(payNo, "数量为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getFirstSubject())) {
                resultMap.put(payNo, "增值主科目编码为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getFirstSubjectName())) {
                resultMap.put(payNo, "增值主科目名称为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getFeesType())) {
                resultMap.put(payNo, "费用类型为空");
                continue;
            }
            if (StringUtils.isEmpty(bizAddFeeEntity.getFeesTypeName())) {
                resultMap.put(payNo, "费用类型名称为空");
                continue;
            }
            Double fixedAmount = bizAddFeeEntity.getFixedAmount();
            //如果一口价为空，设置默认值0
            if (null == fixedAmount) {
                fixedAmount = 0.0;
            }
            param.put("payNo", payNo);
            // 校验payNo是否存在
            BizAddFeeEntity checkEntity = bizAddFeeRepository.queryPayNo(param);
            if (null != checkEntity && StringUtils.isEmpty(checkEntity.getPayNo())) {
                // 如果一口价大于0，生成费用
                if (fixedAmount > 0) {
                    FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
                    String feesNo = "STO" + snowflakeSequenceService.nextStringId();
                    bizAddFeeEntity.setFeesNo(feesNo);
                    // 更改业务数据状态
                    bizAddFeeEntity.setIsCalculated("1");
                    fee.setFeesNo(feesNo);
                    //金额
                    fee.setCost(new BigDecimal(fixedAmount));
                    fee.setCalculateTime(JAppContext.currentTimestamp());
                    fee.setUnitPrice(bizAddFeeEntity.getUnitPrice());
                    fee.setSubjectCode("wh_value_add_subject");
                    fee.setOtherSubjectCode(bizAddFeeEntity.getFeesType());
                    fee.setCustomerId(bizAddFeeEntity.getCustomerid());
                    fee.setCustomerName(bizAddFeeEntity.getCustomerName());
                    fee.setWarehouseCode(bizAddFeeEntity.getWarehouseCode());
                    fee.setUnit(bizAddFeeEntity.getFeesUnit());
                    fee.setParam1(bizAddFeeEntity.getItem());
                    fee.setCustomerName(bizAddFeeEntity.getCustomerName());
                    fee.setWarehouseName(bizAddFeeEntity.getWarehouseName());
                    fee.setCreateTime(bizAddFeeEntity.getCreateTime());
                    fee.setCreator("system");
                    fee.setCostType("FEE_TYPE_GENEARL");
                    fee.setDelFlag("0");
                    fee.setStatus("0");
                    fee.setExternalProductNo(bizAddFeeEntity.getExternalNo());
                    // 计算成功
                    fee.setIsCalculated("1");
                    fee.setParam2(new SimpleDateFormat("yyyyMM").format(bizAddFeeEntity.getCreateTime()));
                    feelist.add(fee);
                }else{
                    //不生成费用，业务数据计算状态为0
                    bizAddFeeEntity.setIsCalculated("0");
                }
                addlist.add(bizAddFeeEntity);
            }
            resultMap.put(payNo, "success");
        }
        try {
            if (!CollectionUtils.isEmpty(addlist)) {
                bizAddFeeRepository.omssave(addlist);
                logger.info("oms对接生成"+addlist.size()+"条业务数据");
            }
            if (!CollectionUtils.isEmpty(feelist)) {
                bizAddFeeRepository.feesave(feelist);
                logger.info("通过一口价生成"+feelist.size()+"条费用");
            }
            logger.info("oms对接接口保存成功");
        } catch (Exception e) {
            logger.info("保存失败：" + addlist.toString());
        }
        return resultMap;
    }
}