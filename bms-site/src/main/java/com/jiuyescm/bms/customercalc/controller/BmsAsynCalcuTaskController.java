package com.jiuyescm.bms.customercalc.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.service.IAddFeeService;
import com.jiuyescm.bms.biz.storage.vo.BizAddFeeVo;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsAsynCalcuTaskController")
public class BmsAsynCalcuTaskController {

	private static final Logger logger = LoggerFactory.getLogger(BmsAsynCalcuTaskController.class.getName());

	@Autowired
	private IBmsCalcuTaskService bmsAsynCalcuTaskService;
	@Autowired
	private ISystemCodeService systemCodeService;
	@Autowired
	private IAddFeeService addFeeService;

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @throws Exception 
	 */
	@DataProvider
	public void query(Page<BmsCalcuTaskVo> page, Map<String, Object> param) throws Exception {
		try {
			String crePersonId = ContextHolder.getLoginUser().getUsername();
			param.put("crePersonId", crePersonId);
			PageInfo<BmsCalcuTaskVo> pageInfo = bmsAsynCalcuTaskService.query(param, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}
	
	@DataProvider
	public List<BmsCalcuTaskVo> queryDetail(Map<String, Object> param) throws Exception {
		List<BmsCalcuTaskVo> list = null;
		try {
			list = bmsAsynCalcuTaskService.queryDetail(param);
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
		return list;
	}
	
	@Expose
	public Map<String, String> findUrlBySubjectCode(String code){
		Map<String, String> map = new HashMap<String, String>();
		SystemCodeEntity sysEntity = systemCodeService.getSystemCode("BIZ_URL", code);
		if (null == sysEntity) {
			throw new BizException("请先去数据字典配置对应的路径！");
		}
		map.put("name", sysEntity.getCodeName());
		map.put("url", sysEntity.getExtattr1());
		map.put("icon",sysEntity.getExtattr2());
		return map;
	}
	
	/**
	 * 对商家下所有费用重算
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年5月29日 下午4:43:02
	 *
	 * @param taskVo
	 * @return
	 * @throws ParseException 
	 */
	@Expose
	public String reCalculate(BmsCalcuTaskVo taskVo) throws ParseException{
	    if (null == taskVo) {
	        throw new BizException("参数不能为空，请选择一条数据！");
        }
	    Integer creMonth = taskVo.getCreMonth();
	    String startTime = creMonth.toString().substring(0, 4) + "-" + creMonth.toString().substring(4, 6) + "-" + "01";
	    String endTime = DateUtil.getLastDay(startTime);
	    
	    //各科目参数组装
	    Map<String, Object> cond = new HashMap<String, Object>();
	    cond.put("customerId", taskVo.getCustomerId());
	    cond.put("customerid", taskVo.getCustomerId());
	    cond.put("merchantId", taskVo.getCustomerId());
	    cond.put("createTime", Timestamp.valueOf(startTime + " 00:00:00"));
	    cond.put("createEndTime", Timestamp.valueOf(endTime + " 23:59:59"));
	    cond.put("creTime", Timestamp.valueOf(startTime + " 00:00:00"));
	    cond.put("creEndTime", Timestamp.valueOf(endTime + " 23:59:59"));
	    cond.put("startTime", Timestamp.valueOf(startTime + " 00:00:00"));
	    cond.put("endTime", Timestamp.valueOf(endTime + " 23:59:59"));
	    cond.put("isCalculate", "99");
	    //重算所有科目
	    String result = bmsAsynCalcuTaskService.reCalculate(cond);
	    if (!"ok".equals(result)) {
            return result;
        }else {
            //汇总商家该月份下所有科目需要发送的任务
	        List<BmsCalcuTaskVo> taskVos = bmsAsynCalcuTaskService.queryAllSubjectTask(cond);
            for (BmsCalcuTaskVo calcuTaskVo : taskVos) {
                calcuTaskVo.setCrePerson(ContextHolder.getLoginUser().getCname());
                calcuTaskVo.setCrePersonId(ContextHolder.getLoginUserName());
                try {
                    bmsAsynCalcuTaskService.sendTask(calcuTaskVo);
                    logger.info("mq发送成功,商家id:"+calcuTaskVo.getCustomerId()+",年月:"+calcuTaskVo.getCreMonth()+",科目id:"+calcuTaskVo.getSubjectCode());
                } catch (Exception e) {
                    logger.error("mq发送失败:", e);
                }
            }
        }
	    return "操作成功! 正在重算...";
	}
	
	@Expose
	public void test(){
	    List<BizAddFeeVo> bizAddFeeVoList = new ArrayList<BizAddFeeVo>();
        BizAddFeeVo bizAddFeeVo = new BizAddFeeVo();
        bizAddFeeVo.setPayNo("FBB01000000069");               //增值单编号
        bizAddFeeVo.setExternalNo("Z120000213359");    //外部订单号
        bizAddFeeVo.setCreateTime(Timestamp.valueOf("2019-05-12 14:56:16"));     //业务时间
        bizAddFeeVo.setOperationTime(Timestamp.valueOf("2019-05-01 14:53:12"));//操作时间
        bizAddFeeVo.setWarehouseCode("B01");    //仓库号
        bizAddFeeVo.setWarehouseName("北京01仓");
        bizAddFeeVo.setCustomerid("1100002217");
        bizAddFeeVo.setCustomerName("北京魁星恒信商贸有限公司");
        bizAddFeeVo.setFeesUnit("个");
        bizAddFeeVo.setNum(1500d);
        bizAddFeeVo.setFixedAmount(0d);
        bizAddFeeVo.setFirstSubject("100001");
        bizAddFeeVo.setFirstSubjectName("全检费");
        bizAddFeeVo.setFeesType("wh_check_qty");
        bizAddFeeVo.setFeesTypeName("全检费-产品数量检查");
        bizAddFeeVo.setRemark(null);
        bizAddFeeVo.setServiceContent("到货全检");
        bizAddFeeVoList.add(bizAddFeeVo);
        addFeeService.save(bizAddFeeVoList);
	}
	
}
