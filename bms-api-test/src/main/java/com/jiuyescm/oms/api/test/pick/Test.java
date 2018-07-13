package com.jiuyescm.oms.api.test.pick;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap; 
import java.util.List;
import java.util.Map; 

public class Test {
	
	
	public List<Entity> queryQmSyncLogEntity(){  
		List<Entity> listResult = new ArrayList<Entity>();
		PreparedStatement psmt = null;
		ResultSet rs = null; 
		try {  
			String sql="select tt.do_no,tt.prdNum,tt.billTypeCode from ( "+
					"SELECT d.do_no, SUM(di.qty) prdNum,  "+
						" GROUP_CONCAT(di.product_id ORDER BY di.product_id)  billTypeCode  "+
							" FROM wms_do d  left JOIN wms_do_items di ON di.do_no = d.do_no   "+
								" WHERE d.operate_status = 'ACCEPT' AND d.exception_flag = 0  "+
									"	 AND d.suspend_flag = 0 and d.customer_id ='1100000622'  "+  
									"	 AND d.actual_carrier_code = '1500000016'   "+
									"	 and d.cre_time>='2016-11-11 00:00:00'  "+
								" GROUP BY di.do_no) tt where LENGTH(tt.billTypeCode) <=87 ";
			psmt=ConnUtils.CONN.prepareStatement(sql); 
 
			rs = psmt.executeQuery();
			while (rs.next()) {  
				Entity entity = new Entity(rs.getString("do_no") ,rs.getInt("prdNum"),rs.getString("billTypeCode"));  
				listResult.add(entity); 
			}
			return listResult;
		} catch (SQLException e) { 
			e.printStackTrace();
		} 
		return null;
	} 
	
	public void insert(String pickno,String orderno){
		PreparedStatement psmt = null;
		try {  
			String sql="insert into pick_data_temp(pickno,orderno) values(?,?)";
			psmt=ConnUtils.CONN.prepareStatement(sql); 
			psmt.setString(1, pickno);
			psmt.setString(2, orderno);
			psmt.executeUpdate();  
		} catch (SQLException e) { 
			e.printStackTrace();
		}  
	}

	public Map<String,Entity>  spilitAndhbOrd(List<Entity> list){
		Map<String,Entity> result = new HashMap<String,Entity>(); 
		for(Entity entity : list){
			if(result.containsKey(entity.getBillTypeCode())){
				Entity oEntity =  result.get(entity.getBillTypeCode());
				List<String> ordernos = oEntity.getOrdernos();
				ordernos.add(entity.getDo_no());
				oEntity.setOrdernos(ordernos);
				oEntity.setPrdNum(oEntity.getPrdNum() + entity.getPrdNum());
				result.put(entity.getBillTypeCode(), oEntity);
			}else{
				List<String> product = new ArrayList<String>(); 
				List<String> ordernos = new ArrayList<String>();
				if(entity.getBillTypeCode().indexOf(",") != -1) 
					Collections.addAll(product, entity.getBillTypeCode().split(",")); 
				else
					product.add(entity.getBillTypeCode());
				
				ordernos.add(entity.getDo_no());
				entity.setProducts(product);
				entity.setOrdernos(ordernos);
				result.put(entity.getBillTypeCode(), entity);
			} 
		}
		return result;
	}
	
