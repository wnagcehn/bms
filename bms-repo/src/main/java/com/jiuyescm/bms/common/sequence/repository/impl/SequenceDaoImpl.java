package com.jiuyescm.bms.common.sequence.repository.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.common.sequence.repository.SequenceDao;

@SuppressWarnings("rawtypes")
@Repository("sequenceDao")
public class SequenceDaoImpl extends MyBatisDao implements SequenceDao {
	private Log log = LogFactory.getLog(SequenceDaoImpl.class);
	
	private static final String STR_FORMAT10 = "0000000000";  
//	
//	private static final String STR_FORMAT08="00000000";
	

	@Autowired
    private ISystemCodeRepository systemCodeRepository;

	@Override
	public long getStartNum(String idName, long getNums) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idname_in", idName);
		map.put("nums_in", getNums);
		map.put("startnum_out", 0);
		this.selectOneForObject("com.jiuyescm.bms.common.sequence.entity.SequenceEntityMapper.call_sequence", map);
		log.debug("开始流水:"+map.get("startnum_out"));
		return (long)map.get("startnum_out");
	}
    //批量获取单号
	@Override
	public List<String> getBillNoList(String idName,String startName,int size, String formatStr) {
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
	//单个生成单号
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
	
	public static void main(String args[]){
//		    Integer intHao =1; 
//		    intHao++; 
//		    DecimalFormat df = new DecimalFormat(STR_FORMAT10); 
//		    System.out.println(df.format(intHao));
		System.out.println(20/15);
		    
	}
	

}
