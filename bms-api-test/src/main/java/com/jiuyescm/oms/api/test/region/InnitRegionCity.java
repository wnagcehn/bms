package com.jiuyescm.oms.api.test.region;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnitRegionCity {

	public List<RegionCity> queryRegionCity() throws SQLException{  
		List<RegionCity> listResult = new ArrayList<RegionCity>();
		PreparedStatement psmt=MysqlConnUtils.CONN.prepareStatement("select t.province_code as provinceCode,t.province_name as provinceName,t2.city as cityName from pub_region_province t left join pub_region t2 on t.province_name = t2.province where t2.city != '市辖区' and t2.city != '市辖县' and t2.delflag = 0 group by t2.province,t2.city,t.province_code,t.province_name order by t.province_code asc"); 
		ResultSet rs = psmt.executeQuery();
		Map<String,List<RegionCity>> mapCity=new HashMap<String,List<RegionCity>>();
		while (rs.next()) {  
			RegionCity city = new RegionCity(rs.getString("provinceCode"),rs.getString("provinceName"),rs.getString("cityName"));
			List<RegionCity> list = null;
			if(mapCity.containsKey(city.getProvinceCode())){
				list = mapCity.get(city.getProvinceCode());
			}else{
				list = new ArrayList<RegionCity>();
			}
			list.add(city);
			mapCity.put(city.getProvinceCode(), list);
		}
		for(Object key : mapCity.keySet()){
			List<RegionCity> lists = mapCity.get(key);
			int i = 0;
			for(RegionCity city : lists){
				i++;
				String cityCode =city.getProvinceCode().substring(0, 2)+ String.format("%02d", i)+"00";
				System.out.println(cityCode);
				city.setCityCode(cityCode);
				listResult.add(city);
			} 
		}
		return listResult; 
	} 
	
	public void deleteReionCity() throws SQLException {
		PreparedStatement psmt =MysqlConnUtils.CONN.prepareStatement("delete from pub_region_city");
		psmt.executeUpdate();
	}
	
	public void insertRegionCity(List<RegionCity> listEntity) throws SQLException{
		PreparedStatement psmt =MysqlConnUtils.CONN.prepareStatement("insert into pub_region_city(parent_province_code,parent_province_name,city_code,city_name) values(?,?,?,?)"); 
		for(RegionCity entity : listEntity){
			psmt.setString(1, entity.getProvinceCode());
			psmt.setString(2, entity.getProvinceName());
			psmt.setString(3, entity.getCityCode());
			psmt.setString(4, entity.getCityName());
			psmt.addBatch();          // 加入批量处理  
		} 
		psmt.executeBatch();   
	}
	
	public static void main(String[] org){
		InnitRegionCity innit = new InnitRegionCity();
		try {
			MysqlConnUtils.conn(); 
			List<RegionCity> listResult = innit.queryRegionCity();
			MysqlConnUtils.CONN.setAutoCommit(false); // 设置手动提交  
			innit.deleteReionCity();
			innit.insertRegionCity(listResult);
			MysqlConnUtils.CONN.commit();
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			MysqlConnUtils.close();
		}
		
	}
}
class RegionCity{
	
	private String provinceCode;
	private String provinceName;
	private String cityCode;
	private String cityName;
	public RegionCity() {
		super(); 
	}
	public RegionCity(String provinceCode, String provinceName, String cityName) {
		super();
		this.provinceCode = provinceCode;
		this.provinceName = provinceName; 
		this.cityName = cityName;
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
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	} 
}