package com.jiuyescm.bms.base.servicetype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.base.servicetype.repository.IPubCarrierServicetypeRepository;
import com.jiuyescm.bms.base.servicetype.service.ICarrierProductService;
import com.jiuyescm.bms.base.servicetype.vo.CarrierProductVo;
import com.jiuyescm.constants.RedisCache;
import com.jiuyescm.framework.redis.callback.GetDataCallBack;
import com.jiuyescm.framework.redis.client.IRedisClient;

/**
 * ..ServiceImpl
 * @author wangchen&liuzhicheng
 * 
 */
@Service("carrierProductService")
public class PubCarrierServicetypeServiceImpl implements ICarrierProductService {
	
	private static Logger logger = LoggerFactory.getLogger(PubCarrierServicetypeServiceImpl.class);

	@Autowired
    private IPubCarrierServicetypeRepository pubCarrierServicetypeRepository;
	@Autowired private IRedisClient redisClient;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public CarrierProductVo findById(Long id) throws Exception{
		CarrierProductVo vo = new CarrierProductVo();
		PubCarrierServicetypeEntity entity = pubCarrierServicetypeRepository.findById(id);
		try {
			PropertyUtils.copyProperties(vo, entity);
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
			throw e;
		}
        return  vo;
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<CarrierProductVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception{
    	PageInfo<PubCarrierServicetypeEntity> entityPageInfo = pubCarrierServicetypeRepository.query(condition, pageNo, pageSize);
    	PageInfo<CarrierProductVo> voPageInfo = new PageInfo<CarrierProductVo>();
    	try {
			if(entityPageInfo!=null&&entityPageInfo.getList().size()>0){
				List<CarrierProductVo> list=new ArrayList<CarrierProductVo>();
				for(PubCarrierServicetypeEntity entity:entityPageInfo.getList()){
					CarrierProductVo vo=new CarrierProductVo();
					PropertyUtils.copyProperties(vo, entity);
					list.add(vo);
				}
				voPageInfo.setList(list);
			}
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
			throw e;
		}
        return voPageInfo;
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<CarrierProductVo> query(Map<String, Object> condition) throws Exception{
		List<CarrierProductVo> voList= new ArrayList<CarrierProductVo>();
		try{
			List<PubCarrierServicetypeEntity> entityList=pubCarrierServicetypeRepository.query(condition);
			for(PubCarrierServicetypeEntity entity:entityList){
				CarrierProductVo vo=new CarrierProductVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("转换失败:{}",e);
			throw e;
		}
		return voList;

	}
	
	/**
	 * 保存
	 * @param vo
	 * @return
	 */
    @Override
    public CarrierProductVo save(CarrierProductVo vo) throws Exception{
    	CarrierProductVo vo2 = new CarrierProductVo();
    	PubCarrierServicetypeEntity entity = new PubCarrierServicetypeEntity();
    	try {
			PropertyUtils.copyProperties(entity, vo);
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
			throw e;
		}
    	PubCarrierServicetypeEntity entity2 = pubCarrierServicetypeRepository.save(entity);
    	try {
			PropertyUtils.copyProperties(vo2, entity2);
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
			throw e;
		}
        return vo2;
    }

	/**
	 * 更新
	 * @param vo
	 * @return
	 */
    @Override
    public CarrierProductVo update(CarrierProductVo vo) throws Exception{
    	CarrierProductVo vo2 = new CarrierProductVo();
    	PubCarrierServicetypeEntity entity = new PubCarrierServicetypeEntity();
    	try {
			PropertyUtils.copyProperties(entity, vo);
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
			throw e;
		}
    	PubCarrierServicetypeEntity entity2 = pubCarrierServicetypeRepository.update(entity);
    	try {
			PropertyUtils.copyProperties(vo2, entity2);
		} catch (Exception e) {
			logger.error("转换失败:{}",e);
			throw e;
		}
        return vo2;
    }
	
    @Override
    public List<CarrierProductVo> queryByCarrierid(String carrierid) throws Exception{
		List<CarrierProductVo> voList= new ArrayList<CarrierProductVo>();
		try{
			List<PubCarrierServicetypeEntity> entityList=pubCarrierServicetypeRepository.queryByCarrierid(carrierid);
			for(PubCarrierServicetypeEntity entity:entityList){
				CarrierProductVo vo=new CarrierProductVo();
				PropertyUtils.copyProperties(vo, entity);
				voList.add(vo);
			}
		}catch(Exception e){
			logger.error("转换失败:{}",e);
			throw e;
		}
    	return voList;
    }

	@Override
	public String getCarrierNameById(final String carrierid, final String servicecode)
			throws Exception {
		String code = carrierid + servicecode;
		String result ="";
		CarrierProductVo resultVo = redisClient.get(code, RedisCache.CARRIERPRODUCTCODE_SPACE,CarrierProductVo.class, new GetDataCallBack<CarrierProductVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.fiveMinutes;
			}

			@Override
			public CarrierProductVo invoke() {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("carrierid", carrierid);
				condition.put("servicecode", servicecode);
				condition.put("delflag", "0");
				List<CarrierProductVo> list = new ArrayList<CarrierProductVo>();
								
				
				try{
					list = query(condition);
				}
				catch(Exception ex){
					
				}
				if(list!=null && list.size()>0){
					return list.get(0);
				}
				else{
					return null;
				}
			}
		});

		if(resultVo!=null){
			result += resultVo.getCarriername();
		}
		return result;
	}

	@Override
	public CarrierProductVo findByCode(final String carrierid, final String servicecode)
			throws Exception {
		String code = carrierid + servicecode;
		CarrierProductVo resultVo = redisClient.get(code, RedisCache.CARRIERPRODUCTCODE_SPACE,CarrierProductVo.class, new GetDataCallBack<CarrierProductVo>(){

			@Override
			public int getExpiredTime() {
				return RedisCache.fiveMinutes;
			}

			@Override
			public CarrierProductVo invoke() {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("carrierid", carrierid);
				condition.put("servicecode", servicecode);
				condition.put("delflag", "0");
				List<CarrierProductVo> list = new ArrayList<CarrierProductVo>();
								
				
				try{
					list = query(condition);
				}
				catch(Exception ex){
					
				}
				if(list!=null && list.size()>0){
					return list.get(0);
				}
				else{
					return null;
				}
			}
		});
		return resultVo;
	}
    
    
}
