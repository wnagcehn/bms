package com.jiuyescm.bms.base.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.repository.IBmsGroupCustomerRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;

@Service("bmsGroupCustomerService")
public class BmsGroupCustomerServiceImpl implements IBmsGroupCustomerService {
	
	private static final Logger logger = Logger.getLogger(BmsGroupCustomerServiceImpl.class.getName());

	@Autowired
	private IBmsGroupCustomerRepository bmsGroupCustomerRepository;
	@Override
	public List<BmsGroupCustomerVo> queryAllByGroupId(int groupId) throws Exception {
		List<BmsGroupCustomerVo> voList=null;
		try{
			List<BmsGroupCustomerEntity> entityList=bmsGroupCustomerRepository.queryAllByGroupId(groupId);
			voList=new ArrayList<BmsGroupCustomerVo>();
			for(BmsGroupCustomerEntity entity:entityList){
				BmsGroupCustomerVo vo=new BmsGroupCustomerVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("queryAllByGroupId:",e);
			throw e;
		}
		return voList;
	}

	@Override
	public int addBatch(List<BmsGroupCustomerVo> list) throws Exception {
		try{
			List<BmsGroupCustomerEntity> entityList=new ArrayList<BmsGroupCustomerEntity>();
			for(BmsGroupCustomerVo vo:list){
				BmsGroupCustomerEntity entity=new BmsGroupCustomerEntity();
				PropertyUtils.copyProperties(entity,vo);
				entityList.add(entity);
			}
			return bmsGroupCustomerRepository.addBatch(entityList);
		}catch(Exception e){
			logger.error("addBatch:",e);
			throw e;
		}
	}

	@Override
	public int delGroupCustomer(BmsGroupCustomerVo customerVo) throws Exception {
		try{
			BmsGroupCustomerEntity customerEntity=new BmsGroupCustomerEntity();
			PropertyUtils.copyProperties(customerEntity,customerVo);
			int k=bmsGroupCustomerRepository.delGroupCustomer(customerEntity);
			return k;
		}catch(Exception e){
			logger.error("delGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public int updateGroupCustomer(BmsGroupCustomerVo subjectVo) throws Exception {
		try{
			BmsGroupCustomerEntity entity=new BmsGroupCustomerEntity();
			PropertyUtils.copyProperties(entity, subjectVo);
			return bmsGroupCustomerRepository.updateGroupCustomer(entity);
		}catch(Exception e){
			logger.error("updateGroupSubject:",e);
			throw e;
		}
	}

	@Override
	public PageInfo<BmsGroupCustomerVo> queryGroupCustomer(
			BmsGroupCustomerVo queryCondition, int pageNo, int pageSize) throws Exception {
		PageInfo<BmsGroupCustomerVo> pageVoInfo=null;
		try{
			if(queryCondition==null){
				return pageVoInfo;
			}
			pageVoInfo=new PageInfo<BmsGroupCustomerVo>();
			BmsGroupCustomerEntity queryEntity=new BmsGroupCustomerEntity();
			PropertyUtils.copyProperties(queryEntity, queryCondition);
			PageInfo<BmsGroupCustomerEntity> pageInfo=bmsGroupCustomerRepository.queryGroupCustomer(queryEntity, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsGroupCustomerVo> list=new ArrayList<BmsGroupCustomerVo>();
				for(BmsGroupCustomerEntity entity:pageInfo.getList()){
					BmsGroupCustomerVo voEntity=new BmsGroupCustomerVo();
					PropertyUtils.copyProperties(voEntity, entity);
					list.add(voEntity);
				}
				pageVoInfo.setList(list);
			}
		}catch(Exception e){
			throw e;
		}
		return pageVoInfo;
	}

	@Override
	public List<String> checkCustomerCodeExist(int groupId,
			List<String> customerIdList) {
		return bmsGroupCustomerRepository.checkCustomerCodeExist(groupId,customerIdList);
	}

	@Override
	public int queryCustomerCountByGroupId(int groupId) {
		return bmsGroupCustomerRepository.queryCustomerCountByGroupId(groupId);
	}

	@Override
	public List<String> queryCustomerByGroupId(int groupId) {
		// TODO Auto-generated method stub
		return bmsGroupCustomerRepository.queryCustomerByGroupId(groupId);
	}


}
