/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */

package com.jiuyescm.bms.quotation.transport.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
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
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.constants.TransportMessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.common.enumtype.TransportProductTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.CommonComparePR;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportValueAddedEntity;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineRangeService;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineService;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportTemplateService;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportValueAddedService;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("transportTemplateController")
public class PriceTransportTemplateController extends CommonComparePR<PriceTransportLineEntity>{

	private static final Logger logger = Logger.getLogger(PriceTransportTemplateController.class.getName());

	@Resource
	private IPriceTransportTemplateService priceTransportTemplateService;

	@Resource
	private IPriceTransportLineService priceTransportLineService;

	@Resource
	IPriceTransportLineRangeService priceTransportLineRangeService;

	@Resource
	IPriceTransportValueAddedService priceTransportValueAddedService;

	@Resource
	private ISystemCodeService systemCodeService; // 业务类型

	@Resource
	private SequenceService sequenceService;

	@Resource
	private IAddressService addressService;

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IDeliverService deliverService;
	
	@Resource
	private IPubAirportService airportService;
	@Resource 
	private IEwareHouseService elecWareHouseService;
	
	private static Map<String, String> linePropertyMap = Maps.newHashMap();
	
	private static Map<String, String> ladderMap = Maps.newHashMap();
	
	private Map<String, PubAirportEntity> airportMap = null;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	@Resource
	private Lock lock;
	
