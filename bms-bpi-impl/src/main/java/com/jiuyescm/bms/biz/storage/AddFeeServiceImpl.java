package com.jiuyescm.bms.biz.storage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IAddFeeService;

@Service("addFeeService")
public class AddFeeServiceImpl implements IAddFeeService {

	private Logger logger = LoggerFactory.getLogger(AddFeeServiceImpl.class);
	
    @Autowired
    private IBizAddFeeRepository bizAddFeeRepository;

    @Override
    public String save(List<BizAddFeeEntity> list) {
        //保存list
        List<BizAddFeeEntity> addlist = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        //校验
        for (BizAddFeeEntity bizAddFeeEntity : list) {
            String payNo =bizAddFeeEntity.getPayNo();
            if(StringUtils.isEmpty(payNo)){
                continue;
            }
            if(null==bizAddFeeEntity.getCreateTime()){
                result.append(payNo+"：时间为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getWarehouseCode())){
                result.append(payNo+"：仓库ID为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getWarehouseName())){
                result.append(payNo+"：仓库名为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getCustomerid())){
                result.append(payNo+"：商家ID为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getCustomerName())){
                result.append(payNo+"：商家名称为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getFeesUnit())){
                result.append(payNo+"：费用单位为空;");
                continue;
            }
            if(null==bizAddFeeEntity.getNum()){
                result.append(payNo+"：数量为空;");
                continue;
            }
            if(null==bizAddFeeEntity.getFixedAmount()){
                result.append(payNo+"：一口价为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getFirstSubject())){
                result.append(payNo+"：增值主科目编码为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getFirstSubjectName())){
                result.append(payNo+"：增值主科目名称为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getFeesType())){
                result.append(payNo+"：费用类型为空;");
                continue;
            }
            if(StringUtils.isEmpty(bizAddFeeEntity.getFeesTypeName())){
                result.append(payNo+"：费用类型名称为空;");
                continue;
            }
            addlist.add(bizAddFeeEntity);
        }
        try {
            bizAddFeeRepository.omssave(addlist);
            logger.info("oms对接接口保存成功");
        } catch (Exception e) {
            logger.info("保存失败："+addlist.toString());
        }
        String resultString = result.toString();
        if(StringUtils.isEmpty(resultString)){
            return "保存成功";
        }else{
            return resultString;
        }
    }
}