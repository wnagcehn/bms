package com.jiuyescm.bms.billcheck.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.appconfig.TenantConfig;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;
import com.jiuyescm.bms.bill.customer.BillPeriodInfoEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillPeriodInfoRepository;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRecordRepository;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;
import com.jiuyescm.bms.billcheck.BillCheckAdjustInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.BillReceiptFollowEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.crm.module.api.IModuleDataOpenService;
import com.jiuyescm.crm.module.vo.FieldDataOpenVO;
import com.jiuyescm.crm.module.vo.ModuleDataOpenVO;
import com.jiuyescm.exception.BizException;

@Service("billCheckInfoService")
public class BillCheckInfoServiceImp implements IBillCheckInfoService {

    private static final Logger logger = Logger.getLogger(BillCheckInfoServiceImp.class.getName());

    @Resource
    private IBillCheckInfoRepository billCheckInfoRepository;
    @Resource
    private IBillCheckLogRepository billCheckLogRepository;
    @Resource
    private IBillReceiveMasterRepository billReceiveMasterRepository;
    @Resource
    private IBillReceiveMasterRecordRepository billReceiveMasterRecordRepository;
    @Autowired
    private IModuleDataOpenService moduleDataOpenService;
    @Autowired
    private IBillPeriodInfoRepository billPeriodInfoRepository;
    
    @Autowired
    private TenantConfig tenantConfig;
    
    private static final String BILL_DAY = "BILL_DAY";
    private static final String BILL_DAY_NEXT_MONTH = "BILL_DAY_NEXT_MONTH";
    private static final String INVOICE_DAY = "INVOICE_DAY";
    private static final String INVOICE_DAY_NEXT_MONTH = "INVOICE_DAY_NEXT_MONTH";
    private static final String yyyyMMdd = "yyyy-MM-dd";
    private static SimpleDateFormat sdf= new SimpleDateFormat(yyyyMMdd);
    