	static{
		linePropertyMap.put("始发仓","fromWarehouseName");
		linePropertyMap.put("始发站点","startStation");
		linePropertyMap.put("始发省份","fromProvinceName");
		linePropertyMap.put("始发城市","fromCityName");
		linePropertyMap.put("始发区","fromDistrictName");
		linePropertyMap.put("目的仓","endWarehouseName");
		linePropertyMap.put("目的站点","endStation");
		linePropertyMap.put("目的省份","toProvinceName");
		linePropertyMap.put("目的城市","toCityName");
		linePropertyMap.put("目的区","toDistrictName");
		linePropertyMap.put("产品类型", "transportTypeCode");
		linePropertyMap.put("发车周期","sendCycle");
		linePropertyMap.put("时效(天）","timeliness");
		linePropertyMap.put("重泡比","w2bubbleRatio");
		linePropertyMap.put("货值占比（%）","valueRatio");
	}
	static{
		ladderMap.put( "温度类型","temperatureTypeName");
		ladderMap.put("最低起运量（KG）", "minWeightShipment");
		ladderMap.put("最低起运价（元）", "minPriceShipment");
		ladderMap.put("起运箱数（箱）", "boxShipment");
		ladderMap.put("起运点数（个）", "pointShipment");		
	}
	
	

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceTransportTemplateEntity> page, Map<String, Object> param) {
		if (null == param) {
			param = new HashMap<String, Object>();
		}
		param.put("delFlag", "0");
		PageInfo<PriceTransportTemplateEntity> pageInfo = priceTransportTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceTransportTemplateEntity entity) {
		if (Session.isMissing()) {
			return "长时间未操作，用户已失效，请重新登录再试！";
		} else if (entity == null) {
			return "页面传递参数有误！";
		}
		//校验是否为标准报价
		PriceTransportTemplateEntity queryEntity = null;
		if (StringUtils.isEmpty(entity.getTemplateType())) {
			return MessageConstant.QUOTE_TYPE_NULL_MSG;
		}else if (StringUtils.isNotEmpty(entity.getTemplateType()) && 
				TemplateTypeEnum.STANDARD.getCode().equals(entity.getTemplateType())) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
			condition.put("templateTypeCode", entity.getTemplateTypeCode());
			queryEntity = priceTransportTemplateService.query(condition);
		}
		//新增、修改
		if (entity.getId() == null) {
			if (null != queryEntity) {
				return MessageConstant.QUOTE_STANDARD_EXIST_MSG;
			}
			String templateNo = sequenceService.getBillNoOne(PriceTransportTemplateEntity.class.getName(), "TMB", "00000");
			entity.setTemplateCode(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceTransportTemplateService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增运输其他报价模板,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
		} else {
			if (queryEntity.getId() != entity.getId()) {
				return MessageConstant.QUOTE_STANDARD_EXIST_MSG;
			}
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新运输其他报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
		}
		return null;
	}

	@DataResolver
	public String saveAll(Collection<PriceTransportTemplateEntity> datas) {
		if (datas == null) {
			return "页面传递参数有误！";
		}
		for (PriceTransportTemplateEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				return this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				priceTransportTemplateService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新运输其他报价模板,模板编码【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				priceTransportTemplateService.delete(temp.getId());
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除运输其他报价模板,模板编码【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
			} else {
				// do nothing;
			}
		}
		return null;
	}

	@DataResolver
	public String update(PriceTransportTemplateEntity entity) {
		try {
			if (entity != null) {
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新运输其他报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "SUCCESS";
		} catch (Exception ex) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("数据库操作失败:", ex);
			return "数据库操作失败";
		}
	}

	@DataResolver
	public String delete(PriceTransportTemplateEntity entity) {
		try {
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportTemplateService.update(entity);
			// 删除TransportTemplate下的运输路线
			Map<String, Object> lineParam = new HashMap<String, Object>();
			lineParam.put("templateId", entity.getId());

			List<PriceTransportLineEntity> lineList = priceTransportLineService.query(lineParam);
			if (lineList != null && lineList.size() > 0) {
				for (PriceTransportLineEntity line : lineList) {
					line.setDelFlag("1");
					line.setLastModifier(JAppContext.currentUserName());
					line.setLastModifyTime(JAppContext.currentTimestamp());

					// 找到运输路线对应的梯度报价
					Map<String, Object> rangeParam = new HashMap<String, Object>();
					rangeParam.put("lineId", line.getId());
					List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeService.query(rangeParam);
					if (rangeList != null && rangeList.size() > 0) {
						for (PriceTransportLineRangeEntity range : rangeList) {
							range.setDelFlag("1");
							range.setLastModifier(JAppContext.currentUserName());
							range.setLastModifyTime(JAppContext.currentTimestamp());
							// 删除梯度报价
							priceTransportLineRangeService.update(range);
						}
					}
					// 删除运输路线
					priceTransportLineService.update(line);
				}
			}

			// 删除TransportTemplate下的运输增值报价
			List<PriceTransportValueAddedEntity> valueAddedList = priceTransportValueAddedService.query(lineParam);
			if (valueAddedList != null && valueAddedList.size() > 0) {
				for (PriceTransportValueAddedEntity valueAdded : valueAddedList) {
					valueAdded.setDelFlag("1");
					valueAdded.setLastModifier(JAppContext.currentUserName());
					valueAdded.setLastModifyTime(JAppContext.currentTimestamp());
					priceTransportValueAddedService.update(valueAdded);
				}
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废运输其他报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "SUCCESS";
		} catch (Exception ex) {
		    logger.error("异常:", ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}
	
	/**
	 * 批量作废报价
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String cancelQuatation(PriceTransportTemplateEntity entity) {
		try {
			// 删除TransportTemplate下的运输路线
			Map<String, Object> lineParam = new HashMap<String, Object>();
			lineParam.put("templateId", entity.getId());

			List<PriceTransportLineEntity> lineList = priceTransportLineService.query(lineParam);
			if (lineList != null && lineList.size() > 0) {
				for (PriceTransportLineEntity line : lineList) {
					line.setDelFlag("1");
					line.setLastModifier(JAppContext.currentUserName());
					line.setLastModifyTime(JAppContext.currentTimestamp());

					// 找到运输路线对应的梯度报价
					Map<String, Object> rangeParam = new HashMap<String, Object>();
					rangeParam.put("lineId", line.getId());
					List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeService.query(rangeParam);
					if (rangeList != null && rangeList.size() > 0) {
						for (PriceTransportLineRangeEntity range : rangeList) {
							range.setDelFlag("1");
							range.setLastModifier(JAppContext.currentUserName());
							range.setLastModifyTime(JAppContext.currentTimestamp());
							// 删除梯度报价
							priceTransportLineRangeService.update(range);
						}
					}
					// 删除运输路线
					priceTransportLineService.update(line);
				}
			}

			// 删除TransportTemplate下的运输增值报价
			List<PriceTransportValueAddedEntity> valueAddedList = priceTransportValueAddedService.query(lineParam);
			if (valueAddedList != null && valueAddedList.size() > 0) {
				for (PriceTransportValueAddedEntity valueAdded : valueAddedList) {
					valueAdded.setDelFlag("1");
					valueAdded.setLastModifier(JAppContext.currentUserName());
					valueAdded.setLastModifyTime(JAppContext.currentTimestamp());
					priceTransportValueAddedService.update(valueAdded);
				}
			}
			return "SUCCESS";
		} catch (Exception ex) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
	}


	@DataResolver
	public String copyTransportTemplate(PriceTransportTemplateEntity entity) {
		if (Session.isMissing()) {
			return "长时间未操作，用户已失效，请重新登录再试！";
		} else if (entity == null) {
			return "页面传递参数有误！";
		}
		String oldTemplateId = String.valueOf(entity.getId());
		// String oldTemplateNo = entity.getTemplateCode();

		String newTemplateNo = sequenceService.getBillNoOne(PriceTransportTemplateEntity.class.getName(), "TMB", "00000");
		entity.setId(null);
		entity.setTemplateCode(newTemplateNo);
		if (entity != null && StringUtils.isNotBlank(entity.getTemplateName())) {
			entity.setTemplateName(String.format("%s_2", entity.getTemplateName()));
		}

		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setCreator(JAppContext.currentUserName());
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		priceTransportTemplateService.save(entity);
		String newTemplateId = String.valueOf(priceTransportTemplateService.findIdByTemplateCode(newTemplateNo));
		if (StringUtils.isNotBlank(newTemplateId) && StringUtils.isNoneBlank(oldTemplateId)) {
			// 找出之前oldTemplateId对应的路线
			Map<String, Object> lineParam = new HashMap<String, Object>();
			lineParam.put("templateId", oldTemplateId);
			lineParam.put("delFlag", "0");
			List<PriceTransportLineEntity> lineList = priceTransportLineService.query(lineParam);
			if (lineList != null && lineList.size() > 0) {
				Map<String, Object> rangeParam = new HashMap<String, Object>();
				for (PriceTransportLineEntity line : lineList) {
					String oldLineId = String.valueOf(line.getId());
					// String oldLineNo = line.getTransportLineNo();

					line.setId(null);
					line.setTemplateId(newTemplateId);// 将运输路线关联到复制后的新模板上
					String lineNo = sequenceService.getBillNoOne(PriceTransportLineEntity.class.getName(), "TL", "00000");
					line.setTransportLineNo(lineNo);
					line.setCreator(JAppContext.currentUserName());
					line.setCreateTime(JAppContext.currentTimestamp());
					line.setLastModifier(JAppContext.currentUserName());
					line.setLastModifyTime(JAppContext.currentTimestamp());
					line.setDelFlag("0");
					priceTransportLineService.save(line);

					String newLineId = String.valueOf(priceTransportLineService.findIdByLineNo(lineNo));
					if (StringUtils.isNoneBlank(oldLineId) && StringUtils.isNotBlank(newLineId)) {
						// 找到oldlineId对应的梯度报价
						rangeParam.put("lineId", oldLineId);
						lineParam.put("delFlag", "0");
						List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeService.query(rangeParam);
						if (rangeList != null && rangeList.size() > 0) {
							for (PriceTransportLineRangeEntity range : rangeList) {
								range.setId(null);
								range.setLineId(newLineId);// 将运输梯度报价关联到复制后的新路线上
								range.setCreator(JAppContext.currentUserName());
								range.setCreateTime(JAppContext.currentTimestamp());
								range.setLastModifier(JAppContext.currentUserName());
								range.setLastModifyTime(JAppContext.currentTimestamp());
								range.setDelFlag("0");
							}
						}
						priceTransportLineRangeService.saveList(rangeList);
					}
				}
				// priceTransportLineService.saveList(lineList);
			}
		}

		return null;
	}

	/**
	 * 运输路线模板下载
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTransportLineTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_line_template.xlsx");
		return new DownloadFile("运输路线导入模板.xlsx", is);
	}

	
	/**
	 * 导入干线汇总模板
	 * 
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportLineAll(final UploadFile file, final Map<String, Object> parameter) throws Exception {
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		//获取此时的商家id
		final String customerId=(String)parameter.get("customerId");
		final String customerName=(String)parameter.get("customerName");//获取当前的id	老是为空的字符串没有啥意思
		//final String customerId=JAppContext.currentUserName();
		String lockString=getMd5("BMS_QUO_IMPORT_TRANSPORT_RECEIVE"+customerId);
		
		lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {
			@Override
            public Map<String, Object> handleObtainLock() {
                // TODO Auto-generated method stub
                try {
                    Map<String, Object> re = importTransportLine(file, parameter, infoList, map);
                    return re;
                } catch (Exception e) {
                    logger.error("异常:", e);
                }
                return null;
            }

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("商家【"+customerName+"】的报价导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return map;
	}
	
	/**
	 * 导入
	 * 
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@FileResolver
	public Map<String, Object> importTransportLine(UploadFile file, Map<String, Object> parameter,List<ErrorMessageVo> infoList,Map<String, Object> map) throws Exception {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		// 获取当前的id
		String id = (String) parameter.get("id");

		ErrorMessageVo errorVo = null;
		// 校验 TODO
		List<PriceTransportLineEntity> lineList = null;
		
		try {
			lineList = getObjectByFile(file.getInputStream());
		} catch (Exception e1){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e1.toString());

			logger.error(e1.getMessage(), e1);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg(e1.getMessage());
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}

		// 解析Excel
		if (null == lineList || lineList.size() <= 0) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
		String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
		// 模板信息必填项校验
		map = impExcelCheckInfo(infoList, lineList, map, currentNo);
		
		if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			return map;
		}
		
		// 获得初步校验后和转换后的数据
		lineList = (List<PriceTransportLineEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
		
		
		//重复校验(包括Excel校验和数据库校验)
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("templateId", id);
		paramMap.put("delFlag", "0");
		List<PriceTransportLineEntity> orgList=getOrgList(paramMap);
		List<PriceTransportLineEntity> teList=lineList;
		Map<String,Object> mapCheck=super.compareWithImportLineData(orgList, teList, infoList,getKeyDataProperty(), map);
		if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
			return map;
		}	
			
		// 设置属性
		String lineNo = null;
		for (int i = 0; i < lineList.size(); i++) {
			PriceTransportLineEntity line = lineList.get(i);
			line.setTemplateId(id);
			lineNo = sequenceService.getBillNoOne(PriceTransportLineEntity.class.getName(), "TL", "00000");
			line.setTransportLineNo(lineNo);
			line.setDelFlag("0");// 设置为未作废
			line.setCreator(userName);
			line.setCreateTime(currentTime);
			line.setLastModifier(userName);
			line.setLastModifyTime(currentTime);
			if (line.getChild()!=null&&line.getChild().size()>0) {
				for (PriceTransportLineRangeEntity priceTransportLineRangeEntity : line.getChild()) {
					priceTransportLineRangeEntity.setCreator(userName);
					priceTransportLineRangeEntity.setCreateTime(currentTime);
					priceTransportLineRangeEntity.setLastModifier(userName);
					priceTransportLineRangeEntity.setLastModifyTime(currentTime);
					priceTransportLineRangeEntity.setDelFlag("0");
				}
			}
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		// 插入正式表
		int insertNum = 0;
		try {
			insertNum = priceTransportLineService.saveListWithChild(lineList);
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			if(null!=e.getMessage()&&(e.getMessage().indexOf("Duplicate entry"))>0){
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("违反唯一性约束,插入数据失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
		if (insertNum <= 0) {
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("存档运输路线表失败!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			return map;
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData("");
				model.setOldData("");
				model.setOperateDesc("导入运输其他报价,共计【"+insertNum+"】条");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.IMPORT.getCode());
				model.setRemark("");
				model.setOperateTableKey("");
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return map;
		}
	}

	/**
	 * 校验导入信息
	 * @param infoList
	 * @param lineList
	 * @param map
	 * @param currentNo
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfo(List<ErrorMessageVo> infoList, List<PriceTransportLineEntity> lineList, Map<String, Object> map, String currentNo) {
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> tempretureMap = new HashMap<String,String>();
		Map<String, String> elecWareHouseMap = new HashMap<String,String>();
		Map<String, String> carMap =new LinkedHashMap<String,String>();
		
		//温度类型
		List<SystemCodeEntity> systemCodeEntities = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		for (SystemCodeEntity systemCodeEntity : systemCodeEntities) {
			tempretureMap.put(systemCodeEntity.getCodeName(), systemCodeEntity.getCode());
		}
		
		//所有的车型
		List<SystemCodeEntity> carModelList = systemCodeService.findEnumList("MOTORCYCLE_TYPE");
		if(carModelList!=null && carModelList.size()>0){
			for(int i=0;i<carModelList.size();i++){
				carMap.put(carModelList.get(i).getCodeName(), carModelList.get(i).getCode());
			}
		}
		
		
		//获取所有的机场信息
		airportMap = new HashMap<String, PubAirportEntity>();
		//机场
		List<PubAirportEntity> airportList = airportService.query(null);
		if (null != airportList && airportList.size() > 0) {
			for (PubAirportEntity entity : airportList) {
				airportMap.put(entity.getAirportName(), entity);
			}
		}
		
		// 当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		if (lineList != null && lineList.size() > 0) {
			//电商仓库
			PageInfo<PubElecWarehouseEntity> ewareHousePageInfo = elecWareHouseService.queryAll(null, 0, Integer.MAX_VALUE);
			if (null != ewareHousePageInfo && ewareHousePageInfo.getList().size() > 0) {
				for (PubElecWarehouseEntity elecWareHouse : ewareHousePageInfo.getList()) {
					if (null != elecWareHouse) {
						elecWareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
						wareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
					}
				}
			}
			// 仓库
			List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
			if (wareHouseList != null && wareHouseList.size() > 0) {
				for (WarehouseVo wareHouse : wareHouseList) {
					wareHouseMap.put(wareHouse.getWarehousename(), wareHouse.getWarehouseid());
				}
			}
			
			// 判断市区 存不存在
			List<RegionVo> regionList = new ArrayList<RegionVo>();
			List<RegionVo> targetList = new ArrayList<RegionVo>();
			int line = 2;
			for (int i = 0; i < lineList.size(); i++) {
				// 当前行号
				line++;
				PriceTransportLineEntity lineData = lineList.get(i);	
				
				//新增根据业务类型判断必填项				
				//获取产品类型			
 				Map<String,Object> myParamMap=new HashMap<String,Object>();
				myParamMap.put("codeName", lineData.getTransportTypeCode());
				List<SystemCodeEntity> sList=systemCodeService.queryCodeList(myParamMap);
				if(StringUtils.isNotBlank(lineData.getTransportTypeCode())){
					if(sList!=null && sList.size()>0){
						//父类类型
						String superTransportTypeCode=sList.get(0).getExtattr1();
						//现属类型
						String transportTypeCode=sList.get(0).getCode();
						
						if(StringUtils.isNotBlank(transportTypeCode)){
							checkTransport(infoList,line,lineData);
						}else{
							setMessage(infoList, line, "产品类型不存在!");
						}				
						//只有同城和城际才会校验地址
						if((TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_TCZC.getCode().equals(transportTypeCode)) ||
						   TransportProductTypeEnum.CJLD.getCode().equals(superTransportTypeCode)){
							if (!StringUtils.isEmpty(lineData.getFromProvinceName())) {
								// 起始地址（校验省市区）
								RegionVo regionVo = new RegionVo();
								regionVo.setProvince(lineData.getFromProvinceName());
								regionVo.setCity(lineData.getFromCityName());
								regionVo.setDistrict(lineData.getFromDistrictName());
								regionVo.setLineNo(line);
								regionList.add(regionVo);
							}
							
							if (!StringUtils.isEmpty(lineData.getToProvinceName())) {
								// 目的地址
								RegionVo targetVo = new RegionVo();
								targetVo.setProvince(lineData.getToProvinceName());
								targetVo.setCity(lineData.getToCityName());
								targetVo.setDistrict(lineData.getToDistrictName());
								targetVo.setLineNo(line);
								targetList.add(targetVo);
							}
						}
					}else{
						setMessage(infoList, line, "产品类型没有在数据字典中维护!");
					}
				}else{
					setMessage(infoList, line, "产品类型为空!");
				}
				
			}
			// ===================================起始地址
			// 校验======================================================================
			if(regionList!=null && regionList.size()>0 && infoList.size() <= 0){
				RegionEncapVo regionEncapVo=addressService.matchStandardByAlias(regionList);			// 获取此时的正确信息和报错信息
				List<ErrorMsgVo> errorMsgList = regionEncapVo.getErrorMsgList();
				List<RegionVo> correctRegionList = regionEncapVo.getRegionList();

				// 如匹配到到名称则将code设值
				if (errorMsgList.size() <= 0) {
					for (PriceTransportLineEntity linePrice : lineList) {
						if (StringUtils.isNotBlank(linePrice.getFromCityName())) {
							for (RegionVo region : correctRegionList) {
								if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(linePrice.getFromProvinceName()) && region.getProvince().equals(linePrice.getFromProvinceName())){
									linePrice.setFromProvinceId(region.getProvincecode());
									if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(linePrice.getFromCityName()) && region.getCity().equals(linePrice.getFromCityName())){
										linePrice.setFromCityId(region.getCitycode());
										if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(linePrice.getFromDistrictName()) && region.getDistrict().equals(linePrice.getFromDistrictName())){
											linePrice.setFromDistrictId(region.getDistrictcode());		
										}
									}
								}
							}
						}
					}
				} else {
					for (ErrorMsgVo error : errorMsgList) {
						setMessage(infoList, error.getLineNo(), error.getErrorMsg());
					}
				}
			}
			
			// ===================================目的地址
			// 校验======================================================================
			if(targetList!=null && targetList.size()>0 && infoList.size() <=0){
				RegionEncapVo targetEncapVo=addressService.matchStandardByAlias(targetList);

				// 获取此时的正确信息和报错信息
				List<ErrorMsgVo> errorTargetList = targetEncapVo.getErrorMsgList();
				List<RegionVo> correctTargetList = targetEncapVo.getRegionList();

				// 如匹配到到名称则将code设值
				if (errorTargetList.size() <= 0) {
					for (PriceTransportLineEntity linePrice : lineList) {
						if (StringUtils.isNotBlank(linePrice.getToProvinceName())) {
							for (RegionVo region : correctTargetList) {
								if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(linePrice.getToProvinceName()) && region.getProvince().equals(linePrice.getToProvinceName())){
									linePrice.setToProvinceId(region.getProvincecode());
									if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(linePrice.getToCityName()) && region.getCity().equals(linePrice.getToCityName())){
										linePrice.setToCityId(region.getCitycode());
										if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(linePrice.getToDistrictName()) && region.getDistrict().equals(linePrice.getToDistrictName())){
											linePrice.setToDistrictId(region.getDistrictcode());		
										}
									}
								}
							}
						}
					}
				} else {
					for (ErrorMsgVo error : errorTargetList) {
						setMessage(infoList, error.getLineNo(), error.getErrorMsg());
					}
				}
			}	
			// ==============================================================================================================
		}
		// ======================================================================================================================
		int lineNo = 2;
		for (int i = 0; i < lineList.size(); i++) {
			PriceTransportLineEntity line = lineList.get(i);
			lineNo = lineNo + 1;
			
			//新增根据业务类型判断必填项
			//获取产品类型			
			Map<String,Object> myParamMap=new HashMap<String,Object>();
			myParamMap.put("code", line.getTransportTypeCode());
			List<SystemCodeEntity> sList=systemCodeService.queryCodeList(myParamMap);
			//父类类型
			String superTransportTypeCode="";
			//现属类型
			String transportTypeCode="";		
			if(sList!=null && sList.size()>0){
				superTransportTypeCode=sList.get(0).getExtattr1();
				transportTypeCode=sList.get(0).getCode();
			}	
			
			// 判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			//验证始发仓是否在系统中维护
			String warehouseName = line.getFromWarehouseName();
			if (StringUtils.isNotBlank(warehouseName)) {
				//如果导入的始发仓就是九曳仓库，则取出九曳仓库ID;
				//如果始发仓不是九曳仓库（可能情况 1-确实未维护 2-九曳电商的仓库）
				//如果产品类型是非九曳电商 并且始发未在九曳仓维护，返回错误；
				//如果产品类型是九曳电商， 并且始发未在九曳仓维护.继续判断是否为电商仓库，如果是电商仓库，取出电商仓库id；如果在电商仓库中未维护，返回错误
				if (wareHouseMap.containsKey(warehouseName) && StringUtils.isNoneBlank(wareHouseMap.get(warehouseName))) {
					logger.info(String.format("从仓库缓存Map中取数据[%s]", warehouseName));
					// 从缓存wareHouseMap中直接取
					line.setFromWarehouseId(wareHouseMap.get(warehouseName));
				} else {
					//如果产品类型是电商专列,始发仓可能是电商仓，也可能是九曳仓
					if(TransportProductTypeEnum.TCZC_DSZL.getCode().equals(transportTypeCode)){
						if(elecWareHouseMap.containsKey(warehouseName)){//如果始发仓市电商仓中的仓库
							line.setFromWarehouseId(elecWareHouseMap.get(warehouseName));
						}
						else{
							setMessage(infoList, lineNo,"始发站点["+warehouseName+"]没有在电商仓库和九曳仓库中维护!");
						}
					}
					else{
						setMessage(infoList, lineNo, "始发仓【"+warehouseName+"】没有在仓库表中维护!");
					}
					
				}
			}
			//验证目的仓是否在系统中维护
			String endWHName = line.getEndWarehouseName();
			if (StringUtils.isNotBlank(endWHName)) {
				if (wareHouseMap.containsKey(endWHName) && StringUtils.isNoneBlank(wareHouseMap.get(endWHName))) {
					logger.info(String.format("从仓库缓存Map中取数据[%s]", warehouseName));
					// 从缓存wareHouseMap中直接取
					line.setEndWarehouseId(wareHouseMap.get(endWHName));
				} else {
					setMessage(infoList, lineNo, "目的仓【"+endWHName+"】没有在仓库表中维护!");
				}
			}
			//电商仓库 (目的站点,车型)
			String elecWarehouseName = line.getEndStation();
			if((TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_DSZL.getCode().equals(transportTypeCode))){
				if(isNullInfo(elecWarehouseName)){
					setMessage(infoList, lineNo, TransportMessageConstant.ENDSTATION_NULL_MSG);
				}else {
					if(!elecWareHouseMap.containsKey(elecWarehouseName) || isNullInfo(elecWareHouseMap.get(elecWarehouseName))){
						if(!wareHouseMap.containsKey(elecWarehouseName)){ //如果电商仓未维护，并且九曳仓库中也找不到
							setMessage(infoList, lineNo,"目的站点["+elecWarehouseName+"]没有在电商仓库和九曳仓库中维护!");
						}
					}
				}
			}
			
		/*	if (line != null && line.getW2bubbleRatio() == null) {
				line.setW2bubbleRatio(0.3333333d);// 默认1:3
			}*/
			if (line.getChild()!=null&&line.getChild().size()>0) {
				for (PriceTransportLineRangeEntity priceTransportLineRangeEntity : line.getChild()) {
					//判断温度的
					if (StringUtils.isNoneBlank(priceTransportLineRangeEntity.getTemperatureTypeName())) {
						if (tempretureMap.containsKey(priceTransportLineRangeEntity.getTemperatureTypeName())) {
							priceTransportLineRangeEntity.setTemperatureTypeCode(tempretureMap.get(priceTransportLineRangeEntity.getTemperatureTypeName()));
						}else {
							setMessage(infoList, lineNo, "温度类型：{"+priceTransportLineRangeEntity.getTemperatureTypeName()+"}没有在数据字典中维护!");
						}
					}
					//判断车型
					String carModelName=priceTransportLineRangeEntity.getCarModelCode();
					if (StringUtils.isNoneBlank(carModelName)) {
						if (carMap.containsKey(carModelName)) {
							priceTransportLineRangeEntity.setCarModelCode(carMap.get(carModelName));
						}else {
							setMessage(infoList, lineNo, "车型：{"+carModelName+"}没有在数据字典中维护!");
						}
					}
				}
			}
			// ==============================================================================================================
			DecimalFormat decimalFormat = new DecimalFormat("0");
			double addNum = ((double) (i + 1) / lineList.size()) * 400;
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300 + Integer.valueOf(decimalFormat.format(addNum)));
		}
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, lineList); // 无基本错误信息
		}
		return map;
	}


	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 *//*
	private List<PriceTransportLineEntity> readExcelProduct(UploadFile file, BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {

					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<PriceTransportLineEntity> lineList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceTransportLineEntity p = (PriceTransportLineEntity) BeanToMapUtil.convertMapNull(PriceTransportLineEntity.class, data);
				lineList.add(p);
			}
			return lineList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
*/
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo, String msg) {
		ErrorMessageVo errorVo;
		errorVo = new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}

	// ------------------------------------------------------------------------
	/**
	 * 复制运输报价和增值服务报价
	 * 
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String copyTransportTemplateValueAdded(PriceTransportTemplateEntity entity) {
		if (Session.isMissing()) {
			return "长时间未操作，用户已失效，请重新登录再试！";
		} else if (entity == null) {
			return "页面传递参数有误！";
		}
		String oldTemplateId = String.valueOf(entity.getId());
		// String oldTemplateNo = entity.getTemplateCode();

		String newTemplateNo = sequenceService.getBillNoOne(PriceTransportTemplateEntity.class.getName(), "TMB", "00000");
		entity.setId(null);
		entity.setTemplateCode(newTemplateNo);
		if (entity != null && StringUtils.isNotBlank(entity.getTemplateName())) {
			entity.setTemplateName(String.format("%s_2", entity.getTemplateName()));
		}

		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setCreator(JAppContext.currentUserName());
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		priceTransportTemplateService.save(entity);
		String newTemplateId = String.valueOf(priceTransportTemplateService.findIdByTemplateCode(newTemplateNo));
		if (StringUtils.isNotBlank(newTemplateId) && StringUtils.isNoneBlank(oldTemplateId)) {
			// 找出之前oldTemplateId对应的路线
			Map<String, Object> valueAddedParam = new HashMap<String, Object>();
			valueAddedParam.put("templateId", oldTemplateId);
			valueAddedParam.put("delFlag", "0");
			List<PriceTransportValueAddedEntity> valueAddedList = priceTransportValueAddedService.query(valueAddedParam);
			if (valueAddedList != null && valueAddedList.size() > 0) {
				for (PriceTransportValueAddedEntity valueAdded : valueAddedList) {
					valueAdded.setId(null);
					valueAdded.setTemplateId(newTemplateId);// 将增值服务报价关联到复制后的新模板上
					String quotationNo = sequenceService.getBillNoOne(PriceTransportValueAddedEntity.class.getName(), "VA", "00000");
					valueAdded.setQuotationNo(quotationNo);
					valueAdded.setCreator(JAppContext.currentUserName());
					valueAdded.setCreateTime(JAppContext.currentTimestamp());
					valueAdded.setLastModifier(JAppContext.currentUserName());
					valueAdded.setLastModifyTime(JAppContext.currentTimestamp());
					valueAdded.setDelFlag("0");
				}
				priceTransportValueAddedService.saveList(valueAddedList);
			}
		}

		return null;
	}

	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int) (DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag"));
	}

	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);

		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		}
	}

	public boolean containDistinctName(String distinctNameParam, Set<String> distinctNameList) {
		if (distinctNameList != null && distinctNameList.size() > 0) {
			for (String distinctName : distinctNameList) {
				if (StringUtils.equalsIgnoreCase(distinctNameParam, distinctName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 解析文件流得到对象
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private List<PriceTransportLineEntity> getObjectByFile(InputStream inputStream) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 读取表头信息
		Row row = null;
		Map<Integer, String> lineProperties = Maps.newHashMap();
		Map<Integer, String> ladderProperties = Maps.newHashMap();
		
		List<Integer> weightColumn = Lists.newArrayList();
		List<Integer> volumeColumn = Lists.newArrayList();
		List<Integer> carColumn = Lists.newArrayList();
		List<Integer> boxColumn = Lists.newArrayList();
		List<Integer> pointColumn = Lists.newArrayList();
		//第一列表头
		for (int i = 0; i < 1; i++) {
			row = sheet.getRow(i);
			int cellIndex =0;
			for (Cell c : row) {
				String returnStr = getCellValueIfMerge(sheet,row,i,c);
				if ("重量阶梯（元/KG）".equals(returnStr)) {
					weightColumn.add(cellIndex);
				} else if ("体积阶梯（元/方）".equals(returnStr)) {
					volumeColumn.add(cellIndex);
				} else if ("整车报价".equals(returnStr)) {
					carColumn.add(cellIndex);
				} else if ("箱数阶梯（元/箱）".equals(returnStr)) {
					boxColumn.add(cellIndex);
				} else if ("点数阶梯（元/个）".equals(returnStr)) {
					pointColumn.add(cellIndex);
				}else if (linePropertyMap.containsKey(returnStr)) {
					lineProperties.put(cellIndex, linePropertyMap.get(returnStr));
				} else if (ladderMap.containsKey(returnStr)) {
					ladderProperties.put(cellIndex, ladderMap.get(returnStr));
				}
				cellIndex++;
			}
		}
		//第二列表头获取阶梯明细
		Map<Integer, PriceTransportLineRangeEntity> columnRanges = Maps.newHashMap();
		PriceTransportLineRangeEntity range = null;
		for (int i = 1; i < 2; i++) {
			row = sheet.getRow(i);
			Cell c = null;
			if (weightColumn.size()>0) {
				for (int column : weightColumn) {
					c = row.getCell(column);
					String value = getCellValueIfMerge(sheet, row, i, c);
					range = new PriceTransportLineRangeEntity();
					if(!value.contains("~")){
						throw new  RuntimeException("表头格式不对,第"+(column+1)+"列缺~");
					}
					String[] weights = value.split("~");
					String weight0 = weights[0];
					String weight1 = weights[1];
					range.setWeightLowerLimit(StringUtils.isEmpty(weight0)?null:Double.valueOf(weight0));
					range.setWeightUpperLimit(StringUtils.isEmpty(weight1)?null:Double.valueOf(weight1));
					columnRanges.put(column,range);
				}
			}
			if (volumeColumn.size()>0) {
				for (int column : volumeColumn) {
					c = row.getCell(column);
					String value = getCellValueIfMerge(sheet, row, i, c);
					range = new PriceTransportLineRangeEntity();
					if(!value.contains("~")){
						throw new  RuntimeException("表头格式不对,第"+(column+1)+"列缺~");
					}
					String[] volumes = value.split("~");
					String volume0 = volumes[0];
					String volume1 = volumes[1];
					range.setVolumeLowerLimit(StringUtils.isEmpty(volume0)?null:Double.valueOf(volume0));
					range.setVolumeUpperLimit(StringUtils.isEmpty(volume1)?null:Double.valueOf(volume1));
					columnRanges.put(column,range);
				}
			}
			if (carColumn.size()>0) {
				for (int column : carColumn) {
					c = row.getCell(column);
					String value = getCellValueIfMerge(sheet, row, i, c);
					range = new PriceTransportLineRangeEntity();
					range.setCarModelCode(value);
					columnRanges.put(column,range);
				}
			}
			if (boxColumn.size()>0) {
				for (int column : boxColumn) {
					c = row.getCell(column);
					String value = getCellValueIfMerge(sheet, row, i, c);
					range = new PriceTransportLineRangeEntity();
					if(!value.contains("~")){
						throw new  RuntimeException("表头格式不对,第"+(column+1)+"列缺~");
					}
					String[] boxs = value.split("~");
					String box0 = boxs[0];
					String box1 = boxs[1];
					range.setBoxLowerLimit(StringUtils.isEmpty(box0)?null:Double.valueOf(box0));
					range.setBoxUpperLimit(StringUtils.isEmpty(box1)?null:Double.valueOf(box1));
					columnRanges.put(column,range);
				}
			}
			if (pointColumn.size()>0) {
				for (int column : pointColumn) {
					c = row.getCell(column);
					String value = getCellValueIfMerge(sheet, row, i, c);
					range = new PriceTransportLineRangeEntity();
					if(!value.contains("~")){
						throw new  RuntimeException("表头格式不对,第"+(column+1)+"列缺~");
					}
					String[] points = value.split("~");
					String point0 = points[0];
					String point1 = points[1];
					range.setPointLowerLimit(StringUtils.isEmpty(point0)?null:Double.valueOf(point0));
					range.setPointUpperLimit(StringUtils.isEmpty(point1)?null:Double.valueOf(point1));
					columnRanges.put(column,range);
				}
			}
		}
		
		int rows = sheet.getLastRowNum();
		List<PriceTransportLineEntity> list = Lists.newArrayList();
		PriceTransportLineEntity line = null;
		for (int i = 2; i <= rows; i++) {			
			row = sheet.getRow(i);
			
			//如遇空行，直接跳过
			if(row==null){
				continue;
			}
			//再加判断如果值为空，则也直接跳过
			String re="";
			for(int col = row.getFirstCellNum() ; col < row.getLastCellNum() ; col++ ){
				 XSSFCell cell = (XSSFCell) row.getCell(col);					
				 re+= (cell == null?"":cell.toString());
			}
			if(StringUtils.isBlank(re)){
				continue;
			}
			
			line = new PriceTransportLineEntity();
			
			PriceTransportLineRangeEntity originRange = null;
			
			List<PriceTransportLineRangeEntity> ranges = Lists.newArrayList();
			
			originRange = new PriceTransportLineRangeEntity();

			short lastCellNum = row.getLastCellNum();
			for(int col = row.getFirstCellNum() ; col < lastCellNum ; col++ ){
				 XSSFCell cell = (XSSFCell) row.getCell(col);
				 if(cell == null ){
                     continue;
                 }			
				 String value = cell == null?null:cell.toString();
				 if (lineProperties.containsKey(col)) {
						String fieldName = StringUtils.capitalize(lineProperties.get(col));		
						Method method = PriceTransportLineEntity.class.getMethod("set"+fieldName, String.class);
						method.invoke(line, value);						
					}
				 else if (ladderProperties.containsKey(col)) {
						if("temperatureTypeName".equals(ladderProperties.get(col))){
							originRange.setTemperatureTypeName(value);
						}else if ("minWeightShipment".equals(ladderProperties.get(col))) {							
							originRange.setMinWeightShipment(StringUtil.isNumeric(value)?Double.valueOf(value):null);
						}else if("minPriceShipment".equals(ladderProperties.get(col))){
							originRange.setMinPriceShipment(StringUtil.isNumeric(value)?Double.valueOf(value):null);
						}else if("boxShipment".equals(ladderProperties.get(col))){
							originRange.setBoxShipment(StringUtil.isEmpty(value)?null:Double.valueOf(value));
						}else if("pointShipment".equals(ladderProperties.get(col))){
							originRange.setPointShipment(StringUtil.isEmpty(value)?null:Double.valueOf(value));
						}
					}
				 else if (columnRanges.containsKey(col)) {
						if (StringUtils.isNotBlank(value)) {
							PriceTransportLineRangeEntity columnRange0 = columnRanges.get(col);
							
							PriceTransportLineRangeEntity columnRange = new PriceTransportLineRangeEntity();
							BeanUtils.copyProperties(columnRange0, columnRange);
							
							columnRange.setUnitPrice(StringUtil.isEmpty(value)?null:Double.valueOf(value));
							if (originRange!=null) {
								columnRange.setTemperatureTypeName(originRange.getTemperatureTypeName());
								columnRange.setMinWeightShipment(originRange.getMinWeightShipment());
								columnRange.setMinPriceShipment(originRange.getMinPriceShipment());
								columnRange.setBoxShipment(originRange.getBoxShipment());
								columnRange.setPointShipment(originRange.getPointShipment());
							}
							ranges.add(columnRange);
						}
					}
				 
			}
			line.setChild(ranges);
			if(line!=null){
				list.add(line);
			}
			
		}
		return list;
	}

	// i 行编号
	private String getCellValueIfMerge(Sheet sheet,Row row, int i,Cell c) {
		boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
		// 判断是否具有合并单元格
		if (isMerge) {
			String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
			return rs;
		} else {
			return getCellValue(c);
		}
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public String getMergedRegionValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {

				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell);
				}
			}
		}

		return null;
	}

	/**
	 * 判断合并了行
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 *//*
	private boolean isMergedRow(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row == firstRow && row == lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}*/

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	private boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

