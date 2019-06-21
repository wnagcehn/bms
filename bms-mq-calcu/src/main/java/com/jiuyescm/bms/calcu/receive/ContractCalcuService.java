package com.jiuyescm.bms.calcu.receive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calculate.vo.CalcuBaseInfoVo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;

/**
 * 合同在线计算服务
 * 
 * @author caojianwei
 *
 */
@Component("contractCalcuService")
public class ContractCalcuService {

    private Logger logger = LoggerFactory.getLogger(ContractCalcuService.class);

    @Autowired
    private IContractQuoteInfoService contractQuoteInfoService;
    @Autowired
    private IReceiveRuleRepository receiveRuleRepository;
    @Autowired
    private IFeesCalcuService feesCalcuService;

    public void calcuForContract(Object entity, Object fee, BmsCalcuTaskVo vo, Map<String, Object> errorMap,
            ContractQuoteQueryInfoVo queryVo, CalcuBaseInfoVo cbiVo, String feesNo) {
        logger.info("taskId={} 合同在线查询参数：{}", vo.getTaskId(), JSONObject.fromObject(queryVo));
        queryCtForContract(vo, entity, fee, queryVo, errorMap, cbiVo, feesNo);
    }

    private void queryCtForContract(BmsCalcuTaskVo vo, Object entity, Object fee, ContractQuoteQueryInfoVo queryVo,
            Map<String, Object> errorMap, CalcuBaseInfoVo cbiVo, String feesNo) {
        ContractQuoteInfoVo cqVo = null;
        try {
            cqVo = contractQuoteInfoService.queryUniqueColumns(queryVo);
            logger.info("taskId={} 费用编号={} 查询出的合同在线结果【{}】", vo.getTaskId(), feesNo, JSONObject.fromObject(cqVo));

        } catch (BizException ex) {
            logger.info("taskId={} 费用编号={} 合同在线合同缺失 {}", vo.getTaskId(), feesNo, ex.getMessage());
            errorMap.put("success", "fail");
            errorMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
            errorMap.put("msg", ex.getMessage());
            errorMap.put("code", ex.getCode());
            //对返回的错误特殊处理
            hand(vo,entity,fee,errorMap);
            return;
        }
        if (cqVo == null) {
            return;
        }

        Map<String, String> contractMap = new HashMap<>();
        contractMap.put("contractNo", cqVo.getQuoteVo().getContractNo());// 合同编号
        contractMap.put("serviceOrderNo", cqVo.getQuoteVo().getServiceOrderNo());// 订购号
        contractMap.put("version", cqVo.getQuoteVo().getVersion());// 合同版本
        contractMap.put("ruleCode", cqVo.getQuoteVo().getRuleCode());// 规则编号

        logger.info("taskId={} contractInfo={}", vo.getTaskId(), contractMap);
        if (StringUtil.isEmpty(cqVo.getRuleCode())) {
            logger.info("taskId={} 费用编号={} 合同在线规则未绑定", vo.getTaskId(), feesNo);
            errorMap.put("success", "fail");
            errorMap.put("is_calculated", CalculateState.Quote_Miss.getCode());
            errorMap.put("msg", "合同在线规则未绑定");
            return;
        }
        try {
            Map<String, Object> con = new HashMap<>();
            con.put("quotationNo", cqVo.getRuleCode());
            BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
            if (null == ruleEntity) {
                String msg = "规则【" + cqVo.getRuleCode() + "】不存在";
                logger.info("taskId={} {}", vo.getTaskId(), msg);
                errorMap.put("success", "fail");
                errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
                errorMap.put("msg", msg);
                return;
            }
            // 获取合同在线查询条件
            Map<String, Object> cond = new HashMap<String, Object>();
            feesCalcuService.ContractCalcuService(entity, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
            logger.info("taskId={} 费用编号={} 获取合同在线报价参数：{}", vo.getTaskId(), feesNo, cond);
            ContractQuoteInfoVo rtnQuoteInfoVo = null;
            try {
                if (cond == null || cond.size() == 0) {
                    logger.info("taskId={} 费用编号={} 规则引擎拼接条件异常", vo.getTaskId(), feesNo);
                    errorMap.put("success", "fail");
                    errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
                    errorMap.put("msg", "规则引擎拼接条件异常");
                    return;
                }
                logger.info("taskId={} 费用编号={} 查询报价报价参数{}", vo.getTaskId(), feesNo, JSONObject.fromObject(cqVo));
                rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(cqVo, cond);
            } catch (BizException e) {
                String msg = "获取合同在线报价异常:" + e.getMessage();
                logger.info("taskId={}  费用编号={} 获取合同在线报价异常{}", vo.getTaskId(), feesNo, msg);
                errorMap.put("success", "fail");
                errorMap.put("is_calculated", CalculateState.Quote_Miss.getCode());
                errorMap.put("msg", msg);
                return;
            }

            logger.info("taskId={} 费用编号={} 获取合同在线报价结果{}", vo.getTaskId(), feesNo, JSONObject.fromObject(rtnQuoteInfoVo));

            if (null == rtnQuoteInfoVo || null == rtnQuoteInfoVo.getQuoteMaps()
                    || 0 == rtnQuoteInfoVo.getQuoteMaps().size()) {
                logger.info("taskId={} 费用编号={} 合同在线报价缺失 ", vo.getTaskId(), feesNo);
                errorMap.put("success", "fail");
                errorMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
                errorMap.put("msg", "合同在线报价缺失");
                return;
            }

            if (rtnQuoteInfoVo.getQuoteMaps().size() > 1) {
            
                //非耗材和非配送
                if(!"wh_material_use".equals(vo.getSubjectCode()) && !"de_delivery_amount".equals(vo.getSubjectCode()) && !"wh_stand_material_use".equals(vo.getSubjectCode())){
                    logger.info("taskId={} 费用编号={} 合同在线匹配多条报价，不进行计算，系统错误 ", vo.getTaskId(), feesNo);
                    errorMap.put("success", "fail");
                    errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
                    errorMap.put("msg", "合同在线匹配多条报价");
                    return;
                }              
                //如果是配送，要做地址筛选
                if("de_delivery_amount".equals(vo.getSubjectCode())){
                    List<Map<String, String>> priceList=QuoteFilter(rtnQuoteInfoVo,fee);
                    if(priceList.size()==0){
                        logger.info("taskId={} 费用编号={} 合同在线报价缺失 ", vo.getTaskId(), feesNo);
                        errorMap.put("success", "fail");
                        errorMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
                        errorMap.put("msg", "合同在线报价缺失");
                        return;
                    }else if(priceList.size()>1){
                        logger.info("taskId={} 费用编号={} 合同在线匹配多条报价，不进行计算，系统错误 ", vo.getTaskId(), feesNo);
                        errorMap.put("success", "fail");
                        errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
                        errorMap.put("msg", "合同在线匹配多条报价");
                        return;
                    }else{
                        rtnQuoteInfoVo.setQuoteMaps(priceList);
                    }
                }
            }

            // 调用规则计算费用
            feesCalcuService.ContractCalcuService(fee, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(),
                    ruleEntity.getQuotationNo());
            errorMap.put("success", "succ");
        } catch (Exception ex) {
            logger.info("taskId={} 费用编号={} 系统异常{}", vo.getTaskId(), feesNo, ex);
            errorMap.put("success", "fail");
            errorMap.put("is_calculated", CalculateState.Sys_Error.getCode());
            errorMap.put("msg", ex.getMessage());
            return;
        }
    }
    
    
    private List<Map<String, String>> QuoteFilter(ContractQuoteInfoVo rtnQuoteInfoVo,Object fee){
        List<Map<String, String>> newList=new ArrayList<Map<String, String>>();
        
        //费用
        FeesReceiveDispatchEntity feeEntity=(FeesReceiveDispatchEntity) fee;
        //报价
        List<Map<String, String>> quoteMaps=rtnQuoteInfoVo.getQuoteMaps();
        //收件省市的对应
        Map<String,String> settingMap=rtnQuoteInfoVo.getSettingMap();
        
        if(settingMap==null){
            return newList;
        }
        
        TreeMap<Integer,List<Map<String, String>>> levelMap=new TreeMap<Integer,List<Map<String, String>>>();
                
        //业务数据市
        String toProvinceName = StringUtil.isEmpty(feeEntity.getToProvinceName())?"":feeEntity.getToProvinceName();
        //业务数据市
        String toCityName = StringUtil.isEmpty(feeEntity.getToCityName())?"":feeEntity.getToCityName();
        //业务数据区
        String toDistrictName = StringUtil.isEmpty(feeEntity.getToDistrictName())?"":feeEntity.getToDistrictName();
              
        for(Map<String,String> priceMap:quoteMaps){
            int level = 0;
            //报价中的省
            String province_quote = StringUtil.isEmpty(priceMap.get(settingMap.get("receiveProvince")))?"":priceMap.get(settingMap.get("receiveProvince"));
            //报价中的市
            String city_quote = StringUtil.isEmpty(priceMap.get(settingMap.get("receiveCity")))?"":priceMap.get(settingMap.get("receiveCity"));
            //报价中的区
            String district_quote = StringUtil.isEmpty(priceMap.get(settingMap.get("receiveDistrict")))?"":priceMap.get(settingMap.get("receiveDistrict"));
           
            
            if(!toDistrictName.equals(district_quote) && StringUtils.isNotEmpty(district_quote)){
                continue;//区不匹配
            }
            if(!toCityName.equals(city_quote) && StringUtils.isNotEmpty(city_quote)){
                continue;//市不匹配
            }
            if(!toProvinceName.equals(province_quote) && StringUtils.isNotEmpty(province_quote)){
                continue;//省不匹配
            }
            
            
            level+= toDistrictName.equals(district_quote)?1:-1; //区优先级
            level+= toCityName.equals(city_quote)?1:-1;     //市优先级
            level+= toProvinceName.equals(province_quote)?1:-1;     //市优先级

            if (levelMap.containsKey(level)) {
                levelMap.get(level).add(priceMap);
            }else {
                List<Map<String, String>> valueList=new ArrayList<>();
                valueList.add(priceMap);
                levelMap.put(level, valueList);
            }  
        }

        if(levelMap.size()>0 && levelMap.get(levelMap.lastKey())!=null){
            newList = levelMap.get(levelMap.lastKey());
        }
             
        return newList;
    }

    private void hand(BmsCalcuTaskVo vo, Object entity,Object fee,Map<String, Object> errorMap){
        if(errorMap.get("code")!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //如果是配送
            if("de_delivery_amount".equals(vo.getSubjectCode())){
                FeesReceiveDispatchEntity dispatch=(FeesReceiveDispatchEntity) fee;
                if("CONTRACT_LIST_NULL".equals(errorMap.get("code"))){                   
                    //合同不存在
                    errorMap.put("msg", "商家【"+vo.getCustomerName()+"】在【"+sdf.format(dispatch.getCreateTime())+"】未签订任何合同且该商家未有关联合同商家!");
                }else if("CONTRACT_CONFIG_NULL".equals(errorMap.get("code")) || "CONTRACT_SUBJECT_NULL".equals(errorMap.get("code"))){
                    //服务未配置 或者未订购科目
                    errorMap.put("msg", "商家【"+vo.getCustomerName()+"】物流商【"+dispatch.getCarrierName()+"】未订购科目【配送费】的服务项!");
                    errorMap.put("is_calculated", CalculateState.No_Dinggou.getCode());               
                 }
            }else{ //仓储
                FeesReceiveStorageEntity storage=(FeesReceiveStorageEntity) fee;
                if("CONTRACT_LIST_NULL".equals(errorMap.get("code"))){                   
                    //合同不存在
                    errorMap.put("msg", "商家【"+vo.getCustomerName()+"】在【"+sdf.format(storage.getCreateTime())+"】未签订任何合同且该商家未有关联合同商家!");
                }
          
                if("CONTRACT_SUBJECT_NULL".equals(errorMap.get("code"))){
                    errorMap.put("is_calculated", CalculateState.No_Dinggou.getCode());
                    //未订购科目
                    if("wh_value_add_subject".equals(vo.getSubjectCode())){
                        BizAddFeeEntity add=(BizAddFeeEntity) entity;
                        errorMap.put("msg", "商家【"+vo.getCustomerName()+"】未订购科目【"+add.getFeesTypeName()+"】的服务项!");
                    }else{
                        errorMap.put("msg", "商家【"+vo.getCustomerName()+"】未订购科目【"+vo.getSubjectName()+"】的服务项!");
                    }
                }
            }
        }
    }
    
}
