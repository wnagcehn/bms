package com.jiuyescm.bms.common.datapower.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupWarehouseEntity;
import com.jiuyescm.bms.common.datapower.entity.Ware_houseEntity;
import com.jiuyescm.bms.common.datapower.service.DatagroupWarehouseService;

@Controller("datagroupWarehousePR")
public class DatagroupWarehousePR {

	@Resource
	private DatagroupWarehouseService datagroupWarehouseService;

	// 根据数组id查询出仓库与数组
	@DataProvider
	public void queryByDgroupid(Map<String, Object> map,
			Page<DatagroupWarehouseEntity> page) {
		PageInfo<DatagroupWarehouseEntity> tmpPageInfo = datagroupWarehouseService
				.query(map, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}

	}

	// 根据仓库id查找出用户
	@DataProvider
	public void queryByCkid(Map<String, Object> map,
			Page<Ware_houseEntity> page) throws Exception {
		List<DatagroupWarehouseEntity> datagroupWarehouse= new ArrayList<DatagroupWarehouseEntity>();
		int msg = (int) map.get("msg");
		if (msg == 1) {
			datagroupWarehouse = datagroupWarehouseService.queryBydatagroupid(map);// 通过数据组id查找出仓库id 从后台
																				
		} else if (msg == 0) {
			datagroupWarehouse = (List<DatagroupWarehouseEntity>) map
					.get("dgroupid");// 拿到前台的
		}

		// List<DatagroupCustomerEntity>
		// datagroupCustomer=datagroupCustomerService.queryBydatagroup(map);//通过数据组id查找出商家id
		List<String> list = new ArrayList<String>();// 初始化一个list
		if (datagroupWarehouse != null && datagroupWarehouse.size() > 0) {
			for (DatagroupWarehouseEntity dEntity : datagroupWarehouse) {
				String warehouseid = dEntity.getWarehouseid();// 仓库id
				// 如果数据组中仓库id和查询条件中的商家一样
				if (map.get("warehouseid") != null) {
					String wID = (String) map.get("warehouseid");
					if (wID.equals(warehouseid)) {
						throw new Exception("查询条件中的仓库ID已存在数据组中");
					}
				}
				list.add(warehouseid);
			}
			map.put("list", list);
		}

		PageInfo<Ware_houseEntity> tmpPageInfo = datagroupWarehouseService
				.queryByCkid(map, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}

	}

}
