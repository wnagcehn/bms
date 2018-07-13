package com.jiuyescm.bms.common.datapower.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.DefaultUser;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupCustomerEntity;
import com.jiuyescm.bms.common.datapower.entity.DatagroupEntity;
import com.jiuyescm.bms.common.datapower.entity.DatagroupWarehouseEntity;
import com.jiuyescm.bms.common.datapower.entity.UserLimitgroupEntity;
import com.jiuyescm.bms.common.datapower.service.DatagroupCustomerService;
import com.jiuyescm.bms.common.datapower.service.DatagroupService;
import com.jiuyescm.bms.common.datapower.service.DatagroupUserService;
import com.jiuyescm.bms.common.datapower.service.DatagroupWarehouseService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.BillNoConstantInterface;

@Controller("datagroupPR")
public class DatagroupPR {
	@Resource
	private DatagroupService datagroupService;
	@Resource
	private SequenceService sequenceService;
	
	@Resource
	private DatagroupCustomerService datagroupCustomerService;//商家数据组
	@Resource
	private DatagroupWarehouseService datagroupWarehouseService;//仓库数据组
	@Resource
	private DatagroupUserService datagroupUserService;//账户数据组

	// 根据数组实体查询
	@DataProvider
	public void queryByDgroupid(Map<String, Object> map,
			Page<DatagroupEntity> page) {
		DatagroupEntity datagroup = new DatagroupEntity();// 数据组
		if (null != map && map.size() > 0) {
			String dgroupid = (String) map.get("dgroupid");// 数据组ID
			String dgroupname = (String) map.get("dgroupname");// 数据简称
			datagroup.setDgroupid(dgroupid);
			datagroup.setDgroupname(dgroupname);

		}
		PageInfo<DatagroupEntity> tmpPageInfo = datagroupService
				.queryDatagroup(datagroup, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}

	}

	@DataResolver
	// 保存数据组
	public void insertByDgroup(List<DatagroupEntity> list) throws Exception {
		DatagroupEntity datagroup = new DatagroupEntity();// 数据组
		Timestamp currentTime = JAppContext.currentTimestamp();
		DefaultUser user = (DefaultUser) DoradoContext.getCurrent()
				.getAttribute(DoradoContext.SESSION,
						ContextHolder.LOGIN_USER_SESSION_KEY);
		if (list != null && list.size() > 0) {
			datagroup = list.get(0);
			datagroup.setId(SequenceGenerator.uuidOf36StringE());
			datagroup.setDgroupid(sequenceService.getBillNoOne(
					DatagroupEntity.class.getName(),
					BillNoConstantInterface.DGroupIDNo.BEGINNAME,
					BillNoConstantInterface.DGroupIDNo.LENGTH));
			datagroup.setCrepersonid(JAppContext.currentUserID());// 登录人id
			datagroup.setCreperson(JAppContext.currentUserName());// 登录人姓名
			datagroup.setCretime(currentTime);
		}
		int msg = datagroupService.insertDatagroup(datagroup);
		if (msg < 1) {
			throw new Exception("新增失败!");
		}
	}

	@DataResolver
	// 修改数据组
	public void updateByDgroup(List<DatagroupEntity> list) throws Exception {
		DatagroupEntity datagroup = new DatagroupEntity();// 数据组
		Timestamp currentTime = JAppContext.currentTimestamp();
		if (list != null && list.size() > 0) {
			datagroup = list.get(0);
			datagroup.setModperson(JAppContext.currentUserName());
			datagroup.setModpersonid(JAppContext.currentUserID());
			datagroup.setModtime(currentTime);
		}
		int msg = datagroupService.updateDatagroup(datagroup);
		if (msg < 1) {
			throw new Exception("修改失败!");
		}

	}

	
	@DataResolver
	// 删除数据组
	public int deleteByDgroup(DatagroupEntity datagroup) throws Exception {
		if (datagroup != null) {
			Map<String,Object> map = new java.util.HashMap<String,Object>();
			map.put("dgroupid", datagroup.getDgroupid());
			List<DatagroupCustomerEntity> customerEntities=datagroupCustomerService.queryBydatagroup(map);
			List<DatagroupWarehouseEntity> datagroupWarehouseEntities=datagroupWarehouseService.queryBydatagroupid(map);
			List<UserLimitgroupEntity> limitgroupEntities=datagroupUserService.queryBydatagroupid(map);
			if(customerEntities.size()==0&&datagroupWarehouseEntities.size()==0&&limitgroupEntities.size()==0){
				int msg = datagroupService.deleteDatagroup(datagroup);
				if (msg < 1) {
					throw new Exception("删除失败!");
				}
			}else{
				return 1;
			}
		
		}
		return 0;

	}

