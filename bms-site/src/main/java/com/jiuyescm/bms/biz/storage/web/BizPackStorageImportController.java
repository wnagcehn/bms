package com.jiuyescm.bms.biz.storage.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.uploader.UploadFile;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.common.web.HttpCommonImport;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.PackStorageTemplateDataType;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("bizPackStorageImportController")
public class BizPackStorageImportController extends HttpCommonImport<BizPackStorageEntity,BizPackStorageEntity> {

	@Resource 
	private IBizPackStorageService bizPackStorageService;
	@Autowired 
	private ICustomerService customerService;
	@Autowired 
	private IWarehouseService warehouseService;
	@Resource 
	private ISystemCodeService systemCodeService;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private Lock lock;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Override
	protected BaseDataType getBaseDataType() {
		return new PackStorageTemplateDataType();
	}

	private Map<String, Object> importPackStorage(UploadFile file){
		return super.importFile(file);
	}
	
	public  Map<String, Object> importPackStorage(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		String lockString=Tools.getMd5("BMS_QUO_IMPORT_PACK_STORAGE");
		lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				try {
					   Map<String, Object> re=importPackStorage(file);
					   return re;
					} catch (Exception e) {
						e.printStackTrace();
						//写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					}
					return null;
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("耗材库存导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(
					LockInsideExecutedException e)
					throws LockInsideExecutedException {
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
	 * 验证 数据 给实体赋值
	 */
	@Override
	protected List<BizPackStorageEntity> validateImportList(
			List<BizPackStorageEntity> importList, List<ErrorMessageVo> infoList) {
	
		List<String> customerNameList=new ArrayList<String>();
		for(BizPackStorageEntity entity:importList){
			customerNameList.add(entity.getCustomerName());
		}
		
		List<CustomerVo> customerVoList=customerService.queryByCustomerName(customerNameList);
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureCodeList = systemCodeService.queryCodeList(param);
		ErrorMessageVo  vo=null;
		int index=0;
		Timestamp currentTime=JAppContext.currentTimestamp();
		String currentUser=JAppContext.currentUserName();
		List<String> Idlist=sequenceService.getBillNoList(BizPackStorageEntity.class.getName(),"BMPS", importList.size(), "0000000000");
		for(BizPackStorageEntity entity:importList){
			entity.setCreateTime(currentTime);
			entity.setCreator(currentUser);
			entity.setLastModifier(currentUser);
			entity.setLastModifyTime(currentTime);
			entity.setDelFlag("0");
			entity.setDataNum(Idlist.get(index));
			index++;
			vo=new ErrorMessageVo();
			vo.setLineNo(index);
			
			String mes="";
			if(!validataDataNull(entity,vo)){
				mes+=vo.getMsg();
			}
			if(!validataWarehouse(entity,wareHouseList,vo)){
				mes+=vo.getMsg();
			}
			if(!validataCustomer(entity,customerVoList,vo)){
				mes+=vo.getMsg();
			}
			if(!validataTemperature(entity,temperatureCodeList,vo)){
				mes+=vo.getMsg();
			}
			if(!StringUtils.isBlank(mes)){
				vo.setMsg(mes);
				infoList.add(vo);
			}
		}
		return importList;
	}
	private List<BizPackStorageEntity> getOrgList(Map<String,Object> map){
		//return bizPackStorageService.queryAllBycurtime(map);
		return null;
	}
	/**
	 * 非空数据校验
	 * @param entity
	 * @param vo
	 * @return
	 */
	private boolean validataDataNull(BizPackStorageEntity entity,ErrorMessageVo vo){
		String mes="";
		try{
			Timestamp stockTimeInput = entity.getCurTime();
			if(null != stockTimeInput) {
				entity.setCurTime(stockTimeInput);
			}else {
				mes+="库存日期不能为空!";
			}	
		}catch(Exception e){
			mes+="输入的库存日期格式不符合(yyyy-MM-dd)!";
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "输入的库存日期格式不符合(yyyy-MM-dd)!", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		if(StringUtils.isBlank(entity.getCustomerName())){
			mes+="商家不能为空!";
		}
		if(StringUtils.isBlank(entity.getWarehouseName())){
			mes+="仓库名称不能为空!";
		}
		if(StringUtils.isBlank(entity.getTemperatureTypeName())){
			mes+="温度类型不能为空!";
		}
		//托数
		if(entity.getPalletNum() == null){
			entity.setPalletNum(0.0d);
		}
		if(entity.getPalletNum() != null && entity.getPalletNum()<0){
			mes+="托数不能小于0!";
		}
		//数量
		if(entity.getQty() == null){
			entity.setQty(0.0d);
		}
		if(entity.getQty() != null && entity.getQty()<0){
			mes+="数量不能小于0! ";
		}
		entity.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
		if(!StringUtils.isBlank(mes)){
			vo.setMsg(mes);
			return false;
		}
		return true;
		
	}
	private boolean validataTemperature(BizPackStorageEntity entity,List<SystemCodeEntity> temperatureCodeList,ErrorMessageVo vo){
		boolean flag=false;
		for(SystemCodeEntity systemCodeEntity:temperatureCodeList){
			if(entity.getTemperatureTypeName().equals(systemCodeEntity.getCodeName())){
				flag=true;
				entity.setTemperatureTypeCode(systemCodeEntity.getCode());
				break;
			}
		}
		if(!flag){
			vo.setMsg("温度类型["+entity.getTemperatureTypeName()+"]没有在表中维护!!");
		}
		return flag;
	}
	private boolean validataWarehouse(BizPackStorageEntity entity,List<WarehouseVo> wareHouseList,ErrorMessageVo vo){
		boolean flag=false;
		for(WarehouseVo wareHousevo:wareHouseList){
			if(entity.getWarehouseName().equals(wareHousevo.getWarehousename())){
				flag=true;
				entity.setWarehouseCode(wareHousevo.getWarehouseid());
				break;
			}
		}
		if(!flag){
			vo.setMsg("导入的仓库名称【"+entity.getWarehouseName()+"】不存在!");
		}
		return flag;
	}
	/**
	 * 验证 商家名称
	 * @param entity
	 * @param customerVoList
	 * @param vo
	 * @return
	 */
	private boolean validataCustomer(BizPackStorageEntity entity,List<CustomerVo> customerVoList,ErrorMessageVo vo){
		boolean flag=false;
		for(CustomerVo customervo:customerVoList){
			if(entity.getCustomerName().equals(customervo.getCustomername())){
				flag=true;
				entity.setCustomerid(customervo.getCustomerid());
				break;
			}
		}
		if(!flag){
			vo.setMsg("导入的商家名称【"+entity.getCustomerName()+"】不存在!");
		}
		return flag;
	}
	@Override
	protected void saveDataBatch(List<BizPackStorageEntity> list)
			throws Exception {
		int k=bizPackStorageService.saveList(list);
		if(k<list.size()){
			throw new Exception("保存数据失败");
		}
	}


	protected List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("curTime");//当前日期
		list.add("warehouseCode");//仓库Id
		list.add("customerid");//商家ID
		list.add("temperatureTypeCode");//温度
		return list;
	}

}
