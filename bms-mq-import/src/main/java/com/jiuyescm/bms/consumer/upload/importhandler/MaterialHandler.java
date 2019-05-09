/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.consumer.upload.importhandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.consumer.common.BmsMaterialImportTask;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
  * <耗材系统导入处理>
  * 
  * @author wangchen870
  * @date 2019年5月9日 上午9:57:46
  */
@Component("materialHandler")
@Scope("prototype")
public class MaterialHandler {

    private static final Logger logger = LoggerFactory.getLogger(MaterialHandler.class);
    
    @Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
    @Autowired private StorageClient storageClient;
    @Autowired private BmsMaterialImportTask bmsMaterialImportTaskCommon;
    @Autowired private IBizOutstockPackmaterialTempService bizOutstockPackmaterialTempService;
    @Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
    @Autowired private IBmsProductsMaterialService bmsProductsMaterialService;
    
    
    private static final String REMARK = "导入数据不规范,请下载查看最后一列说明";
    private Map<String,WarehouseVo> wareHouseMap = null;
    private Map<String,CustomerVo> customerMap = null;
    private Map<String,PubMaterialInfoVo> materialMap = null;
    
    private Map<Integer, String> errMap = null;
    private Map<Integer, String> errorMap = null;
    private Map<String,Integer> repeatMap = null;
    
    List<BizOutstockPackmaterialTempEntity> newList = new ArrayList<BizOutstockPackmaterialTempEntity>();
    private TreeMap<Integer,String> originColumn = null; //源生表头信息
    private List<String> materialCommonList = null;
    
    BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
    
    private String taskId;
    private int batchNum = 1000;
    //生成结果文件集合
    List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
    private int roNo = 1;
    
    //----------初始化基础数据
    public void initKeyValue(){
        errMap = new HashMap<Integer, String>();
        errorMap = new HashMap<Integer, String>();
        repeatMap = new HashMap<String, Integer>();
        originColumn = new TreeMap<Integer,String>();
        wareHouseMap = bmsMaterialImportTaskCommon.queryAllWarehouse();
        customerMap  = bmsMaterialImportTaskCommon.queryAllCustomer();
        materialMap  = bmsMaterialImportTaskCommon.queryAllMaterial();
    }
    
