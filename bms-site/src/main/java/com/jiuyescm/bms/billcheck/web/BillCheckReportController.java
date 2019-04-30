package com.jiuyescm.bms.billcheck.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

		if (param == null){
			param = new HashMap<String, Object>();
		}

		List<String> userIds=new ArrayList<String>();
		BmsGroupUserVo groupUser=bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if(groupUser!=null){//加入權限組
			//判断是否是管理员
			if("0".equals(groupUser.getAdministrator())){//管理员
				
			}else{//非管理员
				userIds=bmsGroupUserService.queryContainUserIds(groupUser);
				StringBuffer user=new StringBuffer();
				for(int i=0;i<userIds.size();i++){
					if(i==userIds.size()-1){
						user.append(userIds.get(i));
					}else{
						user.append(userIds.get(i)+"|");
					}				
				}
				if(StringUtils.isNotBlank(user)){
					param.put("userIds", user);
				}
			}	
		
			PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(), page.getPageSize());
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

		List<BillCheckReceiptSumVo> sumList=queryReport(param);

		if (sumList.size()>0) {
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

		if (param == null){
			param = new HashMap<String, Object>();
		}
		PageInfo<BillCheckInvoiceVo> pageInfo = billCheckInvoiceService.queryReport(param, page.getPageNo(), page.getPageSize());
		//PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(), page.getPageSize());
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

		if (param == null){
			param = new HashMap<String, Object>();
		}
		PageInfo<BillCheckReceiptVo> pageInfo = billCheckReceiptService.queryReport(param, page.getPageNo(), page.getPageSize());
		//PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.queryReceiptDetail(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 预计回款信息查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryExpectReceiptReport(Page<BillExpectReceiptVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}

		List<BillExpectReceiptVo> volist=new ArrayList<BillExpectReceiptVo>();
		
		PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null && pageInfo.getList().size()>0) {
			List<BillCheckInfoVo> list=pageInfo.getList();	
			
			
			//确认金额汇总
			BigDecimal totalConfirmAmount=new BigDecimal(0);
			//截止日前已回款金额汇总
			BigDecimal totalNowReceiptMoney=new BigDecimal(0);
			//截止日前剩余待回款金额汇总
			BigDecimal totalNowTbReceiptMoney=new BigDecimal(0);
			//调整金额汇总
			BigDecimal totalAdjustMoney=new BigDecimal(0);
			
			
			for(BillCheckInfoVo vo:list){
				BigDecimal money=new BigDecimal(0);
				BillExpectReceiptVo entity=new BillExpectReceiptVo();
				entity.setCreateMonth(vo.getCreateMonth());
				entity.setInvoiceName(vo.getInvoiceName());
				entity.setBillName(vo.getBillName());
				entity.setBillStatus(vo.getBillStatus());
				entity.setInvoiceStatus(vo.getInvoiceStatus());
				entity.setReceiptStatus(vo.getReceiptStatus());
				entity.setSellerId(vo.getSellerId());
				entity.setSellerName(vo.getSellerName());
				entity.setConfirmAmount(vo.getConfirmAmount()==null?money:vo.getConfirmAmount());
				entity.setNowTbReceiptMoney(vo.getConfirmAmount()==null?money:vo.getConfirmAmount());
				entity.setNowReceiptMoney(money);
				entity.setAdjustMoney(money);
				entity.setArea(vo.getArea());
			
				param.put("id", vo.getId());
				param.put("receiptDate", param.get("expireDate"));
				BillCheckReceiptVo billCheckReceiptVo=billCheckReceiptService.queyReceipt(param);
				if(billCheckReceiptVo!=null){
					entity.setNowLastReceiptDate(billCheckReceiptVo.getReceiptDate());
					entity.setNowReceiptMoney(billCheckReceiptVo.getReceiptAmount()==null?money:billCheckReceiptVo.getReceiptAmount());
					if(billCheckReceiptVo.getReceiptAmount()!=null){
						entity.setNowTbReceiptMoney(vo.getConfirmAmount().subtract(billCheckReceiptVo.getReceiptAmount()));
					}
				}
				
				//预计回款日期
				param.put("expectReceiptDate", param.get("expireDate"));
				BillReceiptFollowVo receiptFollowVo=billCheckInfoService.queryReceiptFollow(param);
				if(receiptFollowVo!=null){
					entity.setNowLastExpectReceiptDate(receiptFollowVo.getExpectReceiptDate());
				}
				
				//调整金额
				param.put("billCheckId", vo.getId());
				BillCheckAdjustInfoVo adjustVo=billCheckInfoService.queryOneAdjust(param);
				if(adjustVo!=null && adjustVo.getAdjustAmount()!=null){
					entity.setAdjustMoney(adjustVo.getAdjustAmount()==null?money:adjustVo.getAdjustAmount());
				}
				
				
				//汇总
				totalConfirmAmount=totalConfirmAmount.add(entity.getConfirmAmount());
				totalNowReceiptMoney=totalNowReceiptMoney.add(entity.getNowReceiptMoney());
				totalNowTbReceiptMoney=totalNowTbReceiptMoney.add(entity.getNowTbReceiptMoney());
				totalAdjustMoney=totalAdjustMoney.add(entity.getAdjustMoney());
				
				volist.add(entity);
			}
			for(BillExpectReceiptVo vo:volist){
				vo.setTotalConfirmAmount(totalConfirmAmount);
				vo.setTotalNowReceiptMoney(totalNowReceiptMoney);
				vo.setTotalNowTbReceiptMoney(totalNowTbReceiptMoney);
				vo.setTotalAdjustMoney(totalAdjustMoney);
			}
		}
	
		if (volist.size()>0) {
			page.setEntities(volist);
			page.setEntityCount((int)pageInfo.getTotal());
		}
	}
	
	
	public void sumMoney(BillCheckReceiptSumVo sumVo,BillCheckInfoVo vo){
		//开票未回款（账单已确认:待开票、待收款、已收款）
		if(CheckBillStatusEnum.TB_INVOICE.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.RECEIPTED.getCode().equals(vo.getBillStatus())){
			BigDecimal money=new BigDecimal(0);
			//包含以下数据：
			//1.是否需要发票=否，计算账单确认额-收款金额
			//2.是否需要发票=是，计算已开票和部分开票的账单确认额-收款金额
			if("0".equals(vo.getIsneedInvoice())){
				if(vo.getConfirmAmount()!=null){
					money=vo.getConfirmAmount();
				}
				if(vo.getReceiptAmount()!=null){
					money=money.subtract(vo.getReceiptAmount());
				}
				
			}else if("1".equals(vo.getIsneedInvoice())){
				//已开票和部分开票
				if(BillCheckInvoiceStateEnum.INVOICED.getCode().equals(vo.getInvoiceStatus()) || BillCheckInvoiceStateEnum.PART_INVOICE.getCode().equals(vo.getInvoiceStatus())){
					if(vo.getConfirmAmount()!=null){
						money=vo.getConfirmAmount();
					}
					if(vo.getReceiptAmount()!=null){
						money=money.subtract(vo.getReceiptAmount());
					}
				}
			}
			if(sumVo.getInvoiceUnReceiptAmount()==null){
				sumVo.setInvoiceUnReceiptAmount(money);
			}else{
				sumVo.setInvoiceUnReceiptAmount(sumVo.getInvoiceUnReceiptAmount().add(money));
			}
			if(sumVo.getTotalAmount()==null){
				sumVo.setTotalAmount(money);
			}else{
				sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
			}
			
		}
		
		//未开票未回款（账单已确认:待开票、待收款、已收款）
		if(CheckBillStatusEnum.TB_INVOICE.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.TB_RECEIPT.getCode().equals(vo.getBillStatus()) || CheckBillStatusEnum.RECEIPTED.getCode().equals(vo.getBillStatus())){
			BigDecimal money=new BigDecimal(0);
			//包含以下数据：
			//1.是否需要发票=是，计算未开票的账单确认额-收款金额
			if("1".equals(vo.getIsneedInvoice())){
				if(BillCheckInvoiceStateEnum.NO_INVOICE.getCode().equals(vo.getInvoiceStatus())){
					if(vo.getConfirmAmount()!=null){
						money=vo.getConfirmAmount();
					}
					if(vo.getReceiptAmount()!=null){
						money=money.subtract(vo.getReceiptAmount());
					}				
				}
			}
			if(sumVo.getUnInvoiceUnReceiptAmount()==null){
				sumVo.setUnInvoiceUnReceiptAmount(money);
			}else{
				sumVo.setUnInvoiceUnReceiptAmount(sumVo.getUnInvoiceUnReceiptAmount().add(money));
			}
			if(sumVo.getTotalAmount()==null){
				sumVo.setTotalAmount(money);
			}else{
				sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
			}
		}
		
		//账单未确认
		if(CheckBillStatusEnum.TB_CONFIRMED.getCode().equals(vo.getBillStatus())){
			//包含以下数据：
			//1.计算账单确认额-收款金额
			BigDecimal money=new BigDecimal(0);
			if(vo.getConfirmAmount()!=null){
				money=vo.getConfirmAmount();
			}
			if(vo.getReceiptAmount()!=null){
				money=money.subtract(vo.getReceiptAmount());
			}		
			if(sumVo.getUnConfirmAmount()==null){
				sumVo.setUnConfirmAmount(money);
			}else{
				sumVo.setUnConfirmAmount(sumVo.getUnConfirmAmount().add(money));
			}
			if(sumVo.getTotalAmount()==null){
				sumVo.setTotalAmount(money);
			}else{
				sumVo.setTotalAmount(sumVo.getTotalAmount().add(money));
			}
		}
	}
	
	
	private List<BillCheckReceiptSumVo> queryReport(Map<String,Object> param){
        //统计
        if (param == null){
            param = new HashMap<String, Object>();
        }

        PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.querySimple(param, 0, Integer.MAX_VALUE);
        
        Map<String, BillCheckReceiptSumVo> map=new HashMap<String, BillCheckReceiptSumVo>();            

        if(pageInfo!=null && pageInfo.getList().size()>0){      
            //key:部门  value:实体类
            List<BillCheckInfoVo> list=pageInfo.getList();      
            for(BillCheckInfoVo vo:list){
                //部门
                if(StringUtils.isNotBlank(vo.getDeptName())){
                    String deptName=vo.getDeptName();
                    if(map.containsKey(deptName)){
                        //部门名称已存在
                        BillCheckReceiptSumVo sumVo=map.get(deptName);
                        sumMoney(sumVo,vo);
                    }else{
                        //部门不存在时
                        BillCheckReceiptSumVo sumVo=new BillCheckReceiptSumVo();
                        sumVo.setDeptName(deptName);
                        sumMoney(sumVo,vo);
                        map.put(deptName, sumVo);
                    }
                    
                }
            }
        }
        List<BillCheckReceiptSumVo> sumList=new ArrayList<BillCheckReceiptSumVo>();
        
        for(String key : map.keySet()) { 
            BillCheckReceiptSumVo value = map.get(key); 
            //排除账单未确认金额为空的情况
            if(null==value.getUnConfirmAmount()){
                value.setUnConfirmAmount(BigDecimal.ZERO);
            }
            sumList.add(value);
        }
        return sumList;
	}
	
	   /**
     * 应收情况导出
     * @param param
     * @return
     * @throws Exception
     */
    @FileProvider
    public DownloadFile export(Map<String,Object> param) throws Exception{

        List<BillCheckReceiptSumVo> sumList=queryReport(param);
        
        long beginTime = System.currentTimeMillis();
        logger.info("====应收情况报表导出：写入Excel begin.");
        
        try {
//          String filePath =getName() + FileConstant.SUFFIX_XLSX;
            //如果存放上传文件的目录不存在就新建
            String path = getPath();
            File storeFolder = new File(path);
            if(!storeFolder.isDirectory()){
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
                                    
            //导出方法
            hand(poiUtil, workbook, filePath, param,sumList);
            
            //最后写到文件
            poiUtil.write2FilePath(workbook, filePath);
                        
            logger.info("====应收情况报表：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

            InputStream is = new FileInputStream(filePath);
            return new DownloadFile(fileName, is);
        } catch (Exception e) {
            //bmsErrorLogInfoService.
            logger.error("应收情况报表导出失败", e);
        }
        return null;
    }
    
    
    /**
     * 应收情况汇总表导出
     */
    private void hand(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
            String path, Map<String, Object> param,List<BillCheckReceiptSumVo> sumList)throws Exception{

        logger.info("应收情况汇总表导出...");
        Sheet sheet = poiUtil.getXSSFSheet(workbook,"应收情况汇总表");
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

        //第一行（表头）
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
        style2.setDataFormat(df.getFormat("###,###,###,##0.00"));//数据格式只显示整数

        int RowIndex = 1;
        if(CollectionUtils.isNotEmpty(sumList)){
            for(int i=0;i<sumList.size();i++){ 
                BillCheckReceiptSumVo vo = sumList.get(i);
                Row row = sheet.createRow(RowIndex);
                RowIndex++;
                //责任部门名称
                Cell cel0 = row.createCell(0);
                cel0.setCellValue(vo.getDeptName());
                cel0.setCellStyle(style2);
                //开票未回款金额
                Cell cel1 = row.createCell(1);
                cel1.setCellValue(vo.getInvoiceUnReceiptAmount()==null?0d:vo.getInvoiceUnReceiptAmount().doubleValue());
                cel1.setCellStyle(style2);
                //未开票未回款金额
                Cell cel2 = row.createCell(2);
                cel2.setCellValue(vo.getUnInvoiceUnReceiptAmount()==null?0d:vo.getUnInvoiceUnReceiptAmount().doubleValue());
                cel2.setCellStyle(style2);
                //账单未确认金额
                Cell cel3 = row.createCell(3);
                cel3.setCellValue(vo.getUnConfirmAmount()==null?0d:vo.getUnConfirmAmount().doubleValue());
                cel3.setCellStyle(style2);
                //小计
                Cell cel4 = row.createCell(4);
                cel4.setCellValue(vo.getTotalAmount()==null?0d:vo.getTotalAmount().doubleValue());
                cel4.setCellStyle(style2);
            }
        }
    }
    
    private String getPath(){
        SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIPT_SUM_REPORT");
        if(systemCodeEntity == null){
            throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIPT_SUM_REPORT");
        }
        return systemCodeEntity.getExtattr1();
    }
    
    //自定义数字格式方法  
    public static String getFormat(String style,BigDecimal value){  
        DecimalFormat df = new DecimalFormat();  
        df.applyPattern(style);// 将格式应用于格式化器  
        return df.format(value.doubleValue());  
    }
}
