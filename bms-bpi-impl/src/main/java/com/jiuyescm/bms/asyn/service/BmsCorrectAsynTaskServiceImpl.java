package com.jiuyescm.bms.asyn.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCorrectAsynTaskVo;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsCorrectAsynTaskRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.sequence.api.ISequenceService;

@Service("bmsCorrectAsynTaskService")
public class BmsCorrectAsynTaskServiceImpl implements IBmsCorrectAsynTaskService {

    private static final Logger logger = Logger.getLogger(BmsCorrectAsynTaskServiceImpl.class.getName());

    private static final String BMS_CORRECT_WEIGHT_TASK = "BMS.CORRECT.WEIGHT.ASYN.TASK";
    private static final String BMS_CORRECT_MATERIAL_TASK = "BMS.CORRECT.MATERIAL.ASYN.TASK";

    @Autowired
    private IBmsCorrectAsynTaskRepository bmsCorrectAsynTaskRepository;

    @Autowired
    private JmsTemplate jmsQueueTemplate;

    @Autowired
    private ISequenceService sequenceService;

    @Autowired
    private IBmsGroupService bmsGroupService;
    @Resource
    private IBmsGroupCustomerService bmsGroupCustomerService;
    @Autowired
    private ISequenceService sequenceService1;
    @Autowired
    private IBizDispatchBillRepository bizDispatchBillRepository;

    @Override
    public PageInfo<BmsCorrectAsynTaskVo> query(Map<String, Object> condition, int pageNo, int pageSize)
            throws Exception {
        PageInfo<BmsCorrectAsynTaskVo> pageVoInfo = null;
        try {
            pageVoInfo = new PageInfo<BmsCorrectAsynTaskVo>();
            PageInfo<BmsCorrectAsynTaskEntity> pageInfo = bmsCorrectAsynTaskRepository.query(condition, pageNo,
                    pageSize);
            PropertyUtils.copyProperties(pageVoInfo, pageInfo);
            if (pageInfo != null && pageInfo.getList().size() > 0) {
                List<BmsCorrectAsynTaskVo> list = new ArrayList<BmsCorrectAsynTaskVo>();
                for (BmsCorrectAsynTaskEntity entity : pageInfo.getList()) {
                    BmsCorrectAsynTaskVo vo = new BmsCorrectAsynTaskVo();
                    PropertyUtils.copyProperties(vo, entity);
                    list.add(vo);
                }
                pageVoInfo.setList(list);
            }
        } catch (Exception e) {
            logger.error("query:", e);
            throw e;
        }
        return pageVoInfo;
    }

    @Override
    public BmsCorrectAsynTaskVo findById(int id) throws Exception {
        BmsCorrectAsynTaskVo vo = null;
        try {
            BmsCorrectAsynTaskEntity entity = bmsCorrectAsynTaskRepository.findById(id);
            vo = new BmsCorrectAsynTaskVo();
            PropertyUtils.copyProperties(vo, entity);
        } catch (Exception e) {
            logger.error("findById:", e);
            throw e;
        }
        return vo;
    }

    @Override
    public int save(BmsCorrectAsynTaskVo vo) throws Exception {
        try {
            BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
            PropertyUtils.copyProperties(entity, vo);
            return bmsCorrectAsynTaskRepository.save(entity);
        } catch (Exception e) {
            logger.error("save:", e);
            throw e;
        }
    }

    @Override
    public int update(BmsCorrectAsynTaskVo vo) throws Exception {
        try {
            vo.setFinishTime(JAppContext.currentTimestamp());
            BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
            PropertyUtils.copyProperties(entity, vo);
            return bmsCorrectAsynTaskRepository.update(entity);
        } catch (Exception e) {
            logger.error("update:", e);
            throw e;
        }
    }

    @Override
    public List<String> queryCorrectCustomerList(Map<String, Object> conditionMap) {

        return bmsCorrectAsynTaskRepository.queryCorrectCustomerList(conditionMap);
    }

    @Override
    public boolean existTask(BmsCorrectAsynTaskVo voEntity) throws Exception {
        try {
            BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
            PropertyUtils.copyProperties(entity, voEntity);
            return bmsCorrectAsynTaskRepository.existTask(entity);
        } catch (Exception e) {
            logger.error("existTask:", e);
            throw e;
        }
    }

