package com.jiuyescm.bms.base.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.repository.IBmsGroupRepository;
import com.jiuyescm.bms.base.group.repository.IBmsGroupUserRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;

@Service("bmsGroupUserService")
public class BmsGroupUserServiceImpl implements IBmsGroupUserService {
	
	private static final Logger logger = Logger.getLogger(BmsGroupUserServiceImpl.class.getName());

	@Autowired
	private IBmsGroupUserRepository bmsGroupUserRepository;
	@Autowired
	private IBmsGroupRepository bmsGroupRepository;
	@Override
	public int addGroupUser(BmsGroupUserVo voEntity) throws Exception {
		try{
			BmsGroupUserEntity entity=new BmsGroupUserEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return bmsGroupUserRepository.addGroupUser(entity);
		}catch(Exception e){
			logger.error("addGroupUser:",e);
			throw e;
		}
	}

	@Override
	public int deleteGroupUser(int id) {
		return bmsGroupUserRepository.deleteGroupUser(id);
	}

	@Override
	public int updateGroupUser(BmsGroupUserVo voEntity) throws Exception {
		try{
			BmsGroupUserEntity entity=new BmsGroupUserEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			return bmsGroupUserRepository.updateGroupUser(entity);
		}catch(Exception e){
			logger.error("updateGroupUser:",e);
			throw e;
		}
	}

	@Override
	public List<BmsGroupUserVo> queryAllGroupUser() throws Exception {
		List<BmsGroupUserVo> volist=null;
		try{
			volist=new ArrayList<BmsGroupUserVo>();
			List<BmsGroupUserEntity> list=bmsGroupUserRepository.queryAllGroupUser();
			for(BmsGroupUserEntity entity:list){
				BmsGroupUserVo voEntity=new BmsGroupUserVo();
				PropertyUtils.copyProperties(voEntity, entity);
				volist.add(voEntity);
			}
			return volist;
		}catch(Exception e){
			logger.error("queryAllGroupUser:",e);
			throw e;
		}
	}

	@Override
	public PageInfo<BmsGroupUserVo> query(Map<String, Object> condition,
			int pageNo, int pageSize) throws Exception {
		PageInfo<BmsGroupUserVo> pageVoInfo=null;
		try{
			pageVoInfo=new PageInfo<BmsGroupUserVo>();
			PageInfo<BmsGroupUserEntity> pageInfo=bmsGroupUserRepository.query(condition, pageNo, pageSize);
			PropertyUtils.copyProperties(pageVoInfo, pageInfo);
			
			if(pageInfo!=null&&pageInfo.getList().size()>0){
				List<BmsGroupUserVo> list=new ArrayList<BmsGroupUserVo>();
				for(BmsGroupUserEntity entity:pageInfo.getList()){
					BmsGroupUserVo voEntity=new BmsGroupUserVo();
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
	public String checkExistGroupName(String userId) {
		return bmsGroupUserRepository.checkExistGroupName(userId);
	}

	@Override
	public int queryUserCountByGroupId(int groupId) {
		return bmsGroupUserRepository.queryUserCountByGroupId(groupId);
	}

	@Override
	public List<String> queryContainUserIds(String userId) {
		BmsGroupUserEntity groupUser=bmsGroupUserRepository.queryEntityByUserId(userId);
		List<String> list=new ArrayList<String>();
		if(groupUser==null){//未加入權限組
			list.add(userId);
		}else{
			if(groupUser.getAdministrator().equals("0")){//管理员
/*				List<BmsGroupEntity> groupList=bmsGroupRepository.queryAllGroup();*/
				List<Integer> groupIds=bmsGroupRepository.queryAllGroupId(groupUser.getGroupId());
				//List<Integer> groupIds=getAllGroupIds(groupList,groupUser.getGroupId());
				if(groupIds.size()>0){
					List<BmsGroupUserEntity> groupUserList=bmsGroupUserRepository.queryAllByGroupId(groupIds);
					for(BmsGroupUserEntity groupUserEntity:groupUserList){
						list.add(groupUserEntity.getUserId());
					}
					list.add(userId);
				}
				
			}else{//非管理员 只看自己
				list.add(userId);
			}
		}
		return list;
		//return bmsGroupUserRepository.queryContainUserIds(userId);
	}
	/**
	 * 获取群组 以及所有子群组ID
	 */
	private List<Integer> getAllGroupIds(List<BmsGroupEntity> groupList,int groupId){
		List<Integer> list=new ArrayList<Integer>();
		for(BmsGroupEntity entity:groupList){
			if(entity.getParentId()==groupId){
				list.add(entity.getId());
				if(checkHasChild(groupList,entity.getId())){//有子项
					list.addAll(getAllGroupIds(groupList,entity.getId()));//递归调用 获得所有子项
				}
			}
		}
		return list;
	}
	private Boolean checkHasChild(List<BmsGroupEntity> groupList,int id){
		boolean f=false;
		for(BmsGroupEntity entity:groupList){
			if(entity.getParentId()==id){
				f=true;
				break;
			}
		}
		return f;
	}

	@Override
	public BmsGroupUserVo queryEntityByUserId(String userId) {
		// TODO Auto-generated method stub
		try{
			BmsGroupUserEntity groupUser=bmsGroupUserRepository.queryEntityByUserId(userId);
			BmsGroupUserVo vo=new BmsGroupUserVo();
			PropertyUtils.copyProperties(vo, groupUser);
			
			return vo;
		}catch(Exception e){
			logger.error(e);
		}
		return null;
	}

	@Override
	public List<String> queryContainUserIds(BmsGroupUserVo groupUser) {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();

		if("1".equals(groupUser.getAdministrator())){//非管理员
			List<Integer> groupIds=bmsGroupRepository.queryAllGroupId(groupUser.getGroupId());
			if(groupIds.size()>0){
				List<BmsGroupUserEntity> groupUserList=bmsGroupUserRepository.queryAllByGroupId(groupIds);
				for(BmsGroupUserEntity groupUserEntity:groupUserList){
					list.add(groupUserEntity.getUserId());
				}
			}
			list.add(groupUser.getUserId());
		}
		return list;
	}
}
