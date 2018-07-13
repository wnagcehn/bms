package com.jiuyescm.bms.common.datapower.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.bms.common.datapower.entity.DatagroupCustomerEntity;
import com.jiuyescm.bms.common.datapower.entity.Pub_Customer;
import com.jiuyescm.bms.common.datapower.service.DatagroupCustomerService;

@Controller("datagroupCustomerPR")
public class DatagroupCustomerPR {
	@Resource
	private DatagroupCustomerService datagroupCustomerService;

	// 根据数组id查询出商家与数组
	@DataProvider
	public void queryByDgroupid(Map<String, Object> map,
			Page<DatagroupCustomerEntity> page) {
		PageInfo<DatagroupCustomerEntity> tmpPageInfo = datagroupCustomerService
				.selectByPrimaryKey(map, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}

	}

	// 根据商家id查找出商家
	@DataProvider
	public void queryByCustomerid(Map<String, Object> map,
			Page<Pub_Customer> page) throws Exception {
		List<DatagroupCustomerEntity> datagroupCustomer = new ArrayList<DatagroupCustomerEntity>();
		int msg = (int) map.get("msg");
		if (msg == 1) {
			datagroupCustomer = datagroupCustomerService.queryBydatagroup(map);// 通过数据组id查找出商家id 从后台
																				
		} else if (msg == 0) {
			datagroupCustomer = (List<DatagroupCustomerEntity>) map
					.get("dgroupid");// 拿到前台的
		}

		// List<DatagroupCustomerEntity>
		// datagroupCustomer=datagroupCustomerService.queryBydatagroup(map);//通过数据组id查找出商家id
		List<String> list = new ArrayList<String>();// 初始化一个list
		if (datagroupCustomer != null && datagroupCustomer.size() > 0) {
			for (DatagroupCustomerEntity dEntity : datagroupCustomer) {
				String customerid = dEntity.getCustomerid();// 商家id
				// 如果数据组中商家id和查询条件中的商家一样
				if (map.get("customerid") != null) {
					String cID = (String) map.get("customerid");
					if (cID.equals(customerid)) {
						throw new Exception("查询条件中的商家ID已存在数据组中");
					}
				}
				list.add(customerid);
			}
			map.put("list", list);
		}

		PageInfo<Pub_Customer> tmpPageInfo = datagroupCustomerService
				.queryByCustomerid(map, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}

	}

	// 把商家增加到数据组中,dgCustomer是商家和数据组的 ,customer是商家 此方法暂时未用
	@DataResolver
	public void addCustomer(List<DatagroupCustomerEntity> dgCustomer,
			List<Pub_Customer> customer) {
		// System.out.println("ddd");
		String id = dgCustomer.get(0).getDgroupid();
		Timestamp currentTime = JAppContext.currentTimestamp();
		for (Pub_Customer pub_Customer : customer) {
			DatagroupCustomerEntity datagroupCustomer = new DatagroupCustomerEntity();
			datagroupCustomer.setCustomerid(pub_Customer.getCustomerid());// 商家id
			datagroupCustomer.setCustomername(pub_Customer.getShortname());// 商家名
			datagroupCustomer.setDgroupid(id);// 数据组id
			datagroupCustomer.setCreperson(JAppContext.currentUserName());
			datagroupCustomer.setCrepersonid(JAppContext.currentUserID());
			datagroupCustomer.setCretime(currentTime);
			dgCustomer.add(datagroupCustomer);
		}
	}

	// 选择数据id时查找出商家信息      未用
	@DataProvider
	public void findByGroupID(Map<String, Object> map, Page<Pub_Customer> page) {
		List<DatagroupCustomerEntity> datagroupCustomer = datagroupCustomerService
				.queryBydatagroup(map);// 通过数据组id查找出商家id
		List<String> list = new ArrayList<String>();// 初始化一个list
		if (datagroupCustomer != null && datagroupCustomer.size() > 0) {
			for (DatagroupCustomerEntity dEntity : datagroupCustomer) {
				String customerid = dEntity.getCustomerid();// 商家id
				list.add(customerid);
			}
			map.put("list", list);// 添加至map
		}

		PageInfo<Pub_Customer> tmpPageInfo = datagroupCustomerService
				.queryByCustomerid(map, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}

	}

}