	// 校验数据组名称
	@Expose
	public String validator(String dgroupname) {
		List<DatagroupEntity> tList = datagroupService.findByName(dgroupname);
		if (tList.size() > 0) {
			return "此数据组名称已存在，请重新输入！";
		}
		return null;
	}

	// 保存各个数据组
	@DataResolver
	public void addAll(List<UserLimitgroupEntity> limitgroupEntities,List<DatagroupWarehouseEntity> datagroupWarehouseEntities,List<DatagroupCustomerEntity> datagroupCustomerEntities) {
		
		Timestamp currentTime = JAppContext.currentTimestamp();//时间
		//数据组商家
		List<DatagroupCustomerEntity> datagroupCustomerEntities2 =new ArrayList<DatagroupCustomerEntity>();//删除集合
		List<DatagroupCustomerEntity> datagroupCustomerEntities3 =new ArrayList<DatagroupCustomerEntity>();//新增集合
		for (DatagroupCustomerEntity customer : datagroupCustomerEntities) {
			if (EntityState.DELETED.equals(EntityUtils.getState(customer))) {
				System.out.println("删除产品 : " + customer.getCustomerid());// 执行产品删除操作
				DatagroupCustomerEntity customerEntity=new DatagroupCustomerEntity();//商家
				customerEntity.setDgroupid(customer.getDgroupid());
				customerEntity.setCustomerid(customer.getCustomerid());//要删除的商家id
				datagroupCustomerEntities2.add(customerEntity);//新增至删除集合
				
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(customer))) {
				System.out.println("修改产品 : " + customer.getCustomerid());// 执行产品修改操作
				
			} else if (EntityState.NEW.equals(EntityUtils.getState(customer))) {
				System.out.println("新增产品 : " + customer.getCustomerid());// 执行产品新增操作
				DatagroupCustomerEntity customerEntity=new DatagroupCustomerEntity();//商家
				customerEntity.setDgroupid(customer.getDgroupid());
				customerEntity.setCustomerid(customer.getCustomerid());//要新增的商家id
				customerEntity.setCreperson(JAppContext.currentUserName());//新增人姓名
				customerEntity.setCrepersonid(JAppContext.currentUserID());//新增人id
				customerEntity.setCretime(currentTime);
				datagroupCustomerEntities3.add(customerEntity);
			}
		}
		if(datagroupCustomerEntities2!=null &&datagroupCustomerEntities2.size()>0){
			datagroupCustomerService.deleteCustomer(datagroupCustomerEntities2);
		}
		if(datagroupCustomerEntities3!=null &&datagroupCustomerEntities3.size()>0){
			datagroupCustomerService.addCustomer(datagroupCustomerEntities3);
		}
		
