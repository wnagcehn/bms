package com.jiuyescm.bms.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsWeightRepository;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 配送费用初始化
 * 
 * @author wangchen
 *
 */
@JobHander(value = "dispatchBillInitJob")
@Service
public class DispatchBillInitJob extends IJobHandler {

	@Autowired
	private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired
	private IBizDispatchBillService bizDispatchBillService;
	@Autowired
	private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired
	private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	@Autowired
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBmsGroupService bmsGroupService;
	@Autowired
	private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired
	private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepository;
	@Autowired
	private IAddressService omsAddressService;
	@Autowired
	private IBmsProductsWeightRepository bmsProductsWeightRepository;
	@Autowired
	private IPriceDispatchDao iPriceDispatchDao;
	@Autowired
	private ICarrierService carrierService;

	private String _subjectCode = "de_delivery_amount";

	String[] subjects = null;
	List<SystemCodeEntity> scList = null;
	List<String> changeCusList = null;
	List<String> throwWeightList = null;
	List<SystemCodeEntity> monthFeeList = null;
	Map<String, String> carrierMap = null;

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("DispatchBillInitJob start.");
		XxlJobLogger.log("开始配送费用初始化任务");
		return CalcJob(params);
	}

	private ReturnT<String> CalcJob(String[] params) {

		long startTime = System.currentTimeMillis();
		int num = 1000;

		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.length > 0) {
			try {
				map = JobParameterHandler.handler(params);// 处理定时任务参数
			} catch (Exception e) {
				XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："
						+ (System.currentTimeMillis() - startTime) + "毫秒");
				return ReturnT.FAIL;
			}
		} else {
			// 未配置最多执行多少运单
			map.put("num", num);
		}

		// 初始化配置
		initConf();

		// 查询所有状态为0的业务数据
		long currentTime = System.currentTimeMillis();
		map.put("isCalculated", "0");
		List<BizDispatchBillEntity> bizList = null;
		List<FeesReceiveDispatchEntity> feesList = new ArrayList<FeesReceiveDispatchEntity>();
		try {
			XxlJobLogger.log("dispatchBillInitJob查询条件map:【{0}】  ", map);
			bizList = bizDispatchBillService.query(map);
			if (CollectionUtils.isNotEmpty(bizList)) {
				XxlJobLogger.log("【配送运单】查询行数【{0}】耗时【{1}】", bizList.size(), (System.currentTimeMillis() - currentTime));
				// 初始化费用
				initFees(bizList, feesList);
				// 批量更新业务数据&批量写入费用表
				updateAndInsertBatch(bizList, feesList);
			}

			// 只有业务数据查出来小于1000才发送mq，这时候才代表统计完成，才发送MQ
			if (CollectionUtils.isEmpty(bizList) || bizList.size() < num) {
				sendTask();
			}

		} catch (Exception e) {
			XxlJobLogger.log("【终止异常】,查询业务数据异常,原因: {0} ,耗时： {1}毫秒", e.getMessage(),
					((System.currentTimeMillis() - currentTime)));
			return ReturnT.FAIL;
		}

		XxlJobLogger.log("初始化费用总耗时：【{0}】毫秒", System.currentTimeMillis() - startTime);
		return ReturnT.SUCCESS;

	}

	private void initFees(List<BizDispatchBillEntity> bizList, List<FeesReceiveDispatchEntity> feesList) {
		for (BizDispatchBillEntity entity : bizList) {
			String feesNo = "STO" + snowflakeSequenceService.nextStringId();
			// 业务数据修改
			entity.setFeesNo(feesNo);
			entity.setIsCalculated("1");
			entity.setRemark("");

			// 费用
			FeesReceiveDispatchEntity feeEntity = new FeesReceiveDispatchEntity();
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0l;// 当前系统时间
			// 计费参数获取
			// 1)***********************获取计费物流商******************************
			getSubjectId(getCarrierId(entity));
			String carrierId = entity.getChargeCarrierId();
			XxlJobLogger.log("-->" + entity.getId() + "验证数据时物流商-- 【{0}】  ", entity.getChargeCarrierId());
			current = System.currentTimeMillis();
			XxlJobLogger.log("-->" + entity.getId() + "验证物流商  耗时【{0}】毫秒 ", (current - start));

			// 2)***********************获取新泡重************************
			start = System.currentTimeMillis();// 操作开始时间
			// 新增逻辑，若要按抛重计算，此时的泡重需要我们自己去计算运单中所有耗材中最大的体积/6000
			double newThrowWeight = getNewThrowWeight(entity);
			// 将新泡重赋值
			// entity.setThrowWeight(newThrowWeight);
			entity.setCorrectThrowWeight(newThrowWeight);
			current = System.currentTimeMillis();
			XxlJobLogger.log("-->" + entity.getId() + "泡重获取  耗时【{0}】毫秒 ", (current - start));

			// 3)***********************获取省市区和计费重量***********************
			start = System.currentTimeMillis();// 操作开始时间
			String provinceId = "";
			String cityId = "";
			String districtId = "";
			// 调整省市区不为空 优先取调整省市区
			if (StringUtils.isNotBlank(entity.getAdjustProvinceId()) || StringUtils.isNotBlank(entity.getAdjustCityId())
					|| StringUtils.isNotBlank(entity.getAdjustDistrictId())) {
				provinceId = entity.getAdjustProvinceId();
				cityId = entity.getAdjustCityId();
				districtId = entity.getAdjustDistrictId();
			} else {
				provinceId = entity.getReceiveProvinceId();
				cityId = entity.getReceiveCityId();
				districtId = entity.getReceiveDistrictId();
			}

			// 如果是顺丰配送 匹配标准地址
			if (throwWeightList.contains(carrierId)) {
				RegionVo vo = new RegionVo(ReplaceChar(provinceId), ReplaceChar(cityId), ReplaceChar(districtId));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if ((StringUtils.isNotBlank(provinceId) && StringUtils.isBlank(matchVo.getProvince()))
						|| StringUtils.isNotBlank(cityId) && StringUtils.isBlank(matchVo.getCity())
						|| StringUtils.isNotBlank(districtId) && StringUtils.isBlank(matchVo.getDistrict())) {
					XxlJobLogger.log("-->" + entity.getId() + "收件人地址存在空值  省【{0}】市【{1}】区【{2}】 运单号【{3}】:" + provinceId,
							cityId, districtId, entity.getWaybillNo());
				} else {
					entity.setReceiveProvinceId(matchVo.getProvince());
					entity.setReceiveCityId(matchVo.getCity());
					entity.setReceiveDistrictId(matchVo.getDistrict());

					feeEntity.setToProvinceName(matchVo.getProvince());
					feeEntity.setToCityName(matchVo.getCity());
					feeEntity.setToDistrictName(matchVo.getDistrict());
				}

				// 如果存在调整重量，优先取
				if (!DoubleUtil.isBlank(entity.getAdjustWeight())) {
					double dd = getResult(entity.getAdjustWeight(), carrierId);
					entity.setWeight(dd);
					entity.setTotalWeight(entity.getAdjustWeight());
				} else {
					// 新增逻辑判断，判断是否有修正重量，用运单号去修正表里去查询
					boolean hasCorrect = false;
					double correctWeight = 0.0;
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("waybillNo", entity.getWaybillNo());
					BmsMarkingProductsEntity markEntity = bmsProductsWeightRepository.queryMarkVo(condition);
					if (markEntity != null && markEntity.getCorrectWeight() != null
							&& !DoubleUtil.isBlank(markEntity.getCorrectWeight().doubleValue())) {
						hasCorrect = true;
						correctWeight = markEntity.getCorrectWeight().doubleValue();
					}

					// 纠正泡重不存在
					if (DoubleUtil.isBlank(entity.getCorrectThrowWeight())) {
						if (hasCorrect) {
							// 有纠正重量时，直接比较纠正重量和物流商重量
							if (entity.getCarrierWeight() >= correctWeight) {
								// 物流商重量大于等于纠正重量
								double dd = getResult(entity.getCarrierWeight(), carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getCarrierWeight());
							} else {
								// 物流商重量小于纠正重量
								double dd = getResult(correctWeight, carrierId);
								entity.setWeight(dd);
								double resultWeight = compareWeight(entity.getTotalWeight(),
										getResult(entity.getTotalWeight(), carrierId), correctWeight);
								entity.setTotalWeight(resultWeight);
							}
						} else {
							// 没有纠正重量，物流商重量和运单重量比较
							if (entity.getCarrierWeight() >= entity.getTotalWeight()) {
								// 物流商重量大于等于运单重量
								double dd = getResult(entity.getCarrierWeight(), carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getCarrierWeight());
							} else {
								// 物流商重量小于重量
								double dd = getResult(entity.getTotalWeight(), carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getTotalWeight());
							}
							/*
							 * if(!DoubleUtil.isBlank(entity.getTotalWeight())){
							 * double dd = getResult(entity.getTotalWeight());
							 * entity.setWeight(dd); //实际重量存在时取实际重量
							 * entity.setTotalWeight(entity.getTotalWeight());
							 * }else{ XxlJobLogger.log("-->"+entity.getId()+
							 * "--------顺丰非同城，纠正重量，泡重和实际重量都不存在，无法计算--------");
							 * entity.setIsCalculated(CalculateState.Sys_Error.
							 * getCode());
							 * feeEntity.setIsCalculated(CalculateState.
							 * Sys_Error.getCode());
							 * entity.setRemark(entity.getRemark()+
							 * "顺丰非同城，泡重和实际重量都不存在，无法计算;"); }
							 */
						}

					} else {
						// 泡重存在时
						if (hasCorrect) {
							// 有纠正重量时比较 泡重和 纠正重量
							if (correctWeight >= entity.getCorrectThrowWeight()) {
								// 纠正重量大于抛重重量，按纠正重量算
								double dd = getResult(correctWeight, carrierId);
								entity.setWeight(dd);
								// 有纠正重量，比较纠正重量和运单重量是否相等
								double resultWeight = compareWeight(entity.getTotalWeight(),
										getResult(entity.getTotalWeight(), carrierId), correctWeight);
								entity.setTotalWeight(resultWeight);
							} else if (correctWeight < entity.getCorrectThrowWeight()) {
								// 纠正重量小于抛重时，按抛重算
								double dd = getResult(entity.getCorrectThrowWeight(), carrierId);
								entity.setWeight(dd);// 计费重量
								entity.setTotalWeight(entity.getCorrectThrowWeight()); // 实际重量 eg:5.1
							}
						} else {
							if (!DoubleUtil.isBlank(entity.getTotalWeight())) {
								// 没有纠正重量时比较 泡重和 实际重量
								if (entity.getTotalWeight() >= entity.getCorrectThrowWeight()) {
									// 运单重量大于抛重时，按运单重量算
									double dd = getResult(entity.getTotalWeight(), carrierId);
									entity.setWeight(dd);
									entity.setTotalWeight(entity.getTotalWeight());
								} else if (entity.getTotalWeight() < entity.getCorrectThrowWeight()) {
									// 运单重量小于抛重时，按抛重算
									double dd = getResult(entity.getCorrectThrowWeight(), carrierId);
									entity.setWeight(dd);// 计费重量
									entity.setTotalWeight(entity.getCorrectThrowWeight()); // 实际重量 eg:5.1
								}
							} else {
								// 实际重量为空时，直接取泡重
								double dd = getResult(entity.getCorrectThrowWeight(), carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getCorrectThrowWeight());
							}
						}
					}
				}
			} else {
				RegionVo vo = new RegionVo(ReplaceChar(provinceId), ReplaceChar(cityId), ReplaceChar(districtId));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if ((StringUtils.isNotBlank(provinceId) && StringUtils.isBlank(matchVo.getProvince()))
						|| StringUtils.isNotBlank(cityId) && StringUtils.isBlank(matchVo.getCity())) {
					XxlJobLogger.log("-->" + entity.getId() + "收件人地址存在空值  省【{0}】市【{1}】区【{2}】 运单号【{3}】:" + provinceId,
							cityId, districtId, entity.getWaybillNo());
				} else {
					if (StringUtils.isNotBlank(entity.getAdjustProvinceId())
							|| StringUtils.isNotBlank(entity.getAdjustCityId())
							|| StringUtils.isNotBlank(entity.getAdjustDistrictId())) {
						entity.setAdjustProvinceId(matchVo.getProvince());
						entity.setAdjustCityId(matchVo.getCity());
						entity.setReceiveProvinceId(matchVo.getProvince());
						entity.setReceiveCityId(matchVo.getCity());
					} else {
						entity.setReceiveProvinceId(matchVo.getProvince());
						entity.setReceiveCityId(matchVo.getCity());
					}
					feeEntity.setToProvinceName(matchVo.getProvince());
					feeEntity.setToCityName(matchVo.getCity());
				}

				if (!DoubleUtil.isBlank(entity.getAdjustWeight())) {
					entity.setWeight(Math.ceil(entity.getAdjustWeight())); // 计费重量:6
					entity.setTotalWeight(entity.getAdjustWeight()); // 实际重量 eg:5.1
				} else {
					// 新增逻辑判断，判断是否有修正重量，用运单号去修正表里去查询
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("waybillNo", entity.getWaybillNo());
					BmsMarkingProductsEntity markEntity = bmsProductsWeightRepository.queryMarkVo(condition);
					if (markEntity != null && markEntity.getCorrectWeight() != null
							&& !DoubleUtil.isBlank(markEntity.getCorrectWeight().doubleValue())) {
						double weight = markEntity.getCorrectWeight().doubleValue();
						entity.setWeight(Math.ceil(weight));// 计费重量 : 6
						// 比较重量
						double resultWeight = compareWeight(entity.getTotalWeight(), Math.ceil(entity.getTotalWeight()),
								weight);
						entity.setTotalWeight(resultWeight);// 实际重量 eg:5.1
					} else {
						entity.setWeight(Math.ceil(entity.getTotalWeight()));// 计费重量 : 6
						entity.setTotalWeight(entity.getTotalWeight());// 实际重量  eg:5.1
					}

				}
			}

			// 此时费用写入计费重量
			feeEntity.setChargedWeight(entity.getWeight());

			// 费用初始化
			feeEntity.setCreator("system");
			feeEntity.setCreateTime(entity.getCreateTime());// 费用表的创建时间应为业务表的创建时间
			feeEntity.setDeliveryDate(entity.getCreateTime());
			feeEntity.setOutstockNo(entity.getOutstockNo()); // 出库单号
			feeEntity.setWarehouseCode(entity.getWarehouseCode()); // 仓库ID
			feeEntity.setWarehouseName(entity.getWarehouseName()); // 仓库名称
			feeEntity.setCustomerid(entity.getCustomerid()); // 商家ID
			feeEntity.setCustomerName(entity.getCustomerName()); // 商家名称
			feeEntity.setDeliveryid(entity.getDeliverid()); // 物流商ID
			feeEntity.setDeliverName(entity.getDeliverName()); // 物流商名称
			feeEntity.setCarrierid(entity.getChargeCarrierId());
			feeEntity.setCarrierName(carrierMap.get(entity.getChargeCarrierId()));
			feeEntity.setWaybillNo(entity.getWaybillNo()); // 运单号
			feeEntity.setTotalWeight(entity.getTotalWeight());// 实际重量
			feeEntity.setSubjectCode(_subjectCode);
			feeEntity.setOtherSubjectCode(_subjectCode);
			feeEntity.setToProvinceName(provinceId);// 目的省
			feeEntity.setToCityName(cityId); // 目的市
			feeEntity.setToDistrictName(districtId);// 目的区
			feeEntity.setExternalNo(entity.getExternalNo()); // 外部单号
			feeEntity.setAcceptTime(entity.getAcceptTime()); // 揽收时间
			feeEntity.setSignTime(entity.getSignTime());
			feeEntity.setStatus("0");
			feeEntity.setDelFlag("0");
			feeEntity.setAmount(0.0d); // 入仓金额
			feeEntity.setTemperatureType(entity.getTemperatureTypeCode());// 温度
			feeEntity.setFeesNo(feesNo);
			feeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
			feeEntity.setBigstatus(entity.getBigstatus());
			feeEntity.setSmallstatus(entity.getSmallstatus());
			feeEntity.setChargedWeight(entity.getWeight()); // 从weight中取出计费重量
			feeEntity.setWeightLimit(0.0d);
			feeEntity.setUnitPrice(0.0d);
			feeEntity.setHeadWeight(0.0d);
			feeEntity.setHeadPrice(0.0d);
			feeEntity.setContinuedWeight(0.0d);
			feeEntity.setContinuedPrice(0.0d);
			feeEntity.setPriceId("");
			feeEntity.setServiceTypeCode(StringUtils.isEmpty(entity.getAdjustServiceTypeCode())
					? entity.getServiceTypeCode() : entity.getAdjustServiceTypeCode());
			feeEntity.setIsCalculated("99");
			feeEntity.setCalcuMsg("");
			feesList.add(feeEntity);
		}
	}

	/**
	 * 根据物流商id获取到物流商名字
	 * 
	 * @param carrierId
	 * @return
	 */
	private String getSubjectId(String carrierId) {
		String subjectID = "";
		for (SystemCodeEntity scEntity : scList) {
			if (scEntity.getExtattr1().equals(carrierId)) {
				subjectID = scEntity.getCode();
				break;
			}
		}
		return subjectID;
	}

	/**
	 * 获取计算的物流商id
	 * 
	 * @param carrierId
	 * @return
	 */
	private String getCarrierId(BizDispatchBillEntity entity) {
		String carrierId = "";
		// 物流商的判断
		// 1.有调整物流商时直接按调整物流商计算
		// 2.无调整物流商时判断是否是指定的商家 获取 此时的实际物流商是否为顺丰，如果是顺丰则需去物流商调整查询该运单，判断其修改的原因
		// 根据责任方判断计费物流商:商家的责任就按实际物流商顺丰计费;我们的责任就按订单物流商九曳计费
		// 不为顺丰的继续按实际物流商计算
		if (StringUtils.isBlank(entity.getAdjustCarrierId())) {
			// 调整物流商为空时 判断实际物流商是否是顺丰
			if ("1500000015".equals(entity.getCarrierId()) && changeCusList.size() > 0
					&& changeCusList.contains(entity.getCustomerid())) {
				// 判断是否是指定的商家
				// 根据运单号和物流商顺丰去物流商修改表里查询
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("carrierId", entity.getCarrierId());
				param.put("waybillNo", entity.getWaybillNo());
				param.put("warehouseCode", entity.getWarehouseCode());
				param.put("customerid", entity.getCustomerid());
				BizDispatchCarrierChangeEntity changeEntity = bizDispatchBillService.queryCarrierChangeEntity(param);
				if (changeEntity != null) {
					// 判断其修改的原因
					if ("customer_reason".equals(changeEntity.getUpdateReasonTypeCode())) {
						// 商家原因 按实际物流商顺丰计费
						carrierId = entity.getCarrierId();
						changeEntity.setDutyType("商家原因");
					} else {
						// 获取商家仓库对应的省配送范围
						List<String> proviceList = getProviceList(entity);
						// 在九曳配送范围内的按九曳
						if (proviceList.contains(entity.getReceiveProvinceId())) {
							// 配送范围内的按原始物流商算，且更新为商家原因
							carrierId = changeEntity.getOldCarrierId();
							changeEntity.setDutyType("我司原因");
						} else {
							// 不在九曳范围内的按顺丰
							carrierId = entity.getCarrierId();
							// 第一次为空时保存原修改原因，后面不修改
							if (StringUtils.isBlank(changeEntity.getOldUpdateReason())) {
								changeEntity.setOldUpdateReason(changeEntity.getUpdateReasonTypeCode() + ","
										+ changeEntity.getUpdateReasonType() + ","
										+ changeEntity.getUpdateReasonDetailCode() + ","
										+ changeEntity.getUpdateReasonDetail());
							}
							// 修改原因
							changeEntity.setDutyType("商家原因");
							changeEntity.setUpdateReasonTypeCode("customer_reason");
							changeEntity.setUpdateReasonType("商家原因");
							changeEntity.setUpdateReasonDetailCode("order_out_of_range");
							changeEntity.setUpdateReasonDetail("订单地址超配送范围");
						}
					}
					// 更新责任原因至物流商修改表
					bizDispatchBillService.updateCarrierChange(changeEntity);

					// 修改原始物流商
					entity.setOriginCarrierId("1500000016");
					entity.setOriginCarrierName("JY");
				} else {
					carrierId = entity.getCarrierId();
				}
			} else {
				carrierId = entity.getCarrierId();
			}

		} else {
			carrierId = entity.getAdjustCarrierId();
		}

		// chargeCarrierId=carrierId;
		entity.setChargeCarrierId(carrierId);
		XxlJobLogger.log("-->" + entity.getId() + "获取到计费物流商-- 【{0}】 ", entity.getChargeCarrierId());
		return entity.getChargeCarrierId();
	}

	public double getNewThrowWeight(BizDispatchBillEntity entity) {
		double throwWeight = 0d;
		try {
			// 验证是否存在签约服务
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("waybillNo", entity.getWaybillNo());
			Double volumn = bizOutstockPackmaterialRepository.getMaxVolumByMap(condition);
			if (!DoubleUtil.isBlank(volumn)) {
				throwWeight = (double) volumn / 6000;
				throwWeight = (double) Math.round(throwWeight * 100) / 100;
			}
		} catch (Exception ex) {
			XxlJobLogger.log("-->" + entity.getId() + "获取泡重失败:{1}", ex.getMessage());
		}
		return throwWeight;
	}

	private void initConf() {
		subjects = initSubjects();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "DISPATCH_COMPANY");
		scList = systemCodeService.querySysCodes(map);

		map = new HashMap<String, Object>();
		map.put("typeCode", "MONTH_FEE_COUNT");
		monthFeeList = systemCodeService.querySysCodes(map);// 月结账号

		map = new HashMap<String, Object>();
		throwWeightList = new ArrayList<String>();
		map.put("typeCode", "THROW_WEIGHT_CARRIER");
		List<SystemCodeEntity> throwList = systemCodeService.querySysCodes(map);// 计抛的物流商
		for (SystemCodeEntity s : throwList) {
			throwWeightList.add(s.getCode());
		}

		// 指定的商家
		map = new HashMap<String, Object>();
		map.put("groupCode", "jiuye_to_shunfeng");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup = bmsGroupService.queryOne(map);
		if (bmsGroup != null) {
			changeCusList = bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
		}

		// 物流商
		carrierMap = getCarrier();
	}

	private String ReplaceChar(String str) {
		if (StringUtils.isNotBlank(str)) {
			String[] arr = { " ", "\\", "/", ",", ".", "-" };
			for (String a : arr) {
				str = str.replace(a, "");
			}
		}
		return str;
	}

	/**
	 * 根据物流商判断进位规则 1.除顺丰外 1.1kg或者1.6kg，重量进位为2 2.顺丰运费计算规则（超重1.4kg时 重量进位为1.5,
	 * ;超重1.6kg时 重量进位为*2计算）
	 * 
	 * @param weight
	 * @return
	 */
	public double getResult(double weight, String carrierId) {
		// 原重
		double a = weight;
		// 现重
		double c = 0.0;

		if ("1500000015".equals(carrierId)) {
			// 顺丰
			int b = (int) a;
			double min = a - b;

			if (0 < min && min < 0.5) {
				c = b + 0.5;
			}

			if (0.5 < min && min < 1) {
				c = b + 1;
			}
			if (min == 0 || min == 0.5) {
				c = a;
			}
		} else {
			// 除顺丰外
			c = Math.ceil(a);
		}

		return c;
	}

	/**
	 * 若 进位重量 = 纠正重量，则返回 运单重量 若 进位重量 != 纠正重量，则返回纠正重量
	 * 
	 * @param waybillWeight
	 *            运单重量
	 * @param carryWeight
	 *            进位重量
	 * @param corrWeight
	 *            纠正重量
	 * @return
	 */
	private double compareWeight(double waybillWeight, double carryWeight, double corrWeight) {
		if (DoubleUtil.isBlank(waybillWeight)) {
			// 如果运单重量没有，当做0处理
			waybillWeight = 0.0;
		}
		return carryWeight == corrWeight ? waybillWeight : corrWeight;
	}

	private List<String> getProviceList(BizDispatchBillEntity entity) {

		List<String> reList = new ArrayList<String>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupCode", "bms_jy_area");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup = bmsGroupService.queryOne(map);
		if (bmsGroup != null) {
			List<BmsGroupCustomerVo> list;
			try {
				list = bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
				if (list.size() > 0) {
					BmsGroupCustomerVo vo = list.get(0);
					if (vo != null) {
						// 查询九曳配送范围
						map.put("customerId", vo.getCustomerid());
						map.put("subjectId", "JIUYE_DISPATCH");
						map.put("wareHouseId", entity.getWarehouseCode());
						reList = iPriceDispatchDao.queryJiuYeArea(map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return reList;
	}

	/**
	 * 获取物流商的id和名称
	 * 
	 * @return
	 */
	public Map<String, String> getCarrier() {
		Map<String, String> map = new HashMap<String, String>();
		// 获取所有的物流商信息
		List<CarrierVo> carrierList = carrierService.queryAllCarrier();
		for (CarrierVo vo : carrierList) {
			map.put(vo.getCarrierid(), vo.getName());
		}
		return map;
	}

	private String[] initSubjects() {
		// 这里的科目应该在科目组中配置,动态查询
		// de_delivery_amount(运费 )
		Map<String, String> map = bmsGroupSubjectService.getSubject("job_subject_dispatch");
		if (map.size() == 0) {
			String[] strs = { "de_delivery_amount" };
			return strs;
		} else {
			int i = 0;
			String[] strs = new String[map.size()];
			for (String value : map.keySet()) {
				strs[i] = value;
				i++;
			}
			return strs;
		}
	}

	private void sendTask() throws Exception {
		Map<String, Object> sendTaskMap = new HashMap<String, Object>();
		sendTaskMap.put("isCalculated", "99");
		sendTaskMap.put("subjectList", Arrays.asList(subjects));
		// 对这些费用按照商家、科目、时间排序
		List<BmsCalcuTaskVo> list = bmsCalcuTaskService.queryDisByMap(sendTaskMap);
		for (BmsCalcuTaskVo vo : list) {
			vo.setCrePerson("系统");
			vo.setCrePersonId("system");
			try {
				bmsCalcuTaskService.sendTask(vo);
				XxlJobLogger.log("mq发送成功,商家id:{0},年月:{1},科目id:{2}", vo.getCustomerId(), vo.getCreMonth(),
						vo.getSubjectCode());
			} catch (Exception e) {
				XxlJobLogger.log("mq发送失败{0}", e);
			}
		}
	}

	public void updateAndInsertBatch(List<BizDispatchBillEntity> billList, List<FeesReceiveDispatchEntity> feesList) {
		try {
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0l;// 当前系统时间
			bizDispatchBillService.newUpdateBatch(billList);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ", (current - start));
			start = System.currentTimeMillis();// 系统开始时间
			feesReceiveDispatchService.InsertBatch(feesList);
			current = System.currentTimeMillis();
			XxlJobLogger.log("更新费用数据耗时：【{0}】毫秒 ", (current - start));
		} catch (Exception e) {
			XxlJobLogger.log("-->批量保存异常" + e.getMessage());
		}

	}

}