    @Override
    public PageInfo<BillCheckInfoVo> query(Map<String, Object> condition, int pageNo, int pageSize) {
        try {

            //获取当前日期的long值
            Timestamp tt = new Timestamp(System.currentTimeMillis());
            Date d = new Date();
            d = tt;
            String time = sdf.format(d) + " 00:00:00";
            long current = sdf.parse(time).getTime();

            String overStatus = "";
            if (condition != null && condition.get("overStatus") != null) {
                overStatus = condition.get("overStatus").toString();
            }

            PageInfo<BillCheckInfoEntity> pageInfo = new PageInfo<BillCheckInfoEntity>();
            if (condition != null && condition.get("invoiceNo") != null && !"".equals(condition.get("invoiceNo"))) {
                // 发票号不为空时，需要根据发票号去查询子票再查询主表
                pageInfo = billCheckInfoRepository.queryByInvoiceNo(condition, pageNo, pageSize);

            } else if (condition != null && condition.get("followType") != null
                    && !"".equals(condition.get("followType"))) {
                // 跟进类型不为空时，需要根据跟进类型去查询子表再查询主表
                pageInfo = billCheckInfoRepository.queryByFollowType(condition, pageNo, pageSize);
            } else {
                pageInfo = billCheckInfoRepository.query(condition, pageNo, pageSize);
            }

            PageInfo<BillCheckInfoVo> result = new PageInfo<BillCheckInfoVo>();

            PropertyUtils.copyProperties(result, pageInfo);

            List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
            // 确认金额总计
            BigDecimal totalConfirmAmount = new BigDecimal(0);
            // 发票金额总计
            BigDecimal totalInvoiceAmount = new BigDecimal(0);
            // 未收款金额
            BigDecimal totalUnReceiptAmount = new BigDecimal(0);
            // 开票未回款金额
            BigDecimal totalInvoiceUnReceiptAmount = new BigDecimal(0);
            // 已确认未开票金额
            BigDecimal totalConfirmUnInvoiceAmount = new BigDecimal(0);
            // 收款金额
            BigDecimal totalReceiptAmount = new BigDecimal(0);

            for (BillCheckInfoEntity entity : pageInfo.getList()) {
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                // 统计金额
                // 确认金额
                totalConfirmAmount = totalConfirmAmount.add(vo.getConfirmAmount());
                // 发票金额
                totalInvoiceAmount = totalInvoiceAmount.add(vo.getInvoiceAmount());
                // 未收款金额
                totalUnReceiptAmount = totalUnReceiptAmount.add(vo.getUnReceiptAmount());
                // 开票未回款金额
                totalInvoiceUnReceiptAmount = totalInvoiceUnReceiptAmount.add(vo.getInvoiceUnReceiptAmount());
                // 已确认未回款金额
                totalConfirmUnInvoiceAmount = totalConfirmUnInvoiceAmount.add(vo.getConfirmUnInvoiceAmount());
                // 收款金额
                totalReceiptAmount = totalReceiptAmount.add(vo.getReceiptAmount());
                voList.add(vo);
            }

            for (BillCheckInfoVo entity : voList) {
                entity.setTotalConfirmAmount(totalConfirmAmount);
                entity.setTotalInvoiceAmount(totalInvoiceAmount);
                entity.setTotalUnReceiptAmount(totalUnReceiptAmount);
                entity.setTotalInvoiceUnReceiptAmount(totalInvoiceUnReceiptAmount);
                entity.setTotalConfirmUnInvoiceAmount(totalConfirmUnInvoiceAmount);
                entity.setTotalReceiptAmount(totalReceiptAmount);
                BigDecimal m = new BigDecimal(0);
                entity.setAdjustMoney(m);
                // 查询调整金额
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("billCheckId", entity.getId());
                BillCheckAdjustInfoEntity adjustEntity = billCheckInfoRepository.queryOneAdjust(param);
                if (adjustEntity != null) {
                    entity.setAdjustMoney(adjustEntity.getAdjustAmount() == null ? m : adjustEntity.getAdjustAmount());
                }
                
                // 判断超期状态（新）
                // 从商家账期表中获取，分为四种情况
                // 1.账单日（业务月份的最后一天）取后一天
                // 2.账单日次月初（业务月份的最后一天）取下个月第一天
                // 3.开票日（判断是否需要开票和开票日期是否为空）取后一天
                // 4.开票日次月初（判断是否需要开票和开票日期是否为空）取下个月第一天
                // 5.加上月数和天数后的日期减去当前日期
                // 6.小于等于0为超期， 大于0小于20为临期，大于等于20为正常
                if (entity.getUnReceiptAmount() != null && entity.getUnReceiptAmount().compareTo(new BigDecimal(1)) > 0) {    
                    String res = "";
                    try {
                        res = isOverdue(current, entity);
                    } catch (Exception e) {
                        logger.error("判断是否超期异常", e);
                    }
                    entity.setOverStatus(res);
                }

                // 设置预分配金额和待催款金额
                if (getPreMoney().containsKey(
                        entity.getInvoiceName() + "&" + entity.getCreateMonth() + "&" + entity.getBillName())) {
                    BillCheckInfoEntity amountVo = getPreMoney().get(
                            (entity.getInvoiceName() + "&" + entity.getCreateMonth() + "&" + entity.getBillName()));
                    entity.setPreDistibutionAmount(amountVo.getPreDistibutionAmount());
                    entity.setTbDunAmount(amountVo.getTbDunAmount());
                }
            }
            List<BillCheckInfoVo> resultList = new ArrayList<BillCheckInfoVo>();
            if (StringUtils.isNotBlank(overStatus)) {
                for (BillCheckInfoVo entity : voList) {
                    if (overStatus.equals(entity.getOverStatus())) {
                        resultList.add(entity);
                    }
                }
                result.setList(resultList);
                return result;
            }

            result.setList(voList);
            return result;
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);         
        }
        return null;
    }
    
    /**
     * 判断是否超期
     * 
     * @author wangchen870
     * @date 2019年7月1日 下午2:41:08
     *
     * @param current  当前时间
     * @param entity   账单实体
     * @return
     */
    private String isOverdue(long current, BillCheckInfoVo entity) {
        BillPeriodInfoEntity periodInfo = getForPeriodInfo(entity);
        if (null == periodInfo || null == periodInfo.getBasicCode()) {
            throw new BizException("请先去商家账期设置界面配置！");
        }
        //超期时间
        long overTime = 0L;
        Calendar c = Calendar.getInstance();
        String result = "";
        try {
          //取业务月份当月最后一天（如：1901，取2019-01-31）
            String creMonth = DateUtil.getLastDay("20" + String.valueOf(entity.getCreateMonth()).substring(0, 2) + "-"+ String.valueOf(entity.getCreateMonth()).substring(2, 4) + "-01");
            
            switch (periodInfo.getBasicCode()) {
            case BILL_DAY:
                result = nextDay(current, entity, periodInfo, c, sdf, sdf.parse(creMonth), overTime);
                break;
            case BILL_DAY_NEXT_MONTH:
                result = nextMonthFirstDay(current, entity, periodInfo, c, sdf, creMonth, overTime);
                break;
            default:
                if ("1".equals(entity.getIsneedInvoice()) && entity.getInvoiceDate() != null) {
                    // 需要开票时用开票时间去判断
                    if (INVOICE_DAY.equals(periodInfo.getBasicCode())) {
                        result = nextDay(current, entity, periodInfo, c, sdf, entity.getInvoiceDate(), overTime);
                    }
                    if (INVOICE_DAY_NEXT_MONTH.equals(periodInfo.getBasicCode())) {
                        result = nextMonthFirstDay(current, entity, periodInfo, c, sdf, sdf.format(entity.getInvoiceDate()), overTime);
                    }
                } else if ("0".equals(entity.getIsneedInvoice()) && entity.getConfirmDate() != null) {
                    // 不需要开票时用确认时间去判断
                    if (INVOICE_DAY.equals(periodInfo.getBasicCode())) {
                        result = nextDay(current, entity, periodInfo, c, sdf, entity.getConfirmDate(), overTime);
                    }
                    if (INVOICE_DAY_NEXT_MONTH.equals(periodInfo.getBasicCode())) {
                        result = nextMonthFirstDay(current, entity, periodInfo, c, sdf, sdf.format(entity.getConfirmDate()), overTime);
                    }
                }
                break;
            }
        } catch (Exception e) {
            logger.error("日期转换异常：", e);
            throw new BizException("日期转换异常!");
        }
        return result;
    }

    /**
     * 
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月14日 下午5:16:38
     *
     * @param current   当前日期转long
     * @param entity    账单vo    
     * @param sdf       日期格式化
     * @param creMonth  业务日期，当月最后一天（如1901，转成2019-01-31）
     * @param overTime  超期日期
     * @throws ParseException
     */
    private String nextMonthFirstDay(long current, BillCheckInfoVo entity, BillPeriodInfoEntity periodInfo, Calendar c,
            SimpleDateFormat sdf, String creMonth, long overTime)
            throws ParseException {
        String nextMonthFirstDay = DateUtil.getFirstDayOfGivenMonth(creMonth, 1, yyyyMMdd);
        c.setTime(sdf.parse(nextMonthFirstDay));
        if (null != periodInfo.getAddMonth()) {
            c.add(Calendar.MONTH, periodInfo.getAddMonth());
            //可能出现1-31变成2-28的问题
            if (c.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH) < 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        if (null != periodInfo.getAddDay()) {
            c.add(Calendar.DAY_OF_MONTH, periodInfo.getAddDay());
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        overTime = c.getTime().getTime();
        int days = (int) ((overTime - current) / (1000 * 60 * 60 * 24));
        if (days >= 20) {
            return "正常";
        } else if (days > 0 && days < 20) {
            return "临期";
        } else {
            //days <= 0
            return "超期";
        }
    }

    /**
     * 加一天
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月14日 下午5:14:00
     *
     * @param current    当前日期转成long
     * @param entity     账单vo
     * @param periodInfo 商家账期配置
     * @param c          Calendar日期类
     * @param sdf        日期格式化
     * @param creMonth   业务日期，当月最后一天（如1901，转成2019-01-31）
     * @param overTime   超期日期
     * @throws ParseException
     */
    private String nextDay(long current, BillCheckInfoVo entity, BillPeriodInfoEntity periodInfo, Calendar c,
            SimpleDateFormat sdf, Date creMonth, long overTime) throws ParseException {
        c.setTime(creMonth);  
        c.add(Calendar.DAY_OF_MONTH, 1);
        if (null != periodInfo.getAddMonth()) {
            c.add(Calendar.MONTH, periodInfo.getAddMonth());
            //可能出现1-31变成2-28的问题
            if (c.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH) < 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        if (null != periodInfo.getAddDay()) {
            c.add(Calendar.DAY_OF_MONTH, periodInfo.getAddDay());
        }
 
        overTime = c.getTime().getTime();
        int days = (int) ((overTime - current) / (1000 * 60 * 60 * 24));
        if (days >= 20) {
            return "正常";
        } else if (days > 0 && days < 20) {
            return "临期";
        } else {
            //days <= 0
            return "超期";
        }
    }

    /**
     * 获取商家账期配置
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月14日 下午5:16:21
     *
     * @param entity
     * @return
     */
    private BillPeriodInfoEntity getForPeriodInfo(BillCheckInfoVo entity) {
        BillPeriodInfoEntity periodInfoEntity = new BillPeriodInfoEntity();
        BillPeriodInfoEntity periodNullEntity = null;
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("delFlag", "0");
        List<BillPeriodInfoEntity> periodList = billPeriodInfoRepository.query(map); 
        boolean exe = false;
        for (BillPeriodInfoEntity periodEntity : periodList) {
            if (StringUtils.isBlank(periodEntity.getInvoiceName())) {
                periodNullEntity = periodEntity;
            }
            if (entity.getInvoiceName().equals(periodEntity.getInvoiceName())) {
                periodInfoEntity = periodEntity;
                exe = true;
                break;
            }
        }
        if (!exe) {
            return periodNullEntity;
        }
        return periodInfoEntity;
    }

    @Override
    public PageInfo<BillCheckInfoVo> queryWarn(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        PageInfo<BillCheckInfoEntity> pageInfo = billCheckInfoRepository.queryWarn(condition, pageNo, pageSize);
        PageInfo<BillCheckInfoVo> result = new PageInfo<BillCheckInfoVo>();

        try {
            List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
            for (BillCheckInfoEntity entity : pageInfo.getList()) {
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                vo.setWarnMessage("预警");
                voList.add(vo);
            }
            result.setList(voList);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }

        return result;
    }

    @Override
    public PageInfo<BillCheckInfoVo> queryWarnList(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        PageInfo<BillCheckInfoEntity> pageInfo = billCheckInfoRepository.queryWarnList(condition, pageNo, pageSize);
        PageInfo<BillCheckInfoVo> result = new PageInfo<BillCheckInfoVo>();

        try {
            List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
            for (BillCheckInfoEntity entity : pageInfo.getList()) {
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                vo.setWarnMessage("预警");
                voList.add(vo);
            }
            result.setList(voList);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }

        return result;
    }

    @Override
    public List<BillCheckAdjustInfoVo> queryAdjust(Map<String, Object> condition) {
        List<BillCheckAdjustInfoEntity> list = billCheckInfoRepository.queryAdjust(condition);
        List<BillCheckAdjustInfoVo> voList = new ArrayList<BillCheckAdjustInfoVo>();
        for (BillCheckAdjustInfoEntity entity : list) {
            BillCheckAdjustInfoVo vo = new BillCheckAdjustInfoVo();
            try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public BillCheckInfoVo queryOne(Map<String, Object> condition) {
        BillCheckInfoVo billCheckInfoVo = new BillCheckInfoVo();
        BillCheckInfoEntity entity = billCheckInfoRepository.queryOne(condition);
        if (entity == null) {
            return null;
        }
        try {
            PropertyUtils.copyProperties(billCheckInfoVo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return billCheckInfoVo;
    }

    @Override
    public int update(BillCheckInfoVo billCheckInfoVo) {
        int result = 0;
        try {
            BillCheckInfoEntity entity = new BillCheckInfoEntity();

            PropertyUtils.copyProperties(entity, billCheckInfoVo);

            result = billCheckInfoRepository.update(entity);
        } catch (Exception ex) {
            logger.error("保存失败", ex);
        }
        return result;
    }

    @Override
    public int updateOne(BillCheckInfoVo billCheckInfoVo) {
        // TODO Auto-generated method stub
        int result = 0;
        try {
            BillCheckInfoEntity entity = new BillCheckInfoEntity();

            PropertyUtils.copyProperties(entity, billCheckInfoVo);

            result = billCheckInfoRepository.updateOne(entity);
        } catch (Exception ex) {
            logger.error("保存失败", ex);
        }
        return result;
    }

    @Override
    public int saveList(List<BillCheckInfoVo> list) {
        List<BillCheckInfoEntity> enList = new ArrayList<BillCheckInfoEntity>();
        for (BillCheckInfoVo entity : list) {
            BillCheckInfoEntity vo = new BillCheckInfoEntity();
            try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            enList.add(vo);
        }
        int result = billCheckInfoRepository.saveList(enList);
//        for (BillCheckInfoEntity billCheckInfoEntity : enList) {
//            saveCrm(billCheckInfoEntity);
//        }
        return result;
    }

    @Override
    public int save(BillCheckInfoVo vo) {
        BillCheckInfoEntity entity = new BillCheckInfoEntity();
        try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return billCheckInfoRepository.save(entity);
    }

    @Override
    public int saveNew(BillCheckInfoVo vo) {
        BillCheckInfoEntity entity = new BillCheckInfoEntity();
        try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return billCheckInfoRepository.saveNew(entity);
    }

    @Override
    public List<BillCheckInfoVo> queryList(Map<String, Object> condition) {
        List<BillCheckInfoEntity> list = billCheckInfoRepository.queryList(condition);
        List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
        for (BillCheckInfoEntity entity : list) {
            BillCheckInfoVo vo = new BillCheckInfoVo();
            try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public int saveReceiptFollow(BillReceiptFollowVo vo) {
        BillReceiptFollowEntity entity = new BillReceiptFollowEntity();
        try {
            PropertyUtils.copyProperties(entity, vo);
        } catch (Exception ex) {
            logger.error("转换失败");
        }

        // 保存回款反馈
        int result = billCheckInfoRepository.saveReceiptFollow(entity);
        if (result > 0) {
            // 更新账单
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", vo.getBillCheckId());
            // 查询账单
            BillCheckInfoEntity bInfo = billCheckInfoRepository.queryOne(condition);
            // 回款反馈
            BillReceiptFollowEntity receiptFollowVo = billCheckInfoRepository.queryReceiptFollow(condition);
            bInfo.setExpectReceiptDate(receiptFollowVo.getExpectReceiptDate());
            billCheckInfoRepository.update(bInfo);
        }

        return result;
    }

    @Override
    public PageInfo<BillReceiptFollowVo> queryReceiptFollow(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub

        PageInfo<BillReceiptFollowEntity> pageInfo = billCheckInfoRepository.queryReceiptFollow(condition, pageNo,
                pageSize);

        PageInfo<BillReceiptFollowVo> result = new PageInfo<BillReceiptFollowVo>();

        List<BillReceiptFollowVo> voList = new ArrayList<BillReceiptFollowVo>();
        for (BillReceiptFollowEntity entity : pageInfo.getList()) {
            BillReceiptFollowVo vo = new BillReceiptFollowVo();
            try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            voList.add(vo);
        }

        result.setList(voList);
        return result;
    }

    @Override
    public int saveAjustList(List<BillCheckAdjustInfoVo> list) {
        List<BillCheckAdjustInfoEntity> enList = new ArrayList<BillCheckAdjustInfoEntity>();
        for (BillCheckAdjustInfoVo vo : list) {
            BillCheckAdjustInfoEntity entity = new BillCheckAdjustInfoEntity();
            try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            enList.add(entity);
        }
        return billCheckInfoRepository.saveAjustList(enList);
    }

    @Override
    public BillCheckAdjustInfoVo queryOneAdjust(Map<String, Object> condition) {
        BillCheckAdjustInfoVo vo = null;
        try {
            BillCheckAdjustInfoEntity entity = billCheckInfoRepository.queryOneAdjust(condition);
            if (entity != null) {
                vo = new BillCheckAdjustInfoVo();
                PropertyUtils.copyProperties(vo, entity);
            }
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return vo;
    }

    @Override
    public int updateAjustList(List<BillCheckAdjustInfoVo> list) {
        List<BillCheckAdjustInfoEntity> enList = new ArrayList<BillCheckAdjustInfoEntity>();
        for (BillCheckAdjustInfoVo vo : list) {
            BillCheckAdjustInfoEntity entity = new BillCheckAdjustInfoEntity();
            try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            enList.add(entity);
        }
        return billCheckInfoRepository.updateAjustList(enList);
    }

    @Override
    public int saveReceiptFollowList(List<BillReceiptFollowVo> list) {
        List<BillReceiptFollowEntity> elist = new ArrayList<BillReceiptFollowEntity>();

        for (BillReceiptFollowVo vo : list) {
            BillReceiptFollowEntity entity = new BillReceiptFollowEntity();
            try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败:{0}", ex);
            }
            elist.add(entity);
        }

        return billCheckInfoRepository.saveReceiptFollowList(elist);
    }

    @Override
    public String getBillCheckStatus(int id) {
        return billCheckInfoRepository.getBillCheckStatus(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
    @Override
    public int confirmBillCheckInfo(BillCheckInfoVo billCheckInfoVo, BillCheckLogVo vo) throws Exception {

        try {
            BillCheckInfoEntity entity = new BillCheckInfoEntity();
            PropertyUtils.copyProperties(entity, billCheckInfoVo);
            BillCheckLogEntity logEntity = new BillCheckLogEntity();
            PropertyUtils.copyProperties(logEntity, vo);

            int k = billCheckLogRepository.addCheckLog(logEntity);
            if (k > 0) {
                return billCheckInfoRepository.update(entity);
            } else {
                throw new BizException("添加账单日志失败");
            }

        } catch (Exception ex) {
            logger.error("转换失败:", ex);
            throw ex;
        }
    }

    @Override
    public List<BillCheckInfoVo> queryAllByBillName(List<String> invoiceNameList) {
        List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
        try {
            List<BillCheckInfoEntity> list = billCheckInfoRepository.queryAllByBillName(invoiceNameList);
            for (BillCheckInfoEntity entity : list) {
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                voList.add(vo);
            }
        } catch (Exception e) {
            logger.error("queryAllByInvoiceName error:", e);
        }
        return voList;
    }

    @Override
    public List<BillCheckInfoVo> queryAllByBillCheckId(List<Integer> checkIdList) {
        List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
        try {
            List<BillCheckInfoEntity> list = billCheckInfoRepository.queryAllByBillCheckId(checkIdList);
            for (BillCheckInfoEntity entity : list) {
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                voList.add(vo);
            }
        } catch (Exception e) {
            logger.error("queryAllByBillCheckId error:", e);
        }
        return voList;
    }

    @Override
    public BillReceiptFollowVo queryReceiptFollow(Map<String, Object> condition) {
        BillReceiptFollowVo vo = new BillReceiptFollowVo();
        BillReceiptFollowEntity entity = billCheckInfoRepository.queryReceiptFollow(condition);
        if (entity == null) {
            return null;
        }
        try {
            PropertyUtils.copyProperties(vo, entity);

        } catch (Exception e) {
            logger.error("queryAllByBillCheckId error:", e);
        }
        return vo;
    }

    @Override
    public PageInfo<BillCheckInfoVo> queryReceiptDetail(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        try {
            PageInfo<BillCheckInfoEntity> pageInfo = billCheckInfoRepository.queryReceiptDetail(condition, pageNo,
                    pageSize);
            PageInfo<BillCheckInfoVo> result = new PageInfo<BillCheckInfoVo>();

            PropertyUtils.copyProperties(result, pageInfo);

            List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();

            // 确认金额总计
            BigDecimal totalConfirmAmount = new BigDecimal(0);
            // 发票金额总计
            BigDecimal totalInvoiceAmount = new BigDecimal(0);
            // 未收款金额
            BigDecimal totalUnReceiptAmount = new BigDecimal(0);
            // 收款金额
            BigDecimal totalReceiptAmount = new BigDecimal(0);

            for (BillCheckInfoEntity entity : pageInfo.getList()) {
                BillCheckInfoVo vo = new BillCheckInfoVo();

                PropertyUtils.copyProperties(vo, entity);

                // 预计到期时间
                if (vo.getExpectReceiptDate() != null) {
                    Date day = new Date();
                    if (day.getTime() > vo.getExpectReceiptDate().getTime()) {
                        Long days = (day.getTime() - vo.getExpectReceiptDate().getTime()) / (1000 * 60 * 60 * 24);
                        vo.setOverDays(days + "");
                    }
                }
                // 应收账款天数
                Date day = getDate(vo.getCreateMonth());
                Date nowDay = new Date();
                // 当未收款金额不为0或为空时，取“当前日期-账单月份最后一天”
                if (vo.getUnReceiptAmount() == null || vo.getUnReceiptAmount().compareTo(BigDecimal.ZERO) != 0) {
                    nowDay = new Date();
                } else if (vo.getUnReceiptAmount().compareTo(BigDecimal.ZERO) == 0) {
                    // 当未收款金额为0时，取“收款日期-账单月份最后一天
                    if (vo.getReceiptDate() != null) {
                        nowDay = vo.getReceiptDate();
                    }
                }
                if (day != null) {
                    if (nowDay.getTime() < day.getTime()) {
                        vo.setReceiptDays(0 + "");
                    } else {
                        Long days = (nowDay.getTime() - day.getTime()) / (1000 * 60 * 60 * 24);
                        vo.setReceiptDays(days + "");
                    }
                }

                // 统计金额
                // 确认金额
                totalConfirmAmount = totalConfirmAmount.add(vo.getConfirmAmount());
                // 发票金额
                totalInvoiceAmount = totalInvoiceAmount.add(vo.getInvoiceAmount());
                // 未收款金额
                totalUnReceiptAmount = totalUnReceiptAmount.add(vo.getUnReceiptAmount());
                // 收款金额
                totalReceiptAmount = totalReceiptAmount.add(vo.getReceiptAmount());

                voList.add(vo);
            }

            for (BillCheckInfoVo entity : voList) {
                entity.setTotalConfirmAmount(totalConfirmAmount);
                entity.setTotalInvoiceAmount(totalInvoiceAmount);
                entity.setTotalUnReceiptAmount(totalUnReceiptAmount);
                entity.setTotalReceiptAmount(totalReceiptAmount);
            }

            result.setList(voList);
            return result;
        } catch (Exception ex) {
            logger.error("queryReceiptDetail error:{0}", ex);
        }
        return null;
    }

    public Date getDate(int createMonth) {
        try {
            String createDate = "20" + createMonth + "";
            int year = Integer.parseInt(createDate.substring(0, 4));
            int month = Integer.parseInt(createDate.substring(4, 6));

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));

            String date = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = df.parse(date);

            return date1;
        } catch (ParseException e) {
            logger.error("getDate error:{0}", e);
        }
        return null;
    }

    /**
     * 获取“预分配金额” 、“待催款金额”
     * 
     * @return
     */
    public Map<String, BillCheckInfoEntity> getPreMoney() {

        Map<String, BillCheckInfoEntity> returnMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        Map<String, BigDecimal> accountMap = new HashMap<>();

        List<BillCheckInfoEntity> list = billCheckInfoRepository.queryAllUnreceipt(map);

        for (BillCheckInfoEntity entity : list) {
            BigDecimal accountAmount = new BigDecimal(0d);

            if (accountMap.containsKey(entity.getInvoiceName())) {
                accountAmount = accountMap.get(entity.getInvoiceName());
            } else {
                accountAmount = entity.getAccountAmount();
            }

            BigDecimal unReceiptAmount = entity.getUnReceiptAmount();
            if (accountAmount.compareTo(BigDecimal.ZERO) > 0) {
                // 账户金额大于0时
                if (accountAmount.compareTo(unReceiptAmount) >= 0) {
                    // 账户金额大于未收款金额时，赋值
                    accountMap.put(entity.getInvoiceName(), accountAmount.subtract(unReceiptAmount));// 账户余额
                    entity.setPreDistibutionAmount(unReceiptAmount);// 预分配金额
                } else {
                    // 账户金额小于未收款金额时，赋值
                    accountMap.put(entity.getInvoiceName(), BigDecimal.ZERO);// 账户余额30240.6
                    entity.setPreDistibutionAmount(accountAmount);// 预分配金额
                }
            } else {
                accountMap.put(entity.getInvoiceName(), BigDecimal.ZERO);// 账户余额
                entity.setPreDistibutionAmount(BigDecimal.ZERO);// 预分配金额
            }
            entity.setTbDunAmount(unReceiptAmount.subtract(entity.getPreDistibutionAmount()));// 待催款金额

            returnMap.put(entity.getInvoiceName() + "&" + entity.getCreateMonth() + "&" + entity.getBillName(), entity);
        }
        return returnMap;
    }

    @Override
    public BillCheckInfoVo queryBillCheck(Map<String, Object> condition) {
        BillCheckInfoVo billCheckInfoVo = new BillCheckInfoVo();
        BillCheckInfoEntity entity = billCheckInfoRepository.queryBillCheck(condition);
        if (entity == null) {
            return null;
        }
        try {
            PropertyUtils.copyProperties(billCheckInfoVo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return billCheckInfoVo;
    }

    public PageInfo<BillCheckInfoVo> queryForOut(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        PageInfo<BillCheckInfoEntity> pageInfo = billCheckInfoRepository.queryForOut(condition, pageNo, pageSize);
        PageInfo<BillCheckInfoVo> result = new PageInfo<BillCheckInfoVo>();

        try {
            List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();
            for (BillCheckInfoEntity entity : pageInfo.getList()) {
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                voList.add(vo);
            }
            result.setList(voList);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }

        return result;
    }

    @Override
    public BillCheckInfoVo getLatestBill(Map<String, Object> condition) {
        BillCheckInfoVo vo = new BillCheckInfoVo();
        BillCheckInfoEntity entity = billCheckInfoRepository.getLatestBill(condition);
        try {
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return vo;
    }

    @Override
    public void importCheck(String createMonth, String billName) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("createMonth", createMonth);
        condition.put("billName", billName);
        BillCheckInfoEntity entity = billCheckInfoRepository.queryBillCheck(condition);
        if (entity == null) {
            throw new BizException("BILL_NULL", "账单不存在!");
        }
        // 状态为已确认
        if ("CONFIRMED".equals(entity.getBillCheckStatus())) {
            throw new BizException("CONFIRMED_NULL", "对账状态为已确认，无法导入!");
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
    @Override
    public void adjustMoney(String billNo, Double adjustMoney, String adjustReason, String username, String userId) {
        // 账单导入主表更新调整金额
        BillReceiveMasterEntity brmVo = new BillReceiveMasterEntity();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        brmVo.setBillNo(billNo);
        brmVo.setLastModifier(username);
        brmVo.setLastModifierId(userId);
        brmVo.setLastModifyTime(currentTime);
        brmVo.setAdjustAmount(adjustMoney);
        brmVo.setAdjustReason(adjustReason);
        try {
            billReceiveMasterRepository.update(brmVo);
        } catch (Exception e) {
            throw new BizException("UPDATE_NULL", "账单主表更新确认金额失败!");
        }

        // 账单导入记录表写入
        BillReceiveMasterRecordEntity recordEntity = new BillReceiveMasterRecordEntity();
        BigDecimal masterAdjustMoney = BigDecimal.valueOf(adjustMoney);
        recordEntity.setBillNo(billNo);
        recordEntity.setCreateTime(currentTime);
        recordEntity.setCreator(username);
        recordEntity.setCreatorId(userId);
        recordEntity.setAdjustAmount(masterAdjustMoney);
        try {
            billReceiveMasterRecordRepository.save(recordEntity);
        } catch (Exception e) {
            throw new BizException("UPDATE_NULL", "账单导入记录表更新确认金额失败!");
        }
    }

    @Override
    public List<BillCheckInfoEntity> querySnapshot(Map<String, Object> condition) {
        return billCheckInfoRepository.querySnapshot(condition);
    }

    @Override
    public List<BillCheckInfoEntity> queryReceipt(Map<String, Object> condition) {
        return billCheckInfoRepository.queryReceipt(condition);
    }

    @Override
    public List<BillCheckInfoEntity> queryCheckReceipt(Map<String, Object> condition) {
        return billCheckInfoRepository.queryCheckReceipt(condition);
    }

    @Override
    public List<BillCheckInfoEntity> querySnapshotExpect(Map<String, Object> condition) {
        return billCheckInfoRepository.querySnapshotExpect(condition);
    }

    @Override
    public List<BillCheckInfoEntity> queryIncomeReport(Map<String, Object> condition) {
        return billCheckInfoRepository.queryIncomeReport(condition);
    }

    @Override
    public PageInfo<BillCheckInfoEntity> queryIncomeDetail(Map<String, Object> condition, int pageNo, int pageSize) {
        return billCheckInfoRepository.queryIncomeDetail(condition, pageNo, pageSize);
    }

    @Override
    public List<BillCheckInfoEntity> queryIncomeReportBizCus(Map<String, Object> condition) {
        return billCheckInfoRepository.queryIncomeReportBizCus(condition);
    }

    @Override
    public List<BillCheckInfoEntity> queryIncomeReportCalCus(Map<String, Object> condition) {
        return billCheckInfoRepository.queryIncomeReportCalCus(condition);
    }

    @Override
    public void saveCrm(BillCheckInfoEntity entity) {
        try {
            entity.setCrmAsynTime(JAppContext.currentTimestamp());
            // 根据id获取账单
            Map<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("id", entity.getId());
            List<BillCheckInfoEntity> entities = billCheckInfoRepository.queryId(conditionMap);
            if(CollectionUtils.isEmpty(entities)){
                logger.info("根据账单ID：" + entity.getId()+"未找到账单");
                return;
            }
            List<FieldDataOpenVO> listFieldDataOpenVO = getFieldDataOpenVOList(entities.get(0));
            if(CollectionUtils.isEmpty(listFieldDataOpenVO)){
                logger.info("不向crm推送数据：" + entity.getId());
                return;
            }
            //封装CRM参数
            ModuleDataOpenVO moduleVo = new ModuleDataOpenVO();
            moduleVo.setFieldDataVos(listFieldDataOpenVO);
            moduleVo.setUniqueCheckFieldApiKey("id");
            Long tenantId = tenantConfig.getTenantId();
            String json = JSON.toJSON(moduleVo).toString();
            logger.info("发送CRM参数：" + json);
            Long result = moduleDataOpenService.saveModuleData(tenantId, "bill", moduleVo);
            logger.info("接收crm结果：" + result);
            entity.setCrmStatus(1l);
        } catch (Exception e) {
            logger.error("CRM保存接口失败:", e);
            entity.setCrmStatus(2l);
        }
        
        try {
            billCheckInfoRepository.updateCrm(entity);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("BMS保存CRM推送状态和推送时间失败失败:", e);

        }
    }

    private List<FieldDataOpenVO> getFieldDataOpenVOList(BillCheckInfoEntity entity) {
        // 查询mk_id
        Map<String, Object> mkConditionMap = new HashMap<>();
        String invoiceName = entity.getInvoiceName();
        mkConditionMap.put("invoiceName", invoiceName);
        List<BillCheckInfoEntity> mkEntities = billCheckInfoRepository.querySourceId(mkConditionMap);
        if (CollectionUtils.isEmpty(mkEntities)) {
            logger.info("根据invoiceName在pub_customer_base未查询到商家：" + invoiceName);
            return null;
        }
        BillCheckInfoEntity mkEntity = mkEntities.get(0);
        String sourceIdString = mkEntity.getSourceId();
        if(StringUtils.isBlank(sourceIdString)){
            logger.info("source_id为空：" + invoiceName);
            return null;
        }
        Long sId = null;
        try {
            sId = Long.valueOf(sourceIdString);
        } catch (NumberFormatException e) {
            logger.info("source_id转换long失败：" + invoiceName);
            e.printStackTrace();
            return null;
        }
        FieldDataOpenVO vo1 = getFieldDataOpenVO("mk_id",sId, "customer", true);
        // 获取账单状态
        Map<String, String> enumMap = CheckBillStatusEnum.getMap();
        String billStatusString = enumMap.get(entity.getBillStatus());
        FieldDataOpenVO vo2 = getFieldDataOpenVO("bill_status", billStatusString, null, null);
        // 获取总金额
        FieldDataOpenVO vo3 = getFieldDataOpenVO("confirm_amount", entity.getConfirmAmount(), null, null);
        // 获取已回款
        FieldDataOpenVO vo4 = getFieldDataOpenVO("receipt_amount", entity.getReceiptAmount(), null, null);
        // 获取待回款
        FieldDataOpenVO vo5 = getFieldDataOpenVO("un_receipt_amount", entity.getUnReceiptAmount(), null, null);
        // 获取商家合同名称
        FieldDataOpenVO vo6 = getFieldDataOpenVO("invoice_name", entity.getInvoiceName(), null, null);
        // 获取业务月份
        FieldDataOpenVO vo7 = getFieldDataOpenVO("create_month", entity.getCreateMonth(), null, null);
        // 获取账单编号
        FieldDataOpenVO vo8 = getFieldDataOpenVO("id", entity.getId(), null, null);
        // 获取账单名称
        FieldDataOpenVO vo9 = getFieldDataOpenVO("bill_name", entity.getBillName(), null, null);
        List<FieldDataOpenVO> list = new ArrayList<FieldDataOpenVO>();
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        list.add(vo6);
        list.add(vo7);
        list.add(vo8);
        list.add(vo9);
        return list;
    }

    private FieldDataOpenVO getFieldDataOpenVO(String fieldApiKey, Object fieldValue, String relModule, Boolean bool) {
        FieldDataOpenVO vo = new FieldDataOpenVO();
        vo.setFieldApiKey(fieldApiKey);
        vo.setFieldValue(fieldValue);
        vo.setRelModule(relModule);
        vo.setRelationField(bool);
        return vo;
    }
    
    @Override
    public PageInfo<BillCheckInfoVo> querySimple(Map<String, Object> condition, int pageNo, int pageSize) {
        try {
            PageInfo<BillCheckInfoEntity> pageInfo = billCheckInfoRepository.querySimple(condition, pageNo, pageSize);

            PageInfo<BillCheckInfoVo> result = new PageInfo<BillCheckInfoVo>();

            PropertyUtils.copyProperties(result, pageInfo);

            List<BillCheckInfoVo> voList = new ArrayList<BillCheckInfoVo>();

            for (BillCheckInfoEntity entity : pageInfo.getList()) {
                //因为vo的createMonth为int类型，如果entity的createMonth为null会导致转换失败
                entity.setCreateMonth(0);
                BillCheckInfoVo vo = new BillCheckInfoVo();
                PropertyUtils.copyProperties(vo, entity);
                voList.add(vo);
            }

            result.setList(voList);
            return result;
        } catch (Exception ex) {
            logger.error("转换失败:{0}", ex);
        }
        return null;
    }

    @Override
    public PubCustomerBaseEntity queryMk(Map<String, Object> condition) {
        // TODO Auto-generated method stub
        return billCheckInfoRepository.queryMk(condition);
    }
}