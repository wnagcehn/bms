package com.jiuyescm.bms.general.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.general.service.SequenceService;


/**
 * 流水号获取service实现类
 * @author zhengyishan
 *
 */
@Service("sequenceService")
public class SequenceServiceImpl  extends MyBatisDao implements SequenceService {

	@Override
	public String getBillNoOne(String idName, String startName, String formatStr) {
		if(idName==null || startName==null){
			return null;
		}
		
		//取得开始流水
		long startNum = 0;
		try{
			startNum=this.getStartNum(idName, 1);
		}
		catch(Exception ex){
			String error = ex.getMessage();
		}
		DecimalFormat df = new DecimalFormat(formatStr); 
		String no="GETFALID";
        no=startName+df.format(startNum);
        return no;
	}

	@Override
	public List<String> getBillNoList(String idName, String startName, int size, String formatStr) {
		List<String> list=new ArrayList<String>();
		if(idName==null || startName==null || size==0){
			return null;
		}
		
		//取得开始流水
		long startNum=this.getStartNum(idName, size);
		DecimalFormat df = new DecimalFormat(formatStr); 
		String no="GETFALID";
		for(int i=0;i<size;i++){
			no=startName+df.format(startNum);
			list.add(no);
			startNum++;
		}
		return list;
	}
	
	private long getStartNum(String idName, long getNums) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idname_in", idName);
		map.put("nums_in", getNums);
		map.put("startnum_out", 0);
		//this.selectOneForObject("com.jiuyescm.bms.general.entity.SequenceEntityMapper.call_sequence", map);
		this.selectList("com.jiuyescm.bms.general.entity.SequenceEntityMapper.call_sequence", map);
		return (long)map.get("startnum_out");
	}

}
