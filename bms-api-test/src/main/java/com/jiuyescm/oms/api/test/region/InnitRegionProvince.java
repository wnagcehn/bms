package com.jiuyescm.oms.api.test.region;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

public class InnitRegionProvince {
	
	public List<RegionProvince> queryRegionProvince() throws SQLException{  
		List<RegionProvince> listResult = new ArrayList<RegionProvince>();
		PreparedStatement psmt=MysqlConnUtils.CONN.prepareStatement("select t.province as provinceName from pub_region t where t.delflag = 0 group by t.province"); 
		ResultSet	rs = psmt.executeQuery();
		int i = 0;
		while (rs.next()) {  
			i++;
			String provinceCode = String.format("%02d", i)+"0000";
			System.out.println(provinceCode);
			RegionProvince syncLogEntity = new RegionProvince(provinceCode,rs.getString("provinceName"));
			listResult.add(syncLogEntity); 
		}
		return listResult; 
	} 
	
	public void deleteReionProvince() throws SQLException {
		PreparedStatement psmt =MysqlConnUtils.CONN.prepareStatement("delete from pub_region_province");
		psmt.executeUpdate();
	}
	
	public void insertRegionProvince(List<RegionProvince> listEntity) throws SQLException{
		PreparedStatement psmt =MysqlConnUtils.CONN.prepareStatement("insert into pub_region_province(province_code,province_name) VALUES(?,?)"); 
		for(RegionProvince entity : listEntity){
			psmt.setString(1, entity.getProvinceCode());
			psmt.setString(2, entity.getProvinceName());
			psmt.addBatch();          // 加入批量处理  
		} 
		psmt.executeBatch();   
	}
	
	public static void main(String[] org){
		InnitRegionProvince innit = new InnitRegionProvince();
		try {
			MysqlConnUtils.conn(); 
			List<RegionProvince> listResult = innit.queryRegionProvince();
			MysqlConnUtils.CONN.setAutoCommit(false); // 设置手动提交  
			innit.deleteReionProvince();
			innit.insertRegionProvince(listResult);
			MysqlConnUtils.CONN.commit();
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			MysqlConnUtils.close();
		}
		
	}
}
class RegionProvince{
	
	private String provinceCode;
	private String provinceName;
	
	public RegionProvince() {
		super(); 
	}

	public RegionProvince(String provinceCode, String provinceName) {
		super();
		this.provinceCode = provinceCode;
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
}
