package com.jiuyescm.oms.api.test.pick;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

public class ReductionReference {

	public List<String> queryTempEntity(){  
		List<String> listResult = new ArrayList<String>();
		PreparedStatement psmt = null;
		ResultSet rs = null; 
		try {  
			String sql="select t.orderno from pick_data_temp t where t.pickno in( "+
					"'MP3','MP7','MP15','MP1','MP5','MP8','MP9','MP27','MP4','MP14','MP41','MP2','MP40','MP65', "+
					"'MP99','MP24','MP227','MP53','MP107','MP152','MP46','MP62','MP222','MP199','MP25','MP141', "+
					"'MP28','MP33','MP63','MP181','MP96','MP203','MP29','MP61','MP30','MP233','MP220','MP121', "+
					"'MP18','MP240') GROUP BY t.orderno";
			psmt=ConnUtils.CONN.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {  
				listResult.add(rs.getString("orderno")); 
			}
			return listResult;
		} catch (SQLException e) { 
			e.printStackTrace();
		} 
		return null;
	} 
	
	public List<OrderNoReferenceEntity> queryEntity(List<String> ordernos){
		List<OrderNoReferenceEntity> result = new ArrayList<OrderNoReferenceEntity>();
		List<List<String>> splitList = splitList(ordernos, 50);
		PreparedStatement psmt = null;
		ResultSet rs = null; 
		for(List<String> list : splitList){
			String sql = "select t.orderno,t.reference from oms_orderinfo t where t.orderno in (";
			for(String orderno : list){
				sql+= "'"+orderno+"',";
			}
			sql = sql.substring(0,sql.lastIndexOf(","))+")"; 
			try {   
				psmt=OmsConnUtils.CONN.prepareStatement(sql);
				rs = psmt.executeQuery();
				while (rs.next()) {  
					result.add(new OrderNoReferenceEntity(rs.getString("orderno"),rs.getString("reference")));
					System.out.println(rs.getString("orderno")+"---"+rs.getString("reference"));
				} 
				psmt.close();
				rs.close();
			} catch (SQLException e) { 
				e.printStackTrace();
				psmt=null;
				rs=null;
			}
		}
		return result;
	}
	
	public void batchUpdateDo(List<OrderNoReferenceEntity> listEntity){
		PreparedStatement psmt = null; 
		try {  
			String sql="update wms_do t set t.reference_no = ? where t.do_no = ?"; 
			ConnUtils.CONN.setAutoCommit(false);
			psmt=ConnUtils.CONN.prepareStatement(sql);
			for(OrderNoReferenceEntity entity : listEntity){ 
				psmt.setString(1, entity.getReference());
				psmt.setNString(2, entity.getOrderno());
				psmt.addBatch();
			} 
			psmt.executeBatch(); 
			ConnUtils.CONN.commit();
			psmt.close();
		} catch (SQLException e) { 
			e.printStackTrace();
			psmt= null;
		} 
	}
	
	public static void main(String[] org){
		/*
		ReductionReference r = new ReductionReference();
		try {
			ConnUtils.conn();
		} catch (SQLException e) {  
			e.printStackTrace();
			return;
		}
		List<String> listOrder= r.queryTempEntity();
		ConnUtils.close();
		try {
			OmsConnUtils.conn();
		} catch (SQLException e) {  
			e.printStackTrace();
			return;
		}
		List<OrderNoReferenceEntity> listEntity = r.queryEntity(listOrder);
		OmsConnUtils.close();
		
		try {
			ConnUtils.conn();
		} catch (SQLException e) {  
			e.printStackTrace();
			return;
		}
		r.batchUpdateDo(listEntity);
		ConnUtils.close();
		*/
		
	}
	
	public <T> List<List<T>> splitList(List<T> list, int pageSize) {

		int listSize = list.size();
		int page = (listSize + (pageSize - 1)) / pageSize;

		List<List<T>> listArray = new ArrayList<List<T>>();
		for (int i = 0; i < page; i++) {
			List<T> subList = new ArrayList<T>();
			for (int j = 0; j < listSize; j++) {
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
				if (pageIndex == (i + 1)) {
					subList.add(list.get(j));
				}
				if ((j + 1) == ((j + 1) * pageSize)) {
					break;
				}
			}
			listArray.add(subList);
		}
		return listArray;
	}
	
	
}
class OrderNoReferenceEntity{
	private String orderno;
	private String reference;
	
	public OrderNoReferenceEntity() {
		super(); 
	}
	
	public OrderNoReferenceEntity(String orderno, String reference) {
		super();
		this.orderno = orderno;
		this.reference = reference;
	}

	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
