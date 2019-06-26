/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.consumer.export.billhandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IBillPrepareExportTaskService;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.service.IFeesTransportMasterService;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.jiuyescm.tms.open.api.ITmsForOmsService;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年5月8日 下午5:20:48
 */
@Service("prepareBillHandler")
@Scope("prototype")
public class PrepareBillHandler {

    private static final Logger logger = LoggerFactory.getLogger(PrepareBillHandler.class);
    
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IBillPrepareExportTaskService billPrepareExportTaskService;
    @Autowired
    private ISystemCodeService systemCodeService;
    @Autowired
    private IFeesReceiveDispatchService feesReceiveDispatchService;
    @Autowired 
    private IBmsGroupService bmsGroupService;
    @Autowired 
    private IBmsGroupCustomerService bmsGroupCustomerService;
    @Autowired
    private IFeesReceiveStorageService feesReceiveStorageService;
    @Autowired
    private IBizDispatchBillService bizDispatchBillService;
    @Autowired
    private IPubMaterialInfoService pubMaterialInfoService;
    @Autowired
    private IBizOutstockPackmaterialService bizOutstockPackmaterialServiceImpl;
    @Autowired
    private IBmsBizInstockInfoService bmsBizInstockInfoService;
    @Autowired
    private IBmsGroupSubjectService bmsGroupSubjectService;
    @Autowired
    private IFeesAbnormalService feesAbnormalService;
    @Autowired
    private StorageClient storageClient;
    @Autowired
    private IFeesTransportMasterService feesTransportMasterService;
    @Autowired
    private ITmsForOmsService tmsForOmsService;
    
    private static final int PAGESIZE = 10000;
    FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
    
    private static ThreadLocal<Double> totalAmount = new ThreadLocal<Double>();
    //运费
    private static ThreadLocal<Double> totalDeliveryCost = new ThreadLocal<Double>();
    //商品金额
    private static ThreadLocal<Double> totalProductAmount = new ThreadLocal<Double>();
    //改地址退件费
    private static ThreadLocal<Double> totalReturnAmount = new ThreadLocal<Double>();
    //干线应付总费用
    private static ThreadLocal<Double> paytotalAmount = new ThreadLocal<Double>();
    
    private Double process = 0d;
    private Map<String, String> mapWarehouse;
    
    /*
     * 异步导出
     */
    public void export(String json) throws Exception{
        //初始化进度
        process = 0d;    
        logger.info("JSON开始解析……");
        Map<String, Object> condition = resolveJsonToMap(json);
        if (null == condition) {
            logger.info("未传入json");
            return;
        }
        
        //日期重新转换，发MQ日期格式错误
        String year = "";
        String month = "";
        if (condition.containsKey("year") && condition.containsKey("month")) {
            year = condition.get("year").toString();
            month = condition.get("month").toString();
        }
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
            String startDateStr = year + "-" + month + "-01 00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = sdf.parse(startDateStr);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDay);
            Date endDate = cal.getTime();
            condition.put("startDate", startDate);
            condition.put("endDate", endDate);
        }
        
        //获取任务ID
        String taskId = condition.get("taskId").toString();
        logger.info("任务ID：{}",taskId);
        
        //获取商家
        @SuppressWarnings("unchecked")
        Map<String, String> customerMap = (Map<String, String>) condition.get("cu");
        
        //查出该任务用于继续累加备注
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskId", taskId);
        BillPrepareExportTaskEntity entity = billPrepareExportTaskService.queryBillTask(param);
        if (null == entity) {
            logger.info("未查到任务");
            return;
        }
        
        //运行折扣（进度从折扣后的开始累加）
        if ((Boolean)condition.get("isDiscount")) {
            process += entity.getProgress();
        }
        //不运行折扣（直接从30开始累加）
        else {
            process += 30;
        }
        
        //更新进度
        updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), process, null, null);
        
        // 初始化参数
        init();
        
        StopWatch stw = new StopWatch();
        stw.start();
        POISXSSUtil poiUtil = new POISXSSUtil();
        SXSSFWorkbook xssfWorkbook = poiUtil.getXSSFWorkbook();
        logger.info("====预账单导出：" + "[" + customerMap.get("customerName").toString() + "]" + "写入Excel begin.");
        
        //判断是否是按子商家导出       
        if ((Boolean)condition.get("isChildCustomer") == true) {
            condition.put("customerId", customerMap.get("customerId"));
            condition.put("customerName", customerMap.get("customerName"));
        }else {     
            List<String> customerIdList=billPrepareExportTaskService.getChildCustomerId(condition.get("customerId").toString());
            if(customerIdList.size()<=0){
                return;
            }
            condition.put("customerId", "");
            condition.put("customerIds", customerIdList);
        }
        
        //基础数据和映射
        Map<String, String> transportTypeMap = getEnumList("TRANSPORT_TYPE");
        Map<String, String> temMap = getEnumList("TEMPERATURE_TYPE");
        List<String> warehouseList = queryPreBillWarehouse(condition);  
        
        //进行数据组装，失败只更新状态和备注，不更新进度；成功只更新进度
        // 配送费
        try {
            handDispatch(xssfWorkbook, poiUtil, condition, transportTypeMap);
        } catch (Exception e) {
            logger.error("配送费导出失败!", e);
            updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0,StringUtils.isBlank(entity.getRemark())?"配送费失败":entity.getRemark()+";配送费失败", null);
            return;
        }
        updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), process+=20, null, null);
        
        // 存储费
        try {
            handStorage(xssfWorkbook, condition, poiUtil, warehouseList);
        } catch (Exception e) {
            logger.error("仓储费导出失败!", e);
            updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0,StringUtils.isBlank(entity.getRemark())?"仓储费失败":entity.getRemark()+"仓储费失败", null);
            return;
        }
        updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), process+=20, null, null);
        
        // 耗材费(是否分仓)
        try { 
            if ((Boolean)condition.get("isSepWarehouse")) {
                handMaterial(xssfWorkbook, poiUtil, condition);
            }else {         
                handMaterialNotSepWareHouse(xssfWorkbook, poiUtil, condition);
            }
        } catch (Exception e) {
            logger.error("耗材使用费导出失败!", e);
            updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0,StringUtils.isBlank(entity.getRemark())?"耗材使用费失败":entity.getRemark()+";耗材使用费失败", null);
            return;
        }
        updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), process+=20, null, null);
        
        try {
            // 出库(TB)
            handOutstock(xssfWorkbook, poiUtil, condition, temMap);
            //入库
            handInstock(xssfWorkbook, poiUtil, condition);
            // 增值
            handAdd(xssfWorkbook, poiUtil, condition);
            // 理赔
            handAbnormal(xssfWorkbook, poiUtil, condition);
            // 改地址和退件费
            handAbnormalChange(xssfWorkbook, poiUtil, condition); 
            // 干线
            handTransport(taskId, xssfWorkbook, poiUtil, condition, entity);
        } catch (Exception e) {
            logger.error("其它费用导出失败:", e);
            updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0,StringUtils.isBlank(entity.getRemark())?"其它费用导出失败":entity.getRemark()+";其它费用导出失败", null);
            return;
        }
        updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), process+=5, null, null);
        
        //上传到Fastdfs
        String filePath = "";
        InputStream inputStream = null;
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            xssfWorkbook.write(out);
            inputStream = new ByteArrayInputStream(out.toByteArray());
            StorePath storePath = storageClient.uploadFile(inputStream, inputStream.available(), "xlsx");
            filePath = storePath.getFullPath();
        } catch (Exception e) {
            logger.error("上传到Fastdfs失败", e);
            updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0,StringUtils.isBlank(entity.getRemark())?"上传Fastdfs失败":entity.getRemark()+";上传Fastdfs失败", null);
            return;
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != out) {
                out.close();
            }
        }

        //折扣运行失败后,预账单一样需要产生,并且可以下载: 只是状态是失败, 进度条是70%.
        //折扣运行成功，预账单成功，状态为成功，进度条是100%
        process+=5;
        if (process == 70) {
            updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), process, null, filePath);
        }
        if (process == 100) {
            updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), process, null, filePath);
        }
        
        stw.stop();
        logger.info("====预账单导出：" + "[" + customerMap.get("customerName").toString() + "]" + "写入Excel end.==总耗时："
                + stw.getTotalTimeMillis() + "毫秒");
    }
    
    /**
     * 宅配
     * 
     * @param xssfWorkbook
     * @param poiUtil
     * @param condition
     * @param filePath
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    private void handDispatch(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil, Map<String, Object> condition,
            Map<String, String> transportTypeMap) throws Exception {
        int pageNo = 1;
        boolean doLoop = true;
        List<FeesReceiveDispatchEntity> dataList = new ArrayList<FeesReceiveDispatchEntity>();
        while (doLoop) {
            PageInfo<FeesReceiveDispatchEntity> pageList = feesReceiveDispatchService
                    .querydistributionDetailByBizData(condition, pageNo, PAGESIZE);
            if (null != pageList && pageList.getList() != null && pageList.getList().size() > 0) {
                if (pageList.getList().size() < PAGESIZE) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }
                dataList.addAll(pageList.getList());
            } else {
                doLoop = false;
            }
        }

        if (dataList.size() == 0) {// 添加默认数据
            Sheet sheet = poiUtil.getXSSFSheet(xssfWorkbook, "宅配");
            CellStyle cs = xssfWorkbook.createCellStyle();
            Font font = xssfWorkbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(font.BOLDWEIGHT_BOLD);
            cs.setFont(font);
            cs.setAlignment(cs.ALIGN_LEFT);

            Row r = sheet.createRow(0);
            Cell c = r.createCell(0);
            c.setCellValue("这段时间没有产生运单");
            c.setCellStyle(cs);
            return;
        }
        List<Map<String, Object>> headMap = getDispathHeadMap();

        List<Map<String, Object>> itemMap = getDispathItemMap(dataList, transportTypeMap);
        poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook, "宅配", headMap, itemMap);
    }
    
    /*
     * 宅配title
     */
    private List<Map<String, Object>> getDispathHeadMap() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, String> headMapDict = Maps.newLinkedHashMap();
        headMapDict.clear();
        headMapDict.put("warehouseCode", "仓库");
        headMapDict.put("customerName", "商家名称");
        headMapDict.put("shopName", "店铺名称");
        headMapDict.put("orderNo", "九曳订单号");
        headMapDict.put("externalNo", "商家订单号");
        headMapDict.put("waybillNo", "运单号");
        headMapDict.put("createTime", "运单生成时间");
