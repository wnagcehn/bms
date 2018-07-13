package com.jiuyescm.bms.bill.customer.web;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.bill.customer.vo.BillCustomerInfoVo;
import com.jiuyescm.cfm.common.JAppContext;



@Controller("billCustomerInfoController")
public class BillCustomerInfoController {
	
	private static final Logger logger = Logger.getLogger(BillCustomerInfoController.class.getName());
	@Autowired
	private IBillCustomerInfoService billCustomerInfoService;
	
	@DataProvider
	public void queryAll(Page<BillCustomerInfoVo> page,Map<String,Object> parameter) throws Exception{
		PageInfo<BillCustomerInfoVo> pageInfo=null;
		try{
			pageInfo=billCustomerInfoService.queryList(parameter, page.getPageNo(), page.getPageSize());
			if(page!=null){
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int)pageInfo.getTotal());
			}
		}catch(Exception e){
			logger.error("queryAll error:",e);
			throw e;
		}
		
	}
	
	@DataResolver
	public void save(BillCustomerInfoVo voEntity) throws Exception{
			if(EntityState.NEW.equals(EntityUtils.getState(voEntity)) ){
				voEntity.setDelFlag("0");
				voEntity.setCreateTime(JAppContext.currentTimestamp());
				voEntity.setCreator(JAppContext.currentUserID());
				
				if(StringUtils.isNotBlank(voEntity.getTel())){
					if(voEntity.getTel().length()>=16){
						throw new Exception("电话号码过长!");
					}
				}
				if(StringUtils.isNotBlank(voEntity.getPhone())){
					if(voEntity.getPhone().length()>32){
						throw new Exception("手机号码过长!");
					}
				}
				
				if(billCustomerInfoService.checkCustomerNameExist(voEntity.getCustomerName())){
					throw new Exception("开票商家【"+voEntity.getCustomerName()+"】已存在!");
				}
				if(StringUtils.isNotBlank(voEntity.getSysCustomerId())){
					boolean f=billCustomerInfoService.checkSysCustomerHasBind(voEntity.getSysCustomerId(),voEntity.getCustomerId());
					if(f){
						throw new Exception("系统商家【"+voEntity.getSysCustomerId()+"】已绑定开票商家,不能重新绑定开票商家!");
					}
				}
				billCustomerInfoService.insertEntity(voEntity);
				
			}else if(EntityState.MODIFIED.equals(EntityUtils.getState(voEntity))){
				voEntity.setLastModifier(JAppContext.currentUserID());
				voEntity.setLastModifyTime(JAppContext.currentTimestamp());
				if(StringUtils.isNotBlank(voEntity.getTel())){
					if(voEntity.getTel().length()>=16){
						throw new Exception("电话号码过长!");
					}
				}
				if(StringUtils.isNotBlank(voEntity.getPhone())){
					if(voEntity.getPhone().length()>32){
						throw new Exception("手机号码过长!");
					}
				}
				if(StringUtils.isNotBlank(voEntity.getSysCustomerId())){
					boolean f=billCustomerInfoService.checkSysCustomerHasBind(voEntity.getSysCustomerId(),voEntity.getCustomerId());
					if(f){
						throw new Exception("系统商家【"+voEntity.getSysCustomerId()+"】已绑定开票商家,不能重新绑定开票商家!");
					}
				}
				billCustomerInfoService.updateEntity(voEntity);
			}else{
				
			}
	}
	
	@DataResolver
	public void remove(BillCustomerInfoVo voEntity) throws Exception{
		voEntity.setLastModifier(JAppContext.currentUserID());
		voEntity.setLastModifyTime(JAppContext.currentTimestamp());
		voEntity.setDelFlag("1");
		billCustomerInfoService.removeEntity(voEntity);
	}
	
}
