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
        }
        return null;
    }
}