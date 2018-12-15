package com.jiuyescm.bms.report.bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.bill.repository.IReportBillImportDetailRepository;
import com.jiuyescm.bms.report.vo.ReportBillBizDetailVo;
import com.jiuyescm.bms.report.vo.ReportBillReceiptDetailVo;
import com.jiuyescm.bms.report.vo.ReportBillStorageDetailVo;

@Service("reportBillImportDetailService")
public class ReportBillImportDetailServiceImpl implements IReportBillImportDetailService {
	
	private static final Logger logger = Logger.getLogger(ReportBillImportDetailServiceImpl.class.getName());

	@Autowired IReportBillImportDetailRepository billImportDetailRepository;

	@Override
	public PageInfo<ReportBillReceiptDetailVo> queryReceipt(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		
		PageInfo<ReportBillReceiptDetailVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<ReportBillReceiptDetailVo>();
			PageInfo<ReportBillReceiptDetailEntity> pageInfo=billImportDetailRepository.queryReceipt(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportBillReceiptDetailVo> list=new ArrayList<ReportBillReceiptDetailVo>();
				for(ReportBillReceiptDetailEntity entity:pageInfo.getList()){
					ReportBillReceiptDetailVo vo=new ReportBillReceiptDetailVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("queryReceipt:",e);
		}
		return pageVoInfo;
	}

	@Override
	public PageInfo<ReportBillStorageDetailVo> queryStorage(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<ReportBillStorageDetailVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<ReportBillStorageDetailVo>();
			PageInfo<ReportBillStorageDetailEntity> pageInfo=billImportDetailRepository.queryStorage(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportBillStorageDetailVo> list=new ArrayList<ReportBillStorageDetailVo>();
				for(ReportBillStorageDetailEntity entity:pageInfo.getList()){
					ReportBillStorageDetailVo vo=new ReportBillStorageDetailVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("queryStorage:",e);
		}
		return pageVoInfo;
	}

	@Override
	public PageInfo<ReportBillBizDetailVo> queryBiz(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageInfo<ReportBillBizDetailVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<ReportBillBizDetailVo>();
			PageInfo<ReportBillBizDetailEntity> pageInfo=billImportDetailRepository.queryBiz(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<ReportBillBizDetailVo> list=new ArrayList<ReportBillBizDetailVo>();
				for(ReportBillBizDetailEntity entity:pageInfo.getList()){
					ReportBillBizDetailVo vo=new ReportBillBizDetailVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			logger.error("queryBiz:",e);
		}
		return pageVoInfo;
	}

	@Override
	public List<Map<String, Object>> queryReceiptExport(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return billImportDetailRepository.queryReceiptExport(condition);
	}

	
	

}
