package com.jiuyescm.bms.asyn.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.discount.repository.IBmsDiscountAsynTaskRepository;
import com.jiuyescm.bms.common.enumtype.BmsCorrectAsynTaskStatusEnum;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDiscountRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.contract.quote.api.IContractDiscountService;
import com.jiuyescm.contract.quote.vo.CarrierInfoVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountQueryVo;
import com.jiuyescm.contract.quote.vo.ContractDiscountVo;
import com.jiuyescm.contract.quote.vo.SubjectInfoVo;
import com.jiuyescm.utils.JsonUtils;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsDiscountAsynTaskService")
public class BmsDiscountAsynTaskServiceImpl implements IBmsDiscountAsynTaskService {
	
	private static final Logger logger = Logger.getLogger(BmsDiscountAsynTaskServiceImpl.class.getName());
	
	@Autowired
    private IBmsDiscountAsynTaskRepository bmsDiscountAsynTaskRepository;
	
    @Resource
    private IContractDiscountService contractDiscountService;
    
    @Autowired 
    ICustomerDictService customerDictService;
    
    @Resource
    private JmsTemplate jmsQueueTemplate;
    
    @Resource
    private ISystemCodeRepository systemCodeRepository;
    
    @Resource
    private IPriceContractDiscountRepository priceContractDiscountRepository;
    
    private static final String BMS_DISCOUNT_ASYN_TASK = "BMS.DISCOUNT.ASYN.TASK";
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsDiscountAsynTaskEntity findById(Long id) {
        return bmsDiscountAsynTaskRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsDiscountAsynTaskRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition){
		return bmsDiscountAsynTaskRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity) {
        return bmsDiscountAsynTaskRepository.save(entity);
    }
    
