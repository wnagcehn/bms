package com.jiuyescm.bms.biz.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizTihuoBillRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizTihuoBillRepository")
@SuppressWarnings("rawtypes")
public class BizTihuoBillRepositoryImp extends MyBatisDao implements IBizTihuoBillRepository{

	public BizTihuoBillRepositoryImp() {
		super();
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public PageInfo<BizTihuoBillEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizTihuoBillEntity> list = selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizTihuoBillEntity> pageInfo = new PageInfo<BizTihuoBillEntity>(list);
        return pageInfo;
    }

    @Override
    public BizTihuoBillEntity findById(Long id) {
        BizTihuoBillEntity entity = (BizTihuoBillEntity) selectOne("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.findById", id);
        return entity;
    }

	@Override
	@SuppressWarnings("unchecked")
    public BizTihuoBillEntity save(BizTihuoBillEntity entity) {
        insert("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.save", entity);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BizTihuoBillEntity update(BizTihuoBillEntity entity) {
        update("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.delete", id);
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<BizTihuoBillEntity> countByDate(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BizTihuoBillEntity> pList=selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.countByDate", condition);
		return pList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int insertBillList(List<BizTihuoBillEntity> list) {
		// TODO Auto-generated method stub
		int num=insertBatch("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.save", list);
		return num;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BizTihuoBillEntity> queryData(Map<String, Object> condition) {
		// TODO Auto-generated method stub
        List<BizTihuoBillEntity> list = selectList("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.queryData", condition);
		return list;
	}

	@Override
	public int deleteBizByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.biz.dispatch.mapper.BizTihuoBillMapper.deleteBizByMap", condition);
	}

}
