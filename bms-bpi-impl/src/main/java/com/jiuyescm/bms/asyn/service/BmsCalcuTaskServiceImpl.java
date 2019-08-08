package com.jiuyescm.bms.asyn.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.asyn.repo.IBmsAsynCalcuTaskRepository;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchPackageRepository;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockMasterRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizProductStorageRepository;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.common.enumtype.MQSubjectEnum;
import com.jiuyescm.bms.subject.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.sequence.api.ISnowflakeSequenceService;

import net.sf.json.JSONObject;

@Service("bmsCalcuTaskService")
public class BmsCalcuTaskServiceImpl implements IBmsCalcuTaskService {

	private Logger logger = LoggerFactory
			.getLogger(BmsCalcuTaskServiceImpl.class);

	@Autowired
	IBmsAsynCalcuTaskRepository bmsAsynCalcuTaskRepositoryimpl;
	@Autowired
	private ISnowflakeSequenceService snowflakeSequenceService;
	@Autowired
	private JmsTemplate jmsQueueTemplate;
	@Autowired
	private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepository;
	@Autowired
	private IBizDispatchBillRepository bizDispatchBillRepository;
	
	@Autowired
	private Lock lock;
	@Autowired
	private ICustomerDictService customerDictService;
	@Autowired
	private IBmsSubjectInfoService bmsSubjectService;
	@Autowired
	IBizPalletInfoRepository bizPalletInfoRepository;
	@Autowired
	IBizOutstockMasterRepository bizOutstockMasterRepository;
	@Autowired
	IBizAddFeeRepository bizAddFeeRepository;
	@Autowired
	IBizProductStorageRepository bizProductStorageRepository;
	@Autowired
	IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	@Autowired
	IBizDispatchPackageRepository bizDispatchPackageRepository;
	
	private static final String FEES_TYPE_ITEM = "item";
	private static final String FEES_TYPE_PALLET = "pallet";
	private static final String SEND_MQ = "TRUE";
	private static final String NOT_SEND_MQ = "FALSE";