/*	*//**
	 * 判断sheet页中是否含有合并单元格
	 * 
	 * @param sheet
	 * @return
	 *//*
	private boolean hasMerged(Sheet sheet) {
		return sheet.getNumMergedRegions() > 0 ? true : false;
	}*/

	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param lastCol
	 *            结束列
	 */
	/*private void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}
*/
	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {

		if (cell == null)
			return "";

		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

			return cell.getStringCellValue();

		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

			return String.valueOf(cell.getBooleanCellValue());

		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

			return cell.getCellFormula();

		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("#.####");
			return String.valueOf(df.format(cell.getNumericCellValue()));

		}
		return "";
	}
	
	
	private void checkTransport(List<ErrorMessageVo> infoList,int lineNo,PriceTransportLineEntity line){
		//获取产品类型			
		Map<String,Object> myParamMap=new HashMap<String,Object>();
		myParamMap.put("codeName", line.getTransportTypeCode());
		List<SystemCodeEntity> sList=systemCodeService.queryCodeList(myParamMap);
		if(sList!=null && sList.size()>0){
			//父类类型
			String superTransportTypeCode=sList.get(0).getExtattr1();
			//现属类型
			String transportTypeCode=sList.get(0).getCode();

			if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_TCZC.getCode().equals(transportTypeCode)) {
				//同城
				//始发省份、始发城市、始发区 、目的省份、目的城市、目的区 、 车型
				if (isNullInfo(line.getFromProvinceName())){
					setMessage(infoList, lineNo, "同城"+TransportMessageConstant.FROMPROVINCE_NULL_MSG);
				}
				if (isNullInfo(line.getFromCityName())){
					setMessage(infoList, lineNo, "同城"+TransportMessageConstant.FROMCITY_NULL_MSG);
				}
				/*if (isNullInfo(line.getFromDistrictId())) {
					setMessage(infoList, lineNo, "同城"+TransportMessageConstant.FROMDISTRICT_NULL_MSG);
				}*/
				if (isNullInfo(line.getToProvinceName())) {
					setMessage(infoList, lineNo, "同城"+TransportMessageConstant.TOPROVINCE_NULL_MSG);
				}
				if (isNullInfo(line.getToCityName())) {
					setMessage(infoList, lineNo, "同城"+TransportMessageConstant.TOCITY_NULL_MSG);
				}
				/*if (isNullInfo(line.getToDistrictId())) {
					setMessage(infoList, lineNo, "同城"+TransportMessageConstant.TODISTRICT_NULL_MSG);
				}	*/
				
				line.setTransportTypeCode(transportTypeCode);
				
			}else if (TransportProductTypeEnum.CJLD.getCode().equals(superTransportTypeCode)) {
				//城际
				//始发省份、始发城市、 目的省份、 目的城市、是否泡货、重量/体积
				if (isNullInfo(line.getFromProvinceName())){
					setMessage(infoList, lineNo, "城际"+TransportMessageConstant.FROMPROVINCE_NULL_MSG);
				}
				if (isNullInfo(line.getFromCityName())){
					setMessage(infoList, lineNo, "城际"+TransportMessageConstant.FROMCITY_NULL_MSG);
				}
				if (isNullInfo(line.getToProvinceName())) {
					setMessage(infoList, lineNo, "城际"+TransportMessageConstant.TOPROVINCE_NULL_MSG);
				}
				if (isNullInfo(line.getToCityName())) {
					setMessage(infoList, lineNo, "城际"+TransportMessageConstant.TOCITY_NULL_MSG);
				}
						
				line.setTransportTypeCode(transportTypeCode);
			}else if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_DSZL.getCode().equals(transportTypeCode) ) {
				//电商专列
				//始发仓 、 电商仓(目的站点)、 车型
				if(isNullInfo(line.getFromWarehouseName())){
					setMessage(infoList, lineNo, "电商专列"+TransportMessageConstant.FROMWAREHOUSE_NULL_MSG);
				}
				//电商仓（目的站点）
				if(isNullInfo(line.getEndStation())){
					setMessage(infoList, lineNo, "电商专列"+TransportMessageConstant.ENDSTATION_NULL_MSG);
				}
				line.setTransportTypeCode(transportTypeCode);
			}else if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_HXD.getCode().equals(transportTypeCode)) {
				//航鲜达		
				//机场（开始站点）、  目的省份、目的城市、 重量
				if (isNullInfo(line.getStartStation())) {
					setMessage(infoList, lineNo, "航鲜达"+TransportMessageConstant.STARTSTATION_NULL_MSG);
				}else{
					if(!airportMap.containsKey(line.getStartStation()) || null == airportMap.get(line.getStartStation())){
						setMessage(infoList, lineNo,"始发站点["+line.getStartStation()+"]没有在机场表中维护!");
					}else {
						PubAirportEntity airportEntity = airportMap.get(line.getStartStation());
						line.setToProvinceId(airportEntity.getProvince());
						line.setToCityId(airportEntity.getCity());
					}
				}
				/*if (isNullInfo(line.getToProvinceId())) {
					setMessage(infoList, lineNo, "航鲜达"+TransportMessageConstant.TOPROVINCE_NULL_MSG);
				}*/
				/*if (isNullInfo(line.getToCityId())) {
					setMessage(infoList, lineNo, "航鲜达"+TransportMessageConstant.TOCITY_NULL_MSG);

				}*/
				line.setTransportTypeCode(transportTypeCode);
			}else{
				setMessage(infoList, lineNo, "产品类型"+transportTypeCode+"没有维护");
			}
		}else{
			setMessage(infoList, lineNo, "产品类型没有维护");
		}	
	}
	
	/**
	 * 判断是否为空
	 * @param info
	 * @return
	 */
	private boolean isNullInfo(String info){
		if (StringUtils.isEmpty(info)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 新增map(始发仓加电商仓)
	 */
	@DataProvider
	public Map<String, String> getPubWareHouse(){
		Map<String, String> map = new LinkedHashMap<String,String>();
		//电商仓库
		PageInfo<PubElecWarehouseEntity> ewareHousePageInfo = elecWareHouseService.queryAll(null, 0, Integer.MAX_VALUE);
		if (null != ewareHousePageInfo && ewareHousePageInfo.getList().size() > 0) {
			for (PubElecWarehouseEntity elecWareHouse : ewareHousePageInfo.getList()) {
				if (null != elecWareHouse) {
					map.put(elecWareHouse.getWarehouseCode(), elecWareHouse.getWarehouseName());
				}
			}
		}
		// 仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if (wareHouseList != null && wareHouseList.size() > 0) {
			for (WarehouseVo wareHouse : wareHouseList) {
				map.put(wareHouse.getWarehouseid(), wareHouse.getWarehousename());
			}
		}
		return map;
	}
	
	@DataResolver
	public String deleteBatch(PriceTransportTemplateEntity entity) {
		
		int i = priceTransportLineService.deleteBatch(entity.getId());
		
		if(i>0){
			return "SUCCESS";
		}else{
			return "false";
		}
		
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
	 *//*
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceTransportLineEntity> lineList, Map<String, Object> map, String currentNo) {
		// ======================================================================================================================
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> timelessMap = new HashMap<String, String>();
		Map<String, String> deliverMap = new HashMap<String, String>();
		// 当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		if (lineList != null && lineList.size() > 0) {
			// 仓库
			List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
			if (wareHouseList != null && wareHouseList.size() > 0) {
				for (WarehouseVo wareHouse : wareHouseList) {
					wareHouseMap.put(wareHouse.getWarehousename(), wareHouse.getWarehouseid());
				}
			}
			// 承运商
			List<DeliverVo> deliverList = deliverService.queryAllDeliver();
			if (deliverList != null && deliverList.size() > 0) {
				for (DeliverVo deliver : deliverList) {
					deliverMap.put(deliver.getDelivername(), deliver.getDeliverid());
				}
			}
			// 时效信息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("typeCode", "DISPATCH_TIME");
			List<SystemCodeEntity> timelessList = systemCodeService.queryCodeList(param);
			if (timelessList != null && timelessList.size() > 0) {
				for (SystemCodeEntity scEntity : timelessList) {
					timelessMap.put(scEntity.getCodeName(), scEntity.getCode());
				}
			}

			// 判断省市区 存不存在
			List<RegionVo> regionList = new ArrayList<RegionVo>();
			List<RegionVo> targetList = new ArrayList<RegionVo>();
			int line = 1;
			for (int i = 0; i < lineList.size(); i++) {

				// 当前行号
				line = line + 1;
				PriceTransportLineEntity lineData = lineList.get(i);
				// ==============================================================================================================
				// 判断地址是否为空
				if (StringUtils.isBlank(lineData.getFromProvinceName()) || StringUtils.isBlank(lineData.getToProvinceName())) {
					setMessage(infoList, line, "始发省份或者目的省份不能为空!");
				}
				if (StringUtils.isBlank(lineData.getFromCityName()) || StringUtils.isBlank(lineData.getToCityName())) {
					setMessage(infoList, line, "始发城市或者目的城市不能为空!");
				}
				if (StringUtils.isBlank(lineData.getFromDistrictName()) || StringUtils.isBlank(lineData.getToDistrictName())) {
					setMessage(infoList, line, "始发地区或者目的地区不能为空!");
				}
				// ===================================始发校验======================================================================
				// 起始地址
				RegionVo regionVo = new RegionVo();
				regionVo.setProvince(lineData.getFromProvinceName());
				regionVo.setCity(lineData.getFromCityName());
				regionVo.setDistrict(lineData.getFromDistrictName());
				regionVo.setLineNo(line);
				regionList.add(regionVo);
				// 目的地址
				RegionVo targetVo = new RegionVo();
				targetVo.setProvince(lineData.getToProvinceName());
				targetVo.setCity(lineData.getToCityName());
				targetVo.setDistrict(lineData.getToDistrictName());
				targetVo.setLineNo(line);
				targetList.add(targetVo);
			}
			// ===================================起始地址
			// 校验======================================================================
			RegionEncapVo regionEncapVo = addressService.matchStandardByAlias(regionList);
			// 获取此时的正确信息和报错信息
			List<ErrorMsgVo> errorMsgList = regionEncapVo.getErrorMsgList();
			List<RegionVo> correctRegionList = regionEncapVo.getRegionList();

			// 如匹配到到名称则将code设值
			if (errorMsgList.size() <= 0) {
				for (PriceTransportLineEntity linePrice : lineList) {
					for (RegionVo region : correctRegionList) {
						if (StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(linePrice.getFromProvinceName())
								&& region.getProvince().equalsIgnoreCase(linePrice.getFromProvinceName())) {
							linePrice.setFromProvinceId(region.getProvincecode());
							if (StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(linePrice.getFromCityName()) && region.getCity().equalsIgnoreCase(linePrice.getFromCityName())) {
								linePrice.setFromCityId(region.getCitycode());
								if (StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(linePrice.getFromDistrictName())
										&& region.getDistrict().equalsIgnoreCase(linePrice.getFromDistrictName())) {
									linePrice.setFromDistrictId(region.getDistrictcode());
									break;
								}
							}
						}
					}
				}
			} else {
				for (ErrorMsgVo error : errorMsgList) {
					setMessage(infoList, error.getLineNo(), error.getErrorMsg());
				}

			}
			// ===================================目的地址
			// 校验======================================================================
			RegionEncapVo targetEncapVo = addressService.matchStandardByAlias(targetList);
			// 获取此时的正确信息和报错信息
			List<ErrorMsgVo> errorTargetList = targetEncapVo.getErrorMsgList();
			List<RegionVo> correctTargetList = targetEncapVo.getRegionList();

			// 如匹配到到名称则将code设值
			if (errorTargetList.size() <= 0) {
				for (PriceTransportLineEntity linePrice : lineList) {
					for (RegionVo region : correctTargetList) {
						if (StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(linePrice.getToProvinceName())
								&& region.getProvince().equalsIgnoreCase(linePrice.getToProvinceName())) {
							linePrice.setToProvinceId(region.getProvincecode());
							if (StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(linePrice.getToCityName()) && region.getCity().equalsIgnoreCase(linePrice.getToCityName())) {
								linePrice.setToCityId(region.getCitycode());
								if (StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(linePrice.getToDistrictName())
										&& region.getDistrict().equalsIgnoreCase(linePrice.getToDistrictName())) {
									linePrice.setToDistrictId(region.getDistrictcode());
									break;
								}
							}
						}
					}
				}
			} else {
				for (ErrorMsgVo error : errorTargetList) {
					setMessage(infoList, error.getLineNo(), error.getErrorMsg());
				}

			}
			// ==============================================================================================================
		}
		// ======================================================================================================================
		int lineNo = 0;
		for (int i = 0; i < lineList.size(); i++) {
			PriceTransportLineEntity line = lineList.get(i);
			lineNo = lineNo + 1;

			String carrierName = line.getCarrierName();
			if (StringUtils.isNotBlank(carrierName)) {
				if (deliverMap.containsKey(carrierName) && StringUtils.isNotBlank(deliverMap.get(carrierName))) {
					logger.info(String.format("从承运商缓存Map中取数据[%s]", carrierName));
					// 从缓存wareHouseMap中直接取
					line.setCarrierCode(deliverMap.get(carrierName));
				} else {
					setMessage(infoList, lineNo, "承运商名称没有在承运商表中维护!");
				}
			}

			// 判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			String warehouseName = line.getFromWarehouseName();
			if (StringUtils.isNotBlank(warehouseName)) {
				if (wareHouseMap.containsKey(warehouseName) && StringUtils.isNoneBlank(wareHouseMap.get(warehouseName))) {
					logger.info(String.format("从仓库缓存Map中取数据[%s]", warehouseName));
					// 从缓存wareHouseMap中直接取
					line.setFromWarehouseId(wareHouseMap.get(warehouseName));
				} else {
					WarehouseVo warehouse = warehouseService.queryWarehouseByWarehouseName(warehouseName);
					if (warehouse == null) {
						setMessage(infoList, lineNo, "始发仓名称没有在仓库表中维护!");
					} else if (warehouse.getWarehouseid() != null) {
						line.setFromWarehouseId(warehouse.getWarehouseid());
						// 放入缓存wareHouseMap
						wareHouseMap.put(warehouse.getWarehousename(), warehouse.getWarehouseid());
					}
				}
			}
			String endWHName = line.getEndWarehouseName();
			if (StringUtils.isNotBlank(endWHName)) {
				if (wareHouseMap.containsKey(endWHName) && StringUtils.isNoneBlank(wareHouseMap.get(endWHName))) {
					logger.info(String.format("从仓库缓存Map中取数据[%s]", warehouseName));
					// 从缓存wareHouseMap中直接取
					line.setEndWarehouseId(wareHouseMap.get(endWHName));
				} else {
					WarehouseVo endWH = warehouseService.queryWarehouseByWarehouseName(warehouseName);
					if (endWH == null) {
						setMessage(infoList, lineNo, "目的仓名称没有在仓库表中维护!");
					} else if (endWH.getWarehouseid() != null) {
						line.setEndWarehouseId(endWH.getWarehouseid());
						// 放入缓存wareHouseMap
						wareHouseMap.put(endWH.getWarehousename(), endWH.getWarehouseid());
					}
				}
			}

			// 判断时效信息是否在时效表中维护
			String time = line.getTimeliness();
			if (StringUtils.isNotBlank(time)) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("typeCode", "DISPATCH_TIME");
				param.put("codeName", time);
				List<SystemCodeEntity> timelessList = systemCodeService.queryCodeList(param);
				if (timelessList == null || timelessList.size() == 0) {
					setMessage(infoList, lineNo, "时效没有在表中维护!");
				} else {
					line.setTimeliness(timelessList.get(0).getCode());
				}
			}
			if (line != null && line.getW2bubbleRatio() == null) {
				line.setW2bubbleRatio(0.3333333d);// 默认1:3
			}
			// ==============================================================================================================

			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, lineList); // 无基本错误信息
			}

			DecimalFormat decimalFormat = new DecimalFormat("0");
			double addNum = ((double) (i + 1) / lineList.size()) * 400;
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300 + Integer.valueOf(decimalFormat.format(addNum)));
		}
		return map;
	}*/
	
	/**
	 * 导入运输路线模板
	 * 
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("unchecked")
	@FileResolver
	public Map<String, Object> importTransportLine(UploadFile file, Map<String, Object> parameter) throws Exception {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 获取当前的id
		String id = (String) parameter.get("id");

		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;

		// 导入成功返回模板信息
		List<PriceTransportLineEntity> lineList = new ArrayList<PriceTransportLineEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new TransportLineImportTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			// 解析Excel
			lineList = readExcelProduct(file, bs);
			if (null == lineList || lineList.size() <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			 condition.put("tempid", currentNo); 

			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, lineList, map, currentNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			// 获得初步校验后和转换后的数据
			lineList = (List<PriceTransportLineEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);

			// 设置属性
			String lineNo = null;
			for (int i = 0; i < lineList.size(); i++) {
				PriceTransportLineEntity line = lineList.get(i);
				String billNo = id;
				line.setTemplateId(billNo);
				lineNo = sequenceService.getBillNoOne(PriceTransportLineEntity.class.getName(), "TL", "00000");
				line.setTransportLineNo(lineNo);
				line.setDelFlag("0");// 设置为未作废
				line.setCreator(userName);
				line.setCreateTime(currentTime);
				line.setLastModifier(userName);
				line.setLastModifyTime(currentTime);
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			// 插入正式表
			int insertNum = priceTransportLineService.saveList(lineList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("存档运输路线表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return map;
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				return map;
			}
		} catch (Exception e) {
			logger.error("导入运输路线模板异常：", e);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
		}
		
		return map;
	}
*/
	
	private String getMd5(String str){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("异常:", e);
			return null;
		}
	}

	public List<PriceTransportLineEntity> getOrgList(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		List<PriceTransportLineEntity> list=priceTransportLineService.query(parameter);
		return list;
	}

	public List<String> getKeyDataProperty() {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();
		list.add("fromWarehouseId");//始发仓	  
		list.add("startStation");//始发站点
		list.add("fromProvinceId");//始发省份
		list.add("fromCityId");//始发城市
		list.add("fromDistrictId");//始发区
		list.add("endWarehouseId");//目的仓
		list.add("endStation");//目的站点
		list.add("toProvinceId");//目的省份
		list.add("toCityId");//目的城市
		list.add("toDistrictId");//目的区
		list.add("transportTypeCode");//业务类型
		/*list.add("timeliness");//时效(天)
*/		//list.add("temperatureTypeName");//温度类型
		return list;
	}
}
