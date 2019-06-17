package com.jiuyescm.bms.bill.customer.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerDetailService;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.vo.ExportDataVoEntity;
import com.jiuyescm.bms.common.web.HttpCommanExport;
import com.jiuyescm.common.utils.upload.BillProcessReportType;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("billCustomerDetailController")
public class BillCustomerDetailController {

	private static final Logger logger = LoggerFactory.getLogger(BillCustomerDetailController.class.getName());

	@Autowired
	private IBillCustomerDetailService billCustomerDetailService;
	@Autowired
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Autowired
	private ISystemCodeService systemCodeService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BillCustomerDetailEntity findById(Long id) throws Exception {
		return billCustomerDetailService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillCustomerDetailEntity> page, Map<String, Object> param) {
	    if (null == param) {
            param = new HashMap<String, Object>();
        }
	    try {
	        if(param.get("receiptDate")!="" && param.get("receiptDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("receiptDate"));
	            dateString=dateString+" 00:00:00";
	            param.put("receiptDate", formatter.parse(dateString));
	        }
	        if(param.get("receiptEndDate")!="" && param.get("receiptEndDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("receiptEndDate"));
	            dateString=dateString+" 23:59:59";
	            param.put("receiptEndDate", formatter.parse(dateString));
	        }
	        if(param.get("invoiceDate")!="" && param.get("invoiceDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("invoiceDate"));
	            dateString=dateString+" 00:00:00";
	            param.put("invoiceDate", formatter.parse(dateString));
	        }
	        if(param.get("invoiceEndDate")!="" && param.get("invoiceEndDate")!=null){
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	            String dateString=formatter.format(param.get("invoiceEndDate"));
	            dateString=dateString+" 23:59:59";
	            param.put("invoiceEndDate", formatter.parse(dateString));
	        } 
        } catch (Exception e) {
            logger.error("日期转换异常：", e);
            throw new BizException("日期转换异常！");
        }    
		PageInfo<BillCustomerDetailEntity> pageInfo = billCustomerDetailService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(BillCustomerDetailEntity entity) {
		if (null == entity.getId()) {
			billCustomerDetailService.save(entity);
		} else {
			billCustomerDetailService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BillCustomerDetailEntity entity) {
		billCustomerDetailService.delete(entity.getId());
	}
	
	/**
	 * 导出
	 */
    @FileProvider
    public DownloadFile downLoadData(Map<String,Object> parameter) throws Exception{
        try{
            String path=getPath();
            HttpCommanExport commanExport=new HttpCommanExport(path);
            ExportDataVoEntity voEntity=new ExportDataVoEntity();
            voEntity.setTitleName("结算进度报表导出");
            voEntity.setBaseType(new BillProcessReportType());
            voEntity.setDataList(getDataList(parameter));
            return commanExport.exportFile(voEntity);
        }catch(Exception e){
            //写入日志
            bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
            throw e;
        }
        
    }
    
    private String getPath(){
        SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_BILL_PROCESS_REPORT ");
        if(systemCodeEntity == null){
            throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_BILL_PROCESS_REPORT ");
        }
        return systemCodeEntity.getExtattr1();
    }
    
    private List<Map<String,Object>> getDataList(Map<String,Object> parameter) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BillCustomerDetailEntity> list = billCustomerDetailService.query(parameter);
        List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=null;
        for (BillCustomerDetailEntity entity : list) {
            map = new HashMap<>();
            map.put("createMonth",entity.getCreateMonth());
            map.put("mkInvoiceName", entity.getMkInvoiceName());
            if ("0".equals(entity.getIsPrepare())) {
                map.put("isPrepare", "否");
            } else if ("1".equals(entity.getIsPrepare())) {
                map.put("isPrepare", "是");
            } else if ("2".equals(entity.getIsPrepare())) {
                map.put("isPrepare", "部分生成");
            }
            map.put("prepareTime", sdf.format(entity.getPrepareTime()));
            map.put("prepareAmount", entity.getPrepareAmount());
            map.put("isImport", "0".equals(entity.getIsImport())?"否":"是");
            map.put("billName", entity.getBillName());
            map.put("billCheckStatus", BillCheckStateEnum.getDesc(entity.getBillCheckStatus()));
            map.put("confirmDate", sdf.format(entity.getConfirmDate()));
            map.put("invoiceStatus", BillCheckInvoiceStateEnum.getDesc(entity.getInvoiceStatus()));
            map.put("invoiceDate", sdf.format(entity.getInvoiceDate()));
            map.put("receiptStatus", BillCheckReceiptStateEnum.getDesc(entity.getReceiptStatus()));
            map.put("receiptDate", sdf.format(entity.getReceiptDate()));
            map.put("confirmAmount", entity.getConfirmAmount());
            map.put("invoiceAmount", entity.getInvoiceAmount());
            map.put("receiptAmount", entity.getReceiptAmount());
            map.put("balanceName", entity.getBalanceName());
            mapList.add(map);
        }
        return mapList;
    }
	
}
