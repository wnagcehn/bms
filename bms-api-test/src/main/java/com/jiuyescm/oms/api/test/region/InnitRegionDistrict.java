package com.jiuyescm.oms.api.test.region;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnitRegionDistrict {
	
	public List<RegionDistrict> queryRegionDistrict() throws SQLException{  
		List<RegionDistrict> listResult = new ArrayList<RegionDistrict>();
		PreparedStatement psmt=MysqlConnUtils.CONN.prepareStatement("select t.city_code as cityCode,t.city_name as cityName,t2.district as districtName from pub_region_city t left join pub_region t2 on t.parent_province_name = t2.province and t.city_name = t2.city where t2.city != '市辖区' and t2.city != '市辖县'  and t2.delflag = 0 group by t2.province,t2.city,t2.district,t.city_code,t.city_name order by t.city_code asc"); 
		
		ResultSet rs = psmt.executeQuery();
		Map<String,List<RegionDistrict>> mapCity=new HashMap<String,List<RegionDistrict>>();
		while (rs.next()) {  
			RegionDistrict district = new RegionDistrict(rs.getString("cityCode"),rs.getString("cityName"),rs.getString("districtName"));
			List<RegionDistrict> list = null;
			if(mapCity.containsKey(district.getCityCode())){
				list = mapCity.get(district.getCityCode());
			}else{
				list = new ArrayList<RegionDistrict>();
			}
			list.add(district);
			mapCity.put(district.getCityCode(), list);
		}
		for(Object key : mapCity.keySet()){
			List<RegionDistrict> lists = mapCity.get(key);
			int i = 0;
			for(RegionDistrict district : lists){
				i++;
				String districtCode =district.getCityCode().substring(0, 4)+ String.format("%02d", i);
				System.out.println(districtCode);
				district.setDistrictCode(districtCode);
				listResult.add(district);
			} 
		}
		return listResult; 
	} 
	
	public void deleteReionDistrict() throws SQLException {
		PreparedStatement psmt =MysqlConnUtils.CONN.prepareStatement("delete from pub_region_district");
		psmt.executeUpdate();
	}
	
	public void insertRegionDistrict(List<RegionDistrict> listEntity) throws SQLException{
		PreparedStatement psmt =MysqlConnUtils.CONN.prepareStatement("insert into pub_region_district(parent_city_code,parent_city_name,district_code,district_name) values(?,?,?,?)"); 
		for(RegionDistrict entity : listEntity){
			psmt.setString(1, entity.getCityCode());
			psmt.setString(2, entity.getCityName());
			psmt.setString(3, entity.getDistrictCode());
			psmt.setString(4, entity.getDistrictName());
			psmt.addBatch();          // 加入批量处理  
		} 
		psmt.executeBatch();   
	}
	
	public static void main(String[] org){
		InnitRegionDistrict innit = new InnitRegionDistrict();
		try {
			MysqlConnUtils.conn(); 
			List<RegionDistrict> listResult = innit.queryRegionDistrict();
			MysqlConnUtils.CONN.setAutoCommit(false); // 设置手动提交  
			innit.deleteReionDistrict();
			innit.insertRegionDistrict(listResult);
			MysqlConnUtils.CONN.commit();
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			MysqlConnUtils.close();
		}
		
	}
}
class RegionDistrict{
	 
	private String cityCode;
	private String cityName;
	private String districtCode;
	private String districtName;
	
	public RegionDistrict() {
		super(); 
	}

	public RegionDistrict(String cityCode, String cityName, String districtName) {
		super();
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.districtName = districtName;
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

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	 
}