//        headMapDict.put("zexpressNum", "转寄后运单号");
        headMapDict.put("totalWeight", "运单重量");
        headMapDict.put("totalQty", "商品数量");
        headMapDict.put("productDetail", "商品明细");
        headMapDict.put("carrierName", "计费物流商");
        headMapDict.put("serviceTypeName", "物流产品类型");
        headMapDict.put("transportType", "运输方式");
        headMapDict.put("receiver", "收件人");
        headMapDict.put("receiveProvice", "收件人省");
        headMapDict.put("receiveCity", "收件人市");
        headMapDict.put("receiveDistrict", "收件人区县");
        headMapDict.put("temperatureType", "温度");
        headMapDict.put("chargeWeight", "计费重量");
        headMapDict.put("headPrice", "首重金额");
        headMapDict.put("continuePrice", "续重金额");
        headMapDict.put("amount", "运费");
        headMapDict.put("discountAmount", "折扣后运费");
        headMapDict.put("carrierCalStatus", "运费计算状态");
        headMapDict.put("carrierRemark", "运费计算备注");
        headMapDict.put("orderOperatorAmount", "操作费");
        headMapDict.put("operateAmount", "折扣后操作费");
        headMapDict.put("orderCalStatus", "操作费计算状态");
        headMapDict.put("orderRemark", "操作费计算备注");
        headMapDict.put("dutyType", "责任方");
        headMapDict.put("reasonDetail", "原因详情");

        Set<String> set = headMapDict.keySet();
        for (String s : set) {
            HashMap<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("title", headMapDict.get(s));
            itemMap.put("columnWidth", 25);
            itemMap.put("dataKey", s);
            list.add(itemMap);
        }
        return list;
    }
    
    /*
     * 宅配内容
     */
    private List<Map<String, Object>> getDispathItemMap(List<FeesReceiveDispatchEntity> dataList, Map<String, String> transportTypeMap) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double yunfei = 0d;
        double caozao = 0d;
        double caozuo = 0d;
        for (FeesReceiveDispatchEntity entity : dataList) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("warehouseCode", entity.getWarehouseName());
            map.put("customerName", entity.getCustomerName());
            map.put("shopName", entity.getShopName());
            map.put("orderNo", entity.getOutstockNo());
            map.put("externalNo", entity.getExternalNo());
            //有转寄运单号取转寄的   没有的取运单号
            map.put("waybillNo", StringUtils.isBlank(entity.getZexpressnum())?entity.getWaybillNo():entity.getZexpressnum());
            map.put("createTime", entity.getCreateTime() == null ? "" : sdf.format(entity.getCreateTime()));
