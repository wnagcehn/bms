package com.jiuyescm.bms.base.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.config.repository.IBmsWarehouseConfigRepository;
import com.jiuyescm.bms.base.config.service.IBmsWarehouseConfigService;
import com.jiuyescm.bms.base.config.vo.BmsWarehouseConfigVo;


@Service("bmsWarehouseConfigService")
public class BmsWarehouseConfigServiceImpl implements IBmsWarehouseConfigService{

	private static final Logger logger = Logger.getLogger(BmsWarehouseConfigServiceImpl.class.getName());

	@Autowired
	private IBmsWarehouseConfigRepository bmsWarehouseConfigRepository;
	@Override
	public List<BmsWarehouseConfigVo> queryAll() throws Exception {
		List<BmsWarehouseConfigVo> volist=null;
		try{
			volist=new ArrayList<BmsWarehouseConfigVo>();
			List<BmsWarehouseConfigEntity> list=bmsWarehouseConfigRepository.queryAll();
			for(BmsWarehouseConfigEntity entity:list){
				BmsWarehouseConfigVo voEntity=new BmsWarehouseConfigVo();
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
	public int saveEntity(BmsWarehouseConfigVo voEntity) throws Exception {
		String warehouseCode=voEntity.getWarehouseCode();
		try{
			BmsWarehouseConfigEntity entity=new BmsWarehouseConfigEntity();
			PropertyUtils.copyProperties(entity, voEntity);
			if(bmsWarehouseConfigRepository.checkConfigExist(warehouseCode)){
				return bmsWarehouseConfigRepository.updateEntity(entity);
			}else{
				return bmsWarehouseConfigRepository.insertEntity(entity);
			}
		}catch(Exception e){
		    logger.error("异常",e);
			throw e;
		}
		
			

	}

}
