package com.jiuyescm.oms.api.test.edb; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;  

import com.jiuyescm.oms.api.test.common.ConnUtils;
import com.jiuyescm.oms.api.test.common.EDBMakeSign; 

public class EDBTestReceiveRunsUtils {
	
	public static final String secretKey="6ef75f4412543f44e4d29ef1ba472702";
	
	public static final String method_RequesGoodsInfoToOMS ="RequestOrdersToOMS";
	
	public static final String method_RequesInStorageToOMS ="RequestOrdersToOMS";
	
	public static final String method_RequestOrdersToOMS ="RequestOrdersToOMS";
	
	public static final String method_RequesUpdateOrderStateToOMS ="RequesUpdateOrderStateToOMS"; 

	public static final String apiUrl="http://api.test.jiuyescm.com/apiman-gateway/edb/receive/1.0"; 
	
	public static final int pageSize = 50; 
	
	public List<EdbReceiveEBPInfoEntity> queryReceiveEBPInfoEntity(String method,String customer,int startIndex){  
		List<EdbReceiveEBPInfoEntity> listResult = new ArrayList<EdbReceiveEBPInfoEntity>();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try { 
			psmt = ConnUtils.CONN.prepareStatement("SELECT TOP(?) * FROM OMS_ReceiveEBPInfo where InterfaceType =(?) and DCode = (?) AND ID<( SELECT min(ID) FROM (SELECT TOP(?) ID FROM OMS_ReceiveEBPInfo where InterfaceType =(?) and DCode = (?)  ORDER BY ID desc) AS TabTemp) ORDER BY ID DESC");
			psmt.setInt(1, pageSize);
			psmt.setString(2, method); 
			psmt.setString(3, customer);
			psmt.setInt(4, startIndex);
			psmt.setString(5, method); 
			psmt.setString(6, customer);
			rs = psmt.executeQuery();
			while (rs.next()) {  
				EdbReceiveEBPInfoEntity receiveEBPInfoEntiy = new EdbReceiveEBPInfoEntity(rs.getInt("id"), 
						rs.getString("JsonStr"), rs.getString("ClientCode"), rs.getString("InterfaceType"),
						rs.getString("DCode"),null); 
				listResult.add(receiveEBPInfoEntiy); 
			}
			return listResult;
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		return null;
	}  
	 
 
	
	private String doPost(String urlStr,String xmlInfo) {
		StringBuffer result = new StringBuffer(); 
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream()); 
			out.write(new String(xmlInfo.getBytes("UTF-8")));
			out.flush();
			out.close();	
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream())); 
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				result.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result.toString();
	} 
	
	public void run(String method,String customerId,int startIndex,int endIndex){ 
		
		while(endIndex > startIndex){
			List<EdbReceiveEBPInfoEntity> resultData = this.queryReceiveEBPInfoEntity(method, customerId, startIndex);
			if(null != resultData && !resultData.isEmpty()){
				for(EdbReceiveEBPInfoEntity sync : resultData){ 
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd%HH:mm:ss");
					String stDate = df.format(new Date()); 
					String sign=EDBMakeSign.sign(stDate, secretKey); 
					String urlStr = apiUrl+"?method="+sync.getInterfaceType()+"&timestamp="+stDate+"&sign="+sign;
					String result = this.doPost(urlStr,sync.getJsonStr());
					System.out.println(result);
					System.out.println();
				}
			}  
			startIndex += pageSize;
		} 
	}
	
	public static void main(String[] org){
		/*
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
		String body="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><request><entryOrder><entryOrderCode>r16092700002</entryOrderCode><ownerCode>JIUYETEST</ownerCode><warehouseCode>B01</warehouseCode><entryOrderId>120000000772</entryOrderId><entryOrderType>3</entryOrderType><outBizCode>120000000772</outBizCode><confirmType>0</confirmType><status>FULFILLED</status><operateTime></operateTime><remark></remark></entryOrder><orderLines><orderLine><outBizCode>120000000772null1</outBizCode><orderLineNo></orderLineNo><ownerCode>JIUYETEST</ownerCode><itemCode>1101000010001</itemCode><itemId>1700005245</itemId><itemName></itemName><inventoryType></inventoryType><planQty>10</planQty><actualQty>10</actualQty><batchCode></batchCode><productDate></productDate><expireDate></expireDate><produceCode>19000101</produceCode><remark></remark></orderLine></orderLines></request>";
		String appKey="jiuye";
		String format="xml";
		String method="edb.entryorder.confirm";
		String sign_method="md5";
		String timestamp =df.format(new Date());
		String token = "JIUYETEST";
		String v = "2.0";
		String secret = "fcf9b138-2b35-4bc8-8920-e9a8f416d8c7";
		String sign = EDBMakeSign.sign(timestamp,secret);
		String url="http://vip26.edb08.com.cn/qimenrouter/router/jyapi.aspx?appkey="+appKey
				+"&format="+format+"&method="+method+"&sign_method="+sign_method+"&timestamp="+timestamp
				+"&token="+token+"&v="+v+"&sign="+sign;
		System.out.println(url);
		try {
			body = URLEncoder.encode(body, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		body ="method="+method+"&format="+format+"&appkey="+appKey+"&v="+v+"&sign="+sign+"&sign_method="+sign_method+
				"&timestamp="+timestamp+"&token="+token+"&businessdata="+body;
		System.out.println(body);
		*/
		/*
		String apiUrl="http://apix.jiuyescm.com/webOMSReceive/Receive.aspx";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stDate = df.format(new Date()); 
		String sign=EDBMakeSign.sign(stDate, "cfe5540b00eea178"); 
		String urlStr = apiUrl+"?method=RequestOrdersToOMS&timestamp="+stDate+"&sign="+sign+"&appkey=jiuye&customerId=KEQ"; */
		String sign=EDBMakeSign.sign("1476742333", "fcf9b138-2b35-4bc8-8920-e9a8f416d8c7"); 
		System.out.println(sign);
	}
}