    @Override
    public int saveBatch(List<BmsCorrectAsynTaskVo> voList) throws Exception {
        try {
            List<BmsCorrectAsynTaskEntity> list = new ArrayList<BmsCorrectAsynTaskEntity>();
            for (BmsCorrectAsynTaskVo voEntity : voList) {
                BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
                PropertyUtils.copyProperties(entity, voEntity);
                list.add(entity);
            }
            return bmsCorrectAsynTaskRepository.saveBatch(list);
        } catch (Exception e) {
            logger.error("saveBatch:", e);
            throw e;
        }
    }

    @Override
    public List<BmsCorrectAsynTaskVo> queryList(Map<String, Object> condition) throws Exception {
        // TODO Auto-generated method stub
        List<BmsCorrectAsynTaskEntity> list = bmsCorrectAsynTaskRepository.queryList(condition);
        List<BmsCorrectAsynTaskVo> voList = new ArrayList<BmsCorrectAsynTaskVo>();
        for (BmsCorrectAsynTaskEntity entity : list) {
            BmsCorrectAsynTaskVo vo = new BmsCorrectAsynTaskVo();
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
    public String updateCorrect(BmsCorrectAsynTaskVo vo) throws Exception {
        // 发送MQ消息类型
        String task = "";
        logger.info("之前的taskId:" + vo.getTaskId());
        if (StringUtils.isBlank(vo.getTaskId()))
            return "任务ID为空";
        Map<String, Object> queryConfition = new HashMap<>();
        queryConfition.put("taskId", vo.getTaskId());
        List<BmsCorrectAsynTaskEntity> list = bmsCorrectAsynTaskRepository.queryList(queryConfition);
        if (CollectionUtils.isEmpty(list))
            return "没有查询到此任务";
        String bizType = list.get(0).getBizType();
        if ("weight_correct".equals(bizType)) {
            task += BMS_CORRECT_WEIGHT_TASK;
        } else if ("material_correct".equals(bizType)) {
            task += BMS_CORRECT_MATERIAL_TASK;
        } else {
            return "此任务不是纠正任务";
        }
        String taskStatus = list.get(0).getTaskStatus();
        if (!"SUCCESS".equals(taskStatus) && !"EXCEPTION".equals(taskStatus) && !"FAIL".equals(taskStatus))
            return "此纠正任务的状态不能纠正";
        // 发送MQ消息纠正
        // String taskId = snowflakeSequenceService.nextStringId();
        String id = String.valueOf(sequenceService.nextSeq("BMS.CORRECT"));
        String taskId = "CT";
        for (int i = 1; i <= 10 - id.length(); i++) {
            taskId += "0";
        }
        taskId += id;
        // 更新任务表（更新修改人，修改时间）
        try {
            BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
            entity.setId(vo.getId());
            entity.setTaskId(taskId);
            entity.setLastModifier(vo.getLastModifier());
            entity.setLastModifyTime(vo.getLastModifyTime());
            entity.setTaskStatus("PROCESS");
            bmsCorrectAsynTaskRepository.update(entity);
        } catch (Exception e) {
            logger.error("updateCorrect:", e);
            return "更新失败";
        }

        try {
            final String msg = taskId;
            logger.info("生成最新的taskId:" + taskId);
            jmsQueueTemplate.send(task, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(msg);
                }
            });
        } catch (Exception e) {
            logger.error("send MQ:", e);
            return "MQ发送失败！";
        }
        return "纠正成功";
    }

