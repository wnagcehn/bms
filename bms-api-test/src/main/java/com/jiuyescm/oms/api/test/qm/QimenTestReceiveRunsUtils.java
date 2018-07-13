package com.jiuyescm.oms.api.test.qm; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;  

import com.jiuyescm.oms.api.test.common.ConnUtils;
import com.jiuyescm.oms.api.test.common.QimenSign;
public class QimenTestReceiveRunsUtils {
	
	public static final String appKey = "23301412";
	
	public static final String secretKey="6ef75f4412543f44e4d29ef1ba472702";//"sandbox412543f44e4d29ef1ba472702";
	
	public static final String method_singleitem_synchronize ="singleitem.synchronize";
	
	public static final String method_entryorder_create ="entryorder.create";
	
	public static final String method_returnorder_create ="returnorder.create";
	
	public static final String method_stockout_create ="stockout.create";
	
	public static final String method_deliveryorder_create ="deliveryorder.create"; 
	
	//public static final String apiUrl="http://apix.jiuyescm.com/webQMReceive/Receive.aspx"; 
	//public static final String apiUrl="http://api.test.jiuyescm.com/webQMReceive/Receive.aspx"; 
	//public static final String apiUrl="http://192.168.0.39:8080/apiman-gateway/qimen/receive/1.0";
	public static final String apiUrl = "http://121.40.155.209:18080/apiman-gateway/qimen/receive/1.0";
	