	public Map<String,Entity> hb(Map<String,Entity> map){
		String rKey1 = null;
		String rKey2 = null;
		boolean havehb = false;
		for(String key : map.keySet()){   
			for(String key2 : map.keySet()){
				if(map.get(key2).getProducts().size() != map.get(key).getProducts().size() &&
						map.get(key2).getProducts().containsAll(map.get(key).getProducts())){//包含
					havehb = true;
					rKey1 = key;
					rKey2 = key2;
					break;
				} 
			}
			if(havehb){
				break;
			}
		}
		if(havehb){
			Entity hEntity1 = map.get(rKey1); 
			Entity hEntity2 = map.get(rKey2); 
			int allNum = hEntity1.getPrdNum()+ hEntity2.getPrdNum(); 
			hEntity2.setPrdNum(allNum);
			hEntity2.getOrdernos().addAll(hEntity1.getOrdernos()); 
			map.remove(rKey1);
			map.put(rKey2,hEntity2); 
			this.hb(map);
		}else{
			return map;
		} 
		return map;
	}
	/*
	public Map<String,Entity> hb2(Map<String,Entity> map){
		String rKey1 = null;
		String rKey2 = null;
		boolean havehb = false;
		List<String> hp = new ArrayList<>();
		for(String key : map.keySet()){   
			for(String key2 : map.keySet()){
				List<String> a = map.get(key2).getProducts();
				List<String> b = map.get(key).getProducts();
				a.addAll(b); 
				Set<String> set=new HashSet<String>();  
		        for (int i = 0; i < a.size(); i++) {   
		            set.add(a.get(i));             
		        }  
		        if(set.size() <= 5){
		        	hp.addAll(set);
		        	havehb = true;
					rKey1 = key;
					rKey2 = key2;
					break;
		        } 
			}
			if(havehb){
				break;
			}
		}
		if(havehb){
			Entity hEntity1 = map.get(rKey1); 
			Entity hEntity2 = map.get(rKey2);
			String doNos= hEntity1.getDoNos() +","+hEntity2.getDoNos();
			String customerId = hEntity2.getCustomerId(); 
			int allNum = hEntity1.getAllNum()+ hEntity2.getAllNum();
			String billTypeCode = hEntity2.getBillTypeCode();  
			Entity nEntity = new Entity(doNos, customerId, null, null, allNum,billTypeCode);
			nEntity.setProducts(hp);
			map.remove(rKey1);
			map.remove(rKey2);
			map.put(doNos, nEntity);
			this.hb2(map);
		}else{
			return map;
		} 
		return map;
	}*/
	public static void main(String[] org){ 
		Test ut = new Test();
		 try {
			ConnUtils.conn();
		} catch (SQLException e) {  
			e.printStackTrace();
			return;
		}
		 List<Entity> list = ut.queryQmSyncLogEntity();
		 ConnUtils.close(); 
		 System.out.println(list.size());
		 Map<String,Entity> map =ut.spilitAndhbOrd(list);
		 map = ut.hb(map);
		 System.out.println(map.size());
//		 int rows = 0;
//		 for(String key : map.keySet()){ 
//			 rows+=map.get(key).getOrdernos().size();
//		 }
//		 System.out.println(rows); 
		 try {
				ConnUtils.conn();
		 } catch (SQLException e) {  
			e.printStackTrace();
			return;
		 }
		 int i=0; 
		 for(String key : map.keySet()){ 
			 i++;
			 List<String> ordernos = map.get(key).getOrdernos();
			 for(String orderno : ordernos){ 
				ut.insert("MP"+i, orderno); 
			} 
		 } 
		 System.out.println("完成"); 
//		 System.out.println("合并商品数不大于8个");
//		 map = ut.hb2(map);
//		 System.out.println("size3:"+map.size());
//		 for(String key : map.keySet()){   
//			 System.out.println(map.get(key).toString());
//		 } 
		 //1973
		 //752
	}
	
}
class Entity{
	private String do_no;
	private int prdNum;
	private String billTypeCode; 
	private List<String> products;
	private List<String> ordernos;
	
	public Entity() {
		super(); 
	}
	 
	public Entity(String do_no, int prdNum, String billTypeCode) {
		super();
		this.do_no = do_no;
		this.prdNum = prdNum;
		this.billTypeCode = billTypeCode;
	}

	public String getDo_no() {
		return do_no;
	}

	public void setDo_no(String do_no) {
		this.do_no = do_no;
	}

	public int getPrdNum() {
		return prdNum;
	}

	public void setPrdNum(int prdNum) {
		this.prdNum = prdNum;
	}

	public String getBillTypeCode() {
		return billTypeCode;
	}

	public void setBillTypeCode(String billTypeCode) {
		this.billTypeCode = billTypeCode;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public List<String> getOrdernos() {
		return ordernos;
	}

	public void setOrdernos(List<String> ordernos) {
		this.ordernos = ordernos;
	}
 
	
}