    @Override
    public String saveCorrect(BmsCorrectAsynTaskVo vo) {
        String customerId = vo.getCustomerId();
        if(StringUtils.isBlank(customerId)){
            return "商家不能为空";
        }
        String year = vo.getYear();
        Integer yearInteger = Integer.valueOf(year);
        if(null==yearInteger){
            return "年不能为空";
        }
        String month = vo.getMonth();
        Integer monthInteger = Integer.valueOf(month);
        if(null==monthInteger){
            return "月份不能为空";
        }
        String creator = vo.getCreator();
        // 查询任务是否存在
        List<BmsCorrectAsynTaskEntity> list = null;
        Map<String, Object> queryCondition = new HashMap<>();
        StringBuilder creMonthStringBuilder = new StringBuilder(year);
        String monthWithZero = null;
        if (monthInteger < 10) {
            monthWithZero = "0" + vo.getMonth();
        } else {
            monthWithZero = vo.getMonth();
        }
        creMonthStringBuilder.append(monthWithZero);
        String creMonth = creMonthStringBuilder.toString();
        queryCondition.put("createMonth", creMonth);
        queryCondition.put("customerId", customerId);
        try {
            list = bmsCorrectAsynTaskRepository.queryList(queryCondition);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return "创建失败：当前商家在当前月份已存在纠正任务";
        }

        // 查询不需要运单纠正的商家
        List<String> notCorCustList = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupCode", "notpartin_orderCorrent_customer");
        map.put("bizType", "group_customer");
        BmsGroupVo bmsCancelCus = bmsGroupService.queryOne(map);
        if (null != bmsCancelCus) {
            notCorCustList = bmsGroupCustomerService.queryCustomerByGroupId(bmsCancelCus.getId());
        }
        if (CollectionUtils.isNotEmpty(notCorCustList) && notCorCustList.contains(customerId)) {
            return "创建失败：当前商家为不需要运单纠正的商家";
        }
        // 创建任务
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearInteger);
        cal.set(Calendar.MONTH, monthInteger-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();
        cal.roll(Calendar.DATE, -1);
        Date endDate = cal.getTime();
        
        //查询此商家此月份是否存在业务
        Map<String, Object> bizParam = new HashMap<>();
        bizParam.put("customerid", customerId);
        bizParam.put("startTime", startDate);
        bizParam.put("endTime", endDate);
        List<BizDispatchBillEntity> entityList=  bizDispatchBillRepository.queryBizCustomerid(bizParam);
        if(CollectionUtils.isEmpty(entityList)){
            return "创建失败：当前商家当前月份不存在业务";
        }
        
        Timestamp createTime = JAppContext.currentTimestamp();
        BmsCorrectAsynTaskEntity entity1 = createEntity(creMonth, createTime, startDate, endDate, customerId,
                "weight_correct", creator);
        BmsCorrectAsynTaskEntity entity2 = createEntity(creMonth, createTime, startDate, endDate, customerId,
                "material_correct", creator);
        List<BmsCorrectAsynTaskEntity> listEntities = new ArrayList<>();
        listEntities.add(entity1);
        listEntities.add(entity2);
        bmsCorrectAsynTaskRepository.saveBatch(listEntities);
        for (BmsCorrectAsynTaskEntity entity : listEntities) {
            try {
                final String msg = entity.getTaskId();
                String task = "";
                if("weight_correct".equals(entity.getBizType())){
                    //发送重量调整MQ
                    task = BMS_CORRECT_WEIGHT_TASK;
                }else if("material_correct".equals(entity.getBizType())) {
                    //发送耗材调整MQ
                    task = BMS_CORRECT_MATERIAL_TASK;
                }
                jmsQueueTemplate.send(task, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(msg);
                    }
                });
            } catch (Exception e) {
                logger.info("send MQ fail:", e);
                return "MQ发送失败！";
            }
        }
        return "纠正成功";
    }

    private BmsCorrectAsynTaskEntity createEntity(String createMonth, Timestamp createTime, Date startDate,
            Date endDate, String customerid, String bizType, String creator) {
        BmsCorrectAsynTaskEntity entity = new BmsCorrectAsynTaskEntity();
        entity.setCreator(creator);
        entity.setCreateTime(createTime);
        entity.setDelFlag("0");
        entity.setTaskRate(0);
        entity.setTaskStatus("WAIT");
        entity.setCreateMonth(createMonth);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setCustomerId(customerid);
        entity.setBizType(bizType);
        String id1 = String.valueOf(sequenceService1.nextSeq("BMS.CORRECT"));
        String taskId1 = "CT";
        for (int j = 1; j <= 10 - id1.length(); j++) {
            taskId1 += "0";
        }
        taskId1 += id1;
        entity.setTaskId(taskId1);
        return entity;
    }

}