    public void handImportFile(final String taskId) throws Exception{
        //开始处理
        bmsMaterialImportTaskCommon.setTaskStatus(taskId, 0, FileAsynTaskStatusEnum.PROCESS.getCode());
        //初始化仓库，商家，耗材信息，错误信息
        try {
            initKeyValue();
        } catch (Exception e) {
            logger.info("任务ID【{}】 -> 初始化仓库,商家,耗材数据异常",taskId);
            bmsMaterialImportTaskCommon.setTaskStatus(taskId, 10, FileAsynTaskStatusEnum.EXCEPTION.getCode());
            return;
        }
        
        //----------查询任务----------
        taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
        logger.info("任务ID【{}】 -> 领取任务",taskId );
        if (null == taskEntity) {
            logger.info("任务ID【{}】 -> 任务不存在",taskId);
            return;
        }
        bmsMaterialImportTaskCommon.setTaskProcess(taskId, 5);
        
        initColumnNames();
        
        //----------读取excel
        logger.info("任务ID【{}】 -> 准备读取excel...",taskId);
        
        long start = System.currentTimeMillis();
        XlsxWorkBook reader = null;
        byte[] bytes = storageClient.downloadFile(taskEntity.getOriginFilePath(), new DownloadByteArray());
        logger.info("任务ID【{}】 -> byte长度【{}】",taskId,bytes.length);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        try{
            reader = new XlsxWorkBook(inputStream); 
            Sheet sheet = reader.getSheets().get(0);
            //更新文件总行数
            BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 30,null, sheet.getRowCount(), null, null, null, null);
            bmsFileAsynTaskService.update(updateEntity);
            reader.readSheet(sheet.getSheetId(), new SheetReadCallBack() {
                @Override
                public void readTitle(List<String> columns) {
                    //源生表头
                    int a = 1;
                    for (String column : columns) {
                        originColumn.put(a, column);
                        a++;
                    }
                    
                    //----------校验表头----------
                    logger.info("任务ID【{}】 -> 校验表头...",taskId); 
                    String[] str = {"出库日期", "仓库", "商家", "出库单号", "运单号"}; //必填列
                    if(!checkTitle(columns,str)){
                        logger.info("任务ID【{}】 -> 模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号",taskId);
                        bmsMaterialImportTaskCommon.setTaskStatus(taskId, 36, FileAsynTaskStatusEnum.FAIL.getCode(), "模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号");
                        return;
                    }
                    // 表格列数
                    int cols = columns.size();
                    if((cols-5)%2 != 0){ // 如果列数不对则 返回
                        bmsMaterialImportTaskCommon.setTaskStatus(taskId, 37, FileAsynTaskStatusEnum.FAIL.getCode(), "表格列数不对");
                        return;
                    }
                    
                    //判断列头是否重复
                    List<String> mMap=new ArrayList<String>();
                    int col = (columns.size() - 5) / 2; //有多少种耗材
                    for(int i = 0; i < col; i++){
                        String codeName = originColumn.get(i * 2 + 6);//耗材编码对应的列名           
                        if(!mMap.contains(codeName)){
                            mMap.add(codeName);
                        }else{
                            bmsMaterialImportTaskCommon.setTaskStatus(taskId, 38, FileAsynTaskStatusEnum.FAIL.getCode(), "表格列名不对,存在重复列名，请检查");
                            return;
                        }
                    }
                    logger.info("任务ID【{}】 -> 表头校验完成，准备读取Excel内容……",taskId); 
                    bmsMaterialImportTaskCommon.setTaskProcess(taskId, 50);
                }

                @Override
                public void read(DataRow dr) {
                    //行错误信息
                    String errorMsg="";         

                    //组装往临时表存的数据，校验出错捕获加入errList
                    List<BizOutstockPackmaterialTempEntity> tempList = null;
                    
                    try {
                        tempList = loadTemp(dr, errorMsg);
                    } catch (Exception e) {
                        errorMap.put(dr.getRowNo(), e.getMessage());
                        errMap.clear();
                    }

                    //组装好的数据存入全局List中
                    if (tempList == null) {
                        tempList = new ArrayList<BizOutstockPackmaterialTempEntity>();
                    }
                    for( int i = 0 ; i < tempList.size() ; i++){
                        newList.add(tempList.get(i));
                    }
                    
                    //1000条数据   
                    if (newList.size() >= batchNum) {
                        if(errorMap.size()==0){
                            int result = saveTo();
                            if(result < 0){
                                logger.error("任务ID【{}】 ->,保存到临时表失败", taskId);
                            }
                        }
                    }                           
                    return;
                }

                @Override
                public void finish() {
                    repeatMap.clear();
                    bmsMaterialImportTaskCommon.setTaskProcess(taskId, 70);
                    //保存数据到临时表
                    if (errorMap.size() == 0) {
                        int result = saveTo();
                        if (result < 0) {
                            logger.error("临时表数据保存失败");
                        }
                    }   
                }

                @Override
                public void error(Exception ex) {
                    // TODO Auto-generated method stub
                    
                }  
            });
            
            //更新文件读取行数
            if(sheet.getRowCount()<=0){
                logger.info("未从excel读取到任何数据");
                bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.FAIL.getCode(),"未从excel读取到任何数据");
                return;
            }
            logger.info("excel读取完成，读取行数【"+sheet.getRowCount()+"】");
            BmsFileAsynTaskVo updateEntity2 = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 72,null, sheet.getRowCount(), null, null, null, null);
            bmsFileAsynTaskService.update(updateEntity2);

            long end = System.currentTimeMillis();
            logger.info("任务ID【{}】 -> *****读取excel,耗时【{}】毫秒",taskId,(end-start));
        }
        catch(Exception ex){
            logger.error("任务ID【{}】 -> excel解析异常{}",taskId,ex);
            bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.EXCEPTION.getCode());
            return;
        }/*finally {
            reader.close();
        }*/
        
        //如果excel数据本身存在问题，直接生产结果文件返回给用户
        if(errorMap.size()>0){
            logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
            try {
                exportResultFile(reader);
            } catch (Exception e) {
                logger.error("文件创建失败！", e);
            }
            return;
        }
        
        //数据库层面重复校验  false - 校验不通过 存在重复  原则上 同一运单号，同一耗材，只有一条
        if(!dbCheck()){
            try {
                exportResultFile(reader);
            } catch (Exception e) {
                logger.error("文件创建失败！", e);
            }
            return;
        }
        bmsMaterialImportTaskCommon.setTaskProcess(taskId, 80); 
        logger.info("************ OK **********");
        
        try{
            logger.info("任务ID【{}】 -> 保存数据到正式表",taskId);
            int k=bizOutstockPackmaterialService.saveDataFromTemp(taskId);
            if(k>0){
                logger.info("任务ID【{}】 -> 保存数据到正式表成功 耗时【{}】",taskId,System.currentTimeMillis()-start);
                bmsMaterialImportTaskCommon.setTaskProcess(taskId, 90);
                BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 100,FileAsynTaskStatusEnum.SUCCESS.getCode(), null, JAppContext.currentTimestamp(), null, null, "导入成功");
                bmsFileAsynTaskService.update(updateEntity);
                newList.clear();
            }else{
                logger.error("任务ID【{}】 -> 未从临时表中保存数据到业务表",taskId);
                bmsMaterialImportTaskCommon.setTaskStatus(taskId,99, FileAsynTaskStatusEnum.FAIL.getCode(),"未从临时表中保存数据到业务表，批次号【"+taskId+"】,任务编号【"+taskId+"】");
                bizOutstockPackmaterialTempService.deleteBybatchNum(taskId);
                newList.clear();
                return;
            }
        }catch(Exception e){
            logger.error("任务ID【{}】 -> 异步导入异常{}",taskId,e);
            bmsMaterialImportTaskCommon.setTaskStatus(taskId,99, FileAsynTaskStatusEnum.EXCEPTION.getCode(),"从临时表中保存数据到业务表异常");
            bizOutstockPackmaterialTempService.deleteBybatchNum(taskId);
            return;
        }
        
        try{
            // 耗材打标
            Map<String,Object> condition = Maps.newHashMap();
            condition.put("batchNum", taskId);
            condition.put("taskId", taskId);
            logger.info("任务ID【{}】 -> 进行耗材和保温袋打标操作",taskId);
            //泡沫箱打标
            int pmxResult=bmsProductsMaterialService.markPmx(condition);
            if(pmxResult>0){
                logger.info("任务ID【{}】 -> 泡沫箱打标成功",taskId);
                bmsProductsMaterialService.saveMarkPmx(condition);
                logger.info("任务ID【{}】 -> 泡沫箱原始数据成功",taskId);

            }
            //纸箱打标
            int zxResult=bmsProductsMaterialService.markZx(condition);
            if(zxResult>0){
                //保存标对应得纸箱明细
                logger.info("任务ID【{}】 -> 纸箱打标成功",taskId);
                bmsProductsMaterialService.saveMarkZx(condition);
                logger.info("任务ID【{}】 -> 纸箱原始数据成功",taskId);

            }
            //保温袋打标
            int bwdResult=bmsProductsMaterialService.markBwd(condition);
            if(bwdResult>0){
                //保存标对应得保温袋明细
                logger.info("任务ID【{}】 -> 保温袋打标成功",taskId);
                bmsProductsMaterialService.saveMarkBwd(condition);
                logger.info("任务ID【{}】 -> 保温袋原始数据成功",taskId);

            }
        }catch(Exception e){
            logger.error("任务ID【{}】 -> 异步导入成功，打标异常{}",taskId,e);
            BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 100,FileAsynTaskStatusEnum.SUCCESS.getCode(), null, JAppContext.currentTimestamp(), null, null, "异步导入成功，打标异常");
            bmsFileAsynTaskService.update(updateEntity);            
        }
    }
    
    /**
     * 初始化列头数据
     */
    private void initColumnNames(){
        
        materialCommonList = new ArrayList<String>();
        materialCommonList.add("出库日期");
        materialCommonList.add("仓库");
        materialCommonList.add("商家");
        materialCommonList.add("出库单号");
        materialCommonList.add("运单号");
    }
    
    /**
     * 生成结果文件
     * @throws IOException 
     * @throws ParseException 
     */

    
    private List<Map<String, Object>> getBizHead(List<String> exportColumns){
        List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
        Map<String, Object> itemMap = null;
        for (String colName : exportColumns) {
            itemMap = new HashMap<String, Object>();
            itemMap.put("title", colName);
            itemMap.put("columnWidth", 25);
            itemMap.put("dataKey", colName);
            headInfoList.add(itemMap);
        }
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "备注");
        headInfoList.add(itemMap);
        return headInfoList;
    }
    
    private void exportResultFile(XlsxWorkBook reader) throws ParseException, IOException{
        if(!StringUtil.isEmpty(taskEntity.getResultFilePath())){
            logger.info("删除历史结果文件");
            boolean resultF = storageClient.deleteFile(taskEntity.getResultFilePath());
            if(resultF){
                logger.info("删除历史结果文件-成功");
            }
            else{
                logger.info("删除历史结果文件-失败");
            }
        }
        
        List<String> exportColumns = new ArrayList<String>();
        
        for (Map.Entry<Integer, String> map : originColumn.entrySet()) {
            exportColumns.add(map.getValue());
        }
        originColumn.clear();
        
        final POISXSSUtil poiUtil = new POISXSSUtil();
        final SXSSFWorkbook workbook = new SXSSFWorkbook(10000);    
        
        final List<Map<String, Object>> headDetailMapList = getBizHead(exportColumns); 
        
        //重新读取Excel，生成结果文件
        logger.info("重新读取Excel--->生成结果文件");
        try{
            Sheet sheet = reader.getSheets().get(0);
            reader.readSheet(sheet.getSheetId(), new SheetReadCallBack() {

                @Override
                public void readTitle(List<String> columns) {

                }

                @Override
                public void read(DataRow dr) {   
                    SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM/dd");
                    Map<String, Object> dataItem = new HashMap<String, Object>();
                    for (DataColumn dc : dr.getColumns()) {
                        if ("出库日期".equals(dc.getTitleName())) {
                            try {
                                if (StringUtils.isBlank(dc.getColValue())) {
                                    continue;
                                }
                                dataItem.put(dc.getTitleName(), format.format(DateUtil.transStringToTimeStamp(dc.getColValue())));
                            } catch (Exception e) {
                                logger.error("日期格式异常！");
                                errorMap.put(dr.getRowNo(), "日期格式异常！");
                            }           
                        }else {
                            dataItem.put(dc.getTitleName(), dc.getColValue());
                        }
                    }
                    if (errorMap.containsKey(dr.getRowNo())) {
                        dataItem.put("备注", errorMap.get(dr.getRowNo()));
                    }
                    dataList.add(dataItem);
                    
                    //1000条写入一次
                    if (dataList.size() >= batchNum) {
                        try {
                            poiUtil.exportExcel2FilePath(poiUtil, workbook, "耗材出库结果文件",roNo, headDetailMapList, dataList);
                            roNo = roNo + dataList.size();
                        } catch (IOException e) {
                            logger.error("写入结果文件失败！", e);
                        }
                        dataList.clear();
                    }
                }

                @Override
                public void finish() {
                    try {
                        poiUtil.exportExcel2FilePath(poiUtil, workbook, "耗材出库结果文件",roNo, headDetailMapList, dataList);
                        roNo = 1;
                    } catch (IOException e) {
                        logger.error("写入结果文件失败！", e);
                    }
                    dataList.clear();
                }

                @Override
                public void error(Exception ex) {
                    // TODO Auto-generated method stub
                    
                }
                
            });
        }catch (Exception e) {
            logger.error("任务ID【{}】 -> 第二次excel解析异常{}",taskId, e);
            bmsMaterialImportTaskCommon.setTaskStatus(taskId, 99, FileAsynTaskStatusEnum.EXCEPTION.getCode());
        }finally {
            reader.close();
        }
        
        errorMap.clear();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        workbook.dispose();
        byte[] b1 = os.toByteArray();
        StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
        String resultFullPath = resultStorePath.getFullPath();
        logger.info("上传结果文件到FastDfs - 成功");
        BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 99,FileAsynTaskStatusEnum.FAIL.getCode(), null, JAppContext.currentTimestamp(), taskEntity.getOriginFileName(), resultFullPath, REMARK);
        bmsFileAsynTaskService.update(updateEntity);
    }
    
    /**
     * 数据库重复性校验
     * @return
     */
    private boolean dbCheck(){

        List<BizOutstockPackmaterialTempEntity> list = bizOutstockPackmaterialTempService.queryContainsList(taskEntity.getTaskId(), batchNum);
        if(null == list || list.size() <= 0){
            return true;
        }
        Map<String,String> map=Maps.newLinkedHashMap();
        
        //一共几行,去重
        Set<Integer> rowNos = new TreeSet<>();
        for(BizOutstockPackmaterialTempEntity entity:list){
            rowNos.add(entity.getRowExcelNo());
        }
        
        //存在重复记录
        for(BizOutstockPackmaterialTempEntity entity:list){
            String row=String.valueOf(entity.getRowExcelNo());
            
            String mes="";
            if(map.containsKey(row)){
                mes=map.get(row);
                mes+=","+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
                map.put(row,mes);
            }else{
                mes="系统中已存在运单号【" + entity.getWaybillNo() + "】,"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
                map.put(row,mes);
            }
        }
        Set<String> set=map.keySet();
        for(String key:set){
            Integer rowNum=Integer.valueOf(key);
            if(errorMap.containsKey(rowNum)){
                errorMap.put(rowNum, errorMap.get(rowNum)+","+map.get(key));
            }else{
                errorMap.put(rowNum, map.get(key));
            }
        }
        return false;
    }
    
    public boolean checkTitle(List<String> headColumn, String[] str) {
        if(headColumn == null){
            return false;
        }
        if(headColumn.size() < str.length){
             return false;
        }
        for (String s : str) {
            if(!headColumn.contains(s)){
                return false;
            }
        } 
        return true;
    }
    
    /**
     * 将excel中的内容转换为timestamp类型
     * @param value
     * @return
     * @throws ParseException 
     */
    public Timestamp changeValueToTimestamp(String value) throws ParseException{
        if(StringUtil.isNumeric(value)){
            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = dateFormat.format(date);
        }
        String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
        Date date = DateUtils.parseDate(value, dataPatterns);
        Timestamp ts=new Timestamp(date.getTime());
        return ts;
    }
    
    private int saveTo(){
        int k = 0;
        logger.info("任务ID【{}】 -> 保存数据到临时表 转化成对象数【{}】",taskId,newList.size());
        long start = System.currentTimeMillis();
        try {
            //保存到临时表
            if (newList.size() > 0) {
                bizOutstockPackmaterialTempService.saveBatch(newList); 
            }       
        } catch (Exception e) {
            logger.info("任务ID【{}】 -> 写入临时表-失败:【{}】",taskId,e);
            k = -1;
        }
        logger.info("保存到临时表，耗时：【{}】毫秒", System.currentTimeMillis()-start);
        bmsMaterialImportTaskCommon.setTaskProcess(taskId, 75);
        newList.clear();
        return k;
    }
    
  //-------------组装数据------------
    private List<BizOutstockPackmaterialTempEntity> loadTemp(DataRow dr, String errorMsg) {
        BizOutstockPackmaterialTempEntity tempEntity = null;
        List<BizOutstockPackmaterialTempEntity> tempList = new ArrayList<BizOutstockPackmaterialTempEntity>(); 
        boolean isWaybillNull = false; //运单号是否为空  true-空  false-非空
        //本行是否拥有耗材
        boolean isHaveMaterial = false;
        
        tempEntity = new BizOutstockPackmaterialTempEntity();
        tempEntity.setRowExcelNo(dr.getRowNo());
        try {
            for (DataColumn dc : dr.getColumns()) {
                switch (dc.getTitleName()) {
                case "出库日期":
                    if (StringUtils.isNotBlank(dc.getColValue())) {
                        tempEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
                    }else {
//                      errMap.put(dr.getRowNo(), dc.getTitleName()+"是必填项");
                        errorMsg += "出库日期是必填项;";
                    }
                    break;
                case "仓库":
                    if (StringUtils.isNotBlank(dc.getColValue())) {
                        tempEntity.setWarehouseName(dc.getColValue());
                        //如果没找到，报错
                        if (wareHouseMap.containsKey(dc.getColValue())) {
                            tempEntity.setWarehouseCode(wareHouseMap.get(dc.getColValue()).getWarehouseid());
                        }else {
                            errorMsg+="仓库不存在;";
                        }
                    }else {
//                      errMap.put(dr.getRowNo(), dc.getTitleName()+"是必填项");
                        errorMsg+="仓库是必填项;";
                    }
                    break;
                case "商家":
                    if (StringUtils.isNotBlank(dc.getColValue())) {
                        tempEntity.setCustomerName(dc.getColValue());
                        //如果没找到，报错
                        if (customerMap.containsKey(dc.getColValue())) {
                            tempEntity.setCustomerId(customerMap.get(dc.getColValue()).getCustomerid());
                        }else {
                            errorMsg+="商家不存在;";
                        }
                    }else {
//                      errMap.put(dr.getRowNo(), dc.getTitleName()+"是必填项");
                        errorMsg+="商家是必填项;";
                    }
                    break;
                case "出库单号":
                    if (StringUtils.isNotBlank(dc.getColValue())) {
                        tempEntity.setOutstockNo(dc.getColValue());
                    }else {
//                      errMap.put(dr.getRowNo(), dc.getTitleName()+"是必填项");
                        errorMsg+="出库单号是必填项;";
                    }
                    break;
                case "运单号":
                    if (StringUtils.isNotBlank(dc.getColValue())) {
                        tempEntity.setWaybillNo(dc.getColValue());
                    }else {
//                      errMap.put(dr.getRowNo(), dc.getTitleName()+"是必填项");
                        errorMsg+="运单号是必填项;";
                        isWaybillNull = true;
                    }
                    break;
                default:
                    break;
                }
            }
            
        } catch (Exception e) {
            errorMsg+="第【"+ dr.getRowNo() +"】行格式不正确;";
        }       
        
        if (isWaybillNull) {
            return tempList;
        }
        
        //起始列
        int index=0;
        for (DataColumn dc:dr.getColumns()) {
            if(dc.getTitleName().contains("数量")){
                index=dc.getColNo()-1;
                break;
            }
        }
        
        int count=1;
        String num0 = "";
        BigDecimal num = null;
        BizOutstockPackmaterialTempEntity newTempEntity = null;
        String materialType = "";
        String materialName = "";
        for (DataColumn dc : dr.getColumns()) {         
            if (dc.getColNo() >= index) {
                try {
                    if(count==1){
                        newTempEntity=new BizOutstockPackmaterialTempEntity();
                        PropertyUtils.copyProperties(newTempEntity, tempEntity);
                    }
                    if ("干冰重量".equals(dc.getTitleName())) {
                        num0 = dc.getColValue();
                        if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
                            count = 1;
                            continue;
                        }else if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
                            errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
                            count = 1;
                            continue;
                        }else if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isNotBlank(newTempEntity.getConsumerMaterialCode())) {
                            errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
                            count = 1;
                            continue;
                        }else {
                            isHaveMaterial = true;
                            //校验重量
                            num0 = dc.getColValue();
                            if(!StringUtil.isNumeric(dc.getColValue())){
                                errorMsg += "【"+num0+"】不是数字;";
                            }
                            else{
                                if(num0.contains("-") || "0".equals(num0)){
                                    errorMsg += "【"+dc.getTitleName()+"】必须>0";
                                }
                            }
                            
                            //校验耗材Code
                            if(!materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
                                errorMsg += "耗材【"+newTempEntity.getConsumerMaterialCode()+"】不存在;";
                            }else if (!materialName.equals(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialType())) {
                                errorMsg += materialName+"类型下无耗材【"+ newTempEntity.getConsumerMaterialCode() +"】;";
                            }
                            
                            if(errorMsg.length()>0){
                                count = 1;
                                continue;
                            }
                            
                            newTempEntity.setConsumerMaterialName(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialName());
                            if(materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
                                PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(newTempEntity.getConsumerMaterialCode());
                                newTempEntity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
                                +"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
                            }
                            String key = newTempEntity.getWaybillNo() + newTempEntity.getConsumerMaterialCode();
                            if(repeatMap.containsKey(key)){
                                errorMsg += "数据重复--第【"+repeatMap.get(key)+"】行已存在运单【"+newTempEntity.getWaybillNo()+"】和耗材【"+newTempEntity.getConsumerMaterialCode()+"】的组合;";
                            }
                            else{
                                repeatMap.put(key, dr.getRowNo());
                            }
                            
                            if(errorMsg.length()>0){
                                count = 1;
                                continue;
                            }else {
                                num = new BigDecimal(num0);
                                newTempEntity.setWeight(num);
                            }
                        }   
                    }else if (dc.getTitleName().contains("数量")) {
                        //数量
                        num0 = dc.getColValue();
                        if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
                            count=1;
                            continue;
                        }else if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
                            errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
                            count=1;
                            continue;
                        }else if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isNotBlank(newTempEntity.getConsumerMaterialCode())) {
                            errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
                            count=1;
                            continue;
                        }else {
                            isHaveMaterial = true;
                            //校验数量
                            num0 = dc.getColValue();
                            if(!StringUtil.isNumeric(dc.getColValue())){
                                errorMsg += "【"+num0+"】不是数字;";
                            }
                            else{
                                if(num0.contains("-") || "0".equals(num0)){
                                    errorMsg += "【"+dc.getTitleName()+"】必须>0";
                                }
                            }
                            
                            //校验耗材Code
                            if(!materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
                                errorMsg += "耗材【"+newTempEntity.getConsumerMaterialCode()+"】不存在;";
                            }else if (!materialName.equals(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialType())) {
                                errorMsg += materialName+"类型下无耗材【"+ newTempEntity.getConsumerMaterialCode() +"】;";
                            }
                            
                            if(errorMsg.length()>0){
                                count=1;
                                continue;
                            }

                            newTempEntity.setConsumerMaterialName(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialName());
                            if(materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
                                PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(newTempEntity.getConsumerMaterialCode());
                                newTempEntity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
                                +"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
                            }
                            String key = newTempEntity.getWaybillNo() + newTempEntity.getConsumerMaterialCode();
                            if(repeatMap.containsKey(key)){
                                errorMsg += "数据重复--第【"+repeatMap.get(key)+"】行已存在运单【"+newTempEntity.getWaybillNo()+"】和耗材【"+newTempEntity.getConsumerMaterialCode()+"】的组合;";
                            }
                            else{
                                repeatMap.put(key, dr.getRowNo());
                            }
                            
                            if(errorMsg.length()>0){
                                count=1;
                                continue;
                            }else {
                                num = new BigDecimal(num0);
                                newTempEntity.setNum(num);
                                newTempEntity.setAdjustNum(num);
                            }
                        }
                    }else {     
                        materialName = dc.getTitleName();
                        if (StringUtils.isNotBlank(dc.getColValue())) {
                            newTempEntity.setConsumerMaterialCode(dc.getColValue().trim());
                        }
                    }

                    count++;
                    if(count>2){
                        if(StringUtils.isNotBlank(newTempEntity.getConsumerMaterialCode())){
                            newTempEntity.setRowExcelName(materialType);
                            newTempEntity.setCreator(taskEntity.getCreator());
                            newTempEntity.setDelFlag("0");
                            newTempEntity.setExtattr5("origin");
                            newTempEntity.setWriteTime(JAppContext.currentTimestamp());
                            newTempEntity.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
                            newTempEntity.setBatchNum(taskEntity.getTaskId());
                            tempList.add(newTempEntity);
                        }
                        count=1;
                    }
                } catch (Exception e) {
                    errorMsg+="列【"+ dc.getTitleName() + "】格式不正确;";
                }
            }
        }
                
        
        int rowNo = dr.getRowNo();
        if(!StringUtil.isEmpty(errorMsg)){
            if(errMap.containsKey(rowNo)){
                errMap.put(rowNo, errMap.get(rowNo)+errorMsg);
            }
            else{
                errMap.put(rowNo, errorMsg);
            }
        }
        if(!isHaveMaterial){
            if(errMap.containsKey(rowNo)){
                errMap.put(rowNo, errMap.get(rowNo)+"本行未录入任何耗材;");
            }
            else{
                errMap.put(rowNo, "本行未录入任何耗材;");
            }
        }
        
        if (errMap.size() > 0) {
            throw new BizException(errMap.get(dr.getRowNo()));
        }
        return tempList;
    }
}