	@Override
	public PageInfo<BmsCalcuTaskVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) throws BizException {
		// 日期拼接
		String creMonth = "";
		if (Integer.valueOf(condition.get("createMonth").toString()) < 10) {
			creMonth = condition.get("createYear").toString() + "0"
					+ condition.get("createMonth");
		} else {
			creMonth = condition.get("createYear").toString()
					+ condition.get("createMonth");
		}
		condition.put("creMonth", creMonth);
		
		PageInfo<BmsCalcuTaskVo> pageVoInfo = new PageInfo<BmsCalcuTaskVo>();
		try {		
			PageInfo<BmsAsynCalcuTaskEntity> pageInfo =bmsAsynCalcuTaskRepositoryimpl
					.queryInfoByCustomerIdSe(condition, pageNo,pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			} catch (Exception e) {
			logger.error("查询计算任务异常", e);
			throw new BizException("查询计算任务异常");
		}
		return pageVoInfo;
	}

	@Override
	public List<BmsCalcuTaskVo> queryDetail(Map<String, Object> map)
			throws BizException {
		List<BmsCalcuTaskVo> list = new ArrayList<BmsCalcuTaskVo>();
		try {
			List<BmsAsynCalcuTaskEntity> entities = bmsAsynCalcuTaskRepositoryimpl
					.queryDetail(map);
			if (CollectionUtils.isNotEmpty(entities)) {
				for (BmsAsynCalcuTaskEntity taskEntity : entities) {
					BmsCalcuTaskVo taskVo = new BmsCalcuTaskVo();
					PropertyUtils.copyProperties(taskVo, taskEntity);
					list.add(taskVo);
				}
			}
		} catch (Exception e) {
			logger.error("查询计算任务明细异常", e);
			throw new BizException("查询计算任务明细异常");
		}
		return list;
	}

	@Override
	public BmsCalcuTaskVo sendTask(final BmsCalcuTaskVo vo) throws Exception {
		logger.info("请求参数：", JSONObject.fromObject(vo));

		if (StringUtil.isEmpty(vo.getSubjectCode())
				|| StringUtil.isEmpty(vo.getCustomerId())
				|| vo.getCreMonth() == null) {
			throw new BizException("商家ID,科目编码,业务年月必填");
		}

		String customerName = customerDictService.getCustomerNameByCode(vo
				.getCustomerId());
		if (StringUtil.isEmpty(customerName)) {
			throw new BizException("商家【" + vo.getCustomerId() + "】不存在");
		}

		BmsSubjectInfoVo subjectVo = bmsSubjectService.queryReceiveByCode(vo
				.getSubjectCode());
		if (subjectVo == null || StringUtil.isEmpty(subjectVo.getSubjectName())) {
			throw new BizException("科目【" + vo.getSubjectCode() + "】不存在");
		}
		vo.setCustomerName(customerName);
		vo.setSubjectName(subjectVo.getSubjectName());
		vo.setCreTime(JAppContext.currentTimestamp());
		String customerId = vo.getCustomerId();
		String subjectCode = vo.getSubjectCode();
		String creMonth = vo.getCreMonth().toString();
		// 判断商品按件
		if ("wh_product_storage".equals(subjectCode)
				&& "item".equals(vo.getFeesType())) {
			vo.setFeesType(FEES_TYPE_ITEM);
		} else if ("wh_product_storage".equals(subjectCode)) {
			vo.setFeesType(FEES_TYPE_PALLET);
		}
		// 发送mq标记
		String mq = null;
		try {
			// 查询计算任务表数据
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("customerId", customerId);
			taskMap.put("subjectCode", subjectCode);
			taskMap.put("creMonth", creMonth);
			taskMap.put("feesType", vo.getFeesType());
			List<BmsAsynCalcuTaskEntity> calList = bmsAsynCalcuTaskRepositoryimpl
					.query(taskMap);
			// 此次任务ID
			String taskId = "CT" + snowflakeSequenceService.nextStringId();// 任务ID
			vo.setTaskId(taskId);

			// 如果不存在，计算任务表新增
			if (CollectionUtils.isEmpty(calList)) {
				vo.setTaskStatus(0);
				// 新增计算任务表
				saveTask(vo);
				// 写入日志表
				saveTaskLog(vo);
				// 发送mq
				mq = SEND_MQ;
			} else {
				BmsAsynCalcuTaskEntity calEntity = calList.get(0);
				Integer calTaskStatus = calEntity.getTaskStatus();
				// 如果状态为处理中（0,10），写入日志表
				if (0 == calTaskStatus || 10 == calTaskStatus) {
					// 写入日志表
					saveTaskLog(vo);
					// 不发mq
					mq = NOT_SEND_MQ;
					// 如果状态为结束，写入日志表，更新状态为0,task_id改变，发mq
				} else if (20 == calTaskStatus || 30 == calTaskStatus
						|| 50 == calTaskStatus) {
					// 保存任务：更新状态为0,task_id改变
					Date now=new Date();
					BmsAsynCalcuTaskEntity entity = new BmsAsynCalcuTaskEntity();
					entity.setFinishCount(0);
					entity.setCalcuStatus(0);
					entity.setTaskRate(0);
					entity.setTaskStatus(0);
					entity.setFeesCount(0);
					entity.setUncalcuCount(0);
					entity.setCalcuCount(0);
					entity.setContractMissCount(0);
					entity.setQuoteMissCount(0);
					entity.setNoExeCount(0);
					entity.setSysErrorCount(0);
					entity.setBeginCount(0);
					entity.setTaskId(calEntity.getTaskId());
					entity.setNewid(vo.getTaskId());
					entity.setTotalAmount(BigDecimal.ZERO);
					entity.setCreTime(new Timestamp(now.getTime()));
					entity.setFinTimeIsNull("null");
					entity.setProcTimeIsNull("null");
					entity.setCrePerson(vo.getCrePerson());
					entity.setCrePersonId(vo.getCrePersonId());
					bmsAsynCalcuTaskRepositoryimpl.updateByTaskId(entity);
					// 写入日志表
					saveTaskLog(vo);
					// 发送mq
					mq = SEND_MQ;
				}
			}
		} catch (Exception ex) {
			logger.error("系统异常", ex);
			vo.setTaskStatus(0);
			vo.setRemark("系统异常");
			// 不发mq
			mq = NOT_SEND_MQ;
		}
		// 判断发送mq
		if (SEND_MQ.equals(mq)) {
			sendMq(vo);
		}
		return vo;
	}

	private void sendMq(final BmsCalcuTaskVo vo) {
		String subjectCode = vo.getSubjectCode();
		String mqQueue = null;
		if ("wh_product_storage".equals(subjectCode)
				&& "item".equals(vo.getFeesType())) {
			mqQueue = MQSubjectEnum.getDesc(subjectCode + "_item");
		} else {
			mqQueue = MQSubjectEnum.getDesc(subjectCode);
		}
		jmsQueueTemplate.send(mqQueue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(vo.getTaskId());
			}
		});
	}

	@Override
	public void saveTask(BmsCalcuTaskVo vo) throws Exception {
		BmsAsynCalcuTaskEntity calcuTask = new BmsAsynCalcuTaskEntity();
		try {
			PropertyUtils.copyProperties(calcuTask, vo);
			bmsAsynCalcuTaskRepositoryimpl.save(calcuTask);
		} catch (Exception e) {
			logger.error("保存任务异常", e);
			throw e;
		}
	}

	@Override
	public void saveTaskLog(BmsCalcuTaskVo vo) throws Exception {
		BmsAsynCalcuTaskEntity calcuTask = new BmsAsynCalcuTaskEntity();
		try {
			PropertyUtils.copyProperties(calcuTask, vo);
			bmsAsynCalcuTaskRepositoryimpl.saveLog(calcuTask);
		} catch (Exception e) {
			logger.error("保存任务日志异常", e);
			throw e;
		}
	}

	@Override
	public void update(BmsCalcuTaskVo taskVo) throws Exception {
		try {
			BmsAsynCalcuTaskEntity calcuTask = new BmsAsynCalcuTaskEntity();
			PropertyUtils.copyProperties(calcuTask, taskVo);
			bmsAsynCalcuTaskRepositoryimpl.update(calcuTask);
		} catch (Exception e) {
			logger.error("更新计算任务异常", e);
			throw new BizException("更新计算任务异常");
		}
	}

	@Override
	public void updateRate(String taskId, Integer taskRate) {
		BmsAsynCalcuTaskEntity calcuTask = new BmsAsynCalcuTaskEntity();
		calcuTask.setTaskId(taskId);
		calcuTask.setTaskRate(taskRate);
		try {
			bmsAsynCalcuTaskRepositoryimpl.update(calcuTask);
		} catch (Exception e) {
			logger.error("更新计算进度异常", e);
			throw new BizException("更新计算进度异常");
		}
	}

	@Override
	public BmsCalcuTaskVo queryCalcuTask(String taskId) throws Exception {
		BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
		try {
			BmsAsynCalcuTaskEntity calcuTask = bmsAsynCalcuTaskRepositoryimpl
					.queryOne(taskId);
			if (calcuTask == null) {
				return null;
			}
			PropertyUtils.copyProperties(vo, calcuTask);
			return vo;
		} catch (Exception e) {
			logger.error("查询计算任务异常", e);
			throw new BizException("查询计算任务异常");
		}
	}

	@Override
	public List<BmsCalcuTaskVo> queryByMap(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = bmsAsynCalcuTaskRepositoryimpl
				.queryByMap(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}
	
	@Override
	public List<BmsCalcuTaskVo> queryPackageTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		
		List<BmsAsynCalcuTaskEntity> list = bizDispatchPackageRepository.queryPackageTask(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}
	
	@Override
    public List<BmsCalcuTaskVo> queryMaterialTask(Map<String, Object> condition) {
        // TODO Auto-generated method stub
        
        List<BmsAsynCalcuTaskEntity> list = bizOutstockPackmaterialRepository.queryTask(condition);
        List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
        if (list == null) {
            return null;
        }
        for (BmsAsynCalcuTaskEntity entity : list) {
            BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
            try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
                logger.error("转换失败");
            }
            voList.add(vo);
        }
        return voList;
    }

	@Override
	public List<BmsCalcuTaskVo> queryDispatchTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list = bizDispatchBillRepository.queryTask(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}
	@Override
	public List<BmsCalcuTaskVo> queryAddTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list = bizAddFeeRepository.queryTask(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;	}
	
	@Override
	public List<BmsCalcuTaskVo> queryDisByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list = bmsAsynCalcuTaskRepositoryimpl
				.queryDisByMap(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}

	public void validate(BmsCalcuTaskVo vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", vo.getCustomerId());
		map.put("subjectCode", vo.getSubjectCode());
		map.put("creMonth", vo.getCreMonth());
		List<BmsAsynCalcuTaskEntity> calList = bmsAsynCalcuTaskRepositoryimpl
				.query(map);
		// 状态0和10的新的直接丢弃，不处理
		for (BmsAsynCalcuTaskEntity entity : calList) {
			if (entity.getTaskStatus() == 0 || entity.getTaskStatus() == 10) {
				// 不处理，不发送mq
				return;
			}
		}
		// 状态20和30的将原来的置为丢弃40，新的插入
		// 40的老的不处理，新的插入
		List<BmsAsynCalcuTaskEntity> updateList = new ArrayList<BmsAsynCalcuTaskEntity>();
		for (BmsAsynCalcuTaskEntity entity : calList) {
			if (entity.getTaskStatus() == 20 || entity.getTaskStatus() == 30) {
				entity.setTaskStatus(40);
				updateList.add(entity);
			}
		}
		if (updateList.size() > 0) {
			// 更新历史的
			bmsAsynCalcuTaskRepositoryimpl.updateBatch(updateList);
		}
		sendMq(vo);
	}

	@Override
	public List<BmsCalcuTaskVo> query(Map<String, Object> condition) {
		List<BmsCalcuTaskVo> result = new ArrayList<>();
		try {
			List<BmsAsynCalcuTaskEntity> list = bmsAsynCalcuTaskRepositoryimpl
					.query(condition);
			if (null != list && list.size() > 0) {
				for (BmsAsynCalcuTaskEntity entity : list) {
					BmsCalcuTaskVo voEntity = new BmsCalcuTaskVo();
					PropertyUtils.copyProperties(voEntity, entity);
					result.add(voEntity);
				}
			}
		} catch (Exception e) {
			logger.error("查询计算任务异常", e);
		}
		return result;
	}

	@Override
	public PageInfo<BmsCalcuTaskVo> queryPage(Map<String, Object> condition,
			int pageNo, int pageSize) {
		PageInfo<BmsCalcuTaskVo> pageVoInfo = new PageInfo<BmsCalcuTaskVo>();
		List<BmsCalcuTaskVo> result = new ArrayList<>();
		try {
			PageInfo<BmsAsynCalcuTaskEntity> pageInfo = bmsAsynCalcuTaskRepositoryimpl
					.query(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			List<BmsAsynCalcuTaskEntity> list = pageInfo.getList();
			if (null != list && list.size() > 0) {
				for (BmsAsynCalcuTaskEntity entity : list) {
					BmsCalcuTaskVo voEntity = new BmsCalcuTaskVo();
					PropertyUtils.copyProperties(voEntity, entity);
					result.add(voEntity);
				}
			}
		} catch (Exception e) {
			logger.error("查询计算任务异常", e);
		}
		pageVoInfo.setList(result);
		return pageVoInfo;
	}

	@Override
	public List<BmsCalcuTaskVo> queryPalletTask(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = bizPalletInfoRepository
				.queryPalletTask(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}
	
	@Override
	public List<BmsCalcuTaskVo> queryOutstockTask(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = bizOutstockMasterRepository
				.queryOutstockTask(condition);
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public List<BmsCalcuTaskVo> queryProTask(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = bizProductStorageRepository.queryTask(condition);
		return trans(list);
	}
	
	@Override
	public List<BmsCalcuTaskVo> queryInsTask(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = bmsBizInstockInfoRepository.queryTask(condition);
		return trans(list);
	}
	
	private List<BmsCalcuTaskVo> trans(List<BmsAsynCalcuTaskEntity> list) {
		List<BmsCalcuTaskVo> voList = new ArrayList<BmsCalcuTaskVo>();
		if (list == null) {
			return null;
		}
		for (BmsAsynCalcuTaskEntity entity : list) {
			BmsCalcuTaskVo vo = new BmsCalcuTaskVo();
			try {
				PropertyUtils.copyProperties(vo, entity);
			} catch (Exception ex) {
				logger.error("转换失败");
			}
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 重算商家在某个月份下所有科目的费用
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor={BizException.class})
    @Override
    public String reCalculate(Map<String, Object> cond) {
        //耗材重算
        if (bizOutstockPackmaterialRepository.reCalculate(cond) == 0) {
            throw new BizException("托数重算异常!");
        }
        //出库重算
        if (bizOutstockMasterRepository.reCalculate(cond) == 0) {
            throw new BizException("出库单重算异常！");
        }
        //配送重算
        if (bizDispatchBillRepository.reCalculate(cond) == 0) {
            throw new BizException("配送费重算异常！");
        }
        //标准包装方案重算
        if (bizDispatchPackageRepository.reCalculate(cond) == 0) {
            throw new BizException("标准包装方案重算异常！");
        }
        //入库重算
        if (bmsBizInstockInfoRepository.reCalculate(cond) == 0) {
            throw new BizException("入库单重算异常！");
        }
        //商品按件重算
        if (bizProductStorageRepository.reCalculateForAll(cond) == 0) {
            throw new BizException("商品按件重算异常！");
        }
        //增值重算
        if (bizAddFeeRepository.retryCalcu(cond) == 0) {
            throw new BizException("增值费重算异常！");
        }
        //托数重算
        if (bizPalletInfoRepository.reCalculate(cond) == 0) {
            throw new BizException("托数重算异常！");
        }
        
        return "ok";
    }
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public List<BmsCalcuTaskVo> queryAllSubjectTask(Map<String, Object> param){
	    List<BmsCalcuTaskVo> allTasks = new ArrayList<BmsCalcuTaskVo>();
	    //查询耗材需要重算的任务
	    List<BmsCalcuTaskVo> materialTasks = queryMaterialTask(param);
	    //查询出库需要重算的任务
	    List<BmsCalcuTaskVo> outstockTasks = queryOutstockTask(param);
	    //查询配送需要重算的任务
	    List<BmsCalcuTaskVo> dispatchTasks = queryDispatchTask(param);
	    //查询包材需要重算的任务
	    List<BmsCalcuTaskVo> packageTasks = queryPackageTask(param);
	    //查询入库需要重算的任务
	    List<BmsCalcuTaskVo> instockTasks = queryInsTask(param);
	    //查询商品按件需要重算的任务
	    List<BmsCalcuTaskVo> productTasks = queryProTask(param);
	    //查询增值费需要重算的任务
	    List<BmsCalcuTaskVo> addTasks = queryAddTask(param);
	    //查询托数需要重算的任务
	    List<BmsCalcuTaskVo> palletTasks = queryPalletTask(param);
	    
	    allTasks.addAll(materialTasks);
	    allTasks.addAll(outstockTasks);
	    allTasks.addAll(dispatchTasks);
	    allTasks.addAll(packageTasks);
	    allTasks.addAll(instockTasks);
	    allTasks.addAll(productTasks);
	    allTasks.addAll(addTasks);
	    allTasks.addAll(palletTasks);
	    
	    return allTasks;
	}

	
	@Override
	public PageInfo<BmsCalcuTaskVo> queryTask(Map<String, Object> param,
			int pageNo, int pageSize) {
		PageInfo<BmsCalcuTaskVo> pageVoInfo = new PageInfo<BmsCalcuTaskVo>();
		try {
		PageInfo<BmsAsynCalcuTaskEntity> page= bmsAsynCalcuTaskRepositoryimpl.query(param, pageNo, pageSize);
		PropertyUtils.copyProperties(pageVoInfo, page);
		} catch (Exception e) {
			logger.info("查询任务列表错误:"+e);
		}
		
		return pageVoInfo;
	}
	
}