package com.jiuyescm.bms.correct.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.service.IBmsMarkingProductsService;
import com.jiuyescm.bms.correct.service.IBmsOrderProductService;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;
import com.jiuyescm.bms.correct.vo.BmsOrderProductVo;

/**
 * ..Controller
 * 
 * @author wangchen
 * 
 */
@Controller("bmsOrderProductController")
public class BmsOrderProductController {

    @Autowired
    private IBmsOrderProductService bmsOrderProductService;
    @Autowired
    private IBmsMarkingProductsService bmsMarkingProductsService;

    /**
     * 分页查询
     * 
     * @param page
     * @param param
     * @throws Exception
     */
    @DataProvider
    public void query(Page<BmsOrderProductVo> page, Map<String, Object> param) throws Exception {
        String waybillNo = (String) param.get("waybillNo");
        //使用运单号查找
        if (StringUtils.isNotBlank(waybillNo)) {
            //查询运单号的products_mark
            PageInfo<BmsMarkingProductsVo> pageInfo = bmsMarkingProductsService.query(param, 1, 20);
            List<BmsMarkingProductsVo> list = pageInfo.getList();
            if(!CollectionUtils.isEmpty(list)){
                param.put("waybillNo", "");
                param.put("productMark", list.get(0).getProductsMark());
            }
            
        }
        PageInfo<BmsOrderProductVo> pageInfo = bmsOrderProductService
                .query(param, page.getPageNo(), page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }

}