//            map.put("zexpressNum", entity.getZexpressnum());
            map.put("totalWeight", entity.getTotalWeight() == null ? 0 : entity.getTotalWeight());
            map.put("totalQty", entity.getTotalQuantity() == null ? 0 : entity.getTotalQuantity());
            map.put("productDetail", entity.getProductDetail());
            map.put("carrierName", entity.getCarrierName());
            map.put("serviceTypeName", entity.getServiceTypeName());
            map.put("transportType", entity.getTransportType()==null?"":transportTypeMap.get(entity.getTransportType()));
            map.put("receiver", entity.getReceiveName());
            map.put("receiveProvice", entity.getToProvinceName());
            map.put("receiveCity", entity.getToCityName());
            map.put("receiveDistrict", entity.getToDistrictName());
            map.put("temperatureType", entity.getTemperatureType());
            map.put("chargeWeight", entity.getChargedWeight() == null ? 0 : entity.getChargedWeight());
            map.put("headPrice", entity.getHeadPrice() == null ? 0 : entity.getHeadPrice());
            map.put("continuePrice", entity.getContinuedPrice() == null ? 0 : entity.getContinuedPrice());
            map.put("amount", entity.getAmount() == null ? 0 : entity.getAmount());
            map.put("discountAmount", entity.getDiscountAmount() == null ? 0 : entity.getDiscountAmount());
            map.put("carrierCalStatus", getCalInfo(entity.getDispatchCal()));
            map.put("carrierRemark", entity.getDispatchRemark());
            map.put("orderOperatorAmount",
                    entity.getOrderOperatorAmount() == null ? 0 : entity
                            .getOrderOperatorAmount());
            map.put("operateAmount", entity.getOperateAmount() == null ? 0 : entity.getOperateAmount());
            map.put("orderCalStatus", getCalInfo(entity.getStorageCal()));
            map.put("orderRemark", entity.getStorageRemark());
            map.put("dutyType", entity.getDutyType());
            map.put("reasonDetail", entity.getUpdateReasonDetail());
            list.add(map);
            yunfei = yunfei + (entity.getAmount() == null ? 0 : entity.getAmount());
            caozao = caozao
                    + Double.valueOf(entity.getOrderOperatorAmount() == null ? "0" : entity.getOrderOperatorAmount());
            caozuo = caozuo + (entity.getOperateAmount() == null ? 0 : entity.getOperateAmount());
        }
        Map<String, Object> finalMap = Maps.newHashMap();
        finalMap.put("continuePrice", "合计金额：");
        finalMap.put("amount", yunfei);
        finalMap.put("orderOperatorAmount", caozao);
        finalMap.put("operateAmount", caozuo);
        list.add(finalMap);
        return list;
    }
    
    /**
     * 获取商家类别配置
     * 
     * @author wangchen870
     * @date 2019年5月16日 上午10:56:54
     *
     * @param groupCode
     * @param bizType
     * @return
     */
    private List<String> getSettingsCus(String groupCode, String bizType){
        Map<String,Object> map= new HashMap<String, Object>();
        List<String> cusList = null;
        map.put("groupCode", groupCode);
        map.put("bizType", bizType);
        BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
        if(bmsGroup!=null){
            cusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
        }
        return cusList;
    }
    
    /**
     * 预账单仓储
     * 
     * @param workbook
     * @param parameter
     * @param poiUtil
     * @param warehouseList
     */
    @SuppressWarnings({ "unused", "unchecked" })
    private void handStorage(SXSSFWorkbook workbook, Map<String, Object> parameter, POISXSSUtil poiUtil,
            List<String> warehouseList) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        Timestamp startTime = DateUtil.formatTimestamp(parameter.get("startDate"));

        for (String warehouseCode : warehouseList) {
            //配置按件商家，是否显示处置费列
            int conIndex = 0;
            int newIndex = 0;
            int move2 = 0;
            //库存件数列显示标识和移动的列数
            boolean itemFlag = true;
            int itemMove = 0;
            
            parameter.put("warehouseCode", warehouseCode);
            FastDateFormat sdf = FastDateFormat.getInstance("yyyy/MM/dd");
            Set<String> set = new TreeSet<String>();
            
            //判断是否是按件商家
            List<String> cusList = getSettingsCus("customer_unit", "group_customer");
            
            //判断是否是按子商家导出       
            if ((Boolean)parameter.get("isChildCustomer") == true) {                
                for (String cus : cusList) {
                    if (parameter.get("customerId").equals(cus)) {
                        newIndex++;
                    }
                }
            }else {         
                List<String> customerList=(List<String>) parameter.get("customerIds");
                for (String cus : cusList) {
                    if (customerList.contains(cus)) {
                        newIndex++;
                    }
                }
            }
            
            //判断是否是"不需要库存件数的商家"
            cusList = getSettingsCus("no_need_item_customer", "group_customer");

            //判断是否是按子商家导出       
            if ((Boolean)parameter.get("isChildCustomer") == true) {                
                for (String cus : cusList) {
                    if (parameter.get("customerId").equals(cus)) {
                        itemFlag = false;
                    }
                }
            }else {         
                List<String> customerList=(List<String>) parameter.get("customerIds");
                for (String cus : cusList) {
                    if (customerList.contains(cus)) {
                        itemFlag = false;
                    }
                }
            }
            
            
            // 商品按件存储
            List<FeesReceiveStorageEntity> itemsList = feesReceiveStorageService.queryPreBillStorageByItems(parameter);
            for (FeesReceiveStorageEntity entity : itemsList) {
                conIndex++;
                if (!set.contains(entity.getCustomerId()+"&"+sdf.format(entity.getCreateTime()))) { 
                    set.add(entity.getCustomerId()+"&"+sdf.format(entity.getCreateTime()));
                    
                }
            }
            
            // 处置费
            List<FeesReceiveStorageEntity> disposalList = feesReceiveStorageService.queryPreBillPallet(parameter);
            for (FeesReceiveStorageEntity entity : disposalList) {
                conIndex++;
                if (!set.contains(entity.getCustomerId()+"&"+sdf.format(entity.getCreateTime()))) {
                    set.add(entity.getCustomerId()+"&"+sdf.format(entity.getCreateTime()));
                }
            }

            if (conIndex == 0) {
                continue;
            }

            // st
            Sheet sheet = workbook.createSheet(mapWarehouse.get(warehouseCode));

            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);

            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setWrapText(true);
            style.setFont(font);

            Row row0 = sheet.createRow(0);
            row0.setHeight((short) (20 * 20));
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("商家名称");
            cell0.setCellStyle(style);
            Cell cell1 = row0.createCell(1);
            cell1.setCellValue("仓库名称");
            cell1.setCellStyle(style);
            Cell cell2 = row0.createCell(2);
            cell2.setCellValue("日期");
            cell2.setCellStyle(style);
            Cell cell3 = row0.createCell(3);
            cell3.setCellValue("库存板数");
            cell3.setCellStyle(style);
            
            //如果配置了"不需要库存件数的商家"，itemFlag为false，不显示列
            if (itemFlag) {
                Cell cell4 = row0.createCell(9);
                cell4.setCellStyle(style);
                cell4.setCellValue("库存件数");
            }else {
                itemMove = 1;
            }  
            
            //如果商品按件存储有数据，展示入库件数/存储费小计列
            if (newIndex > 0) {
                
                Cell cell8 = row0.createCell(18-itemMove);
                cell8.setCellValue("存储费按件小计/元");
                cell8.setCellStyle(style);
                
                sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
                
                //如果配置了"不需要库存件数的商家"，itemFlag=false，不显示列
                if (itemFlag) {
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 11, 11));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 17));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 18, 18));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 19, 19));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 20, 20));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 17, 17)); 
                }else {
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 16));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 17, 17));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 18, 18));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 19, 19));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 11, 11));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
                }  
            }else {
                move2 = 1;
                
              //如果配置了"不需要库存件数的商家"，itemFlag=false，不显示列
                if (itemFlag) {
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));                
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 11, 11));
                        
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 17));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 18, 18));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 19, 19));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 17, 17));
                }else {
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));                
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
                        
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 16));
                    
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 17, 17));
                    sheet.addMergedRegion(new CellRangeAddress(0, 2, 18, 18));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 11, 11));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
                }
            }
            
            Cell cell5 = row0.createCell(10-itemMove);
            cell5.setCellStyle(style);
            cell5.setCellValue("入库板数");
            Cell cell6 = row0.createCell(11-itemMove);
            cell6.setCellValue("出库板数");
            cell6.setCellStyle(style);
            Cell cell7 = row0.createCell(12-itemMove);
            cell7.setCellValue("仓储费/托/元");
            cell7.setCellStyle(style);

            Cell cell9 = row0.createCell(19-move2-itemMove);
            cell9.setCellValue("处置费小计/元");
            cell9.setCellStyle(style);
            Cell cell10 = row0.createCell(20-move2-itemMove);
            cell10.setCellValue("收入合计");
            cell10.setCellStyle(style);

            Row row1 = sheet.createRow(1);
            row1.setHeight((short) (25 * 20));

            Cell cell23 = row1.createCell(3);
            cell23.setCellValue("冷冻");
            cell23.setCellStyle(style);

            Cell cell24 = row1.createCell(4);
            cell24.setCellValue("冷藏");
            cell24.setCellStyle(style);

            Cell cell25 = row1.createCell(5);
            cell25.setCellValue("恒温");
            cell25.setCellStyle(style);

            Cell cell26 = row1.createCell(6);
            cell26.setCellValue("常温");
            cell26.setCellStyle(style);

            Cell cell27 = row1.createCell(7);
            cell27.setCellValue("常温包材");
            cell27.setCellStyle(style);
            
            Cell cell28 = row1.createCell(8);
            cell28.setCellValue("冷冻包材");
            cell28.setCellStyle(style);

            Cell cellk29 = row1.createCell(12-itemMove);
            cellk29.setCellValue("冷冻费小计/元");
            cellk29.setCellStyle(style);

            Cell cellk30 = row1.createCell(13-itemMove);
            cellk30.setCellValue("冷藏费小计/元");
            cellk30.setCellStyle(style);

            Cell cellk31 = row1.createCell(14-itemMove);
            cellk31.setCellValue("恒温费小计/元");
            cellk31.setCellStyle(style);

            Cell cellk32 = row1.createCell(15-itemMove);
            cellk32.setCellValue("常温费小计/元");
            cellk32.setCellStyle(style);

            Cell cellk33 = row1.createCell(16-itemMove);
            cellk33.setCellValue("常温包材费小计/元");
            cellk33.setCellStyle(style);
            
            Cell cellk34 = row1.createCell(17-itemMove);
            cellk34.setCellValue("冷冻包材费小计/元");
            cellk34.setCellStyle(style);

    
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 7, 7));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 8, 8));

            List<String> dateList = new ArrayList<String>(set);

            int rowIndex = 3;
            double totalcost = 0.0;
            double ldcost = 0.0;
            double lccost = 0.0;
            double hwcost = 0.0;
            double cwcost = 0.0;
            double ccfcost = 0.0;
            double czfcost = 0.0;
            double cwpackcost = 0.0;
            double ldCost = 0.0;
            double colcost = 0.0;
            double paCost = 0.0;
            double incost = 0.0;
            double incolCost = 0.0;
            double outcost = 0.0;
            double outcolCost = 0.0;
            double rowProCost = 0.0;

            for (int i = 0; i < dateList.size(); i++) {
                double rowCost = 0.0;
                String timestampKey = dateList.get(i);
    
                Row row = sheet.createRow(rowIndex); 
                rowIndex++;

                Integer index = new Integer(0);
                double rowcolCost = 0.0;
                double cwCost = 0.0;
                
                //处置费金额（包含入库、出库）
                double dispalCost=0.0;
                
                int pIndex = 0;
                
                // 库存板数
                for (FeesReceiveStorageEntity entity : disposalList) {
                    if ((entity.getCustomerId()+"&"+sdf.format(entity.getCreateTime())).equals(timestampKey)) {
                        //商家名称
                        Cell cell40=row.createCell(0);
                        cell40.setCellValue(entity.getCustomerName());      
                        //仓库名称
                        Cell cell41=row.createCell(1);
                        cell41.setCellValue(entity.getWarehouseName());
                        //时间
                        Cell cell42=row.createCell(2);
                        cell42.setCellValue(timestampKey.substring(timestampKey.indexOf("&")+1));
                        //商品
                        if ("product".equals(entity.getBizType())) {
                            String tempretureType = entity.getTempretureType();
                            Integer quantity = entity.getQuantity();
                            // 托数（冷冻、冷藏、恒温、常温）
                            if ("LD".equals(tempretureType)) {
                                Cell cell43 = row.createCell(3);
                                cell43.setCellValue(quantity);
                            } else if ("LC".equals(tempretureType)) {
                                Cell cell43 = row.createCell(4);
                                cell43.setCellValue(quantity);
                            } else if ("HW".equals(tempretureType)) {
                                Cell cell43 = row.createCell(5);
                                cell43.setCellValue(quantity);
                            } else if ("CW".equals(tempretureType)) {
                                Cell cell43 = row.createCell(6);
                                cell43.setCellValue(quantity);
                            }
                            
                            //不是当月的时间，则是上月结余，上月结余的不显示金额
                            if(!entity.getCreateTime().before(startTime)){
                                // 列小计
                                double cost = entity.getCost().doubleValue();
                                if ("LD".equals(tempretureType)) {
                                    Cell cell49 = row.createCell(12-itemMove);
                                    cell49.setCellValue(cost);
                                    ldcost = ldcost + cost;
                                } else if ("LC".equals(tempretureType)) {
                                    Cell cell49 = row.createCell(13-itemMove);
                                    cell49.setCellValue(cost);
                                    lccost = lccost + cost;
                                } else if ("HW".equals(tempretureType)) {
                                    Cell cell49 = row.createCell(14-itemMove);
                                    cell49.setCellValue(cost);
                                    hwcost = hwcost + cost;
                                } else if ("CW".equals(tempretureType)) {
                                    Cell cell49 = row.createCell(15-itemMove);
                                    cell49.setCellValue(cost);
                                    cwcost = cwcost + cost;
                                }
                                // 行小计
                                rowCost = rowCost + cost;
                            }                       
                        }else if ("material".equals(entity.getBizType())) {
                            //耗材
                            double materialCost = entity.getCost().doubleValue();
                            //1.常温--常温
                            //2.冷冻--冷冻、冷藏、恒温
                            if ("CW".equals(entity.getTempretureType())) {
                                Cell cell46 = row.createCell(7);
                                cell46.setCellValue(entity.getQuantity());
                                
                                //不是当月的时间，则是上月结余，上月结余的不显示金额
                                if(!entity.getCreateTime().before(startTime)){
                                    Cell cell49 = row.createCell(16-itemMove);
                                    cell49.setCellValue(materialCost);
                                    //累加行
                                    cwCost = cwCost+materialCost;
                                    //累加列
                                    cwpackcost = cwpackcost+materialCost;
                                }
                            }else {
                                Cell cell47 = row.createCell(8);
                                cell47.setCellValue(entity.getQuantity()+index);
                                index = entity.getQuantity()+index;
                                
                                //不是当月的时间，则是上月结余，上月结余的不显示金额
                                if(!entity.getCreateTime().before(startTime)){
                                    Cell cell50 = row.createCell(17-itemMove);
                                    cell50.setCellValue(materialCost+ldCost);
                                    //累加行
                                    ldCost = materialCost+ldCost;
                                    //累加列
                                    colcost = colcost + materialCost;
                                }   
                            }
                            rowcolCost+= cwCost+ldCost; 
                        }else if ("instock".equals(entity.getBizType()) || "outstock".equals(entity.getBizType())) {
                            //入库/出库
                            double palletCost = entity.getCost().doubleValue();
                            if ("instock".equals(entity.getBizType())) {
                                //入库托数
                                Cell cell70 = row.createCell(10-itemMove);
                                cell70.setCellValue(entity.getAdjustNum()==0?entity.getQuantity():entity.getAdjustNum());
                            }else if ("outstock".equals(entity.getBizType())) {
                                //出库托数
                                Cell cell71 = row.createCell(11-itemMove);
                                cell71.setCellValue(entity.getAdjustNum()==0?entity.getQuantity():entity.getAdjustNum());
                            }else {
                                continue;
                            }   
                            
                            //不是当月的时间，则是上月结余，上月结余的不显示金额
                            if(!entity.getCreateTime().before(startTime)){
                                //处置费小计
                                Cell cell72 = row.createCell(19-move2-itemMove);
                                dispalCost=dispalCost+palletCost;
                                cell72.setCellValue(dispalCost);
                                
                                //累加行
                                incost = rowcolCost+palletCost;
                                //累加列
                                czfcost = czfcost+palletCost;
                                
                                rowcolCost= incost;
                            }                           
                        }
                        
                    }else {
                        ldCost = 0.0;
                        incost = 0.0;
                    }
                }
                
                Integer qty = new Integer(0);
                double cCost = 0.0;
                double rowTotalCost = 0.0;
                double colTotalCost = 0.0;
                
                //商品存储费（按件）
                for (FeesReceiveStorageEntity entity : itemsList) {
                    if ((entity.getCustomerId()+"&"+sdf.format(entity.getCreateTime())).equals(timestampKey)) {                     
                        
                        //商家名称
                        Cell cell40=row.createCell(0);
                        cell40.setCellValue(entity.getCustomerName());      
                        //仓库名称
                        Cell cell41=row.createCell(1);
                        cell41.setCellValue(entity.getWarehouseName()); 
                        //时间
                        Cell cell42=row.createCell(2);
                        cell42.setCellValue(timestampKey.substring(timestampKey.indexOf("&")+1));
                        //库存件数
                        double productCost=0d;
                        if(cusList.contains(entity.getCustomerId())){
                            productCost = entity.getCost().doubleValue();
                        }

                        //不是当月的时间，则是上月结余，上月结余的不显示金额
                        if(!entity.getCreateTime().before(startTime) && newIndex > 0){
                            //存储费按件小计
                            Cell cell61 = row.createCell(18-itemMove);
                            cell61.setCellValue(productCost+cCost);
                            //累加行
                            cCost = cCost + productCost;
                            rowProCost = rowProCost + productCost;
                            //累加列
                            ccfcost = ccfcost+productCost;
                        }   
                        
                        if (itemFlag) {
                            Cell cell60 = row.createCell(9);
                            cell60.setCellValue(entity.getQuantity()+qty);
                            //件数累加
                            qty = qty+entity.getQuantity(); 
                        }                  
                    }else {
                        rowProCost = 0.0;
                    }       
                }          
                
                //行总的+存储费（托）
                rowCost = rowCost+rowcolCost;
                //行总的+存储费（件）
                rowCost = rowCost+cCost;
                // 总计
                totalcost = rowCost + totalcost;
                // 行小计
                Cell cell49 = row.createCell(20-move2-itemMove);
                cell49.setCellValue(rowCost);
            }

            Row row = sheet.createRow(rowIndex);
            Cell cellLast0 = row.createCell(12-itemMove);
            cellLast0.setCellValue(ldcost);

            Cell cellLast1 = row.createCell(13-itemMove);
            cellLast1.setCellValue(lccost);

            Cell cellLast6 = row.createCell(14-itemMove);
            cellLast6.setCellValue(hwcost);

            Cell cellLast2 = row.createCell(15-itemMove);
            cellLast2.setCellValue(cwcost);

            Cell cellLast3 = row.createCell(16-itemMove);
            cellLast3.setCellValue(cwpackcost);
            
            Cell cellLast4 = row.createCell(17-itemMove);
            cellLast4.setCellValue(colcost);
            
            if (newIndex > 0) {
                Cell cellLast5 = row.createCell(18-itemMove);
                cellLast5.setCellValue(ccfcost);
            }
            
            Cell cellLast7 = row.createCell(19-move2-itemMove);
            cellLast7.setCellValue(czfcost);

            Cell cellLast = row.createCell(20-move2-itemMove);
            cellLast.setCellValue(totalcost);
        }
    }
    
    private String getCalInfo(String status) {
        if (StringUtils.isBlank(status)) {
            return "未计算";
        }
        Map<String, String> map = CalculateState.getMap();
        if (map.containsKey(status)) {
            return map.get(status);
        } else {
            return status;
        }
    }

    /**
     * 耗材(分仓)
     * 
     * @param xssfWorkbook
     * @param poiUtil
     * @param condition
     * @param filePath
     * @throws Exception
     */
    private void handMaterial(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil,
            Map<String, Object> condition) throws Exception {
        List<BizDispatchBillEntity> warehouseList = bizDispatchBillService.queryAllWarehouseFromBizData(condition);
        for (BizDispatchBillEntity entity : warehouseList) {
            List<Map<String, Object>> dataPackMaterialList = new ArrayList<Map<String, Object>>();
            int pageNo = 1;
            int size = 20000;
            boolean doLoop = true;
            List<FeesReceiveMaterial> dataList = new ArrayList<FeesReceiveMaterial>();
            //整理Title信息
            condition.put("warehouseCode", entity.getWarehouseCode());
            List<PubMaterialInfoVo> materialInfoList = getAllMaterial();
            List<BizOutstockPackmaterialEntity> ListHead = bizOutstockPackmaterialServiceImpl
                    .getMaterialCodeFromBizData(condition);
            List<String> materialCodeList = getMaterialCodeList(ListHead);
            List<Map<String, Object>> headPackMaterialMapList = getHeadPackMaterialMap(
                    materialCodeList, materialInfoList);

            while (doLoop) {
                PageInfo<FeesReceiveMaterial> packMaterialList = bizOutstockPackmaterialServiceImpl
                        .queryMaterialFromBizData(condition, pageNo, size);
                if (packMaterialList.getList().size() < size) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }

                dataList.addAll(packMaterialList.getList());
            }

            if (dataList.size() == 0) {
                continue;
            }

            //组装数据
            loadData(dataPackMaterialList, dataList, materialInfoList);
            
            poiUtil.exportExcelFilePath(poiUtil, xssfWorkbook,
                    entity.getWarehouseName() + "耗材使用费",
                    headPackMaterialMapList, dataPackMaterialList);    
        }
    }
    
    /**
     * 耗材(不分仓)
     * 
     * @param xssfWorkbook
     * @param poiUtil
     * @param condition
     * @param filePath
     * @throws Exception
     */
    private void handMaterialNotSepWareHouse(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil,
            Map<String, Object> condition) throws Exception {
        List<Map<String, Object>> headPackMaterialMapList = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        // 整理Title信息
        cond.putAll(condition);
        cond.put("warehouseCode","");
        List<PubMaterialInfoVo> materialInfoList = getAllMaterial();
        List<BizOutstockPackmaterialEntity> ListHead = bizOutstockPackmaterialServiceImpl.getMaterialCodeFromBizData(cond);
        List<String> materialCodeList = getMaterialCodeList(ListHead);
        headPackMaterialMapList = getHeadPackMaterialMap(materialCodeList, materialInfoList);
        
        //切分时间，一天一天查
        String startTime =condition.get("startTime") + " 00:00:00";
        String endTime = condition.get("endTime") + " 00:00:00";
        Map<String, String> diffMap = DateUtil.getSplitTime(startTime, endTime, 1);
        int lineNo = 1;
        int size = 20000;
        for (Map.Entry<String, String> entry : diffMap.entrySet()) {
            List<Map<String, Object>> dataPackMaterialList = new ArrayList<Map<String, Object>>();
            logger.info("startTime:["+entry.getKey()+"] endTime["+entry.getValue()+"]");
            cond.put("startTime", entry.getKey());
            cond.put("endTime", entry.getValue());
            
            int pageNo = 1;
            boolean doLoop = true;
            List<FeesReceiveMaterial> dataList = new ArrayList<FeesReceiveMaterial>();
            
            while (doLoop) {
                PageInfo<FeesReceiveMaterial> packMaterialList = bizOutstockPackmaterialServiceImpl
                        .queryMaterialFromBizData(cond, pageNo, size);
                if (packMaterialList.getList().size() < size) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }

                dataList.addAll(packMaterialList.getList());
            }
            
            //组装数据
            loadData(dataPackMaterialList, dataList, materialInfoList);

            if (CollectionUtils.isNotEmpty(headPackMaterialMapList) && CollectionUtils.isNotEmpty(dataPackMaterialList)) {
                poiUtil.exportExcel2FilePath(poiUtil, xssfWorkbook,"耗材使用费", lineNo, headPackMaterialMapList, dataPackMaterialList);
                lineNo += dataPackMaterialList.size();
            }   
        }   
    }
    
    /**
     * 出库单title
     */
    private void handOutstock(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil, Map<String, Object> condition,
            Map<String, String> temMap) throws Exception {
        int pageNo = 1;
        int lineNo = 1;
        boolean doLoop = true;
        while (doLoop) {
            PageInfo<FeesReceiveStorageEntity> pageInfo = 
                    feesReceiveStorageService.queryOutStockPage(condition, pageNo, FileConstant.EXPORTPAGESIZE);
            if (null != pageInfo && pageInfo.getList().size() > 0) {
                if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
                    doLoop = false;
                }else {
                    pageNo += 1; 
                }
            }else {
                doLoop = false;
            }
            
            //头、内容信息
            List<Map<String, Object>> headDetailMapList = getOutHead(); 
            List<Map<String, Object>> dataDetailList = getOutHeadItem(pageInfo.getList(),temMap);
            
            if (!(pageNo == 1 && (pageInfo == null || pageInfo.getList().size() <= 0))) {
                poiUtil.exportExcel2FilePath(poiUtil, xssfWorkbook, "TB", lineNo, headDetailMapList, dataDetailList);
                if (null != pageInfo && pageInfo.getList().size() > 0) {
                    lineNo += pageInfo.getList().size();
                }
            }
        }
    }
    
    /**
     * 出库单内容
     */
    public List<Map<String, Object>> getOutHead(){
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发货时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outstockNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tempretureType");
        headInfoList.add(itemMap);
            

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quantity");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库箱数");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "box");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库重量(吨)");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "weight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "B2B订单操作费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderCost");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库装车费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outHandCost");
        headInfoList.add(itemMap);
        
        return headInfoList;
    }
    
    private List<Map<String, Object>> getOutHeadItem(List<FeesReceiveStorageEntity> list,Map<String, String> temMap){
            List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
            Map<String, Object> dataItem = null;
            Map<String,Map<String, Object>> entityMap=new HashMap<String,Map<String, Object>>();
            for (FeesReceiveStorageEntity entity : list) {
                if(!entityMap.containsKey(entity.getOutstockNo())){
                    dataItem = new HashMap<String, Object>();
                    dataItem.put("warehouseName", entity.getWarehouseName());
                    dataItem.put("createTime", entity.getCreateTime());
                    dataItem.put("customerName", entity.getCustomerName() );
                    dataItem.put("orderType", entity.getOrderType());
                    dataItem.put("outstockNo", entity.getOutstockNo());
                    dataItem.put("tempretureType", temMap.get(entity.getTempretureType()));
                    dataItem.put("quantity", entity.getQuantity());
                    dataItem.put("box", entity.getBox());
                    dataItem.put("weight", entity.getWeight() == null?"":(double)entity.getWeight()/1000);
                    //B2B订单操作费（区分是订单操作费还是出库装车费）
                    if("wh_b2b_work".equals(entity.getSubjectCode())){
                        dataItem.put("orderCost", entity.getCost());
                    }else if("wh_b2b_handwork".equals(entity.getSubjectCode())){
                        dataItem.put("outHandCost", entity.getCost());
                    }
                    dataList.add(dataItem);
                    entityMap.put(entity.getOutstockNo(), dataItem);
                }else{
                    Map<String, Object> data=entityMap.get(entity.getOutstockNo());
                    //B2B订单操作费（区分是订单操作费还是出库装车费）
                    if("wh_b2b_work".equals(entity.getSubjectCode())){
                        data.put("orderCost", entity.getCost());
                    }else if("wh_b2b_handwork".equals(entity.getSubjectCode())){
                        data.put("outHandCost", entity.getCost());
                    }
                }   
            }           
        return dataList;
    }
    
    private void handInstock(SXSSFWorkbook xssfWorkbook, POISXSSUtil poiUtil, Map<String, Object> condition) throws IOException {
        int pageNo = 1;
        int instockLineNo = 1;
        boolean doLoop = true;
        while (doLoop) {
            PageInfo<FeesReceiveStorageEntity> instockList = bmsBizInstockInfoService.queryForBill(condition, pageNo, PAGESIZE);
            if (null != instockList && instockList.getList().size() > 0) {
                if (instockList.getList().size() < PAGESIZE) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }
            } else {
                doLoop = false;
            }

            List<Map<String, Object>> headInstockList = getInstockHead();
            List<Map<String, Object>> dataInstockList = getInstockItem(instockList.getList());

            if (!(pageNo == 1 && (instockList == null || instockList.getList().size() <= 0))) {
                poiUtil.exportExcel2FilePath(poiUtil, xssfWorkbook, "入库", instockLineNo, headInstockList, dataInstockList);
                if (null != instockList && instockList.getList().size() > 0) {
                    instockLineNo += instockList.getList().size();
                }
            }
        }
    }
    
    /**
     * 入库-title
     */
    private List<Map<String, Object>> getInstockHead() {
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收货确认时间");
        itemMap.put("columnWidth", 35);
        itemMap.put("dataKey", "instockDate");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 40);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockType");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNum");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalQty");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalBox");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库重量(吨)");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库操作费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockAmount");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库卸货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "b2cAmount");
        headInfoList.add(itemMap);

        return headInfoList;
    }
    
    /**
     * 入库-content
     */
    private List<Map<String, Object>> getInstockItem(List<FeesReceiveStorageEntity> list) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        DateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> dataItem = null;
        for (FeesReceiveStorageEntity entity : list) {
            if (map.containsKey(entity.getInstockNo())) {
                if ("wh_instock_work".equals(entity.getSubjectCode())) {
                    map.get(entity.getInstockNo()).put("instockAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
                }else if ("wh_b2c_handwork".equals(entity.getSubjectCode())) {
                    map.get(entity.getInstockNo()).put("b2cAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
                }
            }else {
                dataItem = new HashMap<String, Object>();
                dataItem.put("warehouseName", entity.getWarehouseName());
                dataItem.put("instockDate", sdff.format(entity.getInstockDate()));
                dataItem.put("customerName", entity.getCustomerName());
                dataItem.put("instockType", entity.getInstockType());
                dataItem.put("instockNo", entity.getInstockNo());
                dataItem.put("externalNum", entity.getExternalNum());
                dataItem.put("totalQty", entity.getQuantity());
                dataItem.put("totalBox", entity.getBox());
                dataItem.put("totalWeight", entity.getWeight() == null ? "":(double)entity.getWeight()/1000);
                if ("wh_instock_work".equals(entity.getSubjectCode())) {
                    dataItem.put("instockAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
                }else if ("wh_b2c_handwork".equals(entity.getSubjectCode())) {
                    dataItem.put("b2cAmount", entity.getCost()!=null?entity.getCost().doubleValue():BigDecimal.ZERO);
                }
                map.put(entity.getInstockNo(), dataItem);
                dataList.add(dataItem);
            }           
        }
        return dataList;
    }
    
    /**
     * 预账单增值
     * 
     * @param workbook
     * @param poiUtil
     * @param condition
     * @param filePath
     * @throws IOException
     */
    private void handAdd(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition)
            throws IOException {
        int pageNo = 1;
        int addLineNo = 1;
        boolean doLoop = true;
        totalAmount.set(0d);// 置零

        // 仓储增值类型
        Map<String, String> subjectMap = bmsGroupSubjectService.getExportSubject("receive_wh_valueadd_subject");

        while (doLoop) {
            PageInfo<FeesReceiveStorageEntity> pageList = feesReceiveStorageService.queryPreBillStorageAddFee(condition,
                    pageNo, PAGESIZE);
            if (null != pageList && pageList.getList() != null && pageList.getList().size() > 0) {
                if (pageList.getList().size() < PAGESIZE) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }
            } else {
                doLoop = false;
                return;
            }

            List<Map<String, Object>> headAddList = getAddHead();
            List<Map<String, Object>> dataAddList = getAddItem(pageList.getList(), subjectMap);
            if (!doLoop) {
                // 添加合计
                dataAddList.addAll(getAddSumItem());
            }

            poiUtil.exportExcel2FilePath(poiUtil, workbook, "增值", addLineNo, headAddList, dataAddList);
            if (null != pageList && pageList.getList().size() > 0) {
                addLineNo += pageList.getList().size();
            }
        }
    }

    /**
     * 增值-title
     */
    private List<Map<String, Object>> getAddHead() {
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "wmsId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "客户名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值项目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "subjectCode");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值服务内容");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "serviceContent");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quality");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单位");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unit");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);

        return headInfoList;
    }

    /**
     * 增值-content
     */
    private List<Map<String, Object>> getAddItem(List<FeesReceiveStorageEntity> list, Map<String, String> dictcodeMap) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = null;
        double t_amount = 0d;
        double amount = 0d;
        for (FeesReceiveStorageEntity entity : list) {
            dataItem = new HashMap<String, Object>();
            dataItem.put("wmsId", entity.getWmsId());
            dataItem.put("createTime", sdf.format(entity.getCreateTime()));
            dataItem.put("warehouseName", entity.getWarehouseName());
            dataItem.put("customerName", entity.getCustomerName());
            dataItem.put("subjectCode",
                    entity.getOtherSubjectCode() == null ? "" : dictcodeMap.get(entity.getOtherSubjectCode()));
            dataItem.put("serviceContent", entity.getServiceContent());
            dataItem.put("quality", entity.getExactQuantity());
            dataItem.put("unit", entity.getUnit());
            dataItem.put("unitPrice", entity.getUnitPrice());
            if (null != entity.getCost()) {
                amount = entity.getCost().doubleValue();
            }
            t_amount += amount;
            dataItem.put("amount", amount);
            dataItem.put("remark", entity.getRemarkContent());
            dataList.add(dataItem);
        }
        totalAmount.set(totalAmount.get() + t_amount);
        return dataList;
    }

    /**
     * 增值-合计
     */
    private List<Map<String, Object>> getAddSumItem() {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = new HashMap<String, Object>();
        dataItem.put("unitPrice", "合计金额");
        dataItem.put("amount", totalAmount.get());
        dataList.add(dataItem);
        return dataList;
    }
    
    /**
     * 理赔
     * 
     * @param workbook
     * @param poiUtil
     * @param condition
     * @param filePath
     * @throws Exception
     */
    private void handAbnormal(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition) throws Exception {
        int pageNo = 1;
        int abnormalLineNo = 1;
        boolean doLoop = true;
        totalProductAmount.set(0d);// 置零
        totalDeliveryCost.set(0d);
        totalReturnAmount.set(0d);

        while (doLoop) {
            PageInfo<FeesAbnormalEntity> abnormalList = feesAbnormalService.queryPreBillAbnormal(condition, pageNo,
                    PAGESIZE);
            if (null != abnormalList && abnormalList.getList().size() > 0) {
                if (abnormalList.getList().size() < PAGESIZE) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }
            } else {
                doLoop = false;
            }

            List<Map<String, Object>> headAbnormalList = getAbnormalHead();
            List<Map<String, Object>> dataAbnormalList = getAbnormalItem(abnormalList.getList());
            if (!doLoop) {
                // 添加合计
                dataAbnormalList.addAll(getAbnormalSumItem());
            }
            if (!(pageNo == 1 && (abnormalList == null || abnormalList.getList().size() <= 0))) {
                poiUtil.exportExcel2FilePath(poiUtil, workbook, "理赔", abnormalLineNo, headAbnormalList, dataAbnormalList);
                if (null != abnormalList && abnormalList.getList().size() > 0) {
                    abnormalLineNo += abnormalList.getList().size();
                }
            }
        }
    }

    /**
     * 理赔-title
     */
    private List<Map<String, Object>> getAbnormalHead() {
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发货仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expressnum");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "客户");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "责任方");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "dutyType");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔付类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payType");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔付商品金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productAmountJ2c");
        headInfoList.add(itemMap);

