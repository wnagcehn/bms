/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.billcheck.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataResolver;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckLogService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;

/**
  * 收款调整
  * 
  * @author wangchen
  * @date 2019年7月3日 上午10:19:26
  */
@Controller("billCheckAdjustInfoController")
public class BillCheckAdjustInfoController {

    private static final Logger logger = Logger.getLogger(BillCheckAdjustInfoController.class.getName());
    
    @Autowired
    private IBillCheckInfoService billCheckInfoService;
    @Autowired
    private IBillCheckLogService billCheckLogService;
    @Autowired
    private IBmsGroupUserService bmsGroupUserService;
    
    /*
     * 更新回款调整
     */
    @DataResolver
    public String update(Collection<BillCheckAdjustInfoVo> datas) {
        if(Session.isMissing()){
            return "长时间未操作，用户已失效，请重新登录再试！";
        }
        try {
            for(BillCheckAdjustInfoVo adjustVo:datas){
                adjustVo.setLastModifier(ContextHolder.getLoginUser().getCname());
                adjustVo.setLastModifierId(ContextHolder.getLoginUserName());
                adjustVo.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
                int result=billCheckInfoService.updateAdjustInfo(adjustVo);
                if(result<=0){
                    return "更新回款调整失败";
                }

                //更新账单跟踪
                updateBillcheckInfo(adjustVo, "修改");
                return "更新回款调整成功";
            } 
            return "数据库操作成功";
        } catch (Exception e) {
            logger.error("数据库操作失败",e);
            return "数据库操作失败";
        }
        
    }
    
    /*
     * 更新主账单金额和状态，写入日志表
     */
    private void updateBillcheckInfo(BillCheckAdjustInfoVo adjustVo, String status) {
        
        // 统计金额判断状态
        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", adjustVo.getBillCheckId());
            List<BillCheckInfoVo> blist = billCheckInfoService.queryList(condition);
            if (blist.size() > 0) {
                BillCheckInfoVo checkVo = blist.get(0);
                // 总回款金额
                BigDecimal totalReceiptAmount = BigDecimal.ZERO;
                // 账单应收总额
                BigDecimal checkReceiptAmount = BigDecimal.ZERO;
                // 调整金额
                BigDecimal adjustAmount = BigDecimal.ZERO;
                // 未作废为修改，作废为删除
                if ("0".equals(adjustVo.getDelFlag())) {
                    adjustAmount = adjustVo.getAdjustAmount();
                }
                
                // 总回款金额
                if (checkVo.getReceiptAmount() != null) {
                    totalReceiptAmount = totalReceiptAmount.add(checkVo.getReceiptAmount());
                }

                // 确认金额+调整金额=账单应收总额
                if (checkVo.getConfirmAmount() != null) {
                    checkReceiptAmount = checkReceiptAmount.add(checkVo.getConfirmAmount());
                    checkReceiptAmount = checkReceiptAmount.add(adjustAmount);
                }

                if (totalReceiptAmount.compareTo(checkReceiptAmount) == 0) {
                    checkVo.setReceiptAmount(totalReceiptAmount);
                    // 回款状态
                    checkVo.setReceiptStatus(BillCheckReceiptStateEnum.RECEIPTED.getCode());
                    // 账单状态（已回款）
                    checkVo.setBillStatus(CheckBillStatusEnum.RECEIPTED.getCode());
                } else if (totalReceiptAmount.compareTo(BigDecimal.ZERO) == 0) {
                    // 回款状态
                    checkVo.setReceiptStatus(BillCheckReceiptStateEnum.UN_RECEIPT.getCode());
                    // 账单状态（待收款）
                    // 如果是待确认状态下有回款金额，状态还是待确认
                    if (!CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(checkVo.getBillStatus())) {
                        checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
                    }
                } else {
                    checkVo.setReceiptAmount(totalReceiptAmount);
                    // 部分回款
                    checkVo.setReceiptStatus(BillCheckReceiptStateEnum.PART_RECEIPT.getCode());
                    // 账单状态（待回款）
                    // 如果是待确认状态下有回款金额，状态还是待确认
                    if (!CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(checkVo.getBillStatus())) {
                        checkVo.setBillStatus(CheckBillStatusEnum.TB_RECEIPT.getCode());
                    }
                }

                // △未收款金额=应收款总额-总回款金额
                checkVo.setUnReceiptAmount(checkReceiptAmount.subtract(totalReceiptAmount));
                // △开票未回款金额=发票金额+调整金额-总回款金额
                BigDecimal invoiceMoney = new BigDecimal(0);
                if (checkVo.getInvoiceAmount() != null) {
                    invoiceMoney = checkVo.getInvoiceAmount();
                }
                checkVo.setInvoiceUnReceiptAmount(invoiceMoney.subtract(totalReceiptAmount).add(adjustAmount));
                billCheckInfoService.update(checkVo);
                    
                // 新增日志
                String groupName = bmsGroupUserService.checkExistGroupName(JAppContext.currentUserID());
                try {
                    BillCheckLogVo billCheckLogVo = new BillCheckLogVo();
                    billCheckLogVo.setBillCheckId(checkVo.getId());
                    billCheckLogVo.setBillStatusCode(checkVo.getBillStatus());
                    billCheckLogVo.setOperateDesc("回款调整" + status);      
                    billCheckLogVo.setLogType(0);
                    billCheckLogVo.setCreator(ContextHolder.getLoginUser().getCname());
                    billCheckLogVo.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    billCheckLogVo.setCreatorId(ContextHolder.getLoginUserName());
                    billCheckLogVo.setDeptName(groupName);
                    billCheckLogVo.setDelFlag("0");
                    billCheckLogService.addBillCheckLog(billCheckLogVo);
                } catch (Exception e) {
                    logger.error("日志保存异常:", e);
                }
            }
        } catch (Exception e) {
            logger.error("更新账单异常：", e);
        } 
    }

    /*
     * 删除回款调整
     */
    @DataResolver
    public String delete(BillCheckAdjustInfoVo adjustVo) {
        try {
            adjustVo.setLastModifier(ContextHolder.getLoginUser().getCname());
            adjustVo.setLastModifierId(ContextHolder.getLoginUserName());
            adjustVo.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
            adjustVo.setDelFlag("1");
            int result=billCheckInfoService.updateAdjustInfo(adjustVo);
            if(result<=0){
                return "删除回款调整失败";
            }
            
            //更新账单跟踪
            updateBillcheckInfo(adjustVo, "删除");
            return "删除回款调整成功";
        } catch (Exception e) {
            logger.error("数据库操作失败",e);
            return "数据库操作失败";
        }
    }
    
    /*
     * 删除回款调整
     */
    @DataResolver
    public String save(BillCheckAdjustInfoVo adjustVo) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCheckId", adjustVo.getBillCheckId());
        List<BillCheckAdjustInfoVo> adjustlist = billCheckInfoService.queryAdjust(param);
        
        // 回款调整只能存在一条
        if (adjustlist.size() > 0) {
            return "该账单已存在回款调整记录，不可重复调整！";
        }
        
        try {
            adjustVo.setDelFlag("0");
            adjustVo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            adjustVo.setCreator(ContextHolder.getLoginUser().getCname());
            adjustVo.setCreatorId(ContextHolder.getLoginUserName());
            int result = billCheckInfoService.saveAjust(adjustVo);
            if(result<=0){
                return "新增回款调整失败";
            }
            
            //更新账单跟踪
            updateBillcheckInfo(adjustVo, "新增");
            return "新增回款调整成功";     
        } catch (Exception e) {
            logger.error("数据库操作失败",e);
            return "数据库操作失败";
        }

    }
    
}


