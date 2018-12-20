package com.jiuyescm.bms.fees.out.transport.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.web.HttpCommonImport;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportImportEntity;
import com.jiuyescm.bms.fees.out.transport.service.IFeesPayTransportService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.TransportPayFeesDataType;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.forwarder.api.IForwarderService;
import com.jiuyescm.mdm.forwarder.vo.ForwarderVo;
/**
 * 运输应付费用导入 继承HttpCommonImport基类  实现方法
 * @author wuliangfeng
 *
 */
@Controller("feesPayTransportImportController")
public class FeesPayTransportImportController extends HttpCommonImport<FeesPayTransportImportEntity,FeesPayTransportEntity> {

	@Resource
	private IFeesPayTransportService feesPayTransportService;
	
	@Autowired 
	private ICustomerService customerService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IForwarderService forwarderService;
	
	@Resource
	private SequenceService sequenceService;
	
	@Resource
	private IProjectService projectService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Override
	protected BaseDataType getBaseDataType() {
		return new TransportPayFeesDataType();
	}

	private boolean setvalidateCondition(List<FeesPayTransportImportEntity> importList,List<String> customerNameList,List<String> forwarderNameList,List<ErrorMessageVo> infoList){
		List<String> waybillNos=new ArrayList<String>();
		int index=0;
		for(FeesPayTransportImportEntity importEntity:importList){
			index++;
			if(!customerNameList.contains(importEntity.getCustomername())){
				customerNameList.add(importEntity.getCustomername());
			}
			if(!forwarderNameList.contains(importEntity.getForwardername())){
				forwarderNameList.add(importEntity.getForwardername());
			}
			if(!waybillNos.contains(importEntity.getWaybillno())){
				waybillNos.add(importEntity.getWaybillno());
			}else{
				ErrorMessageVo vo=new ErrorMessageVo();
				vo.setLineNo(index);
				vo.setMsg("导入运单号【"+importEntity.getWaybillno()+"】重复");
				infoList.add(vo);
				return false;
			}
		}
		return true;
	}
	@Override
	protected List<FeesPayTransportEntity> validateImportList(
			List<FeesPayTransportImportEntity> importList,
			List<ErrorMessageVo> infoList) {
		ErrorMessageVo vo=null;
		int index=0;
		List<FeesPayTransportEntity> list=new ArrayList<FeesPayTransportEntity>();
		List<String> customerNameList=new ArrayList<String>();
		List<String> forwarderNameList=new ArrayList<String>();
		if(!setvalidateCondition(importList,customerNameList,forwarderNameList,infoList)){
			return list;
		}
		List<ForwarderVo> forwardervoList=forwarderService.queryListByNames(forwarderNameList);
		List<CustomerVo> customervoList=customerService.queryByCustomerName(customerNameList);
		List<SystemCodeEntity> systemCodelist=systemCodeService.findEnumList("MOTORCYCLE_TYPE");
		for(FeesPayTransportImportEntity entity:importList){
			index++;
			vo=new ErrorMessageVo();
			vo.setLineNo(index);
			vo.setMsg("");
			if(validateEntity(entity,vo,systemCodelist)){//检验 必填项
				List<FeesPayTransportEntity> importDatalist=new ArrayList<FeesPayTransportEntity>();
				if(validataData(entity,importDatalist,vo,systemCodelist)){//检验数据是否重复 
					//检验商家  物流商 设置物流商 宅配商ID
					if(!validataCustomer(entity,vo,customervoList,importDatalist)||
							!validateForwarder(entity,vo,forwardervoList,importDatalist)){
						infoList.add(vo);
					}else{
						list.addAll(importDatalist);
					}
				}else{
					infoList.add(vo);
				}
			}else{
				infoList.add(vo);
			}
		}
		return list;
	}
	private Map<String,String> getSubjectMap(){
		
		Map<String,String> map=new HashMap<String,String>();
		List<SystemCodeEntity> systemCodelist=systemCodeService.findEnumList("ts_value_add_subject");//增值服务费
		for(SystemCodeEntity codeEntity:systemCodelist){
			map.put(codeEntity.getCode(), codeEntity.getCodeName());
		}
		systemCodelist=systemCodeService.findEnumList("ts_base_subject");//运费
		for(SystemCodeEntity codeEntity:systemCodelist){
			map.put(codeEntity.getCode(), codeEntity.getCodeName());
		}
		return map;
	}
	private boolean validataData(FeesPayTransportImportEntity importEntity,List<FeesPayTransportEntity> importDatalist,ErrorMessageVo vo,
			List<SystemCodeEntity> systemCodelist){
		if(changeToDataList(importEntity,importDatalist,vo,systemCodelist)){//转换成功 
			List<FeesPayTransportEntity> queryList=feesPayTransportService.queryByWaybillNo(importEntity.getWaybillno());
			String mes="";
			if(queryList==null||queryList.size()==0){
				return true;
			}
			Map<String,String> systemMap=getSubjectMap();
			for(FeesPayTransportEntity importDataEntity:importDatalist){
				for(FeesPayTransportEntity queryEntity:queryList){
					if(importDataEntity.getWaybillNo().equals(queryEntity.getWaybillNo())
							&&importDataEntity.getSubjectCode().equals(queryEntity.getSubjectCode())&&
							importDataEntity.getOtherSubjectCode().equals(queryEntity.getOtherSubjectCode()))
					{
						mes+=systemMap.get(importDataEntity.getOtherSubjectCode())+"已存在;";
						break;
					}
				}
			}
			if(!StringUtils.isBlank(mes)){
				mes="运单号【"+importEntity.getWaybillno()+"】"+mes;
				vo.setMsg(mes);
				return false;
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	private FeesPayTransportEntity getEntity(FeesPayTransportImportEntity importEntity,ErrorMessageVo vo,List<SystemCodeEntity> systemCodelist){
		FeesPayTransportEntity entity=new FeesPayTransportEntity();
		entity.setOrderno(importEntity.getOrderno());
		entity.setWaybillNo(importEntity.getWaybillno());
		entity.setCustomerName(importEntity.getCustomername());
		if(!StringUtils.isBlank(importEntity.getOrgaddress())){
			String[] orgAddress=importEntity.getOrgaddress().split("/");
			if(orgAddress.length==2){
				entity.setOriginatingcity(orgAddress[0]);
				entity.setOriginatingdistrict(orgAddress[1]);
			}
		}
		if(!StringUtils.isBlank(importEntity.getTargetadress())){
			String[] trgAddress=importEntity.getTargetadress().split("/");
			if(trgAddress.length==2){
				entity.setTargetcity(trgAddress[0]);
				entity.setTargetdistrict(trgAddress[1]);
			}
		}
		entity.setOrgaddress(importEntity.getOrgaddress());
		entity.setTargetaddress(importEntity.getTargetadress());
		entity.setProductdetails(importEntity.getProductdetails());
		entity.setWeight(importEntity.getWeight());
		entity.setVolume(importEntity.getVolume());
		entity.setCarmodel(importEntity.getCarmodel());
		entity.setQuantity(importEntity.getQuantity());
		entity.setAccepttime(importEntity.getAccepttime());
		entity.setSigntime(importEntity.getSigntime());
		entity.setCretime(importEntity.getSigntime());
		entity.setCrepersonname(JAppContext.currentUserName());
		entity.setCreperson(JAppContext.currentUserID());
		entity.setForwarderName(importEntity.getForwardername());
		entity.setDistributiontype(importEntity.getDistributiontype());
		entity.setOperationtime(JAppContext.currentTimestamp());
		if(!StringUtils.isBlank(importEntity.getCarmodel())){
			for(SystemCodeEntity code:systemCodelist){
				if(code.getCodeName().equals(entity.getCarmodel())){
					entity.setCarmodel(code.getCode());//设置为编码
					break;
				}
			}
		}
		return entity;
	}
	//转换实体
	private boolean changeToDataList(FeesPayTransportImportEntity importEntity,List<FeesPayTransportEntity> list,ErrorMessageVo vo,List<SystemCodeEntity> systemCodelist){
		String mes="";
		//验证提货费
		if(!StringUtils.isBlank(importEntity.getPickupcharge())){
			try{
				FeesPayTransportEntity entity=getEntity(importEntity,vo,systemCodelist);
				double pickupcharge=Double.valueOf(importEntity.getPickupcharge());
				entity.setTotleprice(pickupcharge);
				entity.setSubjectCode("ts_value_add_subject");//增值服务费
				entity.setOtherSubjectCode("ts_takes");//提货费
				String templateNo =sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YFFY", "0000000000");
				entity.setFeesNo(templateNo);
				list.add(entity);
			}catch(Exception e){
				mes+="提货费【"+importEntity.getPickupcharge()+"】格式不正确!";
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "提货费【"+importEntity.getPickupcharge()+"】格式不正确!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				
			}	
		}
		if(!StringUtils.isBlank(importEntity.getFreight())){
			try{
				FeesPayTransportEntity entity=getEntity(importEntity,vo,systemCodelist);
				double freight=Double.valueOf(importEntity.getFreight());
				entity.setTotleprice(freight);
				entity.setSubjectCode("ts_base_subject");//运费
				entity.setOtherSubjectCode("ts_trans_amount");//运费
				String templateNo =sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YFFY", "0000000000");
				entity.setFeesNo(templateNo);
				list.add(entity);
			}catch(Exception e){
				mes+="运费【"+importEntity.getFreight()+"】格式不正确!";
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "运费【"+importEntity.getFreight()+"】格式不正确!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}	
		}
		if(!StringUtils.isBlank(importEntity.getDeliverycharges())){
			try{
				FeesPayTransportEntity entity=getEntity(importEntity,vo,systemCodelist);
				double delivercharges=Double.valueOf(importEntity.getDeliverycharges());
				entity.setTotleprice(delivercharges);
				entity.setSubjectCode("ts_value_add_subject");//送货费
				entity.setOtherSubjectCode("ts_send");//送货费
				String templateNo =sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YFFY", "0000000000");
				entity.setFeesNo(templateNo);
				list.add(entity);
			}catch(Exception e){
				mes+="送货费【"+importEntity.getDeliverycharges()+"】格式不正确!";
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "送货费【"+importEntity.getDeliverycharges()+"】格式不正确!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}	
		}
		if(!StringUtils.isBlank(importEntity.getDischargingcharges())){
			try{
				FeesPayTransportEntity entity=getEntity(importEntity,vo,systemCodelist);
				double dischargingcharges=Double.valueOf(importEntity.getDischargingcharges());
				entity.setTotleprice(dischargingcharges);
				entity.setSubjectCode("ts_value_add_subject");//卸货
				entity.setOtherSubjectCode("ts_handwork");//装卸费
				String templateNo =sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YFFY", "0000000000");
				entity.setFeesNo(templateNo);
				list.add(entity);
			}catch(Exception e){
				mes+="装卸费【"+importEntity.getDischargingcharges()+"】格式不正确!";
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "卸货费【"+importEntity.getDischargingcharges()+"】格式不正确!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}	
		}
		if(!StringUtils.isBlank(importEntity.getReverselogisticsfee())){
			try{
				FeesPayTransportEntity entity=getEntity(importEntity,vo,systemCodelist);
				double reverselogisticsfee=Double.valueOf(importEntity.getReverselogisticsfee());
				entity.setTotleprice(reverselogisticsfee);
				entity.setSubjectCode("ts_value_add_subject");//逆向物流费
				entity.setOtherSubjectCode("ts_reverse_logistic");//逆向物流费
				String templateNo =sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YFFY", "0000000000");
				entity.setFeesNo(templateNo);
				list.add(entity);
			}catch(Exception e){
				mes+="逆向物流费【"+importEntity.getReverselogisticsfee()+"】格式不正确!";
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "逆向物流费【"+importEntity.getReverselogisticsfee()+"】格式不正确!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}	
		}
		if(!StringUtils.isBlank(importEntity.getCompensation())){
			try{
				FeesPayTransportEntity entity=getEntity(importEntity,vo,systemCodelist);
				double ompensation=Double.valueOf(importEntity.getCompensation());
				entity.setTotleprice(ompensation);
				entity.setSubjectCode("ts_value_add_subject");//赔偿费
				entity.setOtherSubjectCode("ts_abnormal");//赔偿费
				String templateNo =sequenceService.getBillNoOne(FeesPayTransportEntity.class.getName(), "YFFY", "0000000000");
				entity.setFeesNo(templateNo);
				list.add(entity);
			}catch(Exception e){
				mes+="赔偿费【"+importEntity.getCompensation()+"】格式不正确！";
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "赔偿费【"+importEntity.getCompensation()+"】格式不正确！", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}	
		}
		if(StringUtils.isBlank(mes)){
			return true;
		}else{
			mes="运单号【"+importEntity.getOrderno()+"】"+mes;
			vo.setMsg(mes);
			return false;
		}
	}
	private boolean validateForwarder(FeesPayTransportImportEntity entity,ErrorMessageVo vo,List<ForwarderVo> forwardervoList,List<FeesPayTransportEntity> importDatalist){
		boolean flag=false;
		for(ForwarderVo forwardervo:forwardervoList){
			if(entity.getForwardername().equals(forwardervo.getName())){
				flag=true;
				setForwardId(forwardervo.getForwardId(),importDatalist);
				break;
			}
		}
		if(!flag){
			vo.setMsg(vo.getMsg()+"导入的承运商名称【"+entity.getForwardername()+"】不存在!");
		}
		return flag;
	}
	private void setForwardId(String forwarderid,List<FeesPayTransportEntity> importDatalist){
		for(FeesPayTransportEntity entity:importDatalist){
			entity.setForwarderId(forwarderid);
		}
	}
	private void setCustomerid(String customerid,List<FeesPayTransportEntity> importDatalist){
		for(FeesPayTransportEntity entity:importDatalist){
			entity.setCustomerId(customerid);
		}
	}
	private void setProject(CusprojectRuleVo projectvo,List<FeesPayTransportEntity> importDatalist){
		for(FeesPayTransportEntity entity:importDatalist){
			entity.setProjectId(projectvo.getProjectid());
			entity.setProjectName(projectvo.getProjectName());
		}
	}
	private boolean validataProject(FeesPayTransportImportEntity entity,ErrorMessageVo vo,List<CusprojectRuleVo> cusprojectvoList,List<FeesPayTransportEntity> importDatalist){
		boolean flag=false;
		for(CusprojectRuleVo projectvo:cusprojectvoList){
			if(entity.getCustomername().equals(projectvo.getCustomername())){
				flag=true;
				setProject(projectvo,importDatalist);
				break;
			}
		}
		if(!flag){
			vo.setMsg(vo.getMsg()+"导入的商家【"+entity.getCustomername()+"】没有绑定相关项目!");
		}
		return flag;
	}
	private boolean validataCustomer(FeesPayTransportImportEntity entity,ErrorMessageVo vo,List<CustomerVo> customervoList,List<FeesPayTransportEntity> importDatalist){
		boolean flag=false;
		for(CustomerVo customervo:customervoList){
			if(entity.getCustomername().equals(customervo.getCustomername())){
				flag=true;
				setCustomerid(customervo.getCustomerid(),importDatalist);
				break;
			}
		}
		if(!flag){
			vo.setMsg(vo.getMsg()+"导入的商家名称【"+entity.getCustomername()+"】不存在!");
		}
		return flag;
	}
	private boolean validateEntity(FeesPayTransportImportEntity entity,ErrorMessageVo vo,
			List<SystemCodeEntity> systemCodelist){
		if(StringUtils.isBlank(entity.getOrderno())){
			vo.setMsg(vo.getMsg()+"订单号不能为空!");
			return false;
		}
		if(StringUtils.isBlank(entity.getWaybillno())){
			vo.setMsg(vo.getMsg()+"运单号不能为空!");
			return false;
		}
		if(StringUtils.isBlank(entity.getCustomername())){
			vo.setMsg(vo.getMsg()+"商家名称不能为空!");
			return false;
		}
		if(StringUtils.isBlank(entity.getForwardername())){
			vo.setMsg(vo.getMsg()+"承运商不能为空!");
			return false;
		}
		if(entity.getSigntime()==null){
			vo.setMsg(vo.getMsg()+"签收时间不能为空!");
			return false;
		}
		/*
		if(!StringUtils.isBlank(entity.getOrgaddress())){
			String[] arr=entity.getOrgaddress().split("/");
			if(arr.length!=2){
				vo.setMsg("始发地地址格式不正确 【市/区】");
				return false;
			}
		}
		if(!StringUtils.isBlank(entity.getTargetadress())){
			String[] arr=entity.getOrgaddress().split("/");
			if(arr.length!=2){
				vo.setMsg("到达地地址格式不正确【市/区】!");
				return false;
			}
		}
		if(!StringUtils.isBlank(entity.getCarmodel())){
			boolean flag=false;
			for(SystemCodeEntity code:systemCodelist){
				if(code.getCodeName().equals(entity.getCarmodel())){
					flag=true;
					break;
				}
			}
			if(!flag){
				vo.setMsg("系统中不存在车型【"+entity.getCarmodel()+"】");
				return false;
			}
		}*/
		return true;
	}
	
	@Override
	protected void saveDataBatch(
			List<FeesPayTransportEntity> list) throws Exception {		
		int k=feesPayTransportService.saveDataBatch(list);
		if(k<list.size()){
			throw new Exception("保存数据失败");
		}
	}
	/**
	 *  Excel 导入
	 * @param file
	 * @return
	 */
	@FileResolver
	public Map<String,Object> importExcel(UploadFile file,Map<String, Object> parameter){
		return this.importFile(file);
	}
	/**
	 * 获取进度
	 * @return
	 */
	@Expose
	public int getProgress() {
		return this.getProgressStatus();
	}
	
	@Expose
	public void setProgress(){
		 this.setProgressStatus();
	}
	
	@FileProvider
	public DownloadFile downloadTemple(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_payfee_template.xlsx");
		return new DownloadFile("应付运费导入模板.xlsx", is);
	}

}
