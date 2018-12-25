/**
 * 
 */
package com.jiuyescm.bms.report.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.report.biz.entity.BizWarehouseImportReportEntity;
import com.jiuyescm.bms.report.biz.entity.BizWarehouseNotImportEntity;
import com.jiuyescm.bms.report.biz.repository.IBizWarehouseImportReportRepository;
import com.jiuyescm.bms.report.biz.repository.IBizWarehouseNotImportRepository;
import com.jiuyescm.bms.report.biz.service.IBizWarehouseImportReportService;
import com.jiuyescm.bms.report.vo.BizWarehouseImportReportVo;
import com.jiuyescm.bms.report.vo.BizWarehouseNotImportVo;

/**
 * @author yangss&liuzhicheng
 */
@Service("bizWarehouseImportReportService")
public class BizWarehouseImportReportServiceImpl implements IBizWarehouseImportReportService{
	
	private static final Logger logger = Logger.getLogger(BizWarehouseImportReportServiceImpl.class.getName());

	@Autowired
	private IBizWarehouseImportReportRepository bizWarehouseImportReportRepository;
	
	@Autowired
	private IBizWarehouseNotImportRepository bizWarehouseNotImportRepository;
	
	@Override
	public PageInfo<BizWarehouseImportReportVo> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		PageInfo<BizWarehouseImportReportVo> pageVoInfo = null;
		try {
			pageVoInfo = new PageInfo<BizWarehouseImportReportVo>();
			PageInfo<BizWarehouseImportReportEntity> pageInfo = bizWarehouseImportReportRepository.query(condition, pageNo, pageSize);
			
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BizWarehouseImportReportVo> list=new ArrayList<BizWarehouseImportReportVo>();
				for(BizWarehouseImportReportEntity entity : pageInfo.getList()){
					BizWarehouseImportReportVo vo = new BizWarehouseImportReportVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			}
		} catch (Exception e) {
			logger.error("各仓业务数据导入查询异常:",e);
		}
		return pageVoInfo;
	}
	
	@Override
	public List<BizWarehouseImportReportVo> queryAll(Map<String, Object> condition) {
		List<BizWarehouseImportReportVo> listVo = null;
		List<BizWarehouseImportReportEntity> list = bizWarehouseImportReportRepository.queryAll(condition);
		try {
			if(list != null && list.size() > 0){
				listVo = new ArrayList<BizWarehouseImportReportVo>();
				for(BizWarehouseImportReportEntity entity : list){
					BizWarehouseImportReportVo vo = new BizWarehouseImportReportVo();
					PropertyUtils.copyProperties(vo, entity);
					listVo.add(vo);
				}
			}
		} catch (Exception e) {
			logger.error("queryStorage:",e);
		}
		return listVo;
	}

	@Override
	public PageInfo<BizWarehouseNotImportVo> queryNotImport(
			Map<String, Object> condition,int pageNo, int pageSize) {
		PageInfo<BizWarehouseNotImportEntity> entityPage= bizWarehouseNotImportRepository.queryNotImport(condition,pageNo, pageSize);
		List<BizWarehouseNotImportEntity> entityList = entityPage.getList();
		
		if(entityPage != null && entityPage.getList().size() > 0){
			PageInfo<BizWarehouseNotImportVo> pageVoInfo = new PageInfo<BizWarehouseNotImportVo>();
			try {
				PropertyUtils.copyProperties(pageVoInfo, entityPage);
				List<BizWarehouseNotImportVo> list=new ArrayList<BizWarehouseNotImportVo>();
				for(BizWarehouseNotImportEntity entity : entityPage.getList()){
					BizWarehouseNotImportVo vo = new BizWarehouseNotImportVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				pageVoInfo.setList(list);
			} catch (Exception e) {
				logger.error("未导入商家查询异常:",e);
			}
			return pageVoInfo;
		}else{
			return null;
		}
//			List<BizWarehouseNotImportVo> voList = new ArrayList<BizWarehouseNotImportVo>();
//			for(BizWarehouseNotImportEntity entity : entityList){
//				BizWarehouseNotImportVo vo = new BizWarehouseNotImportVo();
//				try {
//					PropertyUtils.copyProperties(vo, entity);
//				} catch (IllegalAccessException | InvocationTargetException
//						| NoSuchMethodException e) {
//					e.printStackTrace();
//				}
//				voList.add(vo);
//			}
//			//使用list进行对比，找出未导入商家
//			List<String> actualImportList = new ArrayList<String>();
//			for(int i=0;i<voList.size();i++){
//				if("ACTUAL".equals(voList.get(i).getImportType())){
//					actualImportList.add(voList.get(i).getCustomerId());
//				}
//			}
//			List<BizWarehouseNotImportVo> notImportList = new ArrayList<BizWarehouseNotImportVo>();
//			for(int i=0;i<voList.size();i++){
//				if("THEORY".equals(voList.get(i).getImportType())&&!actualImportList.contains(voList.get(i).getCustomerId())){
//					notImportList.add(voList.get(i));
//				}
//			}
//			PageHelper.startPage(pageNo, pageSize);
//			PageInfo<BizWarehouseNotImportVo> page = new PageInfo<BizWarehouseNotImportVo>(notImportList);
		
	}
}
