package com.jiuyescm.bms.common.datapower.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DataUser;
import com.jiuyescm.bms.common.datapower.entity.UserLimitgroupEntity;
import com.jiuyescm.bms.common.datapower.service.DatagroupUserService;

@Controller("datagroupUserPR")
public class DatagroupUserPR {
	@Resource
	private DatagroupUserService datagroupUserService;
	
	// 根据数组id查询出用户与数组
		@DataProvider
		public void queryByDgroupid(Map<String, Object> map,
				Page<UserLimitgroupEntity> page) {
			PageInfo<UserLimitgroupEntity> tmpPageInfo = datagroupUserService
					.query(map, page.getPageNo(), page.getPageSize());
			if (tmpPageInfo != null) {
				page.setEntities(tmpPageInfo.getList());
				page.setEntityCount((int) tmpPageInfo.getTotal());
			}

		}

		// 根据用户id查找用户
		@DataProvider
		public void queryByUserid(Map<String, Object> map,
				Page<DataUser> page) throws Exception {
			List<UserLimitgroupEntity> datagroupUser= new ArrayList<UserLimitgroupEntity>();
			int msg = (int) map.get("msg");
			if (msg == 1) {
				datagroupUser = datagroupUserService.queryBydatagroupid(map);// 通过数据组id查找出用户id 从后台
																					
			} else if (msg == 0) {
				datagroupUser = (List<UserLimitgroupEntity>) map
						.get("dgroupid");// 拿到前台的
			}

			// List<DatagroupCustomerEntity>
			// datagroupCustomer=datagroupCustomerService.queryBydatagroup(map);//通过数据组id查找出商家id
			List<String> list = new ArrayList<String>();// 初始化一个list
			if (datagroupUser != null && datagroupUser.size() > 0) {
				for (UserLimitgroupEntity dEntity : datagroupUser) {
					String userid = dEntity.getUserid();// 用户id
					// 如果数据组中仓库id和查询条件中的商家一样
					if (map.get("userid") != null) {
						String uID = (String) map.get("userid");
						if (uID.equals(userid)) {
							throw new Exception("查询条件中的用户ID已存在数据组中");
						}
					}
					list.add(userid);
				}
				map.put("list", list);
			}

			PageInfo<DataUser> tmpPageInfo = datagroupUserService
					.queryByUserid(map, page.getPageNo(), page.getPageSize());
			if (tmpPageInfo != null) {
				page.setEntities(tmpPageInfo.getList());
				page.setEntityCount((int) tmpPageInfo.getTotal());
			}

		}


}
