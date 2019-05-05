package com.jiuyescm.bms.billcheck.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBillCheckInvoiceService;
import com.jiuyescm.bms.billcheck.service.IBillCheckReceiptService;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptSumVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptVo;
import com.jiuyescm.bms.billcheck.vo.BillExpectReceiptVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

@Controller("billCheckReportController")
public class BillCheckReportController {
    private static final Logger logger = Logger.getLogger(BillCheckReportController.class.getName());
    @Resource
    private ISystemCodeService systemCodeService;
    @Resource
    private IBillCheckInfoService billCheckInfoService;

    @Resource
    private IBillCheckInvoiceService billCheckInvoiceService;

    @Resource
    private IBillCheckReceiptService billCheckReceiptService;

    @Resource
    private IBmsGroupUserService bmsGroupUserService;

    /**
     * 分页查询收款明细表
     * 
     * @param page
     * @param param
     */
    @DataProvider
    public void queryReceiptDetail(Page<BillCheckInfoVo> page, Map<String, Object> param) {

        if (param == null) {
            param = new HashMap<String, Object>();
        }

        List<String> userIds = new ArrayList<String>();
        BmsGroupUserVo groupUser = bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
        if (groupUser != null) {// 加入權限組
            // 判断是否是管理员
            if ("0".equals(groupUser.getAdministrator())) {// 管理员

            } else {// 非管理员
                userIds = bmsGroupUserService.queryContainUserIds(groupUser);
                StringBuffer user = new StringBuffer();
                for (int i = 0; i < userIds.size(); i++) {
                    if (i == userIds.size() - 1) {
                        user.append(userIds.get(i));
                    } else {
                        user.append(userIds.get(i) + "|");
                    }
                }
                if (StringUtils.isNotBlank(user)) {
                    param.put("userIds", user);
                }
            }

            PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(),
                    page.getPageSize());
            if (pageInfo != null) {
                page.setEntities(pageInfo.getList());
                page.setEntityCount((int) pageInfo.getTotal());
            }
        }
    }

    /**
     * 分页查询收款明细表
     * 
     * @param page
     * @param param
     */
    @DataProvider
    public void queryReceiptSum(Page<BillCheckReceiptSumVo> page, Map<String, Object> param) {

        List<BillCheckReceiptSumVo> sumList = queryReport(param);

        if (sumList.size() > 0) {
            page.setEntities(sumList);
            page.setEntityCount(sumList.size());
        }

    }

    /**
     * 分页查询发票明细表
     * 
     * @param page
     * @param param
     */
    @DataProvider
    public void queryInvoiceReport(Page<BillCheckInvoiceVo> page, Map<String, Object> param) {

        if (param == null) {
            param = new HashMap<String, Object>();
        }
        PageInfo<BillCheckInvoiceVo> pageInfo = billCheckInvoiceService.queryReport(param, page.getPageNo(),
                page.getPageSize());
        // PageInfo<BillCheckInfoVo> pageInfo =
        // billCheckInfoService.queryReceiptDetail(param, page.getPageNo(),
        // page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }

    /**
     * 分页查询收款明细表
     * 
     * @param page
     * @param param
     */
    @DataProvider
    public void queryReceiptReport(Page<BillCheckReceiptVo> page, Map<String, Object> param) {

        if (param == null) {
            param = new HashMap<String, Object>();
        }
        PageInfo<BillCheckReceiptVo> pageInfo = billCheckReceiptService.queryReport(param, page.getPageNo(),
                page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }

    /**
     * 预计回款信息查询
     * 
     * @param page
     * @param param
     */
    @DataProvider
    public void queryExpectReceiptReport(Page<BillExpectReceiptVo> page, Map<String, Object> param) {

        if (param == null) {
            param = new HashMap<String, Object>();
        }

        List<BillExpectReceiptVo> volist = new ArrayList<BillExpectReceiptVo>();

        PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, page.getPageNo(), page.getPageSize());
        if (pageInfo != null && pageInfo.getList().size() > 0) {
            List<BillCheckInfoVo> list = pageInfo.getList();

            // 确认金额汇总
            BigDecimal totalConfirmAmount = new BigDecimal(0);
            // 截止日前已回款金额汇总
            BigDecimal totalNowReceiptMoney = new BigDecimal(0);
            // 截止日前剩余待回款金额汇总
            BigDecimal totalNowTbReceiptMoney = new BigDecimal(0);
            // 调整金额汇总
            BigDecimal totalAdjustMoney = new BigDecimal(0);

            for (BillCheckInfoVo vo : list) {
                BigDecimal money = new BigDecimal(0);
                BillExpectReceiptVo entity = new BillExpectReceiptVo();
                entity.setCreateMonth(vo.getCreateMonth());
                entity.setInvoiceName(vo.getInvoiceName());
                entity.setBillName(vo.getBillName());
                entity.setBillStatus(vo.getBillStatus());
                entity.setInvoiceStatus(vo.getInvoiceStatus());
                entity.setReceiptStatus(vo.getReceiptStatus());
                entity.setSellerId(vo.getSellerId());
                entity.setSellerName(vo.getSellerName());
                entity.setConfirmAmount(vo.getConfirmAmount() == null ? money : vo.getConfirmAmount());
                entity.setNowTbReceiptMoney(vo.getConfirmAmount() == null ? money : vo.getConfirmAmount());
                entity.setNowReceiptMoney(money);
                entity.setAdjustMoney(money);
                entity.setArea(vo.getArea());

                param.put("id", vo.getId());
                param.put("receiptDate", param.get("expireDate"));
                BillCheckReceiptVo billCheckReceiptVo = billCheckReceiptService.queyReceipt(param);
                if (billCheckReceiptVo != null) {
                    entity.setNowLastReceiptDate(billCheckReceiptVo.getReceiptDate());
                    entity.setNowReceiptMoney(billCheckReceiptVo.getReceiptAmount() == null ? money
                            : billCheckReceiptVo.getReceiptAmount());
                    if (billCheckReceiptVo.getReceiptAmount() != null) {
                        entity.setNowTbReceiptMoney(vo.getConfirmAmount().subtract(
                                billCheckReceiptVo.getReceiptAmount()));
                    }
                }

                // 预计回款日期
                param.put("expectReceiptDate", param.get("expireDate"));
                BillReceiptFollowVo receiptFollowVo = billCheckInfoService.queryReceiptFollow(param);
                if (receiptFollowVo != null) {
                    entity.setNowLastExpectReceiptDate(receiptFollowVo.getExpectReceiptDate());
                }

                // 调整金额
                param.put("billCheckId", vo.getId());
                BillCheckAdjustInfoVo adjustVo = billCheckInfoService.queryOneAdjust(param);
                if (adjustVo != null && adjustVo.getAdjustAmount() != null) {
                    entity.setAdjustMoney(adjustVo.getAdjustAmount() == null ? money : adjustVo.getAdjustAmount());
                }

                // 汇总
                totalConfirmAmount = totalConfirmAmount.add(entity.getConfirmAmount());
                totalNowReceiptMoney = totalNowReceiptMoney.add(entity.getNowReceiptMoney());
                totalNowTbReceiptMoney = totalNowTbReceiptMoney.add(entity.getNowTbReceiptMoney());
                totalAdjustMoney = totalAdjustMoney.add(entity.getAdjustMoney());

                volist.add(entity);
            }
            for (BillExpectReceiptVo vo : volist) {
                vo.setTotalConfirmAmount(totalConfirmAmount);
                vo.setTotalNowReceiptMoney(totalNowReceiptMoney);
                vo.setTotalNowTbReceiptMoney(totalNowTbReceiptMoney);
                vo.setTotalAdjustMoney(totalAdjustMoney);
            }
        }

        if (volist.size() > 0) {
            page.setEntities(volist);
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }

    public void sumMoney(BillCheckReceiptSumVo sumVo, BillCheckInfoVo vo) {
        // 开票未回款（账单已确认:待开票、待收款、已收款）
        if (CheckBillStatusEnum.TB_INVOICE.getCode().equals(vo.getBillStatus())
                || CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus())
                || CheckBillStatusEnum.RECEIPTED.getCode().equals(vo.getBillStatus())) {
            BigDecimal money = new BigDecimal(0);
            // 包含以下数据：
            // 1.是否需要发票=否，计算账单确认额-收款金额
            // 2.是否需要发票=是，计算已开票和部分开票的账单确认额-收款金额
            if ("0".equals(vo.getIsneedInvoice())) {
                if (vo.getConfirmAmount() != null) {
                    money = vo.getConfirmAmount();
                }
                if (vo.getReceiptAmount() != null) {
                    money = money.subtract(vo.getReceiptAmount());
                }

            } else if ("1".equals(vo.getIsneedInvoice())) {
                // 已开票和部分开票
                if (BillCheckInvoiceStateEnum.INVOICED.getCode().equals(vo.getInvoiceStatus())
                        || BillCheckInvoiceStateEnum.PART_INVOICE.getCode().equals(vo.getInvoiceStatus())) {
                    if (vo.getConfirmAmount() != null) {
                        money = vo.getConfirmAmount();
                    }
                    if (vo.getReceiptAmount() != null) {
                        money = money.subtract(vo.getReceiptAmount());
                    }
                }
            }
            if (sumVo.getInvoiceUnReceiptAmount() == null) {
                sumVo.setInvoiceUnReceiptAmount(money);
            } else {
                sumVo.setInvoiceUnReceiptAmount(sumVo.getInvoiceUnReceiptAmount().add(money));
            }
            if (sumVo.getTotalAmount() == null) {
                sumVo.setTotalAmount(money);
            } else {
                sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
            }

        }

        // 未开票未回款（账单已确认:待开票、待收款、已收款）
        if (CheckBillStatusEnum.TB_INVOICE.getCode().equals(vo.getBillStatus())
                || CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus())
                || CheckBillStatusEnum.RECEIPTED.getCode().equals(vo.getBillStatus())) {
            BigDecimal money = new BigDecimal(0);
            // 包含以下数据：
            // 1.是否需要发票=是，计算未开票的账单确认额-收款金额
            if ("1".equals(vo.getIsneedInvoice())) {
                if (BillCheckInvoiceStateEnum.NO_INVOICE.getCode().equals(vo.getInvoiceStatus())) {
                    if (vo.getConfirmAmount() != null) {
                        money = vo.getConfirmAmount();
                    }
                    if (vo.getReceiptAmount() != null) {
                        money = money.subtract(vo.getReceiptAmount());
                    }
                }
            }
            if (sumVo.getUnInvoiceUnReceiptAmount() == null) {
                sumVo.setUnInvoiceUnReceiptAmount(money);
            } else {
                sumVo.setUnInvoiceUnReceiptAmount(sumVo.getUnInvoiceUnReceiptAmount().add(money));
            }
            if (sumVo.getTotalAmount() == null) {
                sumVo.setTotalAmount(money);
            } else {
                sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
            }
        }

        // 账单未确认
        if (CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(vo.getBillStatus())) {
            // 包含以下数据：
            // 1.计算账单确认额-收款金额
            BigDecimal money = new BigDecimal(0);
            if (vo.getConfirmAmount() != null) {
                money = vo.getConfirmAmount();
            }
            if (vo.getReceiptAmount() != null) {
                money = money.subtract(vo.getReceiptAmount());
            }
            if (sumVo.getUnConfirmAmount() == null) {
                sumVo.setUnConfirmAmount(money);
            } else {
                sumVo.setUnConfirmAmount(sumVo.getUnConfirmAmount().add(money));
            }
            if (sumVo.getTotalAmount() == null) {
                sumVo.setTotalAmount(money);
            } else {
                sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
            }
        }
    }

    private List<BillCheckReceiptSumVo> queryReport(Map<String, Object> param) {
        // 统计
        if (param == null) {
            param = new HashMap<String, Object>();
        }

        PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.querySimple(param, 0, Integer.MAX_VALUE);

        Map<String, BillCheckReceiptSumVo> map = new HashMap<String, BillCheckReceiptSumVo>();

        if (pageInfo != null && pageInfo.getList().size() > 0) {
            // key:部门 value:实体类
            List<BillCheckInfoVo> list = pageInfo.getList();
            for (BillCheckInfoVo vo : list) {
                // 部门
                if (StringUtils.isNotBlank(vo.getDeptName())) {
                    String deptName = vo.getDeptName();
                    if (map.containsKey(deptName)) {
                        // 部门名称已存在
                        BillCheckReceiptSumVo sumVo = map.get(deptName);
                        sumMoney(sumVo, vo);
                    } else {
                        // 部门不存在时
                        BillCheckReceiptSumVo sumVo = new BillCheckReceiptSumVo();
                        sumVo.setDeptName(deptName);
                        sumMoney(sumVo, vo);
                        map.put(deptName, sumVo);
                    }

                }
            }
        }
        List<BillCheckReceiptSumVo> sumList = new ArrayList<BillCheckReceiptSumVo>();

        for (String key : map.keySet()) {
            BillCheckReceiptSumVo value = map.get(key);
            // 排除账单未确认金额为空的情况
            if (null == value.getUnConfirmAmount()) {
                value.setUnConfirmAmount(BigDecimal.ZERO);
            }
            sumList.add(value);
        }
        return sumList;
    }

    /**
     * 应收情况导出
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @FileProvider
    public DownloadFile export(Map<String, Object> param) throws Exception {

        List<BillCheckReceiptSumVo> sumList = queryReport(param);

        long beginTime = System.currentTimeMillis();
        logger.info("====应收情况报表导出：写入Excel begin.");

        try {
            // String filePath =getName() + FileConstant.SUFFIX_XLSX;
            // 如果存放上传文件的目录不存在就新建
            String path = getPath();
            File storeFolder = new File(path);
            if (!storeFolder.isDirectory()) {
                storeFolder.mkdirs();
            }

            // 如果文件存在直接删除，重新生成
            String fileName = "应收情况汇总表" + FileConstant.SUFFIX_XLSX;
            String filePath = path + FileConstant.SEPARATOR + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            POISXSSUtil poiUtil = new POISXSSUtil();
            SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();

            // 导出方法
            hand(poiUtil, workbook, filePath, param, sumList);

            // 最后写到文件
            poiUtil.write2FilePath(workbook, filePath);

            logger.info("====应收情况报表：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

            InputStream is = new FileInputStream(filePath);
            return new DownloadFile(fileName, is);
        } catch (Exception e) {
            // bmsErrorLogInfoService.
            logger.error("应收情况报表导出失败", e);
        }
        return null;
    }

    /**
     * 导出
     */
    private void hand(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String path, Map<String, Object> param,
            List<BillCheckReceiptSumVo> sumList) throws Exception {

        logger.info("应收情况汇总表导出...");
        Sheet sheet = poiUtil.getXSSFSheet(workbook, "应收情况汇总表");
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 8000);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setFont(font);

        // 第一行（表头）
        Row row0 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("部门名称");
        cell0.setCellStyle(style);
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("开票未回款");
        cell1.setCellStyle(style);
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("未开票未回款");
        cell2.setCellStyle(style);
        Cell cell3 = row0.createCell(3);
        cell3.setCellValue("账单未确认");
        cell3.setCellStyle(style);
        Cell cell4 = row0.createCell(4);
        cell4.setCellValue("小计");
        cell4.setCellStyle(style);

        CellStyle style2 = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat(); // 此处设置数据格式
        style2.setDataFormat(df.getFormat("###,###,###,##0.00"));// 数据格式只显示整数

        int RowIndex = 1;
        if (CollectionUtils.isNotEmpty(sumList)) {
            for (int i = 0; i < sumList.size(); i++) {
                BillCheckReceiptSumVo vo = sumList.get(i);
                Row row = sheet.createRow(RowIndex);
                RowIndex++;
                // 责任部门名称
                Cell cel0 = row.createCell(0);
                cel0.setCellValue(vo.getDeptName());
                cel0.setCellStyle(style2);
                // 开票未回款金额
                Cell cel1 = row.createCell(1);
                cel1.setCellValue(vo.getInvoiceUnReceiptAmount() == null ? 0d : vo.getInvoiceUnReceiptAmount()
                        .doubleValue());
                cel1.setCellStyle(style2);
                // 未开票未回款金额
                Cell cel2 = row.createCell(2);
                cel2.setCellValue(vo.getUnInvoiceUnReceiptAmount() == null ? 0d : vo.getUnInvoiceUnReceiptAmount()
                        .doubleValue());
                cel2.setCellStyle(style2);
                // 账单未确认金额
                Cell cel3 = row.createCell(3);
                cel3.setCellValue(vo.getUnConfirmAmount() == null ? 0d : vo.getUnConfirmAmount().doubleValue());
                cel3.setCellStyle(style2);
                // 小计
                Cell cel4 = row.createCell(4);
                cel4.setCellValue(vo.getTotalAmount() == null ? 0d : vo.getTotalAmount().doubleValue());
                cel4.setCellStyle(style2);
            }
        }
    }

    private String getPath() {
        SystemCodeEntity systemCodeEntity = systemCodeService
                .getSystemCode("GLOABL_PARAM", "EXPORT_RECEIPT_SUM_REPORT");
        if (systemCodeEntity == null) {
            throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIPT_SUM_REPORT");
        }
        return systemCodeEntity.getExtattr1();
    }

    // 自定义数字格式方法
    public static String getFormat(String style, BigDecimal value) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(style);// 将格式应用于格式化器
        return df.format(value.doubleValue());
    }

    /**
     * 收款信息查询导出
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @FileProvider
    public DownloadFile exportReceipt(Map<String, Object> param) throws Exception {

        if (param == null) {
            param = new HashMap<String, Object>();
        }
        PageInfo<BillCheckReceiptVo> pageInfo = billCheckReceiptService.queryReport(param, 1, Integer.MAX_VALUE);

        long beginTime = System.currentTimeMillis();
        logger.info("====收款信息查询报表导出：写入Excel begin.");

        try {
            // 如果存放上传文件的目录不存在就新建
            String path = getPathReceipt();
            File storeFolder = new File(path);
            if (!storeFolder.isDirectory()) {
                storeFolder.mkdirs();
            }

            // 如果文件存在直接删除，重新生成
            String fileName = "收款信息查询报表" + FileConstant.SUFFIX_XLSX;
            String filePath = path + FileConstant.SEPARATOR + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            POISXSSUtil poiUtil = new POISXSSUtil();
            SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();

            // 导出方法
            handReceipt(poiUtil, workbook, filePath, param, pageInfo);

            // 最后写到文件
            poiUtil.write2FilePath(workbook, filePath);

            logger.info("====收款信息查询报表：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

            InputStream is = new FileInputStream(filePath);
            return new DownloadFile(fileName, is);
        } catch (Exception e) {
            // bmsErrorLogInfoService.
            logger.error("收款信息查询报表导出失败", e);
        }
        return null;
    }

    /**
     * 收款信息查询报表导出
     */
    private void handReceipt(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String path, Map<String, Object> param,
            PageInfo<BillCheckReceiptVo> pageInfo) throws Exception {
        List<BillCheckReceiptVo> list = pageInfo.getList();

        logger.info("收款信息查询报表导出...");
        Sheet sheet = poiUtil.getXSSFSheet(workbook, "收款信息查询报表");
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 8000);
        sheet.setColumnWidth(7, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 8000);
        sheet.setColumnWidth(10, 6000);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setFont(font);

        // 第一行（表头）
        Row row0 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("业务月份");
        cell0.setCellStyle(style);
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("商家合同名称");
        cell1.setCellStyle(style);
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("账单名称");
        cell2.setCellStyle(style);
        Cell cell3 = row0.createCell(3);
        cell3.setCellValue("责任部门");
        cell3.setCellStyle(style);
        Cell cell4 = row0.createCell(4);
        cell4.setCellValue("区域");
        cell4.setCellStyle(style);
        Cell cell5 = row0.createCell(5);
        cell5.setCellValue("销售员");
        cell5.setCellStyle(style);
        Cell cell6 = row0.createCell(6);
        cell6.setCellValue("收款金额");
        cell6.setCellStyle(style);
        Cell cell7 = row0.createCell(7);
        cell7.setCellValue("收款日期");
        cell7.setCellStyle(style);
        Cell cell8 = row0.createCell(8);
        cell8.setCellValue("创建人");
        cell8.setCellStyle(style);
        Cell cell9 = row0.createCell(9);
        cell9.setCellValue("创建日期");
        cell9.setCellStyle(style);
        Cell cell10 = row0.createCell(10);
        cell10.setCellValue("备注");
        cell10.setCellStyle(style);

        CellStyle style2 = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat(); // 此处设置数据格式
        style2.setDataFormat(df.getFormat("###,###,###,##0.00"));// 数据格式只显示整数

        int RowIndex = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                BillCheckReceiptVo vo = list.get(i);
                Row row = sheet.createRow(RowIndex);
                RowIndex++;
                // 业务月份
                Cell cel0 = row.createCell(0);
                cel0.setCellValue(vo.getCreateMonth() + "");
                cel0.setCellStyle(style2);
                // 商家合同名称
                Cell cel1 = row.createCell(1);
                cel1.setCellValue(vo.getInvoiceName());
                cel1.setCellStyle(style2);
                // 账单名称
                Cell cel2 = row.createCell(2);
                cel2.setCellValue(vo.getBillName());
                cel2.setCellStyle(style2);
                // 责任部门
                Cell cel3 = row.createCell(3);
                cel3.setCellValue(vo.getDeptName());
                cel3.setCellStyle(style2);
                // 区域
                Cell cel4 = row.createCell(4);
                cel4.setCellValue(vo.getArea());
                cel4.setCellStyle(style2);
                // 销售员
                Cell cel5 = row.createCell(5);
                cel5.setCellValue(vo.getSellerName());
                cel5.setCellStyle(style2);
                // 收款金额
                Cell cel6 = row.createCell(6);
                cel6.setCellValue(vo.getReceiptAmount() == null ? 0d : vo.getReceiptAmount().doubleValue());
                cel6.setCellStyle(style2);
                // 收款日期
                Cell cel7 = row.createCell(7);
                if(null!=vo.getReceiptDate()){
                    cel7.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(vo.getReceiptDate()));
                }
                cel7.setCellStyle(style2);
                // 创建人
                Cell cel8 = row.createCell(8);
                cel8.setCellValue(vo.getCreator());
                cel8.setCellStyle(style2);
                // 创建日期
                Cell cel9 = row.createCell(9);
                if(null!=vo.getCreateTime()){
                    cel9.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getCreateTime()));
                }
                cel9.setCellStyle(style2);
                // 备注
                Cell cel10 = row.createCell(10);
                cel10.setCellValue(vo.getRemark());
                cel10.setCellStyle(style2);
            }
        }
    }

    private String getPathReceipt() {
        SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM",
                "EXPORT_BILL_CHECK_RECEIPT_REPORT");
        if (systemCodeEntity == null) {
            throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_BILL_CHECK_RECEIPT_REPORT");
        }
        return systemCodeEntity.getExtattr1();
    }

    /**
     * 收款主报表导出
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @FileProvider
    public DownloadFile exportReceiptDetail(Map<String, Object> param) throws Exception {

        if (param == null) {
            param = new HashMap<String, Object>();
        }
        PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, 1, Integer.MAX_VALUE);
        long beginTime = System.currentTimeMillis();
        logger.info("====收款主报表导出：写入Excel begin.");

        try {
            // 如果存放上传文件的目录不存在就新建
            String path = getPathReceiptDetail();
            File storeFolder = new File(path);
            if (!storeFolder.isDirectory()) {
                storeFolder.mkdirs();
            }

            // 如果文件存在直接删除，重新生成
            String fileName = "收款主报表" + FileConstant.SUFFIX_XLSX;
            String filePath = path + FileConstant.SEPARATOR + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            POISXSSUtil poiUtil = new POISXSSUtil();
            SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();

            // 导出方法
            handReceiptDetail(poiUtil, workbook, filePath, param, pageInfo);

            // 最后写到文件
            poiUtil.write2FilePath(workbook, filePath);

            logger.info("====收款主报表：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

            InputStream is = new FileInputStream(filePath);
            return new DownloadFile(fileName, is);
        } catch (Exception e) {
            // bmsErrorLogInfoService.
            logger.error("收款主报表导出失败", e);
        }
        return null;
    }

    /**
     * 收款主报表导出
     */
    private void handReceiptDetail(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String path, Map<String, Object> param,
            PageInfo<BillCheckInfoVo> pageInfo) throws Exception {
        List<BillCheckInfoVo> list = pageInfo.getList();

        logger.info("收款主报表导出...");
        Sheet sheet = poiUtil.getXSSFSheet(workbook, "收款主报表");
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 8000);
        sheet.setColumnWidth(7, 8000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 8000);
        sheet.setColumnWidth(10, 3000);
        sheet.setColumnWidth(11, 8000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 3000);
        sheet.setColumnWidth(14, 8000);
        sheet.setColumnWidth(15, 4000);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setFont(font);

        // 第一行（表头）
        Row row0 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("月份");
        cell0.setCellStyle(style);
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("商家合同名称");
        cell1.setCellStyle(style);
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("账单名称");
        cell2.setCellStyle(style);
        Cell cell3 = row0.createCell(3);
        cell3.setCellValue("区域");
        cell3.setCellStyle(style);
        Cell cell4 = row0.createCell(4);
        cell4.setCellValue("销售员");
        cell4.setCellStyle(style);
        Cell cell5 = row0.createCell(5);
        cell5.setCellValue("责任部门");
        cell5.setCellStyle(style);
        Cell cell6 = row0.createCell(6);
        cell6.setCellValue("确认金额");
        cell6.setCellStyle(style);
        Cell cell7 = row0.createCell(7);
        cell7.setCellValue("发票金额");
        cell7.setCellStyle(style);
        Cell cell8 = row0.createCell(8);
        cell8.setCellValue("开票日期");
        cell8.setCellStyle(style);
        Cell cell9 = row0.createCell(9);
        cell9.setCellValue("收款金额");
        cell9.setCellStyle(style);
        Cell cell10 = row0.createCell(10);
        cell10.setCellValue("收款日期");
        cell10.setCellStyle(style);
        Cell cell11 = row0.createCell(11);
        cell11.setCellValue("未收款金额");
        cell11.setCellStyle(style);
        Cell cell12 = row0.createCell(12);
        cell12.setCellValue("计划回款时间");
        cell12.setCellStyle(style);
        Cell cell13 = row0.createCell(13);
        cell13.setCellValue("超期天数");
        cell13.setCellStyle(style);
        Cell cell14 = row0.createCell(14);
        cell14.setCellValue("备注");
        cell14.setCellStyle(style);
        Cell cell15 = row0.createCell(15);
        cell15.setCellValue("应收账款天数");
        cell15.setCellStyle(style);

        CellStyle style2 = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat(); // 此处设置数据格式
        style2.setDataFormat(df.getFormat("###,###,###,##0.00"));// 数据格式只显示整数

        int RowIndex = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                BillCheckInfoVo vo = list.get(i);
                Row row = sheet.createRow(RowIndex);
                RowIndex++;
                // 月份
                Cell cel0 = row.createCell(0);
                cel0.setCellValue(vo.getCreateMonth() + "");
                cel0.setCellStyle(style2);
                // 商家合同名称
                Cell cel1 = row.createCell(1);
                cel1.setCellValue(vo.getInvoiceName());
                cel1.setCellStyle(style2);
                // 账单名称
                Cell cel2 = row.createCell(2);
                cel2.setCellValue(vo.getBillName());
                cel2.setCellStyle(style2);
                // 区域
                Cell cel3 = row.createCell(3);
                cel3.setCellValue(vo.getArea());
                cel3.setCellStyle(style2);
                // 销售员
                Cell cel4 = row.createCell(4);
                cel4.setCellValue(vo.getSellerName());
                cel4.setCellStyle(style2);
                // 责任部门
                Cell cel5 = row.createCell(5);
                cel5.setCellValue(vo.getDeptName());
                cel5.setCellStyle(style2);
                // 确认金额
                Cell cel6 = row.createCell(6);
                cel6.setCellValue(vo.getConfirmAmount() == null ? 0d : vo.getConfirmAmount().doubleValue());
                cel6.setCellStyle(style2);
                // 发票金额
                Cell cel7 = row.createCell(7);
                cel7.setCellValue(vo.getInvoiceAmount() == null ? 0d : vo.getInvoiceAmount().doubleValue());
                cel7.setCellStyle(style2);
                // 开票日期
                Cell cel8 = row.createCell(8);
                if(null!=vo.getInvoiceDate()){
                    cel8.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(vo.getInvoiceDate()));
                }
                cel8.setCellStyle(style2);
                // 收款金额
                Cell cel9 = row.createCell(9);
                cel9.setCellValue(vo.getReceiptAmount() == null ? 0d : vo.getReceiptAmount().doubleValue());
                cel9.setCellStyle(style2);
                // 收款日期
                Cell cel10 = row.createCell(10);
                if (null != vo.getReceiptDate()) {
                    cel10.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(vo.getReceiptDate()));
                }
                cel10.setCellStyle(style2);
                // 未收款金额
                Cell cel11 = row.createCell(11);
                cel11.setCellValue(vo.getUnReceiptAmount() == null ? 0d : vo.getUnReceiptAmount().doubleValue());
                cel11.setCellStyle(style2);
                // 计划回款时间
                Cell cel12 = row.createCell(12);
                if (null != vo.getExpectReceiptDate()) {
                    cel12.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(vo.getExpectReceiptDate()));
                }
                cel12.setCellStyle(style2);
                // 超期天数
                Cell cel13 = row.createCell(13);
                cel13.setCellValue(vo.getOverDays());
                cel13.setCellStyle(style2);
                // 备注
                Cell cel14 = row.createCell(14);
                cel14.setCellValue(vo.getRemark());
                cel14.setCellStyle(style2);
                // 应收账款天数
                Cell cel15 = row.createCell(15);
                cel15.setCellValue(vo.getReceiptDays());
                cel15.setCellStyle(style2);
            }
        }
    }

    private String getPathReceiptDetail() {
        SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM",
                "EXPORT_RECEIPT_DETAIL_REPORT");
        if (systemCodeEntity == null) {
            throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIPT_DETAIL_REPORT");
        }
        return systemCodeEntity.getExtattr1();
    }
}
