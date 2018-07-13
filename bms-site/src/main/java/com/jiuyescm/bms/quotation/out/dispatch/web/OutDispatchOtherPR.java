package com.jiuyescm.bms.quotation.out.dispatch.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.chargerule.payrule.service.IPayRuleService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherDetailEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherService;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherTemplateService;
import com.jiuyescm.bms.quotation.storage.web.StroageExtraQuoteController;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("outDispatchOtherPR")
public class OutDispatchOtherPR {
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	@Resource
	private IPriceOutDispatchOtherService priceOutDispatchService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IPriceOutDispatchOtherTemplateService priceOutDispatchTemplateService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private IAddressService addressService;
	
	@Resource
	private IPayRuleService payRuleService;
	
	@Resource 
	private IFeesCalcuService feesCalcuService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	
	private static final Logger logger = Logger.getLogger(StroageExtraQuoteController.class);

	
	/**
	 * 分页查询主模板
	 * 
	 * @param pagechi
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceOutDispatchOtherTemplateEntity> page, Map<String, Object> param) {
		/*param.put("typeCode", "DISPATCH_COMPANY");*/
		PageInfo<PriceOutDispatchOtherTemplateEntity> pageInfo = priceOutDispatchTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	@DataProvider
	public Map<String, String> getPubWareHouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	/**
	 * 查询所有的九曳配送模板信息
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryAll(Page<PriceOutDispatchOtherDetailEntity> page,Map<String,Object> parameter){
		PageInfo<PriceOutDispatchOtherDetailEntity> tmpPageInfo = priceOutDispatchService.queryAll(parameter,page.getPageNo(), page.getPageSize());		
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	
	}
	
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<PriceOutDispatchOtherDetailEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(PriceOutDispatchOtherDetailEntity temp:datas){
				//对操作类型进行判断
				//此为新增九曳配送报价模板
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					priceOutDispatchService.createPriceDistribution(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("新增配送其他报价模板对应关系,模板ID【"+temp.getTemplateId()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_out_dispatch_other_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.INSERT.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateId());
						model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:"+e.getMessage());
					}
					
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					//修改电商仓库
					priceOutDispatchService.updatePriceDistribution(temp);
					try{
						PubRecordLogEntity model=new PubRecordLogEntity();
						model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
						model.setNewData(JSON.toJSONString(temp));
						model.setOldData("");
						model.setOperateDesc("更新配送其他报价模板对应关系,模板ID【"+temp.getTemplateId()+"】");
						model.setOperatePerson(JAppContext.currentUserName());
						model.setOperateTable("price_out_dispatch_other_detail");
						model.setOperateTime(JAppContext.currentTimestamp());
						model.setOperateType(RecordLogOperateType.UPDATE.getCode());
						model.setRemark("");
						model.setOperateTableKey(temp.getTemplateId());
						model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
						pubRecordLogService.AddRecordLog(model);
					}catch(Exception e){
						logger.error("记录日志失败,失败原因:"+e.getMessage());
					}
				
				}
				
			}
			
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			
			return "数据库操作失败";
		}
		
	
	}
	
	/**
	 * 删除配送报价模板
	 */
	@DataResolver
	public String removePriceDistribution(PriceOutDispatchOtherDetailEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setDelFlag("1");
			p.setLastModifier(JAppContext.currentUserName());
			p.setLastModifyTime(JAppContext.currentTimestamp());
			priceOutDispatchService.removePriceDistribution(p);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(p));
				model.setOldData("");
				model.setOperateDesc("作废配送其他报价模板对应关系,模板ID【"+p.getTemplateId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_dispatch_other_detail");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(p.getTemplateId());
				model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "数据库操作成功";
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			
			return "数据库操作失败";
		}
			
	}
	
	
	
	/**
	 * 配送模板信息下载
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile getDispatchTemplate(Map<String, String> parameter) throws IOException {
			InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/dispatch/dispatch_template.xlsx");
			return new DownloadFile("配送模板信息导入模板.xlsx", is);
	}

	/**
	 * 复制新模板
	 */
	@DataResolver
	public String copyTemplate(PriceOutDispatchOtherTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			//先复制主模板
			String templateNo =sequenceService.getBillNoOne(PriceOutDispatchOtherTemplateEntity.class.getName(), "OMBZ", "00000");
			entity.setTemplateCode(templateNo);
			entity.setTemplateName(entity.getTemplateName());
			entity.setRemark(entity.getRemark());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceOutDispatchTemplateService.save(entity);
			
			//通过模板id查找对应的id
			Integer id=priceOutDispatchService.getId(templateNo);
			
			//复制子模板	
			List<PriceOutDispatchOtherDetailEntity> list=priceOutDispatchService.getDispatchById(entity.getId().toString());		
			for(int i=0;i<list.size();i++){
				PriceOutDispatchOtherDetailEntity p=list.get(i);
				p.setTemplateId(id.toString());
			}
			priceOutDispatchService.insertBatchTmp(list);

			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			
			return "数据库操作失败";
		}
		
	}
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DataResolver
	public Object calculate(BizTihuoBillEntity data){
		
		ReturnData returnResult = new ReturnData();	
		//通过宅配商id和费用科目查询计费规则
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("deliveryid", data.getDeliverid());
		ruleParam.put("subjectId","DISPATCH_OTHER_SUBJECT_TYPE_TIHUO");
		BillRulePayEntity ruleEntity=payRuleService.queryByDeliverId(ruleParam);
		if(ruleEntity==null){
			returnResult.setCode("SUCCESS");
			returnResult.setData("未查询到该商家的规则");
			return returnResult;
		}
		
		CalcuReqVo reqVo= new CalcuReqVo();
		List<PriceOutDispatchOtherDetailEntity> list=new ArrayList<>(data.getPriceList());
		reqVo.setQuoEntites(list);
		reqVo.setBizData(data);
		reqVo.setRuleNo(ruleEntity.getQuotationNo());
		reqVo.setRuleStr(ruleEntity.getRule());
		
		CalcuResultVo vo=feesCalcuService.FeesCalcuService(reqVo);				
		if("succ".equals(vo.getSuccess())){
			data.setAmount(vo.getPrice().doubleValue());				
		}	
		returnResult.setCode("SUCCESS");
		returnResult.setData(vo.getPrice()==null?"无匹配价格":vo.getPrice());

		return returnResult;
	}
	
	
}
