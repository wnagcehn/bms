package com.jiuyescm.bms.correct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsMaterialRepository;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.bms.correct.vo.BmsMarkingMaterialVo;
import com.jiuyescm.bms.correct.vo.BmsMaterialMarkOriginVo;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;

@Service("bmsProductsMaterialService")
public class BmsProductsMaterialServiceImp implements IBmsProductsMaterialService{
	
	private static final Logger logger = Logger.getLogger(BmsProductsMaterialServiceImp.class.getName());

	
	@Resource
	private IBmsProductsMaterialRepository bmsProductsMaterialRepository;
	
	@Override
	public List<BmsProductsMaterialAccountVo> queyAllMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=bmsProductsMaterialRepository.queyAllMax(condition);
		List<BmsProductsMaterialAccountVo> voList = new ArrayList<BmsProductsMaterialAccountVo>();
    	for(BmsProductsMaterialAccountEntity entity : list) {
    		BmsProductsMaterialAccountVo vo = new BmsProductsMaterialAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public List<BmsProductsMaterialAccountVo> queyAllPmxMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=bmsProductsMaterialRepository.queyAllPmxMax(condition);
		List<BmsProductsMaterialAccountVo> voList = new ArrayList<BmsProductsMaterialAccountVo>();
    	for(BmsProductsMaterialAccountEntity entity : list) {
    		BmsProductsMaterialAccountVo vo = new BmsProductsMaterialAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}


	@Override
	public List<BmsProductsMaterialAccountVo> queyAllZxMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=bmsProductsMaterialRepository.queyAllZxMax(condition);
		List<BmsProductsMaterialAccountVo> voList = new ArrayList<BmsProductsMaterialAccountVo>();
    	for(BmsProductsMaterialAccountEntity entity : list) {
    		BmsProductsMaterialAccountVo vo = new BmsProductsMaterialAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}
	
	@Override
	public List<BmsProductsMaterialAccountVo> queyAllBwxMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=bmsProductsMaterialRepository.queyAllBwxMax(condition);
		List<BmsProductsMaterialAccountVo> voList = new ArrayList<BmsProductsMaterialAccountVo>();
    	for(BmsProductsMaterialAccountEntity entity : list) {
    		BmsProductsMaterialAccountVo vo = new BmsProductsMaterialAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}
	
	@Override
	public List<BmsMarkingMaterialVo> queyNotMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsMarkingMaterialEntity> list=bmsProductsMaterialRepository.queyNotMax(condition);
		List<BmsMarkingMaterialVo> voList = new ArrayList<BmsMarkingMaterialVo>();
    	for(BmsMarkingMaterialEntity entity : list) {
    		BmsMarkingMaterialVo vo = new BmsMarkingMaterialVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}
	
	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxBwd(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BizOutstockPackmaterialEntity> list=bmsProductsMaterialRepository.queyNotMaxBwd(condition);
		return list;
	}
	
	@Override
	public List<BmsMarkingMaterialVo> queryMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsMarkingMaterialEntity> list=bmsProductsMaterialRepository.queryMark(condition);
		List<BmsMarkingMaterialVo> voList = new ArrayList<BmsMarkingMaterialVo>();
    	for(BmsMarkingMaterialEntity entity : list) {
    		BmsMarkingMaterialVo vo = new BmsMarkingMaterialVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}

	@Override
	public int updateMarkList(List<BmsMarkingMaterialVo> list) {
		// TODO Auto-generated method stub
		List<BmsMarkingMaterialEntity> enList=new ArrayList<BmsMarkingMaterialEntity>();
		for(BmsMarkingMaterialVo vo : list) {
			BmsMarkingMaterialEntity entity = new BmsMarkingMaterialEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败",ex);
            }
    		enList.add(entity);
    	}
		return bmsProductsMaterialRepository.updateMarkList(enList);
	}