		//仓库数据组	
		List<DatagroupWarehouseEntity> datagroupWarehouseEntities2=new ArrayList<DatagroupWarehouseEntity>();//删除集合
		List<DatagroupWarehouseEntity> datagroupWarehouseEntities3=new ArrayList<DatagroupWarehouseEntity>();//新增集合
		for (DatagroupWarehouseEntity warehouseEntity : datagroupWarehouseEntities) {
			if (EntityState.DELETED.equals(EntityUtils.getState(warehouseEntity))) {
				System.out.println("删除产品 : " + warehouseEntity.getWarehouseid());// 执行产品删除操作
				DatagroupWarehouseEntity warehouseEntity2=new DatagroupWarehouseEntity();//仓库
				warehouseEntity2.setDgroupid(warehouseEntity.getDgroupid());
				warehouseEntity2.setWarehouseid(warehouseEntity.getWarehouseid());//要删除的仓库id
				datagroupWarehouseEntities2.add(warehouseEntity2);//新增至删除集合
				
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(warehouseEntity))) {
				System.out.println("修改产品 : " + warehouseEntity.getWarehouseid());// 执行产品修改操作
				
			} else if (EntityState.NEW.equals(EntityUtils.getState(warehouseEntity))) {
				System.out.println("新增产品 : " + warehouseEntity.getWarehouseid());// 执行产品新增操作
				DatagroupWarehouseEntity warehouseEntity3=new DatagroupWarehouseEntity();//仓库
				warehouseEntity3.setDgroupid(warehouseEntity.getDgroupid());
				warehouseEntity3.setWarehouseid(warehouseEntity.getWarehouseid());//要新增的仓库id
				warehouseEntity3.setCreperson(JAppContext.currentUserName());//新增人姓名
				warehouseEntity3.setCrepersonid(JAppContext.currentUserID());//新增人id
				warehouseEntity3.setCretime(currentTime);
				datagroupWarehouseEntities3.add(warehouseEntity3);
			}
		}
		if(datagroupWarehouseEntities2!=null &&datagroupWarehouseEntities2.size()>0){
			datagroupWarehouseService.deleteCk(datagroupWarehouseEntities2);
		}
		if(datagroupWarehouseEntities3!=null &&datagroupWarehouseEntities3.size()>0){
			datagroupWarehouseService.addCk(datagroupWarehouseEntities3);
		}
		
		//用户数据组	
				List<UserLimitgroupEntity> limitgroupEntities2=new ArrayList<UserLimitgroupEntity>();//删除集合
				List<UserLimitgroupEntity> limitgroupEntities3=new ArrayList<UserLimitgroupEntity>();//新增集合
				for (UserLimitgroupEntity userLimitgroupEntity : limitgroupEntities) {
					if (EntityState.DELETED.equals(EntityUtils.getState(userLimitgroupEntity))) {
						System.out.println("删除产品 : " + userLimitgroupEntity.getUserid());// 执行产品删除操作
						UserLimitgroupEntity userLimitgroupEntity2=new UserLimitgroupEntity();//用户
						userLimitgroupEntity2.setId(userLimitgroupEntity.getId());
						userLimitgroupEntity2.setUserid(userLimitgroupEntity.getUserid());//用户id
						userLimitgroupEntity2.setGroupid(userLimitgroupEntity.getGroupid());//数据组id
						//userLimitgroupEntity2.setWarehouseid(warehouseEntity.getWarehouseid());//要删除的用户id
						limitgroupEntities2.add(userLimitgroupEntity2);//新增至删除集合
						
					} else if (EntityState.MODIFIED.equals(EntityUtils.getState(userLimitgroupEntity))) {
						System.out.println("修改产品 : " + userLimitgroupEntity.getUserid());// 执行产品修改操作
						
					} else if (EntityState.NEW.equals(EntityUtils.getState(userLimitgroupEntity))) {
						System.out.println("新增产品 : " + userLimitgroupEntity.getUserid());// 执行产品新增操作
						UserLimitgroupEntity userLimitgroupEntity3=new UserLimitgroupEntity();//用户
						userLimitgroupEntity3.setGroupid(userLimitgroupEntity.getGroupid());
						userLimitgroupEntity3.setUserid(userLimitgroupEntity.getUserid());//用户id
						userLimitgroupEntity3.setId(SequenceGenerator.uuidOf36StringE());//id
						userLimitgroupEntity3.setCreperson(JAppContext.currentUserName());//新增人姓名
						userLimitgroupEntity3.setCrepersonid(JAppContext.currentUserID());//新增人id
						userLimitgroupEntity3.setCretime(currentTime);
						limitgroupEntities3.add(userLimitgroupEntity3);
					}
				}
				if(limitgroupEntities2!=null &&limitgroupEntities2.size()>0){
					datagroupUserService.deleteUser(limitgroupEntities2);
				}
				if(limitgroupEntities3!=null &&limitgroupEntities3.size()>0){
					datagroupUserService.addUser(limitgroupEntities3);
				}

	}

}