	public static final int pageSize = 50;
	
	
	public List<QmSyncLogEntity> queryQmSyncLogEntity(String method,String customer,int startIndex){ 
		 
		List<QmSyncLogEntity> listResult = new ArrayList<QmSyncLogEntity>();
		PreparedStatement psmt = null;
		ResultSet rs = null; 
		try { 
			psmt=ConnUtils.CONN.prepareStatement("select top(?) * from dbo.OMS_QMSyncLog t where t.ID not in( select top(?) t2.ID from dbo.OMS_QMSyncLog t2 where t2.method = ? and t2.customerid=? order by t2.createtime asc) and t.method = ? and t.customerid=? order by t.createtime asc");
			psmt.setInt(1, pageSize);
			psmt.setInt(2, startIndex);
			psmt.setString(3, method); 
			psmt.setString(4, customer);
			psmt.setString(5, method); 
			psmt.setString(6, customer);
			rs = psmt.executeQuery();
			while (rs.next()) {  
				QmSyncLogEntity syncLogEntity = new QmSyncLogEntity(rs.getInt("id"),rs.getString("method"),rs.getString("format"), 
						rs.getString("app_key"), rs.getString("v"), rs.getString("sign"),
						rs.getString("sign_method"), rs.getString("customerid"), rs.getString("content"), rs.getDate("createtime"));
				listResult.add(syncLogEntity); 
			}
			return listResult;
		} catch (SQLException e) { 
			e.printStackTrace();
		} 
		return null;
	} 
	
	
	public String toUrl(String apiUrl,QmSyncLogEntity sync){  
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
		String stDate = df.format(date);  
		 
		String url = apiUrl + "?method="+sync.getMethod()+"&timestamp="+stDate+""
						+ "&format="+sync.getFormat()+"&app_key="+sync.getApp_key()+""
								+ "&v="+sync.getV()+"&sign="+sync.getSign()+"&sign_method="+sync.getSign_method()+""
										+ "&customerId="+sync.getCustomerid();
		return url;
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
			System.out.println("URL:"+urlStr);
			System.out.println("XML_DATA:"+xmlInfo);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("URL:"+urlStr);
			System.out.println("XML_DATA:"+xmlInfo);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("URL:"+urlStr);
			System.out.println("XML_DATA:"+xmlInfo);
			e.printStackTrace();
		} 
		return result.toString();
	} 
	
	public void run(String method,String customerId,int startIndex,int endIndex){ 
		
		while(endIndex > startIndex){
			List<QmSyncLogEntity> resultData = this.queryQmSyncLogEntity(method, customerId, startIndex);
			if(null != resultData && !resultData.isEmpty()){
				for(QmSyncLogEntity sync : resultData){ 
					sync.setApp_key(appKey);
					String urlStr = this.toUrl(apiUrl,sync);
					String sign=QimenSign.sign(urlStr, sync.getContent(), secretKey);
					sync.setSign(sign);
					urlStr=this.toUrl(apiUrl,sync);
//					System.out.println(urlStr);
					String result = this.doPost(urlStr,sync.getContent());
					//System.out.println(result);
					//System.out.println();
				}
			}  
			startIndex += pageSize;
		} 
	}
	
	public static void main(String[] org){
		 
	    /*
	    String apiUrl="http://apix.jiuyescm.com/webQMReceive/Receive.aspx";
		Date date =new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
		String stDate = df.format(new Date());  
		QmSyncLogEntity sync= new QmSyncLogEntity(1,"deliveryorder.create","xml","23301412","2.0","","md5","jiuye-test","",date);
		String url = apiUrl + "?method="+sync.getMethod()+"&timestamp="+stDate+""
						+ "&format="+sync.getFormat()+"&app_key="+sync.getApp_key()+""
								+ "&v="+sync.getV()+"&sign_method="+sync.getSign_method()+""
										+ "&customerId="+sync.getCustomerid();
		//String body="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><actionType>add</actionType><warehouseCode>B01</warehouseCode><ownerCode>jiuye-test</ownerCode><supplierCode></supplierCode><supplierName></supplierName><item><itemCode>jiuyeTest001</itemCode><itemId></itemId><itemName>久耶测试商品001</itemName><shortName>久耶测试1</shortName><englishName></englishName><barCode>374632</barCode><skuProperty></skuProperty><stockUnit></stockUnit><color></color><size></size><title></title><categoryId></categoryId><categoryName></categoryName><pricingCategory></pricingCategory><safetyStock>0</safetyStock><itemType>ZC</itemType><seasonCode></seasonCode><seasonName></seasonName><brandCode></brandCode><brandName></brandName><isSNMgmt>N</isSNMgmt><isShelfLifeMgmt>N</isShelfLifeMgmt><rejectLifecycle>0</rejectLifecycle><lockupLifecycle>0</lockupLifecycle><adventLifecycle>0</adventLifecycle><isBatchMgmt>N</isBatchMgmt><batchCode></batchCode><batchRemark></batchRemark><packCode></packCode><pcs></pcs><originAddress></originAddress><approvalNumber></approvalNumber><isFragile>N</isFragile><isHazardous>N</isHazardous><remark>28407849</remark><isValid>Y</isValid><isSku>Y</isSku><packageMaterial></packageMaterial></item></request>";
		//String body="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><entryOrder><entryOrderCode>jiuyeTest20160926001</entryOrderCode><ownerCode>jiuye-test</ownerCode><warehouseCode>B01</warehouseCode><orderType>CGRK</orderType><logisticsCode>OTHER</logisticsCode><logisticsName></logisticsName><expressCode></expressCode><supplierCode></supplierCode><supplierName></supplierName><operatorCode></operatorCode><operatorName></operatorName><senderInfo><company></company><name>久耶测试商品001</name><zipCode>100866</zipCode><tel></tel><mobile></mobile><email></email><countryCode></countryCode><province>北京</province><city>北京市</city><area>顺义区</area><town></town><detailAddress>北京市顺义区</detailAddress></senderInfo><receiverInfo><company></company><name>九曳北京仓</name><zipCode></zipCode><tel></tel><mobile>021-56668298</mobile><email></email><countryCode></countryCode><province>北京</province><city>北京市</city><area>顺义区</area><town></town><detailAddress>发货地址发货地址</detailAddress></receiverInfo><remark></remark></entryOrder><orderLines><orderLine><ownerCode>jiuye-test</ownerCode><orderLineNo>1</orderLineNo><itemCode>jiuyeTest001</itemCode><itemId></itemId><itemName></itemName><planQty>50</planQty><skuProperty></skuProperty><purchasePrice>0.0</purchasePrice><retailPrice>0.0</retailPrice><inventoryType>ZP</inventoryType><produceCode></produceCode><batchCode></batchCode><extendProps></extendProps></orderLine></orderLines></request>";
		String body="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><deliveryOrder><deliveryOrderCode>jiuyeTestC20160926001</deliveryOrderCode><orderType>PTCK</orderType><warehouseCode>B01</warehouseCode><ownerCode>jiuye-test</ownerCode><orderFlag></orderFlag><sourcePlatformCode>OTHER</sourcePlatformCode><sourcePlatformName></sourcePlatformName><createTime>2016-09-25 16:58:13</createTime><placeOrderTime>2016-09-25 15:58:49</placeOrderTime><payTime>2016-09-25 13:50:28</payTime><payNo></payNo><operatorCode></operatorCode><operatorName></operatorName><operateTime>2016-09-25 16:58:13</operateTime><shopNick>JIUYE-拼多多</shopNick><sellerNick></sellerNick><buyerNick></buyerNick><logisticsCode>JIUYE</logisticsCode><logisticsName></logisticsName><expressCode></expressCode><logisticsAreaCode></logisticsAreaCode><deliveryRequirements><scheduleType>0</scheduleType><deliveryType></deliveryType></deliveryRequirements><senderInfo><company></company><name>九曳北京仓</name><zipCode>100866</zipCode><tel></tel><mobile>021-56668298</mobile><email></email><countryCode></countryCode><province>北京</province><city>北京市</city><area>顺义区</area><town></town><detailAddress>发货地址发货地址</detailAddress></senderInfo><receiverInfo><company></company><name>杨福泉</name><zipCode>null</zipCode><tel></tel><mobile>13920547741</mobile><email></email><countryCode></countryCode><province>天津</province><city>天津市</city><area>塘沽区</area><town></town><detailAddress>天津天津塘沽区天津市塘沽区新北路4668号创新创业园28栋2楼A座天津阿部配线有限公司</detailAddress></receiverInfo><isUrgency>N</isUrgency><invoiceFlag>N</invoiceFlag><insuranceFlag>否</insuranceFlag><insurance><type></type><amount>0.0</amount></insurance><buyerMessage></buyerMessage><sellerMessage></sellerMessage></deliveryOrder><orderLines><orderLine><orderLineNo></orderLineNo><sourceOrderCode>160925-76057369534</sourceOrderCode><subSourceOrderCode></subSourceOrderCode><ownerCode>jiuye-test</ownerCode><itemCode>jiuyeTest001</itemCode><itemId></itemId><itemName></itemName><extCode></extCode><planQty>2</planQty><actualPrice>7.25</actualPrice></orderLine></orderLines></request>";
		String sign=QimenSign.sign(url, body, "6ef75f4412543f44e4d29ef1ba472702");
	    url = url+ "&sign="+sign; 
	    System.out.println(sign);
	    System.out.println(url);
	    */  
		/*
		String body="<?xml version=\"1.0\" encoding=\"utf-8\"?><request><actionType>add</actionType><warehouseCode>OTHER</warehouseCode><ownerCode>c1474510013842</ownerCode><item><itemCode>H20161009002</itemCode><itemName>合并B</itemName><goodsCode /><englishName>TESTB</englishName><barCode>161009B</barCode><stockUnit /><length>0</length><width>0</width><height>0</height><volume>0</volume><grossWeight>0.000</grossWeight><netWeight>0.000</netWeight><title>合并B</title><categoryId>0</categoryId><categoryName /><safetyStock>0</safetyStock><itemType>ZC</itemType><tagPrice>0.00</tagPrice><retailPrice>515.00</retailPrice><costPrice>0.00</costPrice><purchasePrice>0</purchasePrice><shelfLife>0</shelfLife><originAddress>中国</originAddress><remark /><createTime>2016-06-20 14:45:20</createTime><updateTime>2016-06-20 14:45:20</updateTime><isValid>Y</isValid><isSku>Y</isSku></item></request>";
		QimenTestReceiveRunsUtils runUtils = new QimenTestReceiveRunsUtils();
		Date date =new Date(); 
		QmSyncLogEntity sync= new QmSyncLogEntity(1,method_singleitem_synchronize,"xml",appKey,"2.0","","md5","c1474510013842",body,date);
		String urlStr = runUtils.toUrl(apiUrl,sync);
		String sign=QimenSign.sign(urlStr, sync.getContent(), secretKey);
		sync.setSign(sign);
		urlStr=runUtils.toUrl(apiUrl,sync);
		System.out.println(urlStr);
		String result = runUtils.doPost(urlStr,sync.getContent());
		System.out.println(result);
		*/
		 
		/*
		String body="<?xml version=\"1.0\" encoding=\"utf-8\"?><request><deliveryOrder><deliveryOrderCode>JIUYETEST201610180001</deliveryOrderCode><orderType>JYCK</orderType><warehouseCode>005</warehouseCode><orderFlag/><sourcePlatformCode>TM</sourcePlatformCode><createTime>2016-10-11 10:25:56</createTime><placeOrderTime>2016-10-11 10:08:57</placeOrderTime><payTime>2016-10-11 10:09:18</payTime><operateTime>2016-10-11 10:25:56</operateTime><shopNick>水世界食品专营店</shopNick><buyerNick>zjdylqy</buyerNick><totalAmount>159</totalAmount><freight>0</freight><logisticsCode>SF</logisticsCode><invoiceFlag>N</invoiceFlag><buyerMessage/><arAmount>0</arAmount><serviceFee>0</serviceFee><sellerMessage/><remark/><expressCode/><senderInfo><name>黄忠敏</name><mobile>15801716466</mobile><province>上海</province><city>上海市</city><area>松江区</area><detailAddress>闵行区澄江路885号，洪源冷库</detailAddress></senderInfo><receiverInfo><name>李群英</name><mobile>13376899881</mobile><tel/><province>浙江省</province><city>金华市</city><area>东阳市</area><zipCode>322100</zipCode><detailAddress>浙江省 金华市 东阳市 江北街道学士路中天世纪花城南区26幢2号</detailAddress></receiverInfo><deliveryRequirements><deliveryType>PTPS</deliveryType></deliveryRequirements><extendProps><key1>0</key1><tax/><pay_id/><pay_account/><buyer_name/><id_card_type/><id_card/><buyer_phone/><receiver_id_card/><pay_ent_name/><pay_ent_no/><hz_purchaser_id/><paid/><key2>1</key2><trade_flag>0</trade_flag><erpTradeNo>ZZDJY201610110072</erpTradeNo><trade_type>1</trade_type><send_type/></extendProps></deliveryOrder><orderLines><orderLine><orderLineNo>1</orderLineNo><ownerCode>JIUYETEST</ownerCode><itemCode>jytest01</itemCode><itemId/><itemName>ZZD测试</itemName><sourceOrderCode>2514573236692622</sourceOrderCode><subSourceOrderCode>2514573236692622</subSourceOrderCode><payNo/><planQty>2</planQty><actualPrice>159</actualPrice><discountAmount>39</discountAmount></orderLine></orderLines></request>";
		QimenTestReceiveRunsUtils runUtils = new QimenTestReceiveRunsUtils();
		Date date =new Date(); 
		QmSyncLogEntity sync= new QmSyncLogEntity(1,method_deliveryorder_create,"xml",appKey,"2.0","","md5","JIUYETEST",body,date);
		String urlStr = runUtils.toUrl(apiUrl,sync);
		String sign=QimenSign.sign(urlStr, sync.getContent(), secretKey);
		sync.setSign(sign);
		urlStr=runUtils.toUrl(apiUrl,sync);
		System.out.println(urlStr);
		String result = runUtils.doPost(urlStr,sync.getContent());
		System.out.println(result);
		*/
		String bod="TrustPhone:17612219133	,TrustTel:1761221   9133	";
		System.out.println(bod.replaceAll("[\\t\\n\\r]", ""));
		System.out.println(bod.replaceAll("[\\t]", ""));
		System.out.println(bod.replaceAll("[\\n]", ""));
		System.out.println(bod.replaceAll("[\\r]", ""));
	}
	

	
}