    /**
     * 批量保存
     * @param voList
     * @return
     * @throws Exception
     */
	@Override
	public int saveBatch(List<BmsDiscountAsynTaskEntity> voList) throws Exception {
		try{
			List<BmsDiscountAsynTaskEntity> list=new ArrayList<BmsDiscountAsynTaskEntity>();
			for(BmsDiscountAsynTaskEntity voEntity:voList){
				BmsDiscountAsynTaskEntity entity=new BmsDiscountAsynTaskEntity();
				PropertyUtils.copyProperties(entity,voEntity);
				list.add(entity);
			}
			return bmsDiscountAsynTaskRepository.saveBatch(list);
		}catch(Exception e){
			logger.error("saveBatch:",e);
			throw e;
		}
	}

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity) {
        return bmsDiscountAsynTaskRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        bmsDiscountAsynTaskRepository.delete(id);
    }

	@Override
	public BmsDiscountAsynTaskEntity queryTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsDiscountAsynTaskRepository.queryTask(condition);
	}
	
	@Override
	public boolean existTask(BmsDiscountAsynTaskEntity voEntity) throws Exception {
		try{
			BmsDiscountAsynTaskEntity entity=new BmsDiscountAsynTaskEntity();
			PropertyUtils.copyProperties(entity,voEntity);
			return bmsDiscountAsynTaskRepository.existTask(entity);
		}catch(Exception e){
			logger.error("existTask:",e);
			throw e;
		}
	}

    @Override
    public int sendTask(Map<String, Object> map) {
        // TODO Auto-generated method stub
        if(map==null || map.get("customerid")==null || map.get("createMonth")==null || map.get("taskId")==null){
            return 0;
        }
        
        List<BmsDiscountAsynTaskEntity> bdatList = new ArrayList<>();
        
        //商家id
        String customerId=map.get("customerid").toString();
        //时间格式 201903
        String createMonth=map.get("createMonth").toString();
        //任务id
        String taskId=map.get("taskId").toString();
        //创建时间
        PubCustomerVo customer = customerDictService.queryById(customerId);
        if(customer.getContractAttr()==2){
            try {
                ContractDiscountQueryVo queryVo=new ContractDiscountQueryVo();
                queryVo.setCustomerId(customerId);
                queryVo.setSettlementTime(createMonth);
                logger.info("查询合同在线折扣参数"+JSONObject.fromObject(queryVo));
                List<ContractDiscountVo> disCountVo=contractDiscountService.querySubject(queryVo);
                logger.info("查询合同在线折扣结果"+JSONArray.fromObject(disCountVo));
                
                if(disCountVo.size()>0){
                    for(ContractDiscountVo vo:disCountVo){
                        if(vo.getSubjectVoList().size()>0){
                            for(SubjectInfoVo s:vo.getSubjectVoList()){
                                BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
                                String startDateStr =createMonth + "-01 00:00:00";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date startDate = sdf.parse(startDateStr);
                                Date endDate = DateUtils.addMonths(startDate, 1);
                                Timestamp startTime = new Timestamp(startDate.getTime());
                                Timestamp endTime = new Timestamp(endDate.getTime());
                                newEntity.setStartDate(startTime);
                                newEntity.setEndDate(endTime);                
                                // 生成任务，写入任务表
                                newEntity.setTaskId(taskId);
                                newEntity.setCreateMonth(createMonth);
                                newEntity.setTaskRate(0);
                                newEntity.setDelFlag("0");
                                newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
                                newEntity.setCreator(JAppContext.currentUserName());
                                newEntity.setCreateTime(JAppContext.currentTimestamp());
                                newEntity.setBizTypecode("STORAGE");
                                newEntity.setCustomerId(vo.getCustomerId());
                                newEntity.setSubjectCode(s.getSubjectId());
                                newEntity.setDiscountType(s.getDiscountType());
                                newEntity.setCustomerType("contract");
                                bdatList.add(newEntity);                     
                            }           
                        }
                        if(vo.getCarrierVoList().size()>0){
                            for(CarrierInfoVo s:vo.getCarrierVoList()){
                                BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();
                                String startDateStr = createMonth + "-01 00:00:00";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date startDate = sdf.parse(startDateStr);
                                Date endDate = DateUtils.addMonths(startDate, 1);
                                Timestamp startTime = new Timestamp(startDate.getTime());
                                Timestamp endTime = new Timestamp(endDate.getTime());
                                newEntity.setStartDate(startTime);
                                newEntity.setEndDate(endTime);
                                // 生成任务，写入任务表
                                newEntity.setTaskId(taskId);
                                newEntity.setCreateMonth(createMonth);
                                newEntity.setTaskRate(0);
                                newEntity.setDelFlag("0");
                                newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
                                newEntity.setCreator(JAppContext.currentUserName());
                                newEntity.setCreateTime(JAppContext.currentTimestamp());
                                newEntity.setBizTypecode("DISPATCH");
                                newEntity.setCustomerId(vo.getCustomerId());                
                                SystemCodeEntity sys=(SystemCodeEntity) getDispatchMap().get(s.getCarrierId());
                                newEntity.setCarrierId(s.getCarrierId());   
                                newEntity.setDiscountType(s.getDiscountType());
                                newEntity.setCustomerType("contract");
                                if (null != sys) {
                                    newEntity.setSubjectCode(sys.getCode());
                                    bdatList.add(newEntity); 
                                }   
                            }           
                        }           

                    }
                }
            } catch (Exception e) {
                logger.error("查询合同在线折扣失败", e);
                // TODO: handle exception
                return 0;
            }
        }
        else{
            try {
                BmsDiscountAsynTaskEntity entity = new BmsDiscountAsynTaskEntity();
                String startD = createMonth + "-01 00:00:00";
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDa = sd.parse(startD);
                Timestamp starttime = new Timestamp(startDa.getTime());
                entity.setStartDate(starttime);
                entity.setCustomerId(customerId);
                
                List<PriceContractDiscountItemEntity> cusList = priceContractDiscountRepository.queryByCustomerId(entity);
                if(cusList.isEmpty()){
                    logger.info("BMS未查询到商家折扣报价或商家合同过期");
                    return 0;
                }
                
                
                for (PriceContractDiscountItemEntity bizEntity : cusList) {
                    BmsDiscountAsynTaskEntity newEntity = new BmsDiscountAsynTaskEntity();          
                    String startDateStr = createMonth + "-01 00:00:00";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date startDate = sdf.parse(startDateStr);
                    Date endDate = DateUtils.addMonths(startDate, 1);
                    Timestamp startTime = new Timestamp(startDate.getTime());
                    Timestamp endTime = new Timestamp(endDate.getTime());
                    newEntity.setStartDate(startTime);
                    newEntity.setEndDate(endTime); 
                    newEntity.setTaskId(taskId);
                    newEntity.setCarrierId(bizEntity.getCarrierId());
                    newEntity.setCreateMonth(createMonth);
                    newEntity.setTaskRate(0);
                    newEntity.setDelFlag("0");
                    newEntity.setTaskStatus(BmsCorrectAsynTaskStatusEnum.WAIT.getCode());
                    newEntity.setCreator(JAppContext.currentUserName());
                    newEntity.setCreateTime(JAppContext.currentTimestamp());
                    newEntity.setBizTypecode(bizEntity.getBizTypeCode());
                    newEntity.setCustomerId(customerId);
                    
                    newEntity.setSubjectCode(bizEntity.getSubjectId());
                    newEntity.setCustomerType("bms");
                    bdatList.add(newEntity);
                } 
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("BMS未查询到商家折扣异常",e);
                return 0;
            }
            
        }
        if (bdatList.size() > 0) {
            bmsDiscountAsynTaskRepository.saveBatch(bdatList);
            for (BmsDiscountAsynTaskEntity bmsDiscountAsynTaskEntity : bdatList) {
                try {           
                    logger.info("开始发送MQ");
                    Map<String,Object> condition=new HashMap<>();
                    condition.put("taskId", bmsDiscountAsynTaskEntity.getTaskId());
                    final String msg = JsonUtils.toJson(condition);
                    jmsQueueTemplate.send(BMS_DISCOUNT_ASYN_TASK, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(msg);
                        }
                    });
                    logger.info("MQ发送成功");
                } catch (Exception e) {
                    logger.error("send MQ:", e);
                }
            }      
        }

        return 1;
    }
	
    
    public Map<String,Object> getDispatchMap(){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("typeCode", "DISPATCH_COMPANY");
        List<SystemCodeEntity> scList = systemCodeRepository.queryCodeList(map);
        map.clear();
        for(SystemCodeEntity s:scList){
            map.put(s.getExtattr1(), s);
        }
        return map;
    }
}
