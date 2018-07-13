/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */

package com.jiuyescm.bms.quotation.out.transport.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.properties.util.PropertiesUtil;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayTemplateEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayValueAddedEntity;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineRangeService;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineService;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayTemplateService;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayValueAddedService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.TransportLineImportTemplateDataType;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应付运输费用报价
 * @author wubangjun
 */
@Controller("transportPayTemplateController")
public class PriceTransportPayTemplateController {

	private static final Logger logger = Logger.getLogger(PriceTransportPayTemplateController.class.getName());

	@Resource
	private IPriceTransportPayTemplateService priceTransportPayTemplateService;
	
	@Resource
	private IPriceTransportPayLineService priceTransportPayLineService;
	
	@Resource
	private IPriceTransportPayLineRangeService	priceTransportPayLineRangeService;
	
	@Resource
	private IPriceTransportPayValueAddedService priceTransportPayValueAddedService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型

	@Resource
	private IPriceDispatchService priceDispatchService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IAddressService addressService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubRecordLogService pubRecordLogService;
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceTransportPayTemplateEntity> page, Map<String, Object> param) {
		if(param != null){
			param.put("delFlag", "0");
		}
		PageInfo<PriceTransportPayTemplateEntity> pageInfo = priceTransportPayTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceTransportPayTemplateEntity entity) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		if (entity.getId() == null) {
			String templateNo = sequenceService.getBillNoOne(PriceTransportPayTemplateEntity.class.getName(), "TPMB", "00000");
			entity.setTemplateCode(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceTransportPayTemplateService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增应付运输报价模板,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		} else {
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportPayTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新应付运输报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		}
		return null;
	}
	
	@DataResolver
	public void saveAll(Collection<PriceTransportPayTemplateEntity> datas) {
		if (datas == null) {
			return;
		}
		for (PriceTransportPayTemplateEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				priceTransportPayTemplateService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新应付运输报价模板,模板编码【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				priceTransportPayTemplateService.delete(temp.getId());
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除应付运输报价模板,模板编码【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else {
				// do nothing;
			}
		}
	}
	
	@DataResolver
	public String update(PriceTransportPayTemplateEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportPayTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新应付运输报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}

	@DataResolver
	public String delete(PriceTransportPayTemplateEntity entity) {
		try{
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportPayTemplateService.update(entity);
			//删除TransportTemplate下的运输路线
			Map<String, Object> lineParam = new HashMap<String, Object>();
			lineParam.put("templateId", entity.getId());
			
			List<PriceTransportPayLineEntity> lineList = priceTransportPayLineService.query(lineParam);
			if(lineList!= null && lineList.size()>0){
				for(PriceTransportPayLineEntity line : lineList){
					line.setDelFlag("1");
					line.setLastModifier(JAppContext.currentUserName());
					line.setLastModifyTime(JAppContext.currentTimestamp());
					
					//找到运输路线对应的梯度报价
					Map<String, Object> rangeParam = new HashMap<String, Object>();
					rangeParam.put("lineId", line.getId());
					List<PriceTransportPayLineRangeEntity> rangeList = priceTransportPayLineRangeService.query(rangeParam);
					if(rangeList != null && rangeList.size()>0){
						for(PriceTransportPayLineRangeEntity range: rangeList){
							range.setDelFlag("1");
							range.setLastModifier(JAppContext.currentUserName());
							range.setLastModifyTime(JAppContext.currentTimestamp());
							//删除梯度报价
							priceTransportPayLineRangeService.update(range);
						}
					}
					//删除运输路线
					priceTransportPayLineService.update(line);
				}
			}
			
			//删除TransportTemplate下的运输增值报价
			List<PriceTransportPayValueAddedEntity> valueAddedList = priceTransportPayValueAddedService.query(lineParam);
			if(valueAddedList!= null && valueAddedList.size()>0){
				for(PriceTransportPayValueAddedEntity valueAdded : valueAddedList){
					valueAdded.setDelFlag("1");
					valueAdded.setLastModifier(JAppContext.currentUserName());
					valueAdded.setLastModifyTime(JAppContext.currentTimestamp());
					priceTransportPayValueAddedService.update(valueAdded);
				}
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("删除应付运输报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public String copyTransportTemplate(PriceTransportPayTemplateEntity entity){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		String oldTemplateId = String.valueOf(entity.getId());
		//String oldTemplateNo = entity.getTemplateCode();
		
		String newTemplateNo = sequenceService.getBillNoOne(PriceTransportPayTemplateEntity.class.getName(), "TPMB", "00000");
		entity.setId(null);
		entity.setTemplateCode(newTemplateNo);
		if(entity != null && StringUtils.isNotBlank(entity.getTemplateName())){
			entity.setTemplateName(String.format("%s_2", entity.getTemplateName()));
		}

		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setCreator(JAppContext.currentUserName());
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		priceTransportPayTemplateService.save(entity);
		String newTemplateId = String.valueOf(priceTransportPayTemplateService.findIdByTemplateCode(newTemplateNo));
		if(StringUtils.isNotBlank(newTemplateId) && StringUtils.isNoneBlank(oldTemplateId)){
			//找出之前oldTemplateId对应的路线
			Map<String, Object> lineParam = new HashMap<String, Object>();
			lineParam.put("templateId", oldTemplateId);
			lineParam.put("delFlag", "0");
			List<PriceTransportPayLineEntity> lineList = priceTransportPayLineService.query(lineParam);
			if(lineList!= null && lineList.size()>0){
				Map<String, Object> rangeParam = new HashMap<String, Object>();
				for(PriceTransportPayLineEntity line : lineList){
					String oldLineId = String.valueOf(line.getId());
					//String oldLineNo = line.getTransportLineNo();
					
					line.setId(null);
					line.setTemplateId(newTemplateId);//将运输路线关联到复制后的新模板上
					String lineNo = sequenceService.getBillNoOne(PriceTransportPayLineEntity.class.getName(), "TPL", "00000");
					line.setTransportLineNo(lineNo);
					line.setCreator(JAppContext.currentUserName());
					line.setCreateTime(JAppContext.currentTimestamp());
					line.setLastModifier(JAppContext.currentUserName());
					line.setLastModifyTime(JAppContext.currentTimestamp());
					line.setDelFlag("0");
					priceTransportPayLineService.save(line);
					
					String newLineId = String.valueOf(priceTransportPayLineService.findIdByLineNo(lineNo));
					if(StringUtils.isNoneBlank(oldLineId) && StringUtils.isNotBlank(newLineId)){
						//找到oldlineId对应的梯度报价
						rangeParam.put("lineId", oldLineId);
						lineParam.put("delFlag", "0");
						List<PriceTransportPayLineRangeEntity> rangeList = priceTransportPayLineRangeService.query(rangeParam);
						if(rangeList != null && rangeList.size()>0){
							for(PriceTransportPayLineRangeEntity range: rangeList){
								range.setId(null);
								range.setLineId(newLineId);//将运输梯度报价关联到复制后的新路线上
								range.setCreator(JAppContext.currentUserName());
								range.setCreateTime(JAppContext.currentTimestamp());
								range.setLastModifier(JAppContext.currentUserName());
								range.setLastModifyTime(JAppContext.currentTimestamp());
								range.setDelFlag("0");
							}
						}
						priceTransportPayLineRangeService.saveList(rangeList);
					}
				}
				//priceTransportLineService.saveList(lineList);
			}
		}
		
		return null;
	}
	
	/**
	 * 运输路线模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTransportLineTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_line_template.xlsx");
		return new DownloadFile("运输路线导入模板.xlsx", is);
	}
	//---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 导入运输路线模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportLine(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		//获取当前的id
		String id=(String)parameter.get("id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceTransportPayLineEntity> lineList = new ArrayList<PriceTransportPayLineEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new TransportLineImportTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			// 解析Excel
			lineList = readExcelProduct(file,bs);
			if (null == lineList || lineList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			/*condition.put("tempid", currentNo);*/
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, lineList, map, currentNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			// 获得初步校验后和转换后的数据
			lineList = (List<PriceTransportPayLineEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			
			//设置属性
			String lineNo = null;
			for(int i=0;i<lineList.size();i++){
				PriceTransportPayLineEntity line  =lineList.get(i);
				String billNo = id;
				line.setTemplateId(billNo);
				lineNo = sequenceService.getBillNoOne(PriceTransportPayLineEntity.class.getName(), "TPL", "00000");
				line.setTransportLineNo(lineNo);
				line.setDelFlag("0");// 设置为未作废
				line.setCreator(userName);
				line.setCreateTime(currentTime);
				line.setLastModifier(userName);
				line.setLastModifyTime(currentTime);
			}
			//插入正式表
			int insertNum = priceTransportPayLineService.saveList(lineList);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("存档运输路线表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入应付运输线路,共计【"+insertNum+"】条");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return map;
			}
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			// TODO: handle exception
		} finally{
			PropertiesUtil proUtil = new PropertiesUtil();
			String path = proUtil.getImpExcelFilePath();
			File storeFolder = new File(path);
			// 如果存放上传文件的目录不存在就新建
			if (!storeFolder.isDirectory()) {
				storeFolder.mkdirs();
			}
			File destFile = FileOperationUtil.getDestFile(file.getFileName(), storeFolder);
			// 将文件放到相应目录
			file.transferTo(destFile);
		}
		

		return map;
	}
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<PriceTransportPayLineEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			//BaseDataType ddt = new DispatchTemplateDataType();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<PriceTransportPayLineEntity> lineList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceTransportPayLineEntity p = (PriceTransportPayLineEntity) BeanToMapUtil.convertMapNull(PriceTransportPayLineEntity.class, data);
				lineList.add(p);
			}
			return lineList;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 校验Excel里面的内容合法性
	 * 
	 * @param infoList
	 * @param list
	 * @param map
	 * @param objList
	 * @param currentNo
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceTransportPayLineEntity> lineList, Map<String, Object> map, String currentNo) {
		//判断省市区 存不存在
		List<RegionVo> regionList = new ArrayList<RegionVo>();
		List<RegionVo> targetList = new ArrayList<RegionVo>();
		int lineNum=1;
		for (int i = 0; i < lineList.size(); i++) {
			
			//当前行号
			lineNum = lineNum+1;	
			PriceTransportPayLineEntity lineData = lineList.get(i);
			//==============================================================================================================
			//判断地址是否为空
			if(StringUtils.isBlank(lineData.getFromProvinceName()) || StringUtils.isBlank(lineData.getToProvinceName())){
				setMessage(infoList, lineNum,"始发省份或者目的省份不能为空!");
			}
			if(StringUtils.isBlank(lineData.getFromCityName()) || StringUtils.isBlank(lineData.getToCityName())){
				setMessage(infoList, lineNum,"始发城市或者目的城市不能为空!");
			}
			if(StringUtils.isBlank(lineData.getFromDistrictName()) || StringUtils.isBlank(lineData.getToDistrictName())){
				setMessage(infoList, lineNum,"始发地区或者目的地区不能为空!");
			}
			//===================================始发校验======================================================================
			//起始地址
			RegionVo regionVo = new RegionVo();
			regionVo.setProvince(lineData.getFromProvinceName());
			regionVo.setCity(lineData.getFromCityName());
			regionVo.setDistrict(lineData.getFromDistrictName());
			regionVo.setLineNo(lineNum);
			regionList.add(regionVo);
			//目的地址
			RegionVo targetVo = new RegionVo();
			targetVo.setProvince(lineData.getToProvinceName());
			targetVo.setCity(lineData.getToCityName());
			targetVo.setDistrict(lineData.getToDistrictName());
			targetVo.setLineNo(lineNum);
			targetList.add(targetVo);
		}
		
		List<ErrorMsgVo> errorMsgList = null;
		List<RegionVo> correctRegionList = null;
		if(regionList != null && regionList.size()>0){
			RegionEncapVo regionEncapVo = addressService.matchStandardByAlias(regionList);
			//获取此时的正确信息和报错信息
			errorMsgList = regionEncapVo.getErrorMsgList();
			correctRegionList = regionEncapVo.getRegionList();
			if(errorMsgList != null && errorMsgList.size()>0){
				for(ErrorMsgVo error:errorMsgList){
					setMessage(infoList, error.getLineNo(),error.getErrorMsg());
				}
			}
		}
		List<ErrorMsgVo> errorTargetList = null;
		List<RegionVo> correctTargetList = null;
		if(targetList != null && targetList.size()>0){
			RegionEncapVo targetEncapVo = addressService.matchStandardByAlias(targetList);
			//获取此时的正确信息和报错信息
			errorTargetList = targetEncapVo.getErrorMsgList();
			correctTargetList = targetEncapVo.getRegionList();
			if(errorTargetList != null && errorTargetList.size()>0){
				for(ErrorMsgVo error : errorTargetList){
					setMessage(infoList, error.getLineNo(), error.getErrorMsg());
				}
			}
		}
		
		
		int lineNo = 0;
		for (int i = 0; i < lineList.size(); i++) {
			PriceTransportPayLineEntity line = lineList.get(i);
			lineNo = lineNo+1;
				
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			String warehouseName= line.getFromWarehouseName();
			if(StringUtils.isNotBlank(warehouseName)){			
				WarehouseVo warehouse = warehouseService.queryWarehouseByWarehouseName(warehouseName);
				if(warehouse == null){
					setMessage(infoList, lineNo,"始发仓名称没有在仓库表中维护!");
				}else if(warehouse.getWarehouseid()!=null){
					line.setFromWarehouseId(warehouse.getWarehouseid());
				}
			}
			String endWHName= line.getEndWarehouseName();
			if(StringUtils.isNotBlank(endWHName)){
				WarehouseVo endWH = warehouseService.queryWarehouseByWarehouseName(warehouseName);
				if(endWH == null){
					setMessage(infoList, lineNo,"目的仓名称没有在仓库表中维护!");
				}else if(endWH.getWarehouseid()!=null){
					line.setEndWarehouseId(endWH.getWarehouseid());
				}
			}
			
			//判断时效信息是否在时效表中维护
			String time= line.getTimeliness();
			if(StringUtils.isNotBlank(time)){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("typeCode", "DISPATCH_TIME");
				param.put("codeName", time);
				List<SystemCodeEntity> timelessList= systemCodeService.queryCodeList(param);
				if(timelessList==null || timelessList.size()==0){
					setMessage(infoList, lineNo,"时效没有在表中维护!");
				}else{
					line.setTimeliness(timelessList.get(0).getCode());
				}
			}
			//===================================起始地址 校验======================================================================
			//如匹配到到名称则将code设值
			if(errorMsgList.size()<=0){
					for(RegionVo region : correctRegionList){
						if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(line.getFromProvinceName()) && region.getProvince().equalsIgnoreCase(line.getFromProvinceName())){
							line.setFromProvinceId(region.getProvincecode());
							if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(line.getFromCityName()) && region.getCity().equalsIgnoreCase(line.getFromCityName())){
								line.setFromCityId(region.getCitycode());
								if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(line.getFromDistrictName()) && region.getDistrict().equalsIgnoreCase(line.getFromDistrictName())){
									line.setFromDistrictId(region.getDistrictcode());	
									break;
								}
							}
						}
					}
			}
			//===================================目的地址 校验======================================================================
			//如匹配到到名称则将code设值
			if(errorTargetList.size()<=0){
				for(RegionVo region : correctTargetList){
					if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(line.getToProvinceName()) && region.getProvince().equalsIgnoreCase(line.getToProvinceName())){
						line.setToProvinceId(region.getProvincecode());
						if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(line.getToCityName()) && region.getCity().equalsIgnoreCase(line.getToCityName())){
							line.setToCityId(region.getCitycode());
							if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(line.getToDistrictName()) && region.getDistrict().equalsIgnoreCase(line.getToDistrictName())){
								line.setToDistrictId(region.getDistrictcode());	
								break;
							}
						}
					}
				}
			}
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, lineList); // 无基本错误信息
			}
			
		}
		return map;
	}
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	
	
	//------------------------------------------------------------------------
	/**
	 * 复制运输报价和增值服务报价
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String copyTransportTemplateValueAdded(PriceTransportPayTemplateEntity entity){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		String oldTemplateId = String.valueOf(entity.getId());
		//String oldTemplateNo = entity.getTemplateCode();
		
		String newTemplateNo = sequenceService.getBillNoOne(PriceTransportPayTemplateEntity.class.getName(), "TPMB", "00000");
		entity.setId(null);
		entity.setTemplateCode(newTemplateNo);
		if(entity != null && StringUtils.isNotBlank(entity.getTemplateName())){
			entity.setTemplateName(String.format("%s_2", entity.getTemplateName()));
		}

		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setCreator(JAppContext.currentUserName());
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		priceTransportPayTemplateService.save(entity);
		String newTemplateId = String.valueOf(priceTransportPayTemplateService.findIdByTemplateCode(newTemplateNo));
		if(StringUtils.isNotBlank(newTemplateId) && StringUtils.isNoneBlank(oldTemplateId)){
			//找出之前oldTemplateId对应的路线
			Map<String, Object> valueAddedParam = new HashMap<String, Object>();
			valueAddedParam.put("templateId", oldTemplateId);
			valueAddedParam.put("delFlag", "0");
			List<PriceTransportPayValueAddedEntity> valueAddedList = priceTransportPayValueAddedService.query(valueAddedParam);
			if(valueAddedList!= null && valueAddedList.size()>0){
				for(PriceTransportPayValueAddedEntity valueAdded : valueAddedList){
					valueAdded.setId(null);
					valueAdded.setTemplateId(newTemplateId);//将增值服务报价关联到复制后的新模板上
					String quotationNo = sequenceService.getBillNoOne(PriceTransportPayValueAddedEntity.class.getName(), "PVA", "00000");
					valueAdded.setQuotationNo(quotationNo);
					valueAdded.setCreator(JAppContext.currentUserName());
					valueAdded.setCreateTime(JAppContext.currentTimestamp());
					valueAdded.setLastModifier(JAppContext.currentUserName());
					valueAdded.setLastModifyTime(JAppContext.currentTimestamp());
					valueAdded.setDelFlag("0");
				}
				priceTransportPayValueAddedService.saveList(valueAddedList);
			}
		}
		
		return null;
	}
}
