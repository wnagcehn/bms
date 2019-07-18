package com.jiuyescm.bms.biz.storage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.entity.PubMaterialVo;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IAddFeeService;
import com.jiuyescm.bms.biz.storage.vo.BizAddFeeVo;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.jiuyescm.utils.JsonUtils;

@Service("addFeeService")
public class AddFeeServiceImpl implements IAddFeeService {

    private Logger logger = LoggerFactory.getLogger(AddFeeServiceImpl.class);

    @Autowired
    private IBizAddFeeRepository bizAddFeeRepository;
    @Autowired
    private ISnowflakeSequenceService snowflakeSequenceService;
    @Autowired
    private ISystemCodeRepository systemCodeRepository;  
        
    @Override
    public Map<String, String> save(List<BizAddFeeVo> listVo) {
        if (CollectionUtils.isEmpty(listVo)) {
            logger.info("增值服务单，传入数据为空！！！！");
            return null;
        }
        List<BizAddFeeEntity> list = new ArrayList<BizAddFeeEntity>();
        for (BizAddFeeVo vo : listVo) {
            logger.info("增值服务单，OMS入参：" + JsonUtils.toJson(vo));
            BizAddFeeEntity paramEntity = new BizAddFeeEntity();
            try {
                PropertyUtils.copyProperties(paramEntity, vo);
            } catch (Exception ex) {
               
                logger.error("转换失败", ex);

            }
            list.add(paramEntity);
        }
        
        //从配置中获取不需要计费的费用科目
        List<String> codeList = getNoExeSubject();
        logger.info("不计费的科目有：" + JsonUtils.toJson(codeList));

        // 保存list
        List<BizAddFeeEntity> addlist = new ArrayList<>();
        List<FeesReceiveStorageEntity> feelist = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> resultMap = new HashMap<>();
        // 校验payNo集合
        HashSet<String> payNoSet = new HashSet<>();
        // 校验
        for (BizAddFeeEntity bizAddFeeEntity : list) {
            String payNo = bizAddFeeEntity.getPayNo();
            try {
                if (StringUtils.isEmpty(payNo)) {
                    logger.info("增值服务单，增值费编号为空" + payNo);
                    resultMap.put(payNo, "增值服务单，增值费编号为空");
                    continue;
                }
                if (payNoSet.contains(payNo)) {
                    logger.info("增值服务单，单号已重复" + payNo);
                    continue;
                }
                payNoSet.add(payNo);
                if (null == bizAddFeeEntity.getCreateTime()) {
                    resultMap.put(payNo, "增值服务单，业务时间为空");
                    logger.info("增值服务单，业务时间为空" + payNo);
                    continue;
                }
                if (null == bizAddFeeEntity.getOperationTime()) {
                    resultMap.put(payNo, "增值服务单，操作时间为空");
                    logger.info("增值服务单，操作时间为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getWarehouseCode())) {
                    resultMap.put(payNo, "增值服务单，仓库ID为空");
                    logger.info("增值服务单，仓库ID为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getWarehouseName())) {
                    resultMap.put(payNo, "增值服务单，仓库名为空");
                    logger.info("增值服务单，仓库名为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getCustomerid())) {
                    resultMap.put(payNo, "增值服务单，商家ID为空");
                    logger.info("增值服务单，商家ID为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getCustomerName())) {
                    resultMap.put(payNo, "增值服务单，商家名称为空");
                    logger.info("增值服务单，商家名称为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getFirstSubject())) {
                    resultMap.put(payNo, "增值服务单，增值主科目编码为空");
                    logger.info("增值服务单，增值主科目编码为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getFirstSubjectName())) {
                    logger.info("增值服务单，增值主科目名称为空" + payNo);
                    resultMap.put(payNo, "增值服务单，增值主科目名称为空");
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getFeesType())) {
                    resultMap.put(payNo, "增值服务单，费用类型为空");
                    logger.info("增值服务单，费用类型为空" + payNo);
                    continue;
                }
                if (StringUtils.isEmpty(bizAddFeeEntity.getFeesTypeName())) {
                    resultMap.put(payNo, "增值服务单，费用类型名称为空");
                    logger.info("增值服务单，费用类型名称为空" + payNo);
                    continue;
                }
                
                //一级类型为“杂项销售出库”，二级类型为“耗材使用”的增值单 特殊处理写入耗材出库明细表
                if("100010".equals(bizAddFeeEntity.getFirstSubject()) && "wh_consumablesuse".equals(bizAddFeeEntity.getFeesType())){
                    if(bizAddFeeEntity.getList()==null || bizAddFeeEntity.getList().size()<=0){
                        resultMap.put(payNo, "一级类型为“杂项销售出库”，二级类型为“耗材使用”的增值单 耗材明细必传");
                        logger.info("一级类型为“杂项销售出库”，二级类型为“耗材使用”的增值单 耗材明细必传" + payNo);
                        continue;
                    }                   
                    for(PubMaterialVo vo:bizAddFeeEntity.getList()){
                        if(StringUtils.isEmpty(vo.getMaterialCode())){
                            resultMap.put(payNo, "杂项销售出库，耗材编码为空");
                            logger.info("杂项销售出库，耗材编码为空" + payNo);
                            continue;
                        }
                    }
                    
                }else{
                    if (StringUtils.isEmpty(bizAddFeeEntity.getFeesUnit())) {
                        resultMap.put(payNo, "增值服务单，费用单位为空");
                        logger.info("增值服务单，费用单位为空" + payNo);
                        continue;
                    }
                    if (null == bizAddFeeEntity.getNum()) {
                        logger.info("增值服务单，数量为空" + payNo);
                        resultMap.put(payNo, "增值服务单，数量为空");
                        continue;
                    }                
                    Double fixedAmount = bizAddFeeEntity.getFixedAmount();
                    // 如果一口价为空，设置默认值0
                    if (null == fixedAmount) {
                        fixedAmount = 0.0;
                    }
                    param.put("payNo", payNo);
                    // 校验payNo是否存在
                    BizAddFeeEntity checkEntity = bizAddFeeRepository.queryPayNo(param);
                    if (null == checkEntity) {
                        // 如果费用科目不需要计费
                        logger.info("OMS增值费编号："+ payNo + ", 费用类型：" + bizAddFeeEntity.getFeesType());
                        if (codeList.contains(bizAddFeeEntity.getFeesType())) {
                            FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
                            String feesNo = "STO" + snowflakeSequenceService.nextStringId();
                            bizAddFeeEntity.setFeesNo(feesNo);
                            // 更改业务数据状态
                            bizAddFeeEntity.setIsCalculated("1");
                            fee.setFeesNo(feesNo);
                            // 金额
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
                            fee.setParam2(bizAddFeeEntity.getCreateTime()==null?"":new SimpleDateFormat("yyyyMM").format(bizAddFeeEntity.getCreateTime()));
                            feelist.add(fee);
                        } else {
                            // 不生成费用，业务数据计算状态为0
                            bizAddFeeEntity.setIsCalculated("0");
                        }
                        bizAddFeeEntity.setDelFlag("0");
                        addlist.add(bizAddFeeEntity);
                    } else {
                        logger.info("增值服务单，已存在" + payNo);
                    }
                    resultMap.put(payNo, "SUCCESS");
                }
            } catch (Exception e) {
                logger.error("增值服务单,处理异常：" + payNo, e);
            }
        }
        try {
            if (!CollectionUtils.isEmpty(addlist)) {
                logger.info("增值服务单,oms对接生成" + addlist.size() + "条业务数据");
                bizAddFeeRepository.omssave(addlist);

            }
            if (!CollectionUtils.isEmpty(feelist)) {
                logger.info("增值服务单,通过一口价生成" + feelist.size() + "条费用");
                bizAddFeeRepository.feesave(feelist);

            }
            logger.info("增值服务单,oms对接接口保存成功");
        } catch (Exception e) {
            logger.error("增值服务单,保存失败：", e);
        }
        return resultMap;
    }

    /*
     * 获取不需要计费的费用科目
     */
    private List<String> getNoExeSubject() {
        List<String> codeList = new ArrayList<String>();
        List<SystemCodeEntity> systemCodeList = systemCodeRepository.findEnumList("NO_CALCULATE_STORAGE_ADD");    
        for (SystemCodeEntity systemCodeEntity : systemCodeList) {
            if (null == systemCodeEntity.getCode()) {
                continue;
            }
            codeList.add(systemCodeEntity.getCode());
        }
        return codeList;
    }
}