//      itemMap = new HashMap<String, Object>();
//      itemMap.put("title", "赔付运费");
//      itemMap.put("columnWidth", 25);
//      itemMap.put("dataKey", "deliveryCost");
//      headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否免运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isDeliveryFreeJ2c");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "登记人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createPersonName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);

        return headInfoList;
    }

    /**
     * 理赔-content
     */
    private List<Map<String, Object>> getAbnormalItem(List<FeesAbnormalEntity> list) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = null;
        double t_productAmount = 0d;
        double t_deliveryCost = 0d;
        for (FeesAbnormalEntity entity : list) {
            dataItem = new HashMap<String, Object>();
            dataItem.put("warehouseName", entity.getWarehouseName());
            dataItem.put("createTime", sdf.format(entity.getCreateTime()));
            dataItem.put("expressnum", entity.getExpressnum());
            dataItem.put("customerName", entity.getCustomerName());
            dataItem.put("dutyType", entity.getReason());
            dataItem.put("payType", entity.getReasonDetail());
            double productAmount=entity.getProductAmountJ2c()==null?0d:entity.getProductAmountJ2c();
            t_productAmount+=productAmount;
            dataItem.put("productAmountJ2c", productAmount);
            double deliveryCost=entity.getDeliveryCost()==null?0d:entity.getDeliveryCost();
            t_deliveryCost+=deliveryCost;
            if("0".equals(entity.getIsDeliveryFreeJ2c())){
                dataItem.put("isDeliveryFreeJ2c", "否"); 
            }else if("1".equals(entity.getIsDeliveryFreeJ2c())){
                dataItem.put("isDeliveryFreeJ2c", "是");
            }
            dataItem.put("createPersonName", entity.getCreatePersonName());
            dataItem.put("remark", entity.getRemark());
            dataList.add(dataItem);
        }

        totalProductAmount.set(totalProductAmount.get() + t_productAmount);
        totalDeliveryCost.set(totalDeliveryCost.get() + t_deliveryCost);
        return dataList;
    }

    /**
     * 理赔-合计
     */
    private List<Map<String, Object>> getAbnormalSumItem() {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = new HashMap<String, Object>();
        dataItem.put("expressnum", "合计金额");
        dataItem.put("productAmountJ2c", totalProductAmount.get());
        dataItem.put("deliveryCost", totalDeliveryCost.get());
        dataList.add(dataItem);
        return dataList;
    }
    
    /**
     * 改地址推荐费
     * 
     * @param poiUtil
     * @param workbook
     * @param path
     * @param billno
     * @throws IOException
     */
    private void handAbnormalChange(SXSSFWorkbook workbook, POISXSSUtil poiUtil, Map<String, Object> condition) throws IOException {
        int pageNo = 1;
        int abnormalLineNo = 1;
        boolean doLoop = true;
        totalProductAmount.set(0d);// 置零
        totalDeliveryCost.set(0d);
        totalReturnAmount.set(0d);
        
        while (doLoop) {
            PageInfo<FeesAbnormalEntity> abnormalList = feesAbnormalService.queryPreBillAbnormalChange(condition,
                    pageNo, PAGESIZE);
            if (null != abnormalList && abnormalList.getList().size() > 0) {
                if (abnormalList.getList().size() < PAGESIZE) {
                    doLoop = false;
                } else {
                    pageNo += 1;
                }
            } else {
                doLoop = false;
            }

            List<Map<String, Object>> headAbnormalChangeList = getAbnormalChangeHead();
            List<Map<String, Object>> dataAbnormalChangeList = getAbnormalChangeItem(abnormalList.getList());
            if (!doLoop) {
                // 添加合计
                dataAbnormalChangeList.addAll(getAbnormalChangeSumItem());
            }
            if (!(pageNo == 1 && (abnormalList == null || abnormalList.getList().size() <= 0))) {
                poiUtil.exportExcel2FilePath(poiUtil, workbook, "改地址和退件费", abnormalLineNo, headAbnormalChangeList,
                        dataAbnormalChangeList);
                if (null != abnormalList && abnormalList.getList().size() > 0) {
                    abnormalLineNo += abnormalList.getList().size();
                }
            }
        }
    }

    /**
     * 改地址推荐费-title
     */
    private List<Map<String, Object>> getAbnormalChangeHead() {
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发货仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expressnum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "退货单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "returnOrderno");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "客户");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "责任方");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dutyType");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔付类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payType");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "returnedAmountC2j");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "登记人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createPersonName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);

        return headInfoList;
    }

    /**
     * 改地址推荐费-content
     */
    private List<Map<String, Object>> getAbnormalChangeItem(List<FeesAbnormalEntity> list) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = null;
        double t_amount = 0d;

        for (FeesAbnormalEntity entity : list) {
            dataItem = new HashMap<String, Object>();       
            dataItem.put("warehouseName", entity.getWarehouseName());
            dataItem.put("createTime", sdf.format(entity.getCreateTime()));
            dataItem.put("expressnum", entity.getExpressnum());
            dataItem.put("returnOrderno", entity.getReturnOrderno());
            dataItem.put("customerName", entity.getCustomerName());
            dataItem.put("dutyType", entity.getReason());
            dataItem.put("payType", entity.getReasonDetail());
            double amount = (entity.getReturnedAmountC2j() == null ? 0 : entity.getReturnedAmountC2j());
            t_amount += amount;
            dataItem.put("returnedAmountC2j", amount);
            dataItem.put("createPersonName", entity.getCreatePersonName());
            dataItem.put("remark", entity.getRemark());
            dataList.add(dataItem);
        }

        totalReturnAmount.set(totalReturnAmount.get() + t_amount);
        return dataList;
    }

    /**
     * 改地址推荐费-合计
     */
    private List<Map<String, Object>> getAbnormalChangeSumItem() {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = new HashMap<String, Object>();
        dataItem.put("expressnum", "合计金额");
        dataItem.put("returnedAmountC2j", totalReturnAmount.get());
        dataList.add(dataItem);
        return dataList;
    }
    
    /*
     * 解析JSON---->Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> resolveJsonToMap(String json) {
        //解析JSON
        Map<String, Object> map = null;
        try {
            map = (Map<String, Object>)JSON.parse(json);
        } catch (Exception e) {
            logger.error("JSON解析异常 {}", e);
            return null;
        }
        return map;
    }
    
    /**
     * 更新导出任务表
     * 
     * @param taskId
     * @param process
     * @param taskState
     * @param remark
     * @param filePath
     */
    private void updateExportTask(String taskId, String taskState, double process, String remark, String filePath) {
        BillPrepareExportTaskEntity entity = new BillPrepareExportTaskEntity();
        if (StringUtils.isNotEmpty(taskState)) {
            entity.setTaskState(taskState);
        }
        if (StringUtils.isNotEmpty(remark)) {
            entity.setRemark(remark);
        }
        if (StringUtils.isNotEmpty(filePath)) {
            entity.setFilePath(filePath);
        }
        if (!DoubleUtil.isBlank(process)) {
            entity.setProgress(process);
        }
        entity.setTaskId(taskId);
        billPrepareExportTaskService.update(entity);
    }
    
    /**
     * 初始化数据
     */
    private void init() {
        mapWarehouse = getwarehouse();
    }
    
    private Map<String, String> getwarehouse() {
        List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (WarehouseVo warehouseVo : warehouseVos) {
            map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
        }
        return map;
    }
    
    /**
     * 温度类型
     * @return
     */
    @DataProvider
    public Map<String, String> getEnumList(String type){
        List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList(type);
        Map<String, String> map =new LinkedHashMap<String,String>();
        if(systemCodeList!=null && systemCodeList.size()>0){
            for(int i=0;i<systemCodeList.size();i++){
                map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
            }
        }
        return map;
    }
    
    /*
     * 统计费用表的仓库
     */
    public List<String> queryPreBillWarehouse(Map<String, Object> param) {
        return feesReceiveStorageService.queryPreBillWarehouse(param);
    }
    
    /*
     * 获取未作废的全部耗材
     */
    private List<PubMaterialInfoVo> getAllMaterial() {
        Map<String, Object> conditionMap = Maps.newHashMap();
        conditionMap.put("delFlag", 0);
        return pubMaterialInfoService.queryList(conditionMap);
    }
    
    private List<String> getMaterialCodeList(List<BizOutstockPackmaterialEntity> ListHead) {
        List<String> materialCodeList = new ArrayList<String>();
        for (BizOutstockPackmaterialEntity entity : ListHead) {
            materialCodeList.add(entity.getConsumerMaterialCode());
        }
        return materialCodeList;
    }
    
    private List<Map<String, Object>> getHeadPackMaterialMap(List<String> materialCodeList,
            List<PubMaterialInfoVo> materialInfoList) {
        Map<String, String> headMap = getHeadMap(materialCodeList, materialInfoList);
        List<Map<String, Object>> headMapList = new ArrayList<Map<String, Object>>();
        Set<String> set = headMap.keySet();
        for (String key : set) {
            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap = new HashMap<String, Object>();
            itemMap.put("title", headMap.get(key));
            itemMap.put("columnWidth", 25);
            itemMap.put("dataKey", key);
            headMapList.add(itemMap);
        }
        return headMapList;
    }
    
    /*
     * 耗材的title
     */
    private Map<String, String> getHeadMap(List<String> materialCodeList, List<PubMaterialInfoVo> materialInfoList) {
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("warehouseName", "仓库");
        map.put("customerName", "商家名称");
        map.put("waybillNo", "运单号");
        map.put("outstockNo", "出库单号");
        map.put("totalqty", "商品总数");
        map.put("productDetail", "商品和数量");
        map.put("externalNo", "外部物流号");
        map.put("carrierName", "物流商简称");
        map.put("createTime", "接单时间");
        map.put("receiveProvinceId", "收件人省");
        map.put("receiveCityId", "收件人市");
        map.put("receiveDetailAddress", "收件人地址");
        map.put("packPlanNo", "包材方案编号");
        map.put("packGroupNo", "包材组编号");
        map.put("packPlanName", "包材方案名称");
        map.put("packPlanCost", "包材方案金额");
        
        // 按序号获取systemCode表中的类别并排序
        Map<String, Object> param = new HashMap<>();
        param.put("typeCode", "PACKMAGERIAL_SORT");
        List<SystemCodeEntity> entityList = systemCodeService.queryBySortNo(param);
        final List<String> orderList = new ArrayList<>();
        for (SystemCodeEntity entity : entityList) {
            orderList.add(entity.getCodeName());
        }

        List<String> materialTypeList = new ArrayList<String>();
        HashMap<String, Object> codeMap = new HashMap<>();
        for (String code : materialCodeList) {
            String marterialType = getMaterialType(materialInfoList, code);

            if (!materialTypeList.contains(marterialType)) {
                materialTypeList.add(marterialType);
                codeMap.put(marterialType, code);
            }
        }

        // 按上述list进行排序
        Collections.sort(materialTypeList, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                
                if (orderList.indexOf(o1) >= 0 && orderList.indexOf(o2) >= 0) {
                    return orderList.indexOf(o1)-orderList.indexOf(o2);
                }else if (orderList.indexOf(o1) >= 0 && orderList.indexOf(o2) <= 0) {
                    return -1;
                }else if (orderList.indexOf(o1) <= 0 && orderList.indexOf(o2) >= 0) {
                    return 1;
                }else {
                    return 0;
                }
            }
        });

        // 遍历输出
        for (String marterialTypeDetail : materialTypeList) {
            map.put(marterialTypeDetail + "_name", marterialTypeDetail);
            map.put(marterialTypeDetail + "_code", "编码");
            map.put(marterialTypeDetail + "_type", "规格");
            if (((String) codeMap.get(marterialTypeDetail)).contains("GB")) {
                map.put(marterialTypeDetail + "_count", "重量");
            } else {
                map.put(marterialTypeDetail + "_count", "数量");
            }
            map.put(marterialTypeDetail + "_unitprice", "单价");
            map.put(marterialTypeDetail + "_cost", "金额");
        }
        map.put("totalCost", "合计");
        return map;
    }
    
    /*
     * 获取耗材类型
     */
    private String getMaterialType(List<PubMaterialInfoVo> materialInfoList, String materialCode) {
        for (PubMaterialInfoVo infoVo : materialInfoList) {
            if (StringUtils.isNotBlank(infoVo.getBarcode()) && infoVo.getBarcode().equals(materialCode)) {
                return infoVo.getMaterialType();
            }
        }
        return "其他";
    }
    
    /*
     * 组装耗材的数据
     */
    private void loadData(List<Map<String, Object>> dataPackMaterialList, List<FeesReceiveMaterial> dataList,
            List<PubMaterialInfoVo> materialInfoList) {
        for (FeesReceiveMaterial materialEntity : dataList) {
            boolean flag = false;
            Map<String, Object> matchMap = null;
            for (Map<String, Object> map : dataPackMaterialList) {
                if (map.get("waybillNo").equals(
                        materialEntity.getWaybillNo())) {
                    flag = true;
                    matchMap = map;
                    break;
                }
            }
            if (flag) {
                // 检查耗材类型
                String marterialType = getMaterialType(materialInfoList,
                        materialEntity.getProductNo());
                if (matchMap.containsKey(marterialType + "_name")) {
                    matchMap.put(
                            marterialType + "_name",
                            matchMap.get(marterialType + "_name") + ","
                                    + materialEntity.getProductName() == null ? ""
                                    : materialEntity.getProductName());
                    if (materialEntity.getProductNo().contains("GB")) {
                        matchMap.put(
                                marterialType + "_count",
                                matchMap.get(marterialType + "_count")
                                        + "," + materialEntity.getWeight() == null ? ""
                                        : Double.valueOf(materialEntity.getWeight()));
                    } else {
                        matchMap.put(
                                marterialType + "_count",
                                matchMap.get(marterialType + "_count")
                                        + ","
                                        + materialEntity.getQuantity() == null ? ""
                                        : Double.valueOf(materialEntity.getQuantity()));
                    }
                    matchMap.put(marterialType + "_cost",
                            matchMap.get(marterialType + "_cost") + ","
                                    + materialEntity.getCost() == null ? ""
                                    : Double.valueOf(materialEntity.getCost()));
                    double totleCost = matchMap.get("totalCost") == null ? 0d
                            : Double.parseDouble(matchMap.get("totalCost")
                                    .toString());
                    totleCost += materialEntity.getCost() == null ? 0d
                            : Double.valueOf(materialEntity.getCost());
                    matchMap.put("totalCost", totleCost);// 金额
                } else {
                    matchMap.put(marterialType + "_name",
                            materialEntity.getProductName() == null ? ""
                                    : materialEntity.getProductName());
                    if (StringUtils.isNotBlank(materialEntity.getProductNo())) {
                        if (materialEntity.getProductNo().contains("GB")) {
                            matchMap.put(marterialType + "_count",
                                    materialEntity.getWeight() == null ? ""
                                            : Double.valueOf(materialEntity.getWeight()));
                        } else {
                            matchMap.put(marterialType + "_count",
                                    materialEntity.getQuantity() == null ? ""
                                            : Double.valueOf(materialEntity.getQuantity()));
                        }
                    }   
                    matchMap.put(marterialType + "_code",
                            materialEntity.getProductNo() == null?"":materialEntity.getProductNo());
                    matchMap.put(marterialType + "_type",
                            materialEntity.getSpecDesc());
                    matchMap.put(marterialType + "_unitprice",
                            materialEntity.getUnitPrice() == null ? ""
                                    : Double.valueOf(materialEntity.getUnitPrice()));
                    matchMap.put(marterialType + "_cost", materialEntity
                            .getCost() == null ? "" : Double.valueOf(materialEntity.getCost()));
                    double totleCost = matchMap.get("totalCost") == null ? 0d
                            : Double.parseDouble(matchMap.get("totalCost")
                                    .toString());
                    totleCost += materialEntity.getCost() == null ? 0d
                            : materialEntity.getCost();
                    matchMap.put("totalCost", totleCost);// 金额
                }
            } else {
                Map<String, Object> dataItem = new HashMap<String, Object>();
                dataItem.put("warehouseName",
                        materialEntity.getWarehouseName());
                dataItem.put("customerName",
                        materialEntity.getCustomerName());
                dataItem.put("waybillNo", materialEntity.getWaybillNo());
                dataItem.put("outstockNo", materialEntity.getOutstockNo());
                dataItem.put("totalqty", materialEntity.getTotalqty());
                dataItem.put("productDetail",
                        materialEntity.getProductDetail());
                dataItem.put("externalNo", materialEntity.getExternalNo());
                dataItem.put("carrierName", materialEntity.getCarrierName());
                dataItem.put("createTime", materialEntity.getCreateTime());
                dataItem.put("receiveProvinceId",
                        materialEntity.getReceiveProvinceId());
                dataItem.put("receiveCityId",
                        materialEntity.getReceiveCityId());
                dataItem.put("receiveDetailAddress",
                        materialEntity.getReceiveDetailAddress());
                //新增标准包装方案字段
                dataItem.put("packPlanNo", materialEntity.getPackPlanNo());
                dataItem.put("packGroupNo", materialEntity.getPackGroupNo());
                dataItem.put("packPlanName", materialEntity.getPackPlanName());
                dataItem.put("packPlanCost", materialEntity.getPackPlanCost());

                //只走标准包装方案，无多余耗材的情况
                if (StringUtils.isNotBlank(materialEntity.getProductNo())) {
                    String marterialType = getMaterialType(materialInfoList,
                            materialEntity.getProductNo()==null?"":materialEntity.getProductNo());
                    dataItem.put(marterialType + "_name",
                            materialEntity.getProductName()==null?"":materialEntity.getProductName());
                    dataItem.put(marterialType + "_code",
                            materialEntity.getProductNo()==null?"":materialEntity.getProductNo());
                    dataItem.put(marterialType + "_type",
                            materialEntity.getSpecDesc()==null?"":materialEntity.getSpecDesc());
                    if (materialEntity.getProductNo().contains("GB")) {
                        dataItem.put(marterialType + "_count", materialEntity
                                .getWeight() == null ? "" : Double.valueOf(materialEntity
                                .getWeight()));
                    } else {
                        dataItem.put(marterialType + "_count", materialEntity
                                .getQuantity() == null ? "" : Double.valueOf(materialEntity
                                .getQuantity()));
                    }
                    dataItem.put(marterialType + "_unitprice", materialEntity
                            .getUnitPrice() == null ? "" : Double.valueOf(materialEntity
                            .getUnitPrice()));
                    dataItem.put(marterialType + "_cost", materialEntity
                            .getCost() == null ? "" :Double.valueOf(materialEntity.getCost()));
                    // 第一次加上包材的金额
                    dataItem.put("totalCost", materialEntity.getCost()
                            + (materialEntity.getPackPlanCost() == null ? 0d : materialEntity.getPackPlanCost()));
                }else {
                    dataItem.put("totalCost", materialEntity.getPackPlanCost());// 包材金额
                }                   
                dataPackMaterialList.add(dataItem);
            }

        }
    }
    
    /**
     * 干线费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月25日 上午10:12:20
     *
     * @param taskId
     * @param workbook
     * @param poiUtil
     * @param condition
     * @param entity
     * @throws Exception
     */
    private void handTransport(String taskId, SXSSFWorkbook workbook, POISXSSUtil poiUtil,
            Map<String, Object> condition, BillPrepareExportTaskEntity entity) throws Exception {
        int result = 0;
        paytotalAmount.set(0d);
        Map<String, Object> cond = new HashMap<String, Object>();
        Map<String, String> cuMap = new HashMap<String, String>();
        List<Map<String, String>> cuList = new ArrayList<Map<String, String>>();
        List<FeesTransportMasterEntity> transportList = new ArrayList<FeesTransportMasterEntity>();

        // 拼接年月
//        String year = "";
//        String month = "";
//        String creMonth = "";
//        String startTime = "";
//        String endTime = "";
//        creMonth = exchangeDate(condition, year, month);
//        startTime = creMonth + "-01 00:00:00";
//        endTime = DateUtil.getLastDay(creMonth+"-01") + " 23:59:59";
        cond.put("beginTime", condition.get("startTime"));
        cond.put("endTime", condition.get("endTime"));
        cond.put("delFlag", "0");

        String customerId = condition.get("customerid").toString();
        // 区分是否按照子商家生成
        if (!(Boolean) condition.get("isChildCustomer")) {
            cuList = billPrepareExportTaskService.getChildCustomer(customerId);
        } else {
            cuMap.put("customerId", customerId);
            cuList.add(cuMap);
        }

        for (Map<String, String> map : cuList) {
            try {
                result = tmsForOmsService.getAccountBillOrderCount(map.get("customerId"), condition.get("createMonth").toString());
            } catch (Exception e) {
                logger.error("调用TMS接口异常：", e);
                throw new BizException("调用TMS接口异常:", e);
            }
            
            // TMS无计算费用
            if (result == 0) {
                continue; 
            }
            
            cond.put("customerId", map.get("customerId"));
            transportList = feesTransportMasterService.queryForPrepareBill(cond);
            if (result != transportList.size()) {
                //TMS条数和BMS干线条数不等
                updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 0,StringUtils.isBlank(entity.getRemark())?"该商家" + condition.get("month").toString() + "月有干线计费未提交至BMS，请联系干线运营人员及时提交结算数据，提交后重新生成预账单，谢谢！": 
                            entity.getRemark()+";该商家" + condition.get("month").toString()+"月有干线计费未提交至BMS，请联系干线运营人员及时提交结算数据，提交后重新生成预账单，谢谢！", null);
                continue;
            }
            
            
        }
        
        if (CollectionUtils.isEmpty(transportList)) {
            return;
        }
        
        List<Map<String, Object>> headTransportList = getTransportHead();
        List<Map<String, Object>> dataTransportList = getTransportItem(transportList);
        dataTransportList.addAll(getTransportSumItem());
        
        poiUtil.exportExcel2FilePath(poiUtil, workbook, "干线", 1, headTransportList, dataTransportList);

    }

    /*
     * 日期转换
     */
    private String exchangeDate(Map<String, Object> condition, String year, String month) {
        String creMonth;
        if (condition.containsKey("year") && condition.containsKey("month")) {
            year = condition.get("year").toString();
            month = condition.get("month").toString();
        }
        if (Integer.parseInt(month) < 10) {
            creMonth = year + "-0" + month;
        } else {
            creMonth = year + "-" + month;
        }
        return creMonth;
    }

    
    /**
     * 干线费-title
     */
    private List<Map<String, Object>> getTransportHead() {
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "订单创建日期");
        itemMap.put("columnWidth", 30);
        itemMap.put("dataKey", "creDate");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运输订单号");
        itemMap.put("columnWidth", 30);
        itemMap.put("dataKey", "orderNo");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部订单号");
        itemMap.put("columnWidth", 30);
        itemMap.put("dataKey", "outstockNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温控");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureTypeCode");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "派车单号");
        itemMap.put("columnWidth", 30);
        itemMap.put("dataKey", "routeNo");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调度人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dispatcherName");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "交接单号");
        itemMap.put("columnWidth", 30);
        itemMap.put("dataKey", "transportNo");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家全称");
        itemMap.put("columnWidth", 40);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "承运产品");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "projectName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发站");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendSite");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发省份");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendProvince");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发城市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendCity");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendDistrict");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的站");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveSite");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的省份");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveProvince");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的城市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveCity");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveDistrict");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的地址");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveAddress");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "体积");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "actualVolume");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "actualWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否泡货");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isLight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "车型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "capacityTypeCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "实发箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "actualPackingQty");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "实发件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "actualGoodsQty");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "实收箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiptPackingQty");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "实收件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiptGoodsQty");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否退货");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isBacktrack");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发货日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "beginDate");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收货日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "endDate");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "提货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsTakes");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsTransAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "送货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsSend");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "装货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ysZh");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "卸货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ysXh");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "逆向物流费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsReverseLogistic");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "延时等待费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsDelayWaiting");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "缠绕膜费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsWrappingFilm");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "放空费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsEmptying");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔付费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsClaim");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "纸面回单费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ysZmhd");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "拆箱费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ysCx");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "贴码费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ysTm");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "保险费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsInsurance");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "分流费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsFl");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "上楼搬运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsSlby");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "过夜制冷费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsGyzl");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据打印费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsDjdy");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "加点费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsAddSite");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "中转费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsZz");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "垫付费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsDf");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "押车费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsYc");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "理货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsTallying");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "交货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsJh");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "过路费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsGl");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "码货费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsMh");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "托盘费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tsPallet");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "应付总计");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payTotalAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "客户是否需要保险");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "needInsurance");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);

        return headInfoList;
    }
    
    /**
     * 干线费-content
     */
    private List<Map<String, Object>> getTransportItem(List<FeesTransportMasterEntity> list) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        double t_payTotalAmount = 0d;
        Map<String, Object> dataItem = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (FeesTransportMasterEntity entity : list) {
            dataItem = new HashMap<String, Object>();       
            dataItem.put("creDate", entity.getCreatedDt()==null?"":sdf.format(entity.getCreatedDt()));
            dataItem.put("orderNo", entity.getOrderNo());
            dataItem.put("outstockNo", entity.getOutstockNo());
            dataItem.put("temperatureTypeCode", entity.getTemperatureTypeCode()==null?"":BmsEnums.tempretureType.getDesc(entity.getTemperatureTypeCode()));
            dataItem.put("routeNo", entity.getRouteNo());
            dataItem.put("dispatcherName", entity.getDispatcherName());
            dataItem.put("transportNo", entity.getTransportNo());
            dataItem.put("customerName", entity.getCustomerName());
            dataItem.put("projectName", entity.getProjectName());
            dataItem.put("sendSite", entity.getSendSite());
            dataItem.put("sendProvince", entity.getSendProvince());
            dataItem.put("sendCity", entity.getSendCity());
            dataItem.put("sendDistrict", entity.getSendDistrict());
            dataItem.put("receiveSite", entity.getReceiveSite());
            dataItem.put("receiveProvince", entity.getReceiveProvince());
            dataItem.put("receiveCity", entity.getReceiveCity());
            dataItem.put("receiveDistrict", entity.getReceiveDistrict());
            dataItem.put("receiveAddress", entity.getReceiveAddress());
            dataItem.put("actualVolume", entity.getActualVolume()==null?"":entity.getActualVolume().doubleValue());
            dataItem.put("actualWeight", entity.getActualWeight()==null?"":entity.getActualWeight().doubleValue());
            dataItem.put("isLight", entity.getLight()==null?"":BmsEnums.light.getDesc(entity.getLight()));
            //车型
            dataItem.put("capacityTypeCode", entity.getCapacityTypeCode());
            dataItem.put("actualPackingQty", entity.getActualPackingQty()==null?"":entity.getActualPackingQty().doubleValue());
            dataItem.put("actualGoodsQty", entity.getActualGoodsQty()==null?"":entity.getActualGoodsQty().doubleValue());
            dataItem.put("receiptPackingQty", entity.getReceiptPackingQty()==null?"":entity.getReceiptPackingQty().doubleValue());
            dataItem.put("receiptGoodsQty", entity.getReceiptGoodsQty()==null?"":entity.getReceiptGoodsQty().doubleValue());
            dataItem.put("isBacktrack", entity.getHasBacktrack()==null?"":BmsEnums.hasBacktrack.getDesc(entity.getHasBacktrack()));
            dataItem.put("beginDate", entity.getBeginTime()==null?"":sdf.format(entity.getBeginTime()));
            dataItem.put("endDate", entity.getEndTime()==null?"":sdf.format(entity.getEndTime()));
            dataItem.put("tsTakes", entity.getTsTakes());
            dataItem.put("tsTransAmount", entity.getTsTransAmount());
            dataItem.put("tsSend", entity.getTsSend());
            dataItem.put("ysZh", entity.getYsZh());
            dataItem.put("ysXh", entity.getYsXh());
            dataItem.put("tsReverseLogistic", entity.getTsReverseLogistic());
            dataItem.put("tsDelayWaiting", entity.getTsDelayWaiting());
            dataItem.put("tsWrappingFilm", entity.getTsWrappingFilm());
            dataItem.put("tsEmptying", entity.getTsEmptying());
            dataItem.put("tsClaim", entity.getTsClaim());
            dataItem.put("ysZmhd", entity.getYsZmhd());
            dataItem.put("ysCx", entity.getYsCx());
            dataItem.put("ysTm", entity.getYsTm());
            dataItem.put("tsInsurance", entity.getTsInsurance());
            dataItem.put("tsFl", entity.getTsFl());
            dataItem.put("tsSlby", entity.getTsSlby());
            dataItem.put("tsGyzl", entity.getTsGyzl());
            dataItem.put("tsDjdy", entity.getTsDjdy());
            dataItem.put("tsAddSite", entity.getTsAddSite());
            dataItem.put("tsZz", entity.getTsZz());
            dataItem.put("tsDf", entity.getTsDf());
            dataItem.put("tsYc", entity.getTsYc());
            dataItem.put("tsTallying", entity.getTsTallying());
            dataItem.put("tsJh", entity.getTsJh());
            dataItem.put("tsGl", entity.getTsGl());
            dataItem.put("tsMh", entity.getTsMh());
            dataItem.put("tsPallet", entity.getTsPallet());
            dataItem.put("payTotalAmount", entity.getPayTotalAmount());
            t_payTotalAmount += entity.getPayTotalAmount()==null?0d:entity.getPayTotalAmount();
            dataItem.put("needInsurance", entity.getNeedInsurance()==null?"":BmsEnums.needInsurance.getDesc(entity.getNeedInsurance()));
            dataItem.put("remark", entity.getRemark());            
            dataList.add(dataItem);
        }
        
        paytotalAmount.set(t_payTotalAmount);
        return dataList;
    }
    
    /**
     * 干线-合计
     */
    private List<Map<String, Object>> getTransportSumItem() {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataItem = new HashMap<String, Object>();
        dataItem.put("tsPallet", "合计金额");
        dataItem.put("payTotalAmount", paytotalAmount.get());
        dataList.add(dataItem);
        return dataList;
    }
    
}