	@Override
	public BmsMarkingMaterialVo queryMarkVo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsMarkingMaterialEntity entity=bmsProductsMaterialRepository.queryMarkVo(condition);
		try {
			BmsMarkingMaterialVo vo=new BmsMarkingMaterialVo();
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            logger.error("转换失败",ex);
        }
		return null;
	}

	@Override
	public int markMaterial(Map<String, Object> condition) {
		return bmsProductsMaterialRepository.markMaterial(condition);
	}

	@Override
	public int markPmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.markPmx(condition);
	}


	@Override
	public int markZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.markZx(condition);
	}
	
	
	@Override
	public int markBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.markBwd(condition);
	}
	
	@Override
	public List<BmsProductsMaterialAccountVo> queyMaterialCount(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=bmsProductsMaterialRepository.queyMaterialCount(condition);
		List<BmsProductsMaterialAccountVo> voList = new ArrayList<BmsProductsMaterialAccountVo>();
    	for(BmsProductsMaterialAccountEntity entity : list) {
    		BmsProductsMaterialAccountVo vo = new BmsProductsMaterialAccountVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}
	
	@Override
	public List<BmsMaterialMarkOriginVo> queryByMark(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsMaterialMarkOriginEntity> list=bmsProductsMaterialRepository.queryByMark(condition);
		List<BmsMaterialMarkOriginVo> voList = new ArrayList<BmsMaterialMarkOriginVo>();
    	for(BmsMaterialMarkOriginEntity entity : list) {
    		BmsMaterialMarkOriginVo vo = new BmsMaterialMarkOriginVo();
    		try {
                PropertyUtils.copyProperties(vo, entity);
            } catch (Exception ex) {
               logger.error("转换失败",ex);
            }
    		voList.add(vo);
    	}
		return voList;
	}


	@Override
	public int saveList(List<BmsProductsMaterialAccountVo> list) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> enList=new ArrayList<BmsProductsMaterialAccountEntity>();
		for(BmsProductsMaterialAccountVo vo : list) {
			BmsProductsMaterialAccountEntity entity = new BmsProductsMaterialAccountEntity();
    		try {
                PropertyUtils.copyProperties(entity, vo);
            } catch (Exception ex) {
                logger.error("转换失败",ex);
            }
    		enList.add(entity);
    	}
		return bmsProductsMaterialRepository.saveList(enList);
	}

	@Override
	public int saveMaterial(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveMaterial(condition);
	}

	
	@Override
	public int savePmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.savePmx(condition);
	}


	@Override
	public int saveZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveZx(condition);
	}
	
	@Override
	public BmsMarkingMaterialVo queryOneMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BmsMarkingMaterialEntity entity=bmsProductsMaterialRepository.queryOneMark(condition);
		try {
			BmsMarkingMaterialVo vo=new BmsMarkingMaterialVo();
            PropertyUtils.copyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            logger.error("转换失败",ex);
        }
		return null;
	}

	@Override
	public int deleteMarkMaterialByWaybillNo(List<String> waybillNoList) {
		
		return bmsProductsMaterialRepository.deleteMarkMaterialByWaybillNo(waybillNoList);
	}

	@Override
	public int updateMaterialAccount(Map<String,Object> condition) {
		// TODO Auto-generated method stub

		return bmsProductsMaterialRepository.updateMaterialAccount(condition);
	}

	@Override
	public int saveBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveBwd(condition);
	}


	@Override
	public BizOutstockPackmaterialEntity queryBwdMaterial(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.queryBwdMaterial(condition);
	}


	@Override
	public Map<String, String> getMaterialMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.getMaterialMap(condition);
	}


	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxMaterial(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.queyNotMaxMaterial(condition);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxPmx(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.queyNotMaxPmx(condition);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxZx(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.queyNotMaxZx(condition);
	}
	

	@Override
	public int saveMarkMaterial(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveMarkMaterial(condition);
	}

	@Override
	public int saveMarkPmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveMarkPmx(condition);
	}

	@Override
	public int saveMarkZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveMarkZx(condition);
	}

	@Override
	public int saveMarkBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.saveMarkBwd(condition);
	}

	@Override
	public int updatePmxzxMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.updatePmxzxMark(waybillNoList);
	}


	@Override
	public int updateBwdMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.updateBwdMark(waybillNoList);
	}

	@Override
	public int updatePmxMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.updatePmxMark(waybillNoList);
	}

	@Override
	public int updateZxMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		return bmsProductsMaterialRepository.updateZxMark(waybillNoList);
	}





}
