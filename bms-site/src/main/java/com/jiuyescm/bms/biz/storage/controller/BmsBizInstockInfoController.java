package com.jiuyescm.bms.biz.storage.controller;

import java.sql.Timestamp;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.service.IBmsBizInstockInfoService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsBizInstockInfoController")
public class BmsBizInstockInfoController {

	//private static final Logger logger = LoggerFactory.getLogger(BmsBizInstockInfoController.class.getName());

	@Autowired
	private IBmsBizInstockInfoService bmsBizInstockInfoService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BmsBizInstockInfoEntity findById(Long id) throws Exception {
		return bmsBizInstockInfoService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsBizInstockInfoEntity> page, Map<String, Object> param) {
		PageInfo<BmsBizInstockInfoEntity> pageInfo = bmsBizInstockInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(BmsBizInstockInfoEntity entity) {
		String username = JAppContext.currentUserName();
		String userId = JAppContext.currentUserID();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		if (null == entity.getId()) {
			entity.setDelFlag("0");
			entity.setIsCalculated("0");
			entity.setCreator(username);
			entity.setCreateTime(currentTime);
			bmsBizInstockInfoService.save(entity);
		} else {
			entity.setLastModifier(username);
			entity.setLastModifierId(userId);
			entity.setLastModifyTime(currentTime);
			bmsBizInstockInfoService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsBizInstockInfoEntity entity) {
		entity.setDelFlag("1");
		bmsBizInstockInfoService.delete(entity);
	}
	
}
